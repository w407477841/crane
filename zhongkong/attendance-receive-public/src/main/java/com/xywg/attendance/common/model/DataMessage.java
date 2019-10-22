package com.xywg.attendance.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hjy
 * @date 2019/2/25
 */
@Data
public class DataMessage implements Serializable {


    private static final long serialVersionUID = 824818494265163018L;
    private String keyName;

    private Map<String, String> map;

}
