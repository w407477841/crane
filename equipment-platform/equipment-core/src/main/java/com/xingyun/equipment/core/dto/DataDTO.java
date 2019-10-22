package com.xingyun.equipment.core.dto;

import lombok.Data;

/**
 * 数据包装类
 * 
 * @author lw sakljkl
 *
 * @param <T>
 */
@Data
public class DataDTO<T> {
	/**
	 * 对象
	 */
	private T list;
	/**
	 * 条数
	 */
	private Integer total;

	private DataDTO(T t) {
		this(t, 0);
	}

	private DataDTO(T t, int total) {
		this.list = t;
		this.total = total;
	}

	public static <T> DataDTO<T> factory(T t) {
		return new DataDTO<>(t, 0);
	}

	public static <T> DataDTO<T> factory(T t, int total) {
		return new DataDTO<>(t, total);
	}
}
