package com.mammon.print.config;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/6/10 13:43
 */
@Slf4j
public class SerialPortHandler implements SerialPortDataListener {
    //监听串口设备数据,推送到socket
//    private final WebSocketService webSocketService;
//
//    public SerialPortHandler(WebSocketService webSocketService) {
//        this.webSocketService = webSocketService;
//    }

    @Override
    public int getListeningEvents() {
        // 持续返回数据流模式
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        // 收到数据立即返回
//         return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
//         return SerialPort.LISTENING_EVENT_DATA_WRITTEN;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        // 读取串口流
        InputStream inputStream = null;
        try {
            inputStream = event.getSerialPort().getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String weight = "";
            if (bytesRead > 0) {
                weight = new String(buffer, 0, bytesRead).trim();
            }
            if (StringUtils.isBlank(weight)) {
                return;
            }
            int endIndex = weight.trim().indexOf(" ");
            weight = weight.substring(0, endIndex + 1).trim();
            BigDecimal socket = StockUtil.parseBigDecimal(Long.parseLong(weight));
            log.info("收到串口数据: {},{}", weight, socket);
            // 数据推送到socket
//        webSocketService.sendMessage(portName, hexString);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
