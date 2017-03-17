package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.OperatorSymbolRef;
import mkolaczek.elm.psi.node.TypeExport;
import mkolaczek.elm.psi.node.ValueExport;

public interface TypeOfExport<T extends PsiElement> {

    Class<T> psiClass();

    TypeOfExport<TypeExport> TYPE = () -> TypeExport.class;
    TypeOfExport<OperatorSymbolRef> OPERATOR = () -> OperatorSymbolRef.class;
    TypeOfExport<ValueExport> VALUE = () -> ValueExport.class;
}
