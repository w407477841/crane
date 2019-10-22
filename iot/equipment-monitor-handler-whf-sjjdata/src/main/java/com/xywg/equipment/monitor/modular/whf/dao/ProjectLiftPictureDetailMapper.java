package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDataModel;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftPictureDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyj
 * @since 2019-08-02
 */
public interface ProjectLiftPictureDetailMapper extends BaseMapper<ProjectLiftPictureDetail> {

   void insertImg(@Param("t")ProjectLiftPictureDetail detail,@Param("month")String month);

}
