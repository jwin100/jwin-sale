package com.mammon.biz.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class FileUtil {

    public static File urlToFile(URL url, String prefix, String suffix) throws IOException {
        InputStream inputStream = null;
        File file = null;
        FileOutputStream outputStream = null;
        try {
            file = File.createTempFile(prefix, suffix);
            URLConnection urlConn = null;
            urlConn = url.openConnection();
            inputStream = urlConn.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return file;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
