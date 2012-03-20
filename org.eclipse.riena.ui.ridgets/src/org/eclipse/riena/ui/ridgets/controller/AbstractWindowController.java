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
package org.eclipse.riena.ui.ridgets.controller;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.ui.core.context.IContext;
import org.eclipse.riena.ui.ridgets.ClassRidgetMapper;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IDefaultActionManager;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IShellRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Controller for a view that is or has a window.
 * 
 */
public abstract class AbstractWindowController implements IController, IContext {

	/**
	 * @since 3.0
	 */
	public static final String RIDGET_ID_OK = "dialogOkButton"; //$NON-NLS-1$
	/**
	 * @since 3.0
	 */
	public static final String RIDGET_ID_CANCEL = "dialogCancelButton"; //$NON-NLS-1$

	/**
	 * The ridget id to use for the window ridget.
	 */
	public static final String RIDGET_ID_WINDOW = "windowRidget"; //$NON-NLS-1$

	/**
	 * Return code to indicate that the window was a OK-ed (value: {@value} ).
	 * 
	 * @see #getReturnCode()
	 * @since 1.2
	 */
	public static final int OK = 0;

	/**
	 * Return code to indicate that the window was canceled (value: {@value} ).
	 * 
	 * @see #getReturnCode()
	 * @since 1.2
	 */
	public static final int CANCEL = 1;

	private final Map<String, IRidget> ridgets;
	private final Map<String, Object> context;
	private IWindowRidget windowRidget;
	private boolean blocked;
	private int returnCode;
	private boolean configured = false;

	private IDefaultActionManager actionManager;

	public AbstractWindowController() {
		super();
		ridgets = new HashMap<String, IRidget>();
		context = new HashMap<String, Object>();
	}

	/**
	 * Make {@code action} the default action while the focus is within
	 * {@code focusRidget} including it's children.
	 * <p>
	 * If a default action is available and enabled, it will be invoked whenever
	 * the user presses ENTER within the window.
	 * <p>
	 * Note: the algorithm stops at the first match. It will check the most
	 * specific (innermost) ridget first and check the most general (outermost)
	 * ridget last.
	 * 
	 * @param focusRidget
	 *            the ridget that needs to have the focus to activate this rule.
	 *            Never null.
	 * @param action
	 *            this ridget will become the default action, while focusRidget
	 *            has the focus. Never null.
	 * 
	 * @since 2.0
	 */
	public void addDefaultAction(final IRidget focusRidget, final IActionRidget action) {
		actionManager = getWindowRidget().addDefaultAction(focusRidget, action);
	}

	public void addRidget(final String id, final IRidget ridget) {
		ridgets.put(id, ridget);
	}

	public void afterBind() {
		returnCode = OK;
		getWindowRidget().updateFromModel();
		if (actionManager != null) {
			actionManager.activate();
		}
	}

	public void configureRidgets() {
		setWindowRidget((IWindowRidget) getRidget(RIDGET_ID_WINDOW));
		configureOkCancelButtons();
	}

	/**
	 * @since 3.0
	 */
	protected void configureOkCancelButtons() {
		final IActionRidget okAction = getRidget(IActionRidget.class, RIDGET_ID_OK);
		if (okAction != null) {
			okAction.setText("&Okay"); //$NON-NLS-1$
			okAction.addListener(new IActionListener() {
				public void callback() {
					close(OK);
				}
			});
		}
		final IActionRidget cancelAction = getRidget(IActionRidget.class, RIDGET_ID_CANCEL);
		if (cancelAction != null) {
			cancelAction.addListener(new IActionListener() {
				public void callback() {
					close(CANCEL);
				}
			});
		}
	}

	/**
	 * @since 1.2
	 */
	public Object getContext(final String key) {
		return context.get(key);
	}

	public <R extends IRidget> R getRidget(final String id) {
		return (R) ridgets.get(id);
	}

	/**
	 * @since 2.0
	 */
	public <R extends IRidget> R getRidget(final Class<R> ridgetClazz, final String id) {
		R ridget = getRidget(id);

		if (ridget != null) {
			return ridget;
		}
		if (RienaStatus.isTest()) {
			try {
				if (ridgetClazz.isInterface() || Modifier.isAbstract(ridgetClazz.getModifiers())) {
					final Class<R> mappedRidgetClazz = (Class<R>) ClassRidgetMapper.getInstance().getRidgetClass(
							ridgetClazz);
					if (mappedRidgetClazz != null) {
						ridget = mappedRidgetClazz.newInstance();
					}
					Assert.isNotNull(
							ridget,
							"Could not find a corresponding implementation for " + ridgetClazz.getName() + " in " + ClassRidgetMapper.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					ridget = ridgetClazz.newInstance();
				}
			} catch (final InstantiationException e) {
				throw new RuntimeException(e);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			addRidget(id, ridget);
		}

		return ridget;
	}

	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	/**
	 * @return The window ridget.
	 */
	public IWindowRidget getWindowRidget() {
		if (RienaStatus.isTest() && windowRidget == null) {
			final Class<IWindowRidget> mappedRidgetClazz = (Class<IWindowRidget>) ClassRidgetMapper.getInstance()
					.getRidgetClass(IShellRidget.class);
			try {
				if (mappedRidgetClazz != null) {
					windowRidget = mappedRidgetClazz.newInstance();
				}
				Assert.isNotNull(
						windowRidget,
						"Could not find a corresponding implementation for IWindowRidget in " + ClassRidgetMapper.class.getName()); //$NON-NLS-1$ 
			} catch (final InstantiationException e) {
				throw new RuntimeException(e);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return windowRidget;
	}

	/**
	 * Returns the return code for this window.
	 * <p>
	 * These codes are window specific, but two return codes are already
	 * defined: {@link #OK} and {@link #CANCEL}.
	 * 
	 * @since 1.2
	 */
	public int getReturnCode() {
		return returnCode;
	}

	public boolean isBlocked() {
		return this.blocked;
	}

	public void setBlocked(final boolean blocked) {
		this.blocked = blocked;
		// TODO: implement
	}

	/**
	 * @since 1.2
	 */
	public void setContext(final String key, final Object value) {
		context.put(key, value);
	}

	/**
	 * Set the return code for this window.
	 * <p>
	 * These codes are window specific, but two return codes are already
	 * defined: {@link #OK} and {@link #CANCEL}.
	 * 
	 * @since 1.2
	 */
	public void setReturnCode(final int returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * Sets the window ridget.
	 * 
	 * @param windowRidget
	 *            The window ridget.
	 */
	public void setWindowRidget(final IWindowRidget windowRidget) {
		this.windowRidget = windowRidget;
	}

	/**
	 * Closes the dialog and sets given return code.
	 * 
	 * @param returnCode
	 *            the return code to set. These codes are window specific, but
	 *            two return codes are already defined: {@link #OK} and
	 *            {@link #CANCEL}.
	 * @since 3.0
	 */
	public void close(final int returnCode) {
		setReturnCode(returnCode);
		getWindowRidget().dispose();
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
