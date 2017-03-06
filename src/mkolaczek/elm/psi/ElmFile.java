package mkolaczek.elm.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmFileType;
import mkolaczek.elm.ElmLanguage;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.NotNull;

public class ElmFile extends PsiFileBase {
    public ElmFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ElmLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ElmFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simple File";
    }

    public Module module() {
        return PsiTreeUtil.findChildOfType(this, Module.class);
    }
}
