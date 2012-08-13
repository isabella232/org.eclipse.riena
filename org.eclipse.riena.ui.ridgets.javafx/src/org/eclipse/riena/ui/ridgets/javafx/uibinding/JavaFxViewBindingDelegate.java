package org.eclipse.riena.ui.ridgets.javafx.uibinding;

import javafx.scene.Node;

import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;
import org.eclipse.riena.ui.javafx.utils.JavaFxNodeIdentificationSupport;
import org.eclipse.riena.ui.ridgets.annotation.processor.RidgetContainerAnnotationProcessor;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;

public class JavaFxViewBindingDelegate extends AbstractViewBindingDelegate {

	public JavaFxViewBindingDelegate() {
		this(JavaFxControlRidgetMapper.getInstance());
	}

	public JavaFxViewBindingDelegate(final IControlRidgetMapper<Object> mapper) {
		super(JavaFxBindingPropertyLocator.getInstance(), mapper);
	}

	@Override
	public void injectRidgets(final IController controller) {
		super.injectRidgets(controller);
		RidgetContainerAnnotationProcessor.getInstance().processAnnotations(
				controller);
	}

	@Override
	public void addUIControl(final Object uiControl, final String bindingId) {
		super.addUIControl(uiControl, bindingId);
		JavaFxBindingPropertyLocator.getInstance().setBindingProperty(
				uiControl, bindingId);

		if (uiControl instanceof Node) {
			JavaFxNodeIdentificationSupport.setIdentification((Node) uiControl,
					bindingId);
		}
	}

}
