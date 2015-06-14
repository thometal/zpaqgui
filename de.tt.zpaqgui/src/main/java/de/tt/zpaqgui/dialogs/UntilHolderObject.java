package de.tt.zpaqgui.dialogs;

import java.util.Date;

public class UntilHolderObject {
    private short version;
    private long date;
    private boolean isversionset;

    public UntilHolderObject(final short version) {
        isversionset = true;

        this.version = version;
    }

    public UntilHolderObject(final long date) {
        isversionset = false;

        this.date = date;
    }

    public short getVersion() {
        return version;
    }

    public long getDate() {
        return date;
    }


    public boolean isVersionSet() {
        return isversionset;
    }

    @Override
    public String toString() {
        if (isversionset) {
            return String.valueOf(version);
        }
        return new Date(date).toString();
    }
}
