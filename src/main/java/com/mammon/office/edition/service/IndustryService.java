package com.mammon.office.edition.service;

import com.mammon.office.edition.dao.IndustryDao;
import com.mammon.office.edition.domain.entity.IndustryEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:37:57
 */
@Service
public class IndustryService {

    @Resource
    private IndustryDao industryDao;

    public List<IndustryEntity> findAllByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return industryDao.findAllByIds(ids);
    }

    public IndustryEntity findById(String id) {
        return industryDao.findById(id);
    }
}
