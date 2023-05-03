package mocks;

import org.intellij.lang.annotations.Language;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.lang.String.format;

public class LibreTranslateMock extends BaseWireMock {

    public LibreTranslateMock(String host, int port) {
        super(host, port);
    }

    public void mockTranslation() {

        @Language("json")
        final var json = """
                {
                   "translatedText": "Niemand hat jemals während der Überprüfung von Chuck Norris Code gesprochen und lebte darüber zu erzählen"
                 }
                """;

        wireMock.register(post(urlPathEqualTo("/translate"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(json)
                ));

    }

    public void verifyTranslationHasBeenCalledWithLanguage(String lang){
        String requestJson = format("""
                {"q":"Chuck Norris's OSI network model has only one layer - Physical.", "source": "en", "target":"%s", "format":"text"}
                """, lang);
        wireMock.verifyThat(
                postRequestedFor(
                        urlPathEqualTo("/translate"))
                            .withRequestBody(equalToJson(requestJson)));
    }

}
