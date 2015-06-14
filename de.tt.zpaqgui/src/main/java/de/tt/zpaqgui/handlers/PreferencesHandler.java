package de.tt.zpaqgui.handlers;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.dialogs.PreferencesDialog;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;

@SuppressWarnings("restriction")
public class PreferencesHandler {

    @Execute
    public void execute(@Preference(nodePath = "de.tt.zpaqgui") IEclipsePreferences prefs, UISynchronize sync,
                        @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, ZPAQSettings settings) {

        final PreferencesDialog dialog = new PreferencesDialog(shell);
        dialog.setPreferences(prefs);
        dialog.setUISynchronize(sync);
        dialog.setSettings(settings);
        dialog.open();
    }
}
