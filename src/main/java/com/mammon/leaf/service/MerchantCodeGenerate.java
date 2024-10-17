package com.mammon.leaf.service;

import com.mammon.merchant.dao.MerchantDao;
import com.mammon.leaf.dao.MerchantCodeSeqDao;
import com.mammon.utils.CodeGen;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/10/9 23:21
 */
@Service
public class MerchantCodeGenerate {

    @Resource
    private MerchantCodeSeqDao merchantCodeSeqDao;

    @Resource
    private MerchantDao merchantDao;

    public long create() {
        do {
            long seqNo = merchantCodeSeqDao.next();
            long code = CodeGen.convertNumberCode(seqNo);
            if (!merchantDao.existsMerchantNo(code)) {
                return code;
            }
        } while (true);
    }
}
