/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt.uiprocess;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.swt.RienaWindowRenderer;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.nls.Messages;

/**
 * The window visualizing the progress of an {@link UIProcess}. Have a look at
 * {@link ApplicationWindow} to get more detailed information about window
 * handling.
 */
public class UIProcessWindow extends ApplicationWindow implements IUIProcessWindow {

	private static final int CANCEL_BUTTON_WIDTH = 70;
	private static final int PROGRESS_BAR_WIDTH = 210;

	private final LnFUpdater lnfUpdater = LnFUpdater.getInstance();
	private final UIProcessControl progressControl;
	private final Set<IProcessWindowListener> windowListeners;
	private final RienaWindowRenderer windowRenderer;

	private ProgressBar progressBar;
	private Text description;
	private Label percent;
	private Button cancelButton;
	private boolean cancelEnabled = true;
	private boolean cancelVisible = true;

	public UIProcessWindow(final Shell parentShell, final UIProcessControl progressControl) {
		super(parentShell);
		this.progressControl = progressControl;
		windowListeners = new HashSet<IProcessWindowListener>();
		windowRenderer = new RienaWindowRenderer(this);
	}

	@Override
	public void create() {
		final int style = windowRenderer.computeShellStyle() & ~SWT.MIN & ~SWT.MAX;
		// style = style & ~SWT.CLOSE;
		setShellStyle(style);
		super.create();
	}

	/**
	 * do the layouting for {@link FormLayout} for the parent here
	 */
	private void createWindowLayout(final Composite parent) {
		final GridLayout layout = new GridLayout();
		layout.marginTop = 10;
		layout.marginBottom = 10;
		layout.marginLeft = 20;
		layout.marginRight = 20;
		parent.setLayout(layout);
	}

	/**
	 * On this place the {@link IUIProcessCanvas} gets layouted.
	 * 
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(final Composite parent) {

		final Control contentsComposite = windowRenderer.createContents(parent);
		final Composite centerComposite = windowRenderer.getCenterComposite();

		createWindowLayout(centerComposite);

		// description
		final int style = SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.NO_FOCUS;
		description = new Text(centerComposite, style);
		GridDataFactory.fillDefaults().minSize(PROGRESS_BAR_WIDTH, 35).grab(true, true).applyTo(description);

		// percent
		percent = new Label(centerComposite, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).indent(0, 5).minSize(50, 13).grab(true, false)
				.applyTo(percent);
		// progressBar
		progressBar = new ProgressBar(centerComposite, SWT.HORIZONTAL);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		GridDataFactory.fillDefaults().minSize(PROGRESS_BAR_WIDTH, 15).applyTo(progressBar);

		cancelButton = new Button(centerComposite, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				progressControl.fireCanceled(false);
			}

			public void widgetSelected(final SelectionEvent e) {
				progressControl.fireCanceled(false);
			}
		});
		cancelButton.setText(Messages.UIProcessWindow_cancel);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).indent(0, 10).minSize(CANCEL_BUTTON_WIDTH, 0)
				.grab(true, false).applyTo(cancelButton);

		lnfUpdater.updateUIControls(centerComposite.getParent(), true);
		description.setBackground(centerComposite.getBackground());

		return contentsComposite;
	}

	public Label getPercent() {
		return percent;
	}

	/**
	 * @since 4.0
	 */
	public Text getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		getDescription().setText(description);
	}

	public void closeWindow() {
		close();
	}

	@Override
	public boolean close() {
		fireWindowAboutToClose();
		return super.close();
	}

	public void openWindow() {
		open();
		cancelButton.setFocus();
	}

	@Override
	public int getShellStyle() {
		return super.getShellStyle();
	}

	@Override
	public void setShellStyle(final int newShellStyle) {
		super.setShellStyle(newShellStyle);
	}

	public void addProcessWindowListener(final IProcessWindowListener listener) {
		windowListeners.add(listener);
	}

	protected void fireWindowAboutToClose() {
		for (final IProcessWindowListener listener : windowListeners) {
			listener.windowAboutToClose();
		}
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * This method does nothing, because this window has no menu, not cool or
	 * tool bar and no status line.
	 * 
	 * @see org.eclipse.jface.window.ApplicationWindow#createTrimWidgets(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void createTrimWidgets(final Shell shell) {
		// do nothing
	}

	/**
	 * @param cancelVisible
	 * @since 4.0
	 */
	protected void setCancelVisible(final boolean cancelVisible) {
		if (this.cancelVisible != cancelVisible) {
			this.cancelVisible = cancelVisible;
			if (cancelButton != null) {
				cancelButton.setVisible(cancelVisible);
			}
		}
	}

	/**
	 * @param cancelEnabled
	 * @since 4.0
	 */
	protected void setCancelEnabled(final boolean cancelEnabled) {
		if (this.cancelEnabled != cancelEnabled) {
			this.cancelEnabled = cancelEnabled;
			if (cancelButton != null) {
				cancelButton.setEnabled(cancelEnabled);
			}
		}
	}

}
