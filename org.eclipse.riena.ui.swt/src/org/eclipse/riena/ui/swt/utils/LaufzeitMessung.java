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
package org.eclipse.riena.ui.swt.utils;

import java.util.ArrayList;

/**
 * @since 6.2
 *
 */
public class LaufzeitMessung {

	ArrayList<Entry> messungen = new ArrayList<Entry>();
	String wholeString = "";

	public void start() {
		messungen.add(new Entry());
		messungen.get(messungen.size() - 1).startMessung();
	}

	public void end() {
		messungen.get(messungen.size() - 1).endMessung();
		toWholeString();
	}

	public void toWholeString() {
		this.wholeString += messungen.get(messungen.size() - 1).getValue() + " \n ";
	}

	public void printAll() {
		for (final Entry entry : messungen) {
			System.err.println("----------------Messergebnisse-------------------");
			System.out.println("---------------");
			System.out.println(entry.getValue());
		}
	}

	private class Entry {
		private long start;
		private long end;
		private long value;

		public void startMessung() {
			setStart(System.currentTimeMillis());
		}

		public void endMessung() {
			setEnd(System.currentTimeMillis());
			setValue((end - start));
		}

		public long getStart() {
			return start;
		}

		public void setStart(final long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(final long end) {
			this.end = end;
		}

		public long getValue() {
			return value;
		}

		public void setValue(final long value) {
			this.value = value;
		}

	}

}
