package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.MainDefinedValues;
import mkolaczek.elm.psi.node.ValueName;
import mkolaczek.elm.psi.node.ValueNameRef;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ElmSyntaxHighlightingAnnotator implements Annotator {

    private static final TextAttributesKey ELM_DEFINITION_NAME =
            createTextAttributesKey("ELM_DEFINITION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {


        if (isValueDefinition(element)) {
            Annotation annotation = holder.createInfoAnnotation(element, null);
            annotation.setTextAttributes(ELM_DEFINITION_NAME);

        }
    }

    public boolean isValueDefinition(@NotNull PsiElement element) {
        return (element instanceof ValueName || element instanceof ValueNameRef)
                && element.getParent() instanceof MainDefinedValues;
    }
}
