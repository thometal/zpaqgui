package de.tt.zpaqgui.model;

import java.util.Comparator;

public class ArchiveModelComparator implements Comparator<ArchiveEntry> {
    @Override
    public int compare(ArchiveEntry arg0, ArchiveEntry arg1) {
        return arg0.getName().compareToIgnoreCase(arg1.getName());
    }
}
