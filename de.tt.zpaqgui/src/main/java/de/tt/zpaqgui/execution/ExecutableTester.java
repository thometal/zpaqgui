package de.tt.zpaqgui.execution;

import java.io.IOException;
import java.util.Scanner;

public class ExecutableTester extends Thread {
    private String zpaqlocation;
    private ZPAQExecutableTesterCallback callback;
    private boolean choselegalzpaqlocation;

    @Override
    public void run() {

        choselegalzpaqlocation = false;

        Scanner sc = null;

        String infotext = null;
        try {
            final Process exec = Runtime.getRuntime().exec(zpaqlocation);

            sc = new Scanner(exec.getInputStream());

            while (sc.hasNext()) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                final String line = sc.nextLine();

                if (line.indexOf("zpaq") == 0 && line.indexOf("journaling archiver") == 11) {
                    infotext = "Recognized ZPAQ Version: " + line.substring(6, 10);
                    choselegalzpaqlocation = true;
                    break;
                }
            }
            if (!choselegalzpaqlocation) {
                infotext = "Illegal ZPAQVersion";
            }
        } catch (IOException e) {
            infotext = "Illegal ZPAQVersion";
            e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.close();
            }
            if (callback != null) {
                callback.getResult(infotext, choselegalzpaqlocation);
            }
        }
    }

    public void setLocation(String zpaqlocation) {
        this.zpaqlocation = zpaqlocation;
    }

    public void removeCallback(ZPAQExecutableTesterCallback callback) {
        callback = null;
    }

    public void setCallback(ZPAQExecutableTesterCallback callback) {
        this.callback = callback;
    }

    public interface ZPAQExecutableTesterCallback {
        void getResult(String infotext, boolean islegallocation);
    }
}
