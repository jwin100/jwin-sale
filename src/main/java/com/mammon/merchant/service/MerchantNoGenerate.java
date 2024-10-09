package com.mammon.merchant.service;

import com.mammon.merchant.dao.MerchantDao;
import com.mammon.merchant.dao.MerchantNoSequenceDao;
import com.mammon.merchant.utils.CodeGen;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/10/9 23:21
 */
@Service
public class MerchantNoGenerate {

    @Resource
    private MerchantNoSequenceDao merchantNoSequenceDao;

    @Resource
    private MerchantDao merchantDao;

    public long create() {
        do {
            long seqNo = merchantNoSequenceDao.next();
            long code = Long.parseLong(CodeGen.convert(seqNo));
            if (!merchantDao.existsMerchantNo(code)) {
                return code;
            }
        } while (true);
    }
}
