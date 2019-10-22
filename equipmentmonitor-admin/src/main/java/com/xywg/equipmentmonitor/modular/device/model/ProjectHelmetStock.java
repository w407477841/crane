package com.xywg.equipmentmonitor.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * 设备库存
 * @author hjy
 * @since 2018-11-23
 */
@TableName("t_project_device_stock")
@Data
public class ProjectHelmetStock extends BaseEntity<ProjectHelmetStock> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * imei
     */
	@TableField(value = "device_no")
	private String deviceNo;


	private Integer type;

	private Integer status;

	private String comments;

	private String gprs;

	@TableField(exist = false)
	private Integer projectId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
