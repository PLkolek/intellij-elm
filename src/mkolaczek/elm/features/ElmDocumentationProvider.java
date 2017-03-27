package mkolaczek.elm.features;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.DocCommented;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mkolaczek.elm.psi.node.Module.module;

public class ElmDocumentationProvider implements DocumentationProvider {

    public static final String NEWLINE = "\n&nbsp&nbsp&nbsp&nbsp ";

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        String result = quickInfoText(element);
        return result != null ? wrapInHtmlTags(result) : null;
    }

    private String quickInfoText(PsiElement element) {
        if (element instanceof Module) {
            Module module = (Module) element;
            String settingsString = module.settingsList()
                                          .map(val -> String.format("%swhere { %s }",
                                                  NEWLINE,
                                                  concatValues(val.elements())))
                                          .orElse("");
            String type = (module.typeStr() + " ").replaceAll("^\\s+", "");
            return String.format("%smodule %s%s%sexposing (%s)",
                    type, module.getName(), settingsString, NEWLINE, exposedValuesString(module));

        }
        if (element instanceof TypeConstructor) {
            return ((TypeConstructor) element).typeDeclaration().getText();
        }
        if (element instanceof OperatorDeclaration) {
            String operatorName = ((OperatorDeclaration) element).getName();
            Optional<InfixDeclaration> infix = module(element).declarations(TypeOfDeclaration.INFIX, operatorName)
                                                              .findFirst();
            String infixDeclText = infix.map(PsiElement::getText).orElse("");
            return StringEscapeUtils.escapeHtml(infixDeclText + "\n" + element.getText());
        }

        if (element instanceof Declaration) {
            return StringEscapeUtils.escapeHtml(element.getText());
        }
        return null;
    }

    private String exposedValuesString(Module module) {
        Optional<ModuleValueList> valueList = module.header().flatMap(ModuleHeader::exposingList);
        String exposedValuesStr = "..";
        if (valueList.isPresent() && !valueList.get().isOpenListing()) {
            exposedValuesStr = concatValues(valueList.get().values(ExposedValue.class));
        }
        return exposedValuesStr;
    }

    private String concatValues(Collection<? extends PsiElement> values) {
        String[] exposedValues = values
                .stream()
                .map(PsiElement::getText)
                .map(str -> str.replaceAll("[ \t]+", " "))
                .map(str -> str.replace("\r", ""))
                .map(str -> str.replace("\n", ""))
                .toArray(String[]::new);
        return chopLongLines(exposedValues);
    }

    private String chopLongLines(String[] exposedValues) {
        List<List<String>> lines = Lists.newArrayList();
        List<String> currentLine = Lists.newArrayList();
        int currLength = 0;
        for (String exposedValue : exposedValues) {
            currentLine.add(exposedValue);
            currLength += exposedValue.length();
            if (currLength > 50) {
                lines.add(currentLine);
                currLength = 0;
                currentLine = Lists.newArrayList();
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }
        return lines.stream()
                    .map(strs -> Joiner.on(", ").join(strs))
                    .collect(Collectors.joining("," + NEWLINE));
    }

    private String wrapInHtmlTags(String text) {
        return String.format("<html><head></head><body>%s</body></html>", text);
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof TypeConstructor) {
            element = ((TypeConstructor) element).typeDeclaration();
        }
        if (element instanceof DocCommented) {
            DocCommented docCommented = (DocCommented) element;
            String docCommentText = docCommented.docComment().map(PsiElement::getText).orElse("");
            return String.format("<html><head></head><body><pre>%s</pre><p>%s</p></body></html>",
                    quickInfoText(docCommented),
                    docCommentText);
        }
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }
}
