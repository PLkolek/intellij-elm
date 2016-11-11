package mkolaczek.elm.documentation;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmModuleValueList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ElmDocumentationProvider implements DocumentationProvider {
    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof ElmModule) {
            ElmModule module = (ElmModule) element;
            ElmModuleValueList valueList = module.exposedValues();
            String exposedValues = "..";
            if (!valueList.isOpenListing()) {
                exposedValues = valueList.values().stream().map(PsiElement::getText).collect(
                        Collectors.joining(", "));
            }
            return String.format("module %s exposing (%s)", module.getName(), exposedValues);
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
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
