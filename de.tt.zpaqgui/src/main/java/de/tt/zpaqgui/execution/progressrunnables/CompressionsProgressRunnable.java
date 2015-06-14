package de.tt.zpaqgui.execution.progressrunnables;

import de.tt.zpaqgui.execution.outputparser.CompressionsOutputParser;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Inject;

@Creatable
public class CompressionsProgressRunnable extends ProgressRunnableBase {

    @Inject
    public void setOutputParser(final CompressionsOutputParser compressionoutputparser) {
        this.outputparser = compressionoutputparser;
    }

    @Override
    boolean beforeStart(final IProgressMonitor monitor) {
        monitor.beginTask("Scan files & compressing first block: " + config.getArchive().getAbsolutePath(), IProgressMonitor.UNKNOWN);
        this.outputparser.setDuringProcessValues("Compressing file: " + config.getArchive().getAbsolutePath(), MAX_WORK_ITEMS);

        return false;
    }

    @Override
    void beforeEnd(final IProgressMonitor monitor) {

    }
}
