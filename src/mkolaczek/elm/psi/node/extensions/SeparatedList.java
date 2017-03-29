package mkolaczek.elm.psi.node.extensions;

import com.google.common.collect.Lists;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.*;

public abstract class SeparatedList extends ASTWrapperPsiElement {
    private final Token separator;

    protected SeparatedList(@NotNull ASTNode node, Token separator) {
        super(node);
        this.separator = separator;
    }

    public <T extends PsiElement> Collection<T> values(Class<T> nodeType) {
        return PsiTreeUtil.findChildrenOfType(this, nodeType);
    }

    public void deleteSeparator(@NotNull PsiElement listedValue) {
        List<? extends PsiElement> values = Lists.newArrayList(values(listedValue.getClass()));
        int index = values.indexOf(listedValue);
        if (values.size() > 1) {
            PsiElement comma = index > 0 ? prevVisibleLeaf(listedValue) : nextVisibleLeaf(listedValue);
            PsiElement ws = index > 0 ? prevLeaf(listedValue) : nextLeaf(listedValue);
            if (comma != null && comma.getNode().getElementType() == separator) {
                if (ws instanceof PsiWhiteSpace) {
                    ws.delete();
                }
                comma.delete();
            }
        }
    }

}
