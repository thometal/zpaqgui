package de.tt.zpaqgui.model;

import java.util.List;

public interface Model<T> {
    List<T> getFiles(final T type);

    String getPath(final T type);
}
