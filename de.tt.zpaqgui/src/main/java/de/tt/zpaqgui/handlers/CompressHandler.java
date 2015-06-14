package de.tt.zpaqgui.handlers;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.dialogs.CompressionsDialog;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.CompressionsProgressRunnable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;
import java.io.File;
import java.util.List;

public class CompressHandler {

    @Execute
    public void execute(
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
            @Named(IServiceConstants.ACTIVE_SELECTION) List<File> selection,
            final EModelService modelService, final MApplication application,
            final ExecutionManager execmanger, final CompressionsProgressRunnable progressrunnable) {

        final CMDLineConfig config = new CMDLineConfig();
        config.setSelectedFileEntries(selection);
        config.setZpaqCommand(Command.ADD);

        final CompressionsDialog dialog = new CompressionsDialog(shell);
        dialog.setExecmanager(execmanger);
        dialog.setProgressrunnable(progressrunnable);
        dialog.setZPAQCMDLineConfig(config);
        dialog.open();
    }

    @CanExecute
    public boolean canExecute(final ZPAQSettings settings, @Named(IServiceConstants.ACTIVE_SELECTION) List<File> selection) {
        return settings.isZpaqLocationLoaded() && !selection.isEmpty();
    }
}
