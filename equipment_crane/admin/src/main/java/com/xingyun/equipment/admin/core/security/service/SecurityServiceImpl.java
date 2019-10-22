package com.xingyun.equipment.admin.core.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xingyun.equipment.admin.config.properties.JwtProperties;
import com.xingyun.equipment.admin.config.properties.XywgProperties;
import com.xingyun.equipment.admin.core.common.constant.Const;

import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.modular.system.model.Operation;
import com.xingyun.equipment.admin.modular.system.model.Organization;
import com.xingyun.equipment.admin.modular.system.service.IOperationService;
import com.xingyun.equipment.admin.modular.system.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.config.properties.CacheProperties;
import com.xingyun.equipment.admin.core.security.factory.UserFactory;
import com.xingyun.equipment.admin.core.security.vo.UserVO;
import com.xingyun.equipment.admin.modular.system.dao.UserMapper;
import com.xingyun.equipment.admin.modular.system.model.User;


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
	private UserMapper   userMapper;
	@Autowired
	private IOrganizationService organizationService;

	@Autowired
	private IOperationService  operationService;
	

	@Autowired
	
	CacheProperties  cacheProperties;

	@Autowired
	JwtProperties  jwtProperties;

	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 查询用户
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Wrapper<User>  wrapper  =new EntityWrapper<>();
 		wrapper.eq("code",username );
		User user = SqlHelper.getObject(userMapper.selectList(wrapper));
		return UserFactory.getUserDTO(user);
	}
	/**
	 * 从缓存中取 该用户的权限
	 */
	@Override
	public Collection<? extends GrantedAuthority> loadGrantedAuthorityByUser(String username,String keyPrefix) {


		String key  = "device_platform:"+Const.token.get()+":auths";

		if(!redisUtil.exists(key)){
			System.out.println("<<<<<<<<<<<<<<<<<<缓存中不存在 "+username+"的权限  >>>>>>>>>>>>>>>>>>");
			List<SimpleGrantedAuthority>  authoritys = new ArrayList<>();

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

	public UserVO getUser(String username, String keyPrefix) {

		String key = "device_platform:"+Const.token.get()+":user"    ;

		System.out.println("user  key="+key);

		if(redisUtil.exists(key)){
			return (UserVO)redisUtil.get(key);
		}else{
			Wrapper<User>  wrapper  =new EntityWrapper<>();
			wrapper.eq("code",username );
			User user = SqlHelper.getObject(userMapper.selectList(wrapper));
			UserVO userVo = UserVO.factory(user);
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
