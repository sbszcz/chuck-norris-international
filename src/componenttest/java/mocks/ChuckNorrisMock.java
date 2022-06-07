package mocks;

import org.intellij.lang.annotations.Language;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class ChuckNorrisMock extends BaseWireMock {

    public ChuckNorrisMock(String host, int port) {
        super(host, port);
    }

    public void mockRandomJokes() {

        @Language("json")
        final var json = """
                {
                    "categories": [],
                    "created_at": "2020-01-05 13:42:19.324003",
                    "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                    "id": "7tuczvb2rpy-xn7alkmz0w",
                    "updated_at": "2020-01-05 13:42:19.324003",
                    "url": "https://api.chucknorris.io/jokes/7tuczvb2rpy-xn7alkmz0w",
                    "value": "Chuck Norris's OSI network model has only one layer - Physical."
                }
                """;

        wireMock.register(get(urlPathEqualTo("/jokes/random"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json;charset=UTF-8")
                                .withBody(json)
                ));

    }

    public void verifyRandomEndpointHasBeenCalledCorrectly(){
        wireMock.verifyThat(1,
                getRequestedFor(
                        urlPathEqualTo("/jokes/random")));
    }
}
