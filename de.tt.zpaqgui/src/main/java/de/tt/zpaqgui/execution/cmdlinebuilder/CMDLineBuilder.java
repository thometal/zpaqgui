package de.tt.zpaqgui.execution.cmdlinebuilder;

import de.tt.zpaqgui.execution.CMDLineConfig;

import java.util.List;

public interface CMDLineBuilder {

    void buildCMDLine(final List<String> args, final CMDLineConfig config);
}
