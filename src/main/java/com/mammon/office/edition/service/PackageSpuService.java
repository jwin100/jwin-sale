package com.mammon.office.edition.service;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.office.edition.dao.PackageSpuDao;
import com.mammon.office.edition.domain.entity.PackageSpuEntity;
import com.mammon.office.edition.domain.enums.PackageSpuStatusConst;
import com.mammon.office.edition.domain.vo.PackageSpuListVo;
import com.mammon.utils.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 11:41:59
 */
@Service
public class PackageSpuService {

    @Resource
    private PackageSpuDao packageSpuDao;

    public List<PackageSpuListVo> findAll() {
        List<PackageSpuEntity> list = packageSpuDao.findAll(PackageSpuStatusConst.上架);
        return list.stream()
                .map(this::convertSpu)
                .sorted(Comparator.comparing(PackageSpuListVo::getSort))
                .collect(Collectors.toList());
    }

    public PackageSpuEntity findById(String id) {
        return packageSpuDao.findById(id);
    }

    public List<PackageSpuEntity> findAllByIds(List<String> ids) {
        return packageSpuDao.findAllByIds(ids);
    }

    private PackageSpuListVo convertSpu(PackageSpuEntity entity) {
        PackageSpuListVo vo = new PackageSpuListVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setAbilityDesc(JsonUtil.toList(entity.getAbilityDesc(), String.class));
        return vo;
    }
}
