package org.eclipse.riena.navigation.ui.e4.rendering;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class RienaDynamicWindowLayout extends Layout {
	private final int style;
	private final int[] childrenSizes;

	public RienaDynamicWindowLayout(final int[] childrenSizes) {
		this(SWT.HORIZONTAL, childrenSizes);
	}

	public RienaDynamicWindowLayout(final int style, final int[] childrenSizes) {
		this.style = style;
		this.childrenSizes = childrenSizes;
	}

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		if (composite.getChildren().length != childrenSizes.length) {
			throw new IllegalStateException("The children sizes cound passed to the constructor must match the actual children size.");
		}

		int totalSize = 0;
		for (final int s : childrenSizes) {
			totalSize += Math.max(0, s);
		}

		if ((style & SWT.HORIZONTAL) > 0) {
			return new Point(Math.max(totalSize, wHint), hHint);
		} else { // style = SWT.VERTICAL
			return new Point(wHint, Math.max(totalSize, hHint));
		}
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Control[] children = composite.getChildren();
		if (children.length != childrenSizes.length) {
			throw new IllegalStateException("The children sizes cound passed to the constructor must match the actual children size.");
		}

		int minusOnesCount = 0;
		int usedSize = 0;
		for (final int s : childrenSizes) {
			if (s >= 0) {
				usedSize += s;
			} else {
				minusOnesCount++;
			}
		}

		final Rectangle clientArea = composite.getClientArea();
		if ((style & SWT.HORIZONTAL) > 0) {
			final int availableSize = clientArea.width;
			final int sizeOfMinusOnes = Math.max(0, (availableSize - usedSize) / minusOnesCount);

			int x = 0;
			for (int i = 0; i < children.length; i++) {
				final Control c = children[i];
				final int width = childrenSizes[i] < 0 ? sizeOfMinusOnes : childrenSizes[i];
				c.setBounds(new Rectangle(x, 0, width, clientArea.height));
				x += width;
			}
		} else { // style = SWT.VERTICAL
			final int availableSize = clientArea.height;
			final int sizeOfMinusOnes = Math.max(0, (availableSize - usedSize) / minusOnesCount);

			int y = 0;
			for (int i = 0; i < children.length; i++) {
				final Control c = children[i];
				final int height = childrenSizes[i] < 0 ? sizeOfMinusOnes : childrenSizes[i];
				c.setBounds(new Rectangle(0, y, clientArea.width, height));
				y += height;
			}
		}
	}
}
