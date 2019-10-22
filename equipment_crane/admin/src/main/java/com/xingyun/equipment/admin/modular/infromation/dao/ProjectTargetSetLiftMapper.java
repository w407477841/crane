package com.xingyun.equipment.admin.modular.infromation.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetLift;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-08-21
 */
public interface ProjectTargetSetLiftMapper extends BaseMapper<ProjectTargetSetLift> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectTargetSetLift> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    /**
     * 判重
     * @param request
     * @return
     */
    int checkBySpecificationAndManufactor(@Param("t") RequestDTO<ProjectTargetSetLift> request);

    /**
     * 占用
     * @param map
     * @return
     * @throws Exception
     */
    Integer plusCallTimes(Map<String,Object> map) throws Exception;

    /**
     * 解除占用
     * @param projectLift
     * @return
     * @throws Exception
     */
    Integer minusCallTimes(@Param("list") List<ProjectLift> projectLift);
}
