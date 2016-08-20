package com.github.denofevil.aurelia;

import com.intellij.codeInsight.completion.CompletionUtil;
import com.intellij.lang.javascript.index.FrameworkIndexingHandler;
import com.intellij.lang.javascript.psi.JSNamespaceImpl;
import com.intellij.lang.javascript.psi.JSQualifiedName;
import com.intellij.lang.javascript.psi.JSQualifiedNameImpl;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.lang.javascript.psi.ecmal4.JSClass;
import com.intellij.lang.javascript.psi.resolve.BaseJSSymbolProcessor;
import com.intellij.lang.javascript.psi.types.JSContext;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

/**
 * @author Dennis.Ushakov
 */
public class FrameworkHandler extends FrameworkIndexingHandler {
    @Override
    public void addContextType(BaseJSSymbolProcessor.TypeInfo info, PsiElement context) {
        JSClass controller = findController(context);
        if (controller != null) {
            JSQualifiedName namespace = JSQualifiedNameImpl.buildProvidedNamespace(controller);
            info.addType(new JSNamespaceImpl(namespace, JSContext.INSTANCE, true), false);
        }
    }

    @Override
    public void addContextNames(PsiElement context, List<String> names) {
        JSClass controller = findController(context);
        if (controller != null) {
            names.add(controller.getQualifiedName());
        }
    }

    private JSClass findController(PsiElement context) {
        if (!(context instanceof JSReferenceExpression) || ((JSReferenceExpression)context).getQualifier() != null) {
            return null;
        }
        if (!Aurelia.present(context.getProject())) return null;

        final PsiElement original = CompletionUtil.getOriginalOrSelf(context);
        PsiFile hostFile = FileContextUtil.getContextFile(original != context ? original : context.getContainingFile().getOriginalFile());
        if (hostFile == null) return null;

        PsiDirectory directory = hostFile.getOriginalFile().getParent();
        if (directory == null) return null;

        PsiFile controllerFile = directory.findFile(hostFile.getVirtualFile().getNameWithoutExtension() + ".ts");
        controllerFile = controllerFile == null ?
                         directory.findFile(hostFile.getVirtualFile().getNameWithoutExtension() + ".js") :
                         controllerFile;

        return PsiTreeUtil.findChildOfType(controllerFile, JSClass.class);
    }
}
