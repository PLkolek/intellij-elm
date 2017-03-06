package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import mkolaczek.elm.psi.node.ElmRuneOfAutocompletion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmAutocompleteRuneReference extends PsiReferenceBase<ElmRuneOfAutocompletion> {
    public ElmAutocompleteRuneReference(ElmRuneOfAutocompletion element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        assert false; //should never be called
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ElmTypeReference.variants(myElement);
    }
}
