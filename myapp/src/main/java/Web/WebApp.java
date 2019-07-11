package Web;


import Core.Current;
import Core.Processor;
import Entity.Commodity;
import Entity.Result;
import fi.iki.elonen.NanoHTTPD;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.Properties;

import Core.CommodityService;
import Core.ResultService;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import sun.security.action.GetBooleanAction;
import com.alibaba.fastjson.JSONObject;

public class WebApp extends NanoHTTPD{
    Producer<String, String> producer;
    public WebApp(int port) throws IOException{
        super(port);
        Properties properties = new Properties(); //--2
        properties.put("metadata.broker.list","dist-1:9092,dist-2:9092,dist-3:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("request.require.acks","1");
        ProducerConfig config=new ProducerConfig(properties);
        producer=new Producer<>(config);
    }
    static public void main(String[] args){
        try {
            new WebApp(30441).start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        if(uri.startsWith("/clear/database")){
            //CommodityService.getCommodity(1);

            CommodityService.clearCommodity();
            ResultService.initResult();
            return newFixedLengthResponse("clear database success");

        }else if(uri.startsWith("/post/order")){
            Map<String, String> json = new HashMap<String, String>();
            try {
                session.parseBody(json);
                String body = json.get("postData");
                //System.out.println(body);

                JSONObject data = JSONObject.parseObject(body);
                producer.send(new KeyedMessage<>("kafka_spark",data.toJSONString()));
                //System.out.println("send order");
                return newFixedLengthResponse("post order success");
            }catch(Exception e){
                e.printStackTrace();
                return newFixedLengthResponse("post order fail, please retry");
            }


        }else if(uri.startsWith("/insert/commodity")){
            Map<String, String> json = new HashMap<String, String>();
            try {
                session.parseBody(json);
                String body = json.get("postData");
                //System.out.println(body);
                JSONObject data = JSONObject.parseObject(body);
                InsertCommodity(data);
                return newFixedLengthResponse("insert commodity success");
            }catch(Exception e){
                e.printStackTrace();
                return newFixedLengthResponse("insert commodity fail, please retry");
            }
        }else if(uri.startsWith("/get/result")){
            Map<String, String> parms = session.getParms();
            if(parms.get("id") != null){
                int id = Integer.parseInt(parms.get("id"));
                Result result = ResultService.getResultById(id);
                if(result == null) return newFixedLengthResponse("no result for this id");
                JSONObject json = new JSONObject();

                json.put("id",result.getId());
                json.put("initiator",result.getInitiator());
                json.put("paid",result.getPaid());
                json.put("success",result.getSuccess());
                json.put("user_id",result.getUserId());
                return newFixedLengthResponse(json.toJSONString());
            }else{
                return newFixedLengthResponse("no id specified");
            }

        }else if(uri.startsWith("/get/totalTxAmount")){
            Map<String, String> parms = session.getParms();
            if(parms.get("Initiator") != null){
                String initator = parms.get("Initiator");
                if((initator.equals("RMB") || initator.equals("USD") ||
                    initator.equals("JPY") || initator.equals("EUR") )){
                    String result = "Get Total Tx Amount Error";
                    try{
                        result = new Double(Current.getTotalTxAmount(initator)).toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return newFixedLengthResponse(result);
                }
            }
            return newFixedLengthResponse("Invalid parms");
        }
        return super.serve(session);
    }

    void InsertCommodity(JSONObject json){
        Commodity commodity = new Commodity();
        //System.out.println(String.format("id is %s",json.get("id")));
        commodity.setInventory(Integer.parseInt(json.get("inventory").toString()));
        commodity.setCurrency(json.get("currency").toString());
        commodity.setId(Integer.parseInt(json.get("id").toString()));
        commodity.setName(json.get("name").toString());
        commodity.setPrice(Double.parseDouble(json.get("price").toString()));
        CommodityService.insertCommodity(commodity);
        return;
    }
    void PostOrder(JSONObject json){

    }
    String GetResultById(){
        return "result";
    }
}
