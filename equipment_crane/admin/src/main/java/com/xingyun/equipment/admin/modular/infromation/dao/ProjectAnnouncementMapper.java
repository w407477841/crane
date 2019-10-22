package com.xingyun.equipment.admin.modular.infromation.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectAnnouncement;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectAnnouncementFile;

/**
 * <p>
 * 通知公告 Mapper 接口
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
public interface ProjectAnnouncementMapper extends BaseMapper<ProjectAnnouncement> {
	/**
	 * 通知公告列表
	 * 
	 * @param rowBounds
	 * @param wrapper
	 * @return
	 */
	List<ProjectAnnouncement> selectPageList(RowBounds rowBounds,RequestDTO<ProjectAnnouncement> request);
     /**
      * 新增
      * @param c
      * @return
      */
	int insertProjectAnnouncement(ProjectAnnouncement c);
	 /**
     * 新增文件
     * @param c
     * @return
     */
	int insertProjectAnnouncementFile(ProjectAnnouncementFile c);
	/**
	 * 编辑
	 * @param c
	 * @return
	 */
	int updateProjectAnnouncement(ProjectAnnouncement c);
	/**
	 * 删除详细
	 * @param id
	 * @return
	 */
	int deleteProjectAnnouncementFile(@Param ("id")Integer id);
	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	ProjectAnnouncement selectProjectAnnouncement(@Param ("id")Serializable id);
	/**
	 * 查询单条文件
	 * @param id
	 * @return
	 */
	List<ProjectAnnouncementFile> selectProjectAnnouncementFile(@Param ("id")Serializable id);
	
	 /**
     * 物理删除
     * @param ids
     * @return
     */
    boolean deletesProjectAnnouncement(@Param("ids") List<? extends Serializable>ids);

	 /**
    * 物理删除文件
    * @param ids
    * @return
    */
    boolean deletesProjectAnnouncementFile(@Param("ids") List<? extends Serializable>ids);
    /**
     * 查编号
     * @param code
     * @return
     */
    String selectMaxCode(@Param("code") String code);
}
