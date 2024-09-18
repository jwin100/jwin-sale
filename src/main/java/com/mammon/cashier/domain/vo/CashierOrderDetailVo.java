package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.enums.CashierOrderStatus;
import com.mammon.cashier.domain.enums.CashierRefundMark;
import com.mammon.cashier.domain.model.CashierPayTypeModel;
import com.mammon.enums.CommonSource;
import com.mammon.enums.IEnum;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.clerk.domain.vo.UserVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierOrderDetailVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    /**
     * 自定义单号
     */
    private String customerNo;

    /**
     * 订单号
     */
    private String orderNo;

    private int category;

    private int type;

    private String subject;

    /**
     * 原价
     */
    private BigDecimal originalAmount;

    /**
     * 抹零类型
     */
    private int ignoreType;

    private String ignoreTypeName;

    /**
     * 抹零金额
     */
    private BigDecimal ignoreAmount;

    /**
     * 整单折扣
     */
    private BigDecimal discount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠金额
     */
    private BigDecimal preferentialAmount;

    private BigDecimal collectAmount;

    /**
     * 调整金额(手动加减价)
     */
    private BigDecimal adjustAmount;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 实付
     */
    private BigDecimal realityAmount;

    /**
     * 计次核销次数
     */
    private long countedTotal;

    /**
     * 本单积分
     */
    private long integral;

    /**
     * 支付方式
     */
    private List<CashierPayTypeModel> payType;

    /**
     * 订单状态
     */
    private int status;

    private String statusName;

    private String message;

    /**
     * 退款标识
     *
     * @see CashierRefundMark
     */
    private int refundMark;

    private String refundMarkName;

    /**
     * 退货总金额
     */
    private BigDecimal refundAmount;


    /**
     * 已退积分
     */
    private long refundIntegral;

    /**
     * 会员
     */
    private String memberId;

    /**
     * 发送短信,1:发送,0:不发
     */
    private int sendSms;

    /**
     * 操作人(下单人)
     */
    private String operationId;

    /**
     * 服务人员(json字符串)
     */
    private List<String> serviceAccountIds;

    /**
     * 订单来源
     */
    private int source;

    private String sourceName;

    /**
     * 订单项
     */
    private long skuItem;

    /**
     * 商品数
     */
    private long skuTotal;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 销售时间
     */
    private LocalDateTime cashierTime;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    private List<CashierOrderProductVo> skus = new ArrayList<>();

    private List<CashierOrderPayVo> pays = new ArrayList<>();

    /**
     * 服务人员信息
     */
    private List<UserVo> serviceAccounts = new ArrayList<>();

    /**
     * 操作人信息
     */
    private UserVo operationAccount;

    /**
     * 会员信息
     */
    private MemberInfoVo member;

    public String getSourceName() {
        return IEnum.getNameByCode(this.getSource(), CommonSource.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CashierOrderStatus.class);
    }

    public String getRefundMarkName() {
        return IEnum.getNameByCode(this.getRefundMark(), CashierRefundMark.class);
    }

    public String getIgnoreTypeName() {
        return ignoreTypeName;
    }
}
