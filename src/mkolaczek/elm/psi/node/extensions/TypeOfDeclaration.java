package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.PortDeclaration;
import mkolaczek.elm.psi.node.TypeDeclaration;

public interface TypeOfDeclaration<T extends PsiNamedElement> {

    Class<T> psiClass();

    TypeOfDeclaration<TypeDeclaration> TYPE = () -> TypeDeclaration.class;
    TypeOfDeclaration<OperatorDeclaration> OPERATOR = () -> OperatorDeclaration.class;
    TypeOfDeclaration<PortDeclaration> PORT = () -> PortDeclaration.class;
}

