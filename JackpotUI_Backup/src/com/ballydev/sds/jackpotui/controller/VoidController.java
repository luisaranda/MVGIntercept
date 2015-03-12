/*****************************************************************************
 * $Id: VoidController.java,v 1.77, 2011-03-03 15:48:44Z, Subha Viswanathan$
 * $Date: 3/3/2011 9:48:44 AM$
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

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.ecash.enumconstants.AccountTransTypeEnum;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpotui.composite.VoidComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.VoidForm;
import com.ballydev.sds.jackpotui.print.PrintWithGraphics;
import com.ballydev.sds.jackpotui.print.SlipImage;
import com.ballydev.sds.jackpotui.print.SlipPrinting;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.PrintDTO;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;
import com.ballydev.sds.jackpotui.util.VoidReportDTO;
import com.barcodelib.barcode.Barcode;

/**
 * This class acts as a controller for all the events performed in the 
 * VoidComposite 
 * @author vijayrajm
 * @version $Revision: 78$
 */
public class VoidController extends SDSBaseController {

	/**
	 * VoidForm instance
	 */
	private VoidForm form;

	/** 
	 * VoidComposite instance
	 */
	private VoidComposite voidComposite;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * This variable hold the value of slot or stand number based on site config.
	 */
	private String slotOrStandCurrentValue=null;
			
	/**
	 * Report Date Format
	 */
	private String REPORT_DATE_FORMAT = "dd-MMM-yy HH:mm:ss";
	
	
	/**
	 * List that holds the pending jp details
	 */
	private List<JackpotDTO> pendingJackpotDTOList =null;
		
	/**
	 * VoidController constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public VoidController(Composite parent, int style, VoidForm form,
			SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createVoidComposite(parent, style, getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(voidComposite);
		log.info("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();		
		super.registerEvents(voidComposite);
		form.addPropertyChangeListener(this);
		setDefaultValue();
		FocusUtility.setTextFocus(voidComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(voidComposite);
	}

	/**
	 * This method is used to perform action for widgetselected event 
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {}

	/**
	 * This method is used to register customized listeners
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		VoidMouseListener listener = new VoidMouseListener();
		voidComposite.getSeqRadioImage().addMouseListener(listener);
		voidComposite.getSeqRadioImage().addTraverseListener(this);
		voidComposite.getStandSlotRadioImage().addMouseListener(listener);
		voidComposite.getStandSlotRadioImage().addTraverseListener(this);
		voidComposite.getButtonComposite().getBtnVoid().addMouseListener(listener);
		voidComposite.getButtonComposite().getBtnVoid().getTextLabel().addTraverseListener(this);
		
	}

	/**
	 * A method to create VoidComposite
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createVoidComposite(Composite p, int s, boolean[] paramFlags) {
		voidComposite = new VoidComposite(p, s, paramFlags);
	}

	/**
	 * A method to get the boolean values based on the configuration parameters
	 * enabled/disabled
	 * @return paramFlags
	 */
	public boolean[] getSiteConfigurationParameters() {
		boolean[] paramFlags = new boolean[2];
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
			paramFlags[0] = true;
		} else {
			paramFlags[0] = false;
		}
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
			paramFlags[1] = true;
		} else {
			paramFlags[1] = false;
		}
		return paramFlags;
	}
	
	private void setDefaultValue(){
		if(voidComposite.getRadioButtonControl() != null){
			voidComposite.getRadioButtonControl().setSelectedButton(voidComposite.getSeqRadioImage());
		}
		
	}

    private void processJackpotAdjustment() {
		try {
			
			log.info("BEFORE PROCESS JP ADJUSTMENT IS CALLED");
			log.info("B4 marketing 's method is called:");
			log.info("Site id: "+ MainMenuController.jackpotForm.getSiteId());
			log.info("Associated card: "+ MainMenuController.jackpotForm.getAssociatedPlayerCard());
			log.info("pl card: "+ MainMenuController.jackpotForm.getPlayerCard());
			log.info("Old jp amt: "+ MainMenuController.jackpotForm.getJackpotNetAmount());
			log.info("New jp amt: "+ 0);
			log.info("Slot No: "+ MainMenuController.jackpotForm.getSlotNo());
			log.info("msg id"+ MainMenuController.jackpotForm.getMessageId());
			
			int marketingResp = 0;			
			marketingResp = JackpotServiceLocator.getService().processJackpotAdjustment(
					MainMenuController.jackpotForm.getSiteId(),
					MainMenuController.jackpotForm.getAssociatedPlayerCard(),
					MainMenuController.jackpotForm.getPlayerCard(),
					MainMenuController.jackpotForm.getJackpotNetAmount(),
					0,
					MainMenuController.jackpotForm.getSlotNo(),
					MainMenuController.jackpotForm.getMessageId());
			/*if(marketingResp > 0){
				MessageDialogUtil
				.displayTouchScreenInfoDialog(LabelLoader
						.getLabelValue(MessageKeyConstants.SUCCESS_POSTING_JACKPOT_ADJUSTMENT_PLAYER_DETAILS_TO_MARKETING));
			}*/
			log.debug("marketingResp: "+marketingResp);			
			
			log.info("BEFORE PROCESS JP ADJUSTMENT IS CALLED");
		} catch (JackpotEngineServiceException e) {
			MessageDialogUtil
					.displayTouchScreenErrorMsgDialog(LabelLoader
							.getLabelValue(""
									+ "An exception has occured while sending the jackpot adjustment details to the marketing engine"));
			
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN",e2);
			return;
		}
	}

    
    /**
     * Method to print the Jackpot Void Report
     */
    public void printJackpotVoidReport(List<JackpotDTO> pendingJackpotDTOList)
    {    	
    	log.debug("Within the printJackpotVoidReport method");
    	List<VoidReportDTO> pendingVoidJpDTOList = new ArrayList<VoidReportDTO>();
		
		if(pendingJackpotDTOList!=null && pendingJackpotDTOList.size()>0){
			log.debug("pendingJackpotDTOList is not null");
			log.info("jackpotDTOList is not null");
			for(int i=0; i<pendingJackpotDTOList.size(); i++)
			{
				JackpotDTO jackpotDTO = (JackpotDTO) pendingJackpotDTOList.get(i);
				log.info("jackpotDto value for Seq no: "+jackpotDTO.getSequenceNumber());
										
				VoidReportDTO voidReportDTO = new VoidReportDTO();
				voidReportDTO.setSeq(IAppConstants.JACKPOT_SLIP_SEQUENCE_NO_PREFIX + String.valueOf(jackpotDTO.getSequenceNumber()));
				voidReportDTO.setDate(com.ballydev.sds.framework.util.DateUtil.getDateString(jackpotDTO.getTransactionDate(), REPORT_DATE_FORMAT));
				voidReportDTO.setSlotNo(jackpotDTO.getAssetConfigNumber());
				voidReportDTO.setStandNo(jackpotDTO.getAssetConfigLocation());
				if(jackpotDTO.getSlotDenomination()!=0){
					voidReportDTO.setDenom(JackpotUIUtil.getFormattedAmounts(String.valueOf(ConversionUtil.centsToDollar(jackpotDTO.getSlotDenomination()))));
				}	
				if(jackpotDTO.getOriginalAmount()!=0){
					voidReportDTO.setAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() 
							+ JackpotUIUtil.getFormattedAmounts(
									String.valueOf(ConversionUtil.twoPlaceDecimalOf(ConversionUtil.centsToDollar(jackpotDTO.getOriginalAmount())))));
				}				
				pendingVoidJpDTOList.add(voidReportDTO);
			}
			HashMap<String,String> hashMap = new HashMap<String,String>();
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_HEADING, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_HEADING));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_SEQ_NO, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_SEQ_NO));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_DATE, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_DATE));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_TIME, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_TIME));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_SLOT_NO, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_SLOT_NO));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_STAND_NO, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_STAND_NO));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_DENOM, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_DENOM));
			hashMap.put(LabelKeyConstants.PRINT_JPVOID_AMOUNT, LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JPVOID_AMOUNT));
			
			log.info("pendingVoidJpDTOList : "+pendingVoidJpDTOList.size());
			SlipImage slipImage = new SlipImage();
			String reportStr = slipImage.getVoidJackpotReportSlip(pendingVoidJpDTOList, hashMap);
			
			log.info("Slip Image String : "+reportStr);
			
//			SlipPrinting slip = new SlipPrinting();
//			slip.printSlip(image);
			
			/** SEND THE SLIP IMAGE FOR PRINT */ 
			PrintWithGraphics printWithGraphics = new PrintWithGraphics();
			try {
				printWithGraphics.printReport(reportStr);
			} catch (Exception e) {
				log.error("Exception when printing the void report");
			}
			
			log.info("After printin the slip ");
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_REPORT_PRINT_SUCCESS));
		}		
		
		if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
			TopMiddleController.getCurrentComposite().dispose();
		}
		CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
		callInitialScreenUtil.callVoidJPFirstScreen();
    }

	/**
	 * This method returns VoidComposite
	 */
	@Override
	public Composite getComposite() {
		return voidComposite;
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
	 * VoidMouseListener class
	 *
	 */
	private class VoidMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {			
			
		}

		public void mouseDown(MouseEvent e) {

			try {
				Control control = (Control) e.getSource();

				if (!(control instanceof TSButtonLabel)
						&& !(control instanceof SDSImageLabel)) {
					return;
				}
				
				if(control instanceof SDSImageLabel){
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("void")) {
						
						populateForm(voidComposite);
										
						if(form.getSlotNo()!=null)
						{
							slotOrStandCurrentValue=form.getSlotNo().trim();
						}
						
						List<String> fieldNames = new ArrayList<String>();
						
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
						}
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
						}
						fieldNames.add(FormConstants.FORM_SEQUENCE_NO);
						
						if(!form.getSlotNo().equalsIgnoreCase(IAppConstants.ALL_CONSTANT) || form.getSlotNo().length()!=1){
							fieldNames.add(FormConstants.FORM_SLOT_NO);
						}			
						boolean validate = validate("VoidForm",
								fieldNames, form,
								voidComposite);				
						
						/* Populating the form with screen values */
					
						if(!validate){
							return;
						}
						SessionUtility sessionUtility = new SessionUtility();
						if(form.getSequenceNumber().length() == 0 && slotOrStandCurrentValue.length()==0)
						{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
							voidComposite.getTxtSequenceNo().forceFocus();
							return;
						}
						else if(form.getSequenceNumber().length()>0 && slotOrStandCurrentValue.length()>0)
						{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
							voidComposite.getTxtSequenceNo().forceFocus();					
							return;
						}
						
						//PROCESS EMPLOYEE ID VALIDATION
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
								&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
								ProgressIndicatorUtil.openInProgressWindow();
								
								try{							
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmpPassword(), MainMenuController.jackpotForm.getSiteId());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
														|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
											voidComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
		                                        
			                                	ProgressIndicatorUtil.closeInProgressWindow();
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
												voidComposite.getTxtEmployeePwd().setText("");
												voidComposite.getTxtEmployeePwd().forceFocus();
												return;
										}
										else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
										{
											log.info("User does not contain the PROCESS VOID JP function");									
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeeId().forceFocus();
											return;																
										}
										else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.debug("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeeId().forceFocus();
											return;				
										}
										else{								
											MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
											MainMenuController.jackpotForm.setVoidEmployeeId(form.getEmployeeId());
										}								
									}
									else{
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
										voidComposite.getTxtEmployeePwd().setText("");
										voidComposite.getTxtEmployeeId().forceFocus();
										return;
									}
								}catch(Exception ex){
									JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
									log.error("Exception while checking the user authentication",ex);
									return;
								}
								finally{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
						}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
								ProgressIndicatorUtil.openInProgressWindow();						
								try{						
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.jackpotForm.getActorLogin(), form.getEmpPassword(), MainMenuController.jackpotForm.getSiteId());
									log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
									log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
									if(userDtoEmpPasswordChk!=null){
										/*if(userDtoEmpPasswordChk.getUserName()==null)
										{
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;
										}*/
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                        
			                                	ProgressIndicatorUtil.closeInProgressWindow();
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
												voidComposite.getTxtEmployeePwd().setText("");
												voidComposite.getTxtEmployeePwd().forceFocus();
												return;
										}
										else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
										{
											log.info("User does not contain the PROCESS VOID JP function");									
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;																
										}
										else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.debug("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeeId().forceFocus();
											return;					
										}
										else{								
											MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(MainMenuController.jackpotForm.getActorLogin());
											MainMenuController.jackpotForm.setVoidEmployeeId(MainMenuController.jackpotForm.getActorLogin());
										}	
									}							
									else{
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										voidComposite.getTxtEmployeePwd().setText("");
										voidComposite.getTxtEmployeePwd().forceFocus();
										return;
									}
								}catch(Exception ex){
									JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
									log.error("Exception while checking the employee password with framework",ex);
									return;
								}
								finally{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
						}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							ProgressIndicatorUtil.openInProgressWindow();
					
							try{								
								log.info("Called framework's authenticate user method");							
								UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId());
								if(userDTOEmpIdCheck!=null){							
									log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
									if (userDTOEmpIdCheck.isErrorPresent() && userDTOEmpIdCheck.getMessageKey() != null
											&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDTOEmpIdCheck.getMessageKey())||
													IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDTOEmpIdCheck.getMessageKey())||
													IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT.equals(userDTOEmpIdCheck.getMessageKey()))) {
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
									{
										log.info("User does not contain the PROCESS VOID JP function");									
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;																
									}
									else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
											&& !userDTOEmpIdCheck.isSignedOn())
									{
										log.debug("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;				
									}								
									else{		
										MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										MainMenuController.jackpotForm.setVoidEmployeeId(form.getEmployeeId());
									}
								}
								else{
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									voidComposite.getTxtEmployeeId().forceFocus();
									return;
								}															
							}catch(Exception ex){
								JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
								log.error("Exception while checking the employee id with framework",ex);
								return;
							}
							finally
							{
								ProgressIndicatorUtil.closeInProgressWindow();
							}
						}else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
						{
							log.info("User does not contain the PROCESS VOID JP function");									
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
							voidComposite.getTxtSequenceNo().forceFocus();
							return;																
						}				
						
						if(!form.getSlotNo().equalsIgnoreCase(IAppConstants.ALL_CONSTANT) && form.getSlotNo().length()>0)
						{
							try
							{			
								JackpotAssetInfoDTO jackpotAssetInfoDTO = null;
								log.info("Calling the getJackpotAssetInfo web method");
								log.info("*******************************************");
								log.info("Slot no: "+form.getSlotNo());
								log.info("JackpotAssetParamType.ASSET_CONFIG_NUMBER: "+JackpotAssetParamType.ASSET_CONFIG_NUMBER);						
								log.info("*******************************************");
								jackpotAssetInfoDTO = JackpotServiceLocator.getService().getJackpotAssetInfo( StringUtil.padAcnfNo(form.getSlotNo()),
										JackpotAssetParamType.ASSET_CONFIG_NUMBER, MainMenuController.jackpotForm.getSiteId());
								
								log.info("Values returned after calling the getJackpotAssetInfo web method");
								log.info("*******************************************");
								log.info("JackpotAssetInfoDTO values returned: "+jackpotAssetInfoDTO.toString());					
								log.info("*******************************************");
								if (jackpotAssetInfoDTO != null) {
									if (jackpotAssetInfoDTO.getErrorCode() != 0) {
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
										voidComposite.getTxtSlotOrLocNo().forceFocus();
										return;
									}
									if (jackpotAssetInfoDTO.getAssetConfigNumber() == null) {
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
										log.info("Invalid slot no");
										voidComposite.getTxtSlotOrLocNo().forceFocus();
										return;
									}
									if (jackpotAssetInfoDTO.getSiteId() != null
											&& !jackpotAssetInfoDTO.getSiteId().equals(
													MainMenuController.jackpotForm.getSiteId())) {
										log.info("Slot does not belong to this site");
										MessageDialogUtil
												.displayTouchScreenErrorMsgDialog(LabelLoader
														.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
										voidComposite.getTxtSlotOrLocNo().setFocus();
										return;
									} else {
										MainMenuController.jackpotForm.setSlotNo(form.getSlotNo());
									}
								}
							}catch (JackpotEngineServiceException ex) {
								log.info("Error while calling asset to check if slot no entered is correct");
								log.error(ex);
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(ex,LabelLoader.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
								voidComposite.getTxtSlotOrLocNo().setFocus();
								return;
							}catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}
						}
						String statusDesc = null;
						JackpotDTO jpVoidStatusDTO = null;
						log.info("Seq no :"+form.getSequenceNumber()+"test");
						log.info("Seq no length: "+form.getSequenceNumber().length());
						
						if(form.getSequenceNumber().trim().length()>=1){
							try {
								log.info("Calling the getJackpotStatusForVoid web method");
								log.info("*******************************************");
								log.info("Sequence no: "+form.getSequenceNumber());
								log.info("Site id: "+MainMenuController.jackpotForm.getSiteId());						
								log.info("*******************************************");
								/*statusDesc = JackpotServiceLocator.getService().getJackpotStatus(
									Long.parseLong(form.getSequenceNumber()), MainMenuController.jackpotForm.getSiteId());*/
								jpVoidStatusDTO = JackpotServiceLocator.getService().getJackpotStatusForVoid(
										Long.parseLong(form.getSequenceNumber()), MainMenuController.jackpotForm.getSiteId());
														
								short statusFlagId = jpVoidStatusDTO.getStatusFlagId();
								if(statusFlagId != 0) {
									if(statusFlagId == ILookupTableConstants.PROCESSED_STATUS_ID){
										statusDesc = ILookupTableConstants.PROCESSED_ST_DESC;
									}else if(statusFlagId == ILookupTableConstants.CHANGE_STATUS_ID){
										statusDesc = ILookupTableConstants.CHANGE_ST_DESC;
									}else if(statusFlagId == ILookupTableConstants.REPRINT_STATUS_ID){
										statusDesc = ILookupTableConstants.REPRINT_ST_DESC;
									}else if(statusFlagId == ILookupTableConstants.VOID_STATUS_ID){
										statusDesc = ILookupTableConstants.VOID_ST_DESC;
									}else if(statusFlagId == ILookupTableConstants.PENDING_STATUS_ID){
										statusDesc = ILookupTableConstants.PENDING_ST_DESC;
									} else if(statusFlagId == ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID){
										statusDesc = ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC;
									} else if(statusFlagId == ILookupTableConstants.PRINTED_STATUS_ID) {
										statusDesc = ILookupTableConstants.PRINTED_ST_DESC; // FOR CASHIER DESK
									}
								}
								if(log.isInfoEnabled()) {
									log.info("Values returned after calling the getJackpotStatusForVoid web method");
									log.info("*******************************************");
									log.info("Status Description: " + statusDesc);					
									log.info("*******************************************");
								}
							}catch (JackpotEngineServiceException e1) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e1, e1.getMessage());
								log.error("Exception while checking the getJackpotStatusForVoid web method",e1);
								return;
							}catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}
							if(statusDesc==null){
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
								voidComposite.getTxtSequenceNo().forceFocus();
								return;
							} else {
								if (statusDesc.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC)
										|| statusDesc.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC)
										|| statusDesc.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC)
										|| (statusDesc.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC)) // FOR CASHIER DESK
										) {				
									
									// add new web method here to get the gaming day for the slip seq and the current slip gaming day
									String currentGamingDate = null;
									String slipTransactionDate = null;
									try{
										List<String> strDateLst = JackpotServiceLocator.getService()
												.getGamingDayInfoForSlipSeq(
														MainMenuController.jackpotForm.getSiteId(),
														Long.parseLong(form.getSequenceNumber()));
										log.debug("strDateLst: "+strDateLst);
										if(strDateLst!=null && strDateLst.size()>=1){									
											currentGamingDate = strDateLst.get(0);
											if(strDateLst.size()==2){
												slipTransactionDate = strDateLst.get(1);
											}
										}
										
									}catch (JackpotEngineServiceException e1) {
										JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
										handler.handleException(e1, e1.getMessage());
										log.error("Exception while checking the getGamingDayInfoForSlipSeq web method",e1);
										return;
									}catch (Exception e2) {
										JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
										handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
										log.error("SERVICE_DOWN",e2);
										return;
									}
									
									log.info("currentGamingDate: "+currentGamingDate);
									log.info("slipTransactionDate: "+slipTransactionDate);
									if (slipTransactionDate == null
											|| currentGamingDate.equalsIgnoreCase(slipTransactionDate)) {
										
										// VALIDATING CASHLESS ACCOUNT NUMBER WITH ECASH ACCOUNT DETAILS
										if (MainMenuController.jackpotSiteConfigParams.get(
												ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT)
												.equalsIgnoreCase("Yes")
												&& jpVoidStatusDTO.getPostToAccounting().equalsIgnoreCase("Y")
												&& jpVoidStatusDTO.getAccountNumber() != null
												&& Long.parseLong(jpVoidStatusDTO.getAccountNumber()) > 0) {
											CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator
													.getService().isJackpotOperationAllowed(
															jpVoidStatusDTO.getAccountNumber(),
															MainMenuController.jackpotForm.getSiteId(),
															AccountTransTypeEnum.VOIDJACKPOT.getTransId());
											if (!cashlessAccountDTO.isSuccess()) {
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(
														LabelLoader.getLabelValue(MessageKeyConstants.JKPT_ACCOUNT_NUMBER)
														+ jpVoidStatusDTO.getAccountNumber()
														+ LabelLoader.getLabelValue(MessageKeyConstants.JKPT_PENDING_INVALID_ACCOUNT_NUMBER));
												voidComposite.getTxtSequenceNo().forceFocus();
												return;
											}

										}
										boolean userResp = MessageDialogUtil
												.displayTouchScreenConfirmDialog(
														LabelLoader.getLabelValue(MessageKeyConstants.CONFIRM_VOID_SEQ_MSG)
																+ " " + form.getSequenceNumber()
																+ " " + LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SLOT)
																+ ": " + StringUtil.trimAcnfNo(jpVoidStatusDTO
																		.getAssetConfigNumber())
																+ ". " + LabelLoader.getLabelValue(MessageKeyConstants.HPJP_AMOUNT)
																+ " " + JackpotUIUtil.getFormattedAmounts(ConversionUtil
																		.centsToDollar(jpVoidStatusDTO.getHpjpAmount()))
																+ ". " + LabelLoader.getLabelValue(MessageKeyConstants.NET_AMOUNT)
																+ " " + JackpotUIUtil.getFormattedAmounts(ConversionUtil
																		.centsToDollar(jpVoidStatusDTO.getJackpotNetAmount()))
																+ " ?", voidComposite);
																		
										if(userResp){
											
											/** To change the status of the slip to void */
											//boolean status=false;
											PrintDTO printDTO=new PrintDTO();
											JackpotDTO jackpotDTO=null;
											try {
												JackpotDTO voidDTO = new JackpotDTO();
												voidDTO.setSequenceNumber(Long.parseLong(form.getSequenceNumber()));
												voidDTO.setVoidEmployeeLogin(MainMenuController.jackpotForm.getVoidEmployeeId());
												voidDTO.setSiteId(MainMenuController.jackpotForm.getSiteId());
												voidDTO.setCashDeskLocation(LabelLoader.getLabelValue(LabelKeyConstants.SLIP_PROCESS_BY_SDS));
												String printerUsed = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);	
												if(printerUsed!=null){
													voidDTO.setPrinterUsed(printerUsed);
													log.debug("------------- PRINTER USED: "+printerUsed);
												}									
												log.info("Calling the postJackpotStatus web method");
												log.info("*******************************************");
												log.info("jackpotDTo values: "+voidDTO.toString());					
												log.info("*******************************************");
												jackpotDTO = JackpotServiceLocator.getService().postJackpotVoidStatus(voidDTO);
												log.info("Values returned after calling the postJackpotStatus web method");
												log.info("*******************************************");
												log.info("JackpotDTO values returned: "+jackpotDTO.toString());					
												log.info("*******************************************");
											} 
											catch (JackpotEngineServiceException e1) {
												JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
												handler.handleException(e1, e1.getMessage());
												log.error("Exception while checking the postJackpotStatus web method",e1);
												return;
											}								
											catch (Exception e2) {
												JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
												handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
												log.error("SERVICE_DOWN",e2);
												return;
											}
											if (jackpotDTO.isPostedSuccessfully()) {
												MessageDialogUtil
														.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_SUCCESS)+" "+ LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SEQUENCE)+" "+form.getSequenceNumber()+".");
												
												//SETTING THIS FLAG TO BE USED  WHEN SEND ADJUSTMENT TO MARKETING
												MainMenuController.jackpotForm.setProcessFlagId(jackpotDTO.getProcessFlagId());
																						
												JackpotFilter filter = new JackpotFilter();
												filter.setSiteId(MainMenuController.jackpotForm.getSiteId());
												
												JackpotDTO processedDTO = null;
												try{
													if(log.isInfoEnabled()) {
														log.info("Calling the getVoidJackpotSlipDetails web method");
														log.info("*******************************************");
														log.info("Sequence no: "+form.getSequenceNumber());
														log.info("Site id: "+filter.getSiteId());	
														log.info("*******************************************");
													}
													processedDTO = JackpotServiceLocator.getService()
															.getVoidJackpotSlipDetails(
																	Long.parseLong(form.getSequenceNumber()),
																	filter.getSiteId());
													if(log.isInfoEnabled()) {
														log.info("Values returned after calling the getVoidJackpotSlipDetails web method");
														log.info("*******************************************");
														log.info("JackpotDTO values returned: "+processedDTO.toString());					
														log.info("*******************************************");
													}
													UserDTO userDTO = sessionUtility.getUserDetails(MainMenuController.jackpotForm.getVoidEmployeeId(), MainMenuController.jackpotForm.getSiteId());
													if(userDTO!=null){
														processedDTO.setEmployeeName(userDTO.getFirstName() +" "+ userDTO.getLastName());								
													}
													processedDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getVoidEmployeeId());								
													printDTO = SlipUtil.setPrintDTOValues(processedDTO);								
												}catch (JackpotEngineServiceException e1) {
													JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
													handler.handleException(e1, e1.getMessage());
													log.error("Exception while checking the getVoidJackpotSlipDetails web method",e1);
													return;
												}catch (Exception e2) {
													JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
													handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
													log.error("SERVICE_DOWN",e2);
													return;
												}
												
												if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PRINT_VOID_SLIP_FOR_VOID_COMMAND).equalsIgnoreCase("yes")){
													SlipImage slipImage = new SlipImage();
													String image = null;
													try {				
														if(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(IAppConstants.EPSON_PRINTER_SUBSTRING)){
															log.debug("Epson printer was selected, calling buildSlipImage method");					
															image = slipImage.buildSlipImage(printDTO, IAppConstants.VOID_PRINT_SLIP_TYPE, SlipUtil.voidSlipMap,
																	0, processedDTO.getJackpotNetAmount());
															SlipPrinting slipPrinting = new SlipPrinting();
															slipPrinting.printSlip(image);
														}else if(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).contains(IAppConstants.LASER_JET_PRINTER_SUBSTRING)){
															log.info("Laser Jet printer was selected, calling getLaserPrinterImage method");
															image = SlipImage.getLaserPrinterImage(printDTO, IAppConstants.VOID_PRINT_SLIP_TYPE, SlipUtil.voidSlipMap,
																	0, processedDTO.getJackpotNetAmount());
															PrintWithGraphics printWithGraphics = new PrintWithGraphics();	
															Image barcodeImg = null;
															if(MainMenuController.jackpotForm.isPrintBarcode()){
																Barcode barcode = JackpotServiceLocator.getService().createBarcode(printDTO.getSiteId(), processedDTO.getJackpotNetAmount(),
																		processedDTO.getSequenceNumber(), processedDTO.getBarEncodeFormat());
																String initialBarFile = processedDTO.getBarEncodeFormat() + ".jpg";
																barcode.createBarcodeImage(initialBarFile);

																File barcodeFile = new File(initialBarFile);
																barcodeImg = ImageIO.read(barcodeFile); 
															}													
															printWithGraphics.printTextImage(image, barcodeImg, null);
														} else {
															log.info("Default was selected, calling getLaserPrinterImage method");
															image = SlipImage.getLaserPrinterImage(printDTO, IAppConstants.VOID_PRINT_SLIP_TYPE, SlipUtil.voidSlipMap,
																	0, processedDTO.getJackpotNetAmount());
															PrintWithGraphics printWithGraphics = new PrintWithGraphics();	
															Image barcodeImg = null;
															if(MainMenuController.jackpotForm.isPrintBarcode()){
																Barcode barcode = JackpotServiceLocator.getService().createBarcode(printDTO.getSiteId(), processedDTO.getJackpotNetAmount(),
																		processedDTO.getSequenceNumber(), processedDTO.getBarEncodeFormat());
																String initialBarFile = processedDTO.getBarEncodeFormat() + ".jpg";
																barcode.createBarcodeImage(initialBarFile);

																File barcodeFile = new File(initialBarFile);
																barcodeImg = ImageIO.read(barcodeFile); 
															}													
															printWithGraphics.printTextImage(image, barcodeImg, null);
														}
													} catch (Exception e1) {
														e1.printStackTrace();
														log.error(e);
													}
													
												}
												
												log.info("BEFORE SENDING AUDIT TRAIL INFO FOR VOID");							
												try {
													if(processedDTO.getHpjpAmountRounded()!=0){
														Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME,
																LabelLoader.getLabelValue(LabelKeyConstants.VOID_JACKPOT_SCREEN),
																LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_JP_FIELD),
															String.valueOf(ConversionUtil.centsToDollar(processedDTO.getHpjpAmountRounded())),
															String.valueOf(0),
															LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)+": "+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()+" ,"
																+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "+processedDTO.getAssetConfigNumber()+" ,"
																+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO)+": "+form.getSequenceNumber(),
															EnumOperation.VOID_JACKPOT , processedDTO.getAssetConfigNumber());
													}else{
														Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME,
																LabelLoader.getLabelValue(LabelKeyConstants.VOID_JACKPOT_SCREEN),
																LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_JP_FIELD),
															String.valueOf(ConversionUtil.centsToDollar(processedDTO.getHpjpAmount())),
															String.valueOf(0),
															LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)+": "+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()+" ,"
																+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "+processedDTO.getAssetConfigNumber()+" ,"
																+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO)+": "+form.getSequenceNumber(),
															EnumOperation.VOID_JACKPOT , processedDTO.getAssetConfigNumber());
													}
												} catch (Exception ex) {
													log.error("Exception when sending audit trail info",ex);
												}
												log.info("AFTER SENDING AUDIT TRAIL INFO");
												//VALUES ARE SET TO JACKPOT FORM - TO BE USED WHEN SENDING (PT11) JP ADJUSTMENT TO MARKETING
												if(processedDTO.getAssetConfigNumber()!=null) {
													MainMenuController.jackpotForm.setSlotNo(processedDTO.getAssetConfigNumber());
												}
												if(processedDTO.getAssociatedPlayerCard()!=null) {
													MainMenuController.jackpotForm.setAssociatedPlayerCard(processedDTO.getAssociatedPlayerCard());
												}
												if(processedDTO.getPlayerCard()!=null){
													MainMenuController.jackpotForm.setPlayerCard(processedDTO.getPlayerCard());											
												}							
												if(processedDTO.getMessageId()!=0) {
													MainMenuController.jackpotForm.setMessageId(processedDTO.getMessageId());
												}
												if(processedDTO.getJackpotNetAmount()!=0) {
													MainMenuController.jackpotForm.setJackpotNetAmount(processedDTO.getJackpotNetAmount());
												}									
												
												//BY DEFAULT PT 11 SHOULD BE SENT FOR A VOID TRANSACTION
												///DO NOT SEND FOR CANCEL CREDIT JPS
												if(processedDTO.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
													processJackpotAdjustment();
												}else {
													log.info("Adjustment is NOT SENT to Marketing AS CCJP");
												}												
																						
												form.setSequenceNumber("");
												CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
												callInitialScreenUtil.callVoidJPFirstScreen();							
												
											} else {
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_FAILED));
											}									
										}else{
											voidComposite.getTxtSequenceNo().setFocus();
											return;
										}
									}else if(slipTransactionDate!=null){
										log.debug("Str slip processed gaming day: "+DateUtil.getStringDate(DateUtil.getDateFromString(slipTransactionDate, "yyyy-MM-dd").getTime()));
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+" "
												+ form.getSequenceNumber() + " "+LabelLoader.getLabelValue(MessageKeyConstants.WAS_PRINTED_ON)+" "
												+ DateUtil.getStringDate(DateUtil.getDateFromString(slipTransactionDate, "yyyy-MM-dd").getTime())  +" " +LabelLoader.getLabelValue(MessageKeyConstants.SLIP_CANNOT_BE_VOIDED_TODAY));
										voidComposite.getTxtSequenceNo().forceFocus();
										return;
									}
//									}else {
//										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_FAILED));
//									}
								
									
									
								} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.VOID_ST_DESC)) {
								MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JP_SLIP_ALREADY_VOIDED));
								voidComposite.getTxtSequenceNo().forceFocus();
								return;
								}else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC)) {
									MessageDialogUtil
									.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_CREDIT_KEYED_OFF_CANT_VOID));
									voidComposite.getTxtSequenceNo().forceFocus();
									return;
								}
								else {
									MessageDialogUtil
											.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
									voidComposite.getTxtSequenceNo().forceFocus();
									return;
								}
							}					
						}else if(form.getSlotNo().equalsIgnoreCase(IAppConstants.ALL_CONSTANT)){
							
							boolean allUserResp = MessageDialogUtil.displayTouchScreenConfirmDialogOnActiveShell(LabelLoader.getLabelValue(MessageKeyConstants.CONFIRM_MSG_TO_VOID_PENDING_JPS));
										
							if(allUserResp){
								log.debug("Void all pending jackpot slips");
								
								try{
								log.info("Calling the voidAllPendingJackpotSlips web method");
								log.info("*******************************************");
								log.info("Site Id: "+MainMenuController.jackpotForm.getSiteId());
								log.info("Void Emp Id: "+form.getEmployeeId());
								log.info("*******************************************");
								String printerUsed = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);						
								pendingJackpotDTOList = JackpotServiceLocator.getService().voidAllPendingJackpotSlips(MainMenuController.jackpotForm.getSiteId(), MainMenuController.jackpotForm.getVoidEmployeeId(), printerUsed, 
										MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getAssetConfigNumberUsed());
								log.info("Values returned after calling the voidAllPendingJackpotSlips web method");
								log.info("*******************************************");
								log.info("JackpotDTO values returned: "+pendingJackpotDTOList.toString());
								log.info("*******************************************");
							}catch (JackpotEngineServiceException e1) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e1, e1.getMessage());
								log.error("Exception while checking the voidAllPendingJackpotSlips web method",e1);
								return;
							}
							catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}
							if(pendingJackpotDTOList!=null && pendingJackpotDTOList.size()>0){
								log.info("Voiding of pending slips was a success");
								//MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.PENDING_SLIPS_HAVE_BEEN_VOIDED_SUCCESSFULLY_PRINT_REPORT));
								printJackpotVoidReport(pendingJackpotDTOList);
								/** COMMENTED - NEEDS CONFIRMATION */
								//processJackpotAdjustment();
								
								log.info("BEFORE SENDING AUDIT TRAIL INFO FOR VOID RPT on ALL");
								try {
									for(int i=0; i<pendingJackpotDTOList.size(); i++)
									{
										if(pendingJackpotDTOList.get(i).getOriginalAmount()!=0){
											Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME,
													LabelLoader.getLabelValue(LabelKeyConstants.VOID_JACKPOT_SCREEN),
													LabelLoader.getLabelValue(LabelKeyConstants.ORIGINAL_AMT_FIELD),
												String.valueOf(ConversionUtil.centsToDollar(pendingJackpotDTOList.get(i).getOriginalAmount())),
												String.valueOf(0),
												LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)+": "+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()+" ,"
													+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "+pendingJackpotDTOList.get(i).getAssetConfigNumber()+" ,"
													+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO)+": "+pendingJackpotDTOList.get(i).getSequenceNumber()+" "
													+LabelLoader.getLabelValue(LabelKeyConstants.PENDING_JP_ARE_VOIDED),
												EnumOperation.VOID_JACKPOT, pendingJackpotDTOList.get(i).getAssetConfigNumber());										
										}								
									}							
								} catch (Exception ex) {
									log.error("Exception when sending audit trail info",ex);
								}
								log.info("AFTER SENDING AUDIT TRAIL INFO");																	
							}else{
								log.info("No pending records to void");
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO));
								CallInitialScreenUtil call = new CallInitialScreenUtil();
								call.callVoidJPFirstScreen();
							}
								
							}else{
								CallInitialScreenUtil call = new CallInitialScreenUtil();
								call.callVoidJPFirstScreen();
							}
						}else if(!form.getSlotNo().equalsIgnoreCase(IAppConstants.ALL_CONSTANT)){
							
							boolean userResp = MessageDialogUtil.displayTouchScreenConfirmDialogOnActiveShell(LabelLoader.getLabelValue(MessageKeyConstants.CONFIRM_MSG_TO_VOID_PENDING_JPS));
												
							if(userResp){						
								List<JackpotDTO> pendingJackpotDTOList = null;
								try{
									log.info("Calling the voidPendingJackpotSlipsForSlotNo web method");
									log.info("*******************************************");
									log.info("Slot no: "+form.getSlotNo());
									log.info("Site Id: "+MainMenuController.jackpotForm.getSiteId());
									log.info("Void Emp Id: "+form.getEmployeeId());
									log.info("*******************************************");
									String printerUsed = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
									pendingJackpotDTOList = JackpotServiceLocator.getService().voidPendingJackpotSlipsForSlotNo(StringUtil.padAcnfNo(form.getSlotNo()), MainMenuController.jackpotForm.getSiteId(), MainMenuController.jackpotForm.getVoidEmployeeId(), 
											printerUsed, MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getAssetConfigNumberUsed());
									log.info("Values returned after calling the voidPendingJackpotSlipsForSlotNo web method");
									log.info("*******************************************");
									log.info("JackpotDTO values returned: "+pendingJackpotDTOList.toString());					
									log.info("*******************************************");
								}catch (JackpotEngineServiceException e1) {
									JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
									handler.handleException(e1, e1.getMessage());
									log.error("Exception while checking the voidPendingJackpotSlipsForSlotNo web method",e1);
									return;
								}catch (Exception e2) {
									JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
									handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
									log.error("SERVICE_DOWN",e2);
									return;
								}
								
								if(pendingJackpotDTOList!=null && pendingJackpotDTOList.size()>0){
									log.info("Voiding of pending slips was a success");
									//MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.PENDING_SLIPS_HAVE_BEEN_VOIDED_SUCCESSFULLY_PRINT_REPORT));
									printJackpotVoidReport(pendingJackpotDTOList);
									/** COMMENTED - NEEDS CONFIRMATION */
									//processJackpotAdjustment();
									log.info("BEFORE SENDING AUDIT TRAIL INFO FOR VOID RPT");
									long originalNetAmt =0; 
									try {
										for(int i=0; i<pendingJackpotDTOList.size(); i++)
										{
											if(pendingJackpotDTOList.get(i).getOriginalAmount()!=0){
												originalNetAmt = pendingJackpotDTOList.get(i).getOriginalAmount();
											}								
										}
										if(originalNetAmt!=0){
										Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME,
												LabelLoader.getLabelValue(LabelKeyConstants.VOID_JACKPOT_SCREEN),
												LabelLoader.getLabelValue(LabelKeyConstants.ORIGINAL_AMT_FIELD),
											String.valueOf(ConversionUtil.centsToDollar(originalNetAmt)),
											String.valueOf(0),
											LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)+": "+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()+" ,"
												+LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "+form.getSlotNo()+" "
												+LabelLoader.getLabelValue(LabelKeyConstants.PENDING_JP_ARE_VOIDED),
											EnumOperation.VOID_JACKPOT, form.getSlotNo());
										}
									} catch (Exception ex) {
										log.error("Exception when sending audit trail info",ex);
									}
									log.info("AFTER SENDING AUDIT TRAIL INFO");
								}else{
									log.info("No pending records to void");
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO));
									CallInitialScreenUtil call = new CallInitialScreenUtil();
									call.callVoidJPFirstScreen();
								}				
								
							}else{
								CallInitialScreenUtil call = new CallInitialScreenUtil();
								call.callVoidJPFirstScreen();
							}
						}
					}			
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {	
						boolean response= false;				
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS), voidComposite);
						if(response){
							if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
								TopMiddleController.getCurrentComposite().dispose();
							}
							CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
							callInitialScreenUtil.callVoidJPFirstScreen();
						}
					} 
				}else if (control instanceof TSButtonLabel) {
					TSButtonLabel touchScreenRadioButton = (TSButtonLabel) control;
					String radioImageName = touchScreenRadioButton.getName();
					System.out.println("Radio Image Name:" + radioImageName);

					if (radioImageName.equalsIgnoreCase("slotstandradio")) {
						voidComposite.getTxtSlotOrLocNo().setEnabled(true);
						voidComposite.getTxtSequenceNo().setEnabled(false);
						voidComposite.getTxtSequenceNo().setText("");
						/*form.setSlotStandYes(true);
						form.setSequenceYes(false);*/
						
						
					} else if (radioImageName.equalsIgnoreCase("seqradio")) {
						voidComposite
						.getTxtSequenceNo().setEnabled(true);
						voidComposite
								.getTxtSlotOrLocNo()
								.setEnabled(false);
						voidComposite
						.getTxtSlotOrLocNo().setText("");
						/*form.setSequenceYes(true);
						form.setSlotStandYes(false);*/
					} 
					voidComposite
							.getRadioButtonControl().setSelectedButton(
									touchScreenRadioButton);
					MainMenuController.jackpotForm
							.setMiddleControl(touchScreenRadioButton
									.getName());
					log.info("The selected shift is "
							+ MainMenuController.jackpotForm.getMiddleControl());
					populateForm(voidComposite);				
				}
				
			} catch (Exception ex) {
				log.error(ex);
			}
		}
		public void mouseUp(MouseEvent e) {
			
		}		
	}
}
