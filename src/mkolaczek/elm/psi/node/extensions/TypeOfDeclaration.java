package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.*;

public interface TypeOfDeclaration<Declaration, Export extends PsiElement> {

    Class<Declaration> psiClass();

    TypeOfExposed<Export> exposedAs();

    TypeOfDeclaration<TypeDeclaration, TypeExposing> TYPE = new TypeOfDeclaration<TypeDeclaration, TypeExposing>() {
        @Override
        public Class<TypeDeclaration> psiClass() {
            return TypeDeclaration.class;
        }

        @Override
        public TypeOfExposed<TypeExposing> exposedAs() {
            return TypeOfExposed.TYPE;
        }
    };

    TypeOfDeclaration<OperatorDeclaration, OperatorSymbolRef> OPERATOR
            = new TypeOfDeclaration<OperatorDeclaration, OperatorSymbolRef>() {
        @Override
        public Class<OperatorDeclaration> psiClass() {
            return OperatorDeclaration.class;
        }

        @Override
        public TypeOfExposed<OperatorSymbolRef> exposedAs() {
            return TypeOfExposed.OPERATOR;
        }
    };

    TypeOfDeclaration<PortDeclaration, ValueExposing> PORT = new TypeOfDeclaration<PortDeclaration, ValueExposing>() {
        @Override
        public Class<PortDeclaration> psiClass() {
            return PortDeclaration.class;
        }

        @Override
        public TypeOfExposed<ValueExposing> exposedAs() {
            return TypeOfExposed.VALUE;
        }
    };
}

