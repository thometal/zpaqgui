package de.tt.zpaqgui.execution.progressrunnables;

import de.tt.zpaqgui.execution.outputparser.DecompressionsOutputParser;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Inject;

@Creatable
public class DecompressionsProgressRunnable extends ProgressRunnableBase {

    @Inject
    public void setOutputParser(final DecompressionsOutputParser extractionoutputparser) {
        this.outputparser = extractionoutputparser;
    }

    @Override
    boolean beforeStart(final IProgressMonitor monitor) {
        monitor.beginTask("Reading archive headers & extracting first block: " + config.getArchive().getAbsolutePath(), IProgressMonitor.UNKNOWN);
        this.outputparser.setDuringProcessValues("Extracting from: " + config.getArchive().getAbsolutePath(), MAX_WORK_ITEMS);

        return false;
    }

    @Override
    void beforeEnd(final IProgressMonitor monitor) {

    }
}
