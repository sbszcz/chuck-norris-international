package dev.sbszcz.cni.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
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

    private RestTemplate restTemplate;

    public LibreTranslateClient(@Qualifier("libreTranslateRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String translateTextTo(String text, String targetLanguage) {

        ResponseEntity<TranslatedResponseVO> response;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(new TranslationRequestVO(text, targetLanguage), headers);

        try {
            response = restTemplate.postForEntity("/translate", entity, TranslatedResponseVO.class);
        } catch (HttpStatusCodeException e) {
            throw e;
        }

        final TranslatedResponseVO translation = response.getBody();
        if (translation == null) {
            throw new IllegalStateException("translation response did not contain json body");
        }

        return translation.text;
    }
}
