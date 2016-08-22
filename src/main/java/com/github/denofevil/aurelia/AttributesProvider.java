package com.github.denofevil.aurelia;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dennis.Ushakov
 */
public class AttributesProvider implements XmlAttributeDescriptorsProvider {
    @Override
    public XmlAttributeDescriptor[] getAttributeDescriptors(XmlTag xmlTag) {
        return new XmlAttributeDescriptor[] {new AttributeDescriptor(Aurelia.REPEAT_FOR)};
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String name, XmlTag xmlTag) {
        for (String attr : Aurelia.INJECTABLE) {
            if (name.endsWith("." + attr)) {
                XmlElementDescriptor descriptor = xmlTag.getDescriptor();
                if (descriptor != null) {
                    String attrName = name.substring(0, name.length() - attr.length() - 1);
                    XmlAttributeDescriptor attributeDescriptor = descriptor.getAttributeDescriptor(attrName, xmlTag);
                    return attributeDescriptor != null ? attributeDescriptor :
                           descriptor.getAttributeDescriptor("on" + attrName, xmlTag);
                }
            }
        }
        if (Aurelia.REPEAT_FOR.equals(name)) {
            return new AttributeDescriptor(name);
        }
        return null;
    }

    private static class AttributeDescriptor extends BasicXmlAttributeDescriptor {
        private final String name;

        public AttributeDescriptor(String name) {
            this.name = name;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public boolean hasIdType() {
            return false;
        }

        @Override
        public boolean hasIdRefType() {
            return false;
        }

        @Override
        public boolean isEnumerated() {
            return false;
        }

        @Override
        public PsiElement getDeclaration() {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void init(PsiElement psiElement) {

        }

        @Override
        public Object[] getDependences() {
            return ArrayUtil.EMPTY_OBJECT_ARRAY;
        }

        @Override
        public boolean isFixed() {
            return false;
        }

        @Override
        public String getDefaultValue() {
            return null;
        }

        @Override
        public String[] getEnumeratedValues() {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
    }
}
