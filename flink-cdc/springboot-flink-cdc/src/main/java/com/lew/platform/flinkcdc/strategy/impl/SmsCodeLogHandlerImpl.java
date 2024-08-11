
/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.lew.platform.flinkcdc.constant.CdcTableNameEnum;
import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import com.lew.platform.flinkcdc.entity.SmsCode;
import com.lew.platform.flinkcdc.strategy.AbstractLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yolen
 * @date 2024/8/11
 */
@Slf4j
@Component
public class SmsCodeLogHandlerImpl extends AbstractLogHandler<SmsCode> {
    public SmsCodeLogHandlerImpl() {
        super(SmsCode.class);
    }

    @Override
    public void handleInsertLog(DataChangeInfo data) {
        // 处理增量数据
        SmsCode smsCode = parseDataToPO(data);
        log.info("handle insert data: [{}]", JSONUtil.toJsonStr(smsCode));
    }

    @Override
    public void handleUpdateLog(DataChangeInfo data) {
        // 处理变更数据
        SmsCode smsCode = parseDataToPO(data);
        log.info("handle update data: [{}]", JSONUtil.toJsonStr(smsCode));
    }

    @Override
    public void handleDeleteLog(DataChangeInfo data) {
        // 处理删除数据
        SmsCode smsCode = JSONUtil.toBean(data.getBeforeData(), poClazz);
        log.info("handle delete data: [{}]", JSONUtil.toJsonStr(smsCode));
    }

    @Override
    public String tableName() {
        return CdcTableNameEnum.SMS_CODE.getTableName();
    }
}
