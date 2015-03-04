/*****************************************************************************
 * $Id: BarcodeScannerPreferenceController.java,v 1.14, 2010-05-27 13:48:33Z, Verma, Nitin Kumar$
 * $Date: 5/27/2010 8:48:33 AM$
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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.ares.Ares;
import com.ballydev.sds.framework.ares.AresBarcodeEvent;
import com.ballydev.sds.framework.ares.AresErrorEvent;
import com.ballydev.sds.framework.ares.AresEvent;
import com.ballydev.sds.framework.ares.AresEventListener;
import com.ballydev.sds.framework.ares.AresNoReadEvent;
import com.ballydev.sds.framework.ares.AresStatusEvent;
import com.ballydev.sds.framework.constant.IConstants;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.TSCheckBox;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.BarcodeText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.BarcodeScannerPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.BarcodeScannerPreferenceForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.util.AresObject;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * This Class will show all scanner related information which will be available to the TS application.  
 * @author VNitinkumar
 */
public class BarcodeScannerPreferenceController  extends SDSBaseController implements IVoucherConstants{

	private BarcodeScannerPreferenceComposite composite;

	private BarcodeScannerPreferenceForm form;

	private String tktManufacturer;

	private String tktModel;

	private String tktDriver;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/** Instance of Ares */
	private static Ares ares;

	public BarcodeScannerPreferenceController(Composite parent, int style ,BarcodeScannerPreferenceForm form, SDSValidator pValidator) throws Exception 
	{
		super(form, pValidator);
		composite = new BarcodeScannerPreferenceComposite(parent,style);
		this.form = form;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);

		List<ComboLabelValuePair> lstPort = new ArrayList<ComboLabelValuePair>();
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM1), IVoucherConstants.VOU_PREF_TKT_PRINT_COM1));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM2),IVoucherConstants.VOU_PREF_TKT_PRINT_COM2));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM3), IVoucherConstants.VOU_PREF_TKT_PRINT_COM3));
		lstPort.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_COM4), IVoucherConstants.VOU_PREF_TKT_PRINT_COM4));

		List<ComboLabelValuePair> lstType = new ArrayList<ComboLabelValuePair>();
		lstType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_DUPLO), IVoucherConstants.TKTSCANNER_DUPLO_MANFT));
		lstType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_CUMMINS),IVoucherConstants.TKTSCANNER_CUMMINS_MANFT));
		lstType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_MULTISCAN), IVoucherConstants.TKTSCANNER_MULTISCAN_MANFT));
		lstType.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_METROLOGIC), IVoucherConstants.TKTSCANNER_METROLOGIC_MANFT));

		form.setScannerPortList(lstPort);
		form.setScannerTypeList(lstType);

		composite.getComboScannerPort().setSelectedIndex(0);
		composite.getComboScannerType().setSelectedIndex(0);
		
		form.setSelectedScannerPort(lstPort.get(0).getLabel());
		form.setSelectedScannerType(lstType.get(0).getLabel());
		form.setIsBarcodeScanner(composite.getChkBoxBarcodeScanner().isSelected());
		
		tktManufacturer=IVoucherConstants.TKTSCANNER_DUPLO_MANFT;
		tktModel=IVoucherConstants.TKTSCANNER_DUPLO_MODEL;
		tktDriver=IVoucherConstants.TKTSCANNER_DUPLO_DRIVER;
		
		form.setDriver(tktDriver);
		form.setManufacturer(tktManufacturer);
		form.setModel(tktModel);
		
		setPreferencesValues();
		populateScreen(composite);
		setButtonSelection();
		
		if( !Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
			composite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SCANNER_PREF_VALUE_TO_SET));
			composite.disableControls();
			return;
		}
		this.registerCustomizedListeners(composite);
//		setScanner();
	}

	private void setPreferencesValues() {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		if( !Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER)) ) {
			form.setIsBarcodeScanner(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER));
		}

		setSelectedScannerPortValue(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_PORT));
		setSelectedScannerTypeValue(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_TYPE));

		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_MANUFACTURER)) ) {
			form.setManufacturer(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_MANUFACTURER));
		}

		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_MODEL)) ) {
			form.setModel(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_MODEL));
		}

		//TEMP FIX UNTIL THE FRAMEWORK PROVIDES THE SCANNER SUPPORT
		if( !Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_DRIVER)) ) {
			form.setDriver(preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_DRIVER));
		}
//		if(!Util.isEmpty(SDSPreferenceStore.getStringStoreValue(IVoucherPreferenceConstants.SCANNER_DRIVER))){
//			form.setDriver(SDSPreferenceStore.getStringStoreValue(IVoucherPreferenceConstants.SCANNER_DRIVER));
//		}

	}

	@SuppressWarnings("unchecked")
	public void setSelectedScannerPortValue(String selectedScannerPortValue) {
		
		if( !Util.isEmpty(selectedScannerPortValue) ) {
			List<ComboLabelValuePair> lstPort = form.getScannerPortList();

			if( lstPort != null && lstPort.size() > 0 ) {
				
				for( int i = 0; i < lstPort.size(); i++) {
					ComboLabelValuePair comboLabelValuePair = lstPort.get(i);
					if( !Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedScannerPortValue) )	{
						form.setSelectedScannerPort(selectedScannerPortValue);
						composite.getComboScannerPort().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedScannerTypeValue(String selectedScannerTypeValue) {
		
		if( !Util.isEmpty(selectedScannerTypeValue) ) {
			List<ComboLabelValuePair> lstType = form.getScannerTypeList();

			if(lstType != null && lstType.size() > 0) {
				
				for(int i = 0; i < lstType.size(); i++) {
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);
					if( !Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedScannerTypeValue) ) {
						form.setSelectedScannerType(selectedScannerTypeValue);
						composite.getComboScannerType().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	public boolean isAresAttached() {
		return ares != null;
	}

	public void resetScanner() {
		try {
			ares.reset();
		}catch (Exception e) {
			log.error("ares.reset exception :" + e.getMessage());
		}
	}

	public void stopScanner() {
		try {
			ares.stop();
		}catch (Exception e) {
			log.error("ares.stop exception :" + e.getMessage());
		}
	}

	public void closeScannerPort() {
		try{
			ares.close();
		} catch(Exception e) {
			log.error("ares.close exception :" + e.getMessage());
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
			if( imgLabel.getName().equalsIgnoreCase(PREFERENCE_BARCODE_SCANNER_CTRL_BTN_TICKET_SCANNER_TEST) ) {
				try {
					if( !Util.isEmpty(tktModel)&& !Util.isEmpty(tktManufacturer)&&!Util.isEmpty(tktModel)
							&& ( !Util.isEmpty(form.getSelectedScannerPort())) ) {
						stopScanner();
						resetScanner();
						closeScannerPort();
						ares = AresObject.getAres(form.getManufacturer(), form.getModel());
						setScanner();
						log.info("Trying to connect to scanner port "+form.getSelectedScannerPort().trim());
						ares.open(form.getSelectedScannerPort().trim());
						log.info("Successfully connected to scanner port "+form.getSelectedScannerPort().trim());
						form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_CONNECTED));

					} else {
						form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_NOT_SELECTED));
					}
				} catch(Exception e1) {
					e1.printStackTrace();
					if( e1.getMessage() != null ) {
						if( !e1.getMessage().contains("native_psmisc") )
							form.setTestStatus(e1.getMessage());
						else 
							form.setTestStatus(LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_SCANNER_EXCEPTION));
					} else {
						form.setTestStatus(LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_SCANNER_EXCEPTION));
					}
					log.error("Ares GetBarcode Reader Exception occurred:" + e1);
					if( ares != null ) {
						stopScanner();
						resetScanner();
						closeScannerPort();
					}
					ares = null;
				}
				try {
					populateScreen(composite);
				} catch (Exception e1) {				
					e1.printStackTrace();
				}
			}
		}
		else if( ctrl instanceof TSCheckBox ) {
			try {
				populateForm(composite);
			} catch (Exception e1) {					
				e1.printStackTrace();
			}				
			if( form.getSelectedScannerType().contains(TKTSCANNER_DUPLO_MANFT) ) {
				tktManufacturer=IVoucherConstants.TKTSCANNER_DUPLO_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_DUPLO_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_DUPLO_DRIVER;
			} else if( form.getSelectedScannerType().contains(TKTSCANNER_CUMMINS_MANFT) ) {
				tktManufacturer=IVoucherConstants.TKTSCANNER_CUMMINS_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_CUMMINS_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_CUMMINS_DRIVER;
			} else if( form.getSelectedScannerType().contains(TKTSCANNER_MULTISCAN_MANFT) ) {
				tktManufacturer=IVoucherConstants.TKTSCANNER_MULTISCAN_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_MULTISCAN_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_MULTISCAN_DRIVER;
			} else if( form.getSelectedScannerType().contains(TKTSCANNER_METROLOGIC_MANFT) ) {
				tktManufacturer	= IConstants.TKTSCANNER_METROLOGIC_MANFT;
				tktModel		= IConstants.TKTSCANNER_METROLOGIC_MODEL;
				tktDriver 		= IConstants.TKTSCANNER_METROLOGIC_SCAN_DRIVER;
			}
			else {
				tktManufacturer=IVoucherConstants.TKTSCANNER_DUPLO_MANFT;
				tktModel=IVoucherConstants.TKTSCANNER_DUPLO_MODEL;
				tktDriver=IVoucherConstants.TKTSCANNER_DUPLO_DRIVER;
			}
			form.setDriver(tktDriver);
			form.setManufacturer(tktManufacturer);
			form.setModel(tktModel);
//			form.setScannerPort(scannerPort);
			try {
				populateScreen(composite);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		else if( ctrl instanceof SDSTSCheckBox ) {
			SDSTSCheckBox chkBoxBarcodeScanner = (SDSTSCheckBox) e.getSource();

			if( chkBoxBarcodeScanner.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_BTN_BARCODE_SCANNER) ) {
				
				if( !chkBoxBarcodeScanner.isSelected() ) {
					chkBoxBarcodeScanner.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					composite.getDetailsCompoiste().setEnabled(true);
					form.setIsBarcodeScanner(true);
					chkBoxBarcodeScanner.setSelected(true);
				} else {
					chkBoxBarcodeScanner.setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					composite.getDetailsCompoiste().setEnabled(false);
					form.setIsBarcodeScanner(false);
					chkBoxBarcodeScanner.setSelected(false);
				}
			}
		}
	}

	public void setButtonSelection() {
		
		if( form.getIsBarcodeScanner() ) {			
			composite.getDetailsCompoiste().setEnabled(true);
			composite.getChkBoxBarcodeScanner().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			form.setIsBarcodeScanner(true);
		} else {
			composite.getDetailsCompoiste().setEnabled(false);
			composite.getChkBoxBarcodeScanner().setImage(new Image(org.eclipse.swt.widgets.Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			form.setIsBarcodeScanner(false);
		}
	}
	
	@Override
	public BarcodeScannerPreferenceComposite getComposite() {
		return composite;
	}

	public BarcodeScannerPreferenceForm getForm() {
		return form;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite)	{
		BarcodeScannerPreferenceComposite barcodeScannerPreferenceComposite = (BarcodeScannerPreferenceComposite)argComposite;
		barcodeScannerPreferenceComposite.getBtnTicketScannerTest().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerPort().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerPort().getLeft().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerPort().getRight().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerType().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerType().getLeft().addMouseListener(this);
		barcodeScannerPreferenceComposite.getComboScannerType().getRight().addMouseListener(this);
		barcodeScannerPreferenceComposite.getChkBoxBarcodeScanner().addMouseListener(this);
	}
	
	private void setScanner() {
		if( ares == null ) {
			return;
		}
		ares.addAresEventListener(new AresEventListener(){

			public void aresEvent(AresEvent ae) {
				try {
					if( ae instanceof AresBarcodeEvent ) {
						final String barcode = ((AresBarcodeEvent)ae).getBarcode();
						ares.accept();
						if ( composite != null && !composite.isDisposed() ) {
							composite.getDisplay().syncExec(new Runnable() {
								public void run(){
									form.setTestStatus(BarcodeText.expandBarcode(barcode));
								}
							});
						}
					} else if (ae instanceof AresErrorEvent) {
						final String eventStr = ae.toString();
						composite.getDisplay().syncExec(new Runnable() {
							public void run(){
								form.setTestStatus(eventStr);
							}
						});
					} else if (ae instanceof AresNoReadEvent) {
						composite.getDisplay().syncExec(new Runnable() {
							public void run(){
								form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_NO_READ));
							}
						});

						ares.reject();
					} else if (ae instanceof AresStatusEvent) {
						AresStatusEvent status = (AresStatusEvent)ae;
						switch(status.getStatusType()) {
						case AresStatusEvent.MULTI_FEED_STOP:		            	                    
							composite.getDisplay().syncExec(new Runnable() {
								public void run(){
									form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_MULTI_FEED));
								}
							});

							break;
						case AresStatusEvent.START_CHECK:		            	                
							ares.startOk();
							composite.getDisplay().syncExec(new Runnable() {
								public void run(){
									form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_START));
								}
							});

							break;
						case AresStatusEvent.STOP:
							composite.getDisplay().syncExec(new Runnable() {
								public void run(){
									form.setTestStatus(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.FRAMEWORK_SCANNER_STOP));
								}
							});

							break;
						default:
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Ares Event Exception occurred:"+e.getMessage());
				}
			}
		});
	}
}
