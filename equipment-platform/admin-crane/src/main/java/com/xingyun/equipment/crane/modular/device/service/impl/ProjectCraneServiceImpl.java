package com.xingyun.equipment.crane.modular.device.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.crane.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.crane.core.util.BigDecimalUtil;
import com.xingyun.equipment.crane.core.util.CreateExcel;
import com.xingyun.equipment.crane.core.util.ProducerUtil;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneFileMapper;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneStatisticsDailyMapper;
import com.xingyun.equipment.crane.modular.device.dto.ProjectCraneStaticsticsByDateDTO;
import com.xingyun.equipment.crane.modular.device.model.*;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneAlarmService;
import com.xingyun.equipment.crane.modular.device.vo.*;
import com.xingyun.equipment.crane.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneMapper;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneVideoMapper;
import com.xingyun.equipment.crane.modular.device.dto.ProjectCraneDTO;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;

import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Service
@Slf4j
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements ProjectCraneService {

    @Autowired
    private ProjectCraneVideoMapper projectCraneVideoMapper;
    @Autowired
    private ProjectCraneFileMapper projectCraneFileMapper;
    @Autowired
    ZbusProducerHolder  zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProducerUtil producerUtil;
    @Autowired
    private CreateExcel createExcel;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    ProjectCraneStatisticsDailyMapper statisticsDailyMapper;
    @Autowired
    private IProjectCraneAlarmService iProjectCraneAlarmService;

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
        List<ProjectCraneVO> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByDeviceNoAndProjectId(RequestDTO<ProjectCrane> request) {
        return new ResultDTO(true, baseMapper.checkByDeviceNoAndProjectId(request));
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByCraneNoAndProjectId(RequestDTO<ProjectCrane> request) {
        return new ResultDTO(true, baseMapper.checkByCraneNoAndProjectId(request));
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByGprsAndProjectId(RequestDTO<ProjectCrane> request) {
        return new ResultDTO(true, baseMapper.checkByGprsAndProjectId(request));
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
        projectCrane.setAlarmStatus(0);
        projectCrane.setTimeSum(0L);
        //插入主表对象
        baseMapper.insert(projectCrane);
        //然后做一个占用处理
        baseMapper.plusCallTimes(projectCrane.getSpecification(),projectCrane.getManufactor());

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
        EntityWrapper<ProjectCraneFile> fileEw = new EntityWrapper<ProjectCraneFile>();
        fileEw.eq("info_id", request.getId());
        List<ProjectCraneFile> list2 = projectCraneFileMapper.selectList(fileEw);
        ProjectCraneDTO projectCraneDTO = new ProjectCraneDTO();
        projectCraneDTO.setCrane(projectCraneVO);
        projectCraneDTO.setVideos(list);
        projectCraneDTO.setAnnex(list2);
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
        String key = Const.DEVICE_INFO_PREFIX+"crane:" + request.getBody().getCrane().getDeviceNo();
        if(redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        ProjectCrane projectCrane = request.getBody().getCrane();
        List<ProjectCraneVideo> list = request.getBody().getVideos();
        List<ProjectCraneFile> list2 = request.getBody().getAnnex();
        //根据编辑传过来得对象id 查询出现在数据库里得数据对象
        ProjectCrane projectCrane1Temp=baseMapper.selectById(projectCrane.getId());
        //编辑时做删除占用  和重新占用处理
        //把原来得减一
        baseMapper.minusCallTimes(projectCrane1Temp.getSpecification(),projectCrane1Temp.getManufactor());
        //把现在得加一
        baseMapper.plusCallTimes(projectCrane.getSpecification(),projectCrane.getManufactor());
        //编辑操作
        baseMapper.updateById(projectCrane);


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
//        //针对文件
        EntityWrapper<ProjectCraneFile> fileEw = new EntityWrapper<ProjectCraneFile>();
        ew.eq("crane_id", projectCrane.getId());
        projectCraneFileMapper.delete(fileEw);

        //然后重新插入
        for (int i = 0; i < list2.size(); i++) {
            ProjectCraneFile projectCraneFile = list2.get(i);
            projectCraneFile.setInfoId(projectCrane.getId());
            projectCraneFileMapper.insert(projectCraneFile);
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
            String key = Const.DEVICE_INFO_PREFIX +"crane:"+ deviceNum;
            String redisKey = Const.DEVICE_PLATFORM +"crane:"+ deviceNum;
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
                if(num>0){
                    //先判断是否有相同的设备编号已启用的 如果有不能启用
                    message="相同的黑匣子编号不能同时启用";
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
            String key = Const.DEVICE_INFO_PREFIX+"crane:" + baseMapper.selectById(idList.get(i)).getDeviceNo();
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

        //1.查询设备
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("device_no",request.getDeviceNo());
        ProjectCrane crane =  this.selectOne(wrapper);

        String key = com.xingyun.equipment.Const.DEVICE_CURRENT+crane.getProjectId()+":crane:"+crane.getDeviceNo();
        if(redisUtil.exists(key)){
            log.info("存在 key:{}",key);
            ProjectCraneDetailVO craneDetailVO  = JSONUtil.toBean((String)redisUtil.get(key),ProjectCraneDetailVO.class );
            craneDetailVO.setCreateTime(craneDetailVO.getDeviceTime());
            return new ResultDTO(true,craneDetailVO );
        }
        log.info("不存在 key:{}",key);
        return new ResultDTO<>(true,null);
    }

    @Override
    public ResultDTO<ProjectCraneDetailVO> selectTop100CraneDetails(String uuid, String devicNo) {
        return new ResultDTO(true,baseMapper.selectTop100CraneDetails(uuid,devicNo));
    }

    /**
     * 分时段统计列表
     * @param requestDTO
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectCraneVO>>> getAnalysisList(RequestDTO<ProjectCraneVO> requestDTO) {
        Integer projectId = requestDTO.getBody().getProjectId();
        Page<ProjectCrane> page = new Page<ProjectCrane>(requestDTO.getPageNum(), requestDTO.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("a.is_del", 0).in("a.org_id",Const.orgIds.get());
        if (null != projectId) {
            ew.eq("a.project_id",projectId);
        }
        ew.groupBy("a.project_id");
        List<ProjectCraneVO> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     * 导出台账
     * @param response
     * @param projectId
     */
    @Override
    public void getDeviceAccountExcel(HttpServletResponse response, Integer projectId) {


        Map<String, Object> map = new HashMap<>(10);
        if (projectId == null) {
            map.put("orgIds", Const.orgIds.get());
        } else {
            map.put("projectId", projectId);
        }
        List<ProjectCraneStaticsticsDailyVO> craneStaticDailyList = statisticsDailyMapper.selectPageListNoPage(map);

        // 添加当天的数据
        //今天的
        String keyPre = "device_platform:crane:statistics:";
        for (ProjectCraneStaticsticsDailyVO daily : craneStaticDailyList) {
            String key = keyPre + daily.getDeviceNo();
            if (redisUtil.exists(key)) {
                ProjectCraneStaticsticsDailyVO currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStaticsticsDailyVO.class);
                daily.setLiftFrequency(currentDay.getLiftFrequency() + daily.getLiftFrequency());
                daily.setWeightAlarm(currentDay.getWeightAlarm() + daily.getWeightAlarm());
            }
        }
        List<Object> row = null;
        List<List<Object>> rows = new ArrayList();
        ProjectCraneStaticsticsDailyVO vo = null;
        row = new ArrayList<>();
        row.add("工程名称");
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("在线状态");
        row.add("总运行时长");
        row.add("总吊重次数");
        row.add("总违章次数");
        rows.add(row);
        for (int i = 0; i < craneStaticDailyList.size(); i++) {
            row = new ArrayList();
            vo = craneStaticDailyList.get(i);
            if (null != vo.getProjectName() && !"".equals(vo.getProjectName())) {
                row.add(vo.getProjectName());
            } else {
                row.add("");
            }
            if (null != vo.getCraneNo() && !"".equals(vo.getCraneNo())) {
                row.add(vo.getCraneNo());
            } else {
                row.add("");
            }
            if (null != vo.getDeviceNo() && !"".equals(vo.getDeviceNo())) {
                row.add(vo.getDeviceNo());
            } else {
                row.add("");
            }
            if (null != vo.getIsOnline() && !"".equals(vo.getIsOnline())) {
                row.add(1==vo.getIsOnline()?"在线":"离线");
            } else {
                row.add("");
            }
            if (null != vo.getTimeSum() && !"".equals(vo.getTimeSum())) {
                row.add(formatDateTime(Long.parseLong(vo.getTimeSum().toString())));
            } else {
                row.add("");
            }
            row.add(vo.getLiftFrequency());
            row.add(vo.getWeightAlarm());
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code = URLEncoder.encode("设备台账");
        createExcel.export(response, rows, filename, code + ".xls");
    }

    @Override
    public void getAnalysisListExcel(HttpServletResponse response, Integer projectId) {
        EntityWrapper<RequestDTO<ProjectCraneVO>> ew=new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        if (projectId == null) {
            ew.in("a.org_id", Const.orgIds.get());
        } else {
            ew.eq("a.project_id",projectId);
        }
        ew.groupBy("a.project_id");
        List<ProjectCraneVO> list = baseMapper.getMapList(ew);
        List<Object> row = null;
        List<List<Object>> rows = new ArrayList();
        ProjectCraneVO vo = null;
        row = new ArrayList<>();
        row.add("工程名称");
        row.add("施工单位");
        rows.add(row);
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
            vo = list.get(i);
            if (null != vo.getProjectName() && !"".equals(vo.getProjectName())) {
                row.add(vo.getProjectName());
            } else {
                row.add("");
            }
            if (null != vo.getBuilderName() && !"".equals(vo.getBuilderName())) {
                row.add(vo.getBuilderName());
            } else {
                row.add("");
            }
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code = URLEncoder.encode("设备工作统计");
        createExcel.export(response, rows, filename, code + ".xls");
    }

    /**
     * 设备台账
     * @param requestDTO
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<GetInfoVO>>> getDeviceAccount(RequestDTO<GetInfoVO> requestDTO) {
        Integer projectId = requestDTO.getBody().getProjectId();
        Map<String, Object> map = new HashMap<>(10);
        if (projectId == null) {
            map.put("orgIds", Const.orgIds.get());
        } else {
            map.put("projectId", projectId);
        }
        String keyWord = requestDTO.getBody().getKeyWord();
        if (keyWord != null && !keyWord.equals("")) {
            map.put("keyWord", keyWord);
        }//wrapperInfo.like
        Page<ProjectCraneStaticsticsDailyVO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<ProjectCraneStaticsticsDailyVO> craneStaticDailyList = statisticsDailyMapper.selectPageList(page, map);

        craneStaticDailyList.forEach(daily -> {

            daily.setStartTime(requestDTO.getStartTime());

        });


        // 添加当天的数据
        //今天的
        String keyPre = "device_platform:crane:statistics:";
        for (ProjectCraneStaticsticsDailyVO daily : craneStaticDailyList) {
            String key = keyPre + daily.getDeviceNo();
            if (redisUtil.exists(key)) {
                ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                daily.setLiftFrequency(currentDay.getLiftFrequency() + daily.getLiftFrequency());
                daily.setWeightAlarm(currentDay.getWeightAlarm() + daily.getWeightAlarm());
            }
        }
        return new ResultDTO(true, DataDTO.factory(craneStaticDailyList, page.getTotal()));
    }

    /**
     * 违章信息已处理未处理
     * @param requestDTO
     * @return
     */
    @Override
    public ResultDTO<AlarmInfoDTO> selectAlarmInfoCount(RequestDTO<AlarmInfoVO> requestDTO) {
        String deviceNo = requestDTO.getBody().getDeviceNo();
        Integer type = requestDTO.getBody().getType();
        String startTime = requestDTO.getStartTime();
        String endTime = requestDTO.getEndTime();

        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        List<Integer> idList = new ArrayList<>();
        if (type != null) {
            if (type == 1) {
                idList.add(2);
                idList.add(4);
                idList.add(6);
                idList.add(8);
                idList.add(10);
                idList.add(12);
                idList.add(14);
                alarmWrapper.in("alarm_id", idList);
            } else if (type == 2) {
                idList.add(3);
                idList.add(5);
                idList.add(7);
                idList.add(9);
                idList.add(11);
                idList.add(13);
                idList.add(15);
                idList.add(16);
                alarmWrapper.in("alarm_id", idList);
            } else {
                idList.add(1);
                alarmWrapper.in("alarm_id", idList);
            }

        } else {
            return new ResultDTO<>(false, null, "查询类型不可为空");
        }
        alarmWrapper.eq("is_del", 0);
        alarmWrapper.eq("device_no", deviceNo);
        alarmWrapper.between("create_time", startTime, endTime);
        List<ProjectCraneAlarm> alarmList = iProjectCraneAlarmService.selectList(alarmWrapper);
        AlarmInfoDTO dto = new AlarmInfoDTO();
        dto.setTotal(alarmList.size());
        dto.setHandled((int) alarmList.stream().filter(item -> item.getIsHandle() == 1).count());
        dto.setUnHandled((int) alarmList.stream().filter(item -> item.getIsHandle() != 1).count());
        return new ResultDTO(true, dto);
    }

    public static String formatDateTime(long mss) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = mss / dd;
        long hour = (mss - day * dd) / hh;
        long minute = (mss - day * dd - hour * hh) / mi;
        long second = (mss - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = mss - day * dd - hour * hh - minute * mi - second * ss;
        return day + "天 " + hour + "时"+minute + "分";
    }


    /**
     * 日期排序
     * @param requestDTO
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectCraneStaticsticsByDateDTO>>> getAnalysisListByDate(@RequestBody RequestDTO<ProjectCraneStaticsticsByDateDTO> requestDTO) {
        Integer projectId = requestDTO.getBody().getProjectId();
        List<String> dayList = requestDTO.getBody().getDayList();

        //该项目下的所有设备列表
        EntityWrapper<RequestDTO<ProjectCraneVO>> ew=new EntityWrapper<>();
        ew.eq("a.is_del",0);
        ew.eq("a.project_id", projectId);
        List<ProjectCraneVO> deviceAllList = baseMapper.getMapList(ew);
        List<ProjectCraneStaticsticsByDateDTO> dateDTOList=new ArrayList<ProjectCraneStaticsticsByDateDTO>();
        for (String str : dayList) {
            ProjectCraneStaticsticsByDateDTO dateDTO = new ProjectCraneStaticsticsByDateDTO();
            dateDTO.setWorkDate(str);
            dateDTOList.add(dateDTO);
        }
        for(ProjectCraneStaticsticsByDateDTO dateDTO:dateDTOList){
            String str = dateDTO.getWorkDate();
            EntityWrapper<RequestDTO> ewDevice = new EntityWrapper<RequestDTO>();

            List<ProjectCraneStatisticsByDateVO> deviceList = Lists.newArrayList();
//        设备列表
            if(requestDTO.getBody().getType()==1){
                ewDevice.eq("crane.is_del", 0).eq(null != projectId, "crane.project_id", projectId);
                //全天统计
                ewDevice.eq("DATE_FORMAT(daily.work_date,'%Y-%m-%d')", str);
                ewDevice.groupBy("crane.device_no");
//                查出来的有值的设备
                deviceList = baseMapper.getAnalysisListByDateDevice(ewDevice);
            }
            if(requestDTO.getBody().getType()==2){
                long chuan = Long.valueOf(str.replaceAll("[-\\s:]",""));
                long start = Long.valueOf(requestDTO.getStartTime().substring(0, 10).replaceAll("[-\\s:]",""));
                if(chuan>=start){
                    //分时统计
                    ewDevice.between("d.create_time", (str+" "+requestDTO.getBody().getTimeIntervalMin()), (str+" "+requestDTO.getBody().getTimeIntervalMax()));
                    ewDevice.groupBy("d.crane_id");
                    deviceList = baseMapper.getAnalysisByDuration(ewDevice);
                    if(deviceList.size()>0){
                        ProjectCrane crane = baseMapper.selectById(deviceList.get(0).getCraneId());
                        deviceList.forEach(bean ->{
                            bean.setCraneNo(crane.getCraneNo());
                        });
                    }
                }
            }
            //放最后完整的list
            Set<String> dbSet = getDbSet(deviceList);
//                deviceAllList所有的设备列表
            for(ProjectCraneVO projectCraneVO:deviceAllList){

                if(!dbSet.contains(projectCraneVO.getDeviceNo())){
                    ProjectCraneStatisticsByDateVO craneStatisticsByDateVO= new  ProjectCraneStatisticsByDateVO(projectCraneVO.getCraneNo(),projectCraneVO.getDeviceNo(),0,BigDecimal.ZERO,0,0);
                    deviceList.add(craneStatisticsByDateVO);
                }
            }
            if(requestDTO.getBody().getType()==1) {
                for (ProjectCraneStatisticsByDateVO dateVO : deviceList) {
                    if (DateUtil.endOfDay(DateUtil.parse(str)).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                        // 添加当天的数据
                        //今天的
                        String keyPre = "device_platform:crane:statistics:";
                        String key = keyPre + dateVO.getDeviceNo();
                        if (redisUtil.exists(key)) {
                            ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                            dateVO.setLiftFrequency(currentDay.getLiftFrequency() + dateVO.getLiftFrequency());
                        }
                    }
                }
            }
            dateDTO.setDeviceList(deviceList);

        }
        return new ResultDTO(true, DataDTO.factory(dateDTOList));
    }

    private  Set<String> getDbSet(List<ProjectCraneStatisticsByDateVO> deviceList){
        Set<String>  set = new HashSet<>();
         for(ProjectCraneStatisticsByDateVO vo: deviceList ){
             set.add(vo.getDeviceNo());
        }
        return set;
    }


    /**
     * 综合平均数
     * @param requestDTO
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectCraneStatisticsByDateVO>>> getAnalysisListAvg(RequestDTO<ProjectCraneStatisticsByDateVO> requestDTO) {
        Integer projectId = requestDTO.getBody().getProjectId();
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        Map<String, Object> map = new HashMap<>(10);
        wrapperInfo.eq(null != projectId, "project_id", projectId);
        if (projectId == null) {
            Wrapper<ProjectCrane> craneWrapper = new EntityWrapper<>();
            craneWrapper.in("org_id", Const.orgIds.get());
            map.put("org_id", Const.orgIds.get());
        } else {
            map.put("projectId", projectId);
        }
        if (requestDTO.getStartTime() != null) {
            map.put("startTime", requestDTO.getStartTime());
        }
        if (requestDTO.getEndTime() != null) {
            map.put("endTime", requestDTO.getEndTime()+" 23:59:59");
        }
        List<ProjectCraneStatisticsByDateVO> dateList =
                baseMapper.getAnalysisListAvg(map);
        BigDecimal liftFrequencyAvg;
        for (ProjectCraneStatisticsByDateVO daily : dateList) {
            map.put("deviceNo",daily.getDeviceNo());
            ProjectCraneStatisticsByDateVO info = baseMapper.getMaxAndMin(map);
            daily.setLiftFrequencyMax(info.getLiftFrequencyMax());
            daily.setLiftFrequencyMin(info.getLiftFrequencyMin());
            liftFrequencyAvg = BigDecimalUtil.safeDivide(daily.getLiftFrequency(), requestDTO.getBody().getDays(), BigDecimal.ZERO);
            daily.setLiftFrequencyAvg(liftFrequencyAvg);
        }


        //该项目下的所有设备列表
        EntityWrapper<RequestDTO<ProjectCraneVO>> ew=new EntityWrapper<>();
        ew.eq("a.is_del",0);
        ew.eq("a.project_id", projectId);
        List<ProjectCraneVO> deviceAllList = baseMapper.getMapList(ew);
        Set<String> dbSet = getDbSet(dateList);
//                deviceAllList所有的设备列表
        for(ProjectCraneVO projectCraneVO:deviceAllList){
            if(!dbSet.contains(projectCraneVO.getDeviceNo())){
                ProjectCraneStatisticsByDateVO craneStatisticsByDateVO= new  ProjectCraneStatisticsByDateVO(projectCraneVO.getCraneNo(),projectCraneVO.getDeviceNo(),0,BigDecimal.ZERO,0,0);
                dateList.add(craneStatisticsByDateVO);
            }
        }
        if (DateUtil.endOfDay(DateUtil.parse(requestDTO.getEndTime())).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
            // 添加当天的数据
            //今天的
            String keyPre = "device_platform:crane:statistics:";
            for (ProjectCraneStatisticsByDateVO daily : dateList) {
                String key = keyPre + daily.getDeviceNo();
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    daily.setLiftFrequency(currentDay.getLiftFrequency() + daily.getLiftFrequency());
                    liftFrequencyAvg = BigDecimalUtil.safeDivide(daily.getLiftFrequency(), requestDTO.getBody().getDays(), BigDecimal.ZERO);

                    daily.setLiftFrequencyAvg(liftFrequencyAvg);
                    if(daily.getLiftFrequency()>daily.getLiftFrequencyMax()){
                        daily.setLiftFrequencyMax(daily.getLiftFrequency());
                    }
                    if(daily.getLiftFrequency()<daily.getLiftFrequencyMin()){
                        daily.setLiftFrequencyMin(daily.getLiftFrequency());
                    }
                }
            }
        }
        return new ResultDTO(true, DataDTO.factory(dateList));
    }

}
