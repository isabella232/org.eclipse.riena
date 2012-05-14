package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.ValidCharacters;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A text field with validation
 */
public class Snippet374184 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());

			final Text text = UIControlsFactory.createText(shell);
			GridDataFactory.swtDefaults().indent(20, 0).applyTo(text);
			final ITextRidget zip = (ITextRidget) SwtRidgetFactory.createRidget(text);

			zip.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS), ValidationTime.ON_UI_CONTROL_EDIT);
			zip.addValidationRule(new MinLength(5), ValidationTime.ON_UI_CONTROL_EDIT);

			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

}
