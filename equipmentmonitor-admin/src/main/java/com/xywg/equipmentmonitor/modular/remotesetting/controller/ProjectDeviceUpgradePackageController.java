package com.xywg.equipmentmonitor.modular.remotesetting.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xywg.equipmentmonitor.modular.remotesetting.service.ProjectDeviceUpgradePackageService;
import com.xywg.equipmentmonitor.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2018-09-30
 */
@RestController
@RequestMapping("/ssdevice/remotesetting/projectDeviceUpgradePackage")
public class ProjectDeviceUpgradePackageController extends BaseController<ProjectDeviceUpgradePackage, ProjectDeviceUpgradePackageService> {

    @Override
    public String insertRole() {
        return null;
    }

    @Override
    public String updateRole() {
        return null;
    }

    @Override
    public String deleteRole() {
        return null;
    }

    @Override
    public String viewRole() {
        return null;
    }

    @ApiOperation("条件分页查询")
    @PostMapping("/getPageList")
    public ResultDTO<DataDTO<List<ProjectDeviceUpgradePackageVO>>> getPageList(@RequestBody RequestDTO request) {
        return service.getPageList(request);
    }

    @ApiOperation("新增")
    @PostMapping("/insertModel")
    public ResultDTO<Object> insertModel(@RequestBody RequestDTO<List<ProjectDeviceUpgradePackageVO>> request) {
        try {
            List<String> packageNameList = new ArrayList<>();
            List<ProjectDeviceUpgradePackageVO> listVo = request.getBody();
            if (listVo == null || listVo.size() == 0) {
                return new ResultDTO<>(false, null, "空数据无须操作");
            }

            for (ProjectDeviceUpgradePackageVO deviceUpgradePackageVO : listVo) {
                if (deviceUpgradePackageVO.getUseType() == null ) {
                    return new ResultDTO<>(false, null, "类型必填");
                }
                String fileName=deviceUpgradePackageVO.getName();
                boolean b=fileName.contains("_")&&fileName.contains(".");
                if(!b){
                    return new ResultDTO<>(false, null, "文件名不符合规范,示例:xx_xxx_vx.x.x.bin");
                }
                if(packageNameList.contains(deviceUpgradePackageVO.getName())){
                    return new ResultDTO<>(false, null, "包名不允许重复");
                }
                packageNameList.add(deviceUpgradePackageVO.getName());
            }
            //查询包名是否重复
            for(String packageName :packageNameList){
                EntityWrapper<ProjectDeviceUpgradePackage> ew = new EntityWrapper<>();
                ew.eq("is_del", 0);
                ew.eq("name",packageName);
                List<ProjectDeviceUpgradePackage> dbList = service.selectList(ew);
                if(dbList.size()>0){
                    return new ResultDTO<>(false,null,"包名:"+packageName+"已存在");
                }
            }

            List<ProjectDeviceUpgradePackage> list = new ArrayList<>();
            for (ProjectDeviceUpgradePackageVO vo : listVo) {
                vo.setType(vo.getUseType().toString());
                ProjectDeviceUpgradePackage packages = new ProjectDeviceUpgradePackage();
                String fileName=vo.getName();
                vo.setVersion(fileName.substring(fileName.lastIndexOf("_")+1,fileName.lastIndexOf(".")));
                BeanUtils.copyProperties(vo, packages);
                list.add(packages);
            }
            return new ResultDTO<>(true, service.insertInfo(list));
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }


    @ApiOperation("条件分页查询设备")
    @PostMapping("/getDeviceList")
    public ResultDTO<DataDTO<List<ProjectDeviceUpgrade>>> getDeviceList(@RequestBody RequestDTO request) {
        return service.getDeviceList(request);
    }

    @ApiOperation("新增设备升级数据")
    @PostMapping("/insertDeviceUpgrade")
    public ResultDTO<Object> insertDeviceUpgrade(@RequestBody RequestDTO request) {
        try {
            Integer upgradeId = request.getUpgradeId();
            String deviceIds = request.getDeviceIds();
            return service.insertDeviceUpgrade(upgradeId, deviceIds);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

}

