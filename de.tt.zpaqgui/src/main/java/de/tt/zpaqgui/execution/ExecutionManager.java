package de.tt.zpaqgui.execution;

import de.tt.zpaqgui.execution.progressrunnables.ProgressRunnableBase;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;

@Creatable
@Singleton
public class ExecutionManager {

    @Inject
    private Shell shell;

    @Inject
    private UISynchronize sync;

    public void startBlocking(final ProgressRunnableBase runnable) {
        try {
            new ProgressMonitorDialog(shell).run(true, true, runnable);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            runnable.destroy();
        }
    }

    public void startNonBlocking(final ProgressRunnableBase runnable) {
        sync.asyncExec(() -> {
            try {
                new ProgressMonitorDialog(shell).run(true, true, runnable);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                runnable.destroy();
            }
        });
    }
}
