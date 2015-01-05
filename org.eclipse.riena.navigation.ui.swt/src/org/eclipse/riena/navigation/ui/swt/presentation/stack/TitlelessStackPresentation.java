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
package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import org.eclipse.riena.ui.swt.UIConstants;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class preserves some public properties to avoid breaking clients. The real implementation moved to the bundle
 * <tt>org.eclipse.riena.navigation.ui.swt.presentation</tt>
 * 
 * @see TitlelessStackPresentation3xRAP
 */
public class TitlelessStackPresentation {

	/**
	 * @since 3.0
	 * 
	 */
	public static final String DATA_KEY_CONTENT_COMPOSITE = UIConstants.DATA_KEY_CONTENT_COMPOSITE;
	/**
	 * Property to distinguish the view of the navigation.
	 */
	public static final String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$
	/**
	 * @since 3.0
	 */
	public static final String PROPERTY_SUBAPPLICATION_NODE = "subapplication.node"; //$NON-NLS-1$

	/**
	 * Property to distinguish the view of the status line.
	 */
	public static final String PROPERTY_STATUSLINE = "statusLine"; //$NON-NLS-1$

	/**
	 * Left padding of the navigation.<br>
	 * Gap between left shell border and navigation.
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_PADDING_LEFT = 2;

	/**
	 * Right padding of the sub-module view.<br>
	 * Gap between right shell border and sub-module view.
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_PADDING_RIGHT = DEFAULT_PADDING_LEFT;

	/**
	 * Bottom padding of the navigation/the sub-module view.<br>
	 * Gap between bottom shell border and sub-module view.
	 */
	public static final int PADDING_BOTTOM = SwtUtilities.convertYToDpi(2);

	/**
	 * Gap between navigation and sub-module view
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_NAVIGATION_SUB_MODULE_GAP = 4;
}
