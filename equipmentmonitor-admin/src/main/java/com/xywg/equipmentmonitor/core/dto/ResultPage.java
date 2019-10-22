package com.xywg.equipmentmonitor.core.dto;

import java.util.List;

public class ResultPage<T> {

	int total;
	 
	List<T> data;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public ResultPage(int total, List<T> data) {
		super();
		this.total = total;
		this.data = data;
	}

	public ResultPage() {
		super();
	}
	
	
	
}
