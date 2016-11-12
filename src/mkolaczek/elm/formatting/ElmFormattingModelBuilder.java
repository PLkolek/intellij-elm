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
import mkolaczek.elm.psi.ElmElementTypes;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                new ElmBlock(element.getNode(), createSpaceBuilder(settings)), settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ElmLanguage.INSTANCE)
                .after(ElmTokenTypes.COMMA).spacing(1, 1, 0, false, 0)
                .after(ElmTokenTypes.EXPOSING).spacing(1, 1, 0, false, 0)
                .after(ElmTokenTypes.MODULE).spacing(1, 1, 0, false, 0)
                .after(ElmTokenTypes.IMPORT).spacing(1, 1, 0, false, 0)
                .before(ElmElementTypes.DOC_COMMENT).spacing(0, 0, 2, false, 0)
                .after(ElmElementTypes.DOC_COMMENT).spacing(0, 0, 2, false, 0)
                .beforeInside(ElmTokenTypes.END_COMMENT, ElmElementTypes.DOC_COMMENT).spacing(0, 0, 1, true, 99999999)
                .aroundInside(ElmTokenTypes.DOT, ElmElementTypes.MODULE_NAME).spacing(0, 0, 0, false, 0)
                .aroundInside(ElmTokenTypes.SYM_OP, ElmElementTypes.OPERATOR).spacing(0, 0, 0, false, 0)
                .aroundInside(ElmTokenTypes.COMMA_OP, ElmElementTypes.OPERATOR).spacing(0, 0, 0, false, 0)
                .around(ElmElementTypes.OPEN_LISTING_NODE).spacing(0, 0, 0, false, 0)
                .between(ElmElementTypes.IMPORT_2, ElmElementTypes.IMPORT_2).spacing(0, 0, 1, false, 0)
                .around(ElmTokenTypes.AS).spacing(1, 1, 0, false, 0)
                //remove all spacing to determine if the line should be wrapped
                .before(ElmElementTypes.EXPOSING_NODE).spacing(1, 1, 0, false, 0)
                .before(ElmTokenTypes.LPAREN).spacing(0, 0, 0, false, 0)
                .before(ElmTokenTypes.RPAREN).spacing(0, 0, 0, false, 0)
                .before(ElmTokenTypes.COMMA).spacing(0, 0, 0, false, 0)
                .before(ElmElementTypes.MODULE_VALUE_LIST).spacing(0, 0, 0, false, 0);

    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
