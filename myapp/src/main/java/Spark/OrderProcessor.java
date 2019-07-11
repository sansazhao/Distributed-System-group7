package Spark;

import Core.Current;
import Core.Processor;

import Core.ResultService;
import Spark.SimpleLock;
import com.alibaba.fastjson.JSONObject;


public class OrderProcessor {
    static Processor processor;
    static public void init(){
        processor = new Processor();
        try {
            //ResultService.initResult();
            //Current.initTotalTxAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public String process(String order){

        String result = "";
        if(processor == null) init();
        try {
            //lock.lock();
            result = processor.process(order).toJSONString();
            //System.out.println(result);
            //lock.unlock();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
