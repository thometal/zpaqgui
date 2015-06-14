package de.tt.zpaqgui.execution.cmdlinebuilder;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.execution.CMDLineConfig;
import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Creatable
@Singleton
public class CMDLineBuilderFactory {

    @Inject
    private ZPAQSettings settings;

    @Inject
    private ListCMDLineBuilder lcmdmodel;

    @Inject
    private DecompressionCMDLineBuilder extractmodel;

    @Inject
    private CompressionCMDLineBuilder addmodel;

    public List<String> buildCMDLine(final CMDLineConfig config) {

        final CMDLineBuilder model;

        switch (config.getZpaqCommand()) {
            case LIST:
                model = lcmdmodel;
                break;
            case EXTRACT:
                model = extractmodel;
                break;
            case ADD:
                model = addmodel;
                break;
            default:
                return null;
        }

        final List<String> args = new ArrayList<>();
        args.add(settings.getZpaqLocation().getAbsolutePath());

        model.buildCMDLine(args, config);

        return args;
    }
}
