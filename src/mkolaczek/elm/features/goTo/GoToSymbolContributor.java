package mkolaczek.elm.features.goTo;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.InfixOperatorDeclaration;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static mkolaczek.elm.ProjectUtil.modules;

public class GoToSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {

        Stream<String> constructors = typeDecls(project).flatMap(TypeDeclaration::constructors)
                                                        .map(TypeConstructor::getName);

        Stream<String> types = typeDecls(project).map(PsiNamedElement::getName);
        Stream<String> operators = operators(project).map(InfixOperatorDeclaration::parensName)
                                                     .flatMap(Streams::stream);
        return Stream.of(constructors, types, operators).flatMap(identity()).toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name,
                                           String pattern,
                                           Project project,
                                           boolean includeNonProjectItems) {

        Stream<TypeDeclaration> types = typeDecls(project).filter(d -> name.equals(d.getName()));
        Stream<TypeConstructor> constructors = typeDecls(project).flatMap(TypeDeclaration::constructors)
                                                                 .filter(c -> name.equals(c.getName()));
        Stream<InfixOperatorDeclaration> operators = operators(project).filter(o -> o.sameParensName(name));

        return Stream.of(types, constructors, operators)
                     .flatMap(identity())
                     .toArray(NavigationItem[]::new);
    }

    private Stream<TypeDeclaration> typeDecls(Project project) {
        return modules(project).flatMap(Module::typeDeclarations);
    }

    private Stream<InfixOperatorDeclaration> operators(Project project) {
        return modules(project).flatMap(Module::operatorDeclarations);
    }
}
