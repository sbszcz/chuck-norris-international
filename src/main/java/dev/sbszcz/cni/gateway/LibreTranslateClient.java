package dev.sbszcz.cni.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Component
public class LibreTranslateClient {

    private final RestClient restClient;

    public LibreTranslateClient(@Qualifier("libretranslateRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

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

    public String translateTextTo(String text, String targetLanguage) {

        TranslatedResponseVO response;
        TranslationRequestVO body = new TranslationRequestVO(text, targetLanguage);


        try {

            response = restClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(TranslatedResponseVO.class);
        } catch (HttpStatusCodeException e) {
            throw e;
        }

        if (response == null) {
            throw new IllegalStateException("translation response did not contain json body");
        }

        return response.text;
    }
}
