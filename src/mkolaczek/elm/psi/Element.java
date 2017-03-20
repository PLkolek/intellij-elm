package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.boilerplate.ElmLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class Element extends IElementType {

    private final String name;

    public Element(String name) {
        this(debugName(name), name);
    }

    static String debugName(String name) {
        return stream(name.split("\\s+")).map(String::toUpperCase).collect(joining("_"));
    }

    public Element(@NotNull @NonNls String debugName, String name) {
        super(debugName, ElmLanguage.INSTANCE);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
