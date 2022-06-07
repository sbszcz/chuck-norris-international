package dev.sbszcz.cni.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate;

    public ChuckNorrisClient(@Qualifier("chuckNorrisRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRandom() {

        final ResponseEntity<ChuckNorrisResponseVO> response;

        try {
            response = restTemplate.getForEntity("/jokes/random", ChuckNorrisResponseVO.class);
        } catch (HttpStatusCodeException e) {
            // todo: logging
            throw new RuntimeException(e);
        }

        final ChuckNorrisResponseVO joke = response.getBody();
        if (joke == null) {
            throw new IllegalStateException("chuck norris response did not contain json body");
        }

        return joke.value;
    }


}
