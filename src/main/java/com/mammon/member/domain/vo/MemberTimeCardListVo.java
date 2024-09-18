package com.mammon.member.domain.vo;

import com.mammon.market.domain.vo.MarketTimeCardSpuVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2023/11/19 17:15
 */
@Data
public class MemberTimeCardListVo {

    private String id;

    private String memberId;

    private String timeCardId;

    private String name;

    /**
     * 有效期(0:永久有效,1:结束日期)
     */
    private int expireType;

    private LocalDateTime expireTime;

    private List<String> spuIds;

    private List<MarketTimeCardSpuVo> spus = new ArrayList<>();

    /**
     * 剩余计次次数
     */
    private int nowTimeCard;

    private int totalTimeCard;
}
