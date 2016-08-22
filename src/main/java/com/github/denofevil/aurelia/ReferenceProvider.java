package com.github.denofevil.aurelia;

import com.intellij.lang.ecmascript6.resolve.ES6PsiUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dennis.Ushakov
 */
public class ReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        PsiElement parent = psiElement.getParent();
        if (psiElement instanceof XmlAttributeValue && parent instanceof XmlAttribute) {
            String attrName = ((XmlAttribute)parent).getName();
            if (attrName.startsWith("data-")) attrName = attrName.substring(5);
            if (Aurelia.AURELIA_APP.equals(attrName)) {
                String text = ElementManipulators.getValueText(psiElement);
                TextRange range = ElementManipulators.getValueTextRange(psiElement);
                return ES6PsiUtil.createImportExportFromClauseReferences(psiElement, range.getStartOffset(), text, this);
            }
        }
        return PsiReference.EMPTY_ARRAY;
    }
}
