/**
 * 
 */
package com.ballydev.sds.slipsui.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSRadioImageLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.slips.dto.SlipsReportDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slipsui.composite.ReportComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.ReportForm;
import com.ballydev.sds.slipsui.print.PrintWithGraphics;
import com.ballydev.sds.slipsui.print.SlipImage;
import com.ballydev.sds.slipsui.print.SlipImageBuilderUtility;
import com.ballydev.sds.slipsui.print.SlipUtil;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.ConversionUtil;
import com.ballydev.sds.slipsui.util.FocusUtility;
import com.ballydev.sds.slipsui.util.PadderUtil;
import com.ballydev.sds.slipsui.util.ReportPrintDTO;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * This class acts as a controller for all the events performed in the 
 * ReportComposite 
 * @author gsrinivasulu
 *
 */
public class ReportController extends SDSBaseController {

	private ReportForm form;
	
	private ReportComposite reportComposite;
	
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	public ReportController(Composite parent , int style, ReportForm form , SDSValidator validator) throws Exception {
		super(form,validator);
		this.form = form;
		createReportComposite(parent,style);
		super.registerEvents(reportComposite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(reportComposite);
		FocusUtility.setTextFocus(reportComposite);
	}

	private void createReportComposite(Composite parent, int style) {
		reportComposite = new ReportComposite(parent,style);
		TopMiddleController.setCurrentComposite(reportComposite);		
	}

	/**
	 * ReportMouseListener class
	 *
	 */
	private class ReportMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			
		}

		public void mouseDown(MouseEvent e) {
			try {
				Control control = (Control) e.getSource();

				if (!(control instanceof SDSImageLabel)
						&& !(control instanceof SDSRadioImageLabel)) {
					return;
				}

				if (control instanceof SDSImageLabel) {
					if(((SDSImageLabel)control).getName().equalsIgnoreCase("submit")){

						populateForm(reportComposite);
						boolean validate = validate("ReportForm", form, reportComposite);
						
						if(!validate) {
							return;
						}
						
						if(form.getEmployeeId().length()>0)
						{
							ProgressIndicatorUtil.openInProgressWindow();
							SlipsReportDTO reportDTO = null;
							try {
//								reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
//										StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH), DateUtil.getLocalTimeFromUTC(form.getFromDate().getTime()).getTime(), 
//										DateUtil.getLocalTimeFromUTC(form.getToDate().getTime()).getTime());
								reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
										StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH), DateUtil.convertDateToString(form.getFromDate()), 
										DateUtil.convertDateToString(form.getToDate()));
							
							} catch (SlipsEngineServiceException e1) {
								log.info("EXCEPTION calling getSlipsReportInfo", e1);							
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,  e1.getMessage());	
								return;
							}
							catch (Exception e1) {
								log.info("EXCEPTION calling getSlipsReportInfo");	
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e1);
								return;
							}
								
							log.info("reportDTO values: "+reportDTO);
							System.out.println("Beef amt: "+reportDTO.getBeefAmount());
							ReportPrintDTO printDTO = null;
							if(reportDTO != null && ((reportDTO.getBeefAmount()!=null && reportDTO.getBeefAmount()>0) 
									|| (reportDTO.getVoidBeefAmount()!=null && reportDTO.getVoidBeefAmount()!=0))) {
								
								printDTO = new ReportPrintDTO();
								if(reportDTO.getEmployeeId()!=null){
									printDTO.setSlotAttendantId(PadderUtil.lPad(reportDTO.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
								}
								if(reportDTO.getEmployeeName()!=null){
									printDTO.setSlotAttendantName(reportDTO.getEmployeeName());
								}						
								
								if(reportDTO.getBeefAmount()!=null){
								/*	printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getBeefAmount()))); */
									
									printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));
									
								}	
								if(reportDTO.getVoidBeefAmount()!=null){
									/*printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));*/
									
									printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));
									
								}					
								printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getFromDate()));
								printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getToDate()));
								printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
								printDTO.setPrinterSchema(reportDTO.getPrinterSchema());
								printDTO.setSlipSchema(reportDTO.getSlipSchema());
								printDTO.setSiteId(MainMenuController.slipForm.getSiteId());
								
								printDTO.setCasinoName(MainMenuController.slipForm.getSiteLongName());
								
								/** BUILDING THE SLIP IMAGE AND PRINT*/
								SlipImage slipImage = new SlipImage();
								SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
								PrintWithGraphics printGrap = new PrintWithGraphics();
								String image = null;
								HashMap<String, String> slipI18NMap = SlipUtil.reportSlipMap;
								if (SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.EPSON_PRINTER.toUpperCase())){
									image = slipImage.buildSlipImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printUtil.printSlipImage(image);
								}else if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.LASER_PRINTER.toUpperCase())) {
									image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printGrap.printTextImage(image);
								} else{
									image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printGrap.printTextImage(image);
								}
//								
								MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPORT_SUCCESS));
								ProgressIndicatorUtil.closeInProgressWindow();
							}else{							
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
								reportComposite.getTxtEmpId().forceFocus();
							}
						}
						else{
							ProgressIndicatorUtil.openInProgressWindow();
							SlipsReportDTO reportDTO;
							try 
							{
								reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
										DateUtil.convertDateToString(form.getFromDate()), DateUtil.convertDateToString(form.getToDate()));
						
							}catch (SlipsEngineServiceException e1) {
								log.info("EXCEPTION calling getSlipsReportInfo", e1);							
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,  e1.getMessage());	
								return;
							}
							catch (Exception e1) {
								log.info("EXCEPTION calling getSlipsReportInfo");	
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e1);
								return;
							}
							
							ReportPrintDTO printDTO = null;
							if(reportDTO != null && ((reportDTO.getBeefAmount()!=null && reportDTO.getBeefAmount()!=0)
									|| ( reportDTO.getVoidBeefAmount() !=null && reportDTO.getVoidBeefAmount()!=0))) {
								printDTO = new ReportPrintDTO();
																
								if(reportDTO.getBeefAmount()!=null){
									/*printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));*/
									
									printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));								
								}	
								if(reportDTO.getVoidBeefAmount()!=null){
									/*printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));*/
									
									printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
											+ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));	
									
								}	
								printDTO.setSiteId(MainMenuController.slipForm.getSiteId());
								printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getFromDate()));
								printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getToDate()));
								printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
								printDTO.setPrinterSchema(reportDTO.getPrinterSchema());
								printDTO.setSlipSchema(reportDTO.getSlipSchema());
								
								printDTO.setCasinoName(MainMenuController.slipForm.getSiteLongName());
															
								/** BUILDING THE SLIP IMAGE AND PRINT*/
								SlipImage slipImage = new SlipImage();
								SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
								PrintWithGraphics printGrap = new PrintWithGraphics();
								String image = null;
								HashMap<String, String> slipI18NMap = SlipUtil.reportSlipMap;
								if (SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.EPSON_PRINTER.toUpperCase())){
									image = slipImage.buildSlipImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printUtil.printSlipImage(image);
								}else if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.LASER_PRINTER.toUpperCase())) {
									image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printGrap.printTextImage(image);
								} else{
									image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
									log.debug("Slip Image: "+image);
									/** SEND THE SLIP IMAGE FOR PRINT */ 
									printGrap.printTextImage(image);
								}
								
								MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPORT_SUCCESS));
								ProgressIndicatorUtil.closeInProgressWindow();
							}else{
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
								reportComposite.getTxtEmpId().forceFocus();
							}
						}
						
						CallInitialScreen initialScreen = new CallInitialScreen();
						initialScreen.callReportScreen();
						
					}
				}
			} catch (Exception e1) {				
				ProgressIndicatorUtil.closeInProgressWindow();
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				log.error(e1);	
			}
		}

		public void mouseUp(MouseEvent e) {
			
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ReportMouseListener listener = new ReportMouseListener();
		reportComposite.getButtonComposite().getBtnSubmit().addMouseListener(listener);
		reportComposite.getButtonComposite().getBtnSubmit().getTextLabel().addTraverseListener(this);
	}
	
	
	/*@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			Control control = (Control) e.getSource();
			if (!(control instanceof CbctlButton)
					&& !(control instanceof TouchScreenRadioButton)) {
				return;
			}
			
			if(control instanceof CbctlButton) {
				if(((CbctlButton)control).getName().equalsIgnoreCase("report")){
					populateForm(reportComposite);
					boolean validate = validate("ReportForm", form, reportComposite);
					
					if(!validate) {
						return;
					}
					
					if(form.getEmployeeId().length()>0)
					{
						ProgressIndicatorUtil.openInProgressWindow();
						SlipsReportDTO reportDTO = null;
						try {
//							reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
//									StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH), DateUtil.getLocalTimeFromUTC(form.getFromDate().getTime()).getTime(), 
//									DateUtil.getLocalTimeFromUTC(form.getToDate().getTime()).getTime());
							reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
									StringUtil.padLeftZeros(form.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH), DateUtil.convertDateToString(form.getFromDate()), 
									DateUtil.convertDateToString(form.getToDate()));
						
						} catch (SlipsEngineServiceException e1) {
							log.info("EXCEPTION calling getSlipsReportInfo", e1);							
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,  e1.getMessage());	
							return;
						}
						catch (Exception e1) {
							log.info("EXCEPTION calling getSlipsReportInfo");	
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e1);
							return;
						}
							
						log.info("reportDTO values: "+reportDTO);
						System.out.println("Beef amt: "+reportDTO.getBeefAmount());
						ReportPrintDTO printDTO = null;
						if(reportDTO != null && ((reportDTO.getBeefAmount()!=null && reportDTO.getBeefAmount()>0) 
								|| (reportDTO.getVoidBeefAmount()!=null && reportDTO.getVoidBeefAmount()!=0))) {
							
							printDTO = new ReportPrintDTO();
							if(reportDTO.getEmployeeId()!=null){
								printDTO.setSlotAttendantId(PadderUtil.lPad(reportDTO.getEmployeeId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
							}
							if(reportDTO.getEmployeeName()!=null){
								printDTO.setSlotAttendantName(reportDTO.getEmployeeName());
							}						
							
							if(reportDTO.getBeefAmount()!=null){
								printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getBeefAmount()))); 
								
								printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));
								
							}	
							if(reportDTO.getVoidBeefAmount()!=null){
								printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));
								
								printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));
								
							}					
							printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getFromDate()));
							printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getToDate()));
							printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
							printDTO.setPrinterSchema(reportDTO.getPrinterSchema());
							printDTO.setSlipSchema(reportDTO.getSlipSchema());
							printDTO.setSiteId(MainMenuController.slipForm.getSiteId());
							
							printDTO.setCasinoName(MainMenuController.slipForm.getSiteLongName());
							
							*//** BUILDING THE SLIP IMAGE AND PRINT*//*
							SlipImage slipImage = new SlipImage();
							SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
							PrintWithGraphics printGrap = new PrintWithGraphics();
							String image = null;
							HashMap<String, String> slipI18NMap = SlipUtil.reportSlipMap;
							if (SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.EPSON_PRINTER.toUpperCase())){
								image = slipImage.buildSlipImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printUtil.printSlipImage(image);
							}else if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.LASER_PRINTER.toUpperCase())) {
								image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printGrap.printTextImage(image);
							} else{
								image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printGrap.printTextImage(image);
							}
//							
							MessageDialogUtil
							.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPORT_SUCCESS));
							ProgressIndicatorUtil.closeInProgressWindow();
						}else{							
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil
							.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
							reportComposite.getTxtEmpId().forceFocus();
						}
					}
					else{
						ProgressIndicatorUtil.openInProgressWindow();
						SlipsReportDTO reportDTO;
						try 
						{
							reportDTO = SlipsServiceLocator.getService().getSlipsReportInfo(MainMenuController.slipForm.getSiteId(),
									DateUtil.convertDateToString(form.getFromDate()), DateUtil.convertDateToString(form.getToDate()));
					
						}catch (SlipsEngineServiceException e1) {
							log.info("EXCEPTION calling getSlipsReportInfo", e1);							
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,  e1.getMessage());	
							return;
						}
						catch (Exception e1) {
							log.info("EXCEPTION calling getSlipsReportInfo");	
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e1);
							return;
						}
						
						ReportPrintDTO printDTO = null;
						if(reportDTO != null && ((reportDTO.getBeefAmount()!=null && reportDTO.getBeefAmount()!=0)
								|| ( reportDTO.getVoidBeefAmount() !=null && reportDTO.getVoidBeefAmount()!=0))) {
							printDTO = new ReportPrintDTO();
															
							if(reportDTO.getBeefAmount()!=null){
								printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));
								
								printDTO.setBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getBeefAmount())));								
							}	
							if(reportDTO.getVoidBeefAmount()!=null){
								printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));
								
								printDTO.setVoidBeefAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
										+ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(reportDTO.getVoidBeefAmount())));	
								
							}	
							printDTO.setSiteId(MainMenuController.slipForm.getSiteId());
							printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getFromDate()));
							printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(form.getToDate()));
							printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()));
							printDTO.setPrinterSchema(reportDTO.getPrinterSchema());
							printDTO.setSlipSchema(reportDTO.getSlipSchema());
							
							printDTO.setCasinoName(MainMenuController.slipForm.getSiteLongName());
														
							*//** BUILDING THE SLIP IMAGE AND PRINT*//*
							SlipImage slipImage = new SlipImage();
							SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
							PrintWithGraphics printGrap = new PrintWithGraphics();
							String image = null;
							HashMap<String, String> slipI18NMap = SlipUtil.reportSlipMap;
							if (SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.EPSON_PRINTER.toUpperCase())){
								image = slipImage.buildSlipImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printUtil.printSlipImage(image);
							}else if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.LASER_PRINTER.toUpperCase())) {
								image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printGrap.printTextImage(image);
							} else{
								image = slipImage.getLaserPrinterImage(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, slipI18NMap);
								log.debug("Slip Image: "+image);
								*//** SEND THE SLIP IMAGE FOR PRINT *//* 
								printGrap.printTextImage(image);
							}
							
							MessageDialogUtil
							.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPORT_SUCCESS));
							ProgressIndicatorUtil.closeInProgressWindow();
						}else{
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil
							.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_TO_PRINT_FOR_DATE));
							reportComposite.getTxtEmpId().forceFocus();
						}
					}
					CallInitialScreen initialScreen = new CallInitialScreen();
					initialScreen.callReportScreen();	
				}
			}
		} catch (Exception e1) {
			ProgressIndicatorUtil.closeInProgressWindow();
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e1, e1.getMessage());
			log.error(e1);		
		}
		finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}*/
	
	/**
	 * A method to get the keyboard focus
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		super.focusLost(e);
		reportComposite.setRedraw(true);
		reportComposite.redraw();
	}
	
	public ReportForm getForm() {
		return form;
	}

	public void setForm(ReportForm form) {
		this.form = form;
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return reportComposite;
	}
}
