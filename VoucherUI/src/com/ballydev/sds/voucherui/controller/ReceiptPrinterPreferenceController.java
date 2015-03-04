/*****************************************************************************
 * $Id: ReceiptPrinterPreferenceController.java,v 1.20, 2010-04-08 13:45:07Z, Verma, Nitin Kumar$
 * $Date: 4/8/2010 8:45:07 AM$
  ****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.TSCheckBox;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.ReceiptPrinterPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.ReceiptPrinterPreferenceForm;
import com.ballydev.sds.voucherui.print.EpsonWithLPTPort;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * @author Nithya kalyani R
 * @version $Revision: 21$ 
 */
public class ReceiptPrinterPreferenceController  extends SDSBaseController implements IVoucherConstants {

	private ReceiptPrinterPreferenceComposite composite;

	private ReceiptPrinterPreferenceForm form;

	private String tktManufacturer;

	private String tktModel;

	private String tktDriver;


	public ReceiptPrinterPreferenceController(Composite parent, int style ,ReceiptPrinterPreferenceForm form, SDSValidator pValidator) throws Exception 
	{
		super(form, pValidator);
		composite = new ReceiptPrinterPreferenceComposite(parent,style);
		this.form = form;
		super.registerEvents(composite);
		this.registerCustomizedListeners(composite);
		form.addPropertyChangeListener(this);

		List<ComboLabelValuePair> lstPort = new ArrayList<ComboLabelValuePair>();
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM1), IVoucherConstants.VOU_PREF_TKT_PRINT_COM1));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM2),IVoucherConstants.VOU_PREF_TKT_PRINT_COM2));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM3), IVoucherConstants.VOU_PREF_TKT_PRINT_COM3));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM4), IVoucherConstants.VOU_PREF_TKT_PRINT_COM4));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_LPT1), IVoucherConstants.VOU_PREF_TKT_PRINT_LPT1));
		form.setPrinterPortList(lstPort);	

		List<ComboLabelValuePair> lstPrinterType = new ArrayList<ComboLabelValuePair>();
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE1), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1));
//		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE2),IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE2));
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE3), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE3));		

		form.setPrinterTypeList(lstPrinterType);

		composite.getComboPrinterPort().setSelectedIndex(0);		
		form.setSelectedPrinterPort(lstPort.get(0).getLabel());
		form.setSelectedPrinterType(lstPrinterType.get(0).getLabel());
		
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		
		if( LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE3).equals(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE)) ) {
			tktManufacturer	= "";
			tktModel		= "";
			tktDriver		= "";
		} else {
			tktManufacturer	= IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
			tktModel		= IVoucherConstants.TKTSCANNER_ITHACA_MODEL;
			tktDriver		= IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
		}
		form.setDriver(tktDriver);
		form.setManufacturer(tktManufacturer);
		form.setModel(tktModel);	
		setPreferenceValues();
		populateScreen(composite);
		
		setButtonSelection();
		
		if( !Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
			composite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_RECEIPT_PRINT_PREF_VALUE_TO_SET));
			composite.disableControls();
			return;
		}
	}
	
	public void setButtonSelection() {
		
		if( form.getIsReceiptPrinting() ) {			
			composite.getDetailsCompoiste().setEnabled(true);
			composite.getChkReceiptPrinting().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			form.setIsReceiptPrinting(true);
		} else {
			composite.getDetailsCompoiste().setEnabled(false);
			composite.getChkReceiptPrinting().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			form.setIsReceiptPrinting(false);
		}
	}
	
	public void setPreferenceValues(){

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_RECEIPT_PRINTING)))
		{
			form.setIsReceiptPrinting(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_RECEIPT_PRINTING));
		}
		setSelectedPrinterPortValue(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT));
		setSelectedPrinterTypeValue(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE));

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_DRIVER))
				&& VOU_PREF_TKT_PRINT_LPT1.equals(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT)) ) {
			form.setDriver(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_DRIVER));
		}
		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MANUFACTURER))
				&& VOU_PREF_TKT_PRINT_LPT1.equals(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT)) ) {
			form.setManufacturer(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MANUFACTURER));
		}
		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MODEL))
				&& VOU_PREF_TKT_PRINT_LPT1.equals(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT)) ) {
			form.setModel(preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MODEL));
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedPrinterPortValue(String selectedPrinterPortValue)
	{
		if(!Util.isEmpty(selectedPrinterPortValue))
		{
			List<ComboLabelValuePair> lstPort = form.getPrinterPortList();

			if(lstPort != null && lstPort.size() >0)
			{
				for(int i=0; i< lstPort.size(); i++)
				{
					ComboLabelValuePair comboLabelValuePair = lstPort.get(i);
					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedPrinterPortValue))
					{
						form.setSelectedPrinterPort(selectedPrinterPortValue);
						composite.getComboPrinterPort().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedPrinterTypeValue(String selectedPrinterTypeValue){
		
		if(!Util.isEmpty(selectedPrinterTypeValue)) {
			List<ComboLabelValuePair> lstType = form.getPrinterTypeList();

			if(lstType != null && lstType.size() >0) {
				for(int i=0; i< lstType.size(); i++) {
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);

					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedPrinterTypeValue)) {
						form.setSelectedPrinterType(selectedPrinterTypeValue);
						composite.getComboPrinterType().setSelectedIndex(i);
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

		if( ctrl instanceof SDSImageLabel ) {
			SDSImageLabel imgLabel = (SDSImageLabel) e.getSource();
			
			if( imgLabel.getName().equalsIgnoreCase(PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING_TEST)) {
				@SuppressWarnings("unused")
				String exceptionMessage ="";
				boolean exceptionOccured = false;
				Printer printer = null;
				
				try {					
					if(Util.isEmpty(tktModel)&& Util.isEmpty(tktManufacturer)&& Util.isEmpty(tktModel) &&(Util.isEmpty(form.getSelectedPrinterPort()))){					
						IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_PORT, IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE, IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER, IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL, IVoucherConstants.TKTSCANNER_ITHACA_MODEL);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER, IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
					}
					
					if( !(form.getSelectedPrinterPort()).equals(VOU_PREF_TKT_PRINT_LPT1) ) {

						Class.forName(form.getDriver());
						printer = Printer.getPrinter(tktManufacturer, tktModel);
						printer.open(form.getSelectedPrinterPort());
						printer.printVoidReceipt();
					} else {
						EpsonWithLPTPort epsonWithLPTPort = new EpsonWithLPTPort();
						epsonWithLPTPort.printVoidReceipt_LPT(form.getSelectedPrinterType());
						System.out.println("asdfas");
					}
					
				} catch (PrinterNoSuchDriverException e1) {
					exceptionOccured = true;
					exceptionMessage = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.REC_PRINT_EXPTN_NO_DRIVER)+e1.getMessage();
				} catch (ClassNotFoundException e1) {
					exceptionOccured = true;
					exceptionMessage = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.REC_PRINT_EXPTN_NO_CLASS_DEF)+e1.getMessage();
				} catch (PrinterException e1) {
					exceptionOccured = true;
					exceptionMessage = e1.getMessage();
				}catch(Exception e1){
					exceptionOccured = true;
					exceptionMessage = e1.getMessage();
				}
				if( exceptionOccured ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.REC_PRINT_EXPTN));					
				} 
				else if( !(form.getSelectedPrinterPort()).equals(VOU_PREF_TKT_PRINT_LPT1) ) {
					String printerStatus = null;
					try {
						printerStatus = printer.getStatus();
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.RECEIPT_PRINT_CONNECTION_SUCESS)+"\n"+printerStatus);
					} catch (IOException ex) {
						ex.printStackTrace();
						printerStatus = LabelLoader.getLabelValue(ITicketConstants.VOU_TICKET_PRINTER_CONNECTION_ERROR);
						MessageDialogUtil.displayTouchScreenInfoDialog(printerStatus);
					}
//					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.RECEIPT_PRINT_CONNECTION_SUCESS));
				}
				try {
					populateScreen(composite);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if( ctrl instanceof TSCheckBox ) {
			try {
				populateForm(composite);
			} catch (Exception e1) {					
				e1.printStackTrace();
			}				
			if( form.getSelectedPrinterType().contains(TKTSCANNER_ITHACA_MANFT) ) {
				tktManufacturer	= IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
				tktModel		= IVoucherConstants.TKTSCANNER_ITHACA_MODEL;
				tktDriver		= IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			} else if( form.getSelectedPrinterType().contains(TKTSCANNER_EPSON_MANFT) ) {
				tktManufacturer	= IVoucherConstants.TKTSCANNER_EPSON_MANFT;
				tktModel		= IVoucherConstants.TKTSCANNER_EPSON_MODEL;
				tktDriver		= IVoucherConstants.TKTSCANNER_EPSON_DRIVER;
			} else if( form.getSelectedPrinterType().contains(TKTSCANNER_ITHACA_750_MANFT) ) {
				tktManufacturer	= IVoucherConstants.TKTSCANNER_ITHACA_750_MANFT;
				tktModel		= IVoucherConstants.TKTSCANNER_ITHACA_750_MODEL;
				tktDriver		= IVoucherConstants.TKTSCANNER_EPSON_DRIVER;
			} else if( form.getSelectedPrinterType().contains(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE3))) {
				tktManufacturer	= "";
				tktModel		= "";
				tktDriver		= "";
			} else {
				tktManufacturer	= IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
				tktModel		= IVoucherConstants.TKTSCANNER_ITHACA_MODEL;
				tktDriver		= IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			}			
			form.setDriver(tktDriver);
			form.setManufacturer(tktManufacturer);
			form.setModel(tktModel);
			try {
				populateScreen(composite);
			} catch (Exception e2) {				
				e2.printStackTrace();
			}
		}

		else if( ctrl instanceof SDSTSCheckBox ) {
			SDSTSCheckBox chkBoxReceiptPrinter = (SDSTSCheckBox) e.getSource();

			if( chkBoxReceiptPrinter.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING) ) {

				if( !chkBoxReceiptPrinter.isSelected() ) {
					chkBoxReceiptPrinter.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					composite.getDetailsCompoiste().setEnabled(true);
					form.setIsReceiptPrinting(true);
					chkBoxReceiptPrinter.setSelected(true);
				} else {
					chkBoxReceiptPrinter.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					composite.getDetailsCompoiste().setEnabled(false);
					form.setIsReceiptPrinting(false);
					chkBoxReceiptPrinter.setSelected(false);
				}
			}
		}
	}

	@Override
	public ReceiptPrinterPreferenceComposite getComposite() {
		return composite;
	}

	public ReceiptPrinterPreferenceForm getForm() {
		return form;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ReceiptPrinterPreferenceComposite barcodeScannerPreferenceComposite = (ReceiptPrinterPreferenceComposite)argComposite;
		barcodeScannerPreferenceComposite.getBtnReceiptPrinterTest().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterPort().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterPort().getRight().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterPort().getLeft().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterType().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterType().getRight().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboPrinterType().getLeft().addMouseListener(this);
		barcodeScannerPreferenceComposite.getChkReceiptPrinting().addMouseListener(this);
	}
}
