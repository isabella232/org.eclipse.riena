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
package org.eclipse.riena.ui.swt;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.swt.facades.DialogConstantsFacade;
import org.eclipse.riena.ui.swt.layout.DpiGridLayout;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * A dialog for showing messages and with an own renderer for the border and the title bar.
 */
public class RienaMessageDialog extends MessageDialog {

	private final LnFUpdater lnfUpdater = LnFUpdater.getInstance();

	// --- start - code from JFace MessageDialog.java ---

	/**
	 * Convenience method to open a simple themed dialog as specified by the <code>kind</code> flag.
	 * 
	 * @param kind
	 *            the kind of dialog to open, one of {@link #ERROR}, {@link #INFORMATION}, {@link #QUESTION}, {@link #WARNING}, {@link #CONFIRM}, or
	 *            {@link #QUESTION_WITH_CANCEL}.
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @param style
	 *            {@link SWT#NONE} for a default dialog, or {@link SWT#SHEET} for a dialog with sheet behavior
	 * @return <code>true</code> if the user presses the OK or Yes button, <code>false</code> otherwise
	 * @since 3.0
	 */
	public static boolean open(final int kind, final Shell parent, final String title, final String message, int style) {
		final RienaMessageDialog dialog = new RienaMessageDialog(parent, title, null, message, kind, getButtonLabels(kind), 0);
		style &= SWT.SHEET;
		dialog.setShellStyle(dialog.getShellStyle() | style);
		return dialog.open() == 0;
	}

	/**
	 * Convenience method to open a themed confirm (OK/Cancel) dialog.
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @return <code>true</code> if the user presses the OK button, <code>false</code> otherwise
	 * @since 3.0
	 */
	public static boolean openConfirm(final Shell parent, final String title, final String message) {
		return open(CONFIRM, parent, title, message, SWT.NONE);
	}

	/**
	 * Convenience method to open a themed error dialog.
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @since 3.0
	 */
	public static void openError(final Shell parent, final String title, final String message) {
		open(ERROR, parent, title, message, SWT.NONE);
	}

	/**
	 * Convenience method to open a themed information dialog.
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @since 3.0
	 */
	public static void openInformation(final Shell parent, final String title, final String message) {
		open(INFORMATION, parent, title, message, SWT.NONE);
	}

	/**
	 * Convenience method to open a themed Yes/No question dialog.
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @return <code>true</code> if the user presses the Yes button, <code>false</code> otherwise
	 * @since 3.0
	 */
	public static boolean openQuestion(final Shell parent, final String title, final String message) {
		return open(QUESTION, parent, title, message, SWT.NONE);
	}

	/**
	 * Convenience method to open a themed warning dialog.
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param title
	 *            the dialog's title, or <code>null</code> if none
	 * @param message
	 *            the message
	 * @since 3.0
	 */
	public static void openWarning(final Shell parent, final String title, final String message) {
		open(WARNING, parent, title, message, SWT.NONE);
	}

	private static String[] getButtonLabels(final int kind) {
		final DialogConstantsFacade facade = DialogConstantsFacade.getDefault();
		switch (kind) {
		case ERROR:
		case INFORMATION:
		case WARNING:
			return new String[] { facade.getOkLabel() };
		case CONFIRM:
			return new String[] { facade.getOkLabel(), facade.getCancelLabel() };
		case QUESTION:
			return new String[] { facade.getYesLabel(), facade.getNoLabel() };
		case QUESTION_WITH_CANCEL:
			return new String[] { facade.getYesLabel(), facade.getNoLabel(), facade.getCancelLabel() };
		default:
			throw new IllegalArgumentException("Illegal value for kind in MessageDialog.open()"); //$NON-NLS-1$
		}
	}

	// --- end - code from JFace MessageDialog.java ---

	private final RienaWindowRenderer dlgRenderer;

	/**
	 * Creates a Riena message dialog.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param dialogTitle
	 *            the dialog title, or <code>null</code> if none
	 * @param dialogTitleImage
	 *            the dialog title image, or <code>null</code> if none
	 * @param dialogMessage
	 *            the dialog message
	 * @param dialogImageType
	 *            one of the following values:
	 *            <ul>
	 *            <li><code>MessageDialog.NONE</code> for a dialog with no image</li>
	 *            <li><code>MessageDialog.ERROR</code> for a dialog with an error image</li>
	 *            <li><code>MessageDialog.INFORMATION</code> for a dialog with an information image</li>
	 *            <li><code>MessageDialog.QUESTION </code> for a dialog with a question image</li>
	 *            <li><code>MessageDialog.WARNING</code> for a dialog with a warning image</li>
	 *            </ul>
	 * @param dialogButtonLabels
	 *            an array of labels for the buttons in the button bar
	 * @param defaultIndex
	 *            the index in the button label array of the default button
	 * 
	 * @see {@link MessageDialog#MessageDialog(Shell, String, Image, String, int, String[], int)}
	 */
	public RienaMessageDialog(final Shell parentShell, final String dialogTitle, final Image dialogTitleImage, final String dialogMessage,
			final int dialogImageType, final String[] dialogButtonLabels, final int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
		dlgRenderer = new RienaWindowRenderer(this);
	}

	/**
	 * Create the dialog area and the button bar for the receiver.
	 * 
	 * @param parent
	 */
	@Override
	protected void createDialogAndButtonArea(final Composite parent) {
		// create the contents area
		DpiGridLayout gridLayout = new DpiGridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);
		dlgRenderer.createContents(parent);

		// create the dialog area and button bar
		final Composite centerComposite = dlgRenderer.getCenterComposite();
		gridLayout = new DpiGridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = LayoutConstants.getSpacing().x * 2;
		gridLayout.verticalSpacing = LayoutConstants.getSpacing().y;
		gridLayout.marginWidth = LayoutConstants.getMargins().x;
		gridLayout.marginHeight = LayoutConstants.getMargins().y;
		centerComposite.setLayout(gridLayout);
		dialogArea = createDialogArea(centerComposite);
		buttonBar = createButtonBar(centerComposite);

		lnfUpdater.updateUIControls(parent, true);

	}

	@Override
	public void create() {
		// compute the 'styled' shell style, before creating the shell
		setShellStyle(dlgRenderer.computeShellStyle());
		super.create();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If an error image in the Riena Look&Feel is defined, the LnF image will be returned; otherwise the standard SWT error image for a message box well be
	 * returned.
	 */
	@Override
	public Image getErrorImage() {
		Image image = getLnfImage(LnfKeyConstants.MESSAGE_BOX_ERROR_ICON);
		if (image == null) {
			image = super.getErrorImage();
		}
		return image;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If an warning image in the Riena Look&Feel is defined, the LnF image will be returned; otherwise the standard SWT warning image for a message box well be
	 * returned.
	 */
	@Override
	public Image getWarningImage() {
		Image image = getLnfImage(LnfKeyConstants.MESSAGE_BOX_WARNING_ICON);
		if (image == null) {
			image = super.getWarningImage();
		}
		return image;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If an information image in the Riena Look&Feel is defined, the LnF image will be returned; otherwise the standard SWT information image for a message box
	 * well be returned.
	 */
	@Override
	public Image getInfoImage() {
		Image image = getLnfImage(LnfKeyConstants.MESSAGE_BOX_INFO_ICON);
		if (image == null) {
			image = super.getInfoImage();
		}
		return image;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If an question image in the Riena Look&Feel is defined, the LnF image will be returned; otherwise the standard SWT question image for a message box well
	 * be returned.
	 */
	@Override
	public Image getQuestionImage() {
		Image image = getLnfImage(LnfKeyConstants.MESSAGE_BOX_QUESTION_ICON);
		if (image == null) {
			image = super.getQuestionImage();
		}
		return image;
	}

	private Image getLnfImage(final String lnfKey) {
		return LnfManager.getLnf().getImage(lnfKey);
	}

}
