package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.*;

public interface TypeOfDeclaration<Declaration extends PsiElement> {

    Class<Declaration> psiClass();

    TypeOfDeclaration<TypeDeclaration> TYPE = () -> TypeDeclaration.class;
    TypeOfDeclaration<OperatorDeclaration> OPERATOR = () -> OperatorDeclaration.class;
    TypeOfDeclaration<InfixDeclaration> INFIX = () -> InfixDeclaration.class;
    TypeOfDeclaration<PortDeclaration> PORT = () -> PortDeclaration.class;
    TypeOfDeclaration<ValueDeclaration> VALUE = () -> ValueDeclaration.class;
}

