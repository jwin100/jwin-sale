package com.mammon.goods.gate;

import com.mammon.common.ResultJson;
import com.mammon.enums.CommonDeleted;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.vo.CategoryTreeVo;
import com.mammon.goods.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-24 13:05:16
 */
@RestController
@RequestMapping("/gate/goods/category")
public class GateCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        CategoryListQuery query = new CategoryListQuery();
        query.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        List<CategoryEntity> list = categoryService.findAll(merchantNo, query);
        return ResultJson.ok(list);
    }

    @GetMapping("/tree")
    public ResultJson tree(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        List<CategoryTreeVo> list = categoryService.tree(merchantNo);
        return ResultJson.ok(list);
    }

    /**
     * 只返回一级分类
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/one-level/list")
    public ResultJson oneLevelList(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId) {
        CategoryListQuery query = new CategoryListQuery();
        query.setLevel(CategoryLevel.ONE.getCode());
        query.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        List<CategoryEntity> list = categoryService.findAll(merchantNo, query);
        return ResultJson.ok(list);
    }

    @GetMapping("/sub-list/{pid}")
    public ResultJson subList(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              @PathVariable("pid") String pid) {
        CategoryListQuery query = new CategoryListQuery();
        query.setPid(pid);
        query.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        query.setLevel(CategoryLevel.TWO.getCode());
        List<CategoryEntity> list = categoryService.findAll(merchantNo, query);
        return ResultJson.ok(list);
    }
}
