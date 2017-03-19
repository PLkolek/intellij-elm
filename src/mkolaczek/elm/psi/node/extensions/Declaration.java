package mkolaczek.elm.psi.node.extensions;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.features.goTo.ItemPresentation;
import org.jetbrains.annotations.NotNull;

public abstract class Declaration extends ElmNamedElement implements DocCommented {
    public Declaration(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }
}
