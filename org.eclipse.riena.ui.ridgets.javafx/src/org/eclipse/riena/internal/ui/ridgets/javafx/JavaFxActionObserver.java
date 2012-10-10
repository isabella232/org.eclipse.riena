package org.eclipse.riena.internal.ui.ridgets.javafx;

import javafx.event.ActionEvent;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IRidget;

public class JavaFxActionObserver extends
		AbstractJavaFxObserver<IActionListener, ActionEvent> {

	public JavaFxActionObserver(IRidget source) {
		super(source);
	}

	@Override
	protected ListenerList<IActionListener> createList() {
		return new ListenerList<IActionListener>(IActionListener.class);
	}

	@Override
	protected void fireAction(ActionEvent evt) {
		final ListenerList<IActionListener> listeners = getListeners();
		if (listeners != null) {
			for (final IActionListener listener : listeners.getListeners()) {
				listener.callback();
			}
		}
	}

}
