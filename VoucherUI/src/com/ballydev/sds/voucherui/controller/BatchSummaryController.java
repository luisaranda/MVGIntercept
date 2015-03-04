/*****************************************************************************
 * $Id: BatchSummaryController.java,v 1.43.5.0, 2013-10-25 16:52:01Z, Sornam, Ramanathan$
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSTableViewer;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.service.FrameworkServiceLocator;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.BaseDTO;
import com.ballydev.sds.voucher.dto.BatchInfoDTO;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.BatchSummaryComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.composite.VoucherTSInputDialog;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.BatchDetailsForm;
import com.ballydev.sds.voucherui.form.BatchReconciliationForm;
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;
import com.ballydev.sds.voucherui.form.PrintBatchForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.BatchNoComparator;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUIValidator;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class BatchSummaryController extends SDSBaseController {


	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private BatchSummaryComposite composite = null;

	private BatchReconciliationForm batchReconciliationForm = null;

	private Composite parent;

	/**
	 * The maximum number of records allowed to display in each page as there is
	 * no scroll bar in a Touch Screen UI.
	 */
	private int numOfRecordsPerPage = 7;

	/**
	 * This field returns the total number of reconciled vouchers in the database.
	 */
	private int totalRecordCount = 0;

	/**
	 * The mode of totalRecordCount by numOfRecordsPerPage
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

	private List<BatchSummaryDTO> batchSummaryDTOList = new ArrayList<BatchSummaryDTO>();

	private SDSTSTableViewer tableViewer;

	private Image enableImage;
	
	private Image disableImage;
	
	public BatchSummaryController(Composite parent, int style, SDSValidator validator, BatchReconciliationForm batchReclForm) throws Exception {
		super(batchReclForm, validator);
		createImages();
		this.parent = parent;
		batchReconciliationForm = batchReclForm;
		createComposite(parent, style, batchReconciliationForm.isCashier());
		registerCustomizedListeners(composite);
		VoucherMiddleComposite.setCurrentComposite(composite);
	}

	public BatchSummaryController(Composite parent, int style, BatchReconciliationForm batchForm, SDSValidator validator, boolean is) throws Exception {
		super(batchForm, validator);
		this.parent = parent;
		batchReconciliationForm = batchForm;
		createComposite(parent, style, batchReconciliationForm.isCashier());
		registerCustomizedListeners(composite);
		VoucherMiddleComposite.setCurrentComposite(composite);
	}

	private void createComposite(Composite parent, int style,boolean isCashier) {
		composite = new BatchSummaryComposite(parent, style, isCashier);
		try {
			BatchSummaryComposite batchSummaryComposite = ((BatchSummaryComposite)composite);
			batchSummaryComposite.getILblChangeEmployeeOrKiosk().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_CHANGE_EMPLOYEE));
			if( VoucherUtil.getReconLevel() == 1 ) {
				disableChangeEmployeeOrKiosk(batchSummaryComposite);
			}
			for( int i = 0; i < batchReconciliationForm.getBatchSummaries().size(); i++ ) {
				BatchSummaryDTO batchSummaryDTO = new BatchSummaryDTO();
				ObjectMapping.copyAlikeFields(batchReconciliationForm.getBatchSummaries().get(i), batchSummaryDTO);
				batchSummaryDTOList.add(batchSummaryDTO);
			}	
			tableViewer = composite.getTableViewer();
			adjustTableSizeBasedOnResolution();
			loadBatchDetailsFromResponse();
			parent.layout();
			registerEvents(getComposite());
			populateScreen(composite);
			if(KeyBoardUtil.getKeyBoardShell()!=null){
				KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
			}

			enableImage = composite.getButtonImage();
			disableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
							//composite.getButtonDisableImage();
			
			disableLoadSelectedBatch(composite);
			disablePrintBatchDetails(composite);
			disableChangeEmployeeOrKiosk(composite);
			disableDeleteSelectedBatch(composite);
		} catch (Exception e) {
			log.error("Error occurred in BatchSummaryController.createComposite", e);
			e.printStackTrace();
		}

	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		composite.getTableViewer().getTable().addFocusListener(this);
		addTSTableColumnListener(composite.getTableViewer());

		BatchSummaryComposite batchSummaryComposite = ((BatchSummaryComposite)argComposite);

		batchSummaryComposite.getILblLoadSelectedBatch().addMouseListener(this);
		batchSummaryComposite.getILblLoadSelectedBatch().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblDeleteSelectedBatch().addMouseListener(this);
		batchSummaryComposite.getILblDeleteSelectedBatch().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblChangeEmployeeOrKiosk().addMouseListener(this);
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblPrintBatchDetails().addMouseListener(this);
		batchSummaryComposite.getILblPrintBatchDetails().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblCancel().addMouseListener(this);
		batchSummaryComposite.getILblCancel().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblNextRecords().addMouseListener(this);
		batchSummaryComposite.getILblNextRecords().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblLastPageRecords().addMouseListener(this);
		batchSummaryComposite.getILblLastPageRecords().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblPreviousRecords().addMouseListener(this);
		batchSummaryComposite.getILblPreviousRecords().getTextLabel().addTraverseListener(this);

		batchSummaryComposite.getILblFirstPageRecords().addMouseListener(this);
		batchSummaryComposite.getILblFirstPageRecords().getTextLabel().addTraverseListener(this);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		if( (Control) e.widget instanceof SDSImageLabel ) {
			try {
				eventOnBatchDetail(e);
			} catch(Exception e1) {
				log.error(e1);
			}
		}
	}

	public void widgetSelected(SelectionEvent e) {
		try {
			eventOnBatchTableDetail(e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void eventOnBatchTableDetail(TypedEvent typedEvent) {
		try {
			if( ((Control) typedEvent.getSource() instanceof Table) ) {
				
				Table tableCtrl = (Table)((Control) typedEvent.getSource());				
				BatchSummaryComposite batchSummaryComposite = (BatchSummaryComposite)getComposite(); 
				
				if( tableCtrl.getData("name").equals(IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMMARY) ){
					
					enableLoadSelectedBatch(batchSummaryComposite);
					enablePrintBatchDetails(batchSummaryComposite);
					
					if( VoucherUtil.getReconLevel() != 1 ){
						enableChangeEmployeeOrKiosk(batchSummaryComposite);
					}
					enableDeleteSelectedBatch(batchSummaryComposite);			
				}
			}
		} catch (Exception ex) {
			log.error("Error occurred in BatchSummaryController.eventOnBatchTableDetail", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * @param e
	 */
	private void eventOnBatchDetail(TypedEvent typedEvent) {
		try {
			Control triggeredCtrl = (Control) typedEvent.getSource();

			if( (triggeredCtrl instanceof SDSImageLabel) ) {

				/**MAKE THE SELECTED ITEM VALUE TO NULL BEFORE EACH SELECTION, 
				 * BCOZ THE POPULATE FORM IS CALLED ONCE AT THE START**/
				batchReconciliationForm.setBatchSummaryDTO(null);

				populateForm(composite);
				BatchSummaryComposite batchSummaryComposite = (BatchSummaryComposite)getComposite(); 

				if( batchSummaryComposite != null ) {
					/** User clicked next arrow to get next page of records **/
					if( typedEvent.getSource().equals(batchSummaryComposite.getILblNextRecords()) ) {
						showNextPage();
						disableLoadSelectedBatch(batchSummaryComposite);
						disablePrintBatchDetails(batchSummaryComposite);
						disableChangeEmployeeOrKiosk(batchSummaryComposite);
						disableDeleteSelectedBatch(batchSummaryComposite);	
					}

					/** User clicked previous arrow to get previous page of records **/
					else if( typedEvent.getSource().equals(batchSummaryComposite.getILblPreviousRecords()) ) {
						showPreviousPage();
						disableLoadSelectedBatch(batchSummaryComposite);
						disablePrintBatchDetails(batchSummaryComposite);
						disableChangeEmployeeOrKiosk(batchSummaryComposite);
						disableDeleteSelectedBatch(batchSummaryComposite);	
					}

					/** User clicked first page arrow to get previous page of records **/
					else if( ((SDSImageLabel) triggeredCtrl).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW) ) {
						if( totalRecordCount > numOfRecordsPerPage ) {
							initialIndex = (numOfRecordsPerPage*2) < totalRecordCount ? (numOfRecordsPerPage*2) : totalRecordCount; 
							showPreviousPage();
							tableViewer.setCurrentPage(1);
							composite.getLblTotalPageCount().setText(
									/*pageCountString +*/ tableViewer.getCurrentPage() + "/" + numOfPages);
							composite.layout();
							HideAndShowPageButtons();
							disableLoadSelectedBatch(batchSummaryComposite);
							disablePrintBatchDetails(batchSummaryComposite);
							disableChangeEmployeeOrKiosk(batchSummaryComposite);
							disableDeleteSelectedBatch(batchSummaryComposite);	
						}
					}

					/** User clicked last page arrow to get previous page of records **/
					else if( ((SDSImageLabel) triggeredCtrl).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_LAST_ARROW) ) {
						if( totalRecordCount > numOfRecordsPerPage ) {
							if(modValue > 0) {
								initialIndex = totalRecordCount - modValue;						
							}else if( modValue == 0 ) {
								initialIndex = totalRecordCount - numOfRecordsPerPage;
							}
							showNextPage();
							tableViewer.setCurrentPage(numOfPages);
							composite.getLblTotalPageCount().setText(
									/*pageCountString +*/ tableViewer.getCurrentPage() + "/" + numOfPages);
							composite.layout();
							HideAndShowPageButtons();
							disableLoadSelectedBatch(batchSummaryComposite);
							disablePrintBatchDetails(batchSummaryComposite);
							disableChangeEmployeeOrKiosk(batchSummaryComposite);
							disableDeleteSelectedBatch(batchSummaryComposite);	
						}
					}else if (((SDSImageLabel) triggeredCtrl).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_PRINT_BATCH_DTL)) {
						if((batchReconciliationForm.getBatchSummaryDTO()==null)||(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber()==null)){
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TABLE_ITEM));
							return;
						}
						new PrintBatchController(new PrintBatchForm(), null, batchReconciliationForm.getBatchSummaryDTO(), batchSummaryDTOList);
					}
					else if( triggeredCtrl.equals(batchSummaryComposite.getILblLoadSelectedBatch()) ) {
						if( (batchReconciliationForm.getBatchSummaryDTO()==null)||(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber()==null)){
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TABLE_ITEM));
							return;
						}
						//Load the selected Batch
						Composite parent = getComposite().getParent();
						getComposite().dispose();
						batchReconciliationForm.getBatchSummaries().clear();
						ArrayList<BatchSummaryDTO> batchSummaryDTOLst = new ArrayList<BatchSummaryDTO>();
						for(int i=0;i<batchSummaryDTOList.size();i++){
							BatchSummaryDTO batchSummaryDTO2 = new BatchSummaryDTO();
							ObjectMapping.copyAlikeFields(batchSummaryDTOList.get(i), batchSummaryDTO2);
							batchSummaryDTOLst.add(batchSummaryDTO2);

						}
						batchReconciliationForm.setBatchSummaries(batchSummaryDTOLst);	
						List<BatchSummaryDTO> batchSummaries = (batchReconciliationForm).getBatchSummaries();					
						BatchSummaryDTO batchSummaryDTO  = null;
						if(batchSummaries != null && !batchSummaries.isEmpty()){							
							for(int j=0;j<batchSummaries.size();j++){
								batchSummaryDTO  = batchSummaries.get(j);	
								if(((batchSummaryDTO.getBatchNumber().equals(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber())))){
									batchSummaryDTO  = batchSummaries.get(j);									
									BatchDetailsForm batchDetailsForm = new BatchDetailsForm();
									batchDetailsForm.setBatchNbrValue(batchSummaryDTO.getBatchNumber().toString());
									batchDetailsForm.setBatchCouponAmountValue( ConversionUtil.voucherAmountFormat( batchSummaryDTO.getCouponAmount() != null ?batchSummaryDTO.getCouponAmount().toString() : ""));
									batchDetailsForm.setBatchCouponCountValue(batchSummaryDTO.getCouponCount() != null ?batchSummaryDTO.getCouponCount().toString() : "");
									batchDetailsForm.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat( batchSummaryDTO.getVoucherAmount() != null ?batchSummaryDTO.getVoucherAmount().toString() : ""));
									batchDetailsForm.setBatchVoucherCountValue(batchSummaryDTO.getVoucherCount() != null ?batchSummaryDTO.getVoucherCount().toString() : "");
									batchDetailsForm.setBatchEmployeeValue( batchSummaryDTO.getEmpId());
									//batchReconciliationForm.setCashier(true);
									batchReconciliationForm.setBatchId(batchSummaryDTO.getBatchNumber());
									batchReconciliationForm.setEmployeeAssetId( batchSummaryDTO.getEmpId());
									new BatchDetailsController(parent,SWT.NONE, batchDetailsForm,null, batchReconciliationForm,false);
									return;									
								}
							}
						} else {
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_NO_BATCH_AVAILABLE));
						}


					} else if (triggeredCtrl.equals(batchSummaryComposite.getILblDeleteSelectedBatch())) {
						try {
							if((batchReconciliationForm.getBatchSummaryDTO()==null)||(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber()==null)){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TABLE_ITEM));
								return;
							}
							ArrayList<BatchSummaryDTO> batchSummaryDTOLst = new ArrayList<BatchSummaryDTO>();
							for(int i=0;i<batchSummaryDTOList.size();i++){
								BatchSummaryDTO batchSummaryDTO2 = new BatchSummaryDTO();
								ObjectMapping.copyAlikeFields(batchSummaryDTOList.get(i), batchSummaryDTO2);
								batchSummaryDTOLst.add(batchSummaryDTO2);

							}
							batchReconciliationForm.setBatchSummaries(batchSummaryDTOLst);					
							List<BatchSummaryDTO> batchSummaries = (batchReconciliationForm).getBatchSummaries();
							if(batchSummaries != null && !batchSummaries.isEmpty()){
								BatchSummaryDTO batchSummaryDTO = null;							
								for(int j=0;j<batchSummaries.size();j++){
									batchSummaryDTO  = batchSummaries.get(j);								
									if(batchSummaries.size()>1 && ((batchSummaryDTO.getBatchNumber().equals(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber())))){
										batchSummaryDTO  = batchSummaries.get(j);							

										if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_DELETE_CONFIRMATION), batchSummaryComposite)== true){

											BatchInfoDTO batchForm = new BatchInfoDTO();
											batchForm.setBatchId(batchSummaryDTO.getBatchNumber());
											batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
											BaseDTO baseForm =ServiceCall.getInstance().cancelBatch(batchForm);
											if(baseForm!=null){
												if(baseForm.isErrorPresent())
												{
													MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
												}
												else
												{
													Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), "", batchSummaryDTO.getBatchNumber().toString(), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_DELETED), EnumOperation.DELETE_OPERATION,PreferencesUtil.getClientAssetNumber());
													MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
													batchReconciliationForm.getBatchSummaries().remove(batchSummaryDTO);
													remove(batchSummaryDTO);
													batchSummaryDTOList.remove(batchSummaryDTO);
													deleteTableItem(composite.getTableViewer().getTable());
													loadBatchDetailsFromResponse();
													adjustTableSizeBasedOnResolution();
													HideAndShowPageButtons();
													populateScreen(composite);													
													disableLoadSelectedBatch(batchSummaryComposite);
													disablePrintBatchDetails(batchSummaryComposite);
													disableChangeEmployeeOrKiosk(batchSummaryComposite);
													disableDeleteSelectedBatch(batchSummaryComposite);

												}
											}
										}
										return;
									}
									else if( batchSummaries.size()==1){
										batchSummaryDTO  = batchSummaries.get(0);	

										if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_DELETE_CONFIRMATION), batchSummaryComposite)== true){

											BatchInfoDTO batchForm = new BatchInfoDTO();										
											batchForm.setBatchId(batchSummaryDTO.getBatchNumber());
											batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
											BaseDTO baseForm =ServiceCall.getInstance().cancelBatch(batchForm);
											if(baseForm!=null){
												if(baseForm.isErrorPresent())
												{
													MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
												}
												else
												{
													MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
													Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), "", batchSummaryDTO.getBatchNumber().toString(), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_DELETED), EnumOperation.DELETE_OPERATION,PreferencesUtil.getClientAssetNumber());
													batchReconciliationForm.getBatchSummaries().clear();
													batchSummaryDTOList.clear();
													batchReconciliationForm.setEmployeeAssetId("");
													batchReconciliationForm.setBatchId(null);
													deleteTableItem(composite.getTableViewer().getTable());
													populateScreen(composite);
													disableLoadSelectedBatch(batchSummaryComposite);
													disablePrintBatchDetails(batchSummaryComposite);
													disableChangeEmployeeOrKiosk(batchSummaryComposite);
													disableDeleteSelectedBatch(batchSummaryComposite);
													batchSummaryComposite.getTableViewer().getTable().clearAll();
												}
											}
										}
									}
								}
							}
							else{
								return;
							}
						}catch(VoucherEngineServiceException ex)	{
							log.error("Exception while cancelling the batch", ex);						
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
						}


					} else if( triggeredCtrl.equals(batchSummaryComposite.getILblChangeEmployeeOrKiosk()) ) {
						if( ( batchReconciliationForm.getBatchSummaryDTO() == null ) 
								|| (batchReconciliationForm.getBatchSummaryDTO().getBatchNumber() == null) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TABLE_ITEM));
							return;
						}
						try {
							//Change Employee
							VoucherTSInputDialog dialog = new VoucherTSInputDialog(batchSummaryComposite.getShell(),SWT.None|SWT.APPLICATION_MODAL);
							dialog.setLocation(170,250);
							String result =null;							
							dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));	
							result = dialog.open(5,true);							
							changeCashier(result);
						} catch(VoucherEngineServiceException ex)	{
							log.error("Exception while changing the location", ex);						
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
						}

					}
					else if(triggeredCtrl.equals(batchSummaryComposite.getILblCancel())){
						showReconciliationPage();

					}
				}
			} else if( ((Control) typedEvent.getSource() instanceof Table) ){
				Table tableCtrl = (Table)((Control) typedEvent.getSource());				
				BatchSummaryComposite batchSummaryComposite = (BatchSummaryComposite)getComposite(); 
				
				if( tableCtrl.getData("name").equals(IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMMARY) ){
					enableLoadSelectedBatch(batchSummaryComposite);
					enablePrintBatchDetails(batchSummaryComposite);
					
					if( VoucherUtil.getReconLevel() != 1 ){
						enableChangeEmployeeOrKiosk(batchSummaryComposite);
					}
					enableDeleteSelectedBatch(batchSummaryComposite);			
				}

			}
		} catch (Exception ex) {
			log.error("Error occurred in BatchSummaryController.eventOnBatchDetail", ex);
			ex.printStackTrace();
		}
	}

	private void remove(BatchSummaryDTO batchSummaryDTO) {
		int index = -1;
		boolean flag = false;
		for(BatchSummaryDTO tempDTO : batchSummaryDTOList) {
			++index;
			if(tempDTO.getBatchNumber().equals(batchSummaryDTO.getBatchNumber())) {
				flag = true;
				break; 
			}
		}
		if(flag) {
			batchSummaryDTOList.remove(index);
		}
	}

	/**
	 * This method shows the reconciliation page
	 * @throws Exception
	 */
	private void showReconciliationPage() throws Exception {
		getComposite().dispose();
		/**CHECK IF THE CALL TO THE RECONCILATION PAGE IS FROM THE BATCH SUMMARY
		 *DETAILS PAGE OR FROM THE CURRENT BATCH DETAILS PAGE
		 * IF FROM THE SUMMARY PAGE, ASSIGN THE BATCH ID TO NULL
		 * **/

		if( batchReconciliationForm.getBatchSummaries().size() > 2 ) {
			batchReconciliationForm.setBatchId(null);
			batchReconciliationForm.setEmployeeAssetId(null);
			batchReconciliationForm.getBatchSummaries().clear();
		}
		/* Retrofit for PCI*/
		batchReconciliationForm.setAccepted("");
		batchReconciliationForm.setTotal("");
		batchReconciliationForm.setUncommitted("");
		batchReconciliationForm.setNotRead("");
		
		ReconciliationController ctrl = new ReconciliationController(parent, SWT.NONE,batchReconciliationForm,new SDSValidator(getClass(),true));
		ctrl.populateScreen(ctrl.getComposite());
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {		
		super.focusGained(e);
		((BatchSummaryComposite)composite).redraw();
	}

	private void showPreviousPage() throws Exception{
		if(tableViewer.getCurrentPage() == 1){
			return;
		}

		tableViewer.setCurrentPage(tableViewer.getCurrentPage() - 1);

		currentPage--;
		composite.getLblTotalPageCount().setText(
				/*pageCountString +*/  tableViewer.getCurrentPage() + "/" + numOfPages);
		composite.layout();
		composite.getTableViewer().getTable()
		.setSelection(-1);

		if (initialIndex > totalRecordCount - modValue) {
			batchReconciliationForm.getBatchSummaries().clear();			

			int count = initialIndex - numOfRecordsPerPage - modValue;
			for (int i = count; i < count + numOfRecordsPerPage; i++) {

				BatchSummaryDTO batchSummaryDTO = batchSummaryDTOList.get(i);
				batchReconciliationForm.getBatchSummaries().add(batchSummaryDTO);

			}
			populateScreen(composite);
			initialIndex -= modValue;		

		} else if (initialIndex > numOfRecordsPerPage && initialIndex <= (totalRecordCount - modValue)) {
			initialIndex -= numOfRecordsPerPage;
			batchReconciliationForm.getBatchSummaries().clear();

			int count = initialIndex - numOfRecordsPerPage;
			for (int i = count; i < count + numOfRecordsPerPage; i++) {
				BatchSummaryDTO batchSummaryDTO = batchSummaryDTOList.get(i);				
				batchReconciliationForm.getBatchSummaries().add(batchSummaryDTO);
			}
			populateScreen(composite);
		} 
		composite.layout();
		HideAndShowPageButtons();
	}

	private void showNextPage() throws Exception{
		if(tableViewer.getCurrentPage() == numOfPages){
			return;
		}
		tableViewer.setCurrentPage(tableViewer.getCurrentPage() + 1);
		composite.getLblTotalPageCount().setText(/*pageCountString+*/tableViewer.getCurrentPage()+"/"+numOfPages);
		composite.layout();
		composite.getTableViewer().getTable().setSelection(-1);

		if (initialIndex < totalRecordCount - modValue) {
			batchReconciliationForm.getBatchSummaries().clear();					
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				BatchSummaryDTO batchSummaryDTO = batchSummaryDTOList.get(i);
				batchReconciliationForm.getBatchSummaries().add(batchSummaryDTO);
			}
			populateScreen(composite);
			initialIndex += numOfRecordsPerPage;
		} else if (initialIndex < totalRecordCount) {
			batchReconciliationForm.getBatchSummaries().clear();					
			for (int i = initialIndex; i < initialIndex + modValue; i++) {
				BatchSummaryDTO batchSummaryDTO =batchSummaryDTOList.get(i);
				batchReconciliationForm.getBatchSummaries().add(batchSummaryDTO);
			}
			populateScreen(composite);
			initialIndex += modValue;

		}
		composite.layout();
		HideAndShowPageButtons();
	}

	private void HideAndShowPageButtons() {
		//Don't hide/show. Just Enable/Disable
		if(tableViewer.getCurrentPage() == numOfPages) {
			composite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
			composite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_LAST_PAGE)));
		}
		else {
			composite.getILblNextRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_NEXT_PAGE)));
			composite.getILblLastPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_LAST_PAGE)));
		}
		if(tableViewer.getCurrentPage() == 1 || tableViewer.getCurrentPage() == 0) {	
			composite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
			composite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_FIRST_PAGE)));
		}
		else {
			composite.getILblPreviousRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_PREVIOUS_PAGE)));
			composite.getILblFirstPageRecords().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.DISPLAY_FIRST_PAGE)));
		}
	}

	/**
	 * This method performs the change cashier/ location operation 
	 * @param result
	 * @throws VoucherEngineServiceException
	 */
	private void changeCashier(String result) throws VoucherEngineServiceException{
		try {
			result = VoucherStringUtil.lPadWithZero(result, 5);
			BatchSummaryComposite batchSummaryComposite = (BatchSummaryComposite)getComposite();
			populateForm(batchSummaryComposite);
			String oldEmpValue = batchReconciliationForm.getBatchSummaryDTO().getEmpId().trim(); 
				
			if(result != null && result.trim().equalsIgnoreCase(oldEmpValue)){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_EDIT_VALUE_ALREADY_ENTERED));
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(batchSummaryComposite.getShell(),SWT.None|SWT.APPLICATION_MODAL);
				dialog.setLocation(170,250);
				String retValue =null;				
				dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));	
				retValue = dialog.open(5,true);
				changeCashier(retValue);
				return;
			}

			if(result != null){

				// Check the validity of the changed employee				
				String errorMsg = VoucherUIValidator.validateEmployeeId(result);
				if(errorMsg.trim().length()!=0){
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(errorMsg);
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(batchSummaryComposite.getShell(),SWT.None|SWT.APPLICATION_MODAL);
					dialog.setLocation(170,250);
					String retValue =null;
					dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));	
					retValue = dialog.open(5,true);					
					changeCashier(retValue);
					return;
				}

				UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(result, SDSApplication.getSiteDetails().getId());				
				if(userDTO == null || userDTO.getUserName() == null) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));	
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(batchSummaryComposite.getShell(),SWT.None|SWT.APPLICATION_MODAL);
					dialog.setLocation(170,250);
					String retValue =null;					
					dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));	
					retValue = dialog.open(5,true);					
					changeCashier(retValue);
					return;
				}				
				populateForm(composite);
				BatchInfoDTO batchForm = new BatchInfoDTO();
				batchForm.setEmployeeId(result);				
				batchForm.setReconTypeId(1);
				BatchSummaryDTO batchSummaryDTO = null;
				BaseDTO baseForm =  null;
				batchReconciliationForm.getBatchSummaries().clear();
				ArrayList<BatchSummaryDTO> batchSummaryDTOLst = new ArrayList<BatchSummaryDTO>();
				for(int i=0;i<batchSummaryDTOList.size();i++){
					BatchSummaryDTO batchSummaryDTO2 = new BatchSummaryDTO();
					ObjectMapping.copyAlikeFields(batchSummaryDTOList.get(i), batchSummaryDTO2);
					batchSummaryDTOLst.add(batchSummaryDTO2);

				}
				batchReconciliationForm.setBatchSummaries(batchSummaryDTOLst);			
				List<BatchSummaryDTO> batchSummaries = (batchReconciliationForm).getBatchSummaries();
				if(batchSummaries != null && !batchSummaries.isEmpty()){											
					for(int j=0;j<batchSummaries.size();j++){
						batchSummaryDTO  = batchSummaries.get(j);								
						if(((batchSummaryDTO.getBatchNumber().equals(batchReconciliationForm.getBatchSummaryDTO().getBatchNumber())))){
							batchSummaryDTO  = batchSummaries.get(j);						
							batchForm.setBatchId(batchSummaryDTO.getBatchNumber());		
							batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
							baseForm =  ServiceCall.getInstance().changeLocation(batchForm);						
							batchReconciliationForm.setEmployeeAssetId(result);								
							batchReconciliationForm.getBatchSummaries().get(j).setEmpId(result);													
							batchReconciliationForm.getBatchSummaries().get(j).setCashierOrKiosk(result);							
						}
					}
				}	

				if(baseForm!=null){
					if(baseForm.isErrorPresent()){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
					}
					else {		
						Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EMPLOYEE_ID),oldEmpValue, batchReconciliationForm.getEmployeeAssetId(), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_CASHIER_CHANGED)+batchSummaryDTO.getBatchNumber(), EnumOperation.MODIFY_OPERATION,PreferencesUtil.getClientAssetNumber());
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID) + LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_EMPLOYEE_ID_CHANGE_SUCCESS)
								+"\n"+VoucherUtil.getI18nMessageForDisplay(baseForm));						
					}
				}
				batchSummaryDTOList.clear();
				for(int i=0;i<batchReconciliationForm.getBatchSummaries().size();i++){		
					BatchSummaryDTO batchSummary = new BatchSummaryDTO();
					ObjectMapping.copyAlikeFields(batchReconciliationForm.getBatchSummaries().get(i), batchSummary);
					batchSummaryDTOList.add(batchSummary);
				}	
				loadBatchDetailsFromResponse();
				populateScreen(composite);
				disableLoadSelectedBatch(batchSummaryComposite);
				disablePrintBatchDetails(batchSummaryComposite);
				disableChangeEmployeeOrKiosk(batchSummaryComposite);
				disableDeleteSelectedBatch(batchSummaryComposite);			
			}

		} catch(VoucherEngineServiceException ex)	{
			log.error("Exception while changing the location", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
		catch(Exception ex)	{
			log.error("Exception while changing the location", ex);					

		}


	}

	/**
	 * Loads the details into the table viewer from dataQueries the DB and l 
	 *
	 */
	@SuppressWarnings("unchecked")
	private void loadBatchDetailsFromResponse()
	{
		totalRecordCount = 0;
		modValue = 0;
		numOfPages=0;
		//currentPage=0;
		initialIndex = 0;
		tableViewer.setCurrentPage(0);

		composite.getTableViewer().setFullModelList(batchSummaryDTOList);
		composite.getTableViewer().setNumOfRecordsPerPage(numOfRecordsPerPage);
//		composite.getTableViewer().sortFullModel(batchReconciliationForm, composite.getBatchNoComparator());
		BatchNoComparator batchNoComparator = new BatchNoComparator();
		batchNoComparator.setDirection(SWT.DOWN);
		Collections.sort(batchSummaryDTOList, batchNoComparator);
		
//		composite.layout();
		batchReconciliationForm.getBatchSummaries().clear();

		if(batchSummaryDTOList!=null){			
			totalRecordCount = batchSummaryDTOList.size();
		}

		if(totalRecordCount > 0){		
			numOfPages=totalRecordCount/numOfRecordsPerPage;
			modValue = totalRecordCount % numOfRecordsPerPage;
			if(modValue>0){
				numOfPages+=1;
			}
			if(totalRecordCount < numOfRecordsPerPage){
				numOfRecordsPerPage = totalRecordCount;
			}
			for (int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++) {
				BatchSummaryDTO batchSummaryDTO = new BatchSummaryDTO();
				ObjectMapping.copyAlikeFields(batchSummaryDTOList.get(i), batchSummaryDTO);					
				batchReconciliationForm.getBatchSummaries().add(batchSummaryDTO);
			}			
			tableViewer.setCurrentPage(tableViewer.getCurrentPage() + 1);
			initialIndex += numOfRecordsPerPage;			

			//pageCountString = LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
			composite.getLblTotalPageCount().setText(/*pageCountString + " " + */tableViewer.getCurrentPage() + "/" + numOfPages);
			composite.layout();


			try {
				if (composite.getTableViewer().getTable().getHorizontalBar().isVisible()) {
					ScrollBar horizondalScroll = composite.getTableViewer().getTable().getHorizontalBar();				
					horizondalScroll.setThumb(1);

				}
				if (composite.getTableViewer().getTable().getVerticalBar().isVisible()) {
					ScrollBar verticalScroll = composite
					.getTableViewer().getTable()
					.getHorizontalBar();					
					verticalScroll.setMaximum(1);
				}
			} catch (SWTException e) {
				e.printStackTrace();
			}
		} else {
			composite.getLblTotalPageCount().setText(/*pageCountString + " " + */0 + "/" + 0);
		}
		HideAndShowPageButtons();
	}


	/**
	 * Method to adjust the table size based on the screen resolution
	 */
	public void adjustTableSizeBasedOnResolution() {

		int screenHeight =Display.getCurrent().getActiveShell().getBounds().width;
		int screenWidth =Display.getCurrent().getActiveShell().getBounds().height;

		if ((screenHeight == 800 || screenHeight == 802) && (screenWidth == 600 || screenWidth == 602)) {
			numOfRecordsPerPage = 5;
		} 
		else if ((screenHeight == 1024 || screenHeight == 1026) && (screenWidth == 768 || screenWidth == 770)) {
			numOfRecordsPerPage = 7;
		}
		else if ((screenHeight == 1152 || screenHeight == 1154) && (screenWidth == 864 || screenWidth == 866)) {
			numOfRecordsPerPage = 9;
		} 
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 768|| screenWidth == 770)) {
			numOfRecordsPerPage = 7;
		}  
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 800 || screenWidth == 802)) {
			numOfRecordsPerPage = 8;
		}  
		else if ((screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 1024 || screenWidth == 1026)) {
			numOfRecordsPerPage = 15;
		} else {
			numOfRecordsPerPage = 7;//Default
		}		
	}
	
	private void enableLoadSelectedBatch(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblLoadSelectedBatch().setEnabled(true);
		batchSummaryComposite.getILblLoadSelectedBatch().setImage(enableImage);
		batchSummaryComposite.getILblLoadSelectedBatch().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableLoadSelectedBatch(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblLoadSelectedBatch().setEnabled(false);
		batchSummaryComposite.getILblLoadSelectedBatch().setImage(disableImage);
		batchSummaryComposite.getILblLoadSelectedBatch().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableDeleteSelectedBatch(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblDeleteSelectedBatch().setEnabled(true);
		batchSummaryComposite.getILblDeleteSelectedBatch().setImage(enableImage);
		batchSummaryComposite.getILblDeleteSelectedBatch().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableDeleteSelectedBatch(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblDeleteSelectedBatch().setEnabled(false);
		batchSummaryComposite.getILblDeleteSelectedBatch().setImage(disableImage);
		batchSummaryComposite.getILblDeleteSelectedBatch().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableChangeEmployeeOrKiosk(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().setEnabled(true);
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().setImage(enableImage);
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableChangeEmployeeOrKiosk(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().setEnabled(false);
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().setImage(disableImage);
		batchSummaryComposite.getILblChangeEmployeeOrKiosk().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enablePrintBatchDetails(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblPrintBatchDetails().setEnabled(true);
		batchSummaryComposite.getILblPrintBatchDetails().setImage(enableImage);
		batchSummaryComposite.getILblPrintBatchDetails().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disablePrintBatchDetails(BatchSummaryComposite batchSummaryComposite) {
		batchSummaryComposite.getILblPrintBatchDetails().setEnabled(false);
		batchSummaryComposite.getILblPrintBatchDetails().setImage(disableImage);
		batchSummaryComposite.getILblPrintBatchDetails().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}

	private void createImages() {
		disableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
	}


}
