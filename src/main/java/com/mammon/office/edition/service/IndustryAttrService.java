package com.mammon.office.edition.service;

import com.mammon.office.edition.dao.IndustryAttrDao;
import com.mammon.office.edition.dao.IndustryAttrMapDao;
import com.mammon.office.edition.domain.entity.IndustryAttrEntity;
import com.mammon.office.edition.domain.entity.IndustryAttrMapEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 11:40:38
 */
@Service
public class IndustryAttrService {

    @Resource
    private IndustryAttrDao industryAttrDao;

    @Resource
    private IndustryAttrMapDao industryAttrMapDao;

    public List<IndustryAttrEntity> findAll() {
        return industryAttrDao.findAll();
    }

    public List<IndustryAttrEntity> findAllByIndustryId(String industryId) {
        List<IndustryAttrMapEntity> attrMaps = industryAttrMapDao.findAllByIndustryId(industryId);
        if (attrMaps.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> attrIds = attrMaps.stream().map(IndustryAttrMapEntity::getIndustryAttrId).collect(Collectors.toList());
        return industryAttrDao.findAllByIds(attrIds);
    }
}
