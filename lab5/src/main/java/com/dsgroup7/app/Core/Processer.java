package com.dsgroup7.app.Core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dsgroup7.app.Entity.Commodity;
import com.dsgroup7.app.Entity.Result;
import com.dsgroup7.app.Service.CommodityService;
import com.dsgroup7.app.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

@Component
public class Processer {
    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ResultService resultService;

    private HashMap<String, Double> rate = new HashMap<>();

    private Double getExchangeRate(String initiator) {
        if (initiator.equals("RMB")) return 2.0;
        else if (initiator.equals("RMB")) return 12.0;
        else if (initiator.equals("JPY")) return 0.15;
        else return 9.0;
    }

    private void updateTotalTxAmount(String initiator, Double paid) {

    }

    private void lock() {}
    private void unlock() {}

    public void receive() {

    }

    public JSONObject process(String in) {
        JSONObject order = JSONObject.parseObject(in);
        int user_id = order.getIntValue("user_id");
        String initiator = order.getString("initiator");
        long time = order.getLongValue("time");
        List<JSONObject> items = JSON.parseArray(order.getString("items"), JSONObject.class);
        double totalPrice = 0;
//        System.out.println(user_id);
//        System.out.println(initiator);
//        System.out.println(time);
//        System.out.println(order.getString("items"));

        lock();
        // ??
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
                commodity = commodityService.getCommodity(id);
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

        }

        System.out.println(totalPrice);

        try {
            sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (success) {
            for (int id : cache.keySet()) {
                commodityService.updateCommodity(cache.get(id));
            }

            updateTotalTxAmount(initiator, totalPrice);
        }
        else {
            totalPrice = 0;
        }

        Result result = new Result();
        result.setUserId(user_id);
        result.setInitiator(initiator);
        result.setSuccess(success ? "true" : "false");
        result.setPaid(totalPrice);

        resultService.addResult(result);
        unlock();

        return (JSONObject) JSONObject.toJSON(result);
    }
}
