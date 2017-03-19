package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;

import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;

public class Expression {
    public static Parser definition() {
        //TODO: continue
        return sequence("definition",
                rootPattern());
    }

    private static Parser rootPattern() {
        return or(
                Basic.operator().ll2(),
                Pattern.term()
        );
    }
}
