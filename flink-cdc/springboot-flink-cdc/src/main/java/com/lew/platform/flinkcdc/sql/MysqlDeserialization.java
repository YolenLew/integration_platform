/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.sql;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Optional;

/**
 * mysql消息读取自定义序列化
 *
 * @author Yolen
 * @date 2024/8/10
 */
public class MysqlDeserialization implements DebeziumDeserializationSchema<DataChangeInfo> {
    public static final String TS_MS = "ts_ms";
    public static final String BIN_FILE = "file";
    public static final String POS = "pos";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String SOURCE = "source";

    /**
     * 反序列化数据,转为变更JSON对象
     *
     * @param sourceRecord 源数据
     * @param collector    收集器
     */
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<DataChangeInfo> collector) {
        String topic = sourceRecord.topic();
        String[] fields = topic.split("\\.");
        String database = fields[1];
        String tableName = fields[2];
        Struct struct = (Struct)sourceRecord.value();
        final Struct source = struct.getStruct(SOURCE);
        DataChangeInfo dataChangeInfo = new DataChangeInfo();
        // 获取操作类型  CREATE UPDATE DELETE
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        String opName = operation.toString().toUpperCase();
        int eventType = OperatorTypeEnum.getTypeByOperation(opName);
        // 注意：这里可以忽略改变前的数据，直接处理最新数据
        dataChangeInfo.setBeforeData(getJsonObject(struct, BEFORE).toString());
        dataChangeInfo.setAfterData(getJsonObject(struct, AFTER).toString());
        if (eventType == OperatorTypeEnum.DELETE.getType()) {
            dataChangeInfo.setData(getJsonObject(struct, BEFORE).toString());
        } else {
            dataChangeInfo.setData(getJsonObject(struct, AFTER).toString());
        }
        dataChangeInfo.setOperatorType(eventType);
        dataChangeInfo.setFileName(Optional.ofNullable(source.get(BIN_FILE)).map(Object::toString).orElse(""));
        dataChangeInfo.setFilePos(
            Optional.ofNullable(source.get(POS)).map(Object::toString).map(Integer::parseInt).orElse(0));
        dataChangeInfo.setDatabase(database);
        dataChangeInfo.setTableName(tableName);
        dataChangeInfo.setOperatorTime(Optional.ofNullable(struct.get(TS_MS)).map(Object::toString).map(Long::parseLong)
            .orElseGet(System::currentTimeMillis));
        // 输出数据
        collector.collect(dataChangeInfo);
    }

    /**
     * 提取监听的DB数据
     *
     * @param value        value
     * @param fieldElement fieldElement
     * @return jsonNode数据
     */
    private ObjectNode getJsonObject(Struct value, String fieldElement) {
        Struct element = value.getStruct(fieldElement);
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
        if (element != null) {
            Schema afterSchema = element.schema();
            List<Field> fieldList = afterSchema.fields();
            for (Field field : fieldList) {
                Object afterValue = element.get(field);
                jsonNode.putPOJO(field.name(), afterValue);
            }
        }
        return jsonNode;
    }

    @Override
    public TypeInformation<DataChangeInfo> getProducedType() {
        return TypeInformation.of(DataChangeInfo.class);
    }
}
