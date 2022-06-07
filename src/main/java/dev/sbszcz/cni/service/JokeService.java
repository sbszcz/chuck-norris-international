package dev.sbszcz.cni.service;

import dev.sbszcz.cni.gateway.ChuckNorrisClient;
import org.springframework.stereotype.Component;

@Component
public class JokeService {

    private final ChuckNorrisClient chuckNorrisClient;

    public JokeService(ChuckNorrisClient chuckNorrisClient) {
        this.chuckNorrisClient = chuckNorrisClient;
    }

    public String randomJoke() {
        return chuckNorrisClient.getRandom();
    }
}
