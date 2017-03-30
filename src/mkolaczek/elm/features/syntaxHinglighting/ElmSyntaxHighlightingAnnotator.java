package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static java.util.function.Function.identity;
import static mkolaczek.util.Streams.stream;

public class ElmSyntaxHighlightingAnnotator implements Annotator {

    private static final TextAttributesKey ELM_DEFINITION_NAME =
            createTextAttributesKey("ELM_DEFINITION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    private static final TextAttributesKey ELM_TYPE_ANNOTATION_NAME =
            createTextAttributesKey("ELM_TYPE_ANNOTATION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    private static final TextAttributesKey ELM_TYPE =
            createTextAttributesKey("ELM_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME);
    private static final TextAttributesKey ELM_TYPE_ANNOTATION_SIGNATURE_TYPES =
            createTextAttributesKey("ELM_TYPE_ANNOTATION_SIGNATURE_TYPES",
                    DefaultLanguageHighlighterColors.CLASS_REFERENCE);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Declarations) {
            highlightTopLevelValues((Declarations) element, holder);
        }
        if (element instanceof TypeAnnotationEnd) {
            highlightTypeAnnotations((TypeAnnotationEnd) element, holder);
        }
        if (element instanceof TypeDeclaration) {
            highlightTypeDeclaration((TypeDeclaration) element, holder);
        }
    }

    private void highlightTypeDeclaration(@NotNull TypeDeclaration element, @NotNull AnnotationHolder holder) {
        Stream.of(
                element.typeRefs(),
                element.constructors().map(TypeConstructor::getNameIdentifier),
                Stream.of(element.getNameIdentifier())
        ).flatMap(identity()).forEach(t -> highlight(t, holder, ELM_TYPE));

    }

    private void highlightTypeAnnotations(@NotNull TypeAnnotationEnd element,
                                          @NotNull AnnotationHolder holder) {
        stream(element.typeExpression())
                .flatMap(e -> Stream.concat(e.typeRefs(), e.typeVariables()))
                .forEach(t -> highlight(t, holder, ELM_TYPE_ANNOTATION_SIGNATURE_TYPES));
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
