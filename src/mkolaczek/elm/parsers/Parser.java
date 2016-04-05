package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;

import java.util.function.Function;


public interface Parser extends Function<PsiBuilder, Boolean> {
}

