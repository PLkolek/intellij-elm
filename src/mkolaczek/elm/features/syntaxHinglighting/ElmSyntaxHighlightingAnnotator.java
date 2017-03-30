package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.Declarations;
import mkolaczek.elm.psi.node.PortDeclaration;
import mkolaczek.elm.psi.node.TypeAnnotation;
import mkolaczek.elm.psi.node.ValueDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static mkolaczek.util.Streams.stream;

public class ElmSyntaxHighlightingAnnotator implements Annotator {

    private static final TextAttributesKey ELM_DEFINITION_NAME =
            createTextAttributesKey("ELM_DEFINITION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    private static final TextAttributesKey ELM_TYPE_ANNOTATION_NAME =
            createTextAttributesKey("ELM_TYPE_ANNOTATION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    private static final TextAttributesKey ELM_TYPE_ANNOTATION_SIGNATURE_TYPES =
            createTextAttributesKey("ELM_TYPE_ANNOTATION_SIGNATURE_TYPES",
                    DefaultLanguageHighlighterColors.CLASS_REFERENCE);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Declarations) {
            highlightTopLevelValues((Declarations) element, holder);
        }
        if (element instanceof TypeAnnotation) {
            TypeAnnotation typeAnnotation = (TypeAnnotation) element;
            stream(typeAnnotation.typeExpression())
                    .flatMap(e -> Stream.concat(e.typeRefs(), e.typeVariables()))
                    .forEach(t -> highlight(t, holder, ELM_TYPE_ANNOTATION_SIGNATURE_TYPES));
        }
    }

    private void highlightTopLevelValues(@NotNull Declarations element, @NotNull AnnotationHolder holder) {
        element.declarations().forEach(d -> {
            if (d instanceof ValueDeclaration) {
                ValueDeclaration valueDeclaration = (ValueDeclaration) d;
                valueDeclaration.topLevelValues().forEach(v -> highlight(v, holder, ELM_DEFINITION_NAME));
            }
            if (d instanceof PortDeclaration) {
                PortDeclaration portDeclaration = (PortDeclaration) d;
                highlight(portDeclaration.getNameIdentifier(), holder, ELM_DEFINITION_NAME);
            }
            if (d instanceof TypeAnnotation) {
                TypeAnnotation typeAnnotation = (TypeAnnotation) d;
                typeAnnotation.value()
                              .ifPresent(valueNameRef -> highlight(valueNameRef, holder, ELM_TYPE_ANNOTATION_NAME));
            }
        });
    }

    private void highlight(@Nullable PsiElement element,
                           @NotNull AnnotationHolder holder,
                           @NotNull TextAttributesKey style) {
        if (element == null) {
            return;
        }
        Annotation annotation = holder.createInfoAnnotation(element, null);
        annotation.setTextAttributes(style);
    }
}
