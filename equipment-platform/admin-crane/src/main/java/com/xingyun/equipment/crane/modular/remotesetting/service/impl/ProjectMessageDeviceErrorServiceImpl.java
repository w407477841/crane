package com.xingyun.equipment.crane.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.dao.ProjectMessageDeviceErrorMapper;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectMessageDeviceError;
import com.xingyun.equipment.crane.modular.remotesetting.service.ProjectMessageDeviceErrorService;
import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public class ProjectMessageDeviceErrorServiceImpl extends ServiceImpl<ProjectMessageDeviceErrorMapper, ProjectMessageDeviceError> implements ProjectMessageDeviceErrorService {
    @Autowired
    private IUserService userService;

    @Override
    public ResultDTO<DataDTO<List<ProjectMessageDeviceError>>> getPageList(RequestDTO<ProjectMessageDeviceError> request) {
        Page<ProjectMessageDeviceError> page = new Page<>(request.getPageNum(), request.getPageSize());
        ProjectMessageDeviceError projectDeviceErrorLog =request.getBody();
        EntityWrapper<ProjectMessageDeviceError> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getDeviceNo())) {
            ew.eq("device_no", projectDeviceErrorLog.getDeviceNo());
        }
        if (projectDeviceErrorLog.getProjectId() != null) {
            ew.eq("project_id", projectDeviceErrorLog.getProjectId());
        }
        ew.orderBy("createTime",false);
        List<ProjectMessageDeviceError> list = baseMapper.selectPage(page,ew);
        List<ProjectMessageDeviceError> resList= new ArrayList<>();

        //将字符串id值转为 名字串
        for(ProjectMessageDeviceError  deviceError:list){
            if(StringUtils.isNotBlank(deviceError.getUserIds())){
                String[] userArray= deviceError.getUserIds().split(",");
                List<User>  userList=userService.getListUserByIds(userArray);

                StringBuilder stringBuilder=new StringBuilder();
                for(User user:userList){
                    stringBuilder.append(user.getName()).append(",");
                }
                deviceError.setUserNames(stringBuilder.toString().substring(0,stringBuilder.toString().length()-1));
            }
            resList.add(deviceError);
        }

        return new ResultDTO<>(true, DataDTO.factory(resList, page.getTotal()));
    }
}
