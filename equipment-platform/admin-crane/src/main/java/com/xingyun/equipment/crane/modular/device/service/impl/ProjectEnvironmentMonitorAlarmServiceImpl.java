package com.xingyun.equipment.crane.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.core.dto.*;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.crane.core.util.SMSUtils;
import com.xingyun.equipment.crane.modular.baseinfo.dao.ProjectUserMapper;
import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectUser;
import com.xingyun.equipment.crane.modular.device.dao.ProjectEnvironmentMessageMapper;
import com.xingyun.equipment.crane.modular.device.dao.ProjectEnvironmentMonitorAlarmMapper;
import com.xingyun.equipment.crane.modular.device.dto.CountAlarmByDeviceNo;
import com.xingyun.equipment.crane.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorService;
import com.xingyun.equipment.crane.modular.device.vo.AlarmInfoVO;
import com.xingyun.equipment.crane.modular.device.vo.DeviceUUIDVO;
import com.xingyun.equipment.crane.modular.infromation.dao.ProjectMessageModelMapper;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectMessageModel;
import com.xingyun.equipment.system.dao.UserMapper;
import com.xingyun.equipment.system.model.User;
import org.apache.commons.lang.StringUtils;
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
    protected ProjectUserMapper projectUserMapper;
    @Autowired
    private ProjectMessageModelMapper projectMessageModelMapper;
    @Autowired
    private ProjectEnvironmentMessageMapper projectEnvironmentMessageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ZbusProducerHolder zbusProducerHolder         ;

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

        Page<AlarmInfoVO> page = new Page<>(pageNum, pageSize);
        System.out.println(pageNum);
        System.out.println(pageSize);
        List<AlarmInfoVO> list = baseMapper.getAlarmInfo(page, DateUtil.format(new Date(), "yyyyMM"), uuids);
        Map<String,Object > map = new HashMap<>();
        map.put("list",list);
        map.put("total",page.getTotal());
        return new AppDataResultDTO(true,map);
    }

    @Override
    public AppDataResultDTO<List<AlarmInfoVO>> getAlarmInfoByFlag(int flag,String[] uuids, Integer pageSize, Integer pageNum) {

        Page<AlarmInfoVO> page = new Page<>(pageNum, pageSize);
        System.out.println(pageNum);
        System.out.println(pageSize);
        List<AlarmInfoVO> list = baseMapper.getAlarmInfoByFlag(page,flag, DateUtil.format(new Date(), "yyyyMM"), uuids);
        Map<String,Object > map = new HashMap<>();
        map.put("list",list);
        map.put("total",page.getTotal());
        return new AppDataResultDTO(true,map);
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
