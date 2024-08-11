/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 数据操作类型枚举
 *
 * @author Yolen
 * @date 2024/8/10
 */
@Getter
@AllArgsConstructor
public enum OperatorTypeEnum {
    /**
     * 新增
     */
    INSERT(1, "CREATE"),
    /**
     * 修改
     */
    UPDATE(2, "UPDATE"),
    /**
     * 删除
     */
    DELETE(3, "DELETE"),
    ;

    private final Integer type;
    private final String operation;

    public static Integer getTypeByOperation(String operation) {
        return Arrays.stream(values()).filter(e -> e.getOperation().equalsIgnoreCase(operation)).findFirst()
            .map(OperatorTypeEnum::getType).orElse(0);
    }
}
