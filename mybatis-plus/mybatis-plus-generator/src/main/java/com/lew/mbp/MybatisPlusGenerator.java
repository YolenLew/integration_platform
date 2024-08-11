
/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.mbp;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.lew.mbp.config.MybatisPlusConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Yolen
 * @date 2024/7/6
 */
public class MybatisPlusGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisPlusGenerator.class);

    public static void main(String[] args) {
        // 代码生成器
        Properties properties = new Properties();
        try {
            properties.load(MybatisPlusGenerator.class.getResourceAsStream("/mybatis-plus.properties"));
        } catch (IOException e) {
            LOGGER.error("failed to load `mybatis-plus.properties` file", e);
            return;
        }
        MybatisPlusConfig mybatisPlusConfig = new MybatisPlusConfig(properties);
        AutoGenerator mpg = new AutoGenerator(mybatisPlusConfig.dataSourceConfig().build());
        mpg.global(mybatisPlusConfig.globalConfig());
        mpg.packageInfo(mybatisPlusConfig.packageConfig());
        mpg.strategy(mybatisPlusConfig.strategyConfig());
        mpg.execute();
    }
}
