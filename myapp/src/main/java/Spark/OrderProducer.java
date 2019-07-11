package Spark;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Arrays;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import kafka.producer.ProducerConfig;
import scala.Serializable;

import java.util.Properties;


public class OrderProducer{
    private static  String topic;
    private static Producer<String, String> producer;


    static String order(){
        JSONObject object = new JSONObject();
        //string
        object.put("user_id",123);
        object.put("initiator","USD");
        object.put("time",155);
        JSONObject item = new JSONObject();
        item.put("id","1");
        item.put("number",1);
        List<JSONObject> items = Arrays.asList(item);
        object.put("items", items);
        String result = object.toString();
        return result;
    }

    private void run(){

    }

    public static  void main(String args[]) {
        Properties properties = new Properties(); //--2
        properties.put("metadata.broker.list","dist-1:9092,dist-2:9092,dist-3:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("request.require.acks","1");
        ProducerConfig config=new ProducerConfig(properties);
        producer=new Producer<>(config);
        //Processor p = new Processor();
        while(true){
            String message = order();
            producer.send(new KeyedMessage<>("kafka_spark",message));
            System.out.println("sent " + message);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
