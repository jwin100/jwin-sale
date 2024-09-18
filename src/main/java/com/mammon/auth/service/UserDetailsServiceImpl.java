package com.mammon.auth.service;

import com.mammon.auth.domain.UserDetail;
import com.mammon.enums.CommonDeleted;
import com.mammon.exception.CustomException;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.entity.ResourceEntity;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.clerk.service.ResourceService;
import com.mammon.clerk.service.AccountRoleMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl {

    @Resource
    private AccountService accountService;

    @Resource
    private AccountRoleMapService accountRoleMapService;

    @Resource
    private ResourceService resourceService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;


    /**
     * 根据手机号加载用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     * @throws UsernameNotFoundException 如果找不到用户，则抛出该异常
     */
    public UserDetail loadUserByPhone(String phone) {
        AccountEntity entity = accountService.findByPhone(phone);
        return convertUser(entity);
    }


    public UserDetail loadUserById(String id) {
        AccountEntity entity = accountService.findById(id);
        return convertUser(entity);
    }

    private UserDetail convertUser(AccountEntity entity) {
        if (entity == null || entity.getDeleted() == CommonDeleted.DELETED.getCode()) {
            throw new CustomException("账号错误");
        }
        AccountRoleMapEntity userRole = accountRoleMapService.findByAccountId(entity.getId());
        if (userRole == null) {
            throw new CustomException("账号错误");
        }
        List<ResourceVo> resourceVos = resourceService.findAllByRoleId(userRole.getRoleId());
        List<String> perms = resourceVos.stream()
                .map(ResourceEntity::getPermissions)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());

        MerchantEntity merchant = merchantService.findByMerchantNo(entity.getMerchantNo());
        if (merchant == null) {
            throw new CustomException("商户信息错误");
        }

        MerchantStoreVo store = merchantStoreService.findByStoreNo(entity.getMerchantNo(), entity.getStoreNo());
        if (store == null) {
            throw new CustomException("门店信息错误");
        }

        UserDetail userDetail = new UserDetail();
        userDetail.setId(entity.getId());
        userDetail.setMerchantNo(entity.getMerchantNo());
        userDetail.setStoreNo(entity.getStoreNo());
        userDetail.setUsername(entity.getPhone());
        userDetail.setPhone(entity.getPhone());
        userDetail.setPassword(entity.getPassword());
        userDetail.setStatus(entity.getStatus());
        userDetail.setMerchantStatus(merchant.getStatus());
        userDetail.setStoreStatus(store.getStatus());
        userDetail.setStoreExpireDate(store.getEndDate());
        userDetail.setPerms(perms);
        return userDetail;
    }
}
