package dev.sbszcz.cni.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Component
public class ChuckNorrisClient {

    record ChuckNorrisResponseVO(
            Set<String> categories,
            String createdAt,
            String iconUrl,
            String id,
            String value
    ){}

    @Value("${chucknorris.protocol}") String protocol;
    @Value("${chucknorris.host}") String hostName;
    @Value("${chucknorris.port:0}") int port;

    private String baseUri(String protocol, String hostName, int port) {
        final var sb = new StringBuilder(protocol).append("://").append(hostName);
        if (port > 0) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

    public String getRandom() {

        final ChuckNorrisResponseVO response;

        RestClient restClient = RestClient.create();

        try {
            response = restClient.get()
                    .uri(baseUri(protocol, hostName, port) + "/jokes/random")
                    .retrieve()
                    .body(ChuckNorrisResponseVO.class);

        } catch (HttpStatusCodeException e) {
            // todo: logging
            throw new RuntimeException(e);
        }

        if (response == null) {
            throw new IllegalStateException("chuck norris response did not contain json body");
        }

        return response.value;
    }


}
