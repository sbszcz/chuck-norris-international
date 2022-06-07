package dev.sbszcz.cni.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class RestTemplateConfiguration {

    @Value("${debug.service.requests:false}")
    private boolean debugServiceRequests;

    @Bean("chuckNorrisRestTemplate")
    public RestTemplate chuckNorrisRestTemplate(
            @Value("${chucknorris.protocol}") String protocol,
            @Value("${chucknorris.host}") String hostName,
            @Value("${chucknorris.port:0}") int port
    ) {
        return restTemplateForEndpoint(protocol, hostName, port);
    }

    @Bean("libreTranslateRestTemplate")
    public RestTemplate libreTranslateRestTemplate(
            @Value("${libretranslate.protocol}") String protocol,
            @Value("${libretranslate.host}") String hostName,
            @Value("${libretranslate.port:0}") int port
    ) {
        return restTemplateForEndpoint(protocol, hostName, port);
    }

    private RestTemplate restTemplateForEndpoint(String protocol, String hostName, int port) {

        final RestTemplateBuilder templateBuilder = new RestTemplateBuilder()
                .rootUri(baseUri(protocol, hostName, port))
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3));

        if (debugServiceRequests) {
            templateBuilder
                    .requestFactory(() -> new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()))
                    .interceptors(new LogRequestResponseDetailsInterceptor());
        }

        return templateBuilder.build();
    }

    private String baseUri(String protocol, String hostName, int port) {
        final var sb = new StringBuilder(protocol).append("://").append(hostName);
        if (port > 0) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

}
