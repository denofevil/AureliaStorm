package com.github.denofevil.aurelia

import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author Dennis.Ushakov
 */
object Aurelia {
    var INJECTABLE = arrayOf("bind", "one-way", "two-way", "one-time", "delegate", "trigger", "call")
    var REPEAT_FOR = "repeat.for"
    var AURELIA_APP = "aurelia-app"

    val AURELIA_DETECTOR_FILES = listOf(
            // Aurelia is included with with module loader (from jspm_packages when using JSPM) or module bundler (from node_modules when using Webpack)
            "aurelia-framework.js",
            // project bootstrapped with script tag (contains aurelia-framework and other essential Aurelia modules)
            "aurelia-core.js"
    )

    fun present(project: Project) = AURELIA_DETECTOR_FILES.any {
        FilenameIndex.getFilesByName(project, it, GlobalSearchScope.allScope(project)).isNotEmpty()
    }
}
