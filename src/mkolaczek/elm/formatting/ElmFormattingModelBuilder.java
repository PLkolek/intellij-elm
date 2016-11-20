package mkolaczek.elm.formatting;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import mkolaczek.elm.ElmLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mkolaczek.elm.psi.ElmElementTypes.*;
import static mkolaczek.elm.psi.ElmTokenTypes.*;

public class ElmFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                new ElmBlock(element.getNode(), createSpaceBuilder(settings)), settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ElmLanguage.INSTANCE)
                .after(COMMA).spacing(1, 1, 0, false, 0)
                .after(EXPOSING).spacing(1, 1, 0, false, 0)
                .after(WHERE).spacing(1, 1, 0, false, 0)
                .after(PORT).spacing(1, 1, 0, false, 0)
                .after(EFFECT).spacing(1, 1, 0, false, 0)
                .after(MODULE).spacing(1, 1, 0, false, 0)
                .after(IMPORT).spacing(1, 1, 0, false, 0)
                .around(EQUALS).spacing(1, 1, 0, false, 0)

                //between comments
                .before(DOC_COMMENT).spacing(0, 0, 2, false, 0)
                .after(DOC_COMMENT).spacing(0, 0, 2, false, 0)
                .between(MULTILINE_COMMENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT_CONTENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT_CONTENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(MULTILINE_COMMENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(MULTILINE_COMMENT, COMMENT).spacing(0, 0, 1, false, 0)
                .beforeInside(END_COMMENT, DOC_COMMENT).spacing(0, 0, 1, true, 99999999)
                //between comments and module
                .between(COMMENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)
                .between(COMMENT_CONTENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)
                .between(MULTILINE_COMMENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)

                //module name
                .aroundInside(DOT, MODULE_NAME).spacing(0, 0, 0, false, 0)

                //operators
                .aroundInside(SYM_OP, OPERATOR).spacing(0, 0, 0, false, 0)

                .aroundInside(COMMA_OP, OPERATOR).spacing(0, 0, 0, false, 0)
                .around(OPEN_LISTING_NODE).spacing(0, 0, 0, false, 0)
                .between(IMPORT_2, IMPORT_2).spacing(0, 0, 1, false, 0)
                .around(AS).spacing(1, 1, 0, false, 0)
                .after(LBRACKET).spacing(1, 1, 0, false, 0)
                .before(RBRACKET).spacing(1, 1, 0, false, 0)
                //remove all spacing to determine if the line should be wrapped
                .before(EXPOSING_NODE).spacing(1, 1, 0, false, 0)
                .before(MODULE_VALUE_LIST).spacing(0, 0, 0, false, 0)
                .before(LPAREN).spacing(0, 0, 0, false, 0)
                .before(RPAREN).spacing(0, 0, 0, false, 0)
                .before(EFFECT_MODULE_SETTINGS).spacing(1, 1, 0, false, 0)
                .before(EFFECT_MODULE_SETTINGS_LIST).spacing(0, 0, 0, false, 0)
                .before(LBRACKET).spacing(0, 0, 0, false, 0)
                .before(RBRACKET).spacing(0, 0, 0, false, 0)
                .before(COMMA).spacing(0, 0, 0, false, 0);

    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
