package com.xingyun.equipment.crane.modular.device.vo;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class WeightPercentVO {
    //项目编号
    private Integer projectId;
    //项目名称
    private String projectName;
    //黑匣子编号
    private String deviceNo;
    //设备编号
    private String craneNo;
    //吊重次数
    private Integer liftFrequency;
    //违章次数
    private Integer weightAlarm;
    private Integer type;
    /**
     * 40以下
     */
    private Integer percentage0;
    /**
     * 40-60
     */
    private Integer percentage40;
    /**
     * 60-80
     */
    private Integer percentage60;
    /**
     * 80-90
     */
    private Integer percentage80;
    /**
     * 90-110
     */
    private Integer percentage90;
    /**
     * 110-120
     */
    private Integer percentage110;
    /**
     * 120以上
     */
    private Integer percentage120;
    //时间范围
    private Integer dayNum;
    //载荷
    private BigDecimal load;

    //状态
    private Integer status;
    //月使用频率
    private BigDecimal frequencyMonth;
    private String keyWord;

    private Integer rangeAlarm;

    private Integer limitAlarm;

    private Integer builder;
}
