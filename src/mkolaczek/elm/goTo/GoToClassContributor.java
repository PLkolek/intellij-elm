package mkolaczek.elm.goTo;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.NotNull;

public class GoToClassContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        return ProjectUtil.modules(project).map(Module::getName).toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name,
                                           String pattern,
                                           Project project,
                                           boolean includeNonProjectItems) {
        return ProjectUtil.modules(project).filter(module -> name.equals(module.getName())).toArray(Module[]::new);
    }
}
