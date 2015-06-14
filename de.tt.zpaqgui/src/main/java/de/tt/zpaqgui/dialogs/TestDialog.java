package de.tt.zpaqgui.dialogs;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.TestProgressRunnable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.inject.Inject;


public class TestDialog extends Dialog {

    private ExecutionManager execmanager;

    private TestProgressRunnable progressrunnable;

    private CMDLineConfig config;
    private Combo combo_threads;
    private Combo combo_untilversion;

    @Inject
    public TestDialog(Shell parentShell) {
        super(parentShell);
    }

    public void setZPAQCMDLineConfig(CMDLineConfig config) {
        this.config = config;
        progressrunnable.setConfig(config);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(4, false));

        final Label label_threads = new Label(container, SWT.NONE);
        label_threads.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        label_threads.setText("Threads to use:");

        combo_threads = new Combo(container, SWT.NONE);
        combo_threads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));

        final String[] threads = UIUtility.getThreadCountsArray();

        combo_threads.setItems(threads);
        combo_threads.select(threads.length - 1);

        final Label label_untilversion = new Label(container, SWT.NONE);
        label_untilversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        label_untilversion.setText("Test until version:");

        combo_untilversion = new Combo(container, SWT.READ_ONLY);
        combo_untilversion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        combo_untilversion.setItems(UIUtility.createVersionArray(config.getUntilVersion()));
        combo_untilversion.select(config.getUntilVersion() - 1);

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Extraction Dialog");
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            config.setThreads((short) (combo_threads.getSelectionIndex() + 1));
            config.setUntilVersion((short) (combo_untilversion.getSelectionIndex() + 1));

            execmanager.startNonBlocking(progressrunnable);

            okPressed();
        } else if (IDialogConstants.CANCEL_ID == buttonId) {
            cancelPressed();
        }
    }

    @Override
    protected Point getInitialSize() {
        return new Point(300, 150);
    }

    public void setExecmanager(ExecutionManager execmanager) {
        this.execmanager = execmanager;
    }

    public void setProgressrunnable(TestProgressRunnable progressrunnable) {
        this.progressrunnable = progressrunnable;
    }
}
