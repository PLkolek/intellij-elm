package mkolaczek.elm;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;
import static mkolaczek.elm.psi.ElmTypes.*;

public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        Capture<PsiElement> afterNewLine = psiElement().afterLeaf(psiElement(NEW_LINE));
        Capture<PsiElement> afterModuleNameRef = afterLeaf(childOf(MODULE_NAME_REF));
        Capture<PsiElement> afterModuleAlias = afterLeaf(childOf(MODULE_ALIAS));


        extend(CompletionType.BASIC, or(afterNewLine, psiElement(LOW_VAR)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("import"));
                    }
                }
        );

        extend(CompletionType.BASIC, or(afterModuleNameRef, psiElement(LOW_VAR)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("as"));
                        resultSet.addElement(LookupElementBuilder.create("exposing"));
                    }
                }
        );

        extend(CompletionType.BASIC, or(afterModuleAlias, psiElement(LOW_VAR)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("exposing"));
                    }
                }
        );

    }

    private Capture<PsiElement> childOf(IElementType elementType) {
        return psiElement().withParent(psiElement(elementType));
    }

    private Capture<PsiElement> afterLeaf(Capture<PsiElement> pattern) {
        return psiElement().afterLeafSkipping(whitespaceOrError(), pattern);
    }

    private ElementPattern whitespaceOrError() {
        return or(psiElement(WHITE_SPACE), psiElement(PsiErrorElement.class));
    }
}
