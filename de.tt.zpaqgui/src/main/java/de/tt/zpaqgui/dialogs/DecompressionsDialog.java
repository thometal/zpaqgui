package de.tt.zpaqgui.dialogs;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.DecompressionsProgressRunnable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.inject.Inject;
import java.io.File;


public class DecompressionsDialog extends Dialog {

    private ExecutionManager execmanager;

    private DecompressionsProgressRunnable progressrunnable;

    private CMDLineConfig config;
    private Combo combo_threads;
    private Combo combo_sinceversion;
    private DateTimeDialog date;
    private Button button_datetime;
    private Label label_until;
    private UntilHolderObject until;

    private Text text_extractionpath;

    private Button button_override;

    @Inject
    public DecompressionsDialog(Shell parentShell) {
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

        final Label label_extraction = new Label(container, SWT.NONE);
        label_extraction.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_extraction.setText("Target Folder:");

        text_extractionpath = new Text(container, SWT.BORDER | SWT.FILL);
        text_extractionpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        final Button button_browseextractionfolder = new Button(container, SWT.PUSH);
        button_browseextractionfolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        button_browseextractionfolder.setText("Browse");
        button_browseextractionfolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
                final String extractiondir = dialog.open();

                if (extractiondir == null || extractiondir.isEmpty()) {
                    return;
                }
                text_extractionpath.setText(extractiondir);
            }
        });

        final Label label_threads = new Label(container, SWT.NONE);
        label_threads.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        label_threads.setText("Threads to use:");

        combo_threads = new Combo(container, SWT.NONE);
        combo_threads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));

        final String[] threads = UIUtility.getThreadCountsArray();

        combo_threads.setItems(threads);
        combo_threads.select(threads.length - 1);

        final Label label_sinceversion = new Label(container, SWT.NONE);
        label_sinceversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        label_sinceversion.setText("Extract since Version");

        combo_sinceversion = new Combo(container, SWT.READ_ONLY);
        combo_sinceversion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        combo_sinceversion.setItems(UIUtility.createVersionArray(config.getSinceVersion()));
        combo_sinceversion.select(config.getSinceVersion() - 1);

        final Label label_to = new Label(container, SWT.NONE);
        label_to.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_to.setText("to ");

        label_until = new Label(container, SWT.NONE);
        label_until.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        label_until.setText(config.getUntilVersion() + ". Version.");

        button_datetime = new Button(container, SWT.PUSH);
        button_datetime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        button_datetime.setText("specify");
        button_datetime.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                if (until == null) {
                    date.setIsversionselected(true);
                } else {
                    date.setIsversionselected(until.isVersionSet());
                }

                if (date.open() == Dialog.OK) {
                    until = date.getUntilObject();

                    final String text;

                    if (until.isVersionSet()) {
                        text = until.toString() + ". Version.";
                    } else {
                        text = until.toString();
                    }
                    label_until.setText(text);
                }
            }
        });

        final Label label_override = new Label(container, SWT.NONE);
        label_override.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        label_override.setText("Override existing files:");

        button_override = new Button(container, SWT.CHECK);
        button_override.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));


        date = new DateTimeDialog(getShell());
        date.setUntilVersion(config.getUntilVersion());

        until = new UntilHolderObject(config.getUntilVersion());

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
            config.setExtractionFolder(new File(text_extractionpath.getText()));
            config.setThreads((short) (combo_threads.getSelectionIndex() + 1));
            config.setSinceVersion((short) (combo_sinceversion.getSelectionIndex() + 1));
            config.setOverride(button_override.getSelection());

            if (until.isVersionSet()) {
                config.setUntilVersion(until.getVersion());
            } else {
                config.setUntilDate(until.getDate());
            }

            execmanager.startNonBlocking(progressrunnable);

            System.out.println(until);

            okPressed();
        } else if (IDialogConstants.CANCEL_ID == buttonId) {
            cancelPressed();
        }
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 250);
    }

    public void setExecmanager(ExecutionManager execmanager) {
        this.execmanager = execmanager;
    }

    public void setProgressrunnable(DecompressionsProgressRunnable progressrunnable) {
        this.progressrunnable = progressrunnable;
    }
}
