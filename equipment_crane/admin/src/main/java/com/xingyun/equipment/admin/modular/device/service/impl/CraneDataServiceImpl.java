package com.xingyun.equipment.admin.modular.device.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.common.constant.Const;
import com.xingyun.equipment.admin.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.modular.device.dao.ProjectCraneMapper;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneAlarm;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneCyclicWorkDuration;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneStatisticsDaily;
import com.xingyun.equipment.admin.modular.device.service.*;
import com.xingyun.equipment.admin.modular.device.vo.*;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Service
public class CraneDataServiceImpl implements CraneDataService {
    @Autowired
    private ProjectCraneService projectCraneService;
    @Autowired
    private IProjectCraneStatisticsDailyService projectCraneStaticsDailyService;
    @Autowired
    private IProjectCraneCyclicWorkDurationService iProjectCraneCyclicWorkDurationService;
    @Autowired
    private IProjectCraneAlarmService iProjectCraneAlarmService;
    @Value("${filePath}")
    private String filePath;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private ProjectCraneMapper projectCraneMapper;


    /**
     * 设备流量统计
     *
     * @param requestDTO 请求参数
     * @return 返回的页面信息
     */
    @Override
    public ResultDTO<DataDTO<List<CraneFlowVO>>> selectDeviceFlow(@RequestBody RequestDTO<RequestCraneFlowVO> requestDTO) {
        //项目编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //按照塔机编号或黑匣子编号查询
        String keyWord = requestDTO.getBody().getKeyWord();
        //如果项目编号为空，返回警告
        if (projectId == null) {
            return new ResultDTO<>(false, null, "工程名称不可为空");
        }
        //SIM卡号
        String gprs = requestDTO.getBody().getGprs();
        Wrapper<ProjectCrane> wrapperInfo = new EntityWrapper<>();
        //查询对应条件的塔机
        wrapperInfo.setSqlSelect(
                "project_id as projectId",
                "(select name from t_project_info where t_project_info.id=t_project_crane.project_id limit 1) as owner",
                "crane_no as craneNo",
                "device_no as deviceNo",
                "gprs as gprs");
        wrapperInfo.eq("is_online", 1);
        wrapperInfo.eq("is_del", 0);
        wrapperInfo.eq("project_id", projectId);
        wrapperInfo.like(null != gprs && !"".equals(gprs), "gprs", gprs);
        if (StrUtil.isNotBlank(keyWord)) {
            wrapperInfo.andNew().like("crane_no", keyWord).or().like("device_no", keyWord);
        }
        List<ProjectCrane> craneList;
        Page<ProjectCrane> page = null;
        //如果当前页码数据不为空，则查询当前页数据
        if (requestDTO.getPageNum() != null) {
            page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            projectCraneService.selectPage(page, wrapperInfo);
            craneList = page.getRecords();
        } else {//否则为导出查询，查询所有的
            craneList = projectCraneService.selectList(wrapperInfo);
        }
        List<CraneFlowVO> flowvoList = new ArrayList<>();

        //返回信息
        if (null != craneList && craneList.size() > 0) {
            flowvoList=getFlowvoList(craneList);
        }
        if (page != null) {
            return new ResultDTO<>(true, DataDTO.factory(flowvoList, page.getTotal()));
        } else {
            return new ResultDTO<>(true, DataDTO.factory(flowvoList, 0));
        }
    }

    /**
     * 根据塔机信息返回流量统计数据
     * @param craneList 塔机信息
     * @return 流量统计数据
     */
    private List<CraneFlowVO> getFlowvoList(List<ProjectCrane> craneList)
    {
        List<CraneFlowVO> flowvoList = new ArrayList<>();
        ProjectCrane projectCrane;
        CraneFlowVO craneFlowVO;
        for (ProjectCrane aCraneList : craneList) {
            projectCrane = aCraneList;
            craneFlowVO = new CraneFlowVO();
            //工程编号
            craneFlowVO.setProjectId(projectCrane.getProjectId());
            //工程名称
            craneFlowVO.setProjectName(projectCrane.getOwner());
            //塔机编号
            craneFlowVO.setCraneNo(projectCrane.getCraneNo());
            //SIM卡号
            craneFlowVO.setGprs(projectCrane.getGprs());
            //黑匣子编号
            craneFlowVO.setDeviceNo(projectCrane.getDeviceNo());
            //联通接口，用于获取流量使用信息及SIM卡状态
            String url = "https://api.10646.cn/rws/api/v1/devices/" + projectCrane.getGprs() + "/ctdUsages";
            HttpRequest request = HttpUtil.createGet(url);
            request.header("Authorization", "Basic eWlueWFucWluMzIyMjo1Mzg2NzJhYS01MzExLTQzY2EtYTVkYS0zNzliMjViNzc0MzY=");
            request.header("Accept", "application/json");
            String jsonStr = request.execute().body();
            GetFlow getFlow = JSONObject.parseObject(jsonStr, GetFlow.class);
            //SIM已用流量
            if (getFlow != null && getFlow.getCtdDataUsage() != null) {
                int usage = Integer.parseInt(getFlow.getCtdDataUsage());
                craneFlowVO.setCumulativeFlow(new BigDecimal(usage).divide(new BigDecimal("1048576"), 2, RoundingMode.HALF_DOWN));
            } else {
                craneFlowVO.setCumulativeFlow(new BigDecimal("0"));
            }
            //SIM卡状态
            if (getFlow!=null&& StrUtil.isNotBlank(getFlow.getStatus())) {
                if ("ACTIVATED".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("激活");
                }
                if ("ACTIVATION_READY".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("待激活");
                }
                if ("DEACTIVATED".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("停机");
                }
                if ("INVENTORY".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("库存");
                }
                if ("RETIRED".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("作废");
                }
                if ("TEST_READY".equals(getFlow.getStatus())) {
                    craneFlowVO.setStatus("待用");
                }
            }
            //SIM卡流量上限
            craneFlowVO.setToplimitFlow("100");
            flowvoList.add(craneFlowVO);
        }
        return flowvoList;
    }

    /**
     * 导出设备流量统计Excel接口
     *
     * @param response  返回的响应信息
     * @param projectId 工程编号
     * @param gprs      SIM卡号
     * @return 返回的页面信息
     */
    @Override
    public ResultDTO getDeviceFlowExcel(HttpServletResponse response, Integer projectId, String gprs, String keyWord) {

        RequestCraneFlowVO requestCraneFlowVO = new RequestCraneFlowVO();
        requestCraneFlowVO.setProjectId(projectId);
        requestCraneFlowVO.setGprs(gprs);
        requestCraneFlowVO.setKeyWord(keyWord);
        RequestDTO<RequestCraneFlowVO> requestDTO = new RequestDTO<>();
        requestDTO.setBody(requestCraneFlowVO);
        ResultDTO<DataDTO<List<CraneFlowVO>>> resultDTO = selectDeviceFlow(requestDTO);
        //获取Excel文件内容
        List<CraneFlowVO> flowvoList = resultDTO.getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        CraneFlowVO vo;
        row = new ArrayList<>();
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("SIM卡号");
        row.add("工程名称");
        row.add("卡状态");
        row.add("累计用量(MB)");
        row.add("流量上限(MB)");
        rows.add(row);
        for (CraneFlowVO aFlowvoList : flowvoList) {
            row = new ArrayList<>();
            vo = aFlowvoList;
            //塔机编号
            addExcel(row, vo.getCraneNo());
            //黑匣子编号
            addExcel(row, vo.getDeviceNo());
            //SIM卡号
            addExcel(row, vo.getGprs());
            //工程名称
            addExcel(row, vo.getProjectName());
            //SIM卡状态
            addExcel(row, vo.getStatus());
            //已用流量
            addExcel(row, String.valueOf(vo.getCumulativeFlow()));
            //流量上限
            addExcel(row, vo.getToplimitFlow());
            System.out.println(vo);
            rows.add(row);
        }
        //生成Excel文件
        String filename = projectId + ".xls";
        if (!StrUtil.isEmpty(gprs) && !"".equals(gprs)) {
            filename = projectId + "-" + gprs + ".xls";
        }
        String code = URLEncoder.encode("设备流量统计");
        createExcel(response, rows, filename, code + ".xls");

        return null;
    }

    /**
     * 导出Excel公共方法
     *
     * @param response 返回的response对象
     * @param rows     Excel文件内容
     * @param filename 生成的文件名
     * @param sendName 用户下载的文件名
     */
    private void createExcel(HttpServletResponse response, List rows, String filename, String sendName) {
        ExcelWriter writer = new ExcelWriter(filePath + filename);
        //一次性写出内容
        writer.write(rows);
        //关闭writer，释放内存
        writer.close();
        //下载
        File dis = new File(filePath.substring(0, filePath.length() - 1));
        System.out.println(filePath.substring(0, filePath.length() - 1));
        if (!dis.exists()) {
            if(dis.mkdir()){
                System.out.println("创建文件夹");
            }
        }
        File file = new File(filePath + "/" + filename);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + sendName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            OutputStream os; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                System.out.println(file.getName());
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }

                FileUtil.del(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 设备离线时长统计接口
     * @param requestDTO 请求信息
     * @return 返回的页面请求数据
     */
    @Override
    public ResultDTO<DataDTO<List<DeviceOfflineVO>>> selectDeviceOffline(@RequestBody RequestDTO<DeviceOfflineVO> requestDTO) {
        //工程编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //按照塔机编号/黑匣子编号
        String keyWord = requestDTO.getBody().getKeyWord();
        //查询满足条件的塔机
        Wrapper<ProjectCrane> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect(
                "project_id as projectId",
                "(select name from t_project_info where t_project_info.id=t_project_crane.project_id limit 1) as projectName",
                "crane_no as craneNo",
                "device_no as deviceNo",
                "(select create_time from t_project_crane_heartbeat where end_time is null and t_project_crane_heartbeat.crane_id=t_project_crane.id and status=0 limit 1) as timeOff"
        );
        wrapperInfo.eq("is_online", 0);
        wrapperInfo.eq("is_del", 0);
        //如果工程编号为空，按照org_id过滤
        if (projectId != null) {
            wrapperInfo.eq("project_id", projectId);
        } else {
            wrapperInfo.in("org_id", Const.orgIds.get());
        }
        if (StrUtil.isNotBlank(keyWord)) {
            wrapperInfo.andNew().like("crane_no", keyWord).or().like("device_no", keyWord);
        }
        Page<ProjectCrane> page = null;
        List<ProjectCrane> craneList;
        //如果当前页码信息为空，则查询所有信息
        if (requestDTO.getPageNum() != null) {
            page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            projectCraneService.selectPage(page, wrapperInfo);
            craneList = page.getRecords();
        } else {
            craneList = projectCraneService.selectList(wrapperInfo);
        }
        List<DeviceOfflineVO> offlinevoList = getOfflinevoList(craneList);
        if (page != null) {
            return new ResultDTO<>(true, DataDTO.factory(offlinevoList, page.getTotal()));
        } else {
            return new ResultDTO<>(true, DataDTO.factory(offlinevoList, 0));
        }
    }

    /**
     * 根据塔吊信息获取设备离线信息
     * @param craneList 塔吊信息
     * @return 返回页面信息
     */
    private List<DeviceOfflineVO>  getOfflinevoList( List<ProjectCrane> craneList)
    {
        List<DeviceOfflineVO> offlinevoList = new ArrayList<>();
        ProjectCrane projectCrane;
        DeviceOfflineVO deviceOfflineVO;
        //返回页面信息
        for (ProjectCrane aCraneList : craneList) {
            projectCrane = aCraneList;
            deviceOfflineVO = new DeviceOfflineVO();
            //塔机编号
            deviceOfflineVO.setCraneNo(projectCrane.getCraneNo());
            //黑匣子编号
            deviceOfflineVO.setDeviceNo(projectCrane.getDeviceNo());
            //工程编号
            deviceOfflineVO.setProjectId(projectCrane.getProjectId());
            //工程名称
            deviceOfflineVO.setProjectName(projectCrane.getProjectName());
            //离线时间
            if (projectCrane.getTimeOff() != null) {
                deviceOfflineVO.setTimeOff(DateUtil.format(projectCrane.getTimeOff(), "yyyy-MM-dd HH:mm:ss"));
            }
            //累计离线时长
            if (projectCrane.getTimeOff() != null) {
                deviceOfflineVO.setDurationOff(DateUtil.between(projectCrane.getTimeOff(), new Date(), DateUnit.HOUR) + "时" + (DateUtil.between(projectCrane.getTimeOff(), new Date(), DateUnit.MINUTE) - DateUtil.between(projectCrane.getTimeOff(), new Date(), DateUnit.HOUR) * 60) + "分" + (DateUtil.between(projectCrane.getTimeOff(), new Date(), DateUnit.SECOND) - DateUtil.between(projectCrane.getTimeOff(), new Date(), DateUnit.MINUTE) * 60) + "秒");
            }
            offlinevoList.add(deviceOfflineVO);
        }
        return offlinevoList;
    }

    /**
     * 设备离线时长导出Excel接口
     * @param response  返回的响应信息
     * @param projectId 工程编号
     * @return 返回的页面请求数据
     */
    @Override
    public ResultDTO<DataDTO<List<DeviceOfflineVO>>> selectDeviceOfflineExcel(HttpServletResponse response, Integer projectId, String keyWord) {

        DeviceOfflineVO deviceOfflineVO = new DeviceOfflineVO();
        deviceOfflineVO.setProjectId(projectId);
        deviceOfflineVO.setKeyWord(keyWord);
        RequestDTO<DeviceOfflineVO> requestDTO = new RequestDTO<>();
        requestDTO.setBody(deviceOfflineVO);
        ResultDTO<DataDTO<List<DeviceOfflineVO>>> resultDTO = selectDeviceOffline(requestDTO);
        //获取Excel数据
        List<DeviceOfflineVO> offlinevoList = resultDTO.getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        DeviceOfflineVO vo;
        row = new ArrayList<>();
        row.add("工程名称");
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("离线时间");
        row.add("离线时长");
        rows.add(row);
        for (DeviceOfflineVO anOfflinevoList : offlinevoList) {
            row = new ArrayList<>();
            vo = anOfflinevoList;
            //工程名称
            addExcel(row, vo.getProjectName());
            //塔机编号
            addExcel(row, vo.getCraneNo());
            //黑匣子编号
            addExcel(row, vo.getDeviceNo());
            //设备离线时间
            addExcel(row, vo.getTimeOff());
            //设备累计离线时长
            addExcel(row, vo.getDurationOff());
            rows.add(row);
        }
        //生成Excel
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code=getCode("设备离线时长统计");
        createExcel(response, rows, filename, code + ".xls");
        return null;

    }

    /**
     * 获取org_id对应的塔机编号
     *
     * @return 返回满足org_id的crane_no集合
     */
    private List<String> getOrgIdCraneNo() {
        //查询对应org_id的塔机编号
        Wrapper<ProjectCrane> craneWrapperInfo = new EntityWrapper<>();
        craneWrapperInfo.setSqlSelect("crane_no as craneNo");
        craneWrapperInfo.in("org_id", Const.orgIds.get());
        craneWrapperInfo.eq("is_del", 0);
        List<ProjectCrane> craneList = projectCraneService.selectList(craneWrapperInfo);
        List<String> craneNos = new ArrayList<>();
        for (ProjectCrane crane : craneList) {
            if (!StrUtil.isEmpty(crane.getCraneNo()) && !"".equals(crane.getCraneNo())) {
                craneNos.add(crane.getCraneNo());
            }
        }
        return craneNos;
    }

    /**
     * 设备循环工作时长统计接口
     * @param requestDTO 请求参数
     * @return 返回的页面请求数据
     */
    @Override
    public ResultDTO<DataDTO<List<WeightPercentVO>>> selectMomentPercent(@RequestBody RequestDTO<WeightPercentVO> requestDTO) {
        //工程编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //开始时间
        String startTime = requestDTO.getStartTime();
        //结束时间
        String endTime = requestDTO.getEndTime();
        //塔机编号/黑匣子编号
        String keyWord = requestDTO.getBody().getKeyWord();
        //查询满足条件的每日数据
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect(
                "project_id as projectId",
                "project_name as projectName",
                "crane_no as craneNo",
                "device_no as deviceNo",
                "sum(lift_frequency) as liftFrequency",
                "sum(weight_alarm) as weightAlarm",
                "sum(percentage0) as percentage0",
                "sum(percentage40) as percentage40",
                "sum(percentage60) as percentage60",
                "sum(percentage80) as percentage80",
                "sum(percentage90) as percentage90",
                "sum(percentage110) as percentage110",
                "sum(percentage120) as percentage120");

        wrapperInfo.between("work_date", startTime, endTime);
        if (StrUtil.isNotBlank(keyWord)) {
            wrapperInfo.andNew().like("crane_no", keyWord).or().like("device_no", keyWord);
        }
        if (projectId != null) {
            wrapperInfo.eq("project_id", projectId);
        } else {
            //根据org_id过滤
            List<String> craneNos = getOrgIdCraneNo();
            wrapperInfo.in("crane_no", craneNos);
        }
        wrapperInfo.groupBy("device_no");
        Page<ProjectCraneStatisticsDaily> page = null;
        List<ProjectCraneStatisticsDaily> craneStaticDailyList;
        //如果页码信息为空，则查询所有
        if (requestDTO.getPageNum() != null) {
            page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            projectCraneStaticsDailyService.selectPage(page, wrapperInfo);
            craneStaticDailyList = page.getRecords();
        } else {
            craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);
        }
        List<WeightPercentVO> weightPercentvoList = new ArrayList<>();
        WeightPercentVO weightPercentVO;
        Wrapper<ProjectCraneCyclicWorkDuration> versum;
        BigDecimal sum;
        List<ProjectCraneCyclicWorkDuration> durationList;
        for (ProjectCraneStatisticsDaily statisticsDaily : craneStaticDailyList) {
            weightPercentVO = new WeightPercentVO();
            //工程编号
            weightPercentVO.setProjectId(statisticsDaily.getProjectId());
            //工程名称
            weightPercentVO.setProjectName(statisticsDaily.getProjectName());
            //黑匣子编号
            weightPercentVO.setDeviceNo(statisticsDaily.getDeviceNo());
            //吊重次数
            weightPercentVO.setLiftFrequency(statisticsDaily.getLiftFrequency());
            //重量报警次数
            weightPercentVO.setWeightAlarm(statisticsDaily.getWeightAlarm());
            //力矩百分比90次数
            weightPercentVO.setPercentage80(statisticsDaily.getPercentage0() + statisticsDaily.getPercentage40() + statisticsDaily.getPercentage60() + statisticsDaily.getPercentage80());
            //力矩百分比90~110次数
            weightPercentVO.setPercentage90(statisticsDaily.getPercentage90());
            //工程名称
            weightPercentVO.setProjectName(statisticsDaily.getProjectName());
            //塔机编号
            weightPercentVO.setCraneNo(statisticsDaily.getCraneNo());
            //力矩百分比110~120次数
            weightPercentVO.setPercentage110(statisticsDaily.getPercentage110());
            //力矩百分比120以上次数
            weightPercentVO.setPercentage120(statisticsDaily.getPercentage120());
            // 添加当天的数据
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                //今天的
                String keyPre = "device_platform:crane:statistics:";
                //加上今天的数值
                String key = keyPre + statisticsDaily.getDeviceNo();
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    if (currentDay.getLiftFrequency() == null) {
                        currentDay.setLiftFrequency(0);
                    }
                    if (statisticsDaily.getLiftFrequency() == null) {
                        statisticsDaily.setLiftFrequency(0);
                    }
                    //今天吊重次数+总吊重次数
                    weightPercentVO.setLiftFrequency(currentDay.getLiftFrequency() + weightPercentVO.getLiftFrequency());
                    if (currentDay.getWeightAlarm() == null) {
                        currentDay.setWeightAlarm(0);
                    }
                    if (statisticsDaily.getWeightAlarm() == null) {
                        statisticsDaily.setWeightAlarm(0);
                    }
                    //今日重量报警+总重量报警
                    weightPercentVO.setWeightAlarm(currentDay.getWeightAlarm() + weightPercentVO.getWeightAlarm());
                    if (currentDay.getWeightWarn() == null) {
                        currentDay.setWeightWarn(0);
                    }
                    //今日力矩百分比0~40次数
                    if (currentDay.getPercentage0() == null) {
                        currentDay.setPercentage0(0);
                    }
                    if (currentDay.getPercentage40() == null) {
                        currentDay.setPercentage40(0);
                    }
                    if (currentDay.getPercentage60() == null) {
                        currentDay.setPercentage60(0);
                    }
                    if (currentDay.getPercentage80() == null) {
                        currentDay.setPercentage80(0);
                    }
                    if (statisticsDaily.getPercentage80() == null) {
                        statisticsDaily.setPercentage80(0);
                    }
                    //今日力矩百分比80~90次数+总力矩百分比80~90次数
                    weightPercentVO.setPercentage80(currentDay.getPercentage0()+currentDay.getPercentage40()+currentDay.getPercentage60()+currentDay.getPercentage80()+ weightPercentVO.getPercentage80());
                    if (currentDay.getPercentage90() == null) {
                        currentDay.setPercentage90(0);
                    }
                    if (statisticsDaily.getPercentage90() == null) {
                        statisticsDaily.setPercentage90(0);
                    }
                    //今日力矩百分比00~110次数+总力矩百分比90~110次数
                    weightPercentVO.setPercentage90(currentDay.getPercentage90() + weightPercentVO.getPercentage90());
                    if (currentDay.getPercentage110() == null) {
                        currentDay.setPercentage110(0);
                    }
                    if (statisticsDaily.getPercentage110() == null) {
                        statisticsDaily.setPercentage110(0);
                    }
                    //今日力矩百分比110~120次数+总力矩百分比110~120次数
                    weightPercentVO.setPercentage110(currentDay.getPercentage110() + weightPercentVO.getPercentage110());
                    if (currentDay.getPercentage120() == null) {
                        currentDay.setPercentage120(0);
                    }
                    if (statisticsDaily.getPercentage120() == null) {
                        statisticsDaily.setPercentage120(0);
                    }
                    //今日力矩百分比120以上次数+总力矩百分比120以上次数
                    weightPercentVO.setPercentage120(currentDay.getPercentage120() + weightPercentVO.getPercentage120());
                }

            }

            ///////////////////////////
            versum = new EntityWrapper<>();
            versum.setSqlSelect("moment_percentage as momentPercentage");
            versum.eq("device_no", statisticsDaily.getDeviceNo());
            versum.between("create_time", startTime, endTime);
            durationList = iProjectCraneCyclicWorkDurationService.selectList(versum);
            //载荷系数百分比
            sum = new BigDecimal(0);
            BigDecimal one;
            if (durationList != null && durationList.size() > 0) {

                for (ProjectCraneCyclicWorkDuration aDurationList : durationList) {
                    one = aDurationList.getMomentPercentage().divide(new BigDecimal(100), 2);
                    sum = sum.add(one.pow(3));
                }
            }
            weightPercentVO.setLoad(sum.setScale(2, BigDecimal.ROUND_UP));
            //月使用频率
            float betweenDay = 0;
            try {
                betweenDay = DateUtil.between(DateUtil.parse(startTime, "yyyy-MM-dd"), DateUtil.parse(endTime, "yyyy-MM-dd"), DateUnit.DAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //月使用频率
            Integer liftFrequency = statisticsDaily.getLiftFrequency();
            if (betweenDay > 0) {
                weightPercentVO.setFrequencyMonth(new BigDecimal(liftFrequency).divide(new BigDecimal(betweenDay), 2, RoundingMode.UP));
            } else {
                weightPercentVO.setFrequencyMonth(new BigDecimal(liftFrequency).multiply(new BigDecimal("30")));
            }
            weightPercentvoList.add(weightPercentVO);
        }
        if (page != null) {
            return new ResultDTO<>(true, DataDTO.factory(weightPercentvoList, page.getTotal()));
        } else {
            return new ResultDTO<>(true, DataDTO.factory(weightPercentvoList, 0));
        }
    }

    /**
     * 导出设备循环工作时长Excel接口
     *
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param response  响应信息
     */
    @Override
    public void getMomentPercentExcel(Integer projectId, String startTime, String endTime, HttpServletResponse response) {

        WeightPercentVO weightPercentVO = new WeightPercentVO();
        weightPercentVO.setProjectId(projectId);
        RequestDTO<WeightPercentVO> requestDTO = new RequestDTO<>();
        requestDTO.setBody(weightPercentVO);
        requestDTO.setStartTime(startTime);
        requestDTO.setEndTime(endTime);
        //获取Excel文件信息
        List<WeightPercentVO> weightPercentvoList = selectMomentPercent(requestDTO).getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        WeightPercentVO vo;
        row = new ArrayList<>();
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("工程名称");
        row.add("吊重次数");
        row.add("违章次数");
        row.add("90%以下");
        row.add("90%~110%");
        row.add("110%~120%");
        row.add("120%以上");
        row.add("载荷系数");
        row.add("载荷");
        row.add("使用等级");
        rows.add(row);
        for (WeightPercentVO aWeightPercentvoList : weightPercentvoList) {
            row = new ArrayList<>();
            vo = aWeightPercentvoList;
            //塔机编号
            addExcel(row,vo.getCraneNo());
            //黑匣子编号
            addExcel(row,vo.getDeviceNo());
            //工程名称
            addExcel(row,vo.getProjectName());
            //吊重次数
            addExcel(row,vo.getLiftFrequency().toString());
            //违章次数
            addExcel(row,vo.getWeightAlarm().toString());
            //90%以下
            addExcel(row,vo.getPercentage80().toString());
            //90%~110%
            addExcel(row,vo.getPercentage90().toString());
            //110%~120%
            addExcel(row,vo.getPercentage110().toString());
            //120%以上
            addExcel(row,vo.getPercentage120().toString());
            //载荷系数
            addExcel(row,vo.getLoad().toString());
            //载荷

            if (null != vo.getLoad()) {
                if ((vo.getLoad().compareTo(new BigDecimal("0")) > 0 || vo.getLoad().compareTo(new BigDecimal("0")) == 0) && vo.getLoad().compareTo(new BigDecimal("0.125"))<0) {
                    addExcel(row,"很少吊运额定载荷，经常吊运较轻载荷");
                } else if ((vo.getLoad().compareTo(new BigDecimal("0.125")) >0 || vo.getLoad().compareTo(new BigDecimal("0.125")) == 0) && vo.getLoad().compareTo(new BigDecimal("0.25"))<0) {
                    addExcel(row,"较少吊运额定载荷，经常吊运中等载荷");
                } else if ((vo.getLoad().compareTo(new BigDecimal("0.25")) >0 || vo.getLoad().compareTo(new BigDecimal("0.25")) == 0) && vo.getLoad().compareTo(new BigDecimal("0.5"))<0) {
                    addExcel(row,"有时吊运额定载荷，经常吊运较重载荷");
                } else if ((vo.getLoad().compareTo(new BigDecimal("0.5")) >0 || vo.getLoad().compareTo(new BigDecimal("0.5")) == 0) && vo.getLoad().compareTo(new BigDecimal("1"))<0) {
                    addExcel(row,"经常吊运额定载荷");
                } else {
                    addExcel(row,"经常吊运额定载荷");
                }
            } else {
                addExcel(row,"很少吊运额定载荷，经常吊运较轻载荷");
            }
            //使用频率
            BigDecimal vofrequencyMonth = vo.getFrequencyMonth();
            int test1=vofrequencyMonth.compareTo(new BigDecimal("66.7"));
            int test2=vofrequencyMonth.compareTo(new BigDecimal("133.3"));
            int test3=vofrequencyMonth.compareTo(new BigDecimal("262.5"));
            int test4=vofrequencyMonth.compareTo(new BigDecimal("520.8"));
            int test5=vofrequencyMonth.compareTo(new BigDecimal("1041.6"));
            int test6=vofrequencyMonth.compareTo(new BigDecimal("2083.3"));
            if ((test1 == 0 || test1>0) && test2<0) {
                addExcel(row,"很少使用");
            } else if (test2 >= 0 && test3<0) {
                addExcel(row,"很少使用");
            } else if (test3 >= 0 && test4<0) {
                addExcel(row,"很少使用");
            } else if (test4>=0 &&  test5<0) {
                addExcel(row,"不频繁使用");
            } else if (test5>=0 && test6 <0) {
                addExcel(row,"中等频繁使用");
            } else {
                addExcel(row,"很少使用");
            }
            System.out.println(vo);
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code=getCode("设备循环工作时长");
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * 获取utf-8转译结果
     * @param param 需要转译的字符串
     * @return 转译的结果
     */
    private String getCode(String param)
{
    String code="";
    try{
        code =URLEncoder.encode(param,"utf-8");
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return code;
}
    /**
     * //力矩百分比查看接口
     * @param requestDTO 请求数据
     * @return 响应信息
     */
    @Override
    public ResultDTO<WeightPercentVO> selectMomentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO) {
        //工程编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //黑匣子编号
        String deviceNo = requestDTO.getBody().getDeviceNo();
        //开始时间
        String startTime = requestDTO.getStartTime();
        //结束时间
        String endTime = requestDTO.getEndTime();
        //从project_crane_statistics_daily表中查询数据
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect(
                "project_id as projectId",
                "device_no as deviceNo",
                "project_name as projectName",
                "crane_no as craneNo",
                "builder as builder",
                "sum(lift_frequency) as liftFrequency",
                "sum(weight_alarm) as weightAlarm",
                "sum(range_alarm) as rangeAlarm",
                "sum(limit_alarm) as limitAlarm",
                "sum(percentage0) as percentage0",
                "sum(percentage40) as percentage40",
                "sum(percentage60) as percentage60",
                "sum(percentage80) as percentage80",
                "sum(percentage90) as percentage90",
                "sum(percentage110) as percentage110",
                "sum(percentage120) as percentage120");
        wrapperInfo.between(startTime != null && endTime != null, "work_date", startTime, endTime);
        wrapperInfo.eq(deviceNo != null, "device_no", deviceNo);
        wrapperInfo.eq(projectId != null, "project_id", projectId);
        wrapperInfo.groupBy("device_no");
        List<ProjectCraneStatisticsDaily> craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);
        if (craneStaticDailyList != null && craneStaticDailyList.size() > 0) {
            ProjectCraneStatisticsDaily daily = craneStaticDailyList.get(0);
            WeightPercentVO weightPercentVO = new WeightPercentVO();
            //力矩百分比0~40
            weightPercentVO.setPercentage0(daily.getPercentage0());
            //工程名称
            weightPercentVO.setProjectName(daily.getProjectName());
            //施工单位
            weightPercentVO.setBuilder(daily.getBuilder());
            //重量报警
            weightPercentVO.setWeightAlarm(daily.getWeightAlarm());
            //幅度报警
            weightPercentVO.setRangeAlarm(daily.getRangeAlarm());
            //高度报警
            weightPercentVO.setLimitAlarm(daily.getLimitAlarm());
            //力矩百分比40~60
            weightPercentVO.setPercentage40(daily.getPercentage40());
            //力矩百分比60~80
            weightPercentVO.setPercentage60(daily.getPercentage60());
            //力矩百分比80~90
            weightPercentVO.setPercentage80(daily.getPercentage80());
            //力矩百分比90~110
            weightPercentVO.setPercentage90(daily.getPercentage90());
            //力矩百分比110~120
            weightPercentVO.setPercentage110(daily.getPercentage110());
            //力矩百分比120以上
            weightPercentVO.setPercentage120(daily.getPercentage120());
            //吊钟总数
            weightPercentVO.setLiftFrequency(daily.getLiftFrequency());
            //黑匣子编号
            weightPercentVO.setDeviceNo(daily.getDeviceNo());
            // 添加当天的数据
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                //今天的
                String keyPre = "device_platform:crane:statistics:";
                //加上今天的数值
                String key = keyPre + deviceNo;
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    if (currentDay.getLiftFrequency() == null) {
                        currentDay.setLiftFrequency(0);
                    }
                    //重量报警
                    weightPercentVO.setWeightAlarm(weightPercentVO.getWeightAlarm()+currentDay.getWeightAlarm());
                    //幅度报警
                    weightPercentVO.setRangeAlarm(weightPercentVO.getRangeAlarm()+currentDay.getRangeWarn());
                    //高度报警
                    weightPercentVO.setLimitAlarm(weightPercentVO.getLimitAlarm()+currentDay.getLimitAlarm());
                    //力矩百分比0~40
                    weightPercentVO.setPercentage0(daily.getPercentage0()+currentDay.getPercentage0());
                    //力矩百分比40~60
                    weightPercentVO.setPercentage40(weightPercentVO.getPercentage40()+currentDay.getPercentage40());
                    //力矩百分比60~80
                    weightPercentVO.setPercentage60(weightPercentVO.getPercentage60()+currentDay.getPercentage60());
                    //力矩百分比80~90
                    weightPercentVO.setPercentage80(weightPercentVO.getPercentage80()+currentDay.getPercentage80());
                    //力矩百分比90~110
                    weightPercentVO.setPercentage90(weightPercentVO.getPercentage90()+currentDay.getPercentage90());
                    //力矩百分比110~120
                    weightPercentVO.setPercentage110(weightPercentVO.getPercentage110()+currentDay.getPercentage110());
                    //力矩百分比120以上
                    weightPercentVO.setPercentage120(weightPercentVO.getPercentage120()+currentDay.getPercentage120());
                    //吊钟总数
                    weightPercentVO.setLiftFrequency(weightPercentVO.getLiftFrequency()+currentDay.getLiftFrequency());
                }}
            return new ResultDTO<>(true, weightPercentVO, "成功");
        } else {
            return new ResultDTO<>(true, new WeightPercentVO(), "");
        }
    }


    /**
     * 力矩百分比详情接口
     *
     * @param requestDTO 请求参数
     * @return 返回页面数据
     */
    @Override
    public ResultDTO<DataDTO<List<MomentPercentInfo>>> selectMomentPercentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO) {
        //黑匣子编号
        String deviceNo = requestDTO.getBody().getDeviceNo();
        if (StrUtil.isEmpty(deviceNo) || "".equals(deviceNo)) {
            return new ResultDTO<>(false, null, "黑匣子编号不可为空");
        }
        //请求类型 1：力矩百分比90%以下；2：力矩百分比90~110；3:力矩百分比110~120 4：力矩百分比120以上
        Integer type = requestDTO.getBody().getType();
        //状态：1：正常 2 报警 3预警 4 违章
        Integer status = requestDTO.getBody().getStatus();
        //开始时间
        String startTime = requestDTO.getStartTime();
        //结束时间
        String endTime = requestDTO.getEndTime();
        //获取吊钟详情表重对应时间对应塔机编号对应状态对应力矩百分比的塔机
        Wrapper<ProjectCraneCyclicWorkDuration> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect(

                //"(select crane_no from t_project_crane where t_project_crane.id=t_project_crane_cyclic_work_duration.craneId ) as owner",
                "device_no as deviceNo",
                "begin_time as beginTime",
                "end_time as endTime",
                "begin_range as beginRange",
                "end_range as endRange",
                "begin_height as beginHeight",
                "end_height as endHeight",
                "weight as weight",
                "begin_angle as beginAngle",
                "end_angle as endAngle",
                "moment_percentage as momentPercentage",
                "safety_weight as safetyWeight",
                "multiple_rate as multipleRate",
                "alarm_info as alarmInfo",
                "create_time as createTime"

        );

        if (startTime == null) {
            return new ResultDTO<>(false, null, "开始时间不可为空");

        }
        if (endTime == null) {
            return new ResultDTO<>(false, null, "结束时间不可为空");
        }

        if (type != null) {
            if (type == 1) {

                wrapperInfo.ge("moment_percentage", 0);
                wrapperInfo.lt("moment_percentage", 90);
            } else if (type == 2) {
                wrapperInfo.ge("moment_percentage", 90);
                wrapperInfo.lt("moment_percentage", 110);
            } else if (type == 3) {
                wrapperInfo.ge("moment_percentage", 110);
                wrapperInfo.lt("moment_percentage", 120);
            } else if (type == 4) {
                wrapperInfo.ge("moment_percentage", 120);
            }
        }
        if (status != null) {
            //正常
            if (status == 1) {
                wrapperInfo.like("alarm_info", "%无%");
            }
            //报警
            else if (status == 2) {
                wrapperInfo.like("alarm_info", "%报警%");
            }
            //预警
            else if (status == 3) {
                wrapperInfo.like("alarm_info", "%预警%");
            }
            //违章
            else if (status == 4) {
                wrapperInfo.like("alarm_info", "%重量报警%");
            }
        }
        wrapperInfo.eq("device_no", deviceNo);
        wrapperInfo.between("create_time", startTime, endTime);
        wrapperInfo.orderBy("create_time", false);
        Page<ProjectCraneCyclicWorkDuration> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
        iProjectCraneCyclicWorkDurationService.selectPage(page, wrapperInfo);
        List<MomentPercentInfo> momentPercentInfoList = new ArrayList<>();
        List<ProjectCraneCyclicWorkDuration> projectCraneCyclicWorkDurationList = page.getRecords();
        ProjectCraneCyclicWorkDuration projectCraneCyclicWorkDuration;
        MomentPercentInfo momentPercentInfo;
        for (ProjectCraneCyclicWorkDuration aProjectCraneCyclicWorkDurationList : projectCraneCyclicWorkDurationList) {
            projectCraneCyclicWorkDuration = aProjectCraneCyclicWorkDurationList;
            momentPercentInfo = new MomentPercentInfo();
            //起始角度
            momentPercentInfo.setBeginAngle(projectCraneCyclicWorkDuration.getBeginAngle());
            //结束角度
            momentPercentInfo.setEndAngle(projectCraneCyclicWorkDuration.getEndAngle());
            //起始高度
            momentPercentInfo.setBeginHeight(projectCraneCyclicWorkDuration.getBeginHeight());
            //结束高度
            momentPercentInfo.setEndHeight(projectCraneCyclicWorkDuration.getEndHeight());
            //起始幅度
            momentPercentInfo.setBeginRange(projectCraneCyclicWorkDuration.getBeginRange());
            //结束幅度
            momentPercentInfo.setEndRange(projectCraneCyclicWorkDuration.getEndRange());
            //开始时间
            momentPercentInfo.setBeginTime(projectCraneCyclicWorkDuration.getBeginTime());
            //结束时间
            momentPercentInfo.setEndTime(projectCraneCyclicWorkDuration.getEndTime());
            //黑匣子编号
            momentPercentInfo.setDeviceNo(projectCraneCyclicWorkDuration.getDeviceNo());
            //力矩百分比
            momentPercentInfo.setMomentPercentage(projectCraneCyclicWorkDuration.getMomentPercentage());
            //吊绳倍率
            momentPercentInfo.setMultipleRate(projectCraneCyclicWorkDuration.getMultipleRate());
            //安全吊重
            momentPercentInfo.setSafetyWeight(projectCraneCyclicWorkDuration.getSafetyWeight());
            //重量
            momentPercentInfo.setWeight(projectCraneCyclicWorkDuration.getWeight());
            //报警内容
            momentPercentInfo.setAlarmInfo(projectCraneCyclicWorkDuration.getAlarmInfo());
            momentPercentInfoList.add(momentPercentInfo);
        }
        return new ResultDTO<>(true, DataDTO.factory(momentPercentInfoList, page.getTotal()));
    }

    /**
     * 导出力矩百分比详情Excel接口
     */
    @Override
    public void selectMomentPercentInfoExcel(@RequestBody RequestDTO<WeightPercentVO> requestDTO, HttpServletResponse response) {
        Integer type = requestDTO.getBody().getType();
        ResultDTO<DataDTO<List<MomentPercentInfo>>> resultDTO = selectMomentPercentInfo(requestDTO);
        List<MomentPercentInfo> momentPercentInfoList = resultDTO.getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        MomentPercentInfo vo;
        row = new ArrayList<>();
        row.add("开始时间");
        row.add("结束时间");
        row.add("起始幅度(m)");
        row.add("终止幅度(m)");
        row.add("起始高度(m)");
        row.add("终止高度(m)");
        row.add("起始吊重(t)");
        row.add("终止角度(m)");
        row.add("起始角度(t)");
        row.add("力矩百分比(%)");
        row.add("安全吊重(t)");
        row.add("吊绳倍率");
        row.add("报警内容");
        rows.add(row);
        for (MomentPercentInfo aMomentPercentInfoList : momentPercentInfoList) {
            row = new ArrayList<>();
            vo = aMomentPercentInfoList;
            //起始时间
            addExcel(row,vo.getBeginTime().toString());
            //结束时间
            addExcel(row,vo.getEndTime().toString());
            //起始幅度
            addExcel(row,String.valueOf(vo.getBeginRange()));
            //结束幅度
            addExcel(row,String.valueOf(vo.getEndRange()));
            //起始高度
            addExcel(row,String.valueOf(vo.getBeginHeight()));
            //结束高度
            addExcel(row,String.valueOf(vo.getEndHeight()));
            //起始吊重
            addExcel(row,String.valueOf(vo.getWeight()));
            //结束吊重
            addExcel(row,String.valueOf(vo.getEndAngle()));
            //起始角度
            addExcel(row,String.valueOf(vo.getBeginAngle()));
            //力矩百分比
            addExcel(row,String.valueOf(vo.getMomentPercentage()));
            //安全吊重
            addExcel(row,String.valueOf(vo.getSafetyWeight()));
            //吊绳倍率
            addExcel(row,String.valueOf(vo.getMultipleRate()));
            //报警信息
            addExcel(row,String.valueOf(vo.getAlarmInfo()));
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code;
        if (type != null) {
            code=getCode("设备力矩详情");
        } else {
            code=getCode("设备吊重详情统计");
        }
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览查询接口
     *
     * @param requestDTO 请求参数
     * @return 响应信息
     */
    @Override
    public ResultDTO<DataDTO<List<GetInfoVO>>> selectWarnInfo(@RequestBody RequestDTO<GetInfoVO> requestDTO){
        //请求类型  1、预警信息 2、报警信息 3、违章信息
        Integer type = requestDTO.getBody().getType();
        //工程编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //开始时间
        String startTime = requestDTO.getStartTime();
        //黑匣子编号/塔机编号
        String keyWord = requestDTO.getBody().getKeyWord();
        //黑匣子编号
        String deviceNo = requestDTO.getBody().getDeviceNo();
        if (startTime == null) {
            return new ResultDTO<>(false, null, "开始时间不可为空");
        }
        //结束时间
        String endTime = requestDTO.getEndTime();
        if (endTime == null) {
            return new ResultDTO<>(false, null, "结束时间不可为空");
        }
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        Map<String, Object> map = new HashMap<>(10);
        //预警信息
        if (type == 1) {
            wrapperInfo.setSqlSelect(
                    "project_id as projectId",
                    "project_name as projectName",
                    "crane_no as craneNo",
                    "device_no as deviceNo",
                    "sum(weight_warn) as weightWarn",
                    "sum(range_warn) as rangeWarn",
                    "sum(limit_warn) as limitWarn",
                    "sum(moment_warn) as momentWarn",
                    "sum(collision_warn) as collisionWarn");
        } else if (type == 2) {
            //报警信息
            wrapperInfo.setSqlSelect(
                    "project_id as projectId",
                    "project_name as projectName",
                    "crane_no as craneNo",
                    "device_no as deviceNo",
                    "sum(range_alarm) as rangeAlarm",
                    "sum(limit_alarm) as limitAlarm",
                    "sum(moment_alarm) as momentAlarm",
                    "sum(wind_speed_alarm) as windSpeedAlarm",
                    "sum(tilt_alarm) as tiltAlarm",
                    "sum(collision_alarm) as collisionAlarm");
        } else if (type == 3) {
            //违章信息
            wrapperInfo.setSqlSelect(
                    "project_id as projectId",
                    "project_name as projectName",
                    "crane_no as craneNo",
                    "device_no as deviceNo",
                    "sum(weight_alarm) as weightAlarm");
        }
        wrapperInfo.eq(null != projectId, "project_id", projectId);
        wrapperInfo.ge("work_date", startTime);
        map.put("startTime", startTime);
        wrapperInfo.le("work_date", endTime);
        map.put("endTime", endTime);
        if (projectId != null) {
            map.put("projectId", projectId);
        } else {
            map.put("orgIds", Const.orgIds.get());
        }
        if (deviceNo != null) {
            map.put("deviceNo", deviceNo);
        }
        map.put("keyWord", keyWord);
        wrapperInfo.groupBy("device_no");
        Page<ProjectCraneStaticsticsDailyVO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<ProjectCraneStaticsticsDailyVO> craneStaticDailyList = projectCraneStaticsDailyService.selectPageList(page, map);
        List<GetInfoVO> getInfoVOList = new ArrayList<>();
        // 添加当天的数据
        if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
            //今天的
            String keyPre = "device_platform:crane:statistics:";
            //加上今天的数值
            for (ProjectCraneStaticsticsDailyVO daily : craneStaticDailyList) {
                String key = keyPre + daily.getDeviceNo();
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    if (currentDay.getLiftFrequency() == null) {
                        currentDay.setLiftFrequency(0);
                    }
                    if (daily.getLiftFrequency() == null) {
                        daily.setLiftFrequency(0);
                    }
                    //今天吊重次数+总吊重次数
                    daily.setLiftFrequency(currentDay.getLiftFrequency() + daily.getLiftFrequency());
                    if (currentDay.getCollisionAlarm() == null) {
                        currentDay.setCollisionAlarm(0);
                    }
                    if (daily.getCollisionAlarm() == null) {
                        daily.setCollisionAlarm(0);
                    }
                    //今日碰撞报警+总碰撞报警
                    daily.setCollisionAlarm(currentDay.getCollisionAlarm() + daily.getCollisionAlarm());
                    if (currentDay.getCollisionWarn() == null) {
                        currentDay.setCollisionWarn(0);
                    }
                    if (daily.getCollisionWarn() == null) {
                        daily.setCollisionWarn(0);
                    }
                    //今日碰撞预警+总碰撞预警
                    daily.setCollisionWarn(currentDay.getCollisionWarn() + daily.getCollisionWarn());
                    if (currentDay.getLimitAlarm() == null) {
                        currentDay.setLimitAlarm(0);
                    }
                    if (daily.getLimitAlarm() == null) {
                        daily.setLimitAlarm(0);
                    }
                    //今日高度报警+总高度报警
                    daily.setLimitAlarm(currentDay.getLimitAlarm() + daily.getLimitAlarm());
                    if (currentDay.getLimitWarn() == null) {
                        currentDay.setLimitWarn(0);
                    }
                    if (daily.getLimitWarn() == null) {
                        daily.setLimitWarn(0);
                    }
                    //今日高度预警+总高度预警
                    daily.setLimitWarn(currentDay.getLimitWarn() + daily.getLimitWarn());
                    if (currentDay.getMomentAlarm() == null) {
                        currentDay.setMomentAlarm(0);
                    }
                    if (daily.getMomentAlarm() == null) {
                        daily.setMomentAlarm(0);
                    }
                    //今日力矩报警+总力矩报警
                    daily.setMomentAlarm(currentDay.getMomentAlarm() + daily.getMomentAlarm());
                    if (currentDay.getMomentWarn() == null) {
                        currentDay.setMomentWarn(0);
                    }
                    if (daily.getMomentWarn() == null) {
                        daily.setMomentWarn(0);
                    }
                    //今日力矩预警+总力矩预警
                    daily.setMomentWarn(currentDay.getMomentWarn() + daily.getMomentWarn());
                    if (currentDay.getRangeAlarm() == null) {
                        currentDay.setRangeAlarm(0);
                    }
                    if (daily.getRangeAlarm() == null) {
                        daily.setRangeAlarm(0);
                    }
                    //今日幅度报警+总幅度报警
                    daily.setRangeAlarm(currentDay.getRangeAlarm() + daily.getRangeAlarm());
                    if (currentDay.getRangeWarn() == null) {
                        currentDay.setRangeWarn(0);
                    }
                    if (daily.getRangeWarn() == null) {
                        daily.setRangeWarn(0);
                    }
                    //今日幅度预警+总幅度预警
                    daily.setRangeWarn(currentDay.getRangeWarn() + daily.getRangeWarn());
                    if (currentDay.getWeightAlarm() == null) {
                        currentDay.setWeightAlarm(0);
                    }
                    if (daily.getWeightAlarm() == null) {
                        daily.setWeightAlarm(0);
                    }
                    //今日重量报警+总重量报警
                    daily.setWeightAlarm(currentDay.getWeightAlarm() + daily.getWeightAlarm());
                    if (currentDay.getWeightWarn() == null) {
                        currentDay.setWeightWarn(0);
                    }
                    if (daily.getWeightWarn() == null) {
                        daily.setWeightWarn(0);
                    }
                    //今日重量预警+总重量预警
                    daily.setWeightWarn(currentDay.getWeightWarn() + daily.getWeightWarn());
                    if (currentDay.getTiltAlarm() == null) {
                        currentDay.setTiltAlarm(0);
                    }
                    if (daily.getTiltAlarm() == null) {
                        daily.setTiltAlarm(0);
                    }
                    //今日倾斜报警+总倾斜报警
                    daily.setTiltAlarm(currentDay.getTiltAlarm() + daily.getTiltAlarm());
                    if (currentDay.getWindSpeedAlarm() == null) {
                        currentDay.setWindSpeedAlarm(0);
                    }
                    if (daily.getWindSpeedAlarm() == null) {
                        daily.setWindSpeedAlarm(0);
                    }
                    //今日风俗报警+总风俗报警
                    daily.setWindSpeedAlarm(currentDay.getWindSpeedAlarm() + daily.getWindSpeedAlarm());
                    if (currentDay.getPercentage0() == null) {
                        currentDay.setPercentage0(0);
                    }
                    if (daily.getPercentage0() == null) {
                        daily.setPercentage0(0);
                    }
                    //今日力矩百分比0~40次数+总力矩百分比0~40次数
                    daily.setPercentage0(currentDay.getPercentage0() + daily.getPercentage0());
                    if (currentDay.getPercentage40() == null) {
                        currentDay.setPercentage40(0);
                    }
                    if (daily.getPercentage40() == null) {
                        daily.setPercentage40(0);
                    }
                    //今日力矩百分比40~60次数+总力矩百分比40~60次数
                    daily.setPercentage40(currentDay.getPercentage40() + daily.getPercentage40());
                    if (currentDay.getPercentage60() == null) {
                        currentDay.setPercentage60(0);
                    }
                    if (daily.getPercentage60() == null) {
                        daily.setPercentage60(0);
                    }
                    //今日力矩百分比60~80次数+总力矩百分比60~80次数
                    daily.setPercentage60(currentDay.getPercentage60() + daily.getPercentage60());
                    if (currentDay.getPercentage80() == null) {
                        currentDay.setPercentage80(0);
                    }
                    if (daily.getPercentage80() == null) {
                        daily.setPercentage80(0);
                    }
                    //今日力矩百分比80~90次数+总力矩百分比80~90次数
                    daily.setPercentage80(currentDay.getPercentage80() + daily.getPercentage80());
                    if (currentDay.getPercentage90() == null) {
                        currentDay.setPercentage90(0);
                    }
                    if (daily.getPercentage90() == null) {
                        daily.setPercentage90(0);
                    }
                    //今日力矩百分比00~110次数+总力矩百分比90~110次数
                    daily.setPercentage90(currentDay.getPercentage90() + daily.getPercentage90());
                    if (currentDay.getPercentage110() == null) {
                        currentDay.setPercentage110(0);
                    }
                    if (daily.getPercentage110() == null) {
                        daily.setPercentage110(0);
                    }
                    //今日力矩百分比110~120次数+总力矩百分比110~120次数
                    daily.setPercentage110(currentDay.getPercentage110() + daily.getPercentage110());
                    if (currentDay.getPercentage120() == null) {
                        currentDay.setPercentage120(0);
                    }
                    if (daily.getPercentage120() == null) {
                        daily.setPercentage120(0);
                    }
                    //今日力矩百分比120以上次数+总力矩百分比120以上次数
                    daily.setPercentage120(currentDay.getPercentage120() + daily.getPercentage120());
                }
            }
        }
        ProjectCraneStaticsticsDailyVO statisticsDaily;
        GetInfoVO getInfoVO;
        for (ProjectCraneStaticsticsDailyVO aCraneStaticDailyList : craneStaticDailyList) {
            statisticsDaily = aCraneStaticDailyList;
            getInfoVO = new GetInfoVO();
            //塔机编号
            getInfoVO.setProjectId(statisticsDaily.getProjectId());
            //工程名称
            getInfoVO.setProjectName(statisticsDaily.getProjectName());
            //塔机编号
            getInfoVO.setCraneNo(statisticsDaily.getCraneNo());
            //黑匣子编号
            getInfoVO.setDeviceNo(statisticsDaily.getDeviceNo());
            //开发商名称
            getInfoVO.setBuilderName(statisticsDaily.getBuilderName());
            if (type == 1) {
                if (statisticsDaily.getWeightWarn() != null) {
                    getInfoVO.setWeightWarn(statisticsDaily.getWeightWarn());
                } else {
                    getInfoVO.setWeightWarn(0);
                }
                if (statisticsDaily.getRangeWarn() != null) {
                    getInfoVO.setRangeWarn(statisticsDaily.getRangeWarn());
                } else {
                    getInfoVO.setRangeWarn(0);
                }
                if (statisticsDaily.getLimitWarn() != null) {
                    getInfoVO.setLimitWarn(statisticsDaily.getLimitWarn());
                } else {
                    getInfoVO.setLimitWarn(0);
                }
                if (statisticsDaily.getMomentWarn() != null) {
                    getInfoVO.setMomentWarn(statisticsDaily.getMomentWarn());
                } else {
                    getInfoVO.setMomentWarn(0);
                }
                if (statisticsDaily.getCollisionWarn() != null) {
                    getInfoVO.setCollisionWarn(statisticsDaily.getCollisionWarn());
                } else {
                    getInfoVO.setCollisionWarn(0);
                }
                getInfoVO.setWarnCount(getInfoVO.getWeightWarn() + getInfoVO.getRangeWarn() + getInfoVO.getLimitWarn() + getInfoVO.getMomentWarn() + getInfoVO.getCollisionWarn());
            } else if (type == 2) {

                if (statisticsDaily.getRangeAlarm() != null) {
                    getInfoVO.setRangeAlarm(statisticsDaily.getRangeAlarm());
                } else {
                    getInfoVO.setRangeAlarm(0);
                }
                if (statisticsDaily.getLimitAlarm() != null) {
                    getInfoVO.setLimitAlarm(statisticsDaily.getLimitAlarm());
                } else {
                    getInfoVO.setLimitAlarm(0);
                }

                if (statisticsDaily.getMomentAlarm() != null) {
                    getInfoVO.setMomentAlarm(statisticsDaily.getMomentAlarm());
                } else {
                    getInfoVO.setMomentAlarm(0);
                }

                if (statisticsDaily.getCollisionAlarm() != null) {
                    getInfoVO.setCollisionAlarm(statisticsDaily.getCollisionAlarm());
                } else {
                    getInfoVO.setCollisionAlarm(0);
                }

                if (statisticsDaily.getWindSpeedAlarm() != null) {
                    getInfoVO.setWindSpeedAlarm(statisticsDaily.getWindSpeedAlarm());
                } else {
                    getInfoVO.setWindSpeedAlarm(0);
                }

                if (statisticsDaily.getTiltAlarm() != null) {
                    getInfoVO.setTiltAlarm(statisticsDaily.getTiltAlarm());
                } else {
                    getInfoVO.setTiltAlarm(0);
                }
                getInfoVO.setAlarmCount(getInfoVO.getRangeAlarm() + getInfoVO.getLimitAlarm() + getInfoVO.getMomentAlarm() + getInfoVO.getCollisionAlarm() + getInfoVO.getWindSpeedAlarm() + getInfoVO.getTiltAlarm());
            } else if (type == 3) {
                if (statisticsDaily.getWeightAlarm() != null) {
                    getInfoVO.setWeightAlarm(statisticsDaily.getWeightAlarm());
                } else {
                    getInfoVO.setWeightAlarm(0);
                }

            }
            getInfoVOList.add(getInfoVO);
        }
        return new ResultDTO<>(true, DataDTO.factory(getInfoVOList, page.getTotal()));
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览导出Excel接口
     * @param type 请求类型
     * @param deviceNo 黑匣子编号
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param keyWord 塔吊编号/黑匣子编号
     * @param response 响应信息
     */
    @Override
    public void getWarnInfoExcel(Integer type,String deviceNo, Integer projectId, String startTime, String endTime, String keyWord, HttpServletResponse response){
        GetInfoVO getInfoVO = new GetInfoVO();
        getInfoVO.setProjectId(projectId);
        getInfoVO.setKeyWord(keyWord);
        getInfoVO.setType(type);
        getInfoVO.setDeviceNo(deviceNo);
        RequestDTO<GetInfoVO> requestDTO = new RequestDTO<>();
        requestDTO.setBody(getInfoVO);
        requestDTO.setStartTime(startTime);
        requestDTO.setEndTime(endTime);
        requestDTO.setPageNum(1);
        requestDTO.setPageSize(10000);
        //获取Excel文件内容
        ResultDTO<DataDTO<List<GetInfoVO>>> resultDTO = selectWarnInfo(requestDTO);
        List<GetInfoVO> getInfoVOList = resultDTO.getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        GetInfoVO vo;
        row = new ArrayList<>();
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("工程名称");
        if (type == 1) {
            row.add("总预警");
            row.add("重量预警");
            row.add("幅度预警");
            row.add("高度预警");
            row.add("力矩预警");
            row.add("碰撞预警");
        }
        if (type == 2) {
            row.add("总报警");
            row.add("幅度报警");
            row.add("高度报警");
            row.add("力矩报警");
            row.add("碰撞报警");
            row.add("风速报警");
            row.add("倾角报警");

        }
        if (type == 3) {
            row.add("违章次数");
        }
        rows.add(row);
        for (GetInfoVO aGetInfoVOList : getInfoVOList) {
            row = new ArrayList<>();
            vo = aGetInfoVOList;
            //塔机编号
            addExcel(row, vo.getCraneNo());
            //黑匣子编号
            addExcel(row, vo.getDeviceNo());
            //工程名称
            addExcel(row, vo.getProjectName());
            if (type == 1) {
                //预警总次数
                if (null != vo.getWarnCount()) {
                    row.add(String.valueOf(vo.getWarnCount()));
                } else {
                    row.add("");
                }
                //重量预警次数
                if (null != vo.getWeightWarn()) {
                    row.add(String.valueOf(vo.getWeightWarn()));
                } else {
                    row.add("");
                }
                //幅度预警次数
                if (null != vo.getRangeWarn()) {
                    row.add(String.valueOf(vo.getRangeWarn()));
                } else {
                    row.add("");
                }
                //高度预警次数
                if (null != vo.getLimitWarn()) {
                    row.add(String.valueOf(vo.getLimitWarn()));
                } else {
                    row.add("");
                }
                //力矩预警次数
                if (null != vo.getMomentWarn()) {
                    row.add(String.valueOf(vo.getMomentWarn()));
                } else {
                    row.add("");
                }
                //碰撞预警次数
                if (null != vo.getCollisionWarn()) {
                    row.add(String.valueOf(vo.getCollisionWarn()));
                } else {
                    row.add("");
                }
            } else if (type == 2) {
                //报警总次数
                if (null != vo.getAlarmCount()) {
                    row.add(String.valueOf(vo.getAlarmCount()));
                } else {
                    row.add("");
                }
                //幅度报警次数
                if (null != vo.getRangeAlarm()) {
                    row.add(String.valueOf(vo.getRangeAlarm()));
                } else {
                    row.add("");
                }
                //高度报警次数
                if (null != vo.getLimitAlarm()) {
                    row.add(String.valueOf(vo.getLimitAlarm()));
                } else {
                    row.add("");
                }
                //力矩报警次数
                if (null != vo.getMomentAlarm()) {
                    row.add(String.valueOf(vo.getMomentAlarm()));
                } else {
                    row.add("");
                }
                //碰撞报警次数
                if (null != vo.getCollisionAlarm()) {
                    row.add(String.valueOf(vo.getCollisionAlarm()));
                } else {
                    row.add("");
                }
                //风速报警次数
                if (null != vo.getWindSpeedAlarm()) {
                    row.add(String.valueOf(vo.getWindSpeedAlarm()));
                } else {
                    row.add("");
                }
                //倾度报警次数
                if (null != vo.getTiltAlarm()) {
                    row.add(String.valueOf(vo.getTiltAlarm()));
                } else {
                    row.add("");
                }
            }
            if (type == 3) {
                //重量报警次数
                if (null != vo.getWeightAlarm()) {
                    row.add(String.valueOf(vo.getWeightAlarm()));
                } else {
                    row.add("");
                }
            }
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code;
        if (type == 1) {
            code=getCode("预警信息一览");
        } else if (type == 2) {
            code=getCode("报警信息一览");
        } else {
            code=getCode("违章信息一览");
        }
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情
     *
     * @param requestDTO 请求参数
     * @return 返回的页面信息
     */
    @Override
    public ResultDTO<DataDTO<List<AlarmInfoDTO>>> selectAlarmInfo(@RequestBody RequestDTO<AlarmInfoVO> requestDTO) {
        //黑匣子编号
        String deviceNo = requestDTO.getBody().getDeviceNo();
        //是否处理
        Integer isHandeler = requestDTO.getBody().getIsHandle();
        //请求类型 1 预警信息详情  2 报警信息详情  3 违章信息详情
        Integer type = requestDTO.getBody().getType();
        if (deviceNo == null) {
            return new ResultDTO<>(false, null, "黑匣子编号不可为空");
        }
        //开始时间
        String startTime = requestDTO.getStartTime();
        if (null == startTime || "".equals(startTime)) {
            return new ResultDTO<>(false, null, "开始时间不可为空");
        }
        //结束时间
        String endTime = requestDTO.getEndTime();
        if (null == endTime || "".equals(endTime)) {
            return new ResultDTO<>(false, null, "结束时间不可为空");
        }
        //查询project_crane_alarm表
        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        alarmWrapper.setSqlSelect(
                "id",
                "device_no as deviceNo",
                "create_time as createTime",
                "end_time as endTime",
                "alarm_info as alarmInfo",
                "status as status",
                "modify_user_name as modifyUserName",
                "modify_time as modifyTime",
                "comments as comments",
                "is_handle as isHandle",
                "alarm_time as alarmTime"
        );
        //存放报警类型
        List<Integer> idList = new ArrayList<>();
        if (type != null) {
            if (type == 1) {
                Integer[] ids={2,4,6,8,10,12,14};
                idList= Arrays.asList(ids);
            } else if (type == 2) {
                Integer[] ids={3,5,7,9,11,13,15,16};
                idList= Arrays.asList(ids);
            } else {
                idList.add(1);
            }
            alarmWrapper.in("alarm_id", idList);

        } else {
            return new ResultDTO<>(false, null, "查询类型不可为空");
        }
        alarmWrapper.eq("device_no", deviceNo);
        alarmWrapper.between("create_time", startTime, endTime);
        alarmWrapper.orderBy("create_time", false);
        if (isHandeler != null) {
            alarmWrapper.eq("is_handle", isHandeler);
        }
        Page<ProjectCraneAlarm> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
        iProjectCraneAlarmService.selectPage(page, alarmWrapper);
        List<AlarmInfoVO> alarmvoList = new ArrayList<>();
        ProjectCraneAlarm projectCraneAlarm;
        AlarmInfoVO alarmInfoVO;
        List<ProjectCraneAlarm> alarmList = page.getRecords();
        if (null != alarmList && alarmList.size() > 0) {
            for (ProjectCraneAlarm anAlarmList : alarmList) {
                projectCraneAlarm = anAlarmList;
                alarmInfoVO = new AlarmInfoVO();
                alarmInfoVO.setId(projectCraneAlarm.getId());
                alarmInfoVO.setDeviceNo(projectCraneAlarm.getDeviceNo());
                alarmInfoVO.setBeginTime(DateUtil.formatDateTime(projectCraneAlarm.getCreateTime()));
                alarmInfoVO.setEndTime(DateUtil.formatDateTime(projectCraneAlarm.getEndTime()));
                alarmInfoVO.setAlarmTime(DateUtil.formatDateTime(projectCraneAlarm.getAlarmTime()));
                alarmInfoVO.setDeviceNo(projectCraneAlarm.getDeviceNo());
                alarmInfoVO.setAlarmInfo(projectCraneAlarm.getAlarmInfo());
                alarmInfoVO.setIsHandle(projectCraneAlarm.getIsHandle());
                alarmInfoVO.setModifyUserName(projectCraneAlarm.getModifyUserName());
                alarmInfoVO.setModifyTime(DateUtil.formatDateTime(projectCraneAlarm.getModifyTime()));
                alarmInfoVO.setComments(projectCraneAlarm.getComments());
                alarmvoList.add(alarmInfoVO);
            }
        }
        AlarmInfoDTO dto = new AlarmInfoDTO();
        dto.setList(alarmvoList);
        dto.setTotal(alarmvoList.size());
        dto.setHandled((int) alarmvoList.stream().filter(item -> item.getIsHandle() == 1).count());
        dto.setUnHandled((int) alarmvoList.stream().filter(item -> item.getIsHandle() != 1).count());
        return new ResultDTO(true, DataDTO.factory(alarmvoList, page.getTotal()));
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情导出Excel
     *
     * @param requestDTO 请求参数
     */
    @Override
    public void selectAlarmInfoExcel(@RequestBody RequestDTO<AlarmInfoVO> requestDTO, HttpServletResponse response) {
        ResultDTO<DataDTO<List<AlarmInfoDTO>>> resultDTO = selectAlarmInfo(requestDTO);
        Integer type = requestDTO.getBody().getType();
        List<AlarmInfoVO> getInfoVOList = resultDTO.getData().getList().get(0).getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        AlarmInfoVO vo;
        row = new ArrayList<>();
        row.add("开始时间");
        row.add("结束时间");
        row.add("报警详情");
        row.add("是否处理");
        row.add("处理人");
        row.add("处理时间");
        row.add("处理意见");
        rows.add(row);
        for (AlarmInfoVO aGetInfoVOList : getInfoVOList) {
            row = new ArrayList<>();
            vo = aGetInfoVOList;
            addExcel(row,vo.getBeginTime());
            addExcel(row,vo.getEndTime());
            addExcel(row,vo.getAlarmInfo());
            if (null != vo.getIsHandle()) {
                if (vo.getIsHandle() == 0) {
                    row.add("未处理");
                } else if (vo.getIsHandle() == 1) {
                    row.add("已处理");
                }
            } else {
                row.add("");
            }
            addExcel(row,vo.getModifyUserName());
            addExcel(row,vo.getModifyTime());
            addExcel(row,vo.getComments());
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code;
        if (type == 1) {
            code=getCode("预警信息一览");
        } else if (type == 2) {
            code=getCode("报警信息一览");
        } else {
            code=getCode("违章信息一览");
        }
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * @param requestDTO 请求参数
     * @return 工作等级统计接口
     */
    @Override
    public ResultDTO<DataDTO<List<WorkGradeVO>>> selectWorkGrade(@RequestBody RequestDTO<WorkGradeVO> requestDTO) {
        //工程编号
        Integer projectId = requestDTO.getBody().getProjectId();
        //开始时间
        String startTime = requestDTO.getStartTime();
        //结束时间
        String endTime = requestDTO.getEndTime();
        //塔机编号/黑匣子编号
        String keyWord = requestDTO.getBody().getKeyWord();
        //查询project_crane_statistics_daily表
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect(
                "project_id as projectId",
                "project_name as projectName",
                "crane_no as craneNo",
                "device_no as deviceNo",
                "(select specification from t_project_crane where t_project_crane.device_no=t_project_crane_statistics_daily.device_no limit 1) as owner",
                "sum(lift_frequency) as liftFrequency",
                "sum(weight_alarm) as weightAlarm",
                "sum(moment_alarm) as momentAlarm");

        if (null == startTime || "".equals(startTime)) {
            return new ResultDTO<>(false, null, "开始时间不可为空");
        }
        if (null == endTime || "".equals(endTime)) {
            return new ResultDTO<>(false, null, "结束时间不可为空");
        }
        wrapperInfo.between("work_date", startTime, endTime);
        //org_id过滤
        if (projectId != null) {
            wrapperInfo.eq("project_id", projectId);
        } else {
            List<String> craneNos = getOrgIdCraneNo();
            wrapperInfo.in("crane_no", craneNos);
        }
        if (StrUtil.isNotBlank(keyWord)) {
            wrapperInfo.andNew().like("crane_no", keyWord).or().like("device_no", keyWord);
        }
        wrapperInfo.groupBy("device_no");
        Page<ProjectCraneStatisticsDaily> page = null;
        List<ProjectCraneStatisticsDaily> craneStaticDailyList;
        //当前页码信息为空，查询所有满足条件的信息
        if (requestDTO.getPageNum() != null) {
            page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            projectCraneStaticsDailyService.selectPage(page, wrapperInfo);
            craneStaticDailyList = page.getRecords();
        } else {
            craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);
        }
        List<WorkGradeVO> workGradevoList = new ArrayList<>();
        ProjectCraneStatisticsDaily statisticsDaily;
        WorkGradeVO workGradeVO;
        //超载次数
        BigDecimal overloadFrequency;
        //吊重次数
        BigDecimal liftFrequency;
        Wrapper<ProjectCraneStatisticsDaily> createTimeWrapper;
        List<ProjectCraneStatisticsDaily> createTimeList;
        for (ProjectCraneStatisticsDaily aCraneStaticDailyList : craneStaticDailyList) {
            statisticsDaily = aCraneStaticDailyList;
            workGradeVO = new WorkGradeVO();
            //工程编号
            workGradeVO.setProjectId(statisticsDaily.getProjectId());
            //工程名称
            workGradeVO.setProjectName(statisticsDaily.getProjectName());
            //塔机编号
            workGradeVO.setCraneNo(statisticsDaily.getCraneNo());
            //黑匣子编号
            workGradeVO.setDeviceNo(statisticsDaily.getDeviceNo());
            //型号
            workGradeVO.setSpecification(statisticsDaily.getOwner());
            //吊重起始时间
            createTimeWrapper = new EntityWrapper<>();
            createTimeWrapper.setSqlSelect("work_date as workDate");
            createTimeWrapper.eq("device_no", statisticsDaily.getDeviceNo());
            createTimeWrapper.between("work_date", startTime, endTime);
            createTimeWrapper.orderBy("work_date");
            createTimeList = projectCraneStaticsDailyService.selectList(createTimeWrapper);
            if (createTimeList != null && createTimeList.size() > 0) {
                workGradeVO.setWorkDate(DateUtil.format(createTimeList.get(0).getWorkDate(), "yyyy-MM-dd HH:mm:ss"));
            }
            //吊重次数、、
            workGradeVO.setLiftFrequency(statisticsDaily.getLiftFrequency());
            //超载次数=重量报警+力矩报警
            workGradeVO.setOverloadFrequency(statisticsDaily.getWeightAlarm() + statisticsDaily.getMomentAlarm());
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                //今天的
                String keyPre = "device_platform:crane:statistics:";
                //加上今天的数值
                String key = keyPre + statisticsDaily.getDeviceNo();
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    //今天吊重次数+总吊重次数
                    workGradeVO.setLiftFrequency(currentDay.getLiftFrequency() + statisticsDaily.getLiftFrequency());
                    workGradeVO.setOverloadFrequency(statisticsDaily.getWeightAlarm()+currentDay.getWeightAlarm() + statisticsDaily.getMomentAlarm()+currentDay.getMomentAlarm());
                log.info("当天:[{}]",workGradeVO.toString());
                }
            }
            //超载率
            overloadFrequency = new BigDecimal(workGradeVO.getOverloadFrequency());
            liftFrequency = new BigDecimal(workGradeVO.getLiftFrequency());
            if (workGradeVO.getLiftFrequency() != null && workGradeVO.getLiftFrequency() > 0) {
                workGradeVO.setOverloadRate(overloadFrequency.multiply(new BigDecimal("100")).divide(liftFrequency, 2,RoundingMode.HALF_DOWN));
            }
            else{
                workGradeVO.setOverloadRate(new BigDecimal("0"));
            }
            //月使用频率
            long betweenDay;
            betweenDay = DateUtil.between(DateUtil.parse(startTime, "yyyy-MM-dd"),
                    DateUtil.parse(endTime, "yyyy-MM-dd"), DateUnit.DAY) + 1;
            liftFrequency = new BigDecimal(workGradeVO.getLiftFrequency());
            log.info("====liftFrequency:",liftFrequency.doubleValue());
            log.info("====workGradeVO[{}]",workGradeVO.toString());
            workGradeVO.setFrequencyMonth(liftFrequency.multiply(new BigDecimal("3000")).divide(new BigDecimal(betweenDay), 8, RoundingMode.HALF_DOWN).divide(new BigDecimal("100"), 2, RoundingMode.HALF_DOWN)
            );
            workGradevoList.add(workGradeVO);
        }
        if (page != null) {
            return new ResultDTO<>(true, DataDTO.factory(workGradevoList, page.getTotal()));
        } else {
            return new ResultDTO<>(true, DataDTO.factory(workGradevoList, 0));
        }
    }

    /**
     * 向Excel文件添加数据
     * @param row 行
     * @param info 数据
     */
    private void addExcel(List<String> row, String info) {
        if (StrUtil.isNotBlank(info)) {
            row.add(info);
        } else {
            row.add("");
        }
        //return row;
    }

    /**
     * 工作等级统计导出接口
     *
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param keyWord   塔吊编号/黑匣子编号
     * @param response  响应信息
     */
    @Override
    public void getWorkGradeExcel(Integer projectId, String startTime, String endTime, String keyWord, HttpServletResponse response) {

        WorkGradeVO workGradeVO = new WorkGradeVO();
        workGradeVO.setProjectId(projectId);
        workGradeVO.setKeyWord(keyWord);
        RequestDTO<WorkGradeVO> requestDTO = new RequestDTO<>();
        requestDTO.setBody(workGradeVO);
        requestDTO.setStartTime(startTime);
        requestDTO.setEndTime(endTime);
        //获取Excel文件内容
        ResultDTO<DataDTO<List<WorkGradeVO>>> resultDTO = selectWorkGrade(requestDTO);
        List<WorkGradeVO> workGradevoList = resultDTO.getData().getList();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        WorkGradeVO vo;
        row = new ArrayList<>();
        row.add("工程名称");
        row.add("塔机编号");
        row.add("黑匣子编号");
        row.add("规格型号");
        row.add("吊重起始时间");
        row.add("吊重次数");
        row.add("超载次数");
        row.add("超载率(%)");
        row.add("月使用频率(次/月)");
        row.add("使用等级");
        rows.add(row);
        for (WorkGradeVO aWorkGradevoList : workGradevoList) {
            row = new ArrayList<>();
            vo = aWorkGradevoList;
            //工程名称
            addExcel(row, vo.getProjectName());
            //塔机编号
            addExcel(row, vo.getCraneNo());
            //黑匣子编号
            addExcel(row, vo.getDeviceNo());
            //设备型号
            addExcel(row, String.valueOf(vo.getSpecification()));
            //吊重起始时间
            addExcel(row, vo.getWorkDate());
            //吊重次数
            addExcel(row, String.valueOf(vo.getLiftFrequency()));
            //超载次数
            addExcel(row, String.valueOf(vo.getOverloadFrequency()));
            //超载率
            addExcel(row, String.valueOf(vo.getOverloadRate()));
            //月使用频率
            addExcel(row, String.valueOf(vo.getFrequencyMonth()));
            //使用等级
            BigDecimal voFrequency = vo.getFrequencyMonth();
            int test1=voFrequency.compareTo(new BigDecimal("133.3"));
            int test2=voFrequency.compareTo(new BigDecimal("262.5"));
            int test3=voFrequency.compareTo(new BigDecimal("520.8"));
            int test4=voFrequency.compareTo(new BigDecimal("1041.6"));
            int test5=voFrequency.compareTo(new BigDecimal("2083.3"));
            if (test1<0) {
                addExcel(row, "很少使用");
            } else if (test2<0) {
                addExcel(row, "很少使用");
            } else if (test3<0) {
                addExcel(row, "很少使用");
            } else if (test4<0) {
                addExcel(row, "不频繁使用");
            } else if (test5<0) {
                addExcel(row, "中等频繁使用");
            } else {
                addExcel(row, "---");
            }
            System.out.println(vo);
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code = getCode("工作等级统计");//URLEncoder.encode("工作等级统计");
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * 设备在线统计
     *
     * @param requestDTO 请求参数
     * @return 返回页面信息
     */
    @Override
    public ResultDTO<List<IsOnlineVO>> selectIsOnline(@RequestBody RequestDTO<IsOnlineVO> requestDTO) {
        String startTime = requestDTO.getStartTime();
        String endTime = requestDTO.getEndTime();
        //请求类型 1：按设备  2按项目
        Integer type = requestDTO.getBody().getType();
        List<IsOnlineVO> isOnlineVOList = new ArrayList<>();
        IsOnlineVO vo;
        //查询所有的设备
        Wrapper<ProjectCrane> craneWrapperInfo = new EntityWrapper<>();
        craneWrapperInfo.setSqlSelect("crane_no as craneNo", "create_time as createTime", "project_id as projectId");
        craneWrapperInfo.eq("is_del", 0);
        craneWrapperInfo.le("create_time", new Date());
        craneWrapperInfo.in("org_id", Const.orgIds.get());
        List<ProjectCrane> craneList = projectCraneService.selectList(craneWrapperInfo);
        //根据org_id过滤设备号
        List<String> craneNos = new ArrayList<>();
        for (ProjectCrane aCraneList1 : craneList) {
            if (null != aCraneList1.getCraneNo() && !"".equals(aCraneList1.getCraneNo())) {
                craneNos.add(aCraneList1.getCraneNo());
            }
        }
        //查询每天的在线设备
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo = new EntityWrapper<>();
        wrapperInfo.setSqlSelect("count(*) as builder", "crane_no as craneNo", "device_no as deviceNo",
                "DATE_FORMAT(work_date,'%Y-%m-%d') as workDate", "project_id as projectId");
        wrapperInfo.gt("lift_frequency", 0);
        //如果按设备查询，根据日期分组，否则按照日期和工程id分组
        if (type != null && type == 1) {
            wrapperInfo.groupBy("DATE_FORMAT(work_date,'%Y-%m-%d')");
        } else {
            wrapperInfo.groupBy("DATE_FORMAT(work_date,'%Y-%m-%d'),project_id");
        }
        //根据org_id过滤
        wrapperInfo.in("crane_no", craneNos);
        List<ProjectCraneStatisticsDaily> craneStaticDailyList = projectCraneStaticsDailyService.selectList(wrapperInfo);
        if (type != null && type == 1) {
            //如果查询时间包含今日
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                int todayOnline = 0;
                //从缓存中获取今日数据，遍历craneList,根据device_no判断今天是否存在在线设备
                for (ProjectCrane crane : craneList) {
                    String key = "device_platform:crane:statistics:" + crane.getDeviceNo();
                    if (redisUtil.exists(key)) {
                        ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                        //如果吊重数>0,则今日在线设备数量+1
                        if (currentDay != null && currentDay.getLiftFrequency() > 0) {
                            todayOnline += 1;
                        }
                    }
                }
                vo = new IsOnlineVO();
                //今日在线设备数量
                vo.setOnlineNumber(todayOnline);
                //日期为今日
                vo.setWorkDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                //设备数量为查到的craneList的长度
                vo.setCount(craneList.size());
                //在线比例
                vo.setOnlineRate(new BigDecimal(todayOnline).divide(new BigDecimal(craneList.size()), 2, RoundingMode.UP).multiply(new BigDecimal(100)).multiply(new BigDecimal("100")));
                isOnlineVOList.add(vo);
            }
            //根据日期创建集合
            Date index = new Date();
            try {
                index = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                index = DateUtil.offsetHour(index, -24);
            }
            while (DateUtil.endOfDay(index).getTime() >= DateUtil.endOfDay(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss")).getTime()) {
                vo = new IsOnlineVO();
                vo.setWorkDate(DateUtil.format(index, "yyyy-MM-dd"));
                isOnlineVOList.add(vo);
                index = DateUtil.offsetHour(index, -24);
            }
            ProjectCraneStatisticsDaily mydaily;
            ProjectCrane crane;
            int startIndex;
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                startIndex = 1;
            } else {
                startIndex = 0;
            }
            for (int i = startIndex; i < isOnlineVOList.size(); i++) {
                vo = isOnlineVOList.get(i);
                vo.setOnlineNumber(0);
                vo.setCount(0);
                //获取该天在线数量
                if (craneStaticDailyList != null && craneStaticDailyList.size() > 0) {
                    for (ProjectCraneStatisticsDaily aCraneStaticDailyList : craneStaticDailyList) {
                        mydaily = aCraneStaticDailyList;
                        if (vo.getWorkDate() != null && mydaily.getWorkDate() != null && DateUtil.endOfDay(DateUtil.parse(vo.getWorkDate(), "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(mydaily.getWorkDate()).getTime()) {
                            System.out.println(vo.getWorkDate());
                            if (mydaily.getBuilder() != null) {
                                vo.setOnlineNumber(mydaily.getBuilder());
                            }
                        }
                    }
                }
                //获取该天设备总数量
                for (ProjectCrane aCraneList : craneList) {
                    crane = aCraneList;
                    if (vo.getWorkDate() != null && crane.getCreateTime() != null && DateUtil.endOfDay(DateUtil.parse(vo.getWorkDate(), "yyyy-MM-dd")).getTime() >= DateUtil.endOfDay(crane.getCreateTime()).getTime()) {
                        vo.setCount(vo.getCount() + 1);
                    }
                }

                //获取在线比例
                vo.setOnlineRate(new BigDecimal(0));
                if (vo.getCount() != 0 && vo.getOnlineNumber() != 0) {
                    //vo.setOnlineRate(Math.round(((float) vo.getOnlineNumber() / (vo.getCount())) * 10000.00) / 100.00);
                    vo.setOnlineRate(new BigDecimal(vo.getOnlineNumber()).multiply(new BigDecimal(100)).divide(new BigDecimal(vo.getCount()), 2, RoundingMode.UP));
                }
            }
            for (IsOnlineVO anIsOnlineVOList : isOnlineVOList) {
                vo = anIsOnlineVOList;
                vo.setWorkDate(vo.getWorkDate().substring(0, 10));
            }

            return new ResultDTO<>(true, isOnlineVOList, "成功");
        } else {
            //获取所有的工程
            Wrapper<ProjectInfo> projectWrapper = new EntityWrapper<>();
            projectWrapper.setSqlSelect("name as name", "id as id");
            projectWrapper.in("org_id", Const.orgIds.get());
            List<ProjectInfo> projectList = iProjectInfoService.selectList(projectWrapper);
            //如果包含当天的
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                int todayOnline;
                int todayCount;
                for (ProjectInfo projectInfo : projectList) {
                    todayOnline = 0;
                    todayCount = 0;
                    for (ProjectCrane crane : craneList) {

                        String key = "device_platform:crane:statistics:" + crane.getDeviceNo();
                        if (redisUtil.exists(key)) {
                            ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                            if (currentDay != null && currentDay.getLiftFrequency() > 0 && projectInfo.getId().intValue() == crane.getProjectId().intValue()) {
                                todayOnline += 1;
                            }
                        }
                    }
                    //获取该天设备总数量
                    for (ProjectCrane crane : craneList) {
                        if (projectInfo.getId().intValue() == crane.getProjectId().intValue() && DateUtil.endOfDay(new Date()).getTime() >= DateUtil.endOfDay(crane.getCreateTime()).getTime()) {
                            todayCount++;
                        }
                    }
                    vo = new IsOnlineVO();
                    //工程编号
                    vo.setProjectId(projectInfo.getId());
                    //工程名称
                    vo.setProjectName(projectInfo.getName());
                    //在线数量
                    vo.setOnlineNumber(todayOnline);
                    vo.setWorkDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    //总数量
                    vo.setCount(todayCount);
                    //vo.setOnlineRate(Math.round((((float) todayOnline*1.00) / todayCount)*100.00)/100.00);
                    vo.setOnlineRate(new BigDecimal(todayOnline).multiply(new BigDecimal(100)).divide(new BigDecimal(todayCount), 2, RoundingMode.UP));
                    isOnlineVOList.add(vo);
                }
            }
            int beginIndex = isOnlineVOList.size();
            Date index = new Date();
            ProjectInfo projectInfo;
            try {
                index = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss")).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                index = DateUtil.offsetHour(index, -24);
            }
            //按照日期和工程生成对象
            while (DateUtil.endOfDay(index).getTime() >= DateUtil.endOfDay(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss")).getTime()) {
                for (ProjectInfo aProjectList : projectList) {
                    projectInfo = aProjectList;
                    vo = new IsOnlineVO();
                    vo.setProjectId(projectInfo.getId());
                    vo.setProjectName(projectInfo.getName());
                    vo.setWorkDate(DateUtil.format(index, "yyyy-MM-dd"));
                    isOnlineVOList.add(vo);
                }
                index = DateUtil.offsetHour(index, -24);
            }
            System.out.println(isOnlineVOList);
            ProjectCraneStatisticsDaily mydaily;
            ProjectCrane crane;
            for (int i = beginIndex; i < isOnlineVOList.size(); i++) {

                vo = isOnlineVOList.get(i);
                vo.setOnlineNumber(0);
                vo.setCount(0);
                //获取该天在线数量
                // if (DateUtil.endOfDay(sDateFormat.parse(endTime)).getTime() == DateUtil.endOfDay(new Date()).getTime()) {
                for (ProjectCraneStatisticsDaily aCraneStaticDailyList : craneStaticDailyList) {
                    mydaily = aCraneStaticDailyList;
                    if (vo.getWorkDate() != null && mydaily.getWorkDate() != null && DateUtil.endOfDay(DateUtil.parse(vo.getWorkDate(), "yyyy-MM-dd")).getTime() == DateUtil.endOfDay(mydaily.getWorkDate()).getTime() && mydaily.getProjectId().intValue() == vo.getProjectId().intValue()) {
                        vo.setOnlineNumber(mydaily.getBuilder());
                    }
                }

                //获取该天设备总数量
                for (ProjectCrane aCraneList : craneList) {
                    crane = aCraneList;
                    if (vo.getWorkDate() != null && crane.getCreateTime() != null && vo.getProjectId().intValue() == crane.getProjectId().intValue() && DateUtil.endOfDay(DateUtil.parse(vo.getWorkDate(), "yyyy-MM-dd")).getTime() >= DateUtil.endOfDay(crane.getCreateTime()).getTime()) {
                        vo.setCount(vo.getCount() + 1);
                    }
                }
                //获取在线比例
                vo.setOnlineRate(new BigDecimal(0));
                if (vo.getCount() != 0 && vo.getOnlineNumber() != 0) {
                    //vo.setOnlineRate(Math.round(((float) vo.getOnlineNumber() / vo.getCount()) * 10000.00) / 100.00);
                    vo.setOnlineRate(new BigDecimal(vo.getOnlineNumber()).
                            divide(new BigDecimal(vo.getCount()), 2, RoundingMode.UP).multiply(new BigDecimal(100)));
                }
            }
            for (IsOnlineVO anIsOnlineVOList : isOnlineVOList) {
                vo = anIsOnlineVOList;
                System.out.println(vo.getWorkDate());
                vo.setWorkDate(vo.getWorkDate().substring(0, 10));
            }
            return new ResultDTO<>(true, isOnlineVOList, "成功");
        }
    }


    /**
     * 导出设备在线统计Excel接口
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 1：按设备 2 按项目
     * @param response 响应信息
     */
    @Override
    public void selectIsOnlineExcel(String startTime, String endTime, Integer type, HttpServletResponse response) {
        RequestDTO<IsOnlineVO> requestDTO = new RequestDTO<>();
        IsOnlineVO isOnlineVO = new IsOnlineVO();
        isOnlineVO.setType(type);
        requestDTO.setStartTime(startTime);
        requestDTO.setEndTime(endTime);
        requestDTO.setBody(isOnlineVO);
        ResultDTO<List<IsOnlineVO>> resultDTO = selectIsOnline(requestDTO);
        System.out.println("ok");
        List<IsOnlineVO> isOnlineVOList = resultDTO.getData();
        List<String> row;
        List<List<String>> rows = new ArrayList<>();
        row = new ArrayList<>();
        row.add("日期");
        if (type != null && type == 2) {
            row.add("工程名称");
        }
        row.add("在线数量");

        row.add("总数量");
        row.add("在线率(%)");
        rows.add(row);
        IsOnlineVO vo;
        for (IsOnlineVO anIsOnlineVOList : isOnlineVOList) {
            row = new ArrayList<>();
            vo = anIsOnlineVOList;
            if (null != vo.getWorkDate()) {
                row.add(vo.getWorkDate());
            } else {
                row.add("");
            }
            if (type != null && type == 2) {

                addExcel(row,vo.getProjectName());
            }
            if (null != vo.getOnlineNumber()) {
                row.add(String.valueOf(vo.getOnlineNumber()));
            } else {
                row.add("");
            }
            row.add(String.valueOf(vo.getCount()));
            row.add(String.valueOf(vo.getOnlineRate()));
            System.out.println(vo);
            rows.add(row);
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
        String code = getCode("设备在线统计");// URLEncoder.encode("设备在线统计");
        createExcel(response, rows, filename, code + ".xls");
    }

    /**
     * 设备台账
     *
     * @param requestDTO 请求参数
     * @return 返回的页面数据
     */
    @Override
    public ResultDTO<DataDTO<List<GetInfoVO>>> getDeviceAccount(@RequestBody RequestDTO<GetInfoVO> requestDTO) {
        Map<String, Object> map = new HashMap<>(10);
        Page<ProjectCraneStaticsticsDailyVO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<ProjectCraneStaticsticsDailyVO> craneStaticDailyList = projectCraneStaticsDailyService.selectPageList(page, map);
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
     * 塔吊首页接口
     * @param requestDTO 请求参数
     * @return 返回的页面请求数据
     */
    @Override
    public ResultDTO<DataDTO<List<TowerIndexVO>>> towerIndex(@RequestBody RequestDTO<TowerIndexVO> requestDTO) {
        Wrapper<ProjectCrane> craneWrapperInfo = new EntityWrapper<>();
        craneWrapperInfo.setSqlSelect("crane_no as craneNo");
        craneWrapperInfo.in("org_id", Const.orgIds.get());
        craneWrapperInfo.eq("is_del", 0);
        List<ProjectCrane> craneList = projectCraneService.selectList(craneWrapperInfo);
        List<String> craneNos = new ArrayList<>();
        for (ProjectCrane aCraneList : craneList) {
            if (null != aCraneList.getCraneNo() && !"".equals(aCraneList.getCraneNo())) {
                craneNos.add(aCraneList.getCraneNo());
            }
        }
        String param = requestDTO.getBody().getParam();
        Page page = new Page(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<TowerIndexVO> ll = projectCraneMapper.selectTowerIndex(page, param);
        System.out.println(ll);
        TowerIndexVO vo;
        Wrapper<ProjectCraneStatisticsDaily> wrapperInfo;
        ProjectCraneStatisticsDaily daily;
        String keyPre = "device_platform:crane:statistics:";
        BigDecimal monthDay = new BigDecimal("30");
        BigDecimal liftFrequency;
        BigDecimal dayAccount;
        DateTime offlineTime;
        long hours;
        long mins;
        long seconds;
        for (TowerIndexVO aLl : ll) {
            wrapperInfo = new EntityWrapper<>();
            vo = aLl;
            //违章次数
            vo.setWeightAlarm(0);
            wrapperInfo.setSqlSelect("sum(lift_frequency) as liftFrequency",
                    "device_no as deviceNo",
                    "sum(weight_alarm) as weightAlarm",
                    "(count(1)+1) as dayAccount");
            wrapperInfo.eq(vo.getCraneNo() != null, "crane_no", vo.getCraneNo());
            wrapperInfo.in("crane_no", craneNos);
            wrapperInfo.groupBy("crane_no");
            daily = projectCraneStaticsDailyService.selectOne(wrapperInfo);
            if (daily != null) {
                String key = keyPre + daily.getDeviceNo();
                vo.setWeightAlarm(vo.getWeightAlarm() + daily.getWeightAlarm());
                if (redisUtil.exists(key)) {
                    ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
                    vo.setWeightAlarm(vo.getWeightAlarm() + currentDay.getWeightAlarm());
                    liftFrequency = new BigDecimal(daily.getLiftFrequency() + currentDay.getLiftFrequency());
                    dayAccount = new BigDecimal(daily.getDayAccount());
                } else {
                    liftFrequency = new BigDecimal(daily.getLiftFrequency());
                    dayAccount = new BigDecimal(daily.getDayAccount());
                }
                vo.setFrequencyMonth(liftFrequency.divide(dayAccount, 2, RoundingMode.HALF_UP).multiply(monthDay).floatValue());
                //离线时长
                if (vo.getAssembleDate() != null) {
                    offlineTime = DateUtil.parse(vo.getAssembleDate(), "yyyy-MM-dd HH:mm:ss");
                    hours = DateUtil.between(offlineTime, new Date(), DateUnit.HOUR);
                    mins = DateUtil.between(offlineTime, new Date(), DateUnit.MINUTE) - hours * 60;
                    seconds = DateUtil.between(offlineTime, new Date(), DateUnit.SECOND) - hours * 60 * 60 - mins * 60;
                    vo.setAssembleDate(hours + "时" + mins + "分" + seconds + "秒");
                } else {
                    vo.setAssembleDate("-时-分-秒");
                }
            }
        }
        return new ResultDTO<>(true, DataDTO.factory(ll, page.getTotal()));
    }


}


