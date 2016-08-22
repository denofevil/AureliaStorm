package com.github.denofevil.aurelia

import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.lang.javascript.index.FrameworkIndexingHandler
import com.intellij.lang.javascript.psi.JSNamespaceImpl
import com.intellij.lang.javascript.psi.JSQualifiedNameImpl
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.lang.javascript.psi.resolve.BaseJSSymbolProcessor
import com.intellij.lang.javascript.psi.types.JSContext
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.FileContextUtil
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author Dennis.Ushakov
 */
class FrameworkHandler : FrameworkIndexingHandler() {
    override fun addContextType(info: BaseJSSymbolProcessor.TypeInfo, context: PsiElement) {
        val controller = findController(context) ?: return
        val namespace = JSQualifiedNameImpl.buildProvidedNamespace(controller)
        info.addType(JSNamespaceImpl(namespace, JSContext.INSTANCE, true), false)
    }

    override fun addContextNames(context: PsiElement, names: MutableList<String>) {
        val controller = findController(context) ?: return
        names.add(controller.getQualifiedName()!!)
    }

    private fun findController(context: PsiElement): JSClass<*>? {
        if (context !is JSReferenceExpression || context.qualifier != null) {
            return null
        }
        if (!Aurelia.present(context.getProject())) return null

        val original = CompletionUtil.getOriginalOrSelf<PsiElement>(context)
        val hostFile = FileContextUtil.getContextFile(if (original !== context) original else context.getContainingFile().originalFile) ?: return null

        val directory = hostFile.originalFile.parent ?: return null

        val controllerFile = directory.findFile(hostFile.virtualFile.nameWithoutExtension + ".ts") ?:
                directory.findFile(hostFile.virtualFile.nameWithoutExtension + ".js")

        return PsiTreeUtil.findChildOfType<JSClass<*>>(controllerFile, JSClass::class.java)
    }
}
