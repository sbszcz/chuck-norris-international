package dev.sbszcz.cni.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Component
public class ChuckNorrisClient {

    private final RestClient restClient;

    public ChuckNorrisClient(@Qualifier("chucknorrisRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    record ChuckNorrisResponseVO(
            Set<String> categories,
            String createdAt,
            String iconUrl,
            String id,
            String value
    ){}

    public String getRandom() {

        final ChuckNorrisResponseVO response;

        try {
            response = restClient.get()
                    .uri("/jokes/random")
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
