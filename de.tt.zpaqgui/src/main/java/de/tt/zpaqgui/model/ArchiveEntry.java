package de.tt.zpaqgui.model;

public abstract class ArchiveEntry {

    private String name;
    private long date;
    private short lastchange;
    private ArchiveFolder parent;
    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public short getLastChange() {
        return lastchange;
    }

    public void setLastChange(final short lastchange) {
        this.lastchange = lastchange;
    }

    public void setDate(final long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return this instanceof ArchiveFolder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (date ^ (date >>> 32));
        result = prime * result + lastchange;
        result = prime * result + (int) (size ^ (size >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ArchiveEntry))
            return false;
        ArchiveEntry other = (ArchiveEntry) obj;
        if (date != other.date)
            return false;
        if (size != other.size)
            return false;
        if (lastchange != other.lastchange)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public void setParent(ArchiveFolder parent) {
        this.parent = parent;
    }

    public ArchiveFolder getParentFolder() {
        return parent;
    }
}
