package com.github.denofevil.aurelia;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author Dennis.Ushakov
 */
public class Aurelia {
    public static String[] INJECTABLE = new String[] {"bind", "one-way", "two-way", "one-time", "delegate", "trigger"};
    public static String REPEAT_FOR = "repeat.for";

    public static boolean present(Project project) {
        return FilenameIndex.getFilesByName(project, "aurelia-core.js", GlobalSearchScope.allScope(project)).length > 0;
    }
}
