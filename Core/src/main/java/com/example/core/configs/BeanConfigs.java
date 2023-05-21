package com.example.core.configs;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class BeanConfigs {

    @Bean
    MinioClient minioClient(Environment env){
        String url = Objects.requireNonNull(env.getProperty("minio.host.url"));
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("minio.host.port")));
        return MinioClient.builder()
                .endpoint(url,port,false)
                .credentials(Objects.requireNonNull(env.getProperty("minio.host.access-key")), Objects.requireNonNull(env.getProperty("minio.host.secret-key")))
                .build();
    }
    @Bean
    OkHttpClient client(){
        return new OkHttpClient().newBuilder().build();
    }
}
