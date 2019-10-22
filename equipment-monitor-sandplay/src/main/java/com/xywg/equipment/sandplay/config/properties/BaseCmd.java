package com.xywg.equipment.sandplay.config.properties;

import lombok.Data;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:13 2018/9/25
 * Modified By : wangyifei
 */
@Data
public class BaseCmd {
    /**地址*/
    private List<String> addr ;
    /**控制码*/
    private List<String> control;
    /**数据位*/
    private List<String> data;
    /**校验位*/
    private List<String> crc ;
 }
