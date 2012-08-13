package org.eclipse.riena.ui.ridgets.javafx.swtjavafx.uibinding;

import javafx.scene.Node;

import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;
import org.eclipse.riena.ui.javafx.utils.JavaFxNodeIdentificationSupport;
import org.eclipse.riena.ui.ridgets.annotation.processor.RidgetContainerAnnotationProcessor;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;
import org.eclipse.swt.widgets.Widget;

public class SwtJavaFxViewBindingDelegate extends AbstractViewBindingDelegate {

	public SwtJavaFxViewBindingDelegate() {
		this(SwtJavaFxControlRidgetMapper.getInstance());
	}

	public SwtJavaFxViewBindingDelegate(
			final IControlRidgetMapper<Object> mapper) {
		super(SwtJavaFxBindingPropertyLocator.getInstance(), mapper);
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

		setBindingProperty(uiControl, bindingId);
		setIdentificationSupport(uiControl, bindingId);

	}

	private void setBindingProperty(final Object uiControl,
			final String bindingId) {
		if (uiControl instanceof Node) {
			JavaFxBindingPropertyLocator.getInstance().setBindingProperty(
					uiControl, bindingId);
		} else {
			SWTBindingPropertyLocator.getInstance().setBindingProperty(
					uiControl, bindingId);
		}
	}

	private void setIdentificationSupport(final Object uiControl,
			final String bindingId) {
		if (uiControl instanceof Node) {
			JavaFxNodeIdentificationSupport.setIdentification((Node) uiControl,
					bindingId);
		} else if (uiControl instanceof Widget) {
			WidgetIdentificationSupport.setIdentification((Widget) uiControl,
					bindingId);
		}
	}

}
