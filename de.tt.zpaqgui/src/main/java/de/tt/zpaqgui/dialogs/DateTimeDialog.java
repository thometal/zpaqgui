package de.tt.zpaqgui.dialogs;


import de.tt.zpaqgui.UIUtility;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Calendar;

public class DateTimeDialog extends Dialog {

    public DateTimeDialog(Shell parentShell) {
        super(parentShell);
    }

    private Button button_date;
    private Button button_version;
    private DateTime datetime_calendar;
    private DateTime datetime_time;
    private Combo combo_version;
    private Calendar cal = Calendar.getInstance();
    private Short version = null;
    private boolean isversionselected;

    public void setIsversionselected(boolean isversionselected) {
        this.isversionselected = isversionselected;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(3, false));

        button_date = new Button(container, SWT.RADIO);
        button_date.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        button_date.setSelection(!isversionselected);
        button_date.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final Button button_date = (Button) e.getSource();

                if (button_date.getSelection()) {
                    datetime_time.setEnabled(true);
                    datetime_calendar.setEnabled(true);
                    combo_version.setEnabled(false);

                    isversionselected = false;
                }
            }
        });

        datetime_time = new DateTime(container, SWT.TIME);
        datetime_time.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        datetime_time.setEnabled(!isversionselected);
        datetime_time.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                cal.set(datetime_calendar.getYear(), datetime_calendar.getMonth(), datetime_calendar.getDay(), datetime_time.getHours(), datetime_time.getMinutes(), datetime_time.getSeconds());
            }
        });

        datetime_calendar = new DateTime(container, SWT.CALENDAR);
        datetime_calendar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 2));
        datetime_calendar.setEnabled(!isversionselected);
        datetime_calendar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                cal.set(datetime_calendar.getYear(), datetime_calendar.getMonth(), datetime_calendar.getDay(), datetime_time.getHours(), datetime_time.getMinutes(), datetime_time.getSeconds());
            }
        });

        button_version = new Button(container, SWT.RADIO);
        button_version.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        button_version.setSelection(isversionselected);
        button_version.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final Button button_version = (Button) e.getSource();

                if (button_version.getSelection()) {
                    datetime_time.setEnabled(false);
                    datetime_calendar.setEnabled(false);
                    combo_version.setEnabled(true);

                    isversionselected = true;
                }
            }
        });

        combo_version = new Combo(container, SWT.READ_ONLY);
        combo_version.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        combo_version.setEnabled(isversionselected);
        combo_version.setItems(UIUtility.createVersionArray(version));
        combo_version.select(version - 1);
        combo_version.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                version = (short) (combo_version.getSelectionIndex() + 1);
            }
        });

        return container;
    }

    public UntilHolderObject getUntilObject() {
        if (isversionselected) {
            return new UntilHolderObject(version.shortValue());
        }
        return new UntilHolderObject(cal.getTime().getTime());
    }

    public void setUntilVersion(short version) {
        this.version = version;
    }
}
