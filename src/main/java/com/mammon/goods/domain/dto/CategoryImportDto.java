package com.mammon.goods.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/24 11:19
 */
@Data
public class CategoryImportDto {

    @ExcelProperty("一级分类")
    private String oneCategory;

    @ExcelProperty("二级分类")
    private String twoCategory;
}
