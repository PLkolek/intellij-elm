package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.abstr.SeparatedList;

public class CommaSeparatedList extends SeparatedList {
    public CommaSeparatedList(ASTNode node) {
        super(node, Tokens.COMMA);
    }
}
