package com.xingyun.equipment.admin.core.util;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author 王一飞
 *
 */
public class TreeObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer pid;
	private String code;
	private String pCode;
	
	
	private List<? extends TreeObject> children;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public List<? extends TreeObject> getChildren() {
		return children;
	}
	public void setChildren(List<? extends TreeObject> children) {
		this.children = children;
	}

}
