package mkolaczek.elm.features.syntaxHinglighting;

import com.google.common.collect.ImmutableMap;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ElmStringAnnotator implements Annotator {

    private static final Map<IElementType, String> tokenErrorMessages = ImmutableMap.of(
            Tokens.INVALID_EOL_IN_STRING, "Invalid end of line in single line string",
            StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN, "Invalid escape character",
            StringEscapesTokenTypes.INVALID_UNICODE_ESCAPE_TOKEN, "Invalid unicode escape characters"
    );

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();
        if (tokenErrorMessages.containsKey(elementType)) {
            holder.createErrorAnnotation(element, tokenErrorMessages.get(elementType));
        }
    }
}
