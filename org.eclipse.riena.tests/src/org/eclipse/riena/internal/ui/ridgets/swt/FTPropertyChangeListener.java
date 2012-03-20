/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

final class FTPropertyChangeListener implements PropertyChangeListener {

	private int count;
	private PropertyChangeEvent event;

	public void propertyChange(final PropertyChangeEvent evt) {
		count++;
		event = evt;
	}

	public int getCount() {
		return count;
	}

	public PropertyChangeEvent getEvent() {
		return event;
	}
}
