package dev.sbszcz.cni.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Component
public class LibreTranslateClient {

    record TranslatedResponseVO(
            @JsonProperty("translatedText") String text
    ) {
    }

    record TranslationRequestVO(
            @JsonProperty("q") String text,
            @JsonProperty("source") String source,
            @JsonProperty("target") String target,
            @JsonProperty("format") String format
    ) {
        public TranslationRequestVO(String text, String target) {
            this(text, "en", target, "text");
        }
    }


    @Value("${libretranslate.protocol}") String protocol;
    @Value("${libretranslate.host}") String hostName;
    @Value("${libretranslate.port:0}") int port;


    private String baseUri(String protocol, String hostName, int port) {
        final var sb = new StringBuilder(protocol).append("://").append(hostName);
        if (port > 0) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

    public String translateTextTo(String text, String targetLanguage) {

        TranslatedResponseVO response;

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        TranslationRequestVO body = new TranslationRequestVO(text, targetLanguage);
//        var entity = new HttpEntity<>(body, headers);

//        MappingJackson2HttpMessageConverter jacksonConverter = restTemplate.getMessageConverters().stream()
//                .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
//                .map(c -> (MappingJackson2HttpMessageConverter) c)
//                .findFirst()
//                .orElseThrow();

//        boolean canWrite = jacksonConverter.canWrite(
//                TranslationRequestVO.class, MediaType.APPLICATION_JSON
//        );

//        System.out.println("Can Jackson write TranslationRequestVO? " + canWrite);

        RestClient restClient = RestClient.create();

        try {

            response = restClient.post()
                    .uri(baseUri(protocol, hostName, port) + "/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(TranslatedResponseVO.class);
//            response = restTemplate.postForEntity("/translate", entity, TranslatedResponseVO.class);
        } catch (HttpStatusCodeException e) {
            throw e;
        }

//        final TranslatedResponseVO translation = response.getBody();
        if (response == null) {
            throw new IllegalStateException("translation response did not contain json body");
        }

        return response.text;
    }
}
