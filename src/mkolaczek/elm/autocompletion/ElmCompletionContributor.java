package mkolaczek.elm.autocompletion;

import com.google.common.base.Joiner;
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
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toList;
import static mkolaczek.elm.autocompletion.ModulePattern.module;
import static mkolaczek.elm.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        autocomplete(afterWhitespace("\n").and(inBlock(IMPORTS, DECLARATIONS)),
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
            return newArrayList(moduleName);
        });
        autocomplete(afterLeaf(Tokens.AS), parameters -> {
            ElmImport2 importLine = getParentOfType(parameters.getPosition(), ElmImport2.class);
            Preconditions.checkState(importLine != null, "As must be a child of import line");
            ElmModuleNameRef module = importLine.importedModule();
            String[] words = module.getName().split("\\.");
            return Names.suggest(words);
        });

        autocomplete(afterLeaf(e(TYPE)), parameters -> {
            Optional<ElmModuleHeader> header = ((ElmFile) parameters.getPosition().getContainingFile()).header();
            if (!header.isPresent()) {
                return Lists.newArrayList();
            }
            return header.get().typeExports()
                         .stream()
                         .flatMap(ElmCompletionContributor::typeCompletions)
                         .collect(toList());
        });


        autocomplete(afterLeaf(e(ALIAS)), parameters -> {
            Optional<ElmModuleHeader> header = ((ElmFile) parameters.getPosition().getContainingFile()).header();
            if (!header.isPresent()) {
                return Lists.newArrayList();
            }
            return header.get().typeExports()
                         .stream()
                         .filter(ElmTypeExport::withoutConstructors)
                         .map(ElmTypeExport::typeName).collect(toList());
        });

        autocomplete(afterLeaf(EQUALS, PIPE).inside(e(TYPE_DECL_NODE)), parameters -> {
            Optional<ElmModuleHeader> header = ((ElmFile) parameters.getPosition().getContainingFile()).header();
            if (!header.isPresent()) {
                return Lists.newArrayList();
            }
            ElmTypeDeclaration declaration = getParentOfType(parameters.getPosition(), ElmTypeDeclaration.class);
            assert declaration != null;
            Optional<String> typeName = declaration.typeName();
            Collection<String> constructors = typeName.flatMap(header.get()::typeExport)
                                                      .map(ElmTypeExport::constructors)
                                                      .orElse(newArrayList());
            List<String> presentConstructors = declaration.constructors()
                                                          .stream()
                                                          .map(ElmTypeConstructor::getText)
                                                          .collect(toList());

            constructors.removeAll(presentConstructors);
            return constructors;
        });


        autocomplete(afterWhitespace("\n").and(after(IMPORTS)),
                keyword("type"));
        autocomplete(afterLeaf(TYPE), keyword("alias"));


    }

    private static Stream<String> typeCompletions(ElmTypeExport typeExport) {
        ArrayList<String> result = Lists.newArrayList(typeExport.typeName());
        if (!typeExport.withoutConstructors()) {
            result.add(typeDeclaration(typeExport));
        }
        return result.stream();
    }

    private static String typeDeclaration(ElmTypeExport typeExport) {
        return typeExport.typeName() + " = " + Joiner.on(" | ").join(typeExport.constructors());
    }

    @NotNull
    private LookupElementBuilder keyword(String item) {
        return LookupElementBuilder.create(item).withInsertHandler(AddSpaceInsertHandler.INSTANCE);
    }

    private void autocomplete(Capture<PsiElement> pattern, final LookupElementBuilder... completions) {
        autocomplete2(pattern, parameters -> Arrays.asList(completions));
    }

    private void autocomplete(Capture<PsiElement> pattern,
                              Function<CompletionParameters, Collection<String>> autocompletion) {
        Function<Collection<String>, Collection<LookupElementBuilder>> wrapper = strings -> strings.stream()
                                                                                                   .map(LookupElementBuilder::create)
                                                                                                   .collect(toList());
        autocomplete2(pattern, wrapper.compose(autocompletion));
    }


    private void autocomplete2(Capture<PsiElement> pattern,
                               Function<CompletionParameters, Collection<LookupElementBuilder>> autocompletion) {
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
