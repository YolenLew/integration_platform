/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Yolen
 * @date 2024/8/11
 */
@Getter
@AllArgsConstructor
public enum CdcTableNameEnum {
    SMS_CODE("sms_code"),
    ;

    private final String tableName;
}
