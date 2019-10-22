package com.xingyun.equipment.simpleequipment.core.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hjy
 * @date 2018/9/19
 * 一条完整的报文数据结构实体
 */
@Data
public class CompleteDataPojo implements Serializable {

    /**
     * 固定码
     */
    private String fixedCode;
    /**
     * 版本号
     */
    private String version;
    /**
     * 命令代码
     */
    private String command;
    /**
     * 表示数据包是请求包、应答包还是主动发送包，0：应答包；1：请求包
     */
    private String aQ;
    /**
     * 表示数据总长度
     */
    private Integer dataLength;
    /**
     * 表示数据表是否正确，正确为0，错误为大于0的正整数
     */
    private String errorStatus;
    /**
     * 0xFF	CRC16校验	BCD
     **/
    private String crc16;
    /*----------------------------------------↑头部数据-----------------------------------------*/
    /**
     * 数据体
     */
    private Map<String, String> dataMap;


    public CompleteDataPojo(String fixedCode, String version, String command, String aQ, Integer dataLength, String errorStatus, String crc16, Map<String, String> dataMap) {
        this.fixedCode = fixedCode;
        this.version = version;
        this.command = command;
        this.aQ = aQ;
        this.dataLength = dataLength;
        this.errorStatus = errorStatus;
        this.crc16 = crc16;
        this.dataMap = dataMap;
    }

    public CompleteDataPojo() {
    }
}
