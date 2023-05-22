package com.github.denofevil.aurelia

import com.intellij.json.psi.JsonFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiManager

/**
 * @author Dennis.Ushakov
 */
object Aurelia {
    val ICON = IconLoader.getIcon("/icons/aurelia-icon.svg", Aurelia::class.java)

    val INJECTABLE = arrayOf("bind", "one-way", "two-way", "one-time", "delegate", "trigger", "call")
    const val REPEAT_FOR = "repeat.for"
    const val VIRTUAL_REPEAT_FOR = "virtual-repeat.for"
    const val AURELIA_APP = "aurelia-app"
    const val CASE = "case"
    const val REF = "ref"

    fun present(project: Project) = CachedValuesManager.getManager(project).getCachedValue(project) {
        val aureliaLoaded = hasDependency(project)
        CachedValueProvider.Result
            .create(aureliaLoaded, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS, ProjectRootModificationTracker.getInstance(project))
    }!!

    private fun hasDependency(project: Project): Boolean {
        var hasDependency = false

        VfsUtilCore.iterateChildrenRecursively(project.baseDir, null) { virtualFile ->
            if (!virtualFile.isDirectory && virtualFile.name == "package.json") {
                val jsonFile = PsiManager.getInstance(project).findFile(virtualFile)
                val jsonObject = (jsonFile as? JsonFile)?.topLevelValue as? JsonObject

                jsonObject?.findProperty("dependencies")?.let { dependenciesProp ->
                    val dependenciesObject = dependenciesProp.value as? JsonObject
                    val aureliaDependency = dependenciesObject?.findProperty("aurelia")
                    val aureliaCliDependency = dependenciesObject?.findProperty("aurelia-cli")
                    if (aureliaCliDependency != null || aureliaDependency != null) {
                        hasDependency = true
                        return@iterateChildrenRecursively false
                    }
                }
            }

            true
        }

        return hasDependency
    }
}
