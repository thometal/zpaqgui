package de.tt.zpaqgui.execution.progressrunnables;

import de.tt.zpaqgui.execution.outputparser.TestOutputParser;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Inject;

@Creatable
public class TestProgressRunnable extends ProgressRunnableBase {

    @Inject
    public void setOutputParser(final TestOutputParser testoutputparser) {
        this.outputparser = testoutputparser;
    }

    @Override
    boolean beforeStart(final IProgressMonitor monitor) {
        monitor.beginTask("Reading archive headers & testing first block: " + config.getArchive().getAbsolutePath(), IProgressMonitor.UNKNOWN);
        this.outputparser.setDuringProcessValues("Testing: " + config.getArchive().getAbsolutePath(), MAX_WORK_ITEMS);

        return false;
    }

    @Override
    void beforeEnd(final IProgressMonitor monitor) {

    }
}
