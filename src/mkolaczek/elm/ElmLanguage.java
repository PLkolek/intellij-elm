package mkolaczek.elm;

import com.intellij.lang.Language;

public class ElmLanguage extends Language {
    public static final ElmLanguage INSTANCE = new ElmLanguage();

    public ElmLanguage() {
        super("Elm");
    }
}
