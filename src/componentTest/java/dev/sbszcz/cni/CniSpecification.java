package dev.sbszcz.cni;

import mocks.ChuckNorrisMock;
import mocks.LibreTranslateMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;

@ActiveProfiles("componentTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CniSpecification {

    @Value("${local.server.port}")
    protected int localServerPort = 0;

    protected RestTemplate cniClient;

    @PostConstruct
    void setupCniClient(){
        cniClient = new RestTemplateBuilder()
                .rootUri("http://localhost:" + localServerPort)
                .setConnectTimeout(Duration.ofSeconds(1))
                .setReadTimeout(Duration.ofSeconds(1))
                .build();
    }

    protected static ChuckNorrisMock chuckNorrisService;

    protected static LibreTranslateMock libreTranslateService;

    @BeforeAll
    static void beforeAll() {

        chuckNorrisService = new ChuckNorrisMock("localhost", 8090);
        chuckNorrisService.startIfNotRunning();
        chuckNorrisService.reset();

        libreTranslateService = new LibreTranslateMock("localhost", 8091);
        libreTranslateService.startIfNotRunning();
        libreTranslateService.reset();
    }

    @AfterEach
    void afterEach(){
        libreTranslateService.reset();
        chuckNorrisService.reset();
    }

    @AfterAll
    static void afterAll() {
        libreTranslateService.stop();
        chuckNorrisService.stop();
    }

}
