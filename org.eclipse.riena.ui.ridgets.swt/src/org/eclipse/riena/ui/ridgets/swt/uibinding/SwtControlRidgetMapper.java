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
package org.eclipse.riena.ui.ridgets.swt.uibinding;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.BrowserRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.CComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.CompletionComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.CompositeRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.DateTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.DateTimeRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.DecimalTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.EmbeddedTitleBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.InfoFlyoutRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LinkRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ListRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MasterDetailsRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MenuItemRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MenuRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MessageBoxRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ModuleTitleBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MultipleChoiceRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.NumericTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ProgressBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ScaleRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ShellRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.SingleChoiceRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.SliderRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.SpinnerRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatusMeterRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineNumberRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToggleButtonRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToolItemRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeTableRidget;
import org.eclipse.riena.ui.ridgets.ClassRidgetMapper;
import org.eclipse.riena.ui.ridgets.swt.ImageButtonRidget;
import org.eclipse.riena.ui.ridgets.uibinding.AbstractControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.ImageButton;
import org.eclipse.riena.ui.swt.InfoFlyout;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.ModuleTitleBar;
import org.eclipse.riena.ui.swt.StatusMeterWidget;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineNumber;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Default implementation of {@link IControlRidgetMapper} for SWT.
 * <p>
 * Note: for mappings between Interfaces and Ridget implementations see {@link ClassRidgetMapper}
 */
public final class SwtControlRidgetMapper extends AbstractControlRidgetMapper {

	private static final SingletonProvider<SwtControlRidgetMapper> SCRM = new SingletonProvider<SwtControlRidgetMapper>(SwtControlRidgetMapper.class);

	/**
	 * Answer the singleton <code>SwtControlRidgetMapper</code>
	 * 
	 * @return the SwtControlRidgetMapper singleton
	 */
	public static SwtControlRidgetMapper getInstance() {
		return SCRM.getInstance();
	}

	private SwtControlRidgetMapper() {
		initDefaultMappings();
	}

	/**
	 * Sets the default mapping of UI control-classes to a ridget-classes
	 */
	private void initDefaultMappings() {
		addMapping(MenuItem.class, MenuItemRidget.class, new MenuItemCondition());
		addMapping(MenuItem.class, MenuRidget.class, new MenuCondition());
		addMapping(ToolItem.class, ToolItemRidget.class);
		addMapping(Text.class, NumericTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_NUMERIC));
		addMapping(Text.class, DecimalTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_DECIMAL));
		addMapping(Text.class, DateTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_DATE));
		addMapping(DatePickerComposite.class, DateTextRidget.class);
		addMapping(Text.class, TextRidget.class);
		addMapping(Label.class, LabelRidget.class);
		addMapping(Table.class, TableRidget.class);
		addMapping(Browser.class, BrowserRidget.class);
		addMapping(Button.class, ToggleButtonRidget.class, new StyleCondition(SWT.CHECK));
		addMapping(Button.class, ToggleButtonRidget.class, new StyleCondition(SWT.TOGGLE));
		addMapping(Button.class, ToggleButtonRidget.class, new StyleCondition(SWT.RADIO));
		addMapping(ImageButton.class, ImageButtonRidget.class);
		addMapping(Button.class, ActionRidget.class);
		addMapping(ChoiceComposite.class, SingleChoiceRidget.class, new SingleChoiceCondition());
		addMapping(ChoiceComposite.class, MultipleChoiceRidget.class, new MultipleChoiceCondition());
		addMapping(Composite.class, CompositeRidget.class, new CompositeWithBindingIdCondition());
		addMapping(Group.class, CompositeRidget.class, new CompositeWithBindingIdCondition());
		addMapping(CCombo.class, CComboRidget.class);
		addMapping(CompletionCombo.class, CompletionComboRidget.class);
		addMapping(Combo.class, ComboRidget.class);
		addMapping(DateTime.class, DateTimeRidget.class);
		addMapping(org.eclipse.swt.widgets.List.class, ListRidget.class);
		addMapping(Link.class, LinkRidget.class);
		addMapping(Tree.class, TreeRidget.class, new TreeWithoutColumnsCondition());
		addMapping(Tree.class, TreeTableRidget.class, new TreeWithColumnsCondition());
		addMapping(MessageBox.class, MessageBoxRidget.class);
		addMapping(Statusline.class, StatuslineRidget.class);
		addMapping(StatuslineNumber.class, StatuslineNumberRidget.class);
		addMapping(EmbeddedTitleBar.class, EmbeddedTitleBarRidget.class);
		addMapping(ModuleTitleBar.class, ModuleTitleBarRidget.class);
		addMapping(MasterDetailsComposite.class, MasterDetailsRidget.class);
		addMapping(Scale.class, ScaleRidget.class);
		addMapping(Spinner.class, SpinnerRidget.class);
		addMapping(Slider.class, SliderRidget.class);
		addMapping(ProgressBar.class, ProgressBarRidget.class);
		addMapping(InfoFlyout.class, InfoFlyoutRidget.class);
		addMapping(StatusMeterWidget.class, StatusMeterRidget.class);
		addMapping(Shell.class, ShellRidget.class);
	}

}
