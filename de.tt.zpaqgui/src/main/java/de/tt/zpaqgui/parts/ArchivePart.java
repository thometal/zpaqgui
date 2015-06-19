package de.tt.zpaqgui.parts;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Constants;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.ListProgressRunnable;
import de.tt.zpaqgui.model.ArchiveEntry;
import de.tt.zpaqgui.model.ArchiveEntrySorter;
import de.tt.zpaqgui.model.ArchiveFolder;
import de.tt.zpaqgui.model.ArchiveModel;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

public class ArchivePart implements PropertyChangeListener {

    private ArchiveModel model;

    private Label label_location;

    private TableViewer table_data;

    @Inject
    private UISynchronize sync;

    @Inject
    private ESelectionService selectionService;

    @Inject
    private ExecutionManager execmanager;

    @Inject
    private ListProgressRunnable progressrunnable;

    private Button button_up;

    private ArchiveFolder currentdir;

    private Combo combo_sinceversion;

    private Combo combo_untilversion;

    private Label label_sinceversion;

    private Label label_untilversion;

    private Button button_showversions;

    private CMDLineConfig config;

    @PostConstruct
    public void createComposite(final MPart part, Composite parent) {
        parent.setLayout(new GridLayout(6, false));

        config = (CMDLineConfig) part.getObject();
        model = (ArchiveModel) config.getModel();
        progressrunnable.setConfig(config);

        final Composite firstline = new Composite(parent, SWT.NONE);
        firstline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
        firstline.setLayout(new GridLayout(2, false));

        button_up = new Button(firstline, SWT.PUSH);
        button_up.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        button_up.setImage(UIUtility.getUp());
        button_up.setEnabled(false);
        button_up.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                up();
            }
        });

        label_location = new Label(firstline, SWT.NONE);
        label_location.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        label_location.setText("loading archive...");

        label_sinceversion = new Label(parent, SWT.NONE);
        label_sinceversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_sinceversion.setText("Show since Version");

        combo_sinceversion = new Combo(parent, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        combo_sinceversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        combo_sinceversion.setItems(UIUtility.createVersionArray(model.getVersions()));
        combo_sinceversion.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (combo_sinceversion.getSelectionIndex() > combo_untilversion.getSelectionIndex()) {
                    combo_sinceversion.select(combo_untilversion.getSelectionIndex());
                }
            }
        });

        label_untilversion = new Label(parent, SWT.NONE);
        label_untilversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_untilversion.setText("until");

        combo_untilversion = new Combo(parent, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        combo_untilversion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        combo_untilversion.setItems(UIUtility.createVersionArray(model.getVersions()));
        combo_untilversion.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (combo_sinceversion.getSelectionIndex() > combo_untilversion.getSelectionIndex()) {
                    combo_untilversion.select(combo_sinceversion.getSelectionIndex());
                }
            }
        });

        button_showversions = new Button(parent, SWT.PUSH | SWT.LEFT);
        button_showversions.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        button_showversions.setImage(UIUtility.getRefresh());
        button_showversions.setEnabled(false);
        button_showversions.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                config.setUntilVersion((short) (combo_untilversion.getSelectionIndex() + 1));
                config.setSinceVersion((short) (combo_sinceversion.getSelectionIndex() + 1));

                execmanager.startBlocking(progressrunnable);
            }
        });

        model.addPropertyChangeListener(this);

        table_data = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        table_data.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
        createColumns(parent, table_data);
        final Table table = table_data.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        table_data.setContentProvider(new ArrayContentProvider());
        table_data.setInput(model.getFiles(null));
        table_data.setComparator(new ArchiveEntrySorter());

        table_data.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                selectionService.setSelection(((IStructuredSelection) event.getSelection()).toList());
            }
        });

        table_data.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                final ArchiveEntry entry = (ArchiveEntry) ((IStructuredSelection) event.getSelection()).getFirstElement();

                if (entry.isDirectory()) {
                    if (currentdir == null || currentdir.getParentFolder() == null) {
                        button_up.setEnabled(true);
                    }
                    currentdir = (ArchiveFolder) entry;

                    table_data.setInput(model.getFiles(currentdir));
                    label_location.setText(model.getPath(currentdir));
                }
            }
        });

        table.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.character == SWT.BS && button_up.isEnabled()) {
                    up();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void up() {
        currentdir = model.getParent(currentdir);
        table_data.setInput(model.getFiles(currentdir));
        label_location.setText(model.getPath(currentdir));

        if (currentdir == null || currentdir.getParentFolder() == null) {
            button_up.setEnabled(false);
        }
    }

    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = {"Filename", "Date", "Size", "Version",};
        int[] bounds = {550, 150, 100, 50};

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ArchiveEntry) element).getName();
            }

            @Override
            public Image getImage(Object element) {
                ArchiveEntry entry = (ArchiveEntry) element;
                if (entry.isDirectory()) {
                    return UIUtility.getFolder();
                }
                return null;
            }
        });

        TableViewerColumn col2 = createTableViewerColumn(titles[1], bounds[1], 1);
        col2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ArchiveEntry entry = (ArchiveEntry) element;
                if (model.isHelperFolder(entry)) {
                    return null;
                }
                return Constants.DATEFORMAT.format(new Date(((ArchiveEntry) element).getDate()));
            }
        });


        TableViewerColumn col3 = createTableViewerColumn(titles[2], bounds[2], 2);
        col3.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	ArchiveEntry entry = (ArchiveEntry) element;
            	if (model.isHelperFolder(entry)) {
                    return null;
                }
                return UIUtility.createReadableSize(entry.getSize());
            }
        });

        TableViewerColumn col4 = createTableViewerColumn(titles[3], bounds[3], 3);
        col4.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                final ArchiveEntry entry = (ArchiveEntry) element;

                if (model.isHelperFolder(entry)) {
                    return null;
                }
                return String.valueOf(entry.getLastChange());
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound,
                                                      final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(
                table_data, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    @Focus
    public void setFocus() {
        table_data.getTable().setFocus();
    }

    @PreDestroy
    public void cleanup() {
        model.removePropertyChangeListener();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        sync.asyncExec(new Runnable() {
            @Override
            public void run() {

                if (!table_data.getControl().isDisposed()) {

                    List<ArchiveEntry> list = model.getFiles(currentdir);
                    table_data.setInput(list);
                    label_location.setText(model.getPath(currentdir));

                    final String[] versions = UIUtility.createVersionArray(model.getVersions());

                    combo_sinceversion.setItems(versions);
                    combo_untilversion.setItems(versions);

                    final short fromversion = config.getSinceVersion();
                    final short untilversion = config.getUntilVersion();

                    if (fromversion == 0) {
                        combo_sinceversion.select(0);
                        config.setSinceVersion((short) 1);
                    } else {
                        combo_sinceversion.select(fromversion - 1);
                    }

                    if (untilversion == 0) {
                        combo_untilversion.select(versions.length - 1);
                        config.setUntilVersion((short) (versions.length));
                    } else {
                        combo_untilversion.select(untilversion - 1);
                    }

                    button_showversions.setEnabled(true);
                }
            }
        });
    }

    public CMDLineConfig getConfig() {
        return config;
    }
}
