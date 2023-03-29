package com.hello.service.impl;

import com.hello.domain.ResponseResult;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.service.UploadService;
import com.hello.utils.PathUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
@Service
@Data
@ConfigurationProperties(prefix = "tencent.cos")
public class COSUploadServiceImpl implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String url=uploadCOS(img);
        if(url!=null){
            return ResponseResult.okResult(url);
        }else {
            new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return null;
    }
    private String secretId;
    private String secretKey;
    private String buckerName;
    private String region;
    private String url;
    private String uploadCOS(MultipartFile img){
        COSClient cosClient = initCos();
        try {
            String fileName = img.getOriginalFilename();
            if(!fileName.endsWith(".png")){
                throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
            }
            InputStream inputStream = img.getInputStream();
            String filePath = PathUtils.generateFilePath(fileName);
            // 上传文件
            cosClient.putObject(new PutObjectRequest(buckerName, filePath, inputStream, null));
            cosClient.setBucketAcl(buckerName, CannedAccessControlList.PublicRead);

            return url + "/" + filePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * 初始化COSClient
     * @return
     */
    private COSClient initCos() {
        // 1 初始化用户身份信息（secretId, secretKey）
        BasicCOSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照
         Region region = new Region(this.region);
         ClientConfig clientConfig = new ClientConfig(region);
        // 从 5.6.54 版本开始，默认使用了 https
//        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(credentials, clientConfig);
    }
}
