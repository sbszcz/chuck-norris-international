package dev.sbszcz.cni.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {


    @Bean("libretranslateRestClient")
    public RestClient libretranslateRestClient(
            @Value("${libretranslate.protocol}") String protocol,
            @Value("${libretranslate.host}") String hostName,
            @Value("${libretranslate.port:0}") int port
    ) {
        return RestClient.builder()
                .baseUrl(baseUri(protocol, hostName, port))
                // FIXME: this restClient won't serialize POST request body without a requestInterceptor (a test will fail)
                .requestInterceptor((request, body, execution) -> execution.execute(request, body))
                .build();

    }


    @Bean("chucknorrisRestClient")
    public RestClient chucknorrisRestClient(
            @Value("${chucknorris.protocol}") String protocol,
            @Value("${chucknorris.host}") String hostName,
            @Value("${chucknorris.port:0}") int port
    ) {

        return RestClient.builder()
                .baseUrl(baseUri(protocol, hostName, port))
                .build();

    }

    private String baseUri(String protocol, String hostName, int port) {
        final var sb = new StringBuilder(protocol).append("://").append(hostName);
        if (port > 0) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }
}
