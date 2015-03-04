/*****************************************************************************
 * $Id: TicketPrinterPreferenceController.java,v 1.27, 2011-03-21 12:36:21Z, SDS Check in user$
 * $Date: 3/21/2011 6:36:21 AM$
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.control.TSCheckBox;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.TicketPrinterPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.TicketPrinterPreferenceForm;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * This is the class used to set the preference value for Printer to print the Vouchers.
 * @author VNitinkumar
 */
public class TicketPrinterPreferenceController  extends SDSBaseController implements IVoucherConstants {

	private TicketPrinterPreferenceComposite composite;

	private TicketPrinterPreferenceForm tPPForm;

	private String tktManufacturer;

	private String tktModel;

	private String tktDriver;
	
	Boolean isSmallSize  = false;
	
	Boolean isNormalSize = false;
	
	public TicketPrinterPreferenceController(Composite parent, int style ,TicketPrinterPreferenceForm form, SDSValidator pValidator) throws Exception {
		super(form, pValidator);
		composite = new TicketPrinterPreferenceComposite(parent,style);
		this.tPPForm = form;
		super.registerEvents(composite);
		this.registerCustomizedListeners(composite);
		form.addPropertyChangeListener(this);

		List<ComboLabelValuePair> lstPort = new ArrayList<ComboLabelValuePair>();
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM1), IVoucherConstants.VOU_PREF_TKT_PRINT_COM1));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM2), IVoucherConstants.VOU_PREF_TKT_PRINT_COM2));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM3), IVoucherConstants.VOU_PREF_TKT_PRINT_COM3));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM4), IVoucherConstants.VOU_PREF_TKT_PRINT_COM4));
		form.setPrinterPortList(lstPort);

		List<ComboLabelValuePair> lstPrinterType = new ArrayList<ComboLabelValuePair>();
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE4), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE4));
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE1), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1));
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE5), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE5));
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE2), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE2));
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE3), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE3));		
		lstPrinterType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE6), IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE6));		
		form.setPrinterTypeList(lstPrinterType);

		composite.getComboPrinterPort().setSelectedIndex(0);
		form.setSelectedPrinterPort(lstPort.get(0).getLabel());
		form.setSelectedPrinterType(lstPrinterType.get(0).getLabel());
		tktManufacturer = IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
		tktModel  		= IVoucherConstants.TKTSCANNER_ITHACA_750_MODEL;
		tktDriver 		= IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
		form.setDriver(tktDriver);
		form.setManufacturer(tktManufacturer);
		form.setModel(tktModel);
		setPreferenceValues();
		populateScreen(composite);
		if( !Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
			composite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_PREF_VALUE_TO_SET));
			composite.disableControls();
			return;
		}
	}

	public void setPreferenceValues() {

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		isSmallSize  = preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_SMALL_SIZE);
		isNormalSize = preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_NORMAL_SIZE);

		if( !Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_TICKET_PRINTING)) ) {
			tPPForm.setIsTicketPrinting(SDSPreferenceStore.getBooleanStoreValue(IVoucherPreferenceConstants.IS_TICKET_PRINTING));
		}	

		setSelectedPrinterPortValue(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_PORT));
		setSelectedPrinterTypeValue(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE));

		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER)) ) {
			tPPForm.setManufacturer(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER));
		}

		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL)) ) {
			tPPForm.setModel(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL));
		}

		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER)) ) {
			tPPForm.setDriver(preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER));
		}		

		if( !Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_SMALL_SIZE)) ) {
			tPPForm.setIsSmallSize(preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_SMALL_SIZE));
		}
		
		if( !Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_NORMAL_SIZE)) ) {
			tPPForm.setIsNormalSize(preferenceStore.getBoolean(IVoucherPreferenceConstants.TICKET_PRINTER_NORMAL_SIZE));
		}

		if( isSmallSize ) {
			boolean toggleStatus = true;
			composite.setPrintSize(toggleStatus);
//			composite.getGrpSizeSelection().setSelectedButton(composite.getRadioImageSmallSize());
			composite.getRadioImageSmallSize().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
			composite.getRadioImageSmallSize().setSelected(true);
			composite.getRadioImageNormalSize().setSelected(false);
			composite.getRadioImageNormalSize().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		}
		else if( isNormalSize ) {
			boolean toggleStatus = true;
			composite.setPrintSize(toggleStatus);
//			composite.getGrpSizeSelection().setSelectedButton(composite.getRadioImageNormalSize());
			composite.getRadioImageNormalSize().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
			composite.getRadioImageNormalSize().setSelected(true);
			composite.getRadioImageSmallSize().setSelected(false);
			composite.getRadioImageSmallSize().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		}	
	}


	@SuppressWarnings("unchecked")
	public void setSelectedPrinterPortValue(String selectedPrinterPortValue) {
		if( !Util.isEmpty(selectedPrinterPortValue) ) {
			List<ComboLabelValuePair> lstPort = tPPForm.getPrinterPortList();

			if( lstPort != null && lstPort.size() > 0 ) {

				for( int i = 0; i < lstPort.size(); i++ ) {
					ComboLabelValuePair comboLabelValuePair = lstPort.get(i);

					if( !Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedPrinterPortValue)) {
						tPPForm.setSelectedPrinterPort(selectedPrinterPortValue);
						composite.getComboPrinterPort().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedPrinterTypeValue(String selectedPrinterTypeValue) {
		if( !Util.isEmpty(selectedPrinterTypeValue) ) {
			List<ComboLabelValuePair> lstType = tPPForm.getPrinterTypeList();

			if( lstType != null && lstType.size() > 0 ) {

				for( int i = 0; i < lstType.size(); i++ ) {
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);
				
					if( !Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedPrinterTypeValue)) {
						tPPForm.setSelectedPrinterType(selectedPrinterTypeValue);
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
			SDSImageLabel button = (SDSImageLabel) e.getSource();
			if( button.getName().equalsIgnoreCase(PREFERENCE_TICKET_PRINTER_CTRL_BTN_TICKET_PRINTING) ) {
				boolean exceptionOccured = false;
				String exceptionMessage = "";
				Printer printer = null;
				try {
					if( Util.isEmpty(tktModel)&& Util.isEmpty(tktManufacturer)&&Util.isEmpty(tPPForm.getDriver()) &&(Util.isEmpty(tPPForm.getSelectedPrinterPort()))) {
						IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_PORT, IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE, IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER, IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL, IVoucherConstants.TKTSCANNER_ITHACA_MODEL);
						preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER, IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
						tktModel = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL);
						tktManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER);

						tPPForm.setDriver(IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
						tPPForm.setSelectedPrinterPort(IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
					}
					Class.forName(tPPForm.getDriver());
					printer = Printer.getPrinter(tPPForm.getManufacturer(), tPPForm.getModel());
					printer.open(tPPForm.getSelectedPrinterPort());

					if( tPPForm.getSelectedPrinterType().equals(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE6)) ) {
						printer.printVoidTicket();
					} else {
						printer.printVoidTicket(isSmallSize);
					}

				} catch (PrinterNoSuchDriverException e1) {
					exceptionOccured = true;
					exceptionMessage = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_EXPTN_NO_DRIVER)+e1.getMessage();
				} catch (ClassNotFoundException e1) {
					exceptionOccured = true;
					exceptionMessage = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_EXPTN_NO_CLASS_DEF)+e1.getMessage();
				} catch (PrinterException e1) {
					exceptionOccured = true;
					e1.printStackTrace();
					exceptionMessage = e1.getMessage();
				} catch(Exception e1) {
					exceptionOccured = true;
					exceptionMessage = e1.getMessage();
				}
				try {
					Class.forName(tPPForm.getDriver());
					printer = Printer.getPrinter(tPPForm.getManufacturer(), tPPForm.getModel());
					printer.open(tPPForm.getSelectedPrinterPort());
					if( exceptionOccured ) {
						String printerStatus = null;
						try {
							printer.modelName(tPPForm.getModel());
							printer.tktSmallSize(tPPForm.getIsSmallSize().booleanValue());
							printerStatus = printer.getStatus();
						} catch (IOException ex) {
							printerStatus = LabelLoader.getLabelValue(ITicketConstants.VOU_TICKET_PRINTER_CONNECTION_ERROR);
						}
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_EXPTN)+"\n"+printerStatus);
					} else {
						String printerStatus = null;
						try {
							printer.modelName(tPPForm.getModel());
							printer.tktSmallSize(tPPForm.getIsSmallSize().booleanValue());
							if( !tPPForm.getSelectedPrinterType().equals(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE6)) ) {
								printerStatus = printer.getStatus();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_CONNECTION_SUCESS) + "\n" + printerStatus);
							} else 
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_CONNECTION_SUCESS));
						} catch (IOException ex) {
							ex.printStackTrace();
							printerStatus = LabelLoader.getLabelValue(ITicketConstants.VOU_TICKET_PRINTER_CONNECTION_ERROR);
							MessageDialogUtil.displayTouchScreenInfoDialog(printerStatus);
						}
					}
					populateScreen(composite);
				} catch (Exception e1) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_PRINT_EXPTN)+"\n"+exceptionMessage);
					e1.printStackTrace();
				}
			}
		} else if( ctrl instanceof TSCheckBox ) {
			try {
				populateForm(composite);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if( tPPForm.getSelectedPrinterType().contains(TKTSCANNER_ITHACA_MANFT +"-"+ TKTSCANNER_ITHACA_MODEL) ) {
				tktManufacturer=IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_ITHACA_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			} else if (tPPForm.getSelectedPrinterType().contains(TKTSCANNER_EPSON_MANFT)) {
				tktManufacturer=IVoucherConstants.TKTSCANNER_EPSON_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_EPSON_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_EPSON_DRIVER;
			} else if (tPPForm.getSelectedPrinterType().contains(TKTSCANNER_ITHACA_750_MANFT +"-"+ TKTSCANNER_ITHACA_750_MODEL)){
				tktManufacturer=IVoucherConstants.TKTSCANNER_ITHACA_750_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_ITHACA_750_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			} else if (tPPForm.getSelectedPrinterType().contains(TKTSCANNER_ITHACA_950_MANFT +"-"+ TKTSCANNER_ITHACA_950_MODEL)){
				tktManufacturer=IVoucherConstants.TKTSCANNER_ITHACA_950_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_ITHACA_950_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			} else if (tPPForm.getSelectedPrinterType().contains(TKTSCANNER_NANOPTIX_MANFT)
					||tPPForm.getSelectedPrinterType().contains("VOU.PREF.TKT.PRINT.TYPE6") ){				
				tktManufacturer=IVoucherConstants.TKTSCANNER_NANOPTIX_MANFT;
					
				tktModel=IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			}
			else {
				tktManufacturer=IVoucherConstants.TKTSCANNER_ITHACA_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_ITHACA_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_ITHACA_DRIVER;
			}
			tPPForm.setDriver(tktDriver);
			tPPForm.setManufacturer(tktManufacturer);
			tPPForm.setModel(tktModel);
			try {
				populateScreen(composite);
			} catch (Exception e2) {				
				e2.printStackTrace();
			}
		}

		else if( ctrl instanceof TSButtonLabel ) {
			TSButtonLabel buttonLabel = (TSButtonLabel) e.getSource();	
			if( buttonLabel.equals(composite.getRadioImageSmallSize()) ) {
				boolean toggleStatus = ctrl.equals(composite.getRadioImageSmallSize());
				composite.setPrintSize(toggleStatus);
				tPPForm.setIsSmallSize(toggleStatus);
				tPPForm.setIsNormalSize(false);
				TSButtonLabel touchScreenRadioButton = (TSButtonLabel) (ctrl);
				composite.getRadioButtonControl().setSelectedButton(touchScreenRadioButton);

			} else if( ctrl.equals(composite.getRadioImageNormalSize()) ) {
				boolean toggleStatus = ctrl.equals(composite.getRadioImageNormalSize());
				composite.setPrintSize(toggleStatus);
				tPPForm.setIsNormalSize(toggleStatus);
				tPPForm.setIsSmallSize(false);
				TSButtonLabel touchScreenRadioButton = (TSButtonLabel) (ctrl);
				composite.getRadioButtonControl().setSelectedButton(touchScreenRadioButton);
			}
		} 
	}

	@Override
	public TicketPrinterPreferenceComposite getComposite() {
		return composite;
	}

	public TicketPrinterPreferenceForm getForm() {
		return tPPForm;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		TicketPrinterPreferenceComposite ticketPrinterPreferenceComposite = (TicketPrinterPreferenceComposite)argComposite;
		ticketPrinterPreferenceComposite.getRadioImageSmallSize().addMouseListener(this);
		ticketPrinterPreferenceComposite.getRadioImageNormalSize().addMouseListener(this);
		ticketPrinterPreferenceComposite.getBtnTicketPrinting().addMouseListener(this);
		ticketPrinterPreferenceComposite.getComboPrinterType().getRight().addMouseListener(this);
		ticketPrinterPreferenceComposite.getComboPrinterType().getLeft().addMouseListener(this);
	}

}
