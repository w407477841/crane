package com.xywg.equipment.monitor.iot.modular.romote.dao;

import com.xywg.equipment.monitor.iot.modular.romote.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 */
public interface UserMapper  {


    List<User> getListUserByIds(@Param("ids") String[] ids);
}
