package com.xywg.equipment.monitor.modular.sjj.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@TableName("t_project_lift")
@Data
public class ProjectLift extends Model<ProjectLift> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 工程名称
     */
    @TableField("project_id")
    private Integer projectId;
    /**
     * 产权编号
     */
    private String identifier;
    /**
     * 规格型号
     */
    private String specification;
    /**
     * 产权单位
     */
    private String owner;
    /**
     * 制造厂家
     */
    private String manufactor;
    /**
     * 最大载重量
     */
    @TableField("max_weight")
    private BigDecimal maxWeight;
    /**
     * 最大载人数
     */
    @TableField("max_people")
    private Integer maxPeople;
    /**
     * 标准高度
     */
    @TableField("standard_height")
    private BigDecimal standardHeight;
    /**
     * 运行速度
     */
    private BigDecimal speed;
    /**
     * 在线状态
     */
    @TableField("is_online")
    private Integer isOnline;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;
    @TableField("device_no")
    private String deviceNo;

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
     * 组织结构
     */
    @TableField("org_id")
    private Integer orgId;

    private String name;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProjectLift{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", identifier=" + identifier +
                ", specification=" + specification +
                ", owner=" + owner +
                ", manufactor=" + manufactor +
                ", maxWeight=" + maxWeight +
                ", maxPeople=" + maxPeople +
                ", standardHeight=" + standardHeight +
                ", speed=" + speed +
                ", isOnline=" + isOnline +
                ", status=" + status +
                ", comments=" + comments +
                ", isDel=" + isDel +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", modifyTime=" + modifyTime +
                ", modifyUser=" + modifyUser +
                ", orgId=" + orgId +
                "}";
    }
}

