package mkolaczek.elm.references;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static mkolaczek.elm.psi.node.Module.module;

public class ModuleReference extends PsiReferenceBase.Poly<ModuleNameRef> {
    public ModuleReference(ModuleNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()), false);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return modules()
                .filter(e -> Objects.equals(e.getName(), myElement.getName()))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    private Stream<? extends ASTWrapperPsiElement> modules() {
        return Stream.of(
                module(myElement)
                        .notAliasedImports()
                        .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                                import_.importedModuleNameString().orElse(null))),
                module(myElement)
                        .aliasedImports()
                        .map(Import::alias)
                        .flatMap(Streams::stream),
                DefaultImports.modules(myElement.getProject())
        ).flatMap(Function.identity());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return modules().toArray(Object[]::new);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
