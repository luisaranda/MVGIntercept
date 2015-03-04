/*****************************************************************************
 *	 Copyright (c) 2006 Bally Technology  1977 - 2007
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
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
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.EnquiryInfoDTO;
import com.ballydev.sds.voucher.dto.TransactionListDTO;
import com.ballydev.sds.voucher.enumconstants.VoucherStatusEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.BarcodeEnquiryComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.BarcodeEnquiryForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.AresObject;
import com.ballydev.sds.voucherui.util.AresUtil;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This class acts as a controller for the barcode enquire composite
 * @author Nithya kalyani R
 * @version $Revision: 39$ 
 */
public class BarcodeEnquiryController extends SDSBaseController implements ModifyListener{

	/**
	 * Instance of BarcodeEnquiryComposite
	 */
	private BarcodeEnquiryComposite barcodeEnquiryComposite;

	/**
	 * Instance of  BarcodeEnquiryForm
	 */
	private BarcodeEnquiryForm barcodeEnquiryForm;

	/**
	 * Instance of logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Backup of the barcode entered
	 */
	private String barcodeBackup = null;

	/**
	 * Variable to track the call of the modify text event
	 */
	private boolean modifyTextCalled = false;

	private boolean setBarcodeCalled = false;

	/**
	 * The maximum number of records allowed to display in each page as there is
	 * no scrollbar in a Touch Screen UI.
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
	private int numOfPages=0;

	/**
	 * Holds the value of the current page number
	 */
	private int currentPage=0;


	/**
	 * This field is used in selecting the next page for batch details records. This
	 * variable holds the index of the first record in each page - 1.
	 */
	private int initialIndex = 0;

	/**
	 * Backup of the transaction list dto result from the service call
	 */
	private List<TransactionListDTO> transactionListDTOList = new ArrayList<TransactionListDTO>();

	/**
	 * Variable to track whether the clear method
	 * is called 
	 */
	private boolean clearValuesCalled = false;

	/** Instance of Ares */
	private static Ares ares;
	
	/**
	 * @param parent
	 * @param style
	 * @param barcodeEnquiryForm
	 * @param pValidator
	 * @throws Exception
	 */
	public BarcodeEnquiryController(Composite parent,int style,BarcodeEnquiryForm barcodeEnquiryForm, SDSValidator pValidator)
	throws Exception {
		super(barcodeEnquiryForm, pValidator);
		barcodeEnquiryComposite = new BarcodeEnquiryComposite(parent,style);
		this.barcodeEnquiryForm = barcodeEnquiryForm;
		VoucherMiddleComposite.setCurrentComposite(barcodeEnquiryComposite);
		parent.layout();
		super.registerEvents(barcodeEnquiryComposite);
		registerCustomizedListeners(barcodeEnquiryComposite);
		barcodeEnquiryForm.addPropertyChangeListener(this);	
		adjustTableSizeBasedOnResolution();
		loadBatchDetailsFromResponse();
		populateScreen(barcodeEnquiryComposite);
		barcodeEnquiryComposite.getTxtBarcode().setFocus();
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
							if ( barcodeEnquiryComposite != null && !barcodeEnquiryComposite.isDisposed() && ae instanceof AresBarcodeEvent) {
								String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
								final String barcode = ((AresBarcodeEvent) ae).getBarcode();
								if( barcode != null && barcode.length() == 18 
										|| (barcode != null && stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16 )) {
									log.debug("Barcode: "+barcode);
									ares.accept();												
									barcodeEnquiryComposite.getDisplay().syncExec(new Runnable() {
										public void run(){
											if( barcodeEnquiryComposite.getTxtBarcode().isFocusControl() ) {
												barcodeEnquiryComposite.getTxtBarcode().setText(barcode);
												String bcode    = barcodeEnquiryComposite.getTxtBarcode().getText();
												String barcode  = bcode;
												String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
												
												if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
													barcode = "00" + barcode.trim();
													barcodeEnquiryComposite.getTxtBarcode().setText(barcode);
												}
												barcodeEnquiryForm.setBarCode(barcode);

												populateBarcode();
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

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {		
		return barcodeEnquiryComposite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {

		BarcodeEnquiryComposite barcodeEnquiryComposite = ((BarcodeEnquiryComposite)argComposite);
		barcodeEnquiryComposite.getILblSubmitBarcode().addMouseListener(this);
		barcodeEnquiryComposite.getILblSubmitBarcode().getTextLabel().addMouseListener(this);
		barcodeEnquiryComposite.getILblSubmitBarcode().getTextLabel().addTraverseListener(this);
		barcodeEnquiryComposite.getILblCancel().addMouseListener(this);
		barcodeEnquiryComposite.getILblCancel().getTextLabel().addMouseListener(this);
		barcodeEnquiryComposite.getILblCancel().getTextLabel().addTraverseListener(this);
		barcodeEnquiryComposite.getILblFirstPageRecords().addMouseListener(this);
		barcodeEnquiryComposite.getILblPreviousRecords().addMouseListener(this);
		barcodeEnquiryComposite.getILblLastPageRecords().addMouseListener(this);
		barcodeEnquiryComposite.getILblNextRecords().addMouseListener(this);
		
		barcodeEnquiryComposite.getTxtBarcode().addModifyListener(this);
		
		Control ctrl[] = argComposite.getChildren();
		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof SDSTSText ) {
				((SDSTSText)ctrl[i]).addModifyListener(this);
			}

		}
	}

	/**
	 * This method takes the barcode and populate the corresponding
	 * details of the barcode in the screen
	 */
	private void populateBarcode() {

		try {
			SessionUtility utility = new SessionUtility();
			String datePattern = utility.getApplicationDateFormat();
			String barcode  = barcodeEnquiryForm.getBarCode();
			String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
			
			if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
				barcode = barcode.replace("**", "");
				barcode = "00" + barcode.trim();
				barcodeEnquiryForm.setBarCode(barcode);
			}

			boolean isValidate = validate(IVoucherConstants.ENQUIRE_VOUCHER_FORM,barcodeEnquiryForm,barcodeEnquiryComposite);

			if( !isValidate ) {
				setBarcodeCalled=true;
				return;
			}	

			if( barcodeEnquiryForm.getBarCode().trim().length() >= 14 ) {
				setBarcodeValue();
			}
			if( barcodeEnquiryForm.getBarCode().substring(0, 14).contains("*") ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_ERROR_CONTAIN_NUMERIC));
				barcodeEnquiryComposite.getTxtBarcode().selectAll();
				barcodeEnquiryComposite.getTxtBarcode().setFocus();
				setBarcodeCalled=true;
				return;
			}

			if( barcode.contains("*") && ((barcodeEnquiryForm.getEamount()==null ) ||
					barcodeEnquiryForm.getEamount().trim().length()==0) ) {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_ENTER_AMT));
				//barcodeEnquiryComposite.getTxtEAmount().setEditable(true);
				barcodeEnquiryForm.setEamount(null);
				barcodeEnquiryComposite.getTxtEAmount().setFocus();
				setBarcodeValue();
				return;
			}

			String amount = null;
			if(barcodeEnquiryForm.getEamount()!=null && barcodeEnquiryForm.getEamount().trim().length()>0) {
				amount = Long.toString(ConversionUtil.dollarToCents(barcodeEnquiryForm.getEamount()));						
			}
			EnquiryInfoDTO enquiryInfoDTO = ServiceCall.getInstance().getVoucherDetails(barcode,amount,(int)SDSApplication.getUserDetails().getSiteId());
			if( enquiryInfoDTO != null ) {
				clearValuesCalled = false;
				transactionListDTOList.clear();
				barcodeEnquiryForm.setAmount(ConversionUtil.centsToDollar(enquiryInfoDTO.getAmount()));						
				
				String eff = DateUtil.getLocalTimeFromUTC(enquiryInfoDTO.getEffectiveDate().getTime()).toString();
				String effectiveDate = DateHelper.getFormatedDate(eff,IVoucherConstants.DATE_FORMAT,datePattern);
				
				barcodeEnquiryForm.setEffectiveDate(effectiveDate);
				
				String exp = DateUtil.getLocalTimeFromUTC(enquiryInfoDTO.getExpireDate().getTime()).toString();
				String expireDate = DateHelper.getFormatedDate(exp,IVoucherConstants.DATE_FORMAT,datePattern);
				
				barcodeEnquiryForm.setExpireDate(expireDate);
//				barcodeEnquiryForm.setTxtOffline(enquiryInfoDTO.getOfflineStatus());
				
				if( enquiryInfoDTO.getOfflineStatus().equals("1") )
					barcodeEnquiryForm.setTxtOffline(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_YES));
				else
					barcodeEnquiryForm.setTxtOffline(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO));

				barcodeEnquiryForm.setTxtTicketType(enquiryInfoDTO.getAmountType());
				barcodeEnquiryForm.setState(enquiryInfoDTO.getTktStatus());

				if( enquiryInfoDTO.getTktStatus().equalsIgnoreCase(VoucherStatusEnum.EXPIRED.getStatus()) ) {
					for( int i =0;i<enquiryInfoDTO.getTrLsitDTO().size();i++ ) {
						if(enquiryInfoDTO.getTrLsitDTO().get(i).getLocation() == null ||
								enquiryInfoDTO.getTrLsitDTO().get(i).getLocation().trim().length()==0) {							
							enquiryInfoDTO.getTrLsitDTO().get(i).setLocation(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_LOCATION_SYSTEM));
							if( enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult() == null ||
									enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult().trim().length() == 0 ) {
								enquiryInfoDTO.getTrLsitDTO().get(i).setTransactionResult(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TR_REASON_EXPIRED));
							}
						}
						if(enquiryInfoDTO.getTrLsitDTO().get(i).getAssetNumber()==null ||
								enquiryInfoDTO.getTrLsitDTO().get(i).getAssetNumber().trim().length()==0) {							
							enquiryInfoDTO.getTrLsitDTO().get(i).setAssetNumber(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_LOCATION_SYSTEM));
							if(enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult()==null||
									enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult().trim().length()==0) {
								enquiryInfoDTO.getTrLsitDTO().get(i).setTransactionResult(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TR_REASON_EXPIRED));
							}
						}
						if(enquiryInfoDTO.getTrLsitDTO().get(i).getAssetNumber()==null ||
								enquiryInfoDTO.getTrLsitDTO().get(i).getAssetNumber().trim().length()==0) {							
							enquiryInfoDTO.getTrLsitDTO().get(i).setAssetNumber(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_LOCATION_SYSTEM));
							if(enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult()==null||
									enquiryInfoDTO.getTrLsitDTO().get(i).getTransactionResult().trim().length()==0) {
								enquiryInfoDTO.getTrLsitDTO().get(i).setTransactionResult(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TR_REASON_EXPIRED));
							}
						}	
					}
				}
				barcodeEnquiryForm.setTransactionListDTOList(enquiryInfoDTO.getTrLsitDTO());
				for( int i = 0; i < enquiryInfoDTO.getTrLsitDTO().size(); i++ ) {
					TransactionListDTO transactionListDTO = enquiryInfoDTO.getTrLsitDTO().get(i);
					transactionListDTOList.add(transactionListDTO);
				}
				adjustTableSizeBasedOnResolution();
				loadBatchDetailsFromResponse();
				populateScreen(barcodeEnquiryComposite);
				setBarcodeCalled = false;
			}else {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_BARCODE_NOT_EXIST, new String[] {AppContextValues.getInstance().getTicketText()}));
				clearValues();
				return;
			}
		} catch (VoucherEngineServiceException e1) {
			log.error("VoucherEngineServiceException while getting the transaction details of the voucher", e1);
			if(e1.getErrorCodes().get(0) == 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_BARCODE_CASINO_ID_NOT_MATCH, new String[] {AppContextValues.getInstance().getTicketText()}));
			} else 
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(e1));
			clearValues();
		} catch (Exception e1) {
			log.error("Exception while getting the transaction details of the voucher", e1);
			VoucherUIExceptionHandler.handleException(e1);
			clearValues();
		}

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		try {
			String bcode    = barcodeEnquiryComposite.getTxtBarcode().getText();
			String barcode  = bcode;
			String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
			
			if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
				barcode = "00" + barcode.trim();
				barcodeEnquiryComposite.getTxtBarcode().setText(barcode);
			}
			populateForm(barcodeEnquiryComposite);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Control control = (Control) e.getSource();
		if( ((Control) e.getSource() instanceof SDSImageLabel)) {
			if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE)) {
				populateBarcode();
			} else if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.VOIDVOUCHER_CTRL_BTN_CANCEL)) {
				clearAllValues();
			}
			/** User clicked next arrow to get next page of records **/
			else if(((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_NEXTARROW_ARROW)){
				try {
					showNextPage();
				} catch (Exception e1) {
					e1.printStackTrace();
				}				
			}

			/** User clicked previous arrow to get previous page of records **/
			else if(((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_PREVIOUS_ARROW)) {
				try {
					showPreviousPage();
				} catch (Exception e1) {
					e1.printStackTrace();
				}					
			}

			/** User clicked first page arrow to get previous page of records **/
			else if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW)) {
				try {
					if(totalRecordCount > numOfRecordsPerPage){
						initialIndex = (numOfRecordsPerPage*2) < totalRecordCount ? (numOfRecordsPerPage*2) : totalRecordCount;
						showPreviousPage();
						currentPage=1;
						barcodeEnquiryComposite.getLblTotalPageCount().setText(
								/*pageCountString +*/ currentPage + "/" + numOfPages);
						barcodeEnquiryComposite.layout();
						HideAndShowPageButtons();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
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
						currentPage=numOfPages;
						barcodeEnquiryComposite.getLblTotalPageCount().setText(
								/*pageCountString +*/ currentPage + "/" + numOfPages);
						barcodeEnquiryComposite.layout();
						HideAndShowPageButtons();					
					}
				} catch (Exception e1) {					
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Loads the details into the table viewer from dataQueries the DB and l 
	 *
	 */
	private void loadBatchDetailsFromResponse()	{
		totalRecordCount = 0;
		modValue	 = 0;
		numOfPages	 = 0;
		currentPage  = 0;
		initialIndex = 0;

		barcodeEnquiryComposite.layout();
		if( barcodeEnquiryForm.getTransactionListDTOList()!= null ) {
			barcodeEnquiryForm.getTransactionListDTOList().clear();
		}

		if( transactionListDTOList != null ){			
			totalRecordCount = transactionListDTOList.size();
		}

		if( totalRecordCount > 0 ) {
			numOfPages = totalRecordCount/numOfRecordsPerPage;
			modValue   = totalRecordCount % numOfRecordsPerPage;
			if( modValue > 0 ) {
				numOfPages += 1;
			}
			if(totalRecordCount < numOfRecordsPerPage){
				numOfRecordsPerPage = totalRecordCount;
			}
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				TransactionListDTO listDTO = new TransactionListDTO();
				com.ballydev.sds.voucherui.util.ObjectMapping.copyAlikeFields(transactionListDTOList.get(i), listDTO);
				barcodeEnquiryForm.getTransactionListDTOList().add(listDTO);
			}			
			currentPage++;
			initialIndex += numOfRecordsPerPage;

			barcodeEnquiryComposite.getLblTotalPageCount().setText(/*pageCountString + " " + */currentPage + "/" + numOfPages);
			barcodeEnquiryComposite.layout();

			try {
				if( barcodeEnquiryComposite.getTableViewer().getTable().getHorizontalBar().isVisible() ) {
					ScrollBar horizondalScroll = barcodeEnquiryComposite.getTableViewer().getTable().getHorizontalBar();				
					horizondalScroll.setThumb(1);

				}
				if( barcodeEnquiryComposite.getTableViewer().getTable().getVerticalBar().isVisible() ) {
					ScrollBar verticalScroll = barcodeEnquiryComposite.getTableViewer().getTable().getHorizontalBar();					
					verticalScroll.setMaximum(1);
				}
			} catch (SWTException e) {
				e.printStackTrace();
			}
		} else {
			barcodeEnquiryComposite.getLblTotalPageCount().setText(/*pageCountString + " " + */0 + "/" + 0);
		}

		HideAndShowPageButtons();
	}

	private void showPreviousPage() throws Exception{
		if(currentPage == 1){
			return;
		}

		currentPage--;
		barcodeEnquiryComposite.getLblTotalPageCount().setText(
				/*pageCountString +*/ currentPage + "/" + numOfPages);
		barcodeEnquiryComposite.layout();
		barcodeEnquiryComposite.getTableViewer().getTable().setSelection(-1);

		if (initialIndex > totalRecordCount - modValue) {
			barcodeEnquiryForm.getTransactionListDTOList().clear();

			int count = initialIndex - numOfRecordsPerPage - modValue;
			for (int i = count; i < count + numOfRecordsPerPage; i++) {
				TransactionListDTO transactionListDTO = transactionListDTOList.get(i);
				barcodeEnquiryForm.getTransactionListDTOList().add(transactionListDTO);	

			}
			populateScreen(barcodeEnquiryComposite);
			initialIndex -= modValue;		

		} else if (initialIndex > numOfRecordsPerPage && initialIndex <= (totalRecordCount - modValue)) {
			initialIndex -= numOfRecordsPerPage;
			barcodeEnquiryForm.getTransactionListDTOList().clear();				

			int count = initialIndex - numOfRecordsPerPage;
			for (int i = count; i < count + numOfRecordsPerPage; i++) {
				TransactionListDTO transactionListDTO = transactionListDTOList.get(i);
				barcodeEnquiryForm.getTransactionListDTOList().add(transactionListDTO);	

			}
			populateScreen(barcodeEnquiryComposite);
		} 
		barcodeEnquiryComposite.layout();
		HideAndShowPageButtons();
	}

	private void showNextPage() throws Exception{
		if(currentPage == numOfPages){
			return;
		}
		currentPage++;
		barcodeEnquiryComposite.getLblTotalPageCount().setText(/*pageCountString+*/currentPage+"/"+numOfPages);
		barcodeEnquiryComposite.layout();
		barcodeEnquiryComposite.getTableViewer().getTable().setSelection(-1);

		if (initialIndex < totalRecordCount - modValue) {
			barcodeEnquiryForm.getTransactionListDTOList().clear();					
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				TransactionListDTO transactionListDTO = transactionListDTOList.get(i);
				barcodeEnquiryForm.getTransactionListDTOList().add(transactionListDTO);				
			}
			populateScreen(barcodeEnquiryComposite);
			initialIndex += numOfRecordsPerPage;
		} else if (initialIndex < totalRecordCount) {
			barcodeEnquiryForm.getTransactionListDTOList().clear();			
			for (int i = initialIndex; i < initialIndex + modValue; i++) {
				TransactionListDTO transactionListDTO = transactionListDTOList.get(i);
				barcodeEnquiryForm.getTransactionListDTOList().add(transactionListDTO);			
			}
			populateScreen(barcodeEnquiryComposite);
			initialIndex += modValue;

		}
		barcodeEnquiryComposite.layout();
		HideAndShowPageButtons();
	}

	private void HideAndShowPageButtons() {
		//Don't hide/show. Just Enable/Disable

		if( currentPage == numOfPages) {
			barcodeEnquiryComposite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
			barcodeEnquiryComposite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_LAST_PAGE)));
		}
		else {
			barcodeEnquiryComposite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_NEXT_PAGE)));
			barcodeEnquiryComposite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_LAST_PAGE)));
		}

		if( currentPage == 1 || currentPage == 0 ) {
			barcodeEnquiryComposite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
			barcodeEnquiryComposite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_FIRST_PAGE)));
		}	
		else {	
			barcodeEnquiryComposite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_PREVIOUS_PAGE)));
			barcodeEnquiryComposite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_FIRST_PAGE)));
		}	
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
			numOfRecordsPerPage = 12;
		} else {
			numOfRecordsPerPage = 5;//Default
		}		
	}

	/**
	 * This method clears all the values in the
	 * screen
	 */
	public void clearAllValues() {
		barcodeEnquiryForm.setAmount("");
		barcodeEnquiryForm.setEamount("");
		barcodeEnquiryForm.setEffectiveDate("");
		barcodeEnquiryForm.setExpireDate("");
		barcodeEnquiryForm.setState("");
		barcodeEnquiryForm.setBarCode("");
		transactionListDTOList.clear();		
		barcodeEnquiryForm.setTransactionListDTOList(new ArrayList<TransactionListDTO>());
		barcodeEnquiryForm.setTransactionListDTOSelectedValue(null);
		barcodeEnquiryForm.setTxtOffline("");
		barcodeEnquiryForm.setTxtTicketType("");
		loadBatchDetailsFromResponse();
		adjustTableSizeBasedOnResolution();
		//barcodeEnquiryComposite.getTxtEAmount().setEditable(false);
		setBarcodeCalled = false;
		try {
			populateScreen(barcodeEnquiryComposite);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		barcodeEnquiryComposite.getTxtBarcode().setFocus();

	}
	/**
	 * This method clears the values in the
	 * screen except the barcode
	 */
	public void clearValues() {
		barcodeEnquiryForm.setAmount("");
		barcodeEnquiryForm.setEamount("");
		barcodeEnquiryForm.setEffectiveDate("");
		barcodeEnquiryForm.setExpireDate("");
		barcodeEnquiryForm.setState("");
		transactionListDTOList.clear();
		barcodeEnquiryForm.setTransactionListDTOList(new ArrayList<TransactionListDTO>());
		barcodeEnquiryForm.setTransactionListDTOSelectedValue(null);
		barcodeEnquiryForm.setTxtOffline("");
		barcodeEnquiryForm.setTxtTicketType("");
		loadBatchDetailsFromResponse();
		//barcodeEnquiryComposite.getTxtEAmount().setEditable(false);
		setBarcodeCalled = false;
		adjustTableSizeBasedOnResolution();
		try {
			populateScreen(barcodeEnquiryComposite);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		barcodeEnquiryComposite.getTxtBarcode().selectAll();
		barcodeEnquiryComposite.getTxtBarcode().setFocus();

	}

	/**
	 * This method clears the values in the
	 * screen except the barcode
	 */
	public void clearValuesForModifyText() {
		barcodeEnquiryForm.setAmount("");
		barcodeEnquiryForm.setEffectiveDate("");
		barcodeEnquiryForm.setExpireDate("");
		barcodeEnquiryForm.setState("");
		transactionListDTOList.clear();
		barcodeEnquiryForm.setTransactionListDTOList(new ArrayList<TransactionListDTO>());
		barcodeEnquiryForm.setTransactionListDTOSelectedValue(null);
		barcodeEnquiryForm.setTxtOffline("");
		barcodeEnquiryForm.setTxtTicketType("");
		barcodeEnquiryForm.setEamount("");
		clearValuesCalled=true;
		loadBatchDetailsFromResponse();
		adjustTableSizeBasedOnResolution();
		try {
			populateScreen(barcodeEnquiryComposite);
		} catch (Exception e) {			
			e.printStackTrace();
		}	
	}

	public void setBarcodeValue() {
		setBarcodeCalled=true;
		barcodeEnquiryForm.setBarCode(VoucherStringUtil.rPad(barcodeEnquiryForm.getBarCode(), 18, '*'));
	}

	public void modifyText(ModifyEvent e) {		
		if(e.getSource() instanceof SDSTSText){
			SDSTSText barcodeText = (SDSTSText) e.getSource();			
			if (barcodeText.getName().equalsIgnoreCase(IVoucherConstants.REDEEMVOUCHER_CTRL_TXT_BARCODE)) {
				if(barcodeEnquiryForm.getBarCode()!=null && barcodeEnquiryForm.getBarCode().trim().length()>0 &&!setBarcodeCalled&& !modifyTextCalled &&!clearValuesCalled) {					
					barcodeBackup = barcodeEnquiryForm.getBarCode();
					boolean retval =MessageDialogUtil.displayTouchScreenConfirmDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_ENQUIRE_MODIFY,new String[] {AppContextValues.getInstance().getTicketText()}),barcodeEnquiryComposite);					
					if(retval) {
						modifyTextCalled = true;
						clearValuesForModifyText();
						modifyTextCalled = false;
					}else {
						modifyTextCalled = true;
						barcodeEnquiryForm.setBarCode(barcodeBackup);	
						try {
							refreshControl(barcodeEnquiryComposite.getTxtBarcode());
						} catch (Exception e1) {						
							e1.printStackTrace();
						}
						modifyTextCalled = false;
						return;
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.keyCode == 13 || e.keyCode == 16777296){	
			populateBarcode();
		} 			   		
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
//		super.focusGained(e) ;
//		//composite.redraw();
//		if (e.getSource() instanceof SDSTSText)
//			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}
}
