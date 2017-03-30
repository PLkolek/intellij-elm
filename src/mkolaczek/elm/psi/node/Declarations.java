package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType;

public class Declarations extends ASTWrapperPsiElement {

    public Declarations(ASTNode node) {
        super(node);
    }

    public Stream<Declaration> declarations() {
        return StreamEx.ofNullable(getChildrenOfType(this, Declaration.class)).flatMap(Arrays::stream);
    }
}
