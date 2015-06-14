package de.tt.zpaqgui.execution.progressrunnables;

import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.cmdlinebuilder.CMDLineBuilderFactory;
import de.tt.zpaqgui.execution.outputparser.OutputParser;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Scanner;


public abstract class ProgressRunnableBase implements IRunnableWithProgress {

    private CMDLineBuilderFactory cmdbuilder;
    protected static final int MAX_WORK_ITEMS = 10000;

    OutputParser outputparser;
    CMDLineConfig config;
    private Process process;

    public CMDLineConfig getConfig() {
        return config;
    }

    public void setConfig(CMDLineConfig config) {
        this.config = config;
    }

    @Inject
    public void setCmdLineBuilderFactory(CMDLineBuilderFactory cmdbuilder) {
        this.cmdbuilder = cmdbuilder;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        if (beforeStart(monitor)) {
            return;
        }

        outputparser.setProgressMonitor(monitor);

        final List<String> cmdline = cmdbuilder.buildCMDLine(config);

        Scanner sc = null;

        try {
            final ProcessBuilder buil = new ProcessBuilder(cmdline);
            buil.redirectErrorStream(true);

            process = buil.start();

            outputparser.parseOutput(createCMDLineString(cmdline));

            sc = new Scanner(process.getInputStream());

            while (sc.hasNext()) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                outputparser.parseOutput(sc.nextLine());

                if (monitor.isCanceled()) {
                    process.destroy();
                    break;
                }
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            beforeEnd(monitor);
            if (sc == null) {
                return;
            }
            sc.close();
        }
    }

    private String createCMDLineString(final List<String> cmdline) {
        final StringBuilder sb = new StringBuilder();

        sb.append('\n');

        final int lastidx = cmdline.size() - 1;

        for (int i = 0; i < lastidx; i++) {
            final String entry = cmdline.get(i);
            sb.append(entry);
            sb.append(' ');
        }
        sb.append(cmdline.get(lastidx));

        return sb.toString();
    }

    public void destroy() {
        process.destroy();
    }

    abstract boolean beforeStart(IProgressMonitor monitor);

    abstract void beforeEnd(IProgressMonitor monitor);

}