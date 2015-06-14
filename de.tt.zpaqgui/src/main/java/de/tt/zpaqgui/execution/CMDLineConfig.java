package de.tt.zpaqgui.execution;

import de.tt.zpaqgui.model.ArchiveFile;

import java.io.File;
import java.util.List;

public class CMDLineConfig implements Cloneable {

    private Command zpaqcmd = null;

    private File archive = null;

    private File extractionfolder = null;

    private Object model;

    private List<ArchiveFile> archiveentries;

    private short sinceversion = 0;
    private short untilversion = 0;
    private long untildate = 0;

    private short threads;

    private String method;

    private List<File> fileentries;

    private byte blocksize = -1;

    private byte fragmentsize = -1;

    private boolean isversionset;

    private boolean isoverride;

	private boolean istest;

    public Command getZpaqCommand() {
        return zpaqcmd;
    }

    public void setZpaqCommand(Command zpaqcmd) {
        this.zpaqcmd = zpaqcmd;
    }

    public File getArchive() {
        return archive;
    }

    public void setArchive(File archive) {
        this.archive = archive;
    }

    public File getExtractionFolder() {
        return extractionfolder;
    }

    public void setExtractionFolder(File extractionfolder) {
        this.extractionfolder = extractionfolder;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public List<ArchiveFile> getSelectedArchiveEntries() {
        return archiveentries;
    }

    public void setSelectedArchiveEntries(List<ArchiveFile> archiveentries) {
        this.archiveentries = archiveentries;
    }

    public short getSinceVersion() {
        return sinceversion;
    }

    public void setSinceVersion(final short sinceversion) {
        this.sinceversion = sinceversion;
    }

    public short getUntilVersion() {
        return untilversion;
    }

    public void setUntilVersion(final short untilversion) {
        isversionset = true;
        this.untilversion = untilversion;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setThreads(final short threads) {
        this.threads = threads;
    }

    public short getThreads() {
        return threads;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public List<File> getSelectedFileEntries() {
        return fileentries;
    }

    public void setSelectedFileEntries(final List<File> filenentries) {
        this.fileentries = filenentries;
    }

    public void setBlockSize(final byte blocksize) {
        this.blocksize = blocksize;
    }

    public void setFragmentSize(final byte fragmentsize) {
        this.fragmentsize = fragmentsize;
    }

    public byte getBlockSize() {
        return blocksize;
    }

    public byte getFragmentSize() {
        return fragmentsize;
    }

    public void setTest(final boolean istest) {
    	this.istest = istest;
    }
    
    public boolean isTest() {
        return istest;
    }
    
    public void setUntilDate(final long untildate) {
        isversionset = false;
        this.untildate = untildate;
    }

    public boolean isVersionSet() {
        return isversionset;
    }

    public long getUntilDate() {
        return untildate;
    }

    public void setOverride(boolean isoverride) {
        this.isoverride = isoverride;
    }

    public boolean isOverride() {
        return isoverride;
    }
}
