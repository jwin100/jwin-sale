package com.mammon.print.channel.gain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GainStatusVo extends GainBasePrintVo {

    private List<GainStatusItemVo> statusList = new ArrayList<>();
}
