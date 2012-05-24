package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * A menu
 */
public class Snippet299754 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());

			final ToolBar toolBar = new ToolBar(shell, SWT.FLAT);

			final ToolItem item1 = new ToolItem(toolBar, SWT.CHECK);
			item1.setText("File");
			final ToolItem item2 = new ToolItem(toolBar, SWT.CHECK);
			item2.setText("Edit");

			final DropdownListener listener = new DropdownListener(item1, item2).add("New", item1).add("Exit", item1).add("Copy", item2);
			item1.addSelectionListener(listener);
			item2.addSelectionListener(listener);

			toolBar.addMouseTrackListener(listener);

			new Composite(shell, SWT.NONE).setFocus();

			shell.setSize(400, 400);
			shell.setVisible(true);

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	static class DropdownListener extends SelectionAdapter implements MouseTrackListener {

		private ToolItem activeMenu;

		public DropdownListener(final ToolItem... toolItems) {
			for (final ToolItem toolItem : toolItems) {
				final Shell menu = new Shell(toolItem.getParent().getShell(), SWT.NO_TRIM | SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
				GridLayoutFactory.swtDefaults().margins(0, 0).spacing(0, 0).applyTo(menu);
				menu.addShellListener(new ShellAdapter() {
					@Override
					public void shellDeactivated(final ShellEvent e) {
						hideMenu(toolItem);
					}
				});
				toolItem.setData(menu);
			}
		}

		/**
		 * @param event
		 */
		private void showMenu(final ToolItem toolItem) {
			final Rectangle rect = toolItem.getBounds();
			final Point pt = toolItem.getParent().toDisplay(new Point(rect.x, rect.y));
			final Shell menu = (Shell) toolItem.getData();
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
			activeMenu = toolItem;
			activeMenu.setSelection(true);
		}

		/**
		 * 
		 */
		private void hideMenu(final ToolItem toolItem) {
			toolItem.setSelection(false);
			((Shell) toolItem.getData()).setVisible(false);
			activeMenu = null;
		}

		/**
		 * create an entry in the given menu
		 */
		public DropdownListener add(final String entry, final ToolItem toolItem) {
			final Shell menu = (Shell) toolItem.getData();
			final ToolBar toolBar = new ToolBar(menu, SWT.FLAT);
			toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			final ToolItem menuItem = new ToolItem(toolBar, SWT.NONE);
			menuItem.setText(entry);
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					hideMenu(toolItem);
				}
			});

			menu.pack();
			return this;
		}

		@Override
		public void widgetSelected(final SelectionEvent event) {
			showMenu(getItem(event));
		}

		/**
		 * @param event
		 * @return
		 */
		private ToolItem getItem(final SelectionEvent event) {
			return (ToolItem) event.widget;
		}

		public void mouseHover(final MouseEvent e) {
			// we are only interested if a menu is open
			if (activeMenu == null) {
				return;
			}
			hideMenu(activeMenu);
			final ToolItem toolItem = ((ToolBar) e.getSource()).getItem(new Point(e.x, e.y));
			showMenu(toolItem);
		}

		public void mouseEnter(final MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExit(final MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
