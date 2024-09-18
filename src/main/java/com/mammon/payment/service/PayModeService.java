package com.mammon.payment.service;

import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.payment.domain.vo.PayModeModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayModeService {

    public List<PayModeModel> getAll() {
        return PayModeConst.payModeModels;
    }

    public List<PayModeModel> cashierPayMode(long merchantNo, long storeNo, String accountId) {
        return PayModeConst.cashierOrderPayModeModels;
    }

    public List<PayModeModel> refundPayMode(long merchantNo, long storeNo, String accountId) {
        List<PayModeModel> vos = PayModeConst.cashierRefundPayModeModels;
        //默认没有开通，后面做了开通，再改这个地方
        boolean isThirdParty = false;
        if (!isThirdParty) {
            vos = vos.stream().filter(x -> !x.isThirdParty()).collect(Collectors.toList());
        }
        return vos;
    }

    public List<PayModeModel> assetPayMode(long merchantNo, long storeNo, String accountId) {
        List<PayModeModel> vos = PayModeConst.cashierMemberModeWayModels;
        vos = vos.stream().filter(x -> !x.isMemberOnly()).collect(Collectors.toList());
        //默认没有开通，后面做了开通，再改这个地方
        boolean isThirdParty = false;
        if (!isThirdParty) {
            vos = vos.stream().filter(x -> !x.isThirdParty()).collect(Collectors.toList());
        }
        return vos;
    }

    public PayModeModel findByPayCode(int payCode) {
        List<PayModeModel> vos = PayModeConst.payModeModels;
        return vos.stream().filter(x -> x.getCode() == payCode).findFirst().orElse(null);
    }
}
