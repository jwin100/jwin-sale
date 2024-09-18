package com.mammon.biz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mammon.common.Generate;
import com.mammon.biz.domain.UploadAuthVo;
import com.mammon.biz.enums.FileTypeConst;
import com.mammon.biz.utils.AuthUtil;
import com.mammon.biz.utils.FileUtil;
import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.mammon.biz.enums.FileNameSuffixConst.fileNameSuffixList;

@Service
@Slf4j
public class FileService {
    private ObjectMapper mapper = new ObjectMapper();

    private static final String BUCKET = "jwin100";
    private static final String OPERATOR = "pictureadmin";
    private static final String PASSWORD = "lKOkznwbUZ2wMipKn4jUgh9LDVWEAfNY";
    private static final String MD5_PASSWORD = DigestUtils.md5Hex("lKOkznwbUZ2wMipKn4jUgh9LDVWEAfNY");
    private static final String FILE_FORMAT = "/merchant/%s/picture_{random32}{.suffix}";

    private static final String REDIS_KEY = "UPYUN_IMAGE_";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public UploadAuthVo uploadFormAuth(long merchantNo, int type) {
        String key = REDIS_KEY + merchantNo + type;
        String auth = stringRedisTemplate.opsForValue().get(key);
        UploadAuthVo vo = new UploadAuthVo();
        try {
            if (auth == null) {
                vo = generateAuth(merchantNo, type);
                stringRedisTemplate.opsForValue().set(key, mapper.writeValueAsString(vo));
                return vo;
            }
            vo = mapper.readValue(auth, UploadAuthVo.class);
            long nowExpire = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000;
            if (vo == null || nowExpire > vo.getExpire()) {
                vo = generateAuth(merchantNo, type);
                stringRedisTemplate.opsForValue().set(key, mapper.writeValueAsString(vo));
                return vo;
            }
            return vo;
        } catch (Exception e) {
            log.error("uploadAuth:" + e.getMessage(), e);
            return vo;
        }
    }

    /**
     * rest方式文件上传
     *
     * @param path
     * @return upload to path
     */
    public String uploadFile(long merchantNo, String path) {
        log.info("path:{}", path);
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String suffixFileName = path.substring(path.lastIndexOf("."));
        if (!fileNameSuffixList.contains(suffixFileName)) {
            suffixFileName = ".png";
        }
        String fileName = Generate.generateUUID() + suffixFileName;
        // 判断文件名
        // 判断文件大小
        try {
            URL url = new URL(path);
            File file = FileUtil.urlToFile(url, "imgtmp", suffixFileName);
            RestManager restManager = new RestManager(BUCKET, OPERATOR, PASSWORD);
            String filePath = FileTypeConst.getFileName(merchantNo, 1) + fileName;
            Map<String, String> params = new HashMap<>();
            params.put(RestManager.PARAMS.CONTENT_MD5.getValue(), UpYunUtils.md5(file, 1024));
            Response response = restManager.writeFile(filePath, file, params);
            return response.isSuccessful() ? filePath : null;
        } catch (IOException | UpException e) {
            log.error("文件上传失败:{}", e.getMessage(), e);
            return null;
        }
    }

    private UploadAuthVo generateAuth(long merchantNo, int type) {
        UploadAuthVo vo = new UploadAuthVo();
        String method = "POST";
        try {
            String fileName = FileTypeConst.getFileName(merchantNo, type);
            String saveKey = String.format(FILE_FORMAT, fileName);
            long expire = LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000;
            String date = AuthUtil.getRfc1123Time();
            String s = "{\"bucket\":\"" + BUCKET + "\",\"save-key\":\"" + saveKey + "\",\"expiration\":\"" + expire + "\",\"date\":\"" + date + "\"}";
            String policy = Base64.getEncoder().encodeToString(s.getBytes());
            String auth = AuthUtil.sign(OPERATOR, MD5_PASSWORD, method, "/" + BUCKET, date, policy, "");
            vo.setPolicy(policy);
            vo.setAuthorization(auth);
            vo.setExpire(expire);
            return vo;
        } catch (Exception e) {
            log.error("generateAuth:" + e.getMessage(), e);
            return null;
        }
    }
}
