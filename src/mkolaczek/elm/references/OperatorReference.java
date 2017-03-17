package mkolaczek.elm.references;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.OperatorSymbolRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;

public class OperatorReference extends ElmReference {
    public OperatorReference(OperatorSymbolRef element) {
        super(element);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return multiResolve()
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    private Stream<OperatorDeclaration> multiResolve() {
        Stream<OperatorDeclaration> resolved;
        if (insideModuleHeader(myElement)) {
            resolved = module(myElement).operatorDeclarations()
                                        .filter(d -> d.sameName(myElement.getName()));
        } else if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exposedOperatorDeclaration(
                                                          myElement.getName()));
        } else {
            throw new IllegalStateException("Operators in code not supperted yet");
        }
        return resolved;
    }

}
