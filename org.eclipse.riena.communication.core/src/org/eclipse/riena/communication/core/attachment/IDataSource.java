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
package org.eclipse.riena.communication.core.attachment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for encapsulating different types of attachments in remote
 * services. This interface is implemented by a number of objects that can be
 * the source of an Attachment object. Attachments are binary content that is
 * transferred as parameter or return value in remote service calls.
 */
public interface IDataSource {

	/**
	 * Returns the {@code InputStream} of this data source. The
	 * {@code InputStream} is guaranteed to be right at the beginning.
	 * 
	 * @return the {@code InputStream} of this data source
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * A name that can be displayed for this datasource. Some implementation
	 * however only show null for the name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Checks whether this data source is valid (has an {@code InputStream} that
	 * can be read).
	 */
	void checkValid() throws IOException;

}
