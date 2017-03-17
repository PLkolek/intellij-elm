package mkolaczek.elm;


public class TestUtil {
    public static String testDataPath(Class<?> testClass) {
        String packagePart = testClass.getPackage().getName().replace(".", "/").substring("mkolaczek.elm.".length());
        return "testdata/" + packagePart;
    }

    public static String testDataPath(Class<?> testClass, String subpackage) {
        return testDataPath(testClass) + "/" + subpackage;
    }
}
