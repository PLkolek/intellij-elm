package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;

import java.util.Collection;

import static mkolaczek.elm.parsers.core.SkipUntil.anyWillParse;
import static mkolaczek.elm.parsers.core.SkipUntil.willParseAfterSkipping;

public class Try implements Parser {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    public Try(Parser contents) {
        this.contents = contents;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean parse(PsiBuilder builder, Collection<Parser> myNextParsers) {
        if (willParse(builder)) {
            contents.parse(builder, myNextParsers);
        } else if (!anyWillParse(myNextParsers, builder)) {
            if (willParseAfterSkipping(this, myNextParsers, builder)) {
                SkipUntil.skipUntil(name(), Lists.newArrayList(this), builder);
                contents.parse(builder, myNextParsers);
            }
        }
        return true;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
        return contents.willParse(psiBuilder);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return contents.name();
    }
}
