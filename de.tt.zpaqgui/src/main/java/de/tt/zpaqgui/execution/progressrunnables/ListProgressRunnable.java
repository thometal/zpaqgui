package de.tt.zpaqgui.execution.progressrunnables;

import de.tt.zpaqgui.execution.outputparser.ListOutputParser;
import de.tt.zpaqgui.model.ArchiveModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import javax.inject.Inject;

import java.util.List;

@Creatable
public class ListProgressRunnable extends ProgressRunnableBase {

    @Inject
    private EPartService partService;

    @Inject
    private EModelService modelService;

    @Inject
    private MApplication application;

    @Inject
    private UISynchronize sync;

    @Inject
    public void setOutputParser(ListOutputParser listoutputparser) {
        this.outputparser = listoutputparser;
    }

    @Override
    boolean beforeStart(IProgressMonitor monitor) {

        ((ArchiveModel) config.getModel()).resetModel();

        final MPart existingpart = partService.findPart(config.getArchive().getAbsolutePath());

        ((ListOutputParser) outputparser).setZPAQCMDLineConfig(config);

        if (existingpart != null) {
            existingpart.setObject(config);
            sync.syncExec(() -> partService.showPart(existingpart, PartState.ACTIVATE));
            return false;
        }

        monitor.beginTask("Loading Archive...", IProgressMonitor.UNKNOWN);

        final MPart newpart = MBasicFactory.INSTANCE.createPart();
        newpart.setElementId(config.getArchive().getAbsolutePath());
        newpart.setLabel(config.getArchive().getName());
        newpart.setContributionURI("bundleclass://de.tt.zpaqgui/de.tt.zpaqgui.parts.ArchivePart");
        newpart.setCloseable(true);
        newpart.setOnTop(true);
        final List<MPartStack> stacks = modelService.findElements(application, "de.tt.zpaqgui.partstack.archivestack",
                MPartStack.class, null);

        stacks.get(0).getChildren().add(newpart);

        newpart.setObject(config);

        sync.asyncExec(() -> partService.showPart(newpart, PartState.ACTIVATE));
        return false;
    }

    @Override
    void beforeEnd(IProgressMonitor monitor) {
        ((ArchiveModel) config.getModel()).listeningFinished();
    }
}
