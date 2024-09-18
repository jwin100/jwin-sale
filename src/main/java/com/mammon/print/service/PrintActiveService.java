package com.mammon.print.service;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.print.domain.dto.PrintActiveDto;
import com.mammon.print.domain.dto.PrintActiveProductDto;
import com.mammon.print.domain.enums.PrintActiveAlignConst;
import com.mammon.print.domain.enums.PrintActiveBoldConst;
import com.mammon.print.domain.enums.PrintActiveSizeConst;
import com.mammon.print.domain.enums.PrintActiveTypeConst;
import com.mammon.print.domain.model.PrintActiveModel;
import com.mammon.print.domain.vo.TemplateItemVo;
import com.mammon.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrintActiveService {

    public void switchModel(TemplateItemVo item, PrintActiveDto dto, int width, List<PrintActiveModel> printActiveModels) {
        String line = initLine(width);
        PrintActiveModel lineModel = convertModel(PrintActiveTypeConst.DIVISION, PrintActiveAlignConst.ALIGN_CENTER,
                PrintActiveSizeConst.TWICE, PrintActiveBoldConst.NORMAL, line);
        PrintActiveModel brModel = convertBrModel();
        // 一行一样解析
        if (item.getItemKey().equals("merchantNameSwitch") && item.getItemStatus() == 1) {
            PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_CENTER,
                    PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, dto.getMerchantName());
            if (model != null) {
                printActiveModels.add(model);
                printActiveModels.add(brModel);
            }
        }
        if (item.getItemKey().equals("storeNameSwitch") && item.getItemStatus() == 1) {
            PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_CENTER,
                    PrintActiveSizeConst.AGAIN, PrintActiveBoldConst.BOLD, dto.getStoreName());
            if (model != null) {
                printActiveModels.add(model);
                printActiveModels.add(brModel);
            }
        }
        if (item.getItemKey().equals("buyerRemarkSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getBuyerRemark())) {
                String buyerRemark = "买家备注：" + dto.getBuyerRemark();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, buyerRemark);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }

                if (lineModel != null) {
                    printActiveModels.add(lineModel);
                }
            }
        }
        if (item.getItemKey().equals("goodsInfoSwitch") && item.getItemStatus() == 1) {
            if (dto.getProduct() == null) {
                return;
            }

            List<PrintActiveModel> printActiveTables = new ArrayList<>();

            String productNameHeaderDesc = "商品";
            PrintActiveModel productNameHeader = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_TABLE,
                    PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, productNameHeaderDesc);
            if (productNameHeader != null) {
                printActiveTables.add(productNameHeader);
            }
            String productQuantityHeaderDesc = "单价,数量,小计";
            PrintActiveModel productQuantityHeader = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_TABLE,
                    PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, productQuantityHeaderDesc);
            if (productQuantityHeader != null) {
                printActiveTables.add(productQuantityHeader);
            }

            if (lineModel != null) {
                printActiveModels.add(lineModel);
            }

            PrintActiveProductDto product = dto.getProduct();

            product.getItems().forEach(x -> {
                String productNameDesc = x.getName();
                PrintActiveModel productNameBody = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_TABLE,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, productNameDesc);
                if (productNameBody != null) {
                    printActiveTables.add(productNameBody);
                }

                String discountStr = "";
                if (StringUtils.isNotBlank(x.getDiscount())) {
                    discountStr = "(" + x.getDiscount() + "折)";
                }
                String productQuantityDesc = x.getReferenceAmount() + "," + x.getQuantity() + "," + x.getRealAmount() + discountStr;
                PrintActiveModel productQuantityBody = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_TABLE,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, productQuantityDesc);
                if (productQuantityBody != null) {
                    printActiveTables.add(productQuantityBody);
                }
            });
            PrintActiveModel productTable = convertModel(PrintActiveTypeConst.TABLE, PrintActiveAlignConst.ALIGN_TABLE,
                    PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, JsonUtil.toJSONString(printActiveTables));
            if (productTable != null) {
                printActiveModels.add(productTable);
            }

            String productFooterDesc = "共" + product.getProductTotal() + "件,合计：" + product.getRealAmount();
            PrintActiveModel productFooter = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT_RIGHT,
                    PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, productFooterDesc);
            if (productFooter != null) {
                printActiveModels.add(productFooter);
            }

            if (lineModel != null) {
                printActiveModels.add(lineModel);
            }
        }

        boolean discountBo = false;
        if (item.getItemKey().equals("discountDetailSwitch") && item.getItemStatus() == 1) {

            String discountDesc = "优惠";

            if (StringUtils.isNotBlank(dto.getDiscount())) {
                String discountStr = "整单折扣：" + dto.getDiscount();
                PrintActiveModel discountModel = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT_RIGHT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, discountDesc + "," + discountStr);
                if (discountModel != null) {
                    discountBo = true;
                    printActiveModels.add(discountModel);
                    printActiveModels.add(brModel);
                }
            }

            if (StringUtils.isNotBlank(dto.getIgnoreAmount())) {
                String ignoreAmountStr = "抹零金额：" + dto.getIgnoreAmount();
                if (!discountBo) {
                    ignoreAmountStr = discountDesc + "," + ignoreAmountStr;
                }
                PrintActiveModel ignoreModel = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_RIGHT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, ignoreAmountStr);
                if (ignoreModel != null) {
                    discountBo = true;
                    printActiveModels.add(ignoreModel);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (item.getItemKey().equals("discountTotalSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getTotalDiscountAmount())) {
                String totalDiscountAmount = "总优惠：" + dto.getTotalDiscountAmount();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_RIGHT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, totalDiscountAmount);
                if (model != null) {
                    discountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (discountBo && lineModel != null) {
            printActiveModels.add(lineModel);
        }

        boolean payAmountBo = false;
        if (item.getItemKey().equals("actualAmountSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getRealAmount())) {
                String realAmount = "实收金额：" + dto.getRealAmount();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, realAmount);
                if (model != null) {
                    payAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (item.getItemKey().equals("paymentMethodSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getPayTypeName())) {
                String payTypeName = "支付方式：" + dto.getPayTypeName();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, payTypeName);
                if (model != null) {
                    payAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (item.getItemKey().equals("thirdPayNoSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getTradeNo())) {
                String tradeNo = "第三方支付单号：" + dto.getTradeNo();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, tradeNo);
                if (model != null) {
                    payAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (payAmountBo && lineModel != null) {
            printActiveModels.add(lineModel);
        }

        boolean refundAmountBo = false;
        if (item.getItemKey().equals("refundAmountSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getRealAmount())) {
                String realAmount = "退款金额：" + dto.getRealAmount();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.BOLD, realAmount);
                if (model != null) {
                    refundAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (item.getItemKey().equals("refundMethodSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getPayTypeName())) {
                String payTypeName = "退款方式：" + dto.getPayTypeName();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, payTypeName);
                if (model != null) {
                    refundAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }

        if (item.getItemKey().equals("thirdRefundNoSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getTradeNo())) {
                String tradeNo = "第三方退款单号：" + dto.getTradeNo();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, tradeNo);
                if (model != null) {
                    refundAmountBo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (refundAmountBo && lineModel != null) {
            printActiveModels.add(lineModel);
        }

        if (item.getItemKey().equals("customerInfoSwitch") && item.getItemStatus() == 1) {
            boolean bo = false;
            if (StringUtils.isNotBlank(dto.getMemberName())) {
                String memberName = "会员姓名：" + dto.getMemberName();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, memberName);
                if (model != null) {
                    bo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
            if (StringUtils.isNotBlank(dto.getMemberPhone())) {
                String memberPhone = "会员电话：" + dto.getMemberPhone();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, memberPhone);
                if (model != null) {
                    bo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
            if (StringUtils.isNotBlank(dto.getMemberIntegral())) {
                String memberIntegral = "会员积分：" + dto.getMemberIntegral();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, memberIntegral);
                if (model != null) {
                    bo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
            if (StringUtils.isNotBlank(dto.getMemberRecharge())) {
                String memberIntegral = "储值余额：" + dto.getMemberRecharge();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, memberIntegral);
                if (model != null) {
                    bo = true;
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
            if (bo && lineModel != null) {
                printActiveModels.add(lineModel);
            }
        }

        if (item.getItemKey().equals("orderCreateTimeSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getCreateOrderTime())) {
                String content = "下单时间：" + dto.getCreateOrderTime();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("orderRefundTimeSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getCreateOrderTime())) {
                String content = "退款时间：" + dto.getCreateOrderTime();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("operatorSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getOperationName())) {
                String content = "操作员：" + dto.getOperationName();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("salesGuideSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getServiceName())) {
                String content = "销售人员：" + dto.getServiceName();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("storeAddressSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getShopAddress())) {
                String content = "门店地址：" + dto.getShopAddress();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("orderNoSwitch") && item.getItemStatus() == 1) {
            if (StringUtils.isNotBlank(dto.getOrderNo())) {
                String content = "订单编号：" + dto.getOrderNo();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_LEFT,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
        if (item.getItemKey().equals("bottomNoticesDesc")) {
            if (StringUtils.isNotBlank(item.getItemValue())) {
                String content = item.getItemValue();
                PrintActiveModel model = convertModel(PrintActiveTypeConst.TEXT, PrintActiveAlignConst.ALIGN_CENTER,
                        PrintActiveSizeConst.NORMAL, PrintActiveBoldConst.NORMAL, content);
                if (model != null) {
                    printActiveModels.add(brModel);
                    printActiveModels.add(model);
                    printActiveModels.add(brModel);
                }
            }
        }
    }

    private String initLine(int width) {
        int length = 16;
        if (width == 80) {
            length = 24;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    private PrintActiveModel convertModel(int type, int textAlign, int textSize, int textWeight, String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        PrintActiveModel model = new PrintActiveModel();
        model.setType(type);
        model.setTextAlign(textAlign);
        model.setTextSize(textSize);
        model.setTextBold(textWeight);
        model.setContent(content);
        return model;
    }

    private PrintActiveModel convertBrModel() {
        PrintActiveModel model = new PrintActiveModel();
        model.setType(PrintActiveTypeConst.LINE);
        return model;
    }
}
