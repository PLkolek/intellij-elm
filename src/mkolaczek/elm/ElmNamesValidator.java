package mkolaczek.elm;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ElmNamesValidator implements NamesValidator {

    @Override
    public boolean isKeyword(@NotNull String name, Project project) {
        return false;
    }

    @Override
    public boolean isIdentifier(@NotNull String name, Project project) {
        return name.matches("[A-Z][a-zA-Z0-9]*(\\.[A-Z][a-zA-Z0-9]*)*");
    }
}
