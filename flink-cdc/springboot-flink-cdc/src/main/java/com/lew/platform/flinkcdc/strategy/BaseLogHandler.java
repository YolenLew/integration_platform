/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.strategy;

import com.lew.platform.flinkcdc.entity.DataChangeInfo;

/**
 * 数据消费处理 顶层接口
 *
 * @author Yolen
 * @date 2024/8/11
 */
public interface BaseLogHandler {
    void handleInsertLog(DataChangeInfo data);

    void handleUpdateLog(DataChangeInfo data);

    void handleDeleteLog(DataChangeInfo data);

    void dispatch(DataChangeInfo data);

    String tableName();
}
