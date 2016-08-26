/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.example.client.controllers.BlockingSubModuleController;
import org.eclipse.riena.example.client.controllers.CComboSubModuleController;
import org.eclipse.riena.example.client.controllers.CarCatalogSubModuleController;
import org.eclipse.riena.example.client.controllers.CheckBoxTableSubModuleController;
import org.eclipse.riena.example.client.controllers.ChoiceSubModuleController;
import org.eclipse.riena.example.client.controllers.ComboAndChoiceSubModuleController;
import org.eclipse.riena.example.client.controllers.ComboSubModuleController;
import org.eclipse.riena.example.client.controllers.CompletionComboSubModuleController;
import org.eclipse.riena.example.client.controllers.ContextMenuSubModuleController;
import org.eclipse.riena.example.client.controllers.ControllerTestsPlaygroundSubModuleController;
import org.eclipse.riena.example.client.controllers.CustomMarkerSubModuleController;
import org.eclipse.riena.example.client.controllers.DateTimeSubModuleController;
import org.eclipse.riena.example.client.controllers.DefaultButtonSubModuleController;
import org.eclipse.riena.example.client.controllers.DetachedSubModuleController;
import org.eclipse.riena.example.client.controllers.DialogSubModuleController;
import org.eclipse.riena.example.client.controllers.FocusableSubModuleController;
import org.eclipse.riena.example.client.controllers.InfoFlyoutSubModuleController;
import org.eclipse.riena.example.client.controllers.LayoutSubModuleController;
import org.eclipse.riena.example.client.controllers.LinkSubModuleController;
import org.eclipse.riena.example.client.controllers.ListSubModuleController;
import org.eclipse.riena.example.client.controllers.ListUsingTableSubModuleController;
import org.eclipse.riena.example.client.controllers.MarkerHidingSubModuleController;
import org.eclipse.riena.example.client.controllers.MarkerSubModuleController;
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController;
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController2;
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController3;
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController4;
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController5;
import org.eclipse.riena.example.client.controllers.MessageBoxSubModuleController;
import org.eclipse.riena.example.client.controllers.MessageMarkerSubModuleController;
import org.eclipse.riena.example.client.controllers.RidgetsSubModuleController;
import org.eclipse.riena.example.client.controllers.StatuslineSubModuleController;
import org.eclipse.riena.example.client.controllers.SvgSubModuleController;
import org.eclipse.riena.example.client.controllers.SystemPropertiesSubModuleController;
import org.eclipse.riena.example.client.controllers.TableSubModuleController;
import org.eclipse.riena.example.client.controllers.TextDateSubModuleController;
import org.eclipse.riena.example.client.controllers.TextNumericSubModuleController;
import org.eclipse.riena.example.client.controllers.TextSubModuleController;
import org.eclipse.riena.example.client.controllers.TraverseSubModuleController;
import org.eclipse.riena.example.client.controllers.TreeSubModuleController;
import org.eclipse.riena.example.client.controllers.TreeTableSubModuleController;
import org.eclipse.riena.example.client.controllers.ValidationSubModuleController;
import org.eclipse.riena.example.client.views.BlockingSubModuleView;
import org.eclipse.riena.example.client.views.CComboSubModuleView;
import org.eclipse.riena.example.client.views.CarCatalogSubModuleView;
import org.eclipse.riena.example.client.views.CheckBoxTableSubModuleView;
import org.eclipse.riena.example.client.views.ChoiceSubModuleView;
import org.eclipse.riena.example.client.views.ComboAndChoiceSubModuleView;
import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.example.client.views.CompletionComboSubModuleView;
import org.eclipse.riena.example.client.views.ContextMenuSubModuleView;
import org.eclipse.riena.example.client.views.ControllerTestsPlaygroundSubModuleView;
import org.eclipse.riena.example.client.views.CustomMarkerSubModuleView;
import org.eclipse.riena.example.client.views.DateTimeSubModuleView;
import org.eclipse.riena.example.client.views.DefaultButtonSubModuleView;
import org.eclipse.riena.example.client.views.DetachedSubModuleView;
import org.eclipse.riena.example.client.views.DialogSubModuleView;
import org.eclipse.riena.example.client.views.DpiLayoutSubModuleView;
import org.eclipse.riena.example.client.views.FocusableSubModuleView;
import org.eclipse.riena.example.client.views.InfoFlyoutSubModuleView;
import org.eclipse.riena.example.client.views.LayoutSubModuleView;
import org.eclipse.riena.example.client.views.LinkSubModuleView;
import org.eclipse.riena.example.client.views.ListSubModuleView;
import org.eclipse.riena.example.client.views.ListUsingTableSubModuleView;
import org.eclipse.riena.example.client.views.MarkerHidingSubModuleView;
import org.eclipse.riena.example.client.views.MarkerSubModuleView;
import org.eclipse.riena.example.client.views.MarkerSubModuleWithAlternativeBackgroundView;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView2;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView3;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView4;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView5;
import org.eclipse.riena.example.client.views.MessageBoxSubModuleView;
import org.eclipse.riena.example.client.views.MessageMarkerSubModuleView;
import org.eclipse.riena.example.client.views.NoControllerSubModuleView;
import org.eclipse.riena.example.client.views.RidgetsSubModuleView;
import org.eclipse.riena.example.client.views.StatuslineSubModuleView;
import org.eclipse.riena.example.client.views.SvgSubModuleView;
import org.eclipse.riena.example.client.views.SystemPropertiesSubModuleView;
import org.eclipse.riena.example.client.views.TableSubModuleView;
import org.eclipse.riena.example.client.views.TextDateSubModuleView;
import org.eclipse.riena.example.client.views.TextNumericSubModuleView;
import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.example.client.views.TraverseSubModuleView;
import org.eclipse.riena.example.client.views.TreeSubModuleView;
import org.eclipse.riena.example.client.views.TreeTableSubModuleView;
import org.eclipse.riena.example.client.views.ValidationSubModuleView;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.AttentionMarker;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Configures the contents of the 'Playground' module.
 * <p>
 * This class is contributed via the plugin.xml (assembly extension).
 */
public class PlaygroundNodeAssembler extends AbstractNavigationAssembler {

	private Set<String> knownTargetIds = null;

	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {

		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList("org.eclipse.riena.example.playground", //$NON-NLS-1$
					"org.eclipse.riena.example.buttons", //$NON-NLS-1$
					"org.eclipse.riena.example.choice", //$NON-NLS-1$
					"org.eclipse.riena.example.combo", //$NON-NLS-1$
					"org.eclipse.riena.example.ccombo", //$NON-NLS-1$
					"org.eclipse.riena.example.list", //$NON-NLS-1$
					"org.eclipse.riena.example.text", //$NON-NLS-1$
					"org.eclipse.riena.example.text.numeric", //$NON-NLS-1$
					"org.eclipse.riena.example.text.date", //$NON-NLS-1$
					"org.eclipse.riena.example.marker", //$NON-NLS-1$
					"org.eclipse.riena.example.messagemarker", //$NON-NLS-1$
					"org.eclipse.riena.example.focusable", //$NON-NLS-1$
					"org.eclipse.riena.example.validation", //$NON-NLS-1$
					"org.eclipse.riena.example.tree", //$NON-NLS-1$
					"org.eclipse.riena.example.treeTable", //$NON-NLS-1$
					"org.eclipse.riena.example.table", //$NON-NLS-1$
					"org.eclipse.riena.example.systemProperties", //$NON-NLS-1$
					"org.eclipse.riena.example.statusLine", //$NON-NLS-1$
					"org.eclipse.riena.example.blocking", //$NON-NLS-1$
					"org.eclipse.riena.example.noController", //$NON-NLS-1$
					"org.eclipse.riena.example.dialog", //$NON-NLS-1$
					"org.eclipse.riena.example.messageBox", //$NON-NLS-1$
					"org.eclipse.riena.example.svgGround" //$NON-NLS-1$
			));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}

	public INavigationNode<?>[] buildNode(final NavigationNodeId navigationNodeId, final NavigationArgument navigationArgument) {

		final WorkareaManager workarea = WorkareaManager.getInstance();
		final IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);

		final IModuleNode playgroundModule = new ModuleNode(new NavigationNodeId("playgroundModule"), "Playground"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(playgroundModule);

		final ISubModuleNode blockingSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.blocking"), "Blocking"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(blockingSubModule, BlockingSubModuleController.class, BlockingSubModuleView.ID, false);
		playgroundModule.addChild(blockingSubModule);

		final ISubModuleNode buttonsSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.buttons"), "Buttons"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(buttonsSubModule, RidgetsSubModuleController.class, RidgetsSubModuleView.ID, false);
		buttonsSubModule.setClosable(true);
		playgroundModule.addChild(buttonsSubModule);

		final ISubModuleNode choiceSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.choice"), "Choice"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(choiceSubModule, ChoiceSubModuleController.class, ChoiceSubModuleView.class.getName(), false);
		choiceSubModule.setClosable(true);
		playgroundModule.addChild(choiceSubModule);

		final ISubModuleNode comboFolderSubModule = buildComboNodes();
		playgroundModule.addChild(comboFolderSubModule);

		final ISubModuleNode contextMenuSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.contextMenu"), "Context Menu"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(contextMenuSubModule, ContextMenuSubModuleController.class, ContextMenuSubModuleView.ID, false);
		playgroundModule.addChild(contextMenuSubModule);

		final ISubModuleNode controllerTestsPlaygroundSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.controllerTestsPlayground"), "Controller Tests Playground"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(controllerTestsPlaygroundSubModule, ControllerTestsPlaygroundSubModuleController.class,
				ControllerTestsPlaygroundSubModuleView.ID);
		playgroundModule.addChild(controllerTestsPlaygroundSubModule);

		final ISubModuleNode dateTimeSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.datetime"), "DateTime"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(dateTimeSubModule, DateTimeSubModuleController.class, DateTimeSubModuleView.ID, false);
		playgroundModule.addChild(dateTimeSubModule);

		final ISubModuleNode defaultButtonSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.defaultbutton"), "Default Button"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(defaultButtonSubModule, DefaultButtonSubModuleController.class, DefaultButtonSubModuleView.ID, false);
		playgroundModule.addChild(defaultButtonSubModule);

		final ISubModuleNode dialogSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.dialog"), "Dialog"); //$NON-NLS-1$ //$NON-NLS-2$
		dialogSubModule.setToolTipText("Tool Tip of\nthe sub-module \"dialog\""); //$NON-NLS-1$
		workarea.registerDefinition(dialogSubModule, DialogSubModuleController.class, DialogSubModuleView.ID, false);
		playgroundModule.addChild(dialogSubModule);

		final ISubModuleNode detachedSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.detached"), "Detached View"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(detachedSubModule, DetachedSubModuleController.class, DetachedSubModuleView.ID, false);
		playgroundModule.addChild(detachedSubModule);

		final ISubModuleNode focusableSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.focusable"), "Focusable"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(focusableSubModule, FocusableSubModuleController.class, FocusableSubModuleView.ID, false);
		playgroundModule.addChild(focusableSubModule);

		final ISubModuleNode flyoutSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.flyout"), "InfoFlyout"); //$NON-NLS-1$//$NON-NLS-2$
		workarea.registerDefinition(flyoutSubModule, InfoFlyoutSubModuleController.class, InfoFlyoutSubModuleView.ID, false);
		playgroundModule.addChild(flyoutSubModule);

		final ISubModuleNode layoutSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.layout"), "Layout"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(layoutSubModule, LayoutSubModuleController.class, LayoutSubModuleView.ID, false);
		playgroundModule.addChild(layoutSubModule);

		final ISubModuleNode dpiLayoutSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.dpiLayout"), "DPI Layout"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(dpiLayoutSubModule, DpiLayoutSubModuleView.ID);
		playgroundModule.addChild(dpiLayoutSubModule);

		final ISubModuleNode linkSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.link"), "Link and Browser"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(linkSubModule, LinkSubModuleController.class, LinkSubModuleView.ID, false);
		playgroundModule.addChild(linkSubModule);

		final ISubModuleNode listSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.list"), "List"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(listSubModule, ListSubModuleController.class, ListSubModuleView.ID, false);
		playgroundModule.addChild(listSubModule);

		final ISubModuleNode listUsingTableSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.listUsingTable"), "List (using Table)"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(listUsingTableSubModule, ListUsingTableSubModuleController.class, ListUsingTableSubModuleView.ID, false);
		playgroundModule.addChild(listUsingTableSubModule);

		final ISubModuleNode masterDetailsFolderSubModule = buildMasterDetailsNodes();
		playgroundModule.addChild(masterDetailsFolderSubModule);

		final ISubModuleNode messageBoxSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.messageBox"), "Message Box"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(messageBoxSubModule, MessageBoxSubModuleController.class, MessageBoxSubModuleView.ID, false);
		playgroundModule.addChild(messageBoxSubModule);

		final ISubModuleNode tableFolderSubModule = buildTableNodes();
		playgroundModule.addChild(tableFolderSubModule);

		final ISubModuleNode textFolderSubModule = buildTextNodes();
		playgroundModule.addChild(textFolderSubModule);

		final ISubModuleNode traverseSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.traverse"), "Traverse"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(traverseSubModule, TraverseSubModuleController.class, TraverseSubModuleView.ID, false);
		playgroundModule.addChild(traverseSubModule);

		final ISubModuleNode treeSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.tree"), "Tree"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(treeSubModule, TreeSubModuleController.class, TreeSubModuleView.ID, false);
		playgroundModule.addChild(treeSubModule);

		final ISubModuleNode treeTableSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.treeTable"), "Tree Table"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(treeTableSubModule, TreeTableSubModuleController.class, TreeTableSubModuleView.ID, false);
		playgroundModule.addChild(treeTableSubModule);

		final ISubModuleNode systemPropertiesSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.systemProperties"), "System Properties"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(systemPropertiesSubModule, SystemPropertiesSubModuleController.class, SystemPropertiesSubModuleView.ID, false);
		playgroundModule.addChild(systemPropertiesSubModule);

		final ISubModuleNode statusLineSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.statusLine"), "Statusline"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(statusLineSubModule, StatuslineSubModuleController.class, StatuslineSubModuleView.ID, false);
		playgroundModule.addChild(statusLineSubModule);

		final ISubModuleNode validationSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.validation"), "Validation"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(validationSubModule, ValidationSubModuleController.class, ValidationSubModuleView.ID, false);
		playgroundModule.addChild(validationSubModule);

		final ISubModuleNode svgSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.svgGround"), "SVG Icons"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(svgSubModule, SvgSubModuleController.class, SvgSubModuleView.ID, false);
		playgroundModule.addChild(svgSubModule);

		final ISubModuleNode noControllerSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.noController"), //$NON-NLS-1$
				"View without Controller"); //$NON-NLS-1$
		workarea.registerDefinition(noControllerSubModule, NoControllerSubModuleView.ID);
		playgroundModule.addChild(noControllerSubModule);

		buildMarkerNodes(moduleGroup);

		return new IModuleGroupNode[] { moduleGroup };
	}

	// helping methods
	//////////////////

	/**
	 * Creates a sub-module with child nodes that demonstrate the usage of combo Ridgets.
	 */
	private ISubModuleNode buildComboNodes() {
		final WorkareaManager workarea = WorkareaManager.getInstance();

		final ISubModuleNode result = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.comboFolder"), "Combos"); //$NON-NLS-1$ //$NON-NLS-2$
		result.setSelectable(false);

		final ISubModuleNode comboSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.combo"), "Combo"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(comboSubModule, ComboSubModuleController.class, ComboSubModuleView.ID, false);
		result.addChild(comboSubModule);

		final ISubModuleNode ccomboSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.ccombo"), "CCombo"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(ccomboSubModule, CComboSubModuleController.class, CComboSubModuleView.ID).setRequiredPreparation(true);
		result.addChild(ccomboSubModule);

		final ISubModuleNode comboCompletionSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.completioncombo"), "CompletionCombo"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(comboCompletionSubModule, CompletionComboSubModuleController.class, CompletionComboSubModuleView.ID, false);
		result.addChild(comboCompletionSubModule);

		final ISubModuleNode comboAndChoiceSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.different"), "Combo and Choice"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(comboAndChoiceSubModule, ComboAndChoiceSubModuleController.class, ComboAndChoiceSubModuleView.ID, false);
		result.addChild(comboAndChoiceSubModule);

		return result;
	}

	/**
	 * Creates a sub-module with child nodes that demonstrate the usage of text Ridgets.
	 * 
	 * @return folder sub-module
	 */
	private ISubModuleNode buildTextNodes() {
		final WorkareaManager workarea = WorkareaManager.getInstance();

		final ISubModuleNode textFolderSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.textFolder"), "Text"); //$NON-NLS-1$ //$NON-NLS-2$
		textFolderSubModule.setSelectable(false);

		final ISubModuleNode textSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text"), "Text"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(textSubModule, TextSubModuleController.class, TextSubModuleView.ID, false).setRequiredPreparation(true);
		textFolderSubModule.addChild(textSubModule);

		final ISubModuleNode textNumbersSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text.numeric"), "Text (Numeric)"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(textNumbersSubModule, TextNumericSubModuleController.class, TextNumericSubModuleView.ID, false)
				.setRequiredPreparation(true);
		textFolderSubModule.addChild(textNumbersSubModule);

		final ISubModuleNode textDateSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text.date"), "Text (Date)"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(textDateSubModule, TextDateSubModuleController.class, TextDateSubModuleView.ID, false).setRequiredPreparation(true);
		textFolderSubModule.addChild(textDateSubModule);

		return textFolderSubModule;
	}

	/**
	 * Creates a sub-module with child nodes that demonstrate the usage of TableRidgets.
	 * 
	 * @return folder sub-module
	 */
	private ISubModuleNode buildTableNodes() {
		final WorkareaManager workarea = WorkareaManager.getInstance();

		final ISubModuleNode tableFolderSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.tableFolder"), "Table"); //$NON-NLS-1$ //$NON-NLS-2$
		tableFolderSubModule.setSelectable(false);

		final ISubModuleNode tableSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.table"), "Table"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(tableSubModule, TableSubModuleController.class, TableSubModuleView.ID, false);
		tableFolderSubModule.addChild(tableSubModule);

		final ISubModuleNode carCatalogSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.edtiableTable"), "Editable Table"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(carCatalogSubModule, CarCatalogSubModuleController.class, CarCatalogSubModuleView.ID, false);
		tableFolderSubModule.addChild(carCatalogSubModule);

		final ISubModuleNode checkBoxTableSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.checkBoxTableSubModule"), "CheckBox"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(checkBoxTableSubModule, CheckBoxTableSubModuleController.class, CheckBoxTableSubModuleView.ID, false);
		tableFolderSubModule.addChild(checkBoxTableSubModule);

		return tableFolderSubModule;
	}

	/**
	 * Creates a sub-module with child nodes that demonstrate the usage of master-details Ridgets.
	 * 
	 * @return folder sub-module
	 */
	private ISubModuleNode buildMasterDetailsNodes() {
		final WorkareaManager workarea = WorkareaManager.getInstance();

		final ISubModuleNode result = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterDetailsFolder"), "Master/Details"); //$NON-NLS-1$ //$NON-NLS-2$
		result.setSelectable(false);

		final ISubModuleNode mdSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterdetails"), "Master/Details"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(mdSubModule, MasterDetailsSubModuleController.class, MasterDetailsSubModuleView.ID, false);
		result.addChild(mdSubModule);

		final ISubModuleNode mdSubModule2 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterdetails2"), "Master/Details II"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(mdSubModule2, MasterDetailsSubModuleController2.class, MasterDetailsSubModuleView2.ID, false);
		result.addChild(mdSubModule2);

		final ISubModuleNode mdSubModule3 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterdetails3"), "Master/Details III"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(mdSubModule3, MasterDetailsSubModuleController3.class, MasterDetailsSubModuleView3.ID, false);
		result.addChild(mdSubModule3);

		final ISubModuleNode mdSubModule4 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterdetails4"), "Master/Details IV"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(mdSubModule4, MasterDetailsSubModuleController4.class, MasterDetailsSubModuleView4.ID, false);
		result.addChild(mdSubModule4);

		final ISubModuleNode mdSubModule5 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.masterdetails5"), "Master/Details V"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(mdSubModule5, MasterDetailsSubModuleController5.class, MasterDetailsSubModuleView5.ID, false);
		result.addChild(mdSubModule5);

		return result;
	}

	/**
	 * Creates a module for markers.
	 * <p>
	 * The children of the module are all sub-modules to demonstrate markers.
	 * 
	 * @param moduleGroup
	 *            parent
	 * @return new module
	 */
	private IModuleNode buildMarkerNodes(final IModuleGroupNode moduleGroup) {

		final WorkareaManager workarea = WorkareaManager.getInstance();

		final IModuleNode markerModule = new ModuleNode(new NavigationNodeId("markerModule"), "Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(markerModule);

		final ISubModuleNode markerSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.marker"), "Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(markerSubModule, MarkerSubModuleController.class, MarkerSubModuleView.ID, true);
		markerModule.addChild(markerSubModule);
		markerSubModule.addMarker(new AttentionMarker());

		final ISubModuleNode markerSubModule2 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.marker2"), "Marker2"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(markerSubModule2, MarkerSubModuleController.class, MarkerSubModuleView.ID, true);
		markerModule.addChild(markerSubModule2);
		markerSubModule2.addMarker(new AttentionMarker());

		final ISubModuleNode markerSubModuleAlternativeBackground = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.markerAlternativeBackground"), "Marker with alternative background (used with alternative LnF)"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(markerSubModuleAlternativeBackground, MarkerSubModuleController.class, MarkerSubModuleWithAlternativeBackgroundView.ID,
				true);
		markerModule.addChild(markerSubModuleAlternativeBackground);
		markerSubModuleAlternativeBackground.addMarker(new AttentionMarker());

		final ISubModuleNode customMarkerSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.custommarker"), "Custom Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(customMarkerSubModule, CustomMarkerSubModuleController.class, CustomMarkerSubModuleView.ID, true);
		markerModule.addChild(customMarkerSubModule);

		final ISubModuleNode markerHidingSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.markerhiding"), "Marker Hiding"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(markerHidingSubModule, MarkerHidingSubModuleController.class, MarkerHidingSubModuleView.ID, false);
		markerModule.addChild(markerHidingSubModule);

		final ISubModuleNode messageMarkerSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.messagemarker"), "Message Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		workarea.registerDefinition(messageMarkerSubModule, MessageMarkerSubModuleController.class, MessageMarkerSubModuleView.ID).setRequiredPreparation(true);
		markerModule.addChild(messageMarkerSubModule);

		return markerModule;

	}

}
