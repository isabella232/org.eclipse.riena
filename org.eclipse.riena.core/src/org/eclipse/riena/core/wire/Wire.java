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
package org.eclipse.riena.core.wire;

/**
 * The {@code Wire} class starts a sentence (fluent interface) for a simple
 * approach of wiring components, i.e. injecting services and extensions.<br>
 * The main goal of using {@code Wire} is to remove the usage of {@code Inject}
 * from within the classes that need wiring (injecting).<br>
 * Wiring currently supports two approaches for wiring:
 * <ul>
 * <li>Wiring by explicitly specifying a class responsible for all the wiring
 * needs of a component. This is done with the class annotation {@code WireWith}
 * .</li>
 * <li>Wiring by annotating the ´bind´ methods of a component for service
 * injection with {@code InjectService} and the ´update´ method for extension
 * injection with {@code InjectExtension}.
 * </ul>
 */
public final class Wire {

	private Wire() {
		// Utility class
	}

	/**
	 * Wire the given {@code bean} instance.
	 * 
	 * @param bean
	 * @return
	 */
	public static WirePuller instance(final Object bean) {
		return new WirePuller(bean);
	}

}
