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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyDescriptor;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Label provider that formats the columns of a {@link TreeRidget} or
 * {@link TreeTableRidget}. {@link IColumnFormatter}s can be used to modify the
 * text, image, foreground color, background color or font of a particular
 * column.
 * <p>
 * The appropriate image for a column is computed in the following fashion:
 * <p>
 * For the tree image (TreeRidget and column 0 in the TreeTableRidget):
 * <ul>
 * <li>if the column has a formatter, use the image from the formatter, if not
 * null</li>
 * <li>if image accessor properties are specified, use the image returned by the
 * property, if not null</li>
 * <li>if image accessor properties for leaves are specified, use the image
 * returned by the property, if not null</li>
 * <li>otherwise no image is shown</li>
 * </ul>
 * <p>
 * For columns 1-n (TreeTableRidget):
 * <ul>
 * <li>if the column has a formatter, use the image from the formatter, if not
 * null</li>
 * <li>if the column has a boolean or Boolean value, use the default image for
 * boolean values (i.e. checked / unchecked box)</li>
 * <li>otherwise no image is shown</li>
 * </ul>
 * <p>
 * The appropriate foreground color for a column is computed in the following
 * fashion:
 * <p>
 * For column 0 (TreeRidget):
 * <ul>
 * <li>if an enablement accessor property is specified and the corresponding
 * tree node is disabled, use a gray color</li>
 * <li>otherwise use the widget's foreground color</li>
 * </ul>
 * <p>
 * For columns 0-n (TreeTableRidget):
 * <ul>
 * <li>if the column has a formatter, use the foreground color from the
 * formatter, if not null</li>
 * <li>otherwise use the widget's foreground</li>
 * </ul>
 */
public final class TreeRidgetLabelProvider extends TableRidgetLabelProvider implements IColorProvider {

	private static final Logger LOGGER = Log4r.getLogger(TreeRidgetLabelProvider.class);
	private static final UpdateIconsTreeListener LISTENER = new UpdateIconsTreeListener();
	private static final String KEY_LABELPROVIDER = "K_TRLP"; //$NON-NLS-1$

	public static final String TREE_KIND_KEY = "kind"; //$NON-NLS-1$
	public static final String TREE_KIND_NAVIGATION = "navigation"; //$NON-NLS-1$

	private final TreeViewer viewer;
	private final IObservableMap enablementAttribute;
	private final IObservableMap imageAttribute;
	private final IObservableMap openImageAttribute;
	private final String[] valueAccessors;

	/**
	 * Creates a new instance.
	 * 
	 * @param viewer
	 *            a non-null {@link TreeViewer} instance
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. for treeRoot and
	 *            all children).
	 * @param knownElements
	 *            a non-null set of observable elements. The label provider may
	 *            track this set to update the tree as necessary - see
	 *            {@link ObservableListTreeContentProvider#getKnownElements()}
	 * @param valueAccessors
	 *            a non-null; non-empty array of Strings. Each String specifies
	 *            an accessor for obtaining an Object value from each child
	 *            object (example "value" specifies "getValue()"). The order in
	 *            the array corresponds to the initial order of the columns,
	 *            i.e. the 1st accessor will be used for column one/the tree,
	 *            the 2nd for column two, the 3rd for column three and so on
	 * @param enablementAccessor
	 *            a String specifying an accessor for obtaining a boolean value
	 *            from each child. The returned value will determine the
	 *            enabled/disabled state of this child. Example: 'enabled'
	 *            specifies "isEnabled()" or "getEnabled()". The parameter can
	 *            be {@code null} to enable all children
	 * @param imageAccessor
	 *            a String specifying an accessor for obtaining a String value,
	 *            which is the key (or filename) of an icon. (example "icon"
	 *            specifies "getIcon()). This key will be used to obtain an icon
	 *            for <b>leaves AND closed nodes</b> of the tree. The
	 *            leafImageAccessor can be null; in that case the default icon
	 *            is used for all leaves and nodes. Note: nodes will only get a
	 *            custom icon if an openNodeImageAccessor is supplied as well
	 *            (see below).
	 * @param openNodeImageAccessor
	 *            a String specifying an accessor for obtaining a String value
	 *            which is the key (or filename) of an icon. (example "icon"
	 *            specifies "getIcon()" ). This key will be used to obtain an
	 *            icon for <b>open nodes</b> of the tree. The
	 *            openNodeImageAccessor can be null; in that case the default
	 *            icon is used for all nodes. Note: nodes will only get a custom
	 *            icon if an imageAccessor is supplied as well (see above).
	 * @param formatters
	 *            an array of IColumnFormatters; one for each column. Individual
	 *            array entries may be null, in that case no formatter will be
	 *            used for that column.
	 */
	public static TreeRidgetLabelProvider createLabelProvider(final TreeViewer viewer, final Class<?> treeElementClass,
			final IObservableSet knownElements, final String[] valueAccessors, final String enablementAccessor,
			final String imageAccessor, final String openNodeImageAccessor, final IColumnFormatter[] formatters) {
		final IObservableMap[] map = createAttributeMap(treeElementClass, knownElements, valueAccessors,
				enablementAccessor, imageAccessor, openNodeImageAccessor);
		final int numColumns = valueAccessors.length;
		return new TreeRidgetLabelProvider(viewer, map, valueAccessors, enablementAccessor, imageAccessor,
				openNodeImageAccessor, formatters, numColumns);
	}

	/**
	 * Create an array of attributes that this label provides will observe. If
	 * observing a bean, and the observed attributes change the label provider
	 * will update the appropriate element.
	 */
	private static IObservableMap[] createAttributeMap(final Class<?> treeElementClass,
			final IObservableSet knownElements, final String[] valueAccessors, final String enablementAccessor,
			final String imageAccessor, final String openNodeImageAccessor) {
		IObservableMap[] result;
		final String[] attributes = computeAttributes(valueAccessors, enablementAccessor, imageAccessor,
				openNodeImageAccessor);
		if (AbstractSWTWidgetRidget.isBean(treeElementClass)) {
			result = BeansObservables.observeMaps(knownElements, treeElementClass, attributes);
		} else {
			result = PojoObservables.observeMaps(knownElements, treeElementClass, attributes);
		}
		return result;
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		//FIXME the current attributeMap does not hold the element and cannot provide a value 
		final String columnText = super.getColumnText(element, columnIndex);
		if (!StringUtils.isGiven(columnText)) {
			if (columnIndex <= valueAccessors.length - 1) {
				final String str = valueAccessors[columnIndex];
				if (str.length() > 1) {
					String s = str.substring(0, 1).toUpperCase();
					s = s + str.substring(1);
					try {
						return String.valueOf(ReflectionUtils.invoke(element, "get" + s)); //$NON-NLS-1$
					} catch (final RuntimeException ex) {
						LOGGER.log(LogService.LOG_WARNING,
								"Unexpected error when accessing property " + str + " in " + element, ex); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
		return columnText;
	}

	private static String[] computeAttributes(final String[] valueAccessors, final String enablementAccessor,
			final String imageAccessor, final String openNodeImageAccessor) {
		int length = valueAccessors.length;
		if (enablementAccessor != null) {
			length++;
		}
		if (imageAccessor != null) {
			length++;
		}
		if (openNodeImageAccessor != null) {
			length++;
		}
		final String[] attributes = new String[Math.max(length, valueAccessors.length)];
		System.arraycopy(valueAccessors, 0, attributes, 0, valueAccessors.length);
		if (length > valueAccessors.length) {
			int index = valueAccessors.length;
			if (enablementAccessor != null) {
				// add the enablement attribute to the list of observed attributes for the label provider
				attributes[index++] = enablementAccessor;
			}
			if (imageAccessor != null) {
				// add the image accessor to the list of observed attributes for the label provider
				attributes[index++] = imageAccessor;
			}
			if (openNodeImageAccessor != null) {
				// add the open node image accessor to the list of observed attributes for the label provider
				attributes[index++] = openNodeImageAccessor;
			}
		}
		return attributes;
	}

	private TreeRidgetLabelProvider(final TreeViewer viewer, final IObservableMap[] attributeMap,
			final String[] valueAccessors, final String enablementAccessor, final String imageAccessor,
			final String openNodeImageAccessor, final IColumnFormatter[] formatters, final int numColumns) {
		super(attributeMap, formatters, numColumns);
		this.valueAccessors = valueAccessors;
		final Tree tree = viewer.getTree();
		tree.removeTreeListener(LISTENER);
		tree.addTreeListener(LISTENER);
		tree.setData(KEY_LABELPROVIDER, this);
		enablementAttribute = findAttribute(attributeMap, enablementAccessor);
		imageAttribute = findAttribute(attributeMap, imageAccessor);
		openImageAttribute = findAttribute(attributeMap, openNodeImageAccessor);
		this.viewer = viewer;
	}

	@Override
	public Image getImage(final Object element) {
		// TODO [ev] this fails when run without osgi - see Bug 299267
		//		boolean isNode = viewer.isExpandable(element);
		//		String result = null;
		//		if (isNode) {
		//			boolean isExpanded = viewer.getExpandedState(element);
		//			return getImageForNode(element, isExpanded);
		//		}
		final String key = getImageKey(element);
		return Activator.getSharedImage(key);
	}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		Image result = null;
		if (columnIndex == 0) {
			// tree column 0 is special, because it contains the node & leaf icons
			// a. use the icon from formatter, if present
			// b. if formatter returns null (=don't care) or no formatter present,
			//    use the standard node / leaf icons
			// c. no automatic checkbox (=boolean) icons for column 0
			final IColumnFormatter formatter = getFormatter(columnIndex);
			if (formatter != null) {
				result = (Image) formatter.getImage(element);
			}
			if (result == null) {
				result = getImage(element);
			}
		} else {
			// other columns: 
			// a. use icon from formatter, if present
			// b. use automatic checkbox icons for boolean values
			result = super.getColumnImage(element, columnIndex);
		}
		return result;
	}

	// IColorProvider methods
	/////////////////////////

	public Color getBackground(final Object element) {
		return null;
	}

	public Color getForeground(final Object element) {
		Color result = null;
		if (enablementAttribute != null) {
			final Object value = enablementAttribute.get(element);
			if (Boolean.FALSE.equals(value)) {
				result = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_GRAY);
			}
		}
		return result;
	}

	// helping methods
	// ////////////////

	private IObservableMap findAttribute(final IObservableMap[] attributeMap, final String accessor) {
		IObservableMap result = null;
		if (accessor != null) {
			for (int i = attributeMap.length - 1; result == null && i > -1; i--) {
				final IObservableMap attribute = attributeMap[i];
				final IBeanObservable beanObservable = (IBeanObservable) attribute;
				final PropertyDescriptor pd = beanObservable.getPropertyDescriptor();
				final String property = pd != null ? pd.getName() : null;
				if (accessor.equals(property)) {
					result = attribute;
				}
			}
		}
		return result;
	}

	/**
	 * Returns the image key for the given element. If the element is a folder,
	 * the image key for an closed or open folder will be returned. If the
	 * element is a leaf and the element has its own image key, the image key of
	 * the element will be returned; otherwise the default image key of a leaf
	 * will be returned.
	 * 
	 * @param element
	 * @return image key
	 */
	private String getImageKey(final Object element) {
		final boolean isNode = viewer.isExpandable(element);
		String result = null;
		if (isNode) {
			final boolean isExpanded = viewer.getExpandedState(element);
			result = getImageKeyForNode(element, isExpanded);
		} else {
			result = getImageKeyForLeaf(element, result);
		}
		return result;
	}

	private String getImageKeyForLeaf(final Object element, String result) {
		if (imageAttribute != null) {
			result = (String) imageAttribute.get(element);
		}
		if ((result == null) && (isSubModuleNode(element.getClass()))) {
			ILnfResource<? extends Resource> lnfResource = null;
			final Tree tree = (Tree) viewer.getControl();
			final boolean navigation = TREE_KIND_NAVIGATION.equals(tree.getData(TREE_KIND_KEY));
			lnfResource = navigation ? LnfManager.getLnf().getLnfResource(
					LnfKeyConstants.SUB_MODULE_TREE_DOCUMENT_LEAF_ICON) : LnfManager.getLnf().getLnfResource(
					LnfKeyConstants.WORKAREA_TREE_DOCUMENT_LEAF_ICON);
			if (lnfResource instanceof ImageLnfResource) {
				final ImageLnfResource imageResource = (ImageLnfResource) lnfResource;
				result = imageResource.getImagePath();
			}
		}
		if (result == null || Activator.getSharedImage(result) == null) {
			result = SharedImages.IMG_LEAF;
		}
		return result;
	}

	private String getImageKeyForNode(final Object element, final boolean isExpanded) {
		String result = null;
		if (imageAttribute != null && openImageAttribute != null) {
			result = isExpanded ? (String) openImageAttribute.get(element) : (String) imageAttribute.get(element);
		}
		if ((result == null) && (isSubModuleNode(element.getClass()))) {
			ILnfResource<?> lnfResource = null;
			final Tree tree = (Tree) viewer.getControl();

			final boolean navigation = TREE_KIND_NAVIGATION.equals(tree.getData(TREE_KIND_KEY));
			if (isExpanded) {

				lnfResource = navigation ? LnfManager.getLnf().getLnfResource(
						LnfKeyConstants.SUB_MODULE_TREE_FOLDER_OPEN_ICON) : LnfManager.getLnf().getLnfResource(
						LnfKeyConstants.WORKAREA_TREE_FOLDER_OPEN_ICON);
			} else {
				lnfResource = navigation ? LnfManager.getLnf().getLnfResource(
						LnfKeyConstants.SUB_MODULE_TREE_FOLDER_CLOSED_ICON) : LnfManager.getLnf().getLnfResource(
						LnfKeyConstants.WORKAREA_TREE_FOLDER_CLOSED_ICON);
			}
			if (lnfResource instanceof ImageLnfResource) {
				final ImageLnfResource imageResource = (ImageLnfResource) lnfResource;
				result = imageResource.getImagePath();
			}
		}
		if (result == null || Activator.getSharedImage(result) == null) {
			result = isExpanded ? SharedImages.IMG_NODE_EXPANDED : SharedImages.IMG_NODE_COLLAPSED;
		}
		return result;
	}

	private boolean isSubModuleNode(final Class<?> elementClass) {
		final Class<?>[] interfaces = elementClass.getInterfaces();
		for (final Class<?> type : interfaces) {
			if (type.getName().equals("org.eclipse.riena.navigation.ISubModuleNode")) { //$NON-NLS-1$
				return true;
			}
		}
		if (elementClass.getSuperclass() != null) {
			return isSubModuleNode(elementClass.getSuperclass());
		}
		return false;
	}

	private void updateNodeImage(final TreeItem item, final boolean isExpanded) {
		final Object element = item.getData();
		Image image = null;
		if (getFormatter(0) != null) {
			image = (Image) getFormatter(0).getImage(element);
		}
		if (image == null) {
			final String key = getImageKeyForNode(element, isExpanded);
			image = Activator.getSharedImage(key);
		}
		item.setImage(image);
	}

	// helping classes
	// ////////////////

	/**
	 * This listener is in charge of updating a tree item's icon whenever the
	 * item is collapsed or expanded.
	 */
	private static final class UpdateIconsTreeListener implements TreeListener {

		public void treeCollapsed(final TreeEvent e) {
			// cannot use treeItem.getExpanded() because it has the old value
			updateIcon((TreeItem) e.item, false);
		}

		public void treeExpanded(final TreeEvent e) {
			// cannot use treeItem.getExpanded() because it has the old value
			updateIcon((TreeItem) e.item, true);
		}

		private void updateIcon(final TreeItem item, final boolean isExpanded) {
			final TreeRidgetLabelProvider labelProvider = (TreeRidgetLabelProvider) item.getParent().getData(
					KEY_LABELPROVIDER);
			labelProvider.updateNodeImage(item, isExpanded);
		}
	}

}
