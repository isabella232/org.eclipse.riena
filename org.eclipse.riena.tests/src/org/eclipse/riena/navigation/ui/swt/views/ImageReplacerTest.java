package org.eclipse.riena.navigation.ui.swt.views;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterType;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.SerializationException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.ILocalWorkingSetManager;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.ISaveableFilter;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementReference;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.themes.IThemeManager;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.ui.wizards.IWizardRegistry;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

import junit.framework.TestCase;

/**
 * Tests of the class {@link ImageReplacer}.
 */
@UITestCase
public class ImageReplacerTest extends TestCase {

	/**
	 * Tests the <i>private</i> method {@code getImageName(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetImageName() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();

		String imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "def.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("def", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "ghi00.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ghi00", imageName); //$NON-NLS-1$

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "ghi00.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ghi", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "/folder00/jkl00_d_.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("jkl00_d_", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "/folderXY/MNO00.png"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("MNO", imageName); //$NON-NLS-1$

		LnfManager.setLnf(originalLnf);

	}

	/**
	 * Tests the <i>private</i> method {@code getImageName(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetScaledImage() throws Exception {

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());

		final ImageReplacer replacer = ImageReplacer.getInstance();

		ImageDescriptor fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		ImageDescriptor imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", new Object[] { fileImageDescriptor, IconSize.NONE }); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(16, imageDescriptor.getImageData().width);
		assertEquals(16, imageDescriptor.getImageData().height);

		final float[] oldDpiFactors = SwtUtilities.getDpiFactors();
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 1.5f, 1.5f }); //$NON-NLS-1$

		fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", new Object[] { fileImageDescriptor, IconSize.NONE }); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(24, imageDescriptor.getImageData().width);
		assertEquals(24, imageDescriptor.getImageData().height);

		final URL url = this.getClass().getResource("/icons/testimagea00.png"); //$NON-NLS-1$
		final ImageDescriptor urlimageDescriptor = ImageDescriptor.createFromURL(url);
		imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", new Object[] { fileImageDescriptor, IconSize.NONE }); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(24, imageDescriptor.getImageData().width);
		assertEquals(24, imageDescriptor.getImageData().height);

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", oldDpiFactors); //$NON-NLS-1$
		LnfManager.setLnf(originalLnf);

	}

	/**
	 * Tests the <i>private</i> method {@code isImageDescriptorSupported}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testIsImageDescriptorSupported() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();

		final ImageDescriptor fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		boolean isSupported = ReflectionUtils.invokeHidden(replacer, "isImageDescriptorSupported", fileImageDescriptor); //$NON-NLS-1$
		assertTrue(isSupported);

		final URL url = this.getClass().getResource("/icons/testimagea00.png"); //$NON-NLS-1$
		final ImageDescriptor urlimageDescriptor = ImageDescriptor.createFromURL(url);
		isSupported = ReflectionUtils.invokeHidden(replacer, "isImageDescriptorSupported", urlimageDescriptor); //$NON-NLS-1$
		assertTrue(isSupported);

	}

	/**
	 * Tests the method {@code getInstance()}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetInstance() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();
		assertNotNull(replacer);

		assertSame(replacer, ImageReplacer.getInstance());
	}

	public void testReplaceImagesCommandContributionItem() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());

		final CommandContributionItem item = new CommandContributionItem(new CommandContributionItemParameter(new MyServiceLocator(), "id", "cmdId", //$NON-NLS-1$ //$NON-NLS-2$
				CommandContributionItem.STYLE_PUSH));

		final URL url = this.getClass().getResource("/icons/testimagea00.png"); //$NON-NLS-1$
		final ImageDescriptor urlImageDescriptor = ImageDescriptor.createFromURL(url);

		ReflectionUtils.setHidden(item, "contributedIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.setHidden(item, "icon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(replacer, "replaceImages", item); //$NON-NLS-1$
		ImageDescriptor iconDescriptor = ReflectionUtils.getHidden(item, "icon"); //$NON-NLS-1$
		assertNotSame(urlImageDescriptor, iconDescriptor);
		assertEquals(16, iconDescriptor.getImageData().width);
		assertEquals(16, iconDescriptor.getImageData().height);

		final float[] oldDpiFactors = SwtUtilities.getDpiFactors();
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 1.5f, 1.5f }); //$NON-NLS-1$

		ReflectionUtils.setHidden(item, "contributedIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.setHidden(item, "icon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(replacer, "replaceImages", item); //$NON-NLS-1$
		iconDescriptor = ReflectionUtils.getHidden(item, "icon"); //$NON-NLS-1$
		assertNotSame(urlImageDescriptor, iconDescriptor);
		assertEquals(24, iconDescriptor.getImageData().width);
		assertEquals(24, iconDescriptor.getImageData().height);

		ReflectionUtils.setHidden(item, "contributedDisabledIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.setHidden(item, "disabledIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(replacer, "replaceImages", item); //$NON-NLS-1$
		iconDescriptor = ReflectionUtils.getHidden(item, "disabledIcon"); //$NON-NLS-1$
		assertNotSame(urlImageDescriptor, iconDescriptor);
		assertEquals(24, iconDescriptor.getImageData().width);
		assertEquals(24, iconDescriptor.getImageData().height);

		ReflectionUtils.setHidden(item, "contributedHoverIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.setHidden(item, "hoverIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(replacer, "replaceImages", item); //$NON-NLS-1$
		iconDescriptor = ReflectionUtils.getHidden(item, "hoverIcon"); //$NON-NLS-1$
		assertNotSame(urlImageDescriptor, iconDescriptor);
		assertEquals(24, iconDescriptor.getImageData().width);
		assertEquals(24, iconDescriptor.getImageData().height);

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", oldDpiFactors); //$NON-NLS-1$
		LnfManager.setLnf(originalLnf);

	}

	// helping classes
	//////////////////

	private static class MyLnf extends RienaDefaultLnf {
		@Override
		public String getIconScaleSuffix(final Point dpi) {

			if (dpi == null) {
				return "00"; //$NON-NLS-1$
			}
			if (dpi.x <= 96) {
				return "00"; //$NON-NLS-1$
			}
			return "03"; //$NON-NLS-1$

		}
	}

	//ServiceLocator mock class for the testReplaceImagesCommandContributionItem
	private class MyServiceLocator implements IServiceLocator {

		@Override
		public Object getService(final Class api) {
			if (api.equals(IWorkbenchLocationService.class)) {
				return new IWorkbenchLocationService() {

					@Override
					public String getServiceScope() {
						return null;
					}

					@Override
					public int getServiceLevel() {
						return 0;
					}

					@Override
					public IWorkbench getWorkbench() {
						return new IWorkbench() {

							@Override
							public boolean hasService(final Class api) {
								return false;
							}

							@Override
							public Object getService(final Class api) {
								return null;
							}

							@Override
							public Object getAdapter(final Class adapter) {
								return null;
							}

							@Override
							public IWorkbenchPage showPerspective(final String perspectiveId, final IWorkbenchWindow window, final IAdaptable input)
									throws WorkbenchException {
								return null;
							}

							@Override
							public IWorkbenchPage showPerspective(final String perspectiveId, final IWorkbenchWindow window) throws WorkbenchException {
								return null;
							}

							@Override
							public boolean saveAllEditors(final boolean confirm) {
								return false;
							}

							@Override
							public boolean saveAll(final IShellProvider shellProvider, final IRunnableContext runnableContext, final ISaveableFilter filter,
									final boolean confirm) {
								return false;
							}

							@Override
							public boolean restart() {
								return false;
							}

							@Override
							public void removeWorkbenchListener(final IWorkbenchListener listener) {

							}

							@Override
							public void removeWindowListener(final IWindowListener listener) {

							}

							@Override
							public IWorkbenchWindow openWorkbenchWindow(final String perspectiveId, final IAdaptable input) throws WorkbenchException {

								return null;
							}

							@Override
							public IWorkbenchWindow openWorkbenchWindow(final IAdaptable input) throws WorkbenchException {
								return null;
							}

							@Override
							public boolean isStarting() {
								return false;
							}

							@Override
							public boolean isClosing() {
								return false;
							}

							@Override
							public IWorkingSetManager getWorkingSetManager() {
								return null;
							}

							@Override
							public IWorkbenchWindow[] getWorkbenchWindows() {
								return null;
							}

							@Override
							public int getWorkbenchWindowCount() {
								return 0;
							}

							@Override
							public IViewRegistry getViewRegistry() {
								return null;
							}

							@Override
							public IThemeManager getThemeManager() {
								return null;
							}

							@Override
							public ISharedImages getSharedImages() {
								return null;
							}

							@Override
							public IProgressService getProgressService() {
								return null;
							}

							@Override
							public IPreferenceStore getPreferenceStore() {
								return null;
							}

							@Override
							public PreferenceManager getPreferenceManager() {
								return null;
							}

							@Override
							public IPerspectiveRegistry getPerspectiveRegistry() {
								return null;
							}

							@Override
							public IWorkbenchOperationSupport getOperationSupport() {
								return null;
							}

							@Override
							public IWizardRegistry getNewWizardRegistry() {
								return null;
							}

							@Override
							public IShellProvider getModalDialogShellProvider() {
								return null;
							}

							@Override
							public IIntroManager getIntroManager() {
								return null;
							}

							@Override
							public IWizardRegistry getImportWizardRegistry() {
								return null;
							}

							@Override
							public IWorkbenchHelpSystem getHelpSystem() {
								return null;
							}

							@Override
							public IExtensionTracker getExtensionTracker() {
								return null;
							}

							@Override
							public IWizardRegistry getExportWizardRegistry() {
								return null;
							}

							@Override
							public IElementFactory getElementFactory(final String factoryId) {
								return null;
							}

							@Override
							public IEditorRegistry getEditorRegistry() {
								return null;
							}

							@Override
							public Display getDisplay() {
								return null;
							}

							@Override
							public IDecoratorManager getDecoratorManager() {
								return null;
							}

							@Override
							public IWorkbenchContextSupport getContextSupport() {
								return null;
							}

							@Override
							public IWorkbenchCommandSupport getCommandSupport() {
								return null;
							}

							@Override
							public IWorkbenchBrowserSupport getBrowserSupport() {
								return null;
							}

							@Override
							public IWorkbenchActivitySupport getActivitySupport() {
								return null;
							}

							@Override
							public IWorkbenchWindow getActiveWorkbenchWindow() {
								return null;
							}

							@Override
							public ILocalWorkingSetManager createLocalWorkingSetManager() {
								return null;
							}

							@Override
							public boolean close() {
								return false;
							}

							@Override
							public void addWorkbenchListener(final IWorkbenchListener listener) {
							}

							@Override
							public void addWindowListener(final IWindowListener listener) {
							}

							public boolean restart(final boolean useCurrentWorkspace) {
								// TODO Auto-generated method stub
								return false;
							}
						};
					}

					@Override
					public IWorkbenchWindow getWorkbenchWindow() {
						return null;
					}

					@Override
					public IWorkbenchPartSite getPartSite() {
						return null;
					}

					@Override
					public IEditorSite getMultiPageEditorSite() {
						return null;
					}

					@Override
					public IPageSite getPageSite() {
						return null;
					}

				};
			} else if (api.equals(ICommandService.class)) {
				return new ICommandService() {

					@Override
					public void dispose() {
					}

					@Override
					public void unregisterElement(final IElementReference elementReference) {
					}

					@Override
					public void setHelpContextId(final IHandler handler, final String helpContextId) {
					}

					@Override
					public void removeExecutionListener(final IExecutionListener listener) {
					}

					@Override
					public IElementReference registerElementForCommand(final ParameterizedCommand command, final UIElement element) throws NotDefinedException {
						return null;
					}

					@Override
					public void registerElement(final IElementReference elementReference) {
					}

					@Override
					public void refreshElements(final String commandId, final Map filter) {
					}

					@Override
					public void readRegistry() {
					}

					@Override
					public ParameterType getParameterType(final String parameterTypeId) {
						return null;
					}

					@Override
					public String getHelpContextId(final String commandId) throws NotDefinedException {
						return null;
					}

					@Override
					public String getHelpContextId(final Command command) throws NotDefinedException {
						return null;
					}

					@Override
					public ParameterType[] getDefinedParameterTypes() {
						return null;
					}

					@Override
					public Collection getDefinedParameterTypeIds() {
						return null;
					}

					@Override
					public Command[] getDefinedCommands() {
						return null;
					}

					@Override
					public Collection getDefinedCommandIds() {
						return null;
					}

					@Override
					public Collection getDefinedCategoryIds() {
						return null;
					}

					@Override
					public Category[] getDefinedCategories() {
						return null;
					}

					@Override
					public Command getCommand(final String commandId) {
						return ReflectionUtils.newInstanceHidden(Command.class, ""); //$NON-NLS-1$
					}

					@Override
					public Category getCategory(final String categoryId) {
						return null;
					}

					@Override
					public ParameterizedCommand deserialize(final String serializedParameterizedCommand) throws NotDefinedException, SerializationException {
						return null;
					}

					@Override
					public void defineUncategorizedCategory(final String name, final String description) {
					}

					@Override
					public void addExecutionListener(final IExecutionListener listener) {
					}
				};
			}
			return null;
		}

		@Override
		public boolean hasService(final Class api) {
			return false;
		}
	}

	public void testReplacePngWithSvgWhileScalingEnabled() {
		final ImageReplacer replacer = ImageReplacer.getInstance();

		LnfManager.setLnf(new MyLnf());

		final Point dpi = SwtUtilities.getDpi();
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpi", new Point(120, 120)); //$NON-NLS-1$
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 1.25f, 1.25f }); //$NON-NLS-1$

		final CommandContributionItem item = new CommandContributionItem(new CommandContributionItemParameter(new MyServiceLocator(), "id", "cmdId", //$NON-NLS-1$ //$NON-NLS-2$
				CommandContributionItem.STYLE_PUSH));

		final URL url = this.getClass().getResource("/icons/0088a00.png"); //$NON-NLS-1$
		final ImageDescriptor urlImageDescriptor = ImageDescriptor.createFromURL(url);

		ReflectionUtils.setHidden(item, "contributedIcon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.setHidden(item, "icon", urlImageDescriptor); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(replacer, "replaceImages", new Object[] { item, LnfManager.getLnf().getSetting(LnfKeyConstants.MENUBAR_ICON_SIZE) }); //$NON-NLS-1$
		final ImageDescriptor iconDescriptor = ReflectionUtils.getHidden(item, "icon"); //$NON-NLS-1$
		assertNotNull(iconDescriptor);
		assertNotNull(urlImageDescriptor);
		assertNotSame(urlImageDescriptor, iconDescriptor);
		assertEquals(20, iconDescriptor.getImageData().width);
		assertEquals(20, iconDescriptor.getImageData().height);

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpi", dpi); //$NON-NLS-1$
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 1.0f, 1.0f }); //$NON-NLS-1$
		LnfManager.dispose();
	}

}
