
/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.strategy;

import cn.hutool.json.JSONUtil;
import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import com.lew.platform.flinkcdc.sql.OperatorTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 数据消费处理 抽象层：定义模板方法及公共方法
 *
 * @author Yolen
 * @date 2024/8/11
 */
public abstract class AbstractLogHandler<T> implements BaseLogHandler {
    /**
     * 处理器监听的表所对应的实体数据模型
     */
    protected Class<T> poClazz;

    /**
     * 数据变更类型和相应处理函数的映射Map
     */
    protected Map<Integer, Consumer<DataChangeInfo>> eventTypeMap;

    protected AbstractLogHandler(Class<T> poClazz) {
        this.poClazz = poClazz;
        eventTypeMap = new HashMap<>();
        eventTypeMap.put(OperatorTypeEnum.INSERT.getType(), this::handleInsertLog);
        eventTypeMap.put(OperatorTypeEnum.UPDATE.getType(), this::handleUpdateLog);
        eventTypeMap.put(OperatorTypeEnum.DELETE.getType(), this::handleDeleteLog);
    }

    @Override
    public void dispatch(DataChangeInfo data) {
        Optional.ofNullable(data).map(DataChangeInfo::getOperatorType).map(eventTypeMap::get)
            .ifPresent(c -> c.accept(data));
    }

    /**
     * 模板方法，转换json数据为PO实体模型
     *
     * @param changeInfo 变动数据
     * @return PO实体
     */
    protected T parseDataToPO(DataChangeInfo changeInfo) {
        return JSONUtil.toBean(changeInfo.getData(), poClazz);
    }
}
