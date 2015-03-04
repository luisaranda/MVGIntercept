/*****************************************************************************
 * $Id: VoucherHomeController.java,v 1.4, 2009-04-08 11:16:09Z, Ganesh, Amirtharaj$
 * $Date: 4/8/2009 6:16:09 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.controller;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.displays.Display;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public class VoucherHomeController extends SDSBaseController implements IVoucherConstants 
{
	private VoucherMiddleComposite composite=null;

	/**
	 *Logger Instance 
	 */

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private VoucherHeaderController headerController = null;

	/**
	 * VoucherHomeController constructor
	 * @param parent
	 * @param style
	 * @throws ExceptionE111
	 */
	public VoucherHomeController(Composite parent,Composite middleComposite,int style) throws Exception {
		super(new SDSForm(), null);		
		AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_START_SCREEN);
		headerController = new  VoucherHeaderController(this, parent,style);
		composite = new VoucherMiddleComposite(middleComposite,style);
		parent.layout();
		middleComposite.layout();
		super.registerEvents(composite);
		headerController.intialize(composite, style);
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();	
		boolean isCustomerDisplay = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY);
		if(isCustomerDisplay){
			displayWelcomeMessage();
		}

	}

	public void displayWelcomeMessage() throws Exception{		
		Display display = null;
		String printerErrorMessage = null;
		boolean printerError = false;
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String dispModel = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL);
			String dispManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER);
			String dispPort = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String displayMessage = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE);
			if(Util.isEmpty(dispModel)|| Util.isEmpty(dispManufacturer)||Util.isEmpty(dispPort)){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_PREF_VALUE_TO_SET));
				return;
			}
			log.debug("disp model: "+dispModel);
			log.debug("disp manuf: "+dispManufacturer);
			log.debug("disp port: "+dispPort);
			log.debug("disp driver: "+dispDriver);
			log.debug("disp type: "+dispType);			
			Class.forName(dispDriver);
			display = Display.getDisplay(dispType);
			if(display!=null){				
				display.close();
			}
			display.open(dispPort);
			display.dispMsg(displayMessage);								
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();	
			printerError = true;
			printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_CLASS_DEF);
		}catch (DisplayNoSuchDriverException e1) {			
			e1.printStackTrace();
			printerError = true;
			String mesg = e1.getMessage();
			if(mesg==null){
				mesg="";
			}
			printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_DRIVER)+mesg;
		} catch (Exception e1) {
			e1.printStackTrace();
			printerError = true;
			printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN);
			log.error("Error occured while printing the ticket: ", e1);																	
		}

		if(printerError){
			if(printerErrorMessage!=null){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
			}else{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN));
			}
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) 
	{
	}

	@Override
	public Composite getComposite() 
	{
		return null;
	}

	public Composite getMiddleComposite() 
	{
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {

	}

	/**
	 * @return the headerController
	 */
	public VoucherHeaderController getHeaderController() {
		return headerController;
	}
}
