package com.github.denofevil.aurelia

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.impl.source.xml.XmlTextImpl
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElementType
import com.intellij.psi.xml.XmlTokenType
import java.util.*

/**
 * @author Dennis.Ushakov
 */
class Injector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, host: PsiElement) {
        if (!Aurelia.present(host.project)) return

        val range = ElementManipulators.getValueTextRange(host)
        if (host is XmlAttributeValue) {
            val parent = host.getParent()
            if (parent is XmlAttribute) {
                val name = parent.name
                for (attr in Aurelia.INJECTABLE) {
                    if (name.endsWith("." + attr)) {
                        registrar.startInjecting(JavascriptLanguage.INSTANCE)
                                .addPlace(null, null, host as PsiLanguageInjectionHost, range)
                                .doneInjecting()
                        return
                    }
                }
            }
        }
        val text = ElementManipulators.getValueText(host)
        var start = text.indexOf("\${")
        while (start >= 0) {
            var end = text.indexOf("}", start)
            end = if (end >= 0) end else range.length
            var injectionCandidate = host.findElementAt(start)
            while (injectionCandidate is PsiWhiteSpace) injectionCandidate = injectionCandidate.getNextSibling()

            if (injectionCandidate != null &&
                    injectionCandidate.startOffsetInParent <= end &&
                    !XmlTokenType.COMMENTS.contains(injectionCandidate.node.elementType) &&
                    injectionCandidate.node.elementType !== XmlElementType.XML_COMMENT &&
                    injectionCandidate !is OuterLanguageElement) {

                registrar.startInjecting(JavascriptLanguage.INSTANCE)
                        .addPlace(null, null, host as PsiLanguageInjectionHost,
                                TextRange(range.startOffset + start + 2, range.startOffset + end))
                        .doneInjecting()
            }
            start = text.indexOf("\${", end)
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return Arrays.asList(XmlTextImpl::class.java, XmlAttributeValueImpl::class.java)
    }
}
