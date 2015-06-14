package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


@Creatable
public class ArchiveModel implements Model<ArchiveEntry> {

    private PropertyChangeListener listener;

    private short lastchange = 1;

    private static final char SLASH = '/';

    private static final String ROOTNAME = ".";

    private final ArchiveFolder root = new ArchiveFolder();

    public ArchiveFolder getParent(final ArchiveEntry archivefile) {
        if (archivefile == null) {
            return null;
        }
        return archivefile.getParentFolder();
    }

    @Override
    public List<ArchiveEntry> getFiles(final ArchiveEntry parent) {
        if (parent == null || parent instanceof ArchiveFile) {
            return root.getChilds();
        }

        return ((ArchiveFolder) parent).getChilds();
    }

    public void add(final String path, final ArchiveEntry archiveentry) {
        checkLastChange(archiveentry);

        if (root.getChilds().isEmpty()) {
            root.setName(ROOTNAME);
        }

        final List<String> folders = getSeparatedFolders(path);
        archiveentry.setName(folders.get(folders.size() - 1));

        List<ArchiveEntry> entries;
        ArchiveFolder entry = root;

        for (int i = 0; i < folders.size() - 1; i++) {
            entries = entry.getChilds();

            final int idx = findIDX(entries, folders.get(i));

            if (idx >= 0) {
                entry = (ArchiveFolder) entries.get(idx);
                continue;
            }

            final ArchiveFolder af = new ArchiveFolder();
            af.setParent(entry);
            af.setName(folders.get(i));
            entries.add(-idx - 1, af);

            entry = af;
        }

        entries = entry.getChilds();

        final int idx = findIDX(entries, folders.get(folders.size() - 1));

        archiveentry.setParent(entry);

        if (idx >= 0) {
        	final ArchiveEntry tmpentry = entries.get(idx);
        	
        	if(tmpentry instanceof ArchiveFolder){
                for (final ArchiveEntry entrytmp : ((ArchiveFolder)tmpentry).getChilds()) {
                    ((ArchiveFolder) archiveentry).addChild(entrytmp);
                }
        	}           
            entries.set(idx, archiveentry);
            return;
        }
        entries.add(-idx - 1, archiveentry);
    }

    private int findIDX(final List<ArchiveEntry> entries, final String search) {

        int i = 0;

        for (; i < entries.size(); i++) {

            final String tmpname = entries.get(i).getName();

            if (search.compareTo(tmpname) > 0) {
                continue;
            }
            if (search.compareTo(tmpname) < 0) {
                return -i - 1;
            }
            if (search.compareTo(tmpname) == 0) {
                return i;
            }
        }
        if (i == entries.size()) {
            return -i - 1;
        }
        return i;
    }

    private void checkLastChange(final ArchiveEntry archivefile) {
        if (lastchange < archivefile.getLastChange()) {
            lastchange = archivefile.getLastChange();
        }
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.listener = listener;
    }

    public void removePropertyChangeListener() {
        listener = null;
    }

    public void listeningFinished() {
        if (listener != null) {
            final PropertyChangeEvent evt = new PropertyChangeEvent(this, "archive", null, getFiles(null));
            listener.propertyChange(evt);
        }
    }

    public void resetModel() {
        for (final ArchiveEntry entry : root.getChilds()) {
            entry.setParent(null);
        }
        root.getChilds().clear();
        System.gc();
    }

    public short getVersions() {
        return lastchange;
    }

    @Override
    public String getPath(final ArchiveEntry currentdir) {
        if (currentdir == null) {
            return root.getName();
        }

        final StringBuilder sb = new StringBuilder(currentdir.getName());
        ArchiveEntry current = currentdir;

        while (current.getParentFolder() != null && current.getParentFolder() != root) {
            current = current.getParentFolder();
            sb.insert(0, current.getName());
        }

        return sb.toString();
    }

    private List<String> getSeparatedFolders(final String path) {

        final List<String> folders = new ArrayList<String>();

        for (int i = 0; i < path.length(); ) {
            final int idx = path.indexOf(SLASH, i);

            if (idx >= 0) {
                folders.add(path.substring(i, idx + 1));
                i = idx + 1;
                continue;
            }
            folders.add(path.substring(i));
            return folders;
        }

        return folders;
    }

    public boolean isHelperFolder(final ArchiveEntry entry) {
        if (!entry.isDirectory()) {
            return false;
        }

        return entry.getDate() == 0;
    }
}
