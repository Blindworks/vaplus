package de.vaplus.client.ui;

import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

public class VdlLoggerFactory extends ViewDeclarationLanguageFactory {

    private ViewDeclarationLanguageFactory wrapped;

    public VdlLoggerFactory(ViewDeclarationLanguageFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
        return new VdlLogger(wrapped.getViewDeclarationLanguage(viewId));
    }

    @Override
    public ViewDeclarationLanguageFactory getWrapped() {
        return wrapped;
    }

}