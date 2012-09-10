package org.eclipse.riena.navigation.ui.javafx.views;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.javafx.utils.JavaFxControlFinder;
import org.eclipse.riena.ui.javafx.utils.RienaCssLoader;
import org.eclipse.riena.ui.ridgets.javafx.swtjavafx.uibinding.SwtJavaFxViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class JavaFxSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(Composite parent) {

		GridLayoutFactory.fillDefaults().margins(0, 0).numColumns(1)
				.applyTo(parent);

		final FXCanvas canvas = new FXCanvas(parent, SWT.BORDER);
		Scene scene = createScene();
		RienaCssLoader.getInstance().addStylesheets(scene);
		canvas.setScene(scene);
		GridDataFactory.fillDefaults().grab(true, true)
				.align(SWT.FILL, SWT.FILL).applyTo(canvas);

	}

	protected abstract Scene createScene();

	@Override
	protected AbstractViewBindingDelegate createBinding() {
		return new SwtJavaFxViewBindingDelegate();
	}

	@Override
	protected void addUIControls(final Composite composite) {

		super.addUIControls(composite);

		FXCanvas canvas = getFxCanvas(composite);
		if (canvas != null) {
			Pane root = getRootPane(canvas);
			if (root != null) {
				final JavaFxControlFinder finder = new JavaFxControlFinder(root) {
					@Override
					public void handleBoundControl(final Node node,
							final String bindingProperty) {
						addUIControl(node);
					}
				};
				finder.run();
			}
		}

	}

	private Pane getRootPane(FXCanvas canvas) {
		if (canvas.getScene() == null) {
			return null;
		}
		Parent root = canvas.getScene().getRoot();
		if (root instanceof Pane) {
			return (Pane) root;
		}
		return null;
	}

	private FXCanvas getFxCanvas(final Composite composite) {
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FXCanvas) {
				return (FXCanvas) children[i];
			} else if (children[i] instanceof Composite) {
				FXCanvas canvas = getFxCanvas((Composite) children[i]);
				if (canvas != null) {
					return canvas;
				}
			}
		}
		return null;
	}

}
