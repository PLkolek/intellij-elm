package mkolaczek.elm.lexer;

import com.intellij.lexer.FlexAdapter;


public class ElmLexerAdapter extends FlexAdapter {
    public ElmLexerAdapter() {
        super(new ElmLexer(null));
    }
}
