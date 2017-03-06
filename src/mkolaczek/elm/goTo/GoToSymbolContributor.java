package mkolaczek.elm.goTo;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.ElmTypeConstructor;
import mkolaczek.elm.psi.node.ElmTypeDeclaration;
import mkolaczek.elm.psi.node.ElmTypeName;
import mkolaczek.util.Optionals;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class GoToSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {

        Stream<String> constructors = typeDecls(project).flatMap(d -> d.constructors().stream())
                                                        .map(ElmTypeConstructor::getName);

        Stream<String> types = typeDecls(project)
                .map(ElmTypeDeclaration::typeNameString)
                .flatMap(Optionals::stream);

        return Stream.concat(constructors, types).toArray(String[]::new);

    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name,
                                           String pattern,
                                           Project project,
                                           boolean includeNonProjectItems) {

        Stream<ElmTypeName> types = typeDecls(project).map(ElmTypeDeclaration::typeName)
                                                      .flatMap(Optionals::stream)
                                                      .filter(d -> name.equals(d.getName()));
        Stream<ElmTypeConstructor> constructors = typeDecls(project).flatMap(d -> d.constructors().stream())
                                                                    .filter(c -> name.equals(c.getName()));
        return Stream.concat(types, constructors)
                     .toArray(NavigationItem[]::new);
    }

    private Stream<ElmTypeDeclaration> typeDecls(Project project) {
        return ProjectUtil.modules(project)
                          .flatMap(m -> m.typeDeclarations().stream());
    }
}
