package com.github.denofevil.aurelia

import com.intellij.lang.javascript.refactoring.JSRenameExtension
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import java.util.*

class RenameExtension : JSRenameExtension {
    companion object {
        private val CANDIDATE_EXTENSIONS = arrayOf("css", "scss", "less", "styl", "html", "spec.ts")
    }

    override fun getAdditionalFilesToRename(original: PsiElement,
                                            originalFile: PsiFile,
                                            newFileName: String): Map<PsiFile, String> {
        val componentVFile = originalFile.virtualFile
        val result = HashMap<PsiFile, String>()
        val dir = originalFile.parent
        if (dir != null && dir.isDirectory) {
            for (extension in CANDIDATE_EXTENSIONS) {
                val file = dir.findFile("${componentVFile.nameWithoutExtension}.$extension")
                if (file != null) {
                    result[file] = "$newFileName.$extension"
                }
            }
        }
        return result
    }

}