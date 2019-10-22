package com.xingyun.equipment.admin.modular.infromation.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectMessageModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短信推送
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-20
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectMessageModelVO extends ProjectMessageModel {

   private String deviceTypeName;

}
