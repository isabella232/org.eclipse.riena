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
package org.eclipse.riena.core.wire;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a notification method that will be called when the wiring for a
 * class has been done.
 * <p>
 * However, this does <b>not</b> mean that all 'expected' services must have
 * been injected. They only have been injected if they where already registered.
 * <br>
 * When this method gets called all extension and service injectors have been
 * created and started, which causes an initial injection.
 * 
 * @since 3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnWiringDone {

}
