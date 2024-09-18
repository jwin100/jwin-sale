package com.mammon.sms.channel.fish.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FishSendResultVo {

    private int code;

    private String smsId;

    private String subStat;

    private String subStatDes;

    private List<FishSendResultDetailVo> resDetail = new ArrayList<>();
}
