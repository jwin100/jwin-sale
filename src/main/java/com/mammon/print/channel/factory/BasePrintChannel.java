package com.mammon.print.channel.factory;

import com.mammon.print.channel.factory.model.PrintInfoVo;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import com.mammon.print.domain.enums.PrintTerminalStatus;
import com.mammon.print.domain.model.PrintActiveModel;
import com.mammon.print.domain.model.PrintTerminalStatusModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BasePrintChannel {

    /**
     * 根据厂商获取各自定义的唯一码
     *
     * @param formConfigStr
     * @return
     */
    String getTerminalCode(String formConfigStr);

    void addPrint(String terminalName, String configStr, String formConfigStr);

    void editPrint(String terminalName, String configStr, String formConfigStr);

    /**
     * 删除
     *
     * @param configStr
     * @param formConfigStr
     */
    void deletePrint(String configStr, String formConfigStr);

    /**
     * 设备关机、重启
     *
     * @param configStr
     * @param formConfigStr
     * @param restartType
     */
    void restartPrint(String configStr, String formConfigStr, int restartType);

    /**
     * 获取设备状态
     *
     * @param configStr
     * @param formConfigStr
     * @return
     */
    PrintTerminalStatus getPrintStatus(String configStr, String formConfigStr);

    /**
     * 获取设备信息
     *
     * @param configStr
     * @param formConfigStr
     * @return
     */
    PrintInfoVo getPrintInfo(String configStr, String formConfigStr);

    /**
     * 打印
     *
     * @param configStr
     * @param formConfigStr
     * @param orderNo
     * @param printActiveModels
     */
    void printIndex(String configStr, PrintTerminalEntity terminal, String orderNo,
                    List<PrintActiveModel> printActiveModels);

    /**
     * 打印机状态回调
     *
     * @param configStr
     * @param request
     * @return
     */
    PrintTerminalStatusModel printStatusNotify(String configStr, HttpServletRequest request);

    Object responseSuccess();

    Object responseFail();
}
