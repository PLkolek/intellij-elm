package mkolaczek.elm.autocompletion;

import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.NotNull;

import static java.util.function.Predicate.isEqual;

public class ModulePattern extends PsiElementPattern<Module, ModulePattern> {
    protected ModulePattern() {
        super(Module.class);
    }

    public static ModulePattern module() {
        return new ModulePattern();
    }

    public ModulePattern effectModule() {
        return with(new PatternCondition<Module>("effectModule") {
            @Override
            public boolean accepts(@NotNull Module module, ProcessingContext context) {
                return module.type().filter(isEqual(Tokens.EFFECT)).isPresent();
            }
        });
    }
}
