package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ElmLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Token extends IElementType {
    public Token(@NotNull @NonNls String debugName) {
        super(debugName, ElmLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "Token." + super.toString();
    }
}