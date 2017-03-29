package mkolaczek.elm.features.refactoring.rename;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import mkolaczek.elm.lexer.ElmLexerAdapter;
import mkolaczek.elm.psi.node.*;

import java.util.regex.Pattern;

class InplaceRenamer extends MemberInplaceRenamer {
    private static final Pattern MODULE_PATTERN = Pattern.compile("[A-Z][a-zA-Z0-9]*(\\.[A-Z][a-zA-Z0-9]*)*");
    private static final Pattern CAP_VAR_PATTERN = Pattern.compile("[A-Z][a-zA-Z0-9]*");
    private static final Pattern LOW_VAR_PATTERN = Pattern.compile("[a-z][a-zA-Z0-9]*");

    public InplaceRenamer(PsiNamedElement elementToRename, PsiElement something, Editor editor) {
        super(elementToRename, something, editor);
        this.elementToRename = elementToRename;
    }

    private final PsiNamedElement elementToRename;

    @Override
    protected boolean isIdentifier(String newName, Language language) {
        if (elementToRename instanceof Module) {
            return MODULE_PATTERN.matcher(newName).matches();
        }

        if (elementToRename instanceof TypeDeclaration || elementToRename instanceof TypeConstructorName) {
            return CAP_VAR_PATTERN.matcher(newName).matches();
        }

        if (elementToRename instanceof OperatorDeclaration) {
            return !newName.isEmpty() && ElmLexerAdapter.isSymbol(newName);
        }

        if (elementToRename instanceof PortDeclaration || elementToRename instanceof ValueName) {
            return LOW_VAR_PATTERN.matcher(newName).matches();
        }

        return super.isIdentifier(newName, language);
    }

}
