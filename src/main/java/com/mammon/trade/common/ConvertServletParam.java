package com.mammon.trade.common;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.mammon.config.JsonMapper;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConvertServletParam {

    private ConvertServletParam() {
    }

    public static Map<String, String> convert(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (Strings.isBlank(contentType)) {
            return convertGetParam(request);
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)
                || MediaType.TEXT_XML_VALUE.equals(contentType)) {
            return convertPostXml(request);
        }
        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType)) {
            return convertGetParam(request);
        }
        Map<String, String> params = convertPostJson(request);
        if (params == null) {
            params = convertGetParam(request);
        }
        return params;
    }

    private static Map<String, String> convertGetParam(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> param = new HashMap<>();
        for (Map.Entry<String, String[]> m : map.entrySet()) {
            param.put(m.getKey(), m.getValue() != null && m.getValue().length > 0 ? m.getValue()[0] : "");
        }
        log.info("回调参数：" + JsonUtil.toJSONString(param));
        return param;
    }

    private static Map<String, String> convertPostJson(HttpServletRequest request) {
        try (BufferedReader br = request.getReader()) {
            String str = "";
            StringBuilder wholeStr = new StringBuilder();
            while ((str = br.readLine()) != null) {
                wholeStr.append(str);
            }
            return JSON.parseObject(wholeStr.toString(), Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static Map<String, String> convertPostXml(HttpServletRequest request) {
        try (BufferedReader br = request.getReader()) {
            String str = "";
            StringBuilder wholeStr = new StringBuilder();
            while ((str = br.readLine()) != null) {
                wholeStr.append(str);
            }
            return XmlUtil.xmlToMap(wholeStr.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
