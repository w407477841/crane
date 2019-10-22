package com.xingyun.equipment.admin.modular.common.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xingyun.equipment.admin.modular.common.model.RecursiveCondition;
/**
 * 递归删除通用
 * @author lw
 * @date 2018年6月14日
 * @company jsxywg
 */
@Service
public interface RecursiveMapper {

	/**
	 * 递归删除
	 * @param table 表名称
	 * @param pname 父节点字段
	 * @param pid	父节点id
	 * @return
	 */
    List<Integer> query(RecursiveCondition query);

	int delete(RecursiveCondition query);
}
