package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ElmLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Element extends IElementType {

    private final String name;

    public Element(String name) {
        this(name, name);
    }

    public Element(@NotNull @NonNls String debugName, String name) {
        super(debugName, ElmLanguage.INSTANCE);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
