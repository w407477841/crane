package com.xywg.iot.modules.helmet.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author hjy
 * @date 2018/12/11
 */
@XmlType(propOrder = { "user" })
@Data
public class CeShiGzzhxxs {
    private List<CeShiUser> user;
}
