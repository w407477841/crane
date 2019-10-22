package com.xingyun.equipment.admin.modular.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.config.RedisConfig;
import com.xingyun.equipment.admin.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.ProducerUtil;
import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.modular.device.dao.ProjectLiftMapper;
import com.xingyun.equipment.admin.modular.device.dao.ProjectLiftVideoMapper;
import com.xingyun.equipment.admin.modular.device.dto.ProjectLiftDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftVideo;
import com.xingyun.equipment.admin.modular.device.service.IProjectLiftService;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftVO;
import com.xingyun.equipment.admin.modular.infromation.service.impl.ProjectTargetSetLiftServiceImpl;
import com.xingyun.equipment.admin.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-08-21
 */
@Service
public class ProjectLiftServiceImpl extends ServiceImpl<ProjectLiftMapper, ProjectLift> implements IProjectLiftService {
    @Autowired
    private ProjectLiftVideoMapper videoMapper;

    @Autowired
    ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProjectTargetSetLiftServiceImpl targetSetLiftService;
    @Autowired
    private ProducerUtil producerUtil;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public ResultDTO<DataDTO<List<ProjectLiftVO>>> selectPageList(RequestDTO<ProjectLiftVO> res) {
        Page<ProjectLiftVO> page=new Page<ProjectLiftVO>(res.getPageNum(), res.getPageSize());
        EntityWrapper<ProjectLiftVO> wrapper=new EntityWrapper<>();
        wrapper.eq("a.is_del",0);
        wrapper.orderBy("a.create_time",false);
        wrapper.in("a.org_id",res.getOrgIds());
        if (res.getId()!=null && !"".equals(res.getId()) ) {
            wrapper.eq("a.project_id", res.getId());
        }
        List<ProjectLiftVO> list=baseMapper.selectPageList(page, wrapper);
        ResultDTO<DataDTO<List<ProjectLiftVO>>>  result=new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
        return result;
    }

    @Override
    public ResultDTO<ProjectLiftDTO> selectInfo(RequestDTO<ProjectLiftDTO> res) {
        ProjectLift projectLift =this.baseMapper.selectById(res.getId());
        ProjectInfo projectInfo = projectInfoMapper.selectById(projectLift.getProjectId());
        ProjectLiftVO projectLiftVO = new ProjectLiftVO();
        BeanUtil.copyProperties(projectLift,projectLiftVO);
        projectLiftVO.setProjectName(projectInfo.getName());
        EntityWrapper<ProjectLiftVideo> ew = new EntityWrapper<ProjectLiftVideo>();
        ew.eq("lift_id", res.getId());
        List<ProjectLiftVideo> videos =videoMapper.selectList(ew);
        ProjectLiftDTO projectLiftDTO = new ProjectLiftDTO();
        projectLiftDTO.setLift(projectLiftVO);
        projectLiftDTO.setVideos(videos);
        return new ResultDTO<ProjectLiftDTO>(true,projectLiftDTO);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertProjectLift(RequestDTO<ProjectLiftDTO> res) throws Exception {

        boolean isSuccess = false;
        ProjectLift projectLift=new ProjectLift();
        ProjectLiftVO projectLiftVO = res.getBody().getLift();
        BeanUtil.copyProperties(projectLiftVO,projectLift);
        List<ProjectLiftVideo> videos =res.getBody().getVideos();

        Map<String,Object> map = new HashMap<>(10);
        map.put("specification",projectLift.getSpecification());
        map.put("manufactor",projectLift.getManufactor());
        targetSetLiftService.plusCallTimes(map);
        res.getBody().setSpeed(projectLift.getSpeed());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("is_online", 0);
        ew.eq("is_del", 0);
        ew.eq("project_id", projectLift.getProjectId());
        ew.eq("device_no", projectLift.getDeviceNo());
        ProjectLift t = baseMapper.selectByName(ew);
        if(t!=null){
            throw new RuntimeException("设备名称重复");
        }else{
            projectLift.setStatus(0);
            projectLift.setIsOnline(0);
            isSuccess = retBool(baseMapper.insert(projectLift));
            Integer liftId= projectLift.getId();
            for(int i=0;i<videos.size();i++) {
                ProjectLiftVideo video = videos.get(i);
                video.setLiftId(liftId);
                video.setIsDel(0);
                videoMapper.insert(video);
            }

            //发送数据
            String uuid=projectInfoService.selectById(projectLift.getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(projectLift),"add","lift");
            } catch (Exception e) {
                e.printStackTrace();
            }


            return isSuccess;

        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateProjectLift(RequestDTO<ProjectLiftDTO> res) throws Exception{
        boolean isSuccess = false;
        String key = RedisConfig.DEVICE_INFO_PREFIX+"lift:" + res.getBody().getDeviceNo();
        if(redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        ProjectLift projectLift=res.getBody().getLift();
        List<ProjectLiftVideo> videos =res.getBody().getVideos();
        res.getBody().setSpeed(projectLift.getSpeed());
        ProjectLift p = baseMapper.selectById(projectLift.getId());
        List<ProjectLift> projectLifts = new ArrayList<>(10);
        projectLifts.add(p);
        targetSetLiftService.minusCallTimes(projectLifts);
        Map<String,Object> map = new HashMap(10);
        map.put("specification",projectLift.getSpecification());
        map.put("manufactor",projectLift.getManufactor());
        targetSetLiftService.plusCallTimes(map);
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("is_del", 0);
        ew.eq("project_id", projectLift.getProjectId());
        ew.eq("device_no", projectLift.getDeviceNo());
        ew.last("and id !="+ projectLift.getId());
        ProjectLift t = baseMapper.selectByName(ew);

       

        if(t!=null){
            throw new RuntimeException("设备名称重复");
        }else{
            EntityWrapper<ProjectLift> ew3 = new EntityWrapper<ProjectLift>();
            ew3.eq("id", projectLift.getId());
            isSuccess = retBool(baseMapper.update(projectLift, ew3));


            //发送数据
            String uuid=projectInfoService.selectById(projectLift.getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(res.getBody()),"edit","lift");
            } catch (Exception e) {
                e.printStackTrace();
            }


            EntityWrapper<ProjectLiftVideo> ew4 = new EntityWrapper<ProjectLiftVideo>();
            ew4.eq("lift_id", projectLift.getId());
            videoMapper.delete(ew4);

            Integer liftId= projectLift.getId();
            for(int i=0;i<videos.size();i++) {
                ProjectLiftVideo video = videos.get(i);
                video.setLiftId(liftId);
                video.setIsDel(0);
                videoMapper.insert(video);
            }

            return isSuccess;


        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> updateStatus(RequestDTO<ProjectLiftDTO> res) {
        String message="";
        Boolean flag=true;
        for(int i=0;i<res.getIds().size();i++) {
            Integer id = Integer.valueOf(res.getIds().get(i).toString());
            String deviceNum = baseMapper.selectById(id).getDeviceNo();
            String key = RedisConfig.DEVICE_INFO_PREFIX+"lift:" + deviceNum;
            if(redisUtil.exists(key)) {
                redisUtil.remove(key);
            }
            ProjectLift projectLift = new ProjectLift();
            if("stop".equals(res.getKey())){//是停用
                String redisKey = RedisConfig.DEVICE_PLATFORM+"lift:" + deviceNum;
                if(redisUtil.exists(redisKey)) {
                    String value = (String)redisUtil.get(redisKey);
                    String topic = value.split("#")[0];
                    String data = "{\"sn\":\"" + deviceNum + "\",\"type\":\"lift\",\"cmd\":\"01\"}";
                    producerUtil.sendCtrlMessage(topic,data);
                }
                projectLift.setStatus(0);
                projectLift.setIsOnline(0);
                message="停用成功";
            }else{//是启用
                ProjectLift p=baseMapper.selectById(id);
                String deviceNo=p.getDeviceNo();
                List<ProjectLift> list=baseMapper.checkByDeviceNo(id,deviceNo);
                if(list.size()>0){
                    message="相同的设备编号不能同时启用";
                    flag=false;
                    break;
                }else{
                    projectLift.setStatus(1);
                    message="启用成功";
                }
            }
            projectLift.setId(id);
            baseMapper.updateById(projectLift);
        }
        return new ResultDTO<>(flag,null,message);
    }

    @Override
    public ResultDTO<List<ProjectLiftVO>> selectListMap(RequestDTO<ProjectLiftVO> res) {
        {
            EntityWrapper<RequestDTO<ProjectLiftVO>> ew = new EntityWrapper<>();
            ew.eq("a.is_del", 0);
            ew.in("a.org_id",res.getOrgIds());
            if(res.getId()!=null && !"".equals(res.getId())) {
                ew.eq("a.project_id", res.getId());
            }
            List<ProjectLiftVO> list = baseMapper.selectListMap(ew);
            return new ResultDTO<>(true,list);
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        for(int i=0;i<idList.size();i++){
            Object id=idList.get(i);
            String key = RedisConfig.DEVICE_INFO_PREFIX+"lift:" + baseMapper.selectById(idList.get(i)).getDeviceNo();
            if(redisUtil.exists(key)) {
                redisUtil.remove(key);
            }
            Wrapper<ProjectLift> wrapper = new EntityWrapper<>();
            wrapper.in("id",idList);
            List<ProjectLift> list = baseMapper.selectList(wrapper);
            targetSetLiftService.minusCallTimes(list);
            RequestDTO<ProjectLiftDTO>  res=  new RequestDTO<>();
            res.setId(Integer.parseInt(id.toString()));
            ResultDTO<ProjectLiftDTO>  resultDTO=selectInfo(res);


            String  uuid =projectInfoService.selectById(resultDTO.getData().getLift().getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid,JSONUtil.toJsonStr(resultDTO.getData()),"delete","lift");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return retBool(this.baseMapper.deleteBatchIds(idList));
    }
}
