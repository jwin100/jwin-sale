package com.mammon.print.service;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mammon.common.Generate;
import com.mammon.config.JsonMapper;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.print.dao.PrintTemplateDao;
import com.mammon.print.domain.dto.PrintTemplateEditDto;
import com.mammon.print.domain.dto.PrintTemplateSettingDto;
import com.mammon.print.domain.entity.PrintTemplateEntity;
import com.mammon.print.domain.enums.PrintTemplateType;
import com.mammon.print.domain.enums.PrintTemplateWidth;
import com.mammon.print.domain.query.PrintTemplateQuery;
import com.mammon.print.domain.vo.*;
import com.mammon.utils.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintTemplateService {

    @Resource
    private PrintTemplateDao printTemplateDao;

    /**
     * 各门店自己添加模板信息，不支持总店统一设置模板
     *
     * @param merchantNo
     * @param storeNo
     * @param dto
     * @return
     */
    public void edit(long merchantNo, long storeNo, PrintTemplateEditDto dto) {
        PrintTemplateType typeEnum = IEnum.getByCode(dto.getType(), PrintTemplateType.class);
        if (typeEnum == null) {
            throw new CustomException("打印模板错误");
        }

        PrintTemplateEntity entity = findByType(merchantNo, dto.getType());
        if (entity == null) {
            throw new CustomException("打印模板错误");
        }
        if (entity.getMerchantNo() == 0) {
            save(merchantNo, typeEnum, dto);
        } else {
            edit(merchantNo, entity.getId(), typeEnum, dto);
        }
    }

    public PrintTemplateEntity save(long merchantNo, PrintTemplateType typeEnum, PrintTemplateEditDto dto) {
        PrintTemplateEntity entity = new PrintTemplateEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setClassify(typeEnum.getClassify());
        entity.setType(typeEnum.getCode());
        entity.setTemplate(initTemplateModule(merchantNo, typeEnum.getCode(), dto.getModules()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        printTemplateDao.save(entity);
        return entity;
    }

    public int edit(long merchantNo, String id, PrintTemplateType typeEnum, PrintTemplateEditDto dto) {
        PrintTemplateEntity entity = new PrintTemplateEntity();
        entity.setId(id);
        entity.setTemplate(initTemplateModule(merchantNo, typeEnum.getCode(), dto.getModules()));
        entity.setUpdateTime(LocalDateTime.now());
        return printTemplateDao.edit(entity);
    }

    public PrintTemplateEntity findById(String id) {
        return printTemplateDao.findById(id);
    }

    public PrintTemplateEntity findByType(long merchantNo, int type) {
        PrintTemplateEntity template = printTemplateDao.findByType(merchantNo, type);
        if (template == null) {
            // 获取默认模板
            template = printTemplateDao.findByType(0, type);
        }
        return template;
    }

    /**
     * 根据分类获取商户模板
     *
     * @param merchantNo
     * @param type
     * @return
     */
    public PrintTemplateVo findDefaultByType(long merchantNo, int type) {
        // 获取商户门店下模板
        PrintTemplateEntity template = findByType(merchantNo, type);
        if (template == null) {
            return null;
        }
        PrintTemplateVo vo = new PrintTemplateVo();
        BeanUtils.copyProperties(template, vo);
//        TypeReference<ArrayList<TemplateModuleVo>> reference = new TypeReference<ArrayList<TemplateModuleVo>>() {
//        };
        vo.setTemplateModules(JsonUtil.toList(template.getTemplate()));
        return vo;
    }

    public List<PrintTemplateListVo> getList(long merchantNo, long storeNo, PrintTemplateQuery query) {
        List<PrintTemplateListVo> list = Arrays.stream(PrintTemplateType.values()).map(type -> {
            PrintTemplateListVo vo = new PrintTemplateListVo();
            vo.setType(type.getCode());
            vo.setClassify(type.getClassify());
            vo.setRemark(type.getRemark());
            return vo;
        }).collect(Collectors.toList());
        if (query.getClassify() != null) {
            return list.stream().filter(x -> x.getClassify() == query.getClassify()).collect(Collectors.toList());
        }
        return list;
    }

    public List<PrintTemplateTypeVo> getTemplateTypeList(int classify) {
        return Arrays.stream(PrintTemplateType.values()).filter(x -> x.getClassify() == classify).map(x -> {
            PrintTemplateTypeVo vo = new PrintTemplateTypeVo();
            vo.setType(x.getCode());
            vo.setTypeName(x.getName());
            return vo;
        }).collect(Collectors.toList());
    }

    public List<PrintTemplateWidthVo> getTemplateWidthList(int classify) {
        return Arrays.stream(PrintTemplateWidth.values()).filter(x -> x.getClassify().getCode() == classify).map(x -> {
            PrintTemplateWidthVo vo = new PrintTemplateWidthVo();
            vo.setCode(x.getCode());
            vo.setName(x.getName());
            return vo;
        }).collect(Collectors.toList());
    }

    private String initTemplateModule(long merchantNo, int type, List<PrintTemplateSettingDto> modules) {
        PrintTemplateVo template = findDefaultByType(merchantNo, type);
        if (template == null) {
            throw new CustomException("打印模板错误");
        }
        List<TemplateModuleVo> templateModules = template.getTemplateModules();
        templateModules.forEach(x -> {
            x.getItems().forEach(item -> {
                if (item.getItemCustomize() == 1) {
                    PrintTemplateSettingDto setting = modules.stream()
                            .filter(y -> y.getItemKey().equals(item.getItemKey()))
                            .findFirst().orElse(null);
                    if (setting != null) {
                        if (item.getItemType() == 1) {
                            item.setItemStatus(setting.getItemStatus());
                        } else {
                            item.setItemValue(setting.getItemValue());
                        }
                    }
                }
            });
        });
        return JsonUtil.toJSONString(templateModules);
    }
}
