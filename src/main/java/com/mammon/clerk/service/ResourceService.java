package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.clerk.domain.dto.ResourceDto;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import com.mammon.clerk.domain.entity.RoleResourceMapEntity;
import com.mammon.clerk.domain.query.ResourcePageQuery;
import com.mammon.clerk.dao.ResourceDao;
import com.mammon.clerk.domain.entity.ResourceEntity;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.merchant.service.MerchantIndustryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService {

    @Resource
    private AccountRoleMapService accountRoleMapService;

    @Resource
    private RoleResourceMapService roleResourceMapService;

    @Resource
    private ResourceDao resourceDao;

    @Resource
    private MerchantIndustryService merchantIndustryService;

    public int save(ResourceDto dto) {
        ResourceEntity entity = new ResourceEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return resourceDao.save(entity);
    }

    public int edit(String id, ResourceDto dto) {
        ResourceEntity entity = new ResourceEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        return resourceDao.edit(entity);
    }

    public int delete(String id) {
        return resourceDao.delete(id);
    }

    public List<ResourceVo> findAllByIds(List<String> ids) {
        List<ResourceEntity> resources = resourceDao.findAllByIds(ids);
        return resources.stream().map(x -> {
            ResourceVo vo = new ResourceVo();
            BeanUtils.copyProperties(x, vo);
            int subCount = resourceDao.countByPid(x.getId());
            vo.setHasChildren(subCount > 0);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<ResourceVo> findAllByRoleId(String roleId) {
        List<RoleResourceMapEntity> roleResourceMaps = roleResourceMapService.findAllByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleResourceMaps)) {
            return Collections.emptyList();
        }
        List<String> resourceIds = roleResourceMaps.stream()
                .map(RoleResourceMapEntity::getResourceId)
                .distinct().collect(Collectors.toList());
        return findAllByIds(resourceIds);
    }

    public List<ResourceEntity> findAll() {
        return resourceDao.findAll();
    }

    public List<ResourceVo> list() {
        List<ResourceEntity> list = findAll();
        return list.stream().map(x -> {
            ResourceVo vo = new ResourceVo();
            BeanUtils.copyProperties(x, vo);
            int subCount = resourceDao.countByPid(x.getId());
            vo.setHasChildren(subCount > 0);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<ResourceVo> findAllByAccountId(String accountId) {
        List<String> resourceIds = findAllResourceIdByAccountId(accountId);
        if (resourceIds.isEmpty()) {
            return Collections.emptyList();
        }
        return findAllByIds(resourceIds);
    }

    private List<String> findAllResourceIdByAccountId(String accountId) {
        AccountRoleMapEntity userRole = accountRoleMapService.findByAccountId(accountId);
        if (userRole == null) {
            return Collections.emptyList();
        }
        List<RoleResourceMapEntity> roleResourceList = roleResourceMapService.findAllByRoleId(userRole.getRoleId());
        if (roleResourceList.isEmpty()) {
            return Collections.emptyList();
        }
        return roleResourceList.stream()
                .map(RoleResourceMapEntity::getResourceId)
                .collect(Collectors.toList());
    }

    public ResourceVo info(String id) {
        //返回目录和对应操作信息
        ResourceVo vo = new ResourceVo();
        ResourceEntity ResourceVo = resourceDao.findById(id);
        BeanUtils.copyProperties(ResourceVo, vo);
        return vo;
    }

    public PageVo<ResourceVo> page(ResourcePageQuery query) {
        //仅仅返回目录列表
        int total = resourceDao.countByPid(null);
        if (total <= 0) {
            return PageResult.of();
        }
        List<ResourceEntity> resourcesParent = resourceDao.findPage(null, query);
        if (resourcesParent.isEmpty()) {
            return PageResult.of();
        }
        List<ResourceVo> vos = resourcesParent.stream().map(x -> {
            ResourceVo vo = new ResourceVo();
            BeanUtils.copyProperties(x, vo);
            int subCount = resourceDao.countByPid(x.getId());
            vo.setHasChildren(subCount > 0);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<ResourceVo> findAllByPid(String pid) {
        List<ResourceEntity> resources = resourceDao.findAllByPid(pid);
        if (resources.isEmpty()) {
            return Collections.emptyList();
        }
        return resources.stream().map(x -> {
            ResourceVo vo = new ResourceVo();
            BeanUtils.copyProperties(x, vo);
            int subCount = resourceDao.countByPid(x.getId());
            vo.setHasChildren(subCount > 0);
            return vo;
        }).collect(Collectors.toList());
    }

    public int pathValidate(long merchantNo, String accountId, String apiPath) {
//        List<ResourceEntity> resources = findAll();
//        ResourceEntity resource = resources.stream()
//                .filter(x -> StringUtils.isNotBlank(x.getApiPath()) &&
//                        Arrays.stream(x.getApiPath().split(",")).anyMatch(y -> y.startsWith(apiPath)))
//                .findFirst().orElse(null);
//        if (resource == null) {
//            // 接口没有在数据库配置，说明可以直接访问
//            return 1;
//        }
        log.info("apiPath:{}", apiPath);
        List<ResourceVo> accountResources = findAllByAccountId(accountId);
//        if (accountResources.stream().noneMatch(x -> x.getId().equals(resource.getId()))) {
//            // 接口有在数据库配置，但是当前用户没权限访问
//            throw new CustomException(ResultCode.NOT_ROLE, "没有权限访问此操作");
//        }
        // 获取到功能ability,判断这个功能是否是付费功能
        // 如果是付费功能 判断当前对应的商户是否购买版本
        // 如果没有版本，拒绝访问
        // 如果购买版本，查看版本里边是否包含此功能 做通过或拒绝处理

//        ResourceVo tempResource = resources.stream()
//                .filter(x -> StringUtils.isNotBlank(x.getApiPath()) &&
//                        Arrays.stream(x.getApiPath().split(",")).anyMatch(y -> y.startsWith(apiPath)))
//                .findFirst().orElse(null);

        //result 1:免费，2:付费可以使用，-1:没有开通版本，-2:版本已过期, -3:当前版本没有此功能
//        int result = industryMerchantService.industryValid(merchantNo, resource.getPermissions());
//        switch (result) {
//            case -1:
//                throw new CustomException(ResultCode.INDUSTRY_NOT);
//            case -2:
//                throw new CustomException(ResultCode.INDUSTRY_ED);
//            case -3:
//                throw new CustomException(ResultCode.INDUSTRY_PERMISSION);
//            case 1:
//            case 2:
//            default:
//                return 1;
//        }
        return 1;
    }
}
