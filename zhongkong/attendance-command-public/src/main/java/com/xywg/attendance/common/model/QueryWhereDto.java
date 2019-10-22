package com.xywg.attendance.common.model;

import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2019/1/4
 */
@Data
public class QueryWhereDto<T> {

    private Integer id;

    private List<T> list;

    private Integer pageSize;

    private Integer pageNum;

    private T body;

    private String keyWord;

}
