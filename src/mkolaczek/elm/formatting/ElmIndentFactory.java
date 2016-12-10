package mkolaczek.elm.formatting;


import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.Indent;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmIndentFactory {
    private static final Set<IElementType> indentedElements = ImmutableSet.of(EXPOSING_NODE,
            LPAREN,
            COMMA,
            RPAREN,
            EXPORTED_VALUE,
            EFFECT_MODULE_SETTINGS,
            LBRACKET,
            RBRACKET);

    public static Indent createIndent(IElementType type) {
        if (indentedElements.contains(type)) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }
}
