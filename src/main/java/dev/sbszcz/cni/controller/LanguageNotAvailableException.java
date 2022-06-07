package dev.sbszcz.cni.controller;

import static java.lang.String.format;

public class LanguageNotAvailableException extends RuntimeException {
    final String givenLanguage;
    public LanguageNotAvailableException(String givenLanguage) {
        super(format("Given language '%s' is not available for translation", givenLanguage));
        this.givenLanguage = givenLanguage;
    }

    public String getGivenLanguage() {
        return givenLanguage;
    }
}
