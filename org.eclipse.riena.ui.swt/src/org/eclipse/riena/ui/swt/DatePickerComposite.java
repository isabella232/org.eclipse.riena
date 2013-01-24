/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Composite of a DatePicker and a SWT Text widget.
 * 
 * @since 1.2
 */
public class DatePickerComposite extends Composite {

	private final static int BUTTON_WIDTH = 16;
	private final static int BUTTON_HEIGHT = 16;

	private final Text textfield;
	private final Button pickerButton;

	private Color bgColor;
	private DatePicker datePicker;
	private IDateConverterStrategy dateConverterStrategy;

	public DatePickerComposite(final Composite parent, final int textStyles) {
		super(parent, SWT.BORDER);
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(0, 0).applyTo(this);

		textfield = new Text(this, checkStyle(textStyles));
		setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(textfield);
		new EventForwarder(textfield, this);

		pickerButton = new Button(this, SWT.ARROW | SWT.DOWN);
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.RIGHT, SWT.FILL).hint(BUTTON_WIDTH, BUTTON_HEIGHT)
				.applyTo(pickerButton);
		pickerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleClick();
			}
		});

		dateConverterStrategy = new RegexDateConverterStrategy(textfield);
	}

	@Override
	public void dispose() {
		if (datePicker != null) {
			datePicker.dispose();
		}
		super.dispose();
	}

	public IDateConverterStrategy getDateConverterStrategy() {
		return dateConverterStrategy;
	}

	public Text getTextfield() {
		return textfield;
	}

	public void setDateConverterStrategy(final IDateConverterStrategy dateConverterStrategy) {
		this.dateConverterStrategy = dateConverterStrategy;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		updateButtonEnablement();
		updateBgColor(enabled);
	}

	@Override
	public void setForeground(final Color color) {
		super.setForeground(color);
		textfield.setForeground(color);
	}

	@Override
	public void setBackground(final Color color) {
		this.bgColor = color;
		updateBgColor(isEnabled());
	}

	@Override
	public void setToolTipText(final String string) {
		super.setToolTipText(string);
		textfield.setToolTipText(string);
	}

	/**
	 * Updates the enabled state of the picker button, based on the composite's
	 * enabled state and the text fields editable state.
	 * 
	 * @since 3.0
	 */
	public void updateButtonEnablement() {
		if (!isDisposed()) {
			final boolean enabledButton = getEnabled() && textfield.getEditable();
			pickerButton.setEnabled(enabledButton);
		}
	}

	// helping methods
	//////////////////

	/**
	 * Removes the {@link SWT.BORDER} style, to prevent a awkward representation
	 * 
	 * @param style
	 *            the SWT style bits
	 * @return the style bits without SWT.BORDER
	 */
	private int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style &= ~SWT.BORDER;
		}
		return style;
	}

	private void handleClick() {
		if (!(textfield.isEnabled() && textfield.getEditable())) {
			return;
		}
		if (datePicker == null || datePicker.isDisposed()) {
			datePicker = new DatePicker(this);
		}
		if (!datePicker.isVisible()) {
			final Point p = textfield.toDisplay(textfield.getLocation().x, textfield.getLocation().y);
			datePicker.setLocation(p.x, p.y + textfield.getBounds().height);
			datePicker.open(parseDate(textfield.getText()));
		} else {
			datePicker.close();
		}
	}

	private Button getPickerButton() {
		return pickerButton;
	}

	private Calendar parseDate(final String dateString) {
		Calendar result = null;
		final Date date = getDateConverterStrategy().getDateFromTextField(dateString);
		if (date != null) {
			result = Calendar.getInstance();
			result.setTime(date);
		}
		return result;
	}

	private void updateBgColor(final boolean isEnabled) {
		final Color color = isEnabled ? bgColor : getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		super.setBackground(color);
		textfield.setBackground(color);
	}

	// helping classes
	//////////////////

	/**
	 * Strategy for converting between {@link String} and {@link Date}
	 */
	public static interface IDateConverterStrategy {
		/**
		 * Parses the given date and sets it to the textfield
		 * 
		 * @param date
		 */
		void setDateToTextField(Date date);

		/**
		 * Converts the given dateString to a {@link Date}
		 * 
		 * @param dateString
		 * @return
		 */
		Date getDateFromTextField(String dateString);
	}

	/**
	 * This class shows and hides a DateTime "date picker" on request. *
	 * <p>
	 * This class is NOT API - do not reference. Public for testing only.
	 */
	public static final class DatePicker {

		private Shell shell;
		private DateTime calendar;
		private DatePickerComposite datePicker;

		/**
		 * On windows the Calendar widget has a header that has a zoomOut /
		 * zoomIn ability. We need to keep count of the clicks needed until we
		 * can close the widget (i.e. last zoom in level). See Bug 288354,
		 * comment #4, point #3.
		 */
		private int clicksToClose = 1;

		/**
		 * Create a new DatePicker instance.
		 * <p>
		 * You must invoke {@link #dispose()} to give up native resources held
		 * by this class.
		 * 
		 * @param text
		 *            a SWT text field that will received the picked date.
		 * @param pickerButton
		 *            the button that will trigger showing the date picker.
		 */
		protected DatePicker(final DatePickerComposite datePicker) {
			this.datePicker = datePicker;
			shell = new Shell(datePicker.getShell(), SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			GridLayoutFactory.fillDefaults().margins(1, 1).applyTo(shell);

			calendar = new DateTime(shell, SWT.CALENDAR | SWT.SHORT);
			calendar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
			shell.pack();

			calendar.addMouseListener(new MouseAdapter() {
				private final IZoneFinder zoneFinder = createZoneFinder(calendar);

				@Override
				public void mouseUp(final MouseEvent e) {
					if (e.button != 1) {
						return;
					}
					if (zoneFinder.getZone() == IZoneFinder.BODY) {
						clicksToClose = Math.max(clicksToClose - 1, 0);
					} else if (zoneFinder.getZone() == IZoneFinder.ZOOM_OUT) {
						clicksToClose = Math.min(4, clicksToClose + 1);
					}
					if (clicksToClose == 0) {
						setDateToTextfield();
						clicksToClose = 1;
						close();
					}
				}
			});

			calendar.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.character == SWT.CR) {
						clicksToClose = Math.max(clicksToClose - 1, 0);
						if (clicksToClose == 0) {
							setDateToTextfield();
							clicksToClose = 1;
							close();
						}
					}
				}
			});

			calendar.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					final Display display = e.widget.getDisplay();
					final Control focusControl = display.getCursorControl();
					if (focusControl != datePicker.getPickerButton()) {
						close();
					}
				}
			});
		}

		public void dispose() {
			if (!SwtUtilities.isDisposed(shell)) {
				shell.dispose();
			}
		}

		public void setLocation(final int x, final int y) {
			shell.setLocation(x, y);
		}

		public boolean isDisposed() {
			return shell == null || shell.isDisposed();
		}

		public boolean isVisible() {
			return shell.isVisible();
		}

		public void close() {
			shell.setVisible(false);
		}

		public void open(Calendar newDate) {
			if (isVisible()) {
				return;
			}

			shell.open();
			shell.setVisible(true);

			// if no date was supplied, use the current date
			if (null == newDate) {
				newDate = Calendar.getInstance();
				newDate.setTime(new Date());
			}

			calendar.setYear(newDate.get(Calendar.YEAR));
			calendar.setMonth(newDate.get(Calendar.MONTH));
			calendar.setDay(newDate.get(Calendar.DAY_OF_MONTH));
		}

		// helping methods
		//////////////////

		private IZoneFinder createZoneFinder(final DateTime parent) {
			IZoneFinder result;
			if (isVistaOrLater()) {
				result = new VistaZoneFinder(parent);
			} else if (Util.isWin32()) {
				result = new XPZoneFinder(parent);
			} else if (Util.isMac()) {
				result = new MacZoneFinder(parent);
			} else {
				result = new DefaultZoneFinder();
			}
			return result;
		}

		private boolean isVistaOrLater() {
			boolean result = false;
			if (Util.isWin32()) {
				final String osVer = System.getProperty("os.version"); //$NON-NLS-1$
				try {
					final double version = Double.valueOf(osVer);
					result = version >= 6.0;
				} catch (final NumberFormatException nfe) {
					// ignore
				} catch (final NullPointerException npe) {
					// ignore
				}
			}
			return result;
		}

		private void setDateToTextfield() {
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, calendar.getYear());
			cal.set(Calendar.MONTH, calendar.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, calendar.getDay());
			datePicker.getDateConverterStrategy().setDateToTextField(cal.getTime());
		}
	}

	/**
	 * Determines what will happen on a click, based on the current cursor
	 * position within the widget (header / body / zoom out area).
	 */
	private interface IZoneFinder extends MouseMoveListener {
		/**
		 * The cursor is in the body of the native picker. Clicking here selects
		 * a date.
		 */
		int BODY = 0;
		/**
		 * The cursor is in the header of the native picker. Clicking here does
		 * not select a date.
		 */
		int HEADER = 1;
		/**
		 * The cursor is in the zoom out area of the native picker (vista /
		 * win7). Clicking here zoom's out. Adds an extra click (zoom in) to the
		 * number of click's required to close the picker (!).
		 */
		int ZOOM_OUT = 2;

		int getZone();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Calculations for Vista and Win7.
	 */
	private static final class VistaZoneFinder implements IZoneFinder {
		/** Height of the header. */
		final int headerHeight = 48;
		/** Height of 'dead' zone at top of header. */
		final int topDeadZone = 4;
		/** Height of 'dead' zone at bottom of header. */
		final int bottomDeadZone = 16;
		/** Top of the buttons. */
		final int buttonTop = 9;
		/** Bottom of the buttons. */
		final int buttonBottom = 26;
		/** Right end of the button / Left start of header. */
		final int headerLeft = 20;
		/** right end of header / Left start of button. */
		final int headerRight = 204;

		private int zone = BODY;

		public VistaZoneFinder(final DateTime parent) {
			SWTFacade.getDefault().addMouseMoveListener(parent, this);
		}

		public void mouseMove(final MouseEvent e) {
			if (e.y < headerHeight) {
				if (e.y > headerHeight - bottomDeadZone || e.y < topDeadZone) {
					zone = HEADER;
				} else {
					if ((e.x < headerLeft && e.y > buttonTop && e.y < buttonBottom)
							|| (e.x > headerRight && e.y > buttonTop && e.y < buttonBottom)) {
						zone = HEADER;
					} else {
						zone = ZOOM_OUT;
					}
				}
			} else {
				zone = BODY;
			}
		}

		public int getZone() {
			return zone;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Calculations for XP (or earlier).
	 */
	private static final class XPZoneFinder implements IZoneFinder {
		/** Height of the header. */
		final int headerHeight = 45;

		private int zone = BODY;

		public XPZoneFinder(final DateTime parent) {
			SWTFacade.getDefault().addMouseMoveListener(parent, this);
		}

		public void mouseMove(final MouseEvent e) {
			zone = e.y < headerHeight ? HEADER : BODY;
		}

		public int getZone() {
			return zone;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Calculations for Mac.
	 */
	private static final class MacZoneFinder implements IZoneFinder {
		/** Height of the header. */
		final int headerHeight = 40;

		private int zone = BODY;

		public MacZoneFinder(final DateTime parent) {
			SWTFacade.getDefault().addMouseMoveListener(parent, this);
		}

		public void mouseMove(final MouseEvent e) {
			zone = e.y < headerHeight ? HEADER : BODY;
		}

		public int getZone() {
			return zone;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Generic implementation. Always assumes to be in the 'body' zone.
	 */
	private static final class DefaultZoneFinder implements IZoneFinder {
		public void mouseMove(final MouseEvent e) {
			// unused
		}

		public int getZone() {
			return BODY;
		}
	}

	/**
	 * Default implementation for a {@link IDateConverterStrategy} that will be
	 * used, when no other implementation was supplied. This implementation
	 * tries to parse the Date with a Regular Expression, but does only support
	 * simple DateFormats like "dd.MM.yyyy" and "dd.MM.yyyy HH:mm"
	 */
	private static class RegexDateConverterStrategy implements IDateConverterStrategy {

		private final Text textfield;

		public RegexDateConverterStrategy(final Text textfield) {
			this.textfield = textfield;
		}

		public void setDateToTextField(final Date date) {
			final String dateString = new SimpleDateFormat("dd.MM.yyyy").format(date); //$NON-NLS-1$

			final String oldText = textfield.getText();

			if (oldText.contains(":")) { //$NON-NLS-1$
				final Pattern pattern = Pattern.compile("([ \\d\\.]+)\\s+(.*?:.*?)"); //$NON-NLS-1$
				final Matcher matcher = pattern.matcher(oldText);

				if (matcher.matches()) {
					final String oldTime = matcher.group(2);
					textfield.setText(dateString + " " + oldTime); //$NON-NLS-1$
				}
			} else {
				textfield.setText(dateString);
			}
			textfield.setFocus();
		}

		public Date getDateFromTextField(final String dateString) {
			Calendar result = null;
			final Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+).*?"); //$NON-NLS-1$
			final Matcher matcher = pattern.matcher(dateString);

			if (matcher.matches() && matcher.groupCount() == 3) {
				int month = Integer.parseInt(matcher.group(2));
				month -= 1;

				int year = Integer.parseInt(matcher.group(3));
				if (year < 100) {
					year += 1900;
				}

				result = Calendar.getInstance();
				result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(1)));
				result.set(Calendar.MONTH, month);
				result.set(Calendar.YEAR, year);
				return result.getTime();
			}
			return null;
		}
	}
}
