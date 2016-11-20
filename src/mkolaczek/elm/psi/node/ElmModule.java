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
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

public class ElmModule extends ASTWrapperPsiElement implements PsiElement, PsiNamedElement, PsiNameIdentifierOwner {
    public ElmModule(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleName.class);
    }

    @Override
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
        return getName() != null && getName().equals(name);
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

    public ElmModuleHeader header() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleHeader.class);
    }

    public String type() {
        IElementType type = header().getFirstChild().getNode().getElementType();
        if (type == ElmTokenTypes.EFFECT) {
            return "effect";
        }
        if (type == ElmTokenTypes.PORT) {
            return "port";
        }
        return "";
    }

    public Optional<EffectModuleSettingsList> settingsList() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, EffectModuleSettingsList.class));
    }
}
