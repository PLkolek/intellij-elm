package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.boilerplate.ElmLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Token extends IElementType {

    private final String name;

    public Token(String name) {
        this(Element.debugName(name), name);
    }

    public Token(@NotNull @NonNls String debugName, String name) {
        super(debugName, ElmLanguage.INSTANCE);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Token." + super.toString();
    }

    public String getName() {
        return name;
    }

}