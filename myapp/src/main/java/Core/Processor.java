package Core;

import Entity.Commodity;
import Entity.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import Core.Current;
import java.util.HashMap;
import java.util.List;

public class Processor {

    private HashMap<String, Double> rate = new HashMap<>();

    private Double getExchangeRate(String initiator) {

//        if (initiator.equals("RMB")) return 2.0;
//        else if (initiator.equals("USD")) return 12.0;
//        else if (initiator.equals("JPY")) return 0.15;
//        else return 9.0;

        Double result;
        try {
            result = Current.getCurrentValue(initiator);
        }catch (Exception e){
            e.printStackTrace();
            result = 0.0;
        }
        return  result;
    }

    private void lock() {}
    private void unlock() {}

    public JSONObject process(String in) {
        JSONObject order = JSONObject.parseObject(in);
        int user_id = order.getIntValue("user_id");
        String initiator = order.getString("initiator");
        long time = order.getLongValue("time");
        List<JSONObject> items = JSON.parseArray(order.getString("items"), JSONObject.class);
        double totalPrice = 0;

//        lock();
        rate.put("RMB", getExchangeRate("RMB"));
        rate.put("USD", getExchangeRate("USD"));
        rate.put("JPY", getExchangeRate("JPY"));
        rate.put("EUR", getExchangeRate("EUR"));

        Boolean success = true;

        HashMap<Integer, Commodity> cache = new HashMap<>();

        for (JSONObject item : items) {
            int id = item.getIntValue("id");
            int number = item.getIntValue("number");
            Commodity commodity;
            if (!cache.containsKey(id)) {
                commodity = CommodityService.getCommodity(id);
                cache.put(id, commodity);
            }
            else commodity = cache.get(id);

            if (number > commodity.getInventory()) {
                success = false;
                break;
            }
            else {
                Integer cur = commodity.getInventory();
                commodity.setInventory(cur - number);
                cache.put(id, commodity);
            }

            totalPrice += commodity.getPrice() * number *
                    rate.get(commodity.getCurrency()) / rate.get(initiator);
            System.out.println(totalPrice);

        }

        if (success) {
            for (int id : cache.keySet()) {
                CommodityService.updateCommodity(cache.get(id));
            }
            try {
                Current.updateTotalTxAmount(initiator, totalPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            totalPrice = 0;
        }

        Result result = new Result();
        result.setUserId(user_id);
        result.setInitiator(initiator);
        result.setSuccess(success ? "true" : "false");
        result.setPaid(totalPrice);

        ResultService.addResult(result);
//        unlock();

        return (JSONObject) JSONObject.toJSON(result);
    }
}
