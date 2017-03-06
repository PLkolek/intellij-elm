package mkolaczek.elm.refactoring;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmTypeConstructor;
import mkolaczek.elm.psi.node.ElmTypeName;

import java.util.regex.Pattern;

public class InplaceRenamer extends MemberInplaceRenamer {
    private static final Pattern MODULE_PATTERN = Pattern.compile("[A-Z][a-zA-Z0-9]*(\\.[A-Z][a-zA-Z0-9]*)*");
    private static final Pattern CAP_VAR_PATTERN = Pattern.compile("[A-Z][a-zA-Z0-9]*");

    public InplaceRenamer(PsiNamedElement elementToRename, PsiElement something, Editor editor) {
        super(elementToRename, something, editor);
        this.elementToRename = elementToRename;
    }

    private final PsiNamedElement elementToRename;

    @Override
    protected boolean isIdentifier(String newName, Language language) {
        if (elementToRename instanceof ElmModule) {
            return MODULE_PATTERN.matcher(newName).matches();
        }

        if (elementToRename instanceof ElmTypeName || elementToRename instanceof ElmTypeConstructor) {
            return CAP_VAR_PATTERN.matcher(newName).matches();
        }

        return super.isIdentifier(newName, language);
    }
}
