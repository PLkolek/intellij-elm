package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

public class ElmWrapFactory {

    private static final Set<IElementType> wrappedElements = ImmutableSet.of();

    public static Wrap createWrap(IElementType type) {
        if (wrappedElements.contains(type)) {
            return Wrap.createWrap(WrapType.NORMAL, false);
        }
        return Wrap.createWrap(WrapType.NONE, false);
    }
}
