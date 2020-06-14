package com.assessment.authentication.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        KeyStore keyStore;
        KeyStore trustStore;
        HttpComponentsClientHttpRequestFactory requestFactory = null;

        try {
            keyStore = KeyStore.getInstance("JCEKS");
            ClassPathResource classPathResource = new ClassPathResource("server-keystore.jks");
            InputStream inputStream = classPathResource.getInputStream();
            keyStore.load(inputStream, "changeit".toCharArray());

            trustStore = KeyStore.getInstance("JCEKS");
            ClassPathResource classPathResourceTruststore = new ClassPathResource("server-truststore.jks");
            InputStream inputStreamTruststore = classPathResourceTruststore.getInputStream();
            trustStore.load(inputStreamTruststore, "changeit".toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
//					.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .loadTrustMaterial(trustStore, null)
                    .loadKeyMaterial(keyStore, "changeit".toCharArray()).build(),
                    NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5))
                    .setMaxConnPerRoute(Integer.valueOf(5))
                    .build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(10000));
            requestFactory.setConnectTimeout(Integer.valueOf(10000));

            restTemplate.setRequestFactory(requestFactory);
        } catch (Exception exception) {
            System.out.println("Exception Occured while creating restTemplate "+exception);
            exception.printStackTrace();
        }
        return restTemplate;
    }

}
