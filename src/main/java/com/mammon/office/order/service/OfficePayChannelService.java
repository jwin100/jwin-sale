package com.mammon.office.order.service;

import com.mammon.office.order.dao.OfficePayChannelDao;
import com.mammon.office.order.domain.entity.OfficePayChannelEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-02-02 16:03:07
 */
@Service
public class OfficePayChannelService {

    @Resource
    private OfficePayChannelDao officePayChannelDao;

    public OfficePayChannelEntity findByCode(String code) {
        return officePayChannelDao.findByCode(code);
    }

    public OfficePayChannelEntity findById(String id) {
        return officePayChannelDao.findById(id);
    }
}
