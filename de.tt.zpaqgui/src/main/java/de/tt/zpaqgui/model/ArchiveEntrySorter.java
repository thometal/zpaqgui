package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

@Creatable
public class ArchiveEntrySorter extends ViewerComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        final ArchiveEntry entry1 = (ArchiveEntry) e1;
        final ArchiveEntry entry2 = (ArchiveEntry) e2;

        if (entry1.isDirectory() == entry2.isDirectory()) {
            return entry1.getName().compareToIgnoreCase(entry2.getName());
        }
        if (entry1.isDirectory()) {
            return -1;
        }
        return 1;
    }
}
