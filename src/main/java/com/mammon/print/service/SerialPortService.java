package com.mammon.print.service;

import cn.hutool.core.util.HexUtil;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.mammon.exception.CustomException;
import com.mammon.print.config.SerialPortHandler;
import com.mammon.print.domain.vo.SerialPortVo;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/6/10 14:00
 */
@Service
@Slf4j
public class SerialPortService {

    /**
     * 获取串口和状态
     *
     * @return
     */
    public List<SerialPortVo> getSerialPortList() {
        List<SerialPort> list = Arrays.asList(SerialPort.getCommPorts());
        log.info("serialPortList: {}", JsonUtil.toJSONString(list));
        return list.stream().map(x -> {
            SerialPortVo vo = new SerialPortVo();
            vo.setFullName(x.getDescriptivePortName());
            vo.setPortName(x.getSystemPortName());
            vo.setPortPath(x.getSystemPortPath());
            return vo;
        }).collect(Collectors.toList());
    }

    //关闭串口
    public void closeSerialPort(String portName) {
        SerialPort serialPort = SerialPort.getCommPort(portName);
        if (serialPort != null) {
            serialPort.closePort();
        }
    }

    //发送数据到串口
    public void sendData(String portName, String data) {
        byte[] buffer2 = HexUtil.decodeHex(data);
        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.writeBytes(buffer2, buffer2.length);
    }

    // 添加串口链接
    public void connectSerialPort(String portName) {
        SerialPort serialPort = SerialPort.getCommPort(portName);
        if (serialPort == null) {
            throw new CustomException("串口错误");
        }
        if (serialPort.isOpen()) {
            return;
        }
        int baudRate = serialPort.getBaudRate(); // 波特率
        int parity = serialPort.getParity(); // 校验位
        int dataBits = serialPort.getNumDataBits(); // 数据位
        int stopBits = serialPort.getNumStopBits(); // 停止位
        serialPort.openPort();
        serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity); // 设置参数
        // SerialPort.TIMEOUT_NONBLOCKING 非阻塞
        // SerialPort.TIMEOUT_READ_BLOCKING and SerialPort.TIMEOUT_READ_SEMI_BLOCKING 阻塞和半阻塞
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 20000, 0); // 设置超时
        serialPort.addDataListener(new SerialPortHandler());
    }

    public BigDecimal getWeight(String portName) {
        SerialPort serialPort = SerialPort.getCommPort(portName);
        boolean open = serialPort.openPort();
        serialPort.setComPortParameters(serialPort.getBaudRate(), 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
//        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); // 设置超时
        log.info("isOpen:{}", open);
        try {
            long stock = serialEvent(serialPort);
            return StockUtil.parseBigDecimal(stock);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BigDecimal.ZERO;
        } finally {
            serialPort.closePort();
        }
    }

    private long serialEvent(SerialPort serialPort) throws Exception {
        // 读取串口流
        int num = 0;
        while (num <= 10) {
            int available = serialPort.bytesAvailable();
            log.info("available: {}", available);
            if (available > 0) {
                byte[] buffer = new byte[available];
                int numRead = serialPort.readBytes(buffer, buffer.length);
                String weight = new String(buffer, 0, numRead).trim();
                long stock = convertStock(weight);
                log.info("收到串口数据: {},{}", weight, stock);
                if (stock > 0) {
                    return stock;
                }
            }
            Thread.sleep(100);
            num++;
        }
        return 0;
    }

    private long convertStock(String weight) {
        if (StringUtils.isBlank(weight)) {
            return 0;
        }
        int endIndex = weight.trim().indexOf(" ");
        weight = weight.substring(0, endIndex + 1).trim();
        try {
            return Long.parseLong(weight);
        } catch (NumberFormatException e) {
            log.error("string convert long error:{}", weight);
            return 0;
        }
    }
}
