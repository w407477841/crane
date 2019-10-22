package com.xingyun.equipment.simpleequipment.receive;

import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * MybatisPlus配置
 * 由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
 * @author wangyifei
 * Date 2017/5/20 21:58
 */
@Configuration
//@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = {"com.xingyun.equipment.simpleequipment.receive.modular.*.dao"})
public class MybatisPlusConfig {
    
    
    /***
	 * plus 的性能优化
	 * @return  性能优化
     */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		System.out.println("==============");
		PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}

	/**
	 *	 mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		return page;
	}
    



    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {

    	return new OptimisticLockerInterceptor();
    }

}
