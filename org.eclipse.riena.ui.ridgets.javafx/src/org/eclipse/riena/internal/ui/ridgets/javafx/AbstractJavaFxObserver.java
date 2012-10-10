package org.eclipse.riena.internal.ui.ridgets.javafx;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.AbstractObserver;

public abstract class AbstractJavaFxObserver<T, E extends Event> extends
		AbstractObserver<T> implements EventHandler<E> {

	public AbstractJavaFxObserver(IRidget source) {
		super(source);
	}

	@Override
	public void handle(E event) {
		fireAction(event);
	}

	/**
	 * This method forwards the given Event to the collection of listeners.
	 * <p>
	 * Must be implemented by subclasses. Subclasses are free to create an
	 * entirely new event and forward that instead of the original one, if
	 * necessary.
	 */
	protected abstract void fireAction(E evt);

}
