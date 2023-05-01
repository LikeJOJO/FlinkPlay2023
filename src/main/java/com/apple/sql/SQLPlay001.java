package com.apple.sql;

import com.apple.bean.Event;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SQLPlay001 {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    SingleOutputStreamOperator<Event> eventStream = env.fromElements(
            new Event("Alice", "./home", 1000L),
            new Event("Bob", "./cart", 1000L),
            new Event("Alice", "./prod?id=1", 5 * 1000L),
            new Event("Cary", "./home", 60 * 1000L),
            new Event("Bob", "./prod?id=3", 90 * 1000L),
            new Event("Alice", "./prod?id=7", 105 * 1000L),
            new Event("Alice", "./home", 1000L),
            new Event("Bob", "./cart", 1000L),
            new Event("Alice", "./prod?id=1", 5 * 1000L),
            new Event("Cary", "./home", 60 * 1000L),
            new Event("Bob", "./prod?id=3", 90 * 1000L),
            new Event("Alice", "./prod?id=7", 105 * 1000L));

    StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
    Table eventTable = tEnv.fromDataStream(eventStream);
    Table visitTable = tEnv.sqlQuery("select url, user, count(1) cnt from " + eventTable + " group by url, user");
    tEnv.toChangelogStream(visitTable).print();
    env.execute();
  }
}
