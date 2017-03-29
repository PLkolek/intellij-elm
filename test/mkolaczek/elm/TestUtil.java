package mkolaczek.elm;


import com.google.common.collect.ObjectArrays;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TestUtil {
    public static String testDataPath(Class<?> testClass) {
        String packagePart = testClass.getPackage().getName().replace(".", "/").substring("mkolaczek.elm.".length());
        return "testdata/" + packagePart;
    }

    public static String testDataPath(Class<?> testClass, String subpackage) {
        return testDataPath(testClass) + "/" + subpackage;
    }

    public static String[] files(String dir, String... files) {
        return Arrays.stream(files).map(f -> dir + f).toArray(String[]::new);
    }

    @NotNull
    public static String[] withBuiltIn(String... completions) {
        return ObjectArrays.concat(completions, new String[]{
                "Basics.",
                "Debug.",
                "List.",
                "Maybe.",
                "Result.",
                "String.",
                "Tuple.",
                "Platform.",
                "Cmd.",
                "Sub."
        }, String.class);
    }
}
