package com.xywg.equipmentmonitor.modular.projectmanagement.service.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.remotesetting.dao.ProjectApplicationConfigMapper;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.config.properties.RemoteUrl;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import com.xywg.equipmentmonitor.modular.projectmanagement.dao.ProjectAreaMapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectArea;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectAreaVoVo;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectInfoVo;
import com.xywg.equipmentmonitor.modular.system.dao.OrganizationMapper;
import com.xywg.equipmentmonitor.modular.system.vo.OrganizationVo;

import cn.hutool.http.HttpUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {
    @Autowired
    ProjectAreaMapper projectAreaMapper;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    RemoteUrl  remoteUrl;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ProjectApplicationConfigMapper projectApplicationConfigMapper;

    @Override
    public boolean insert(ProjectInfo projectInfo) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("name",projectInfo.getName());
        map.put("builder",projectInfo.getBuilder());
        if(baseMapper.getProjectByName(map).size() > 0) {
            throw new RuntimeException("名称重复");
        }
        return retBool(this.baseMapper.insert(projectInfo));
    }

    @Override
    public boolean updateById(ProjectInfo projectInfo) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("name",projectInfo.getName());
        map.put("builder",projectInfo.getBuilder());
        map.put("id",projectInfo.getId());
        if(baseMapper.getProjectByName(map).size() > 0) {
            throw new RuntimeException("名称重复");
        }
        return retBool(this.baseMapper.updateById(projectInfo));
    }

    @Override
    public List<ProjectInfoVo> getProjectInfo(RequestDTO<ProjectInfoVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        return baseMapper.getProjectInfo(map);
    }

    @Override
    public List<ProjectInfo> selectProject(RequestDTO<ProjectInfo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        return baseMapper.selectProject(map);
    }

    @Override
    public boolean bindingProject(RequestDTO<ProjectInfoVo> requestDTO) throws Exception {
        if(baseMapper.bindingProject(requestDTO.getBody()) > 0) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HttpUtil.download(remoteUrl.getProject()+"/ssqdgov/project/projectInfo/bind?id=" + requestDTO.getBody().getSmartProjectId(), os, true);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String getSmartProject(RequestDTO<ProjectInfo> requestDTO) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int pageIndex = requestDTO.getPageNum();
        int pageSize = requestDTO.getPageSize();
        Map<String,Object> param = new HashMap<>(10);
        param.put("pageIndex",pageIndex);
        param.put("pageSize",pageSize);
        param.put("keyword",requestDTO.getKey());
        String url = HttpUtil.urlWithForm(remoteUrl.getProject()+"/ssqdgov/project/projectInfo/getExternalProjectInfo",param, Charset.forName("UTF-8"),false);
        //解析url
        HttpUtil.download(url, os, true);
        String resultStr = StringCompress.decompress(os.toByteArray());
        return resultStr;
    }

    @Override
    public List<ProjectInfoVo> selectProjectInfo(Page<ProjectInfoVo> page,RequestDTO<ProjectInfoVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("key",requestDTO.getKey());
        map.put("orgIds",requestDTO.getOrgIds());
        if(requestDTO.getBody() != null) {
            map.put("orgId",requestDTO.getBody().getOrgId());
        }
        return baseMapper.selectProjectInfo(page,map);
    }

    @Override
    public ProjectInfoVo selectProjectById(RequestDTO<ProjectInfoVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        List<Integer> positions = new ArrayList<>(10);
        List<Integer> builders = new ArrayList<>(10);
        map.put("id",requestDTO.getId());
        ProjectInfoVo projectInfoVo = baseMapper.selectProjectById(map);
        List<ProjectAreaVoVo> projectAreaVoVos = projectAreaMapper.selectAreaInfo(map);
        ProjectArea projectArea = projectAreaMapper.selectById(projectInfoVo.getPosition());
        if(projectArea != null) {
            positions = getAreaIds(projectArea.getCode(),positions,projectAreaVoVos);
        }
        map.put("orgIds",requestDTO.getOrgIds());
        List<OrganizationVo> organizationVos = organizationMapper.selectOrganizationInfo(map);
        if(projectInfoVo.getBuilder()!=null){
            builders = getOrgIds(projectInfoVo.getBuilder(),builders,organizationVos);
        }

        Collections.sort(positions);
        Collections.sort(builders);
        projectInfoVo.setPositions(positions);
        projectInfoVo.setBuilders(builders);
        return projectInfoVo;
    }

    @Override
    public Integer selectExceedCountByOrgId(Integer orgId) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        map.put("orgId",orgId);
        map.put("tableName","t_project_environment_monitor_alarm_" + sdf.format(new Date()));
        return baseMapper.selectExceedCountByOrgId(map);
    }

    @Override
    public Integer getProjectCount() throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds", Const.orgIds.get());
        return baseMapper.getProjectCount(map);
    }

    @Override
    public boolean insertProject(ProjectInfo requestDTO) throws Exception {
        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid",requestDTO.getUuid());
        List<ProjectInfo> list = baseMapper.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException(ResultCodeEnum.PROJECT_UUID_EXIST.msg());
        }
        Wrapper<ProjectApplicationConfig> appWrapper = new EntityWrapper<>();
        appWrapper.eq("topic",requestDTO.getTopic());
        List<ProjectApplicationConfig> projectApplicationConfigs = projectApplicationConfigMapper.selectList(appWrapper);

        if(!(projectApplicationConfigs.size() > 0)) {
            throw new RuntimeException(ResultCodeEnum.PROJECT_TOPIC_NOT_EXIST.msg());
        }else{
            ProjectApplicationConfig projectApplicationConfig = projectApplicationConfigs.get(0);
            requestDTO.setOrgId(projectApplicationConfig.getOrgId());
            requestDTO.setCreateTime(new Date());
            requestDTO.setCreateUser(projectApplicationConfig.getCreateUser());
        }
        return retBool(baseMapper.insert(requestDTO));
    }

    public List<Integer> getAreaIds(Integer id,List<Integer> ids,List<ProjectAreaVoVo> projectAreaVoVos) {
        for(int i = 0;i < projectAreaVoVos.size();i++) {
            if(id.equals(projectAreaVoVos.get(i).getId())){
                ids.add(projectAreaVoVos.get(i).getText());
                if(projectAreaVoVos.get(i).getParentId() == 0) {
                    break;
                }
                getAreaIds(projectAreaVoVos.get(i).getParentId(),ids,projectAreaVoVos);
            }
        }
        return ids;
    }

    public List<Integer> getOrgIds(Integer id,List<Integer> ids,List<OrganizationVo> organizationVos) {
        ids.add(id);
        for(int i = 0;i < organizationVos.size();i++) {
            if(id.equals(organizationVos.get(i).getId())) {
                if(organizationVos.get(i).getPid() == 0) {
                    break;
                }
                getOrgIds(organizationVos.get(i).getPid(),ids,organizationVos);
            }
        }
        return ids;
    }

    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("list",idList);
        List<ProjectInfoVo> projectInfoVos = baseMapper.selectDeviceByProjectId(map);
        for(int i = 0;i < projectInfoVos.size();i++) {
            if("crane".equals(projectInfoVos.get(i).getCategory())) {
                String key = "device_platform:deviceinfo:crane:" + projectInfoVos.get(i).getDeviceNo();
                if(redisUtil.exists(key)) {
                    redisUtil.remove(key);
                }
            }else if("electric".equals(projectInfoVos.get(i).getCategory())) {
                String key = "device_platform:deviceinfo:electric:" + projectInfoVos.get(i).getDeviceNo();
                if(redisUtil.exists(key)) {
                    redisUtil.remove(key);
                }
            }else if("monitor".equals(projectInfoVos.get(i).getCategory())) {
                String key = "device_platform:deviceinfo:monitor:" + projectInfoVos.get(i).getDeviceNo();
                if(redisUtil.exists(key)) {
                    redisUtil.remove(key);
                }
            }else if("lift".equals(projectInfoVos.get(i).getCategory())) {
                String key = "device_platform:deviceinfo:lift:" + projectInfoVos.get(i).getDeviceNo();
                if(redisUtil.exists(key)) {
                    redisUtil.remove(key);
                }
            }else if("water".equals(projectInfoVos.get(i).getCategory())) {
                String key = "device_platform:deviceinfo:water:" + projectInfoVos.get(i).getDeviceNo();
                if(redisUtil.exists(key)) {
                    redisUtil.remove(key);
                }
            }
        }
        return super.deleteBatchIds(idList);
    }
}
