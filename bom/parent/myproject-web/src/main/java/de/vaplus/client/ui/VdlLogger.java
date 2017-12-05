package de.vaplus.client.ui;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageWrapper;

public class VdlLogger extends ViewDeclarationLanguageWrapper {

//    private static final Logger logger = Logger.getLogger(VdlLoggerFactory.class.getName());

    private ViewDeclarationLanguage wrapped;

    public VdlLogger(ViewDeclarationLanguage wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        long start = System.nanoTime();
        UIViewRoot view = super.createView(context, viewId);
        long end = System.nanoTime();
        System.out.println(String.format("create %s: %.6fms", viewId, (end - start) / 1e6));
        return view;
    }

    @Override
    public void buildView(FacesContext context, UIViewRoot view) throws IOException {
        long start = System.nanoTime();
        super.buildView(context, view);
        long end = System.nanoTime();
        System.out.println(String.format("build %s: %.6fms", view.getViewId(), (end - start) / 1e6));
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot view) throws IOException {
        long start = System.nanoTime();
        super.renderView(context, view);
        long end = System.nanoTime();
        System.out.println(String.format("render %s: %.6fms", view.getViewId(), (end - start) / 1e6));
    }

    @Override
    public ViewDeclarationLanguage getWrapped() {
        return wrapped;
    }

}