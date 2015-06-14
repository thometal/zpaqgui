package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;

import java.util.ArrayList;
import java.util.List;

@Creatable
public class ArchiveFolder extends ArchiveEntry {

    private final List<ArchiveEntry> childs = new ArrayList<>();

    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    public List<ArchiveEntry> getChilds() {
        return childs;
    }

    public void addChild(final ArchiveEntry entry) {
        childs.add(entry);
    }
}
