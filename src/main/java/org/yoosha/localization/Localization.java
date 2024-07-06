package org.yoosha.localization;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class Localization {
    private String language;

    public Localization() {
        Locale locale = LocaleContextHolder.getLocale();
        language = locale.getLanguage();
    }

    public Localization(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static final class Localizations {
        // На данный момент проверка идет только на RU, в дальнейшем возможно будут добавлены и другие языки
        public static String RU = "ru";
        public static String EN = "en";
    }
}
