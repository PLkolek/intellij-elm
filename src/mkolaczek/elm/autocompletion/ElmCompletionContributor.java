package mkolaczek.elm.autocompletion;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.ElmImport2;
import mkolaczek.elm.psi.node.ElmModuleNameRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mkolaczek.elm.autocompletion.ModulePattern.module;
import static mkolaczek.elm.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        autocomplete(afterWhitespace("\n").and(inBlock(IMPORTS, DECLARATIONS)).andNot(inside(IMPORT_LINE)),
                keyword("import"));
        autocomplete(afterLeaf(childOf(MODULE_NAME_REF)).andNot(afterWhitespace("\n")),
                keyword("as"),
                exposingCompletion());
        Capture<PsiElement> insideEffectModule = e().inside(module().effectModule());
        autocomplete(afterLeaf(childOf(MODULE_NAME))
                .andNot(insideEffectModule), exposingCompletion());
        autocomplete(afterLeaf(childOf(MODULE_NAME))
                        .andOr(insideEffectModule, afterLeaf(insideEffectModule)),
                whereCompletion());
        autocomplete(afterLeaf(RBRACKET).inside(module().effectModule()), exposingCompletion());
        autocomplete(afterLeaf(childOf(MODULE_ALIAS)), exposingCompletion());
        autocomplete(e().afterLeaf(e().isNull()),
                keyword("module"),
                keyword("port module"),
                keyword("effect module")
        );
        autocomplete(afterLeaf(Tokens.PORT), keyword("module"));
        autocomplete(afterLeaf(EFFECT), keyword("module"));
        autocomplete(afterLeaf(Tokens.MODULE), parameters -> {
            String fileName = parameters.getOriginalFile().getName();
            String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
            return Lists.newArrayList(moduleName);
        });
        autocomplete(afterLeaf(Tokens.AS), parameters -> {
            ElmImport2 importLine = PsiTreeUtil.getParentOfType(parameters.getPosition(), ElmImport2.class);
            Preconditions.checkState(importLine != null, "As must be a child of import line");
            ElmModuleNameRef module = importLine.importedModule();
            String[] words = module.getName().split("\\.");
            return Names.suggest(words);
        });


        autocomplete(afterWhitespace("\n").and(after(IMPORTS)).andNot(inside(TYPE_DECLARATION)),
                keyword("type"));
        autocomplete(afterLeaf(TYPE), keyword("alias"));


    }

    @NotNull
    private LookupElementBuilder keyword(String item) {
        return LookupElementBuilder.create(item).withInsertHandler(AddSpaceInsertHandler.INSTANCE);
    }

    private void autocomplete(Capture<PsiElement> pattern, final LookupElementBuilder... completions) {
        autocomplete2(pattern, parameters -> Arrays.asList(completions));
    }

    private void autocomplete(Capture<PsiElement> pattern,
                              Function<CompletionParameters, List<String>> autocompletion) {
        Function<List<String>, List<LookupElementBuilder>> wrapper = strings -> strings.stream()
                                                                                       .map(LookupElementBuilder::create)
                                                                                       .collect(Collectors.toList());
        autocomplete2(pattern, wrapper.compose(autocompletion));
    }


    private void autocomplete2(Capture<PsiElement> pattern,
                               Function<CompletionParameters, List<LookupElementBuilder>> autocompletion) {
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

    private LookupElementBuilder whereCompletion() {
        return LookupElementBuilder.create("where")
                                   .withInsertHandler((context, item) -> {
                                       Editor editor = context.getEditor();
                                       Project project = editor.getProject();
                                       if (project != null) {
                                           EditorModificationUtil.insertStringAtCaret(editor, " {}");
                                           editor.getCaretModel()
                                                 .moveToOffset(editor.getCaretModel().getOffset() - 1);
                                           PsiDocumentManager.getInstance(project)
                                                             .commitDocument(editor.getDocument());
                                       }
                                   });
    }


    @NotNull
    private LookupElementBuilder exposingCompletion() {
        return LookupElementBuilder.create("exposing")
                                   .withInsertHandler(new ParenthesesInsertHandler<LookupElement>(true,
                                           false,
                                           true,
                                           true) {
                                       @Override
                                       protected boolean placeCaretInsideParentheses(InsertionContext context,
                                                                                     LookupElement item) {
                                           return true;
                                       }

                                       @Nullable
                                       @Override
                                       protected PsiElement findNextToken(@NotNull InsertionContext context) {
                                           final PsiFile file = context.getFile();
                                           PsiElement element = file.findElementAt(context.getTailOffset());
                                           if (element != null && isWhitespace(element)) {
                                               element = file.findElementAt(element.getTextRange().getEndOffset());
                                           }
                                           return element;
                                       }

                                       private boolean isWhitespace(PsiElement element) {
                                           return element.getNode().getElementType() == Tokens.NEW_LINE;
                                       }
                                   });
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier("\u16DC");
    }
}
