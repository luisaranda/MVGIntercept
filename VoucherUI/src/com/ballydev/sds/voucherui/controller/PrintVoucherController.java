/*****************************************************************************
 * $Id: PrintVoucherController.java,v 1.56, 2011-03-21 11:29:10Z, SDS Check in user$
 * $Date: 3/21/2011 5:29:10 AM$
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.PrintVoucherComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.PrintVoucherForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.print.VoucherImage;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.shell.DialogShellForPrint;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This is the class used to control the print voucher from Voucher UI.
 * @author VNitinkumar
 */
public class PrintVoucherController extends SDSBaseController {

	/**
	 * Instance of PrintVoucherComposite
	 */
	private PrintVoucherComposite composite=null;

	/**
	 * Instance of PrintVoucherForm
	 */
	private PrintVoucherForm form = null;

	/**
	 * Logger Instance 
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * MainMenuController constructor
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public PrintVoucherController(Composite parent,int style, PrintVoucherForm form,SDSValidator validator) throws Exception {
		super(form,validator);		
		composite = new PrintVoucherComposite(parent,style);
		this.form=form;
		VoucherMiddleComposite.setCurrentComposite(composite);
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		composite.getTxtTicketAmount().setFocus();
		registerCustomizedListeners(composite);
//		composite.addDisposeListener(new DisposeListener(){
//			public void widgetDisposed(DisposeEvent e) {
//				closePort();
//			}
//		});
		parent.layout();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		if ((Control) e.widget instanceof SDSText) {
			SDSText txtToValidate = (SDSText) e.getSource();
			if(!(txtToValidate.getName().equalsIgnoreCase(IVoucherConstants.PRINTVOUCHER_CTRL_TXT_TICKETAMOUNT))){				
				boolean validate= validateTextFields(form, IVoucherConstants.PRINT_VOUCHER_FORM,composite, txtToValidate,null, false);
				if(!validate){
					return;
				}
				if(form.getTicketAmount()!=null && form.getTicketAmount().trim().length()>0){
					form.setTicketAmount(CurrencyUtil.getCurrencyFormat(Double.valueOf(form.getTicketAmount())));
				}
			}
		}
		
		Control control = (Control) e.getSource();

		if(!((Control)e.getSource() instanceof SDSImageLabel)){
			return;
		}
		if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.PRINT_VOUCHER_CTRL_BTN_CANCEL)){
			resetValues();
			composite.getTxtTicketAmount().setFocus();
		}
		if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.PRINT_VOUCHER_CTRL_BTN_PRINT))	{
			boolean isErrorPresent = false;
			try {
				populateForm(composite);
				boolean isValidate = validate(IVoucherConstants.PRINT_VOUCHER_FORM,form,composite);

				if(!isValidate)
					return;

				DecimalFormatSymbols localDfs = new DecimalFormatSymbols();
				localDfs.setDecimalSeparator('.');
				localDfs.setGroupingSeparator(',');
				DecimalFormat myFormatter = new DecimalFormat("0.00", localDfs);
				myFormatter.applyLocalizedPattern("0.00#");
				form.setTicketAmount(myFormatter.format(Double.valueOf(form.getTicketAmount())));
				
				long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());

				if( !SiteUtil.isTktPrintEnabled() ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_PRINT_NOT_ENABLED,new String[] {AppContextValues.getInstance().getTicketText()}));
					return;
				}

				TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
				ticketInfoDTO.setSessionId(getCurrentSessionId);
				ticketInfoDTO.setAmount(new Long(ConversionUtil.dollarToCents(form.getTicketAmount())));
				ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
				ticketInfoDTO.setTicketType(TicketTypeEnum.NON_SLOT_GENERATED);
				ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
				ticketInfoDTO.setAmountType(AmountTypeEnum.CASHABLE);
				ticketInfoDTO.setEffectiveDate(DateUtil.getCurrentServerDate());
				ticketInfoDTO.setExpireDate(SiteUtil.getNoOfDaysToExpireTkt());
				ticketInfoDTO.setPlayerCardReqd(IVoucherConstants.NO_PLAYER_REQD);
				ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
				ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

				int noOfTkts = 1;
				if( form.getTotalTktsToPrint() != null ) {
					try {
						noOfTkts = Integer.parseInt(form.getTotalTktsToPrint());
						if( noOfTkts>SiteUtil.getNoOfTktsToPrint()) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_MAX_NO_PRINT_EXCEEDED,new String[] {AppContextValues.getInstance().getTicketText()}));
							composite.getTxtNoOfTkts().setFocus();
							return;
						}
					} catch (NumberFormatException e1) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_TKTS_PROPER_VALUE, new String[] {AppContextValues.getInstance().getTicketText()}));
						composite.getTxtNoOfTkts().setFocus();
						return;
					}
				}

				try {
					String amt = null;
					long totalAmt = noOfTkts * new Long(ConversionUtil.dollarToCents(form.getTicketAmount()));
					amt = Long.toString(totalAmt);
					ServiceCall.getInstance().chkEmpTotalAmt(SDSApplication.getLoggedInUserID(), amt, SDSApplication.getSiteDetails().getId());
				} catch( VoucherEngineServiceException ex) {
					log.error("VoucherEngineServiceException while checking for the amx amt", ex);
					if( SiteUtil.allowOverrideForPrint() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_EXCEEDED_MAX_DAILY_AMT));
						ticketInfoDTO.setChkForEmpMaxAmt(false);
						if( KeyBoardUtil.getKeyBoardShell() != null ) {
							KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
						}
						@SuppressWarnings("unused")
						DialogShellForPrint dialogShellForPrint = new DialogShellForPrint(composite.getShell(),	this);

					} else {
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
						composite.getTxtTicketAmount().setFocus();
						ticketInfoDTO.setChkForEmpMaxAmt(true);
					}
					return;
				}
				catch (Exception ex) {
					log.error("Exception while checking for the amx amt : ",ex);
					if( SiteUtil.allowOverrideForPrint() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_EXCEEDED_MAX_DAILY_AMT));
						ticketInfoDTO.setChkForEmpMaxAmt(false);
						if( KeyBoardUtil.getKeyBoardShell() != null ) {
							KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
						}
						@SuppressWarnings("unused")
						DialogShellForPrint dialogShell = new DialogShellForPrint(composite.getShell(),	this);

					} else {
						VoucherUIExceptionHandler.handleException(ex);
						composite.getTxtTicketAmount().setFocus();
						ticketInfoDTO.setChkForEmpMaxAmt(true);
					}
					return;
				}

				List<TicketInfoDTO> createdTicketFormList =  ServiceCall.getInstance().createVoucherList(ticketInfoDTO,noOfTkts);
				if( createdTicketFormList!=null && createdTicketFormList.get(0)!=null ) {
					if( createdTicketFormList.get(0).isErrorPresent() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(createdTicketFormList.get(0)));
						composite.getTxtTicketAmount().setFocus();
					}
					else {
						String error = null;
						VoucherImage img = new VoucherImage();
						StringBuffer bufBarcodesSucess = new StringBuffer();
						for( int j = 0; j < createdTicketFormList.size(); j++ ) {
							if( createdTicketFormList.get(j).isErrorPresent() ) {
								isErrorPresent = true;
								error = VoucherUtil.getI18nMessageForDisplay(createdTicketFormList.get(j));
							} else {
								setAmountAndCount(createdTicketFormList.get(j));
								if( j == createdTicketFormList.size() - 1 ) {
									bufBarcodesSucess.append(createdTicketFormList.get(j).getBarcode());
								} else {
									bufBarcodesSucess.append(createdTicketFormList.get(j).getBarcode()+",");
								}
								Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PRINT_VOUCHER)+AppContextValues.getInstance().getTicketText() , LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE), "", VoucherUtil.formatBarcode(createdTicketFormList.get(j).getBarcode(),createdTicketFormList.get(j).getTicketType().getTicketType(), createdTicketFormList.get(j).getTktStatusId() ), LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_ADDED)+form.getTicketAmount(), EnumOperation.ADD_OPERATION,PreferencesUtil.getClientAssetNumber());
							}
						}						
						try {
							form.setCount("");
							form.setTicketAmount("");
							form.setTotalTktsToPrint("");
							populateScreen(composite);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						composite.getTxtTicketAmount().setFocus();
						List<TicketDetails> detailsList = img.copyObj(createdTicketFormList);
						boolean printerError 		= false;
						String printerErrorMessage 	= "";
						String printerStatus 		= "";
						List<TicketInfoDTO> infoDTO = new ArrayList<TicketInfoDTO>();
						
						for( int i = 0; i < detailsList.size(); i++ ) {
							TicketDetails details = detailsList.get(i);
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
								log.debug("Printer model : " + printerModel);
								log.debug("Printer manuf : " + printerManufacturer);
								log.debug("Printer port  : " + printerPort);
								log.debug("Printer driver: " + printerDriver);
								ProgressIndicatorUtil.openInProgressWindow();
								Printer printer = null;
								Class.forName(printerDriver);
								printer = Printer.getPrinter(printerManufacturer, printerModel);
								printer.open(printerPort);

								try {
									if( !printerModel.equals(IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL) )
										printerStatus = printer.getStatus();
								} catch (IOException e1) {
									throw new PrinterException(LabelLoader.getLabelValue(ITicketConstants.VOU_TICKET_PRINTER_CONNECTION_ERROR));
								}
								printer.printReceipt(details, SDSApplication.getLoggedInUserID(), printerModel);
							} catch(PrinterNoSuchDriverException e1) {
								e1.printStackTrace();
								printerError = true;
								printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_DRIVER)+e1.getMessage();
							} catch(ClassNotFoundException e1) {
								e1.printStackTrace();
								printerError = true;
								printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_CLASS_DEF)+e1.getMessage();
							} catch(PrinterException e1) {
								e1.printStackTrace();
								printerError = true;
								printerErrorMessage = e1.getMessage();
							}catch (Exception e1) {
								e1.printStackTrace();
								log.error("Error occured while printing the ticket: ", e1);
								if( !e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT) ) {
									printerError = true;
									printerErrorMessage = e1.getMessage();
								}
							} finally {
								ProgressIndicatorUtil.closeInProgressWindow();
							}
							if( printerError ) {
								if( SiteUtil.isTktVoidAllowed() ) {
									TicketInfoDTO ticketInfoDTO1 = new TicketInfoDTO();
									ticketInfoDTO1.setSessionId(getCurrentSessionId);
									ticketInfoDTO1.setBarcode(details.getBarcode());
									ticketInfoDTO1.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
									ticketInfoDTO1.setEmployeeId(SDSApplication.getLoggedInUserID());
									ticketInfoDTO1.setTicketType(getTicketType(details.getBarcode()));
									ticketInfoDTO1.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
									ticketInfoDTO1.setOptimisticLockVersion(form.getOptLockVersion());
									ticketInfoDTO1.setUserRoleTypeId(VoucherUtil.getUserRole());
									infoDTO.add(ticketInfoDTO1);
								}
							}
						} if( printerError || isErrorPresent ) {
							if( Util.isEmpty(printerErrorMessage) ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINTER_CONNECTION_ERROR));
							} else {
								if( isErrorPresent ) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(error);
								} else {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
								}
							}
							if( SiteUtil.isTktVoidAllowed() ) {
								ServiceCall.getInstance().voidVoucherList(infoDTO);
							} else {
								MessageDialogUtil.displayTouchScreenInfoDialog(noOfTkts+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE_SUCCESS,new String[] {AppContextValues.getInstance().getTicketText()}));
							}
						} else {
							MessageDialogUtil.displayTouchScreenInfoDialog(noOfTkts+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE_SUCCESS,new String[] {AppContextValues.getInstance().getTicketText()})+ "\n"+LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_CONNECTION_SUCESS)+"\n"+printerStatus);
							composite.getTxtTicketAmount().setFocus();
						}
					}
				}
			} catch(VoucherEngineServiceException ex) {
				log.error("VoucherEngineServiceException while creating the Voucher", ex);
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
				composite.getTxtTicketAmount().setFocus();
			}
			catch (Exception ex){
				log.error("Exception while Creating a Voucher : ",ex);
				VoucherUIExceptionHandler.handleException(ex);
				composite.getTxtTicketAmount().setFocus();
			}
		}

	}

	/**
	 * This method returns the type of the
	 * ticket
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	public TicketTypeEnum getTicketType(String barcode) throws Exception{
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
			throw new VoucherEngineServiceException(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_INVALID_TKT_TYPE));
		}

		return retVal;
	}


	/**
	 * This method resets the form values
	 */
	public void resetValues(){
		try {
			form.setCount("");
			form.setTicketAmount("");
			form.setTotalAmount("");
			form.setTotalCount("");		
			form.setTotalTktsToPrint("");
			populateScreen(composite);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void setAmountAndCount(TicketInfoDTO createdTicketForm)	{
		double amount = 0;
		int count = 0;
		if(!(Util.isEmpty(form.getTotalAmount()))){
			try	{
				amount = Double.parseDouble(form.getTotalAmount());
			}
			catch(NumberFormatException nfe){
				amount =0;
			}
		}
		if(!(Util.isEmpty(form.getTotalCount()))){
			try	{
				count = Integer.parseInt(form.getTotalCount());
			}
			catch(NumberFormatException nfe){
				count =0;
			}
		}
		double totalAmount = 0;
		int totalCount = 0;
		totalAmount =  amount + new Double(ConversionUtil.centsToDollar(createdTicketForm.getAmount()));
		totalCount = count + 1;
		form.setTotalAmount(ConversionUtil.voucherAmountFormat(new Double(totalAmount).toString()));
		form.setTotalCount(new Integer(totalCount).toString());			
	}

	@Override
	public Composite getComposite() {
		return composite;
	}



	public PrintVoucherComposite getPrintVoucherComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		PrintVoucherComposite printVoucherComposite = ((PrintVoucherComposite)argComposite);
		printVoucherComposite.getLblBtnPrint().addMouseListener(this);
		printVoucherComposite.getLblBtnPrint().getTextLabel().addMouseListener(this);
		printVoucherComposite.getLblBtnPrint().getTextLabel().addTraverseListener(this);
		printVoucherComposite.getLblBtnCancel().addMouseListener(this);
		printVoucherComposite.getLblBtnCancel().getTextLabel().addMouseListener(this);
		printVoucherComposite.getLblBtnCancel().getTextLabel().addTraverseListener(this);
	}

	@Override
	public void focusGained(FocusEvent e){
//		super.focusGained(e);
//		//composite.redraw();
//		if (e.getSource() instanceof SDSTSText){
//			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
//		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	@Override
	public void keyTraversed(TraverseEvent e) {		
		super.keyTraversed(e);
		if ( !(e.character > 31 )
				&& !(e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP
						|| e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) ) {
			if( (Control) e.widget instanceof SDSText ) {
				try {
					SDSText text = (SDSText) e.getSource();
					if( text.getName().equalsIgnoreCase(IVoucherConstants.PRINTVOUCHER_CTRL_TXT_TICKETAMOUNT) ) {
						boolean validate = validate(IVoucherConstants.PRINT_VOUCHER_FORM,IVoucherConstants.PRINTVOUCHER_CTRL_TXT_TICKETAMOUNT,form, composite);
						if( !validate ) {
							return;
						}					
						if( form.getTicketAmount() != null && form.getTicketAmount().trim().length() > 0 ) {
							/*DecimalFormat myFormatter = new DecimalFormat("0.00");
							form.setTicketAmount(myFormatter.format(Double.valueOf(form.getTicketAmount())));*/

							form.setTicketAmount(CurrencyUtil.getCurrencyFormat(Double.valueOf(form.getTicketAmount())));
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the form
	 */
	public PrintVoucherForm getForm() {
		return form;
	}
	
//	private void closePort() {
//		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
//		String printerModel = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL);
//		String printerManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER);
//		String printerDriver = preferenceStore.getString(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER);
//		
//		Printer printer = null;
//		try {
//			Class.forName(printerDriver);
//			printer = Printer.getPrinter(printerManufacturer, printerModel);					
//			printer.close();
//		} catch (PrinterException e) {
//			log.error("Exception while closing the Port. ", e);
//		} catch (ClassNotFoundException ex) {
//			log.error("Error occured while Class not found. ", ex);
//		}
//	}
	

}
