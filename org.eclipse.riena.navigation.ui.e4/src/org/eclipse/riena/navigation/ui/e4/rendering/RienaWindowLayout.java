package org.eclipse.riena.navigation.ui.e4.rendering;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class RienaWindowLayout extends Layout {
	private final int style;
	private final int firstChildSize;
	private final int lastChildSize;

	public RienaWindowLayout(final int firstChildSize, final int lastChildSize) {
		this(SWT.HORIZONTAL, firstChildSize, lastChildSize);
	}

	public RienaWindowLayout(final int style, final int firstChildSize, final int lastChildSize) {
		this.style = style;
		this.firstChildSize = firstChildSize;
		this.lastChildSize = lastChildSize;
	}

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		final Control[] children = composite.getChildren();

		if ((style & SWT.HORIZONTAL) > 0) {
			switch (children.length) {
			case 0:
				return new Point(wHint, hHint);
			case 1:
				// the only child takes the entire space
				return children[0].computeSize(firstChildSize, hHint, flushCache);
			default:
				final Point result = children[0].computeSize(firstChildSize, hHint, flushCache);
				final Point c2 = children[1].computeSize(lastChildSize, hHint, flushCache);
				result.x += c2.x;
				result.y += Math.max(result.y, c2.y);

				for (final Control c : children) {
					final Point size = c.computeSize(wHint - firstChildSize - lastChildSize, hHint, flushCache);
					result.x += size.x;
					result.y += Math.max(result.y, size.y);
				}
				return result;
			}
		} else { // style = SWT.VERTICAL
			switch (children.length) {
			case 0:
				return new Point(wHint, hHint);
			case 1:
				// the only child takes the entire space
				return children[0].computeSize(wHint, firstChildSize, flushCache);
			default:
				final Point result = children[0].computeSize(wHint, firstChildSize, flushCache);
				final Point c2 = children[1].computeSize(wHint, lastChildSize, flushCache);
				result.x += Math.max(result.x, c2.x);
				result.y += c2.y;

				for (final Control c : children) {
					final Point size = c.computeSize(wHint, hHint - firstChildSize - lastChildSize, flushCache);
					result.x += Math.max(result.x, size.x);
					result.y += size.y;
				}
				return result;
			}
		}
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Control[] children = composite.getChildren();
		if (children.length == 0) {
			return;
		}

		final Rectangle available = composite.getClientArea();

		if ((style & SWT.HORIZONTAL) > 0) {
			final int[] sizes = computeChildrenSizes(available.width, children.length);
			for (int i = 0, x = 0; i < children.length; x += sizes[i++]) {
				children[i].setBounds(x, 0, sizes[i], available.height);
			}
		} else { // style = SWT.VERTICAL
			final int[] sizes = computeChildrenSizes(available.height, children.length);
			for (int i = 0, y = 0; i < children.length; y += sizes[i++]) {
				children[i].setBounds(0, y, available.width, sizes[i]);
			}
		}
	}

	/**
	 * The first and the last children take the given sizes, while the rest children share the available rest size.
	 */
	private int[] computeChildrenSizes(final int availableSize, final int childrenCount) {
		final int[] result = new int[childrenCount];
		switch (childrenCount) {
		case 0:
			break;
		case 1:
			result[0] = availableSize;
			break;
		case 2:
			result[0] = firstChildSize;
			result[1] = availableSize - firstChildSize;
			break;
		default:
			result[0] = firstChildSize;
			result[childrenCount - 1] = lastChildSize;
			for (int i = 1; i < childrenCount - 1; i++) {
				result[i] = (availableSize - firstChildSize - lastChildSize) / (childrenCount - 2);
			}
			break;
		}
		return result;
	}
}
