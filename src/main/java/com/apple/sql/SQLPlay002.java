package com.apple.sql;

import com.apple.bean.Event;
import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.FormatDescriptor;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SQLPlay002 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        Schema schema = Schema.newBuilder()
                .column("id", DataTypes.STRING())
                .column("name", DataTypes.STRING())
                .column("salary", DataTypes.INT())
                .build();

//        TableDescriptor sourceDescriptor = TableDescriptor.forConnector("datagen")
//                .schema(schema)
//                .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
//                .build();

        TableDescriptor sourceDescriptor = TableDescriptor.forConnector("filesystem")
                .schema(schema)
                .option("path", "localhost://Users/hdong/Downloads/test/abc")
                .format(FormatDescriptor.forFormat("csv")
                        .option("field-delimiter", " ")
                        .build())
                .build();

        tableEnv.createTemporaryTable("SourceTable", sourceDescriptor);

//        tableEnv.executeSql("CREATE TEMPORARY TABLE SinkTable WITH ('connector' = 'blackhole') LIKE SourceTable (EXCLUDING OPTIONS) ");
//        Table table1 = tableEnv.from("SourceTable");
        Table table2 = tableEnv.sqlQuery(
                "SELECT " +
                "id, name, sum(salary) " +
                "FROM SourceTable " +
                "group by id, name");
        tableEnv.toChangelogStream(table2).print();
        env.execute();
    }
}
