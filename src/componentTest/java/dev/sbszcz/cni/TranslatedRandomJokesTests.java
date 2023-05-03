package dev.sbszcz.cni;

import org.intellij.lang.annotations.Language;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class TranslatedRandomJokesTests extends CniSpecification{

    @Test
    void should_successfully_get_random_joke() throws JSONException {

        @Language("json")
        var expectedJson = """
        {"value":"Chuck Norris's OSI network model has only one layer - Physical."}
        """;

        chuckNorrisService.mockRandomJokes();

        final ResponseEntity<String> response = cniClient.getForEntity("/aboutchuck", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(expectedJson, response.getBody(), JSONCompareMode.LENIENT);

        chuckNorrisService.verifyRandomEndpointHasBeenCalledCorrectly();
        libreTranslateService.verifyNoCallHasBeenMade();
    }

    @Test
    void should_successfully_get_random_joke_and_translate_to_language() throws JSONException {

        @Language("json")
        var expectedJson = """
        {"value":"Niemand hat jemals während der Überprüfung von Chuck Norris Code gesprochen und lebte darüber zu erzählen"}
        """;

        chuckNorrisService.mockRandomJokes();
        libreTranslateService.mockTranslation();

        final ResponseEntity<String> response = cniClient.getForEntity("/aboutchuck?lang=de", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(expectedJson, response.getBody(), JSONCompareMode.LENIENT);

        chuckNorrisService.verifyRandomEndpointHasBeenCalledCorrectly();
        libreTranslateService.verifyTranslationHasBeenCalledWithLanguage("de");
    }

    @Test
    void should_respond_with_bad_request_when_language_is_not_supported() throws JSONException {

        @Language("json")
        var expectedJson = """
        {"reason":"Given language 'xx' is not available for translation"}
        """;

        chuckNorrisService.mockRandomJokes();
        libreTranslateService.mockTranslation();

        Throwable ex = catchThrowable(() -> {
            cniClient.getForEntity("/aboutchuck?lang=xx", String.class);
        });

        assertThat(ex).isInstanceOf(HttpStatusCodeException.class);
        HttpStatusCodeException httpEx = (HttpStatusCodeException) ex;

        assertThat(httpEx.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertEquals(expectedJson, httpEx.getResponseBodyAsString(), JSONCompareMode.LENIENT);

        chuckNorrisService.verifyNoCallHasBeenMade();
        libreTranslateService.verifyNoCallHasBeenMade();
    }
}
