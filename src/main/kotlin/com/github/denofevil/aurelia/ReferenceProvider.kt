package com.github.denofevil.aurelia

import com.intellij.lang.ecmascript6.resolve.JSFileReferencesUtil
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext

/**
 * @author Dennis.Ushakov
 */
class ReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(psiElement: PsiElement, processingContext: ProcessingContext): Array<PsiReference> {
        val parent = psiElement.parent
        if (psiElement is XmlAttributeValue && parent is XmlAttribute) {
            val attrName = parent.name.removePrefix("data-")
            if (Aurelia.AURELIA_APP == attrName) {
                val text = ElementManipulators.getValueText(psiElement)
                val range = ElementManipulators.getValueTextRange(psiElement)
                return JSFileReferencesUtil.createImportExportFromClauseReferences(psiElement, range.startOffset, text, this)
            }
        }
        return PsiReference.EMPTY_ARRAY
    }
}
