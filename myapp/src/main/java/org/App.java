package org;


import org.apache.spark.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.dstream.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import java.util.HashMap;
import org.apache.spark.streaming.kafka.*;
import java.util.Map;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import java.util.Arrays;
import java.util.regex.Pattern;

public class App{
    public static void main(String args[])throws Exception {
        System.out.println("hello world");

        SparkConf conf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[2]");


        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(2));
        Map<String, Integer> topicMap = new HashMap<String,Integer>();
        topicMap.put("kafka_spark",1);
        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(jssc,
                        "dist-1:2181,dist-2:2181,dist-3:2181", "spark_receiver", topicMap);

        JavaDStream<String> lines = messages.map(Tuple2::_2);

        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(Pattern.compile(" ").split(x)).iterator());

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> (i1 + i2));
        System.out.println("test");
        wordCounts.print();
        jssc.start();

        jssc.awaitTermination();


    }
}


