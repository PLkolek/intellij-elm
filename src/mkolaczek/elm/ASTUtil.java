package mkolaczek.elm;

import com.intellij.lang.ASTNode;

import static com.intellij.psi.TokenType.ERROR_ELEMENT;
import static com.intellij.psi.TokenType.WHITE_SPACE;

public class ASTUtil {

    public static ASTNode nextSignificant(ASTNode node) {
        node = node.getTreeNext();
        while (!isSignificant(node)) {
            node = node.getTreeNext();
        }
        return node;
    }

    private static boolean isSignificant(ASTNode node) {
        return node == null || (node.getElementType() != WHITE_SPACE && node.getElementType() != ERROR_ELEMENT);
    }

    public static ASTNode firstSignificantChild(ASTNode node) {
        ASTNode child = node.getFirstChildNode();
        return isSignificant(child) ? child : nextSignificant(child);
    }
}
