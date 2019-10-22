package com.xywg.equipmentmonitor.modular.device.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xywg.equipmentmonitor.config.RedisConfig;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.util.ProducerUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.aop.ZbusProducerHolder;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectCraneMapper;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectCraneVideoMapper;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectCraneDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneVideo;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneDetailVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneOrgVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;

import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Service
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements ProjectCraneService {

    @Autowired
    private ProjectCraneVideoMapper projectCraneVideoMapper;
    @Autowired
    ZbusProducerHolder  zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProducerUtil producerUtil;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * @Description: 条件分页查询
     * @Author xieshuaishuai
     * @Date 2018/8/22 19:02
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectCraneVO>>> getPageList(RequestDTO request) {
        Page<ProjectCrane> page = new Page<ProjectCrane>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("a.is_del", 0).in("a.org_id",request.getOrgIds());
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.eq("a.project_id", request.getKey());
        }
        if(null!=request.getDeviceNo()&&!"".equals(request.getDeviceNo()))
        {
            ew.like("a.device_no",request.getDeviceNo());
        }

        List<ProjectCraneVO> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByDeviceNoAndProjectId(RequestDTO<ProjectCrane> request) {
        return new ResultDTO(true, baseMapper.checkByDeviceNoAndProjectId(request));
    }

    /**
     * @Description: 新增
     * @Author xieshuaishuai
     * @Date 2018/8/23 17:39
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<Object> insertInfo(RequestDTO<ProjectCraneDTO> request) throws Exception{
        ProjectCrane projectCrane = new ProjectCrane();
        ProjectCraneVO projectCraneVO = request.getBody().getCrane();
        BeanUtil.copyProperties(projectCraneVO,projectCrane);
        List<ProjectCraneVideo> videos = request.getBody().getVideos();
        projectCrane.setStatus(0);
        projectCrane.setIsOnline(0);
        //插入主表对象
        baseMapper.insert(projectCrane);
        //然后做一个占用处理
        baseMapper.plusCallTimes(projectCrane.getSpecification(),projectCrane.getManufactor());
        //zBus 发送
        String uuid=projectInfoService.selectById(projectCrane.getProjectId()).getUuid();
        try {
            zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(projectCrane),"add","crane");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //循环插入详情表
        for (int i = 0; i < videos.size(); i++) {
            ProjectCraneVideo projectCraneVideo = videos.get(i);
            projectCraneVideo.setCraneId(projectCrane.getId());
            projectCraneVideoMapper.insert(projectCraneVideo);
        }
        return new ResultDTO<>(true);
    }

    /**
     * @Description: 查询单条
     * @Author xieshuaishuai
     * @Date 2018/8/23 20:25
     */
    @Override
    public ResultDTO<ProjectCraneDTO> selectInfo(RequestDTO<ProjectCrane> request) {
        ProjectCrane projectCrane = baseMapper.selectById(request.getId());
        ProjectInfo projectInfo = projectInfoMapper.selectById(projectCrane.getProjectId());
        ProjectCraneVO projectCraneVO = new ProjectCraneVO();
        BeanUtil.copyProperties(projectCrane,projectCraneVO);
        projectCraneVO.setProjectName(projectInfo.getName());
        EntityWrapper<ProjectCraneVideo> ew = new EntityWrapper<ProjectCraneVideo>();
        ew.eq("crane_id", request.getId());
        List<ProjectCraneVideo> list = projectCraneVideoMapper.selectList(ew);
        ProjectCraneDTO projectCraneDTO = new ProjectCraneDTO();
        projectCraneDTO.setCrane(projectCraneVO);
        projectCraneDTO.setVideos(list);
        return new ResultDTO<>(true, projectCraneDTO);
    }

    /**
     * @Description: 更新操作
     * @Author xieshuaishuai
     * @Date 2018/8/23 20:26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<Object> updateInfo(RequestDTO<ProjectCraneDTO> request) throws Exception{
        String key = RedisConfig.DEVICE_INFO_PREFIX+"crane:" + request.getBody().getCrane().getDeviceNo();
        if(redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        ProjectCrane projectCrane = request.getBody().getCrane();
        List<ProjectCraneVideo> list = request.getBody().getVideos();
        //根据编辑传过来得对象id 查询出现在数据库里得数据对象
        ProjectCrane projectCrane1Temp=baseMapper.selectById(projectCrane.getId());
        //编辑时做删除占用  和重新占用处理
        //把原来得减一
        baseMapper.minusCallTimes(projectCrane1Temp.getSpecification(),projectCrane1Temp.getManufactor());
        //把现在得加一
        baseMapper.plusCallTimes(projectCrane.getSpecification(),projectCrane.getManufactor());
        //编辑操作
        baseMapper.updateById(projectCrane);
        //zBus 发送
        String uuid=projectInfoService.selectById(projectCrane.getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid,JSONUtil.toJsonStr(request.getBody()),"edit","crane");
            }catch (Exception ex){
                ex.printStackTrace();
            }


        //先把数据删掉
        EntityWrapper<ProjectCraneVideo> ew = new EntityWrapper<ProjectCraneVideo>();
        ew.eq("crane_id", projectCrane.getId());
        projectCraneVideoMapper.delete(ew);
        //然后重新插入
        for (int i = 0; i < list.size(); i++) {
            ProjectCraneVideo projectCraneVideo = list.get(i);
            projectCraneVideo.setCraneId(projectCrane.getId());
            projectCraneVideoMapper.insert(projectCraneVideo);
        }
        return new ResultDTO<>(true);
    }

    /**
     * @Description:启用操作
     * @Author xieshuaishuai
     * @Date 2018/8/23 21:13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<Object> updateStatus(RequestDTO<ProjectCrane> request) {

        String message="";
        Boolean flag=true;
        for (int i = 0; i < request.getIds().size(); i++) {
            Integer id = Integer.valueOf(request.getIds().get(i).toString());
            String deviceNum = baseMapper.selectById(id).getDeviceNo();
            String key = RedisConfig.DEVICE_INFO_PREFIX +"crane:"+ deviceNum;
            String redisKey = RedisConfig.DEVICE_PLATFORM +"crane:"+ deviceNum;
            if(redisUtil.exists(key)) {
                redisUtil.remove(key);
            }

            ProjectCrane projectCrane = new ProjectCrane();
            if("stop".equals(request.getKey())){//如果是停用
                if(redisUtil.exists(redisKey)) {
                    String value = (String) redisUtil.get(redisKey);
                    String topic = value.split("#")[0];
                    String data = "{\"sn\":\"" + deviceNum + "\",\"type\":\"crane\",\"cmd\":\"01\"}";
                    producerUtil.sendCtrlMessage(topic,data);
                }
                projectCrane.setStatus(0);
                projectCrane.setIsOnline(0);
                message="停用成功";

            }else {//如果是启用
                ProjectCrane p=baseMapper.selectById(id);
                String deviceNo=p.getDeviceNo();
                int num=baseMapper.checkByDeviceNo(id,deviceNo).size();
                if(num>0){//先判断是否有相同的设备编号已启用的 如果有不能启用
                    message="相同的设备编号不能同时启用";
                    flag=false;
                    //直接返回
                    break;
                }else{
                projectCrane.setStatus(1);
                message="启用成功";
                }
            }
            projectCrane.setId(id);
            baseMapper.updateById(projectCrane);
        }
        return new ResultDTO<>(flag, null, message);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        for(int i=0;i<idList.size();i++){
            //遍历对象 然后通过zBus同步数据到智慧工地
            Object id=idList.get(i);
            String key = RedisConfig.DEVICE_INFO_PREFIX+"crane:" + baseMapper.selectById(idList.get(i)).getDeviceNo();
            if(redisUtil.exists(key)) {
                redisUtil.remove(key);
            }
            RequestDTO<ProjectCrane>  res=  new RequestDTO<>();
            res.setId(Integer.parseInt(id.toString()));
            ResultDTO<ProjectCraneDTO> resultDTO=selectInfo(res);
            String  uuid =projectInfoService.selectById(resultDTO.getData().getCrane().getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid,JSONUtil.toJsonStr(resultDTO.getData()),"delete","crane");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //删除占用处理
            ProjectCrane projectCrane=baseMapper.selectById(Integer.parseInt(id.toString()));
            baseMapper.minusCallTimes(projectCrane.getSpecification(),projectCrane.getManufactor());
        }
        return retBool(this.baseMapper.deleteBatchIds(idList));
    }

    /**
     *@Description:不分页查询 地图用
     *@Author xieshuaishuai
     *@Date 2018/9/10 14:54
     */
    @Override
    public ResultDTO<List<ProjectCraneVO>> selectListMap(RequestDTO<ProjectCraneVO> request){
        EntityWrapper<RequestDTO<ProjectCraneVO>> ew=new EntityWrapper<>();
        ew.eq("a.is_del",0).in("a.org_id",request.getOrgIds());
        if(request.getId()!=null && !"".equals(request.getId())) {
            ew.eq("a.project_id", request.getId());
        }
        List<ProjectCraneVO> list=baseMapper.getMapList(ew);
        return new ResultDTO<>(true,list);
    }


    /**
     * 查询集团下所有塔吊(接口)
     * @param request
     * @return
     */
	@Override
	public ResultDTO<ProjectCraneOrgVO> selectCraneList(RequestDTO request) {

            request.setOrgIds(Const.orgIds.get());
            request.setOrgId(null);
	        List<ProjectCraneVO> list = baseMapper.selectCraneList(request);
	        ProjectCraneOrgVO res = new ProjectCraneOrgVO();
	        res.setDeviceList(list);
	        return new ResultDTO<>(true,res);
	}
	   /**
     * 查询塔吊最近一条数据(接口)
     * @param request
     * @return
     */
	@Override
	public ResultDTO<ProjectCraneDetailVO> selectCraneDetail(RequestDTO request) {
		Calendar ca=java.util.Calendar.getInstance();
		SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
	String tableName = "t_project_crane_detail"+"_"+cal.format(ca.getTime());
	Map<String,Object> map = new HashMap<String,Object>();
	map.put("deviceNo", request.getDeviceNo());
	map.put("tableName", tableName);
	//map.put("orgId", request.getOrgId());
	System.out.println("--------------"+map);
	      ProjectCraneDetailVO res = baseMapper.selectCraneDetail(map);
	        return new ResultDTO<>(true,res);
	}

    @Override
    public ResultDTO<ProjectCraneDetailVO> selectTop100CraneDetails(String uuid, String devicNo) {
        return new ResultDTO(true,baseMapper.selectTop100CraneDetails(uuid,devicNo));
    }


    @Override
    public ResultDTO<Object> updateDispatch(RequestDTO<ProjectCrane> res) {
	    //更新转发字段
        List<Integer> ids=  res.getIds().stream().map(x->Integer.valueOf(((Serializable) x).toString())).collect(Collectors.toList());
        baseMapper.updateDispatch(ids);
        //转发设备入缓存
        for(int i=0;i<res.getIds().size();i++)
        {
            Integer id = Integer.valueOf(res.getIds().get(i).toString());
            ProjectCrane lift=baseMapper.selectById(id);
            String deviceNo=lift.getDeviceNo();
            redisUtil.set("xywg:td:dispatch:"+deviceNo,1);
        }
        return new ResultDTO<>(true,"操作成功!");
	}
}
