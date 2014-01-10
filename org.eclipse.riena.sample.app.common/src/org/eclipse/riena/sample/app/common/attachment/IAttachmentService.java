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
package org.eclipse.riena.sample.app.common.attachment;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.riena.communication.core.attachment.Attachment;

/**
 * Interface for the AttachmentService. The AttachmentService demonstrates how
 * to use Attachments within WebService calls.
 */
public interface IAttachmentService {

	/**
	 * @param singleAttachment
	 * @return acknowledgement string
	 */
	String sendSingleAttachment(Attachment singleAttachment);

	/**
	 * @param firstAttachment
	 * @param secondAttachment
	 * @return acknowledgement string
	 */
	String sendTwoAttachments(Attachment firstAttachment, Attachment secondAttachment);

	/**
	 * @param firstString
	 * @param firstAttachment
	 * @param secondString
	 * @param secondAttachment
	 * @param thirdString
	 * @param number
	 * @return acknowledgement string
	 */
	String sendAttachmentsAndData(String firstString, Attachment firstAttachment, String secondString,
			Attachment secondAttachment, String thirdString, int number);

	/**
	 * @return Attachment
	 * 
	 */
	Attachment returnAttachment() throws IOException;

	/**
	 * @param attachmentName
	 * @return Attachment
	 * 
	 * @pre new File(attachmentName).exists()==true
	 */
	Attachment returnAttachmentForRequest(String attachmentName) throws IOException;

	/**
	 * @param attachment
	 * @return Attachment
	 */
	Attachment sendAndReturnAttachment(Attachment attachment);

	/**
	 * pass an inputstream directly through hessian. NOT an official supported
	 * feature.
	 * 
	 * @return
	 */
	InputStream getFile();

	/**
	 * returns an error when transfering the attachment directly
	 * 
	 * @return
	 */
	Attachment getBytesFromSampleWithError() throws IOException;

	/**
	 * tests what happens if attachments are built from URL and the URL creates
	 * an error right at the start (HTTP return code)
	 * 
	 * @return
	 */
	Attachment getBytesFromUrlWithErrorAtStart();

	/**
	 * same as last, but the error happens in the middle. (actually this returns
	 * just half the data with not error response, the testcase is disabled)
	 * 
	 * @return
	 */
	Attachment getBytesFromUrlWithError();

	/**
	 * This testcase should finally work to stream an inputstream from a third
	 * tier
	 * 
	 * @return
	 */
	Attachment getBytesFromUrlWithoutError();

	/**
	 * @param sizeInBytes
	 * @return
	 */
	Attachment getAttachmentForSize(int sizeInBytes) throws IOException;

	/**
	 * @return
	 * @throws IOException
	 */
	Attachment getBytesFromInvalidUrl() throws IOException;

	/**
	 * Endpoint to send file tooo
	 * 
	 * @return
	 * @throws IOException
	 */
	void sendFile(Attachment file);

	/**
	 * @return
	 */
	Object getAttachmentAsObject() throws IOException;

	/**
	 * @return
	 */
	Attachment getEmptyAttachment() throws IOException;

	/**
	 * @param attachment
	 * @return size of the attachment received
	 * @throws IOException
	 */
	int sendAttachmentAndReturnSize(Attachment attachment) throws IOException;

	/**
	 * @param size
	 * @return
	 * @throws IOException
	 */
	Attachment returnAttachmentForSize(int size) throws IOException;
}
