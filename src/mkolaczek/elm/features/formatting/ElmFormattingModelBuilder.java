package mkolaczek.elm.features.formatting;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import mkolaczek.elm.boilerplate.ElmLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.formatting.FormattingModelProvider.createFormattingModelForPsiFile;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        ElmBlock block = ElmBlock.simple(element.getNode(), createSpaceBuilder(settings));
        return createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
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
                .after(TYPE).spacing(1, 1, 0, false, 0)
                .after(ALIAS).spacing(1, 1, 0, false, 0)
                .around(EQUALS).spacing(1, 1, 0, false, 0)
                .around(PIPE).spacing(1, 1, 0, false, 0)
                .after(MODULE_HEADER).spacing(0, 0, 2, false, 1)
                .after(IMPORTS).spacing(0, 0, 3, false, 2)
                .before(RECORD_TYPE).spacing(1, 1, 0, false, 0)
                .before(TUPLE_TYPE).spacing(1, 1, 0, false, 0)
                .before(TYPE_NAME_REF).spacing(1, 1, 0, false, 0)
                .before(LOW_VAR).spacing(1, 1, 0, false, 0)
                .before(TYPE_CONSTRUCTOR_ARGS).spacing(1, 1, 0, false, 0)

                //between comments
                .aroundInside(DOC_COMMENT, MODULE_NODE).spacing(0, 0, 2, false, 0)
                .between(MULTILINE_COMMENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(LINE_COMMENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT_CONTENT, MULTILINE_COMMENT).spacing(0, 0, 1, false, 0)
                .between(LINE_COMMENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(COMMENT_CONTENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(MULTILINE_COMMENT, DOC_COMMENT).spacing(0, 0, 1, false, 0)
                .between(MULTILINE_COMMENT, LINE_COMMENT).spacing(0, 0, 1, false, 0)
                .beforeInside(END_COMMENT, DOC_COMMENT).spacing(0, 0, 1, true, 99999999)
                //between comments and module
                .between(LINE_COMMENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)
                .between(COMMENT_CONTENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)
                .between(MULTILINE_COMMENT, MODULE_HEADER).spacing(0, 0, 1, false, 3)

                //module name
                .aroundInside(DOT, MODULE_NAME).spacing(0, 0, 0, false, 0)

                .afterInside(DOC_COMMENT, DECLARATIONS).spacing(0, 0, 1, false, 0)

                //operators
                .aroundInside(SYM_OP, OPERATOR).spacing(0, 0, 0, false, 0)

                .aroundInside(COMMA_OP, OPERATOR).spacing(0, 0, 0, false, 0)
                .around(OPEN_LISTING_NODE).spacing(0, 0, 0, false, 0)
                .between(IMPORT_LINE, IMPORT_LINE).spacing(0, 0, 1, false, 0)
                .around(AS).spacing(1, 1, 0, false, 0)


                //remove all spacing to determine if the line should be wrapped
                .before(EXPOSING_NODE).spacing(1, 1, 0, false, 0)
                .before(MODULE_VALUE_LIST).spacing(0, 0, 0, false, 0)
                .before(LPAREN).spacing(0, 0, 0, false, 0)
                .before(RPAREN).spacing(0, 0, 0, false, 0)
                .before(EFFECT_MODULE_SETTINGS).spacing(1, 1, 0, false, 0)
                .before(EFFECT_MODULE_SETTINGS_LIST).spacing(0, 0, 0, false, 0)
                .before(LBRACKET).spacing(0, 0, 0, false, 0)
                .before(RBRACKET).spacing(0, 0, 0, false, 0)
                .before(COMMA).spacing(0, 0, 0, false, 0)
                ;

    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
