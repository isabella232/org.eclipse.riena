/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.filter.impl;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerRidget;
import org.eclipse.riena.ui.filter.extension.IRuleMapperExtension;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerNavigationMapper;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerRidgetMapper;
import org.eclipse.riena.ui.filter.extension.IRuleValidatorRidgetMapper;

/**
 * Tests of the class {@link RulesProvider}.
 */
@NonUITestCase
public class RulesProviderTest extends TestCase {

	private RulesProvider provider;

	@Override
	protected void setUp() throws Exception {
		provider = new RulesProvider();
	}

	@Override
	protected void tearDown() throws Exception {
		provider = null;
	}

	/**
	 * Tests the method {@code getRuleMarkerRidget(String).}
	 */
	public void testGetRuleMarkerRidget() {

		final IRuleMapperExtension[] mappers = new IRuleMapperExtension[] { new Mapper() };
		provider.update(mappers);

		IUIFilterRuleMarkerRidget rule = provider.getRuleMarkerRidget("disabled");
		assertEquals(new DisabledMarker(), rule.getMarker());
		rule = provider.getRuleMarkerRidget("hidden");
		assertEquals(new HiddenMarker(), rule.getMarker());
		rule = provider.getRuleMarkerRidget("mandatory");
		assertEquals(new MandatoryMarker(), rule.getMarker());
		rule = provider.getRuleMarkerRidget("output");
		assertEquals(new OutputMarker(), rule.getMarker());
		rule = provider.getRuleMarkerRidget("dummy");
		assertNull(rule);

	}

	/**
	 * Tests the method {@code getRuleMarkerNavigation(String).}
	 */
	public void testGetRuleMarkerNavigation() {

		final IRuleMapperExtension[] mappers = new IRuleMapperExtension[] { new Mapper() };
		provider.update(mappers);

		IUIFilterRuleMarkerNavigation rule = provider.getRuleMarkerNavigation("disabled");
		assertEquals(new DisabledMarker(), rule.getMarker());
		rule = provider.getRuleMarkerNavigation("hidden");
		assertEquals(new HiddenMarker(), rule.getMarker());
		rule = provider.getRuleMarkerNavigation("mandatory");
		assertNull(rule);
		rule = provider.getRuleMarkerNavigation("output");
		assertNull(rule);
		rule = provider.getRuleMarkerNavigation("dummy");
		assertNull(rule);

	}

	/**
	 * Tests the method {@code getRuleMarkerMenuItem(String).}
	 */
	public void testGetRuleMarkerMenuItem() {

		final IRuleMapperExtension[] mappers = new IRuleMapperExtension[] { new Mapper() };
		provider.update(mappers);

		IUIFilterRuleMarkerRidget rule = provider.getRuleMarkerMenuItem("disabled");
		assertEquals(new DisabledMarker(), rule.getMarker());
		rule = provider.getRuleMarkerMenuItem("hidden");
		assertEquals(new HiddenMarker(), rule.getMarker());
		rule = provider.getRuleMarkerMenuItem("mandatory");
		assertNull(rule);
		rule = provider.getRuleMarkerMenuItem("output");
		assertNull(rule);
		rule = provider.getRuleMarkerMenuItem("dummy");
		assertNull(rule);

	}

	private class Mapper implements IRuleMapperExtension {

		public IRuleMarkerNavigationMapper getNavigationDisabledMarker() {
			return new IRuleMarkerNavigationMapper() {
				public IUIFilterRuleMarkerNavigation getRuleClass() {
					return new AbstractRuleMarkerNavigation() {
						public IMarker getMarker() {
							return new DisabledMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerNavigationMapper getNavigationHiddenMarker() {
			return new IRuleMarkerNavigationMapper() {
				public IUIFilterRuleMarkerNavigation getRuleClass() {
					return new AbstractRuleMarkerNavigation() {
						public IMarker getMarker() {
							return new HiddenMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getRidgetDisabledMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new DisabledMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getRidgetHiddenMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new HiddenMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getRidgetMandatoryMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new MandatoryMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getRidgetOutputMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new OutputMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getMenuItemDisabledMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new DisabledMarker();
						}
					};
				}
			};
		}

		public IRuleMarkerRidgetMapper getMenuItemHiddenMarker() {
			return new IRuleMarkerRidgetMapper() {
				public IUIFilterRuleMarkerRidget getRuleClass() {
					return new AbstractRuleMarkerRidget() {
						public IMarker getMarker() {
							return new HiddenMarker();
						}
					};
				}
			};
		}

		public IRuleValidatorRidgetMapper getRidgetValidator() {
			return null;
		}
	}

	private abstract class AbstractRuleMarkerNavigation implements IUIFilterRuleMarkerNavigation {

		public void setNode(final String nodeId) {
		}

		public void apply(final Object object) {
		}

		public boolean matches(final Object... object) {
			return false;
		}

		public void remove(final Object object) {
		}

	}

	private abstract class AbstractRuleMarkerRidget implements IUIFilterRuleMarkerRidget {

		public void setId(final String id) {
		}

		public void apply(final Object object) {
		}

		public boolean matches(final Object... object) {
			return false;
		}

		public void remove(final Object object) {
		}

	}

}
