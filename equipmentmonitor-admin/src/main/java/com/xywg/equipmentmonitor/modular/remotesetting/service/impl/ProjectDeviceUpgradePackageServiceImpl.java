package com.xywg.equipmentmonitor.modular.remotesetting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.HttpUtils;
import com.xywg.equipmentmonitor.modular.remotesetting.dao.ProjectDeviceUpgradePackageMapper;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xywg.equipmentmonitor.modular.remotesetting.service.ProjectDeviceUpgradePackageService;
import com.xywg.equipmentmonitor.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public class ProjectDeviceUpgradePackageServiceImpl extends ServiceImpl<ProjectDeviceUpgradePackageMapper, ProjectDeviceUpgradePackage> implements ProjectDeviceUpgradePackageService {
    @Value("${iot.url}")
    String iotUrl;

    /**
     * 条件分页查询
     *
     * @param request
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectDeviceUpgradePackageVO>>> getPageList(RequestDTO request) {
        Page<ProjectDeviceUpgradePackageVO> page = new Page<>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.like("a.version", request.getKey());
        }
        List<ProjectDeviceUpgradePackageVO> list = baseMapper.getPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     * @param list
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectDeviceUpgradePackage>>> insertInfo(List<ProjectDeviceUpgradePackage> list) {
        for (ProjectDeviceUpgradePackage p : list) {
            baseMapper.insert(p);
        }
        return new ResultDTO<>(true);
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectDeviceUpgrade>>> getDeviceList(RequestDTO request) {
        Page<ProjectDeviceUpgrade> page = new Page<>(request.getPageNum(), request.getPageSize());
        String type = request.getType();
        List<String> listTableName = getTableName(type);

        if (StringUtils.isNotBlank(listTableName.get(0))) {
            List<ProjectDeviceUpgrade> list = baseMapper.getDeviceList(page, listTableName.get(0), listTableName.get(1), request.getKey());
            return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
        } else {
            return new ResultDTO<>(true, DataDTO.factory(null, page.getTotal()));
        }
    }

    @Override
    public ResultDTO<Object> insertDeviceUpgrade(Integer id, String ids) {
        //设备id
        String[] idList = ids.split(",");
        //升级包信息
        ProjectDeviceUpgradePackage upgradePackage = baseMapper.selectById(id);
        List<String> listTableName = getTableName(upgradePackage.getType());
        List<ProjectDeviceUpgrade> projectDeviceUpgradeList = new ArrayList<>();
        //设备编号字符串
        StringBuilder devBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(listTableName.get(0))) {
            List<ProjectDeviceUpgrade> list = baseMapper.getDeviceListNoPage(listTableName.get(0), listTableName.get(1), idList);
            for (ProjectDeviceUpgrade deviceUpgrade : list) {
                deviceUpgrade.setPath(upgradePackage.getPath());
                deviceUpgrade.setVersion(upgradePackage.getVersion());
                deviceUpgrade.setFlag(0);
                deviceUpgrade.setCreateUser(Const.currUser.get().getId());
                projectDeviceUpgradeList.add(deviceUpgrade);
                devBuilder.append(deviceUpgrade.getDeviceNo()).append(",");
            }
        }

        //localhost:9091/ssdevice/activeIssued/upgradePackage
        String url = "http://" + iotUrl + "/ssdevice/activeIssued/upgradePackage";
        Map<String, String> paramMap = new HashMap<>(2);
        paramMap.put("codes", devBuilder.toString());
        paramMap.put("path", upgradePackage.getPath());
        String data = JSONObject.toJSONString(paramMap);
        //通知设备
        String res = HttpUtils.sendPost(url, data);

        String flag = "error";
        StringBuilder resStringBuilder = new StringBuilder();
        List<ProjectDeviceUpgrade> dbList = new ArrayList<>();
        if (!flag.equals(res)) {
            //已经成功发送指令的设备
            String[] devCode = res.split(",");
            for (ProjectDeviceUpgrade devUpgrade : projectDeviceUpgradeList) {
                boolean f = Arrays.asList(devCode).contains(devUpgrade.getDeviceNo());
                if (f) {
                    dbList.add(devUpgrade);
                } else {
                    resStringBuilder.append(devUpgrade.getDeviceNo()).append(",");
                }
            }
            if (dbList.size() > 0) {
                //插入到相关升级履历表
                baseMapper.insertUpgradeRecord(listTableName.get(1), dbList);
            }

            if (StringUtils.isNotBlank(resStringBuilder.toString())) {
                return new ResultDTO<>(true, null, "操作成功," + resStringBuilder.toString()
                        + "设备不在线,无法发送指令");
            } else {
                return new ResultDTO<>(true, null, "操作成功");
            }
        } else {
            return new ResultDTO<>(false, null, "下发重启指令失败");
        }
    }


    private List<String> getTableName(String type) {
        String mainTableName = "";
        String subtablesName = "";
        switch (type) {
            case "1":
                //塔吊
                mainTableName = "t_project_crane";
                subtablesName = "t_project_crane_upgrade_record";
                break;
            case "2":
                //升降机
                mainTableName = "t_project_lift";
                subtablesName = "t_project_lift_upgrade_record";
                break;
            case "3":
                //扬尘
                mainTableName = "t_project_environment_monitor";
                subtablesName = "t_project_environment_monitor_upgrade_record";
                break;
            default:
                break;
        }
        List<String> list = new ArrayList<>();
        list.add(mainTableName);
        list.add(subtablesName);
        return list;
    }
}
