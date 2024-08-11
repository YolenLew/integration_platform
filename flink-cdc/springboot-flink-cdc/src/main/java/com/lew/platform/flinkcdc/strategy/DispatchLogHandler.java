
/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.strategy;

import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 处理器控制中心
 *
 * @author Yolen
 * @date 2024/8/11
 */
@Slf4j
@Component
public class DispatchLogHandler {
    private final Map<String, BaseLogHandler> tableHanderMap;

    public DispatchLogHandler(List<BaseLogHandler> logHandlerList) {
        tableHanderMap = logHandlerList.stream()
            .collect(Collectors.toMap(BaseLogHandler::tableName, Function.identity(), (v1, v2) -> v2, HashMap::new));
    }

    public void handleChangeInfo(DataChangeInfo changeInfo) {
        if (Objects.isNull(changeInfo)) {
            log.warn("Unable to handle empty data!");
            return;
        }
        String tableName = changeInfo.getTableName();
        log.info("Started to handle data of table: [{}]", tableName);
        Optional.ofNullable(tableHanderMap.get(tableName)).ifPresent(h -> h.dispatch(changeInfo));
    }
}
