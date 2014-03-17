package org.eclipse.riena.example.client.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.example.client.views.SharedBrowserView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Demonstrates browser instance sharing without losing the browser session
 */
public class SharedBrowserController extends SubModuleController {

	private static final String SHARED_BROWSERS_MODEL = "sharedBrowsers.model"; //$NON-NLS-1$

	static class Model {
		private String browserURL;

		public String getBrowserURL() {
			return browserURL;
		}

		public void setBrowserURL(final String browserURL) {
			this.browserURL = browserURL;
		}
	}

	private int personCounter;
	private IBrowserRidget browser;

	public SharedBrowserController() {
		// TODO Auto-generated constructor stub
	}

	public SharedBrowserController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		super.configureRidgets();

		browser = getRidget(IBrowserRidget.class, "browser"); //$NON-NLS-1$
		final Model model = (Model) getNavigationNode().getParentOfType(IModuleGroupNode.class).getContext(SHARED_BROWSERS_MODEL);

		if (model == null) {
			// we're on the title page
			browser.setUrl(getHtmlPageURL("sharedbrowser_title.html")); //$NON-NLS-1$
			browser.mapScriptFunction("jsCreateModuleGroup", this); //$NON-NLS-1$
		} else {
			// we're somewhere in the webapp
			browser.bindToModel(model, "browserURL"); //$NON-NLS-1$
			browser.updateFromModel();
			browser.mapScriptFunction("theJavaFunction", this); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		browser.execute("appendtolog('node activated: " + getNavigationNode().getNodeId().getInstanceId() + "');"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This function is available in the browser widget and can be called from JavaScript
	 */
	public Object theJavaFunction(final List<Object> args) {
		final String idClicked = (String) args.get(0);
		final IModuleNode parent = getNavigationNode().getParentOfType(IModuleNode.class);
		if ("btn1".equals(idClicked)) {
			parent.getChild(0).activate();
		} else {
			parent.getChild(1).activate();
		}
		return null;
	}

	/**
	 * This function is available in the browser widget and can be called from JavaScript
	 */
	public Object jsCreateModuleGroup(final List<Object> args) {
		final String name = (String) args.get(0);

		final ModuleGroupNode group = new ModuleGroupNode();
		group.setContext("shared.views.context", name);
		final ModuleNode module = new ModuleNode("Shared Brower (" + name + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		group.addChild(module);

		final Model model = new Model();
		model.setBrowserURL(getHtmlPageURL("sharedbrowser_webapp.html")); //$NON-NLS-1$
		group.setContext(SHARED_BROWSERS_MODEL, model);

		getNavigationNode().getParentOfType(ISubApplicationNode.class).addChild(group);

		ISubModuleNode nextSubModuleNode = new SubModuleNode(new NavigationNodeId("onePerson", Integer //$NON-NLS-1$
				.toString(personCounter++)), "Page 1"); //$NON-NLS-1$
		module.addChild(nextSubModuleNode);
		WorkareaManager.getInstance().registerDefinition(nextSubModuleNode, getClass(), SharedBrowserView.class.getName(), true).setRequiredPreparation(true);
		nextSubModuleNode = new SubModuleNode(new NavigationNodeId("onePerson", Integer //$NON-NLS-1$
				.toString(personCounter++)), "Page 2"); //$NON-NLS-1$
		module.addChild(nextSubModuleNode);
		WorkareaManager.getInstance().registerDefinition(nextSubModuleNode, getClass(), SharedBrowserView.class.getName(), true).setRequiredPreparation(true);
		group.activate();

		return null;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private String getHtmlPageURL(final String name) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = getClass().getResourceAsStream(name);
			final File tempFile = File.createTempFile(name, ".html"); //$NON-NLS-1$
			os = new FileOutputStream(tempFile);
			IOUtils.copy(is, os);
			return tempFile.toURI().toURL().toString();
		} catch (final IOException e) {
			throw new MurphysLawFailure("", e); //$NON-NLS-1$
		} finally {
			IOUtils.close(is);
			IOUtils.close(os);
		}
	}

	protected static void go(final String address, final IBrowserRidget browser) {
		if (address == null) {
			browser.setUrl("about:blank"); //$NON-NLS-1$
			return;
		}

		final String http = "http://"; //$NON-NLS-1$
		if (address.startsWith(http)) {
			browser.setUrl(address);
		} else {
			browser.setUrl(http + address);
		}
	}
}
