package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.faultTolerant.ModuleDeclaration;
import org.jetbrains.annotations.NotNull;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        new ModuleDeclaration().parse(builder);
    }

}
