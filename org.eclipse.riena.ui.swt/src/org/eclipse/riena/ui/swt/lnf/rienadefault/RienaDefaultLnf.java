/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *    Florian Pirchner - FontDescriptor
 *******************************************************************************/
package org.eclipse.riena.ui.swt.lnf.rienadefault;

import java.beans.Beans;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Resource;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.FontDescriptor;
import org.eclipse.riena.ui.swt.lnf.ILnfCustomizer;
import org.eclipse.riena.ui.swt.lnf.ILnfMarkerSupportExtension;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfRendererExtension;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;

/**
 * Default Look and Feel of Riena.
 */
public class RienaDefaultLnf implements ILnfCustomizer {

	private static final Logger LOGGER = Log4r.getLogger(RienaDefaultLnf.class);

	private static final String SYSTEM_PROPERTY_LNF_SETTING_PREFIX = "riena.lnf.setting."; //$NON-NLS-1$
	private final Map<String, ILnfResource<?>> resourceTable = new HashMap<String, ILnfResource<?>>();
	private final Set<String> resourcePrefixes = new HashSet<String>();
	private final Map<String, Object> settingTable = new ConcurrentHashMap<String, Object>();
	private final Map<String, ILnfRenderer> rendererTable = new HashMap<String, ILnfRenderer>();
	private final Map<IconSize, String> iconSizeGroupIdentifier = new HashMap<IconSize, String>();
	private ILnfMarkerSupportExtension[] markerSupportList = new ILnfMarkerSupportExtension[0];
	private ILnfTheme theme;
	private boolean initialized;

	/**
	 * Create a look and feel with the given theme.
	 * 
	 * @param theme
	 *            the theme this look and feel will use
	 */
	public RienaDefaultLnf(final ILnfTheme theme) {
		setTheme(theme);
	}

	/**
	 * Create a look and feel with no theme.
	 */
	public RienaDefaultLnf() {
		this(null);
	}

	public ILnfResource<?> getLnfResource(final String key) {
		return resourceTable.get(key);
	}

	public ILnfResource<?> putLnfResource(final String key, final ILnfResource<?> resource) {
		final int dot = key.indexOf('.');
		if (dot != -1) {
			resourcePrefixes.add(key.substring(0, dot));
		}
		return resourceTable.put(key, resource);
	}

	public boolean containsLnfResourcePrefix(final String prefix) {
		return resourcePrefixes.contains(prefix);
	}

	public Object getLnfSetting(final String key) {
		return settingTable.get(key);
	}

	public Object putLnfSetting(final String key, final Object setting) {
		return settingTable.put(key, setting);
	}

	/**
	 * Returns the String value of the iconsize group identifier for the given Iconsize.
	 * 
	 * @param iconSize
	 *            the desired iconSize
	 * @return the Group identifier of the mapped iconsize, returns the given iconsize.defaultMapping if no group identifier exists.
	 * @since 6.2
	 */
	public String getIconSizeGroupIdentifier(final IconSize iconSize) {
		Assert.isNotNull(iconSize, "iconSize must not be null"); //$NON-NLS-1$
		final String value = iconSizeGroupIdentifier.get(iconSize);
		if (value != null) {
			return value;
		}
		return iconSize.getDefaultMapping();
	}

	/**
	 * @since 6.2
	 */
	public String putIconSizeGroupIdentifier(final IconSize iconSize, final String group) {
		return iconSizeGroupIdentifier.put(iconSize, group);
	}

	/**
	 * Initializes the Look and Feel. Fills the tables of resources and renderers.
	 */
	public void initialize() {
		if (!isInitialized()) {
			uninitialize();
			setInitialized(true);
			initializeTheme();
			readSystemProperties();
		}
	}

	/**
	 * Reads system properties to overwrite Look&Feel settings.
	 */
	private void readSystemProperties() {

		final Properties sysProps = (Properties) System.getProperties().clone();
		final Set<Object> sysPropKeys = sysProps.keySet();
		for (final Object propKey : sysPropKeys) {
			final String propKeyName = propKey.toString();
			if (propKeyName.startsWith(SYSTEM_PROPERTY_LNF_SETTING_PREFIX)) {
				final String lnfKey = propKeyName.substring(SYSTEM_PROPERTY_LNF_SETTING_PREFIX.length());
				final Object lnfValue = sysProps.get(propKeyName);
				putLnfSetting(lnfKey, lnfValue);
			}
		}

	}

	/**
	 * Uninitializes the Look and Feel. Disposes resources and clears the tables of resources and renderers.
	 */
	public void uninitialize() {
		resourceTable.clear();
		resourcePrefixes.clear();
		settingTable.clear();
		setInitialized(false);
	}

	/**
	 * Puts the given renderers into the table of renderer.
	 * 
	 * @param rendererExtensions
	 *            descriptors of renderer
	 * @since 1.2
	 */
	@InjectExtension
	public void update(final ILnfRendererExtension[] rendererExtensions) {
		rendererTable.clear();
		for (final ILnfRendererExtension rendererExtension : rendererExtensions) {
			final String id = rendererExtension.getLnfId();
			if (StringUtils.isEmpty(id) || id.equals(getLnfId())) {
				if (StringUtils.isEmpty(id)) {
					if (rendererTable.get(rendererExtension.getLnfKey()) != null) {
						continue;
					}
				}
				rendererTable.put(rendererExtension.getLnfKey(), rendererExtension.createRenderer());
			}
		}
	}

	/**
	 * Stores the given marker supports in a list.
	 * 
	 * @param markerSupportExtensions
	 *            array of marker supports
	 * @since 2.0
	 */
	@InjectExtension
	public void update(final ILnfMarkerSupportExtension[] markerSupportExtensions) {
		markerSupportList = markerSupportExtensions;
	}

	/**
	 * Returns the marker support that will be used according to the Look&Feel setting ({@code LnfKeyConstants.MARKER_SUPPORT_ID}) and for the given Ridget
	 * class.
	 * 
	 * @param ridgetClass
	 *            class of the Ridget
	 * @return marker support or {@code null} if no appropriate marker support was found
	 * @since 2.0
	 */
	public AbstractMarkerSupport getMarkerSupport(final Class<? extends IRidget> ridgetClass) {
		final String markerSupportID = getStringSetting(LnfKeyConstants.MARKER_SUPPORT_ID);

		for (final ILnfMarkerSupportExtension lnfMarkerSupportExtension : markerSupportList) {
			if (StringUtils.isEmpty(lnfMarkerSupportExtension.getId())) {
				continue;
			}
			if (lnfMarkerSupportExtension.getId().equals(markerSupportID)) {
				return lnfMarkerSupportExtension.createMarkerSupport();
			}
		}

		// only log this when running in an bundle environment
		if (Platform.getBundle("org.eclipse.swt") != null) { //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "No MarkerSupport with the ID \"" + markerSupportID + "\" exists."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return null;
	}

	/**
	 * Initializes the theme.
	 */
	protected void initializeTheme() {
		putLnfResource("black", new ColorLnfResource(0, 0, 0)); //$NON-NLS-1$
		putLnfResource("white", new ColorLnfResource(255, 255, 255)); //$NON-NLS-1$

		if (getTheme() != null) {
			getTheme().customizeLnf(this);
		}
	}

	/**
	 * Returns the resource for the given key.
	 * 
	 * @param key
	 *            key whose associated resource is to be returned.
	 * @return the resource to which this map maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Resource getResource(final String key) {
		final ILnfResource<?> value = resourceTable.get(key);
		if (value != null) {
			return value.getResource();
		} else {
			return null;
		}
	}

	/**
	 * Returns the color for the given key.
	 * 
	 * @param key
	 *            key whose associated color is to be returned.
	 * @return the color to which this map maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Color getColor(final String key) {
		return getColor(key, null);
	}

	/**
	 * Returns the color for the given key.
	 * 
	 * @param key
	 *            key whose associated color is to be returned.
	 * @return the color to which this map maps the specified key, or the {@code defaultValue} if the map contains no mapping for this key.
	 * 
	 * @since 2.0
	 */
	public Color getColor(final String key, final Color defaultValue) {
		final Resource value = getResource(key);
		if (value instanceof Color) {
			return (Color) value;
		}
		return defaultValue;
	}

	/**
	 * Returns the font for the given key.
	 * 
	 * @param key
	 *            key whose associated font is to be returned.
	 * @return the font to which this map maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Font getFont(final String key) {
		final Resource value = getResource(key);
		if (value instanceof Font) {
			return (Font) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the font for the given key. The passed properties height and style will be applied to the font. <br>
	 * 
	 * @param key
	 *            key whose associated font is to be returned.
	 * @param height
	 *            the font height to use. If it is < 0, the <code>LnfKeyConstants.FONTDESCRIPTOR_DEFAULT_HEIGHT</code> will be used. See also
	 *            {@link FontData#setHeight(int)}.
	 * @param style
	 *            the font style to use. See also {@link FontData#setStyle(int)} .
	 * 
	 * @return the font to which this map maps the specified key with differing height and style, or <code>null</code> if the map contains no mapping for this
	 *         lnfKeyConstants key.
	 * @since 1.2
	 */
	public Font getFont(final String key, final int height, final int style) {
		final FontDescriptor fontDescriptor = new FontDescriptor(key, height, style, this);
		return fontDescriptor.getFont();
	}

	/**
	 * Returns the image for the given key.
	 * 
	 * @param key
	 *            key whose associated image is to be returned.
	 * @return the image to which this map maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Image getImage(final String key) {
		final Resource value = getResource(key);
		if (value instanceof Image) {
			return (Image) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the renderer for the given key.
	 * 
	 * @param key
	 *            key whose associated renderer is to be returned.
	 * @return the renderer to which this renderer maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public ILnfRenderer getRenderer(final String key) {
		return rendererTable.get(key);
	}

	/**
	 * Returns the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Object getSetting(final String key) {
		return settingTable.get(key);
	}

	/**
	 * Returns the integer value of the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public Integer getIntegerSetting(final String key) {
		final Object value = getSetting(key);
		if (value == null) {
			if (Beans.isDesignTime()) { // in case of design time we return a "0" so that dependent code does not fail
				return new Integer(0);
			} else {
				return null;
			}
		}
		if (value instanceof Integer) {
			return (Integer) value;
		} else {
			final String strgValue = value.toString();
			try {
				return new Integer(strgValue);
			} catch (final NumberFormatException e) {
				if (Beans.isDesignTime()) { // in case of design time we return a "0" so that dependent code does not fail
					return Integer.valueOf(0);
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * Returns the integer value of the setting for the given key. If no value is set, the given default value is returned.
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @param defaultValue
	 *            value to return, if no value is set
	 * @return the setting to which this setting maps the specified key, or the default value if the map contains no mapping for this key.
	 * @since 1.2
	 */
	public Integer getIntegerSetting(final String key, final Integer defaultValue) {
		Integer value = getIntegerSetting(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Returns the boolean value of the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or <code>false</code> if the map contains no mapping for this key.
	 */
	public Boolean getBooleanSetting(final String key) {
		final Object value = getSetting(key);
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else {
			return Boolean.valueOf(value.toString());
		}
	}

	/**
	 * Returns the boolean value of the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @param defaultValue
	 *            value to return, if no value is set
	 * @return the setting to which this setting maps the specified key, or the {@code defaultValue} if the map contains no mapping for this key.
	 * @since 1.2
	 */
	public Boolean getBooleanSetting(final String key, final boolean defaultValue) {
		Boolean value = getBooleanSetting(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Returns the String value of the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public String getStringSetting(final String key) {
		final Object value = getSetting(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the String value of the setting for the given key
	 * 
	 * @param key
	 *            key whose associated setting is to be returned.
	 * @param defaultValue
	 *            value to return, if no value is set
	 * @return the setting to which this setting maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public String getStringSetting(final String key, final String defaultValue) {
		final Object value = getSetting(key);
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof String) {
			return (String) value;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns the current theme of the Look and Feel.
	 * 
	 * @return theme
	 */
	public ILnfTheme getTheme() {
		if (theme == null) {
			theme = new RienaDefaultTheme();
			initialize();
		}
		return theme;
	}

	/**
	 * Sets the theme to be used by the Look and Feel.
	 * <p>
	 * <b>Note:</b> Setting (changing) a new theme might result in system resources such as colors, fonts and images which will not be disposed.
	 * 
	 * @param newTheme
	 *            the theme to be used
	 */
	public void setTheme(final ILnfTheme newTheme) {
		if (theme != newTheme) {
			theme = newTheme;
			setInitialized(false);
			initialize();
		}
	}

	/**
	 * @return the initialize
	 */
	private boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialize
	 *            the initialize to set
	 */
	private void setInitialized(final boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * The ID of this Look and Feel.<br>
	 * The ID of the default LnF is empty.
	 * 
	 * @return look'n'feel ID
	 */
	protected String getLnfId() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation always returns zero factors. So the default, the calculated factors are used.
	 * </p>
	 * 
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfCustomizer#getDpiFactors(org.eclipse.swt.graphics.Point)
	 * @since 6.0
	 */
	public float[] getDpiFactors(final Point dpi) {
		return new float[] { 0.0f, 0.0f };
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfCustomizer#getIconScaleSuffix(float[])
	 * @since 6.0
	 */
	public String getIconScaleSuffix(final Point dpi) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfCustomizer#useDpiGridLayout()
	 * 
	 * @since 6.1
	 */
	public boolean useDpiGridLayout() {
		return true;
	}

}
