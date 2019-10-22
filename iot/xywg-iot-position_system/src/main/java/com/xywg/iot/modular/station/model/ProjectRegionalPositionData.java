package com.xywg.iot.modular.station.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
@TableName("t_project_regional_position_data")
@Data
public class ProjectRegionalPositionData extends Model<ProjectRegionalPositionData> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 图片id
     */
	@TableField("map_id")
	private Integer mapId;
    /**
     * 采集时间
     */
	@TableField("collect_time")
	private Date collectTime;
    /**
     * 备注
     */
	private String comments;
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
    /**
     * 标签号
     */
	@TableField("label_no")
	private String labelNo;
    /**
     * 所属基站
     */
	@TableField("station_no")
	private String stationNo;
    /**
     * 人员信息
     */
	@TableField("identity_code")
	private String identityCode;




	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
