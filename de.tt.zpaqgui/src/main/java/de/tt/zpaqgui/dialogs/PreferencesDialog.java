package de.tt.zpaqgui.dialogs;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.execution.ExecutableTester;
import de.tt.zpaqgui.execution.ExecutableTester.ZPAQExecutableTesterCallback;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.*;
import org.osgi.service.prefs.BackingStoreException;

public class PreferencesDialog extends Dialog implements ZPAQExecutableTesterCallback {

    private IEclipsePreferences prefs;
    private UISynchronize sync;

    protected String zpaqlocation;
    private Label label_zpaqinfo;
    private ZPAQSettings settings;
    private Link link_zpaqhp;
	private Text text_zpaqlocation;

    public PreferencesDialog(Shell parentShell) {
        super(parentShell);
    }

    public void setPreferences(IEclipsePreferences prefs) {
        this.prefs = prefs;
    }

    public void setUISynchronize(UISynchronize sync) {
        this.sync = sync;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));

        final Label label_zpaqtitle = new Label(container, SWT.NONE);
        label_zpaqtitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 2, 1));
        label_zpaqtitle.setText("ZPAQ Executable:");

        text_zpaqlocation = new Text(container, SWT.BORDER
                | SWT.FILL);
        text_zpaqlocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));

        final Button button_zpaqlocation = new Button(container, SWT.PUSH);
        button_zpaqlocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        button_zpaqlocation.setText("Browse");
        button_zpaqlocation.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                final FileDialog dialog = new FileDialog(parent.getShell());
                final String location = dialog.open();

                if (location == null) {
                    return;
                }
                zpaqlocation = location;

                text_zpaqlocation.setText(zpaqlocation);

                runZPAQLocationTester();
            }
        });

        label_zpaqinfo = new Label(container, SWT.NONE);
        label_zpaqinfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 2, 1));

        link_zpaqhp = new Link(container, SWT.NONE);
        link_zpaqhp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 2, 2));
        link_zpaqhp.setText("Get latest <a>ZPAQ</a>!");
        link_zpaqhp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Program.launch("http://mattmahoney.net/dc/zpaq.html");
            }
        });

        if (settings.isZpaqLocationLoaded()) {
            zpaqlocation = settings.getZpaqLocation().getAbsolutePath();
            text_zpaqlocation.setText(zpaqlocation);
            runZPAQLocationTester();
        }

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Preferences Dialog");
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            prefs.put("zpaqlocation", text_zpaqlocation.getText());
            try {
                prefs.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
            okPressed();
        } else if (IDialogConstants.CANCEL_ID == buttonId) {
            cancelPressed();
        }
    }

    @Override
    protected void cancelPressed() {
        System.out.println("cancel");
        setReturnCode(CANCEL);
        close();
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 200);
    }

    public void setSettings(ZPAQSettings settings) {
        this.settings = settings;
    }

    private void runZPAQLocationTester() {
        final ExecutableTester tester = new ExecutableTester();
        tester.setLocation(zpaqlocation);
        tester.setCallback(this);
        tester.start();
    }

    @Override
    public void getResult(final String infotext, final boolean islegallocation) {
        sync.syncExec(new Runnable() {
            @Override
            public void run() {
                label_zpaqinfo.setText(infotext);

                final Button ok = getButton(IDialogConstants.OK_ID);

                ok.setEnabled(islegallocation);
            }
        });
    }
}
