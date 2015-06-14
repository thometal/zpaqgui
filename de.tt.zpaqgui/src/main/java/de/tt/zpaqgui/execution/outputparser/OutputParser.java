package de.tt.zpaqgui.execution.outputparser;

import org.eclipse.core.runtime.IProgressMonitor;

public interface OutputParser {

    void parseOutput(final String line);

    void setProgressMonitor(final IProgressMonitor monitor);

    void setDuringProcessValues(final String text, final int items);
}
