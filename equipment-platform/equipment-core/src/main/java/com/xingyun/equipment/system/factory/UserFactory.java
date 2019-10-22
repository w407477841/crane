package com.xingyun.equipment.system.factory;

import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.transfer.UserDto;
import org.springframework.beans.BeanUtils;


/**
 * 用户创建工厂
 *
 * @author wangcw
 * @date 2017-05-05 22:43
 */
public class UserFactory {

    public static User createUser(UserDto userDto){
        if(userDto == null){
            return null;
        }else{
            User user = new User();
            BeanUtils.copyProperties(userDto,user);
            return user;
        }
    }
}
