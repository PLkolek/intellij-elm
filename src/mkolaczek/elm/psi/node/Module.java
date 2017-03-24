package mkolaczek.elm.psi.node;


import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.extensions.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.intellij.psi.util.PsiTreeUtil.*;
import static mkolaczek.util.Streams.stream;

public class Module extends ElmNamedElement implements DocCommented {
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
        return super.getName() != null ? super.getName() : "Main";
    }

    @NotNull
    @Override
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.moduleName(getProject(), name);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getContainingFile().delete();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
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
        return findChildrenOfType(this, Import.class);
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

    public Stream<Declaration> declarations() {
        return findChildrenOfType(this, Declaration.class).stream();
    }

    public <T extends PsiNamedElement> Stream<T> declarations(TypeOfDeclaration<T, ?> typeOfDeclaration) {
        return findChildrenOfType(this, typeOfDeclaration.psiClass()).stream();
    }

    public <T extends PsiNamedElement> Stream<T> declarations(TypeOfDeclaration<T, ?> typeOfDeclaration, String name) {
        return findChildrenOfType(this, typeOfDeclaration.psiClass())
                .stream()
                .filter(decl -> name.equals(decl.getName()));
    }

    public Optional<Declarations> declarationsNode() {
        return Optional.ofNullable(getChildOfType(this, Declarations.class));
    }

    public <D extends PsiNamedElement> Stream<D> exportedDeclaration(TypeOfDeclaration<D, ? extends PsiNamedElement> typeOfDeclaration,
                                                                     String symbol) {
        boolean isExposed = exposed(typeOfDeclaration.exposedAs()).map(PsiNamedElement::getName)
                                                                  .filter(symbol::equals)
                                                                  .count() > 0;

        return isExposed || exposesEverything() ? declarations(typeOfDeclaration, symbol) : Stream.empty();

    }


    public boolean exposesEverything() {
        return header().flatMap(HasExposing::exposingList).map(ModuleValueList::isOpenListing).orElse(true);
    }

    //SHORTCUTS
    public <T extends PsiElement> Stream<T> exposed(TypeOfExposed<T> exposedElementsType) {
        return stream(header()).flatMap(h -> h.exposed(exposedElementsType));
    }
}
