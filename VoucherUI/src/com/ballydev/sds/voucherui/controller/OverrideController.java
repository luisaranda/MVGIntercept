/*****************************************************************************
 * $Id: OverrideController.java,v 1.15, 2011-02-10 11:41:24Z, Verma, Nitin Kumar$
 * $Date: 2/10/2011 5:41:24 AM$
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
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
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
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.enumconstants.ErrorCodeEnum;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.enumconstants.TransactionReasonErrorEnum;
import com.ballydev.sds.voucher.enumconstants.UserRoleEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.OverrideComposite;
import com.ballydev.sds.voucherui.composite.VoucherHeaderComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.OverrideForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.shell.DialogShellForOverride;
import com.ballydev.sds.voucherui.util.AresObject;
import com.ballydev.sds.voucherui.util.AresUtil;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.Text;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This Class is used to handle override functionality for those tickets 
 * which are not present in the database but printed from the valid slot machine. 
 * @author VNitinkumar
 */
public class OverrideController extends SDSBaseController {
	
	/**
	 * Instance of Composite where default screen will populate
	 */
	private static Composite middleComp = null; 
	
	/**
	 *Instance of the voucher header composite 
	 */
	private static VoucherHeaderComposite headerComp = null;

	/**
	 *Instance of OverrideComposite
	 */
	private OverrideComposite overrideComposite = null;
	
	/**
	 *Instance of OverrideForm
	 */
	private OverrideForm overrideForm = null;

	/**
	 * The maximum number of records allowed to display in each page as there is
	 * no scroll bar in a Touch Screen UI.
	 */
	private int numOfRecordsPerPage = 5;

	/**
	 * This field returns this total number of reconciled vouchers in the database.
	 */
	private int totalRecordCount = 0;

	/**
	 * the mode of totalRecordCount by numOfRecordsPerPage
	 */
	private int modValue = 0;

	/**
	 * Total number of pages the records run into.
	 */
	private int numOfPages = 0;

	/**
	 * Holds the value of the current page number
	 */
	private int currentPage = 0;

	/**
	 * This field is used in selecting the next page for batch details records. This
	 * variable holds the index of the first record in each page - 1.
	 */
	private int initialIndex = 0;

	/** Instance of Ares */
	private static Ares ares;
	
	/**
	 * Instance of Image used to show images in enable mode
	 */
	private Image enableImage;
	
	/**
	 * Instance of Image used to show images in disable mode
	 */
	private Image disableImage;
	
	public static long totalAmount = 0l;
	
	String amount = "0.00";
	
	/**
	 * Backup of the ticket info list DTO result from the service call
	 */
	private List<TicketInfoDTO> tktInfoDTOList = new ArrayList<TicketInfoDTO>();
	
	/**
	 * Instance of logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	
	/**
	 * Constructor of OverrideController
	 * @param parent
	 * @param style
	 * @param overrideFrm
	 * @param pValidator
	 * @param vHomeComp
	 * @throws Exception
	 */
	public OverrideController(Composite parent,int style,OverrideForm overrideFrm, SDSValidator pValidator, VoucherHeaderComposite vHomeComp)
	throws Exception {
		super(overrideFrm, pValidator);
		middleComp = parent;
		headerComp = vHomeComp;
		overrideComposite = new OverrideComposite(parent,style);
		this.overrideForm = overrideFrm;
		VoucherMiddleComposite.setCurrentComposite(overrideComposite);
		super.registerEvents(overrideComposite);
		registerCustomizedListeners(overrideComposite);

		enableImage = overrideComposite.getButtonImage();
		disableImage = overrideComposite.getButtonDisableImage();
		
		overrideFrm.addPropertyChangeListener(this);
		adjustTableSizeBasedOnResolution();
		loadTicketDetailFromResponse();
		populateScreen(overrideComposite);
		parent.layout();
		overrideComposite.getTxtBarcode().setFocus();
		totalAmount = 0l;
		try {
			amount = Text.convertDollar((long)(totalAmount));
		} catch (Throwable t) {
			//eat up
		}
		overrideComposite.getLblTotalTktAmount().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_TOTAL_AMOUNT) + ":  " + amount);
		disableDeleteBtn();
		disableAuthBtn();
		setScanner();
	}

	// Method to set scanner event for the Barcode text box, only single time for the particular view.
	private void setScanner() {

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean isBarcodeScannerEnabled = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER);
		if( isBarcodeScannerEnabled ) {
			ares = AresUtil.registerAresEvent(AresObject.getAres());
			if( ares != null ) {
				ares.addAresEventListener(new AresEventListener(){
					public void aresEvent(AresEvent ae) {
						try {
							if( overrideComposite != null && !overrideComposite.isDisposed() && ae instanceof AresBarcodeEvent) {
								String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
								final String barcode = ((AresBarcodeEvent)ae).getBarcode();
								if( barcode != null && barcode.length() == 18 
										|| (barcode != null && stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) 
												&& barcode.length() == 16 )) {
									log.debug("Barcode: " + barcode);
									ares.accept();
									overrideComposite.getDisplay().syncExec(new Runnable() {
										public void run(){
											if( overrideComposite.getTxtBarcode().isFocusControl() ) {
												overrideComposite.getTxtBarcode().setText(barcode);
//												overrideForm.setBarCode(barcode);
											}
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

	@Override
	public void mouseDown(MouseEvent e) {

		String bcode    = overrideComposite.getTxtBarcode().getText();
		String barcode  = bcode;
		String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
		
		if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
			barcode = "00" + barcode.trim();
			overrideComposite.getTxtBarcode().setText(barcode);
		}

		try {
			populateForm(overrideComposite);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Control control = (Control) e.getSource();
		if( ((Control) e.getSource() instanceof SDSImageLabel) ) {
			
			if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE) ) {

				try {
					if( overrideForm.getPlayerId().equals(IVoucherConstants.PLAYER_CARD_ZERO) ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.ZERO_PLAYER_ID));
						return;
					}
					if( !(overrideForm.getBarCode().startsWith("5") || overrideForm.getBarCode().startsWith("6") || overrideForm.getBarCode().startsWith("00")) ) {
						if( overrideForm.getBarCode().length() == 18 ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_NOT_FROM_SLOT));
							return;
						}
					}

					if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
						overrideForm.setBarCode(barcode);
					}
					
					boolean isValidate = validate(IVoucherConstants.OVERRIDE_VOUCHER_FORM, overrideForm, overrideComposite);
					if(!isValidate)
						return;
					
					IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
					String locationValueFromPreference    = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
					String employeeTypeCashierPreference  = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_IS_CASHIER);
					
					TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
					ticketInfoDTO.setBarcode(overrideForm.getBarCode());
					ticketInfoDTO.setAmount(new Long(ConversionUtil.dollarToCents(overrideForm.getAmount())));
					ticketInfoDTO.setEffectiveDate(new java.sql.Timestamp(overrideComposite.getTicketCreatedTime().getSelection().getTime()));
					ticketInfoDTO.setTransAssetNumber(locationValueFromPreference);
					ticketInfoDTO.setEmployeeId(SDSApplication.getLoggedInUserID());
					ticketInfoDTO.setAcnfNumber(overrideForm.getAssetNumber());
					
					ticketInfoDTO.setTicketType(TicketTypeEnum.NON_SLOT_GENERATED);
					ticketInfoDTO.setGameBarcode(false);
					
					if(	overrideForm.getBarCode().startsWith("6") ) {
						ticketInfoDTO.setTicketType(TicketTypeEnum.SLOT_GENERATED_NEW_JERSEY);
					}
					if( overrideForm.getPlayerId() == null || overrideForm.getPlayerId().equals("") ) {
						ticketInfoDTO.setPlayerId(IVoucherConstants.DEFAULT_PLAYER_ID);
						ticketInfoDTO.setPlayerCardReqd("0");
					} else {
						ticketInfoDTO.setPlayerId(overrideForm.getPlayerId());
						ticketInfoDTO.setPlayerCardReqd("0");
					}
					
					ticketInfoDTO.setAmountType(AmountTypeEnum.CASHABLE);
					ticketInfoDTO.setTktPrintTime(new java.sql.Timestamp(overrideComposite.getTicketCreatedTime().getSelection().getTime()));
					ticketInfoDTO.setTransactionReason(TransactionReasonErrorEnum.TICKET_NOT_FOUND.getTransactionReason());
					
					if( employeeTypeCashierPreference.trim().equals("true") )
						ticketInfoDTO.setUserRoleTypeId(UserRoleEnum.CASHIER_WORK_STATION.getUserRoleTypeId());	
					else
						ticketInfoDTO.setUserRoleTypeId(UserRoleEnum.AUDITOR_WORK_STATION.getUserRoleTypeId());
					
					ticketInfoDTO.setChkForEmpMaxAmt(true);
					
					SessionUtility sessionUtility = new SessionUtility();
					
					int siteId 			= Integer.valueOf(sessionUtility.getSiteDetails().getNumber());
					int barcodeAssetNo 	= Integer.valueOf(overrideForm.getAssetNumber().trim());
					int inputAssetNo   	= Integer.valueOf(ticketInfoDTO.getBarcode().substring(4,9).trim());
					int barcodeSiteId  	= Integer.valueOf(ticketInfoDTO.getBarcode().substring(1,4).trim());
					
					if ( VoucherUtil.isGameBarcode(ticketInfoDTO.getBarcode()) == 1 ){
						inputAssetNo = Integer.valueOf(ticketInfoDTO.getBarcode().substring(3,8).trim());
					}

					if ( VoucherUtil.isGameBarcode(ticketInfoDTO.getBarcode()) == 2 ){
						inputAssetNo = barcodeAssetNo;
					}

					double maxTktRedemptionAmt = Double.parseDouble(SiteUtil.getMaxTktRedemptionAmt());
					
					if( barcodeAssetNo != inputAssetNo ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ASSET_MISMATCH));
						return;
					}
					if( siteId != barcodeSiteId && VoucherUtil.isGameBarcode(ticketInfoDTO.getBarcode()) == 0 ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_NOT_FROM_LOGGED_IN_SITE));
						return;
					}
					for( TicketInfoDTO td : tktInfoDTOList ) {
						if( overrideForm.getBarCode().equals(td.getBarcode()) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE_ALREADY_EXIST));
							return;
						}
					}
					TicketInfoDTO ticketInfoDTO2 = ServiceCall.getInstance().verifyVoucherForOverride(ticketInfoDTO, siteId);

					if( ticketInfoDTO2.getErrorCodeId() == ErrorCodeEnum.SUCCESS.getErrorCode() ) {
						for( TicketInfoDTO tdto : tktInfoDTOList ) {
							if( tdto.getPlayerId().equals(ticketInfoDTO2.getPlayerId()) 
									&& !ticketInfoDTO2.getPlayerId().equals(IVoucherConstants.PLAYER_CARD_ZERO) )
								ticketInfoDTO2.setErrorCodeId(ErrorCodeEnum.OVERRIDE_PLAYER_ID_IN_MULTIPLE_OVERRIDES.getErrorCode());
						}
					}
					
					if( (double) (ticketInfoDTO2.getAmount()/100) > Double.parseDouble((ConversionUtil.centsToDollar(maxTktRedemptionAmt))) ) {
						ticketInfoDTO2.setErrorCodeId(ErrorCodeEnum.OVERRIDE_MAX_REDEEMED_AMOUNT_EXCEEDED.getErrorCode());
					}
					
					tktInfoDTOList.add(ticketInfoDTO2);
					overrideForm.setLstTktInfoDTO(tktInfoDTOList);
					
					totalAmount = totalAmount + new Long(ConversionUtil.dollarToCents(overrideForm.getAmount()));
					
					String totalTktAmount = "0.00";
					try {
						totalTktAmount = Text.convertDollar((long)(totalAmount));
					} catch (Throwable t) {
						// eat up
					}
					overrideComposite.getLblTotalTktAmount().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_TOTAL_AMOUNT) + ":  " + totalTktAmount);
					
					clearValues();
					adjustTableSizeBasedOnResolution();
					loadTicketDetailFromResponse();
					populateScreen(overrideComposite);
					enableAuthBtn();
					disableDeleteBtn();
					
				} catch(VoucherEngineServiceException ex) {
					log.error("Exception while verifying the barcode basic CRC verification and sequence etc. ", ex);
					VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					overrideComposite.getTxtBarcode().setFocus();
					
				} catch (Exception ex){
					log.error("Exception while verifying the barcode basic CRC verification and sequence etc. ",ex);
					VoucherUIExceptionHandler.handleException(ex);
					overrideComposite.getTxtBarcode().setFocus();
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEVOUCHER_CTRL_BTN_DELETE) ) {
				try {
					populateForm(overrideComposite);
					TicketInfoDTO tInfoDTO = overrideForm.getTicketListDTOSelectedValue();
					
					if( (tInfoDTO == null ) || tInfoDTO.getBarcode() == null ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TABLE_ITEM));
						return;
					}
					
					for( TicketInfoDTO tktInfoDTO : tktInfoDTOList ) {
						if( tktInfoDTO.getBarcode().equals(tInfoDTO.getBarcode()) ) {
							tktInfoDTOList.remove(tktInfoDTO);
							overrideForm.setLstTktInfoDTO(tktInfoDTOList);
							totalAmount = totalAmount - tktInfoDTO.getAmount();
							break;
						}
					}
					if( overrideComposite.getTableViewer().getTable().getSelectionIndex() == -1 ) {
						disableDeleteBtn();
					}
					if( totalAmount == 0l ) {
						disableAuthBtn();
					}
					
					try {
						amount = Text.convertDollar((long)(totalAmount));
					} catch (Throwable t) {
						// eat up
					}
					overrideComposite.getLblTotalTktAmount().setText("Total Amount:  " + amount);

					adjustTableSizeBasedOnResolution();
					loadTicketDetailFromResponse();
					
					populateScreen(overrideComposite);
					
					overrideForm.setTicketListDTOSelectedValue(null);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.OVERRIDEVOUCHER_CTRL_BTN_AUTHORISE) ) {
				try {
					for( TicketInfoDTO tDTO : tktInfoDTOList ) {
						if( tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_INVALID_LOCATION.getErrorCode() 
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_CRC_CHECK_FAILED.getErrorCode() 
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_RESTRICTED_LOCATION.getErrorCode()
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_MAX_REDEEMED_AMOUNT_EXCEEDED.getErrorCode()
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_UNKNOWN_ERROR.getErrorCode()
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_ALREADY_REDEEMED.getErrorCode() ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_WITH_ERROR_MSG));
							return;
						}
						if( tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_PREVIOUS_TICKET_OUT_OF_SEQUENCE.getErrorCode() 
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_NEXT_TICKET_OUT_OF_SEQUENCE.getErrorCode()
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_PLAYER_ID_IN_MULTIPLE_OVERRIDES.getErrorCode()
								|| tDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_ALREADY_EXISTS.getErrorCode() ) {
							boolean retVal = MessageDialogUtil.displayTouchScreenQuestionDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_WITH_WARNING_MSG));
							if( retVal ) {
								break;
							} else
								return;
						}
					}
					@SuppressWarnings("unused")
					DialogShellForOverride dialogShellForOverride = new DialogShellForOverride(overrideComposite.getShell(), this);
					disableDeleteBtn();
					
				} catch (Exception e1) {
					log.error("Exception while displaying the Override shell. ", e1);
				}
			}
	
			/** User clicked next arrow to get next page of records **/
			else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_NEXTARROW_ARROW) ) {				
				try {
					showNextPage();
				} catch (Exception e1) {
					log.error("Exception while pressed next button. ", e1);
					e1.printStackTrace();
				}				
			}
	
			/** User clicked previous arrow to get previous page of records **/
			else if(((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_PREVIOUS_ARROW)) {
				try {
					showPreviousPage();
				} catch (Exception e1) {
					log.error("Exception while pressed previous button. ", e1);
					e1.printStackTrace();
				}
			}
	
			/** User clicked first page arrow to get previous page of records **/
			else if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW)) {
				try {
					if(totalRecordCount > numOfRecordsPerPage){
						initialIndex = (numOfRecordsPerPage*2) < totalRecordCount ? (numOfRecordsPerPage*2) : totalRecordCount;
						showPreviousPage();
						currentPage = 1;
						overrideComposite.getLblTotalPageCount().setText(
								/*pageCountString +*/ currentPage + "/" + numOfPages);
						overrideComposite.layout();
						HideAndShowPageButtons();
					}
				} catch (Exception e1) {
					log.error("Exception while pressed first arrow button. ", e1);
				}
			}
	
			/** User clicked last page arrow to get previous page of records **/
			else if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_LAST_ARROW)) {
				try {
					if(totalRecordCount > numOfRecordsPerPage){
						if(modValue > 0) {
							initialIndex = totalRecordCount - modValue;						
						}else if (modValue == 0){
							initialIndex = totalRecordCount - numOfRecordsPerPage;
						}
						showNextPage();
						currentPage = numOfPages;
						overrideComposite.getLblTotalPageCount().setText(
								/*pageCountString +*/ currentPage + "/" + numOfPages);
						overrideComposite.layout();
						HideAndShowPageButtons();
					}
				} catch (Exception e1) {					
					log.error("Exception while pressed last button. ", e1);
				}
			}
		}
	}

	/**
	 * After submit clear the UI controls.
	 */
	private void clearValues() {
		overrideForm.setBarCode("");
		overrideForm.setAmount("");
		overrideForm.setAssetNumber("");
		overrideForm.setPlayerId("");
		overrideComposite.getTxtBarcode().setFocus();
	}

	public void widgetSelected(SelectionEvent e) {
		try {
			eventOnTktNotFoundTable(e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void eventOnTktNotFoundTable(TypedEvent typedEvent) {
		try {
			if( ((Control) typedEvent.getSource() instanceof Table) ) {
				
				Table tableCtrl = (Table)((Control) typedEvent.getSource());	
				OverrideComposite overridComposite = (OverrideComposite)overrideComposite;
				
				if( tableCtrl.getData("name").equals(IVoucherConstants.OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SUMMARY) ) {
					overridComposite.getImgLblDelete().setEnabled(true);
					overridComposite.getImgLblDelete().setImage(enableImage);
				}
			}
		} catch (Exception ex) {
			log.error("Error occurred in OverrideController.eventOnBatchTableDetail", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Loads the details into the table viewer from dataQueries the DB and l 
	 */
	public void loadTicketDetailFromResponse()	{
		totalRecordCount = 0;
		modValue	 = 0;
		numOfPages	 = 0;
		currentPage  = 0;
		initialIndex = 0;

		overrideComposite.layout();
		
		ArrayList<TicketInfoDTO> localTktInfoDTO = new ArrayList<TicketInfoDTO>();

		if( tktInfoDTOList != null ) {
			totalRecordCount = tktInfoDTOList.size();
		}

		if( totalRecordCount > 0 ) {
			numOfPages = totalRecordCount/numOfRecordsPerPage;
			modValue   = totalRecordCount % numOfRecordsPerPage;
			if( modValue > 0 ) {
				numOfPages += 1;
			}
			if( totalRecordCount < numOfRecordsPerPage ) {
				numOfRecordsPerPage = totalRecordCount;
			}
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				TicketInfoDTO tktInforDTO = new TicketInfoDTO();
				com.ballydev.sds.voucherui.util.ObjectMapping.copyAlikeFields(tktInfoDTOList.get(i), tktInforDTO);
				localTktInfoDTO.add(tktInforDTO);
			}			
			currentPage++;
			initialIndex += numOfRecordsPerPage;

			overrideForm.setLstTktInfoDTO(localTktInfoDTO);
			overrideComposite.getLblTotalPageCount().setText(/*pageCountString + " " + */currentPage + "/" + numOfPages);
			overrideComposite.layout();

			try {
				if( overrideComposite.getTableViewer().getTable().getHorizontalBar().isVisible() ) {
					ScrollBar horizondalScroll = overrideComposite.getTableViewer().getTable().getHorizontalBar();				
					horizondalScroll.setThumb(1);

				}
				if( overrideComposite.getTableViewer().getTable().getVerticalBar().isVisible() ) {
					ScrollBar verticalScroll = overrideComposite.getTableViewer().getTable().getHorizontalBar();					
					verticalScroll.setMaximum(1);
				}
			} catch (SWTException e) {
				e.printStackTrace();
			}
		} else {
			overrideComposite.getLblTotalPageCount().setText(/*pageCountString + " " + */0 + "/" + 0);
		}
		
		HideAndShowPageButtons();
	}

	/**
	 * Show previous page table records.
	 * @throws Exception
	 */
	private void showPreviousPage() throws Exception{
		if( currentPage == 1 || currentPage == 0 ) {
			return;
		}

		currentPage--;
		overrideComposite.getLblTotalPageCount().setText(
				/*pageCountString +*/ currentPage + "/" + numOfPages);
		overrideComposite.layout();
		overrideComposite.getTableViewer().getTable().setSelection(-1);

		if( initialIndex > totalRecordCount - modValue ) {
			overrideForm.getLstTktInfoDTO().clear();

			int count = initialIndex - numOfRecordsPerPage - modValue;
			for (int i = count; i < count + numOfRecordsPerPage; i++ ) {
				TicketInfoDTO ticketInfoDTO = tktInfoDTOList.get(i);
				overrideForm.getLstTktInfoDTO().add(ticketInfoDTO);	

			}
			populateScreen(overrideComposite);
			initialIndex -= modValue;		

		} else if (initialIndex > numOfRecordsPerPage && initialIndex <= (totalRecordCount - modValue)) {
			initialIndex -= numOfRecordsPerPage;
			overrideForm.getLstTktInfoDTO().clear();				

			int count = initialIndex - numOfRecordsPerPage;
			for (int i = count; i < count + numOfRecordsPerPage; i++) {
				TicketInfoDTO ticketInfoDTO = tktInfoDTOList.get(i);
				overrideForm.getLstTktInfoDTO().add(ticketInfoDTO);	

			}
			populateScreen(overrideComposite);
		} 
		overrideComposite.layout();
		HideAndShowPageButtons();
	}

	/**
	 * Show next page table records.
	 * @throws Exception
	 */
	private void showNextPage() throws Exception{
		if( currentPage == numOfPages ) {
			return;
		}
		currentPage++;
		overrideComposite.getLblTotalPageCount().setText(/*pageCountString+*/currentPage+"/"+numOfPages);
		overrideComposite.layout();
		overrideComposite.getTableViewer().getTable().setSelection(-1);

		if (initialIndex < totalRecordCount - modValue) {
			overrideForm.getLstTktInfoDTO().clear();					
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				TicketInfoDTO ticketInfoDTO = tktInfoDTOList.get(i);
				overrideForm.getLstTktInfoDTO().add(ticketInfoDTO);				
			}
			populateScreen(overrideComposite);
			initialIndex += numOfRecordsPerPage;
		} else if (initialIndex < totalRecordCount) {
			overrideForm.getLstTktInfoDTO().clear();			
			for (int i = initialIndex; i < initialIndex + modValue; i++) {
				TicketInfoDTO ticketInfoDTO = tktInfoDTOList.get(i);
				overrideForm.getLstTktInfoDTO().add(ticketInfoDTO);			
			}
			populateScreen(overrideComposite);
			initialIndex += modValue;

		}
		overrideComposite.layout();
		HideAndShowPageButtons();
	}

	/**
	 * Method to adjust the table size based on the screen resolution
	 */
	public void adjustTableSizeBasedOnResolution() {

		int screenHeight =Display.getCurrent().getActiveShell().getBounds().width;
		int screenWidth =Display.getCurrent().getActiveShell().getBounds().height;

		if ((screenHeight == 800 || screenHeight == 802) && (screenWidth == 600 || screenWidth == 602)) {
			numOfRecordsPerPage = 4;
		}
		else if ((screenHeight == 1024 || screenHeight == 1026) && (screenWidth == 768 || screenWidth == 770)) {
			numOfRecordsPerPage = 5;
		}
		else if ((screenHeight == 1152 || screenHeight == 1154) && (screenWidth == 864 || screenWidth == 866)) {
			numOfRecordsPerPage = 7;
		}
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 768|| screenWidth == 770)) {
			numOfRecordsPerPage = 5;
		}
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 800 || screenWidth == 802)) {
			numOfRecordsPerPage = 6;
		}
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 1024 || screenWidth == 1026)) {
			numOfRecordsPerPage = 11;
		} else {
			numOfRecordsPerPage = 5;//Default
		}
	}
	
	private void HideAndShowPageButtons() {
		//Don't hide/show. Just Enable/Disable

		if( currentPage == numOfPages) {
			overrideComposite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
			overrideComposite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_LAST_PAGE)));
		}
		else {
			overrideComposite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_NEXT_PAGE)));
			overrideComposite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_LAST_PAGE)));
		}

		if( currentPage == 1 || currentPage == 0 ) {
			overrideComposite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
			overrideComposite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_FIRST_PAGE)));
		}	
		else {	
			overrideComposite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_PREVIOUS_PAGE)));
			overrideComposite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_FIRST_PAGE)));
		}	
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		overrideComposite.getTableViewer().getTable().addFocusListener(this);
		addTSTableColumnListener(overrideComposite.getTableViewer());
		
		OverrideComposite overrideComp = ((OverrideComposite)argComposite);
		
		overrideComp.getImgLblSubmit().addMouseListener(this);
		overrideComp.getImgLblSubmit().getTextLabel().addMouseListener(this);

		overrideComp.getImgLblDelete().addMouseListener(this);
		overrideComp.getImgLblAuthorise().addMouseListener(this);
		
		overrideComp.getILblFirstPageRecords().addMouseListener(this);
		overrideComp.getILblPreviousRecords().addMouseListener(this);
		overrideComp.getILblLastPageRecords().addMouseListener(this);
		overrideComp.getILblNextRecords().addMouseListener(this);

	}
	
	private void disableDeleteBtn() {
		overrideComposite.getImgLblDelete().setEnabled(false);
		overrideComposite.getImgLblDelete().setImage(disableImage);
	}

	private void disableAuthBtn() {
		overrideComposite.getImgLblAuthorise().setEnabled(false);
		overrideComposite.getImgLblAuthorise().setImage(disableImage);
	}

	private void enableAuthBtn() {
		overrideComposite.getImgLblAuthorise().setEnabled(true);
		overrideComposite.getImgLblAuthorise().setImage(enableImage);
	}

	@Override
	public Composite getComposite() {
		return overrideComposite;
	}

	/**
	 * @return the overrideComposite
	 */
	public OverrideComposite getOverrideComposite() {
		return overrideComposite;
	}

	/**
	 * @return
	 */
	public List<TicketInfoDTO> getTktInfoDTOList() {
		return tktInfoDTOList;
	}

	/**
	 * @return
	 */
	public OverrideForm getOverrideForm() {
		return overrideForm;
	}

	/**
	 * @return the headerComp
	 */
	public static VoucherHeaderComposite getHeaderComp() {
		return headerComp;
	}

	/**
	 * @return the middleComp
	 */
	public static Composite getMiddleComp() {
		return middleComp;
	}
	
}
