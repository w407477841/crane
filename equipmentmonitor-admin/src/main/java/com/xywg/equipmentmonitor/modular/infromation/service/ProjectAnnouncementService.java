package com.xywg.equipmentmonitor.modular.infromation.service;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectAnnouncement;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetEnvironment;
import com.xywg.equipmentmonitor.modular.infromation.vo.ProjectAnnouncementVO;

/**
 * <p>
 * 通知公告 服务类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
public interface ProjectAnnouncementService extends IService<ProjectAnnouncement> {
	   /**
     * <p>
     *新增通知公告
     * </p>
     *
     * @param c 
     * @return boolean
     */
    boolean insertProjectAnnouncement(ProjectAnnouncementVO c);
   /**
    * 编辑通知公告
    * @param c
    * @return
    */
	boolean updateProjectAnnouncement(ProjectAnnouncementVO c);
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
    boolean deleteProjectAnnouncement(List<? extends Serializable> idList);
    /**
     * 查询单条
     * @param id
     * @return
     */
    
	ProjectAnnouncementVO selectProjectAnnouncementOne(Serializable id);
	/**
	 * 查询列表
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectAnnouncement>>> getPageList(RequestDTO<ProjectAnnouncement> request);
    /**
	 *获取编码
	 * 
	 * @param code
	 * @return
 	 */
     String getDocumentCode(String code);
}
