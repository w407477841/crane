package com.xingyun.equipment;

import com.xingyun.equipment.security.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:48 2019/7/25
 * Modified By : wangyifei
 */
@Component
public class IgnoreImpl implements Ignore {


    @Override
    public void ignore(WebSecurity web) {
        web.ignoring().antMatchers("/fff");
    }
}
