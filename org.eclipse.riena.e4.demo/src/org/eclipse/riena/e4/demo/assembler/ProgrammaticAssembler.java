package org.eclipse.riena.e4.demo.assembler;

import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class ProgrammaticAssembler extends AbstractNavigationAssembler {
	private static final String MY_ID = "org.eclipse.riena.e4.demo.assembly22";
	private static final String MY_VIEW_ID = "org.eclipse.riena.e4.demo.view4";
	private static final String MY_PERSPECTIVE_ID = "org.eclipse.riena.e4.demo.perspective3";

	private static final String MODULE = "module";
	private static final String MODULE_GROUP = "moduleGroup";
	private static final String SUBMODULE = "subModule";

	public INavigationNode<?>[] buildNode(NavigationNodeId nodeId,
			NavigationArgument navigationArgument) {

		SubApplicationNode result = null;
		if (MY_ID.equals(nodeId.getTypeId())) {
			result = new SubApplicationNode(nodeId,
					"Sub-Application (MyAssembler.java)");
			WorkareaManager.getInstance().registerDefinition(result,
					MY_PERSPECTIVE_ID);

			ModuleGroupNode moduleGroupNode = new ModuleGroupNode(
					new NavigationNodeId(MODULE_GROUP));
			result.addChild(moduleGroupNode);

			ModuleNode module = new ModuleNode(new NavigationNodeId(MODULE),
					"Modul (MyAssembler.java)");
			moduleGroupNode.addChild(module);

			SubModuleNode subModuleNode = new SubModuleNode(
					new NavigationNodeId(SUBMODULE),
					"Sub-Modul (MyAssembler.java)");
			WorkareaManager.getInstance().registerDefinition(subModuleNode,
					MY_VIEW_ID);
			module.addChild(subModuleNode);
		}

		return result == null ? null : new INavigationNode<?>[] { result };
	}

	public boolean acceptsToBuildNode(NavigationNodeId nodeId,
			NavigationArgument argument) {
		return MY_ID.equals(nodeId.getTypeId());
	}

}
