package com.mammon.auth.service;

import com.mammon.auth.channel.factory.TradeChannelCode;
import com.mammon.auth.channel.factory.TradeOpenChannel;
import com.mammon.auth.channel.factory.TradeOpenFactory;
import com.mammon.auth.domain.UserDetail;
import com.mammon.auth.domain.dto.*;
import com.mammon.auth.domain.enums.CaptchaConst;
import com.mammon.auth.domain.enums.WechatLoginStatus;
import com.mammon.auth.domain.vo.*;
import com.mammon.clerk.domain.entity.AccountScanEntity;
import com.mammon.clerk.domain.enums.AccountLogType;
import com.mammon.clerk.domain.enums.AccountScanStatus;
import com.mammon.clerk.domain.vo.AccountScanStatusVo;
import com.mammon.clerk.service.AccountLoginLogService;
import com.mammon.clerk.service.AccountScanService;
import com.mammon.common.Generate;
import com.mammon.common.ResultCode;
import com.mammon.enums.CommonLoginSource;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.service.RedisService;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.QrCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {

    /**
     * 登录token超时时间
     */
    private static final long ACCESS_TOKEN_EXPIRE = 43200;

    /**
     * 刷新token超时时间
     */
    private static final long REFRESH_TOKEN_EXPIRE = 8640000;

    /**
     * 登录token缓存前缀
     */
    private static final String ACCESS_TOKEN_PREFIX = "ACCESS_TOKEN:";

    private static final String ACCESS_TOKEN_ACCOUNT_PREFIX = "ACCESS_TOKEN_ACCOUNT:";

    /**
     * 刷新token缓存前缀
     */
    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";

    private static final String REFRESH_TOKEN_ACCOUNT_PREFIX = "REFRESH_TOKEN_ACCOUNT:";

    private static final String SCAN_PATH = "https://ai.jwin100.cn";

    @Resource
    private RedisService redisService;

    @Resource
    public PasswordEncoder passwordEncoder;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    @Resource
    private GenerateService generateService;

    @Resource
    private AccountService accountService;

    @Resource
    private AccountScanService accountScanService;

    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Resource
    private AccountLoginLogService accountLoginLogService;

    public LoginVo login(HttpServletRequest request, LoginDto dto) {
        switch (dto.getGrantType()) {
            case "password":
                return passwordLogin(request, dto);
            case "sms_code":
                return smsCaptchaLogin(request, dto);
            case "refresh_token":
                return refreshTokenLogin(request, dto);
            default:
                throw new CustomException("登录方式错误");
        }
    }

    public UserDetail validateToken(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        String accountId = redisService.get(key);
        log.info("validToken,token:{},accountId:{}", accessToken, accountId);
        if (StringUtils.isBlank(accountId)) {
            throw new CustomException(ResultCode.EXPIRE_LOGIN);
        }
        UserDetail userDetail = userDetailsServiceImpl.loadUserById(accountId);
        validAccount(userDetail);
        userDetail.setPassword(null);
        return userDetail;
    }

    public LoginVo passwordLogin(HttpServletRequest request, LoginDto dto) {
        // 验证登录次数
        validLoginTotal(dto.getPhone());
        // 验证账号
        UserDetail userDetail = userDetailsServiceImpl.loadUserByPhone(dto.getPhone());
        validAccount(userDetail);
        // 验证密码
        validPassword(dto.getPassword(), userDetail.getPassword());
        LoginVo vo = new LoginVo();
        login(request, vo, userDetail.getId(), dto.getPlatform());
        return vo;
    }

    /**
     * 验证码登录，如果未注册自动注册
     *
     * @param request
     * @param dto
     * @return
     */
    public LoginVo smsCaptchaLogin(HttpServletRequest request, LoginDto dto) {
        // 验证短信码
        smsCaptchaService.validSmsCaptcha(dto.getPhone(), dto.getSmsCaptcha());
        boolean existsAccount = accountService.existByPhone(dto.getPhone());
        boolean register = false;
        if (!existsAccount) {
            register = true;
            RegisterDto registerDto = new RegisterDto();
            registerDto.setPhone(dto.getPhone());
            registerDto.setOpenId(dto.getOpenId());
            generateService.register(registerDto);
        }
        // 验证账号
        UserDetail userDetail = userDetailsServiceImpl.loadUserByPhone(dto.getPhone());
        validAccount(userDetail);
        // 修改openId同/{channelCode}/{code}接口一起删掉
        if (StringUtils.isNotBlank(dto.getOpenId())) {
            accountService.editOpenId(userDetail.getId(), dto.getOpenId());
        }
        LoginVo vo = new LoginVo();
        vo.setRegister(register);
        login(request, vo, userDetail.getId(), dto.getPlatform());
        return vo;
    }

    /**
     * 新注册用户登录
     *
     * @param phone
     * @return
     */
    public LoginVo registerLogin(HttpServletRequest request, String phone, int source) {
        // 验证账号
        UserDetail userDetail = userDetailsServiceImpl.loadUserByPhone(phone);
        validAccount(userDetail);
        LoginVo vo = new LoginVo();
        login(request, vo, userDetail.getId(), source);
        return vo;
    }

    /**
     * 公众平台小程序登录
     */
    public MiniAppLoginVo miniappLogin(HttpServletRequest request, String channelCode, MiniAppLoginDto dto) {
        TradeOpenChannel channel = TradeOpenFactory.channels.get(channelCode);
        if (channel == null) {
            throw new CustomException("登录信息错误");
        }
        int source = getMiniAppLoginSource(channelCode, dto.getSource());
        String openId = channel.getOpenId(dto.getCode());
        if (StringUtils.isBlank(openId)) {
            throw new CustomException("微信登录异常，请使用其他方式登录");
        }
        MiniAppLoginVo vo = new MiniAppLoginVo();
        vo.setOpenId(openId);
        // 根据openId获取用户信息
        AccountEntity account = accountService.findByOpenId(openId);
        if (account == null) {
            // 说明是第一次登录，需绑定手机号，绑定手机号后走验证码登录逻辑，同时绑定手机号
            vo.setCode(WechatLoginStatus.BING_PHONE.getCode());
            return vo;
        }
        UserDetail userDetail = userDetailsServiceImpl.loadUserByPhone(account.getPhone());
        validAccount(userDetail);
        vo.setCode(WechatLoginStatus.LOGIN_SUCCESS.getCode());
        miniappLogin(request, vo, userDetail.getId(), source);
        return vo;
    }

    public MiniAppLoginVo miniappPhoneLogin(HttpServletRequest request, String channelCode, MiniAppPhoneLoginDto dto) {
        TradeOpenChannel channel = TradeOpenFactory.channels.get(channelCode);
        if (channel == null) {
            throw new CustomException("登录信息错误");
        }
        int source = getMiniAppLoginSource(channelCode, dto.getSource());
        String phone = channel.getPhoneNumber(dto.getCode());
        // 根据手机号查用户是否存在
        AccountEntity account = accountService.findByPhone(phone);
        if (account != null) {
            // 修改入openId
            accountService.editOpenId(account.getId(), dto.getOpenId());
            return miniappLogin(request, dto.getOpenId(), phone, source);
        }
        // 否则根据openId查用户是否存在
        account = accountService.findByOpenId(dto.getOpenId());
        if (account != null) {
            return miniappLogin(request, dto.getOpenId(), phone, source);
        }
        // 否则注册新账号并登录
        RegisterDto registerDto = new RegisterDto();
        registerDto.setPhone(phone);
        registerDto.setOpenId(dto.getOpenId());
        registerDto.setSource(source);
        generateService.register(registerDto);
        return miniappLogin(request, dto.getOpenId(), phone, source);
    }

    private int getMiniAppLoginSource(String channelCode, int source) {
        if (source > 0) {
            return source;
        }
        if (TradeChannelCode.WECHAT.getCode().equals(channelCode)) {
            return CommonLoginSource.WX_AMP.getCode();
        }
        return 0;
    }

    private MiniAppLoginVo miniappLogin(HttpServletRequest request, String openId, String phone, int source) {
        UserDetail userDetail = userDetailsServiceImpl.loadUserByPhone(phone);
        // 验证账号
        validAccount(userDetail);
        MiniAppLoginVo vo = new MiniAppLoginVo();
        vo.setOpenId(openId);
        miniappLogin(request, vo, userDetail.getId(), source);
        return vo;
    }

    public LoginVo refreshTokenLogin(HttpServletRequest request, LoginDto dto) {
        String accessToken = refreshToken(dto.getRefreshToken());
        LoginVo vo = new LoginVo();
        vo.setRefreshToken(dto.getRefreshToken());
        vo.setAccessToken(accessToken);
        return vo;
    }

    public AuthScanVo scan(LoginScanDto dto) {
        // 看着没传，先写一个兜底
        if (dto.getSource() == 0) {
            dto.setSource(5);
        }
        String id = accountScanService.save(dto.getSource());
        String url = String.format("%s?scanId=%s", SCAN_PATH, id);
        String qrCode = QrCodeUtil.getBase64QRCode(url);
        AuthScanVo vo = new AuthScanVo();
        vo.setId(id);
        vo.setQrCode(qrCode);
        return vo;
    }

    /**
     * 扫码登录
     *
     * @param request
     * @param accountId
     * @param scanId
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanLogin(HttpServletRequest request, String accountId, String scanId) {
        AccountScanEntity scan = accountScanService.findById(scanId);
        AccountScanStatusVo scanStatusVo = accountScanService.validScanStatus(scan);
        if (scanStatusVo != null) {
            throw new CustomException(scanStatusVo.getStatusName());
        }
        UserDetail userDetail = userDetailsServiceImpl.loadUserById(accountId);
        validAccount(userDetail);
        LoginVo vo = new LoginVo();
        login(request, vo, userDetail.getId(), scan.getSource());
        // 登录信息存入扫码表
        String loginInfo = JsonUtil.toJSONString(vo);
        accountScanService.editLoginInfo(scanId, loginInfo);
    }

    public void editScanScanned(String id) {
        accountScanService.editStatus(id, AccountScanStatus.SCANNED.getCode());
    }

    public void editScanCancel(String id) {
        accountScanService.editStatus(id, AccountScanStatus.CANCEL.getCode());
    }

    public AuthScanLoginStatusVo scanStatus(String id) {
        AccountScanStatusVo scanStatus = accountScanService.findStatus(id);
        AuthScanLoginStatusVo vo = new AuthScanLoginStatusVo();
        vo.setStatus(scanStatus.getStatus());
        vo.setStatusName(scanStatus.getStatusName());
        vo.setLoginVo(scanStatus.getLoginVo());
        return vo;
    }

    /**
     * 验证登录次数
     *
     * @param phone
     */
    private void validLoginTotal(String phone) {
        String key = CaptchaConst.LOGIN_ERROR_TOTAL + phone;
        int total = 0;
        String totalResult = redisService.get(key);
        if (StringUtils.isNotBlank(totalResult)) {
            total = Integer.parseInt(totalResult);
        }
        if (total > 3) {
            throw new CustomException("登录次数过多，请使用验证码登录。");
        }
        redisService.set(key, String.valueOf(total));
    }

    /**
     * 验证账号
     *
     * @param userDetail
     * @return
     */
    private void validAccount(UserDetail userDetail) {
        if (userDetail == null) {
            throw new CustomException("账号不存在");
        }
        if (userDetail.getMerchantStatus() != CommonStatus.ENABLED.getCode()) {
            throw new CustomException("商户已禁用");
        }
        if (userDetail.getStoreStatus() != CommonStatus.ENABLED.getCode()) {
            throw new CustomException("门店已禁用");
        }
        if (userDetail.getStoreExpireDate() != null && LocalDate.now().isAfter(userDetail.getStoreExpireDate())) {
            throw new CustomException("门店已过期");
        }
        if (userDetail.getStatus() != CommonStatus.ENABLED.getCode()) {
            throw new CustomException("账号已禁用");
        }
    }

    /**
     * 验证密码
     *
     * @param password
     * @param rawPassword
     */
    private void validPassword(String password, String rawPassword) {
        if (!passwordEncoder.matches(password, rawPassword)) {
            throw new CustomException("密码错误");
        }
    }

    private void login(HttpServletRequest request, LoginVo vo, String accountId, int source) {
        vo.setAccessToken(getAccessToken(accountId));
        vo.setRefreshToken(getRefreshToken(accountId));
        loginLog(request, accountId, source);
    }

    private void miniappLogin(HttpServletRequest request, MiniAppLoginVo vo, String accountId, int source) {
        vo.setAccessToken(getAccessToken(accountId));
        vo.setRefreshToken(getRefreshToken(accountId));
        loginLog(request, accountId, source);
    }

    /**
     * 生成登录token并缓存(不做单点登录的情况这样写)
     *
     * @param accountId
     * @return
     */
    private String getAccessToken(String accountId) {
        String accountIdKey = ACCESS_TOKEN_ACCOUNT_PREFIX + accountId;
        String accessToken = redisService.get(accountIdKey);
        if (StringUtils.isBlank(accessToken)) {
            accessToken = Generate.generateUUID();
        }
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        redisService.set(key, accountId, ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
        redisService.set(accountIdKey, accessToken, ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
        return accessToken;
    }

    /**
     * 生成刷新token并缓存
     *
     * @param accountId
     * @return
     */
    private String getRefreshToken(String accountId) {
        String accountIdKey = REFRESH_TOKEN_ACCOUNT_PREFIX + accountId;
        String refreshToken = redisService.get(accountIdKey);
        if (StringUtils.isBlank(refreshToken)) {
            refreshToken = Generate.generateUUID();
        }
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        redisService.set(key, accountId, REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);
        redisService.set(accountIdKey, refreshToken, REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);
        return refreshToken;
    }

    private String refreshToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        String accountId = redisService.get(key);
        if (StringUtils.isBlank(accountId)) {
            throw new CustomException(ResultCode.EXPIRE_LOGIN);
        }
        return getAccessToken(accountId);
    }

    private void loginLog(HttpServletRequest request, String accountId, int source) {
        LocalDateTime now = LocalDateTime.now();
        int type = AccountLogType.LOGIN.getCode();
        accountLoginLogService.save(request, accountId, type, now, source);
    }
}
