package com.mammon.clerk.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mammon.clerk.domain.enums.AccountCashMode;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultCode;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.domain.vo.MerchantVo;
import com.mammon.merchant.service.MerchantIndustryService;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.office.edition.domain.entity.IndustryAttrEntity;
import com.mammon.clerk.dao.AccountDao;
import com.mammon.clerk.domain.dto.AccountDto;
import com.mammon.clerk.domain.dto.FirstSetPasswordDto;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.enums.RoleDefaultConst;
import com.mammon.clerk.domain.query.AccountQuery;
import com.mammon.clerk.domain.vo.AccountDetailVo;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.domain.vo.RoleVo;
import com.mammon.clerk.domain.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

    private static final long ACCOUNT_START_NO = 100;

    @Resource
    private RoleService roleService;

    @Resource
    private AccountRoleMapService accountRoleMapService;

    @Resource
    private AccountDao accountDao;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private ResourceService resourceService;

    @Resource
    private MerchantIndustryService merchantIndustryService;

    public String register(long merchantNo, long storeNo, String phone, String openId) {
        RoleEntity role = roleService.findByDefaultStatus(0, RoleDefaultConst.商户默认);
        if (role == null) {
            log.error("商户默认角色错误，null,商户注册失败");
            throw new CustomException("系统异常，请稍后重试");
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setRoleId(role.getId());
        accountDto.setStoreNo(storeNo);
        accountDto.setName("商户管理员");
        accountDto.setPhone(phone);
        accountDto.setOpenId(openId);
        return save(merchantNo, accountDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public String save(long merchantNo, AccountDto dto) {
        boolean existAccount = existByPhone(dto.getPhone());
        if (existAccount) {
            throw new CustomException(ResultCode.BAD_REQUEST, "手机号已有注册");
        }
        RoleEntity role = roleService.info(merchantNo, dto.getRoleId());
        if (role == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "职位信息错误");
        }
        String rawPassword = encodePassword(dto.getPassword());
        AccountEntity entity = new AccountEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setMerchantNo(merchantNo);
        entity.setId(Generate.generateUUID());
        entity.setUsername(dto.getPhone());
        entity.setPassword(rawPassword);
        entity.setMobileCashMode(AccountCashMode.SCAN_MODE.getCode());
        entity.setOpenId(dto.getOpenId());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        accountDao.save(entity);

        AccountRoleMapEntity userRole = new AccountRoleMapEntity();
        userRole.setAccountId(entity.getId());
        userRole.setRoleId(role.getId());
        accountRoleMapService.save(userRole);
        return entity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(String id, AccountDto dto) {
        AccountEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "店员信息错误");
        }
        entity.setName(dto.getName());
        entity.setUsername(dto.getPhone());
        entity.setPhone(dto.getPhone());
        entity.setStoreNo(dto.getStoreNo());
        entity.setUpdateTime(LocalDateTime.now());
        AccountEntity tempAccount = findByPhone(entity.getPhone());
        if (tempAccount != null && !tempAccount.getId().equals(id)) {
            throw new CustomException(ResultCode.BAD_REQUEST, "手机号已有注册");
        }
        accountDao.edit(entity);

        RoleEntity role = roleService.info(entity.getMerchantNo(), dto.getRoleId());
        if (role == null) {
            throw new CustomException("设置权限错误");
        }
        accountRoleMapService.deleteByAccountId(entity.getId());
        AccountRoleMapEntity userRole = new AccountRoleMapEntity();
        userRole.setAccountId(entity.getId());
        userRole.setRoleId(role.getId());
        accountRoleMapService.save(userRole);
    }

    /**
     * 创建门店初始化店员归属门店和角色
     *
     * @param id
     * @param storeNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void editStoreNo(String id, long storeNo, String roleId) {
        if (StringUtils.isBlank(roleId)) {
            AccountRoleMapEntity accountRole = accountRoleMapService.findByAccountId(id);
            if (accountRole != null) {
                roleId = accountRole.getRoleId();
            }
        }
        accountDao.editStoreNo(id, storeNo);
        accountRoleMapService.deleteByAccountId(id);
        AccountRoleMapEntity accountRole = new AccountRoleMapEntity();
        accountRole.setAccountId(id);
        accountRole.setRoleId(roleId);
        accountRoleMapService.save(accountRole);
    }

    public void editName(String id, String name) {
        AccountEntity account = accountDao.findById(id);
        if (account == null) {
            throw new CustomException("店员信息错误");
        }
        account.setName(name);
        accountDao.edit(account);
    }

    public void editPhone(String id, String originalPhone, String newPhone) {
        accountDao.editPhone(id, originalPhone, newPhone);
    }

    public void editOpenId(String id, String openId) {
        accountDao.editOpenId(id, openId);
    }

    public void editStatus(String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("状态信息错误");
        }
        accountDao.editStatus(id, status);
    }

    public String encodePassword(String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void editPassword(String id, String newPassword) {
        AccountEntity account = findById(id);
        if (account == null) {
            throw new CustomException("账号信息错误");
        }
        String password = encodePassword(newPassword);
        editPassword(id, account.getPassword(), password);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initAccount(long merchantNo, String accountId, FirstSetPasswordDto dto) {
        if (StrUtil.isNotBlank(dto.getMerchantName())) {
            merchantService.editMerchantName(merchantNo, dto.getMerchantName());
        }
        if (StrUtil.isNotBlank(dto.getName())) {
            editName(accountId, dto.getName());
        }
        if (!dto.getConfirmPassword().equals(dto.getPassword())) {
            throw new CustomException("两次密码输入不一致");
        }
        String password = encodePassword(dto.getPassword());
        editPassword(accountId, null, password);
    }

    public void editPassword(String id, String originalPassword, String newPassword) {
        accountDao.editPassword(id, originalPassword, newPassword);
    }

    public void editMobileCashMode(String id, int cashMode) {
        accountDao.editMobileCashMode(id, cashMode);
    }

    public void delete(long merchantNo, String id) {
        AccountEntity account = findById(id);
        if (account == null) {
            throw new CustomException("店员信息错误");
        }
        MerchantEntity merchant = merchantService.findByMerchantNo(merchantNo);
        if (merchant != null && merchant.getAccountId().equals(id)) {
            throw new CustomException("商户管理人员不可删除");
        }
        List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchantNo);
        stores.stream().filter(x -> x.getAccountId().equals(id))
                .findFirst().ifPresent(x -> {
                    throw new CustomException(String.format("店员为(%s)门店管理人员不可删除", x.getStoreName()));
                });
        accountDao.delete(merchantNo, id);
    }

    public UserVo info(String id) {
        AccountEntity account = findById(id);
        if (account == null) {
            return null;
        }
        return convertUser(account);
    }

    public boolean existByPhone(String phone) {
        return accountDao.findByPhone(phone) != null;
    }

    public AccountEntity findByPhone(String phone) {
        return accountDao.findByPhone(phone);
    }

    public AccountEntity findById(String id) {
        return accountDao.findById(id);
    }

    public AccountEntity findByOpenId(String openId) {
        return accountDao.findByOpenId(openId);
    }

    public PageVo<UserVo> page(long merchantNo,
                               long storeNo,
                               String accountId,
                               AccountQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }

        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        if (query.isLookMy()) {
            query.setStoreNo(null);
            query.setId(accountId);
        }

        //仅仅返回目录列表
        int total = accountDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<AccountEntity> list = accountDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<UserVo> vos = list.stream().map(this::convertUser).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<UserVo> list(long merchantNo,
                             long storeNo,
                             String accountId,
                             AccountQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return Collections.emptyList();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        if (query.isLookMy()) {
            query.setStoreNo(null);
            query.setId(accountId);
        }
        List<AccountEntity> list = accountDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(this::convertUser).collect(Collectors.toList());
    }

    private UserVo convertUser(AccountEntity account) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(account, vo);
        RoleVo role = new RoleVo();
        AccountRoleMapEntity accountRoleMapEntity = accountRoleMapService.findByAccountId(account.getId());
        if (accountRoleMapEntity != null) {
            RoleEntity roleEntity = roleService.info(account.getMerchantNo(), accountRoleMapEntity.getRoleId());
            BeanUtils.copyProperties(roleEntity, role);
            vo.setRoleId(role.getId());
            vo.setRole(role);
        }
        MerchantEntity merchant = merchantService.findByMerchantNo(account.getMerchantNo());
        if (merchant != null) {
            vo.setMerchantName(merchant.getMerchantName());
        }

        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(account.getMerchantNo(), account.getStoreNo());
        if (storeVo != null) {
            vo.setStoreName(storeVo.getStoreName());
            if (account.getId().equals(storeVo.getAccountId())) {
                vo.setStoreMain(true);
            }
        }
        return vo;
    }

    public List<UserVo> findAllByStoreNo(long merchantNo, long storeNo, Integer deleted) {
        List<AccountEntity> accounts = accountDao.findAllByStoreNo(merchantNo, storeNo, null, deleted);
        return accounts.stream().map(this::convertUser).collect(Collectors.toList());
    }

    public List<UserVo> findAll(long merchantNo, Integer deleted) {
        List<AccountEntity> accounts = accountDao.findAllByStoreNo(merchantNo, 0, null, deleted);
        return accounts.stream().map(this::convertUser).collect(Collectors.toList());
    }

    public List<UserVo> findAllByIds(long merchantNo, List<String> accountIds) {
        if (CollUtil.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        List<AccountEntity> accounts = accountDao.findAllByIds(merchantNo, accountIds);
        return accounts.stream().map(this::convertUser).collect(Collectors.toList());
    }

    public List<UserVo> accountSearch(long merchantNo, String searchKey) {
        AccountQuery query = new AccountQuery();
        query.setSearchKey(searchKey);
        List<AccountEntity> accounts = accountDao.findList(merchantNo, query);
        return accounts.stream().map(this::convertUser).collect(Collectors.toList());
    }

    public AccountDetailVo detail(long merchantNo,
                                  long storeNo,
                                  String accountId) {
        MerchantVo merchantVo = merchantService.merchantInfo(merchantNo);
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        List<ResourceVo> resources = resourceService.findAllByAccountId(accountId);
        List<IndustryAttrEntity> industryAttrs = merchantIndustryService.merchantChargeAbility(merchantNo);
        UserVo userVo = info(accountId);
        AccountDetailVo vo = new AccountDetailVo();
        vo.setMerchantVo(merchantVo);
        vo.setStoreVo(storeVo);
        vo.setUserVo(userVo);
        vo.setResources(resources);
        vo.setIndustryAttrs(industryAttrs);
        return vo;
    }
}
