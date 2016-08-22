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

    fun present(project: Project) = FilenameIndex
            .getFilesByName(project, "aurelia-core.js", GlobalSearchScope.allScope(project))
            .isNotEmpty()
}
