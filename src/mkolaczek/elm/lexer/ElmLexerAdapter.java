package mkolaczek.elm.lexer;

import com.intellij.lexer.FlexAdapter;


public class ElmLexerAdapter extends FlexAdapter {

    public ElmLexerAdapter() {
        super(new ElmLexer(null));
    }

    public static boolean isSymbol(int c) {
        return "+-/*=.<>:&|^?%#~!".indexOf(c) != -1;
    }

    public static boolean isSymbol(String newName) {
        return newName.chars()
                      .mapToObj(ElmLexerAdapter::isSymbol)
                      .reduce(true, Boolean::logicalAnd);
    }
}
