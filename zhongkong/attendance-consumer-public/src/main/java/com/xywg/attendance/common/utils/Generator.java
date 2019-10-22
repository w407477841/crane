package com.xywg.attendance.common.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjy
 * @date 2019/2/20
 * 代码自动生成 工具类
 */
public class Generator {
    private static String projectPath = "E:\\代码自动生成\\";
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */

    public static GlobalConfig gc(){
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(projectPath);
        gc.setAuthor("z");
        gc.setOpen(false);
        return gc;
    }
    public static DataSourceConfig dsc(){

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
        dsc.setUsername("ifa");
        dsc.setPassword("ifa");
        dsc.setUrl("jdbc:oracle:thin:@192.168.1.122:1522:sales");
        // dsc.setUrl("jdbc:oracle:thin:@//192.168.1.122:1522/sales");
        return dsc;
    }
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置

        mpg.setGlobalConfig(gc());

        // 数据源配置

        mpg.setDataSource(dsc());

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("system");
        pc.setParent("com.xywg.attendance.modular");
        pc.setEntity("model");
        pc.setMapper("dao");
        pc.setXml("dao.mapping");
        pc.setService("service");
        pc.setServiceImpl("service.impl");

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        // String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath +"/"+pc.getParent().replaceAll("\\.","/")+"/"+pc.getXml().replaceAll("\\.","/")+"/" + tableInfo.getEntityName() + "Mapper.xml" ;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setTablePrefix("t_");
       // strategy.setSuperEntityClass("com.xywg.iot.BaseModel");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(
                "project"
        );
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }

}
