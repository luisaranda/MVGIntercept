/*****************************************************************************
 * $Id: OverridePrintController.java,v 1.7, 2011-03-21 11:29:10Z, SDS Check in user$
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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.IMessageConstants;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.service.FrameworkServiceLocator;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.LoginInfoDTO;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.OverridePrintComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.OverridePrintForm;
import com.ballydev.sds.voucherui.form.PrintVoucherForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.print.VoucherImage;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;


/**
 * Controller class of the Parent Composite
 * 
 */
public class OverridePrintController extends SDSBaseController {

	private OverridePrintComposite composite;

	private OverridePrintForm form = null;


	private PrintVoucherController redeemVoucherController;

	private TicketInfoDTO updatedTicketInfoDTO;

	/**
	 * Logger Instance
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	
	private Composite middleComposite = null;
	/**
	 * BaseController constructor
	 * @param parent
	 * @param style
	 * @throws Exception
	 **/
	public OverridePrintController(Composite parent,int style,OverridePrintForm form, SDSValidator validator,PrintVoucherController redeemVoucherController) throws Exception 
	{
		super(form, validator);	
		middleComposite = parent;
		composite = new OverridePrintComposite(parent,style);
		this.form = form;
		super.registerEvents(composite);
		this.registerCustomizedListeners(composite);
		form.addPropertyChangeListener(this);
		this.redeemVoucherController = redeemVoucherController;
		populateScreen(composite);	

	}

	@Override
	public void mouseDown(MouseEvent e){
		Control control = (Control) e.getSource();
		try {
			if(!((Control)e.getSource() instanceof SDSImageLabel)&& !((Control) e.getSource() instanceof SDSTSLabel)){
				return;
			}

			if(((Control) e.getSource() instanceof SDSTSLabel)){
				if (((SDSTSLabel) control).getName().equalsIgnoreCase("keyboard"))	{
					try {
						Shell keyBoardShell = new Shell();
						Rectangle screenBounds;
						Rectangle keyboardBounds;
						keyBoardShell = KeyBoardUtil.createKeyBoard();
						screenBounds = middleComposite.getBounds();
						keyboardBounds = keyBoardShell.getBounds();
						if( Util.isSmallerResolution() )
							keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height	- keyboardBounds.height + 403);
						else
							keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height	- keyboardBounds.height + 763);
						composite.getTxtUserId().setFocus();						

					} 
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}			
			if(((Control) e.getSource() instanceof SDSImageLabel)){
				if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_CANCEL))	{
					try {
						composite.getParent().dispose();
					}
					catch(Exception ex)	{
						ex.printStackTrace();
					}
				}
				if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_SUBMIT)){
					if(!((Control)e.getSource() instanceof SDSImageLabel)){
						return;
					}
					populateForm(composite);				
					boolean isValidate = validate(IVoucherConstants.OVERRIDE_PRINT_FORM,form,composite);				
					if(!isValidate){
						return;
					}


					ProgressIndicatorUtil.openInProgressWindow();
					UserDTO userDTO;
					try {
						if(VoucherStringUtil.lPadWithZero(form.getUserId().trim(),5).equalsIgnoreCase(VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID().trim(),5))) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_OVERRIDE_SAME_USER));
							composite.getTxtUserId().setFocus();
							composite.getTxtUserId().selectAll();
							return;
						}
						userDTO = FrameworkServiceLocator.getService().authenticateUser(form.getUserId(), form.getPassword(),(int) SDSApplication.getUserDetails().getSiteId());						
						if(userDTO == null ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							composite.getTxtUserId().setFocus();
							composite.getTxtUserId().selectAll();
							return;
						}	
						if (userDTO != null && userDTO.isErrorPresent()){
							if (userDTO.getMessageKey() != null){
								if(IMessageConstants.ANA_MESSAGES_INVALID_PASSWORD.equals(userDTO.getMessageKey())){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.USER_PWD_WRONG));
									composite.getTxtPassword().setFocus();
									composite.getTxtPassword().selectAll();
									return;
								}
							}
						}
						if(!VoucherUtil.isValidUserForThisFucntion(userDTO,IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE)) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.USER_NO_PERMISSION));	
							return;
						}
					} catch (Exception e1) {
						log.error("Exception occured while checking for the employee existence", e1);
					}finally{
						ProgressIndicatorUtil.closeInProgressWindow();
					}




					try	{
						composite.getParent().dispose();
					}
					catch(Exception ex){
						log.error("Error occurred in OverrideAuthController.widgetSelected", ex);
					}

					PrintVoucherForm redeemForm = redeemVoucherController.getForm();	
					boolean isErrorPresent = false;

					TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();				
					ticketInfoDTO.setAmount(new Long(ConversionUtil.dollarToCents(redeemForm.getTicketAmount())));
					ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());				
					ticketInfoDTO.setTicketType(TicketTypeEnum.NON_SLOT_GENERATED);
					ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
					ticketInfoDTO.setAmountType(AmountTypeEnum.CASHABLE);				
					ticketInfoDTO.setEffectiveDate(DateUtil.getCurrentServerDate());				
					ticketInfoDTO.setExpireDate(SiteUtil.getNoOfDaysToExpireTkt());
					ticketInfoDTO.setPlayerCardReqd(IVoucherConstants.NO_PLAYER_REQD);
					ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());				
					ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
					ticketInfoDTO.setChkForEmpMaxAmt(false);
					int noOfTkts = 1;
					if(redeemForm.getTotalTktsToPrint()!=null){
						try {
							noOfTkts = Integer.parseInt(redeemForm.getTotalTktsToPrint());
							if(noOfTkts>SiteUtil.getNoOfTktsToPrint()){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_MAX_NO_PRINT_EXCEEDED,new String[] {AppContextValues.getInstance().getTicketText()}));
								redeemVoucherController.getPrintVoucherComposite().getTxtNoOfTkts().setFocus();
								return;
							}
						} catch (NumberFormatException e1) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_TKTS_PROPER_VALUE, new String[] {AppContextValues.getInstance().getTicketText()}));
							redeemVoucherController.getPrintVoucherComposite().getTxtNoOfTkts().setFocus();
							return;
						}
					}

					LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
					loginInfoDTO.setManagerId(form.getUserId());
					loginInfoDTO.setPassword(form.getPassword());

					List<TicketInfoDTO> createdTicketFormList =  ServiceCall.getInstance().createVoucherList(ticketInfoDTO,noOfTkts);				
					if(createdTicketFormList!=null && createdTicketFormList.get(0)!=null){
						if(createdTicketFormList.get(0).isErrorPresent())	{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(createdTicketFormList.get(0)));
							composite.getParent().dispose();
							redeemVoucherController.getPrintVoucherComposite().getTxtNoOfTkts().setFocus();
						}
						else{					
							String error = null;
							VoucherImage img = new VoucherImage();						
							StringBuffer bufBarcodesSucess = new StringBuffer();					
							for(int j=0;j<createdTicketFormList.size();j++){					
								if(createdTicketFormList.get(j).isErrorPresent()){
									isErrorPresent = true;
									error = VoucherUtil.getI18nMessageForDisplay(createdTicketFormList.get(j));							
								}else{
									redeemVoucherController.setAmountAndCount(createdTicketFormList.get(j));
									if(j==createdTicketFormList.size()-1){
										bufBarcodesSucess.append(createdTicketFormList.get(j).getBarcode());
									}else{
										bufBarcodesSucess.append(createdTicketFormList.get(j).getBarcode()+",");
									}
									Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PRINT_VOUCHER)+AppContextValues.getInstance().getTicketText() , LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE), "", VoucherUtil.formatBarcode(createdTicketFormList.get(j).getBarcode(), (createdTicketFormList.get(j).getTicketType() == null ? null : createdTicketFormList.get(j).getTicketType().getTicketType()), createdTicketFormList.get(j).getTktStatusId()), LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_ADDED)+redeemForm.getTicketAmount(), EnumOperation.ADD_OPERATION,PreferencesUtil.getClientAssetNumber());
								}
							}						
							try {									
								redeemForm.setCount("");
								redeemForm.setTicketAmount("");	
								redeemForm.setTotalTktsToPrint("");
								populateScreen(composite);
							} catch (Exception e1) {						
								e1.printStackTrace();
							}
							redeemVoucherController.getPrintVoucherComposite().getTxtTicketAmount().setFocus();
							List<TicketDetails> detailsList = img.copyObj(createdTicketFormList);
							boolean printerError = false;
							String printerErrorMessage = "";
							List<TicketInfoDTO> infoDTO = new ArrayList<TicketInfoDTO>(); 
							for(int i=0;i<detailsList.size();i++){
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
									log.debug("Printer model: "+printerModel);
									log.debug("Printer manuf: "+printerManufacturer);
									log.debug("Printer port: "+printerPort);
									log.debug("Printer driver: "+printerDriver);
									ProgressIndicatorUtil.openInProgressWindow();
									Printer printer = null;
									Class.forName(printerDriver);
									printer = Printer.getPrinter(printerManufacturer, printerModel);					
									printer.open(printerPort);
									printer.printReceipt(details, SDSApplication.getLoggedInUserID(), printerModel);								
								} catch (PrinterNoSuchDriverException e1) {
									e1.printStackTrace();
									printerError = true;
									printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_DRIVER)+e1.getMessage();
								} catch (ClassNotFoundException e1) {
									e1.printStackTrace();
									printerError = true;
									printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_EXPTN_NO_CLASS_DEF)+e1.getMessage();
								} catch (PrinterException e1) {
									e1.printStackTrace();
									printerError = true;
									printerErrorMessage = e1.getMessage();
								}catch (Exception e1) {
									e1.printStackTrace();
									log.error("Error occured while printing the ticket: ", e1);
									if(!e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT)){								
										printerError = true;	
										printerErrorMessage = e1.getMessage();
									}								
								}finally{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
								if(printerError) {
									if(SiteUtil.isTktVoidAllowed()) {								
										TicketInfoDTO ticketInfoDTO1 = new TicketInfoDTO();
										ticketInfoDTO1.setBarcode(details.getBarcode());
										ticketInfoDTO1.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
										ticketInfoDTO1.setEmployeeId(SDSApplication.getLoggedInUserID());
										ticketInfoDTO1.setTicketType(redeemVoucherController.getTicketType(details.getBarcode()));
										ticketInfoDTO1.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
										ticketInfoDTO1.setOptimisticLockVersion(redeemForm.getOptLockVersion());					
										ticketInfoDTO1.setUserRoleTypeId(VoucherUtil.getUserRole());
										infoDTO.add(ticketInfoDTO1);
									}
								}
							}if(printerError || isErrorPresent){
								if(SiteUtil.isTktVoidAllowed()) {	
									ServiceCall.getInstance().voidVoucherList(infoDTO);
								}else {
									MessageDialogUtil.displayTouchScreenInfoDialog(noOfTkts+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE_SUCCESS,new String[] {AppContextValues.getInstance().getTicketText()}));
								}
								if(Util.isEmpty(printerErrorMessage)){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINTER_CONNECTION_ERROR));
								}else{
									if(isErrorPresent) {
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(error);
									}else {
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
									}
								}							
							}else{
								MessageDialogUtil.displayTouchScreenInfoDialog(noOfTkts+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE_SUCCESS,new String[] {AppContextValues.getInstance().getTicketText()})+ "\n"+LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_PRINT_CONNECTION_SUCESS));
								redeemVoucherController.getPrintVoucherComposite().getTxtTicketAmount().setFocus();
							}					
						}
					}
				}
			}
		}
		catch(VoucherEngineServiceException ex)	{							
			log.error("VoucherEngineServiceException while creating the Voucher", ex);				
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
		}
		catch (Exception ex){								
			log.error("Exception while Creating a Voucher : ",ex);
			VoucherUIExceptionHandler.handleException(ex);			
		}
	}

	@Override
	public Composite getComposite() 
	{
		return composite;
	}

	public TicketInfoDTO getUpdatedTicketForm()
	{
		return updatedTicketInfoDTO;
	}

	@Override
	public void focusGained(FocusEvent e) 
	{
		super.focusGained(e) ;
		//composite.redraw();
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		OverridePrintComposite overridePrintComposite = ((OverridePrintComposite)argComposite);
		
		overridePrintComposite.getILblSubmit().addMouseListener(this);
		overridePrintComposite.getILblSubmit().getTextLabel().addMouseListener(this);
		
		overridePrintComposite.getILblCancel().addMouseListener(this);
		overridePrintComposite.getILblCancel().getTextLabel().addMouseListener(this);
		overridePrintComposite.getLblKeyBoard().addMouseListener(this);
	}

}
