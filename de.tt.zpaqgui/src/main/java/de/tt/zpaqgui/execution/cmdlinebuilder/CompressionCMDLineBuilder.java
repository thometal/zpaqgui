package de.tt.zpaqgui.execution.cmdlinebuilder;

import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.Option;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Singleton;
import java.io.File;
import java.util.List;

@Creatable
@Singleton
public class CompressionCMDLineBuilder implements CMDLineBuilder {

    @Override
    public void buildCMDLine(final List<String> args, final CMDLineConfig config) {
        args.add(Command.ADD.getString());
        args.add(config.getArchive().getAbsolutePath());

        writeNewFileEntries(args, config);

        writeParameter(args, config);
    }

    private void writeNewFileEntries(final List<String> args, final CMDLineConfig config) {

        final List<File> selectedfiles = config.getSelectedFileEntries();

        for (final File file : selectedfiles) {
            args.add(file.getAbsolutePath());
        }
    }

    private void writeParameter(final List<String> args, final CMDLineConfig config) {

        args.add(Option.METHOD.getString());

        final int blocksize = config.getBlockSize();

        if (blocksize == -1) {
            args.add(config.getMethod());
        } else {
            args.add(config.getMethod() + config.getBlockSize());
        }


        args.add(Option.THREADS.getString());
        args.add(String.valueOf(config.getThreads()));


        final int fragmentsize = config.getFragmentSize();

        if (fragmentsize == -1) {
            return;
        }

        args.add(Option.FRAGMENT.getString());
        args.add(String.valueOf(fragmentsize));
    }
}
