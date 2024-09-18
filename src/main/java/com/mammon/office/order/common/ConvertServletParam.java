package com.mammon.office.order.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class ConvertServletParam {

    private ConvertServletParam() {
    }

    public static Map<String, String> convertParam(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    public static JSONObject getRequestBody(HttpServletRequest request) {
        ServletInputStream stream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return JsonUtil.toObject(sb.toString());
    }
}
