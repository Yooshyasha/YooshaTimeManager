package org.yoosha.localization;

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public record UIText(Localization localization) {

    public String getText(String key) {
        String jsonString = null;

        try (InputStream is = getClass().getResourceAsStream("/ui/text/text.json")) {
            if (is != null) {jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);}
        } catch (IOException e) {throw new RuntimeException(e);}

        if (jsonString != null) {
            try {
                JsonReader reader = Json.createReader(new StringReader(jsonString));

                String langCode = Objects.equals(localization.getLanguage(),
                        Localization.Localizations.RU) ? "ru" : "en";

                return reader.readObject().get(langCode).asJsonObject().get(key)
                        .toString().replace("\"", "");
            } catch (Exception ex) {throw new RuntimeException(ex);}
        }
        return null;
    }
}
