package com.mwx.utils;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * MinIO文件上传
 * @author Hojin
 * @date 2022/11/1 18:16
 */
@Component
@EnableConfigurationProperties(MinioProperties.class)
public class MinioUploader {
    //这句话先被执行 加载顺序要在先 如果下面一行执行 要报空指针
    @Resource
    private MinioProperties minioProperties;
    @Autowired
    private MinioClient minioClient;

    @Bean
    public MinioClient getMinioClient() throws Exception {
        MinioClient minioClient =
                new MinioClient(minioProperties.getEndPoint(), minioProperties.getAccessKey(), minioProperties.getSecretKey());

        boolean flag = minioClient.bucketExists(minioProperties.getBucketName());
        if (flag) {
            System.out.println("bucket is exist");
        } else {
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        return minioClient;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception {
        //存储文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();
        //获取输入流
        InputStream inputStream = file.getInputStream();
        //文件大小 文件上传限制 文件上传时参数
        PutObjectOptions putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
        //上传文件
        minioClient.putObject(minioProperties.getBucketName(), fileName, inputStream, putObjectOptions);
        //http://192.168.235.128:9000/shop/new.jpg
        String retUrl = minioProperties.getEndPoint() + "/" + minioProperties.getBucketName() + "/" + fileName;
        return retUrl;
    }
}
