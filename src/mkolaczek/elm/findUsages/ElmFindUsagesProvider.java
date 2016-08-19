package mkolaczek.elm.findUsages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.ElmLexerAdapter;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.psi.ElmTokenTypes.COMMENT_TOKENS;
import static mkolaczek.elm.psi.ElmTokenTypes.KEY_TOKENS;

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
        return "module";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return getNodeText(element, true);
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return ((PsiNamedElement) element).getName();
    }
}
