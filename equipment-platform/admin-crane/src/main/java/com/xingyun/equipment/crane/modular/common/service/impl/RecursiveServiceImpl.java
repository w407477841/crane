package com.xingyun.equipment.crane.modular.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.xingyun.equipment.crane.modular.common.dao.RecursiveMapper;
import com.xingyun.equipment.crane.modular.common.model.RecursiveCondition;
import com.xingyun.equipment.crane.modular.common.service.RecursiveService;

/**
 * 递归通用
 * 
 * @author lw
 * @date 2018年6月15日
 * @company jsxywg
 */
@Service
public class RecursiveServiceImpl implements RecursiveService {
	@Autowired
	private RecursiveMapper mapper;

	/**
	 * 递归删除
	 */
	@Override
	public <T> int recursiveDelete(Class<T> clazz, String pName, Integer pId) {

		TableName table = clazz.getAnnotation(TableName.class);
		String tableName = table.value();
		RecursiveCondition query = new RecursiveCondition(tableName, pId, pName, null, null);
		List<Integer> list = Lists.newArrayList();
		querySubItem(pId, list, query);
		if (list.isEmpty()) {
			return 0;
		} else {
			query.setIds(list);
			return mapper.delete(query);
		}
	}

	@Override
	public <T> List<Integer> recursiveQuery(Class<T> clazz, String pName, Integer pId) {
		return recursiveQuery(clazz, pName, pId, false);
	}

	@Override
	public <T> List<Integer> recursiveQueryWithFlag(Class<T> clazz, String pName, Integer pId, Boolean flag) {
		return recursiveQuery(clazz, pName, pId, true);
	}

	/**
	 * 递归查询
	 */
	private <T> List<Integer> recursiveQuery(Class<T> clazz, String pName, Integer pId, Boolean flag) {

		TableName table = clazz.getAnnotation(TableName.class);
		String tableName = table.value();
		List<Integer> list = Lists.newArrayList();
		RecursiveCondition query = null;
		if (flag) {
			query = new RecursiveCondition(tableName, pId, pName, 1, null);
		} else {

			query = new RecursiveCondition(tableName, pId, pName, null, null);
		}
		querySubItem(pId, list, query);
		return list;
	}

	private void querySubItem(Integer pId, List<Integer> list, RecursiveCondition query) {
		query.setPId(pId);
		List<Integer> subIds = mapper.query(query);
		if (subIds != null && subIds.size() > 0) {
			for (Integer id : subIds) {
				this.querySubItem(id, list, query);
			}
		}
		list.add(pId);

	}

}
