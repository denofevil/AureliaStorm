package com.github.denofevil.aurelia.cli

import com.github.denofevil.aurelia.Aurelia
import com.intellij.execution.filters.Filter
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.javascript.nodejs.util.NodePackage
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.module.WebModuleBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectTemplate
import com.intellij.platform.ProjectTemplatesFactory
import java.io.File
import javax.swing.Icon

class ProjectGenerator : NpmPackageProjectGenerator() {
    override fun presentablePackageName() = "Aurelia &CLI:"

    override fun getName() = "Aurelia"

    override fun packageName() = "aurelia-cli"

    override fun getDescription() =
        "Create a new <a href='https://aurelia.io'>Aurelia</a> project using <a href='https://aurelia.io/docs/cli/'>Aurelia CLI</a>."

    override fun getIcon(): Icon {
        return Aurelia.ICON
    }

    override fun executable(pkg: NodePackage): String {
        return pkg.systemDependentPath + File.separator + "bin" + File.separator + "aurelia-cli.js"
    }

    override fun getNpxCommands(): List<NpxPackageDescriptor.NpxCommand> {
        return listOf(NpxPackageDescriptor.NpxCommand(packageName(), "au"))
    }

    override fun generatorArgs(project: Project, dir: VirtualFile, settings: Settings): Array<String> {
        return arrayOf("new", project.name, "--here", "--unattended", "-i", "npm")
    }

    override fun customizeModule(baseDir: VirtualFile, entry: ContentEntry?) {}
    override fun filters(project: Project, baseDir: VirtualFile): Array<Filter> = emptyArray()
    override fun generatorArgs(project: Project, baseDir: VirtualFile): Array<String> = emptyArray()
}

class ProjectTemplateFactory : ProjectTemplatesFactory() {
    override fun getGroups(): Array<String> = arrayOf(WebModuleBuilder.GROUP_NAME)

    override fun createTemplates(group: String?, context: WizardContext): Array<ProjectTemplate> {
        return arrayOf(ProjectGenerator())
    }
}