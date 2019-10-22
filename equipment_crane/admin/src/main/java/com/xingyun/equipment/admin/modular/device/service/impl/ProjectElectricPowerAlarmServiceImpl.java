package com.xingyun.equipment.admin.modular.device.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.DateUtils;
import com.xingyun.equipment.admin.core.util.SMSUtils;
import com.xingyun.equipment.admin.modular.baseinfo.dao.ProjectUserMapper;
import com.xingyun.equipment.admin.modular.baseinfo.model.ProjectUser;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPowerAlarm;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPower;
import com.xingyun.equipment.admin.modular.device.dao.ProjectElectricPowerAlarmMapper;
import com.xingyun.equipment.admin.modular.device.dao.ProjectMessageElectricMapper;
import com.xingyun.equipment.admin.modular.device.dto.CountAlarmByDeviceNo;
import com.xingyun.equipment.admin.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xingyun.equipment.admin.modular.device.service.ProjectElectricPowerAlarmService;
import com.xingyun.equipment.admin.modular.device.service.ProjectElectricPowerService;
import com.xingyun.equipment.admin.modular.infromation.dao.ProjectMessageModelMapper;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectMessageModel;
import com.xingyun.equipment.admin.modular.system.dao.UserMapper;
import com.xingyun.equipment.admin.modular.system.model.User;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
@Service
public class ProjectElectricPowerAlarmServiceImpl extends ServiceImpl<ProjectElectricPowerAlarmMapper, ProjectElectricPowerAlarm> implements ProjectElectricPowerAlarmService {

	@Autowired
	private ProjectElectricPowerService projectElectricPowerService;
	@Autowired
	private ProjectMessageElectricMapper projectMessageElectricMapper;
	@Autowired
    protected ProjectUserMapper projectUserMapper;
    @Autowired
    private ProjectMessageModelMapper projectMessageModelMapper;
    @Autowired
    private UserMapper userMapper;
    
	@Override
	public ResultDTO<DataDTO<List<ProjectElectricPowerAlarm>>> selectPageList(
			RequestDTO<ProjectElectricPowerAlarm> res) {
		Page<ProjectElectricPowerAlarm> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectElectricPowerAlarm>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);


        List<ProjectElectricPowerAlarm> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
	}

	@Override
	public ResultDTO<List<CountAlarmByDeviceNo>> countAlarmByDeviceNo(
			RequestDTO<CountAlarmByDeviceNo> res) {
		String initialDate = DateUtils.getDate();
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
        List<ProjectElectricPowerAlarm> alarmByDateTimeList = baseMapper.getAlarmByDateTime(countAlarmByDeviceNo);
//        CountAlarmByDeviceNo count = getCount(alarmByDateTimeList);
        CountAlarmByDeviceNo count = null;
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
	public ResultDTO<DataDTO<List<ProjectElectricPowerAlarm>>> countAlarmByDeviceNoDetail(
			RequestDTO<CountAlarmByDeviceNo> res) {
		String initialDate = DateUtils.getDate();
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

        Page<ProjectElectricPowerAlarm> page = new Page<>(res.getPageNum(), res.getPageSize());
        List<ProjectElectricPowerAlarm> alarmByDateTimeList = baseMapper.getPageAlarmByDateTime(page, countAlarmByDeviceNo);
        return new ResultDTO<>(true, DataDTO.factory(alarmByDateTimeList, page.getTotal()));
    }

	@Override
	public ResultDTO<List<ProjectUser>> getWorkListByMonitorId(Integer monitorId) {
        if (monitorId == null) {
            return new ResultDTO<>(true, null);
        }
        ProjectElectricPower projectElectricPower = projectElectricPowerService.selectById(monitorId);
        List<ProjectUser> list = projectUserMapper.selectUserByProjectId(projectElectricPower.getProjectId());
        return new ResultDTO<>(true, list);
    }

	@Override
	public ResultDTO handleSMSSubmit(
			RequestDTO<ProjectEnvironmentMessageDTO> requestDTO) {
        try {
            ProjectMessageModel projectMessageModel = projectMessageModelMapper.selectById(requestDTO.getBody().getModel());

            ProjectEnvironmentMessageDTO projectEnvironmentMessageDTO = requestDTO.getBody();
            projectEnvironmentMessageDTO.setTitle(projectMessageModel.getTitle());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            projectEnvironmentMessageDTO.setSendTime(sf.format(new Date()));
            projectEnvironmentMessageDTO.setRelatedUser(StringUtils.join(projectEnvironmentMessageDTO.getRelatedUserArray(), ","));
            projectMessageElectricMapper.insert(projectEnvironmentMessageDTO);

            EntityWrapper<ProjectUser> ew = new EntityWrapper<>();
            ew.eq("is_del", 0);
            ew.in("id", projectEnvironmentMessageDTO.getRelatedUserArray());

            //获取人员信息列表
            List<ProjectUser> projectUser = projectUserMapper.selectList(ew);
            StringBuilder sb = new StringBuilder();
            for (ProjectUser user : projectUser) {
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
	public ResultDTO<List<ProjectMessageModel>> getSMSModel(
			RequestDTO<ProjectMessageModel> res) {
        EntityWrapper<ProjectMessageModel> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
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
	
	

}
