package com.github.denofevil.aurelia;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.javascript.JavascriptLanguage;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl;
import com.intellij.psi.impl.source.xml.XmlTextImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dennis.Ushakov
 */
public class Injector implements MultiHostInjector {
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {
        if (!Aurelia.present(host.getProject())) return;

        TextRange range = ElementManipulators.getValueTextRange(host);
        if (host instanceof XmlAttributeValue) {
            PsiElement parent = host.getParent();
            if (parent instanceof XmlAttribute) {
                String name = ((XmlAttribute)parent).getName();
                for (String attr : Aurelia.INJECTABLE) {
                    if (name.endsWith("." + attr)) {
                        registrar.startInjecting(JavascriptLanguage.INSTANCE).
                                addPlace(null, null, (PsiLanguageInjectionHost) host,
                                        range).
                                doneInjecting();
                        return;
                    }
                }
            }
        }
        String text = ElementManipulators.getValueText(host);
        int start = text.indexOf("${");
        while (start >= 0) {
            int end = text.indexOf("}", start);
            end = end >= 0 ? end : range.getLength();
            registrar.startInjecting(JavascriptLanguage.INSTANCE).
                    addPlace(null, null, (PsiLanguageInjectionHost) host,
                            new TextRange(range.getStartOffset() + start + 2, range.getStartOffset() + end)).
                    doneInjecting();
            start = text.indexOf("${", end);
        }
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Arrays.asList(XmlTextImpl.class, XmlAttributeValueImpl.class);
    }
}
