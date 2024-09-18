package com.mammon.print.channel.gain;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.model.PrintInfoVo;
import com.mammon.print.channel.gain.model.*;
import com.mammon.print.common.ConvertServletParam;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import com.mammon.print.domain.enums.*;
import com.mammon.print.domain.model.PrintActiveModel;
import com.mammon.print.domain.model.PrintTerminalStatusModel;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author dcl
 * @since 2024/3/26 18:24
 */
@Service
@Slf4j
public class GainService implements BasePrintChannel {

    @Resource
    private GainClient gainClient;

    @Override
    public String getTerminalCode(String formConfigStr) {
        GainFormConfigModel dto = JsonUtil.toObject(formConfigStr, GainFormConfigModel.class);
        return dto.getTerminalCode();
    }

    @Override
    public void addPrint(String terminalName, String configStr, String formConfigStr) {
        GainConfigModel config = JsonUtil.toObject(configStr, GainConfigModel.class);
        if (config == null) {
            throw new CustomException("渠道配置信息错误");
        }
        GainFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, GainFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        String reqTime = getTimeStamp();
        String securityStr = config.getClientId() + reqTime + config.getClientSecret() + formConfig.getTerminalCode();

        GainAddPrintDto dto = new GainAddPrintDto();
        dto.setReqTime(reqTime);
        dto.setSecurityCode(md5Secret(securityStr));
        dto.setMemberCode(config.getClientId());
        dto.setDeviceId(formConfig.getTerminalCode());
        dto.setDevName(terminalName);
        GainBasePrintVo result = gainClient.addPrint(dto);
        log.info("添加设备返回信息：{}", JsonUtil.toJSONString(result));
        if (1 != result.getCode()) {
            throw new CustomException(result.getMsg());
        }
    }

    @Override
    public void editPrint(String terminalName, String configStr, String formConfigStr) {
        GainConfigModel config = JsonUtil.toObject(configStr, GainConfigModel.class);
        if (config == null) {
            throw new CustomException("渠道配置信息错误");
        }
        GainFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, GainFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        String reqTime = getTimeStamp();
        String securityStr = config.getClientId() + reqTime + config.getClientSecret() + formConfig.getTerminalCode();

        GainAddPrintDto dto = new GainAddPrintDto();
        dto.setReqTime(reqTime);
        dto.setSecurityCode(md5Secret(securityStr));
        dto.setMemberCode(config.getClientId());
        dto.setDeviceId(formConfig.getTerminalCode());
        dto.setDevName(terminalName);
        GainBasePrintVo result = gainClient.editPrint(dto);
        log.info("修改设备返回信息：{}", JsonUtil.toJSONString(result));
        if (1 != result.getCode()) {
            throw new CustomException(result.getMsg());
        }
    }

    @Override
    public void deletePrint(String configStr, String formConfigStr) {
        GainConfigModel config = JsonUtil.toObject(configStr, GainConfigModel.class);
        if (config == null) {
            throw new CustomException("渠道配置信息错误");
        }
        GainFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, GainFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        String reqTime = getTimeStamp();
        String securityStr = config.getClientId() + reqTime + config.getClientSecret() + formConfig.getTerminalCode();

        GainDeletePrintDto dto = new GainDeletePrintDto();
        dto.setReqTime(reqTime);
        dto.setSecurityCode(md5Secret(securityStr));
        dto.setMemberCode(config.getClientId());
        dto.setDeviceId(formConfig.getTerminalCode());
        GainBasePrintVo result = gainClient.deletePrint(dto);
        log.info("print-delete,request:{},result:{}", JsonUtil.toJSONString(dto), result);
        if (1 != result.getCode()) {
            throw new CustomException(result.getMsg());
        }
    }

    @Override
    public void restartPrint(String configStr, String formConfigStr, int restartType) {
        throw new CustomException("此打印机暂无此功能");
    }

    @Override
    public PrintTerminalStatus getPrintStatus(String configStr, String formConfigStr) {
        GainConfigModel config = JsonUtil.toObject(configStr, GainConfigModel.class);
        if (config == null) {
            throw new CustomException("渠道配置信息错误");
        }
        GainFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, GainFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        String reqTime = getTimeStamp();
        String securityStr = config.getClientId() + reqTime + config.getClientSecret();

        GainStatusDto dto = new GainStatusDto();
        dto.setReqTime(reqTime);
        dto.setSecurityCode(md5Secret(securityStr));
        dto.setMemberCode(config.getClientId());
        dto.setDeviceId(formConfig.getTerminalCode());
        GainStatusVo result = gainClient.getPrintStatus(dto);
        if (1 != result.getCode()) {
            throw new CustomException(result.getMsg());
        }
        GainStatusItemVo itemVo = result.getStatusList().stream()
                .filter(x -> x.getDeviceId().equals(formConfig.getTerminalCode()))
                .findFirst().orElse(null);
        if (itemVo == null) {
            throw new CustomException("设备信息错误");
        }
        if (itemVo.getOnline() == 0) {
            return PrintTerminalStatus.OFFLINE;
        } else {
            if (itemVo.getStatus() == 2) {
                return PrintTerminalStatus.PAPER;
            }
            return PrintTerminalStatus.ONLINE;
        }
    }

    @Override
    public PrintInfoVo getPrintInfo(String configStr, String formConfigStr) {
        return null;
    }

    @Override
    public void printIndex(String configStr, PrintTerminalEntity terminal, String orderNo, List<PrintActiveModel> printActiveModels) {
        GainConfigModel config = JsonUtil.toObject(configStr, GainConfigModel.class);
        if (config == null) {
            throw new CustomException("渠道配置信息错误");
        }
        GainFormConfigModel formConfig = JsonUtil.toObject(terminal.getFormConfig(), GainFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        String reqTime = getTimeStamp();
        // 注意顺序（按文档来）
        String securityStr = config.getClientId() + formConfig.getTerminalCode() + reqTime + config.getClientSecret();

        GainPrintDto dto = new GainPrintDto();
        dto.setReqTime(reqTime);
        dto.setSecurityCode(md5Secret(securityStr));
        dto.setMemberCode(config.getClientId());
        dto.setDeviceId(formConfig.getTerminalCode());
        dto.setMsgDetail(printContent(terminal, printActiveModels));
        dto.setCharset("1");
        log.info("发送打印信息,req:{}", JsonUtil.toJSONString(dto));
        GainBasePrintVo result = gainClient.printIndex(dto);
        log.info("发送打印信息,resp:{}", JsonUtil.toJSONString(result));
        if (1 != result.getCode()) {
            throw new CustomException("打印失败");
        }
    }

    @Override
    public PrintTerminalStatusModel printStatusNotify(String configStr, HttpServletRequest request) {
        Map<String, String> params = ConvertServletParam.convertParam(request);
        if (params.isEmpty()) {
            return null;
        }
        // 接口只接收打印机状态推送，其他的忽略
        String cmd = params.getOrDefault("cmd", "");
        if (!"printStatus".equals(cmd)) {
            return null;
        }
        int status = PrintTerminalStatus.OFFLINE.getCode();
        String online = params.getOrDefault("status", "0");
        switch (online) {
            case "1":
            case "3":
            case "4":
            case "5":
            case "8":
                status = PrintTerminalStatus.ONLINE.getCode();
                break;
            case "2":
                status = PrintTerminalStatus.PAPER.getCode();
                break;
            case "0":
            default:
        }

        PrintTerminalStatusModel model = new PrintTerminalStatusModel();
        model.setTerminalCode(params.get("deviceID"));
        model.setStatus(status);
        return model;
    }

    @Override
    public Object responseSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("data", "OK");
        return map;
    }

    @Override
    public Object responseFail() {
        Map<String, String> map = new HashMap<>();
        map.put("data", "FAIL");
        return map;
    }

    private String getTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        long timeStampMillis = now.toInstant(ZoneOffset.UTC).toEpochMilli();
        return String.valueOf(timeStampMillis);
    }

    private String md5Secret(String securityStr) {
        return MD5.create().digestHex(securityStr.getBytes(StandardCharsets.UTF_8));
    }

    private String printContent(PrintTerminalEntity terminal, List<PrintActiveModel> printActiveModels) {
        StringBuilder sb = new StringBuilder();
        printActiveModels.forEach(x -> {
            if (x.getType() == PrintActiveTypeConst.TEXT) {
                sb.append(textFormat(x)).append(textBr());
            }
            if (x.getType() == PrintActiveTypeConst.TABLE) {
                // 表格
                StringBuilder table = new StringBuilder();
                List<String> trs = new ArrayList<>();
                List<PrintActiveModel> printActiveTables = JsonUtil.toList(x.getContent(), PrintActiveModel.class);
                for (PrintActiveModel model : printActiveTables) {
                    // 每一行
                    List<String> tds = new ArrayList<>();
                    String[] contents = model.getContent().split(",");
                    for (String s : contents) {
                        tds.add(lineTd(s));
                    }
                    trs.add(lineTr(terminal.getWidth(), tds));
                }

                sb.append(lineTable(terminal.getWidth(), trs)).append(textBr());
            }
        });
        return sb.toString();
    }

    private String textFormat(PrintActiveModel model) {
        // 字体宽度
        String wSize = textWidth(model.getTextBold());
        String hSize = textSize(model.getTextSize());
        String align = textAlign(model.getTextAlign());
        return String.format("<gpWord %s %s %s>%s</gpWord>", align, wSize, hSize, model.getContent());
    }

    private String textWidth(int width) {
        String wSize = "Wsize=0";
        if (width == PrintActiveBoldConst.BOLD) {
            wSize = "Wsize=1";
        }
        return wSize;
    }

    private String textSize(int height) {
        String hSize = "Hsize=0";
        if (height == PrintActiveSizeConst.AGAIN) {
            hSize = "Hsize=1";
        } else if (height == PrintActiveSizeConst.TWICE) {
            hSize = "Hsize=2";
        }
        return hSize;
    }

    private String textAlign(int align) {
        String textAlign = "Align=0";
        if (align == PrintActiveAlignConst.ALIGN_RIGHT) {
            textAlign = "Align=2";
        } else if (align == PrintActiveAlignConst.ALIGN_CENTER) {
            textAlign = "Align=1";
        }
        return textAlign;
    }

    private String textBr() {
        return "<gpBr/>";
    }

    /**
     * 打印机宽度
     *
     * @param terminalWidth
     * @return
     */
    private String widthType(int terminalWidth) {
        String type = "Type=0";
        if (terminalWidth == 80) {
            type = "Type=1";
        }
        return type;
    }

    private String lineTable(int terminalWidth, List<String> trs) {
        String trStr = String.join("", trs);
        String widthType = widthType(terminalWidth);
        return String.format("<gpTable %s>%s</gpTable>", widthType, trStr);
    }

    private String lineTr(int terminalWidth, List<String> tds) {
        if (CollectionUtils.isEmpty(tds)) {
            return "";
        }
        String tdStr = String.join("", tds);
        String widthType = widthType(terminalWidth);
        if (tds.size() == 2) {
            return String.format("<gpTR2 %s>%s</gpTR2>", widthType, tdStr);
        } else if (tds.size() == 3) {
            return String.format("<gpTR3 %s>%s</gpTR2>", widthType, tdStr);
        } else if (tds.size() == 4) {
            return String.format("<gpTR4 %s>%s</gpTR2>", widthType, tdStr);
        } else {
            return "<tr>" + tdStr + "</tr>";
        }
    }

    private String lineTd(String content) {
        return "<td>" + content + "</td>";
    }
}
