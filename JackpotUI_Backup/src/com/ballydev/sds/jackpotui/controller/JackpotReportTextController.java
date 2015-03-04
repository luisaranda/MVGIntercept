package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpotui.composite.JackpotReportTextComposite;
import com.ballydev.sds.jackpotui.composite.TopMiddleComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.print.PrintWithGraphics;
import com.ballydev.sds.jackpotui.print.SlipImage;
import com.ballydev.sds.jackpotui.print.SlipPrinting;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JpReportPrintDTO;
import com.ballydev.sds.jackpotui.util.PadderUtil;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;

public class JackpotReportTextController extends SDSBaseController{
	
	private JackpotReportTextComposite jackpotReportTextComposite;
	
	private long jpTotalOrgAmt  = 0l;
	private long jpTotalNetAmt  = 0l;
	private long jpTotalTaxAmt  = 0l;
	private long jpTotalVoidedAmt  = 0l;
	private List<JackpotReportsDTO> jpListDTO;
	
	private Date fromDate;
	
	private Date toDate;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	

	public JackpotReportTextController(TopMiddleComposite jackpotMiddleComposite, int style,
			SDSForm sdsForm,SDSValidator validator,List<JackpotReportsDTO>  jpListDTO,long totOrgAmt, long totNetAmt, long totTaxAmt, long totVoidAmt, Date fromDate, Date toDate) throws Exception {
		super(sdsForm, validator);
		this.jpTotalOrgAmt = totOrgAmt;
		this.jpTotalNetAmt = totNetAmt;
		this.jpTotalTaxAmt = totTaxAmt;
		this.jpTotalVoidedAmt = totVoidAmt;
		this.jpListDTO = jpListDTO;
		this.fromDate = fromDate;
		this.toDate = toDate;
		jackpotReportTextComposite = new JackpotReportTextComposite(jackpotMiddleComposite,style);
		TopMiddleController.setCurrentComposite(jackpotReportTextComposite);
		this.registerEvents(jackpotReportTextComposite);
		this.registerCustomizedListeners(jackpotReportTextComposite);
		if (jpListDTO != null && jpListDTO.size() > 0) {
			displayReport(jackpotReportTextComposite, jpListDTO);
		}
		populateScreen(jackpotReportTextComposite);
	}

	@Override
	public Composite getComposite() {
		return jackpotReportTextComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		JackpotSlipReportListener listener = new JackpotSlipReportListener();
		jackpotReportTextComposite.getButtonComposite().getBtnPrint().addMouseListener(listener);
		jackpotReportTextComposite.getButtonComposite().getBtnPrint().getTextLabel().addTraverseListener(this);
		jackpotReportTextComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		jackpotReportTextComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
		jackpotReportTextComposite.getBtnForward().addMouseListener(listener);
		jackpotReportTextComposite.getBtnForward().addTraverseListener(this);
		jackpotReportTextComposite.getBtnBackward().addMouseListener(listener);
		jackpotReportTextComposite.getBtnBackward().addTraverseListener(this);
		jackpotReportTextComposite.getBtnUp().addMouseListener(listener);
		jackpotReportTextComposite.getBtnUp().addTraverseListener(this);
		jackpotReportTextComposite.getBtnDown().addMouseListener(listener);
		jackpotReportTextComposite.getBtnDown().addTraverseListener(this);
	}

	public void widgetSelected(SelectionEvent e) {}
	

	/**
	 * Method to display the jp slip report
	 * @param jpSlipReportComposite
	 * @param listJackpotReport
	 */
	public void displayReport(JackpotReportTextComposite jackpotRptTxtComposite,
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
				totalHPJPAmount = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(String.valueOf(jpTotalNetAmt)));
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
			
			buffer.append(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil.getCurrentServerDate()) + "\n \n");
		    jackpotRptTxtComposite.getTxtReportsDisplay().setText(
							buffer.toString());
		} catch (Exception e) {
			log.error("Exception while displaying the report values"+e.getMessage());
		}
	}
	
	private class JackpotSlipReportListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {

			Control control = (Control) e.getSource();
			if (control instanceof SDSImageLabel) {
			if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
				log.debug("Jackpot Rpt scrn was cancelled");
				JackpotUIUtil.disposeCurrentMiddleComposite();
				CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
				initialScreen.callReportJPFirstScreen();
			}else if (((SDSImageLabel) control).getName().equalsIgnoreCase("print")) {

				
				if(jpListDTO!=null && (jpTotalNetAmt!=0 || jpTotalVoidedAmt!=0)){
					log.info("BEFORE CALLING THE SlipImage");
											
					JpReportPrintDTO printDTO = new JpReportPrintDTO();
					
					if(jpListDTO.get(0).getSlotAttendantId()!=null && jpListDTO.get(0).getSlotAttendantId().length()>1){									
						printDTO.setSlotAttendantId(StringUtil.padLeftZeros(jpListDTO.get(0).getSlotAttendantId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
						log.info("Emp Id : "+printDTO.getSlotAttendantId());		
					}						
					if(jpListDTO.get(0).getSlotAttendantFirstName()!=null){
						log.info("Emp First Name : "+jpListDTO.get(0).getSlotAttendantFirstName());
						printDTO.setSlotAttendantName(jpListDTO.get(0).getSlotAttendantFirstName());
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
					printDTO.setFromDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(fromDate));
					printDTO.setToDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(toDate));
					printDTO.setPrinterSchema(jpListDTO.get(0).getPrinterSchema());
					printDTO.setSlipSchema(jpListDTO.get(0).getSlipSchema());					
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
							image = slipImage.getLaserPrinterImageSlipRpt(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, SlipUtil.reportSlipMap, jpListDTO);
							log.debug("Image : "+image);
							/** SEND THE SLIP IMAGE FOR PRINT */ 
							PrintWithGraphics printWithGraphics = new PrintWithGraphics();
							printWithGraphics.printReport(image);
						}else{
							log.info("Default was selected, calling getLaserPrinterImage method");
							image = slipImage.getLaserPrinterImageSlipRpt(printDTO, IAppConstants.REPORT_PRINT_SLIP_TYPE, SlipUtil.reportSlipMap, jpListDTO);
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
			

				

			} else if (control instanceof TSButtonLabel) {
				if (((TSButtonLabel) control).getName().equalsIgnoreCase(
						"btnUp")) {
					if (jackpotReportTextComposite.getTxtReportsDisplay() != null) {
						StyledText text = jackpotReportTextComposite
						.getTxtReportsDisplay();
						if(text.getTopIndex() ==0){
							jackpotReportTextComposite.getBtnUp().setImage(jackpotReportTextComposite.getDisableUpImage());
							//jackpotReportTextComposite.getBtnDown().setImage(jackpotReportTextComposite.getBtnDownImage());
						}else{
							if(text.getTopIndex() != 0){
								jackpotReportTextComposite.getBtnUp().setImage(jackpotReportTextComposite.getBtnUpImage());
							}
							jackpotReportTextComposite.getBtnDown().setImage(jackpotReportTextComposite.getBtnDownImage());
							text.setTopIndex(text.getTopIndex() - 2);
						}
						
					}
				} else if (((TSButtonLabel) control).getName()
						.equalsIgnoreCase("btnDown")) {
					if (jackpotReportTextComposite.getTxtReportsDisplay() != null) {
						StyledText text = jackpotReportTextComposite
								.getTxtReportsDisplay();
						System.out.println("Top index"+text.getTopIndex());
						int incIndex = text.getTopIndex() + 2;
						System.out.println("incIndex:"+incIndex);
						if(text.getTopIndex() != incIndex){
							text.setTopIndex(incIndex);
							if(text.getTopIndex() != incIndex){
								if(text.getTopIndex() != 0){
									jackpotReportTextComposite.getBtnUp().setImage(jackpotReportTextComposite.getBtnUpImage());
								}
								jackpotReportTextComposite.getBtnDown().setImage(jackpotReportTextComposite.getDisableDownImage());
							}else{
								if(text.getTopIndex() != 0){
									jackpotReportTextComposite.getBtnUp().setImage(jackpotReportTextComposite.getBtnUpImage());
								}
								jackpotReportTextComposite.getBtnDown().setImage(jackpotReportTextComposite.getBtnDownImage());
							}
							
						}else{
							if(text.getTopIndex() != 0){
								jackpotReportTextComposite.getBtnUp().setImage(jackpotReportTextComposite.getBtnUpImage());
							}
							jackpotReportTextComposite.getBtnDown().setImage(jackpotReportTextComposite.getDisableDownImage());
						}
					}
				}else if (((TSButtonLabel) control).getName()
						.equalsIgnoreCase("btnForward")) {
					if (jackpotReportTextComposite.getTxtReportsDisplay() != null) {
						StyledText text = jackpotReportTextComposite
								.getTxtReportsDisplay();
						System.out.println("Horiz index"+text.getHorizontalIndex());
						int incIndex = text.getHorizontalIndex() + 2;
						System.out.println("incIndex:"+incIndex);
						if(text.getHorizontalIndex() != incIndex){
							text.setHorizontalIndex(incIndex);
							if(text.getHorizontalIndex() != incIndex){
								jackpotReportTextComposite.getBtnForward().setImage(jackpotReportTextComposite.getDisableNextImage());
								if(text.getHorizontalIndex() != 0){
									jackpotReportTextComposite.getBtnBackward().setImage(jackpotReportTextComposite.getBtnPrevImage());
								}	
							}else{
								jackpotReportTextComposite.getBtnForward().setImage(jackpotReportTextComposite.getBtnNextImage());
								if(text.getHorizontalIndex() != 0){
									jackpotReportTextComposite.getBtnBackward().setImage(jackpotReportTextComposite.getBtnPrevImage());
								}
							}
							
						}else{
							jackpotReportTextComposite.getBtnForward().setImage(jackpotReportTextComposite.getDisableNextImage());
							if(text.getHorizontalIndex() != 0){
								jackpotReportTextComposite.getBtnBackward().setImage(jackpotReportTextComposite.getBtnPrevImage());
							}
						}
					}
				}else if (((TSButtonLabel) control).getName()
						.equalsIgnoreCase("btnBackward")) {
					if (jackpotReportTextComposite.getTxtReportsDisplay() != null) {
						
						StyledText text = jackpotReportTextComposite
								.getTxtReportsDisplay();
						System.out.println("Hori Index initial:"+text.getHorizontalIndex());
						if(text.getHorizontalIndex() == 0){
							//jackpotReportTextComposite.getBtnForward().setImage(jackpotReportTextComposite.getBtnNextImage());
							jackpotReportTextComposite.getBtnBackward().setImage(jackpotReportTextComposite.getDisablePrevImage());
						}else{
							if(text.getHorizontalIndex() != 0){
								jackpotReportTextComposite.getBtnBackward().setImage(jackpotReportTextComposite.getBtnPrevImage());
							}
							
							text.setHorizontalIndex(text.getHorizontalIndex() - 2);
							jackpotReportTextComposite.getBtnForward().setImage(jackpotReportTextComposite.getBtnNextImage());
							
						}
						
					}
				}
				
			}
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
