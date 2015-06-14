package de.tt.zpaqgui.parts;

import de.tt.zpaqgui.model.OutStreamModel;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ConsolePart implements PropertyChangeListener {

    private Text text_console;

    @Inject
    private OutStreamModel model;

    @Inject
    private UISynchronize sync;

    @PostConstruct
    public void createComposite(Composite parent) {
        parent.setLayout(new GridLayout());

        text_console = new Text(parent, SWT.V_SCROLL | SWT.H_SCROLL);

        text_console.setLayoutData(new GridData(GridData.FILL_BOTH));
        text_console.setEditable(false);

        model.addPropertyChangeListener(this);

        System.out.println("created");
    }

    @Focus
    public void setFocus() {
        text_console.setFocus();
    }

    @PreDestroy
    public void cleanup() {
        model.removePropertyChangeListener();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {

        final String newline = System.getProperty("line.separator");

        sync.asyncExec(new Runnable() {
            @Override
            public void run() {
                if (!text_console.isDisposed()) {
                    text_console.append(evt.getNewValue() + newline);
                }
            }
        });
    }
}
