package com.dsgroup7.app;

import com.alibaba.fastjson.JSONObject;
import com.dsgroup7.app.Core.Processer;

public class App {
    public static void main(String[] args) {
        String in = "{" +
                "\"user_id\":123456," +
                "\"initiator\":\"RMB\"," +
                "\"time\":1558868400000," +
                "\"items\":[" +
                "{\"id\":\"1\",\"number\":2}," +
                "{\"id\":\"3\",\"number\":1}" +
                "]" +
                "}";
        Processer processer = new Processer();
        JSONObject result = processer.process(in);
        System.out.println(result.toString());
    }
}
