/*
 * Copyright (c) This is yolen copyright message. 2019-2024. All rights reserved.
 */

package com.lew.platform.flinkcdc.listener;

import com.lew.platform.flinkcdc.entity.DataChangeInfo;
import com.lew.platform.flinkcdc.sql.MysqlDeserialization;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * DB数据变更监听器
 *
 * @author Yolen
 * @date 2024/8/10
 */
@Slf4j
@Component
public class MysqlEventListener implements ApplicationRunner {

    private final DataChangeSink dataChangeSink;

    public MysqlEventListener(DataChangeSink dataChangeSink) {
        this.dataChangeSink = dataChangeSink;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (StreamExecutionEnvironment env = construtExecutionEnv()) {
            MySqlSource<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSource();
            DataStream<DataChangeInfo> streamSource =
                env.fromSource(dataChangeInfoMySqlSource, WatermarkStrategy.noWatermarks(), "mysql-source")
                    .setParallelism(1);

            streamSource.addSink(dataChangeSink);
            // 同步执行：env.execute("mysql-stream-cdc")
            JobClient jobClient = env.executeAsync("mysql-stream-cdc");
            log.info("Started mysql source job with id: [{}]", jobClient.getJobID());
        } catch (Exception e) {
            log.error("failed to build mysql source to capture data change: [{}]", e.getMessage(), e);
        }
    }

    private StreamExecutionEnvironment construtExecutionEnv() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 启动检查点 每隔 5 分钟做一次检查点
        env.enableCheckpointing(Duration.ofMinutes(5).toMillis());
        // 设置模式为exactly-once （这是默认值）
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 确保检查点之间有至少 10 秒的间隔【checkpoint最小间隔500ms】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(Duration.ofSeconds(10).toMillis());
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(Duration.ofMinutes(1).toMillis());
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的 Checkpoint (重启策略)
        env.getCheckpointConfig()
            .setExternalizedCheckpointCleanup(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // --------------- 重启策略相关 --------
        // 固定间隔策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(5, Time.of(30, TimeUnit.SECONDS)));

        return env;
    }

    /**
     * 构造变更数据源
     *
     * @return 监听数据源
     */
    private MySqlSource<DataChangeInfo> buildDataChangeSource() {
        return MySqlSource.<DataChangeInfo>builder().hostname("192.168.211.110").port(3306).databaseList("ylang_test")
            .tableList("ylang_test.sms_code").username("root").password("root")
            // initial初始化快照,即全量导入后增量导入(检测更新数据写入)
            // latest:只进行增量导入(不读取历史变化)
            // timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
            .startupOptions(StartupOptions.latest()).deserializer(new MysqlDeserialization())
            // GMT+8
            .serverTimeZone("UTC").build();
    }
}
