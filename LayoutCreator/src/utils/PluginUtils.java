package utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class PluginUtils {

    /**
     * 获取Java文件的Class类对象
     */
    public static PsiClass getFileClass(PsiFile file) {
        for (PsiElement psiElement : file.getChildren()) {
            if (psiElement instanceof PsiClass) {
                return (PsiClass) psiElement;
            }
        }
        return null;
    }

}
