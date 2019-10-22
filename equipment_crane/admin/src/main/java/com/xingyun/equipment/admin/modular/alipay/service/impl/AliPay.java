package com.xingyun.equipment.admin.modular.alipay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.xingyun.equipment.admin.config.AliPayConfig;
import com.xingyun.equipment.admin.modular.alipay.model.AlipayBean;
import com.xingyun.equipment.admin.modular.alipay.model.ProjectDeviceDTO;
import com.xingyun.equipment.admin.modular.alipay.model.ProjectTopUp;
import com.xingyun.equipment.admin.modular.alipay.model.ProjectTopUpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AliPay {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProjectTopUpServiceImpl projectTopUpService;

    public AlipayClient buildClient(){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.GATEWAY_URL, AliPayConfig.APP_ID, AliPayConfig.PRIVATE_KEY, "json", AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, "RSA2");
        return alipayClient;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public String getAliPayOrderStr(ProjectTopUpDTO dto) {

        AlipayBean bean= new AlipayBean();
        bean.setBody("设备充值");
        bean.setSubject("设备充值");
//        bean.setTotal_amount(String.valueOf(dto.getPrice()*dto.getDevices().size()*dto.getChargeTime()));
        bean.setTotal_amount(String.valueOf(0.01));
        //最终返回加签之后的，app需要传给支付宝app的订单信息字符串
        String orderString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNum =sdf.format(new Date());
        bean.setOut_trade_no("XYWG"+dateNum+String.format("%02d",new Random().nextInt(100)));
        saveData(dto,bean.getOut_trade_no());
        logger.info("==================支付宝下单,商户订单号为："+bean.getOut_trade_no());


        AlipayTradePagePayResponse response=null;
        try{
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
            AlipayClient alipayClient = buildClient();

            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradePagePayRequest ali_request = new AlipayTradePagePayRequest();

            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

            //业务参数传入,可以传很多，参考API
            //model.setPassbackParams(URLEncoder.encode(request.getBody().toString())); //公用参数（附加数据）
            model.setBody(bean.getBody());                       //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
            model.setSubject(bean.getSubject());                 //商品名称
            model.setOutTradeNo(bean.getOut_trade_no());           //商户订单号(自动生成)
            model.setTotalAmount(bean.getTotal_amount());         //支付金额
            model.setProductCode(bean.getProduct_code());        	  //销售产品码（固定值）
            logger.info("====================异步通知的地址为："+AliPayConfig.NOTIFY_URL);
            ali_request.setBizContent(JSON.toJSONString(bean));
            ali_request.setReturnUrl(AliPayConfig.RETURN_URL);
            ali_request.setNotifyUrl(AliPayConfig.NOTIFY_URL);
            // 这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(ali_request); //返回支付宝订单信息(预处理)
             response=  alipayClient.pageExecute(ali_request);
            logger.info("返回"+response);
            orderString=response.getBody();//就是orderString 可以直接给APP请求，无需再做处理。


        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.info("与支付宝交互出错，未能生成订单，请检查代码！");
        }

        if(response.isSuccess()){
            return orderString;
        }else{
           return "";
        }

    }

    public String queryAlipay(String outTradeNo){
        AlipayClient alipayClient = buildClient();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryResponse response=null;
        request.setBizContent("{" +
                "   \"out_trade_no\":\"XYWG2019062915230567\"," +
                "   \"trade_no\":\"\"" +
                "  }");
        try {
            response   = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.print(response.getBody());
        return response.getBody();
    }

    /**
     * 插入支付记录表
     * @param dto
     * @return
     */
    public boolean saveData(ProjectTopUpDTO dto,String outTradeNo){
        ProjectTopUp topUp =new ProjectTopUp();
        BeanUtils.copyProperties(dto,topUp);
        List<ProjectTopUp> list=new ArrayList<>();
        List<ProjectDeviceDTO> deviceDTOS =dto.getDevices();

        for(ProjectDeviceDTO device:deviceDTOS){
            topUp.setChargeMoney(new BigDecimal (dto.getPrice()*topUp.getChargeTime()));
            topUp.setCraneNo(device.getCraneNo());
            topUp.setDeviceNo(device.getDeviceNo());
            topUp.setGprs(device.getGprs());
            topUp.setProjectId(device.getProjectId());
            topUp.setProjectName(device.getProjectName());
            topUp.setOutTradeNo(outTradeNo);
            topUp.setIsDel(1);
            topUp.setStatus(0);
            list.add(topUp);
        }

     return   projectTopUpService.insertBatch(list);
    }


}
