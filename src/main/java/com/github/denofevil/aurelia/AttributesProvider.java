package com.github.denofevil.aurelia;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.XmlElementDescriptor;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dennis.Ushakov
 */
public class AttributesProvider implements XmlAttributeDescriptorsProvider {
    @Override
    public XmlAttributeDescriptor[] getAttributeDescriptors(XmlTag xmlTag) {
        return XmlAttributeDescriptor.EMPTY;
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
        return null;
    }
}
