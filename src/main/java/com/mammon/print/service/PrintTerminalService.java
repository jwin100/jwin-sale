package com.mammon.print.service;

import cn.hutool.json.JSONUtil;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.PrintChannelFactory;
import com.mammon.print.channel.factory.model.PrintInfoVo;
import com.mammon.print.dao.PrintTerminalDao;
import com.mammon.print.domain.dto.PrintTerminalDto;
import com.mammon.print.domain.dto.PrintTerminalQuery;
import com.mammon.print.domain.entity.PrintChannelEntity;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import com.mammon.print.domain.enums.PrintTerminalStatus;
import com.mammon.print.domain.vo.PrintTerminalBindTypeVo;
import com.mammon.print.domain.vo.PrintTerminalVo;
import com.mammon.utils.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintTerminalService {

    @Resource
    private PrintTerminalDao printTerminalDao;

    @Resource
    private PrintChannelService printChannelService;

    @Transactional(rollbackFor = CustomException.class)
    public void save(long merchantNo, long storeNo, PrintTerminalDto dto) {
        PrintChannelEntity channel = printChannelService.findById(dto.getChannelId());
        if (channel == null) {
            throw new CustomException("获取渠道信息错误");
        }
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        factory.addPrint(dto.getName(), channel.getConfigStr(), dto.getFormConfig());
        PrintTerminalStatus status = factory.getPrintStatus(channel.getConfigStr(), dto.getFormConfig());
        String terminalCode = factory.getTerminalCode(dto.getFormConfig());

        PrintTerminalEntity entity = new PrintTerminalEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setTerminalCode(terminalCode);
        entity.setFormConfig(dto.getFormConfig());
        entity.setBindTypes(JsonUtil.toJSONString(dto.getBindTypes()));
        entity.setStatus(status.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        printTerminalDao.save(entity);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void edit(long merchantNo, long storeNo, String id, PrintTerminalDto dto) {
        PrintChannelEntity channel = printChannelService.findById(dto.getChannelId());
        if (channel == null) {
            throw new CustomException("获取渠道信息错误");
        }
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        factory.editPrint(dto.getName(), channel.getConfigStr(), dto.getFormConfig());
        PrintTerminalStatus status = factory.getPrintStatus(channel.getConfigStr(), dto.getFormConfig());
        String terminalKey = factory.getTerminalCode(dto.getFormConfig());

        PrintTerminalEntity entity = new PrintTerminalEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setTerminalCode(terminalKey);
        entity.setFormConfig(dto.getFormConfig());
        entity.setBindTypes(JsonUtil.toJSONString(dto.getBindTypes()));
        entity.setStatus(status.getCode());
        entity.setUpdateTime(LocalDateTime.now());
        printTerminalDao.edit(entity);
    }

    public void editStatus(String terminalCode, int status) {
        PrintTerminalEntity terminal = findByTerminalCode(terminalCode);
        if (terminal == null) {
            return;
        }
        editStatusById(terminal.getId(), status);
    }

    public void editStatusById(String id, int status) {
        printTerminalDao.editStatus(id, status);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void delete(String id) {
        PrintTerminalEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException("设备信息错误");
        }
        PrintChannelEntity channel = printChannelService.findById(entity.getChannelId());
        if (channel == null) {
            throw new CustomException("获取渠道信息错误");
        }
        printTerminalDao.delete(id);
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        factory.deletePrint(channel.getConfigStr(), entity.getFormConfig());
    }

    /**
     * 设备关机、重启
     *
     * @param id
     * @param restartType (0:关机,1:重启
     */
    @Transactional(rollbackFor = Exception.class)
    public void restart(String id, int restartType) {
        PrintTerminalEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException("设备信息错误");
        }
        PrintChannelEntity channel = printChannelService.findById(entity.getChannelId());
        if (channel == null) {
            throw new CustomException("获取渠道信息错误");
        }
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        factory.restartPrint(channel.getConfigStr(), entity.getFormConfig(), restartType);
        if (restartType != 1) {
            editStatusById(id, PrintTerminalStatus.OFFLINE.getCode());
        }
    }

    /**
     * 获取设备连线状态
     */
    public int findConnectStatus(String id) {
        PrintTerminalEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException("设备信息错误");
        }
        PrintChannelEntity channel = printChannelService.findById(entity.getChannelId());
        if (channel == null) {
            throw new CustomException("获取渠道信息错误");
        }
        BasePrintChannel factory = PrintChannelFactory.get(channel.getChannelCode());
        PrintTerminalStatus status = factory.getPrintStatus(channel.getConfigStr(), entity.getFormConfig());
        printTerminalDao.editStatus(id, status.getCode());
        return status.getCode();
    }

    public PrintTerminalVo info(String id) {
        PrintTerminalEntity entity = findById(id);
        if (entity == null) {
            return null;
        }
        return convertTerminal(entity);
    }

    public PrintTerminalEntity findById(String id) {
        return printTerminalDao.findById(id);
    }

    public PrintTerminalEntity findByTerminalCode(String terminalCode) {
        return printTerminalDao.findByTerminalCode(terminalCode);
    }

    public List<PrintTerminalEntity> findAllByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return printTerminalDao.findAllByIds(ids);
    }

    public List<PrintTerminalEntity> findAllByClassify(long merchantNo, long storeNo, int classify) {
        return printTerminalDao.findAllByClassify(merchantNo, storeNo, classify);
    }

    public PageVo<PrintTerminalVo> page(long merchantNo, long storeNo, PrintTerminalQuery dto) {
        int total = printTerminalDao.count(merchantNo, storeNo, dto);
        if (total <= 0) {
            return PageResult.of();
        }
        List<PrintTerminalEntity> list = printTerminalDao.page(merchantNo, storeNo, dto);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<PrintTerminalVo> vos = list.stream().map(this::convertTerminal).collect(Collectors.toList());
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, vos);
    }

    private PrintTerminalVo convertTerminal(PrintTerminalEntity entity) {
        if (entity == null) {
            return null;
        }
        PrintTerminalVo vo = new PrintTerminalVo();
        BeanUtils.copyProperties(entity, vo);

        List<Integer> bindTypes = JsonUtil.toList(entity.getBindTypes(), Integer.class);
        List<PrintTerminalBindTypeVo> bindTypeVos = bindTypes.stream().map(x -> {
            PrintTerminalBindTypeVo bindTypeVo = new PrintTerminalBindTypeVo();
            bindTypeVo.setType(x);
            return bindTypeVo;
        }).collect(Collectors.toList());
        vo.setBindTypes(bindTypeVos);

        PrintChannelEntity channel = printChannelService.findById(entity.getChannelId());
        if (channel != null) {
            vo.setChannelName(channel.getChannelName());
        }
        return vo;
    }
}
