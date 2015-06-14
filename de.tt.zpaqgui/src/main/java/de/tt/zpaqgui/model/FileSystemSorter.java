package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import java.io.File;

@Creatable
public class FileSystemSorter extends ViewerComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        final File f1 = (File) e1;
        final File f2 = (File) e2;

        if (f1.isDirectory() == f2.isDirectory()) {
            return f1.getPath().compareToIgnoreCase(f2.getPath());
        }
        if (f1.isDirectory()) {
            return -1;
        }
        return 1;
    }
}
