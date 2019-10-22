package com.xywg.attendance.common.model;

import lombok.Data;

import java.util.Map;

/**
 * @author hjy
 * @date 2019/2/25
 */
@Data
public class DataMessage {

    private String keyName;

    private Map<String,String> map;

}
