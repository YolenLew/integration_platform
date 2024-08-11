/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.mbp.config;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

/**
 * 自定义类型转换 解决mybatis plus中tinyint(1)映射成boolean类型的问题
 *
 * @author Yolen
 * @date 2023/4/5
 */
public class MysqlTypeConvertCustom extends MySqlTypeConvert implements ITypeConvert {

    private static final String TINYINT_TYPE = "tinyint(1)";

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String str = fieldType.toLowerCase();
        if (str.contains(TINYINT_TYPE)) {
            return DbColumnType.INTEGER;
        }
        return super.processTypeConvert(globalConfig, fieldType);
    }
}
