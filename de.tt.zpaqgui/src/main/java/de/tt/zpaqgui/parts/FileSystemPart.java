package de.tt.zpaqgui.parts;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.Constants;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.ListProgressRunnable;
import de.tt.zpaqgui.model.ArchiveModel;
import de.tt.zpaqgui.model.FileSystemModel;
import de.tt.zpaqgui.model.FileSystemSorter;

import org.eclipse.e4.ui.di.Focus;
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

import java.io.File;
import java.util.Date;

public class FileSystemPart {

    private TableViewer tableviewer;
    private Label label;

    @Inject
    private FileSystemModel model;

    @Inject
    private FileSystemSorter sorter;

    @Inject
    private ESelectionService selectionService;
    
    @Inject
    private ZPAQSettings settings;
    
    @Inject
    private ListProgressRunnable listrunnable;
    
    @Inject
    private ExecutionManager execmanager;

    private File currentdir;
    private Button button;

    @PostConstruct
    public void createComposite(Composite parent) {
        parent.setLayout(new GridLayout());

        final Composite firstline = new Composite(parent, SWT.NONE);
        firstline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
        firstline.setLayout(new GridLayout(2, false));

        button = new Button(firstline, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        button.setImage(UIUtility.getUp());
        button.setEnabled(false);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                up();
            }
        });

        label = new Label(firstline, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        label.setText(".");

        tableviewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, tableviewer);
        final Table table = tableviewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tableviewer.setContentProvider(new ArrayContentProvider());
        tableviewer.setInput(model.getFiles(null));
        tableviewer.setComparator(sorter);

        tableviewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                selectionService.setSelection(((IStructuredSelection) event.getSelection()).toList());
            }
        });

        tableviewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                final File file = (File) ((IStructuredSelection) event.getSelection()).getFirstElement();

                if (file.isDirectory()) {
                    if (currentdir == null) {
                        button.setEnabled(true);
                    }

                    currentdir = file;
                    tableviewer.setInput(model.getFiles(currentdir));
                    label.setText(model.getPath(currentdir));
                }else if(file.getName().contains(".zpaq") && settings.isZpaqLocationLoaded()){
                	
                	final CMDLineConfig config = new CMDLineConfig();
                    config.setArchive(file);
                    config.setZpaqCommand(Command.LIST);
                    config.setModel(new ArchiveModel());

                    listrunnable.setConfig(config);

                    execmanager.startBlocking(listrunnable);
				}
            }
        });
        
        table.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.character == SWT.BS && button.isEnabled()) {
                    up();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        tableviewer.getControl().setLayoutData(gridData);
    }

    private void up() {
        currentdir = currentdir.getParentFile();
        tableviewer.setInput(model.getFiles(currentdir));
        label.setText(model.getPath(currentdir));

        if (currentdir == null) {
            currentdir = null;
            button.setEnabled(false);
        }
    }


    public TableViewer getViewer() {
        return tableviewer;
    }

    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = {"Name", "Date ", "Size"};
        int[] bounds = {350, 150, 100};

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                final File f = ((File) element);

                if (f.getParent() != null) {
                    return f.getName();
                } else {
                    return f.getPath();
                }
            }

            @Override
            public Image getImage(Object element) {
                File file = (File) element;
                if (file.isDirectory()) {
                    return UIUtility.getFolder();
                }
                return null;
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return Constants.DATEFORMAT.format(new Date(((File) element).lastModified()));
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (((File) element).isFile()) {
                    return UIUtility.createReadableSize(((File) element).length());
                } else {
                    return null;
                }
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound,
                                                      final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(
                tableviewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    @Focus
    public void setFocus() {
        tableviewer.getTable().setFocus();
    }

    @PreDestroy
    public void cleanup() {

    }
}
