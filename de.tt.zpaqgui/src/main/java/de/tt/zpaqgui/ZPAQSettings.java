package de.tt.zpaqgui;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

@Creatable
@Singleton
@SuppressWarnings("restriction")
public class ZPAQSettings {

    @Inject
    @Preference(nodePath = "de.tt.zpaqgui", value = "zpaqlocation")
    private String zpaqlocation;

    @Inject
    @Preference(nodePath = "de.tt.zpaqgui", value = "show_console")
    private boolean showconsole;


    public File getZpaqLocation() {
        return new File(zpaqlocation);
    }

    public boolean isZpaqLocationLoaded() {
        return zpaqlocation != null;
    }

    public boolean showConsole() {
        return showconsole;
    }
}
