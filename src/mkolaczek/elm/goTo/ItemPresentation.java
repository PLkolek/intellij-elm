package mkolaczek.elm.goTo;

import mkolaczek.elm.ElmIcon;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ItemPresentation implements com.intellij.navigation.ItemPresentation {

    private final String name;

    public ItemPresentation(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return name;
    }

    @Nullable
    @Override
    public String getLocationString() {
        return "flelfle";
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return ElmIcon.FILE;
    }
}
