package mkolaczek.elm.formatting;


import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.Indent;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

import static mkolaczek.elm.psi.ElmElementTypes.MODULE_NAME;
import static mkolaczek.elm.psi.ElmElementTypes.MODULE_VALUE_LIST;
import static mkolaczek.elm.psi.ElmTokenTypes.EXPOSING;

public class ElmIndentFactory {
    private static final Set<IElementType> indentedElements = ImmutableSet.of(MODULE_VALUE_LIST, EXPOSING, MODULE_NAME);

    public static Indent createIndent(IElementType type) {
        if (indentedElements.contains(type)) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }
}
