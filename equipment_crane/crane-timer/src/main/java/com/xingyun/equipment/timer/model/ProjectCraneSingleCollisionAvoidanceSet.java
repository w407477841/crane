package com.xingyun.equipment.timer.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 塔吊单区防碰撞
 *
 * @author hjy
 */
@Data
@TableName("t_project_crane_single_collision_avoidance_set")
public class ProjectCraneSingleCollisionAvoidanceSet extends Model<ProjectCraneSingleCollisionAvoidanceSet> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;

    /**
     * 工程名称
     */
    @TableField("project_id")
    private Integer projectId;
    /**
     * 起始角度
     */
    @TableField("start_angle")
    private BigDecimal startAngle;

    /**
     * 终止角度
     */
    @TableField("end_angle")
    private BigDecimal endAngle;

    /**
     * 起始高度
     */
    @TableField("start_height")
    private BigDecimal startHeight;

    /**
     * 终止高度
     */
    @TableField("end_height")
    private BigDecimal endHeight;

    /**
     * 起始幅度
     */
    @TableField("start_range")
    private BigDecimal startRange;

    /**
     * 终止幅度
     */
    @TableField("end_range")
    private BigDecimal endRange;


    /**
     * 设置状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 区域编号
     */
    @TableField("page_range")
    private Integer pageRange;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 删除标志
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createUserName;
    /**
     * 修改日期
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField(value = "modify_user", fill = FieldFill.UPDATE)
    private Integer modifyUser;

    public ProjectCraneSingleCollisionAvoidanceSet(String deviceNo, Integer projectId, BigDecimal startAngle, BigDecimal endAngle, BigDecimal startHeight, BigDecimal endHeight, BigDecimal startRange, BigDecimal endRange, Integer status, Integer pageRange, Integer isDel, Date createTime) {
        this.deviceNo = deviceNo;
        this.projectId = projectId;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.startRange = startRange;
        this.endRange = endRange;
        this.status = status;
        this.pageRange = pageRange;
        this.isDel = isDel;
        this.createTime = createTime;
    }
    public ProjectCraneSingleCollisionAvoidanceSet() {
    }
}
