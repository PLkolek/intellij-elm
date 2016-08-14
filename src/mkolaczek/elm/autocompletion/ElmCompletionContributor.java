package mkolaczek.elm.autocompletion;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.ElmTypes;
import mkolaczek.elm.psi.node.ElmImport2;
import mkolaczek.elm.psi.node.ElmModuleNameRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static mkolaczek.elm.psi.ElmTypes.*;

public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        simpleAutocomplete(Patterns.justAfterLeaf(NEW_LINE), LookupElementBuilder.create("import "));
        simpleAutocomplete(Patterns.afterLeaf(Patterns.childOf(MODULE_NAME_REF)), LookupElementBuilder.create("as "), exposingCompletion());
        simpleAutocomplete(Patterns.afterLeaf(Patterns.childOf(MODULE_ALIAS)), exposingCompletion());
        simpleAutocomplete(psiElement().afterLeaf(psiElement().isNull()), LookupElementBuilder.create("module "));
        simpleAutocomplete(Patterns.afterLeaf(Patterns.childOf(MODULE_NAME)), exposingCompletion());
        simpleAutocomplete(afterLeaf(MODULE), parameters -> {
            String fileName = parameters.getOriginalFile().getName();
            String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
            return Lists.newArrayList(moduleName);
        });
        simpleAutocomplete(afterLeaf(AS), parameters -> {
            ElmImport2 importLine = PsiTreeUtil.getParentOfType(parameters.getPosition(), ElmImport2.class);
            Preconditions.checkState(importLine != null, "As must be a child of import line");
            ElmModuleNameRef module = importLine.findImportedModule();
            String[] words = module.getName().split("\\.");
            return Names.suggest(words);
        });
    }

    private Capture<PsiElement> afterLeaf(IElementType elementType) {
        return Patterns.afterLeaf(psiElement(elementType));
    }

    private void simpleAutocomplete(Capture<PsiElement> pattern, final LookupElementBuilder... completions) {
        autocomplete(pattern, parameters -> Arrays.asList(completions));
    }


    private void simpleAutocomplete(Capture<PsiElement> pattern, Function<CompletionParameters, List<String>> autocompletion) {
        Function<List<String>, List<LookupElementBuilder>> wrapper = strings -> strings.stream()
                                                                                       .map(LookupElementBuilder::create)
                                                                                       .collect(Collectors.toList());
        autocomplete(pattern, wrapper.compose(autocompletion));
    }

    private void autocomplete(Capture<PsiElement> pattern, Function<CompletionParameters, List<LookupElementBuilder>> autocompletion) {
        extend(CompletionType.BASIC, pattern,
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addAllElements(autocompletion.apply(parameters));
                    }
                }
        );
    }

    @NotNull
    private LookupElementBuilder exposingCompletion() {
        return LookupElementBuilder.create("exposing").withInsertHandler(new ParenthesesInsertHandler<LookupElement>(true, false, true, true) {
            @Override
            protected boolean placeCaretInsideParentheses(InsertionContext context, LookupElement item) {
                return true;
            }

            @Nullable
            @Override
            protected PsiElement findNextToken(InsertionContext context) {
                final PsiFile file = context.getFile();
                PsiElement element = file.findElementAt(context.getTailOffset());
                if (element != null && isWhitespace(element)) {
                    element = file.findElementAt(element.getTextRange().getEndOffset());
                }
                return element;
            }

            private boolean isWhitespace(PsiElement element) {
                return element.getNode().getElementType() == ElmTypes.WHITE_SPACE || element.getNode().getElementType() == ElmTypes.NEW_LINE;
            }
        });
    }
}
