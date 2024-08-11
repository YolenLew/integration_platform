/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.mbp.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig.Builder;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Properties;

/**
 * @author Yolen
 * @date 2023/4/5
 */
public class MybatisPlusConfig {

    private final Properties properties;

    public MybatisPlusConfig(Properties properties) {
        this.properties = properties;
    }

    /**
     * 数据源配置
     *
     * @return DataSourceConfig
     */
    public DataSourceConfig.Builder dataSourceConfig() {
        DataSourceConfig.Builder builder =
            new DataSourceConfig.Builder(properties.getProperty("url"), properties.getProperty("username"),
                properties.getProperty("password"));
        builder.typeConvert(new MysqlTypeConvertCustom());
        return builder;
    }

    /**
     * 包配置
     *
     * @return PackageConfig
     */
    public PackageConfig packageConfig() {
        PackageConfig.Builder packageBuilder = new PackageConfig.Builder();
        packageBuilder.parent(properties.getProperty("package.parent"))
            .moduleName(properties.getProperty("package.moduleName")).entity(properties.getProperty("package.entity"))
            .controller(properties.getProperty("package.controller"))
            .service(properties.getProperty("package.service"))
            .serviceImpl(properties.getProperty("package.service.impl"))
            .mapper(properties.getProperty("package.mapper")).xml(properties.getProperty("package.xml"));
        return packageBuilder.build();
    }

    /**
     * 策略配置
     *
     * @return StrategyConfig
     */
    public StrategyConfig strategyConfig() {
        StrategyConfig.Builder strategyBuilder = new StrategyConfig.Builder();
        strategyBuilder.entityBuilder().naming(NamingStrategy.underline_to_camel).enableLombok()
            .columnNaming(NamingStrategy.underline_to_camel).controllerBuilder().enableRestStyle()
            // 禁止生成service接口
            .serviceBuilder().disableService()
            .formatServiceImplFileName(properties.getProperty("strategy.service.impl.fileName"));
        // 支持指定表
        String tables = properties.getProperty("strategy.include");
        if (tables != null && tables.length() > 0) {
            String[] includeTables = tables.trim().split(",");
            strategyBuilder.addInclude(includeTables);
        }
        return strategyBuilder.build();
    }

    /**
     * 全局配置
     *
     * @return GlobalConfig
     */
    public GlobalConfig globalConfig() {
        GlobalConfig.Builder globalBuilder = new Builder();
        String projectPath = System.getProperty("user.dir");
        globalBuilder.outputDir(projectPath + properties.getProperty("globalConfig.outputDir")).enableSwagger()
            .disableServiceInterface().disableOpenDir();
        return globalBuilder.build();
    }
}
