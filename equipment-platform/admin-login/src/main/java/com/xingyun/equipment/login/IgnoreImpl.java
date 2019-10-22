package com.xingyun.equipment.login;

import com.xingyun.equipment.security.Ignore;
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

            web.ignoring()
                    // word导出
				.antMatchers("/admin-crane/word/download")

                .antMatchers("/admin-crane/craneToPdf/report")
            ;
    }
}
