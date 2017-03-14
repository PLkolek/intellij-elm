package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ChopDefinition {
    private final ImmutableSet<IElementType> chopLocations;
    private final ImmutableSet<IElementType> toIndent;
    private final ImmutableSet<IElementType> toFlatten;


    private ChopDefinition(ImmutableSet<IElementType> chopLocations,
                           ImmutableSet<IElementType> toIndent,
                           ImmutableSet<IElementType> toFlatten) {
        this.chopLocations = chopLocations;
        this.toIndent = toIndent;
        this.toFlatten = toFlatten;
    }

    public static Builder chopOn(IElementType... elements) {
        return new Builder(ImmutableSet.copyOf(elements));
    }

    public static ChopDefinition surrounded(IElementType elementType) {
        if (elementType == RECORD_TYPE) {
            return surrounded(LBRACKET, RBRACKET);
        }
        if (elementType == TUPLE_TYPE) {
            return surrounded(LPAREN, RPAREN);
        }
        throw new IllegalArgumentException("Unsupported type " + elementType);
    }

    public static ChopDefinition surrounded(Token lparen, Token rparen) {
        return chopOn(lparen, COMMA, rparen).flatten(COMMA_SEP, SURROUND_CONTENTS).done();
    }

    public boolean shouldIndent(ASTNode node) {
        return toIndent.contains(node.getElementType());
    }

    public boolean shouldFlatten(ASTNode node) {
        return toFlatten.contains(node.getElementType());
    }

    public boolean shouldWrap(ASTNode node) {
        return chopLocations.contains(node.getElementType());
    }

    public static class Builder {
        private final ImmutableSet<IElementType> chopLocations;
        private ImmutableSet<IElementType> toIndent = ImmutableSet.of();
        private ImmutableSet<IElementType> toFlatten = ImmutableSet.of();

        private Builder(ImmutableSet<IElementType> chopLocations) {
            this.chopLocations = chopLocations;
        }

        public Builder indent(IElementType... elements) {
            this.toIndent = ImmutableSet.copyOf(elements);
            return this;
        }

        public Builder flatten(IElementType... elements) {
            this.toFlatten = ImmutableSet.copyOf(elements);
            return this;
        }

        public ChopDefinition done() {
            return new ChopDefinition(chopLocations, toIndent, toFlatten);
        }
    }
}
