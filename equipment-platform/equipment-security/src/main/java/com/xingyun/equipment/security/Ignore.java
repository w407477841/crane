package com.xingyun.equipment.security;

import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:29 2019/7/25
 * Modified By : wangyifei
 */
public interface Ignore {

    void ignore(WebSecurity web);

}
