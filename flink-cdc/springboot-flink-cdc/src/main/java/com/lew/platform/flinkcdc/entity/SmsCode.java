/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.io.Serializable;

/**
 * 表实体模型
 *
 * @author Yolen
 * @date 2024/8/11
 */
@Data
public class SmsCode implements Serializable {
    private Long id;

    private String code;

    private String phone;

    private DateTime createTime;

    private DateTime deadTime;

    private Integer revision;
}
