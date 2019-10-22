package com.xywg.equipmentmonitor.modular.common.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecursiveCondition {
	private String tableName;
	private Integer pId;
	private String pName;
	private Integer isDel;
	private List<Integer> ids;
}
