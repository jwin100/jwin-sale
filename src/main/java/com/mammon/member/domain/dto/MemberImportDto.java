package com.mammon.member.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.mammon.config.JsonMapper;
import com.mammon.config.excel.LocalDateConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Slf4j
public class MemberImportDto {

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("姓名")
    private String name;

    /**
     * 性别
     */
    @ExcelProperty("性别")
    private String sex;

    /**
     * 生日
     */
    @ExcelProperty("生日")
    private String birthDayStr;

    @ExcelIgnore
    private LocalDate birthDay;

    /**
     * 头像
     */
    @ExcelProperty("头像地址")
    private String avatar;

    @ExcelProperty("会员积分")
    private Long integral;

    @ExcelProperty("储值余额")
    private BigDecimal recharge;

    /**
     * 会员标签
     */
    @ExcelProperty("会员标签(逗号分割)")
    private String tags;

    public LocalDate getBirthDay() {
        log.info("birthDay:{}", this.getBirthDayStr());
        if (StringUtils.isNotBlank(this.getBirthDayStr())) {
            return LocalDate.parse(this.getBirthDayStr(), DateTimeFormatter.ISO_DATE);
        }
        return null;
    }
}
