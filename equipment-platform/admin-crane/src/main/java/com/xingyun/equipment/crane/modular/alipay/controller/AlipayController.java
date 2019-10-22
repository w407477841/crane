package com.xingyun.equipment.crane.modular.alipay.controller;

import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xingyun.equipment.crane.config.AliPayConfig;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.alipay.model.ProjectTopUp;
import com.xingyun.equipment.crane.modular.alipay.model.ProjectTopUpDTO;
import com.xingyun.equipment.crane.modular.alipay.service.IProjectTopUpService;
import com.xingyun.equipment.crane.modular.alipay.service.impl.AliPay;
import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 阿里支付接口入口
 */
@Controller
@RequestMapping("/admin-crane/alipay")
public class AlipayController {

    @Autowired
    AliPay aliPay;

    @Autowired
    IProjectTopUpService projectTopUpService;

    @Autowired
    UserServiceImpl userService;

    @Value("${iot.url}")
    private String wsUrl;
    /**
     * 生成订单页面
     * @param requestDTO
     * @return
     */
    @PostMapping("topay")
    @ResponseBody
    public ResultDTO<ProjectTopUpDTO> topay(@RequestBody RequestDTO<ProjectTopUpDTO> requestDTO){
      String orderStr=  aliPay.getAliPayOrderStr(requestDTO.getBody());
       return new ResultDTO(true,orderStr,"生成订单成功");
    }




    @PostMapping("notifyUrl")
    public String notify_url(HttpServletRequest request){
        System.out.println(new Date()+":支付宝返回异步处理");
        Map<String, String> paramsMap = convertRequestParamsToMap(request);
        String trade_status = paramsMap.get("trade_status");
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);
            if(signVerified){
                if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                    //处理自己系统的业务逻辑，如：将支付记录状态改为成功，需要返回一个字符串success告知支付宝服务器
                    String outTradeNo =paramsMap.get("out_trade_no");
                    System.out.println(new Date()+",当前订单号:"+outTradeNo);
                    EntityWrapper<ProjectTopUp> wrapper = new EntityWrapper<>();
                    wrapper.eq("out_trade_no",outTradeNo);
                  List<ProjectTopUp> list= projectTopUpService.selectList(wrapper);
                  for(ProjectTopUp topUp:list){
                      topUp.setIsDel(0);

                  }
                    projectTopUpService.updateBatchById(list);
                    return "success";
                } else {
                    //支付失败不处理业务逻辑
                    return "failure";
                }
            }else {
                //签名验证失败不处理业务逻辑
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }

    //注意同步返回结果是以get请求形式返回的
    @GetMapping("returnUrl")
    public String return_url(HttpServletRequest request){
        System.out.println(new Date()+":支付宝返回同步处理");
        Map<String, String> paramsMap = convertRequestParamsToMap(request);
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);
//             boolean signVerified=true;
            if(signVerified){
                //处理自己系统的业务逻辑，如：将支付记录状态改为成功，需要返回一个字符串success告知支付宝服务器，同步时不处理具体业务，放到异步处理里做
                String outTradeNo =paramsMap.get("out_trade_no");
                System.out.println(new Date()+",当前订单号:"+outTradeNo);
                EntityWrapper<ProjectTopUp> wrapper = new EntityWrapper<>();
                wrapper.eq("out_trade_no",outTradeNo);
                List<ProjectTopUp> list= projectTopUpService.selectList(wrapper);
                EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
                userEntityWrapper.eq("id",list.get(0).getCreateUser()).eq("is_del",0);
                User user= userService.selectById(list.get(0).getCreateUser());

                for(ProjectTopUp topUp:list){
                    topUp.setIsDel(0);

                }
                projectTopUpService.updateBatchById(list);
                System.out.println("支付成功");
                //跳转支付成功界面
//                CustomWebSocket.sendInfo("支付成功");
//                return "redirect:http://crane.jsxywg.cn";
                return "success";
            }else {
                //跳转支付失败界面
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "success";
    }

    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap();
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        Iterator var3 = entrySet.iterator();

        while(true) {
            while(var3.hasNext()) {
                Map.Entry<String, String[]> entry = (Map.Entry)var3.next();
                String name = (String)entry.getKey();
                String[] values = (String[])entry.getValue();
                int valLen = values.length;
                if(valLen == 1) {
                    retMap.put(name, values[0]);
                } else if(valLen <= 1) {
                    retMap.put(name, "");
                } else {
                    StringBuilder sb = new StringBuilder();
                    String[] var9 = values;
                    int var10 = values.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        String val = var9[var11];
                        sb.append(",").append(val);
                    }

                    retMap.put(name, sb.toString().substring(1));
                }
            }

            return retMap;
        }
    }

    @GetMapping("queryAlipay")
    public String queryAlipay(String outTradeNo){
     return  aliPay.queryAlipay(outTradeNo);
    }
}
