package com.xywg.equipmentmonitor.config.web;
//package com.xywg.admin.config.web;
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.xywg.admin.auth.filter.AuthFilter;
//import com.xywg.admin.auth.security.DataSecurityAction;
//import com.xywg.admin.auth.security.impl.Base64SecurityAction;
//import com.xywg.admin.config.properties.XywgProperties;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * web配置
// *
// * @author wangcw
// * @date 2017-08-23 15:48
// */
////@Configuration
//public class WebRestConfig extends WebMvcConfigurerAdapter {
//
//    @Bean
//    @ConditionalOnProperty(prefix = XywgProperties.PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
//    public AuthFilter jwtAuthenticationTokenFilter() {
//        return new AuthFilter();
//    }
//
//    @Bean
//    public DataSecurityAction dataSecurityAction() {
//        return new Base64SecurityAction();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//
//
//	@Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        //1.需要定义一个convert转换消息的对象;
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
//        		                      SerializerFeature.WriteMapNullValue,
//        		                      SerializerFeature.WriteNullStringAsEmpty,
//        		                      SerializerFeature.DisableCircularReferenceDetect,
//        		                      SerializerFeature.WriteNullListAsEmpty);
//        //3处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        //4.在convert中添加配置信息.
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        //5.将convert添加到converters当中.
//        converters.add(fastJsonHttpMessageConverter);
//    }
//
//}