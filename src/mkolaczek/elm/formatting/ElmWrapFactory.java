package mkolaczek.elm.formatting;

import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

import static mkolaczek.elm.psi.ElmElementTypes.MODULE_NAME;
import static mkolaczek.elm.psi.ElmElementTypes.MODULE_VALUE_LIST;
import static mkolaczek.elm.psi.ElmTokenTypes.EXPOSING;

public class ElmWrapFactory {

    private static final Set<IElementType> wrappedElements = ImmutableSet.of(MODULE_VALUE_LIST, EXPOSING, MODULE_NAME);

    public static Wrap createWrap(IElementType type) {
        if (wrappedElements.contains(type)) {
            return Wrap.createWrap(WrapType.NORMAL, false);
        }
        return Wrap.createWrap(WrapType.NONE, false);
    }
}
