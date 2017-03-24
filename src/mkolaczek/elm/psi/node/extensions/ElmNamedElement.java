package mkolaczek.elm.psi.node.extensions;

import com.google.common.collect.Sets;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ElmNamedElement extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {

    public ElmNamedElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getName() {
        return Optional.ofNullable(getNameIdentifier()).map(PsiElement::getText).orElse(null);
    }

    @NotNull
    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            if (nameIdentifier == this) {
                getNode().replaceAllChildrenToChildrenOf(createNewNameIdentifier(name).getNode());
            } else {
                nameIdentifier.replace(createNewNameIdentifier(name));
            }
        }
        return this;
    }

    @NotNull
    protected abstract PsiElement createNewNameIdentifier(@NonNls @NotNull String name);

    @Override
    public int getTextOffset() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null && nameIdentifier != this ? nameIdentifier.getTextOffset() : super.getTextOffset();
    }

    public boolean sameName(String name) {
        return getName() != null && getName().equals(name);
    }

    public boolean sameName(PsiNamedElement other) {
        return sameName(other.getName());
    }


    public static Predicate<PsiNamedElement> nameIn(Collection<String> names) {
        Set<String> nameSet = Sets.newHashSet(names);
        return e -> nameSet.contains(e.getName());
    }

    public static Predicate<PsiNamedElement> nameIn(Stream<? extends PsiNamedElement> elements) {
        Set<String> nameSet = elements.map(PsiNamedElement::getName).collect(Collectors.toSet());
        return e -> nameSet.contains(e.getName());
    }
}
