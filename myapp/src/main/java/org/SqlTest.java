package org;

import Core.Processor;
import Core.ResultService;
import com.alibaba.fastjson.JSONObject;

public class SqlTest {
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

        ResultService.initResult();
        Processor processor = new Processor();
        JSONObject result = processor.process(in);
        System.out.println(result.toString());

    }
}
