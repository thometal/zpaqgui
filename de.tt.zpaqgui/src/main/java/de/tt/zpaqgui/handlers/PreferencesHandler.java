package de.tt.zpaqgui.handlers;

import java.util.List;

import de.tt.zpaqgui.ZPAQSettings;
import de.tt.zpaqgui.dialogs.PreferencesDialog;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Named;

@SuppressWarnings("restriction")
public class PreferencesHandler {

    @Execute
    public void execute(@Preference(nodePath = "de.tt.zpaqgui") IEclipsePreferences prefs, UISynchronize sync,
                        @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, ZPAQSettings settings,
                        final EModelService modelService, final MApplication application) {

        final PreferencesDialog dialog = new PreferencesDialog(shell);
        dialog.setPreferences(prefs);
        dialog.setUISynchronize(sync);
        dialog.setSettings(settings);
        
        dialog.setBlockOnOpen(true);
        dialog.open();
        	
        final List<MPartStack> stacks = modelService.findElements(application, "de.tt.zpaqgui.partstack.utilsstack",
                    MPartStack.class, null);

        final List<MStackElement> stackelements = stacks.get(0).getChildren();
        
        if (settings.isConsoleEnabled()){
        	if (stackelements.size() == 0){
	        	final MPart newpart = MBasicFactory.INSTANCE.createPart();
	            newpart.setElementId("Console");
	            newpart.setLabel("Console");
	            newpart.setContributionURI("bundleclass://de.tt.zpaqgui/de.tt.zpaqgui.parts.ConsolePart");
	            newpart.setCloseable(true);
	            newpart.setOnTop(true);
	            stackelements.add(newpart);
        	}
            stacks.get(0).setToBeRendered(true);
            stackelements.get(0).setToBeRendered(true);
            stackelements.get(0).setOnTop(true);
        } else{
        	if (stackelements.size() != 0){
        		final  MStackElement element = stackelements.get(0);
        	
        		stackelements.remove(element);
        	}
        	stacks.get(0).setToBeRendered(false);
        }       
    }
}
