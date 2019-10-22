package com.xingyun.equipment.crane.modular.projectmanagement.service.serviceimpl;

import com.xingyun.equipment.crane.modular.projectmanagement.dao.ProjectAreaMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectArea;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectAreaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.modular.projectmanagement.vo.ProjectAreaVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-08-23
 */
@Service
public class ProjectAreaServiceImpl extends ServiceImpl<ProjectAreaMapper, ProjectArea> implements IProjectAreaService {

    @Override
    public List<ProjectAreaVo> selectArea() throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        Map<String,Object> map1 = new HashMap<>(10);
        map.put("parentId",0);
        map1.put("parentId",null);
        ArrayList<Object> list = new ArrayList<>(10);
        List<ProjectAreaVo> projectAreaVos = baseMapper.selectArea(map);
        List<ProjectAreaVo> projectAreaVoList = baseMapper.selectArea(map1);
        if(projectAreaVos.size() > 0) {
            for(int i = 0;i < projectAreaVos.size();i++) {
                ProjectAreaVo projectAreaVo = projectAreaVos.get(i);
                projectAreaVo = getChildren(projectAreaVo,projectAreaVoList);
                list.add(projectAreaVo);
            }
        }
        return projectAreaVos;
    }

    /**
     * 递归加子集
     * @param projectAreaVo
     * @param projectAreaVos
     * @return
     */
    private ProjectAreaVo getChildren(ProjectAreaVo projectAreaVo,List<ProjectAreaVo> projectAreaVos) {
        List<ProjectAreaVo> list = getData(projectAreaVo.getCode(),projectAreaVos);
        List<ProjectAreaVo> list1 = new ArrayList<>(10);
        if(list.size() > 0) {
            for(int i = 0;i < list.size();i++) {
                ProjectAreaVo pa = list.get(i);
                pa = getChildren(pa,projectAreaVos);
                list1.add(pa);
            }
            projectAreaVo.setChildren(list1);
        }
        return projectAreaVo;
    }

    /**
     * 获取子集
     * @param pid
     * @param projectAreaVos
     * @return
     */
    private List<ProjectAreaVo> getData(Integer pid,List<ProjectAreaVo> projectAreaVos) {
        List<ProjectAreaVo> list = new ArrayList<>(10);
        for(int i = 0;i < projectAreaVos.size();i++) {
            if(pid.equals(projectAreaVos.get(i).getParentId())) {
                list.add(projectAreaVos.get(i));
            }
        }
        return list;
    }
}
