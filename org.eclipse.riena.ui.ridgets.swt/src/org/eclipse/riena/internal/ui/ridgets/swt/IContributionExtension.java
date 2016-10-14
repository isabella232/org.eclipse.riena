package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * @since 6.2
 */
@ExtensionInterface(id = "org.eclipse.ui.menus")
public interface IContributionExtension {

	@MapName("toolbar")
	IToolbarExtension getToolBar();

	String getLocationURI();

	@ExtensionInterface
	public interface IToolbarExtension {

		@MapName("command")
		ICommandExtension[] getCommands();

	}

	@ExtensionInterface
	public interface ICommandExtension {
		String getId();

		String getLabel();

	}

}
