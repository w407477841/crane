package com.xingyun.equipment.system.service;

import com.xingyun.equipment.system.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public interface SecurityService extends UserDetailsService {
	/**
	 *
	 * 获取用户权限
	 * @param username
	 * @param keyPrex
	 * @return
	 */
	 Collection<? extends GrantedAuthority> loadGrantedAuthorityByUser(String username, String keyPrex);





	 UserVo getUser(String username, String keyPrex);

	/**
	 * 更新当前 令牌下 用户选择的组织
	 * @param token token
	 * @param orgId  缓存的值
	 * @return
	 */

	 Integer updateOrgId(String token, Integer orgId);

	/**
	 * 获取当前令牌下 用户选择的组织
	 * @param token
	 * @return
	 */

	 Integer getOrgId(String token);

	/**
	 * 缓存用户的当前选择的集团树
	 * @param userId
	 * @return
	 */
	 List<Integer> getOrgids(Integer userId);

	/**
	 *  修改緩存
	 * @param userId
	 * @return
	 */
	 List<Integer> updateOrgids(Integer userId);


	/**
	 * 清除当前用户选择的集团树
	 */
	 void removeOrgids(Integer userId);

}
