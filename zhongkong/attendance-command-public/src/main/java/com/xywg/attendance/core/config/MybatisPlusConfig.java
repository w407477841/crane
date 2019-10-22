package com.xywg.attendance.core.config;

import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * MybatisPlus配置
 *
 * @author wangcw
 * @Date 2017/5/20 21:58
 */
@Configuration
@MapperScan(basePackages = {"**.dao"})
public class MybatisPlusConfig {
    
    
    /***
	 * plus 的性能能优化
	 * @return
     */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
		/*<!-- SQL 执行性能分析，开发环境使用，线上不推荐 maxTime 指的 sql 大执行时? -->*/
		//performanceInterceptor.setMaxTime(1000);
		/*<!--SQL是否格式�? 默认false-->*/
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}

	/**
	 *	 mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		//page.setDialectType("sqlserver");
		return page;
	}
    

	/**
	 *
	 * @return
	 */

	@Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}
