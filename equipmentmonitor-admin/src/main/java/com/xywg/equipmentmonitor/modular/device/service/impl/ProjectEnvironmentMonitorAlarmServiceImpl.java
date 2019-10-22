package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.aop.ZbusProducerHolder;
import com.xywg.equipmentmonitor.core.dto.*;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.core.util.SMSUtils;
import com.xywg.equipmentmonitor.modular.baseinfo.dao.ProjectUserMapper;
import com.xywg.equipmentmonitor.modular.baseinfo.model.ProjectUser;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectEnvironmentMessageMapper;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectEnvironmentMonitorAlarmMapper;
import com.xywg.equipmentmonitor.modular.device.dto.CountAlarmByDeviceNo;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.service.IProjectLiftService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.device.vo.AlarmInfoVO;
import com.xywg.equipmentmonitor.modular.device.vo.DeviceUUIDVO;
import com.xywg.equipmentmonitor.modular.infromation.dao.ProjectMessageModelMapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectMessageModel;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import com.xywg.equipmentmonitor.modular.system.dao.UserMapper;
import com.xywg.equipmentmonitor.modular.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public class ProjectEnvironmentMonitorAlarmServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorAlarmMapper, ProjectEnvironmentMonitorAlarm> implements ProjectEnvironmentMonitorAlarmService {
    @Autowired
    private ProjectEnvironmentMonitorService projectEnvironmentMonitorService;
    @Autowired
    private ProjectCraneService projectCraneService;
    @Autowired
    private IProjectLiftService projectLiftService;


    @Autowired
    protected ProjectUserMapper projectUserMapper;
    @Autowired
    private ProjectMessageModelMapper projectMessageModelMapper;
    @Autowired
    private ProjectEnvironmentMessageMapper projectEnvironmentMessageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ZbusProducerHolder zbusProducerHolder         ;
    @Autowired
    private IProjectInfoService projectInfoService;

    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorAlarm> res) {
        Page<ProjectEnvironmentMonitorAlarm> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectEnvironmentMonitorAlarm>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);


        List<ProjectEnvironmentMonitorAlarm> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }


    /**
     * 按设备count实时监控各种异常次数
     *
     * @return
     */
    @Override
    public ResultDTO<List<CountAlarmByDeviceNo>> countAlarmByDeviceNo(RequestDTO<CountAlarmByDeviceNo> res) {
        String initialDate = "2018-08-01 00:00:00";
        CountAlarmByDeviceNo countAlarmByDeviceNo = res.getBody();
        String beginTime = countAlarmByDeviceNo.getBeginTime();
        String endTime = countAlarmByDeviceNo.getEndTime();
        //开始时间为空 或者开始时间在2018-08-01 00:00:00 以前 设置开始时间为2018-08-01 00:00:00
        if (StringUtils.isBlank(beginTime) || DateUtils.parseDate(beginTime).getTime() < DateUtils.parseDate(initialDate).getTime()
                ) {
            countAlarmByDeviceNo.setBeginTime(initialDate);
        }
        //结束时间为空
        if (StringUtils.isBlank(endTime) || DateUtils.parseDate(endTime).getTime() > System.currentTimeMillis()) {
            countAlarmByDeviceNo.setEndTime(DateUtils.formatDateTime(new Date()));
        } else if (DateUtils.parseDate(endTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
            countAlarmByDeviceNo.setEndTime(initialDate);
        }
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(
                DateUtils.parseDate(countAlarmByDeviceNo.getBeginTime()), DateUtils.parseDate(countAlarmByDeviceNo.getEndTime())
        );
        countAlarmByDeviceNo.setMonths(months);
        List<ProjectEnvironmentMonitorAlarm> alarmByDateTimeList = baseMapper.getAlarmByDateTime(countAlarmByDeviceNo);
        CountAlarmByDeviceNo count = getCount(alarmByDateTimeList);
        if (count == null) {
            return new ResultDTO<>(true, null);
        }
        count.setEndTime(res.getBody().getEndTime());
        count.setBeginTime(res.getBody().getBeginTime());
        List<CountAlarmByDeviceNo> resList = new ArrayList<>();
        resList.add(count);
        return new ResultDTO<>(true, resList);
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> countAlarmByDeviceNoDetail(RequestDTO<CountAlarmByDeviceNo> res) {
        String initialDate = "2018-08-01 00:00:00";
        CountAlarmByDeviceNo countAlarmByDeviceNo = res.getBody();
        String beginTime = countAlarmByDeviceNo.getBeginTime();
        String endTime = countAlarmByDeviceNo.getEndTime();
        //开始时间为空 或者开始时间在2018-08-01 00:00:00 以前 设置开始时间为2018-08-01 00:00:00
        if (StringUtils.isBlank(beginTime) || DateUtils.parseDate(beginTime).getTime() < DateUtils.parseDate(initialDate).getTime()
                ) {
            countAlarmByDeviceNo.setBeginTime(initialDate);
        }
        //结束时间为空
        if (StringUtils.isBlank(endTime) || DateUtils.parseDate(endTime).getTime() > System.currentTimeMillis()) {
            countAlarmByDeviceNo.setEndTime(DateUtils.formatDateTime(new Date()));
        } else if (DateUtils.parseDate(endTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
            countAlarmByDeviceNo.setEndTime(initialDate);
        }
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(
                DateUtils.parseDate(countAlarmByDeviceNo.getBeginTime()), DateUtils.parseDate(countAlarmByDeviceNo.getEndTime())
        );
        countAlarmByDeviceNo.setMonths(months);

        Page<ProjectEnvironmentMonitorAlarm> page = new Page<>(res.getPageNum(), res.getPageSize());
        List<ProjectEnvironmentMonitorAlarm> alarmByDateTimeList = baseMapper.getPageAlarmByDateTime(page, countAlarmByDeviceNo);
        return new ResultDTO<>(true, DataDTO.factory(alarmByDateTimeList, page.getTotal()));
    }

    @Override
    public ResultDTO<List<ProjectUser>> getWorkListByMonitorId(Integer monitorId) {
        if (monitorId == null) {
            return new ResultDTO<>(true, null);
        }
        ProjectEnvironmentMonitor projectEnvironmentMonitor = projectEnvironmentMonitorService.selectById(monitorId);
        List<ProjectUser> list = projectUserMapper.selectUserByProjectId(projectEnvironmentMonitor.getProjectId());
        return new ResultDTO<>(true, list);
    }

    /**
     * 短信推送处理
     *
     * @param requestDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO handleSMSSubmit(RequestDTO<ProjectEnvironmentMessageDTO> requestDTO) {
        try {
            ProjectMessageModel projectMessageModel = projectMessageModelMapper.selectById(requestDTO.getBody().getModel());

            ProjectEnvironmentMessageDTO projectEnvironmentMessageDTO = requestDTO.getBody();
            projectEnvironmentMessageDTO.setTitle(projectMessageModel.getTitle());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            projectEnvironmentMessageDTO.setSendTime(sf.format(new Date()));
            projectEnvironmentMessageDTO.setRelatedUser(StringUtils.join(projectEnvironmentMessageDTO.getRelatedUserArray(), ","));
            projectEnvironmentMessageMapper.insert(projectEnvironmentMessageDTO);

            /*EntityWrapper<ProjectUser> ew = new EntityWrapper<>();
            ew.eq("is_del", 0);
            ew.in("id", projectEnvironmentMessageDTO.getRelatedUserArray());*/

            //获取人员信息列表
            //List<ProjectUser> projectUser = projectUserMapper.selectList(ew);
            List<User>  userList  =userMapper.getListUserByIds(projectEnvironmentMessageDTO.getRelatedUserArray());
            StringBuilder sb = new StringBuilder();
            for (User user : userList) {
                sb.append(user.getPhone()).append(",");
            }
            //真正的发送短信开始

            SMSUtils.sendSMSMessage(sb.toString(), projectEnvironmentMessageDTO.getContent());
        } catch (Exception e) {
            return new ResultDTO<>(false, null, "发送失败");
        }
        return new ResultDTO<>(true, null, "发送成功");
    }

    @Override
    public ResultDTO<List<ProjectMessageModel>> getSMSModel(RequestDTO<ProjectMessageModel> res) {
        EntityWrapper<ProjectMessageModel> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("status",1);
        List<ProjectMessageModel> list = projectMessageModelMapper.selectList(ew);

        List<ProjectMessageModel> newList = new ArrayList<>();
        for (ProjectMessageModel model : list) {
            if (StringUtils.isNotBlank(model.getRelatedUser())) {
                String[] ids = model.getRelatedUser().split(",");
                List<User> userList = null;
                if (ids.length > 0) {
                    userList = userMapper.getListUserByIds(ids);
                }
                model.setUserList(userList);
                newList.add(model);
            } else {
                newList.add(model);
            }
        }
        return new ResultDTO<>(true, newList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppResultDTO<Object> updateAlarm(String tableName, Integer[] ids, Integer userId, String userName) {
        for(Integer id :ids){
            int rows = baseMapper.updateAlarm(tableName, id, userId, userName);
            String deviceTable = null ;
            String type = null;
            if(tableName.startsWith("t_project_crane_alarm")){
                deviceTable = "t_project_crane";
                type = "crane_delete_alarm";

            }else if(tableName.startsWith("t_project_lift_alarm_")){
                deviceTable = "t_project_lift";
                type = "lift_delete_alarm";
            }else if(tableName.startsWith("t_project_environment_monitor_alarm_")){
                deviceTable = "t_project_environment_monitor";
                type = "monitor_delete_alarm";
            }

            DeviceUUIDVO deviceUUIDVO =  baseMapper.selectDeviceUUID(deviceTable,tableName,id);
            if(deviceUUIDVO!=null&&StrUtil.isNotBlank(deviceUUIDVO.getDeviceNo())&&StrUtil.isNotBlank(deviceUUIDVO.getUuid())){
                System.out.println("发送 websokcet"+"/topic/"+type+"/"+deviceUUIDVO.getUuid());
                RemoteDTO remoteDTO = RemoteDTO.factory("/topic/"+type+"/"+deviceUUIDVO.getUuid(),JSONUtil.toJsonStr(deviceUUIDVO));

                try {
                    zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//            if (rows == 0) {
//                String name = baseMapper.selectModifyUserName(tableName, id);
//                message = ("该数据已由XXXX处理!").replace("XXXX", name);
//                return new AppResultDTO(false, message);
//            }
        }

        return new AppResultDTO(true);
    }

    @Override
    public AppDataResultDTO<List<AlarmInfoVO>> getAlarmInfo(String[] uuids, Integer pageSize, Integer pageNum) {
        //TODO 重写
        String month = DateUtil.format(new Date(), "yyyyMM");
        Integer[] enviromentMonitorIds;
        Integer[] craneIds;
        Integer[] liftIds;

        List<Integer> flag1_detailIds = new ArrayList<>();
        List<Integer> flag2_detailIds = new ArrayList<>();
        List<Integer> flag3_detailIds = new ArrayList<>();
        int environmentMonitorListSize;
        int craneListSize;
        int liftListSize;
        int detailListSize;

        Map<String,Map<String,Object>> deviceMaps = new HashMap<>(128);

        //1 查询项目
        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.in("uuid",uuids);
        List<ProjectInfo> projectInfoList = projectInfoService.selectList(wrapper);
        int projectListSize = projectInfoList.size();
        if(projectListSize==0){
            Map<String,Object > map = new HashMap<>(8);
            map.put("list",null);
            map.put("total",0);
            return new AppDataResultDTO(true,map);
        }
        //查询项目
        Integer [] projectIds = new Integer[projectInfoList.size()];
        for(int i = 0;i<projectListSize;i++){
            projectIds[i] = projectInfoList.get(i).getId();
        }
        // 查询扬尘设备
        Wrapper<ProjectEnvironmentMonitor> environmentMonitorWrapper =new EntityWrapper<>();
        environmentMonitorWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_environment_monitor.project_id) projectName","name");
        environmentMonitorWrapper.in("project_id",projectIds);
        List<Map<String,Object>> environmentMonitorList =  projectEnvironmentMonitorService.selectMaps(environmentMonitorWrapper);

        environmentMonitorListSize =  environmentMonitorList.size();
        enviromentMonitorIds = new Integer[environmentMonitorListSize];
        for(int i = 0;i<environmentMonitorListSize;i++){
            enviromentMonitorIds[i] = (Integer) environmentMonitorList.get(i).get("id");
            deviceMaps.put("1_"+enviromentMonitorIds[i],environmentMonitorList.get(i));
        }
        // 查询塔吊
        Wrapper<ProjectCrane> craneWrapper = new EntityWrapper<>();
        craneWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_crane.project_id) projectName","name");
        craneWrapper.in("project_id",projectIds);
        List<Map<String,Object>> craneList = projectCraneService.selectMaps(craneWrapper);
        craneListSize = craneList.size();
        craneIds = new Integer[craneListSize];
        for(int i = 0 ; i < craneListSize;i++){
            craneIds[i] = (Integer) craneList.get(i).get("id");
            deviceMaps.put("3_"+craneIds[i],craneList.get(i));
        }

        // 升降机
        Wrapper<ProjectLift> liftWrapper = new EntityWrapper<>();
        liftWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_lift.project_id) projectName","name");
        liftWrapper.in("project_id",projectIds);
        List<Map<String,Object>> liftList = projectLiftService.selectMaps(liftWrapper);
        liftListSize = liftList.size();
        liftIds = new Integer[liftListSize];
        for(int i = 0 ; i<liftListSize;i++){
            liftIds[i] = (Integer) liftList.get(i).get("id");
            deviceMaps.put("2_"+liftIds[i],liftList.get(i));
        }
        Page<AlarmInfoVO> page = new Page<>(pageNum, pageSize);
        List<AlarmInfoVO> list = baseMapper.getAlarmInfo(page, month, enviromentMonitorIds,liftIds,craneIds);
        for(AlarmInfoVO alarmInfoVO:list){
            Map<String,Object>  deviceMap =   deviceMaps.get(alarmInfoVO.getFlag()+"_"+alarmInfoVO.getDeviceId());
            alarmInfoVO.setName((String) deviceMap.get("name"));
            alarmInfoVO.setProjectName((String) deviceMap.get("projectName"));
        }

        detailListSize = list.size();
        AlarmInfoVO alarmInfoVO;
        for(int i = 0 ; i<detailListSize;i++){
            alarmInfoVO = list.get(i) ;
            if(1==alarmInfoVO.getFlag()){
                flag1_detailIds.add(alarmInfoVO.getDetailId());
            }else if(2==alarmInfoVO.getFlag()){
                flag2_detailIds.add(alarmInfoVO.getDetailId());
            }else if(3==alarmInfoVO.getFlag()){
                flag3_detailIds.add(alarmInfoVO.getDetailId());
            }
        }

        if(flag1_detailIds.size()>0){
            List<Map<String,Object>> monitiorDetailMapList  =  baseMapper.getMonitorDetails(flag1_detailIds,month);
            loadMonitorValue(list,monitiorDetailMapList);


        }

        if(flag2_detailIds.size()>0){
            List<Map<String,Object>> liftDetailMapList  =  baseMapper.getLiftDetails(flag2_detailIds,month);
            loadLiftValue(list,liftDetailMapList);
        }

        if(flag3_detailIds.size()>0){
            List<Map<String,Object>> craneDetailMapList  =  baseMapper.getCraneDetails(flag3_detailIds,month);
            loadCraneValue(list,craneDetailMapList);
        }


        Map<String,Object > map = new HashMap<>(32);
        map.put("list",list);
        map.put("total",page.getTotal());
        return new AppDataResultDTO(true,map);
    }

    @Override
    public AppDataResultDTO<Integer> getAlarmAccount(String[] uuids, Integer pageSize, Integer pageNum) {
        String month = DateUtil.format(new Date(), "yyyyMM");
        Integer[] enviromentMonitorIds;
        Integer[] craneIds;
        Integer[] liftIds;

        int environmentMonitorListSize;
        int craneListSize;
        int liftListSize;


        //1 查询项目
        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.in("uuid",uuids);
        List<ProjectInfo> projectInfoList = projectInfoService.selectList(wrapper);
        int projectListSize = projectInfoList.size();
        if(projectListSize==0){
            Map<String,Object > map = new HashMap<>(8);
            map.put("list",null);
            map.put("total",0);
            return new AppDataResultDTO(true,map);
        }
        //查询项目
        Integer [] projectIds = new Integer[projectInfoList.size()];
        for(int i = 0;i<projectListSize;i++){
            projectIds[i] = projectInfoList.get(i).getId();
        }
        // 查询扬尘设备
        Wrapper<ProjectEnvironmentMonitor> environmentMonitorWrapper =new EntityWrapper<>();
        environmentMonitorWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_environment_monitor.project_id) projectName","name");
        environmentMonitorWrapper.in("project_id",projectIds);
        List<Map<String,Object>> environmentMonitorList =  projectEnvironmentMonitorService.selectMaps(environmentMonitorWrapper);

        environmentMonitorListSize =  environmentMonitorList.size();
        enviromentMonitorIds = new Integer[environmentMonitorListSize];
        for(int i = 0;i<environmentMonitorListSize;i++){
            enviromentMonitorIds[i] = (Integer) environmentMonitorList.get(i).get("id");
        }
        // 查询塔吊
        Wrapper<ProjectCrane> craneWrapper = new EntityWrapper<>();
        craneWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_crane.project_id) projectName","name");
        craneWrapper.in("project_id",projectIds);
        List<Map<String,Object>> craneList = projectCraneService.selectMaps(craneWrapper);
        craneListSize = craneList.size();
        craneIds = new Integer[craneListSize];
        for(int i = 0 ; i < craneListSize;i++){
            craneIds[i] = (Integer) craneList.get(i).get("id");
        }

        // 升降机
        Wrapper<ProjectLift> liftWrapper = new EntityWrapper<>();
        liftWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_lift.project_id) projectName","name");
        liftWrapper.in("project_id",projectIds);
        List<Map<String,Object>> liftList = projectLiftService.selectMaps(liftWrapper);
        liftListSize = liftList.size();
        liftIds = new Integer[liftListSize];
        for(int i = 0 ; i<liftListSize;i++){
            liftIds[i] = (Integer) liftList.get(i).get("id");
        }

        Integer total = baseMapper.getAlarmAccount(month, enviromentMonitorIds,liftIds,craneIds);
        Map<String,Object > map = new HashMap<>(32);
        map.put("list",null);
        map.put("total",total);
        return new AppDataResultDTO(true,map);
    }

    /**
     * 加载扬尘明细中的值
     * @param list
     * @param monitiorDetailMapList
     */
    private void loadMonitorValue(List<AlarmInfoVO> list,List<Map<String,Object>> monitiorDetailMapList){
        for(AlarmInfoVO alarmInfoVO1:list){
            if(1==alarmInfoVO1.getFlag()){
                for(Map<String,Object> monitiorDetailMap:monitiorDetailMapList){
                    if(alarmInfoVO1.getDetailId().equals(monitiorDetailMap.get("id"))){
                        switch (alarmInfoVO1.getAlarmId()){
                            case 1 :
                            case 2 :
                                alarmInfoVO1.setValue(monitiorDetailMap.get("pm25")+" ug/m³");
                                break;
                            case 3:
                            case 4:
                                alarmInfoVO1.setValue(monitiorDetailMap.get("pm10")+" ug/m³");
                                break;
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                alarmInfoVO1.setValue(monitiorDetailMap.get("temperature")+" ℃");
                                break;
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                                alarmInfoVO1.setValue(monitiorDetailMap.get("humidity")+" %rh");
                                break;
                            case 13:
                            case 14:
                                alarmInfoVO1.setValue(monitiorDetailMap.get("noise")+" 分贝");
                                break;
                            case 15:
                            case 16:
                                alarmInfoVO1.setValue(monitiorDetailMap.get("wind_speed")+" m/s");
                                break;
                            default:
                                alarmInfoVO1.setValue("");
                        }
                    }

                }
            }
        }
    }

    /**
     * 加载升降机明细中的值
     * @param list
     * @param liftDetailMapList
     */
    private void loadLiftValue(List<AlarmInfoVO> list,List<Map<String,Object>>liftDetailMapList){
        for(AlarmInfoVO alarmInfoVO1:list){
            if(2==alarmInfoVO1.getFlag()){
                for(Map<String,Object> liftDetailMap:liftDetailMapList){
                    if(alarmInfoVO1.getDetailId().equals(liftDetailMap.get("id"))){
                        switch (alarmInfoVO1.getAlarmId()){
                            case 1 :
                            case 2 :
                                alarmInfoVO1.setValue(liftDetailMap.get("weight")+" T");
                                break;
                            case 3:
                                alarmInfoVO1.setValue(liftDetailMap.get("speed")+" m/s");
                                break;
                            case 4:
                                alarmInfoVO1.setValue((Integer)liftDetailMap.get("front_door_status")==1?"打开":"关闭");
                                break;
                            case 5:
                                alarmInfoVO1.setValue((Integer)liftDetailMap.get("back_door_status")==1?"打开":"关闭");
                                break;
                            default:
                                alarmInfoVO1.setValue("");
                        }
                    }

                }
            }
        }
    }
    /**
     * 加载塔吊明细中的值
     * @param list
     * @param craneDetailMapList
     */
    private void loadCraneValue(List<AlarmInfoVO> list,List<Map<String,Object>>craneDetailMapList){
        for(AlarmInfoVO alarmInfoVO1:list){
            if(3==alarmInfoVO1.getFlag()){
                for(Map<String,Object> craneDetailMap:craneDetailMapList){
                    if(alarmInfoVO1.getDetailId().equals(craneDetailMap.get("id"))){
                        switch (alarmInfoVO1.getAlarmId()){
                            case 1 :
                            case 2 :
                                alarmInfoVO1.setValue(craneDetailMap.get("weight")+" T");
                                break;
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                alarmInfoVO1.setValue(craneDetailMap.get("range")+" m");
                                break;
                            case 7:
                            case 8:
                                alarmInfoVO1.setValue(craneDetailMap.get("height")+" m");
                                break;
                            case 9:
                                alarmInfoVO1.setValue(craneDetailMap.get("moment")+" N.m");
                                break;
                            case 15:
                                alarmInfoVO1.setValue(craneDetailMap.get("wind_speed")+" m/s");
                                break;
                            case 16:
                                alarmInfoVO1.setValue(craneDetailMap.get("tilt_angle")+" °");
                                break;
                            default:
                                alarmInfoVO1.setValue("");
                        }
                    }

                }
            }
        }
    }
    @Override
    public AppDataResultDTO<List<AlarmInfoVO>> getAlarmInfoByFlag(int flag,String[] uuids, Integer pageSize, Integer pageNum) {

        String month = DateUtil.format(new Date(), "yyyyMM");
        Integer[] ids;

        List<Integer> flag1_detailIds = new ArrayList<>();
        List<Integer> flag2_detailIds = new ArrayList<>();
        List<Integer> flag3_detailIds = new ArrayList<>();
        int environmentMonitorListSize;
        int craneListSize;
        int liftListSize;
        int detailListSize;

        Map<String,Map<String,Object>> deviceMaps = new HashMap<>(128);

        //1 查询项目
        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.in("uuid",uuids);
        List<ProjectInfo> projectInfoList = projectInfoService.selectList(wrapper);
        int projectListSize = projectInfoList.size();
        if(projectListSize==0){
            Map<String,Object > map = new HashMap<>(8);
            map.put("list",null);
            map.put("total",0);
            return new AppDataResultDTO(true,map);
        }
        //查询项目
        Integer [] projectIds = new Integer[projectInfoList.size()];
        for(int i = 0;i<projectListSize;i++){
            projectIds[i] = projectInfoList.get(i).getId();
        }
        if(flag == 1){
            // 查询扬尘设备
            Wrapper<ProjectEnvironmentMonitor> environmentMonitorWrapper =new EntityWrapper<>();
            environmentMonitorWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_environment_monitor.project_id) projectName","name");
            environmentMonitorWrapper.in("project_id",projectIds);
            List<Map<String,Object>> environmentMonitorList =  projectEnvironmentMonitorService.selectMaps(environmentMonitorWrapper);

            environmentMonitorListSize =  environmentMonitorList.size();
            ids = new Integer[environmentMonitorListSize];
            for(int i = 0;i<environmentMonitorListSize;i++){
                ids[i] = (Integer) environmentMonitorList.get(i).get("id");
                deviceMaps.put("1_"+ids[i],environmentMonitorList.get(i));
            }
        }else if(flag == 2){
            // 升降机
            Wrapper<ProjectLift> liftWrapper = new EntityWrapper<>();
            liftWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_lift.project_id) projectName","name");
            liftWrapper.in("project_id",projectIds);
            List<Map<String,Object>> liftList = projectLiftService.selectMaps(liftWrapper);
            liftListSize = liftList.size();
            ids = new Integer[liftListSize];
            for(int i = 0 ; i<liftListSize;i++){
                ids[i] = (Integer) liftList.get(i).get("id");
                deviceMaps.put("2_"+ids[i],liftList.get(i));
            }
        }else if(flag == 3){
            // 查询塔吊
            Wrapper<ProjectCrane> craneWrapper = new EntityWrapper<>();
            craneWrapper.setSqlSelect("id","(select name from t_project_info where t_project_info.id = t_project_crane.project_id) projectName","name");
            craneWrapper.in("project_id",projectIds);
            List<Map<String,Object>> craneList = projectCraneService.selectMaps(craneWrapper);
            craneListSize = craneList.size();
            ids = new Integer[craneListSize];
            for(int i = 0 ; i < craneListSize;i++){
                ids[i] = (Integer) craneList.get(i).get("id");
                deviceMaps.put("3_"+ids[i],craneList.get(i));
            }
        }else{
            Map<String,Object > map = new HashMap<>(8);
            map.put("list",null);
            map.put("total",0);
            return new AppDataResultDTO(true,map);
        }





        Page<AlarmInfoVO> page = new Page<>(pageNum, pageSize);
        List<AlarmInfoVO> list = baseMapper.getAlarmInfoByFlag(page,flag, month, ids);
        for(AlarmInfoVO alarmInfoVO:list){
            Map<String,Object>  deviceMap =   deviceMaps.get(alarmInfoVO.getFlag()+"_"+alarmInfoVO.getDeviceId());
            alarmInfoVO.setName((String) deviceMap.get("name"));
            alarmInfoVO.setProjectName((String) deviceMap.get("projectName"));
        }

        detailListSize = list.size();
        AlarmInfoVO alarmInfoVO;
        for(int i = 0 ; i<detailListSize;i++){
            alarmInfoVO = list.get(i) ;
            if(1==alarmInfoVO.getFlag()){
                flag1_detailIds.add(alarmInfoVO.getDetailId());
            }else if(2==alarmInfoVO.getFlag()){
                flag2_detailIds.add(alarmInfoVO.getDetailId());
            }else if(3==alarmInfoVO.getFlag()){
                flag3_detailIds.add(alarmInfoVO.getDetailId());
            }
        }

        if(flag1_detailIds.size()>0){
            List<Map<String,Object>> monitiorDetailMapList  =  baseMapper.getMonitorDetails(flag1_detailIds,month);
            loadMonitorValue(list,monitiorDetailMapList);


        }

        if(flag2_detailIds.size()>0){
            List<Map<String,Object>> liftDetailMapList  =  baseMapper.getLiftDetails(flag2_detailIds,month);
            loadLiftValue(list,liftDetailMapList);
        }

        if(flag3_detailIds.size()>0){
            List<Map<String,Object>> craneDetailMapList  =  baseMapper.getCraneDetails(flag3_detailIds,month);
            loadCraneValue(list,craneDetailMapList);
        }


        Map<String,Object > map = new HashMap<>(32);
        map.put("list",list);
        map.put("total",page.getTotal());
        return new AppDataResultDTO(true,map);
    }

    @Override
    public List<ProjectEnvironmentMonitorAlarmVO> getAlarms2zhgd(String month, Integer projectId, String time) {
        return baseMapper.getAlarms2zhgd(month,projectId,time);
    }

    @Override
    public List<ProjectEnvironmentMonitorAlarmVO> getAlarms2yjgj(String month, Integer projectId, String time,String deviceNo,Integer status) {
        return baseMapper.getAlarms2yjgj(month,projectId,time,deviceNo,status);
    }
    @Override
    public List<ProjectEnvironmentMonitorAlarmVO> getLiftAlarms2Zhgd(String month, Integer projectId, String time,String deviceNo,Integer status) {
        return baseMapper.getLiftAlarms2zhgd(month,projectId,time,deviceNo,status);
    }
    /**
     * 统计处理
     *
     * @param list
     * @return
     */
    private CountAlarmByDeviceNo getCount(List<ProjectEnvironmentMonitorAlarm> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        CountAlarmByDeviceNo countAlarmByDeviceNo = new CountAlarmByDeviceNo(list.get(0).getDeviceNo(), 0, 0, 0, 0, 0, 0, 0, 0);
        for (ProjectEnvironmentMonitorAlarm alarm : list) {
            CountAlarmByDeviceNo resCountAlarmByDeviceNo = addTotal(alarm.getAlarmId(), countAlarmByDeviceNo);
            //总计
            Integer total = resCountAlarmByDeviceNo.getPm25() + resCountAlarmByDeviceNo.getPm10()
                    + resCountAlarmByDeviceNo.getTemperature() + resCountAlarmByDeviceNo.getHumidity()
                    + resCountAlarmByDeviceNo.getNoise() + resCountAlarmByDeviceNo.getWindSpeed()
                    + resCountAlarmByDeviceNo.getTsp();
            resCountAlarmByDeviceNo.setTotal(total);
            countAlarmByDeviceNo = resCountAlarmByDeviceNo;
        }
        return countAlarmByDeviceNo;
    }

    /**
     * 累加次数
     *
     * @param countAlarmByDeviceNo
     * @return
     */
    private CountAlarmByDeviceNo addTotal(Integer alarmId, CountAlarmByDeviceNo countAlarmByDeviceNo) {
        if (alarmId == null) {
            return countAlarmByDeviceNo;
        }
        switch (alarmId) {
            case 1:
            case 2:
                Integer pm25 = countAlarmByDeviceNo.getPm25() + 1;
                countAlarmByDeviceNo.setPm25(pm25);
                break;
            case 3:
            case 4:
                Integer pm10 = countAlarmByDeviceNo.getPm10() + 1;
                countAlarmByDeviceNo.setPm10(pm10);
                break;
            case 5:
            case 6:
                //温度(高温)
                Integer temperature = countAlarmByDeviceNo.getTemperature() + 1;
                countAlarmByDeviceNo.setTemperature(temperature);
                break;
            case 7:
            case 8:

                //温度(低温)
                Integer temperatureLow = countAlarmByDeviceNo.getTemperature() + 1;
                countAlarmByDeviceNo.setTemperature(temperatureLow);
                break;
            case 9:
            case 10:

                //湿度(超标)
                Integer humidity = countAlarmByDeviceNo.getHumidity() + 1;
                countAlarmByDeviceNo.setHumidity(humidity);
                break;
            case 11:
            case 12:
                //湿度(过低)
                Integer humidityLow = countAlarmByDeviceNo.getHumidity() + 1;
                countAlarmByDeviceNo.setHumidity(humidityLow);
                break;
            case 13:
            case 14:
                //噪音
                Integer noise = countAlarmByDeviceNo.getNoise() + 1;
                countAlarmByDeviceNo.setNoise(noise);
                break;
            case 15:
            case 16:
                //风速
                Integer windSpeed = countAlarmByDeviceNo.getWindSpeed() + 1;
                countAlarmByDeviceNo.setWindSpeed(windSpeed);
                break;
            case 17:
            case 18:
                //tsp
                Integer tsp = countAlarmByDeviceNo.getTsp() + 1;
                countAlarmByDeviceNo.setTsp(tsp);
                break;
            default:
                break;
        }
        return countAlarmByDeviceNo;
    }

}
