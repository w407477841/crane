package com.xingyun.equipment.admin.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.remotesetting.dao.ProjectMessageUserDeviceErrorLogMapper;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectMessageUserDeviceErrorLog;
import com.xingyun.equipment.admin.modular.remotesetting.service.ProjectMessageUserDeviceErrorLogService;
import com.xingyun.equipment.admin.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import com.xingyun.equipment.admin.modular.system.dao.UserMapper;
import com.xingyun.equipment.admin.modular.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author hjy
 */
@Service
public class ProjectMessageUserDeviceErrorLogServiceImpl extends ServiceImpl<ProjectMessageUserDeviceErrorLogMapper, ProjectMessageUserDeviceErrorLog> implements ProjectMessageUserDeviceErrorLogService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultDTO<DataDTO<List<ProjectMessageUserDeviceErrorLog>>> getPageList(RequestDTO request) {
        Page<ProjectDeviceUpgradePackageVO> page = new Page<>(request.getPageNum(), request.getPageSize());
        List<ProjectMessageUserDeviceErrorLog> list = baseMapper.getPageList(page);
        List<ProjectMessageUserDeviceErrorLog> resList = new ArrayList<>();
        for (ProjectMessageUserDeviceErrorLog userLog : list) {
            StringBuilder stringBuilder = new StringBuilder();
            if (StringUtils.isNotBlank(userLog.getUserIds())) {
                String[] ids = userLog.getUserIds().split(",");
                if (ids.length > 0) {
                    List<User> userList = userMapper.getListUserByIds(ids);
                    for (User user : userList) {
                        stringBuilder.append(user.getName()).append(",");
                    }
                }
                userLog.setUserNames(stringBuilder.toString());
            }
            resList.add(userLog);
        }
        return new ResultDTO<>(true, DataDTO.factory(resList, page.getTotal()));
    }
}
