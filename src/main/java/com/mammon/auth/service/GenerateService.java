package com.mammon.auth.service;

import com.mammon.auth.domain.vo.LoginVo;
import com.mammon.exception.CustomException;
import com.mammon.merchant.domain.enums.BasicConfigConst;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.auth.domain.dto.RegisterDto;
import com.mammon.clerk.service.AccountService;
import com.mammon.service.RedisService;
import com.mammon.utils.ShortSnowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class GenerateService {

    @Resource
    private RedisService redisService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private AccountService accountService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    @Resource
    private AuthService authService;

    /**
     * 注册用户并返回登录信息
     *
     * @param request HttpServletRequest对象，用于获取请求信息
     * @param dto     注册所需信息的DTO对象
     * @return 登录信息对象LoginVo
     * @throws CustomException 如果短信验证码验证失败或手机号已存在则抛出此异常
     * @Transactional(rollbackFor = CustomException.class) 使用事务注解，当方法内发生CustomException异常时，将回滚事务
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginVo register(HttpServletRequest request, RegisterDto dto) {
        smsCaptchaService.validSmsCaptcha(dto.getPhone(), dto.getSmsCaptcha());
        validExists(dto.getPhone());
        long merchantNo = ShortSnowUtil.snowFlake();
        long storeNo = BasicConfigConst.STORE_START_NO + 1;

        String accountId = accountService.register(merchantNo, storeNo, dto.getPhone(), null);
        merchantService.register(merchantNo, accountId, dto);
        merchantStoreService.register(merchantNo, storeNo, accountId, dto.getPhone());
        // 注册自动登录
        return authService.registerLogin(request, dto.getPhone(), dto.getSource());
    }

    /**
     * 注册新用户
     *
     * @param dto 注册新用户所需的参数对象
     * @throws Exception 当注册新用户过程中发生异常时抛出
     * @Transactional(rollbackFor = Exception.class) 使用事务注解，当方法内发生异常时，将回滚事务
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDto dto) {
        validExists(dto.getPhone());
        long merchantNo = ShortSnowUtil.snowFlake();
        long storeNo = BasicConfigConst.STORE_START_NO + 1;
        String accountId = accountService.register(merchantNo, storeNo, dto.getPhone(), dto.getOpenId());
        merchantService.register(merchantNo, accountId, dto);
        merchantStoreService.register(merchantNo, storeNo, accountId, dto.getPhone());
    }

    private void validExists(String phone) {
        boolean existsAccount = accountService.existByPhone(phone);
        if (existsAccount) {
            throw new CustomException("注册失败：手机号已注册");
        }
    }
}
