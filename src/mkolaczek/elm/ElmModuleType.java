package mkolaczek.elm;

import com.intellij.openapi.module.ModuleType;
import mkolaczek.elm.boilerplate.ElmIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ElmModuleType extends ModuleType<ElmModuleBuilder> {

    public static final ElmModuleType INSTANCE = new ElmModuleType();

    public ElmModuleType() {
        super("ELM_MODULE");
    }

    @NotNull
    @Override
    public ElmModuleBuilder createModuleBuilder() {
        return new ElmModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "Elm";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Support for Elm";
    }

    @Override
    public Icon getBigIcon() {
        return null;
    }

    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return ElmIcon.FILE;
    }
}
