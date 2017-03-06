package mkolaczek.elm.findUsages;

import com.google.common.base.Strings;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.ElmLexerAdapter;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmTypeConstructor;
import mkolaczek.elm.psi.node.ElmTypeDeclaration;
import mkolaczek.elm.psi.node.ElmTypeName;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static mkolaczek.elm.psi.Tokens.COMMENT_TOKENS;
import static mkolaczek.elm.psi.Tokens.KEY_TOKENS;

public class ElmFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new ElmLexerAdapter(), KEY_TOKENS, COMMENT_TOKENS, TokenSet.EMPTY);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return com.intellij.lang.HelpID.FIND_OTHER_USAGES;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof ElmModule) {
            return "module";
        } else if (element instanceof ElmTypeName) {
            return "type";
        }
        return "type constructor";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        String prefix = "";
        ElmModule module = getParentOfType(element, ElmModule.class);
        assert module != null;
        if (element instanceof ElmTypeName) {
            prefix = module.getName() + ".";
        }
        if (element instanceof ElmTypeConstructor) {
            ElmTypeDeclaration type = getParentOfType(element, ElmTypeDeclaration.class);
            assert type != null;
            prefix = module.getName() + "." + type.typeNameString().orElse("") + " ";
        }

        return prefix + getNodeText(element, true);
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return Strings.nullToEmpty(((PsiNamedElement) element).getName());
    }
}
