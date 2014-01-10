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
package org.eclipse.riena.beans.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Bean that stores information about a single word (String).
 */
public class WordNode extends AbstractBean {

	private final WordNode parent;

	private String word;
	private boolean isUpperCase;
	private List<WordNode> children;

	public WordNode(final String word) {
		this(null, word);
	}

	public WordNode(final WordNode parent, final String word) {
		Assert.isNotNull(word);
		this.parent = parent;
		this.word = word;
		children = new ArrayList<WordNode>();
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public int getACount() {
		int result = 0;
		for (final char c : word.toCharArray()) {
			if (c == 'a' || c == 'A') {
				result++;
			}
		}
		return result;
	}

	public float getAQuota() {
		return ((float) getACount()) / word.length() * 100;
	}

	public List<WordNode> getChildren() {
		return new ArrayList<WordNode>(children);
	}

	public WordNode getParent() {
		return parent;
	}

	public String getWord() {
		return isUpperCase ? word.toUpperCase() : word;
	}

	public String getWordIgnoreUppercase() {
		return word;
	}

	public boolean isUpperCase() {
		return isUpperCase;
	}

	public void setChildren(final List<WordNode> children) {
		final List<WordNode> oldChildren = this.children;
		this.children = new ArrayList<WordNode>(children);
		firePropertyChanged("children", oldChildren, this.children); //$NON-NLS-1$
	}

	public void setUpperCase(final boolean isUppercase) {
		final boolean oldValue = isUpperCase;
		isUpperCase = isUppercase;
		firePropertyChanged("upperCase", oldValue, isUpperCase); //$NON-NLS-1$
	}

	public void setWord(final String word) {
		final String oldWord = word;
		this.word = word;
		firePropertyChanged("word", oldWord, this.word); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return word;
	}

	private void addChild(final WordNode child) {
		Assert.isNotNull(child);
		final List<WordNode> newChildren = getChildren();
		newChildren.add(child);
		setChildren(newChildren);
	}
}
