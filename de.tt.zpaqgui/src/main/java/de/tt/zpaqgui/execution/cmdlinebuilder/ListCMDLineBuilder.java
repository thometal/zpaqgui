package de.tt.zpaqgui.execution.cmdlinebuilder;

import de.tt.zpaqgui.execution.CMDLineConfig;
import de.tt.zpaqgui.execution.Command;
import de.tt.zpaqgui.execution.Option;

import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Singleton;

import java.util.List;

@Creatable
@Singleton
public class ListCMDLineBuilder implements CMDLineBuilder {

    @Override
    public void buildCMDLine(List<String> args, CMDLineConfig config) {
        args.add(Command.LIST.getString());

        args.add(config.getArchive().getAbsolutePath());
        args.add(Option.ALL.getString());

        final short untilversion = config.getUntilVersion();
        final short sinceversion = config.getSinceVersion();
        
        if(untilversion != 0 && sinceversion != 0){
        	args.add(Option.ONLY.getString());
        
	        for(short i = sinceversion; i <= untilversion;i++){
	        	args.add(String.format("%04d",i));
	        }
        }
    }
}
