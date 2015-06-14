package de.tt.zpaqgui.handlers;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.dialogs.TestDialog;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.TestProgressRunnable;
import de.tt.zpaqgui.parts.ArchivePart;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;
import java.util.List;


public class TestHandler {

    @Execute
    public void execute(
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
            final ExecutionManager execmanger, final EModelService modelService,
            final MApplication application, final TestProgressRunnable progressrunnable) {

        final List<MPartStack> stacks = modelService.findElements(application, "de.tt.zpaqgui.partstack.archivestack",
                MPartStack.class, null);

        final MPart part = (MPart) stacks.get(0).getSelectedElement();

        if (part == null) {
            return;
        }

        try {
            final CMDLineConfig config = (CMDLineConfig) ((ArchivePart) part.getObject()).getConfig().clone();
            config.setZpaqCommand(Command.EXTRACT);
            config.setTest(true);

            final TestDialog dialog = new TestDialog(shell);
            dialog.setExecmanager(execmanger);
            dialog.setProgressrunnable(progressrunnable);
            dialog.setZPAQCMDLineConfig(config);
            dialog.open();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @CanExecute
    public boolean canExecute(ZPAQSettings settings, EModelService modelService, MApplication application) {
        final List<MPartStack> stacks = modelService.findElements(application, "de.tt.zpaqgui.partstack.archivestack",
                MPartStack.class, null);

        return settings.isZpaqLocationLoaded() && stacks.get(0).getSelectedElement() != null;
    }
}
