package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.aop.ZbusProducerHolder;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.dto.BindHelmetVO;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectHelmetStockMapper;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectCraneDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectEnvironmentMonitorDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectLiftDTO;
import com.xywg.equipmentmonitor.modular.device.model.*;
import com.xywg.equipmentmonitor.modular.device.service.*;
import com.xywg.equipmentmonitor.modular.device.vo.*;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDevice;
import com.xywg.equipmentmonitor.modular.station.service.IProjectDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 安全帽 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Service
public class ProjectHelmetStockServiceImpl extends ServiceImpl<ProjectHelmetStockMapper, ProjectHelmetStock> implements ProjectHelmetStockService {
    @Autowired
    private IProjectHelmetService projectHelmetService;
    @Autowired
    private IProjectDeviceService projectDeviceService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ProjectEnvironmentMonitorService projectEnvironmentMonitorService;
    @Autowired
    ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private ProjectCraneService projectCraneService;
    @Autowired
    private IProjectLiftService projectLiftService;
    @Autowired
    private ProjectElectricPowerService projectElectricPowerService;
    @Autowired
    private IProjectWaterMeterService projectWaterMeterService;

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmetStock>>> selectPageList(RequestDTO<ProjectHelmetStock> res) {
        Page<ProjectHelmetStock> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectHelmetStock>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        if (StringUtils.isNotBlank(res.getType())) {
            ew.eq("type", res.getType());
        }
        if (StringUtils.isNotBlank(res.getKey())) {
            ew.like("a.device_no", res.getKey());
        }
        List<ProjectHelmetStock> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> stockOut(RequestDTO<ProjectHelmetStock> res) {
        ProjectHelmetStock helmetStock = res.getBody();
        String[] imeis = helmetStock.getDeviceNo().split(",");
        if (imeis.length == 0) {
            return new ResultDTO<>(true, null, "出库成功");
        }
        //修改库存表状态
        baseMapper.updateStatus(1, Arrays.asList(imeis), Const.currUser.get().getId(), helmetStock.getType());

        handleOutStock(helmetStock);
        return new ResultDTO<>(true, null, "出库成功");
    }

    @Override
    public ResultDTO<Object> fileUpload(MultipartFile multipartFile) {
        try {
            InputStream fileInputStream = multipartFile.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(fileInputStream);
            List<List<Object>> tempList = reader.read();
            if (tempList.size() < 2) {
                return new ResultDTO<>(false, null, "文件不规范,请下载模板后,在模板上填写");
            }
            //设备类型
            Integer type = Integer.parseInt(tempList.get(0).get(0).toString().substring(0, 1));
            if (type == 0) {
                return new ResultDTO<>(false, null, "文件中第一行第一格的设备类型没有选择");
            }

            Set<String> imeiList = new HashSet<>();
            Set<String> gprsList = new HashSet<>();

            List<ProjectHelmetStock> toDbList = new ArrayList<>();
            //从第二行开始
            for (int i = 1; i < tempList.size(); i++) {
//                for (Object imei : tempList.get(i)) {
                Object  imei =tempList.get(i).get(0);
                Object  gprs = tempList.get(i).get(1);
                if(StringUtils.isBlank(imei.toString()) || StringUtils.isBlank(gprs.toString())){
                    return new ResultDTO<>(false, null, "文件中设备编号和SIM卡都是必填");
                }
                    if (StringUtils.isNotBlank(imei.toString())) {
                        if (imei.toString().trim().length() > 15) {
                            return new ResultDTO<>(false, null, "文件中'" + imei.toString() + "'不符合规范,最长15位");
                        }
                        boolean b = isSpecialChar(imei.toString());
                        if (!b) {
                            return new ResultDTO<>(false, null, "文件中'" + imei.toString() + "'不符合规范,只能由数字或者字母组成");
                        }
                        imeiList.add(imei.toString());

                        ProjectHelmetStock projectHelmetStock = new ProjectHelmetStock();
                        if(StringUtils.isNotBlank(gprs.toString())){
                            if (gprs.toString().trim().length() > 20) {
                                return new ResultDTO<>(false, null, "文件中'" + gprs.toString() + "'不符合规范,最长20位");
                            }

                            String regEx = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
                            Pattern pattern = Pattern.compile(regEx);
                           boolean isNum = pattern.matcher(gprs.toString()).find();

                           if(!isNum){
                               return new ResultDTO<>(false, null, "文件中'" + gprs.toString() + "'不符合规范,只能由数字组成");
                           }
                            projectHelmetStock.setGprs(gprs.toString());
                            gprsList.add(gprs.toString());
                        }
                        projectHelmetStock.setStatus(0);
                        projectHelmetStock.setDeviceNo(imei.toString());
                        projectHelmetStock.setIsDel(0);
                        projectHelmetStock.setType(type);
                        toDbList.add(projectHelmetStock);
                    }
//                }
            }
            if (imeiList.size() == 0) {
                return new ResultDTO<>(false, null, "上传的文件中没有有效的设备编号");
            }

            EntityWrapper<ProjectHelmetStock> ew = new EntityWrapper<>();
            ew.eq("is_del", 0);
            ew.in("device_no", imeiList);
            List<ProjectHelmetStock> listProjectHelmetStockFile = baseMapper.selectList(ew);
             ew = new EntityWrapper<>();
            ew.eq("is_del", 0);
            ew.in("gprs", gprsList);
            List<ProjectHelmetStock> stocks = baseMapper.selectList(ew);
            boolean isRepeat = false;
            StringBuilder sb = new StringBuilder("设备号:");
            if (listProjectHelmetStockFile.size() > 0) {
                isRepeat =true;

                for (ProjectHelmetStock projectHelmetStock : listProjectHelmetStockFile) {
                    sb.append(projectHelmetStock.getDeviceNo()).append(",");
                }


            }
            if (stocks.size()>0){
                sb.append("SIM卡:");
                isRepeat =true;
                for(ProjectHelmetStock stock:stocks){
                    sb.append(stock.getGprs()).append(",");
                }
            }
            if(isRepeat){
                sb.append("已被入库,请勿重复操作");
                return new ResultDTO<>(false, null, sb.toString());
            }

            this.insertBatch(toDbList);
            return new ResultDTO<>(true, null, "批量入库成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultDTO<>(false, null, "操作异常,请刷新重试!");
        }
    }


    /**
     * 校验是否包含特殊字符
     *
     * @return
     */
    public static boolean isSpecialChar(String imei) {
        //String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        String regEx = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(imei);
        return matcher.find();
    }


    /**
     * 根据类型进行相关操作
     *
     * @param stock
     * @return
     */
    private void handleOutStock(ProjectHelmetStock stock) {
        switch (stock.getType()) {
            //设备类型 1安全帽 2基站 3标签 4扬尘 5塔吊 6升降机 7电表 8水表
            case 1:
                projectHelmetOut(stock);
                break;
            case 2:
                projectDeviceOut(stock);
                break;
            case 3:
                projectLabelDeviceOut(stock);
                break;
            case 4:
                projectEnvironmentMonitor(stock);
                break;
            case 5:
                projectCraneOut(stock);
                break;
            case 6:
                projectLiftOut(stock);
                break;
            case 7:
                projectElectricPowerOut(stock);
                break;
            case 8:
                projectWaterOut(stock);
                break;
            default:
                break;
        }
    }

    /**
     * 安全帽出库
     *
     * @param helmetStock
     */
    private void projectHelmetOut(ProjectHelmetStock helmetStock) {
        String[] imeis = helmetStock.getDeviceNo().split(",");
        List<ProjectHelmet> list = new ArrayList<>();
        for (String deviceNo : imeis) {
            ProjectHelmet helmet = new ProjectHelmet();
            helmet.setComments(helmetStock.getComments());
            helmet.setImei(deviceNo);
            helmet.setIsOnline(0);
            helmet.setStatus(0);
            helmet.setCurrentFlag(0);
            helmet.setProjectId(helmetStock.getProjectId());
            helmet.setIsDel(0);
            list.add(helmet);
        }
        projectHelmetService.insertBatch(list);

    }

    /**
     * 插入到标签基站设备管理表
     *
     * @param helmetStock
     */
    private void projectDeviceOut(ProjectHelmetStock helmetStock) {
        String[] imeis = helmetStock.getDeviceNo().split(",");
        List<ProjectDevice> list = new ArrayList<>();
        for (String deviceNo : imeis) {
            ProjectDevice helmet = new ProjectDevice();
            helmet.setComments(helmetStock.getComments());
            helmet.setDeviceNo(deviceNo);
            //标签出库时自动启用1
            helmet.setStatus(1);
            helmet.setCurrentFlag(0);
            helmet.setProjectId(helmetStock.getProjectId());
            helmet.setIsDel(0);
            helmet.setType(helmetStock.getType());
            list.add(helmet);

        }
        projectDeviceService.insertBatch(list);
    }
    /**
     * 插入到基站设备管理表
     *
     * @param helmetStock
     */
    private void projectLabelDeviceOut(ProjectHelmetStock helmetStock) {
        ProjectInfo projectInfo = projectInfoService.selectById(helmetStock.getProjectId());
        String uuid = projectInfo.getUuid();
        String[] imeis = helmetStock.getDeviceNo().split(",");
        for (String deviceNo : imeis) {
            ProjectDevice helmet = new ProjectDevice();
            helmet.setComments(helmetStock.getComments());
            helmet.setDeviceNo(deviceNo);
            //标签出库时自动启用1
            helmet.setStatus(1);
            helmet.setCurrentFlag(0);
            helmet.setProjectId(helmetStock.getProjectId());
            helmet.setIsDel(0);
            helmet.setType(helmetStock.getType());
            try {
                zbusProducerHolder.bindHelmet(uuid,new BindHelmetVO(uuid,1,deviceNo));
                projectDeviceService.insert(helmet);
            } catch (Exception e) {
                if(!"Missing MqClient for publishing message".startsWith(e.getMessage())){
                    projectDeviceService.insert(helmet);
                }
                e.printStackTrace();
            }

        }
    }
    /**
     * 扬尘设备出库
     *
     * @param stock
     */
    private void projectEnvironmentMonitor(ProjectHelmetStock stock) {
        String[] deviceNoArr = stock.getDeviceNo().split(",");
     //   List<ProjectEnvironmentMonitor> list = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoService.selectById(stock.getProjectId());
        String uuid = projectInfo.getUuid();
        for (String deviceNo : deviceNoArr) {
            ProjectEnvironmentMonitor monitor = new ProjectEnvironmentMonitor();
            monitor.setStatus(0);
            monitor.setIsDel(0);
            monitor.setIsOnline(0);
            monitor.setDeviceNo(deviceNo);
            monitor.setProjectId(stock.getProjectId());
            monitor.setOrgId(projectInfo.getOrgId());

          //  list.add(monitor);

            projectEnvironmentMonitorService.insert(monitor);
            if (!StringUtils.isBlank(uuid)) {
                ProjectEnvironmentMonitorDTO environmentMonitorDTO = new ProjectEnvironmentMonitorDTO();
                ProjectEnvironmentMonitorVO environmentMonitorVO = new ProjectEnvironmentMonitorVO();
                environmentMonitorVO.setStatus(0);
                environmentMonitorVO.setIsOnline(0);
                environmentMonitorVO.setProjectId(stock.getProjectId());
                environmentMonitorVO.setDeviceNo(deviceNo);
                environmentMonitorVO.setProjectName(projectInfo.getName());
                environmentMonitorVO.setId(monitor.getId());
                environmentMonitorDTO.setMonitor(environmentMonitorVO);
                //推送
                try {
                    zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(environmentMonitorDTO), "add", "monitor");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 塔吊出库
     */
    private void projectCraneOut(ProjectHelmetStock stock) {
        String[] deviceNoArr = stock.getDeviceNo().split(",");
      //  List<ProjectCrane> list = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoService.selectById(stock.getProjectId());
        String uuid = projectInfo.getUuid();
        for (String deviceNo : deviceNoArr) {
            ProjectCrane projectCrane = new ProjectCrane();
            projectCrane.setStatus(0);
            projectCrane.setIsDel(0);
            projectCrane.setIsOnline(0);
            projectCrane.setDeviceNo(deviceNo);
            projectCrane.setProjectId(stock.getProjectId());
            projectCrane.setOrgId(projectInfo.getOrgId());
         //   list.add(projectCrane);

            projectCraneService.insert(projectCrane);
            if (!StringUtils.isBlank(uuid)) {
                ProjectCraneDTO projectCraneDTO = new ProjectCraneDTO();
                ProjectCraneVO craneVO = new ProjectCraneVO();
                craneVO.setStatus(0);
                craneVO.setIsOnline(0);
                craneVO.setProjectId(stock.getProjectId());
                craneVO.setOrgId(projectInfo.getOrgId());
                craneVO.setDeviceNo(deviceNo);
                craneVO.setId(projectCrane.getId());
                projectCraneDTO.setCrane(craneVO);
                try {
                    zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(projectCraneDTO), "add", "crane");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
     //   projectCraneService.insertBatch(list);
    }

    /**
     * 升降机出库
     */
    private void projectLiftOut(ProjectHelmetStock stock) {
        String[] deviceNoArr = stock.getDeviceNo().split(",");
   //     List<ProjectLift> list = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoService.selectById(stock.getProjectId());
        String uuid = projectInfo.getUuid();
        for (String deviceNo : deviceNoArr) {
            ProjectLift lift = new ProjectLift();
            lift.setStatus(0);
            lift.setIsDel(0);
            lift.setIsOnline(0);
            lift.setDeviceNo(deviceNo);
            lift.setProjectId(stock.getProjectId());
            lift.setOrgId(projectInfo.getOrgId());

     //       list.add(lift);

            projectLiftService.insert(lift);
            if (!StringUtils.isBlank(uuid)) {
                ProjectLiftDTO liftDTO = new ProjectLiftDTO();
                ProjectLiftVO liftVO = new ProjectLiftVO();
                liftVO.setStatus(0);
                liftVO.setIsOnline(0);
                liftVO.setProjectId(stock.getProjectId());
                liftVO.setOrgId(projectInfo.getOrgId());
                liftVO.setDeviceNo(deviceNo);
                liftVO.setId(lift.getId());
                liftDTO.setLift(liftVO);
                try {
                    zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(liftDTO), "add", "lift");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
      //  projectLiftService.insertBatch(list);
    }

    /**
     * 电表出库
     */
    private void projectElectricPowerOut(ProjectHelmetStock stock) {
        String[] deviceNoArr = stock.getDeviceNo().split(",");
      //  List<ProjectElectricPower> list = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoService.selectById(stock.getProjectId());
        String uuid = projectInfo.getUuid();
        for (String deviceNo : deviceNoArr) {
            ProjectElectricPower power = new ProjectElectricPower();
            power.setStatus(0);
            power.setIsDel(0);
            power.setIsOnline(0);
            power.setDeviceNo(deviceNo);
            power.setProjectId(stock.getProjectId());
            power.setOrgId(projectInfo.getOrgId());
          //  list.add(power);

            projectElectricPowerService.insert(power);
            if (!StringUtils.isBlank(uuid)) {
                try {
                    zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(power), "add", "elec");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
       // projectElectricPowerService.insertBatch(list);
    }


    /**
     * 水表出库
     */
    private void projectWaterOut(ProjectHelmetStock stock) {
        String[] deviceNoArr = stock.getDeviceNo().split(",");
       // List<ProjectWaterMeter> list = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoService.selectById(stock.getProjectId());
        String uuid = projectInfo.getUuid();
        for (String deviceNo : deviceNoArr) {
            ProjectWaterMeter water = new ProjectWaterMeter();
            water.setStatus(0);
            water.setIsDel(0);
            water.setIsOnline(0);
            water.setDeviceNo(deviceNo);
            water.setProjectId(stock.getProjectId());
            water.setOrgId(projectInfo.getOrgId());
            projectWaterMeterService.insert(water);
         //   list.add(water);
            if (!StringUtils.isBlank(uuid)) {
                try {
                    zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(water), "add", "water");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
       // projectWaterMeterService.insertBatch(list);
    }


}
