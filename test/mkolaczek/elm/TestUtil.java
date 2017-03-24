package mkolaczek.elm;


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
}
