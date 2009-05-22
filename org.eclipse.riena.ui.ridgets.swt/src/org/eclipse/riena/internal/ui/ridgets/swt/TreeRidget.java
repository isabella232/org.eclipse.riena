/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.IViewerObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSelectableRidget;

/**
 * Ridget for SWT {@link Tree} widgets.
 */
public class TreeRidget extends AbstractSelectableRidget implements ITreeRidget {

	private static final Listener ERASE_LISTENER = new EraseAndPaintListener();

	private final SelectionListener selectionTypeEnforcer;
	private final DoubleClickForwarder doubleClickForwarder;
	private final Queue<ExpansionCommand> expansionStack;
	private ListenerList<IActionListener> doubleClickListeners;

	private DataBindingContext dbc;
	/*
	 * Binds the viewer's multiple selection to the multiple selection
	 * observable. This binding hsa to be disposed when the ridget is set to
	 * output-only, to avoid updating the model. It has to be recreated when the
	 * ridget is set to not-output-only.
	 */
	private Binding viewerMSB;

	private TreeViewer viewer;
	/* keeps the last legal selection when in 'output only' mode */
	private TreeItem[] savedSelection;

	/*
	 * The original array of elements given as input to the ridget via the
	 * #bindToModel method. The ridget however works with the copy (treeRoots)
	 * in order to be independend of modification to the original array.
	 * 
	 * Calling #updateFromModel will synchronize the treeRoots array with the
	 * model array.
	 */
	private Object[] model;
	private Object[] treeRoots;
	private Class<? extends Object> treeElementClass;
	private String childrenAccessor;
	private String parentAccessor;
	private String[] valueAccessors;
	private String[] columnHeaders;
	private String enablementAccessor;
	private String visibilityAccessor;
	private String imageAccessor;
	private boolean showRoots = true;

	public TreeRidget() {
		selectionTypeEnforcer = new SelectionTypeEnforcer();
		doubleClickForwarder = new DoubleClickForwarder();
		expansionStack = new LinkedList<ExpansionCommand>();
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				applyEraseListener();
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				saveSelection();
				if (isOutputOnly()) {
					disposeMultipleSelectionBinding();
				} else {
					createMultipleSelectionBinding();
				}
			}
		});
	}

	@Override
	protected void bindUIControl() {
		Tree control = getUIControl();
		if (control != null && treeRoots != null) {
			checkColumns(control);
			bindToViewer(control);
			bindToSelection();
			control.addSelectionListener(selectionTypeEnforcer);
			control.addMouseListener(doubleClickForwarder);
			updateExpansionState();
			applyEraseListener();
			applyTableColumnHeaders(control);
		}
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Tree.class);
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		if (viewer != null) {
			Object[] elements = viewer.getExpandedElements();
			ExpansionCommand cmd = new ExpansionCommand(ExpansionState.RESTORE, elements);
			expansionStack.add(cmd);
		}
		if (dbc != null) {
			disposeMultipleSelectionBinding();
			dbc.dispose();
			dbc = null;
		}
		Tree control = getUIControl();
		if (control != null) {
			control.removeSelectionListener(selectionTypeEnforcer);
			control.removeMouseListener(doubleClickForwarder);
			control.removeListener(SWT.EraseItem, ERASE_LISTENER);
			control.removeListener(SWT.PaintItem, ERASE_LISTENER);
		}
		if (viewer != null) {
			// IMPORTANT: remove the change listeners from the input model.
			// Has to happen after disposing the binding to avoid affecting
			// the selection.
			// See also https://bugs.eclipse.org/243374
			viewer.setInput(null);
		}
		viewer = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final List<?> getRowObservables() {
		List<?> result = null;
		if (viewer != null) {
			ObservableListTreeContentProvider cp = (ObservableListTreeContentProvider) viewer.getContentProvider();
			result = new ArrayList<Object>(cp.getKnownElements());
		}
		return result;
	}

	protected void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String[] valueAccessors, String[] columnHeaders, String enablementAccessor,
			String visibilityAccessor, String imageAccessor) {
		Assert.isNotNull(treeRoots);
		Assert.isLegal(treeRoots.length > 0, "treeRoots must have at least one entry"); //$NON-NLS-1$
		Assert.isNotNull(treeElementClass);
		Assert.isNotNull(childrenAccessor);
		Assert.isNotNull(parentAccessor);
		Assert.isNotNull(valueAccessors);
		Assert.isLegal(valueAccessors.length > 0, "valueAccessors must have at least one entry"); //$NON-NLS-1$
		if (columnHeaders != null) {
			String msg = "Mismatch between number of valueAccessors and columnHeaders"; //$NON-NLS-1$
			Assert.isLegal(valueAccessors.length == columnHeaders.length, msg);
		}

		unbindUIControl();

		this.model = treeRoots;
		this.treeRoots = new Object[model.length];
		System.arraycopy(model, 0, this.treeRoots, 0, this.treeRoots.length);
		this.treeElementClass = treeElementClass;
		this.childrenAccessor = childrenAccessor;
		this.parentAccessor = parentAccessor;
		this.valueAccessors = new String[valueAccessors.length];
		System.arraycopy(valueAccessors, 0, this.valueAccessors, 0, this.valueAccessors.length);
		if (columnHeaders != null) {
			this.columnHeaders = new String[columnHeaders.length];
			System.arraycopy(columnHeaders, 0, this.columnHeaders, 0, this.columnHeaders.length);
		} else {
			this.columnHeaders = null;
		}
		this.enablementAccessor = enablementAccessor;
		this.visibilityAccessor = visibilityAccessor;
		this.imageAccessor = imageAccessor;

		expansionStack.clear();
		if (treeRoots.length == 1) {
			ExpansionCommand cmd = new ExpansionCommand(ExpansionState.EXPAND, treeRoots[0]);
			expansionStack.add(cmd);
		}

		bindUIControl();
	}

	/**
	 * Returns the TreeViewer instance used by this ridget or null.
	 */
	protected final TreeViewer getViewer() {
		return viewer;
	}

	/**
	 * Returns the column formatters for this ridget. Each entry in the array
	 * corresponds to a column (i.e. 0 for the 1st column, 1 for the 2nd, etc).
	 * If a column has no formatter associated, the array entry will be null.
	 * The array has the length {@code numColumns}.
	 * <p>
	 * Implementation note: This ridget does not support columns, so this array
	 * will be filled with null entries. Subclasses that support column
	 * formatters must override to return an appropriate array.
	 * 
	 * @param numColumns
	 *            return the number of columns, an integer >= 0.
	 * @return an array of IColumnFormatter, never null
	 */
	protected IColumnFormatter[] getColumnFormatters(int numColumns) {
		return new IColumnFormatter[numColumns];
	}

	// public methods
	// ///////////////

	@Override
	public Tree getUIControl() {
		return (Tree) super.getUIControl();
	}

	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ListenerList<IActionListener>(IActionListener.class);
		}
		doubleClickListeners.add(listener);
	}

	public void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor) {
		Assert.isNotNull(valueAccessor);
		String[] myValueAccessors = new String[] { valueAccessor };
		String[] noColumnHeaders = null;
		String noEnablementAccessor = null;
		String noVisibilityAccessor = null;
		String noImageAccessor = null;
		this.bindToModel(treeRoots, treeElementClass, childrenAccessor, parentAccessor, myValueAccessors,
				noColumnHeaders, noEnablementAccessor, noVisibilityAccessor, noImageAccessor);
	}

	public void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor, String enablementAccessor, String visibilityAccessor) {
		Assert.isNotNull(valueAccessor);
		String[] myValueAccessors = new String[] { valueAccessor };
		String[] noColumnHeaders = null;
		String noImageAccessor = null;
		this.bindToModel(treeRoots, treeElementClass, childrenAccessor, parentAccessor, myValueAccessors,
				noColumnHeaders, enablementAccessor, visibilityAccessor, noImageAccessor);
	}

	public void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor, String enablementAccessor, String visibilityAccessor,
			String imageAccessor) {
		Assert.isNotNull(valueAccessor);
		String[] myValueAccessors = new String[] { valueAccessor };
		String[] noColumnHeaders = null;
		this.bindToModel(treeRoots, treeElementClass, childrenAccessor, parentAccessor, myValueAccessors,
				noColumnHeaders, enablementAccessor, visibilityAccessor, imageAccessor);
	}

	public void collapse(Object element) {
		ExpansionCommand cmd = new ExpansionCommand(ExpansionState.COLLAPSE, element);
		expansionStack.add(cmd);
		updateExpansionState();
	}

	public void collapseAll() {
		ExpansionCommand cmd = new ExpansionCommand(ExpansionState.FULLY_COLLAPSE, null);
		expansionStack.add(cmd);
		updateExpansionState();
	}

	public void expand(Object element) {
		ExpansionCommand cmd = new ExpansionCommand(ExpansionState.EXPAND, element);
		expansionStack.add(cmd);
		updateExpansionState();
	}

	public void expandAll() {
		ExpansionCommand cmd = new ExpansionCommand(ExpansionState.FULLY_EXPAND, null);
		expansionStack.add(cmd);
		updateExpansionState();
	}

	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation will try to expand the path to the give option, to
	 * ensure that the corresponding tree element exists.
	 */
	@Override
	public boolean containsOption(Object option) {
		reveal(new Object[] { option });
		return super.containsOption(option);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * For each selection candidate in the List <tt>newSelection</tt>, this
	 * implementation will try to expand the path to the corresponding tree
	 * node, to ensure that the corresponding tree element is selectable.
	 */
	@Override
	public final void setSelection(List<?> newSelection) {
		reveal(newSelection.toArray());
		super.setSelection(newSelection);
		saveSelection();
	}

	public boolean getRootsVisible() {
		return showRoots;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation notes:
	 * <ul>
	 * <li>If showRoots is false, the children of the first entry in the array
	 * of treeRoots will be shown at level-0 of the tree</li>
	 * <li>This method must be ivoked before calling bindToModel(...). If
	 * changed afterwards it requires a call to bindToModel() or
	 * updateFromModel() to take effect.</li>
	 * </ul>
	 */
	public void setRootsVisible(boolean showRoots) {
		firePropertyChange(ITreeRidget.PROPERTY_ROOTS_VISIBLE, this.showRoots, this.showRoots = showRoots);
	}

	@Override
	public void updateFromModel() {
		treeRoots = new Object[model.length];
		System.arraycopy(model, 0, treeRoots, 0, treeRoots.length);
		if (viewer != null) {
			List<Object> selection = getSelection();
			Object[] expandedElements = viewer.getExpandedElements();
			viewer.getControl().setRedraw(false);
			try {
				// IMPORTANT: next line removes listeners from old model
				viewer.setInput(null);
				if (showRoots) {
					viewer.setInput(treeRoots);
				} else {
					FakeRoot fakeRoot = new FakeRoot(treeRoots[0], childrenAccessor);
					viewer.setInput(fakeRoot);
				}
				viewer.setExpandedElements(expandedElements);
				// update column specific formatters
				TreeRidgetLabelProvider labelProvider = (TreeRidgetLabelProvider) viewer.getLabelProvider();
				IColumnFormatter[] formatters = getColumnFormatters(labelProvider.getColumnCount());
				labelProvider.setFormatters(formatters);
				// update expanded/collapsed icons
				viewer.refresh();
				viewer.setSelection(new StructuredSelection(selection));
			} finally {
				viewer.getControl().setRedraw(true);
			}
		}
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	// helping methods
	// ////////////////

	private void applyEraseListener() {
		if (viewer != null) {
			Control control = viewer.getControl();
			control.removeListener(SWT.EraseItem, ERASE_LISTENER);
			if (!isEnabled() && MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
				control.addListener(SWT.EraseItem, ERASE_LISTENER);
				control.addListener(SWT.PaintItem, ERASE_LISTENER);
			}
		}
	}

	private void applyTableColumnHeaders(Tree control) {
		boolean headersVisible = columnHeaders != null;
		control.setHeaderVisible(headersVisible);
		if (headersVisible) {
			TreeColumn[] columns = control.getColumns();
			for (int i = 0; i < columns.length; i++) {
				String columnHeader = ""; //$NON-NLS-1$
				if (i < columnHeaders.length && columnHeaders[i] != null) {
					columnHeader = columnHeaders[i];
				}
				columns[i].setText(columnHeader);
			}
		}
	}

	/**
	 * Initialize databining for tree viewer.
	 */
	private void bindToViewer(final Tree control) {
		viewer = new TreeViewer(control);
		// content
		final Realm realm = SWTObservables.getRealm(Display.getDefault());
		// how to obtain an observable list of children from a given object (expansion)
		IObservableFactory listFactory = new IObservableFactory() {
			public IObservable createObservable(Object target) {
				if (target instanceof Object[]) {
					return Observables.staticObservableList(realm, Arrays.asList((Object[]) target));
				}
				Object value;
				if (target instanceof FakeRoot) {
					value = ((FakeRoot) target).getRoot();
				} else {
					value = target;
				}
				if (AbstractSWTWidgetRidget.isBean(treeElementClass)) {
					return BeansObservables.observeList(realm, value, childrenAccessor, treeElementClass);
				} else {
					return PojoObservables.observeList(realm, value, childrenAccessor, treeElementClass);
				}
			}
		};
		// how to get the parent from a given object
		TreeStructureAdvisor structureAdvisor = new GenericTreeStructureAdvisor(parentAccessor, treeElementClass);

		// how to create the content/structure for the tree
		ObservableListTreeContentProvider viewerCP = new ObservableListTreeContentProvider(listFactory,
				structureAdvisor);

		// refresh icons on addition / removal
		viewer.setContentProvider(viewerCP);
		viewerCP.getKnownElements().addSetChangeListener(new TreeContentChangeListener(viewer, structureAdvisor));
		// labels
		IColumnFormatter[] formatters = getColumnFormatters(valueAccessors.length);
		ILabelProvider viewerLP = TreeRidgetLabelProvider.createLabelProvider(viewer, treeElementClass, viewerCP
				.getKnownElements(), valueAccessors, enablementAccessor, imageAccessor, formatters);
		viewer.setLabelProvider(viewerLP);
		// input
		if (showRoots) {
			viewer.setInput(treeRoots);
		} else {
			FakeRoot fakeRoot = new FakeRoot(treeRoots[0], childrenAccessor);
			viewer.setInput(fakeRoot);
		}
		IObservableMap enablementAttr = createObservableAttribute(viewerCP, enablementAccessor);
		preventDisabledItemSelection(enablementAttr);
		IObservableMap visibilityAttr = createObservableAttribute(viewerCP, visibilityAccessor);
		monitorVisibility(viewer, structureAdvisor, visibilityAttr);
	}

	/**
	 * Initialize databinding related to selection handling (single/multi).
	 */
	private void bindToSelection() {
		dbc = new DataBindingContext();
		// viewer to single selection binding
		IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(viewer);
		dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE).setAfterGetValidator(new OutputAwareValidator(this)),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		// viewer to multi selection binding
		viewerMSB = null;
		if (!isOutputOnly()) {
			createMultipleSelectionBinding();
		}
		saveSelection();
	}

	private void checkColumns(Tree control) {
		int columnCount = control.getColumnCount() == 0 ? 1 : control.getColumnCount();
		String message = String.format("Tree has %d columns, expected: %d", columnCount, valueAccessors.length); //$NON-NLS-1$
		Assert.isLegal(columnCount == valueAccessors.length, message);
	}

	private void createMultipleSelectionBinding() {
		if (viewerMSB == null && dbc != null && viewer != null) {
			StructuredSelection currentSelection = new StructuredSelection(getSelection());
			IViewerObservableList viewerSelections = ViewersObservables.observeMultiSelection(viewer);
			viewerMSB = dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
					UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));
			viewer.setSelection(currentSelection);
		}
	}

	private IObservableMap createObservableAttribute(ObservableListTreeContentProvider viewerCP, String accessor) {
		IObservableMap result = null;
		if (accessor != null) {
			if (AbstractSWTWidgetRidget.isBean(treeElementClass)) {
				result = BeansObservables.observeMap(viewerCP.getKnownElements(), treeElementClass, accessor);
			} else {
				result = PojoObservables.observeMap(viewerCP.getKnownElements(), treeElementClass, accessor);
			}
		}
		return result;
	}

	private void disposeMultipleSelectionBinding() {
		if (viewerMSB != null) { // implies dbc != null
			viewerMSB.dispose();
			dbc.removeBinding(viewerMSB);
			viewerMSB = null;
		}
	}

	/**
	 * Filters out elements that are not visible. Monitors element visibility
	 * and updates the tree ridget.
	 */
	private void monitorVisibility(final TreeViewer viewer, final TreeStructureAdvisor structureAdvisor,
			final IObservableMap visibilityAttr) {
		if (visibilityAttr != null) {
			viewer.addFilter(new ViewerFilter() {
				@Override
				public boolean select(Viewer viewer, Object parentElement, Object element) {
					Object visible = visibilityAttr.get(element);
					return Boolean.FALSE.equals(visible) ? false : true;
				}
			});
			IMapChangeListener mapChangeListener = new IMapChangeListener() {
				public void handleMapChange(MapChangeEvent event) {
					Set<?> affectedElements = event.diff.getChangedKeys();
					for (Object element : affectedElements) {
						Object parent = structureAdvisor.getParent(element);
						if (parent == null || (parent == treeRoots[0] && !showRoots)) {
							viewer.refresh();
						} else {
							viewer.refresh(parent);
						}
					}
				}
			};
			visibilityAttr.addMapChangeListener(mapChangeListener);
		}
	}

	/**
	 * Prevent disabled items from being selected. This listener is executed
	 * before the SelectionTypeEnforcer.
	 */
	private void preventDisabledItemSelection(final IObservableMap enablementAttr) {
		if (enablementAttr != null) {
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {
				/* Holds the last selection. */
				private List<Object> lastSel;

				@SuppressWarnings("unchecked")
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					List<Object> newSel = new ArrayList<Object>(selection.toList());
					boolean changed = false;
					for (Object element : selection.toArray()) {
						Object isEnabled = enablementAttr.get(element);
						if (Boolean.FALSE.equals(isEnabled)) {
							newSel.remove(element);
							changed = true;
						}
					}
					if (changed) {
						/*
						 * If the current selection is empty after rejecting
						 * disabled elements, restore the last selection.
						 */
						if (newSel.isEmpty() && lastSel != null) {
							viewer.setSelection(new StructuredSelection(lastSel));
							setSelection(lastSel);
						} else {
							viewer.setSelection(new StructuredSelection(newSel));
							setSelection(newSel);
							lastSel = newSel;
						}
					} else {
						lastSel = newSel;
					}
				}
			});
		}
	}

	/**
	 * Expand tree paths to candidates before selecting them. This ensures the
	 * tree items to the candidates are created and the candidates become
	 * "known elements" (if they exist).
	 */
	private void reveal(Object[] candidates) {
		if (viewer != null) {
			Control control = viewer.getControl();
			control.setRedraw(false);
			try {
				for (Object candidate : candidates) {
					viewer.expandToLevel(candidate, 0);
				}
			} finally {
				control.setRedraw(true);
			}
		}
	}

	/**
	 * Take a snapshot of the selection in the tree widget.
	 */
	private synchronized void saveSelection() {
		if (viewer != null && isOutputOnly()) {
			// only save selection when in 'output only' mode
			savedSelection = viewer.getTree().getSelection();
		} else {
			savedSelection = new TreeItem[0];
		}
	}

	/**
	 * Resets the selection in the tree widget to the last saved selection.
	 */
	private synchronized void restoreSelection() {
		if (viewer != null) {
			Tree control = viewer.getTree();
			control.deselectAll();
			for (TreeItem item : savedSelection) {
				// use select to avoid scrolling the tree
				control.select(item);
			}
		}
	}

	/**
	 * Updates the expand / collapse state of the viewers model, based on a FIFO
	 * queue of {@link ExpansionCommand}s.
	 */
	private void updateExpansionState() {
		if (viewer != null) {
			viewer.getControl().setRedraw(false);
			try {
				while (!expansionStack.isEmpty()) {
					ExpansionCommand cmd = expansionStack.remove();
					ExpansionState state = cmd.state;
					if (state == ExpansionState.FULLY_COLLAPSE) {
						Object[] expanded = viewer.getExpandedElements();
						viewer.collapseAll();
						for (Object wasExpanded : expanded) {
							viewer.update(wasExpanded, null); // update icon
						}
					} else if (state == ExpansionState.FULLY_EXPAND) {
						viewer.expandAll();
						viewer.refresh(); // update all icons
					} else if (state == ExpansionState.COLLAPSE) {
						viewer.collapseToLevel(cmd.element, 1);
						viewer.update(cmd.element, null); // update icon
					} else if (state == ExpansionState.EXPAND) {
						viewer.expandToLevel(cmd.element, 1);
						viewer.update(cmd.element, null); // update icon
					} else if (state == ExpansionState.RESTORE) {
						Object[] elements = (Object[]) cmd.element;
						viewer.setExpandedElements(elements);
					} else {
						String errorMsg = "unknown expansion state: " + state; //$NON-NLS-1$
						throw new IllegalStateException(errorMsg);
					}
				}
			} finally {
				viewer.getControl().setRedraw(true);
			}
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Enumeration with the expansion states of this ridget.
	 */
	private enum ExpansionState {
		FULLY_COLLAPSE, FULLY_EXPAND, COLLAPSE, EXPAND, RESTORE
	}

	/**
	 * An operation that modifies the expansion state of the tree ridget.
	 */
	private static final class ExpansionCommand {
		/** An expansion modification */
		private final ExpansionState state;
		/** The element to expand / collapse (only for COLLAPSE, EXPAND ops) */
		private final Object element;

		/**
		 * Creates a new ExpansionCommand instance.
		 * 
		 * @param state
		 *            an expansion modification
		 * @param element
		 *            the element to expand / collapse (null for FULLY_EXPAND /
		 *            FULLY_COLLAPSE)
		 */
		ExpansionCommand(ExpansionState state, Object element) {
			this.state = state;
			this.element = element;
		}
	}

	/**
	 * Disallows multiple selection is the selection type of the ridget is
	 * {@link ISelectableRidget.SelectionType#SINGLE}.
	 */
	private final class SelectionTypeEnforcer extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Tree control = (Tree) e.widget;
			if (isOutputOnly()) {
				// ignore this event
				e.doit = false;
				restoreSelection();
			} else if (SelectionType.SINGLE.equals(getSelectionType())) {
				if (control.getSelectionCount() > 1) {
					// ignore this event
					e.doit = false;
					// set selection one item
					TreeItem firstItem = control.getSelection()[0];
					control.setSelection(firstItem);
					// fire event
					Event event = new Event();
					event.type = SWT.Selection;
					event.doit = true;
					control.notifyListeners(SWT.Selection, event);
				}
			}
		}
	}

	/**
	 * Notifies doubleClickListeners when the bound widget is double clicked.
	 */
	private final class DoubleClickForwarder extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (doubleClickListeners != null) {
				for (IActionListener listener : doubleClickListeners.getListeners()) {
					listener.callback();
				}
			}
		}
	}

	/**
	 * Erase listener to paint all cells empty when this ridget is disabled.
	 * <p>
	 * Implementation note: this works by registering this class an an
	 * EraseEListener and indicating we will be repsonsible from drawing the
	 * cells content. We do not register a PaintListener, meaning that we do NOT
	 * paint anything.
	 * 
	 * @see '<a href="http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html"
	 *      >Custom Drawing Table and Tree Items</a>'
	 */
	private static final class EraseAndPaintListener implements Listener {

		private Rectangle bounds = new Rectangle(0, 0, 0, 0);

		/*
		 * Called EXTREMELY frequently. Must be as efficient as possible.
		 */
		public void handleEvent(Event event) {
			if (SWT.EraseItem == event.type) {
				// indicate we are responsible for drawing the cell's content
				event.detail &= ~SWT.FOREGROUND;
			} else if (SWT.PaintItem == event.type) {
				TreeItem item = (TreeItem) event.item;
				Tree tree = item.getParent();
				if (!tree.isEnabled()) {
					GC gc = event.gc;
					bounds.width = tree.getBounds().width;
					bounds.height = tree.getBounds().height;
					gc.fillRectangle(bounds);
				}
			}
		}
	}

	/**
	 * This class is used as the tree viewer's input when showRoots is false.
	 * <p>
	 * It uses reflection to obtain the current list of children from the real
	 * root of the model, while keeping the input element (i.e. this instance)
	 * int the tree all the time. This workaround allows us to update the
	 * level-0 of the tree without having to call setInput(...) on the tree
	 * viewer:
	 * 
	 * <pre>
	 * FakeRoot fakeRoot;
	 * viewer.setInput(...);
	 * // ... later ...
	 * fakeRoot.refresh();
	 * viewer.refresh(fakeRoot);
	 * </pre>
	 * 
	 * It uses reflection to obtain a n update list of children from the real
	 * root of the model.
	 * 
	 * @see TreeRidget#bindToModel(Object[], Class, String, String, String)
	 * @see TreeContentProvider
	 */
	static final class FakeRoot extends ArrayList<Object> {
		private static final long serialVersionUID = 1L;
		private final String accessor;
		private final Object root0;

		FakeRoot(Object root0, String childrenAccessor) {
			Assert.isNotNull(root0);
			Assert.isNotNull(childrenAccessor);
			this.root0 = root0;
			this.accessor = "get" + capitalize(childrenAccessor); //$NON-NLS-1$
			clear();
			addAll(ReflectionUtils.<List<Object>> invoke(root0, accessor));
		}

		Object getRoot() {
			return root0;
		}

		private String capitalize(String name) {
			String result = name.substring(0, 1).toUpperCase();
			if (name.length() > 1) {
				result += name.substring(1);
			}
			return result;
		}
	}

	/**
	 * Advisor class for the Eclipse 3.4 tree databinding framework. See {link
	 * TreeStructureAdvisor}.
	 * <p>
	 * This advisor uses the supplied property name and elementClass to invoke
	 * an appropriate accessor (get/isXXX method) on a element in the tree.
	 * <p>
	 * This functionality is used by the databinding framework to perform expand
	 * operations.
	 * 
	 * @see TreeStructureAdvisor
	 */
	private static final class GenericTreeStructureAdvisor extends TreeStructureAdvisor {

		private static final Object[] EMPTY_ARRAY = new Object[0];

		private final Class<?> beanClass;
		private PropertyDescriptor descriptor;

		GenericTreeStructureAdvisor(String propertyName, Class<?> elementClass) {
			Assert.isNotNull(propertyName);
			String errorMsg = "propertyName cannot be empty"; //$NON-NLS-1$
			Assert.isLegal(propertyName.trim().length() > 0, errorMsg);
			Assert.isNotNull(elementClass);

			String readMethodName = "get" + capitalize(propertyName); //$NON-NLS-1$
			try {
				descriptor = new PropertyDescriptor(propertyName, elementClass, readMethodName, null);
			} catch (IntrospectionException exc) {
				log("Could not introspect bean.", exc); //$NON-NLS-1$
				descriptor = null;
			}
			this.beanClass = elementClass;
		}

		@Override
		public Object getParent(Object element) {
			Object result = null;
			if (element != null && beanClass.isAssignableFrom(element.getClass()) && descriptor != null) {
				Method readMethod = descriptor.getReadMethod();
				if (!readMethod.isAccessible()) {
					readMethod.setAccessible(true);
				}
				try {
					result = readMethod.invoke(element, EMPTY_ARRAY);
				} catch (InvocationTargetException exc) {
					log("Error invoking.", exc); //$NON-NLS-1$
				} catch (IllegalAccessException exc) {
					log("Error invoking.", exc); //$NON-NLS-1$
				}
			}
			return result;
		}

		private String capitalize(String name) {
			String result = name.substring(0, 1).toUpperCase();
			if (name.length() > 1) {
				result += name.substring(1);
			}
			return result;
		}

		private void log(String message, Exception exc) {
			Logger logger = Log4r.getLogger(Activator.getDefault(), TreeRidget.class);
			logger.log(LogService.LOG_ERROR, message, exc);
		}
	}

	/**
	 * This change listener reacts to additions / removals of objects from the
	 * tree and is responsible for updating the image of the <b>parent</b>
	 * element. Specifically:
	 * <ul>
	 * <li>if B gets added to A we have to refresh the icon of A, if A did not
	 * have any children beforehand</li>
	 * <li>if B gets removed to A we have to refresh the icon of A, if B was the
	 * last child underneath A</li>
	 * <ul>
	 */
	private static final class TreeContentChangeListener implements ISetChangeListener {

		private final TreeViewer viewer;
		private final TreeStructureAdvisor structureAdvisor;

		private TreeContentChangeListener(TreeViewer viewer, TreeStructureAdvisor structureAdvisor) {
			Assert.isNotNull(structureAdvisor);
			this.structureAdvisor = structureAdvisor;
			this.viewer = viewer;
			Assert.isNotNull(viewer.getContentProvider());
		}

		/**
		 * Updates the icons of the parent elements on addition / removal
		 */
		public void handleSetChange(SetChangeEvent event) {
			if (viewer.getLabelProvider(0) == null) {
				return;
			}
			Set<Object> parents = new HashSet<Object>();
			for (Object element : event.diff.getAdditions()) {
				Object parent = structureAdvisor.getParent(element);
				if (parent != null) {
					parents.add(parent);
				}
			}
			for (Object element : event.diff.getRemovals()) {
				Object parent = structureAdvisor.getParent(element);
				if (parent != null) {
					parents.add(parent);
				}
			}
			for (Object parent : parents) {
				if (!viewer.isBusy()) {
					viewer.update(parent, null);
				}
			}
		}
	}

}
