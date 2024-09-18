package com.mammon.sms.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class StrReplaceUtil {

    /**
     * ${temp} replace tempValue
     * @param tempValues
     * @param temp
     * @return
     */
    public static String tempReplace(Map<String, String> tempValues, String temp) {
        StringSubstitutor sub = new StringSubstitutor(tempValues);
        return sub.replace(temp);
    }
}
