package org.eclipse.riena.example.client.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.example.client.views.SharedBrowserView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.IBrowserRidget.IBrowserRidgetFunction;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Demonstrates browser instance sharing without losing the browser session
 */
public class SharedBrowserController extends SubModuleController {
	/**
	 * The tab id as defined in <tt>sample-page.html</tt> (about, advantages or usage)
	 */
	private static final String TAB_ID = "webapp.target"; //$NON-NLS-1$
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

	private static int personCounter;
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
			browser.mapScriptFunction("jsCreateModuleGroup", new IBrowserRidgetFunction() { //$NON-NLS-1$
						@Override
						public Object execute(final Object[] jsParams) {
							return jsCreateModuleGroup(jsParams);
						}
					});
		} else {
			// we're somewhere in the webapp
			browser.bindToModel(model, "browserURL"); //$NON-NLS-1$
			browser.updateFromModel();
			browser.mapScriptFunction("initSubModules", new IBrowserRidgetFunction() { //$NON-NLS-1$
						@Override
						public Object execute(final Object[] jsParams) {
							return initSubModules(jsParams);
						}

					});
			browser.mapScriptFunction("itemSelected", new IBrowserRidgetFunction() { //$NON-NLS-1$
						@Override
						public Object execute(final Object[] jsParams) {
							return itemSelected(jsParams);
						}

					});
		}

		getNavigationNode().addSimpleListener(new SimpleNavigationNodeAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter#afterActivated (org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void afterActivated(final INavigationNode<?> source) {
				super.afterActivated(source);
				if (getNavigationNode().getParentOfType(IModuleGroupNode.class).getContext(SHARED_BROWSERS_MODEL) != null) {
					browser.execute("showContent('" + getNavigationNode().getContext(TAB_ID) + "');"); //$NON-NLS-1$//$NON-NLS-2$
				}
			}
		});

	}

	private Object itemSelected(final Object[] args) {
		final String selectedTabId = (String) args[0];
		if (selectedTabId == null) {
			return null;
		}

		for (final Object child : getNavigationNode().getParent().getChildren()) {
			if (child instanceof ISubModuleNode && selectedTabId.equals(((ISubModuleNode) child).getContext(TAB_ID))) {
				((ISubModuleNode) child).activate();
				break;
			}
		}

		return null;
	}

	/**
	 * This function is available in the browser widget and can be called from JavaScript
	 */
	public Object initSubModules(final Object[] args) {
		if (args.length != 2 || !(args[0] instanceof Object[]) || !(args[1] instanceof Object[])) {
			throw new IllegalArgumentException("Invalid JavaScript arguments."); //$NON-NLS-1$
		}

		final Object[] ids = (Object[]) args[0];
		final Object[] labels = (Object[]) args[1];

		getNavigationNode().setLabel((String) labels[0]);
		getNavigationNode().setContext(TAB_ID, ids[0]);

		for (int i = 1; i < ids.length; i++) {
			createSubModuleNode(getNavigationNode().getParentOfType(IModuleNode.class), (String) labels[i], (String) ids[i]);
		}

		return null;
	}

	/**
	 * This function is available in the browser widget and can be called from JavaScript
	 */
	public Object jsCreateModuleGroup(final Object[] args) {
		final String name = (String) args[0];

		final ModuleGroupNode group = new ModuleGroupNode();
		group.setContext(ISubModuleNode.SHARED_VIEWS_CONTEXT_KEY, name);
		final ModuleNode module = new ModuleNode("Shared Brower (" + name + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		group.addChild(module);

		final Model model = new Model();
		model.setBrowserURL(getHtmlPageURL("sample-page.html")); //$NON-NLS-1$
		group.setContext(SHARED_BROWSERS_MODEL, model);

		getNavigationNode().getParentOfType(ISubApplicationNode.class).addChild(group);

		createSubModuleNode(module, "Webapp", null);
		group.activate();

		return null;
	}

	private void createSubModuleNode(final IModuleNode parent, final String label, final String tabId) {
		// there is a submodule for each tab in the webapp
		final ISubModuleNode node = new SubModuleNode(new NavigationNodeId("onePerson", Integer //$NON-NLS-1$
				.toString(personCounter++)), label);
		node.setContext(TAB_ID, tabId);
		parent.addChild(node);
		WorkareaManager.getInstance().registerDefinition(node, getClass(), SharedBrowserView.class.getName(), true).setRequiredPreparation(true);
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
			String result = tempFile.toURI().toURL().toString();

			// this is needed to avoid initial browser refresh on submodule creation
			if (result.startsWith("file:/") && !result.startsWith("file:///")) {
				result = "file:///" + result.substring("file:/".length());
			}
			return result;
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
