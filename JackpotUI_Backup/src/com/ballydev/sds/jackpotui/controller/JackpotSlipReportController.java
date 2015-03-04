/*****************************************************************************
 * $Id: JackpotSlipReportController.java,v 1.45, 2010-09-23 04:29:41Z, Subha Viswanathan$
 * $Date: 9/22/2010 11:29:41 PM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.JackpotSlipReportComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.JackpotSlipReportForm;
import com.ballydev.sds.jackpotui.print.PrintWithGraphics;
import com.ballydev.sds.jackpotui.print.SlipImage;
import com.ballydev.sds.jackpotui.print.SlipPrinting;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JpReportPrintDTO;
import com.ballydev.sds.jackpotui.util.PadderUtil;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;


/**
 * This class acts as a controller for all the events performed in the 
 * JackpotSlipReportComposite
 * @author dambereen
 * @version $Revision: 46$
 */
public class JackpotSlipReportController extends SDSBaseController {

	/**
	 * JackpotSlipReportForm instance
	 */
	private JackpotSlipReportForm form;

	/**
	 * JackpotSlipReportComposite instance
	 */
	private JackpotSlipReportComposite jpSlipReportComposite;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Report Date Format
	 */
	private String REPORT_DATE_FORMAT = "dd-MMM-yy HH:mm:ss";
	private List<JackpotReportsDTO> list = new ArrayList<JackpotReportsDTO>();
	
	List<JackpotReportsDTO> jpReportsDTOList =  null;
	
	long jpTotalOrgAmt  = 0l;
	long jpTotalNetAmt  = 0l;
	long jpTotalTaxAmt  = 0l;
	long jpTotalVoidedAmt  = 0l;
	
	/**
	 * VoidController constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public JackpotSlipReportController(Composite parent, int style, JackpotSlipReportForm form,
			SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		
		CbctlUtil.displayMandatoryLabel(true);	
		createVoidComposite(parent, style);
		TopMiddleController.setCurrentComposite(jpSlipReportComposite);
		log.info("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();		
		super.registerEvents(jpSlipReportComposite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(jpSlipReportComposite);
		jpSlipReportComposite.getTxtEmployeeId().forceFocus();
	}

	/**
	 * This method is used to perform action for widgetselected event 
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {}		
		/*} catch (Exception ex) {
			log.error(ex);
		}*/
	//}

	/**
	 * This method is used to register customized listeners
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		JackpotSlipReportListener listener = new JackpotSlipReportListener();
		jpSlipReportComposite.getButtonComposite().getBtnSubmit().addMouseListener(listener);
		jpSlipReportComposite.getButtonComposite().getBtnSubmit().getTextLabel().addTraverseListener(this);
		
	}

	/**
	 * A method to create VoidComposite
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createVoidComposite(Composite p, int s ) {
		jpSlipReportComposite = new JackpotSlipReportComposite(p, s);
	}


	/**
	 * This method returns VoidComposite
	 */
	@Override
	public Composite getComposite() {
		return jpSlipReportComposite;
	}
	
	/**
	 * A method to get the keyboard focus
	 */
	@Override
	public void focusGained(FocusEvent e) {	
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}
	

	/**
	 * Method to display the jp slip report
	 * @param jpSlipReportComposite
	 * @param listJackpotReport
	 */
	public void displayReport(JackpotSlipReportComposite jpSlipReportComposite,
			List<JackpotReportsDTO> listJackpotReport) {

		try {
			
			SortedMap<Long,JackpotReportsDTO> map = new TreeMap<Long, JackpotReportsDTO>();
			for(int i=0; i<listJackpotReport.size(); i++){								
					long seq = listJackpotReport.get(i).getSequenceNo();
					if(!(map.containsKey(seq))){
						map.put(seq,listJackpotReport.get(i));									
					}
			}
			List<JackpotReportsDTO> jpRptsDTOListNew = new  ArrayList<JackpotReportsDTO>();
			jpRptsDTOListNew.addAll(map.values());
			
			//Date date = new Date();
			String totalTaxAmount = null;
			String totalVoidedAmount = null;
			String totalHPJPAmount = null;
			String totalJackpotAmount = null;
			
			StringBuilder buffer = new StringBuilder("\t ");
			buffer.append("\n");
			buffer.append("\n");
			buffer.append("\t\t\t");
			buffer.append(LabelLoader
					.getLabelValue(LabelKeyConstants.JACKPOT_PRINT_SEQ_NUMBER));
			buffer.append("\t\t\t");
			buffer.append(LabelLoader
					.getLabelValue(LabelKeyConstants.JACKPOT_PRINT_ACNF_NUMBER));
			buffer.append("\t\t\t\t\t");
			buffer.append(LabelLoader
					.getLabelValue(LabelKeyConstants.JACKPOT_HPJP_AMOUNT)+"("+MainMenuController.jackpotForm.getSiteCurrencySymbol()+")");
			buffer.append("\t\t\t\t\t\t");
			buffer.append(LabelLoader
					.getLabelValue(LabelKeyConstants.JACKPOT_NET_AMOUNT)+"("+MainMenuController.jackpotForm.getSiteCurrencySymbol()+")"
					+ "\n");
			buffer.append("\t\t\t");
			buffer.append("----------------------------------------------------------------------------------------------\n");
			for (JackpotReportsDTO jackpotReportsDTO : jpRptsDTOListNew) {
				buffer.append("\t\t\t");

				totalTaxAmount = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalTaxAmt)));
				totalVoidedAmount = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalVoidedAmt)));
				// total jp net amt
				totalHPJPAmount = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalNetAmt)));
				//jp HPJP amt without rounding
				totalJackpotAmount = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalOrgAmt)));
								
				//printedDate = 
				char c = '\u0020';

				buffer.append(PadderUtil.leftPad(new Long(jackpotReportsDTO
						.getSequenceNo()).toString(), 17, c));
				buffer.append(PadderUtil.leftPad(new Long(jackpotReportsDTO
						.getSlotNo()).toString(), 17, c));
				buffer.append(PadderUtil.leftPad(JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jackpotReportsDTO
						.getHpjpAmt()))), 26, c));
				buffer.append(PadderUtil.leftPad(JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jackpotReportsDTO
						.getNetAmt()))), 26, c));
				buffer.append("\t\t\t" + "\n \n");
			}
			
			buffer.append("\t"
					+ LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_PRINT_TOTAL_JACKPOT_AMOUNT)
					+ "\t : \t");
			buffer.append(totalJackpotAmount + "\n \n");
			
			buffer.append("\t"
					+ LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_NET_AMOUNT)+ "\t : \t");
          	buffer.append(totalHPJPAmount + "\n \n");
			
			buffer.append("\t"
							+ LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TOTAL_TAX_AMOUNT)
							+ "\t : \t");
			buffer.append(totalTaxAmount + "\n \n");
			
			buffer.append("\t"
					+ LabelLoader
							.getLabelValue(LabelKeyConstants.JACKPOT_TOTAL_VOIDED_AMOUNT)
					+ "\t : \t");
			buffer.append(totalVoidedAmount + "\n \n");
			
			buffer.append("\t"+ LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_PRINTED_DATE)
					+ "\t : \t");
			
		    /*buffer.append(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()) + "\n \n");
					jpSlipReportComposite.getTxtReportsDisplay().setText(
							buffer.toString());*/
		} catch (Exception e) {
			log.error("Exception while displaying the report values"+e.getMessage());
		}
	}
	
	private class JackpotSlipReportListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {

			try {
				Control control = (Control) e.getSource();

				if (!((Control) e.getSource() instanceof SDSImageLabel)) {
					return;
				}

				if (((Control) e.getSource() instanceof SDSImageLabel)) {

					/*if (((SDSImageLabel) control).getName().equalsIgnoreCase(
							LabelKeyConstants.CTRL_BTN_FORWARD)) {
						if (jpSlipReportComposite.getTxtReportsDisplay() != null) {
							StyledText text = jpSlipReportComposite
									.getTxtReportsDisplay();
							text.setTopIndex(text.getTopIndex() - 2);
						}
					} else if (((CbctlButton) control).getName().equalsIgnoreCase(
							LabelKeyConstants.CTRL_BTN_BACKWARD)) {
						if (jpSlipReportComposite.getTxtReportsDisplay() != null) {
							StyledText text = jpSlipReportComposite
									.getTxtReportsDisplay();
							text.setTopIndex(text.getTopIndex() + 2);
						}
					}*/

				}
				if (((SDSImageLabel) control).getName().equalsIgnoreCase("submit")) {			
					
					populateForm(jpSlipReportComposite);
								
					boolean validate = validate("JackpotSlipReportForm", form, jpSlipReportComposite);				
					
					/* Populating the form with screen values */
				
					if(!validate){
						return;
					}
					jpReportsDTOList = new ArrayList<JackpotReportsDTO>();
					/*if (!com.ballydev.sds.framework.util.Util.isSmallerResolution()) {
						jpSlipReportComposite.getLblReportHeading().setVisible(true);
						jpSlipReportComposite.getGrpTextArea().setVisible(true);
					}*/
					if(form.getEmployeeId()!=null && form.getEmployeeId().length()>=1){					
						try {
							
							
							log.info("Calling the getDetailsToPrintJpSlipReportForDateEmployee web method");
							log.info("*******************************************");
							log.info("Employee Id: "+form.getEmployeeId());
							log.info("From date: "+form.getFromDate());
							log.info("To date: "+form.getToDate());
							log.info("Site id: "+MainMenuController.jackpotForm.getSiteId());	
							log.info("*******************************************");
							ProgressIndicatorUtil.openInProgressWindow();
//							jpReportsDTOList = (List<JackpotReportsDTO>) JackpotServiceLocator.getService().getDetailsToPrintJpSlipReportForDateEmployee(MainMenuController.jackpotForm.getSiteId(),StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH),
//									com.ballydev.sds.framework.util.DateUtil.getLocalTimeFromUTC(form.getFromDate().getTime()).getTime(), com.ballydev.sds.framework.util.DateUtil.getLocalTimeFromUTC(form.getToDate().getTime()).getTime());
							jpReportsDTOList = (List<JackpotReportsDTO>) JackpotServiceLocator.getService().getDetailsToPrintJpSlipReportForDateEmployee(MainMenuController.jackpotForm.getSiteId(),StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH),
									DateUtil.convertDateToString(form.getFromDate()), DateUtil.convertDateToString(form.getToDate()));
							ProgressIndicatorUtil.closeInProgressWindow();
							log.info("Values returned after calling the getDetailsToPrintJpSlipReportForDateEmployee web method");
							log.info("*******************************************");
							if(jpReportsDTOList!=null && jpReportsDTOList.size()>0){
								log.info("JackpotDTO values returned: "+jpReportsDTOList.toString());		
														
								for(int j=0; j< jpReportsDTOList.size(); j++){							
									jpTotalOrgAmt = jpReportsDTOList.get(j).getJackpotOrgAmt();
									jpTotalNetAmt = jpReportsDTOList.get(j).getJackpotNetAmt();
									jpTotalTaxAmt = jpReportsDTOList.get(j).getJackpotTaxAmt();
									jpTotalVoidedAmt = jpReportsDTOList.get(j).getVoidedJackpotAmt();
								}
								
								log.info("Jackpot Org Amt: "+jpTotalOrgAmt);
								log.info("Jackpot Net Amt: "+jpTotalNetAmt);
								log.info("Jackpot Tax Amt: "+jpTotalTaxAmt);
								log.info("Voided Jp Amt: "+jpTotalVoidedAmt);
								log.info("Printer Schema: "+jpReportsDTOList.get(0).getPrinterSchema());
								log.info("Slip Schema: "+jpReportsDTOList.get(0).getSlipSchema());
							}
							log.info("*******************************************");
							if((jpReportsDTOList!=null && jpReportsDTOList.size()>0) && (jpTotalNetAmt!=0 || jpTotalVoidedAmt!=0)){							
								/*
								 * Calling the below method to display the report in the UI 
								 */		
								/*if (com.ballydev.sds.framework.util.Util.isSmallerResolution()){*/
									JackpotUIUtil.disposeCurrentMiddleComposite();
									new JackpotReportTextController(TopMiddleController.jackpotMiddleComposite,
											SWT.NONE,new SDSForm(),new SDSValidator(getClass(),true),
											jpReportsDTOList,jpTotalOrgAmt,jpTotalNetAmt, jpTotalTaxAmt,jpTotalVoidedAmt,form.getFromDate(),form.getToDate());
									
								/*}
								else {
									displayReport(jpSlipReportComposite , jpReportsDTOList);	
									//jpSlipReportComposite.getBtnPrintReport().setEnabled(true);
								}*/												
								
								
							}else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
								form.setEmployeeId("");
								Date date = new Date();							
								form.setFromDate(date);
								form.setToDate(date);
								CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
								initialScreen.callReportJPFirstScreen();
							}
						} catch (JackpotEngineServiceException e1) {
							ProgressIndicatorUtil.closeInProgressWindow();
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							log.error("Exception while checking the getDetailsToPrintJpSlipReportForDateEmployee web method",e1);
							return;
						}catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e2);
							return;
						}
					}else {// If no employee id as input
						try{
//							long jpTotalOrgAmt  = 0l;
//							long jpTotalNetAmt  = 0l;
//							long jpTotalTaxAmt  = 0l;
//							long jpTotalVoidedAmt  = 0l;
							log.info("Calling the getDetailsToPrintJpSlipReportForDate web method");
							log.info("*******************************************");
							log.info("From date: "+form.getFromDate());
							log.info("To date: "+form.getToDate());
							log.info("Site id: "+MainMenuController.jackpotForm.getSiteId());	
							log.info("*******************************************");
							ProgressIndicatorUtil.openInProgressWindow();
							//jpReportsDTOList = (List<JackpotReportsDTO>) JackpotServiceLocator.getService().getDetailsToPrintJpSlipReportForDate(MainMenuController.jackpotForm.getSiteId(), 
							//		com.ballydev.sds.framework.util.DateUtil.getLocalTimeFromUTC(form.getFromDate().getTime()).getTime(), com.ballydev.sds.framework.util.DateUtil.getLocalTimeFromUTC(form.getToDate().getTime()).getTime());
							jpReportsDTOList = (List<JackpotReportsDTO>) JackpotServiceLocator.getService().getDetailsToPrintJpSlipReportForDate(MainMenuController.jackpotForm.getSiteId(), 
									DateUtil.convertDateToString(form.getFromDate()), DateUtil.convertDateToString(form.getToDate()));
							ProgressIndicatorUtil.closeInProgressWindow();
							log.info("Values returned after calling the getDetailsToPrintJpSlipReportForDate web method");
							log.info("*******************************************");
													
							if((jpReportsDTOList!=null && jpReportsDTOList.size()>0) && jpReportsDTOList.size()>0){
								log.info("JackpotDTO values returned: "+jpReportsDTOList.toString());		
														
								for(int j=0; j< jpReportsDTOList.size(); j++){							
									jpTotalOrgAmt = jpReportsDTOList.get(j).getJackpotOrgAmt();
									jpTotalNetAmt = jpReportsDTOList.get(j).getJackpotNetAmt();
									jpTotalTaxAmt = jpReportsDTOList.get(j).getJackpotTaxAmt();
									jpTotalVoidedAmt = jpReportsDTOList.get(j).getVoidedJackpotAmt();
								}
								
								log.info("Jackpot Org Amt: "+jpTotalOrgAmt);
								log.info("Jackpot Net Amt: "+jpTotalNetAmt);
								log.info("Jackpot Tax Amt: "+jpTotalTaxAmt);
								log.info("Voided Jp Amt: "+jpTotalVoidedAmt);
								log.info("Printer Schema: "+jpReportsDTOList.get(0).getPrinterSchema());
								log.info("Slip Schema: "+jpReportsDTOList.get(0).getSlipSchema());
							}
							
							log.info("*******************************************");
							if(jpReportsDTOList!=null && (jpTotalNetAmt!=0 || jpTotalVoidedAmt!=0)){
								/*
								 * Calling the below method to display the report in the UI 
								 */
								/*if (com.ballydev.sds.framework.util.Util.isSmallerResolution()){*/
									JackpotUIUtil.disposeCurrentMiddleComposite();
									new JackpotReportTextController(TopMiddleController.jackpotMiddleComposite,
											SWT.NONE,new SDSForm(),new SDSValidator(getClass(),true),
											jpReportsDTOList,jpTotalOrgAmt,jpTotalNetAmt, jpTotalTaxAmt,jpTotalVoidedAmt,form.getFromDate(),form.getToDate());
								/*}
								else {
									displayReport(jpSlipReportComposite , jpReportsDTOList);
									//jpSlipReportComposite.getBtnPrintReport().setEnabled(true);
								}*/
							}
							else 
							{
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
								form.setEmployeeId("");
								Date date = new Date();							
								form.setFromDate(date);
								form.setToDate(date);
								CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
								initialScreen.callReportJPFirstScreen();
							}
							
						}catch (JackpotEngineServiceException e1) {
							ProgressIndicatorUtil.closeInProgressWindow();
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							log.error("Exception while checking the getDetailsToPrintJpSlipReportForDate web method",e1);
							return;
						}catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e2);
							return;
						}
					}		
				}else if (((SDSImageLabel) control).getName().equalsIgnoreCase("print")) {
					
					if(jpReportsDTOList!=null && (jpTotalNetAmt!=0 || jpTotalVoidedAmt!=0)){
						log.info("BEFORE CALLING THE SlipImage");
												
						JpReportPrintDTO printDTO = new JpReportPrintDTO();
						
						if(form.getEmployeeId()!=null && form.getEmployeeId().length()>1){									
							printDTO.setSlotAttendantId(StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
							log.info("Emp Id : "+printDTO.getSlotAttendantId());		
						}						
						if(jpReportsDTOList.get(0).getSlotAttendantFirstName()!=null){
							log.info("Emp First Name : "+jpReportsDTOList.get(0).getSlotAttendantFirstName());
							printDTO.setSlotAttendantName(jpReportsDTOList.get(0).getSlotAttendantFirstName());
						}								
						if(jpTotalNetAmt!=0){
							printDTO.setJackpotNetAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalNetAmt))));
							log.info("Jackpot Net Amt : "+printDTO.getJackpotNetAmount());
						}							
						if(jpTotalVoidedAmt!=0){
							printDTO.setJackpotVoidAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalVoidedAmt))));
							log.info("Total Void Amt : "+printDTO.getJackpotVoidAmount());
						}
						if(jpTotalTaxAmt!=0){
							printDTO.setJackpotTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalTaxAmt))));
							log.info("Total Tax Amt : "+printDTO.getJackpotTaxAmount());
						}
						if(jpTotalOrgAmt!=0){
							printDTO.setJackpotAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalOrgAmt))));
							log.info("Jackpot Org Amt : "+printDTO.getJackpotAmount());
						}
						printDTO.setSiteId(MainMenuController.jackpotForm.getSiteId());
						printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getFromDate()));
						printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getToDate()));
						printDTO.setPrinterSchema(jpReportsDTOList.get(0).getPrinterSchema());
						printDTO.setSlipSchema(jpReportsDTOList.get(0).getSlipSchema());					
						printDTO.setCasinoName(MainMenuController.jackpotForm.getSiteLongName());					
						
						log.info("Current date: "+DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
						printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
						
						log.info("Report slip map : "+SlipUtil.reportSlipMap);
						log.info("Report map: "+SlipUtil.reportSlipMap);
						SlipImage slipImage = new SlipImage();
						String image = null;
						try {				
							if(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(IAppConstants.EPSON_PRINTER_SUBSTRING)){
								log.info("Epson printer was selected, calling buildSlipImage method");					
								image = slipImage.buildSlipImageSlipRpt(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, SlipUtil.reportSlipMap);
								log.debug("Image : "+image);
								SlipPrinting slipPrinting = new SlipPrinting();
								slipPrinting.printSlip(image);
							}else if(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).contains(IAppConstants.LASER_JET_PRINTER_SUBSTRING)){
								log.info("Laser Jet printer was selected, calling getLaserPrinterImage method");
								image = slipImage.getLaserPrinterImageSlipRpt(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, SlipUtil.reportSlipMap, jpReportsDTOList);
								log.debug("Image : "+image);
								/** SEND THE SLIP IMAGE FOR PRINT */ 
								PrintWithGraphics printWithGraphics = new PrintWithGraphics();
								printWithGraphics.printReport(image);
							}else{
								log.info("Default was selected, calling getLaserPrinterImage method");
								image = slipImage.getLaserPrinterImageSlipRpt(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, SlipUtil.reportSlipMap, jpReportsDTOList);
								log.debug("Image : "+image);
								/** SEND THE SLIP IMAGE FOR PRINT */ 
								PrintWithGraphics printWithGraphics = new PrintWithGraphics();
								printWithGraphics.printReport(image);
							}
						
							log.info("AFTER CALLING THE SlipImage");
						} catch (Exception e1) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, MessageKeyConstants.EXCEPTION_OCCURED_DURING_SLIP_PRINT);
							log.error("Exception occured while printing the slip",e1);
							return;
						}					
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_REPORT_PRINT_SUCCESS));
						//jpSlipReportComposite.getBtnPrintReport().setEnabled(false);					
					}else{					
						log.debug("returned list is null");					
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			   }
			
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
