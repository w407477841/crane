package com.xingyun.equipment.admin.core.util;

import java.util.List;
/**
 * 
 * @author 树形对象接口
 *
 */
public interface  TreeI{

	  Integer getId();
	  Integer getPid();
	  List<? extends TreeI >  getChildren();
	  void setChildren(List<? extends TreeI> children);
	  
	  
	  
	  String getCode();
	  String getPcode();
	
}
