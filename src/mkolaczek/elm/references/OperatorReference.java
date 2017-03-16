package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import mkolaczek.elm.psi.node.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mkolaczek.elm.psi.node.Module.module;

public class OperatorReference extends PsiReferenceBase<Operator> {
    public OperatorReference(Operator element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return module(myElement).operatorDeclarations()
                                .filter(d -> d.sameName(myElement.getName()))
                                .findAny().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return PsiReference.EMPTY_ARRAY;
    }
}
