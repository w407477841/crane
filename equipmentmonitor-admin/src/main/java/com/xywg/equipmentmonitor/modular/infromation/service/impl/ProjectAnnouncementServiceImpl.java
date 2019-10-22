package com.xywg.equipmentmonitor.modular.infromation.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.dao.ProjectAnnouncementMapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectAnnouncement;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectAnnouncementFile;
import com.xywg.equipmentmonitor.modular.infromation.service.ProjectAnnouncementService;
import com.xywg.equipmentmonitor.modular.infromation.vo.ProjectAnnouncementVO;
import com.xywg.equipmentmonitor.modular.system.util.Constant;

/**
 * <p>
 * 通知公告 服务实现类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
@Service
public class ProjectAnnouncementServiceImpl extends ServiceImpl<ProjectAnnouncementMapper, ProjectAnnouncement> implements ProjectAnnouncementService {
	@Autowired
	private ProjectAnnouncementMapper projectAnnouncementMapper;
	/**
	 * 新增
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insertProjectAnnouncement(ProjectAnnouncementVO c) {
		try {
			ProjectAnnouncement projectAnnouncement = c.getProjectAnnouncement();
			 Calendar ca=java.util.Calendar.getInstance();
				SimpleDateFormat  cal=new SimpleDateFormat("yyyyMMdd");
				 Integer code = Integer.parseInt(cal.format(ca.getTime()));
				 
				 projectAnnouncement.setCode(getDocumentCode(code.toString()));
				 
				
				 projectAnnouncement.setOrgId( Const.orgId.get());
			projectAnnouncementMapper.insertProjectAnnouncement(projectAnnouncement);
			
			if(c.getProjectAnnouncementFile().size()> 0) {
				List<ProjectAnnouncementFile>  projectAnnouncementFile = c.getProjectAnnouncementFile();
				for(int i=0;i<projectAnnouncementFile.size();i++) {
					projectAnnouncementFile.get(i).setAnnouncementId(projectAnnouncement.getId());
					projectAnnouncementMapper.insertProjectAnnouncementFile(projectAnnouncementFile.get(i));
				}
				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}
	/**
	 * 编辑
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateProjectAnnouncement(ProjectAnnouncementVO c) {
		try {
			
			ProjectAnnouncement projectAnnouncement = c.getProjectAnnouncement();
			
			projectAnnouncementMapper.updateProjectAnnouncement(projectAnnouncement);
			projectAnnouncementMapper.deleteProjectAnnouncementFile(projectAnnouncement.getId());
			
			if(c.getProjectAnnouncementFile().size()> 0) {
				List<ProjectAnnouncementFile>  projectAnnouncementFile = c.getProjectAnnouncementFile();
				for(int i=0;i<projectAnnouncementFile.size();i++) {
					projectAnnouncementFile.get(i).setAnnouncementId(projectAnnouncement.getId());
					projectAnnouncementMapper.insertProjectAnnouncementFile(projectAnnouncementFile.get(i));
				}
				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}
	 /**
	  * 获取编号
	  */
	 
	 
	 @Override
		public String getDocumentCode(String code) {
			
			String currentCode = projectAnnouncementMapper.selectMaxCode(code);
			if (currentCode == null || "".equals(currentCode) || "null".equals(currentCode)) {
				currentCode = "0001";
			} else {
				currentCode = String.valueOf(Integer.valueOf(currentCode) + 1);
			}
			String num = "";
			for (int i = 0; i < 4 - (currentCode.length()); i++) {
				num += "0";
			}
			
			return code  + num + currentCode;
		}

	 /**
     * 查询单条
     */
	@Override
	public ProjectAnnouncementVO selectProjectAnnouncementOne(Serializable id) {
		ProjectAnnouncementVO res = new ProjectAnnouncementVO();
		res.setProjectAnnouncement(projectAnnouncementMapper.selectProjectAnnouncement(id));
		res.setProjectAnnouncementFile(projectAnnouncementMapper.selectProjectAnnouncementFile(id));
		return res;
	}
	  /**
    * 删除
*/
       @Transactional(rollbackFor = Exception.class)
       @Override
       public boolean deleteProjectAnnouncement(List<? extends Serializable> ids) {
       	try {
       		
       		
       		projectAnnouncementMapper.deletesProjectAnnouncement(ids);
       		projectAnnouncementMapper.deletesProjectAnnouncementFile(ids);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
         }
     /**
      * 列表
      * @param request
      * @return
      */
       @Override
       public ResultDTO<DataDTO<List<ProjectAnnouncement>>> getPageList(RequestDTO<ProjectAnnouncement> request) {
           Page<ProjectAnnouncement> page = new Page<ProjectAnnouncement>(request.getPageNum(), request.getPageSize());
//           request.setOrgIds(Const.orgIds.get());
           List<ProjectAnnouncement> list = projectAnnouncementMapper.selectPageList(page,request);
           return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
       }

}
