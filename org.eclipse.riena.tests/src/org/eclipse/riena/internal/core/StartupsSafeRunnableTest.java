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
package org.eclipse.riena.internal.core;

import org.osgi.framework.Bundle;

import org.eclipse.riena.internal.core.IRienaStartupExtension.When;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link StartupsSafeRunnable}.
 */
@NonUITestCase
public class StartupsSafeRunnableTest extends RienaTestCase {

	public void testOrdering() throws Exception {
		final StartupsSafeRunnable runner = new StartupsSafeRunnable();
		final IRienaStartupExtension[] startups = new IRienaStartupExtension[] { create(When.BEGINNING),
				create(When.END), create(When.BEGINNING), create(null), create(When.END), create(null),
				create(When.BEGINNING) };
		runner.update(startups);
		runner.run();
		int i = 0;
		assertSame(When.BEGINNING, startups[i++].getWhen());
		assertSame(When.BEGINNING, startups[i++].getWhen());
		assertSame(When.BEGINNING, startups[i++].getWhen());
		assertSame(null, startups[i++].getWhen());
		assertSame(null, startups[i++].getWhen());
		assertSame(When.END, startups[i++].getWhen());
		assertSame(When.END, startups[i++].getWhen());
	}

	private IRienaStartupExtension create(final IRienaStartupExtension.When when) {
		return new StartupRunnable(when);
	}

	private static class StartupRunnable implements IRienaStartupExtension {

		private final When when;

		public StartupRunnable(final IRienaStartupExtension.When when) {
			this.when = when;
		}

		public Bundle getContributingBundle() {
			return null;
		}

		public When getWhen() {
			return when;
		}

		public String getRunClassName() {
			return null;
		}

		public Runnable createRunner() {
			return null;
		}

		public String getRequiredBundles() {
			return null;
		}

		public boolean isActivateSelf() {
			return false;
		}

	}
}
