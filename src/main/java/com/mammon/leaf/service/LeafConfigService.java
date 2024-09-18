package com.mammon.leaf.service;

import com.mammon.leaf.dao.LeafConfigDao;
import com.mammon.leaf.domain.entity.LeafConfigEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/9/8 20:07
 */
@Service
public class LeafConfigService {

    @Resource
    private LeafConfigDao leafConfigDao;

    public LeafConfigEntity getInfo() {
        return leafConfigDao.getInfo();
    }
}
