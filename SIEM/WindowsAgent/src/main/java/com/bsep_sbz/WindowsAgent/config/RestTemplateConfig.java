package com.bsep_sbz.WindowsAgent.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;

@Configuration
public class RestTemplateConfig {

    @Value("${server.ssl.key-store}")
    private Resource keyStore;

    @Value("${server.ssl.key-store-password}")
    private char[] keyStorePassword;

    @Value("${server.ssl.trust-store}")
    private Resource trustStore;

    @Value("${server.ssl.trust-store-password}")
    private char[] trustStorePassword;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore.getFile(), keyStorePassword, keyStorePassword)
                .loadTrustMaterial(trustStore.getFile(), trustStorePassword)
                .build();

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();

        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }

}