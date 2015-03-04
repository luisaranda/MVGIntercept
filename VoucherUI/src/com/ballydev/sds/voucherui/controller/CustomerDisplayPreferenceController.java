/*****************************************************************************
 * $Id: CustomerDisplayPreferenceController.java,v 1.22, 2010-04-08 13:45:07Z, Verma, Nitin Kumar$
 * $Date: 4/8/2010 8:45:07 AM$
 *****************************************************************************
 * Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.control.TSCheckBox;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.CustomerDisplayPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.displays.Display;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.form.CustomerDisplayPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class CustomerDisplayPreferenceController  extends SDSBaseController implements IVoucherConstants,DisposeListener {
	
	private CustomerDisplayPreferenceComposite composite;
	private CustomerDisplayPreferenceForm form;

	private String dispManufacturer;

	private String dispModel;

	private String dispDriver;

	public CustomerDisplayPreferenceController(Composite parent, int style ,CustomerDisplayPreferenceForm form, SDSValidator pValidator) throws Exception 
	{
		super(form, pValidator);
		composite = new CustomerDisplayPreferenceComposite(parent,style);
		this.form = form;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(composite);

		List<ComboLabelValuePair> lstType = new ArrayList<ComboLabelValuePair>();
		lstType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_DIS_TYPE1), IVoucherConstants.VOU_PREF_DIS_TYPE1));		

		List<ComboLabelValuePair> lstPort = new ArrayList<ComboLabelValuePair>();
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM1), IVoucherConstants.VOU_PREF_TKT_PRINT_COM1));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM2),IVoucherConstants.VOU_PREF_TKT_PRINT_COM2));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM3), IVoucherConstants.VOU_PREF_TKT_PRINT_COM3));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM4), IVoucherConstants.VOU_PREF_TKT_PRINT_COM4));

		form.setDisplayTypeList(lstType);
		form.setPortList(lstPort);

		composite.getComboDisplayType().setSelectedIndex(0);
		composite.getComboDisplayPort().setSelectedIndex(0);
		form.setSelectedPort(lstPort.get(0).getLabel());
		form.setSelectedDisplayType(lstType.get(0).getLabel());
		
		dispManufacturer = IVoucherConstants.CUST_DISP_PIONEER_MANFT;
		dispModel		 = IVoucherConstants.CUST_DISP_PIONEER_MODEL;
		dispDriver		 = IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
		
		form.setDriver(dispDriver);
		form.setManufacturer(dispManufacturer);
		form.setModel(dispModel);
		form.setIsCustomerDisplay(false);
		setPreferenceValues();
		populateScreen(composite);
		
		setButtonSelection();
		
		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			composite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_PREF_LOG_OUT_TO_SET));
			composite.disableControls();
			return;
		}		
	}

	public void setPreferenceValues(){

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		Boolean isDispCenter = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_CENTER);
		Boolean isDispScroll = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_SCROLL);


		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY))) {
			form.setIsCustomerDisplay(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY));
		}
		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_CENTER))) {
			form.setIsCenterDisplay(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_CENTER));
		}
		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_SCROLL))) {
			form.setIsScrollDisplay(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_SCROLL));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_TEST_STATUS))) {
			form.setTestStatus(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_TEST_STATUS));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE))) {
			form.setWelcomeMessage(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE));
		}

		setSelectedDisplayTypeValue(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE));
		setSelectedDisplayPortValue(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT));

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER))) {
			form.setManufacturer(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL))) {
			form.setModel(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER))) {
			form.setDriver(preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER));
		}

		if(isDispCenter){
			boolean toggleStatus = true;
			composite.setCenterDisplay(toggleStatus);											
			composite.getRadioButtonControl().setSelectedButton(composite.getRadioImageDispCenter());
		}
		else if(isDispScroll){
			boolean toggleStatus = true;
			composite.setCenterDisplay(toggleStatus);											
			composite.getRadioButtonControl().setSelectedButton(composite.getRadioImageDispScroll());
		}	
	}

	@SuppressWarnings("unchecked")
	public void setSelectedDisplayPortValue(String selectedDisplayPortValue)
	{
		if(!Util.isEmpty(selectedDisplayPortValue))
		{
			List<ComboLabelValuePair> lstPort = form.getPortList();

			if(lstPort != null && lstPort.size() >0)
			{
				for(int i=0; i< lstPort.size(); i++)
				{
					ComboLabelValuePair comboLabelValuePair = lstPort.get(i);
					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedDisplayPortValue))
					{
						form.setSelectedPort(selectedDisplayPortValue);
						composite.getComboDisplayPort().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedDisplayTypeValue(String selectedDisplayTypeValue)
	{
		if(!Util.isEmpty(selectedDisplayTypeValue))
		{
			List<ComboLabelValuePair> lstType = form.getDisplayTypeList();

			if(lstType != null && lstType.size() >0)
			{
				for(int i=0; i< lstType.size(); i++)
				{
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);
					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedDisplayTypeValue))
					{
						form.setSelectedDisplayType(selectedDisplayTypeValue);
						composite.getComboDisplayType().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Control ctrl = ((Control)e.getSource());
		try {
			populateForm(composite);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}	

		if (ctrl instanceof SDSImageLabel) {
			SDSImageLabel imgLabel = (SDSImageLabel) ctrl;
			if(imgLabel.getName().equalsIgnoreCase(PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY_TEST)){
				boolean exceptionOccured = false;
				try {

					if(Util.isEmpty(dispModel)&& Util.isEmpty(dispManufacturer)&& Util.isEmpty(dispDriver) &&(Util.isEmpty(form.getSelectedPort()))){
						IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
						preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE, IVoucherConstants.VOU_PREF_DIS_TYPE1);
						preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT, IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
						preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER, IVoucherConstants.CUST_DISP_PIONEER_MANFT);
						preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL, IVoucherConstants.CUST_DISP_PIONEER_MODEL);
						preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER, IVoucherConstants.CUST_DISP_PIONEER_DRIVER);
						dispModel = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL);
						dispManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER);
						form.setDriver(IVoucherConstants.CUST_DISP_PIONEER_DRIVER);
						form.setSelectedPort(IVoucherConstants.VOU_PREF_TKT_PRINT_COM1)	;							

					}
					Display display = null;
					Class.forName(dispDriver);
					display = Display.getDisplay(form.getSelectedDisplayType());	
					if(display!=null){											
						display.close();
					}
					display.open(form.getSelectedPort());
					display.blankDisplay();
					display.dispMsg(form.getTestStatus());	

				}  catch (ClassNotFoundException e1) {
					exceptionOccured = true;					
				}catch(Exception e1){					
					exceptionOccured = true;					
				}
				if(exceptionOccured){			
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_DISP_EXPTN));
				}else{
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_DISP_CONNECTION_SUCESS));
				}
				try {
					populateScreen(composite);
				} catch (Exception e1) {				
					e1.printStackTrace();
				}
			} 
			try {
				populateScreen(composite);
			} catch (Exception e2) {				
				e2.printStackTrace();
			}
		} else if( ctrl instanceof TSCheckBox ) {
			try {
				populateForm(composite);
			} catch (Exception e1) {					
				e1.printStackTrace();
			}				
			if(form.getDisplayType().contains(CUST_DISP_PIONEER_MANFT)){
				dispManufacturer=IVoucherConstants.CUST_DISP_PIONEER_MANFT;
				dispModel=IVoucherConstants.CUST_DISP_PIONEER_MODEL;
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
			}
			else {
				dispManufacturer=IVoucherConstants.CUST_DISP_PIONEER_MANFT;
				dispModel=IVoucherConstants.CUST_DISP_PIONEER_MODEL;
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
			}			
			form.setDriver(dispDriver);
			form.setManufacturer(dispManufacturer);
			form.setModel(dispModel);
		}
		
		else if( ctrl instanceof TSButtonLabel ) {
			TSButtonLabel buttonLabel = (TSButtonLabel) ctrl;
			if (buttonLabel.equals(composite.getRadioImageDispCenter())) {
				boolean toggleStatus = ctrl.equals(composite.getRadioImageDispCenter());
				composite.setCenterDisplay(toggleStatus);
				form.setIsCenterDisplay(toggleStatus);
				form.setIsScrollDisplay(false);
				TSButtonLabel tSButtonLabel = (TSButtonLabel) (ctrl);				
				composite.getRadioButtonControl().setSelectedButton(tSButtonLabel);
			} else if(ctrl.equals(composite.getRadioImageDispScroll())){				
				boolean toggleStatus = ctrl.equals(composite.getRadioImageDispScroll());
				composite.setCenterDisplay(toggleStatus);
				form.setIsScrollDisplay(toggleStatus);
				form.setIsCenterDisplay(false);
				TSButtonLabel tSButtonLabel = (TSButtonLabel) (ctrl);
				composite.getRadioButtonControl().setSelectedButton(tSButtonLabel);

			}
		}
		else if( ctrl instanceof SDSTSCheckBox ) {
			SDSTSCheckBox selectedChkBox = (SDSTSCheckBox) ctrl;
			if( selectedChkBox.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY) ) {
				if( !selectedChkBox.isSelected() ) {
					selectedChkBox.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					composite.getDetailsComposite().setEnabled(true);
					composite.getButtonComposite().setEnabled(true);
					form.setIsCustomerDisplay(true);
					selectedChkBox.setSelected(true);
				} else {
					selectedChkBox.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					composite.getDetailsComposite().setEnabled(false);
					composite.getButtonComposite().setEnabled(false);
					form.setIsCustomerDisplay(false);
					selectedChkBox.setSelected(false);
				}
			}
		}
	}

	public void setButtonSelection() {
		
		if( form.getIsCustomerDisplay() ) {			
			composite.getDetailsComposite().setEnabled(true);
			composite.getBtnCustomerDisplay().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			form.setIsCustomerDisplay(true);
		} else {
			composite.getDetailsComposite().setEnabled(false);
			composite.getButtonComposite().setEnabled(false);
			composite.getBtnCustomerDisplay().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			form.setIsCustomerDisplay(false);
		}
	}
	
	@Override
	public CustomerDisplayPreferenceComposite getComposite() {
		return composite;
	}

	public CustomerDisplayPreferenceForm getForm() {
		return form;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		CustomerDisplayPreferenceComposite custDispPreferenceComposite = (CustomerDisplayPreferenceComposite)argComposite;
		custDispPreferenceComposite.getBtnCustomerDisplay().addMouseListener(this);
		custDispPreferenceComposite.getBtnCustomerDisplayTest().addMouseListener(this);
		custDispPreferenceComposite.getRadioImageDispCenter().addMouseListener(this);
		custDispPreferenceComposite.getRadioImageDispScroll().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayType().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayType().getRight().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayType().getLeft().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayPort().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayPort().getRight().addMouseListener(this);
		custDispPreferenceComposite.getComboDisplayPort().getLeft().addMouseListener(this);
	}

	public void widgetDisposed(DisposeEvent e) {
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			if(Util.isEmpty(dispType) && Util.isEmpty(dispDriver)){
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
				dispType=IVoucherConstants.CUST_DISP_PIONEER_MANFT+" "+IVoucherConstants.CUST_DISP_PIONEER_MODEL;
			}
			Display display = null;
			Class.forName(dispDriver);
			display = Display.getDisplay(dispType);	
			display.blankDisplay();
			display.close();
		} catch (ClassNotFoundException e1) {			
			//e1.printStackTrace();			
		} catch (DisplayNoSuchDriverException e1) {			
			//e1.printStackTrace();
		} catch (IOException e1) {
			//e1.printStackTrace();
		}catch (Exception e1) {
			//e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e) ;
		//composite.redraw();
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}
}
