package com.github.denofevil.aurelia

import com.intellij.codeInspection.InspectionProfileEntry
import com.intellij.lang.javascript.inspections.JSInspectionSuppressor
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.FileContextUtil
import com.sixrr.inspectjs.validity.BadExpressionStatementJSInspection

class InspectionSuppressor : JSInspectionSuppressor() {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId == InspectionProfileEntry.getShortName(BadExpressionStatementJSInspection::class.java.simpleName) &&
            Aurelia.present(element.project) &&
            FileContextUtil.getContextFile(element) != element.containingFile) {
            return true
        }
        return super.isSuppressedFor(element, toolId)
    }
}