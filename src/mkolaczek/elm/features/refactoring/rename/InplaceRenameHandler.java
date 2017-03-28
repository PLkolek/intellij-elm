package mkolaczek.elm.features.refactoring.rename;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenameHandler;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import mkolaczek.elm.boilerplate.ElmLanguage;
import org.jetbrains.annotations.NotNull;

public class InplaceRenameHandler extends MemberInplaceRenameHandler {


    @NotNull
    @Override
    protected MemberInplaceRenamer createMemberRenamer(@NotNull PsiElement element,
                                                       PsiNameIdentifierOwner elementToRename,
                                                       Editor editor) {
        if (elementToRename.getLanguage() == ElmLanguage.INSTANCE) {
            return new InplaceRenamer(elementToRename, element, editor);
        }
        return super.createMemberRenamer(element, elementToRename, editor);
    }
}
