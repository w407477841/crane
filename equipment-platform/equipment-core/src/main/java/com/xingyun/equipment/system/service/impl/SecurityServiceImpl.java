package com.xingyun.equipment.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.system.dao.UserMapper;
import com.xingyun.equipment.system.dto.UserDTO;
import com.xingyun.equipment.system.model.Operation;
import com.xingyun.equipment.system.model.Organization;
import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.service.IOperationService;
import com.xingyun.equipment.system.service.IOrganizationService;
import com.xingyun.equipment.system.service.SecurityService;
import com.xingyun.equipment.system.vo.UserVo;
import com.xingyun.equipment.core.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
* @author: wangyifei
* Description: 权限缓存10分钟，
 * 用户缓存10分钟，
 * token对应组织缓存 jwtProperties 配置时间
 * token对应的组织树缓存jwtProperties 配置时间
* Date: 15:23 2018/9/4
*/
@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private IOrganizationService organizationService;

	@Autowired
	private IOperationService operationService;
	

	@Autowired
	JwtProperties jwtProperties;

	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 查询用户
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Wrapper<User>  wrapper  =new EntityWrapper();
 		wrapper.eq("code",username );
		User user = SqlHelper.getObject(userMapper.selectList(wrapper));
		if(user == null){ return null;}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user.getCode());
		userDTO.setPassword(user.getPassword());
		//账户认证过期
		userDTO.setCredentialsNonExpired(false);
		// 账户已过期
		userDTO.setAccountNonExpired(false);
		// 已启用
		userDTO.setEnabled(user.getStatus() == 0);
		//账户未被锁定
		userDTO.setAccountNonLocked(true);

		return userDTO;
	}
	/**
	 * 从缓存中取 该用户的权限
	 */
	@Override
	public Collection<? extends GrantedAuthority> loadGrantedAuthorityByUser(String username,String keyPrefix) {


		String key  = "device_platform:"+Const.token.get()+":auths";

		if(!redisUtil.exists(key)){
			System.out.println("<<<<<<<<<<<<<<<<<<缓存中不存在 "+username+"的权限  >>>>>>>>>>>>>>>>>>");
			List<SimpleGrantedAuthority>  authoritys = new ArrayList();

			List<Operation>  ops =  operationService.getOperations();

			for(Operation  operation:ops){
				SimpleGrantedAuthority  authority =new SimpleGrantedAuthority("ROLE_"+operation.getPermission());
				authoritys.add(authority);
			}
			redisUtil.set(key,authoritys,10L);
			return authoritys;
			}
		else{
			return (Collection<? extends GrantedAuthority>) redisUtil.get(key);
		}
	}


	@Override

	public UserVo getUser(String username, String keyPrefix) {

		String key = "device_platform:"+Const.token.get()+":user"    ;

		System.out.println("user  key="+key);

		if(redisUtil.exists(key)){
			return (UserVo)redisUtil.get(key);
		}else{
			Wrapper<User>  wrapper  =new EntityWrapper();
			wrapper.eq("code",username );
			User user = SqlHelper.getObject(userMapper.selectList(wrapper));
			UserVo userVo = UserVo.factory(user);
			//查询  所有
			List<Organization> orgs= organizationService.getByUserId(userVo.getId());
			userVo.setGroups(orgs);
			redisUtil.set(key,userVo,10L);
			return userVo;

		}



	}

	@Override
	public Integer updateOrgId(String token,Integer orgId) {
	String key = "device_platform:"+token+":orgid";
		   redisUtil.set(key,orgId,jwtProperties.getExpiration()/1000/60);
		return orgId;
	}

	@Override
	public Integer getOrgId(String token) {
		String key = "device_platform:"+token+":orgid";
		if(redisUtil.exists(key)){
			return (Integer) redisUtil.get(key);
		}

		return null;
	}

	@Override
	public List<Integer> getOrgids(Integer userId) {
		if(userId == null) {
			//只可能是 多部门时，选择集团
			return null;
		}
		String key = "device_platform:"+Const.token.get()+":orgids";
			if(redisUtil.exists(key)){
				return (List<Integer>) redisUtil.get(key);
			}
			return null;

	}

	@Override
	public List<Integer> updateOrgids(Integer userId) {
		if(userId == null) {
			//只可能是 多部门时，选择集团
			return null;
		}
		String key = "device_platform:"+Const.token.get()+":orgids";
			List<Integer> list =  organizationService.getOrgsByParent(getOrgId(Const.token.get()));
			redisUtil.set(key,list,jwtProperties.getExpiration()/1000/60);
			return	list;
		}


	@Override
	public void removeOrgids(Integer userId) {
		String key = "device_platform:"+Const.token.get()+":orgids";
		if(redisUtil.exists(key)){
			redisUtil.remove(key);
		}
		}

}
