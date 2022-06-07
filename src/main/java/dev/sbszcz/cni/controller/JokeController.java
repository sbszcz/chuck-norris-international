package dev.sbszcz.cni.controller;

import dev.sbszcz.cni.service.JokeService;
import dev.sbszcz.cni.service.TranslationService;
import dev.sbszcz.cni.service.TranslationService.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class JokeController {

    private final JokeService jokeService;
    private final TranslationService translationService;

    public JokeController(JokeService jokeService, TranslationService translationService) {
        this.jokeService = jokeService;
        this.translationService = translationService;
    }

    @Value("${available-languages}")
    private String[] availableLanguages;

    record JokeResponseVO(String value){}

    @GetMapping("/aboutchuck")
    public JokeResponseVO aboutchuck(@RequestParam(name = "lang") Optional<String> language) {
        final String jokeText;

        if (language.isPresent()) {
            final String lang = language.get();
            if (languageNotAvailable(lang)) {
                throw new LanguageNotAvailableException(lang);
            }
            var originalJokeText = jokeService.randomJoke();
            jokeText = translationService.translateTextTo(originalJokeText, new Language(lang));
        } else {
            jokeText = jokeService.randomJoke();
        }

        return new JokeResponseVO(jokeText);
    }

    private boolean languageNotAvailable(String language) {
        return !Arrays.asList(availableLanguages).contains(language.toLowerCase());
    }

}
