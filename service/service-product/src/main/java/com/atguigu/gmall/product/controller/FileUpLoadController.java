package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Pch
 * @create 2023-02-15 0:17
 * @description:
 */
@RestController
@RequestMapping("/admin/product")
@RefreshScope

public class FileUpLoadController {

    //  获取自己的服务器地址：配置文件中获取 软编码
    @Value("${minio.endpointUrl}")
    //http://192.168.59.129:9000
    private String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;



    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){

        //  声明一个url
        String url = "";


        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            //  获取客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpointUrl)
                            .credentials(accessKey, secreKey)
                            .build();

            // Make 'asiatrip' bucket if not exist.
            //  判断bucket是否存在
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                //  不存在就创建
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("创建..." + bucketName);
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
                System.out.println("已存在..." + bucketName);
            }

            //  获取上传文件名 originalFilename = atguigu.jpg
            String originalFilename = file.getOriginalFilename();
            //  生成唯一的文件名
            String fileName = System.currentTimeMillis() + UUID.randomUUID().toString();
            //  获取后缀名
            String newFileName = fileName + originalFilename.substring(originalFilename.lastIndexOf("."));
            // Upload known sized input stream.
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(newFileName).stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            //  测试使用
//        minioClient.uploadObject(
//                UploadObjectArgs.builder()
//                        .bucket()
//                        .object("asiaphotos-2015.zip")
//                        .filename("/home/user/Photos/asiaphotos.zip")
//                        .build());

            url = endpointUrl + "/" + bucketName + "/" + newFileName;
            System.out.println("url:\t " + url);

        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }

        //  返回结果
        return Result.ok(url);
    }

    public static void main(String[] args) {
        String str = "atguigu.jpg";
//        System.out.println(str.substring(7));

    }
}
