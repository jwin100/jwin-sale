package com.mammon.document.service;

import cn.hutool.core.collection.CollUtil;
import com.mammon.document.dao.DocumentResourceDao;
import com.mammon.document.domain.entity.DocumentResourceEntity;
import com.mammon.document.domain.vo.DocumentResourceTreeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/8/2 11:59
 */
@Service
public class DocumentResourceService {

    private static final String ROOT_PID = "0";

    @Resource
    private DocumentResourceDao documentResourceDao;

    public List<DocumentResourceTreeVo> getTree() {
        List<DocumentResourceEntity> resources = documentResourceDao.getList();
        return getChildren(ROOT_PID, resources);
    }

    private List<DocumentResourceTreeVo> getChildren(String pid, List<DocumentResourceEntity> resources) {
        return resources.stream().filter(x -> x.getPid().equals(pid))
                .map(x -> {
                    DocumentResourceTreeVo vo = new DocumentResourceTreeVo();
                    vo.setId(x.getId());
                    vo.setName(x.getName());
                    List<DocumentResourceTreeVo> children = getChildren(x.getId(), resources);
                    if (CollUtil.isNotEmpty(children)) {
                        vo.setChildren(children);
                    }
                    return vo;
                }).collect(Collectors.toList());
    }
}
