package com.xingyun.equipment;




import com.xingyun.equipment.system.vo.UserVo;

import java.util.List;

/**
 * 系统常量
 *
 * @author wangcw
 * @date 2017年2月12日 下午9:42:53
 */
public interface Const {

    /**
     * 管理员角色的名字
     */
    String ADMIN_NAME = "administrator";

    /**
     * 管理员id
     */
    Integer ADMIN_ID = 1;

    /**
     * 超级管理员角色id
     */
    Integer ADMIN_ROLE_ID = 1;

    /**
     * 接口文档的菜单名
     */
    String API_MENU_NAME = "接口文档";

    /**
     * 验证码有效时间(分钟)
     */
    Integer SIGN_CODE_VALID_TIME = 5;
    

    

    /**
     * 当前用户
     */
     ThreadLocal<UserVo> currUser =  new ThreadLocal();
    /**
     * 当前选中 的 组织
     */
     ThreadLocal<Integer> orgId  =new ThreadLocal();

     ThreadLocal<List<Integer>> orgIds = new ThreadLocal();

    /**
     *  当前令牌
     */
    ThreadLocal<String>  token   = new ThreadLocal();



    public static  final String  DEVICE_INFO_PREFIX = "device_platform:str:deviceinfo:";

    public static  final String  DEVICE_PLATFORM = "device_platform:devices:";

    public static  final String  DEVICE_CURRENT = "device_platform:current:";
}
