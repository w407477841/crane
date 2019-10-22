package com.xingyun.equipment.crane.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.enums.NettyConstant;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.modular.device.dao.ProjectHelmetMapper;
import com.xingyun.equipment.crane.modular.device.dto.ProjectTransfersDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmet;
import com.xingyun.equipment.crane.modular.device.service.IProjectHelmetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 安全帽 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2018-11-23
 */
@Service
public class ProjectHelmetServiceImpl extends ServiceImpl<ProjectHelmetMapper, ProjectHelmet> implements IProjectHelmetService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmet>>> selectPageList(RequestDTO<ProjectHelmet> res) {
        Page<ProjectHelmet> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectHelmet>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        ew.eq("a.current_flag", 0);
        ew.in("b.org_id", res.getOrgIds());
        if (res.getId() != null && !"".equals(res.getId())) {
            ew.eq("a.project_id", res.getId());
        }
        if (StringUtils.isNotBlank(res.getKey())) {
            ew.like("a.imei", res.getKey());
        }
        List<ProjectHelmet> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    public ResultDTO<Object> updateStatus(RequestDTO<ProjectHelmet> res) {
        String message;
        Integer flag;
        String useFlag = "use";
        if (useFlag.equals(res.getKey())) {
            message = "启用成功";
            flag = 1;
        } else {
            message = "停用成功";
            flag = 0;
        }
        baseMapper.updateStatusByIds(flag, res.getIds());
        return new ResultDTO<>(true, null, message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> projectTransfers(RequestDTO<ProjectTransfersDTO> res) {
        List<ProjectHelmet> listProjectHelmet = res.getBody().getListData();
        Integer projectId = res.getBody().getProjectId();

        if (listProjectHelmet == null || listProjectHelmet.size() == 0 || projectId == null) {
            return new ResultDTO<>(false, null, "项目调拨失败,无效操作");
        }

        List<ProjectHelmet> dbProjectHelmetList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();

        for (ProjectHelmet helmet : listProjectHelmet) {
            idList.add(helmet.getId());

            ProjectHelmet projectHelmet = new ProjectHelmet();
            BeanUtils.copyProperties(helmet, projectHelmet);

            projectHelmet.setProjectId(projectId);
            projectHelmet.setComments(res.getBody().getComments());
            projectHelmet.setCurrentFlag(0);
            projectHelmet.setId(null);

            dbProjectHelmetList.add(projectHelmet);

            //清除redis中的缓存 让设备主动重新连接
            redisUtil.remove(NettyConstant.XYWG_IOT_HELMET + helmet.getImei());

        }
        //在调拨前 要先解除已经绑定的关系 数据状态设置为1 表示它为历史履历 启用状态设备为0 表示未启用
        baseMapper.updateCurrentFlagAndStatusByIds(idList);
        //在插入新的绑定关系时,设置数据状态为0,表示设备最新绑定数据,启用状态保留原来的状态
        this.insertBatch(dbProjectHelmetList);
        return new ResultDTO<>(true, null, "项目调拨成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> personTransfers(RequestDTO<ProjectHelmet> res) {
        ProjectHelmet dbProjectHelmet = baseMapper.selectById(res.getBody().getId());

        List<Integer> idsList = new ArrayList<>();
        idsList.add(res.getBody().getId());
        //人员调拨前,先将该设备解除关联关系, 数据状态置为1 表示它为历史履历 启用状态设备为0 表示未启用
        baseMapper.updateCurrentFlagAndStatusByIds(idsList);
        ProjectHelmet projectHelmet = new ProjectHelmet();
        BeanUtils.copyProperties(dbProjectHelmet, projectHelmet);
        projectHelmet.setIdCardNumber(res.getBody().getIdCardNumber());
        projectHelmet.setComments(res.getBody().getComments());
        projectHelmet.setCurrentFlag(0);
        projectHelmet.setIdCardType(1);
        this.insert(projectHelmet);

        //清除redis中的缓存 让设备主动重新连接
        redisUtil.remove(NettyConstant.XYWG_IOT_HELMET + dbProjectHelmet.getImei());

        return new ResultDTO<>(true, null, "人员调拨成功");
    }

    @Override
    public ResultDTO<Object> updateOrInsertProjectHelmet(RequestDTO<ProjectHelmet> res) {
        final String insert = "insert";
        final String update = "update";

        EntityWrapper<ProjectHelmet> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("imei", res.getBody().getImei());
        if (insert.equals(res.getType())) {
            List<ProjectHelmet> list = baseMapper.selectList(ew);
            if (list.size() > 0) {
                return new ResultDTO<>(false, null, "新增失败,设备已存在");
            }
            ProjectHelmet projectHelmet = res.getBody();
            projectHelmet.setStatus(0);
            projectHelmet.setIsOnline(0);
            baseMapper.insert(res.getBody());
            return new ResultDTO<>(true, null, "新增成功");
        } else if (update.equals(res.getType())) {
            ew.ne("id", res.getBody().getId());
            List<ProjectHelmet> list = baseMapper.selectList(ew);
            if (list.size() > 0) {
                return new ResultDTO<>(false, null, "更新失败,设备已存在");
            }
            baseMapper.updateById(res.getBody());
            return new ResultDTO<>(true, null, "更新成功");
        } else {
            return new ResultDTO<>(false, null, "操作失败");
        }
    }

}
