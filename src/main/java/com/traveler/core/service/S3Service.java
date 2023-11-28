package com.traveler.core.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("{cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;

    }

    public String upload(MultipartFile multipartFile, String fileName) throws IOException {
        File file = convert(multipartFile);
        String imgUrl = putS3(file, fileName);
        return imgUrl;
    }
    public File convert(MultipartFile multipartFile) throws  IllegalStateException,IOException{
           File file = new File(multipartFile.getOriginalFilename());
           try(FileOutputStream fos = new FileOutputStream(file)){
               fos.write(multipartFile.getBytes());
           } catch (IOException e){
               System.out.println("파일 변환 중 오류 발생");
               throw e;
           }
           return file;
    }
    public String putS3(File file, String fileName){
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead); //엑세스 권한 설정을 담기 위해
        amazonS3.putObject(request);
        file.delete();
        return amazonS3.getUrl(bucket,fileName).toString();
    }




}
