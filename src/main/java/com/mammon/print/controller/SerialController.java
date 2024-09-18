package com.mammon.print.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.mammon.common.ResultJson;
import com.mammon.print.domain.vo.SerialPortVo;
import com.mammon.print.service.SerialPortService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dcl
 * @since 2024/6/10 14:10
 */
@RestController
@RequestMapping("/serial")
public class SerialController {

    private final SerialPortService serialPortService;

    public SerialController(SerialPortService serialPortService) {
        this.serialPortService = serialPortService;
    }

    @GetMapping("/list")
    public ResultJson<List<SerialPortVo>> getSerialPortList() {
        List<SerialPortVo> list = serialPortService.getSerialPortList();
        return ResultJson.ok(list);
    }

    @PutMapping("/open/{portName}")
    public ResultJson<Boolean> openSerialPort(@PathVariable String portName) {
        serialPortService.connectSerialPort(portName);
        return ResultJson.ok();
    }

    @PutMapping("/send/{portName}")
    public ResultJson<Void> sendData(@PathVariable String portName,
                                     @RequestParam String data) {
        //发送数据注意，提前与接收设备沟通好协议，发送什么样类型的数据设备才可以进行响应，否则设备无响应
        serialPortService.sendData(portName, data);
        return ResultJson.ok();
    }

    @PutMapping("/close/{portName}")
    public ResultJson<Void> closeSerialPort(@PathVariable String portName) {
        serialPortService.closeSerialPort(portName);
        return ResultJson.ok();
    }

    @GetMapping("/weight/{portName}")
    public ResultJson<BigDecimal> getWeight(@PathVariable String portName) {
        return ResultJson.ok(serialPortService.getWeight(portName));
    }
}
