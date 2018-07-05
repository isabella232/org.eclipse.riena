package org.eclipse.riena.ui.swt;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.layout.DpiGridLayout;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

import junit.framework.TestCase;

@UITestCase
public class AbstractMasterDetailsCompositeTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout(new FillLayout());
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		LnfManager.setLnf(new RienaDefaultLnf());
	}

	public void testAbstractMasterDetailsComposite() throws Exception {

		final RienaDefaultLnf defaultLnf = LnfManager.getLnf();

		final MyLnf lnf = new MyLnf();
		lnf.setUseDpiGridLayout(false);
		LnfManager.setLnf(lnf);
		lnf.initialize();
		MyMasterDetailsComposite composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		assertTrue(composite.getLayout() instanceof GridLayout);

		lnf.setUseDpiGridLayout(true);
		composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		assertTrue(composite.getLayout() instanceof DpiGridLayout);

		LnfManager.setLnf(defaultLnf);

	}

	/**
	 * Tests the method {@code getMargins()}.
	 * 
	 * @throws Exception
	 *             Exception handled by Junit
	 */
	public void testGetMargins() throws Exception {

		final MyLnf lnf = new MyLnf();
		lnf.setUseDpiGridLayout(false);
		LnfManager.setLnf(lnf);
		lnf.initialize();
		MyMasterDetailsComposite composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		((GridLayout) composite.getLayout()).marginHeight = 10;
		((GridLayout) composite.getLayout()).marginWidth = 12;
		assertEquals(10, composite.getMargins().x);
		assertEquals(12, composite.getMargins().y);

		lnf.setUseDpiGridLayout(true);
		composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		((DpiGridLayout) composite.getLayout()).marginHeight = 4;
		((DpiGridLayout) composite.getLayout()).marginWidth = 6;
		assertEquals(4, composite.getMargins().x);
		assertEquals(6, composite.getMargins().y);

	}

	/**
	 * Tests the method {@code getSpacing()}.
	 * 
	 * @throws Exception
	 *             Exception handled by Junit
	 */
	public void testGetSpacing() throws Exception {

		final MyLnf lnf = new MyLnf();
		lnf.setUseDpiGridLayout(false);
		LnfManager.setLnf(lnf);
		lnf.initialize();
		MyMasterDetailsComposite composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		Composite masterAreaComposite = ReflectionUtils.getHidden(composite, "master");
		((GridLayout) masterAreaComposite.getLayout()).horizontalSpacing = 33;
		((GridLayout) composite.getLayout()).verticalSpacing = 22;
		assertEquals(33, composite.getSpacing().x);
		assertEquals(22, composite.getSpacing().y);

		lnf.setUseDpiGridLayout(true);
		composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		masterAreaComposite = ReflectionUtils.getHidden(composite, "master");
		((DpiGridLayout) masterAreaComposite.getLayout()).horizontalSpacing = 9;
		((DpiGridLayout) composite.getLayout()).verticalSpacing = 16;
		assertEquals(9, composite.getSpacing().x);
		assertEquals(16, composite.getSpacing().y);

	}

	/**
	 * Tests the method {@code setMargins(int,int)}.
	 * 
	 * @throws Exception
	 *             Exception handled by Junit
	 */
	public void testSetMargins() throws Exception {

		final MyLnf lnf = new MyLnf();
		lnf.setUseDpiGridLayout(false);
		LnfManager.setLnf(lnf);
		lnf.initialize();
		MyMasterDetailsComposite composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		composite.setMargins(12, 34);
		assertEquals(12, composite.getMargins().x);
		assertEquals(34, composite.getMargins().y);

		lnf.setUseDpiGridLayout(true);
		composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		composite.setMargins(9, 8);
		assertEquals(9, composite.getMargins().x);
		assertEquals(8, composite.getMargins().y);

	}

	/**
	 * Tests the method {@code setSpacing(int,int)}.
	 * 
	 * @throws Exception
	 *             Exception handled by Junit
	 */
	public void testSetSpacing() throws Exception {

		final MyLnf lnf = new MyLnf();
		lnf.setUseDpiGridLayout(false);
		LnfManager.setLnf(lnf);
		lnf.initialize();
		MyMasterDetailsComposite composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		composite.setSpacing(11, 22);
		assertEquals(11, composite.getSpacing().x);
		assertEquals(22, composite.getSpacing().y);

		lnf.setUseDpiGridLayout(true);
		composite = new MyMasterDetailsComposite(shell, SWT.DEFAULT, SWT.TOP);
		composite.setSpacing(31, 41);
		assertEquals(31, composite.getSpacing().x);
		assertEquals(41, composite.getSpacing().y);

	}

	/**
	 * Dummy implementation of AbstractMasterDetailsCompositeNG.
	 */
	private static class MyMasterDetailsComposite extends AbstractMasterDetailsComposite {

		public MyMasterDetailsComposite(final Composite parent, final int style, final int orientation) {
			super(parent, style, orientation);
		}

		@Override
		protected Control createTable(final Composite tableComposite, final TableColumnLayout layout) {
			return new Table(tableComposite, SWT.DEFAULT);
		}

	}

	/**
	 * Look and Feel where it is possible to change the prefered layout.
	 */
	private static class MyLnf extends RienaDefaultLnf {

		private boolean dpi;

		public void setUseDpiGridLayout(final boolean dpi) {
			this.dpi = dpi;
		}

		@Override
		public boolean useDpiGridLayout() {
			return dpi;
		}

	}

}
