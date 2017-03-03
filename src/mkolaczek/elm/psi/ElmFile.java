package mkolaczek.elm.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmFileType;
import mkolaczek.elm.ElmLanguage;
import mkolaczek.elm.psi.node.ElmImport2;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmModuleHeader;
import mkolaczek.elm.psi.node.ElmTypeDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

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

    public Collection<ElmImport2> imports() {
        return PsiTreeUtil.findChildrenOfType(this, ElmImport2.class);
    }

    public Optional<ElmModuleHeader> header() {
        return module().header();
    }

    public ElmModule module() {
        return PsiTreeUtil.findChildOfType(this, ElmModule.class);
    }

    public Collection<ElmTypeDeclaration> typeDeclarations() {
        return PsiTreeUtil.findChildrenOfType(this, ElmTypeDeclaration.class);
    }
}
