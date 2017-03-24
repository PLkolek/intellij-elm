package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.OperatorSymbolRef;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.OPERATOR;

public class OperatorReference extends ElmReference<OperatorSymbolRef> {
    public OperatorReference(OperatorSymbolRef element) {
        super(element);
    }


    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        Stream<OperatorDeclaration> resolved;
        if (insideModuleHeader(myElement)) {
            resolved = module(myElement).declarations(OPERATOR)
                                        .filter(d -> d.sameName(myElement.getName()));
        } else if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exportedDeclaration(OPERATOR, myElement.getName()));
        } else {
            throw new IllegalStateException("Operators in code not supperted yet");
        }
        return resolved;
    }

}
