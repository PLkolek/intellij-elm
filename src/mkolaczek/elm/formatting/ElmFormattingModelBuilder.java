package mkolaczek.elm.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import mkolaczek.elm.ElmLanguage;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                new ElmBlock(element.getNode(),
                        Wrap.createWrap(WrapType.NONE,
                                false),
                        Alignment.createAlignment(),
                        createSpaceBuilder(settings)),
                settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ElmLanguage.INSTANCE)
                .after(ElmTokenTypes.MODULE).spaces(1)
                .after(ElmTokenTypes.COMMA).spacing(1, 1, 0, false, 0)
                //remove all spacing to determine if the line should be wrapped
                .before(ElmTokenTypes.COMMA).spacing(0, 0, 0, false, 0)
                .before(ElmTokenTypes.RPAREN).spacing(0, 0, 0, false, 0);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
