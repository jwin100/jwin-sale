package com.mammon.merchant.service;

import com.mammon.merchant.dao.RegionDao;
import com.mammon.merchant.domain.entity.RegionEntity;
import com.mammon.merchant.domain.vo.RegionPathVo;
import com.mammon.merchant.domain.vo.RegionTreeVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Resource
    private RegionDao regionDao;

    public List<RegionTreeVo> findAllByPid(String pid) {
        List<RegionEntity> list = regionDao.findAllByPid(pid);
        return list.stream().map(x -> {
            RegionTreeVo vo = new RegionTreeVo();
            vo.setValue(x.getId());
            vo.setLabel(x.getName());
            return vo;
        }).collect(Collectors.toList());
    }

    public List<RegionTreeVo> findAll() {
        List<RegionEntity> list = regionDao.findAll();
        return child("0", list);
    }

    private List<RegionTreeVo> child(String pid, List<RegionEntity> list) {
        List<RegionEntity> regions = list.stream()
                .filter(y -> y.getPid().equals(pid)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(regions)) {
            return null;
        }
        return regions.stream().map(region -> {
            RegionTreeVo vo = new RegionTreeVo();
            vo.setValue(region.getId());
            vo.setLabel(region.getName());
            vo.setChildren(child(region.getId(), list));
            return vo;
        }).collect(Collectors.toList());
    }

    public RegionPathVo findById(String id) {
        RegionEntity region = regionDao.findById(id);
        if (region == null) {
            return null;
        }
        RegionPathVo vo = new RegionPathVo();
        vo.setId(id);
        String[] ids = region.getIdPath().split(",");
        String[] names = region.getNamePath().split(",");
        if (ids.length > 0) {
            vo.setProvince(ids[0]);
            vo.setProvinceName(names[0]);
        }
        if (ids.length > 1) {
            vo.setCity(ids[1]);
            vo.setCityName(names[1]);
        }
        if (ids.length > 2) {
            vo.setArea(ids[2]);
            vo.setAreaName(names[2]);
        }
        return vo;
    }
}
