package com.mammon.office.edition.gate;

import com.mammon.common.ResultJson;
import com.mammon.office.edition.service.PackageSkuService;
import com.mammon.office.edition.service.PackageSpecService;
import com.mammon.office.edition.service.PackageSpuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-02-02 11:45:37
 */
@RestController
@RequestMapping("/gate/office-edition/package")
public class GatePackageController {

    @Resource
    private PackageSpuService packageSpuService;

    @Resource
    private PackageSkuService packageSkuService;

    @Resource
    private PackageSpecService packageSpecService;

    @GetMapping("/spu-list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(packageSpuService.findAll());
    }

    @GetMapping("/spec-list")
    public ResultJson spaceList(@RequestParam String spuId) {
        return ResultJson.ok(packageSpecService.getSpecList(spuId));
    }

    @GetMapping("/sku-list")
    public ResultJson skuList(@RequestParam String specs) {
        return ResultJson.ok(packageSkuService.findAllBySpecId(specs));
    }
}
