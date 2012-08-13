package org.eclipse.riena.ui.ridgets.javafx.swtjavafx.uibinding;

import java.util.List;

import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.ui.ridgets.javafx.uibinding.JavaFxControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.AbstractControlRidgetMapper;

public class SwtJavaFxControlRidgetMapper extends AbstractControlRidgetMapper {

	private static final SingletonProvider<SwtJavaFxControlRidgetMapper> SCRM = new SingletonProvider<SwtJavaFxControlRidgetMapper>(
			SwtJavaFxControlRidgetMapper.class);

	/**
	 * Answer the singleton <code>SwtControlRidgetMapper</code>
	 * 
	 * @return the SwtControlRidgetMapper singleton
	 */
	public static SwtJavaFxControlRidgetMapper getInstance() {
		return SCRM.getInstance();
	}

	private SwtJavaFxControlRidgetMapper() {
		initDefaultMappings();
	}

	/**
	 * Sets the default mapping of UI control-classes to a ridget-classes
	 */
	private void initDefaultMappings() {

		List<Mapping> javaFxMappings = JavaFxControlRidgetMapper.getInstance()
				.getMapping();
		for (Mapping mapping : javaFxMappings) {
			addMapping(mapping);
		}
		List<Mapping> swtMappings = SwtControlRidgetMapper.getInstance()
				.getMapping();
		for (Mapping mapping : swtMappings) {
			addMapping(mapping);
		}

	}

}
