package com.mammon.document.service;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.document.dao.DocumentHelpDao;
import com.mammon.document.domain.entity.DocumentHelpEntity;
import com.mammon.document.domain.query.DocumentHelpQuery;
import com.mammon.document.domain.vo.DocumentHelpPageVo;
import com.mammon.document.domain.vo.DocumentHelpVo;
import com.mammon.feedback.domain.entity.FeedbackEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/8/2 11:59
 */
@Service
public class DocumentHelpService {

    @Resource
    private DocumentHelpDao documentHelpDao;

    public PageVo<DocumentHelpPageVo> getPage(DocumentHelpQuery query) {
        int total = documentHelpDao.countPage(query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<DocumentHelpEntity> list = documentHelpDao.findPage(query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<DocumentHelpPageVo> vos = list.stream().map(x -> {
            DocumentHelpPageVo vo = new DocumentHelpPageVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public DocumentHelpVo getInfo(String id) {
        DocumentHelpEntity entity = documentHelpDao.findById(id);
        if (entity == null) {
            return null;
        }
        DocumentHelpVo vo = new DocumentHelpVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
