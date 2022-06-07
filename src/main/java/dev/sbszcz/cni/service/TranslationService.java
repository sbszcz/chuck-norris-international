package dev.sbszcz.cni.service;

import dev.sbszcz.cni.gateway.LibreTranslateClient;
import org.springframework.stereotype.Component;

@Component
public class TranslationService {

    public record Language(String value){}

    final LibreTranslateClient libreTranslateClient;

    public TranslationService(LibreTranslateClient libreTranslateClient) {
        this.libreTranslateClient = libreTranslateClient;
    }

    public String translateTextTo(String text, Language lang) {
        return libreTranslateClient.translateTextTo(text, lang.value);
    }
}
