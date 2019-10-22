package com.xywg.equipmentmonitor.modular.system.controller;

import cn.hutool.json.JSONObject;
import com.xywg.equipmentmonitor.config.properties.CacheProperties;
import com.xywg.equipmentmonitor.config.properties.JwtProperties;
import com.xywg.equipmentmonitor.config.properties.XywgProperties;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.security.dto.UserDTO;
import com.xywg.equipmentmonitor.core.security.enums.LoginType;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.core.security.token.MyUsernameAuthenticationToken;
import com.xywg.equipmentmonitor.core.security.vo.UserVO;
import com.xywg.equipmentmonitor.modular.system.service.IOperationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum.NO_ORG;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:52 2018/9/7
 * Modified By : wangyifei
 */
@Controller
@RequestMapping("ssdevice/")
public class LoginController {
    @Autowired
    XywgProperties   xywgProperties;
    @Autowired
    SecurityService  securityService;
    @Autowired
    BCryptPasswordEncoder   passwordEncoder;
    @Autowired
    JwtProperties   jwtProperties;
    @Autowired
    IOperationService   operationService;

@ApiOperation("登录")
@PostMapping("login")
@ResponseBody
public Object login(@RequestBody UserDTO user){
    //用户名
    String username = user.getUsername();
    //密码
    String password  = user.getPassword();
    //登录类型  1手机    2 用户名
    Integer loginType = user.getLoginType();

    if(loginType.intValue()==LoginType.username.getType()){


         user = (UserDTO) securityService.loadUserByUsername(username);
        if(user ==null){

            return ResultDTO.factory(ResultCodeEnum.NOUSER);
    //            throw new UsernameNotFoundException("用户名不存在");
        }
        if(passwordEncoder.matches(password,user.getPassword())){

            Claims claims =new DefaultClaims();
            Long lasttime=System.currentTimeMillis() + jwtProperties.getExpiration();

            //持有者
            claims.put("sub",username);
            //过期时间
            claims.put("exp", new Date(lasttime));
            //权限
            claims.put("auths",null);
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(lasttime))
                    .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                    .compact();

            Const.token.set(token.split("\\.")[2]);
            UserVO userVO = securityService.getUser(username,CacheProperties.USER_KEY_PREFIX);
            //删除之前的集团缓存，防止切换集团后无反应
            //userService.removeOrgids(user.getId());
            if(userVO.getGroups()==null||userVO.getGroups().size()==0){
                // 没有集团
                return ResultDTO.factory(NO_ORG);
            } else{
                JSONObject   resultJson = new JSONObject();
                resultJson.put("user",userVO);
                resultJson.put("code",200);
                resultJson.put("message","登录成功" );
                resultJson.put("auth",jwtProperties.getAuthPath()+" " + token);
                return resultJson;
            }
        }else{
            return ResultDTO.factory(ResultCodeEnum.UNAUTHORIZED);
            //throw new BadCredentialsException("密码错误");
        }


    }else if(loginType.intValue()==LoginType.phone.getType()){
        if(xywgProperties.getPhoneValid()){
            //已开启手机号+验证码认证

        }else{
            //未开启手机号+验证码认证

        }
    }






    return null;
    }
    @GetMapping("auth")
    @ResponseBody
    public Object auth(@RequestParam("username") String username,@RequestParam("password") String password) {

        UserDTO user = (UserDTO) securityService.loadUserByUsername(username);
        if (user == null) {
            return ResultDTO.factory(ResultCodeEnum.UNAUTHORIZED);
        }
        if (passwordEncoder.matches(password, user.getPassword())) {

            Claims claims = new DefaultClaims();
            Long lasttime = System.currentTimeMillis() + jwtProperties.getExpiration();

            //持有者
            claims.put("sub", username);
            //过期时间
            claims.put("exp", new Date(lasttime));
            //权限
            claims.put("auths", null);
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(lasttime))
                    .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                    .compact();

            Const.token.set(token.split("\\.")[2]);
            UserVO userVO = securityService.getUser(username, CacheProperties.USER_KEY_PREFIX);
            //删除之前的集团缓存，防止切换集团后无反应
            //userService.removeOrgids(user.getId());

                JSONObject resultJson = new JSONObject();
                resultJson.put("code", 200);
                resultJson.put("message", "登录成功");
                resultJson.put("auth", jwtProperties.getAuthPath() + " " + token);
                return resultJson;



        }else{
            return ResultDTO.factory(ResultCodeEnum.UNAUTHORIZED);
        }

    }



}
