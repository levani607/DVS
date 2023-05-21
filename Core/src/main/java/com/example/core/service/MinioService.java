package com.example.core.service;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.util.ImageUtils;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PostPolicy;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
public class MinioService {


    private final MinioClient minioClient;
    private final OkHttpClient client;
    @Value("${myapp.image.scale}")
    private Float scale;
    @Value("${minio.storage.bucket}")
    private String bucketName;
    @Value("${minio.host.url}")
    private String minioUrl;
    @Value("${minio.host.port}")
    private String minioPort;

    @SneakyThrows
    public String uploadAndGetPath(String fileName) {
        ContentType contentType = getContentType(fileName);
        if (!List.of(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_SVG.getMimeType()).contains(contentType.getMimeType())) {
            throw new CoreException(ErrorCode.ILLEGAL_ARGUMENT_PROVIDED, "Content Type not allowed : " + contentType.getMimeType());
        }
        String name = hashInput(fileName);
        if (bucketName == null) {
            throw new CoreException(ErrorCode.ILLEGAL_ARGUMENT_PROVIDED);
        }
        byte[] decode = extractByteArr(fileName);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(name)
                .stream(inputStream, decode.length, 20 * 1024 * 1024).build());
        return String.format("%s/%s", bucketName, name);
    }



    private ContentType getContentType(String fileName) {
        String mimeType = fileName.substring(fileName.indexOf(':') + 1, fileName.indexOf(';'));
        return ContentType.create(mimeType);
    }


    private byte[] extractByteArr(String fileName) {

        String imageString = fileName.substring(fileName.indexOf(',')+1);
        String fileFormat = fileName.substring(fileName.indexOf('/')+1, fileName.indexOf(';'));
        return ImageUtils.downscaleImage(imageString, scale, fileFormat);
    }

    private String hashInput(String fileName) throws NoSuchAlgorithmException {
        String extension = fileName.substring(fileName.indexOf("/") + 1, fileName.indexOf(";"));
        SecureRandom secureRandom = new SecureRandom();
        int i = secureRandom.nextInt(99999, 999999);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(String.valueOf(i).getBytes());
        BigInteger no = new BigInteger(1, digest);

        StringBuilder hashtext = new StringBuilder(no.toString(16));

        while (hashtext.length() < 32) {
            hashtext.insert(0, "0");
        }
        return hashtext + "." + extension;
    }
}
