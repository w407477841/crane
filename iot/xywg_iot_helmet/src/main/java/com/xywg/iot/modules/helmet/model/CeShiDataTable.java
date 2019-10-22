package com.xywg.iot.modules.helmet.model;

import lombok.Data;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hjy
 * @date 2018/12/11
 */
@Data
@XmlRootElement(name = "DataTable")
public class CeShiDataTable {


    private CeShiGzzhxxs gzzhxxs;

}
