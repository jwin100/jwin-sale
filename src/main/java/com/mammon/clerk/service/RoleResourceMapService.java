package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.clerk.dao.RoleResourceMapDao;
import com.mammon.clerk.domain.entity.RoleResourceMapEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleResourceMapService {

    @Resource
    private RoleResourceMapDao roleResourceMapDao;

    public RoleResourceMapEntity save(RoleResourceMapEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        if (roleResourceMapDao.save(entity) > 0) {
            return entity;
        }
        return null;
    }

    public int deleteByRoleId(String roleId) {
        return roleResourceMapDao.deleteByRoleId(roleId);
    }

    public List<RoleResourceMapEntity> findAllByRoleId(String roleId) {
        return roleResourceMapDao.findByRoleId(roleId);
    }

}
