package com.mammon.print.service;

import cn.hutool.json.JSONUtil;
import com.mammon.common.Generate;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.PrintChannelFactory;
import com.mammon.print.dao.PrintRecordDao;
import com.mammon.print.domain.dto.PrintActiveDto;
import com.mammon.print.domain.dto.PrintActiveProductDto;
import com.mammon.print.domain.dto.PrintActiveProductItemDto;
import com.mammon.print.domain.dto.PrintRecordSendDto;
import com.mammon.print.domain.entity.PrintChannelEntity;
import com.mammon.print.domain.entity.PrintRecordEntity;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import com.mammon.print.domain.enums.PrintRecordStatus;
import com.mammon.print.domain.enums.PrintTemplateClassify;
import com.mammon.print.domain.enums.PrintTemplateType;
import com.mammon.print.domain.model.PrintActiveModel;
import com.mammon.print.domain.vo.PrintTemplateVo;
import com.mammon.print.domain.vo.TemplateModuleVo;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrintRecordService {

    @Resource
    private PrintRecordDao printRecordDao;

    @Resource
    private PrintTemplateService printTemplateService;

    @Resource
    private PrintTerminalService printTerminalService;

    @Resource
    private PrintChannelService printChannelService;

    @Resource
    private PrintActiveService printActiveService;

    @Resource
    private AccountService accountService;

    public void send(long merchantNo, long storeNo, String accountId, PrintRecordSendDto dto) {
        // 判断是否开启打印
        PrintRecordEntity entity = new PrintRecordEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setPrintType(dto.getType());
        entity.setContent(JsonUtil.toJSONString(dto.getContents()));
        entity.setStatus(PrintRecordStatus.WAITING.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        PrintTemplateVo templateVo = printTemplateService.findDefaultByType(merchantNo, dto.getType());
        if (templateVo == null) {
            entity.setStatus(PrintRecordStatus.SUBMIT_FAIL.getCode());
            entity.setRemark("获取打印模板错误");
            save(entity);
            throw new CustomException("获取打印模板错误");
        }
        entity.setTemplateId(templateVo.getId());

        List<PrintTerminalEntity> terminals = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getTerminalId())) {
            PrintTerminalEntity terminal = printTerminalService.findById(dto.getTerminalId());
            if (terminal != null && terminal.getClassify() == templateVo.getClassify()) {
                terminals.add(terminal);
            }
        } else {
            terminals = printTerminalService.findAllByClassify(merchantNo, storeNo, templateVo.getClassify());
        }
        if (CollectionUtils.isEmpty(terminals)) {
            entity.setStatus(PrintRecordStatus.SUBMIT_FAIL.getCode());
            entity.setRemark("未获取到打印机信息");
            save(entity);
            throw new CustomException("未获取到打印机信息");
        }

        // 提交到渠道打印(返回提交结果修改到数据库,
        terminals.forEach(x -> {
            entity.setTerminalId(x.getId());
            save(entity);
            List<TemplateModuleVo> templateModules = templateVo.getTemplateModules();
            List<PrintActiveModel> printActiveModels = new ArrayList<>();
            templateModules.forEach(module -> {
                module.getItems().forEach(item -> {
                    printActiveService.switchModel(item, dto.getContents(), x.getWidth(), printActiveModels);
                });
            });
            // 发送打印
            PrintChannelEntity channel = printChannelService.findById(x.getChannelId());
            send(channel, x, dto.getOrderNo(), printActiveModels);
        });
    }

    public void save(PrintRecordEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        printRecordDao.save(entity);
    }

    // 要幂等修改
    public int update(String id, int fromStatus, int toStatus, String remark) {
        // 打印结果回调，修改到数据库
        return printRecordDao.edit(id, fromStatus, toStatus, remark);
    }

    public void test(long merchantNo, long storeNo, String accountId, String terminalId) {
        PrintTerminalEntity terminal = printTerminalService.findById(terminalId);
        if (terminal == null) {
            throw new CustomException("打印机信息错误");
        }
        int printType = PrintTemplateType.CASHIER_ORDER_TICKET.getCode();
        if (terminal.getClassify() == PrintTemplateClassify.PRICE_TAG.getCode()) {
            printType = PrintTemplateType.GOODS_PRICE_TAG.getCode();
        }
//        else if (terminal.getClassify() == PrintClassify.BAR_CODE.getCode()) {
//            printType = PrintType.GOODS_BAR_CODE.getCode();
//        }


        UserVo userVo = accountService.info(accountId);

        PrintActiveDto dto = new PrintActiveDto();
        dto.setMerchantName("捷盈");
        dto.setStoreName("捷盈1号店");
        if (userVo != null) {
            dto.setMerchantName(userVo.getMerchantName());
            dto.setStoreName(userVo.getStoreName());
        }
        dto.setBuyerRemark("购买商品备注信息,很长很长的备注信息，很长很长很长很长！");

        PrintActiveProductDto product = new PrintActiveProductDto();

        List<PrintActiveProductItemDto> items = new ArrayList<>();
        PrintActiveProductItemDto item1 = new PrintActiveProductItemDto();
        item1.setName("很长很长的商品名称 规格信息(规格1_规格2)");
        item1.setQuantity("10");
        item1.setReferenceAmount("10");
        item1.setRealAmount("100");
        items.add(item1);

        PrintActiveProductItemDto item2 = new PrintActiveProductItemDto();
        item2.setName("很长很长的商品名称 没有规格信息");
        item2.setDiscount("9.5");
        item2.setQuantity("10");
        item2.setReferenceAmount("10");
        item2.setRealAmount("100");
        items.add(item2);

        product.setItems(items);
        product.setProductTotal("2");
        product.setRealAmount("200");
        dto.setProduct(product);

        dto.setRealAmount("200");
        dto.setPayTypeName("微信支付");
        dto.setTradeNo("12331111213123123");
        dto.setCreateOrderTime("2020-08-08 22:01:00");
        dto.setOperationName("王老板");
        dto.setShopAddress("王老板的地址很长很长，但是很好记");
        dto.setOrderNo("2020202020020202002");
        dto.setFooterNotice("王老板的店非常欢迎你");


        PrintRecordSendDto sendDto = new PrintRecordSendDto();
        sendDto.setTerminalId(terminalId);
        sendDto.setContents(dto);
        sendDto.setType(printType);
        sendDto.setOrderNo("2020202020020202002");
        send(merchantNo, storeNo, accountId, sendDto);
    }

    private void send(PrintChannelEntity channel, PrintTerminalEntity terminal, String orderNo,
                      List<PrintActiveModel> printActiveModels) {
        if (channel == null) {
            return;
        }
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        factory.printIndex(channel.getConfigStr(), terminal, orderNo, printActiveModels);
    }
}
