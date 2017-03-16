package mkolaczek.elm.psi.node;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.extensions.DocCommented;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static mkolaczek.util.Streams.stream;

public class Module extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, DocCommented {
    public Module(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return findChildOfType(this, ModuleName.class);
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
        return new ItemPresentation(this);
    }

    public boolean sameName(String name) {
        return getName().equals(name);
    }

    public boolean sameName(Module other) {
        return sameName(other.getName());
    }

    @Override
    public Optional<DocComment> docComment() {
        return Optional.ofNullable(getChildOfType(this, DocComment.class));
    }

    public Optional<ModuleHeader> header() {
        return Optional.ofNullable(findChildOfType(this, ModuleHeader.class));
    }

    public String typeStr() {
        Optional<IElementType> type = type();
        if (!type.isPresent() || type.get() == Tokens.MODULE) {
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
        return Optional.ofNullable(findChildOfType(this, EffectModuleSettingsList.class));
    }

    @NotNull
    public static Module module(PsiElement element) {
        return checkNotNull(PsiTreeUtil.getParentOfType(element, Module.class));
    }

    public Collection<Import> imports() {
        return PsiTreeUtil.findChildrenOfType(this, Import.class);
    }

    public Optional<Imports> importsNode() {
        return Optional.ofNullable(findChildOfType(this, Imports.class));
    }

    public Stream<Import> imports(String name) {
        return imports().stream().filter(i -> i.importedAs(name));
    }

    public Stream<Import> aliasedImports() {
        return imports().stream().filter(Import::isAliased);
    }

    public Stream<Import> notAliasedImports() {
        return imports().stream().filter(i -> !i.isAliased());
    }

    public Stream<TypeDeclaration> typeDeclarations() {
        return PsiTreeUtil.findChildrenOfType(this, TypeDeclaration.class).stream();
    }

    public Optional<TypeDeclaration> typeDeclaration(String typeName) {
        return typeDeclarations().filter(decl -> typeName.equals(decl.getName())).findAny();
    }

    public Stream<InfixOperatorDeclaration> operatorDeclarations() {
        return PsiTreeUtil.findChildrenOfType(this, InfixOperatorDeclaration.class).stream();
    }

    public Optional<Declarations> declarationsNode() {
        return Optional.ofNullable(getChildOfType(this, Declarations.class));
    }


    //SHORTCUTS
    public Stream<TypeExport> typeExports() {
        return stream(header()).flatMap(ModuleHeader::typeExports);
    }

    public Stream<OperatorSymbol> operatorSymbolExports() {
        return stream(header()).flatMap(ModuleHeader::operatorExports)
                               .map(Operator::symbol)
                               .flatMap(Streams::stream);
    }
}
