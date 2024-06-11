package com.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.HashMap;
import java.util.Map;
public class HutoolPostRequest {
    public static void main(String[] args) {
        String requestUrl = "https://deeplx.yelochick.com/translate";
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("text", "美好的祝福");
        bodyParams.put("source_lang", "ZH");
        bodyParams.put("target_lang", "EN");
        // 发送POST请求
        String response = HttpRequest.post(requestUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(bodyParams))
                .execute()
                .body();
        // 解析响应JSON
        JSONObject jsonObject = JSONUtil.parseObj(response);
        String dataValue = jsonObject.getStr("data");
        String textKey = (String) bodyParams.get("text");
        // 将得到的text和data放进Map
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(textKey, dataValue);
        // 打印结果
        System.out.println(resultMap);
    }
}