package com.github.denofevil.aurelia

import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author Dennis.Ushakov
 */
object Aurelia {
    var INJECTABLE = arrayOf("bind", "one-way", "two-way", "one-time", "delegate", "trigger")
    var REPEAT_FOR = "repeat.for"
    var AURELIA_APP = "aurelia-app"

    fun present(project: Project): Boolean {
        val aureliaSpecificFileNames = listOf(
                "aurelia-framework.js", // Aurelia is included with with module loader (from jspm_packages when using JSPM) or module bundler (from node_modules when using Webpack)
                "aurelia-core.js" // project bootstrapped with script tag (contains aurelia-framework and other essential Aurelia modules)
        )
        for (aureliaSpecificFileName in aureliaSpecificFileNames) {
            if (FilenameIndex.getFilesByName(project, aureliaSpecificFileName, GlobalSearchScope.allScope(project)).isNotEmpty()) {
                return true;
            }
        }
        return false;
    }
}
