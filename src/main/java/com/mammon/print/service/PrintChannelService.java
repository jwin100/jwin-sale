package com.mammon.print.service;

import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.PrintChannelFactory;
import com.mammon.print.dao.PrintChannelDao;
import com.mammon.print.dao.PrintChannelMapDao;
import com.mammon.print.domain.entity.PrintChannelEntity;
import com.mammon.print.domain.entity.PrintChannelMapEntity;
import com.mammon.print.domain.vo.PrintChannelListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintChannelService {

    @Resource
    private PrintChannelDao printChannelDao;

    @Resource
    private PrintChannelMapDao printChannelMapDao;

    public PrintChannelEntity findById(String id) {
        return printChannelDao.findById(id);
    }

    public PrintChannelEntity findByChannelCode(String channelCode) {
        return printChannelDao.findByChannelCode(channelCode);
    }

    public List<PrintChannelListVo> findListByClassify(Integer classify) {
        if (classify == null) {
            return Collections.emptyList();
        }
        List<PrintChannelMapEntity> channelMaps = printChannelMapDao.findListByClassify(classify);
        List<String> channelIds = channelMaps.stream()
                .map(PrintChannelMapEntity::getChannelId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(channelIds)) {
            return Collections.emptyList();
        }
        List<PrintChannelEntity> list = printChannelDao.findListByIds(channelIds);
        return list.stream().map(x -> {
            PrintChannelListVo vo = new PrintChannelListVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public String findFormConfig(String channelId) {
        PrintChannelEntity entity = printChannelDao.findById(channelId);
        if (entity == null || StringUtils.isBlank(entity.getFormConfig())) {
            return null;
        }
        return entity.getFormConfig();
    }
}
