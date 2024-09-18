package com.mammon.office.edition.service;

import com.mammon.office.edition.dao.PackageSkuDao;
import com.mammon.office.edition.domain.entity.PackageSkuEntity;
import com.mammon.office.edition.domain.vo.PackageSkuListVo;
import com.mammon.office.edition.domain.vo.PackageSkuVo;
import com.mammon.utils.AmountUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 11:41:42
 */
@Service
public class PackageSkuService {

    @Resource
    private PackageSkuDao packageSkuDao;

    public PackageSkuVo findBySkuId(String id) {
        PackageSkuEntity sku = packageSkuDao.findById(id);
        if (sku == null) {
            return null;
        }
        PackageSkuVo vo = new PackageSkuVo();
        BeanUtils.copyProperties(sku, vo);
        vo.setNowAmount(AmountUtil.parseBigDecimal(sku.getNowAmount()));
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(sku.getOriginalAmount()));
        return vo;
    }

    public PackageSkuListVo findAllBySpecId(String specId) {
        PackageSkuEntity sku = packageSkuDao.findAllBySpecs(specId);
        if (sku == null) {
            return null;
        }
        PackageSkuListVo vo = new PackageSkuListVo();
        BeanUtils.copyProperties(sku, vo);
        vo.setNowAmount(AmountUtil.parseBigDecimal(sku.getNowAmount()));
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(sku.getOriginalAmount()));
        return vo;
    }

    public List<PackageSkuListVo> findAllByIds(List<String> ids) {
        List<PackageSkuEntity> list = packageSkuDao.findAllByIds(ids);
        return list.stream().map(x -> {
            PackageSkuListVo vo = new PackageSkuListVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
