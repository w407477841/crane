package com.xywg.equipmentmonitor.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.modular.system.model.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipmentmonitor.modular.system.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户信息列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<User> selectUserInfo(Page<User> page,Map<String, Object> map) throws Exception;

    /**
     * 获取部门下用户
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<User> selectUserByOrgId(Page<User> page,Map<String, Object> map) throws Exception;

    /**
     * 获取未加入该部门下的用户
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<User> selectUserNotInOrg(Page<User> page,Map<String,Object> map) throws Exception;

    /**
     * 根据id获取用户
     * @param map
     * @return
     * @throws Exception
     */
    UserVo selectUserById(Map<String,Object> map) throws Exception;

    /**
     * 用户登录名判重及校验密码是否正确
     * @param map
     * @return
     * @throws Exception
     */
    User checkUser(Map<String,Object> map) throws Exception;

    /**
     * 修改密码
     * @param map
     * @return
     * @throws Exception
     */
    Integer changePWD(Map<String,Object> map) throws Exception;

    /**
     * 手机号判重
     * @param map
     * @return
     * @throws Exception
     */
    User checkPhone(Map<String,Object> map) throws Exception;

    List<User> getListUserByIds(@Param("ids") String[] ids);
}
