package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.extensions.SeparatedList;

public class PipeSeparatedList extends SeparatedList {
    public PipeSeparatedList(ASTNode node) {
        super(node, Tokens.PIPE);
    }
}
