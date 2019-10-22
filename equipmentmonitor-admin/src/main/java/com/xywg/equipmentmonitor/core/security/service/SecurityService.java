package com.xywg.equipmentmonitor.core.security.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.xywg.equipmentmonitor.core.security.vo.UserVO;

public interface SecurityService extends UserDetailsService {
	/**
	 *
	 * 获取用户权限
	 * @param username
	 * @param keyPrex
	 * @return
	 */
	public Collection<? extends GrantedAuthority> loadGrantedAuthorityByUser(String username,String keyPrex);





	public UserVO getUser(String username,String keyPrex);

	/**
	 * 更新当前 令牌下 用户选择的组织
	 * @param token token
	 * @param orgId  缓存的值
	 * @return
	 */

	public Integer updateOrgId(String token ,Integer orgId);

	/**
	 * 获取当前令牌下 用户选择的组织
	 * @param token
	 * @return
	 */

	public Integer getOrgId(String token );

	/**
	 * 缓存用户的当前选择的集团树
	 * @param userId
	 * @return
	 */
	public List<Integer> getOrgids(Integer userId);

	/**
	 *  修改緩存
	 * @param userId
	 * @return
	 */
	public List<Integer> updateOrgids(Integer userId);


	/**
	 * 清除当前用户选择的集团树
	 */
	public void removeOrgids(Integer userId);

}
