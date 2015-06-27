package de.tt.zpaqgui.execution.outputparser;

import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Constants;
import de.tt.zpaqgui.model.ArchiveEntry;
import de.tt.zpaqgui.model.ArchiveFile;
import de.tt.zpaqgui.model.ArchiveFolder;
import de.tt.zpaqgui.model.ArchiveModel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;

import java.text.ParseException;

@Creatable
public class ListOutputParser extends OutputParserBase {

    private ArchiveModel model;

    @Override
    public void setProgressMonitor(final IProgressMonitor monitor) {

    }

    @Override
    public void parseOutput(final String line) {
        super.parseOutput(line);
        if (line.isEmpty() || line.charAt(0) != '-' || line.contains(" -> ")) {
            return;
        }

        final String date = line.substring(2, 23);
        
        if(date.trim().isEmpty()){
        	return;
        }
        
        final boolean isdir = line.charAt(35) == 'd' || line.substring(35, 42).contains("D");

        long size = parseSize(line.substring(22, 35));
        
        String path = line.substring(41);
        int versionseparator = path.indexOf('/');
        short version = Short.parseShort(path.substring(0,versionseparator));
        
        final ArchiveEntry entry;

        if (isdir) {
            entry = new ArchiveFolder();
        } else {
            entry = new ArchiveFile();
        }

        entry.setDate(parseDate(date));
        entry.setSize(size);
       
        entry.setLastChange(version);        
        model.add(path.substring(versionseparator+1), entry);
    }

    private long parseDate(final String string) {
        try {
            return Constants.DATEFORMAT.parse(string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long parseSize(final String size) {
        try {
            return Long.parseLong(size.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setZPAQCMDLineConfig(final CMDLineConfig config) {
        model = ((ArchiveModel) config.getModel());
    }

    @Override
    public void setDuringProcessValues(final String text, final int items) {

    }
}
