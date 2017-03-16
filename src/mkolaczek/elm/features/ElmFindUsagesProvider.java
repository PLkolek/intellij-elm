package mkolaczek.elm.features;

import com.google.common.base.Strings;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.lexer.ElmLexerAdapter;
import mkolaczek.elm.psi.node.*;
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
        return type(element);
    }

    @NotNull
    public static String type(@NotNull PsiElement element) {
        if (element instanceof Module || element instanceof ModuleNameRef) {
            return "module";
        } else if (element instanceof TypeDeclaration || element instanceof TypeNameRef) {
            return "type";
        } else if (element instanceof Operator) {
            return "operator";
        }
        return "type constructor";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        String prefix = "";
        Module module = element instanceof Module ? (Module) element : getParentOfType(element,
                Module.class);
        assert module != null;
        if (element instanceof TypeName) {
            prefix = module.getName() + ".";
        }
        if (element instanceof TypeConstructorName) {
            TypeDeclaration type = getParentOfType(element, TypeDeclaration.class);
            assert type != null;
            prefix = module.getName() + "." + type.getName() + " ";
        }

        return prefix + getNodeText(element, true);
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return Strings.nullToEmpty(((PsiNamedElement) element).getName());
    }
}
