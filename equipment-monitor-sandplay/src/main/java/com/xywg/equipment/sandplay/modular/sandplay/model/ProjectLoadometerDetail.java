package com.xywg.equipment.sandplay.modular.sandplay.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-10-19
 */
@TableName("t_project_loadometer_detail")
public class ProjectLoadometerDetail extends Model<ProjectLoadometerDetail> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer pid;
	private String status;
	private String licence;
	@TableField("name_material")
	private String nameMaterial;
	private String model;
	@TableField("weight_gross")
	private String weightGross;
	@TableField("weight_tare")
	private BigDecimal weightTare;
	@TableField("weight_net")
	private BigDecimal weightNet;
	@TableField("weight_invoice")
	private BigDecimal weightInvoice;
	private BigDecimal dweight;
	private String deliver;
	private String receiver;
	private String transport;
	private String weighman;
	@TableField("time_begin")
	private Date timeBegin;
	@TableField("time_end")
	private Date timeEnd;
	@TableField("is_del")
	private Integer isDel;
	@TableField("modify_time")
	private Date modifyTime;
	@TableField("modify_user")
	private Integer modifyUser;
    /**
     * 批次
     */
	private String flow;
	@TableField("r_id")
	private Integer rId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getNameMaterial() {
		return nameMaterial;
	}

	public void setNameMaterial(String nameMaterial) {
		this.nameMaterial = nameMaterial;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getWeightGross() {
		return weightGross;
	}

	public void setWeightGross(String weightGross) {
		this.weightGross = weightGross;
	}

	public BigDecimal getWeightTare() {
		return weightTare;
	}

	public void setWeightTare(BigDecimal weightTare) {
		this.weightTare = weightTare;
	}

	public BigDecimal getWeightNet() {
		return weightNet;
	}

	public void setWeightNet(BigDecimal weightNet) {
		this.weightNet = weightNet;
	}

	public BigDecimal getWeightInvoice() {
		return weightInvoice;
	}

	public void setWeightInvoice(BigDecimal weightInvoice) {
		this.weightInvoice = weightInvoice;
	}

	public BigDecimal getDweight() {
		return dweight;
	}

	public void setDweight(BigDecimal dweight) {
		this.dweight = dweight;
	}

	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getWeighman() {
		return weighman;
	}

	public void setWeighman(String weighman) {
		this.weighman = weighman;
	}

	public Date getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Date timeBegin) {
		this.timeBegin = timeBegin;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(Integer modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public Integer getrId() {
		return rId;
	}

	public void setrId(Integer rId) {
		this.rId = rId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectLoadometerDetail{" +
			"id=" + id +
			", pid=" + pid +
			", status=" + status +
			", licence=" + licence +
			", nameMaterial=" + nameMaterial +
			", model=" + model +
			", weightGross=" + weightGross +
			", weightTare=" + weightTare +
			", weightNet=" + weightNet +
			", weightInvoice=" + weightInvoice +
			", dweight=" + dweight +
			", deliver=" + deliver +
			", receiver=" + receiver +
			", transport=" + transport +
			", weighman=" + weighman +
			", timeBegin=" + timeBegin +
			", timeEnd=" + timeEnd +
			", isDel=" + isDel +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", flow=" + flow +
			", rId=" + rId +
			"}";
	}
}
