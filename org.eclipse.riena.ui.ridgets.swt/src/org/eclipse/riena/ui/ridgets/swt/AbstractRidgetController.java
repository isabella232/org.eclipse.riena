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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;

/**
 * Controller for using ridgets in a regular SWT view.
 * <p>
 * Your controller will receive its ridgets from a binding delegate. Your subclass must implement the {@link #configureRidgets()} method. References to your
 * ridgets can obtained using {@link #getRidget(String)}.
 * </p>
 * Here's an example showing how this class is used in a <b>regular</b> ViewPart:
 * 
 * <pre>
 * tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
 * delegate.addUIControl(tree, &quot;tree&quot;); // DefaultSwtBindingDelegate delegate
 * 
 * delegate.injectAndBind(controller);
 * parent.addDisposeListener(new DisposeListener() {
 * 	public void widgetDisposed(DisposeEvent e) {
 * 		delegate.unbind(controller);
 * 	}
 * });
 * </pre>
 * 
 * And here's the corresponding controller:
 * 
 * <pre>
 * public class NavigationController extends AbstractRidgetController {
 * 	public void configureRidgets() {
 * 		ITreeRidget tree = getRidget(&quot;tree&quot;);
 * 		// ...
 * 	}
 * }
 * </pre>
 * 
 * If you use the Riena UI / Navigation you should look at the classes SubModuleController and SubModuleView (and their subclasses) instead.
 * 
 * @see DefaultSwtBindingDelegate
 */
public abstract class AbstractRidgetController implements IController {

	private final Map<String, IRidget> map;
	private boolean isBlocked;
	private boolean configured = false;

	public AbstractRidgetController() {
		map = new HashMap<String, IRidget>();
	}

	public final void addRidget(final String id, final IRidget ridget) {
		map.put(id, ridget);
	}

	public boolean removeRidget(final String id) {
		return map.remove(id) != null;
	}

	public final void afterBind() {
		// does nothing and is not called automatically
	}

	public final <R extends IRidget> R getRidget(final String id) {
		return (R) map.get(id);
	}

	public <R extends IRidget> R getRidget(final Class<R> ridgetClazz, final String id) {
		return getRidget(id);
	}

	public final Collection<? extends IRidget> getRidgets() {
		return Collections.unmodifiableCollection(map.values());
	}

	public abstract void configureRidgets();

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(final boolean blocked) {
		isBlocked = blocked;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public void setConfigured(final boolean configured) {
		this.configured = configured;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public boolean isConfigured() {
		return configured;
	}

}
