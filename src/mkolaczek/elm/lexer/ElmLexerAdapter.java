package mkolaczek.elm.lexer;

import com.google.common.collect.ImmutableSet;
import com.intellij.lexer.FlexAdapter;


public class ElmLexerAdapter extends FlexAdapter {
    public static final ImmutableSet<Byte> SYMBOL_TYPES =
            ImmutableSet.of(
                    Character.MATH_SYMBOL,
                    Character.CURRENCY_SYMBOL,
                    Character.MODIFIER_SYMBOL,
                    Character.OTHER_SYMBOL);

    public ElmLexerAdapter() {
        super(new ElmLexer(null));
    }

    public static boolean isSymbol(char c) {
        int type = Character.getType(c);
        boolean isByte = type >= Byte.MIN_VALUE && type <= Byte.MAX_VALUE;
        boolean allowedUnicodeSymbol = isByte && SYMBOL_TYPES.contains((byte) type);
        return (allowedUnicodeSymbol || "+-/*=.$<>:&|\\^?%#@~!,".indexOf(c) != -1) && c != '`';
    }
}
