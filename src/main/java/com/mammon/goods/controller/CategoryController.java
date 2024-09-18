package com.mammon.goods.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.enums.CommonDeleted;
import com.mammon.goods.domain.dto.CategoryDto;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.query.CategoryQuery;
import com.mammon.goods.domain.vo.CategoryVo;
import com.mammon.goods.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/goods/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public ResultJson<CategoryEntity> create(@RequestHeader long merchantNo,
                                             @RequestHeader long storeNo,
                                             @RequestHeader String accountId,
                                             @RequestBody CategoryDto dto) {
        return ResultJson.ok(categoryService.create(merchantNo, dto));
    }

    @PostMapping("/import")
    public ResultJson<Void> categoryImport(@RequestHeader long merchantNo,
                                           @RequestHeader long storeNo,
                                           @RequestHeader String accountId,
                                           @RequestParam("file") MultipartFile file) {
        categoryService.categoryImport(merchantNo, file);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody CategoryDto dto) {
        categoryService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> deleteById(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id) {
        categoryService.deleteById(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<CategoryEntity> info(@RequestHeader long merchantNo,
                                           @RequestHeader long storeNo,
                                           @RequestHeader String accountId,
                                           @PathVariable("id") String id) {
        return ResultJson.ok(categoryService.findById(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<CategoryVo>> page(@RequestHeader long merchantNo,
                                               @RequestHeader long storeNo,
                                               @RequestHeader String accountId,
                                               CategoryQuery query) {
        PageVo<CategoryVo> result = categoryService.page(merchantNo, query);
        return ResultJson.ok(result);
    }

    @GetMapping("/list")
    public ResultJson<List<CategoryEntity>> list(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 CategoryQuery query) {
        int level = CategoryLevel.TWO.getCode();
        if (StringUtils.isBlank(query.getPid())) {
            level = CategoryLevel.ONE.getCode();
        }

        CategoryListQuery categoryQuery = new CategoryListQuery();
        categoryQuery.setSearchKey(query.getSearchKey());
        categoryQuery.setPid(query.getPid());
        categoryQuery.setLevel(level);
        categoryQuery.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        return ResultJson.ok(categoryService.findAll(merchantNo, categoryQuery));
    }
}
