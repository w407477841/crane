package com.xywg.equipmentmonitor.modular.common.service;

import java.util.List;

import org.springframework.stereotype.Service;
/**
 * 通用递归操作
 * @author lw
 * @date 2018年6月21日
 * @company jsxywg
 */
@Service
public interface RecursiveService {
	
	/**
	 * 递归删除
	 * @param classz 实体类(类上需要@TableName(表名)注解)
	 * @param pName 父字段的字段名
	 * @param pid	父id
	 * @return
	 */
	<T> int recursiveDelete(Class<T> classz,String pName,Integer pid);
	/**
	 * 递归查询
	 * @param classz 实体类(类上需要@TableName(表名)注解)
	 * @param pName 父字段的字段名
	 * @param pid	父id
	 * @return
	 */
	<T> List<Integer> recursiveQuery(Class<T> clazz, String pName, Integer pId);

	/**
	 * 递归查询
	 * @param classz 实体类(类上需要@TableName(表名)注解)
	 * @param pName 父字段的字段名
	 * @param pid	父id
	 * @param flag	是否需要is_del=0，true 需要 false 不需要
	 * @return
	 */
	<T> List<Integer> recursiveQueryWithFlag(Class<T> clazz, String pName, Integer pId, Boolean flag);
}
