/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import com.lew.platform.flinkcdc.strategy.DispatchLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * flink数据处理流
 *
 * @author Yolen
 * @date 2024/8/10
 */
@Slf4j
@Component
public class DataChangeSink implements SinkFunction<DataChangeInfo>, Serializable {

    @Override
    public void invoke(DataChangeInfo changeInfo, Context context) {
        try {
            log.info("capture change data: {}", changeInfo);
            DispatchLogHandler dispatchLogHandler = SpringUtil.getBean(DispatchLogHandler.class);
            dispatchLogHandler.handleChangeInfo(changeInfo);
        } catch (Exception e) {
            log.error("failed to handle data with error: [{}]", e.getMessage(), e);
        }
    }
}
