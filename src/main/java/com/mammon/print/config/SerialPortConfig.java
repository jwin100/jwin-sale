package com.mammon.print.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dcl
 * @since 2024/6/14 10:16
 */
@Configuration
public class SerialPortConfig {

    /**
     * if you plan to use the library within an Apache Tomcat application,
     * <p>
     * please set the jSerialComm.library.randomizeNativeName property to true before accessing the SerialPort class to address an issue whereby the Tomcat bootloader tries to reinitialize the library multiple times.
     * <p>
     * This can either be done using -DjSerialComm.library.randomizeNativeName="true" as a command line parameter
     */
    @Bean
    public void config() {
        System.setProperty("jSerialComm.library.randomizeNativeName", "true");
    }
}
