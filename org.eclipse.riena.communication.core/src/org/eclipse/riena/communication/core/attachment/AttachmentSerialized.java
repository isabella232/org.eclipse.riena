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
package org.eclipse.riena.communication.core.attachment;

/**
 * Helper class for Attachment to keep the content of the attachment when
 * transferring it via Hessian
 */
public class AttachmentSerialized {

	// byte array for data when using a binary protocol i.e. hessian
	private ByteArrayDataSource internalDataSource;

	/**
	 * Constructs a new AttachmentSerialized (Attachment in a serialized form)
	 * 
	 * @param dataHandler
	 */
	protected AttachmentSerialized(final IDataSource dataSource) {
		super();
		if (dataSource != null) {
			internalDataSource = new ByteArrayDataSource(dataSource);
		}
	}

	/**
	 * this method is called by hessian after an object was fully deserialized
	 * we use it to create the dataHandler Object from the internal byte array
	 * 
	 * @return Object
	 */
	public Object readResolve() {
		return new Attachment(internalDataSource);
	}

}
