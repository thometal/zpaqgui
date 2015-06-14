package de.tt.zpaqgui.execution.outputparser;

import de.tt.zpaqgui.model.OutStreamModel;
import org.eclipse.core.runtime.IProgressMonitor;

import javax.inject.Inject;


public abstract class OutputParserBase implements OutputParser {

    @Inject
    private OutStreamModel model;

    @Override
    public void parseOutput(final String line) {
        model.setString(line);
    }

    @Override
    public abstract void setProgressMonitor(final IProgressMonitor monitor);
}
