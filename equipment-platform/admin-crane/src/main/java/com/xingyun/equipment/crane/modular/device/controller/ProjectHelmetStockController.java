package com.xingyun.equipment.crane.modular.device.controller;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetStock;
import com.xingyun.equipment.crane.modular.device.service.ProjectHelmetStockService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 库存管理
 *
 * @author : HJY
 */
@RestController
@RequestMapping("admin-crane/device/helmetStock")
public class ProjectHelmetStockController extends BaseController<ProjectHelmetStock, ProjectHelmetStockService> {


    /**
     * 绿色施工监控一览
     *
     * @param res
     * @return
     */
    @ApiOperation("安全帽库存一览")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectHelmetStock>>> selectPageList(@RequestBody RequestDTO<ProjectHelmetStock> res) {
        return service.selectPageList(res);
    }


    /**
     * 入库
     *
     * @param res
     * @return
     */
    @PostMapping("stockPut")
    @ApiOperation("入库")
    ResultDTO<Object> stockPut(@RequestBody RequestDTO<ProjectHelmetStock> res) {
        ProjectHelmetStock projectHelmetStock = res.getBody();
        projectHelmetStock.setStatus(0);
        projectHelmetStock.setType(projectHelmetStock.getType());
        EntityWrapper<ProjectHelmetStock> ew = new EntityWrapper<>();
        ew.eq("device_no", projectHelmetStock.getDeviceNo());
        ew.eq("is_del", 0);
        List<ProjectHelmetStock> list = service.selectList(ew);
        if (list.size() > 0) {
            return new ResultDTO<>(false, null, "新增入库失败,设备已存在");
        }
        service.insert(projectHelmetStock);
        return new ResultDTO<>(true, null, "入库成功");
    }

    /**
     * 出库
     *
     * @param res
     * @return
     */
    @PostMapping("stockOut")
    @ApiOperation("出库")
    ResultDTO<Object> stockOut(@RequestBody RequestDTO<ProjectHelmetStock> res) {
        return service.stockOut(res);
    }

    /**
     * 删除库存
     *
     * @param res
     * @return
     */
    @PostMapping("deleteStock")
    @ApiOperation("删除库存")
    ResultDTO<Object> deleteStock(@RequestBody RequestDTO<ProjectHelmetStock> res) {
        List ids = res.getIds();
        service.deleteBatchIds(ids);
        return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
    }

    /**
     * 文件上传(批量入库)
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    public ResultDTO fileUpload(@RequestParam(value = "file") MultipartFile uploadFile) {

        return service.fileUpload(uploadFile);
    }


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


}
