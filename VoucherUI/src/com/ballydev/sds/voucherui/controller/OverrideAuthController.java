/*****************************************************************************
 * $Id: OverrideAuthController.java,v 1.34.4.0, 2013-10-25 17:50:06Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 12:50:06 PM$
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.IMessageConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
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
import com.ballydev.sds.voucher.dto.TransactionReasonInfoDTO;
import com.ballydev.sds.voucher.dto.TxnReasonsInfoDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TransactionReasonEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.OverrideAuthComposite;
import com.ballydev.sds.voucherui.composite.RedeemVoucherComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.OverrideAuthForm;
import com.ballydev.sds.voucherui.form.RedeemVoucherForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;


/**
 * Controller class of the Parent Composite
 * @author VNitinkumar
 */
public class OverrideAuthController extends SDSBaseController {

	private OverrideAuthComposite composite;

	private OverrideAuthForm form = null;

	private RedeemVoucherController redeemVoucherController;

	private TicketInfoDTO updatedTicketInfoDTO;
	
	private Image disableImage;
	
	private boolean Multiredeem;
	
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
	@SuppressWarnings("unchecked")
	public OverrideAuthController(Composite parent,int style,OverrideAuthForm form, SDSValidator validator,
			RedeemVoucherController redeemVoucherController) throws Exception {
		
		super(form, validator);	
		middleComposite = parent;
		composite = new OverrideAuthComposite(parent,style);

		this.form = form;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		this.redeemVoucherController = redeemVoucherController;
		RedeemVoucherComposite rvc = (RedeemVoucherComposite)redeemVoucherController.getComposite();
		disableImage = rvc.getButtonDisableImage();
//		composite.getLblKeyBoard().addMouseListener(this);
		TxnReasonsInfoDTO txnReasonsForm = null;
		try {
			txnReasonsForm = ServiceCall.getInstance().getTransactionReasons();
			List reasonObjList = txnReasonsForm.getTransactionReasonDTOList();
			List reasonList = new ArrayList();
			for( int i = 0; i < reasonObjList.size(); i++ ) {
				TransactionReasonInfoDTO transForm = (TransactionReasonInfoDTO)reasonObjList.get(i);
				reasonList.add(LabelLoader.getLabelValue(transForm.getDescription()));
			}
			form.setReasonList(reasonList);
			populateScreen(composite);
		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while getting the transaction reasons", ex);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
		registerCustomizedListeners(composite);
	}
	
	public OverrideAuthController(Composite parent,int style,OverrideAuthForm form, SDSValidator validator,
			RedeemVoucherController redeemVoucherController,boolean multiredeem) throws Exception {
		
		super(form, validator);	
		middleComposite = parent;
		composite = new OverrideAuthComposite(parent,style);

		this.form = form;
		this.Multiredeem=multiredeem;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		this.redeemVoucherController = redeemVoucherController;
		RedeemVoucherComposite rvc = (RedeemVoucherComposite)redeemVoucherController.getComposite();
		disableImage = rvc.getButtonDisableImage();
//		composite.getLblKeyBoard().addMouseListener(this);
		TxnReasonsInfoDTO txnReasonsForm = null;
		try {
			txnReasonsForm = ServiceCall.getInstance().getTransactionReasons();
			List reasonObjList = txnReasonsForm.getTransactionReasonDTOList();
			List reasonList = new ArrayList();
			for( int i = 0; i < reasonObjList.size(); i++ ) {
				TransactionReasonInfoDTO transForm = (TransactionReasonInfoDTO)reasonObjList.get(i);
				reasonList.add(LabelLoader.getLabelValue(transForm.getDescription()));
			}
			form.setReasonList(reasonList);
			populateScreen(composite);
		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while getting the transaction reasons", ex);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
		registerCustomizedListeners(composite);
	}
	
	
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
					boolean isValidate = validate(IVoucherConstants.OVERRIDE_AUTH_FORM,form,composite);
					if( !isValidate ) {
						return;
					}

					ProgressIndicatorUtil.openInProgressWindow();
					UserDTO userDTO;
					try {
						if( VoucherStringUtil.lPadWithZero(form.getUserId().trim(),5).equalsIgnoreCase(VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID().trim(),5)) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_OVERRIDE_SAME_USER));
							composite.getTxtUserId().setFocus();
							composite.getTxtUserId().selectAll();
							return;
						}
						userDTO = FrameworkServiceLocator.getService().authenticateUser(form.getUserId(), form.getPassword(),(int) SDSApplication.getUserDetails().getSiteId());						
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
						if( !VoucherUtil.isValidUserForThisFucntion(userDTO,IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.USER_NO_PERMISSION));
							return;
						}
					} catch(Exception e1) {
						log.error("Exception occured while checking for the employee existence", e1);
					} finally {
						ProgressIndicatorUtil.closeInProgressWindow();
					}

					try	{
						composite.getParent().dispose();
					}
					catch(Exception ex) {
						log.error("Error occurred in OverrideAuthController.widgetSelected", ex);
					}

					RedeemVoucherForm redeemForm = redeemVoucherController.getForm();

					long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());

					TicketInfoDTO ticketInfo = new TicketInfoDTO();
					ticketInfo.setSessionId(getCurrentSessionId);
					ticketInfo.setPlayerId(redeemForm.getPlayerId());
					ticketInfo.setBarcode(redeemForm.getBarCode());
					ticketInfo.setTicketType(TicketTypeEnum.NON_SLOT_GENERATED);
					ticketInfo.setGameBarcode(false);
					ticketInfo.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
					ticketInfo.setEmployeeId(SDSApplication.getLoggedInUserID());
					ticketInfo.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
					ticketInfo.setOptimisticLockVersion(redeemForm.getOptLockVersion());
					ticketInfo.setUserRoleTypeId(VoucherUtil.getUserRole());

					LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
					loginInfoDTO.setManagerId(form.getUserId());
					loginInfoDTO.setPassword(form.getPassword());
					ticketInfo.setOverrideCredentials(loginInfoDTO);
					if( form.getSelectedReasonValue().equalsIgnoreCase(TransactionReasonEnum.TEMP_REASON.getTransactionReason()) ) {
						ticketInfo.setTransactionReason(-1);
					} else {
						ticketInfo.setTransactionReason(form.getReasonList().indexOf(form.getSelectedReasonValue()));
					}
//					SessionUtility utility = new SessionUtility();
//					String datePattern = utility.getApplicationDateFormat();

					try {
						updatedTicketInfoDTO =  ServiceCall.getInstance().overrideRedeemVoucher(ticketInfo);
					} catch(VoucherEngineServiceException ex) {
						log.error("Exception while redeeming the Voucher using override option", ex);
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					}

					if( updatedTicketInfoDTO.getErrorCodeId() == 15 || updatedTicketInfoDTO.getErrorCodeId() == 16 ) {
//						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(updatedTicketInfoDTO.getErrorCodeDesc()));
					}
					
					else if( updatedTicketInfoDTO!=null ) {
						
						if( updatedTicketInfoDTO.getErrorCodeId() == 17 ) {
//							MessageDialogUtil.displayTouchScreenInfoDialog(updatedTicketInfoDTO.getErrorCodeDesc());
						}
												
						if( updatedTicketInfoDTO.isErrorPresent()){
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
						}
						else {
							//MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
//							redeemForm.setBarCode(updatedTicketInfoDTO.getBarcode());
//							redeemForm.setState(updatedTicketInfoDTO.getTktStatus());
//							redeemForm.setAmount(ConversionUtil.voucherAmountFormat(new Double(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())).toString()));
//							String eff = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getEffectiveDate().getTime()).toString();
//							String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT,datePattern);
//							redeemForm.setEffectiveDate(effectiveDate);
//							String exp = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getExpireDate().getTime()).toString();
//							String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT,datePattern);
//							redeemForm.setExpireDate(expireDate);
//							redeemForm.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
							
							//Fixed CR# 89327 -- After successful redeem voucher, Voucher Redemption screen should be empty as default screen.
							RedeemVoucherComposite redeemVoucherComposite = (RedeemVoucherComposite)redeemVoucherController.getComposite();
							if( updatedTicketInfoDTO.getTktStatus().equals("VOU.LKP.STAT.REDEEMED") ) {
								redeemForm.setBarCode("");
								redeemForm.setState("");
								redeemForm.setAmount("");
								redeemForm.setEffectiveDate("");
								redeemForm.setExpireDate("");
								redeemForm.setLocation("");
								redeemForm.setCreatedPlayerCardId("");
								redeemForm.setPlayerCardRequired(false);
								redeemForm.setPlayerId("");
								redeemForm.setTicketSeed("");
								redeemVoucherComposite.getTxtBarCode().setText("");
								redeemVoucherComposite.getTxtPlayerId().setText("");
							}

							if( redeemVoucherController.multiRedeemController != null && !redeemVoucherController.multiRedeemController.getComposite().isDisposed()) {
								redeemVoucherController.setMultiRedeemAmount(this.redeemVoucherController.multiRedeemAmount+new Double(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())).doubleValue());
								redeemVoucherController.setMultiRedeemCount(this.redeemVoucherController.multiRedeemCount+1);
						
								String amtTotal = ConversionUtil.voucherAmountFormat(new Double(redeemVoucherController.getMultiRedeemAmount()).toString());
								redeemVoucherController.multiRedeemController.getForm().setAmountTotal(amtTotal);
								redeemVoucherController.multiRedeemController.getForm().setVoucherCoupon(new Integer(redeemVoucherController.getMultiRedeemCount()).toString());
								redeemVoucherController.multiRedeemAmount = redeemVoucherController.getMultiRedeemAmount();
								redeemVoucherController.multiRedeemCount = redeemVoucherController.getMultiRedeemCount();
							}
							
							redeemVoucherComposite.getLblRedeem().setEnabled(false);
							redeemVoucherComposite.getLblRedeem().setImage(disableImage);
							redeemVoucherComposite.getLblRedeem().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
							redeemVoucherComposite.getLblOverride().setEnabled(false);
							redeemVoucherComposite.getLblOverride().setImage(disableImage);
							redeemVoucherComposite.getLblOverride().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
							redeemVoucherComposite.getLblCancel().setEnabled(false);
							redeemVoucherComposite.getLblCancel().setImage(disableImage);
							redeemVoucherComposite.getLblCancel().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());

							Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_REDEEM_VOUCHER), 
									LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE), "", VoucherUtil.formatBarcode(updatedTicketInfoDTO.getBarcode(), 
											(updatedTicketInfoDTO.getTicketType() == null 
									? null : updatedTicketInfoDTO.getTicketType().getTicketType()), updatedTicketInfoDTO.getTktStatusId()), 
												LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_REDEEMED), 
												EnumOperation.MODIFY_OPERATION, PreferencesUtil.getClientAssetNumber());
							try {
								/**
								 * SEND MSG TO EVS IF A CASHABLE PROMO
								 * TKT IS REDEEMED FROM THE CASHIER
								 * WORKSTATION*
								 */
								if( updatedTicketInfoDTO.getAmountType().equals(AmountTypeEnum.CASHABLE_PROMO) ) {
									SessionUtility sessionUtility = new SessionUtility();
									updatedTicketInfoDTO.getUserFormDTO().setSiteId(sessionUtility.getSiteDetails().getId());
									ServiceCall.getInstance().sendUpdateRedeemRequest(updatedTicketInfoDTO);
								}
							} catch (Exception e1) {
								log.error("Exception occured while sending the message to evs",e1);
							}
							redeemVoucherController.displayMessage();
							redeemVoucherController.printReceipt(updatedTicketInfoDTO);
						}
					}
				}
			}
		} catch(Exception ex) {
			log.error("Error occurred in OverrideAuthController.widgetSelected", ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}

	// Moved the code from Mouse down to a new method authControllerAction(), so that key press can also perform the same action.
	// This is to fix CR - 7779

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

	@Override
	public Composite getComposite() {
		return composite;
	}


	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		OverrideAuthComposite overrideAuthComposite = ((OverrideAuthComposite)argComposite);
		
		overrideAuthComposite.getILblSubmit().addMouseListener(this);
		overrideAuthComposite.getILblSubmit().getTextLabel().addMouseListener(this);
		
		overrideAuthComposite.getILblCancel().addMouseListener(this);
		overrideAuthComposite.getILblCancel().getTextLabel().addMouseListener(this);
//		overrideAuthComposite.getLblKeyBoard().addMouseListener(this);
	}

	public TicketInfoDTO getUpdatedTicketForm() {
		return updatedTicketInfoDTO;
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e) ;
		//composite.redraw();
		if( e.getSource() instanceof SDSTSText )
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		authControllerAction(e.getSource());
	}
	
}
