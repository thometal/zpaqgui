package de.tt.zpaqgui.handlers;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.ListProgressRunnable;
import de.tt.zpaqgui.model.ArchiveModel;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;
import java.io.File;

public class OpenHandler {

    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
                        final ListProgressRunnable listrunnable, final ArchiveModel model,
                        final ExecutionManager execmanager) {

        final FileDialog dialog = new FileDialog(shell);
        dialog.setFilterExtensions(UIUtility.FILTER[0]);
        dialog.setFilterNames(UIUtility.FILTER[1]);
        final String selectedfile = dialog.open();

        if (selectedfile == null) {
            return;
        }

        final CMDLineConfig config = new CMDLineConfig();
        config.setArchive(new File(selectedfile));
        config.setZpaqCommand(Command.LIST);
        config.setModel(model);

        listrunnable.setConfig(config);

        execmanager.startBlocking(listrunnable);
    }

    @CanExecute
    public boolean canExecute(ZPAQSettings settings) {
        return settings.isZpaqLocationLoaded();
    }
}
