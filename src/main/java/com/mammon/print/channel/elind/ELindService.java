package com.mammon.print.channel.elind;


import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.print.channel.elind.model.*;
import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.model.PrintInfoVo;
import com.mammon.print.common.ConvertServletParam;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import com.mammon.print.domain.enums.*;
import com.mammon.print.domain.model.PrintActiveModel;
import com.mammon.print.domain.model.PrintTerminalStatusModel;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ELindService implements BasePrintChannel {

    @Resource
    private ElindClient elindClient;

    @Override
    public String getTerminalCode(String formConfigStr) {
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        return formConfig.getTerminalCode();
    }

    @Override
    public void addPrint(String terminalName, String configStr, String formConfigStr) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindAddPrintDto dto = new ElindAddPrintDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        dto.setMsign(formConfig.getTerminalKey());
        ElindResultJson<Void> result = elindClient.addPrint(dto);
        log.info("添加设备返回信息：{}", JsonUtil.toJSONString(result));
        if (!"0".equals(result.getError())) {
            throw new CustomException(result.getErrorDescription());
        }
    }

    @Override
    public void editPrint(String terminalName, String configStr, String formConfigStr) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindAddPrintDto dto = new ElindAddPrintDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        dto.setMsign(formConfig.getTerminalKey());
        ElindResultJson<Void> result = elindClient.addPrint(dto);
        log.info("修改设备返回信息：{}", JsonUtil.toJSONString(result));
        if (!"0".equals(result.getError())) {
            throw new CustomException(result.getErrorDescription());
        }
    }

    @Override
    public void deletePrint(String configStr, String formConfigStr) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindDeletePrintDto dto = new ElindDeletePrintDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        ElindResultJson<Void> result = elindClient.deletePrint(dto);
        log.info("print-delete,request:{},result:{}", JsonUtil.toJSONString(dto), result);
        if (!"0".equals(result.getError())) {
            throw new CustomException(result.getErrorDescription());
        }
    }

    public void restartPrint(String configStr, String formConfigStr, int restartType) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }

        String responseType = "shutdown";
        if (restartType == 1) {
            responseType = "restart";
        }

        ElindResetPrintDto dto = new ElindResetPrintDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        dto.setResponseType(responseType);
        ElindResultJson<Void> result = elindClient.resetPrint(dto);
        if (!"0".equals(result.getError())) {
            throw new CustomException(result.getErrorDescription());
        }
    }

    @Override
    public PrintTerminalStatus getPrintStatus(String configStr, String formConfigStr) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindStatusDto dto = new ElindStatusDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        ElindResultJson<ElindStatusVo> result = elindClient.getPrintStatus(dto);
        if (!"0".equals(result.getError()) || result.getBody() == null) {
            return PrintTerminalStatus.OFFLINE;
        }
        String status = result.getBody().getState();
        switch (status) {
            case "1":
                return PrintTerminalStatus.ONLINE;
            case "2":
                return PrintTerminalStatus.PAPER;
            case "0":
            default:
                return PrintTerminalStatus.OFFLINE;
        }
    }

    @Override
    public PrintInfoVo getPrintInfo(String configStr, String formConfigStr) {
        PrintInfoVo vo = new PrintInfoVo();
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(formConfigStr, ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindPrintInfoDto dto = new ElindPrintInfoDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        ElindResultJson<ElindPrintInfoVo> result = elindClient.printInfo(dto);
        log.info("print-info:{}", JsonUtil.toJSONString(result));
        if (!"0".equals(result.getError()) || result.getBody() == null) {
            return vo;
        }
        ElindPrintInfoVo printInfo = result.getBody();
        if (StringUtils.isBlank(printInfo.getWidth())) {
            return vo;
        }
        vo.setVersion(printInfo.getVersion());
        switch (printInfo.getWidth()) {
            case "58mm":
                vo.setWidth(58);
            case "80mm":
            default:
                vo.setWidth(80);
        }
        return vo;
    }

    @Override
    public void printIndex(String configStr, PrintTerminalEntity terminal, String orderNo,
                           List<PrintActiveModel> printActiveModels) {
        ElindConfigModel model = JsonUtil.toObject(configStr, ElindConfigModel.class);
        if (model == null) {
            throw new CustomException("渠道配置信息错误");
        }
        ElindFormConfigModel formConfig = JsonUtil.toObject(terminal.getFormConfig(), ElindFormConfigModel.class);
        if (formConfig == null) {
            throw new CustomException("设备信息错误");
        }
        ElindPrintDto dto = new ElindPrintDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setClientId(model.getClientId());
        dto.setTimestamp(getTimeStamp());
        dto.setSign(sign(model.getClientId(), dto.getTimestamp(), model.getClientSecret()));
        dto.setAccessToken(model.getAccessToken());
        dto.setMachineCode(formConfig.getTerminalCode());
        dto.setContent(printContent(printActiveModels));
        dto.setOriginId(orderNo);
        log.info("elind-request:{}", JsonUtil.toJSONString(dto));
        ElindResultJson<Void> result = elindClient.printIndex(dto);
        if (!"0".equals(result.getError())) {
            log.error("发送打印失败:request:{},resp:{}", dto, result);
            throw new CustomException("打印失败，打印配置异常");
        }
    }

    @Override
    public PrintTerminalStatusModel printStatusNotify(String configStr, HttpServletRequest request) {
        Map<String, String> params = ConvertServletParam.convertParam(request);
        if (params.isEmpty()) {
            return null;
        }
        int status = PrintTerminalStatus.OFFLINE.getCode();
        String online = params.getOrDefault("online", "0");
        switch (online) {
            case "1":
                status = PrintTerminalStatus.ONLINE.getCode();
                break;
            case "2":
                status = PrintTerminalStatus.PAPER.getCode();
                break;
            case "0":
            default:
        }

        PrintTerminalStatusModel model = new PrintTerminalStatusModel();
        model.setTerminalCode(params.get("machine_code"));
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

    private String printContent(List<PrintActiveModel> printActiveModels) {
        log.info("print-content:{}", JsonUtil.toJSONString(printActiveModels));
        StringBuilder sb = new StringBuilder();
        printActiveModels.forEach(x -> {
            if (x.getType() == PrintActiveTypeConst.LINE) {
                sb.append(textBr());
            }
            if (x.getType() == PrintActiveTypeConst.DIVISION) {
                String content = x.getContent();
                content = textAlign(x.getTextAlign(), content);
                content = textBold(x.getTextBold(), content);
                content = textSize(x.getTextSize(), content);
                sb.append(content);
            }
            if (x.getType() == PrintActiveTypeConst.TEXT) {
                String content = x.getContent();
                content = textAlign(x.getTextAlign(), content);
                content = textBold(x.getTextBold(), content);
                content = textSize(x.getTextSize(), content);
                sb.append(content);
            }
            if (x.getType() == PrintActiveTypeConst.TABLE) {
                // 表格
                StringBuilder table = new StringBuilder();
                List<PrintActiveModel> printActiveTables = JsonUtil.toList(x.getContent(), PrintActiveModel.class);
                for (PrintActiveModel model : printActiveTables) {
                    // 每一行
                    StringBuilder tr = new StringBuilder();
                    String[] contents = model.getContent().split(",");
                    for (String s : contents) {
                        String content = s;
                        content = lineTd(content);
                        tr.append(content);
                    }
                    tr = new StringBuilder(lineTr(tr.toString()));
                    table.append(tr);
                }
                sb.append(lineTable(table.toString()));
            }
        });
        return sb.toString();
    }

    private String textBold(int bold, String content) {
        if (bold == PrintActiveBoldConst.BOLD) {
            return "<FB>" + content + "</FB>";
        }
        return content;
    }

    private String textSize(int size, String content) {
        if (size == PrintActiveSizeConst.AGAIN) {
            return "<FS>" + content + "</FS>";
        }
        if (size == PrintActiveSizeConst.TWICE) {
            return "<FS2>" + content + "</FS2>";
        }
        return content;
    }

    private String textAlign(int align, String content) {
        if (align == PrintActiveAlignConst.ALIGN_RIGHT) {
            return "<right>" + content + "</right>";
        }
        if (align == PrintActiveAlignConst.ALIGN_CENTER) {
            return "<center>" + content + "</center>";
        }
        if (align == PrintActiveAlignConst.ALIGN_LEFT_RIGHT) {
            return "<LR>" + content + "</LR>";
        }
        return content;
    }

    private String textBr() {
        return "\r\n";
    }

    private String lineTable(String content) {
        return "<table>" + content + "</table>";
    }

    private String lineTr(String content) {
        return "<tr>" + content + "</tr>";
    }

    private String lineTd(String content) {
        return "<td>" + content + "</td>";
    }


    private int getTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        return (int) now.toEpochSecond(ZoneOffset.UTC);
    }

    private String sign(String clientId, int timestamp, String clientSecret) {
        String str = clientId + timestamp + clientSecret;
        return MD5.create().digestHex(str.getBytes(StandardCharsets.UTF_8));
    }
}
