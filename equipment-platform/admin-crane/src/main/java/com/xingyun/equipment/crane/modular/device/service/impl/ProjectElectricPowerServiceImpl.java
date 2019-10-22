package com.xingyun.equipment.crane.modular.device.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.xingyun.equipment.crane.core.util.ProducerUtil;
import com.xingyun.equipment.crane.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.modular.device.dao.ProjectElectricPowerMapper;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPower;
import com.xingyun.equipment.crane.modular.device.service.ProjectElectricPowerService;
import com.xingyun.equipment.crane.modular.device.vo.DataVO;
import com.xingyun.equipment.crane.modular.device.vo.ElectricAlarmVO;
import com.xingyun.equipment.crane.modular.device.vo.ProjectElectricPowerVO;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;
import com.xingyun.equipment.system.service.IOrganizationService;

import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 电表设备管理
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
@Service
public class ProjectElectricPowerServiceImpl extends ServiceImpl<ProjectElectricPowerMapper, ProjectElectricPower> implements ProjectElectricPowerService {

    @Autowired
    ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private ProducerUtil producerUtil;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 分页查询列表
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectElectricPower>>> selectPageList(
            RequestDTO<ProjectElectricPower> res) {

        Page<ProjectElectricPower> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectElectricPower>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0).in("a.org_id", res.getOrgIds());
        if (res.getId() != null && !"".equals(res.getId())) {
//        	ew.like("b.name", res.getKey());
            ew.eq("a.project_id", res.getId());
        }

        if (res.getBody() != null) {
            if (res.getBody().getProjectId() != null) {
                ew.eq("a.project_id", res.getBody().getProjectId());
            }
            if (res.getBody().getIsOnline() != null) {
                ew.eq("a.is_online", res.getBody().getIsOnline());
            }
        }
        List<Integer> orgIds = Const.orgIds.get();
        ew.in("a.org_id", orgIds);
        List<ProjectElectricPower> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     * 地图用接口
     */
    @Override
    public ResultDTO<List<ProjectElectricPower>> selectListMap(
            RequestDTO<ProjectElectricPower> res) {
        EntityWrapper<RequestDTO<ProjectElectricPower>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0).in("a.org_id", res.getOrgIds());
        if (res.getId() != null && !"".equals(res.getId())) {
//	        	ew.like("b.name", res.getKey());
            ew.eq("a.project_id", res.getId());
        }
        List<ProjectElectricPower> list = baseMapper.selectListMap(ew);
        return new ResultDTO<>(true, list);
    }

    /**
     * 设备编号重复性验证
     */
    @Override
    public ResultDTO<Object> countDevice(String deviceNo, Integer projectId) {
        Long count = baseMapper.countDevice(deviceNo, projectId);

        return new ResultDTO<>(true, count);

    }

    /**
     * 查询单条详情
     */
    @Override
    public ResultDTO<ProjectElectricPower> selectInfo(
            RequestDTO<ProjectElectricPower> res) {

        ProjectElectricPower power = baseMapper.selectById(res.getId());
        ProjectInfo projectInfo = projectInfoMapper.selectById(power.getProjectId());
        ProjectElectricPowerVO projectElectricPowerVO = new ProjectElectricPowerVO();
        BeanUtil.copyProperties(power, projectElectricPowerVO);
        projectElectricPowerVO.setProjectName(projectInfo.getName());
        return new ResultDTO<ProjectElectricPower>(true, projectElectricPowerVO);
    }

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> insertInfo(RequestDTO<ProjectElectricPower> res) throws Exception {
        ProjectElectricPower power = res.getBody();
        baseMapper.plusCallTimes(power.getSpecification(), power.getManufactor());
        power.setStatus(0);
        power.setIsOnline(0);
        baseMapper.insert(power);
        String uuid = projectInfoService.selectById(res.getBody().getProjectId()).getUuid();
        try {
            zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(power), "add", "elec");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(true, null, "新增成功");
    }

    /**
     * 启用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> updateStatus(RequestDTO<ProjectElectricPower> res) throws Exception {

        String msg = "";
        Boolean flag = true;
        for (int i = 0; i < res.getIds().size(); i++) {
            Integer id = Integer.valueOf(res.getIds().get(i).toString());
            String deviceNum = baseMapper.selectById(id).getDeviceNo();
            String key = Const.DEVICE_INFO_PREFIX + "ameter:" + deviceNum;
            String redisKey = Const.DEVICE_PLATFORM + "meter:" + deviceNum;
            if (redisUtil.exists(key)) {
                redisUtil.remove(key);
            }
            ProjectElectricPower power = new ProjectElectricPower();
            if (res.getStatus() == 0) {//停用
                msg = "停用成功";
                if (redisUtil.exists(redisKey)) {
                    String value = (String) redisUtil.get(redisKey);
                    String topic = value.split("#")[0];
                    String data = "{\"sn\":\"" + deviceNum + "\",\"type\":\"meter\",\"cmd\":\"01\"}";
                    producerUtil.sendCtrlMessage(topic, data);
                }
                power.setStatus(res.getStatus());
                power.setIsOnline(0);
            } else {//启用
                String deviceNo = baseMapper.selectById(id).getDeviceNo();
                int num = baseMapper.checkByDeviceNo(deviceNo, id).size();
                if (num > 0) {//说明已有相同的设备编号已启用
                    msg = "相同的设备编号的设备不能同时启用";
                    flag = false;
                    break;
                } else {
                    power.setStatus(res.getStatus());
                    msg = "启用成功";
                }
            }
            power.setId(id);
            baseMapper.updateById(power);
        }
        return new ResultDTO<>(flag, null, msg);

    }

    /**
     * 编辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> updateInfo(RequestDTO<ProjectElectricPower> res) throws Exception {
        ProjectElectricPower power = res.getBody();

        String key = Const.DEVICE_INFO_PREFIX + "ameter:" + baseMapper.selectById(power.getId()).getDeviceNo();
        if (redisUtil.exists(key)) {
            redisUtil.remove(key);
        }

        ProjectElectricPower powerTemp = baseMapper.selectById(power.getId());
        baseMapper.minusCallTimes(powerTemp.getSpecification(), powerTemp.getManufactor());
        baseMapper.plusCallTimes(power.getSpecification(), power.getManufactor());
        baseMapper.updateById(power);
        String uuid = projectInfoService.selectById(res.getBody().getProjectId()).getUuid();
        try {
            zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(res.getBody()), "edit", "elec");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(true, null, "编辑成功");
    }

    /**
     * 重写删除，去除占用
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        for (int i = 0; i < idList.size(); i++) {
            Object id = idList.get(i);

            String key = Const.DEVICE_INFO_PREFIX + "ameter:" + baseMapper.selectById(idList.get(i)).getDeviceNo();
            if (redisUtil.exists(key)) {
                redisUtil.remove(key);
            }

            ProjectElectricPower monitor = baseMapper.selectById(Integer.parseInt(id.toString()));
            RequestDTO<ProjectElectricPower> requestDTO = new RequestDTO<>();
            requestDTO.setId(Integer.parseInt(id.toString()));
            selectInfo(requestDTO);
            String uuid = projectInfoService.selectById(selectInfo(requestDTO).getData().getProjectId()).getUuid();
            try {
                zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(selectInfo(requestDTO).getData()), "delete", "elec");
            } catch (Exception e) {
                e.printStackTrace();
            }
            baseMapper.minusCallTimes(monitor.getSpecification(), monitor.getManufactor());
        }


        return retBool(baseMapper.deleteBatchIds(idList));
    }

    /**
     * 获取部门下所有设备
     */
    @Override
    public List<ProjectElectricPower> selectByOrgId(Integer orgId) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("orgId", orgId);
        return baseMapper.selectByOrgId(map);
    }

    /**
     * 主页电表接口
     */
    @Override
    public ResultDTO<Map<String, Object>> getElectricInfo(Integer orgId) throws Exception {
        ResultDTO<Map<String, Object>> result = new ResultDTO<>();
        //电表总数
        int all = 0;
        //正常
        int normal = 0;
        //停用
        int discontinuation = 0;
        //异常
        int abnormal = 0;
        //同比
        String tb = "0";
        //环比
        String hb = "0";
        //数量
        BigDecimal amount = new BigDecimal(0);
        //生活用电
        BigDecimal shyd = new BigDecimal(0);
        //生产用电
        BigDecimal scyd = new BigDecimal(0);
        //本月总量
        BigDecimal b = new BigDecimal(0);
        //去年的本月总量
        BigDecimal tb1 = new BigDecimal(0);
        //上月总量
        BigDecimal hb1 = new BigDecimal(0);
        //设备id的集合
        List<Integer> ids = new ArrayList<>();
        //曲线图数据
        List<DataVO> trendList = new ArrayList<>();
        List<DataVO> trendListSh = new ArrayList<>();
        List<DataVO> trendListSc = new ArrayList<>();
        //饼图数据
        List<DataVO> pieList = new ArrayList<>();
        //生活用电
        DataVO pie1 = new DataVO();
        //生产用电
        DataVO pie2 = new DataVO();
        //获取前7天的日期
        List<String> dateList = DateUtils.get7DaysBefore(new Date());
        Page<ProjectElectricPower> page = new Page<>();

        EntityWrapper<RequestDTO<ProjectElectricPower>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        List<Integer> orgIds = organizationService.getOrgsByParent(orgId);
        ew.in("a.org_id", orgIds);
        List<ProjectElectricPower> list = baseMapper.selectPageList(page, ew);

        for (ProjectElectricPower p : list) {
            all++;
            if (p.getStatus() == 0) {
                discontinuation++;
            }
            if (p.getStatus() == 1) {
                normal++;
            }
            ids.add(p.getId());
            //算月用量
            amount = baseMapper.getAmountByDevice("'" + dateList.get(0).toString() + "'", "'" + dateList.get(6).toString() + "'", p.getId());
            if (p.getType() == null) {
                continue;
            }
            if (p.getType() == 1) {
                shyd = shyd.add(amount);
            }
            if (p.getType() == 2) {
                scyd = scyd.add(amount);
            }

        }
        pie1.setName("生活用电");
        pie1.setAmount(shyd);
        pie2.setName("生产用电");
        pie2.setAmount(scyd);
        pieList.add(pie1);
        pieList.add(pie2);
        //有设备才去计算
        if (ids.size() > 0) {
            String month = DateUtils.getYear() + DateUtils.getMonth();
            abnormal = baseMapper.selectAbnormal(month, ids);
            for (int i = 0; i < dateList.size(); i++) {
                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", ids);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendList.add(trend);
            }
            List<Integer> idsSh = new ArrayList<>();
            for (ProjectElectricPower p : list) {
                if (p.getType()!=null && p.getType() == 1) {
                    idsSh.add(p.getId());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {
                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", idsSh);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendListSh.add(trend);
            }
            List<Integer> idsSc = new ArrayList<>();
            for (ProjectElectricPower p : list) {
                if (p.getType()!=null && p.getType() == 2) {
                    idsSc.add(p.getId());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {
                amount = baseMapper.getAmountByDays("'" + dateList.get(i).toString() + "'", "'" + dateList.get(i).toString() + "'", idsSc);
                DataVO trend = new DataVO();
                trend.setName(String.valueOf(DateUtils.parseDate(dateList.get(i)).getTime()));
                trend.setAmount(amount);
                trendListSc.add(trend);
            }


            String beforMonth = (DateUtils.getYear() + "-" + (Integer.valueOf(DateUtils.getMonth()) - 1)) + "%";
            ;
            String nowMonth = DateUtils.getYear() + "-" + DateUtils.getMonth() + "%";
            String beforYear = (Integer.valueOf(DateUtils.getYear()) - 1) + "-" + DateUtils.getMonth() + "%";


            b = baseMapper.getAmountByMonth(nowMonth, ids);
            tb1 = baseMapper.getAmountByMonth(beforYear, ids);
            hb1 = baseMapper.getAmountByMonth(beforMonth, ids);
            DecimalFormat df = new DecimalFormat("#.00");
            if (tb1.compareTo(BigDecimal.ZERO) != 0) {
                tb = df.format(b.subtract(tb1).divide(tb1, 10, RoundingMode.UP).multiply(new BigDecimal(100)).doubleValue());
            }
            if (hb1.compareTo(BigDecimal.ZERO) != 0) {
                hb = df.format(b.subtract(hb1).divide(hb1, 10, RoundingMode.UP).multiply(new BigDecimal(100)).doubleValue());
            }
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("all", all);
        map1.put("normal", normal);
        map1.put("discontinuation", discontinuation);
        map1.put("abnormal", abnormal);

        map2.put("tb", Double.parseDouble(tb));
        map2.put("hb", Double.parseDouble(hb));

        map.put("deviceSum", map1);
        map.put("lineData", trendList);
        map.put("lineDataSh", trendListSh);
        map.put("lineDataSc", trendListSc);
        map.put("pieData", pieList);
        map.put("useData", map2);
        result.setSuccess(true);
        result.setData(map);
        result.setCode(200);
        return result;


    }

    /**
     * 智慧工地用
     */
    @Override
    public byte[] getElectricDetailInfo(RequestDTO<ProjectElectricPower> res) {

        Page<ProjectElectricPower> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName", "t_project_electric_power_detail_" + res.getYearMonth());
        Integer year = Integer.valueOf(res.getYearMonth().substring(0, 4));
        Integer month = Integer.valueOf(res.getYearMonth().substring(4, 6));
        String month1 = "";
        if (month == 1) {
            year = year - 1;
            month = 12;
        } else {
            month = month - 1;
        }
        if (month < 10) {
            month1 = "0" + month;
        } else {
            month1 = month.toString();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        String lastMonthDay = dateFormat.format(calendar.getTime());
        String fristDay = dateFormat.format(calendar1.getTime());
        System.out.println("23322");
        System.out.println(lastMonthDay);
        map.put("lastMonthDay", lastMonthDay);
        map.put("fristDay", fristDay);
        map.put("tableName1", "t_project_electric_power_detail_" + (year.toString() + month1));
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuid", res.getUuid());

        System.out.println(res.getDeviceNo());
        List<ProjectElectricPowerVO> list = baseMapper.getElecDetailInfo(page, map);
        System.out.println("时间" + list);
        Map<String, Object> resultMap = new HashMap<>(10);
        if (list != null && !list.isEmpty() && list.get(0) != null) {
            resultMap.put("electricList", list);
            resultMap.put("total", page.getTotal());
        } else {
            resultMap.put("electricList", new ArrayList<>());
            resultMap.put("total", 0);
        }

        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);

    }

    /**
     * 智慧工地用
     */
    @Override
    public byte[] getAlarmInfo(RequestDTO<ElectricAlarmVO> res) {
        Page<ElectricAlarmVO> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName", "t_project_electric_power_alarm_" + res.getYearMonth());

        String[] uuids = res.getUuids().toString().split(",");
        map.put("alarmId", res.getAlarmId());
        map.put("deviceNo", res.getDeviceNo());

        map.put("uuids", uuids);
        List<ElectricAlarmVO> alarmList = baseMapper.getAlarmInfo(page, map);
        Map<String, Object> resultMap = new HashMap<>(10);
        resultMap.put("total", page.getTotal());

        resultMap.put("alarmList", alarmList);
        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(resultStr);
        return StringCompress.compress(resultStr);
    }

    /**
     * 智慧工地拉取报警明细信息
     */
    @Override
    public byte[] getAlarmDetail(RequestDTO<ElectricAlarmVO> res) {
        Page<ElectricAlarmVO> page = new Page<>(res.getPageNum(), res.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName", "t_project_electric_power_alarm_" + res.getYearMonth());
        map.put("alarmId", res.getAlarmId());
        map.put("deviceNo", res.getDeviceNo());
        List<ElectricAlarmVO> waters = baseMapper.getAlarmDetail(page, map);
        Map<String, Object> resultMap = new HashMap<>(10);
        resultMap.put("total", page.getTotal());
        resultMap.put("infoList", waters);
        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>(true, resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

}
