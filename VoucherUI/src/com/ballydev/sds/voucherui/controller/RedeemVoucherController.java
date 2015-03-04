/*****************************************************************************
 * $Id: RedeemVoucherController.java,v 1.135.1.2.1.0, 2013-10-28 18:29:05Z, Sornam, Ramanathan$
 * $Date: 10/28/2013 12:29:05 PM$
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

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.ares.Ares;
import com.ballydev.sds.framework.ares.AresBarcodeEvent;
import com.ballydev.sds.framework.ares.AresErrorEvent;
import com.ballydev.sds.framework.ares.AresEvent;
import com.ballydev.sds.framework.ares.AresEventListener;
import com.ballydev.sds.framework.ares.AresNoReadEvent;
import com.ballydev.sds.framework.ares.AresStatusEvent;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSKeyPadButton;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.dto.TransactionDetailsDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.enumconstants.ErrorCodeEnum;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TransactionReasonErrorEnum;
import com.ballydev.sds.voucher.enumconstants.TransactionTypeErrorEnum;
import com.ballydev.sds.voucher.enumconstants.VoucherStatusEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucher.util.Message;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.MultiRedeemComposite;
import com.ballydev.sds.voucherui.composite.RedeemVoucherComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.composite.VoucherTSInputDialog;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.displays.Display;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.MultiRedeemForm;
import com.ballydev.sds.voucherui.form.RedeemVoucherForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.EpsonWithLPTPort;
import com.ballydev.sds.voucherui.print.Printer;
import com.ballydev.sds.voucherui.print.PrinterException;
import com.ballydev.sds.voucherui.print.PrinterNoSuchDriverException;
import com.ballydev.sds.voucherui.print.VoucherImage;
import com.ballydev.sds.voucherui.service.IVoucherService;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.service.VoucherServiceLocator;
import com.ballydev.sds.voucherui.shell.DialogShell;
import com.ballydev.sds.voucherui.util.AresObject;
import com.ballydev.sds.voucherui.util.AresUtil;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.ObjectMapping;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUIValidator;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This is the class used to control the redeem operation from Voucher UI.
 * @author VNitinkumar
 */
public class RedeemVoucherController extends VoucherBaseController implements ModifyListener {

	private RedeemVoucherComposite composite = null;

	private RedeemVoucherForm form = null;

	private boolean isVoid = false;

	private TicketInfoDTO ticketInfoDTO = null;

	public int multiRedeemCount = 0;

	public double multiRedeemAmount = 0;

	public MultiRedeemController multiRedeemController;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private String employeeId = null;

	private UserDTO userDTO = null;

	private boolean isVoucherPendingAtDiffLoc = false;

	/** Instance of Ares */
	private static Ares ares;

	/** This variable is to track whether the modify text event is called */
	private boolean modifyTextCalled = false;

	private String barcodeBackup = null;

	/** Player card id entered */
	private String playerCardId = null;

	private TicketInfoDTO tktInfoDTOFromPopulate = null;

	private Image enableImage;
	
	private Image disableImage;
	
	TicketInfoDTO tktInfoDTOChk = null;
	
	boolean multiredeem=false;
	
	/**
	 * RedeemVoucherController constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public RedeemVoucherController(Composite parent, int style,RedeemVoucherForm form, SDSValidator validator, boolean isVoid) throws Exception {
		super(form, validator);
		this.form = form;
		this.isVoid = isVoid;
		createComposite(parent, style);
		setScanner();
	}

	/**
	 * This method creates the composite according to the isVoid variable value
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	private void createComposite(Composite parent, int style) throws Exception {
		composite = new RedeemVoucherComposite(parent, style, false, isVoid);
		VoucherMiddleComposite.setCurrentComposite(composite);
		form.addPropertyChangeListener(this);
		composite.getTxtBarCode().setFocus();

		enableImage = composite.getButtonImage();
		disableImage = composite.getButtonDisableImage();
		
		if( isVoid ) {
			disableVoid();
		} else {
			disableRedeem();
			enableMultiRedeem();
			disableOverride();
			disableCancel();
		}

		parent.layout();
		userDTO = SDSApplication.getUserDetails();
		employeeId = SDSApplication.getLoggedInUserID();
		registerCustomizedListeners(composite);
		super.registerEvents(composite);
	}
	
	// Method to set scanner event for the Barcode text box, only single time for the particular view.
	private void setScanner() {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isBarcodeScannerEnbaled = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER);
		
		if( isBarcodeScannerEnbaled ) {
			ares = AresUtil.registerAresEvent(AresObject.getAres());
			if( ares != null ) {
				
				ares.addAresEventListener(new AresEventListener() {
					public void aresEvent(AresEvent ae) {
						try {
							if( composite != null && !composite.isDisposed() && ae instanceof AresBarcodeEvent ) {
								String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
								final String barcode = ((AresBarcodeEvent) ae).getBarcode();
								if( barcode != null && barcode.length() == 18 
										|| (barcode != null && stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) 
												&& barcode.length() == 16 )) {
									log.debug("Barcode: " + barcode);
									ares.accept();
									composite.getDisplay().syncExec(new Runnable() {
										public void run() {
											form.setBarCode(barcode);
											if( composite.getTxtBarCode().isFocusControl() ) {
												composite.getTxtBarCode().setText(barcode);
											}
										}
									});
								} else {
									log.error("Invalid barcode length");
									ares.reject();
								}
							} else if( ae instanceof AresErrorEvent ) {
								log.debug("Ares ErrorEvent");

							} else if( ae instanceof AresNoReadEvent ) {
								log.debug("No Read");
								ares.reject();
							} else if( ae instanceof AresStatusEvent ) {
								AresStatusEvent status = (AresStatusEvent) ae;
								switch ( status.getStatusType() ) {
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
						} catch ( Exception e ) {
							log.error(e);
							e.printStackTrace();
						}
					}
				});
			}	
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.voucherui.controller.VoucherBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		RedeemVoucherComposite redeemVoucherComposite = ((RedeemVoucherComposite)argComposite);
		
		if( isVoid ) {
			redeemVoucherComposite.getLblSubmitBarcode().addMouseListener(this);
			redeemVoucherComposite.getLblSubmitBarcode().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblSubmitBarcode().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblCancel().addMouseListener(this);
			redeemVoucherComposite.getLblCancel().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblCancel().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblVoid().addMouseListener(this);
			redeemVoucherComposite.getLblVoid().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblVoid().getTextLabel().addTraverseListener(this);

		} else {
			redeemVoucherComposite.getLblSubmitBarcode().addMouseListener(this);
			redeemVoucherComposite.getLblSubmitBarcode().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblSubmitBarcode().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblRedeem().addMouseListener(this);
			redeemVoucherComposite.getLblRedeem().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblRedeem().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblOverride().addMouseListener(this);
			redeemVoucherComposite.getLblOverride().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblOverride().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblMultiRedeem().addMouseListener(this);
			redeemVoucherComposite.getLblMultiRedeem().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblMultiRedeem().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblCancelProcess().addMouseListener(this);
			redeemVoucherComposite.getLblCancelProcess().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblCancelProcess().getTextLabel().addTraverseListener(this);
			redeemVoucherComposite.getLblCancel().addMouseListener(this);
			redeemVoucherComposite.getLblCancel().getTextLabel().addMouseListener(this);
			redeemVoucherComposite.getLblCancel().getTextLabel().addTraverseListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		boolean redeemSiteEnabled = true;
		
		SessionUtility utility = new SessionUtility();
		String datePattern = utility.getApplicationDateFormat();
//		super.widgetSelected(e);
		Control control = (Control) e.getSource();
		try {
			if( !((Control) e.getSource() instanceof SDSImageLabel || (Control) e.getSource() instanceof SDSTSKeyPadButton) ) {
				return;
			}
			populateForm(composite);
			isVoucherPendingAtDiffLoc = false;
			if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_LBL_CANCEL) ) {
				try {
					form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
					form.setPlayerCardRequired(IVoucherConstants.SPECIFIC_PLAYER_REQD.equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
					boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form,composite);
					if( !isValidate ) {
						return;
					}
					try {
						long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
						
						TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
						ticketInfoDTO.setSessionId(getCurrentSessionId);
						ticketInfoDTO.setBarcode(form.getBarCode());
						ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
						ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
						ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
						ticketInfoDTO.setGameBarcode(false);
						ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
						ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
						ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

						TicketInfoDTO updatedTicketInfoDTO = ServiceCall.getInstance().cancelRedeemVoucher(ticketInfoDTO);
						if( ticketInfoDTO != null ) {
							if( updatedTicketInfoDTO.isErrorPresent() ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
							} else {
								MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
								form.setBarCode(updatedTicketInfoDTO.getBarcode());
								form.setState(updatedTicketInfoDTO.getTktStatus());
								form.setAmount((ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));
								String eff = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getEffectiveDate().getTime()).toString();
								String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT,datePattern);
								form.setEffectiveDate(effectiveDate);
								String exp = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getExpireDate().getTime()).toString();
								String expireDate = DateHelper.getFormatedDate(exp, IVoucherConstants.DATE_FORMAT,datePattern);
								form.setExpireDate(expireDate);
								form.setLocation(updatedTicketInfoDTO.getTransAssetNumber());
								form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());

								disableRedeem();
								disableCancel();
								disableOverride();
								disableMultiRedeem();
								resetValues();
								composite.getTxtBarCode().setFocus();
							}
						}
					} catch (VoucherEngineServiceException ex) {
						log.error("Exception while cancelling the Redeem process", ex);
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					} catch (Exception ex) {
						log.error("Exception while cancelling the Redeem process",ex);
						VoucherUIExceptionHandler.handleException(ex);
					}
				} catch (Exception ex) {
					log.error("Exception while Redeeming the Voucher", ex);
					VoucherUIExceptionHandler.handleException(ex);
				}
			}
			if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_LBL_REDEEM) ) {
				try {
					if(ticketInfoDTO.getEffectiveDate().compareTo(ticketInfoDTO.getExpireDate()) < 0 ) {
					form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
					form.setPlayerCardRequired(IVoucherConstants.SPECIFIC_PLAYER_REQD.equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
					boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form,composite);
					if( !isValidate ) {
						return;
					}
					
					long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
					
					double maxTktRedemptionAmt = Double.parseDouble(SiteUtil.getMaxTktRedemptionAmt());					
					if( (Double.parseDouble(form.getAmount())) > Double.parseDouble((ConversionUtil.centsToDollar(maxTktRedemptionAmt))) ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MAX_REDEEM_AMT_EXCEEDED));						
						cancelReedemVoucher();						
						addErrorCodeToTransaction();
						disableRedeem();
						return;
					}
					TicketInfoDTO updatedTicketInfoDTO = null;
					try {
						TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
						ticketInfoDTO.setSessionId(getCurrentSessionId);
						ticketInfoDTO.setBarcode(form.getBarCode());
						ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
						ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
						ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
						ticketInfoDTO.setGameBarcode(false);
						ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
						ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
						ticketInfoDTO.setAmount(ConversionUtil.dollarToCents((Double.parseDouble(form.getAmount()))));
						ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

						TicketInfoDTO tktInfoDTO = null;
						tktInfoDTO = ObjectMapping.copyTktInfoDTOForRedeem(tktInfoDTOFromPopulate);
						if( tktInfoDTO == null ) {
							log.debug("Object from populate is null");
							tktInfoDTO = ServiceCall.getInstance().inquireVoucher(ticketInfoDTO);
						}

						if( tktInfoDTO != null ) {
							if( tktInfoDTO.getAmountType() == AmountTypeEnum.NON_CASHABLE_PROMO ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NCSH_REDEEM));
								cancelReedemVoucher();
								return;
							}

							String eff = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getEffectiveDate().getTime()).toString();
							String effectiveDate = DateHelper.getFormatedDate(eff, IVoucherConstants.DATE_FORMAT,datePattern);
							//long currentTime = System.currentTimeMillis();
							
							long currentTime = DateUtil.getCurrentServerDate().getTime();

							if( tktInfoDTO.getStatus() == VoucherStatusEnum.PENDING && isPendingAtDiffLoc(tktInfoDTO.getTransAssetNumber()) ) {
								if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
													.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
										}
									} else if( SiteUtil.allowPendingTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
												.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowPendingTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										redeemSiteEnabled = false;
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										redeemSiteEnabled = false;
										disableOverride();
									}
								} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowCPNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
										}
									} else if( SiteUtil.allowCPPendingTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowCPPendingTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										redeemSiteEnabled = false;
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										redeemSiteEnabled = false;
										disableOverride();
									}
								} else {
									updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
								}

							} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.VOIDED ) {
								if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
										}
									} else if( SiteUtil.allowVoidedTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowVoidedTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										disableOverride();
									}
								} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowCPNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
										}
									} else if( SiteUtil.allowCPVoidedTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowCPVoidedTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										disableOverride();
									}
								} else {
									updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
								}
							} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED ) {
								if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
										}
									} else if( SiteUtil.allowExpiredTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowExpiredTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										disableOverride();
									}
								} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
									if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
										if( SiteUtil.allowCPNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											disableOverride();
											redeemSiteEnabled = false;
										}
									} else if( SiteUtil.allowCPExpiredTkts() == 2 ) {
										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
												disableOverride();
												return;
											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
												enableOverride();
												return;
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
											}
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
										}
									} else if( SiteUtil.allowCPExpiredTkts() == 1 ) {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
										enableOverride();
									} else {
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
										disableOverride();
									}
								} else {
									if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
										if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											disableOverride();
											return;
										} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											enableOverride();
											return;
										} else {
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
										}
									} else {
										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
									}
								}
							} else {
								if( DateUtil.getDateFromString(effectiveDate,datePattern).getTime() > currentTime ) {
									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
										if( SiteUtil.allowCPNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
											}
										} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
											return;
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											disableOverride();
											redeemSiteEnabled = false;
											return;
										}

									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
										if( SiteUtil.allowNotEffTkts() == 2 ) {
											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
													disableOverride();
													return;
												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
													enableOverride();
													return;
												} else {
													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
												}
											} else {
												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
											}
										} else if( SiteUtil.allowNotEffTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											redeemSiteEnabled = false;
											enableOverride();
											return;
										} else {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											redeemSiteEnabled = false;
											disableOverride();
											return;
										}
									}									
								}else
									if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(), 5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
										if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
											disableOverride();
											return;
										} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
											enableOverride();
											return;
										} else {
											ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
										}
									} else {
										ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
									}
							}

							if( updatedTicketInfoDTO != null ) {
								if( updatedTicketInfoDTO.isErrorPresent() ) {
									if( !(updatedTicketInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED) ) {
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
									}
								} else {
									if( redeemSiteEnabled ) {
										setMultiRedeemAmount(this.multiRedeemAmount 
												+ new Double(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())).doubleValue());
										setMultiRedeemCount(this.multiRedeemCount + 1);
										if(multiRedeemController!=null && !multiRedeemController.getComposite().isDisposed()) {
											String amtTotal = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString());
											multiRedeemController.getForm().setAmountTotal(amtTotal);
											multiRedeemController.getForm().setVoucherCoupon(new Integer(getMultiRedeemCount()).toString());
											multiRedeemController.getComposite().getBtnResetTotal().setEnabled(true);
											multiRedeemController.getComposite().getBtnResetTotal().setImage(enableImage);
											multiRedeemController.getComposite().getBtnResetTotal().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
											this.multiRedeemAmount = getMultiRedeemAmount();
											this.multiRedeemCount = getMultiRedeemCount();
										}
										else
										{
											this.multiRedeemAmount=0;
											this.multiRedeemCount=0;
//											MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
										}
									}
									form.setBarCode(updatedTicketInfoDTO.getBarcode());
									form.setState(updatedTicketInfoDTO.getTktStatus());
									form.setAmount(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));

									form.setEffectiveDate(effectiveDate);
									String exp = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getExpireDate().getTime()).toString();
									String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT,datePattern);
									form.setExpireDate(expireDate);
									form.setLocation(tktInfoDTO.getTransAssetNumber());
									form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
									Util.sendDataToAuditTrail(
											IVoucherConstants.MODULE_NAME,
											LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_REDEEM_VOUCHER),
											LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE),
											"", updatedTicketInfoDTO.getBarcode(),
											LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_REDEEMED),
											EnumOperation.REDEEM_TICKET,
											PreferencesUtil.getClientAssetNumber());

									try {
										/**
										 * SEND MSG TO EVS IF A CASHABLE PROMO
										 * TKT IS REDEEMED FROM THE CASHIER
										 * WORKSTATION
										 */
										if( updatedTicketInfoDTO.getAmountType().equals(AmountTypeEnum.CASHABLE_PROMO) ) {
											SessionUtility sessionUtility = new SessionUtility();
											updatedTicketInfoDTO.getUserFormDTO().setSiteId(sessionUtility.getSiteDetails().getId());
											ServiceCall.getInstance().sendUpdateRedeemRequest(updatedTicketInfoDTO);
										}
									} catch (Exception e1) {
										log.error("Exception occured while sending the message to evs",e1);
									}

									if( redeemSiteEnabled ) {
										displayMessage();
										disableRedeem();
										disableCancel();
										resetValues();
										composite.getTxtBarCode().setFocus();
										printReceipt(updatedTicketInfoDTO);
									}
								}
							}
						}
					} catch (VoucherEngineServiceException ex) {
						log.error("VoucherEngineServiceException while Redeeming the Voucher",ex);
						List<Message> listMsg = ex.getErrorMessages();
						List<String> errorMsg = new ArrayList<String>();
						for (int i = 0; i < listMsg.size(); i++ ) {
							errorMsg.add((listMsg.get(i)).getMessageKey());
						}
						// if(redeemSiteEnabled &&
						// !errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION) ) {
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
						// }
						if( errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION)) {
							enableOverride();
							isVoucherPendingAtDiffLoc = true;
						} else if( errorMsg.contains(IVoucherConstants.VOUCHER_ALREADY_REDEEEMED) || errorMsg.contains(IVoucherConstants.VOUCHER_ASSET_NOT_VALID) ) {

						} else {
							cancelReedemVoucher();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						log.error("Exception occured while Redeeming the Voucher",ex);
						VoucherUIExceptionHandler.handleException(ex);
						cancelReedemVoucher();
					   }
				   }else{
				    MessageDialogUtil
				    .displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EFFECTIVE_DATE)+" "+
						LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CANNOT_GREATER_THAN)+""+
						LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXPIRE_DATE));
			      }
				}catch (Exception ex) {
					ex.printStackTrace();
					log.error("Exception while Redeeming the Voucher", ex);
					VoucherUIExceptionHandler.handleException(ex);
					cancelReedemVoucher();

				}	
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_LBL_MULTIREDEEM) ) {

				try 
				{
					MultiRedeemForm multiRedeemForm = new MultiRedeemForm();					
					String amtTotal = ConversionUtil.voucherAmountFormat(new Double(0.00).toString());
					multiRedeemForm.setAmountTotal(amtTotal);
					multiRedeemForm.setVoucherCoupon(new Integer(0).toString());
					setMultiRedeemAmount(0);
					setMultiRedeemCount(0);
					form.setAmountTotal("0.00");
					form.setVoucherCoupon("0");
					//resetMultiRedeemValues();
					if( multiRedeemController == null || ((MultiRedeemComposite) multiRedeemController.getComposite()).isDisposed() ) {
						multiRedeemController = new MultiRedeemController(composite.getMultiRedeemComposite(), SWT.NONE,
													multiRedeemForm, new SDSValidator(getClass(),true), this);
					}
					disableMultiRedeem();
				} catch (Exception ex) {
					log.error("Exception while creating MultiRedeemComposite",ex);
					ex.printStackTrace();
					VoucherUIExceptionHandler.handleException(ex);
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_LBL_CANCELPROCESS) ) {
				composite.getTxtBarCode().setText("");
				if( navCheck(true, true) ) {
					resetValues();
					composite.getTxtBarCode().setFocus();
					disableRedeem();
					disableOverride();
					disableCancel();
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_LBL_OVERRIDE) ) {
				populateForm(composite);
				form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
				form.setPlayerCardRequired("Y".equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
				boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form, composite);
				if(multiRedeemController!=null && !multiRedeemController.getComposite().isDisposed() ) 
				{
					multiRedeemController.getComposite().getBtnResetTotal().setEnabled(true);
					multiRedeemController.getComposite().getBtnResetTotal().setImage(enableImage);
					multiRedeemController.getComposite().getBtnResetTotal().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
				}
				if( !isValidate ) {
					return;
				}
				if( KeyBoardUtil.getKeyBoardShell() != null ) {
					KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
				}
				boolean MultiRedeem=multiRedeemController!=null && !multiRedeemController.getComposite().isDisposed();
				@SuppressWarnings("unused")
				DialogShell dialogShell = new DialogShell(composite.getShell(),	this,MultiRedeem);
			}

			else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.VOIDVOUCHER_CTRL_BTN_CANCEL) ) {
				resetValues();
				disableVoid();
				composite.getTxtBarCode().setFocus();
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_BTN_VOID) ) {
				try {
					form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
					form.setPlayerCardRequired(IVoucherConstants.SPECIFIC_PLAYER_REQD.equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
					boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form,composite);
					if( !isValidate ) {
						return;
					}
					try {
						TicketTypeEnum ticketTypeEnum = getTicketType(form.getBarCode());
						if( ticketTypeEnum.equals(TicketTypeEnum.SLOT_GENERATED)
							|| ticketTypeEnum.equals(TicketTypeEnum.SLOT_GENERATED_NEW_JERSEY) 
							|| ticketTypeEnum.equals(TicketTypeEnum.STANDARD_VALIDATION_TICKET_TYPE)
							|| ticketTypeEnum.equals(TicketTypeEnum.ENHANCED_VALIDATION_TICKET_TYPE)) {
							if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_VOID_SLOT_GEN_TKTS) ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CANNOT_VOID_SLOT_TKTS,new String[] {AppContextValues.getInstance().getTicketText()}));
								return;
							}
						} else if( ticketTypeEnum.equals(TicketTypeEnum.NON_SLOT_GENERATED) ) {
							if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_VOID_CASHIER_GEN_TKTS) ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CANNOT_VOID_NON_SLOT_TKTS,new String[] {AppContextValues.getInstance().getTicketText()}));
								return;
							}
						}
						
						
						if( tktInfoDTOChk != null && tktInfoDTOChk.getTktStatusId() == 3 ) {
							String errMsg = AppContextValues.getInstance().getTicketText()
									+ " " + LabelLoader.getLabelValue("VOUCHER.VOID.STATUS.FAILED") 
									+ "\n" + AppContextValues.getInstance().getTicketText() 
									+ " " + LabelLoader.getLabelValue("VOUCHER.IS.ALREADY.REDEEEMED");
							VoucherUIExceptionHandler.handleClientException(errMsg);
							return;
						} else if( tktInfoDTOChk != null && tktInfoDTOChk.getTktStatusId() == 4 ) {
							String errMsg1 = AppContextValues.getInstance().getTicketText()
									+ " " + LabelLoader.getLabelValue("VOUCHER.VOID.STATUS.FAILED") 
									+ "\n" + AppContextValues.getInstance().getTicketText() 
									+ " " + LabelLoader.getLabelValue("VOUCHER.IS.ALREADY.VOIDED"); 
							VoucherUIExceptionHandler.handleClientException(errMsg1);
							return;
						}
						
						long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
						
						TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
						ticketInfoDTO.setSessionId(getCurrentSessionId);
						ticketInfoDTO.setBarcode(form.getBarCode());
						ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
						ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
						ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
						ticketInfoDTO.setGameBarcode(false);
						ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
						ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
						ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

						TicketInfoDTO updatedTicketInfo = ServiceCall.getInstance().voidVoucher(ticketInfoDTO);

						if( updatedTicketInfo != null ) {
							if( updatedTicketInfo.isErrorPresent() ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfo));
							} else {
								MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfo));
								form.setBarCode(updatedTicketInfo.getBarcode());
								form.setState(updatedTicketInfo.getTktStatus());
								form.setAmount((ConversionUtil.centsToDollar(ticketInfoDTO.getAmount())));
								String eff = DateUtil.getLocalTimeFromUTC(updatedTicketInfo.getEffectiveDate().getTime()).toString();
								String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT,datePattern);
								form.setEffectiveDate(effectiveDate);
								String exp = DateUtil.getLocalTimeFromUTC(updatedTicketInfo.getExpireDate().getTime()).toString();
								String expireDate = DateHelper.getFormatedDate(exp, IVoucherConstants.DATE_FORMAT,datePattern);
								form.setExpireDate(expireDate);
								form.setLocation(updatedTicketInfo.getTransAssetNumber());
								form.setOptLockVersion(updatedTicketInfo.getOptimisticLockVersion());
								Util.sendDataToAuditTrail(
										IVoucherConstants.MODULE_NAME,
										LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_STC_VOID_TICKET_SCREEN) + AppContextValues.getInstance().getTicketText(),
										LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE),
										"", updatedTicketInfo.getBarcode(),
										LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_VOIDED),
										EnumOperation.VOID_TICKET,
										PreferencesUtil.getClientAssetNumber());
								try {
									if( (updatedTicketInfo.getAmountType().equals(AmountTypeEnum.CASHABLE_PROMO))
											|| (updatedTicketInfo.getAmountType().equals(AmountTypeEnum.NON_CASHABLE_PROMO)) ) {
										SessionUtility sessionUtility = new SessionUtility();
										updatedTicketInfo.getUserFormDTO().setSiteId(sessionUtility.getSiteDetails().getId());
										ServiceCall.getInstance().sendVoidEntryForPromo(updatedTicketInfo);
									}
								} catch (Exception e1) {
									log.error("Exception occured while sending the message to evs",e1);
								}
								Composite parent = VoucherMiddleComposite.getCurrentComposite().getParent();
								int style = VoucherMiddleComposite.getCurrentComposite().getStyle();
								VoucherMiddleComposite.getCurrentComposite().dispose();
								createComposite(parent, style);
							}
						}
					} catch (VoucherEngineServiceException ex) {
						log.error("Exception while voiding the Voucher", ex);
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					} catch (Exception ex) {
						log.error("Exception while voiding the Voucher", ex);
						VoucherUIExceptionHandler.handleException(ex);
					}
				} catch (Exception e1) {
					log.error("Exception while voiding the Voucher", e1);
					VoucherUIExceptionHandler.handleException(e1);
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_BTN_SUBMIT) ) {
				try {
					boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form, composite);
					if( !isValidate)
						return;
					try {
						long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
						
						TicketInfoDTO tktInfoDTO = new TicketInfoDTO();
						tktInfoDTO.setSessionId(getCurrentSessionId);
						tktInfoDTO.setBarcode(form.getBarCode());
						tktInfoDTO.setTicketType(getTicketType(form.getBarCode()));
						tktInfoDTO.setGameBarcode(false);
						tktInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
						tktInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
						tktInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
						tktInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());

						tktInfoDTO = ServiceCall.getInstance().redeemRequestVoucher(tktInfoDTO);

						if( tktInfoDTO != null ) {
							if( tktInfoDTO.isErrorPresent() ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(tktInfoDTO));
							} else {
								MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(tktInfoDTO));
								this.ticketInfoDTO = tktInfoDTO;
								form.setBarCode(tktInfoDTO.getBarcode());
								form.setAmount((ConversionUtil.centsToDollar(tktInfoDTO.getAmount())));

								String eff = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getEffectiveDate().getTime()).toString();
								String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT, datePattern);
								form.setEffectiveDate(effectiveDate);
								String exp = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getExpireDate().getTime()).toString();
								String expireDate = DateHelper.getFormatedDate(exp, IVoucherConstants.DATE_FORMAT,datePattern);
								form.setExpireDate(expireDate);

								form.setLocation(tktInfoDTO.getTransAssetNumber());
								form.setPlayerId(tktInfoDTO.getPlayerId());
								form.setState(tktInfoDTO.getTktStatus());
								form.setOptLockVersion(tktInfoDTO.getOptimisticLockVersion());
								if( (tktInfoDTO.getStatus().equals(VoucherStatusEnum.PENDING)) && tktInfoDTO.isPendingInCurrentTransaction() ) {
									enableOverride();
									// disableMultiRedeem();
									disableOverride();
								} else if( (tktInfoDTO.getStatus().equals(VoucherStatusEnum.PENDING) && !tktInfoDTO.isPendingInCurrentTransaction())
										|| tktInfoDTO.getStatus().equals(VoucherStatusEnum.VOIDED)
										|| tktInfoDTO.getStatus().equals(VoucherStatusEnum.EXPIRED) ) {
									disableRedeem();
									// disableMultiRedeem();
									enableOverride();
								}
							}
						}
					} catch (VoucherEngineServiceException ex) {
						log.error("Exception while requesting for Redeeming the Voucher",ex);
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					} catch (Exception ex) {
						log.error("Exception while requesting for redeeming the Voucher",ex);
						VoucherUIExceptionHandler.handleException(ex);
					}
				} catch (Exception e1) {
					log.error("Exception while submiting the Form ", e1);
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(e1.toString());
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE) ) {
				form.setVoid(this.isVoid);
				populateBarcode();
//				if( multiRedeemController != null && !((MultiRedeemComposite) multiRedeemController.getComposite()).isDisposed() ) {
//					try {
//						if(ticketInfoDTO.getEffectiveDate().compareTo(ticketInfoDTO.getExpireDate()) < 0 ) {
//						form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
//						form.setPlayerCardRequired(IVoucherConstants.SPECIFIC_PLAYER_REQD.equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
//						boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form,composite);
//						if( !isValidate ) {
//							return;
//						}
//						
//						long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
//						
//						double maxTktRedemptionAmt = Double.parseDouble(SiteUtil.getMaxTktRedemptionAmt());					
//						if( (Double.parseDouble(form.getAmount())) > Double.parseDouble((ConversionUtil.centsToDollar(maxTktRedemptionAmt))) ) {
//							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MAX_REDEEM_AMT_EXCEEDED));						
//							cancelReedemVoucher();						
//							addErrorCodeToTransaction();
//							disableRedeem();
//							return;
//						}
//						TicketInfoDTO updatedTicketInfoDTO = null;
//						try {
//							TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
//							ticketInfoDTO.setSessionId(getCurrentSessionId);
//							ticketInfoDTO.setBarcode(form.getBarCode());
//							ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
//							ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
//							ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
//							ticketInfoDTO.setGameBarcode(false);
//							ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
//							ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
//							ticketInfoDTO.setAmount(ConversionUtil.dollarToCents((Double.parseDouble(form.getAmount()))));
//							ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
//
//							TicketInfoDTO tktInfoDTO = null;
//							tktInfoDTO = ObjectMapping.copyTktInfoDTOForRedeem(tktInfoDTOFromPopulate);
//							if( tktInfoDTO == null ) {
//								log.debug("Object from populate is null");
//								tktInfoDTO = ServiceCall.getInstance().inquireVoucher(ticketInfoDTO);
//							}
//
//							if( tktInfoDTO != null ) {
//								if( tktInfoDTO.getAmountType() == AmountTypeEnum.NON_CASHABLE_PROMO ) {
//									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NCSH_REDEEM));
//									cancelReedemVoucher();
//									return;
//								}
//
//								String eff = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getEffectiveDate().getTime()).toString();
//								String effectiveDate = DateHelper.getFormatedDate(eff, IVoucherConstants.DATE_FORMAT,datePattern);
//								//long currentTime = System.currentTimeMillis();
//								
//								long currentTime = DateUtil.getCurrentServerDate().getTime();
//
//								if( tktInfoDTO.getStatus() == VoucherStatusEnum.PENDING && isPendingAtDiffLoc(tktInfoDTO.getTransAssetNumber()) ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowPendingTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//													.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowPendingTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											redeemSiteEnabled = false;
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											redeemSiteEnabled = false;
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowCPPendingTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPPendingTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											redeemSiteEnabled = false;
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											redeemSiteEnabled = false;
//											disableOverride();
//										}
//									} else {
//										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//									}
//
//								} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.VOIDED ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowVoidedTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowVoidedTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																	.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowCPVoidedTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPVoidedTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else {
//										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//									}
//								} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowExpiredTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowExpiredTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												redeemSiteEnabled = false;
//											}
//										} else if( SiteUtil.allowCPExpiredTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPExpiredTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else {
//										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												return;
//											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												enableOverride();
//												return;
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//											}
//										} else {
//											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//										}
//									}
//								} else {
//									if( DateUtil.getDateFromString(effectiveDate,datePattern).getTime() > currentTime ) {
//										if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//												return;
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												redeemSiteEnabled = false;
//												return;
//											}
//
//										} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//												return;
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//												return;
//											}
//										}									
//									}else
//										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(), 5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												return;
//											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												enableOverride();
//												return;
//											} else {
//												ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//											}
//										} else {
//											ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
//											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//										}
//								}
//
//								if( updatedTicketInfoDTO != null ) {
//									if( updatedTicketInfoDTO.isErrorPresent() ) {
//										if( !(updatedTicketInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED) ) {
//											MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
//										}
//									} else {
//										if( redeemSiteEnabled ) {
//											setMultiRedeemAmount(this.multiRedeemAmount 
//													+ new Double(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())).doubleValue());
//											setMultiRedeemCount(this.multiRedeemCount + 1);
//											if( multiRedeemController != null ) {
//												String amtTotal = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString());
//												multiRedeemController.getForm().setAmountTotal(amtTotal);
//												multiRedeemController.getForm().setVoucherCoupon(new Integer(getMultiRedeemCount()).toString());
//												this.multiRedeemAmount = getMultiRedeemAmount();
//												this.multiRedeemCount = getMultiRedeemCount();
//											}
//											MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
//										}
//										form.setBarCode(updatedTicketInfoDTO.getBarcode());
//										form.setState(updatedTicketInfoDTO.getTktStatus());
//										form.setAmount(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));
//
//										form.setEffectiveDate(effectiveDate);
//										String exp = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getExpireDate().getTime()).toString();
//										String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT,datePattern);
//										form.setExpireDate(expireDate);
//										form.setLocation(tktInfoDTO.getTransAssetNumber());
//										form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
//										Util.sendDataToAuditTrail(
//												IVoucherConstants.MODULE_NAME,
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_REDEEM_VOUCHER),
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE),
//												"", updatedTicketInfoDTO.getBarcode(),
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_REDEEMED),
//												EnumOperation.REDEEM_TICKET,
//												PreferencesUtil.getClientAssetNumber());
//
//										try {
//											/**
//											 * SEND MSG TO EVS IF A CASHABLE PROMO
//											 * TKT IS REDEEMED FROM THE CASHIER
//											 * WORKSTATION*
//											 */
//											if( updatedTicketInfoDTO.getAmountType().equals(AmountTypeEnum.CASHABLE_PROMO) ) {
//												SessionUtility sessionUtility = new SessionUtility();
//												updatedTicketInfoDTO.getUserFormDTO().setSiteId(sessionUtility.getSiteDetails().getId());
//												ServiceCall.getInstance().sendUpdateRedeemRequest(updatedTicketInfoDTO);
//											}
//										} catch (Exception e1) {
//											log.error("Exception occured while sending the message to evs",e1);
//										}
//
//										if( redeemSiteEnabled ) {
//											displayMessage();
//											disableRedeem();
//											disableCancel();
//											resetValues();
//											composite.getTxtBarCode().setFocus();
//											printReceipt(updatedTicketInfoDTO);
//										}
//									}
//								}
//							}
//						} catch (VoucherEngineServiceException ex) {
//							log.error("VoucherEngineServiceException while Redeeming the Voucher",ex);
//							List<Message> listMsg = ex.getErrorMessages();
//							List<String> errorMsg = new ArrayList<String>();
//							for (int i = 0; i < listMsg.size(); i++ ) {
//								errorMsg.add((listMsg.get(i)).getMessageKey());
//							}
//							// if(redeemSiteEnabled &&
//							// !errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION) ) {
//							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
//							// }
//							if( errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION)) {
//								enableOverride();
//								isVoucherPendingAtDiffLoc = true;
//							} else if( errorMsg.contains(IVoucherConstants.VOUCHER_ALREADY_REDEEEMED) || errorMsg.contains(IVoucherConstants.VOUCHER_ASSET_NOT_VALID) ) {
//
//							} else {
//								cancelReedemVoucher();
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//							log.error("Exception occured while Redeeming the Voucher",ex);
//							VoucherUIExceptionHandler.handleException(ex);
//							cancelReedemVoucher();
//						   }
//					   }else{
//					    MessageDialogUtil
//					    .displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EFFECTIVE_DATE)+" "+
//							LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CANNOT_GREATER_THAN)+""+
//							LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXPIRE_DATE));
//				      }
//					}catch (Exception ex) {
//						ex.printStackTrace();
//						log.error("Exception while Redeeming the Voucher", ex);
//						VoucherUIExceptionHandler.handleException(ex);
//						cancelReedemVoucher();
//					}	
//				}
				
			}
			if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.BUTTON_CTRL_BTN_EXIT) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite().getParent() != null 
							&& !(VoucherMiddleComposite.getCurrentComposite().getParent().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
					}
				} catch (Exception ex1) {
					log.error("Exception while submiting the Form ", ex1);
				}
			}
		} catch (Exception ex2) {
			log.error("Exception in RedeemVoucherController", ex2);
		}
	}

	private void addErrorCodeToTransaction() {
		try {
			IVoucherService service = VoucherServiceLocator.getService();
			TransactionDetailsDTO transDetailsDTO = getCancelRedeemVoucherErrorTransDetails();
			transDetailsDTO.setErrorCodeId(ErrorCodeEnum.AMOUNT_LARGER_THAN_ALLOWED.getErrorCode());
			transDetailsDTO.setErrorPresent(true);
			transDetailsDTO.setTransactionStatusId(ticketInfoDTO.getTktStatusId());


			TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
			ticketInfoDTO.setBarcode(form.getBarCode());
			ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
			ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
			ticketInfoDTO.setGameBarcode(false);
			ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
			ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
			ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

			service.insertTransWithErrorCode(ticketInfoDTO, transDetailsDTO);
		} catch (VoucherEngineServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static TransactionDetailsDTO getCancelRedeemVoucherErrorTransDetails() {
		TransactionDetailsDTO transactionDetailsDTO = new TransactionDetailsDTO();  
		transactionDetailsDTO.setTransactionReasonId(TransactionReasonErrorEnum.TEMP_REASON.getTransactionReason());
		transactionDetailsDTO.setTransactionTypeId(TransactionTypeErrorEnum.CANCEL_REDEEMED.getTransactionType());
		return transactionDetailsDTO;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.keyCode == 13 || e.keyCode == 16777296 ) {	
			try {
				populateForm(composite);
				populateBarcode();
				//if the multiredeem form is enabled
//				if( multiRedeemController != null && !((MultiRedeemComposite) multiRedeemController.getComposite()).isDisposed() ) {
//					boolean redeemSiteEnabled = true;
//					SessionUtility utility = new SessionUtility();
//					String datePattern = utility.getApplicationDateFormat();
//					try {
//						if(ticketInfoDTO.getEffectiveDate().compareTo(ticketInfoDTO.getExpireDate()) < 0 ) {
//						form.setCreatedPlayerCardId(ticketInfoDTO.getPlayerId());
//						form.setPlayerCardRequired(IVoucherConstants.SPECIFIC_PLAYER_REQD.equalsIgnoreCase(ticketInfoDTO.getPlayerCardReqd()));
//						boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form,composite);
//						if( !isValidate ) {
//							return;
//						}
//						
//						long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
//						
//						double maxTktRedemptionAmt = Double.parseDouble(SiteUtil.getMaxTktRedemptionAmt());					
//						if( (Double.parseDouble(form.getAmount())) > Double.parseDouble((ConversionUtil.centsToDollar(maxTktRedemptionAmt))) ) {
//							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MAX_REDEEM_AMT_EXCEEDED));						
//							cancelReedemVoucher();						
//							addErrorCodeToTransaction();
//							disableRedeem();
//							return;
//						}
//						TicketInfoDTO updatedTicketInfoDTO = null;
//						try {
//							TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
//							ticketInfoDTO.setSessionId(getCurrentSessionId);
//							ticketInfoDTO.setBarcode(form.getBarCode());
//							ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
//							ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
//							ticketInfoDTO.setTicketType(getTicketType(form.getBarCode()));
//							ticketInfoDTO.setGameBarcode(false);
//							ticketInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
//							ticketInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
//							ticketInfoDTO.setAmount(ConversionUtil.dollarToCents((Double.parseDouble(form.getAmount()))));
//							ticketInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
//
//							TicketInfoDTO tktInfoDTO = null;
//							tktInfoDTO = ObjectMapping.copyTktInfoDTOForRedeem(tktInfoDTOFromPopulate);
//							if( tktInfoDTO == null ) {
//								log.debug("Object from populate is null");
//								tktInfoDTO = ServiceCall.getInstance().inquireVoucher(ticketInfoDTO);
//							}
//
//							if( tktInfoDTO != null ) {
//								if( tktInfoDTO.getAmountType() == AmountTypeEnum.NON_CASHABLE_PROMO ) {
//									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NCSH_REDEEM));
//									cancelReedemVoucher();
//									return;
//								}
//
//								String eff = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getEffectiveDate().getTime()).toString();
//								String effectiveDate = DateHelper.getFormatedDate(eff, IVoucherConstants.DATE_FORMAT,datePattern);
//								//long currentTime = System.currentTimeMillis();
//								
//								long currentTime = DateUtil.getCurrentServerDate().getTime();
//
//								if( tktInfoDTO.getStatus() == VoucherStatusEnum.PENDING && isPendingAtDiffLoc(tktInfoDTO.getTransAssetNumber()) ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//														.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowPendingTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//													.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowPendingTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											redeemSiteEnabled = false;
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											redeemSiteEnabled = false;
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowCPPendingTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPPendingTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											redeemSiteEnabled = false;
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											redeemSiteEnabled = false;
//											disableOverride();
//										}
//									} else {
//										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//									}
//
//								} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.VOIDED ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowVoidedTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowVoidedTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																	.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowCPVoidedTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPVoidedTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else {
//										updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//									}
//								} else if( tktInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED ) {
//									if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//											}
//										} else if( SiteUtil.allowExpiredTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowExpiredTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//										if( DateUtil.getDateFromString(effectiveDate, datePattern).getTime() > currentTime ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												redeemSiteEnabled = false;
//											}
//										} else if( SiteUtil.allowCPExpiredTkts() == 2 ) {
//											if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//															.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//												if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//													disableOverride();
//													return;
//												} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//													enableOverride();
//													return;
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//												}
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
//											}
//										} else if( SiteUtil.allowCPExpiredTkts() == 1 ) {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//											enableOverride();
//										} else {
//											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//											disableOverride();
//										}
//									} else {
//										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												return;
//											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												enableOverride();
//												return;
//											} else {
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//											}
//										} else {
//											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//										}
//									}
//								} else {
//									if( DateUtil.getDateFromString(effectiveDate,datePattern).getTime() > currentTime ) {
//										if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE_PROMO ) {
//											if( SiteUtil.allowCPNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowCPNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//												return;
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												redeemSiteEnabled = false;
//												return;
//											}
//
//										} else if( tktInfoDTO.getAmountType() == AmountTypeEnum.CASHABLE ) {
//											if( SiteUtil.allowNotEffTkts() == 2 ) {
//												if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(),5)
//																.equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//													if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//														disableOverride();
//														return;
//													} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//														MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//														enableOverride();
//														return;
//													} else {
//														updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//													}
//												} else {
//													updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//												}
//											} else if( SiteUtil.allowNotEffTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												redeemSiteEnabled = false;
//												enableOverride();
//												return;
//											} else {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												redeemSiteEnabled = false;
//												disableOverride();
//												return;
//											}
//										}									
//									}else
//										if( VoucherStringUtil.lPadWithZero(SDSApplication.getLoggedInUserID(), 5).equalsIgnoreCase(tktInfoDTO.getCreatedEmpId()) ) {
//											if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 0 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_SITE_NOT_ENABLED));
//												disableOverride();
//												return;
//											} else if( SiteUtil.allowCashierToRedeemTheirOwnTkts() == 1 ) {
//												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_OVERRIDE_REQD));
//												enableOverride();
//												return;
//											} else {
//												ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
//												updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//											}
//										} else {
//											ticketInfoDTO.setPlayerId(tktInfoDTOFromPopulate.getPlayerId());
//											updatedTicketInfoDTO = ServiceCall.getInstance().redeemVoucher(ticketInfoDTO);
//										}
//								}
//
//								if( updatedTicketInfoDTO != null ) {
//									if( updatedTicketInfoDTO.isErrorPresent() ) {
//										if( !(updatedTicketInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED) ) {
//											MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
//										}
//									} else {
//										if( redeemSiteEnabled ) {
//											setMultiRedeemAmount(this.multiRedeemAmount 
//													+ new Double(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())).doubleValue());
//											setMultiRedeemCount(this.multiRedeemCount + 1);
//											if( multiRedeemController != null ) {
//												String amtTotal = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString());
//												multiRedeemController.getForm().setAmountTotal(amtTotal);
//												multiRedeemController.getForm().setVoucherCoupon(new Integer(getMultiRedeemCount()).toString());
//												this.multiRedeemAmount = getMultiRedeemAmount();
//												this.multiRedeemCount = getMultiRedeemCount();
//											}
//											MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
//										}
//										form.setBarCode(updatedTicketInfoDTO.getBarcode());
//										form.setState(updatedTicketInfoDTO.getTktStatus());
//										form.setAmount(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));
//
//										form.setEffectiveDate(effectiveDate);
//										String exp = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getExpireDate().getTime()).toString();
//										String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT,datePattern);
//										form.setExpireDate(expireDate);
//										form.setLocation(tktInfoDTO.getTransAssetNumber());
//										form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
//										Util.sendDataToAuditTrail(
//												IVoucherConstants.MODULE_NAME,
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_REDEEM_VOUCHER),
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE),
//												"", updatedTicketInfoDTO.getBarcode(),
//												LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_REDEEMED),
//												EnumOperation.REDEEM_TICKET,
//												PreferencesUtil.getClientAssetNumber());
//
//										try {
//											/**
//											 * SEND MSG TO EVS IF A CASHABLE PROMO
//											 * TKT IS REDEEMED FROM THE CASHIER
//											 * WORKSTATION*
//											 */
//											if( updatedTicketInfoDTO.getAmountType().equals(AmountTypeEnum.CASHABLE_PROMO) ) {
//												SessionUtility sessionUtility = new SessionUtility();
//												updatedTicketInfoDTO.getUserFormDTO().setSiteId(sessionUtility.getSiteDetails().getId());
//												ServiceCall.getInstance().sendUpdateRedeemRequest(updatedTicketInfoDTO);
//											}
//										} catch (Exception e1) {
//											log.error("Exception occured while sending the message to evs",e1);
//										}
//
//										if( redeemSiteEnabled ) {
//											displayMessage();
//											disableRedeem();
//											disableCancel();
//											resetValues();
//											composite.getTxtBarCode().setFocus();
//											printReceipt(updatedTicketInfoDTO);
//										}
//									}
//								}
//							}
//						} catch (VoucherEngineServiceException ex) {
//							log.error("VoucherEngineServiceException while Redeeming the Voucher",ex);
//							List<Message> listMsg = ex.getErrorMessages();
//							List<String> errorMsg = new ArrayList<String>();
//							for (int i = 0; i < listMsg.size(); i++ ) {
//								errorMsg.add((listMsg.get(i)).getMessageKey());
//							}
//							// if(redeemSiteEnabled &&
//							// !errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION) ) {
//							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
//							// }
//							if( errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION)) {
//								enableOverride();
//								isVoucherPendingAtDiffLoc = true;
//							} else if( errorMsg.contains(IVoucherConstants.VOUCHER_ALREADY_REDEEEMED) || errorMsg.contains(IVoucherConstants.VOUCHER_ASSET_NOT_VALID) ) {
//
//							} else {
//								cancelReedemVoucher();
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//							log.error("Exception occured while Redeeming the Voucher",ex);
//							VoucherUIExceptionHandler.handleException(ex);
//							cancelReedemVoucher();
//						   }
//					   }else{
//					    MessageDialogUtil
//					    .displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EFFECTIVE_DATE)+" "+
//							LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CANNOT_GREATER_THAN)+""+
//							LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXPIRE_DATE));
//				      }
//					}catch (Exception ex) {
//						ex.printStackTrace();
//						log.error("Exception while Redeeming the Voucher", ex);
//						VoucherUIExceptionHandler.handleException(ex);
//						cancelReedemVoucher();
//					}	
//				}
			
				
			} catch (Exception e1) {
				log.error("Exception occured while populating the barcode.", e1);
			}
		} 			   		
	}

	public void populateBarcode() {
		SessionUtility utility = new SessionUtility();
		String datePattern = utility.getApplicationDateFormat();
		try {

			String barcode  = form.getBarCode();
			String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
			
			if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION)
					&& barcode.length() == 16) {
				barcode = "00" + barcode.trim();
				form.setBarCode(barcode);
			}

			boolean isValidate = validate(IVoucherConstants.REDEEM_VOUCHER_FORM, form, composite);
			if( !isValidate) {
				return;
			}

			long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());

			TicketInfoDTO tktInfoDTO = new TicketInfoDTO();

			tktInfoDTO.setVoid(form.isVoid());
			tktInfoDTO.setTicketType(getTicketType(form.getBarCode()));
			tktInfoDTO.setGameBarcode(false);
			tktInfoDTO.setSessionId(getCurrentSessionId);
			tktInfoDTO.setBarcode(form.getBarCode());
			barcodeBackup = form.getBarCode();
			tktInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			tktInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
			tktInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
			tktInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
			tktInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
			//Set the Source for Change in TIS Flow to know the inquiry is from Redeem Request screen
			tktInfoDTO.setSource("CWS");
			
			tktInfoDTOChk = ServiceCall.getInstance().inquireVoucher(tktInfoDTO);
			//int amtTypeId = ServiceCall.getInstance().getAmountType(form.getBarCode(),SDSApplication.getSiteDetails().getId());
			if(tktInfoDTOChk!=null && tktInfoDTOChk.getAmountType() == AmountTypeEnum.NON_CASHABLE_PROMO ) {
				if( !isVoid ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NCSH_REDEEM));
					// cancelReedemVoucher();
					return;
				}
			}
			if( !isVoid ) {
				tktInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
				tktInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
				tktInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
				tktInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
				tktInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
				tktInfoDTO = ServiceCall.getInstance().redeemRequestVoucherWithoutStatusCheck(tktInfoDTO);

			} else {
				tktInfoDTO = ServiceCall.getInstance().inquireVoucher(tktInfoDTO);
			}

			tktInfoDTOFromPopulate = ObjectMapping.copyTktInfoDTOForRedeem(tktInfoDTO);

			if( tktInfoDTO != null ) {
				if( tktInfoDTO.isErrorPresent() ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(tktInfoDTO));
					composite.getTxtBarCode().selectAll();
					composite.getTxtBarCode().setFocus();
				} else {
					this.ticketInfoDTO = tktInfoDTO;
					form.setBarCode(tktInfoDTO.getBarcode());
					form.setAmount((ConversionUtil.centsToDollar(tktInfoDTO.getAmount())));
					String eff = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getEffectiveDate().getTime()).toString();
					String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT, datePattern);
					form.setEffectiveDate(effectiveDate);
					String exp = DateUtil.getLocalTimeFromUTC(tktInfoDTO.getExpireDate().getTime()).toString();
					String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT, datePattern);
					form.setExpireDate(expireDate);
					form.setLocation(tktInfoDTO.getTransAssetNumber());
					form.setPlayerId(tktInfoDTO.getPlayerId());
					form.setState(tktInfoDTO.getTktStatus());
					form.setOptLockVersion(tktInfoDTO
							.getOptimisticLockVersion());
					if( (tktInfoDTO.getStatus().equals(VoucherStatusEnum.PENDING) && tktInfoDTO.isPendingInCurrentTransaction()) ) {
						if( !isVoid ) {
							enableRedeem();
							if( !(multiRedeemController != null 
									&& ((MultiRedeemComposite) multiRedeemController.getComposite()) != null 
									&& !((MultiRedeemComposite) multiRedeemController.getComposite()).isDisposed()) ) {
								enableMultiRedeem();
							} else {
								disableMultiRedeem();
							}
							enableCancel();
							disableOverride();
						}
					} else if( (tktInfoDTO.getStatus().equals(VoucherStatusEnum.PENDING) && !tktInfoDTO.isPendingInCurrentTransaction())
							|| tktInfoDTO.getStatus().equals(VoucherStatusEnum.VOIDED)
							|| tktInfoDTO.getStatus().equals(VoucherStatusEnum.EXPIRED) ) {
						if( !isVoid ) {
							enableRedeem();
							if( !(multiRedeemController != null 
									&& ((MultiRedeemComposite) multiRedeemController.getComposite()) != null 
									&& !((MultiRedeemComposite) multiRedeemController.getComposite()).isDisposed()) ) {
								enableMultiRedeem();
							} else {
								disableMultiRedeem();
							}
							disableOverride();
						}
					} else if( tktInfoDTO.getStatus().equals(VoucherStatusEnum.REDEEMED)
							|| tktInfoDTO.getStatus().equals(VoucherStatusEnum.UNREDEEMABLE) ) {
						if( !isVoid ) {
							enableRedeem();
						}
					}
					if( isVoid ) {
						enableVoid();
					}
				}
				populateScreen(composite);
				if( !isVoid && !(tktInfoDTO.getStatus().equals(VoucherStatusEnum.REDEEMED) || tktInfoDTO.getStatus().equals(VoucherStatusEnum.UNREDEEMABLE)) ) {
					if( tktInfoDTO.getPlayerCardReqd().equalsIgnoreCase(IVoucherConstants.SPECIFIC_PLAYER_REQD) ) {
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(
								IDBLabelKeyConstants.VOU_PLAYERID_NOT_VALID,new String[] { tktInfoDTO.getPlayerId() }),composite);
						if( !response ) {
							disableRedeem();
							disableOverride();
							return;
						}
					} else if( tktInfoDTO.getPlayerCardReqd().equalsIgnoreCase(IVoucherConstants.ANY_PLAYER_REQD) ) {
						VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell(), SWT.None | SWT.APPLICATION_MODAL);
						dialog.setLocation(170, 250);
						String result = null;
						dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PLAYERID_REQD));
						dialog.setAltErrorText(true);
						result = dialog.open(10, false);
						if( !enterPlayerId(result) ) {
							disableRedeem();
							disableOverride();
							return;
						} else {
							form.setPlayerId(playerCardId);
							populateScreen(composite);
						}
					}
				}
			}
		} catch (VoucherEngineServiceException ex) {
			log.error("Exception while requesting for Redeeming the Voucher",ex);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
			resetFormValues();
			if( !isVoid ) {			
				disableRedeem();
				disableOverride();
			}
			/*List<Message> listMsg = ex.getErrorMessages();
			List<String> errorMsg = new ArrayList<String>();
			for (int i = 0; i < listMsg.size(); i++ ) {
				errorMsg.add((listMsg.get(i)).getMessageKey());
			}
			if( errorMsg.contains(IVoucherConstants.CASINO_ID_MISMATCH) ) {			
			 */		
			composite.getTxtBarCode().selectAll();
			composite.getTxtBarCode().setFocus();
			 //	}
			disableCancel();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Exception while requesting for redeeming the Voucher",ex);
			VoucherUIExceptionHandler.handleException(ex);
			resetFormValues();
			if( !isVoid ) {
				disableRedeem();
				disableOverride();
			}
			composite.getTxtBarCode().selectAll();
			composite.getTxtBarCode().setFocus();
			disableCancel();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.voucherui.controller.VoucherBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return composite;
	}

	public RedeemVoucherForm getForm() {
		return form;
	}

	private boolean isPendingAtDiffLoc(String inputLocation) {
		boolean retValue = false;
		if( !inputLocation.trim().equalsIgnoreCase(
				PreferencesUtil.getClientAssetNumber().trim())) {
			retValue = true;
		}
		return retValue;
	}

	public double getMultiRedeemAmount() {
		return multiRedeemAmount;
	}

	public void setMultiRedeemAmount(double multiRedeemAmount) {
		this.multiRedeemAmount = multiRedeemAmount;
	}

	public int getMultiRedeemCount() {
		return multiRedeemCount;
	}

	public void setMultiRedeemCount(int multiRedeemCount) {
		this.multiRedeemCount = multiRedeemCount;
	}

	/**
	 * This method resets the form values and sets the multi redeem amount and
	 * count to the latest value
	 */
	public void resetValues() {
		try {
			form.setAmount("");
			form.setBarCode("");
			form.setCreatedPlayerCardId("");
			form.setEffectiveDate("");
			form.setExpireDate("");
			form.setLocation("");
			form.setPlayerCardRequired(false);
			form.setPlayerId("");
			form.setState("");
			form.setTicketSeed("");
			String amtTotal = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString());
			form.setAmountTotal(amtTotal);
			form.setVoucherCoupon(Integer.toString(getMultiRedeemCount()));
			populateScreen(composite);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method resets the form values except the barcode field
	 */
	public void resetFormValues() {
		try {
			form.setAmount("");
			form.setCreatedPlayerCardId("");
			form.setEffectiveDate("");
			form.setExpireDate("");
			form.setLocation("");
			form.setPlayerCardRequired(false);
			form.setPlayerId("");
			form.setState("");
			form.setTicketSeed("");
			form.setBarCode(composite.getTxtBarCode().getText());
			String amtTotal = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString());
			form.setAmountTotal(amtTotal);
			form.setVoucherCoupon(Integer.toString(getMultiRedeemCount()));
			populateScreen(composite);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetMultiRedeemValues() {
		try {
			form.setAmountTotal(ConversionUtil.voucherAmountFormat(0.00));
			form.setVoucherCoupon(Integer.toString(0));
			populateScreen(composite);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * This method resets the form values
	 */
	public void resetAllValues() {
		try {
			form.setAmount("");
			form.setAmountTotal("");
			form.setBarCode("");
			form.setCreatedPlayerCardId("");
			form.setEffectiveDate("");
			form.setExpireDate("");
			form.setLocation("");
			form.setPlayerCardRequired(false);
			form.setPlayerId("");
			form.setState("");
			form.setTicketSeed("");
			form.setVoucherCoupon("");
			populateScreen(composite);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		// composite.redraw();
		if( e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}

	/**
	 * This method returns the type of the ticket
	 * 
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	private TicketTypeEnum getTicketType(String barcode) throws Exception {
		int tktType = Integer.parseInt(barcode.substring(0, 1));
		String gameBarcodeIndicator = barcode.substring(0, 2);
		if ( "00".equals(gameBarcodeIndicator) ) {
			tktType  = 0;
		}
		TicketTypeEnum retVal = null;
		switch (tktType) {
		case 0:
			retVal = TicketTypeEnum.STANDARD_VALIDATION_TICKET_TYPE;
			break;	
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
		case 9:
			retVal = TicketTypeEnum.S2S_GEN_TICKET_TYPE;
			break;		
		}

		if( retVal == null ) {
			throw new VoucherEngineServiceException(
					LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_INVALID_TKT_TYPE));
		}

		return retVal;
	}

	/**
	 * This method is called each time when the barcode text is going to be modified
	 * 
	 * @param formExists
	 * @param clearValues
	 */
	public boolean navCheck(boolean formExists, boolean clearValues) {
		boolean retVal = false;
		@SuppressWarnings("unused")
		boolean isTktExpired = false;
		try {
			if( ticketInfoDTO != null ) {
				// Handled CIS related CR# 72187
				// For CIS barcode, at the time of cancellation, not coming in the active state.
				if( (composite.getLblRedeem() != null && composite.getLblRedeem().isEnabled()
						&& form.getBarCode() != null && form.getBarCode().trim().length() != 0) 
						|| (composite.getLblRedeem() != null && ticketInfoDTO.getAcnfNumber().startsWith("T")
							&& form.getBarCode() != null && form.getBarCode().trim().length() != 0)
						|| (composite.getLblRedeem() != null && ticketInfoDTO.getAcnfNumber().startsWith("I")
							&& form.getBarCode() != null && form.getBarCode().trim().length() != 0)	) {

					if( ticketInfoDTO != null && ticketInfoDTO.getStatus() == VoucherStatusEnum.EXPIRED ) {
						isTktExpired =true; 
					}
					TicketInfoDTO tkForm = null;
					ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
					tkForm = ServiceCall.getInstance().inquireVoucher(ticketInfoDTO);
					SessionUtility utility = new SessionUtility();
					String datePattern = utility.getApplicationDateFormat();
					
					if( tkForm != null && tkForm.getStatus() == VoucherStatusEnum.PENDING && !isVoucherPendingAtDiffLoc ) {
						retVal = MessageDialogUtil.displayTouchScreenQuestionDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_REDEEM_PENDING,new String[] {AppContextValues.getInstance().getTicketText()}));
						if( !retVal ) {
							setScreenReady(false);
							if( barcodeBackup != null ) {
								form.setBarCode(barcodeBackup);
								populateScreen(composite);
							}
							return retVal;
						} else {
							setScreenReady(true);
							retVal = true;
							// Cancel redeem request.
							TicketInfoDTO tktInfoDTO = new TicketInfoDTO();
							if( barcodeBackup != null ) {
								tktInfoDTO.setBarcode(barcodeBackup);
							} else {
								tktInfoDTO.setBarcode(form.getBarCode());
							}
							long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
							
							tktInfoDTO.setSessionId(getCurrentSessionId);
							tktInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(userDTO));
							tktInfoDTO.setEmployeeId(employeeId);
							tktInfoDTO.setTicketType(getTicketType(form.getBarCode()));
							tktInfoDTO.setGameBarcode(false);
							tktInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
							tktInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
							tktInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
	
							TicketInfoDTO updatedTicketInfoDTO = ServiceCall.getInstance().cancelRedeemVoucher(tktInfoDTO);
	
							if( updatedTicketInfoDTO != null && updatedTicketInfoDTO.isErrorPresent() ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
							} else {
								if( formExists && !clearValues ) {
									form.setBarCode(updatedTicketInfoDTO.getBarcode());
									form.setState(updatedTicketInfoDTO.getTktStatus());
									form.setAmount(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));
									String eff = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getEffectiveDate().getTime()).toString();
									String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT,datePattern);
									form.setEffectiveDate(effectiveDate);
									String exp = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getExpireDate().getTime()).toString();
									String expireDate = DateHelper.getFormatedDate(exp, IVoucherConstants.DATE_FORMAT,datePattern);
									form.setExpireDate(expireDate);
									form.setLocation(ticketInfoDTO.getTransAssetNumber());
									form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
									populateScreen(composite);
									disableRedeem();
									disableOverride();
								}
								if( formExists && clearValues ) {
									resetFormValues();
									disableRedeem();
									disableOverride();
									disableCancel();
								}
							}
						}
					} else {
						retVal = true;
						setScreenReady(true);
					}
				} else {
					retVal = true;
					setScreenReady(true);
				}
			}
		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while inquring the Voucher", ex);
			List<Message> listMsg = ex.getErrorMessages();
			List<String> errorMsg = new ArrayList<String>();
			for( int i = 0; i < listMsg.size(); i++ ) {
				errorMsg.add((listMsg.get(i)).getMessageKey());
			}			
			if( errorMsg.contains(IVoucherConstants.VOUCHER_ASSET_NOT_VALID) ) {
				log.debug("Tkt expired. Don't do anything");
				resetValues();				
				composite.getTxtBarCode().setFocus();
			}else {
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
				if( (!errorMsg.contains(IVoucherConstants.VOUCHER_PENDIG_AT_DIFF_LOCATION)) && !errorMsg.contains(IVoucherConstants.VOUCHER_ASSET_NOT_VALID) ) {				
					cancelReedemVoucher();
				}
			}

			setScreenReady(true);
		} catch (Exception ex) {
			log.error("Exception while getting the Voucher", ex);
			VoucherUIExceptionHandler.handleException(ex);
			setScreenReady(true);
		}
		return retVal;
	}

	/**
	 * This method will cancel the redemption process of the voucher which will
	 * bring the voucher back to active status
	 */
	private void cancelReedemVoucher() {

		try {
			if( composite.getLblRedeem() != null && composite.getLblRedeem().isEnabled() ) {
				TicketInfoDTO tkForm = null;
				ticketInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
				tkForm = ServiceCall.getInstance().inquireVoucher(ticketInfoDTO);
				SessionUtility utility = new SessionUtility();
				String datePattern = utility.getApplicationDateFormat();

				if( tkForm != null && tkForm.getStatus() == VoucherStatusEnum.PENDING ) {
					
					long getCurrentSessionId = ServiceCall.getInstance().getCurrentSessionId(SDSApplication.getLoggedInUserID(), SDSApplication.getSiteDetails().getId());
					
					// Cancel redeem request.
					TicketInfoDTO tktInfoDTO = new TicketInfoDTO();
					
					tktInfoDTO.setSessionId(getCurrentSessionId);
					tktInfoDTO.setBarcode(form.getBarCode());
					tktInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(userDTO));
					tktInfoDTO.setEmployeeId(employeeId);
					tktInfoDTO.setTicketType(getTicketType(form.getBarCode()));
					tktInfoDTO.setGameBarcode(false);
					tktInfoDTO.setTransAssetNumber(PreferencesUtil.getClientAssetNumber());
					tktInfoDTO.setOptimisticLockVersion(form.getOptLockVersion());
					tktInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());

					TicketInfoDTO updatedTicketInfoDTO = ServiceCall.getInstance().cancelRedeemVoucher(tktInfoDTO);

					if( updatedTicketInfoDTO != null && updatedTicketInfoDTO.isErrorPresent() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(updatedTicketInfoDTO));
					}

					form.setBarCode(updatedTicketInfoDTO.getBarcode());
					form.setState(updatedTicketInfoDTO.getTktStatus());
					form.setAmount(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(updatedTicketInfoDTO.getAmount())));
					String eff = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getEffectiveDate().getTime()).toString();
					String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT, datePattern);
					form.setEffectiveDate(effectiveDate);
					String exp = DateUtil.getLocalTimeFromUTC(updatedTicketInfoDTO.getExpireDate().getTime()).toString();
					String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT, datePattern);
					form.setExpireDate(expireDate);
					form.setLocation(tktInfoDTO.getTransAssetNumber());
					form.setOptLockVersion(updatedTicketInfoDTO.getOptimisticLockVersion());
					populateScreen(composite);
				}
			}

		} catch (VoucherEngineServiceException ex) {
			log.error("Exception while inquring the Voucher", ex);
			// Ignoring the DB Manipulation(NJ Requirement) message to be display on the UI as it appears twice. One from NAV Check and one from here.
			if( !VoucherUtil.isDBManipulationMessage(ex))
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
		} catch (Exception ex) {
			log.error("Exception while getting the Voucher", ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}

	/**
	 * This method displays the message in the customer display device
	 */
	public void displayMessage() {
		Display display = null;
		String amount = ConversionUtil.voucherAmountFormat(new Double(getMultiRedeemAmount()).toString()).trim();
		String displayMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_REDEEM_MSG) + "(" + CurrencyUtil.getCurrencySymbol() + ")" + amount;

		// CALL THE DISPLAY DEVICE METHOD TO DISPLAY THE MESSAGE
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isCustomerDisplay = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY);
		if( isCustomerDisplay ) {
			String printerErrorMessage = null;
			boolean printerError = false;
			try {
				preferenceStore = PlatformUI.getPreferenceStore();
				String dispModel = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL);
				String dispManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER);
				String dispPort = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT);
				String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
				String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
				if( Util.isEmpty(dispModel) || Util.isEmpty(dispManufacturer)|| Util.isEmpty(dispPort) ) {
					preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE,IVoucherConstants.CUST_DISP_PIONEER_MANFT
							+ " " + IVoucherConstants.CUST_DISP_PIONEER_MODEL);
					preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT,IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
					preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER,IVoucherConstants.CUST_DISP_PIONEER_MANFT);
					preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL,IVoucherConstants.CUST_DISP_PIONEER_MODEL);
					preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER,IVoucherConstants.CUST_DISP_PIONEER_DRIVER);

				}
				log.debug("disp model: " + dispModel);
				log.debug("disp manuf: " + dispManufacturer);
				log.debug("disp port: " + dispPort);
				log.debug("disp driver: " + dispDriver);
				log.debug("disp type: " + dispType);

				Class.forName(dispDriver);
				display = Display.getDisplay(dispType);
				if( display != null ) {
					display.close();
				}
				display.open(dispPort);
				display.blankDisplay();
				display.dispMsg(displayMessage);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_CLASS_DEF) + e1.getMessage();
			} catch (DisplayNoSuchDriverException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_DRIVER) + e1.getMessage();
			} catch (Exception e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN) + e1.getMessage();
				log.error("Error occured while connecting to the display device: ", e1);
			}

			if( printerError) {
				if( printerErrorMessage != null ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
				} else {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN));
				}
			} else {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_CONNECTION_SUCESS));
			}
		}
	}

	/**
	 * This method prints the receipt for the redeemed ticket amount
	 * 
	 * @param updatedTicketInfoDTO
	 */
	public void printReceipt(TicketInfoDTO updatedTicketInfoDTO) {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isReceiptPrinting = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_RECEIPT_PRINTING);
		boolean printerError = false;
		String printerErrorMessage = "";
		if( isReceiptPrinting ) {
			VoucherImage img = new VoucherImage();
			TicketDetails details = img.copyObj(updatedTicketInfoDTO);
			try {
				String printerModel = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MODEL);
				String printerManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_MANUFACTURER);
				String printerPort = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT);
				String printerDriver = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_DRIVER);
				String printerType = preferenceStore.getString(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE);
				if( Util.isEmpty(printerModel) || Util.isEmpty(printerManufacturer) || Util.isEmpty(printerPort) ) {
					preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_PORT,IVoucherConstants.VOU_PREF_TKT_PRINT_COM1);
					preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE,IVoucherConstants.VOU_PREF_TKT_PRINT_TYPE1);
					preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER,IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
					preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL,IVoucherConstants.TKTSCANNER_ITHACA_MODEL);
					preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER,IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
				}
				log.debug("Printer model: " + printerModel);
				log.debug("Printer manuf: " + printerManufacturer);
				log.debug("Printer port: " + printerPort);
				log.debug("Printer driver: " + printerDriver);
				ProgressIndicatorUtil.openInProgressWindow();
				
				if( !(printerPort).equals(IVoucherConstants.VOU_PREF_TKT_PRINT_LPT1) ) {
					Printer printer = null;
					Class.forName(printerDriver);
					printer = Printer.getPrinter(printerManufacturer, printerModel);
					printer.open(printerPort);
					@SuppressWarnings("unused")
					String printerStatus = "";
					try {
						printerStatus = printer.getStatus();
					} catch (IOException e1) {
						throw new PrinterException(LabelLoader.getLabelValue(ITicketConstants.VOU_REC_PRINTER_CONNECTION_ERROR));
					}	
					printer.printCustomerReceipt(details, SDSApplication.getLoggedInUserID());

				} else {
					EpsonWithLPTPort epsonWithLPTPort = new EpsonWithLPTPort();
					epsonWithLPTPort.printCustomerReceipt(details, SDSApplication.getLoggedInUserID(),printerType);
				}
			} catch (PrinterNoSuchDriverException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN_NO_DRIVER) + e1.getMessage();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN_NO_CLASS_DEF) + e1.getMessage();
			} catch (PrinterException e1) {
				e1.printStackTrace();
				printerError = true;
				printerErrorMessage = e1.getMessage();
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error("Error occured while printing the recepit: ", e1);
				if( !e1.getMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT) ) {
					printerError = true;
					printerErrorMessage = e1.getMessage();
				}
			} finally {
				ProgressIndicatorUtil.closeInProgressWindow();
			}
			if( printerError ) {
				if( Util.isEmpty(printerErrorMessage) ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.REC_PRINT_EXPTN));
				} else {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(printerErrorMessage);
				}
			} else {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECEIPT_PRINT_CONNECTION_SUCESS));
			}
		}
	}

	public void modifyText(ModifyEvent e) {
		if( e.getSource() instanceof SDSTSText ) {
			SDSTSText barcodeText = (SDSTSText) e.getSource();
			if( barcodeText.getName().equalsIgnoreCase(
					IVoucherConstants.REDEEMVOUCHER_CTRL_TXT_BARCODE) ) {
				if( composite.getLblRedeem() != null
						&& composite.getLblRedeem().isEnabled()
						&& !modifyTextCalled ) {
					if( form.getBarCode() != null
							&& form.getBarCode().trim().length() != 0 ) {
						modifyTextCalled = true;
						navCheck(true, true);
						modifyTextCalled = false;
					}
				}
			}
		}
	}

	/**
	 * This method is use to insert the player card id for the ticket entered for redeeming
	 * 
	 * @param playerId
	 * @return
	 */
	public boolean enterPlayerId(String playerId) {
		boolean retValue = false;
		try {
			if( playerId == null ) {
				return false;
			} else {
				String errorMsg = VoucherUIValidator.validateEnteredPlayerId(playerId);
				if( errorMsg.trim().length() != 0 ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(errorMsg);
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell(), SWT.None | SWT.APPLICATION_MODAL);
					dialog.setLocation(170, 250);
					String result = null;
					dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PLAYERID_REQD));
					dialog.setAltErrorText(true);
					result = dialog.open(10, false);
					return enterPlayerId(result);
				} else {
//					ServiceCall.getInstance().insertPlayerCardId(form.getBarCode(), playerId,(int)SDSApplication.getUserDetails().getSiteId());
					playerCardId = playerId;
					tktInfoDTOFromPopulate.setPlayerId(playerCardId);
					retValue = true;
				}
			}
		} catch (VoucherEngineServiceException e) {
			log.error("VoucherEngineServiceException occured while inserting the player card id",e);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(e));
		} catch (Exception e) {
			log.error("Exception occured while inserting the player card id",e);
			VoucherUIExceptionHandler.handleException(e);
		}
		return retValue;

	}

	private void enableOverride(){
		composite.getLblOverride().setEnabled(true);
		composite.getLblOverride().setImage(enableImage);
		composite.getLblOverride().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableOverride(){
		composite.getLblOverride().setEnabled(false);
		composite.getLblOverride().setImage(disableImage);
		composite.getLblOverride().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableRedeem(){
		composite.getLblRedeem().setEnabled(true);
		composite.getLblRedeem().setImage(enableImage);
		composite.getLblRedeem().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableRedeem(){
		composite.getLblRedeem().setEnabled(false);
		composite.getLblRedeem().setImage(disableImage);
		composite.getLblRedeem().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableCancel(){
		composite.getLblCancel().setEnabled(true);
		composite.getLblCancel().setImage(enableImage);
		composite.getLblCancel().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableCancel(){
		composite.getLblCancel().setEnabled(false);
		composite.getLblCancel().setImage(disableImage);
		composite.getLblCancel().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableMultiRedeem(){
		composite.getLblMultiRedeem().setEnabled(true);
		composite.getLblMultiRedeem().setImage(enableImage);
		composite.getLblMultiRedeem().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableMultiRedeem(){
		composite.getLblMultiRedeem().setEnabled(false);
		composite.getLblMultiRedeem().setImage(disableImage);
		composite.getLblMultiRedeem().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableVoid() {
		composite.getLblVoid().setEnabled(true);
		composite.getLblVoid().setImage(enableImage);
		composite.getLblVoid().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableVoid() {
		composite.getLblVoid().setEnabled(false);
		composite.getLblVoid().setImage(disableImage);
		composite.getLblVoid().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	

}
