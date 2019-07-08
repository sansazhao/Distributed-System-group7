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
            ResultService.initResult();
            Current.initTotalTxAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public String process(String order){
        SimpleLock lock = new SimpleLock("bigLock","dist-1:2181,dist-2:2181,dist-3:2181");
        String result = "";
        try {
            //lock.lock();
            result = processor.process(order).toJSONString();
            //lock.unlock();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
