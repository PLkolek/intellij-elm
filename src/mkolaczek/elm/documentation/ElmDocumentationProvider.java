package mkolaczek.elm.documentation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import mkolaczek.elm.psi.node.ElmDocComment;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmModuleValueList;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ElmDocumentationProvider implements DocumentationProvider {

    public static final String NEWLINE = "\n&nbsp&nbsp&nbsp&nbsp ";

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        String result = quickInfoText(element);
        return result != null ? wrapInHtmlTags(result) : null;
    }

    private String quickInfoText(PsiElement element) {
        if (element instanceof ElmModule) {
            ElmModule module = (ElmModule) element;
            String settingsString = module.settingsList()
                                          .map(val -> String.format("%swhere { %s }",
                                                  NEWLINE,
                                                  concatValues(val.elements())))
                                          .orElse("");
            String type = (module.type() + " ").replaceAll("^\\s+", "");
            return String.format("%smodule %s%s%sexposing (%s)",
                    type, module.getName(), settingsString, NEWLINE, exposedValuesString(module));

        }
        return null;
    }

    private String exposedValuesString(ElmModule module) {
        ElmModuleValueList valueList = module.exposedValues();
        String exposedValuesStr = "..";
        if (!valueList.isOpenListing()) {
            exposedValuesStr = concatValues(valueList.values());
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

    private String wrapInHtmlTags(String aa) {
        return String.format("<html><head></head><body>" + aa + "</body></html>", aa);
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof ElmModule) {
            ElmModule module = (ElmModule) element;
            ElmDocComment docComment = module.docComment();
            String docCommentText = docComment != null ? docComment.getText() : "";
            return String.format("<html><head></head><body><pre>%s</pre><p>%s</p></body></html>",
                    quickInfoText(module),
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
