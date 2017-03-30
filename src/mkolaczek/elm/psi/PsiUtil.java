package mkolaczek.elm.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleHeader;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType;

public class PsiUtil {

    public static boolean insideModuleHeader(PsiElement target) {
        return PsiTreeUtil.getParentOfType(target, ModuleHeader.class) != null;
    }

    public static Import containingImport(PsiElement target) {
        return PsiTreeUtil.getParentOfType(target, Import.class);
    }

    public static boolean insideImport(PsiElement target) {
        return containingImport(target) != null;
    }


    public static <T extends PsiElement> Stream<T> getChildrenOfType2(PsiElement element, Class<T> aClass) {
        return StreamEx.ofNullable(getChildrenOfType(element, aClass)).flatMap(Arrays::stream);
    }
}
