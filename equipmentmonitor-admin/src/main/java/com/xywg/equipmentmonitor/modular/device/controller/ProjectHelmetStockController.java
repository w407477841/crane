package com.xywg.equipmentmonitor.modular.device.controller;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetStock;
import com.xywg.equipmentmonitor.modular.device.service.ProjectHelmetStockService;
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
@RequestMapping("ssdevice/device/helmetStock")
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
        ew = new EntityWrapper<>();
        ew.eq("gprs", projectHelmetStock.getGprs());
        ew.eq("is_del", 0);
        List<ProjectHelmetStock> list1 = service.selectList(ew);
        StringBuilder sb = new StringBuilder("新增入库失败");
        boolean flag =false;
        if (list.size() > 0) {
            flag = true;
            sb.append(",设备已存在");
        }
        if (list1.size() > 0) {
            flag = true;
            sb.append(",SIM卡已存在");

        }
        if(flag){
            return new ResultDTO<>(false, null, sb.toString());
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
