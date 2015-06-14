package de.tt.zpaqgui.execution.cmdlinebuilder;

import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.Constants;
import de.tt.zpaqgui.execution.Option;
import de.tt.zpaqgui.model.ArchiveEntry;
import de.tt.zpaqgui.model.ArchiveFile;
import de.tt.zpaqgui.model.ArchiveModel;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Singleton;
import java.io.File;
import java.util.Date;
import java.util.List;

@Creatable
@Singleton
public class DecompressionCMDLineBuilder implements CMDLineBuilder {

    @Override
    public void buildCMDLine(final List<String> args, final CMDLineConfig config) {
        args.add(Command.EXTRACT.getString());
        args.add(config.getArchive().getAbsolutePath());

        writeListToExtract(args, config);

        if(!config.isTest()){
        	args.add(Option.TO.getString());

        	writeExtractionPath(args, config);
        }
        writeParameter(args, config);
    }

    private void writeParameter(final List<String> args, final CMDLineConfig config) {

        args.add(Option.UNTIL.getString());

        if (config.isVersionSet()) {
            args.add(String.valueOf(config.getUntilVersion()));
        } else {
            args.add(formatUntilDate(config.getUntilDate()));
        }

        args.add(Option.THREADS.getString());
        args.add(String.valueOf(config.getThreads()));

        if (config.isOverride()) {
            args.add(Option.FORCE.getString());
        }

        if(config.isTest()){
            args.add(Option.TEST.getString());
        }
    }

    private String formatUntilDate(final long untilDate) {
        final Date date = new Date(untilDate);

        return Constants.DATEFORMAT.format(date);
    }

    private void writeExtractionPath(final List<String> args, final CMDLineConfig config) {

        final String tmp = config.getExtractionFolder().getAbsolutePath();
        final String extractionpath;

        if (tmp.charAt(tmp.length() - 1) != File.separatorChar) {
            extractionpath = tmp + File.separator;
        } else {
            extractionpath = tmp;
        }

        final List<ArchiveFile> list = config.getSelectedArchiveEntries();

        if (list == null || list.isEmpty()) {
            args.add(extractionpath);
            return;
        }

        for (final ArchiveEntry archiveenty : list) {
            args.add(extractionpath + archiveenty.getName());
        }
    }

    private void writeListToExtract(final List<String> args, final CMDLineConfig config) {

        final List<ArchiveFile> list = config.getSelectedArchiveEntries();

        if (list == null || list.isEmpty()) {
            return;
        }

        for (final ArchiveEntry archiveentry : list) {
            args.add(((ArchiveModel) config.getModel()).getPath(archiveentry));
        }
    }
}
