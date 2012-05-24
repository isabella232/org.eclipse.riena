package org.eclipse.riena.navigation.ui.swt.e4.application;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

public class RienaBaseViewPart {
	@Inject
	public Composite parent;

	@Inject
	public RienaBaseViewPart() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct() {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(new Color(null, new RGB(77, 77, 77)));
	}

}