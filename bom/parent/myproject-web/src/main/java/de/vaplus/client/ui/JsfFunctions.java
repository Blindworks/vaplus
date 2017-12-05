package de.vaplus.client.ui;

public final class JsfFunctions {
    private JsfFunctions() {}

    public static String nl2br(String string) {
        return (string != null) ? string.replace("\n", "<br/>") : null;
    }
}
