package com.xywg.equipmentmonitor.modular.common.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description 文件上传实体类(t_file_upload)
 * @author caolj
 * @date 2017年12月21日 下午11:11:08
 *
 */

@Data
public class FileUpload implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键，非空
	 */
	private int id;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件大小
	 */
	private String fileSize;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件类型
	 */
	private String fileType;
	/**
	 * 文件模型
	 */
	private String fileModel;
	/**
	 * 创建时间
	 */
	private String fileCreateTime;
	/**
	 * 删除标识 0：未删除 1：已删除
	 */
	private String fileIsDel;
	/**
	 * 文档编写人
	 */
	private String fileAuthor;
	/**
	 * 备注
	 */
	private String fileComments;
	/**
	 * 单据编号，关联单据表
	 */
	private String documentCode;

}
