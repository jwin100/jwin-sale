package com.mammon.chain.service;

import com.mammon.chain.dao.ChainDao;
import com.mammon.chain.dao.ChainSequenceDao;
import com.mammon.chain.domain.entity.ChainEntity;
import com.mammon.common.Generate;
import com.mammon.enums.CommonStatus;
import com.mammon.utils.CodeGen;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2022-10-17 14:58:14
 */
@Service
public class ChainService {

    public static final String SHORT_BASE_URL = "jwn8.cn";

    @Resource
    private ChainDao chainDao;

    @Resource
    private ChainSequenceDao chainSequenceDao;

    /**
     * 创建短连接
     *
     * @param path 长连接
     * @return 短连接
     */
    public String create(String path) {
        String code = generateCode();
        String shortUrl = String.format("%s/%s", SHORT_BASE_URL, code);

        ChainEntity entity = new ChainEntity();
        entity.setId(Generate.generateUUID());
        entity.setCode(code);
        entity.setUrl(shortUrl);
        entity.setLink(path);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        chainDao.save(entity);
        return shortUrl;
    }

    public String generateCode() {
        do {
            long seqNo = chainSequenceDao.next();
            String code = CodeGen.convert(seqNo);
            ChainEntity chain = chainDao.findByCode(code);
            if (chain == null) {
                return code;
            }
        } while (true);
    }

    public String findLinkByCode(String code) {
        ChainEntity entity = chainDao.findByCode(code);
        if (entity == null) {
            return null;
        }
        return entity.getLink();
    }
}
