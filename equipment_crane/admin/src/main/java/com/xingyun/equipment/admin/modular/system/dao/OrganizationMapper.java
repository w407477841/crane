package com.xingyun.equipment.admin.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.vo.OrgVO;
import com.xingyun.equipment.admin.modular.system.model.Organization;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.admin.modular.system.model.User;
import com.xingyun.equipment.admin.modular.system.vo.OrganizationVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
public interface OrganizationMapper extends BaseMapper<Organization> {
    /**
     * 获取部门信息
     * @param map
     * @return
     */
    List<OrganizationVo> selectOrganizationInfo(Map<String,Object> map);

    /**
     *
     * @param userid
     * @return
     */
    @Select("select * from t_sys_organization " +
            "where id in (select ou.org_id from t_sys_organization_user ou where ou.user_id = #{userId} and ou.is_del = 0) " +
            "and is_del =  0 ")
    List<Organization> getByUserId(Integer userid);
    @Select("select id ,parent_id pid from t_sys_organization where is_del = 0")
    List<OrgVO>  selectALlOrg();

    /**
     * 获取当前用户下的项目部
     * @param map
     * @return
     * @throws Exception
     */
    List<Organization> selectOrganization(Map<String,Object> map) throws Exception;
}
