package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ElmLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Element extends IElementType {

    public Element(@NotNull @NonNls String debugName) {
        super(debugName, ElmLanguage.INSTANCE);
    }
}
