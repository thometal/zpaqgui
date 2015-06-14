package de.tt.zpaqgui.dialogs;

import de.tt.zpaqgui.UIUtility;
import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Constants;
import de.tt.zpaqgui.execution.ExecutionManager;
import de.tt.zpaqgui.execution.progressrunnables.CompressionsProgressRunnable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.inject.Inject;
import java.io.File;

public class CompressionsDialog extends Dialog {


    private ExecutionManager execmanager;


    private CompressionsProgressRunnable progressrunnable;

    private CMDLineConfig config;

    private Combo combo_threads;
    private Combo combo_method;

    private Combo combo_fragmentsize;

    private Combo combo_blocksize;

    private Label label_memoryusage_compression_pT_value;

    private Label label_memoryusage_decompression_pT_value;

    private Label label_memoryusage_compression_aT_value;

    private Label label_memoryusage_decompression_aT_value;

    private Label label_memoryusage_compression_dedup_value;

    private Label label_memoryusage_decompression_dedup_value;

    private Text text_archivepath;

    private Label text_archivinfo;


    private Label label_memoryusage_aT;

    @Inject
    public CompressionsDialog(Shell parentShell) {
        super(parentShell);
    }

    private static final String[] methoddata = {"deduplication only", "very fast", "fast", "normal", "good", "very good"};

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(5, false));

        final Label label_compression = new Label(container, SWT.NONE);
        label_compression.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_compression.setText("Archive:");

        text_archivepath = new Text(container, SWT.BORDER | SWT.FILL);
        text_archivepath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        text_archivepath.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                setAppendString();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });

        text_archivepath.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent e) {
                setAppendString();
            }

            @Override
            public void mouseDown(MouseEvent e) {
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });

        final Button button_selectArchive = new Button(container, SWT.PUSH);
        button_selectArchive.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        button_selectArchive.setText("Browse");
        button_selectArchive.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final FileDialog dialog = new FileDialog(parent.getShell(), SWT.SAVE);
                dialog.setFilterExtensions(UIUtility.FILTER[0]);
                dialog.setFilterNames(UIUtility.FILTER[1]);

                final String archivepath = dialog.open();

                if (archivepath == null || archivepath.isEmpty()) {
                    return;
                }
                text_archivepath.setText(archivepath);

                setAppendString();
            }
        });

        final Label label_dummy = new Label(container, SWT.NONE);
        label_dummy.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        label_dummy.setText("");

        text_archivinfo = new Label(container, SWT.NONE);
        text_archivinfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        text_archivinfo.setText("");

        final Label label_method = new Label(container, SWT.NONE);
        label_method.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        label_method.setText("Compression method:");

        combo_method = new Combo(container, SWT.READ_ONLY);
        combo_method.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));

        combo_method.setItems(methoddata);
        combo_method.select(methoddata.length - 1);
        combo_method.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final int selidx = combo_method.getSelectionIndex();

                if (selidx >= 0 && selidx <= 1) {
                    if (combo_blocksize.getSelectionIndex() != 4) {
                        combo_blocksize.select(4);
                    }
                    refreshCompressionMemoryUsage();
                    parent.getShell().layout(true, true);
                    return;
                }
                if (combo_blocksize.getSelectionIndex() != 6) {
                    combo_blocksize.select(6);
                }
                refreshCompressionMemoryUsage();
                parent.getShell().layout(true, true);
            }
        });

        final Label label_blocksize = new Label(container, SWT.NONE);
        label_blocksize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        label_blocksize.setText("Block size:");

        combo_blocksize = new Combo(container, SWT.READ_ONLY);
        combo_blocksize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));

        combo_blocksize.setItems(Constants.BLOCK_SIZES);
        combo_blocksize.select(6);
        combo_blocksize.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshCompressionMemoryUsage();
                reCalcFragmentItems();
                parent.getShell().layout(true, true);
            }
        });

        final Label label_fragment = new Label(container, SWT.NONE);
        label_fragment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        label_fragment.setText("Fragment size:");

        combo_fragmentsize = new Combo(container, SWT.READ_ONLY);
        combo_fragmentsize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));

        combo_fragmentsize.setItems(Constants.FRAGMENT_SIZES);
        combo_fragmentsize.select(6);
        combo_fragmentsize.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshDeduplicationMemoryUsage();
                reCalcFragmentItems();
                parent.getShell().layout(true, true);
            }
        });

        final Label label_threads = new Label(container, SWT.NONE);
        label_threads.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        label_threads.setText("Threads to use:");

        combo_threads = new Combo(container, SWT.NONE);
        combo_threads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        final String[] threads = UIUtility.getThreadCountsArray();

        combo_threads.setItems(threads);
        combo_threads.select(threads.length - 1);
        combo_threads.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshCompressionMemoryUsage();
                label_memoryusage_aT.setText(combo_threads.getText() + " Threads:");
                parent.getShell().layout(true, true);
            }
        });

        final Label label_memoryusage = new Label(container, SWT.NONE);
        label_memoryusage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        label_memoryusage.setText("Memory usage during:");

        final Label label_memoryusage_compression = new Label(container, SWT.NONE);
        label_memoryusage_compression.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_compression.setText("Compression");

        final Label label_memoryusage_decompression = new Label(container, SWT.NONE);
        label_memoryusage_decompression.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_decompression.setText("Decompression");

        final Label label_memoryusage_dedup = new Label(container, SWT.NONE);
        label_memoryusage_dedup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        label_memoryusage_dedup.setText("Deduplication per 1 TB input data:");

        label_memoryusage_compression_dedup_value = new Label(container, SWT.NONE);
        label_memoryusage_compression_dedup_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_compression_dedup_value.setText("~" + Constants.DEDUPLICATION_COMPRESSION_MEMORY + "MB");

        label_memoryusage_decompression_dedup_value = new Label(container, SWT.NONE);
        label_memoryusage_decompression_dedup_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_decompression_dedup_value.setText("~" + Constants.DEDUPLICATION_DECOMPRESSION_MEMORY + "MB");


        final Label label_memoryusage_comp = new Label(container, SWT.NONE);
        label_memoryusage_comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
        label_memoryusage_comp.setText("Compression:");


        final Label label_memoryusage_pT = new Label(container, SWT.NONE);
        label_memoryusage_pT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        label_memoryusage_pT.setText("per Thread:");

        label_memoryusage_compression_pT_value = new Label(container, SWT.NONE);
        label_memoryusage_compression_pT_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_compression_pT_value.setText("~" + Constants.COMPRESSION_MEMORY_USAGE[5] + "MB");

        label_memoryusage_decompression_pT_value = new Label(container, SWT.NONE);
        label_memoryusage_decompression_pT_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_decompression_pT_value.setText("~" + Constants.DECOMPRESSION_MEMORY_USAGE[5] + "MB");

        label_memoryusage_aT = new Label(container, SWT.NONE);
        label_memoryusage_aT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        label_memoryusage_aT.setText(combo_threads.getText() + " Threads:");

        label_memoryusage_compression_aT_value = new Label(container, SWT.NONE);
        label_memoryusage_compression_aT_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_compression_aT_value.setText("~" + Constants.COMPRESSION_MEMORY_USAGE[5] *
                (combo_threads.getSelectionIndex() + 1) + "MB");

        label_memoryusage_decompression_aT_value = new Label(container, SWT.NONE);
        label_memoryusage_decompression_aT_value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label_memoryusage_decompression_aT_value.setText("~" + Constants.DECOMPRESSION_MEMORY_USAGE[5] *
                (combo_threads.getSelectionIndex() + 1) + "MB");

        return container;
    }

    private void refreshDeduplicationMemoryUsage() {
        final int blocksizediff = Constants.DEFAULT_FRAGMENT_SIZE - combo_fragmentsize.getSelectionIndex();

        if (blocksizediff == 0) {
            label_memoryusage_compression_dedup_value.setText(Constants.DEDUPLICATION_COMPRESSION_MEMORY + "MB");
            label_memoryusage_decompression_dedup_value.setText(Constants.DEDUPLICATION_DECOMPRESSION_MEMORY + "MB");
            return;
        }

        final int dedupcompmem;
        final int dedupdecompmem;

        if (blocksizediff < 0) {
            dedupcompmem = Constants.DEDUPLICATION_COMPRESSION_MEMORY / (1 << -blocksizediff);
            dedupdecompmem = Constants.DEDUPLICATION_DECOMPRESSION_MEMORY / (1 << -blocksizediff);
        } else {
            dedupcompmem = Constants.DEDUPLICATION_COMPRESSION_MEMORY * (1 << blocksizediff);
            dedupdecompmem = Constants.DEDUPLICATION_DECOMPRESSION_MEMORY * (1 << blocksizediff);
        }

        label_memoryusage_compression_dedup_value.setText("~" + dedupcompmem + "MB");
        label_memoryusage_decompression_dedup_value.setText("~" + dedupdecompmem + "MB");
    }

    private void refreshCompressionMemoryUsage() {
        final int compmem = calcMemoryUsage(true);
        final int decompmem = calcMemoryUsage(false);

        label_memoryusage_compression_pT_value.setText("~" + compmem + "MB");
        label_memoryusage_decompression_pT_value.setText("~" + decompmem + "MB");

        label_memoryusage_compression_aT_value.setText("~" + compmem * (combo_threads.getSelectionIndex() + 1) + "MB");
        label_memoryusage_decompression_aT_value.setText("~" + decompmem * (combo_threads.getSelectionIndex() + 1) + "MB");
    }

    private int calcMemoryUsage(final boolean iscompression) {
        final int methodidx = combo_method.getSelectionIndex();
        final int blocksizeidx = combo_blocksize.getSelectionIndex();

        final int memory;

        if (iscompression) {
            memory = Constants.COMPRESSION_MEMORY_USAGE[methodidx];
        } else {
            memory = Constants.DECOMPRESSION_MEMORY_USAGE[methodidx];
        }

        final int blocksizediff = blocksizeidx - Constants.DEFAULT_BLOCK_SIZE[methodidx];

        if (blocksizediff == 0) {
            return memory;
        }
        if (blocksizediff < 0) {
            return memory / (1 << -blocksizediff);
        }
        return memory * (1 << blocksizediff);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            config.setArchive(new File(text_archivepath.getText()));
            config.setThreads((short) (combo_threads.getSelectionIndex() + 1));
            config.setMethod(String.valueOf(combo_method.getSelectionIndex()));
            config.setBlockSize((byte) combo_blocksize.getSelectionIndex());
            config.setFragmentSize((byte) combo_fragmentsize.getSelectionIndex());

            execmanager.startNonBlocking(progressrunnable);

            okPressed();
        } else if (IDialogConstants.CANCEL_ID == buttonId) {
            cancelPressed();
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Compresssion Dialog");
    }

    public void setZPAQCMDLineConfig(final CMDLineConfig config) {
        this.config = config;
        progressrunnable.setConfig(config);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 370);
    }

    private void setAppendString() {
        if (new File(text_archivepath.getText()).exists()) {
            text_archivinfo.setText("Appending to selected archive.");
        } else {
            text_archivinfo.setText("");
        }
    }

    public void setExecmanager(ExecutionManager execmanager) {
        this.execmanager = execmanager;
    }

    public void setProgressrunnable(CompressionsProgressRunnable progressrunnable) {
        this.progressrunnable = progressrunnable;
    }

    private void reCalcFragmentItems() {
        final int diff = (combo_blocksize.getSelectionIndex() + 10) - (combo_fragmentsize.getSelectionIndex() + 2);

        if (diff < 0) {
            combo_fragmentsize.select(combo_fragmentsize.getSelectionIndex() + diff);
        }
    }
}
