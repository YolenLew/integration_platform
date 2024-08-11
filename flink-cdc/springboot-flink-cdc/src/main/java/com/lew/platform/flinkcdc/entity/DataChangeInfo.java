/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.entity;

import lombok.Data;

/**
 * 数据变更对象
 *
 * @author Yolen
 * @date 2024/8/10
 */
@Data
public class DataChangeInfo {
    /**
     * 变更前数据
     */
    private String beforeData;
    /**
     * 变更后数据
     */
    private String afterData;

    /**
     * 操作的数据
     */
    private String data;

    /**
     * 变更类型 1新增 2修改 3删除
     */
    private Integer operatorType;
    /**
     * binlog文件名
     */
    private String fileName;
    /**
     * binlog当前读取点位
     */
    private Integer filePos;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 变更时间
     */
    private Long operatorTime;

}
