package com.mammon.cashier.service;

import com.mammon.cashier.domain.query.CashierHangQuery;
import com.mammon.cashier.domain.vo.*;
import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.common.PageResult;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.leaf.enums.DocketType;
import com.mammon.exception.CustomException;
import com.mammon.cashier.dao.CashierHangDao;
import com.mammon.cashier.domain.dto.CashierHangDto;
import com.mammon.cashier.domain.entity.CashierHangEntity;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CashierHangService {

    @Resource
    private CashierHangDao cashierHangDao;

    @Resource
    private LeafCodeService leafCodeService;
    @Autowired
    private MemberService memberService;

    public void create(long merchantNo, long storeNo, String accountId, CashierHangDto dto) {
        String hangNo = leafCodeService.generateDocketNo(DocketType.CASHIER_HANG);
        if (StringUtils.isBlank(hangNo)) {
            throw new CustomException("挂单异常");
        }
        CashierHangEntity entity = new CashierHangEntity();
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.isBlank(dto.getName())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
            dto.setName(df.format(now));
        }
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setName(dto.getName());
        entity.setHangNo(hangNo);
        entity.setDetail(JsonUtil.toJSONString(dto.getDetail()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setRemark(dto.getRemark());
        entity.setOperationId(accountId);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        cashierHangDao.save(entity);
    }

    public void hangDelete(String id) {
        cashierHangDao.deleteById(id);
    }

    public CashierHangEntity findById(long merchantNo, long storeNo, String accountId, String id) {
        return cashierHangDao.findById(merchantNo, storeNo, accountId, id);
    }

    @Transactional(rollbackFor = CustomException.class)
    public CashierHangVo hangTake(long merchantNo, long storeNo, String accountId, String id) {
        CashierHangEntity hang = findById(merchantNo, storeNo, accountId, id);
        if (hang == null) {
            throw new CustomException("取单异常");
        }
        hangDelete(id);
        return convertHangVo(hang);
    }

    public List<CashierHangVo> hangList(long merchantNo, long storeNo, String accountId) {
        List<CashierHangEntity> hangs = cashierHangDao.findAllByAccount(merchantNo, storeNo, accountId);
        return hangs.stream().map(this::convertHangVo).collect(Collectors.toList());
    }

    public PageVo<CashierHangVo> hangPage(long merchantNo, long storeNo, String accountId,
                                          CashierHangQuery query) {
        int total = cashierHangDao.count(merchantNo, storeNo, accountId);
        if (total <= 0) {
            return null;
        }
        List<CashierHangEntity> hangs = cashierHangDao.page(merchantNo, storeNo, accountId, query);
        List<CashierHangVo> items = hangs.stream().map(this::convertHangVo).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, items);
    }

    private CashierHangVo convertHangVo(CashierHangEntity hang) {
        CashierHangVo vo = new CashierHangVo();
        BeanUtils.copyProperties(hang, vo);
        vo.setDetail(JsonUtil.toObject(hang.getDetail(), CashierHangDetailVo.class));
        if (vo.getDetail() != null) {
            vo.setTotal(getTotal(vo.getDetail().getCards()));
            if (StringUtils.isNotBlank(vo.getDetail().getMemberId())) {
                MemberInfoVo member = memberService.findById(vo.getDetail().getMemberId());
                vo.getDetail().setMember(member);
            }
        }
        return vo;
    }

    private long getTotal(List<CashierHangCardVo> cards) {
        long weightCount = cards.stream()
                .filter(x -> x.getUnitType() == UnitType.WEIGHT.getCode())
                .count();
        long amountCount = cards.stream()
                .filter(x -> x.getUnitType() == UnitType.NUMBER.getCode())
                .mapToLong(x -> StockUtil.parse(x.getQuantity()))
                .sum();
        BigDecimal amount = StockUtil.parseBigDecimal(amountCount);
        return weightCount + amount.longValue();
    }
}
