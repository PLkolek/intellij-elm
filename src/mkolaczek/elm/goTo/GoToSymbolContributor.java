package mkolaczek.elm.goTo;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.TypeName;
import mkolaczek.util.Optionals;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class GoToSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {

        Stream<String> constructors = typeDecls(project).flatMap(d -> d.constructors().stream())
                                                        .map(TypeConstructor::getName);

        Stream<String> types = typeDecls(project)
                .map(TypeDeclaration::typeNameString)
                .flatMap(Optionals::stream);

        return Stream.concat(constructors, types).toArray(String[]::new);

    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name,
                                           String pattern,
                                           Project project,
                                           boolean includeNonProjectItems) {

        Stream<TypeName> types = typeDecls(project).map(TypeDeclaration::typeName)
                                                   .flatMap(Optionals::stream)
                                                   .filter(d -> name.equals(d.getName()));
        Stream<TypeConstructor> constructors = typeDecls(project).flatMap(d -> d.constructors().stream())
                                                                 .filter(c -> name.equals(c.getName()));
        return Stream.concat(types, constructors)
                     .toArray(NavigationItem[]::new);
    }

    private Stream<TypeDeclaration> typeDecls(Project project) {
        return ProjectUtil.modules(project)
                          .flatMap(m -> m.typeDeclarations().stream());
    }
}
