package mkolaczek.elm.parsers.core.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class Indentation {
    private final Deque<Integer> indentationLevels = new ArrayDeque<>();


    public static Indentation create() {
        return new Indentation();
    }

    public void push(int indent) {
        indentationLevels.push(indent);
    }

    public void pop() {
        indentationLevels.pop();
    }

    public int current() {
        return Optional.ofNullable(indentationLevels.peekLast()).orElse(0);
    }

    public boolean contains(int currentIndent) {
        return indentationLevels.contains(currentIndent);
    }
}
