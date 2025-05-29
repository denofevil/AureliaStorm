package com.github.denofevil.aurelia

import com.intellij.javascript.nodejs.PackageJsonData
import com.intellij.javascript.nodejs.packageJson.PackageJsonFileManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager

/**
 * Utility class and holder of aurelia syntax strings
 */
object Aurelia {
    val ICON = IconLoader.getIcon("/icons/aurelia-icon.svg", Aurelia::class.java)
    val HOOK_ICON = IconLoader.getIcon("/icons/aurelia-hook.svg", Aurelia::class.java)
    val ANNOTATION_ICON = IconLoader.getIcon("/icons/aurelia-annotation.svg", Aurelia::class.java)
    val OBSERVER_ICON = IconLoader.getIcon("/icons/aurelia-listener.svg", Aurelia::class.java)
    val ATTRIBUTE_ICON = IconLoader.getIcon("/icons/aurelia-attribute.svg", Aurelia::class.java)
    val CLASS_ICON = IconLoader.getIcon("/icons/aurelia-class.svg", Aurelia::class.java)
    val PROPERTY_ICON = IconLoader.getIcon("/icons/aurelia-property.svg", Aurelia::class.java)

    val BINDABLE_ANNOTATIONS = listOf("bindable")
    val OBSERVABLE_ANNOTATIONS = listOf("observable") + BINDABLE_ANNOTATIONS

    val PROPERTY_BINDING_DECLARATIONS = listOf("ref")
    val PROPERTY_BINDINGS = listOf("bind", "one-way", "two-way", "one-time", "from-view", "to-view")
    val EVENT_BINDINGS = listOf("trigger", "delegate", "call", "capture")
    val ATTRIBUTE_BINDING_SUGGESTIONS = PROPERTY_BINDINGS + EVENT_BINDINGS
    val INJECTABLE = listOf("for") + PROPERTY_BINDING_DECLARATIONS + PROPERTY_BINDINGS + EVENT_BINDINGS
    val REPEAT_FOR = listOf("repeat.for", "virtual-repeat.for", "reorderable-repeat.for")
    val LIFECYCLE_METHODS = listOf(
        // Aurelia 1
        "created", "bind", "unbind", "attached", "detached", "activate", "deactivate",
        // Aurelia 2
        "hydrating", "hydrated", "attaching", "attached",
        "detaching", "detached", "binding", "bound",
        "unbinding", "unbound"
    )

    object CustomAttribute {
        const val ANNOTATION = "customAttribute"
        const val CLASS_SUFFIX = "CustomAttribute"
    }

    object CustomElement {
        const val ANNOTATION = "customElement"
        const val CLASS_SUFFIX = "CustomElement"
    }

    val IMPORT_ELEMENTS = listOf("require", "import")
    const val IMPORT_ELEMENT_ATTRIBUTE = "from"
    val CUSTOM_ELEMENTS = listOf("let", "template") + IMPORT_ELEMENTS
    val WHITE_LIST_ATTRIBUTES = listOf(
        "name", "innerhtml", "containerless", "model", "element"
    )
    val ATTRIBUTES_WITHOUT_VALUE = listOf("else", "disabled", "containerless")
    val CUSTOM_ELEMENT_ATTRIBUTES = listOf("element.ref", "controller.ref", "view.ref", "view-model.ref", "component.ref")


    fun isPresentFor(project: Project): Boolean {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            val aureliaRootFolders = getAureliaRootFolders(project)
            CachedValueProvider.Result
                .create(
                    aureliaRootFolders.isNotEmpty(),
                    VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS,
                    ProjectRootModificationTracker.getInstance(project)
                )
        }
    }

    /**
     * Weather this element is child of any aurelia root folders
     */
    fun isFrameworkCandidate(element: PsiElement): Boolean {
        return isPresentFor(element.containingFile)
    }

    /**
     * Weather this element is child of any aurelia root folders
     */
    fun isPresentFor(element: PsiFile?): Boolean {
        if (element == null) return false
        return CachedValuesManager.getManager(element.project).getCachedValue(element) {
            var isPresent = false
            val project = element.project
            val aureliaRootFolders = getAureliaRootFolders(project)

            if (aureliaRootFolders.isNotEmpty()) {
                val fileVirtualFile: VirtualFile? = element.virtualFile ?: element.originalFile.virtualFile
                isPresent = fileVirtualFile != null && aureliaRootFolders.stream().anyMatch { isChildOf(fileVirtualFile, it) }
            }
            CachedValueProvider.Result.create(isPresent, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS)
        }
    }

    fun camelToKebabCase(camel: String): String {
        return camel.replace(Regex("([a-z])([A-Z])"), "$1-$2").lowercase()
    }

    private fun isChildOf(source: VirtualFile, target: VirtualFile): Boolean {
        var currentFile = source.parent
        while (currentFile != null) {
            if (currentFile == target) {
                return true
            }
            currentFile = currentFile.parent
        }
        return false
    }

    private fun getAureliaRootFolders(project: Project): ArrayList<VirtualFile> {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            val aureliaRootFolder = findAureliaRootFolders(project)
            CachedValueProvider.Result.create(aureliaRootFolder, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS)
        }
    }

    private fun findAureliaRootFolders(project: Project): ArrayList<VirtualFile> {
        val aureliaRoots: ArrayList<VirtualFile> = arrayListOf()
        for (packageJson in PackageJsonFileManager.getInstance(project).validPackageJsonFiles) {
            if (hasPackageJsonAureliaDependencies(packageJson)) {
                aureliaRoots.add(packageJson.parent)
            }
        }
        return aureliaRoots
    }

    private fun hasPackageJsonAureliaDependencies(virtualFile: VirtualFile): Boolean {
        val data = PackageJsonData.getOrCreate(virtualFile)
        return data.isDependencyOfAnyType("aurelia") ||
                data.isDependencyOfAnyType("aurelia-framework") ||
                data.isDependencyOfAnyType("aurelia-cli")
    }
}