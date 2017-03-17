package mkolaczek.elm.lexer;

import com.intellij.lexer.FlexAdapter;


public class ElmLexerAdapter extends FlexAdapter {

    public ElmLexerAdapter() {
        super(new ElmLexer(null));
    }

    public static boolean isSymbol(char c) {
        return "+-/*=.<>:&|^?%#~!".indexOf(c) != -1;
    }
}
