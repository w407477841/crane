package com.xywg.equipmentmonitor.generator.action.config;

import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 默认的代码生成的配置
 *
 * @author wangcw
 * @date 2017-10-28-下午8:27
 */
public class GunsGeneratorConfig extends AbstractGeneratorConfig {

    protected void globalConfig() {
        globalConfig.setOutputDir("D:\\mp");//写自己项目的绝对路径,注意具体到java目录
        globalConfig.setFileOverride(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setOpen(false);
        globalConfig.setAuthor("hy");
    }

    protected void dataSourceConfig() {
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("Xingyun*_001");
        dataSourceConfig.setUrl("jdbc:mysql://118.31.69.25:3306/datanode20190625?characterEncoding=utf8");
    }

    protected void strategyConfig() {
        // 此处可以修改为您的表前缀
        strategyConfig.setTablePrefix(new String[]{"t_"});
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setInclude(new String[]{
                "t_project_crane"
        		});
    }

    
    protected void packageConfig() {
        packageConfig.setParent(null);
        packageConfig.setEntity("com.xywg.equipment.monitor.iot.modular.crane.model");
        packageConfig.setMapper("com.xywg.equipment.monitor.iot.modular.crane.dao");
        packageConfig.setXml("com.xywg.equipment.monitor.iot.modular.crane.dao.mapping");
    }

    protected void contextConfig() {
        contextConfig.setProPackage("com.xywg.equipment.monitor.iot");
        contextConfig.setCoreBasePackage("com.xywg.equipment.monitor.iot.modular");
        contextConfig.setBizChName("塔吊");
        contextConfig.setBizEnName("crane");
        contextConfig.setModuleName("crane");
        //写自己项目的绝对路径
        contextConfig.setProjectPath("D:\\mp\\equipmentmonitor");
        contextConfig.setEntityName("crane");
        //这里写已有菜单的名称,当做父节点
        sqlConfig.setParentMenuName(null);

        /**
         * mybatis-plus 生成器开关
         */
        contextConfig.setEntitySwitch(true);
        contextConfig.setDaoSwitch(true);
        contextConfig.setServiceSwitch(true);

        /**
         * guns 生成器开关
         */
        contextConfig.setControllerSwitch(true);
        contextConfig.setIndexPageSwitch(false);
        contextConfig.setAddPageSwitch(false);
        contextConfig.setEditPageSwitch(false);
        contextConfig.setJsSwitch(false);
        contextConfig.setInfoJsSwitch(false);
        contextConfig.setSqlSwitch(false);
        
    }

    @Override
    protected void config() {
        globalConfig();
        dataSourceConfig();
        strategyConfig();
        packageConfig();
        contextConfig();
    }
}
