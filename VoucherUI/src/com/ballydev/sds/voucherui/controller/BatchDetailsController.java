/*****************************************************************************
 * $Id: BatchDetailsController.java,v 1.67, 2010-06-16 14:57:33Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:57:33 AM$
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
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

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSTableViewer;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.service.FrameworkServiceLocator;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.BaseDTO;
import com.ballydev.sds.voucher.dto.BatchInfoDTO;
import com.ballydev.sds.voucher.dto.QuestionableReportInfoDTO;
import com.ballydev.sds.voucher.dto.ReclBatchDetailsInfoDTO;
import com.ballydev.sds.voucher.dto.ReconciliationBatchInfoDTO;
import com.ballydev.sds.voucher.dto.ReconciliationInfoDTO;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.AmountTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.BatchDetailsComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.composite.VoucherTSInputDialog;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.LabelKeyConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.BatchDetailsForm;
import com.ballydev.sds.voucherui.form.BatchReconciliationForm;
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.BatchDetailsPrinter;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUIValidator;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class BatchDetailsController extends SDSBaseController {

	private static final Logger log = VoucherUILogger.getLogger(LabelKeyConstants.MODULE_NAME);

	private static BatchDetailsForm form;

	private boolean isCallFrmReconciliationPg = false;

	private BatchDetailsComposite composite;

	/**
	 * The maximum number of records allowed to display in each page as there is
	 * no scrollbar in a Touch Screen UI.
	 */
	private int numOfRecordsPerPage = 5;

	/**
	 * This field returns ths total number of reconciled vouchers in the database.
	 */
	private int totalRecordCount = 0;

	/**
	 * the mod of totalRecordCount by numOfRecordsPerPage
	 */
	private int modValue = 0;

	/**
	 * Total number of pages the records run into.
	 */
	private int numOfPages = 0; 

	/**
	 * This field is used in selecting the next page for batch details records. This
	 * variable holds the index of the first record in each page - 1.
	 */
	private int initialIndex = 0;

	private ArrayList<ReclBatchDetailsInfoDTO> dispBatchDetailsDTO = new ArrayList<ReclBatchDetailsInfoDTO>();

	private List<ReclBatchDetailsInfoDTO> responseDTOList = null;

	private List<ReclBatchDetailsInfoDTO> responseDTOChkList = null;

	private List<ReclBatchDetailsInfoDTO> questionableResponseDTOChkList = null;


	private BatchReconciliationForm batchReclForm;

	private Composite parent;

	private double voucherAmount = 0;

	private int voucherCount = 0;

	private double couponAmount = 0;

	private int couponCnt = 0;

	private boolean isBatchDelete = false;

	private final int length = 15;

	private final int barCodeLength = 18;

	private final int bufferLength = 2;

	private final int reasonLength = 30;

	private SDSTSTableViewer tableViewer;

	private Date startTime;

	private Date endTime;

	private boolean isQuestionable = false;

	private String casinoName;

	private Image enableImage;
	
	private Image disableImage;
	
	public BatchDetailsController(Composite parent, int style,
			BatchDetailsForm dtlsForm, SDSValidator validator, BatchReconciliationForm batchForm, boolean isCallFrmReconciliationPg) throws Exception  {

		super(dtlsForm, validator);
		form = dtlsForm;
		this.batchReclForm = batchForm;
		this.isCallFrmReconciliationPg = isCallFrmReconciliationPg;
		this.parent = parent;
		createBatchDetailsComposite(parent, style);
		tableViewer = composite.getDispBatchDetailsTableViewer();
		VoucherMiddleComposite.setCurrentComposite(composite);	
		composite.getLblEmployee().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EMPLOYEE_ID));
		composite.getILblChangeEmployee().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.CHANGE_LOCATION));
		
		enableImage = composite.getButtonImage();
		disableImage = composite.getButtonDisableImage();
		
		if( VoucherUtil.getReconLevel() == 1 ) {			
			composite.getILblChangeEmployee().setEnabled(false);
			composite.getILblChangeEmployee().setImage(disableImage);
			composite.getILblChangeEmployee().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		}
		try {
			voucherAmount = ConversionUtil.dollarToCents(form.getBatchVoucherAmountValue());
			voucherCount = Integer.parseInt(form.getBatchVoucherCountValue());
			couponAmount = ConversionUtil.dollarToCents(form.getBatchCouponAmountValue());
			couponCnt = Integer.parseInt(form.getBatchCouponCountValue());			
		} catch (Exception e) {
			e.printStackTrace();
		}

		form.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat( form.getBatchVoucherAmountValue()));		
		form.setBatchCouponAmountValue(ConversionUtil.voucherAmountFormat( form.getBatchCouponAmountValue()));
		adjustTableSizeBasedOnResolution();
		processBatchDetailsFromDB();
		if( isCallFrmReconciliationPg ) {
			filterQuestionables();
			composite.getILblGetQuestionables().setEnabled(false);
			composite.getILblGetQuestionables().setImage(disableImage);
			composite.getILblGetQuestionables().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
			isQuestionable = true;
		}
		loadBatchDetailsFromResponse();
		parent.layout();
		super.registerEvents(composite);
		registerCustomizedListeners(composite);
		form.addPropertyChangeListener(this);
		populateScreen(composite);
		if( KeyBoardUtil.getKeyBoardShell() != null ) {
			KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
		}
		composite.getILblDeleteTkt().setEnabled(false);
		composite.getILblDeleteTkt().setImage(disableImage);
		composite.getILblDeleteTkt().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		composite.getILblModifyTkt().setEnabled(false);
		composite.getILblModifyTkt().setImage(disableImage);
		composite.getILblModifyTkt().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}

	private void processBatchDetailsFromDB() {
		updateTicketForQuestionableTkt();
		responseDTOList = new ArrayList<ReclBatchDetailsInfoDTO> ();
		responseDTOChkList = new ArrayList<ReclBatchDetailsInfoDTO>();
		questionableResponseDTOChkList = new ArrayList<ReclBatchDetailsInfoDTO>();
		ReconciliationInfoDTO reclForm = new ReconciliationInfoDTO();
		if( form.getBatchNbrValue() != null && form.getBatchNbrValue().trim().length() > 0 ) {
			reclForm.setReclId(new Long(form.getBatchNbrValue()));
		} else {
			reclForm.setReclId(null);
		}
		reclForm.setReclAssetLocn(PreferencesUtil.getClientAssetNumber());
		reclForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
		try	{			
			ReconciliationBatchInfoDTO reconciliationBatchForm = ServiceCall.getInstance().getVouchersForBatch(reclForm);
			startTime= reconciliationBatchForm.getStartTime();
			endTime = reconciliationBatchForm.getEndTime();
			ReclBatchDetailsInfoDTO[] batchDetailsInfoDTOs = reconciliationBatchForm.getReclBatchDetailsInfo();
			for( ReclBatchDetailsInfoDTO detailsInfoDTO: batchDetailsInfoDTOs ) {
				ReclBatchDetailsInfoDTO infoDTO = (ReclBatchDetailsInfoDTO)detailsInfoDTO;
				responseDTOList.add(infoDTO);
				responseDTOChkList.add(infoDTO);
			}			
			if( reconciliationBatchForm  != null && reconciliationBatchForm.isErrorPresent() ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(reconciliationBatchForm));
			}

			form.setBatchVoucherCountValue(Integer.toString(voucherCount));
			form.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(voucherAmount)));
			form.setBatchCouponCountValue(Integer.toString(couponCnt));
			form.setBatchCouponAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(couponAmount)));

		} catch(VoucherEngineServiceException ex) {
			log.error("Error occurred in BatchDetailsController.fetchBatchDetailsFromDB", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
		catch (Exception ex ) {			
			log.error("Error occurred in BatchDetailsController.fetchBatchDetailsFromDB", ex);
		}
	}

	private void updateTicketForQuestionableTkt() {
		try {
			BatchInfoDTO batchForm = new BatchInfoDTO();
			Date startTime = batchReclForm.getStartTime();
			Calendar startTimeCal = Calendar.getInstance();
			startTimeCal.setTime(startTime);
			startTimeCal.add(Calendar.HOUR, -2);
			batchForm.setReconciliationStartTime(startTimeCal.getTime());
			Date endTime  = batchReclForm.getEndTime();
			Calendar cal = Calendar.getInstance();
			cal.setTime(endTime);
			cal.add(Calendar.HOUR, 2);
			batchForm.setReconciliationEndTime(cal.getTime());
			batchForm.setBatchId(batchReclForm.getBatchId());			
			batchForm.setEmployeeId(batchReclForm.getEmployeeAssetId());
			batchForm.setReconciliationType(IVoucherConstants.CASHIER);			
			batchForm.setReconTypeId(batchReclForm.isCashier()?1:2);			
			batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));			
			QuestionableReportInfoDTO questionableReportForm =  ServiceCall.getInstance().updateQuestionableVoucher(batchForm);
			if( questionableReportForm != null && questionableReportForm.isErrorPresent())	{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(questionableReportForm));
			}

		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while updating the questionable Vouchers", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
	}

	/**
	 * Loads the details into the table viewer from dataQueries the DB and l 
	 *
	 */
	private void loadBatchDetailsFromResponse() {
		totalRecordCount = 0;
		modValue = 0;
		numOfPages = 0;
		tableViewer.setCurrentPage(0);
		initialIndex = 0;

		composite.getDispBatchDetailsTableViewer().setFullModelList(responseDTOList);
		composite.getDispBatchDetailsTableViewer().setNumOfRecordsPerPage(numOfRecordsPerPage);
		composite.getDispBatchDetailsTableViewer().sortFullModel(form, composite.getTimeStampComparator());

		dispBatchDetailsDTO.clear();
		form.setDispBatchDetailsDTO(dispBatchDetailsDTO);

		if( responseDTOList != null )
			totalRecordCount = responseDTOList.size();

		if( totalRecordCount > 0 ) {
			numOfPages=totalRecordCount/numOfRecordsPerPage;
			modValue = totalRecordCount % numOfRecordsPerPage;
			if( modValue > 0 ) {
				numOfPages += 1;
			}
			if( totalRecordCount < numOfRecordsPerPage ) {
				numOfRecordsPerPage = totalRecordCount;
			}
			for( int i = initialIndex; i < initialIndex + numOfRecordsPerPage; i++ ) {
				ReclBatchDetailsInfoDTO reclDtlForm = (ReclBatchDetailsInfoDTO) responseDTOList.get(i);
				dispBatchDetailsDTO.add(i, reclDtlForm);
			}
			tableViewer.setCurrentPage(tableViewer.getCurrentPage() + 1);
			initialIndex += numOfRecordsPerPage;			

			//pageCountString = LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
			composite.getLblTotalPageCount().setText(/*pageCountString + " " + */tableViewer.getCurrentPage() + "/" + numOfPages);
			composite.layout();


			try {
				if( composite.getDispBatchDetailsTableViewer().getTable().getHorizontalBar().isVisible() ) {
					ScrollBar horizondalScroll = composite.getDispBatchDetailsTableViewer().getTable().getHorizontalBar();					
					horizondalScroll.setThumb(1);

				}
				if( composite.getDispBatchDetailsTableViewer().getTable().getVerticalBar().isVisible() ) {
					ScrollBar verticalScroll = composite.getDispBatchDetailsTableViewer().getTable().getVerticalBar();
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
	 * This method is to create BatchDetailsComposite
	 * 
	 * @param p Composite
	 * @param s Style
	 */
	public void createBatchDetailsComposite(Composite p, int s) {
		composite = new BatchDetailsComposite(p, s);
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		composite.getDispBatchDetailsTableViewer().getTable().addFocusListener(this);
		addTSTableColumnListener(composite.getDispBatchDetailsTableViewer());
		
		BatchDetailsComposite batchDetailsComposite = ((BatchDetailsComposite)argComposite);

		batchDetailsComposite.getILblReloadBatchDtls().addMouseListener(this);
		batchDetailsComposite.getILblReloadBatchDtls().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblGetQuestionables().addMouseListener(this);
		batchDetailsComposite.getILblGetQuestionables().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblPrintBatchDetails().addMouseListener(this);
		batchDetailsComposite.getILblPrintBatchDetails().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblChangeEmployee().addMouseListener(this);
		batchDetailsComposite.getILblChangeEmployee().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblCancel().addMouseListener(this);
		batchDetailsComposite.getILblCancel().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblAddTkt().addMouseListener(this);
		batchDetailsComposite.getILblAddTkt().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblModifyTkt().addMouseListener(this);
		batchDetailsComposite.getILblModifyTkt().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblDeleteTkt().addMouseListener(this);
		batchDetailsComposite.getILblDeleteTkt().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblDeleteBatch().addMouseListener(this);
		batchDetailsComposite.getILblDeleteBatch().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblNextRecords().addMouseListener(this);
		batchDetailsComposite.getILblNextRecords().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblLastPageRecords().addMouseListener(this);
		batchDetailsComposite.getILblLastPageRecords().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblPreviousRecords().addMouseListener(this);
		batchDetailsComposite.getILblPreviousRecords().getTextLabel().addTraverseListener(this);

		batchDetailsComposite.getILblFirstPageRecords().addMouseListener(this);
		batchDetailsComposite.getILblFirstPageRecords().getTextLabel().addTraverseListener(this);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		try {
			Control control = (Control) e.getSource();

			if(((Control) e.getSource() instanceof SDSImageLabel) ) {


				BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
				/** User clicked next arrow to get next page of records **/
				if( e.getSource().equals(batchDetailsComposite.getILblNextRecords()) ) {
					showNextPage();
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);		
				}

				/** User clicked previous arrow to get previous page of records **/
				else if( e.getSource().equals(batchDetailsComposite.getILblPreviousRecords()) ) {
					showPreviousPage();
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);		
				}

				/** User clicked first page arrow to get previous page of records **/
				else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW) ) {
					if( totalRecordCount > numOfRecordsPerPage ) {
						initialIndex = (numOfRecordsPerPage*2) < totalRecordCount ? (numOfRecordsPerPage*2) : totalRecordCount; 
						showPreviousPage();
						tableViewer.setCurrentPage(1);
						composite.getLblTotalPageCount().setText(
								/*pageCountString +*/ tableViewer.getCurrentPage() + "/" + numOfPages);
						composite.layout();
						HideAndShowPageButtons();
						disableDeleteTkt(batchDetailsComposite);
						disableModifyTkt(batchDetailsComposite);		
					}
				}

				/** User clicked last page arrow to get previous page of records **/
				else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_LAST_ARROW) ) {
					if( totalRecordCount > numOfRecordsPerPage ) {
						if( modValue > 0) {
							initialIndex = totalRecordCount - modValue;						
						} else if(modValue == 0 ) {
							initialIndex = totalRecordCount - numOfRecordsPerPage;
						}
						showNextPage();
						tableViewer.setCurrentPage(numOfPages);
						composite.getLblTotalPageCount().setText(
								/*pageCountString +*/ tableViewer.getCurrentPage() + "/" + numOfPages);
						composite.layout();
						HideAndShowPageButtons();
						disableDeleteTkt(batchDetailsComposite);
						disableModifyTkt(batchDetailsComposite);		
					}
				}

				/** User clicked reload details **/
				else if( e.getSource().equals(batchDetailsComposite.getILblReloadBatchDtls()) ) {
					enableGetQuestionables(batchDetailsComposite);
					isQuestionable = false;
					adjustTableSizeBasedOnResolution();
					processBatchDetailsFromDB();
					loadBatchDetailsFromResponse();
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);		
					populateScreen(composite);
				}

				/** User clicked get questionables **/
				else if( e.getSource().equals(batchDetailsComposite.getILblGetQuestionables()) ) {
					filterQuestionables();
					loadBatchDetailsFromResponse();
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);
					disableGetQuestionables(batchDetailsComposite);
					isQuestionable = true;
					populateScreen(composite);
				}

				/** User clicked print batch **/
				else if( e.getSource().equals(batchDetailsComposite.getILblPrintBatchDetails()) ) {
					if( form.getDispBatchDetailsDTO().size() == 0) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA_TO_PRINT));
						return;
					}
					printDetails();					
				}

				else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_DELETE_TKT_BATCH) ) {
					populateForm(composite);
					if( form.getDispBatchDetailsDTOSelectedValue() == null || form.getDispBatchDetailsDTOSelectedValue().getReclVouBrcd() == null ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TKT_TO_DELETE,new String[] {AppContextValues.getInstance().getTicketText()}));
						return;
					}
					String barCodeSelected = form.getDispBatchDetailsDTOSelectedValue().getReclVouBrcd();		

					try	{
						ReclBatchDetailsInfoDTO batchDetailsInfoDTO = getReclBatchDetailsInfoDTO(barCodeSelected);	
						if( batchDetailsInfoDTO != null && batchDetailsInfoDTO.getAuditCodeDesc().equalsIgnoreCase(IVoucherConstants.MISSING_FROM_BATCH) ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MISSING_CANNOT_DELETE,new String[] {AppContextValues.getInstance().getTicketText()}));
							return;
						}
						if( MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_DELETE_CONFIRMATION,new String[]{LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_DELTE_CONFIRM)}), getComposite()) == true ) {
							long batchId = Long.parseLong(form.getBatchNbrValue());
							BatchInfoDTO batchForm = new BatchInfoDTO();
							batchForm.setBatchId(batchId);
							batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
							TicketInfoDTO ticketInfoDTO =  ServiceCall.getInstance().deleteTicketFromBatch(batchForm,barCodeSelected);
							if( ticketInfoDTO != null ) {
								if( ticketInfoDTO.isErrorPresent() ) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
								}
								else {																			
									if( (ticketInfoDTO.getAmountType().getAmountType() == 1)||ticketInfoDTO.getAmountType().getAmountType() == 2 ) {
										couponCnt = couponCnt-1;
										couponAmount = couponAmount-ticketInfoDTO.getAmount();
									} else if( ticketInfoDTO.getAmountType().getAmountType() == 0 ) {
										voucherCount = voucherCount-1;
										voucherAmount = voucherAmount-ticketInfoDTO.getAmount();
									}

									Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_SUMMARY_DETAIL), LabelLoader.getLabelValue(IDBLabelKeyConstants.DELETE_TKT_IN_A_BATCH)+AppContextValues.getInstance().getTicketText(), "", VoucherUtil.formatBarcode(barCodeSelected, (ticketInfoDTO.getTicketType() == null ? null : ticketInfoDTO.getTicketType().getTicketType()), ticketInfoDTO.getTktStatusId()), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_TKT_DELETED,new String[] {AppContextValues.getInstance().getTicketText()}) +form.getBatchNbrValue() , EnumOperation.DELETE_OPERATION,PreferencesUtil.getClientAssetNumber());
									adjustTableSizeBasedOnResolution();									
									processBatchDetailsFromDB();									
									loadBatchDetailsFromResponse();									
									populateScreen(composite);						
									form.setDispBatchDetailsDTOSelectedValue(null);
									disableDeleteTkt(batchDetailsComposite);
									disableModifyTkt(batchDetailsComposite);									
									enableGetQuestionables(batchDetailsComposite);
									isQuestionable = false;
									MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
								}
							}
						}
					}catch(VoucherEngineServiceException ex) {
						log.error("Exception while deleting the Voucher from the batch", ex);						
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
					}	


				} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_MODIY_TKT) ) {
					populateForm(composite);
					if( form.getDispBatchDetailsDTOSelectedValue() == null || form.getDispBatchDetailsDTOSelectedValue().getReclVouBrcd() == null ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_TKT_TO_MODIFY, new String[] {AppContextValues.getInstance().getTicketText()}));
						return;
					}
					String barCodeSelected = form.getDispBatchDetailsDTOSelectedValue().getReclVouBrcd();					
					ReclBatchDetailsInfoDTO batchDetailsInfoDTO = getReclBatchDetailsInfoDTO(barCodeSelected);
					if( batchDetailsInfoDTO != null && batchDetailsInfoDTO.getAuditCodeDesc().equalsIgnoreCase(IVoucherConstants.MISSING_FROM_BATCH) ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MISSING_CANNOT_DELETE,new String[] {AppContextValues.getInstance().getTicketText()}));
						return;
					}
					if( KeyBoardUtil.getKeyBoardShell() != null ) {
						KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
					}					
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
					dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
					dialog.setLocation(170,250);
					String retVal = dialog.open(18,true);
					if( retVal == null || retVal.length() > 0 ) {
						modifyTicket(retVal);
					}
				}
				else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_ADD_TKT_BATCH) ) {
					if( KeyBoardUtil.getKeyBoardShell() != null ) {
						KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
					}
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
					dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
					dialog.setLocation(170,250);				
					String retVal = dialog.open(18,true);	
					addTicket(retVal);
				}

				/** User clicked delete batch **/
				else if( e.getSource().equals(batchDetailsComposite.getILblDeleteBatch()) ) {
					try	{
						if( MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_DELETE_CONFIRMATION), getComposite()) == true ) {
							long batchId = Long.parseLong(form.getBatchNbrValue());

							BatchInfoDTO batchForm = new BatchInfoDTO();
							batchForm.setBatchId(batchId);
							batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
							BaseDTO baseForm =  ServiceCall.getInstance().cancelBatch(batchForm);
							if( baseForm != null ) {
								if( baseForm.isErrorPresent() ) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
								}
								else {
									Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_SUMMARY_DETAIL), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), "",form.getBatchNbrValue() , LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_DELETED), EnumOperation.DELETE_OPERATION,PreferencesUtil.getClientAssetNumber());
									batchDetailsComposite.getDispBatchDetailsTableViewer().getTable().clearAll();
									
									disableReloadBatchDtls(batchDetailsComposite);
									disableChangeEmployee(batchDetailsComposite);
									disableDeleteBatch(batchDetailsComposite);
									disableGetQuestionables(batchDetailsComposite);
									isQuestionable = true;
									disablePrintBatchDetails(batchDetailsComposite);
									disableDeleteTkt(batchDetailsComposite);
									disableModifyTkt(batchDetailsComposite);
									disableAddTkt(batchDetailsComposite);
									form.setBatchNbrValue("");
									form.setBatchEmployeeValue("");
									form.setBatchCouponAmountValue("0");
									form.setBatchCouponCountValue("0.00");
									form.setBatchVoucherAmountValue("0");
									form.setBatchVoucherCountValue("0.00");
									form.setDispBatchDetailsDTO(null);

									populateScreen(composite);
									for( int j = 0; j < batchReclForm.getBatchSummaries().size(); j++ ) {
										if( batchReclForm.getBatchSummaries().get(j).getBatchNumber() == batchReclForm.getBatchId() ) {
											batchReclForm.getBatchSummaries().remove(j);
										}
									}
									isBatchDelete = true;
									batchReclForm.setBatchId(null);
									batchReclForm.setEmployeeAssetId("");
									//batchReclForm.getBatchSummaries().clear();
									MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
									composite.getLblTotalPageCount().setText(0 + "/" + 0);
								}
							}
						}
					} catch(VoucherEngineServiceException ex) {
						log.error("Exception while deleting the batch", ex);						
						VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
					} catch (Exception ex) {
						log.error("Error occurred in BatchDetailsController.widgetSelected", ex);
					}
				}

				/** User clicked change employee to change the employee for the current batch **/
				else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.RECON_CTRL_BTN_CHANGE_LOCATION) ) {
					if( KeyBoardUtil.getKeyBoardShell() != null ) {
						KeyBoardUtil.getKeyBoardShell().getKeyboardShell().dispose();
					}
					VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
					dialog.setLocation(170,250);
					String retVal =null;
					dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
					retVal = dialog.open(5,true);
					changeCashier(retVal);
				}

				/** User clicked cancel **/
				else if( e.getSource().equals(batchDetailsComposite.getILblCancel()) ) {
					if( !isBatchDelete ) {
						//TEMP FIX FOR POPULATING THE FORM. TO BE CHANGED LATER					
						processBatchDetailsFromDB();

						if( batchReclForm.getBatchSummaries() != null && batchReclForm.getBatchSummaries().size() > 0 ) {
							/**SET THE BATCH SUMMARY DTO WITH THE NEW SET OF VALUES THAT ARE MODIFIED IN THE
							 * BATCH DETAILS SCREEN. THIS WILL DISPLAY THE NEW VALUE IN THE BATCH SUMMARY SCREEN
							 *  WHEN THE CANCEL BUTTON IS CLICKED**/
							BatchSummaryDTO batchSummaryDTO = null;
							for( int j = 0; j < batchReclForm.getBatchSummaries().size(); j++ ) {
								if( batchReclForm.getBatchSummaries().get(j).getBatchNumber() == batchReclForm.getBatchId() ) {
									batchSummaryDTO = batchReclForm.getBatchSummaries().get(j);
									batchSummaryDTO.setCashierOrKiosk(batchReclForm.getEmployeeAssetId());
									batchSummaryDTO.setEmpId(batchReclForm.getEmployeeAssetId());
									batchSummaryDTO.setCouponAmount(Double.valueOf(form.getBatchCouponAmountValue()));
									batchSummaryDTO.setCouponCount(Long.valueOf(form.getBatchCouponCountValue()));
									batchSummaryDTO.setVoucherAmount(Double.valueOf(form.getBatchVoucherAmountValue()));
									batchSummaryDTO.setVoucherCount(Long.valueOf(form.getBatchVoucherCountValue()));
								}
							}
						}
					}
					if( isCallFrmReconciliationPg ) {
						composite.dispose();
						ReconciliationController ctrl = new ReconciliationController(parent,SWT.NONE, batchReclForm, new SDSValidator(getClass(),true));
						ctrl.populateScreen(ctrl.getComposite());
					} else {
						Composite parent = getComposite().getParent();
						composite.dispose();
						BatchSummaryController ctrl = new BatchSummaryController(parent,SWT.NONE, batchReclForm, new SDSValidator(getClass(),true),false);
						ctrl.populateScreen(ctrl.getComposite());
					}
				}
			} else if( e.getSource() instanceof Table ) {
				
				Table tableCtrl = (Table)((Control) e.getSource());	
				BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
				
				if( tableCtrl.getData("name").equals(IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL) ) {					
					enableDeleteTkt(batchDetailsComposite);
					enableModifyTkt(batchDetailsComposite);					
				}
			}
		} catch(Exception ex) {
			log.error("Error occurred in BatchDetailsController.widgetSelected", ex);
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
				
				BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
				
				if( tableCtrl.getData("name").equals(IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL) ) {
					
					enableDeleteTkt(batchDetailsComposite);
					enableModifyTkt(batchDetailsComposite);
				}
			}
		} catch (Exception ex) {
			log.error("Error occurred in BatchSummaryController.eventOnBatchTableDetail", ex);
			ex.printStackTrace();
		}
	}

	private void showPreviousPage() throws Exception {
		if( tableViewer.getCurrentPage() == 1 ) {
			return;
		}

		tableViewer.setCurrentPage(tableViewer.getCurrentPage() - 1);
		composite.getLblTotalPageCount().setText(
				/*pageCountString +*/ tableViewer.getCurrentPage() + "/" + numOfPages);
		composite.layout();
		composite.getDispBatchDetailsTableViewer().getTable().setSelection(-1);

		if( initialIndex > totalRecordCount - modValue ) {
			dispBatchDetailsDTO.clear();
			int count = initialIndex - numOfRecordsPerPage - modValue;
			
			for ( int i = count, j = 0; i < count + numOfRecordsPerPage; i++, j++ ) {

				ReclBatchDetailsInfoDTO batchDtlDTO = (ReclBatchDetailsInfoDTO) responseDTOList.get(i);
				dispBatchDetailsDTO.add(j, batchDtlDTO);
			}
			populateScreen(composite);
			initialIndex -= modValue;		

		} else if( initialIndex > numOfRecordsPerPage && initialIndex <= (totalRecordCount - modValue) ) {
			initialIndex -= numOfRecordsPerPage;
			dispBatchDetailsDTO.clear();
			int count = initialIndex - numOfRecordsPerPage;
			for( int i = count, j = 0; i < count + numOfRecordsPerPage; i++, j++ ) {
				ReclBatchDetailsInfoDTO batchDtlDTO = (ReclBatchDetailsInfoDTO) responseDTOList.get(i);
				dispBatchDetailsDTO.add(j, batchDtlDTO);
			}
			populateScreen(composite);
		} 
		HideAndShowPageButtons();
	}

	private void showNextPage() throws Exception{
		if( tableViewer.getCurrentPage() == numOfPages ) {
			return;
		}
		tableViewer.setCurrentPage(tableViewer.getCurrentPage() + 1);
		composite.getLblTotalPageCount().setText(/*pageCountString+*/tableViewer.getCurrentPage()+"/"+numOfPages);
		composite.layout();
		composite.getDispBatchDetailsTableViewer().getTable().setSelection(-1);

		if( initialIndex < totalRecordCount - modValue ) {
			dispBatchDetailsDTO.clear();
			for( int i = initialIndex, j = 0; i < initialIndex + numOfRecordsPerPage; i++, j++ ) {
				ReclBatchDetailsInfoDTO batchDtlDTO = (ReclBatchDetailsInfoDTO) responseDTOList.get(i);
				dispBatchDetailsDTO.add(j, batchDtlDTO);
			}
			populateScreen(composite);
			initialIndex += numOfRecordsPerPage;
		} else if( initialIndex < totalRecordCount ) {
			dispBatchDetailsDTO.clear();
			for( int i = initialIndex, j = 0; i < initialIndex + modValue; i++, j++ ) {
				ReclBatchDetailsInfoDTO batchDtlDTO = (ReclBatchDetailsInfoDTO) responseDTOList.get(i);
				dispBatchDetailsDTO.add(j, batchDtlDTO);
			}
			populateScreen(composite);
			initialIndex += modValue;

		}	

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
	 * Filters the original response to only retain the questionable vouchers.
	 *
	 */
	private void filterQuestionables() {
		try {
			ReclBatchDetailsInfoDTO reclBatchDetailsInfoDTO;		
			long voucherAmt  = 0;
			int voucherCnt   = 0;
			long couponAmt   = 0;
			int couponCount  = 0;
			boolean questionablePresent = false;
			for( int i = 0; i < responseDTOList.size();)
			{
				reclBatchDetailsInfoDTO = responseDTOList.get(i);
				if( reclBatchDetailsInfoDTO.getAuditId() == -1 || reclBatchDetailsInfoDTO.getAuditId() == 0 ) {
					questionablePresent = true;
					responseDTOList.remove(reclBatchDetailsInfoDTO);

					if( reclBatchDetailsInfoDTO.getAuditId() == 0 && (reclBatchDetailsInfoDTO.getAmtType() == 0) ) {
						voucherAmt = voucherAmt+reclBatchDetailsInfoDTO.getTktAmount();
						voucherCnt = voucherCnt+1;					
					}
					else if( reclBatchDetailsInfoDTO.getAuditId() == 0 && ((reclBatchDetailsInfoDTO.getAmtType() == 1) || reclBatchDetailsInfoDTO.getAmtType() == 2) ) {
						couponAmt = couponAmt+reclBatchDetailsInfoDTO.getTktAmount();
						couponCount = couponCount+1;
					}

				}				
				else {
					i++;
				}
				if( questionablePresent ) {
					form.setBatchVoucherCountValue(Integer.toString(voucherCount-voucherCnt));
					form.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(voucherAmount-voucherAmt)));
					form.setBatchCouponCountValue(Integer.toString(couponCnt-couponCount));
					form.setBatchCouponAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(couponAmount- couponAmt)));
				} else {
					form.setBatchVoucherCountValue(Integer.toString(voucherCount));
					form.setBatchVoucherAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(voucherAmount)));
					form.setBatchCouponCountValue(Integer.toString(couponCnt));
					form.setBatchCouponAmountValue(ConversionUtil.voucherAmountFormat(ConversionUtil.centsToDollar(couponAmount)));

				}
				questionableResponseDTOChkList = responseDTOList;
				populateScreen(composite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to adjust the table size based on the screen resolution
	 */
	public void adjustTableSizeBasedOnResolution() {

		int screenHeight =Display.getCurrent().getActiveShell().getBounds().width;
		int screenWidth =Display.getCurrent().getActiveShell().getBounds().height;

		if( (screenHeight == 800 || screenHeight == 802) && (screenWidth == 600 || screenWidth == 602) ) {
			numOfRecordsPerPage = 3;
		} 
		else if( (screenHeight == 1024 || screenHeight == 1026) && (screenWidth == 768 || screenWidth == 770) ) {
			numOfRecordsPerPage = 5;
		}
		else if( (screenHeight == 1152 || screenHeight == 1154) && (screenWidth == 864 || screenWidth == 866) ) {
			numOfRecordsPerPage = 8;
		} 
		else if( (screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 768|| screenWidth == 770) ) {
			numOfRecordsPerPage = 5;
		}  
		else if( (screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 800 || screenWidth == 802) ) {
			numOfRecordsPerPage = 6;
		}  
		else if( (screenHeight == 1280 || screenHeight == 1282) && (screenWidth == 1024 || screenWidth == 1026) ) {
			numOfRecordsPerPage = 13;
		} else {
			numOfRecordsPerPage = 5;//Default
		}		

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		super.focusGained(e);
		composite.redraw();
	}*/

	/**
	 * This method checks for the dupliate barcode
	 * @param listReclBatchDetailsForm
	 * @param enteredBarcode
	 * @return
	 */
	public boolean checkDuplicateBarcode(List<ReclBatchDetailsInfoDTO > listReclBatchDetailsForm, String enteredBarcode ) {
		/** true- barcode already exists in table
		 * false- barcode does not exixts in the table.proceed adding to the table.
		 */		
		boolean barcodeExists = true;
		List<String> barcodeListGot = new ArrayList<String>();
		
		if( listReclBatchDetailsForm != null && listReclBatchDetailsForm.size() != 0 ) {
			if( enteredBarcode != null && !((enteredBarcode.trim()+" ").equalsIgnoreCase(" ")) ) {
				for( int k = 0; k < listReclBatchDetailsForm.size(); k++ ) {
					ReclBatchDetailsInfoDTO tempReclBatchDetailsForm=listReclBatchDetailsForm.get(k);					
					if( tempReclBatchDetailsForm != null && tempReclBatchDetailsForm.getReclVouBrcd() != null ) {
						barcodeListGot.add(tempReclBatchDetailsForm.getReclVouBrcd());			
					}
				}

				if( barcodeListGot != null && barcodeListGot.size() != 0 ) {
					barcodeExists=barcodeListGot.contains(enteredBarcode);
					if( barcodeExists ) {
						barcodeExists = true;						
						return barcodeExists;
					} else {
						barcodeExists = false;						
						return barcodeExists;
					}
				}
			}
		} else {
			barcodeExists = false;
			return barcodeExists;
		}		
		return barcodeExists;
	}

	private StringBuilder printHeader(StringBuilder buf) {
		buf.append(IVoucherConstants.LINE_SEPERATOR);
		buf.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_LOCATION), length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_BARCODE), barCodeLength));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE), reasonLength));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));	
		buf.append(VoucherStringUtil.lPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")", length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.REASON), length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));		
		buf.append(IVoucherConstants.LINE_SEPERATOR);		
		buf.append(IVoucherConstants.LINE_SEPERATOR);
		buf.append(VoucherStringUtil.nCopy('-', length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.nCopy('-', barCodeLength));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.nCopy('-', reasonLength));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));
		buf.append(VoucherStringUtil.nCopy('-', length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));		
		buf.append(VoucherStringUtil.nCopy('-', length));
		buf.append(VoucherStringUtil.rPad(" ", bufferLength));		
		buf.append(IVoucherConstants.LINE_SEPERATOR);
		return buf;
	}


	public void getCasinoDetails(){
		SiteDTO  siteDTO = SDSApplication.getSiteDetails();
		if( siteDTO != null ) {
			casinoName = siteDTO.getLongName();			
		} else {
			casinoName = LabelLoader.getLabelValue(IDBLabelKeyConstants.CASINO_NAME);			
		}

	}

	/**
	 * This method gets the response dto list and prints the
	 * details as a full Table
	 */
	public void printDetails() {

		List<ReclBatchDetailsInfoDTO> cashableBatchDTO    = new ArrayList<ReclBatchDetailsInfoDTO>();
		List<ReclBatchDetailsInfoDTO> nonCashableBatchDTO = new ArrayList<ReclBatchDetailsInfoDTO>();
		List<ReclBatchDetailsInfoDTO> promotionalBatchDTO = new ArrayList<ReclBatchDetailsInfoDTO>();
		List<ReclBatchDetailsInfoDTO> nonValidBatchDTO    = new ArrayList<ReclBatchDetailsInfoDTO>();		

		if( isQuestionable ) {
			for( int i = 0; i < questionableResponseDTOChkList.size(); i++ ) {
				int tktAmtType = questionableResponseDTOChkList.get(i).getAmtType();
				if( tktAmtType == 0 ) {
					cashableBatchDTO.add(questionableResponseDTOChkList.get(i));
				} else if( tktAmtType == 1 ) {
					nonCashableBatchDTO.add(questionableResponseDTOChkList.get(i));
				} else if( tktAmtType == 2 ) {
					promotionalBatchDTO.add(questionableResponseDTOChkList.get(i));
				} else if( tktAmtType == -1 ) {
					nonValidBatchDTO.add(questionableResponseDTOChkList.get(i));
				}			
			}
		} else {
			for(int i = 0; i < responseDTOChkList.size(); i++ ) {
				int tktAmtType = responseDTOChkList.get(i).getAmtType();
				if( tktAmtType == 0 ) {
					cashableBatchDTO.add(responseDTOChkList.get(i));
				} else if( tktAmtType == 1 ) {
					nonCashableBatchDTO.add(responseDTOChkList.get(i));
				} else if( tktAmtType == 2 ) {
					promotionalBatchDTO.add(responseDTOChkList.get(i));
				} else if( tktAmtType == -1 ) {
					nonValidBatchDTO.add(responseDTOChkList.get(i));
				}			
			}
		}


		getCasinoDetails();
		StringBuilder header = new StringBuilder();	
		header.append(IVoucherConstants.LINE_SEPERATOR+IVoucherConstants.LINE_SEPERATOR);
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_RPT_CASINO_NAME), length)+VoucherStringUtil.rPad(casinoName,length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(IVoucherConstants.LINE_SEPERATOR);
		header.append(IVoucherConstants.LINE_SEPERATOR);
		if( isQuestionable ) {
			header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_RPT_NAME),length)+VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_QUES_DETAILS_RPT),length));
			header.append(IVoucherConstants.LINE_SEPERATOR);
			header.append(VoucherStringUtil.nCopy('-', 43));
		} else {
			header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_RPT_NAME),length)+VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_BATCH_DETAILS_RPT),length));
			header.append(IVoucherConstants.LINE_SEPERATOR);
			header.append(VoucherStringUtil.nCopy('-', 35));
		}

		header.append(IVoucherConstants.LINE_SEPERATOR);
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(form.getBatchNbrValue(), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOUCHER_COUNT), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(form.getBatchVoucherCountValue(), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOUCHER_$)+ " ("+CurrencyUtil.getCurrencySymbol()+")", length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.lPad(CurrencyUtil.getCurrencyFormat(form.getBatchVoucherAmountValue()), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(IVoucherConstants.LINE_SEPERATOR);
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EMPLOYEE_ID), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(form.getBatchEmployeeValue(), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_COUPAN_COUNT), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(form.getBatchCouponCountValue(), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_COUPAN_$)+ " ("+CurrencyUtil.getCurrencySymbol()+")", length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.lPad(CurrencyUtil.getCurrencyFormat(form.getBatchCouponAmountValue()), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));		
		header.append(IVoucherConstants.LINE_SEPERATOR);
		header.append(IVoucherConstants.LINE_SEPERATOR);
		header.append(IVoucherConstants.LINE_SEPERATOR);		
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_START_TIME), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));

		SessionUtility utility = new SessionUtility();
		String datePattern = utility.getApplicationDateFormat();

		String startTimeS = DateUtil.getLocalTimeFromUTC(startTime.getTime()).toString();
		String startDate = DateHelper.getFormatedDate(startTimeS,IVoucherConstants.DATE_FORMAT,datePattern);
		
		/*String eff = DateUtil.getLocalTimeFromUTC(
										updatedTicketInfoDTO.getEffectiveDate()
										.getTime()).toString();
								String effectiveDate = DateHelper
								.getFormatedDate(eff,
										IVoucherConstants.DATE_FORMAT,
										datePattern);*/
		
		
		//String startDate = new SimpleDateFormat(DateUtil.getCurrentDateFormat()).format(startTime);

		String endTimeS = DateUtil.getLocalTimeFromUTC(endTime.getTime()).toString();
		String endDate = DateHelper.getFormatedDate(endTimeS,IVoucherConstants.DATE_FORMAT,datePattern);
		//String endDate = new SimpleDateFormat(DateUtil.getCurrentDateFormat()).format(endTime);

		header.append(VoucherStringUtil.lPad(startDate , length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_END_TIME), length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));
		header.append(VoucherStringUtil.lPad(endDate, length));
		header.append(VoucherStringUtil.rPad(" ", bufferLength));

		header = printHeader(header);
		header.append(IVoucherConstants.LINE_SEPERATOR);	
		
		StringBuilder buffer = new StringBuilder();	
		buffer.append(VoucherStringUtil.rPad("Amount Type: "+LabelLoader.getLabelValue(AmountTypeEnum.CASHABLE.toString()), length));
		buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		buffer.append(IVoucherConstants.LINE_SEPERATOR);

		for( int i = 0; i < cashableBatchDTO.size(); i++ ) {			
			Long tktTypeId = cashableBatchDTO.get(i).getTktTypeId();
			String tktType = "";
			if( tktTypeId != null ) {
				if( tktTypeId == 7 || tktTypeId == 1 || tktTypeId == 2 || tktTypeId == 3 || tktTypeId == 4 || tktTypeId == 9 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_NON_SLOT);
				} else if( tktTypeId == 5 || tktTypeId == 6 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_SLOT);
				}				
			}	
			String tktamt = "";
			Long amt = cashableBatchDTO.get(i).getTktAmount();
			if( amt != null ) {
				tktamt = CurrencyUtil.getCurrencyFormat(ConversionUtil.centsToDollar(amt.longValue()));
			}			
			buffer.append(VoucherStringUtil.rPad(cashableBatchDTO.get(i).getAssetConfNbr().trim(), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(cashableBatchDTO.get(i).getReclVouBrcd(), barCodeLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(tktType, reasonLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.lPad(tktamt, length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(cashableBatchDTO.get(i).getAuditCodeDesc()), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));			

			buffer.append(IVoucherConstants.LINE_SEPERATOR);
		}				

		buffer.append(VoucherStringUtil.rPad("Amount Type: "+LabelLoader.getLabelValue(AmountTypeEnum.NON_CASHABLE_PROMO.toString()), length));
		buffer.append(VoucherStringUtil.rPad(" ", bufferLength));	
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		for( int j = 0; j < nonCashableBatchDTO.size(); j++ ) {			
			Long tktTypeId = nonCashableBatchDTO.get(j).getTktTypeId();
			String tktType = "";
			if( tktTypeId != null ) {
				if( tktTypeId == 7 || tktTypeId == 1 || tktTypeId == 2 || tktTypeId == 3 || tktTypeId == 4 || tktTypeId == 9) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_NON_SLOT);
				} else if( tktTypeId == 5 || tktTypeId == 6 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_SLOT);
				}				
			}	
			String tktamt = "";
			Long amt = nonCashableBatchDTO.get(j).getTktAmount();
			if( amt != null ) {
				tktamt = CurrencyUtil.getCurrencyFormat(ConversionUtil.centsToDollar(amt.longValue()));
			}				
			buffer.append(VoucherStringUtil.rPad(nonCashableBatchDTO.get(j).getAssetConfNbr().trim(), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(nonCashableBatchDTO.get(j).getReclVouBrcd(), barCodeLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(tktType, reasonLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.lPad(tktamt, length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(nonCashableBatchDTO.get(j).getAuditCodeDesc()), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));			

			buffer.append(IVoucherConstants.LINE_SEPERATOR);

		}

		buffer.append(VoucherStringUtil.rPad("Amount Type: "+LabelLoader.getLabelValue(AmountTypeEnum.CASHABLE_PROMO.toString()), length));
		buffer.append(VoucherStringUtil.rPad(" ", bufferLength));		
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		
		for( int k = 0; k < promotionalBatchDTO.size(); k++ ) {			
			Long tktTypeId = promotionalBatchDTO.get(k).getTktTypeId();
			String tktType = "";
			
			if( tktTypeId != null ) {
				if( tktTypeId == 7 || tktTypeId == 1 || tktTypeId == 2 || tktTypeId == 3 || tktTypeId == 4 || tktTypeId == 9 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_NON_SLOT);
				} else if( tktTypeId == 5 || tktTypeId == 6 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_SLOT);
				}				
			}	
			String tktamt = "";
			Long amt = promotionalBatchDTO.get(k).getTktAmount();
			if( amt != null ) {
				tktamt = CurrencyUtil.getCurrencyFormat(ConversionUtil.centsToDollar(amt.longValue()));
			}			
			buffer.append(VoucherStringUtil.rPad(promotionalBatchDTO.get(k).getAssetConfNbr().trim(), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(promotionalBatchDTO.get(k).getReclVouBrcd(), barCodeLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(tktType, reasonLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.lPad(tktamt, length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(promotionalBatchDTO.get(k).getAuditCodeDesc()), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));			

			buffer.append(IVoucherConstants.LINE_SEPERATOR);
		}

		buffer.append(VoucherStringUtil.rPad("Amount Type: -", length));
		buffer.append(VoucherStringUtil.rPad(" ", bufferLength));			
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		buffer.append(IVoucherConstants.LINE_SEPERATOR);
		
		for( int i = 0; i < nonValidBatchDTO.size(); i++ ) {			
			Long tktTypeId = nonValidBatchDTO.get(i).getTktTypeId();
			String tktType = "";
			
			if( tktTypeId != null ) {
				if( tktTypeId == 7 || tktTypeId == 1 || tktTypeId == 2 || tktTypeId == 3 || tktTypeId == 4 || tktTypeId == 9 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_NON_SLOT);
				} else if( tktTypeId == 5 || tktTypeId == 6 ) {
					tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_SLOT);
				}				
			}	
			String tktamt = "";
			Long amt = nonValidBatchDTO.get(i).getTktAmount();
			if( amt != null ) {
				tktamt = CurrencyUtil.getCurrencyFormat(ConversionUtil.centsToDollar(amt.longValue()));
			}			
			buffer.append(VoucherStringUtil.rPad(nonValidBatchDTO.get(i).getAssetConfNbr().trim(), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(nonValidBatchDTO.get(i).getReclVouBrcd(), barCodeLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(tktType, reasonLength));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.lPad(tktamt, length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));
			buffer.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(nonValidBatchDTO.get(i).getAuditCodeDesc()), length));
			buffer.append(VoucherStringUtil.rPad(" ", bufferLength));			

			buffer.append(IVoucherConstants.LINE_SEPERATOR);
		}	
		try {
			ProgressIndicatorUtil.openInProgressWindow();
			log.debug("Print details result: "+buffer.toString());
			
			BatchDetailsPrinter batchDetailsPrinter = new BatchDetailsPrinter();
			batchDetailsPrinter.setHeaderString(header.toString());
			batchDetailsPrinter.print(buffer.toString());
			
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_SUCCESS));
		} catch( Exception e ) {
			log.error("Exception occured while printing the details",e);
			if( e.getMessage() != null && e.getMessage().trim().length() > 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(e.getMessage());
			} else {			
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_DETAILS_ERROR));
			}
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}

	}

	/**
	 * This method will get the recl batch info dto for the 
	 * barcode passed
	 * @param barcode
	 * @return
	 */
	private ReclBatchDetailsInfoDTO getReclBatchDetailsInfoDTO(String barcode){
		ReclBatchDetailsInfoDTO batchDetailsInfoDTO = null;
		for( int i = 0; i < responseDTOChkList.size(); i++ ) {
			ReclBatchDetailsInfoDTO infoDTO = responseDTOChkList.get(i);
			if( infoDTO.getReclVouBrcd().equalsIgnoreCase(barcode) ) {				
				batchDetailsInfoDTO = infoDTO;
			}
		}		
		return batchDetailsInfoDTO;
	}

	private void changeCashier(String retVal){
		try	{	

			String oldEmpValue = form.getBatchEmployeeValue();

			if( retVal == null )
				return;
			if( retVal.equalsIgnoreCase(form.getBatchEmployeeValue()) ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_EDIT_VALUE_ALREADY_ENTERED));
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setLocation(170,250);
				String result = null;				
				dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
				result = dialog.open(5,true);						
				changeCashier(result);
				return;
			}
			String errorMsg = VoucherUIValidator.validateEmployeeId(retVal);
			if( errorMsg.trim().length() != 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(errorMsg);
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setLocation(170,250);
				String result = null;			
				dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
				result = dialog.open(5,true);							
				changeCashier(result);
				return;
			}

			UserDTO  userDTO = FrameworkServiceLocator.getService().getUserDetails(retVal, SDSApplication.getSiteDetails().getId());				
			if( userDTO == null || userDTO.getUserName() == null) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_USER));	
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setLocation(170,250);
				String result = null;				
				dialog.setText(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
				result = dialog.open(5,true);							
				changeCashier(result);
				return;
			}				

			BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
			BatchInfoDTO batchForm = new BatchInfoDTO();
			batchForm.setBatchId(new Long(form.getBatchNbrValue()));			
			batchForm.setReconTypeId(batchReclForm.isCashier()?1:2);
			batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));			
			batchForm.setEmployeeId(retVal);			
			BaseDTO baseForm = ServiceCall.getInstance().changeLocation(batchForm);
			if( baseForm != null ) {
				if( baseForm.isErrorPresent() ) {							
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(baseForm));
				}
				else {	
					Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_SUMMARY_DETAIL), LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EMPLOYEE_ID),oldEmpValue, retVal, LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_CASHIER_CHANGED) + form.getBatchNbrValue(), EnumOperation.MODIFY_OPERATION,PreferencesUtil.getClientAssetNumber());
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID) + LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_EMPLOYEE_ID_CHANGE_SUCCESS)
							+"\n"+VoucherUtil.getI18nMessageForDisplay(baseForm));							
					this.batchReclForm.setEmployeeAssetId(retVal);
					form.setBatchEmployeeValue(retVal);
					adjustTableSizeBasedOnResolution();
					processBatchDetailsFromDB();
					loadBatchDetailsFromResponse();		
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);		
					enableGetQuestionables(batchDetailsComposite);
					isQuestionable = false;
					populateScreen(composite);
				}

			}
		}catch(VoucherEngineServiceException ex) {
			log.error("Exception while changing the location", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}
		catch (Exception ex) {
			log.error(ex);
		}
	}

	/**
	 * This method performs the modify operation of the ticket
	 */
	private void modifyTicket(String retVal){
		try	{
			String barCodeSelected = form.getDispBatchDetailsDTOSelectedValue().getReclVouBrcd();	

			ReclBatchDetailsInfoDTO batchDetailsInfoDTO = getReclBatchDetailsInfoDTO(barCodeSelected);

			if( retVal == null)
				return;

			String modifiedBarCode = retVal;
			String errorMsg = VoucherUIValidator.validateEnteredBarcode(modifiedBarCode);
			if( errorMsg.trim().length() != 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(errorMsg);
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
				dialog.setLocation(170,250);
				String result = dialog.open(18,true);
				modifyTicket(result);
				return;
			}
			populateForm(composite);
			if( checkDuplicateBarcode(responseDTOChkList,modifiedBarCode) ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_ALREDY_EXISTS));
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
				dialog.setLocation(170,250);
				String result = dialog.open(18,true);
				modifyTicket(result);
				return;
			}
			long batchId = Long.parseLong(form.getBatchNbrValue());
			BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
			BatchInfoDTO batchForm = new BatchInfoDTO();
			batchForm.setBatchId(batchId);
			batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			TicketInfoDTO ticketInfoDTO = ServiceCall.getInstance().updateTicketInBatch(batchForm,barCodeSelected,modifiedBarCode);
			if( ticketInfoDTO != null ) {
				if( ticketInfoDTO.isErrorPresent() ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
				}
				else {
					if( batchDetailsInfoDTO != null ) {
						if( ((batchDetailsInfoDTO.getAmtType() == 1)||batchDetailsInfoDTO.getAmtType() == 2) ) {
							couponCnt = couponCnt-1;
							couponAmount = couponAmount-batchDetailsInfoDTO.getTktAmount();							
						} else if( batchDetailsInfoDTO.getAmtType() == -1 ) {
							//Do nothing...
							
						} else if( batchDetailsInfoDTO.getAmtType() == 0 ) {									
							voucherCount = voucherCount-1;
							voucherAmount = voucherAmount-batchDetailsInfoDTO.getTktAmount();								
						}
					}							

					if( (ticketInfoDTO.getAmountType().getAmountType() == 1)||ticketInfoDTO.getAmountType().getAmountType() == 2 ) {
						couponCnt = couponCnt+1;
						couponAmount = couponAmount+ticketInfoDTO.getAmount();								
					} else if( ticketInfoDTO.getAmountType().getAmountType() == 0 ) {									
						voucherCount = voucherCount+1;
						voucherAmount = voucherAmount+ticketInfoDTO.getAmount();
					}

					Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_SUMMARY_DETAIL), LabelLoader.getLabelValue(IDBLabelKeyConstants.UPDATE_TKT)+AppContextValues.getInstance().getTicketText(),VoucherUtil.formatBarcode(barCodeSelected, (ticketInfoDTO.getTicketType() == null ? null : ticketInfoDTO.getTicketType().getTicketType()), ticketInfoDTO.getTktStatusId()), VoucherUtil.formatBarcode(retVal, (ticketInfoDTO.getTicketType() == null ? null : ticketInfoDTO.getTicketType().getTicketType()), ticketInfoDTO.getTktStatusId()), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_TKT_EDITED,new String[] {AppContextValues.getInstance().getTicketText()}) + form.getBatchNbrValue(), EnumOperation.MODIFY_OPERATION,PreferencesUtil.getClientAssetNumber());
					adjustTableSizeBasedOnResolution();
					processBatchDetailsFromDB();
					loadBatchDetailsFromResponse();
					populateScreen(composite);
					form.setDispBatchDetailsDTOSelectedValue(null);
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);	
					enableGetQuestionables(batchDetailsComposite);
					isQuestionable = false;
					MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
				}
			}
		}catch(VoucherEngineServiceException ex) {
			log.error("Exception while modifying the Voucher in the batch", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}	
		catch(Exception ex)	{
			log.error("Exception while modifying the Voucher in the batch", ex);				

		}	
	}

	/**
	 * This method performs the add ticket operation
	 */
	private void addTicket(String retVal){
		try	{

			if( retVal == null )
				return;

			String barCodeEntered = retVal;

			String errorMsg = VoucherUIValidator.validateEnteredBarcode(barCodeEntered);
			if( errorMsg.trim().length() != 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(errorMsg);
				VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
				dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
				dialog.setLocation(170,250);				
				String result = dialog.open(18,true);	
				addTicket(result);
				return;
			}
			populateForm(composite);
			if( checkDuplicateBarcode(responseDTOChkList,barCodeEntered) ) {
				ReclBatchDetailsInfoDTO batchDetailsInfoDTO = getReclBatchDetailsInfoDTO(barCodeEntered);	
				if( batchDetailsInfoDTO != null ) {	
					if( !batchDetailsInfoDTO.getAuditCodeDesc().equalsIgnoreCase(IVoucherConstants.MISSING_FROM_BATCH) ) {								
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_ALREDY_EXISTS));
						VoucherTSInputDialog dialog = new VoucherTSInputDialog(composite.getShell());
						dialog.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ENTER_TICKET) +AppContextValues.getInstance().getTicketText());
						dialog.setLocation(170,250);				
						String result = dialog.open(18,true);	
						addTicket(result);
						return;
					}
				}
			}

			long batchId = Long.parseLong(form.getBatchNbrValue());

			BatchDetailsComposite batchDetailsComposite = (BatchDetailsComposite)composite;
			BatchInfoDTO batchForm = new BatchInfoDTO();
			batchForm.setBatchId(batchId);
			batchForm.setUserFormDTO(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			TicketInfoDTO ticketInfoDTO =  ServiceCall.getInstance().addReclVoucherByBatchId(batchForm,barCodeEntered);
			if( ticketInfoDTO != null ) {
				if( ticketInfoDTO.isErrorPresent() ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
				}
				else {					
					if( (ticketInfoDTO.getAmountType().getAmountType() == 1)||ticketInfoDTO.getAmountType().getAmountType() == 2 ) {
						couponCnt = couponCnt+1;
						couponAmount = couponAmount+ticketInfoDTO.getAmount();
					} else if( ticketInfoDTO.getAmountType().getAmountType() == 0 ) {
						voucherCount = voucherCount+1;
						voucherAmount = voucherAmount+ticketInfoDTO.getAmount();
					}

					Util.sendDataToAuditTrail(IVoucherConstants.MODULE_NAME, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_SUMMARY_DETAIL), LabelLoader.getLabelValue(IDBLabelKeyConstants.ADD_TKT_IN_A_BATCH)+AppContextValues.getInstance().getTicketText() ,"", VoucherUtil.formatBarcode(retVal, (ticketInfoDTO.getTicketType() == null ? null : ticketInfoDTO.getTicketType().getTicketType()), ticketInfoDTO.getTktStatusId()), LabelLoader.getLabelValue(IDBLabelKeyConstants.BATCH_TKT_ADDED,new String[] {AppContextValues.getInstance().getTicketText()}) + form.getBatchNbrValue(), EnumOperation.ADD_OPERATION,PreferencesUtil.getClientAssetNumber());
					adjustTableSizeBasedOnResolution();
					processBatchDetailsFromDB();
					loadBatchDetailsFromResponse();
					disableDeleteTkt(batchDetailsComposite);
					disableModifyTkt(batchDetailsComposite);	
					enableGetQuestionables(batchDetailsComposite);
					isQuestionable = false;
					populateScreen(composite);
					MessageDialogUtil.displayTouchScreenInfoDialog(VoucherUtil.getI18nMessageForDisplay(ticketInfoDTO));
				}
			}
		} catch(VoucherEngineServiceException ex) {
			log.error("Exception while adding the Voucher in the batch", ex);						
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex)); 	
		}	
		catch(Exception ex)	{
			log.error("Exception while adding the Voucher in the batch", ex);				
		}	
	}

	private void enableModifyTkt(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblModifyTkt().setEnabled(true);
		batchDetailsComposite.getILblModifyTkt().setImage(enableImage);
		batchDetailsComposite.getILblModifyTkt().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableModifyTkt(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblModifyTkt().setEnabled(false);
		batchDetailsComposite.getILblModifyTkt().setImage(disableImage);
		batchDetailsComposite.getILblModifyTkt().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void enableDeleteTkt(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblDeleteTkt().setEnabled(true);
		batchDetailsComposite.getILblDeleteTkt().setImage(enableImage);
		batchDetailsComposite.getILblDeleteTkt().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableDeleteTkt(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblDeleteTkt().setEnabled(false);
		batchDetailsComposite.getILblDeleteTkt().setImage(disableImage);
		batchDetailsComposite.getILblDeleteTkt().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}

	private void enableGetQuestionables(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblGetQuestionables().setEnabled(true);
		batchDetailsComposite.getILblGetQuestionables().setImage(enableImage);
		batchDetailsComposite.getILblGetQuestionables().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
	}
	
	private void disableGetQuestionables(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblGetQuestionables().setEnabled(false);
		batchDetailsComposite.getILblGetQuestionables().setImage(disableImage);
		batchDetailsComposite.getILblGetQuestionables().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void disableReloadBatchDtls(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblReloadBatchDtls().setEnabled(false);
		batchDetailsComposite.getILblReloadBatchDtls().setImage(disableImage);
		batchDetailsComposite.getILblReloadBatchDtls().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void disableChangeEmployee(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblChangeEmployee().setEnabled(false);
		batchDetailsComposite.getILblChangeEmployee().setImage(disableImage);
		batchDetailsComposite.getILblChangeEmployee().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void disablePrintBatchDetails(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblPrintBatchDetails().setEnabled(false);
		batchDetailsComposite.getILblPrintBatchDetails().setImage(disableImage);
		batchDetailsComposite.getILblPrintBatchDetails().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void disableDeleteBatch(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblDeleteBatch().setEnabled(false);
		batchDetailsComposite.getILblDeleteBatch().setImage(disableImage);
		batchDetailsComposite.getILblDeleteBatch().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	private void disableAddTkt(BatchDetailsComposite batchDetailsComposite) {
		batchDetailsComposite.getILblAddTkt().setEnabled(false);
		batchDetailsComposite.getILblAddTkt().setImage(disableImage);
		batchDetailsComposite.getILblAddTkt().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
	}
	
	
}
