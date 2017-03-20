package mkolaczek.elm.features.syntaxHinglighting;

import com.google.common.collect.ImmutableMap;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.CharacterLiteral;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static mkolaczek.elm.psi.Tokens.*;

public class ElmStringAnnotator implements Annotator {

    private static final Map<IElementType, String> tokenErrorMessages = ImmutableMap.of(
            INVALID_CHARACTER_ESCAPE_TOKEN, "Invalid escape character",
            INVALID_UNICODE_ESCAPE_TOKEN, "Invalid unicode escape characters"
    );

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();
        if (element instanceof CharacterLiteral && !((CharacterLiteral) element).isLengthValid()) {
            holder.createErrorAnnotation(element, "Length of character literal is invalid");
        }
        if (elementType == INVALID_EOL_IN_STRING) {
            holder.createErrorAnnotation(element.getParent(), "Invalid end of line in character literal");
        }
        if (tokenErrorMessages.containsKey(elementType)) {
            holder.createErrorAnnotation(element.getParent(), tokenErrorMessages.get(elementType));
        }
    }
}
