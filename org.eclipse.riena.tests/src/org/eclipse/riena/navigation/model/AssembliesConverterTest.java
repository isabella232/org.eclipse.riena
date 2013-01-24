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
package org.eclipse.riena.navigation.model;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.ISubApplicationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Tests of the class {@code AssembliesConverter}.
 */
@NonUITestCase
public class AssembliesConverterTest extends TestCase {

	/**
	 * Tests the method {@code convert(INavigationAssemblyExtension)}.
	 */
	public void testConvertINavigationAssemblyExtension() {

		final INavigationAssemblyExtension legacyAssembly = new INavigationAssemblyExtension() {

			public ISubApplicationNodeExtension getSubApplicationNode() {
				return null;
			}

			public IModuleGroupNodeExtension getModuleGroupNode() {
				return new IModuleGroupNodeExtension() {

					public String getTypeId() {
						return "typeMg1";
					}

					public String getInstanceId() {
						return null;
					}

					public INavigationAssemblyExtension[] getAssemblies() {
						return null;
					}

					public IModuleNodeExtension[] getModuleNodes() {
						return null;
					}

					public IModuleNodeExtension[] getChildNodes() {
						return null;
					}
				};
			}

			public IModuleNodeExtension getModuleNode() {
				return null;
			}

			public ISubModuleNodeExtension getSubModuleNode() {
				return null;
			}

			public INavigationAssembler createNavigationAssembler() {
				return new TestSecondModuleGroupNodeAssembler();
			}

			public String getNavigationAssembler() {
				return "dummyAssembler";
			}

			public String getParentNodeId() {
				return "0815";
			}

			public int getStartOrder() {
				return 123;
			}

			public String getId() {
				return "4711";
			}

		};

		final INavigationAssembly2Extension assembly2 = AssembliesConverter.convert(legacyAssembly);
		assertNotNull(assembly2);
		assertEquals("4711", assembly2.getId());
		assertEquals("0815", assembly2.getParentNodeId());
		assertEquals(123, assembly2.getStartOrder());
		assertEquals("dummyAssembler", assembly2.getNavigationAssembler());
		assertTrue(assembly2.createNavigationAssembler() instanceof TestSecondModuleGroupNodeAssembler);
		assertEquals(0, assembly2.getSubApplications().length);
		assertEquals(1, assembly2.getModuleGroups().length);
		assertEquals("typeMg1", assembly2.getModuleGroups()[0].getNodeId());
		assertEquals(0, assembly2.getModules().length);
		assertEquals(0, assembly2.getSubModules().length);

	}

	/**
	 * Tests the method {@code convert(ISubApplicationNodeExtension)}.
	 */
	public void testConvertISubApplicationNodeExtension() {

		final ISubApplicationNodeExtension legacySubApp = new ISubApplicationNodeExtension() {

			public String getLabel() {
				return "subAppLabel";
			}

			public String getIcon() {
				return "subAppIcon";
			}

			public String getViewId() {
				return "p2";
			}

			public IModuleGroupNodeExtension[] getModuleGroupNodes() {
				return getChildNodes();
			}

			public IModuleGroupNodeExtension[] getChildNodes() {
				return new IModuleGroupNodeExtension[] { new IModuleGroupNodeExtension() {

					public IModuleNodeExtension[] getModuleNodes() {
						return null;
					}

					public IModuleNodeExtension[] getChildNodes() {
						return null;
					}

					public String getTypeId() {
						return "mgId";
					}

					public String getInstanceId() {
						return null;
					}

					public INavigationAssemblyExtension[] getAssemblies() {
						return null;
					}
				} };
			}

			public String getTypeId() {
				return "subAppId";
			}

			public String getInstanceId() {
				return null;
			}

			public INavigationAssemblyExtension[] getAssemblies() {
				return null;
			}

		};

		final ISubApplicationNode2Extension subApp2 = ReflectionUtils.invokeHidden(AssembliesConverter.class,
				"convert", legacySubApp);
		assertNotNull(subApp2);
		assertEquals("subAppLabel", subApp2.getName());
		assertEquals("subAppIcon", subApp2.getIcon());
		assertEquals("p2", subApp2.getPerspectiveId());
		assertEquals("subAppId", subApp2.getNodeId());
		assertEquals(1, subApp2.getChildNodes().length);
		assertEquals("mgId", subApp2.getChildNodes()[0].getNodeId());

	}

	/**
	 * Tests the method {@code convert(IModuleGroupNodeExtension)}.
	 */
	public void testConvertIModuleGroupNodeExtension() {

		final IModuleGroupNodeExtension legacy = new IModuleGroupNodeExtension() {

			public IModuleNodeExtension[] getModuleNodes() {
				return getChildNodes();
			}

			public IModuleNodeExtension[] getChildNodes() {
				return new IModuleNodeExtension[] { new IModuleNodeExtension() {

					public String getLabel() {
						return null;
					}

					public String getIcon() {
						return null;
					}

					public boolean isUnclosable() {
						return false;
					}

					public ISubModuleNodeExtension[] getSubModuleNodes() {
						return null;
					}

					public ISubModuleNodeExtension[] getChildNodes() {
						return null;
					}

					public String getTypeId() {
						return "m1";
					}

					public String getInstanceId() {
						return null;
					}

					public INavigationAssemblyExtension[] getAssemblies() {
						return null;
					}
				} };
			}

			public String getTypeId() {
				return "mgId";
			}

			public String getInstanceId() {
				return null;
			}

			public INavigationAssemblyExtension[] getAssemblies() {
				return null;
			}

		};

		final IModuleGroupNode2Extension mg2 = ReflectionUtils.invokeHidden(AssembliesConverter.class, "convert",
				legacy);
		assertNotNull(mg2);
		assertNull(mg2.getName());
		assertNull(mg2.getIcon());
		assertEquals("mgId", mg2.getNodeId());
		assertEquals(1, mg2.getChildNodes().length);
		assertEquals("m1", mg2.getChildNodes()[0].getNodeId());

	}

	/**
	 * Tests the method {@code convert(IModuleNodeExtension)}.
	 */
	public void testConvertIModuleNodeExtension() {

		final IModuleNodeExtension legacy = new IModuleNodeExtension() {

			public ISubModuleNodeExtension[] getSubModuleNodes() {
				return getChildNodes();
			}

			public ISubModuleNodeExtension[] getChildNodes() {
				return new ISubModuleNodeExtension[] { new ISubModuleNodeExtension() {

					public String getLabel() {
						return null;
					}

					public String getIcon() {
						return null;
					}

					public String getViewId() {
						return null;
					}

					public boolean isShared() {
						return false;
					}

					public ISubModuleNodeExtension[] getSubModuleNodes() {
						return null;
					}

					public ISubModuleNodeExtension[] getChildNodes() {
						return null;
					}

					public boolean isSelectable() {
						return false;
					}

					public boolean isRequiresPreparation() {
						return false;
					}

					public String getTypeId() {
						return "sm1";
					}

					public String getInstanceId() {
						return null;
					}

					public INavigationAssemblyExtension[] getAssemblies() {
						return null;
					}

					public IController createController() {
						return null;
					}
				} };
			}

			public String getTypeId() {
				return "mId";
			}

			public String getInstanceId() {
				return null;
			}

			public INavigationAssemblyExtension[] getAssemblies() {
				return null;
			}

			public String getLabel() {
				return "mLabel";
			}

			public String getIcon() {
				return "mIcon";
			}

			public boolean isUnclosable() {
				return true;
			}

		};

		final IModuleNode2Extension m2 = ReflectionUtils.invokeHidden(AssembliesConverter.class, "convert", legacy);
		assertNotNull(m2);
		assertEquals("mLabel", m2.getName());
		assertEquals("mIcon", m2.getIcon());
		assertFalse(m2.isClosable());
		assertEquals("mId", m2.getNodeId());
		assertEquals(1, m2.getChildNodes().length);
		assertEquals("sm1", m2.getChildNodes()[0].getNodeId());

	}

}
