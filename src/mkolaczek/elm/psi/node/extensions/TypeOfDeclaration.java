package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.*;

public interface TypeOfDeclaration<Declaration, Export extends PsiElement> {

    Class<Declaration> psiClass();

    TypeOfExport<Export> exportedAs();

    TypeOfDeclaration<TypeDeclaration, TypeExport> TYPE = new TypeOfDeclaration<TypeDeclaration, TypeExport>() {
        @Override
        public Class<TypeDeclaration> psiClass() {
            return TypeDeclaration.class;
        }

        @Override
        public TypeOfExport<TypeExport> exportedAs() {
            return TypeOfExport.TYPE;
        }
    };

    TypeOfDeclaration<OperatorDeclaration, OperatorSymbolRef> OPERATOR
            = new TypeOfDeclaration<OperatorDeclaration, OperatorSymbolRef>() {
        @Override
        public Class<OperatorDeclaration> psiClass() {
            return OperatorDeclaration.class;
        }

        @Override
        public TypeOfExport<OperatorSymbolRef> exportedAs() {
            return TypeOfExport.OPERATOR;
        }
    };

    TypeOfDeclaration<PortDeclaration, ValueExport> PORT = new TypeOfDeclaration<PortDeclaration, ValueExport>() {
        @Override
        public Class<PortDeclaration> psiClass() {
            return PortDeclaration.class;
        }

        @Override
        public TypeOfExport<ValueExport> exportedAs() {
            return TypeOfExport.VALUE;
        }
    };
}

