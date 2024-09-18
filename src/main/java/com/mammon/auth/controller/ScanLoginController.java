package com.mammon.auth.controller;

import com.mammon.auth.domain.dto.LoginScanDto;
import com.mammon.auth.domain.vo.AuthScanLoginStatusVo;
import com.mammon.auth.domain.vo.AuthScanLoginVo;
import com.mammon.auth.domain.vo.AuthScanVo;
import com.mammon.auth.service.AuthService;
import com.mammon.common.ResultJson;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author dcl
 * @since 2024/5/28 9:46
 */
@RestController
@RequestMapping("/oauth/scan-login")
public class ScanLoginController {

    @Resource
    private AuthService authService;

    /**
     * 扫码登录
     * <p>
     * 获取二维码和扫码id
     *
     * @return
     */
    @PostMapping
    public ResultJson<AuthScanVo> scan(LoginScanDto dto) {
        return ResultJson.ok(authService.scan(dto));
    }

    /**
     * 获取扫码结果
     * <p>
     * 根据扫码id获取当前扫码状态
     *
     * @param scanId
     * @return
     */
    @GetMapping("/status/{scanId}")
    public ResultJson<AuthScanLoginStatusVo> getScanStatus(@PathVariable("scanId") String scanId) {
        return ResultJson.ok(authService.scanStatus(scanId));
    }

    /**
     * 扫码登录
     * <p>
     * 设置状态为已扫码
     *
     * @return
     */
    @PostMapping("/status/scanned/{scanId}")
    public ResultJson<Void> editScanScanned(@PathVariable("scanId") String scanId) {
        authService.editScanScanned(scanId);
        return ResultJson.ok();
    }

    /**
     * 扫码登录
     * <p>
     * 设置状态为已取消
     *
     * @return
     */
    @PostMapping("/status/cancel/{scanId}")
    public ResultJson<Void> editScanCancel(@PathVariable("scanId") String scanId) {
        authService.editScanCancel(scanId);
        return ResultJson.ok();
    }

    /**
     * 小程序端调扫码登录
     * <p>
     * 1.pc端选择扫码登录，调用生成二维码接口(/oauth/scan-login)获取到扫码id和二维码并开始轮询扫码结果接口
     * <p>
     * 2.小程序扫码后进入确认页，获取到二维码参数scanId并立即调用已扫码接口(/oauth/scan-login/status/scanned/{scanId})标记已扫码状态
     * <p>
     * 3.小程序确认页点击确定登录调用扫码登录接口(/oauth/scan-login/login/{scanId})，返回code=200代表登录成功
     * <p>
     * 4.pc端轮询扫码结果接口(/oauth/scan-login/status/{scanId})获取登录状态和登录结果
     * <p>
     * 5.取消登录调用取消接口(/oauth/scan-login/status/cancel/{scanId})
     *
     * @param scanId
     * @return
     */
    @PostMapping("/login/{scanId}")
    public ResultJson<Void> scanLogin(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @PathVariable("scanId") String scanId,
                                      HttpServletRequest request) {
        authService.scanLogin(request, accountId, scanId);
        return ResultJson.ok();
    }
}
