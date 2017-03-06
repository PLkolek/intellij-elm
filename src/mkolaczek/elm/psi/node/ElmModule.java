package mkolaczek.elm.psi.node;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.ElmIcon;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ElmModule extends ASTWrapperPsiElement implements PsiElement, PsiNamedElement, PsiNameIdentifierOwner {
    public ElmModule(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, ElmModuleName.class);
    }

    @Override
    @NotNull
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getText() : "Main"; //null means no header line
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            return nameIdentifier.replace(ElmElementFactory.moduleName(getProject(), name));
        }
        return this;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getContainingFile().delete();
    }

    @Override
    public int getTextOffset() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getTextOffset() : super.getTextOffset();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return ElmIcon.FILE;
            }
        };
    }

    public boolean sameName(String name) {
        return getName().equals(name);
    }

    public boolean sameName(ElmModule other) {
        return sameName(other.getName());
    }

    public ElmModuleValueList exposedValues() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleValueList.class);
    }

    public ElmDocComment docComment() {
        return PsiTreeUtil.getChildOfType(this, ElmDocComment.class);
    }

    public Optional<ElmModuleHeader> header() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, ElmModuleHeader.class));
    }

    public String typeStr() {
        Optional<IElementType> type = type();
        if (!type.isPresent()) {
            return "";
        }
        if (type.get() == Tokens.EFFECT) {
            return "effect";
        }
        if (type.get() == Tokens.PORT) {
            return "port";
        }
        throw new IllegalStateException("Unsupported type: " + type.get());
    }

    public Optional<IElementType> type() {
        return header().map(h -> h.getFirstChild().getNode().getElementType());
    }

    public Optional<EffectModuleSettingsList> settingsList() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, EffectModuleSettingsList.class));
    }

    @NotNull
    public static ElmModule module(PsiElement element) {
        return checkNotNull(PsiTreeUtil.getParentOfType(element, ElmModule.class));
    }

    public Collection<ElmImport2> imports() {
        return PsiTreeUtil.findChildrenOfType(this, ElmImport2.class);
    }

    public Collection<ElmTypeDeclaration> typeDeclarations() {
        return PsiTreeUtil.findChildrenOfType(this, ElmTypeDeclaration.class);
    }

    public Optional<ElmTypeDeclaration> typeDeclaration(String typeName) {
        return typeDeclarations().stream().filter(decl -> typeName.equals(decl.typeNameString().orElse(""))).findAny();
    }
}
