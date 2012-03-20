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
package org.eclipse.riena.ui.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * Forwards events from the source control to the target control. Listeners on
 * the target control will receive events from the source control, as if the
 * events had occurred on the target.
 * <p>
 * This class helps custom widgets handle events from child-widgets in a manner
 * that does not expose implementation details.
 * <p>
 * A custom widget that is implemented as a subclass of Composite (for example
 * ChoiceComposite or DatePickerComposite) containing other child-widgets (such
 * as Text or Button widgets), may use an instance of this class to forward
 * events from the child-widgets to the main-widget. Events occurring on the
 * child-widgets will be refired on the main-widget. This allows client code to
 * register listeners with the main-widget (which is exposed) without having to
 * add listeners to the child-widgets (which may be considered internal and thus
 * might not be exposed).
 * 
 * @since 3.0
 */
class EventForwarder implements Listener {

	private static final SWTFacade SWT_FACADE = SWTFacade.getDefault();

	private final Control source;
	private final Control target;

	/**
	 * Creates a class that automatically forwards events from the source
	 * control to the target control.
	 * <p>
	 * Attachment and detachment of the forwarder is handled automatically.
	 * 
	 * @param source
	 *            the source Control; never null
	 * @param target
	 *            the target Control; never null
	 */
	EventForwarder(final Control source, final Control target) {
		Assert.isNotNull(target);
		final int[] eventTypes = { SWT.FocusIn, SWT.FocusOut, SWT.MenuDetect, SWT.DragDetect, SWT.MouseDoubleClick,
				SWTFacade.MouseMove, SWTFacade.MouseEnter, SWTFacade.MouseExit, SWTFacade.MouseHover,
				SWTFacade.MouseWheel, SWT.MouseDown, SWT.Traverse, };
		for (final int type : eventTypes) {
			source.addListener(type, this);
		}
		this.source = source;
		this.target = target;
	}

	public void handleEvent(final Event event) {
		if (target.isDisposed()) {
			return;
		}
		Event e;
		switch (event.type) {
		case SWT.FocusIn:
		case SWT.FocusOut:
		case SWT.MenuDetect:
			notifyTarget(createEvent(event));
			break;
		case SWT.DragDetect:
		case SWT.MouseDoubleClick:
		case SWTFacade.MouseMove:
		case SWTFacade.MouseEnter:
		case SWTFacade.MouseExit:
		case SWTFacade.MouseHover:
		case SWTFacade.MouseWheel:
			notifyTarget(createEvent(event));
			event.type = SWT.None;
			break;
		case SWT.MouseDown:
			e = createEvent(event);
			notifyTarget(e);
			event.doit = e.doit;
			break;
		case SWT.Traverse:
			e = createEvent(event);
			notifyTarget(e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
	}

	// helping methods
	//////////////////

	private Event createEvent(final Event event) {
		final Event result = new Event();
		result.display = event.display;
		result.widget = target;
		result.type = event.type;
		result.detail = event.detail;
		final Point pt = getDisplay().map(source, target, event.x, event.y);
		event.x = pt.x;
		event.y = pt.y;
		result.count = event.count;
		result.time = event.time;
		result.button = event.button;
		result.character = event.character;
		result.keyCode = event.keyCode;
		SWT_FACADE.copyEventKeyLocation(event, result);
		result.stateMask = event.stateMask;
		result.doit = event.doit;
		result.data = event.data;
		return result;
	}

	private Display getDisplay() {
		return source.getDisplay();
	}

	private void notifyTarget(final Event event) {
		target.notifyListeners(event.type, event);
	}
}
