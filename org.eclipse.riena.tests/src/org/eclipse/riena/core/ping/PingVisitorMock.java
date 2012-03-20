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
package org.eclipse.riena.core.ping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

public class PingVisitorMock extends PingVisitor {

	private DefaultPingable pingable;
	private PingVisitor visitorMock;

	public PingVisitorMock() {
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public List<PingResult> getPingResults() {
		return super.getPingResults();
	}

	@Override
	public PingVisitor visit(final IPingable pingable) {
		Assert.assertSame(this.pingable, pingable);
		return visitorMock;
	}

	@Override
	public Collection<IPingable> getChildPingablesOf(final IPingable pingable) {
		return super.getChildPingablesOf(pingable);
	}

	@Override
	public void collectPingMethods(final IPingable pingable, final Set<IPingable> pingableList) {
		super.collectPingMethods(pingable, pingableList);
	}

	@Override
	public void collectPingableMembers(final IPingable pingable, final Set<IPingable> pingableList) {
		super.collectPingableMembers(pingable, pingableList);
	}

	@Override
	public void collectAdditionalPingables(final IPingable pingable, final Set<IPingable> pingableList) {
		super.collectAdditionalPingables(pingable, pingableList);
	}

	void setExpectations(final DefaultPingable pingable, final PingVisitor visitorMock) {
		this.pingable = pingable;
		this.visitorMock = visitorMock;
	}

}
