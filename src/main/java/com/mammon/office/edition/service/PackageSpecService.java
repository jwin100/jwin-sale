package com.mammon.office.edition.service;

import com.mammon.office.edition.dao.PackageSpecDao;
import com.mammon.office.edition.dao.PackageSpecValuesDao;
import com.mammon.office.edition.domain.entity.PackageSpecEntity;
import com.mammon.office.edition.domain.entity.PackageSpecValuesEntity;
import com.mammon.office.edition.domain.vo.PackageSpecItemVo;
import com.mammon.office.edition.domain.vo.PackageSpecListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2023/8/28 10:49
 */
@Service
public class PackageSpecService {

    @Resource
    private PackageSpecDao packageSpecDao;

    @Resource
    private PackageSpecValuesDao packageSpecValuesDao;

    public List<PackageSpecListVo> getSpecList(String spuId) {
        List<PackageSpecEntity> specs = packageSpecDao.findAllBySpuId(spuId);
        List<String> specIds = specs.stream().map(PackageSpecEntity::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(specIds)) {
            return Collections.emptyList();
        }
        List<PackageSpecValuesEntity> specValues = packageSpecValuesDao.findAllBySpecIds(specIds);
        return specs.stream().map(spec -> {
                    PackageSpecListVo vo = new PackageSpecListVo();
                    BeanUtils.copyProperties(spec, vo);
                    vo.setItems(
                            specValues.stream()
                                    .filter(specValue -> specValue.getSpecId().equals(spec.getId()))
                                    .map(specValue -> {
                                        PackageSpecItemVo itemVo = new PackageSpecItemVo();
                                        BeanUtils.copyProperties(specValue, itemVo);
                                        return itemVo;
                                    }).sorted(Comparator.comparing(PackageSpecItemVo::getSort))
                                    .collect(Collectors.toList())
                    );
                    return vo;
                }).sorted(Comparator.comparing(PackageSpecListVo::getSort))
                .collect(Collectors.toList());
    }
}
