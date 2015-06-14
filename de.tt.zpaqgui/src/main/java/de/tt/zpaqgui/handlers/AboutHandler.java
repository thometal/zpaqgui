package de.tt.zpaqgui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;


public class AboutHandler {

    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {

        final MessageDialog dialog = new MessageDialog(shell, "About Dialog", null, "This is the About Dialog", MessageDialog.INFORMATION, new String[]{"OK"}, 0);

        dialog.open();
    }
}
