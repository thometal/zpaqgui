package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Singleton;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Creatable
@Singleton
public class FileSystemModel implements Model<File> {

    @Override
    public List<File> getFiles(final File f) {

        final File[] files;

        if (f == null) {
            files = File.listRoots();
        } else {
            files = f.listFiles();
        }

        if (files.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(files);
    }

    @Override
    public String getPath(final File f) {
        if (f == null) {
            return ".";
        }
        return f.getAbsolutePath();
    }
}
