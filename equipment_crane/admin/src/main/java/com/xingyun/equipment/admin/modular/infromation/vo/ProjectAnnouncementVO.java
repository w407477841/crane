package com.xingyun.equipment.admin.modular.infromation.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectAnnouncement;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectAnnouncementFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通知公告
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectAnnouncementVO  extends ProjectAnnouncement{
	

    private String orgName;
    private String createUserName;
	private ProjectAnnouncement projectAnnouncement; 
	private List<ProjectAnnouncementFile> projectAnnouncementFile ; 


}
