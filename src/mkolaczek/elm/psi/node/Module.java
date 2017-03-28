package mkolaczek.elm.psi.node;


import com.google.common.collect.Sets;
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
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.stream.Collectors.toSet;
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

    public Stream<Import> imports() {
        return findChildrenOfType(this, Import.class).stream();
    }

    public Optional<Imports> importsNode() {
        return Optional.ofNullable(findChildOfType(this, Imports.class));
    }

    public Stream<Import> imports(String name) {
        return imports().filter(i -> i.importedAs(name));
    }

    public Stream<Import> aliasedImports() {
        return imports().filter(Import::isAliased);
    }

    public Stream<Import> notAliasedImports() {
        return imports().filter(i -> !i.isAliased());
    }

    public Stream<Declaration> declarations() {
        return findChildrenOfType(this, Declaration.class).stream();
    }


    public <T extends PsiElement> Stream<T> declarations(TypeOfDeclaration<T, ?> typeOfDeclaration) {
        return Streams.stream(declarationsNode())
                      .flatMap(n -> {
                          T[] decls = getChildrenOfType(n, typeOfDeclaration.psiClass());
                          return decls != null ? Arrays.stream(decls) : Stream.empty();
                      });
    }

    public <T extends PsiNamedElement> Stream<T> declarations(TypeOfDeclaration<T, ?> typeOfDeclaration, String name) {
        return declarations(typeOfDeclaration, Sets.newHashSet(name));
    }

    public <T extends PsiNamedElement> Stream<T> declarations(TypeOfDeclaration<T, ?> typeOfDeclaration,
                                                              Set<String> names) {
        return declarations(typeOfDeclaration).filter(decl -> names.contains(decl.getName()));
    }

    public Stream<? extends ElmNamedElement> declaredValues() {
        return Stream.concat(
                declarations(TypeOfDeclaration.VALUE).flatMap(ValueDeclaration::topLevelValues),
                declarations(TypeOfDeclaration.PORT)
        );
    }

    public Optional<Declarations> declarationsNode() {
        return Optional.ofNullable(getChildOfType(this, Declarations.class));
    }

    public <D extends PsiNamedElement> Stream<D> exportedDeclaration(TypeOfDeclaration<D, ? extends Exposed> typeOfDeclaration,
                                                                     String symbol) {
        boolean isExposed = exposedNames(typeOfDeclaration.exposedAs())
                                                                  .filter(symbol::equals)
                                                                  .count() > 0;

        return isExposed || exposesEverything() ? declarations(typeOfDeclaration, symbol) : Stream.empty();

    }

    public boolean exposesEverything() {
        return header().flatMap(HasExposing::exposingList).map(ModuleValueList::isOpenListing).orElse(true);
    }

    public Stream<String> notExposed(TypeOfExposed<?> typeOfExposed,
                                     Function<Declaration, Stream<String>> valueExtractor) {
        Set<String> excluded = exposedNames(typeOfExposed).collect(toSet());
        return declarations()
                .flatMap(valueExtractor)
                .flatMap(Streams::stream)
                .filter(o -> !excluded.contains(o));
    }

    public Stream<String> exposedNames(TypeOfExposed<?> typeOfExposed) {
        return this.exposed(typeOfExposed).map(Exposed::exposedName);
    }

    //SHORTCUTS
    public <T extends Exposed> Stream<T> exposed(TypeOfExposed<T> exposedElementsType) {
        return stream(header()).flatMap(h -> h.exposed(exposedElementsType));
    }
}
