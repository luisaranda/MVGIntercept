/*****************************************************************************
 * $Id: OverrideUidPwdController.java,v 1.7, 2010-07-28 07:19:50Z, Verma, Nitin Kumar$
 * $Date: 7/28/2010 2:19:50 AM$
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.IMessageConstants;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.service.FrameworkServiceLocator;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.LoginInfoDTO;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.ErrorCodeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.OverrideUidPwdComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.OverrideAuthForm;
import com.ballydev.sds.voucherui.form.OverrideForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.EpsonWithLPTPort;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.print.VoucherImage;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.DefaultScreen;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This class is used to authorize the Approver(override user) credentials.
 * @author VNitinkumar
 */
public class OverrideUidPwdController extends SDSBaseController {

	/**
	 * Instance of OverrideUidPwdComposite
	 */
	private OverrideUidPwdComposite composite;
	
	/**
	 * Instance of OverrideAuthForm
	 */
	private OverrideAuthForm authForm = null;

	/**
	 * Instance of Composite where default screen will populate
	 */
	private Composite middleComposite = null;
	
	/**
	 * Instance of TicketInfoDTO List
	 */
	private List<TicketInfoDTO> tktInfoDTOLst = null;
	
	/**
	 * Instance of OverrideForm
	 */
	private OverrideForm overrideForm = null;
	
	/**
	 * Instance of OverrideController
	 */
	private OverrideController controller = null;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	
	/**
	 * Constructor of OverrideUidPwdController
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @param controller
	 * @throws Exception
	 */
	public OverrideUidPwdController(Composite parent, int style, OverrideAuthForm form, SDSValidator validator
			, OverrideController controller) throws Exception {
		super(form, validator);
		this.tktInfoDTOLst = controller.getTktInfoDTOList();
		this.overrideForm = controller.getOverrideForm();
		this.controller = controller;
		middleComposite = parent;
		composite = new OverrideUidPwdComposite(parent,style);
		this.authForm = form;
		super.registerEvents(composite);
		composite.getTxtReasonForOverride().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_OVERRIDE_REASON));
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(composite);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if( (Control) e.widget instanceof SDSImageLabel  || (Control) e.widget instanceof SDSTSLabel ) {
			try {
				authControllerAction(e.getSource());
			} catch(Exception e1) {
				log.error(e1);
			}
		}
	}

	@SuppressWarnings("static-access")
	private void authControllerAction(Object source) {
		Control control = (Control) source;
		try {
			if( !((Control)source instanceof SDSImageLabel) && !((Control) source instanceof SDSTSLabel) ) {
				return;
			}

			if( ((Control) source instanceof SDSTSLabel) ) {
				if( ((SDSTSLabel) control).getName().equalsIgnoreCase("keyboard")) {
					try {
						Shell keyBoardShell = new Shell();
						Rectangle screenBounds;
						Rectangle keyboardBounds;
						keyBoardShell = KeyBoardUtil.createKeyBoard();
						screenBounds = middleComposite.getBounds();
						keyboardBounds = keyBoardShell.getBounds();
						if( Util.isSmallerResolution() )
							keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height	- keyboardBounds.height + 383);
						else
							keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height	- keyboardBounds.height + 763);
						composite.getTxtUserId().setFocus();						

					} catch (Exception e1) {
						e1.printStackTrace();
					}				
				}
			}			
			if( ((Control) source instanceof SDSImageLabel) ) {
				if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_CANCEL)) {
					try {
						composite.getParent().dispose();
					} catch(Exception ex)	{
						ex.printStackTrace();
					}
				}
				if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_SUBMIT) ) {
					if( !((Control)source instanceof SDSImageLabel) ) {
						return;
					}
					populateForm(composite);
					boolean isValidate = validate(IVoucherConstants.OVERRIDE_AUTH_FORM, authForm, composite);
					if( !isValidate ) {
						return;
					}

					ProgressIndicatorUtil.openInProgressWindow();
					UserDTO userDTO;
					try {
						if( VoucherStringUtil.lPadWithZero(authForm.getUserId().trim(),5).equalsIgnoreCase(VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID().trim(),5)) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_OVERRIDE_SAME_USER));
							composite.getTxtUserId().setFocus();
							composite.getTxtUserId().selectAll();
							return;
						}
						userDTO = FrameworkServiceLocator.getService().authenticateUser(authForm.getUserId(), authForm.getPassword(),(int) SDSApplication.getUserDetails().getSiteId());						
						if( userDTO == null ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							composite.getTxtUserId().setFocus();
							composite.getTxtUserId().selectAll();
							return;
						}	
						if( userDTO != null && userDTO.isErrorPresent() ) {
							if( userDTO.getMessageKey() != null ) {
								if( IMessageConstants.ANA_MESSAGES_INVALID_PASSWORD.equals(userDTO.getMessageKey()) ) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.USER_PWD_WRONG));
									composite.getTxtPassword().setFocus();
									composite.getTxtPassword().selectAll();
									return;
								}
							}
						}
						if( !VoucherUtil.isValidUserForThisFucntion(userDTO, IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.USER_NO_PERMISSION));
							return;
						} else if( !userDTO.isErrorPresent() ) {
							
							SessionUtility sessionUtility = new SessionUtility();
							int siteId = Integer.valueOf(sessionUtility.getSiteDetails().getNumber());
							boolean printOption   = false;
							boolean printerOption = false;
							LoginInfoDTO overrideCredentials = new LoginInfoDTO();
							List<TicketInfoDTO> tdDTOList = new ArrayList<TicketInfoDTO>();
							long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());

							for( TicketInfoDTO tdto : tktInfoDTOLst ) {
								tdto.setSessionId(getCurrentSessionId);
								overrideCredentials.setManagerId(composite.getTxtUserId().getText());
								overrideCredentials.setPassword(composite.getTxtPassword().getText());
								tdto.setOverrideCredentials(overrideCredentials);
								tdDTOList.add(tdto);
//								System.out.println("Barcode is before DB operation: " + tdto.getBarcode() 
//										+ "\n" + "Override User Name before DB operation: " + tdto.getOverrideUsername() 
//										+ "\n" + "Amount is before DB operation: " + tdto.getAmount()
//										+ "\n" + "Status is before DB operation: " + tdto.getStatus());
							}

							List<TicketInfoDTO> authorisedTktDTOList = ServiceCall.getInstance().overrideVouchers(tdDTOList, siteId);
							clearAllValues();

//							for( TicketInfoDTO tdto : authorisedTktDTOList ) {
//								System.out.println("Barcode is after DB operation: " + tdto.getBarcode() 
//										+ "\n" + "Override User Name after DB operation: " + tdto.getOverrideUsername() 
//										+ "\n" + "Amount is after DB operation: " + tdto.getAmount()
//										+ "\n" + "Status is after DB operation: " + tdto.getStatus());
//							}

							controller.totalAmount = 0l;
							controller.getOverrideComposite().getLblTotalTktAmount().setText("Total Amount:  ");
							controller.loadTicketDetailFromResponse();

							int redeemedCount = 0;
							
							for( TicketInfoDTO td : authorisedTktDTOList ) {
								if( td.getErrorCodeId() == ErrorCodeEnum.SUCCESS.getErrorCode() )
									redeemedCount = redeemedCount + 1;
							}

							printOption = MessageDialogUtil.displayTouchScreenQuestionDialogOnActiveShell(redeemedCount + " " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_REDEEMED_MESSAGE) );

							if( printOption ) {
								printerOption = MessageDialogUtil.displayTouchScreenQuestionDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_OVERRIDE_LOGS_PRINTER_SELE_MSG));
							}
							if( !printerOption && printOption ) {
								printReceipt(authorisedTktDTOList, SDSApplication.getUserDetails().getFirstName() + " " + SDSApplication.getUserDetails().getLastName(), false);
							} else if( printerOption && printOption ) {
								printReceipt(authorisedTktDTOList, SDSApplication.getUserDetails().getFirstName() + " " + SDSApplication.getUserDetails().getLastName(), true);
							}
							
							if(Util.isSmallerResolution()) {
								controller.getHeaderComp().getLblOverrideVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
							} else {
								controller.getHeaderComp().getLblOverrideVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG)));
							}
							
							new DefaultScreen().openDefaultScreen(controller.getMiddleComp(), controller.getHeaderComp());
						}
					} catch(VoucherEngineServiceException ex) {
						log.error("Exception while redeeming the ticket with override functionality.", ex);
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					} catch(Exception e1) {
						e1.printStackTrace();
						log.error("Exception occured while checking for the employee existence.", e1);
					} finally {
						ProgressIndicatorUtil.closeInProgressWindow();
					}

					try	{
						composite.getParent().dispose();
					}
					catch( Exception ex ) {
						log.error("Error occurred in OverrideAuthController.widgetSelected", ex);
					}
				}
			}
		} catch(Exception ex) {
			log.error("Error occurred in OverrideAuthController.widgetSelected", ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}
	
	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		OverrideUidPwdComposite overrideUidPwdComposite = ((OverrideUidPwdComposite)argComposite);
		
		overrideUidPwdComposite.getILblSubmit().addMouseListener(this);
		overrideUidPwdComposite.getILblSubmit().getTextLabel().addMouseListener(this);
		
		overrideUidPwdComposite.getILblCancel().addMouseListener(this);
		overrideUidPwdComposite.getILblCancel().getTextLabel().addMouseListener(this);
	}

	/** This method clears all the values in the screen */
	private void clearAllValues() {
		overrideForm.setBarCode("");
		overrideForm.setAmount("");
		overrideForm.setAssetNumber("");
		overrideForm.setPlayerId("");
		overrideForm.setLstTktInfoDTO(new ArrayList<TicketInfoDTO>());
		overrideForm.setTicketListDTOSelectedValue(null);
		tktInfoDTOLst.clear();
	}
	
	/**
	 * This method will send voucher override logs detail to Printer to print. 
	 * @param ticketInfoDTOList
	 * @param cashierName
	 * @param defaultPrinter
	 */
	public void printReceipt(List<TicketInfoDTO> ticketInfoDTOList, String cashierName, boolean defaultPrinter) {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isReceiptPrinting = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_RECEIPT_PRINTING);
		boolean printerError = false;
		String printerErrorMessage = "";
		VoucherImage img = new VoucherImage();
		String printerType = null;
		List<TicketDetails> details = img.copyObject(ticketInfoDTOList, cashierName);
		if( isReceiptPrinting ) {
			try {
				String printerPort = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT);
				printerType = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE);
				log.debug("Printer port: " + printerPort);

				ProgressIndicatorUtil.openInProgressWindow();
				
				if( (printerPort).equals(IVoucherConstants.VOU_PREF_TKT_PRINT_LPT1) ) {
					EpsonWithLPTPort epsonWithLPTPort = new EpsonWithLPTPort();
					epsonWithLPTPort.printCustomerReceipt(details, SDSApplication.getLoggedInUserID(), printerType, cashierName, defaultPrinter);
				}
			} catch (PrinterNoSuchDriverException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN_NO_DRIVER) + e1.getMessage();
			} catch (PrinterException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = e1.getMessage();
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error("Error occured while printing the recepit for Override voucher logs: ", e1);
				if( !e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT) ) {
					printerError = true;
					printerErrorMessage = e1.getMessage();
				}
			} finally {
				ProgressIndicatorUtil.closeInProgressWindow();
			}
			if( printerError && !defaultPrinter ) {
				if( Util.isEmpty(printerErrorMessage) ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN));
				} else {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
				}
			} else if( !defaultPrinter ) {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECEIPT_PRINT_CONNECTION_SUCESS));
			}
		} else {
			try {
				EpsonWithLPTPort epsonWithLPTPort = new EpsonWithLPTPort();
				epsonWithLPTPort.printCustomerReceipt(details, SDSApplication.getLoggedInUserID(), printerType, cashierName, defaultPrinter);
			} catch (PrinterNoSuchDriverException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN_NO_DRIVER) + e1.getMessage();
			} catch (PrinterException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = e1.getMessage();
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error("Error occured while printing the recepit for Override voucher logs: ", e1);
				if( !e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT) ) {
					printerError = true;
					printerErrorMessage = e1.getMessage();
				}
			} finally {
				ProgressIndicatorUtil.closeInProgressWindow();
			}

		}
	}
}
