package mkolaczek.elm.features.goTo;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.PortDeclaration;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static mkolaczek.elm.ProjectUtil.modules;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.*;

public class GoToSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {

        Stream<String> constructors = decls(project, TYPE).flatMap(TypeDeclaration::constructors)
                                                          .map(TypeConstructor::getName);

        Stream<String> types = decls(project, TYPE).map(PsiNamedElement::getName);
        Stream<String> ports = decls(project, PORT).map(PsiNamedElement::getName);
        Stream<String> operators = decls(project, OPERATOR).map(OperatorDeclaration::parensName)
                                                           .flatMap(Streams::stream);
        return Stream.of(constructors, types, operators, ports).flatMap(identity()).toArray(String[]::new);
    }


    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name,
                                           String pattern,
                                           Project project,
                                           boolean includeNonProjectItems) {

        Stream<TypeDeclaration> types = decls(project, TYPE).filter(d -> name.equals(d.getName()));
        Stream<PortDeclaration> ports = decls(project, PORT).filter(d -> name.equals(d.getName()));
        Stream<TypeConstructor> constructors = decls(project, TYPE).flatMap(TypeDeclaration::constructors)
                                                                   .filter(c -> name.equals(c.getName()));
        Stream<OperatorDeclaration> operators = decls(project, OPERATOR).filter(o -> o.sameParensName(name));

        return Stream.of(types, constructors, operators, ports)
                     .flatMap(identity())
                     .toArray(NavigationItem[]::new);
    }

    private <T extends PsiElement> Stream<T> decls(Project project, TypeOfDeclaration<T, ?> type) {
        return modules(project).flatMap((module) -> module.declarations(type));
    }
}
