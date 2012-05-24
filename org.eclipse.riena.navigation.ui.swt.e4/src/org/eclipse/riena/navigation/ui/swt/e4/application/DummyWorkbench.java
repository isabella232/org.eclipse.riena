/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4.application;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.ILocalWorkingSetManager;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.ISaveableFilter;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.themes.IThemeManager;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.ui.wizards.IWizardRegistry;

public class DummyWorkbench implements IWorkbench {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(final Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#getService(java.lang.Class)
	 */
	public Object getService(final Class api) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#hasService(java.lang.Class)
	 */
	public boolean hasService(final Class api) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getDisplay()
	 */
	public Display getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getProgressService()
	 */
	public IProgressService getProgressService() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#addWorkbenchListener(org.eclipse.ui.
	 * IWorkbenchListener)
	 */
	public void addWorkbenchListener(final IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#removeWorkbenchListener(org.eclipse.ui.
	 * IWorkbenchListener)
	 */
	public void removeWorkbenchListener(final IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#addWindowListener(org.eclipse.ui.IWindowListener
	 * )
	 */
	public void addWindowListener(final IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#removeWindowListener(org.eclipse.ui.IWindowListener
	 * )
	 */
	public void removeWindowListener(final IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#close()
	 */
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getActiveWorkbenchWindow()
	 */
	public IWorkbenchWindow getActiveWorkbenchWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getEditorRegistry()
	 */
	public IEditorRegistry getEditorRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getOperationSupport()
	 */
	public IWorkbenchOperationSupport getOperationSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPerspectiveRegistry()
	 */
	public IPerspectiveRegistry getPerspectiveRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPreferenceManager()
	 */
	public PreferenceManager getPreferenceManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getSharedImages()
	 */
	public ISharedImages getSharedImages() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindowCount()
	 */
	public int getWorkbenchWindowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindows()
	 */
	public IWorkbenchWindow[] getWorkbenchWindows() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkingSetManager()
	 */
	public IWorkingSetManager getWorkingSetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#createLocalWorkingSetManager()
	 */
	public ILocalWorkingSetManager createLocalWorkingSetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#openWorkbenchWindow(java.lang.String,
	 * org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(final String perspectiveId, final IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#openWorkbenchWindow(org.eclipse.core.runtime
	 * .IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(final IAdaptable input) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#restart()
	 */
	public boolean restart() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String,
	 * org.eclipse.ui.IWorkbenchWindow)
	 */
	public IWorkbenchPage showPerspective(final String perspectiveId, final IWorkbenchWindow window)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String,
	 * org.eclipse.ui.IWorkbenchWindow, org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchPage showPerspective(final String perspectiveId, final IWorkbenchWindow window,
			final IAdaptable input) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getDecoratorManager()
	 */
	public IDecoratorManager getDecoratorManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#saveAllEditors(boolean)
	 */
	public boolean saveAllEditors(final boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getElementFactory(java.lang.String)
	 */
	public IElementFactory getElementFactory(final String factoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getActivitySupport()
	 */
	public IWorkbenchActivitySupport getActivitySupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getCommandSupport()
	 */
	public IWorkbenchCommandSupport getCommandSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getContextSupport()
	 */
	public IWorkbenchContextSupport getContextSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getThemeManager()
	 */
	public IThemeManager getThemeManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getIntroManager()
	 */
	public IIntroManager getIntroManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getHelpSystem()
	 */
	public IWorkbenchHelpSystem getHelpSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getBrowserSupport()
	 */
	public IWorkbenchBrowserSupport getBrowserSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#isStarting()
	 */
	public boolean isStarting() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#isClosing()
	 */
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getExtensionTracker()
	 */
	public IExtensionTracker getExtensionTracker() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getViewRegistry()
	 */
	public IViewRegistry getViewRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getNewWizardRegistry()
	 */
	public IWizardRegistry getNewWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getImportWizardRegistry()
	 */
	public IWizardRegistry getImportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getExportWizardRegistry()
	 */
	public IWizardRegistry getExportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#saveAll(org.eclipse.jface.window.IShellProvider
	 * , org.eclipse.jface.operation.IRunnableContext,
	 * org.eclipse.ui.ISaveableFilter, boolean)
	 */
	public boolean saveAll(final IShellProvider shellProvider, final IRunnableContext runnableContext,
			final ISaveableFilter filter, final boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getModalDialogShellProvider()
	 */
	public IShellProvider getModalDialogShellProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
