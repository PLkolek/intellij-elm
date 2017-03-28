package mkolaczek.elm.parsers.core.context;

import mkolaczek.elm.psi.Element;

import java.util.ArrayDeque;
import java.util.Deque;

public class AsTypes {

    private final Deque<Element> asTypes = new ArrayDeque<>();

    public void push(Element as) {
        asTypes.push(as);
    }

    public Element pop() {
        return asTypes.pop();
    }
}
