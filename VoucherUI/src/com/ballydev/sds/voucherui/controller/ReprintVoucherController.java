/*****************************************************************************
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

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
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
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.enumconstants.VoucherStatusEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.ReprintVoucherComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.ReprintVoucherForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.print.VoucherImage;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.AresUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This class acts as a controller class for the
 * voucher reprint composite
 * @author Nithya kalyani
 * @version $Revision: 22$ 
 */
public class ReprintVoucherController extends SDSBaseController{

	/**
	 * Instance of ReprintVoucherComposite
	 */
	private ReprintVoucherComposite reprintVoucherComposite;

	/**
	 * Instance of ReprintForm
	 */
	private ReprintVoucherForm reprintForm;

	/**
	 * Instance of Logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Instance of Ares
	 */
	private Ares ares = null;

	/**
	 * Constructor of the class
	 */
	public ReprintVoucherController(Composite parent,int style,ReprintVoucherForm reprintForm, SDSValidator pValidator) throws Exception {
		super(reprintForm,pValidator);		
		reprintVoucherComposite = new ReprintVoucherComposite(parent,style);
		this.reprintForm=reprintForm;
		VoucherMiddleComposite.setCurrentComposite(reprintVoucherComposite);	
		parent.layout();
		super.registerEvents(reprintVoucherComposite);
		reprintForm.addPropertyChangeListener(this);
		reprintVoucherComposite.getTxtBarcode().setFocus();
		registerCustomizedListeners(reprintVoucherComposite);
	}

	@Override
	public Composite getComposite() {		
		return reprintVoucherComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isBarcodeScannerEnbaled = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER);
		if(isBarcodeScannerEnbaled){
			ares = AresUtil.registerAresEvent(ares);
			if(ares!=null){
				ares.addAresEventListener(new AresEventListener(){
					public void aresEvent(AresEvent ae) {				
						try {
							if (ae instanceof AresBarcodeEvent) {
								final String barcode = ((AresBarcodeEvent)ae).getBarcode();
								if (barcode != null && barcode.length() == 18) {
									log.debug("Barcode: "+barcode);
									ares.accept();												
									reprintVoucherComposite.getDisplay().syncExec(new Runnable() {
										public void run(){									
											reprintForm.setBarCode(barcode);									
										}
									});


								} else {
									log.error("Invalid barcode length");
									ares.reject();
								}
							} else if (ae instanceof AresErrorEvent) {
								log.debug("Ares ErrorEvent");

							} else if (ae instanceof AresNoReadEvent) {
								log.debug("No Read");				
								ares.reject();
							} else if (ae instanceof AresStatusEvent) {
								AresStatusEvent status = (AresStatusEvent)ae;
								switch(status.getStatusType()) {
								case AresStatusEvent.MULTI_FEED_STOP:
									log.debug("Multi-feed");					
									break;
								case AresStatusEvent.START_CHECK:
									log.debug("Start check");
									ares.startOk();
									break;
								case AresStatusEvent.STOP:
									log.debug("Normal stop");				
									break;	
								default:
									log.debug("Default case executed");
								}
							}
						} catch (Exception e) {
							log.error(e);
						}

					}


				});
			}
		}



	}

	/**
	 * This method resets the values in the 
	 * reprint composite
	 */
	public void resetValues(){
		reprintForm.setBarCode("");
		try {
			populateScreen(reprintVoucherComposite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		super.widgetSelected(e);
		Control control = (Control) e.getSource();
		if(!((Control)e.getSource() instanceof SDSTSButton)){
			return;
		}
		if (((SDSTSButton) control).getName().equalsIgnoreCase(IVoucherConstants.PRINT_VOUCHER_CTRL_BTN_CANCEL)){
			reprintForm.setBarCode("");
			reprintVoucherComposite.getTxtBarcode().setFocus();
			try {
				populateScreen(reprintVoucherComposite);
			} catch (Exception e1) {				
				e1.printStackTrace();
			}

		}else if (((SDSTSButton) control).getName().equalsIgnoreCase(IVoucherConstants.PRINT_VOUCHER_CTRL_BTN_PRINT)){

		}
	}

	public void populateBarcode() {

		try 
		{
			populateForm(reprintVoucherComposite);
			boolean isValidate = validate(IVoucherConstants.REPRINT_VOUCHER_FORM,reprintForm,reprintVoucherComposite);
			if(!isValidate){					
				return;
			}
			if(!SiteUtil.isTktPrintEnabled()){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_PRINT_NOT_ENABLED,new String[] {AppContextValues.getInstance().getTicketText()}));
				return;
			}
			TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
			ticketInfoDTO.setBarcode(reprintForm.getBarCode());
			ticketInfoDTO.setTicketType(getTicketType(reprintForm.getBarCode()));
			ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
			ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
			ticketInfoDTO.setOptimisticLockVersion(reprintForm.getOptLockVersion());

			TicketInfoDTO inquiredTicketInfoDTO =  ServiceCall.getInstance().reprintVoucher(ticketInfoDTO);

			if(inquiredTicketInfoDTO!=null){
				if( inquiredTicketInfoDTO.isErrorPresent())	{
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(inquiredTicketInfoDTO));	
					reprintVoucherComposite.getTxtBarcode().setFocus();
					reprintVoucherComposite.getTxtBarcode().selectAll();
				}
				else{
					if(inquiredTicketInfoDTO.getStatus()== VoucherStatusEnum.ACTIVE){				
						VoucherImage img = new VoucherImage();						
						log.debug("Barcode to inquire: "+inquiredTicketInfoDTO.getBarcode());
						MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(inquiredTicketInfoDTO));
						resetValues();
						reprintVoucherComposite.getTxtBarcode().setFocus();
						TicketDetails details = img.copyObj(inquiredTicketInfoDTO);
						boolean printerError = false;
						String printerErrorMessage = "";						
						try {							
							IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
							String printerModel = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL);
							String printerManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER);
							String printerPort = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_PORT);
							String printerDriver = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER);
							if(Util.isEmpty(printerModel)|| Util.isEmpty(printerManufacturer)||Util.isEmpty(printerPort)){									
								preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_PORT, IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
								preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE, IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1);
								preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER, IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
								preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL, IVoucherConstants.TKTSCANNER_ITHACA_MODEL);
								preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER, IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
								printerModel = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL);
								printerManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER);
								printerPort = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_PORT);
								printerDriver = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER);								

							}
							ProgressIndicatorUtil.openInProgressWindow();
							Printer printer = null;
							Class.forName(printerDriver);
							printer = Printer.getPrinter(printerManufacturer, printerModel);					
							printer.open(printerPort);
							printer.printReceipt(details, SDSApplication.getLoggedInUserID(), printerModel);						
						} catch (PrinterNoSuchDriverException e1) {					
							printerError = true;
							printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_DRIVER)+e1.getMessage();
						} catch (ClassNotFoundException e1) {					
							printerError = true;
							printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_CLASS_DEF)+e1.getMessage();
						} catch (PrinterException e1) {					
							printerError = true;
							printerErrorMessage = e1.getMessage();
						}catch (Exception e1) {
							log.error("Error occured while printing the ticket: ", e1);
							if(!e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT)){								
								printerError = true;	
								printerErrorMessage = e1.getMessage();
							}		
						}finally{
							ProgressIndicatorUtil.closeInProgressWindow();
						}
						if(printerError){
							if(Util.isEmpty(printerErrorMessage)){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINTER_CONNECTION_ERROR));
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
							}
						}else{
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_CONNECTION_SUCESS));
						}

					}else{
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_STATUS_NOT_ACTIVE));		
						reprintVoucherComposite.getTxtBarcode().setFocus();
						reprintVoucherComposite.getTxtBarcode().selectAll();
					}

				}
			}


		}catch(VoucherEngineServiceException ex)	{
			log.error("Exception while reprinting the voucher", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
			reprintVoucherComposite.getTxtBarcode().setFocus();
			reprintVoucherComposite.getTxtBarcode().selectAll();
		}
		catch (Exception ex){				
			VoucherUIExceptionHandler.handleException(ex); 	
			log.error("Exception while reprinting the Voucher : ",ex);
		}			

	}

	/**
	 * This method returns the ticket type for the 
	 * barcode passed
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	private TicketTypeEnum getTicketType(String barcode) throws Exception{

		int tktType = Integer.parseInt(barcode.substring(0, 1));

		TicketTypeEnum retVal = null;

		switch (tktType) {
		case 1:
			retVal = TicketTypeEnum.CASHABLE_PROMO;
			break;
		case 2:
			retVal = TicketTypeEnum.NON_CASHABLE_PROMO;
			break;
		case 3:
			retVal = TicketTypeEnum.CASHABLE_PROMO_REQ_PLAYER_CARD;
			break;
		case 4:
			retVal = TicketTypeEnum.NON_CASHABLE_PROMO_REQ_PLAYER_CARD;
			break;
		case 5:
			retVal = TicketTypeEnum.SLOT_GENERATED;
			break;
		case 6:
			retVal = TicketTypeEnum.SLOT_GENERATED_NEW_JERSEY;
			break;
		case 7:
			retVal = TicketTypeEnum.NON_SLOT_GENERATED;
			break;

		default:
			break;
		}

		if(retVal == null) {
			throw new Exception("Invalid Ticket Type");//i18n the message
		}

		return retVal;
	}

	@Override
	public void focusGained(FocusEvent e){
		super.focusGained(e);
		//reprintVoucherComposite.redraw();
		if (e.getSource() instanceof SDSTSText){
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.keyCode==13 || e.keyCode == 16777296){	
			populateBarcode();
		} 			   		
	}
}
