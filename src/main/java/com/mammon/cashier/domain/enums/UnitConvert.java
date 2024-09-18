package com.mammon.cashier.domain.enums;

import com.mammon.exception.CustomException;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author dcl
 * @since 2024/5/17 16:50
 */
public class UnitConvert {
    // 千克 担 吨 公斤 斤 两 钱 克
    // 1千克为基础，转换为对应单位的重量

    /**
     * 千克转换为对应单位的重量
     *
     * @param weight
     * @param unitName
     * @return
     */
    public static BigDecimal convertWeight(BigDecimal weight, String unitName) {
        BigDecimal decimal;
        switch (unitName) {
            case "担":
                // 1千克=0.02担
                decimal = new BigDecimal("0.02");
                break;
            case "吨":
                // 1千克=0.001吨
                decimal = new BigDecimal("0.001");
                break;
            case "公斤":
                // 1千克=2公斤
                decimal = new BigDecimal("2");
                break;
            case "斤":
                // 1千克=2斤
                decimal = new BigDecimal("2");
                break;
            case "两":
                // 1千克=20两
                decimal = new BigDecimal("20");
                break;
            case "钱":
                // 1千克=200钱
                decimal = new BigDecimal("200");
                break;
            case "克":
                // 1千克=1000克
                decimal = new BigDecimal("1000");
                break;
            default:
                // 默认是千克
                decimal = new BigDecimal("1");
        }
        return weight.multiply(decimal).setScale(3, RoundingMode.HALF_DOWN);
    }
}
