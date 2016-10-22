package mkolaczek.elm.formatting;


import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.Indent;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

import static mkolaczek.elm.psi.ElmElementTypes.EXPOSING_NODE;
import static mkolaczek.elm.psi.ElmTokenTypes.*;

public class ElmIndentFactory {
    private static final Set<IElementType> indentedElements = ImmutableSet.of(EXPOSING_NODE, LPAREN, COMMA, RPAREN);

    public static Indent createIndent(IElementType type) {
        if (indentedElements.contains(type)) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }
}
