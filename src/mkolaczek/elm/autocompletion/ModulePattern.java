package mkolaczek.elm.autocompletion;

import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.ElmModule;
import org.jetbrains.annotations.NotNull;

import static java.util.function.Predicate.isEqual;

public class ModulePattern extends PsiElementPattern<ElmModule, ModulePattern> {
    protected ModulePattern() {
        super(ElmModule.class);
    }

    public static ModulePattern module() {
        return new ModulePattern();
    }

    public ModulePattern effectModule() {
        return with(new PatternCondition<ElmModule>("effectModule") {
            @Override
            public boolean accepts(@NotNull ElmModule elmModule, ProcessingContext context) {
                return elmModule.type().filter(isEqual(Tokens.EFFECT)).isPresent();
            }
        });
    }
}
