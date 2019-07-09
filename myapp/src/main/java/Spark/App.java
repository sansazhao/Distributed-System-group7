package Spark;


import Core.Current;
import Core.LockService;
import org.apache.spark.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import Core.ResultService;

public class App{

    static void init(){

        ResultService.initResult();
        //Current.initTotalTxAmount();
        Current.connectZookeeper();
        LockService.init();
    }

    public static void main(String args[])throws Exception {
        System.out.println("hello world");
        init();
        SparkConf conf = new SparkConf().setAppName("Order Processing");



        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(3));
        Map<String, Integer> topicMap = new HashMap<String,Integer>();
        topicMap.put("kafka_spark",20);
        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(jssc,
                        "dist-1:2181,dist-2:2181,dist-3:2181", "spark_receiver", topicMap);
        //JavaPairReceiverInputDStream<String, String> messages_2 =
        //        KafkaUtils.createStream(jssc,
        //                "dist-1:2181,dist-2:2181,dist-3:2181", "spark_receiver2", topicMap);
        //messages.print();
        //messages.foreachRDD(print);
        JavaDStream<String> lines = messages.map(Tuple2::_2);
        //JavaDStream<String> lines_2 = messages_2.map(Tuple2::_2);
        //lines.count().print();
        //lines.print();
        //JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(Pattern.compile(" ").split(x)).iterator());
        //lines.print();

        JavaDStream<String> results = lines.map(OrderProcessor::process);
        //JavaDStream<String> results_2 = lines_2.map(OrderProcessor::process);
        //JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> (i1 + i2));
        System.out.println("test");
        results.count().print();
        results.print();
        //results_2.print();
        //wordCounts.print();
        jssc.start();

        jssc.awaitTermination();


    }
}


