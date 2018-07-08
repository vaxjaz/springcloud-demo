package com.jay.li.springcloud.zuul.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author jay
 * @date 2018/6/21
 */
public class ParseRequestUtils {

    public static String getParamString(HttpServletRequest request) {
        String param = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            if (jsonObject != null) {
                param = jsonObject.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }
}
