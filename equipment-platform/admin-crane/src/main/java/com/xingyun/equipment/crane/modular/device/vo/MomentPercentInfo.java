package com.xingyun.equipment.crane.modular.device.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MomentPercentInfo {
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 起始高度
     */
    private BigDecimal beginHeight;
    /**
     * 终止高度
     */
    private BigDecimal endHeight;
    /**
     * 起始幅度
     */
    private BigDecimal beginRange;
    /**
     * 终止幅度
     */
    private BigDecimal endRange;
    /**
     * 起始重量
     */
    private BigDecimal weight;
    /**
     * 安全吊重
     */
    private BigDecimal safetyWeight;
    /**
     * 起始角度
     */
   private BigDecimal beginAngle;
    /**
     * 终止角度
     */
    private BigDecimal endAngle;
    /**
     * 力矩百分比
     */
    private BigDecimal momentPercentage;
    /**
     * 吊绳倍率
     */
    private BigDecimal multipleRate;
    /**
     * 开始日期
     */
    private Date beginTime;
    /**
     * 结束日期
     */
    private Date endTime;
    /**
     * 报警内容
     */
    private String  alarmInfo;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public BigDecimal getBeginHeight() {
        return beginHeight;
    }

    public void setBeginHeight(BigDecimal beginHeight) {
        this.beginHeight = beginHeight;
    }

    public BigDecimal getEndHeight() {
        return endHeight;
    }

    public void setEndHeight(BigDecimal endHeight) {
        this.endHeight = endHeight;
    }

    public BigDecimal getBeginRange() {
        return beginRange;
    }

    public void setBeginRange(BigDecimal beginRange) {
        this.beginRange = beginRange;
    }

    public BigDecimal getEndRange() {
        return endRange;
    }

    public void setEndRange(BigDecimal endRange) {
        this.endRange = endRange;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getSafetyWeight() {
        return safetyWeight;
    }

    public void setSafetyWeight(BigDecimal safetyWeight) {
        this.safetyWeight = safetyWeight;
    }

    public BigDecimal getBeginAngle() {
        return beginAngle;
    }

    public void setBeginAngle(BigDecimal beginAngle) {
        this.beginAngle = beginAngle;
    }

    public BigDecimal getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(BigDecimal endAngle) {
        this.endAngle = endAngle;
    }

    public BigDecimal getMomentPercentage() {
        return momentPercentage;
    }

    public void setMomentPercentage(BigDecimal momentPercentage) {
        this.momentPercentage = momentPercentage;
    }

    public BigDecimal getMultipleRate() {
        return multipleRate;
    }

    public void setMultipleRate(BigDecimal multipleRate) {
        this.multipleRate = multipleRate;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }
}
