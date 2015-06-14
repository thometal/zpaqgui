package de.tt.zpaqgui.execution.outputparser;

import org.eclipse.core.runtime.IProgressMonitor;

public abstract class ProgressOutputParserBase extends OutputParserBase {

    private int lastworked = 0;
    private IProgressMonitor monitor;
    private boolean isinit;
    private String text;
    private int items;


    @Override
    public void parseOutput(final String line) {
        super.parseOutput(line);
        initalizingFinished();
                
        int idx = line.substring(0,Math.min(7,line.length())).indexOf('%');
        
        if(idx == -1){
        	return;
        }
        try {
        	int worked = (int) (Float.valueOf(line.substring(0, idx))*100f);		

        	final int diff = worked - lastworked;

        	if (diff > 0) {
        		monitor.worked(diff);
        		lastworked = worked;
        	}
        }catch(NumberFormatException e){
        	e.printStackTrace();
        }
    }

    private void initalizingFinished() {
        if (!isinit) {
            monitor.beginTask(text, items);
            isinit = true;
        }
    }

    @Override
    public void setProgressMonitor(final IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void setDuringProcessValues(final String text, final int items) {
        this.text = text;
        this.items = items;
    }
}