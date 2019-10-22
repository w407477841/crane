package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 安全帽定位明细(采集数据)
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@TableName("t_project_helmet_position_detail")
@Data
public class ProjectHelmetPositionDetail extends Model<ProjectHelmetPositionDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 安全帽id
     */
	@TableField("helmet_id")
	private Integer helmetId;
    /**
     * imei
     */
	private String imei;
    /**
     * 经度
     */
	private BigDecimal lng;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 百度经度
     */
	@TableField("bd_lng")
	private BigDecimal bdLng;
    /**
     * 百度纬度
     */
	@TableField("bd_lat")
	private BigDecimal bdLat;
    /**
     * 采集时间
     */
	@TableField("collect_time")
	private Date collectTime;
    /**
     * 备注
     */
	private String comments;
	@TableField("id_card_number")
	private String idCardNumber;

    /**
     * 删除标志
     */
	@TableField("is_del")
	private Integer isDel;
    /**
     * 创建日期
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	private Integer createUser;
    /**
     * 修改日期
     */
	@TableField("modify_time")
	private Date modifyTime;
    /**
     * 修改人
     */
	@TableField("modify_user")
	private Integer modifyUser;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
