/*****************************************************************************
 * $Id: ReconciliationController.java,v 1.62.5.0, 2013-10-25 16:52:01Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 11:52:01 AM$
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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
import com.ballydev.sds.framework.constant.IConstants;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabelText;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.SDSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.service.FrameworkServiceLocator;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.BatchInfoDTO;
import com.ballydev.sds.voucher.dto.ReconciliationInfoDTO;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.ares.AresListenerFactory;
import com.ballydev.sds.voucherui.composite.BaseReconciliationComposite;
import com.ballydev.sds.voucherui.composite.ReconciliationComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.BatchDetailsForm;
import com.ballydev.sds.voucherui.form.BatchReconciliationForm;
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.AresObject;
import com.ballydev.sds.voucherui.util.AresUtil;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class ReconciliationController extends BaseVoucherController implements ModifyListener,Runnable{

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private int initStyle;

	private BatchReconciliationForm batchReclForm;

	private Composite parent;

	private boolean batchNoCreated = false;

	private boolean modifyTextCalled = false;

	/** Instance of Ares */
	private static Ares ares;
	
	//Queue to hold tickets read from the HighSpeed Scanner.
	private final ArrayList<String> barcodeQueue = new ArrayList<String>();

	private final ArrayList<String> employeeQueuefrmXML = new ArrayList<String>();

	private static TreeMap<String, ArrayList<String>> addAllBarcodeQuefromXML = new TreeMap<String, ArrayList<String>>();
	
	
	//flag to indicate if High Speed Scanning is in Process.
	private boolean hsInProcess;

	//to avoid the run method being called twice for the same set of Tickets.
	private Object barcodeScannerLock = new Object();
	
	private boolean showMsg;
	private static String xmlRecon = "false";
	private static String doSingleXmlUpload = "true";
	private static String empIdPrefix;
	

	/**
	 * Constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public ReconciliationController(Composite parent,int style,	BatchReconciliationForm form, SDSValidator validator) throws Exception {
		super(parent,style,form,validator);
		this.parent = parent;
		batchReclForm = form;
		composite = new ReconciliationComposite(parent,style);
		((ReconciliationComposite)composite).setCashier(batchReclForm.isCashier());
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		if(batchReclForm.isCashier())
			reconciliationComposite.getRadioButtonControl().setSelectedButton(reconciliationComposite.getTBtnCashier());
		else
			reconciliationComposite.getRadioButtonControl().setSelectedButton(reconciliationComposite.getTBtnKiosk());
		
//		((ReconciliationComposite)composite).setKioskCashierToggle(batchReclForm.isCashier());
		VoucherMiddleComposite.setCurrentComposite(composite);
		getComposite().layout();
		composite.layout();
		parent.layout();
		parent.getParent().layout();
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setFocus();
		if( VoucherUtil.getReconLevel() == 1 ) {
			batchReclForm.setEmployeeAssetId(SDSApplication.getLoggedInUserID());
			populateScreen(composite);
			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setEnabled(false);
		}
		if( AppContextValues.getInstance().getReclEmpId() != null && AppContextValues.getInstance().getReclEmpId().length() > 0 ) {

			batchReclForm.setEmployeeAssetId(AppContextValues.getInstance().getReclEmpId());
		} if( AppContextValues.getInstance().getBatchId() != null ) {
			((ReconciliationComposite)composite).getLblBatchValue().setText(AppContextValues.getInstance().getBatchId());
		}
//		 if( batchReclForm.getBatchId() != null ) {
//			refreshForm(batchReclForm.getBatchId());
//		}
		
		batchReclForm.setTxtUploadXmlFile("");
		populateScreen(composite);
		registerCustomizedListeners((ReconciliationComposite)composite);
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		String scannerMan= preferenceStore.getString(IVoucherPreferenceConstants.SCANNER_MANUFACTURER);
		
		if(scannerMan.equals(IConstants.TKTSCANNER_METROLOGIC_MANFT))
			setScanner();
		else {
			setHSScanner();			
		}
		parent.layout();
		loadReconMethodfromPropFile();
	}

	private void setHSScanner() {		
		if( isBarcodeScannerEnabled() ) {
			ares = AresUtil.registerAresEvent(AresObject.getAres());			
			if( ares != null ) {
				if(AresListenerFactory.getAresEventListener(ares, this)!= null)
					ares.addAresEventListener(AresListenerFactory.getAresEventListener(ares, this));
				else
					log.info("No Listener was found for "+ares.getClass().getName());
			}
		}		
	}

	// Method to set scanner event for the Barcode text box, only single time for the particular view.
	private void setScanner() {		
		if( isBarcodeScannerEnabled()) {
			ares = AresUtil.registerAresEvent(AresObject.getAres());
			if( ares != null ) {
				ares.addAresEventListener(new AresEventListener(){
					public void aresEvent(AresEvent ae) {
						try {
							if( (ReconciliationComposite)composite != null && !((ReconciliationComposite)composite).isDisposed()
									&& ae instanceof AresBarcodeEvent ) {
								final String barcode = ((AresBarcodeEvent)ae).getBarcode();
								String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
								if( barcode != null && barcode.length() == 18 
										|| (barcode != null && stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16 )) {
									log.debug("Barcode: " + barcode);
									ares.accept();
									composite.getDisplay().syncExec(new Runnable() {
										public void run() {
											if( ((ReconciliationComposite)composite).getTxtBarcode().isFocusControl() ) {
												((ReconciliationComposite)composite).getTxtBarcode().setText(barcode);
												batchReclForm.setBarcode(barcode);
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

	private boolean isBarcodeScannerEnabled() {			
		return PlatformUI.getPreferenceStore().getBoolean(IVoucherPreferenceConstants.IS_BARCODE_SCANNER);
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @param isQuestionableVisible
	 * @throws Exception
	 */
	public ReconciliationController(Composite parent,int style,	BatchReconciliationForm form, SDSValidator validator,boolean isQuestionableVisible)	throws Exception{
		super(parent,style,form,validator);
		batchReclForm = form;
		composite = new ReconciliationComposite(parent,style,isQuestionableVisible);
		getComposite().layout();
		composite.layout();
		VoucherMiddleComposite.setCurrentComposite(composite);
		parent.layout();
		parent.getParent().layout();
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		if( VoucherUtil.getReconLevel() == 1 ) {
			batchReclForm.setEmployeeAssetId(SDSApplication.getLoggedInUserID());
			populateScreen(composite);
			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setEnabled(false);
		}
		((ReconciliationComposite)composite).getLblBatchValue().setText(AppContextValues.getInstance().getBatchId());
		if( AppContextValues.getInstance().getReclEmpId() != null ) {
			batchReclForm.setEmployeeAssetId(AppContextValues.getInstance().getReclEmpId());
		} if( AppContextValues.getInstance().getBatchId() != null ) {
			((ReconciliationComposite)composite).getLblBatchValue().setText(AppContextValues.getInstance().getBatchId());
		}
		populateScreen(composite);
		registerCustomizedListeners((ReconciliationComposite)composite);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.controller.BaseVoucherController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {

		Control ctrl[] = argComposite.getChildren();
		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof SDSDatePicker ) {
				((SDSDatePicker)ctrl[i]).addTraverseListener(this);
			} else if( ctrl[i] instanceof SDSTSText ) {
				((SDSTSText)ctrl[i]).addModifyListener(this);
			}
		}

		BaseReconciliationComposite baseReconciliationComposite = ((BaseReconciliationComposite)argComposite);

		baseReconciliationComposite.getBtnAddToBatch().addMouseListener(this);
		baseReconciliationComposite.getBtnAddToBatch().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnAddToBatch().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnSubmit().addMouseListener(this);
		baseReconciliationComposite.getBtnSubmit().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnSubmit().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnCancel().addMouseListener(this);
		baseReconciliationComposite.getBtnCancel().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnCancel().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnQuestionable().addMouseListener(this);
		baseReconciliationComposite.getBtnQuestionable().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnQuestionable().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnCurrentBatch().addMouseListener(this);
		baseReconciliationComposite.getBtnCurrentBatch().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnCurrentBatch().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnBatchSummary().addMouseListener(this);
		baseReconciliationComposite.getBtnBatchSummary().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnBatchSummary().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getTBtnCashier().addMouseListener(this);
		baseReconciliationComposite.getTBtnKiosk().addMouseListener(this);
		
		baseReconciliationComposite.getBtnXMLSubmit().addMouseListener(this);
		baseReconciliationComposite.getBtnXMLSubmit().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnXMLSubmit().getTextLabel().addTraverseListener(this);
		
		baseReconciliationComposite.getBtnBrowse().addMouseListener(this);
		baseReconciliationComposite.getBtnBrowse().getTextLabel().addMouseListener(this);
		baseReconciliationComposite.getBtnBrowse().getTextLabel().addTraverseListener(this);
		
		
	}

	private void reconciliationFocusEventHandler(TypedEvent typedEvent){
		ReconciliationComposite reconciliationComposite= (ReconciliationComposite)getComposite();
		if( typedEvent.getSource().equals(reconciliationComposite.getTxtBarcode())){
			if( !reconciliationComposite.getBtnAddToBatch().getEnabled() ){
				batchReclForm.setBarcode("");
				try {
					populateScreen(reconciliationComposite);
				} catch (Exception ex) {
					log.error("Error occurred in ReconciliationController.reconciliationFocusEventHandler", ex);
				}
				reconciliationComposite.getBtnAddToBatch().setEnabled(true);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		//((ReconciliationComposite)composite).redraw();
		if (e.getSource() instanceof SDSTSText){
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
		}
		if (e.getSource() instanceof SDSTSLabelText)
			KeyBoardUtil.setCurrentTextInFocus(((SDSTSLabelText) e.getSource()).getSdsText());
		reconciliationFocusEventHandler(e);
	}

	private void populateBarcode() {
		try {
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();		
			refreshForm();
			boolean isvalid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM, IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,
								IVoucherConstants.DEPENDS_FOR_EMP, IVoucherConstants.ARG_FOR_EMP, getSdsForm(), reconciliationComposite);
			if (!isvalid) {
				return;
			}
			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());				
			if(userDTO == null || userDTO.getUserName() == null) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
				reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
				return;
			}
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add(IVoucherConstants.RECON_CTRL_LBL_START_TIME);
			fieldNames.add(IVoucherConstants.RECON_CTRL_LBL_END_TIME);
			fieldNames.add(IVoucherConstants.RECON_CTRL_TXT_BARCODE);

			String barcode  = batchReclForm.getBarcode();
			String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
			
			if(stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) && barcode.length() == 16) {
				barcode = "00" + barcode.trim();
				batchReclForm.setBarcode(barcode);
			}

			boolean isValid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM, fieldNames, batchReclForm, reconciliationComposite);
			if( !isValid ) {
				return;
			}
			BatchReconciliationForm form = (BatchReconciliationForm) getSdsForm();
			form.scanBarcode();
			populateScreen(reconciliationComposite);
			//reconciliationComposite.getBtnAddToBatch().setEnabled(false);
			batchReclForm.setBarcode("");
			try {
				populateScreen(reconciliationComposite);
			} catch (Exception ex) {
				log.error("Error occurred in ReconciliationController.reconciliationFocusEventHandler", ex);
			}
			reconciliationComposite.getBtnAddToBatch().setEnabled(true);
			reconciliationComposite.getTxtBarcode().setFocus();
		} catch (Exception ex) {
			log.error("Error occurred in ReconciliationController.reconciliationEventHandler", ex);
		}
	}
	
	/**
	 * This method is similar to the PopulateBarcode,except for it doesnt check the employeeId validity,StartTime and EndTime validity
	 * as they are checked before the reconciliation process (by a HS) is started
	 * and also call's update method on composite because it is being called from a Runnable.
	 */
	private void popHSBarcode() {
		try {
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();		
			BatchReconciliationForm form = (BatchReconciliationForm) getSdsForm();
			form.scanBarcode();
			populateScreen(reconciliationComposite);
			//reconciliationComposite.getBtnAddToBatch().setEnabled(false);
			this.composite.update();
			batchReclForm.setBarcode("");
			try {
				populateScreen(reconciliationComposite);
			} catch (Exception ex) {
				log.error("Error occurred in ReconciliationController.reconciliationFocusEventHandler", ex);
			}
			reconciliationComposite.getBtnAddToBatch().setEnabled(true);
			reconciliationComposite.getTxtBarcode().setFocus();
			this.composite.update();
		} catch (Exception ex) {
			log.error("Error occurred in ReconciliationController.reconciliationEventHandler", ex);
		}
	}

	private void reconciliationEventHandler(TypedEvent typedEvent) throws Exception {
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		if( typedEvent.getSource().equals(reconciliationComposite.getTBtnCashier())
				|| typedEvent.getSource().equals(reconciliationComposite.getTBtnKiosk()) ) {
			boolean toggleStatus = typedEvent.getSource().equals(reconciliationComposite.getTBtnCashier());
			reconciliationComposite.setCashier(toggleStatus);
			batchReclForm.setCashier(toggleStatus);			
			TSButtonLabel touchScreenRadioButton = (TSButtonLabel) ((Control)typedEvent.getSource());				
			reconciliationComposite.getRadioButtonControl().setSelectedButton(touchScreenRadioButton);				
			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setFocus();
			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().selectAll();
		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnAddToBatch()) ) {
			populateBarcode();
		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnCurrentBatch()) ) {
			if( showBatchNotCreatedMsg() ) {
				return;
			};
			getSummary(false);
			if( batchReclForm.getBatchSummaries() != null && !batchReclForm.getBatchSummaries().isEmpty() ) {
				populateForm(getComposite());
				getComposite().dispose();
				new BatchSummaryController(parent,SWT.NONE,new SDSValidator(getClass(),true),batchReclForm);	
			}else {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NO_BATCH_AVAILABLE));
			}

		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnBatchSummary()) ) {
			getSummary(true);
			if( batchReclForm.getBatchSummaries() != null && !batchReclForm.getBatchSummaries().isEmpty() ) {
				populateForm(getComposite());
				getComposite().dispose();
				new BatchSummaryController(parent,SWT.NONE,new SDSValidator(getClass(),true),batchReclForm);
			} else {
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NO_BATCH_AVAILABLE));
			}

		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnSubmit()) ) {
			if(!xmlRecon.equalsIgnoreCase("true")){
				/*
				 * Old way of submitting batch i.e. either by currency counter or manual scanning
				 */
				processSubmit(reconciliationComposite);
			}else{
				/*
				 * Creating Batch from XML 1 by 1 by hitting submit button
				 */
					if(addAllBarcodeQuefromXML!=null && addAllBarcodeQuefromXML.size()>0) {
						
						BatchReconciliationForm form = (BatchReconciliationForm) getSdsForm();
						form.resetScannedTickets();
						ArrayList<String> barcodesfrmXML = new ArrayList<String>();
						String empId = addAllBarcodeQuefromXML.firstEntry().getKey();
						barcodesfrmXML = (ArrayList<String>) addAllBarcodeQuefromXML.firstEntry().getValue();
						
						((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setText(empId);
						batchReclForm.setEmployeeAssetId(empId);
						
						UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());
						if( userDTO == null || userDTO.getUserName() == null ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
							addAllBarcodeQuefromXML.remove(empId);
							return;
						}
						
						createBatchfrmXML();
						populateBatchforXML(empId, barcodesfrmXML);
						processSubmit((ReconciliationComposite) getComposite());
						addAllBarcodeQuefromXML.remove(empId);
					}else{
						MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NO_XML_BATCH_TO_CREATE)));
				    	return;
					}
			}
		} else if(typedEvent.getSource().equals(reconciliationComposite.getBtnCancel())){			
			if(batchNoCreated){
				boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_RECON_VALUES_CANCEL), composite);
				if(response){			
					resetValues();
					reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
				}else{
					reconciliationComposite.getTxtBarcode().setFocus();
					return;
				}
			}else{
				resetValues();
				reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
			}
		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnQuestionable())){
			if( showBatchNotCreatedMsg()){return;};
			loadBatchDetailBtnHandler();	
		}else if( typedEvent.getSource().equals(reconciliationComposite.getBtnBrowse())){

			if(!xmlRecon.equalsIgnoreCase("true")){
				resetValues();
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOT_XML_UPLOAD_METHOD));	
				return;
				
			}
			String xmlFileNameSelected = fileSelectXml();
			log.debug("Xml File selected :"+xmlFileNameSelected);
			batchReclForm.setTxtUploadXmlFile(xmlFileNameSelected);
			if (xmlFileNameSelected != null && xmlFileNameSelected.length() > 0){
				try {
					boolean checkCurrencyFile = validate("BatchReconciliationForm","txtUploadXmlFile", batchReclForm, ((ReconciliationComposite)composite));
					if(!checkCurrencyFile){
						batchReclForm.setTxtUploadXmlFile(null);
						return;
					}
					((ReconciliationComposite)composite).getBtnXMLSubmit().setEnabled(true);
				} catch (Exception ex) {				
					log.error("Error occurred in ReconciliationController.reconciliationEventHandler: XmlUpload", ex);
					VoucherUIExceptionHandler.handleException(ex);
				}
			}
		} else if( typedEvent.getSource().equals(reconciliationComposite.getBtnXMLSubmit())){
			parseUploadXml();
		}
	}

	private void processSubmit(ReconciliationComposite reconciliationComposite) {
		try {
			refreshForm();
			boolean isvalid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,
					IVoucherConstants.DEPENDS_FOR_EMP, IVoucherConstants.ARG_FOR_EMP, getSdsForm(), reconciliationComposite);
			if (!isvalid) {
				return;
			}
			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), 
					SDSApplication.getSiteDetails().getId());				
			if( userDTO == null || userDTO.getUserName() == null ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));	
				reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
				return;
			}

			BatchReconciliationForm batchReconciliationForm = (BatchReconciliationForm) getSdsForm();
			if( batchReconciliationForm.getScannedTickets()==null || batchReconciliationForm.getScannedTickets().size() == 0 ){
				showMessageBox(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_ENSURE_TICKETS_SCANNED));
				reconciliationComposite.getTxtBarcode().setFocus();
				return;
			}
			ReconciliationInfoDTO serviceReconciliationInfoDTO = getServiceReconciliationInfoDTO();
			serviceReconciliationInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			serviceReconciliationInfoDTO.setReclId(Long.parseLong(AppContextValues.getInstance().getBatchId()));
			serviceReconciliationInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
			ReconciliationInfoDTO reconciliationInfoDTO =  ServiceCall.getInstance().scanReclVouchers(serviceReconciliationInfoDTO);
			if(reconciliationInfoDTO!=null){
				if(reconciliationInfoDTO.isErrorPresent())	{
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
				}
				else {
					showMessageBox(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
					batchReconciliationForm.setBatchId(reconciliationInfoDTO.getReclId());
					/*
					 * old way of doing reconciliation i.e. either by currency counter or manual scan 
					 */
					if(!xmlRecon.equalsIgnoreCase("true")){
						batchReconciliationForm.resetScannedTickets();
					}
					Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_RECONCILIATION), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), "", reconciliationInfoDTO.getReclId().toString(), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_CREATED), EnumOperation.ADD_OPERATION,PreferencesUtil.getClientAssetNumber());
					populateScreen(getComposite());
					/*
					 * old way of doing reconciliation i.e. either by currency counter or manual scan 
					 */
					if(!xmlRecon.equalsIgnoreCase("true")){
						reconciliationComposite.getBtnSubmit().setEnabled(false);
					}
					AppContextValues.getInstance().setReclEmpId("");
					AppContextValues.getInstance().setBatchId(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET));
					batchNoCreated = false;
					hsInProcess = false;

				}
			}
		}catch(VoucherEngineServiceException ex)	{
			log.error("Exception while scanning the Vouchers", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		} catch (Exception ex) {				
			log.error("Error occurred in ReconciliationController.reconciliationEventHandler", ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}

	private void processSubmitforXML(ReconciliationComposite reconciliationComposite,  ArrayList<String> barcodesfrmXML) {
		try {
			//refreshForm();
			boolean isvalid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,
					IVoucherConstants.DEPENDS_FOR_EMP, IVoucherConstants.ARG_FOR_EMP, getSdsForm(), reconciliationComposite);
			if (!isvalid) {
				return;
			}
			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), 
					SDSApplication.getSiteDetails().getId());				
			if( userDTO == null || userDTO.getUserName() == null ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));	
				reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
				BatchReconciliationForm batchReconciliationForm = (BatchReconciliationForm) getSdsForm();
				batchReconciliationForm.getScannedDetailsList();
				batchReconciliationForm.removeScanBarcode(barcodesfrmXML);
				reconciliationComposite.getLblTxtTotal().getSdsText().setText("");
				reconciliationComposite.getLblTxtNotRead().getSdsText().setText("");
				reconciliationComposite.getLblTxtAccepted().getSdsText().setText("");
				reconciliationComposite.getLblTxtUncommitted().getSdsText().setText("");
				return;
			}

			BatchReconciliationForm batchReconciliationForm = (BatchReconciliationForm) getSdsForm();
			if( batchReconciliationForm.getScannedTickets()==null || batchReconciliationForm.getScannedTickets().size() == 0 ){
				showMessageBox(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_ENSURE_TICKETS_SCANNED));
				reconciliationComposite.getTxtBarcode().setFocus();
				return;
			}
			ReconciliationInfoDTO serviceReconciliationInfoDTO = getServiceReconciliationInfoDTOforXML(barcodesfrmXML);
			serviceReconciliationInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			serviceReconciliationInfoDTO.setReclId(Long.parseLong(AppContextValues.getInstance().getBatchId()));
			serviceReconciliationInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
			ReconciliationInfoDTO reconciliationInfoDTO =  ServiceCall.getInstance().scanReclVouchers(serviceReconciliationInfoDTO);
			if(reconciliationInfoDTO!=null){
				if(reconciliationInfoDTO.isErrorPresent())	{
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
				}
				else {
				//	showMessageBox(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
					batchReconciliationForm.setBatchId(reconciliationInfoDTO.getReclId());
					//batchReconciliationForm.resetScannedTickets();
					Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_RECONCILIATION), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), "", reconciliationInfoDTO.getReclId().toString(), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_CREATED), EnumOperation.ADD_OPERATION,PreferencesUtil.getClientAssetNumber());
					populateScreen(getComposite());
					//reconciliationComposite.getBtnSubmit().setEnabled(false);
					AppContextValues.getInstance().setReclEmpId("");
					AppContextValues.getInstance().setBatchId(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET));
					batchNoCreated = false;
					hsInProcess = false;

				}
			}
		}catch(VoucherEngineServiceException ex)	{
			log.error("Exception while scanning the Vouchers", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		} catch (Exception ex) {				
			log.error("Error occurred in ReconciliationController.reconciliationEventHandler", ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}

	
	/**
	 * This method resets the form values
	 */
	public void resetValues(){
		try {
			batchReclForm.setAccepted("");
			batchReclForm.setBarcode("");
			batchReclForm.setBatchId(null);
			batchReclForm.setBatchSummaries(null);
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
			reconciliationComposite.setCashier(true);
			batchReclForm.setCashier(true);
			TSButtonLabel touchScreenRadioButton = (TSButtonLabel) (reconciliationComposite.getTBtnCashier());
			reconciliationComposite.getRadioButtonControl().setSelectedButton(touchScreenRadioButton);
			batchReclForm.setDetails("");
			if(reconciliationComposite.getTxtEmployeeOrAsset().isEnabled()) {
				batchReclForm.setEmployeeAssetId("");
			}
			batchReclForm.setNotRead("");
			batchReclForm.setTotal("");
			batchReclForm.setUncommitted("");
			batchReclForm.setQuestionableDetailsList("");
			batchReclForm.setScannedDetailsList("");
			batchReclForm.setEmployeeAssetToggleBtn(true);
			batchReclForm.setStartTime(DateUtil.getUTCTimeFromLocal(DateUtil.getCurrentServerDate().getTime()));
			batchReclForm.setEndTime(DateUtil.getUTCTimeFromLocal(DateUtil.getCurrentServerDate().getTime()));			
			batchNoCreated = false;
			batchReclForm.resetScannedTickets();
			populateScreen(composite);
			((BaseReconciliationComposite)composite).getTxtEmployeeOrAsset().setFocus();
			AppContextValues.getInstance().setReclEmpId("");
			AppContextValues.getInstance().setBatchId(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET));
			((ReconciliationComposite)composite).getLblBatchValue().setText(AppContextValues.getInstance().getBatchId());
			((BaseReconciliationComposite)composite).getBtnSubmit().setEnabled(true);
			barcodeQueue.clear();
//			barcodeQueuefrmXML.clear();
			employeeQueuefrmXML.clear();
			addAllBarcodeQuefromXML.clear();
			batchReclForm.setTxtUploadXmlFile("");
			hsInProcess = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method resets the form values
	 */
	public void resetBatch(){
		try {
			batchReclForm.setAccepted("");
			batchReclForm.setBarcode("");
			batchReclForm.setBatchId(null);
			batchReclForm.setBatchSummaries(null);
			batchReclForm.setCashier(true);
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
			reconciliationComposite.setCashier(true);
			batchReclForm.setCashier(true);			
			TSButtonLabel touchScreenRadioButton = (TSButtonLabel) (reconciliationComposite.getTBtnCashier());				
			reconciliationComposite.getRadioButtonControl().setSelectedButton(touchScreenRadioButton);		
			batchReclForm.setDetails("");			
			batchReclForm.setNotRead("");
			batchReclForm.setTotal("");
			batchReclForm.setUncommitted("");
			batchReclForm.setQuestionableDetailsList("");
			batchReclForm.setScannedDetailsList("");
			batchReclForm.setEmployeeAssetToggleBtn(true);
			batchReclForm.setStartTime(DateUtil.getUTCTimeFromLocal(DateUtil.getCurrentServerDate().getTime()));
			batchReclForm.setEndTime(DateUtil.getUTCTimeFromLocal(DateUtil.getCurrentServerDate().getTime()));			
			batchReclForm.resetScannedTickets();						
			AppContextValues.getInstance().setBatchId(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET));
			((ReconciliationComposite)composite).getLblBatchValue().setText(AppContextValues.getInstance().getBatchId());
			AppContextValues.getInstance().setReclEmpId("");
			batchReclForm.setEmployeeAssetId("");
			barcodeQueue.clear();
//			barcodeQueuefrmXML.clear();
			employeeQueuefrmXML.clear();
			addAllBarcodeQuefromXML.clear();
			batchReclForm.setTxtUploadXmlFile("");
			hsInProcess = false;
			populateScreen(composite);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public ReconciliationInfoDTO getServiceReconciliationInfoDTO(){
		try {
			populateForm(composite);
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		ReconciliationInfoDTO reconciliationInfoDTO =new ReconciliationInfoDTO();
		if((batchReclForm.getScannedTickets()!=null)&&(batchReclForm.getScannedTickets().size()!=0)){
			String[] scannedTkts =(String[])batchReclForm.getScannedTickets().toArray(new String[batchReclForm.getScannedTickets().size()]);			
			reconciliationInfoDTO.setVouBarcodes(scannedTkts);
		}
		reconciliationInfoDTO.setReclStartTime(batchReclForm.getStartTime());
		reconciliationInfoDTO.setReclEndTime(batchReclForm.getEndTime());		
		reconciliationInfoDTO.setReclEmployeeId(batchReclForm.getEmployeeAssetId());
		reconciliationInfoDTO.setReclTypeId(batchReclForm.isCashier()?1:2);		
		reconciliationInfoDTO.setReclManagerId("1");
		reconciliationInfoDTO.getVouBarcodes();
		reconciliationInfoDTO.setReclAssetLocn(PreferencesUtil.getClientAssetNumber());
		reconciliationInfoDTO.setCashier(batchReclForm.isCashier());
		return reconciliationInfoDTO;
	}
	
	public ReconciliationInfoDTO getServiceReconciliationInfoDTOforXML(ArrayList<String> barcodefrmXML){
		try {
			populateForm(composite);
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		ReconciliationInfoDTO reconciliationInfoDTO =new ReconciliationInfoDTO();
		if((barcodefrmXML!=null)&&(barcodefrmXML.size()!=0)){
			String[] scannedTkts = barcodefrmXML.toArray( new String[barcodefrmXML.size()]);	
			reconciliationInfoDTO.setVouBarcodes(scannedTkts);
		}
		reconciliationInfoDTO.setReclStartTime(batchReclForm.getStartTime());
		reconciliationInfoDTO.setReclEndTime(batchReclForm.getEndTime());		
		reconciliationInfoDTO.setReclEmployeeId(batchReclForm.getEmployeeAssetId());
		reconciliationInfoDTO.setReclTypeId(batchReclForm.isCashier()?1:2);		
		reconciliationInfoDTO.setReclManagerId("1");
		reconciliationInfoDTO.getVouBarcodes();
		reconciliationInfoDTO.setReclAssetLocn(PreferencesUtil.getClientAssetNumber());
		reconciliationInfoDTO.setCashier(batchReclForm.isCashier());
		return reconciliationInfoDTO;
	}
	

	private void refreshForm(){
		ReconciliationComposite reconciliationComposite= (ReconciliationComposite)getComposite();
		try {
			populateForm(reconciliationComposite);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		BatchReconciliationForm form = batchReclForm;
		reconciliationComposite.getStartTime().refreshForm(form);
		reconciliationComposite.getEndTime().refreshForm(form);
	}


	public void reviveReconciliationScreen() throws Exception{
		composite = new ReconciliationComposite(parent,initStyle);
		super.registerEvents(composite);
		getSdsForm().addPropertyChangeListener(this);
		composite.getParent().layout();
	}

	private void loadBatchDetailBtnHandler(){
		Composite parent = getComposite().getParent();
		getComposite().dispose();
		try {
			getSummary(false);
		} catch (VoucherEngineServiceException e) {

		}

		BatchDetailsForm batchDetailsForm = new BatchDetailsForm();		
		batchDetailsForm.setBatchNbrValue(batchReclForm.getBatchId().toString());		
		BatchReconciliationForm form = batchReclForm;
		BatchSummaryDTO batchSummaryDTO = new BatchSummaryDTO();
		if(form.getBatchSummaries() != null && !form.getBatchSummaries().isEmpty()) {
			batchSummaryDTO =  form.getBatchSummaries().get(0);
		}
		batchDetailsForm.setBatchCouponAmountValue(ConversionUtil.voucherAmountFormat( batchSummaryDTO.getCouponAmount() != null ?batchSummaryDTO.getCouponAmount().toString() : ""));
		batchDetailsForm.setBatchCouponCountValue(batchSummaryDTO.getCouponCount() != null ?batchSummaryDTO.getCouponCount().toString() : "");
		batchDetailsForm.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat( batchSummaryDTO.getVoucherAmount() != null ?batchSummaryDTO.getVoucherAmount().toString() : ""));
		batchDetailsForm.setBatchVoucherCountValue(batchSummaryDTO.getVoucherCount() != null ?batchSummaryDTO.getVoucherCount().toString() : "");
		batchDetailsForm.setBatchEmployeeValue(batchSummaryDTO.getEmpId());
		batchDetailsForm.setCashier(batchReclForm.isCashier());
		try {					
			new BatchDetailsController(parent,SWT.NONE, batchDetailsForm,null, batchReclForm,true);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error occurred in ReconciliationController.loadBatchDetailBtnHandler", ex);
		}
	}

	private boolean showBatchNotCreatedMsg(){	
		if (batchReclForm.getBatchId() == null){		
			showMessageBox(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_BATCH_NOT_CREATED));
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
			reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
			return true;
		}
		return false;
	}

	private void getSummary(boolean isBatchSummary) throws VoucherEngineServiceException {
		try	{
			ArrayList<BatchSummaryDTO> listOfBatches = new ArrayList<BatchSummaryDTO>();
			BatchInfoDTO batchInfoDTO = new BatchInfoDTO();
			batchInfoDTO.setBatchId(batchReclForm.getBatchId());
			batchInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));

			com.ballydev.sds.voucher.dto.BatchSummaryDTO batchSummaryForm = null;
			if( isBatchSummary ) {
				batchSummaryForm=ServiceCall.getInstance().getBatchSummary((int)SDSApplication.getUserDetails().getSiteId());
			} else {
				batchSummaryForm=ServiceCall.getInstance().showBatch(batchInfoDTO);
			}
			com.ballydev.sds.voucher.dto.BatchSummaryInfo[] listOfBatchesFromSDS = batchSummaryForm.getBatchSummaries();
			if( batchSummaryForm != null ) {
				if( batchSummaryForm.isErrorPresent() ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(batchSummaryForm));
				}
				else if( listOfBatchesFromSDS != null ) {
					for( com.ballydev.sds.voucher.dto.BatchSummaryInfo batchSummaryInfo: listOfBatchesFromSDS ) {
						com.ballydev.sds.voucher.dto.BatchSummaryInfo batchSummary = (com.ballydev.sds.voucher.dto.BatchSummaryInfo) batchSummaryInfo;
						BatchSummaryDTO batchSummaryDTO =  new BatchSummaryDTO();
						batchSummaryDTO.setBatchNumber(batchSummary.getBatchId());
						batchSummaryDTO.setCouponAmount(Double.parseDouble(ConversionUtil.centsToDollar(batchSummary.getCouponTtlAmount())));
						batchSummaryDTO.setCouponCount(batchSummary.getCouponTotalCount());
						batchSummaryDTO.setVoucherAmount(Double.parseDouble(ConversionUtil.centsToDollar(batchSummary.getVoucherTtlAmount())));
						batchSummaryDTO.setVoucherCount(batchSummary.getVoucherTotalCount());
						batchSummaryDTO.setCashierOrKiosk(batchReclForm.getEmployeeAssetId());
						batchSummaryDTO.setEmpId(batchSummary.getEmpId());
						batchSummaryDTO.setAssetNbr(batchSummary.getAssetNbr());
						batchSummaryDTO.setLocCode(batchSummary.getLocCode());
						batchSummaryDTO.setBatchScanDate(batchSummary.getBatchCreatedTs());
						listOfBatches.add(batchSummaryDTO);
					}
					batchReclForm.setBatchSummaries(listOfBatches);
				}
			}
		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while getting the batch details", ex);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
			throw ex;
		}
		catch(Exception e){
			log.error("Error occurred in BatchSummaryController.populateBatchSummary", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	@Override
	public void keyTraversed(TraverseEvent e) {		
		super.keyTraversed(e);		
		if( (e.keyCode == 9) || (e.keyCode == SWT.TAB) ){
			if((Control) e.widget instanceof Text && !((Control) e.widget instanceof SDSText)){
				Text text = (Text) e.getSource();
				if( text.getData().toString().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_LBL_END_TIME) ) {
					try {
						ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
						refreshForm();
						if(!xmlRecon.equalsIgnoreCase("true")){
							boolean isvalid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,IVoucherConstants.DEPENDS_FOR_EMP,IVoucherConstants.ARG_FOR_EMP, getSdsForm(),reconciliationComposite);
							if (!isvalid) {
								return;
							}
							List<String> fieldNames = new ArrayList<String>();
							fieldNames.add(IVoucherConstants.RECON_CTRL_LBL_START_TIME);
							fieldNames.add(IVoucherConstants.RECON_CTRL_LBL_END_TIME);
							
							boolean isValid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM,fieldNames, getSdsForm(), reconciliationComposite);
							if (!isValid) {
								return;
							}
							UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());
							if( userDTO == null || userDTO.getUserName() == null ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
								e.doit = false;
								reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
								return;
							}
							e.doit = true;
							createBatch();
							
						}
					} catch(Exception e1) {
						log.error("Exception occured in the key traversed event while creating the batch no", e1);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		Control [] ctrl = null;
		if ((Control) e.widget instanceof SDSText) {
			try {
				SDSText txtToValidate = (SDSText) e.getSource();
				if( !(txtToValidate.getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID)) ) {

					if(!xmlRecon.equalsIgnoreCase("true")){
						refreshForm();			
						boolean isvalid = validate(IVoucherConstants.BATCH_RECONCILIATION_FORM,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,IVoucherConstants.DEPENDS_FOR_EMP,IVoucherConstants.ARG_FOR_EMP, getSdsForm(),reconciliationComposite);
						if( !isvalid ) {						
							return;
						}
						UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());				
						if( userDTO == null || userDTO.getUserName() == null ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
							return;
						}					
						createBatch();
					}
					
				}
			}  catch (Exception e1) {
				log.error("Exception occured in the mouse down event while creating the batch no", e1);
			}
		}
		if( (Control) e.widget instanceof SDSImageLabel ) {
			try {			
				reconciliationEventHandler(e);
			} catch(Exception e1) {
				log.error(e1);
			}
		}
		
		if (((Control) e.getSource() instanceof TSButtonLabel)) {
			
			if( reconciliationComposite != null && !reconciliationComposite.isDisposed() )
				ctrl = reconciliationComposite.getToggleBtnComposite().getChildren();
			
			doCheck(ctrl, e);
			batchReclForm.setCashier(e.widget.equals(reconciliationComposite.getTBtnCashier()));
		}
	}

	// Checking the toggling status changing the selection.
	private void doCheck(Control[] ctrl, MouseEvent e) {
		TSButtonLabel selectedRadioBtn = (TSButtonLabel) ((Control)e.getSource());
		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof TSButtonLabel ) {
				TSButtonLabel btn = (TSButtonLabel) ctrl[i];
				if( selectedRadioBtn.getName().equalsIgnoreCase(btn.getName().toString()) ) {
					btn.setEnabled(true);
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
				}
				else {
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
					btn.setEnabled(true);
				}
			}
		}
	}

	/**
	 * This method creates the batch no when the emp id is entered
	 */
	private void createBatch() {
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		try {
			if( (AppContextValues.getInstance().getBatchId() == null 
					|| AppContextValues.getInstance().getBatchId().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET))) 
					&& !batchNoCreated && reconciliationComposite.getBtnSubmit().isEnabled() ) {
				//boolean response =MessageDialogUtil.displayTouchScreenConfirmDialogOnActiveShell(IDBLabelKeyConstants.TIME_VALUES_CONTINUE);
				int response = MessageDialogUtil.displayTouchScreenYesNoCancelDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.TIME_VALUES_CONTINUE));
				if( response == 2 ) {
					reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
					if( VoucherUtil.getReconLevel() == 1 ) {
						reconciliationComposite.getStartTime().setFocus();
					}
					return;
				}
				else if( response == 0 ) {
					String startTimeTs = DateUtil.convertDateToString(DateUtil.getCurrentServerDate());
					String endTimeTs = DateUtil.convertDateToString(DateUtil.getCurrentServerDate());
					List<String> timeDetailsList = ServiceCall.getInstance().getTimeDetails(batchReclForm.getEmployeeAssetId(),(int)SDSApplication.getUserDetails().getSiteId());
					try {
						if( timeDetailsList != null && timeDetailsList.size() > 0 && !timeDetailsList.isEmpty() ) {
							startTimeTs = timeDetailsList.get(0);
							endTimeTs   = timeDetailsList.get(1);
						}
					} catch (Exception e) {	}
					Date startTime = DateUtil.convertStringToDate(startTimeTs);
					batchReclForm.setStartTime(startTime);
					Date endTime =  DateUtil.convertStringToDate(endTimeTs);
					batchReclForm.setEndTime(endTime);
					populateScreen(composite);
				}
				ReconciliationInfoDTO serviceReconciliationInfoDTO = getServiceReconciliationInfoDTO();
				serviceReconciliationInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
				serviceReconciliationInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
				ReconciliationInfoDTO reconciliationInfoDTO =  ServiceCall.getInstance().insertReconciliation(serviceReconciliationInfoDTO);
				if( reconciliationInfoDTO!=null ) {
					if( reconciliationInfoDTO.isErrorPresent() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
					}
					else {
						AppContextValues.getInstance().setReclEmpId(batchReclForm.getEmployeeAssetId());
						AppContextValues.getInstance().setBatchId(reconciliationInfoDTO.getReclId().toString());
						reconciliationComposite.getLblBatchValue().setText(reconciliationInfoDTO.getReclId().toString());
						batchNoCreated = true;						
					}
				}
			}
		} catch (VoucherEngineServiceException e1) {
			log.error("VoucherEngineServiceException occured in the createBatch mthd", e1);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(e1));
		} catch(Exception e1) {
			log.error("Exception occured in the createBatch mthd", e1);
			VoucherUIExceptionHandler.handleException(e1);
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		if( (e.keyCode != 9) && (e.keyCode != 13)&& !(e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ALT||e.keyCode == SWT.SHIFT || 
				e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT || e.keyCode == SWT.CTRL) ) {
			Object obj = e.getSource();
			if( obj instanceof SDSTSText ) {
				SDSTSText empText = (SDSTSText) obj;
				if( empText.getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID) ) {
					if( batchReclForm.getEmployeeAssetId()!=null && batchReclForm.getEmployeeAssetId().trim().length()!=0 && batchNoCreated ) {
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_RECON_VALUES_LOST), composite);
						if( response ) {
							resetBatch();
							batchNoCreated = false;
							e.doit = true;
						} else {							
							batchReclForm.setEmployeeAssetId(AppContextValues.getInstance().getReclEmpId());
							try {
								refreshControl(reconciliationComposite.getTxtEmployeeOrAsset());
								e.doit = false;
							} catch( Exception e1 ) {								
								e1.printStackTrace();
							}
							batchNoCreated = true;							
							return;
						}
					}
				}
			}
		} else if( e.keyCode == 13 || e.keyCode == 16777296 ) {
			Object obj = e.getSource();
			if( obj instanceof SDSTSText ) {
				SDSTSText empText = (SDSTSText) obj;
				if( empText.getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_TXT_BARCODE) ) {
					populateBarcode();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		if( e.getSource() instanceof SDSTSText ) {
			SDSTSText barcodeText = (SDSTSText) e.getSource();
			ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
			if( barcodeText.getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID) ) {

				if( batchReclForm.getEmployeeAssetId()!=null && batchReclForm.getEmployeeAssetId().trim().length()!=0 && batchNoCreated && !modifyTextCalled ) {

					boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_RECON_VALUES_LOST), composite);
					if( response ) {
						resetBatch();
						batchNoCreated = false;
					} else {
						//ADDED THIS VARIABLE BCOZ THE MODIFY TEXT EVENT IS CALLED WHILE REFRESH CONTROL FOR THAT 
						//EMPLOYEE ID FIELD IS CALLED WHICH LEADS TO 2 MSG BOX POP UP. HENCE SET THIS VARIABLE TO 
						//TRUE BEFORE CALLING THE REFRESH CONTROL AND RESET IT BACK AGAIN TO FALSE AFTER THAT MTHD CALL
						modifyTextCalled = true;
						batchReclForm.setEmployeeAssetId(AppContextValues.getInstance().getReclEmpId());
						try {
							refreshControl(reconciliationComposite.getTxtEmployeeOrAsset());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						batchNoCreated = true;
						modifyTextCalled = false;
						return;
					}
				}
			}
		}
	}
	
	/**
	 * adding Tickets to a Queue read from a XML 
	 * @param barcode
	 */
/*	public final void addToQueuefromXML(final String barcode) throws Exception {
		barcodeQueuefrmXML.add(barcode);	
	}*/
	
	/**
	 * adding Tickets to a Queue read from a XML 
	 * @param barcode
	 */
	public final void addEmployeeToQueuefromXML(final String barcode) throws Exception {
		employeeQueuefrmXML.add(barcode);	
	}
	
	/**
	 * adding Tickets to a Queue read from a HighSpeed Scanner
	 * @param barcode
	 */
	public final void addToQueue(final String barcode) throws Exception {
	
		barcodeQueue.add(barcode);	
		
		synchronized (barcodeScannerLock){
			if(!hsInProcess){
				hsInProcess = true;
				composite.getDisplay().asyncExec(this);				
			}			
		}
	}

	public final void run() {

		String barcode;

		try {
			
			if (!batchNoCreated) {
				
				//to populate the BatchReconciliationForm with the values entered by the user
				refreshForm();
				
				//Check if an Employee is Assigned for whom the Reconciliation will be Processed.
				if (!isEmployeeAssigned()) {
					resetValues();
					((ReconciliationComposite) this.getComposite()).getTxtEmployeeOrAsset().setFocus();
					return;
				}
				
				//If batch is not created, prompt the user to create one.
				if (!batchNoCreated) {
					createBatch();
				}
				
				//If batch is not created yet,stop the Reconciliation Process.
				if (!isBatchCreated()) {
					resetValues();
					((ReconciliationComposite) this.getComposite()).getTxtEmployeeOrAsset().setFocus();
					return;
				}				
			}
			else if(!checkDates()){
					resetValues();
					return;
			}	
			
			if(!showMsg){
				showMsg = true;
				ProgressIndicatorUtil.openInProgressWindow();				
			}
			
			while(true)	{
				
					if (barcodeQueue.size() == 0) {
						log.info("Barcode Queue is Empty, Exiting");				
						hsInProcess = false;						
						return;
					}
					
					barcode = barcodeQueue.remove(0);
					if(log.isDebugEnabled())
						log.debug("barcode read from queue " + barcode);					
					((ReconciliationComposite) composite).getTxtBarcode().setText(barcode);
					batchReclForm.setBarcode(barcode);
					
					if (barcode.equals(IVoucherConstants.RECON_CTRL_BTN_SUBMIT)){
						hsInProcess = false;
						showMsg = false;
						ProgressIndicatorUtil.closeInProgressWindow();
						processSubmit((ReconciliationComposite) getComposite());					
					}	
					else {
						popHSBarcode();						
					}
						
			}
			

		} catch (Exception e) {			
			log.error("Error occured in run of ReconillationController " + e);
			log.info("Notifying the User Interface thread to Start Processing Again");
			hsInProcess = false;			
			composite.getDisplay().asyncExec(this);
		}
		finally
		{
			ProgressIndicatorUtil.closeInProgressWindow();
			showMsg = false;
		}
	}

	/**
	 * If a batch is not created, error message is displayed and all the vouchers need to be rescanned.
	 * @return
	 * @throws Exception
	 */
	private boolean isBatchCreated() throws Exception{

		if (!batchNoCreated) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOBATID_RESCAN_VOUCHERS)));
			log.info("Batch # not set,exiting");
			barcodeQueue.clear();
			hsInProcess = false;
			return false;
		}
		
		return checkDates();
		
	}
	/**
	 * Checking If the Endtime is after the start time.If not a error message is displayed and all the tickets need to be rescanned.
	 * @return
	 */
	private boolean checkDates() {
		
		if( (!((BatchReconciliationForm)getSdsForm()).getEndTime().after(((BatchReconciliationForm)getSdsForm()).getStartTime()))) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_ENDTIME_BEFORE_START_RESCAN_VOUCHERS)));
			log.info("End time is before start time,exiting");
			barcodeQueue.clear();
			hsInProcess = false;
			return false;
		}
		
		return true;
	}
	
	/**
	 * check if a Valid employeeId is Assigned. If not a error message is displayed and all the tickets need to be rescanned. 
	 * @return
	 * @throws Exception
	 */
	private synchronized boolean isEmployeeAssigned() throws Exception {
		
		String empId = ((BatchReconciliationForm)getSdsForm()).getEmployeeAssetId();		
		if (empId == null || empId.equals("")) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOEMPID_RESCAN_VOUCHERS)));
			log.info("EmployeeAssetId not assigned,exiting");
			barcodeQueue.clear();
			hsInProcess = false;
			return false;
		}
		else
		{
			UserDTO  userDTO = null;
			userDTO = FrameworkServiceLocator.getService().getUserDetails(empId, SDSApplication.getSiteDetails().getId());
			if(userDTO == null || userDTO.getUserName() == null)
			{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOEMPID_RESCAN_VOUCHERS)));
				log.info("Valid EmployeeAssetId not assigned,exiting");
				barcodeQueue.clear();
				hsInProcess = false;
				return false;
			}
		}
		return true;
	}

	/**
	 * This method is used to select the file in file dialog
	 */
	public String fileSelectXml(){   
		log.info("****************Entering into UploadXmlController::fileSelectXml method *****************************");
		String[] filterExtensions = {"*.xml"};
		FileDialog fileDialog = new FileDialog(((ReconciliationComposite)composite).getShell(), SWT.OPEN);
		fileDialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.CHOOSE_XML_FILE));
		fileDialog.setFilterPath("");
		fileDialog.setFilterExtensions(filterExtensions);
		log.info("****************Exit of UploadXmlController::fileSelectXml method ***********************************");
		return fileDialog.open();
	}	

	/**
	 * This method will parse and Upload the xml data as batch
	 */
	public void parseUploadXml() throws Exception{
		
	    SAXBuilder builder = new SAXBuilder();
	    // command line should offer URIs or file names
	    try {
	    	String filePath = batchReclForm.getTxtUploadXmlFile();
	    	InputStream inStream = new FileInputStream(filePath);
	    	Document doc = builder.build(inStream);
	    	Element root = doc.getRootElement(); 
	    	List<?> countRoomS = root.getChildren("CountRoom"); 
	    	/*
	    	 * Parse CountRoom
	    	 */
		    	for (int i = 0; i < countRoomS.size(); i++) {
		    		Element countRoom = (Element) countRoomS.get(i);
		    		List<?> currencyCounterS = countRoom.getChildren("CurrencyCounter"); 
	
		    		/*
		    		 * clear the previous list
		    		 */
		    		addAllBarcodeQuefromXML.clear();
					    		/*
					    		 * Parse CurrencyCounter
					    		 */
					    	for (int j = 0; j < currencyCounterS.size(); j++) {
					    		Element currencyCounter = (Element) currencyCounterS.get(j);
					    		List<?> billCassetteS = currencyCounter.getChildren("BillCassette"); 
							    		/*
							    		 * Parse BillCassette
							    		 */
							    		for(int k=0; k < billCassetteS.size(); k++){
							    			Element billCassette = (Element) billCassetteS.get(k);
							    			addEmployeeToQueuefromXML(billCassette.getAttributeValue("slotNumber"));
							    			List<?> ticketSetS = billCassette.getChildren("TicketSet");
							    			String employeeID = null;
							    			if(ticketSetS.size()!=0){
							    				employeeID = billCassette.getAttributeValue("slotNumber");
							    			}
													/*
													 * Parse TicketSet
													 */
													for(int l=0; l<ticketSetS.size(); l++ ){
														Element ticketSet = (Element)  ticketSetS.get(l);
														List<?> ticketS = ticketSet.getChildren("Ticket");
															/*
															 * Parse Ticket
															 */
														ArrayList<String> barcodeListfrmXML = new ArrayList<String>();
															for(int m=0; m<ticketS.size(); m++){
																Element ticket = (Element)  ticketS.get(m);
//																addToQueuefromXML(ticket.getAttributeValue("barcode"));
																barcodeListfrmXML.add(ticket.getAttributeValue("barcode"));
															}
															
															if(employeeID!=null && employeeID.trim().length() ==5){
																if(empIdPrefix ==null){
																	Properties properties = new Properties();
																    try {
																    	properties.load(new FileInputStream("./settings/reconciliation.properties"));
																    	empIdPrefix = properties.getProperty("empid.prefix");
																    }catch (IOException e) {
																    	log.error("Failed to load the property file "+e);
																    	empIdPrefix = null;
																    }
																}
																if(!employeeID.trim().startsWith(empIdPrefix.trim())){
																	log.info("Employee id prefix in xml does not match with property file.");
																	MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_EMPID_PRE_NOT_MATCH_FOR) + " "+employeeID));
//																	barcodeQueuefrmXML.clear();
																	addAllBarcodeQuefromXML.clear();
																	return;
																}
																String emp = employeeID.substring(1, 5);
																addAllBarcodeQuefromXML.put(emp, barcodeListfrmXML);
															}else{
																log.info("Employee id is null or invalid length.");
																MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_EMPID_NULL_OR_INVALIDLEN)));
//																barcodeQueuefrmXML.clear();
																addAllBarcodeQuefromXML.clear();
																return;
															}
													}
							    		
							    		}
					    	}
	
		    	}

	    	if(addAllBarcodeQuefromXML!=null && addAllBarcodeQuefromXML.size()>0) {
	    		System.out.println("No of emplements with employee id are parsed " + addAllBarcodeQuefromXML.size());
	    		
	    		/*
	    		 * Reseting any previous data from form.
	    		 */
	    		batchReclForm.resetScannedTickets();
    			
		    		if(!doSingleXmlUpload.equalsIgnoreCase("true")){
		    			BatchReconciliationForm batchReconciliationForm = (BatchReconciliationForm) getSdsForm();
		    			ArrayList<String> barcodesfrmXML = new ArrayList<String>();
		    			String empId = addAllBarcodeQuefromXML.firstEntry().getKey();
		    			barcodesfrmXML = (ArrayList<String>) addAllBarcodeQuefromXML.firstEntry().getValue();
		    			
		    			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setText(empId);
		    			batchReclForm.setEmployeeAssetId(empId);
		    			
		    			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());
						if( userDTO == null || userDTO.getUserName() == null ) {
							ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
							addAllBarcodeQuefromXML.remove(empId);
							return;
						}
		    			createBatchfrmXML();
		    			populateBatchforXML(empId, barcodesfrmXML);
		    			processSubmit((ReconciliationComposite) getComposite());
		    			addAllBarcodeQuefromXML.remove(empId);
		    			batchReconciliationForm.resetScannedTickets();
			    		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
			    		batchReclForm.setTxtUploadXmlFile("");
		    			reconciliationComposite.getLblTxtXmlFile().getSdsText().setText("");
		    			return;
		    		}	    	
		    		Set<?> setofBatch = addAllBarcodeQuefromXML.entrySet(); 
		    		Iterator<?> noOfBatches = setofBatch.iterator();
		    		
		    		while(noOfBatches.hasNext()) { 
		    			ArrayList<String> barcodesfrmXML = new ArrayList<String>();
		    			Map.Entry entrySet = (Map.Entry)noOfBatches.next(); 
		    			barcodesfrmXML = (ArrayList<String>) entrySet.getValue();
		    			((ReconciliationComposite)composite).getTxtEmployeeOrAsset().setText(entrySet.getKey().toString());
		    			batchReclForm.setEmployeeAssetId(entrySet.getKey().toString());
		    			System.out.println("processing batch for employee" + entrySet.getKey().toString());
		    			
		    			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(batchReclForm.getEmployeeAssetId(), SDSApplication.getSiteDetails().getId());
						if( userDTO == null || userDTO.getUserName() == null ) {
							ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));
							reconciliationComposite.getTxtEmployeeOrAsset().setFocus();
							continue;
						}
		    			createBatchfrmXML();
		    			
		    			ArrayList<String> copyBarcodesfrmXML = (ArrayList<String>) barcodesfrmXML.clone();

		    			populateBatchforXML(entrySet.getKey().toString(), barcodesfrmXML);
		    			processSubmitforXML((ReconciliationComposite) getComposite(),  copyBarcodesfrmXML);
		    			
		    			/*BatchReconciliationForm form = (BatchReconciliationForm) getSdsForm();
		    			form.resetScannedTickets();*/
		    			
		    		}
		    		/*
		    		 * Clearing the form data after all batches creation.
		    		 */
		    		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		    		batchReclForm.setTxtUploadXmlFile("");
		    		batchReclForm.resetScannedTickets();
	    			reconciliationComposite.getLblTxtXmlFile().getSdsText().setText("");
	    			addAllBarcodeQuefromXML.clear();
	    	}else{
		    	MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NO_DATA_IN_XML_UPLOAD)));
		    	return;
	    	}
	    }
	    catch (JDOMException e) { 
	    	// indicates a well-formedness error
	    	log.error("XML is not well-formed. "+e);
	    	MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOT_WELL_FORMED_XML)));
	    }  
	    catch (IOException e) { 
	    	log.error("Could not check parse the xml because " + e);
	    	MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NOT_VALID_XMLFILEPATH)));
	    }  
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  }
	
	/*
	 * This methods populates batch created from XML upload
	 */
	public void populateBatchforXML(String empID, ArrayList<String> barcodesfrmXML){
		
		String barcode;
		if(barcodesfrmXML !=null && barcodesfrmXML.size()>0){
			while(true)	{
				
				if (barcodesfrmXML.size() == 0) {
					log.info("Barcode Queue is Empty, Exiting");				
					return;
				}
				
				barcode = barcodesfrmXML.remove(0);
				if(log.isDebugEnabled())
					log.debug("barcode read from queue " + barcode);					
				((ReconciliationComposite) composite).getTxtBarcode().setText(barcode);
				batchReclForm.setBarcode(barcode);
				
				if (barcode.equals(IVoucherConstants.RECON_CTRL_BTN_SUBMIT)){
					showMsg = false;
					ProgressIndicatorUtil.closeInProgressWindow();
					processSubmit((ReconciliationComposite) getComposite());					
				}	
				else {
					popHSBarcode();						
				}
			}
		}
	}
	
	/*
	 * This methods reads the reconciliation method from the property file
	 */
	public static void loadReconMethodfromPropFile(){
	
		
		 Properties properties = new Properties();
		    try {
		        properties.load(new FileInputStream("./settings/reconciliation.properties"));
		        doSingleXmlUpload = properties.getProperty("reconciliation.single.upload");   
		        xmlRecon = properties.getProperty("xml.reconciliation.method"); 
		        empIdPrefix = properties.getProperty("empid.prefix"); 
		    } catch (IOException e) {
		    	log.error("Failed to load the property file "+e);
		    	doSingleXmlUpload = "false";
		    	xmlRecon = "false";
		    	empIdPrefix = null;
		    }
	}

	
	/**
	 * This method creates the batch for XML Upload when employee Id is taken from XML
	 */
	private void createBatchfrmXML() {
		ReconciliationComposite reconciliationComposite = (ReconciliationComposite) getComposite();
		try {
			if( (AppContextValues.getInstance().getBatchId() == null 
					|| AppContextValues.getInstance().getBatchId().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET))) 
					&& !batchNoCreated ) {
				//boolean response =MessageDialogUtil.displayTouchScreenConfirmDialogOnActiveShell(IDBLabelKeyConstants.TIME_VALUES_CONTINUE);
				ReconciliationInfoDTO serviceReconciliationInfoDTO = getServiceReconciliationInfoDTO();
				serviceReconciliationInfoDTO.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
				serviceReconciliationInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());
				ReconciliationInfoDTO reconciliationInfoDTO =  ServiceCall.getInstance().insertReconciliation(serviceReconciliationInfoDTO);
				if( reconciliationInfoDTO!=null ) {
					if( reconciliationInfoDTO.isErrorPresent() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(reconciliationInfoDTO));
					}
					else {
						AppContextValues.getInstance().setReclEmpId(batchReclForm.getEmployeeAssetId());
						AppContextValues.getInstance().setBatchId(reconciliationInfoDTO.getReclId().toString());
						reconciliationComposite.getLblBatchValue().setText(reconciliationInfoDTO.getReclId().toString());
//						batchNoCreated = true;						
					}
				}
			}
		} catch (VoucherEngineServiceException e1) {
			log.error("VoucherEngineServiceException occured in the createBatch mthd", e1);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(e1));
		} catch(Exception e1) {
			log.error("Exception occured in the createBatch mthd", e1);
			VoucherUIExceptionHandler.handleException(e1);
		}
	}
}

