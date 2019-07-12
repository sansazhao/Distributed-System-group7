
package Spark;





import Core.Current;
import Core.LockService;
import kafka.serializer.StringDecoder;
import org.apache.spark.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.sql.Connection;
import java.util.HashMap;


import org.apache.spark.streaming.kafka.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import org.apache.spark.api.java.JavaPairRDD;
;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import Core.ResultService;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.regex.Pattern;

import Spark.OrderProcessor;
import java.util.Date;
import javax.activation.DataSource;
import javax.sound.midi.Receiver;


public class App {


    static void init(){

        ResultService.initResult();

        OrderProcessor.init();

        Current.connectZookeeper();
        Current.initTotalTxAmount();
        LockService.init();
    }

    public static void main(String args[]) throws Exception {
        ReceiverImplementation();
        //DirectImplementation();

    }

    static void ReceiverImplementation() throws Exception{
        System.out.println("hello world");
        init();
        SparkConf conf = new SparkConf().setAppName("Order Processing");

        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");

        JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(10));
        Map<String, Integer> topicMap = new HashMap<String,Integer>();
        topicMap.put("kafka_spark",20);

        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(jssc,
                        "dist-1:2181,dist-2:2181,dist-3:2181", "spark_receiver", topicMap);

        JavaDStream<String> lines = messages.map(Tuple2::_2);
        JavaDStream<String> results = lines.map(OrderProcessor::process);


        //JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> (i1 + i2));
        System.out.println("test");
        results.foreachRDD(rdd -> {
            rdd.foreach(str ->{
                System.out.println(str);
            });
        });

        results.count().print();
        //results.print();

        //results_2.print();
        //wordCounts.print();
        jssc.start();

        jssc.awaitTermination();
    }

    static void DirectImplementation() throws Exception{
        init();
        SparkConf conf = new SparkConf().setAppName("Order Processing");



        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");

        JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(3));
        Map<String, Integer> topicMap = new HashMap<String,Integer>();
        topicMap.put("kafka_spark",20);
        Map<String, String> kafkaParms = new HashMap<>();
        Set<String> topics = new HashSet<>();
        topics.add("kafka_spark");
        kafkaParms.put("metadata.broker.list","dist-1:9092,dist-2:9092,dist-3:9092");
        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(jssc,String.class,String.class,
                        StringDecoder.class,StringDecoder.class,
                        kafkaParms,topics);


        //JavaDStream<String> lines = messages.map(Tuple2::_2);


        final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<>();
//        JavaDStream<String> lines = messages.map(Tuple2::_2);
        JavaDStream<String> lines_ = messages.transformToPair(
                new Function<JavaPairRDD<String, String>, JavaPairRDD<String, String>>() {
                    @Override
                    public JavaPairRDD<String, String> call(JavaPairRDD<String, String> rdd) throws Exception {
                        OffsetRange[] offsets = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
                        offsetRanges.set(offsets);
                        return rdd;
                    }
                }
        ).map(Tuple2::_2);

        //JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(Pattern.compile(" ").split(x)).iterator());
        //lines.print();

        //JavaDStream<String> results = lines.map(OrderProcessor::process);

        messages.foreachRDD(new VoidFunction<JavaPairRDD<String, String>>() {
            @Override
            public void call(JavaPairRDD<String, String> t) throws Exception {
                for (OffsetRange offsetRange : offsetRanges.get()) {
                    System.out.println("update kafka_offsets set offset ='"
                            + offsetRange.untilOffset() + "'  where topic='"
                            + offsetRange.topic() + "' and partition='"
                            + offsetRange.partition() + "'");
                }
            }

        });

        //JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> (i1 + i2));
        System.out.println("test");

        //results_2.print();
        //wordCounts.print();
        jssc.start();

        jssc.awaitTermination();
    }
}


