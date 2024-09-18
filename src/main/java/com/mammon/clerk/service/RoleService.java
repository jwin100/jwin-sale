package com.mammon.clerk.service;

import com.mammon.common.*;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.clerk.dao.RoleDao;
import com.mammon.clerk.domain.dto.PowerCreateDto;
import com.mammon.clerk.domain.dto.RoleDto;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.entity.RoleResourceMapEntity;
import com.mammon.clerk.domain.entity.ResourceEntity;
import com.mammon.clerk.domain.enums.ResourceTypeConst;
import com.mammon.clerk.domain.enums.RoleDefaultConst;
import com.mammon.clerk.domain.query.RolePageQuery;
import com.mammon.clerk.domain.vo.PowerVo;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.domain.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class RoleService {

    @Resource
    private AccountService accountService;

    @Resource
    private RoleDao roleDao;

    @Resource
    private ResourceService resourceService;

    @Resource
    private RoleResourceMapService roleResourceMapService;

    @Resource
    private AccountRoleMapService accountRoleMapService;

    @Transactional(rollbackFor = Exception.class)
    public void save(long merchantNo, RoleDto dto) {
        RoleEntity entity = new RoleEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setType(dto.getType());
        entity.setDefaultStatus(RoleDefaultConst.其他);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setRemark(dto.getRemark());
        roleDao.save(entity);
        powerCreate(merchantNo, entity.getId(), dto.getPowers());
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, String id, RoleDto dto) {
        RoleEntity entity = info(merchantNo, id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "角色不存在");
        }
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        entity.setUpdateTime(LocalDateTime.now());
        roleDao.edit(entity);
        powerCreate(merchantNo, entity.getId(), dto.getPowers());
    }

    /**
     * 设置商户注册默认使用的角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void editDefaultStatus(String id) {
        roleDao.batchEdit(0, RoleDefaultConst.商户默认, RoleDefaultConst.其他);
        roleDao.editDefaultStatus(0, id, RoleDefaultConst.商户默认);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editStatus(long merchantNo, String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("修改状态错误");
        }

        RoleEntity role = roleDao.findById(id, merchantNo);
        if (role == null) {
            throw new CustomException("角色信息错误");
        }
        if (merchantNo == 0 && role.getDefaultStatus() == RoleDefaultConst.商户默认) {
            throw new CustomException("为默认注册使用角色，不允许修改");
        }
        roleDao.editStatus(merchantNo, id, status);
    }

    public int delete(long merchantNo, String id) {
        RoleEntity entity = info(merchantNo, id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "角色不存在");
        }
        if (entity.getMerchantNo() == 0) {
            throw new CustomException(ResultCode.BAD_REQUEST, "删除失败，公共角色不允许删除");
        }
        List<AccountRoleMapEntity> list = accountRoleMapService.findAllByRoleId(id);
        if (!CollectionUtils.isEmpty(list)) {
            throw new CustomException(ResultCode.BAD_REQUEST, "有正在使用的权限信息，不允许修改");
        }
        roleResourceMapService.deleteByRoleId(id);
        return roleDao.delete(id);
    }

    public RoleEntity info(long merchantNo, String id) {
        //返回目录和对应操作信息
        return roleDao.findById(id, merchantNo);
    }

    public PageVo<RoleVo> page(long merchantNo, RolePageQuery query) {
        int total = roleDao.countPage(merchantNo);
        if (total <= 0) {
            return null;
        }
        List<RoleEntity> roles = roleDao.findPage(merchantNo, query);
        List<RoleVo> vos = roles.stream().map(role -> {
            RoleVo vo = new RoleVo();
            BeanUtils.copyProperties(role, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<RoleEntity> list(long merchantNo) {
        return roleDao.findAll(merchantNo);
    }

    /**
     * 获取默认角色
     *
     * @return
     */
    public RoleEntity findByDefaultStatus(long merchantNo, int defaultStatus) {
        return roleDao.findDefaultRole(merchantNo, defaultStatus);
    }

    public void powerCreate(long merchantNo, String roleId, List<PowerCreateDto> dtos) {
        roleResourceMapService.deleteByRoleId(roleId);
        getChildren(roleId, dtos);
    }

    private void getChildren(String roleId, List<PowerCreateDto> children) {
        for (PowerCreateDto dto : children) {
            this.powerMenuCreate(roleId, dto.getId());
            ResourceVo resourceVo = resourceService.info(dto.getId());
            if (resourceVo != null && resourceVo.getType() == ResourceTypeConst.按钮
                    && StringUtils.isNotBlank(resourceVo.getDirectoryId())) {
                List<String> directoryIds = Stream.of(resourceVo.getDirectoryId()
                        .split(",")).collect(Collectors.toList());
                directoryIds.forEach(x -> powerMenuCreate(roleId, x));
            }

            if (dto.getChildren().size() > 0) {
                getChildren(roleId, dto.getChildren());
            }
        }
    }

    public void powerMenuCreate(String roleId, String resourceId) throws CustomException {
        RoleResourceMapEntity entity = new RoleResourceMapEntity();
        entity.setRoleId(roleId);
        entity.setResourceId(resourceId);
        roleResourceMapService.save(entity);
    }

    public List<PowerVo> powerMenu(@RequestParam long merchantNo,
                                   @RequestParam String accountId) {
        List<ResourceEntity> resources = resourceService.findAll();
        if (CollectionUtils.isEmpty(resources)) {
            return Collections.emptyList();
        }
        resources = resources.stream().filter(x -> x.getType() != ResourceTypeConst.目录).collect(Collectors.toList());
        return getChildResource("0", resources);
    }

    private List<PowerVo> getChildResource(String id, List<ResourceEntity> resources) {
        return resources.stream()
                .filter(x -> id.equals(x.getPid()))
                .sorted(Comparator.comparing(ResourceEntity::getSort))
                .map(x -> {
                    PowerVo vo = new PowerVo();
                    vo.setId(x.getId());
                    vo.setName(x.getName());
                    vo.setTitle(x.getTitle());
                    vo.setType(x.getType());
                    vo.setChildren(getChildResource(x.getId(), resources));
                    return vo;
                }).collect(Collectors.toList());
    }

    public List<String> powerMenuChecked(long merchantNo, String roleId, String accountId) {
        List<RoleResourceMapEntity> menus = roleResourceMapService.findAllByRoleId(roleId);
        if (menus == null || menus.size() == 0) {
            return null;
        }
        List<String> resourceIds = menus.stream().map(RoleResourceMapEntity::getResourceId).collect(Collectors.toList());
        List<ResourceVo> resources = resourceService.findAllByIds(resourceIds);
        resources = resources.stream().filter(x -> x.getType() != ResourceTypeConst.目录).collect(Collectors.toList());
        return getChildResource1("0", resources);
    }

    private List<String> getChildResource1(String id, List<ResourceVo> resources) {
        List<String> list = new ArrayList<>();
        resources.forEach(x -> {
            if (resources.stream().noneMatch(y -> x.getId().equals(y.getPid()))) {
                list.add(x.getId());
            }
        });
        return list;
    }
}
