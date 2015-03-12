package com.ballydev.sds.jackpot.keypad;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_PROGRSSIVE_BO;

import java.awt.Image;
import java.io.File;
import java.util.Calendar;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.ballydev.sds.common.IHeader;
import com.ballydev.sds.common.IRequest;
import com.ballydev.sds.common.jackpot.IJackPotSlipProcessingMessage;
import com.ballydev.sds.common.message.AssetInfo;
import com.ballydev.sds.common.message.JackpotAlertObject;
import com.ballydev.sds.common.message.SDSMessage;
import com.ballydev.sds.common.message.SystemGeneratedMessage;
import com.ballydev.sds.jackpot.constants.IAlertMessageConstants;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ICommonConstants;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.dao.JackpotDAO;
import com.ballydev.sds.jackpot.db.slip.ProcessSession;
import com.ballydev.sds.jackpot.dto.SlipInfoDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.AccountingUtil;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.jackpot.util.MessageUtil;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.progressive.bo.IProgressiveBO;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;
import com.barcodelib.barcode.Barcode;


/**
 * Processor for Keypad Jackpot process.
 * 
 * @author ranjithkumarm
 *
 */
public class KeypadJackpotProcessor implements IKeypadProcessConstants{

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	public void constructInitiationNAKObject(IJackPotSlipProcessingMessage jackPotSlipProcessingMessage){
		if (jackPotSlipProcessingMessage != null) {
			jackPotSlipProcessingMessage.setProcessSlip(false);
			jackPotSlipProcessingMessage.setPromptJackPotAmount(false);
			jackPotSlipProcessingMessage.setPromptPayLine(false);
			jackPotSlipProcessingMessage.setPromptWinningCombination(false);
			jackPotSlipProcessingMessage.setPromptCoinsPlayed(false);
			jackPotSlipProcessingMessage.setPromptShift(false);			
			jackPotSlipProcessingMessage.setPromptPouchPay(false);
			jackPotSlipProcessingMessage.setPromptCreditMeterHandPay(false);
			jackPotSlipProcessingMessage.setPromptEmployeeAuthorization(false);
			jackPotSlipProcessingMessage.setResponseToInitMessage(true);
		}
	}


	public void constructAuthorizationNAKObject(IJackPotSlipProcessingMessage jackPotSlipProcessingMessage, String errorMessage){
		log.info("Sending constructAuthorizationNAKObject response");
		if(jackPotSlipProcessingMessage!=null && errorMessage!=null && !((errorMessage.trim()+IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE)) ){
			log.info("Inside Sending constructAuthorizationNAKObject response");
			log.info("Error Message :"+errorMessage);
			jackPotSlipProcessingMessage.setErrorMessage(errorMessage);
			jackPotSlipProcessingMessage.setResponseToAuthorizationMessage(true);
		}
	}

	public void constructFinalizationNAKObject(IJackPotSlipProcessingMessage jackPotSlipProcessingMessage, String errorMessage){
		if(jackPotSlipProcessingMessage!=null && errorMessage!=null && !((errorMessage.trim()+IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE)) ){
			log.info("Inside Sending constructFinalizationNAKObject response");
			log.info("Error Message :"+errorMessage);
			jackPotSlipProcessingMessage.setResponseToFinalMessage(true);
			jackPotSlipProcessingMessage.setFinalMessage(errorMessage);
			jackPotSlipProcessingMessage.setClearJackPotFlag(false);
		}
	}

	public void processInitMessageDummy(IHeader msgObj){ 
		try{
			IRequest requestObj=(IRequest)msgObj;
			log.info("Entering to processInitMessageDummy");
			log.info("Employee Card Id Got :"+requestObj.getJackPotSlipEmployeeId());
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage=(IJackPotSlipProcessingMessage)msgObj;
			jackPotSlipProcessingMessage.setResponseToInitMessage(true);
			jackPotSlipProcessingMessage.setProcessSlip(true);
			jackPotSlipProcessingMessage.setPromptJackPotAmount(true);
			jackPotSlipProcessingMessage.setPromptPayLine(true);
			jackPotSlipProcessingMessage.setPromptWinningCombination(true);
			jackPotSlipProcessingMessage.setPromptCoinsPlayed(true);
			jackPotSlipProcessingMessage.setPromptShift(true);
			jackPotSlipProcessingMessage.setPromptPouchPay(false);
			jackPotSlipProcessingMessage.setPromptCreditMeterHandPay(false);
			jackPotSlipProcessingMessage.setPromptEmployeeAuthorization(false);
			log.info("Returning from processInitMessageDummy");

		}catch (Exception e) {
			log.error("Error in processInitMessageDummy", e);
		}
	}


	public void processAuthorizationMessageDummy(IHeader msgObj){ 
		try{
			IRequest requestObj=(IRequest)msgObj;
			log.info("Entering to processAuthorizationMessageDummy");
			log.info("Player Number Got:"+requestObj.getJackPotSlipPlayerNumber());
			log.info("Jackpot Amount Got:"+requestObj.getJackPotSlipJackPotAmount());
			log.info("PayLine Got:"+requestObj.getJackPotSlipPayLine());
			log.info("Winning Combination Got:"+requestObj.getJackPotSlipWinningCombination());
			log.info("Coins Played Got:"+requestObj.getJackPotSlipCoinsPlayed());
			log.info("Shift Got:"+requestObj.getJackPotSlipShift());
			log.info("CMHP Got :"+requestObj.getJackPotSlipCreditMeterHandPay());
			log.info("Pouch Pay Got :"+requestObj.getJackPotSlipPouchPay());
			log.info("Employee Authorization Got :"+requestObj.getJackPotSlipEmployeeAuthorization());			
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage=(IJackPotSlipProcessingMessage)msgObj;
			jackPotSlipProcessingMessage.setResponseToAuthorizationMessage(true);
			jackPotSlipProcessingMessage.setErrorMessage(new String("Test JP Process Ranjith."));
			//jackPotSlipProcessingMessage.setNumberOfPrinters(3);
			//jackPotSlipProcessingMessage.setPrinterLocations("123");

			log.info("Returning from processAuthorizationMessageDummy");

		}catch (Exception e) {
			log.error("Error in processAuthorizationMessageDummy", e);
		}
	}


	public void processFinalMessageDummy(IHeader msgObj){ 
		try{
			IRequest requestObj=(IRequest)msgObj;
			log.info("Entering to processFinalMessageDummy");
			log.info("Printer Location Selected Got :"+requestObj.getJackPotSlipPrinterLocation());
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage=(IJackPotSlipProcessingMessage)msgObj;
			jackPotSlipProcessingMessage.setResponseToFinalMessage(true);
			jackPotSlipProcessingMessage.setClearJackPotFlag(true);
			jackPotSlipProcessingMessage.setFinalMessage("Final Message Testing.");
			log.info("Returning from processFinalMessageDummy");

		}catch (Exception e) {
			log.error("Error in processFinalMessageDummy", e);
		}
	}


	/**
	 * 
	 * Processing Involves five basic stages
	 * Constructing custom request object,
	 * Validation of custom request object,
	 * Processing and Constructing custom response object,
	 * Copying the custom response to messaging object to send,
	 * Storing and updating the request and response for current session in session object.
	 * 
	 * @param msgObj
	 */
	public void processInitMessage(IHeader msgObj) {

		if (log.isInfoEnabled()) {
			log.info("Inside processInitMessage method.");
		}
		try {
			if (msgObj != null) {
				long slipReferenceId 	= 0;
				long jpOriginalAmount = 0;
				int intJackpotId 		= 0;
				String jackpotId 		= null;
				IJackPotSlipProcessingMessage jPMsg = (IJackPotSlipProcessingMessage) msgObj;

				int siteNumberToCheck = msgObj.getSiteId();
				AssetInfo assetInfoToCheck = msgObj.getAssetInfo();

				//GET SLIP REFERENCE ID AND JACKPOTID FOR THE LATEST JP REC FOR THIS SLOT AND SITEID			
//				long slipReferenceId = JackpotDAO.getLatestSlipReferenceId(assetInfoToCheck
//						.getAssetConfigNumber(), siteNumberToCheck);
				
				Object[] rtnObjAry = JackpotDAO.getLatestSlipReferenceIdJpId(assetInfoToCheck
						.getAssetConfigNumber(), siteNumberToCheck);				
				if(rtnObjAry!=null && rtnObjAry.length>0){
					slipReferenceId = (Long)rtnObjAry[0];
					jackpotId = (String)rtnObjAry[1];
					jpOriginalAmount = (Long)rtnObjAry[3];
				}else{
					log.info("Ignore KeyPad Processing Message since there is no matching Pending record");
					KeypadJackpotProcessor processor = new KeypadJackpotProcessor();
					processor.constructInitiationNAKObject((IJackPotSlipProcessingMessage)msgObj);
					return;
				}
				
				if (log.isDebugEnabled()) {
					log.debug("Jackpot Slip Reference ID : " + slipReferenceId);
					log.debug("Jackpot ID : " + jackpotId);
					log.debug("Org Amt : " + jpOriginalAmount);
				}
				
				//LOGIC CHK WHETHER PROG CONTROLLER NOTIFICATION IS ENABLED FOR CREATING THE PROG JP 
				//IF ENABLED IGNORE THE CURRENT PROG JP BY RETURNING, ELSE CARRY ON
				if(jackpotId!=null){
					intJackpotId = Integer.parseInt(jackpotId, IAppConstants.HEXA_RADIX);
					
					if(!JackpotUtil.isJackpotSlotFloorEventHandlingRequired(assetInfoToCheck.getAssetConfigNumber(), siteNumberToCheck, intJackpotId)){
						log.info("PROGRESSIVE CONTROLLER JP NOTIFICATION ENABLED - IGNORE THE KEYPAD PROCESS");	
						return;
					}
				}			

				ProcessSession sessionToVerify = SessionManagerDAO.loadSession(assetInfoToCheck.getAssetConfigNumber(), siteNumberToCheck);
				int attemptsNumberGotHere = -1;
				if (sessionToVerify != null) {
					attemptsNumberGotHere = sessionToVerify.getAttemptsNumber().intValue();
					long slipRefFromProcSession = sessionToVerify.getSlipReferenceId();
					if (slipRefFromProcSession != slipReferenceId) {
						// new jackpot, hence reset the attempts
						if (log.isDebugEnabled()) {
							log.debug("New jackpot, hence reset the attempts and update slip reference Id");
						}
						attemptsNumberGotHere = -1;
						SessionManagerDAO.deleteSession(assetInfoToCheck.getAssetConfigNumber(), siteNumberToCheck);
						sessionToVerify = null; 
					}
				}

				if (KeypadUtil.isAttemptsExceeded(attemptsNumberGotHere, siteNumberToCheck)) {
					// session attempts exceeded so returning NAK.
					if (log.isInfoEnabled()) {
						log.info("Session Attempts exceeded for this slot, so not allowing to process.");
					}
					sessionToVerify.setAttemptsNumber((attemptsNumberGotHere + 1));
					SessionManagerDAO.storeSession(sessionToVerify);
					constructInitiationNAKObject(jPMsg);
					return;
				}

				if (attemptsNumberGotHere == -1) {
					attemptsNumberGotHere = 0;// making it zero since previous
												// session doesnt exist.
				}

				InitiationRequestObject initiationRequestObject = new InitiationRequestObject();
				initiationRequestObject.setEmployeeIdCardInserted(((IRequest) msgObj)
						.getJackPotSlipEmployeeId());

				// validation to be done.
				int siteNumber = msgObj.getSiteId();
				RequestValidator requestValidator = new RequestValidator();
				ValidationResponseDTO validationResponseDTO = requestValidator.validateRequest(initiationRequestObject, siteNumber,jpOriginalAmount);
				if (!(validationResponseDTO.isValidationSuccess())) {
					if (sessionToVerify != null) {
						sessionToVerify.setAttemptsNumber((attemptsNumberGotHere + 1));
						SessionManagerDAO.storeSession(sessionToVerify);
					} else {
						insertNAKInitSession(assetInfoToCheck.getAssetConfigNumber(), siteNumberToCheck,
								attemptsNumberGotHere, slipReferenceId);
					}
					constructInitiationNAKObject(jPMsg);
					return;
				}

				InitiationResponseObject initiationResponseObject = getInitiationResponse(msgObj.getSiteId());

				String requestedFlag = KeypadUtil.copyInitiationObjectToSend(initiationResponseObject, jPMsg);
				String authAmountGotInCents = validationResponseDTO.getEmployeeAuthAmount();
				String authLevelGot = validationResponseDTO.getEmployeeAuthorizationLevel();

				if (requestedFlag != null && !((requestedFlag.trim() + IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					if (authLevelGot != null && !((authLevelGot.trim() + IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
						requestedFlag = requestedFlag + authLevelGot.trim();
					} else {
						requestedFlag = requestedFlag + "0";
					}

					if (authAmountGotInCents != null
							&& !((authAmountGotInCents.trim() + IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
						requestedFlag = requestedFlag + authAmountGotInCents.trim();
					} else {
						requestedFlag = requestedFlag + "0";
					}
				}

				// Storing in session
				AssetInfo assetInfo = msgObj.getAssetInfo();
				//int siteId = KeypadUtil.getSiteIdForNumber(siteNumber);

				if (sessionToVerify != null) {
					SessionManagerDAO.deleteSession(assetInfo.getAssetConfigNumber(), siteNumber);
				}
				ProcessSession sessionObject = new ProcessSession();
				sessionObject.setAcnfNumber(assetInfo.getAssetConfigNumber());
				sessionObject.setAcnfLocation(assetInfo.getAssetConfigLocation());
				sessionObject.setEmployeeCardNo(validationResponseDTO.getEmployeeCardNo());
				sessionObject.setEmployeeId(validationResponseDTO.getEmployeeId());
				sessionObject.setEmployeeFirstName(validationResponseDTO.getEmployeeFirstName());
				sessionObject.setEmployeeLastName(validationResponseDTO.getEmployeeLastName());
				if (requestedFlag != null && !((requestedFlag.trim() + IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					sessionObject.setRequestedFlag(requestedFlag);
				} else {
					String defaultFlag = new String("000000000");// default
																	// flag.
					sessionObject.setRequestedFlag(defaultFlag);
				}

				sessionObject.setSiteId(siteNumber);
				sessionObject.setSessionStatus(SESSION_INIT_RESPONSE);
				sessionObject.setSlipReferenceId(slipReferenceId);
				sessionObject.setAttemptsNumber((attemptsNumberGotHere + 1));
				sessionObject.setJpOriginalAmount(jpOriginalAmount);
				SessionManagerDAO.createSession(sessionObject);
			}
		} catch (Exception e) {
			log.error("Error when getting session in processInitMessage", e);
		}
	}



	public void insertNAKInitSession(String assetNumber, Integer siteId, int attemptsNumber,
			long slipReferenceId) {
		try {
			ProcessSession sessionObject = new ProcessSession();
			sessionObject.setAcnfNumber(assetNumber);
			sessionObject.setSiteId(siteId);
			sessionObject.setSlipReferenceId(slipReferenceId);

			if (attemptsNumber == -1) {
				attemptsNumber = 0;
			}

			sessionObject.setAttemptsNumber((attemptsNumber + 1));
			SessionManagerDAO.createSession(sessionObject);

		} catch (Exception e) {
			log.error("Error in insertNAKInitSession", e);
		}
	}


	/**
	 * 
	 * Processing Involves five basic stages
	 * Constructing custom request object,
	 * Validation of custom request object,
	 * Processing and Constructing custom response object,
	 * Copying the custom response to messaging object to send,
	 * Storing and updating the request and response for current session in session object.
	 * @param msgObj
	 */
	public void processAuthorizationMessage(IHeader msgObj){
		try{
			if(msgObj!=null){

				IJackPotSlipProcessingMessage jPMsg = (IJackPotSlipProcessingMessage) msgObj;	
				PromptDetailsRequestObject promptDetailsRequestObject=new PromptDetailsRequestObject();
				IRequest requestObject=(IRequest)msgObj;
				promptDetailsRequestObject.setEmployeeCrdNumberProcessing(requestObject.getJackPotSlipEmployeeId());
				promptDetailsRequestObject.setPlayerNumber(requestObject.getJackPotSlipPlayerNumber());
				promptDetailsRequestObject.setJackpotAmountOccurred(requestObject.getJackPotSlipJackPotAmount());
				promptDetailsRequestObject.setPayLine(requestObject.getJackPotSlipPayLine());
				promptDetailsRequestObject.setWinningCombination(requestObject.getJackPotSlipWinningCombination());
				promptDetailsRequestObject.setCoinsPlayed(requestObject.getJackPotSlipCoinsPlayed());
				promptDetailsRequestObject.setShiftSelected(requestObject.getJackPotSlipShift());

				if(requestObject.getJackPotSlipPouchPay()==1){
					promptDetailsRequestObject.setPouchPayEnabled(true);
				}else{
					promptDetailsRequestObject.setPouchPayEnabled(false);
				}

				promptDetailsRequestObject.setEmployeeCrdNumberAuthorizing(requestObject.getJackPotSlipEmployeeAuthorization());


//				validation to be done.
				int siteNumber=msgObj.getSiteId();
				//int siteId=KeypadUtil.getSiteIdForNumber(siteNumber);
				AssetInfo assetInfo=msgObj.getAssetInfo();		
				promptDetailsRequestObject.setAssetNumber(assetInfo.getAssetConfigNumber());
				promptDetailsRequestObject.setSiteId(siteNumber);
				ProcessSession processSession=null;
				try{
					processSession = SessionManagerDAO.loadSession(assetInfo.getAssetConfigNumber(), siteNumber);
					if (processSession != null) {
						String requestedFlag = processSession.getRequestedFlag();
						promptDetailsRequestObject.setRequestFlagFromSession(requestedFlag.trim());
					} else {
						constructAuthorizationNAKObject(jPMsg, new String("Previous Session doesnt exist."));
						log.info("Previous Session doesnt exist.");
						return;
					}
				} catch (Exception e) {
					log.error("Error when checking for session in processAuthorizationMessage", e);
				}


				RequestValidator requestValidator=new RequestValidator();
				ValidationResponseDTO validationResponseDTO=requestValidator.validateRequest(promptDetailsRequestObject, siteNumber,processSession.getJpOriginalAmount());
				if(!(validationResponseDTO.isValidationSuccess())){
					constructAuthorizationNAKObject(jPMsg, validationResponseDTO.getErrorMessage());
					log.info("Validation Failed for auth message "+validationResponseDTO.getErrorMessage());
					return;
				}	


				PrinterDetailsObject printerDetailsObject=getAuthorizationResponse();
				if(printerDetailsObject.isErrorPresent()){
					constructAuthorizationNAKObject(jPMsg, printerDetailsObject.getErrorText());

					return;
				}

				KeypadUtil.copyAuthorizationObjectToSend(printerDetailsObject, jPMsg);

				if (processSession != null) {

					InitiationResponseObject flagsRequested=KeypadUtil.getRequestedFlagStatus(processSession.getRequestedFlag());
					if(flagsRequested.isPromptEmployeeAuth()){
						processSession.setAuthEmployeeCardId(validationResponseDTO.getEmployeeIdAuth());
						processSession.setAuthEmployeeCardNo(validationResponseDTO.getEmployeeCardNoAuth());
						processSession.setAuthEmployeeFirstName(validationResponseDTO.getEmployeeFirstNameAuth());
						processSession.setAuthEmployeeLastName(validationResponseDTO.getEmployeeLastNameAuth());
					}
					if(flagsRequested.isPromptForCoinsPlayed()){
						processSession.setCoinsPlayed(promptDetailsRequestObject.getCoinsPlayed());
					}
					if(flagsRequested.isPromptJackpotAmount()){
						processSession.setJpHitAmount(promptDetailsRequestObject.getJackpotAmountOccurred());
					} else {
						processSession.setJpHitAmount(processSession.getJpOriginalAmount());
					}
					if(flagsRequested.isPromptPayLine()){
						processSession.setPayline(String.valueOf(promptDetailsRequestObject.getPayLine()));
					}
					processSession.setPlayerCard(promptDetailsRequestObject.getPlayerNumber());
					if(promptDetailsRequestObject.isPouchPayEnabled()){
						processSession.setPouchPay(1);	
					}else{
						processSession.setPouchPay(0);
					}					
					processSession.setSessionStatus(SESSION_PRINTER_DETAILS_RESPONSE);
					if(flagsRequested.isPromptShift()){
						processSession.setShift(promptDetailsRequestObject.getShiftSelected());
					}
					if(flagsRequested.isPromptWinningCombination()){
						processSession.setWinningCombination(String.valueOf(promptDetailsRequestObject.getWinningCombination()));
					}
					SessionManagerDAO.storeSession(processSession);
				}
			}
		}catch (Exception e) {
			log.error("Error in processAuthorizationMessage ",e);
		}
	}

	/**
	 * 
	 * Processing Involves five basic stages
	 * Constructing custom request object,
	 * Validation of custom request object,
	 * Processing and Constructing custom response object,
	 * Copying the custom response to messaging object to send,
	 * Storing and updating the request and response for current session in session object.
	 * @param msgObj
	 */
	public void processFinalMessage(IHeader msgObj, String cashierDeskEnabled, String enableCheckPrint) {
		try {
			if(log.isDebugEnabled()){
				log.debug("Inside processFinalMessage of KeypadJackpotProcessor");
			}
			if (msgObj != null) {
				IJackPotSlipProcessingMessage jPMsg = (IJackPotSlipProcessingMessage) msgObj;
				IRequest requestObject = (IRequest) msgObj;

				// basic validation for selected printer.
				if (requestObject.getJackPotSlipPrinterLocation() <= 0) {
					constructFinalizationNAKObject(jPMsg, new String("No Printer Selected."));
					return;
				}
				PrinterSelectionRequestObject printerSelectionRequestObject = new PrinterSelectionRequestObject();
				printerSelectionRequestObject.setPrinterSelected(requestObject.getJackPotSlipPrinterLocation());
				PrinterManager printerManager = new PrinterManager();
				String printerNameSelected = printerManager.getPrinterNameForIndex(printerSelectionRequestObject.getPrinterSelected());

				if (!((printerNameSelected != null) && !((printerNameSelected.trim() + IKeypadProcessConstants.SIGLE_SPACE)
						.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE)))) {
					constructFinalizationNAKObject(jPMsg, new String("Selected Printer not available."));
					return;
				}
				printerSelectionRequestObject.setPrinterNameSelected(printerNameSelected);
				int siteNumber = msgObj.getSiteId();
				//int siteId = KeypadUtil.getSiteIdForNumber(siteNumber);
				AssetInfo assetInfo = msgObj.getAssetInfo();

				ProcessSession processSession = null;
				try {
					processSession = SessionManagerDAO.loadSession(assetInfo.getAssetConfigNumber(), siteNumber);
					if (processSession == null) {
						// log error as session does not exist.
						constructFinalizationNAKObject(jPMsg, new String("Previous Session doesnt exist."));
						return;
					}
				} catch (Exception e) {
					log.error("Error when checking session in processFinalMessage", e);
				}

				// Need to do database operations for slip process.
				// Setting needed info on process session fields
				processSession.setAssetLineAddress(msgObj.getLineAddress());
				processSession.setAreaLongName(assetInfo.getAreaLongName());
				processSession.setPrinterSel(printerSelectionRequestObject.getPrinterSelected());
				processSession.setPrinterSelName(printerSelectionRequestObject.getPrinterNameSelected());
				processSession.setSealNumberUsed(assetInfo.getRegulatoryId());
				try {
					processSession.setSiteNumberUsed(String.valueOf(siteNumber));
				} catch (Exception e) {
					log.error("Error when setting site number used");
				}
				//Alerts
				//Object[] to get the XC10 messageId,JackpotId and 0-messageId,1-JackpotId
				Object[] results = new Object[2];
				boolean existenceStatusToCheck = processKeyPadJackpot(processSession, siteNumber,cashierDeskEnabled,results);

				if (!existenceStatusToCheck) {
					constructFinalizationNAKObject(jPMsg, new String("No Jackpot to Process. Please Check."));
					return;
				}

				// This will print the actual slip.
				processKeypadSlip(siteNumber, processSession, msgObj, enableCheckPrint);

				// IF POUCH PAY OR CASH DESK SITE CONFIG PARAMETER DISABLED,
				// THEN POST TO ACCOUNTING
				if ((processSession.getPouchPay() != null && processSession.getPouchPay().intValue() > 0)
						|| (cashierDeskEnabled != null && cashierDeskEnabled.equalsIgnoreCase("No"))) {
					postInfoToAccounting(processSession, siteNumber);
						

					// POST TO PROGRESSIVE AFTER JACKPOT PROCESSING
					if (processSession.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
							|| processSession.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
						if (log.isInfoEnabled()) {
							log.info("Posting TO RESET METER AND LIABILITY for Progressive JP");
						}
						IProgressiveBO progressiveBO = (IProgressiveBO) ServiceLocator.fetchService(LOOKUP_SDS_PROGRSSIVE_BO,IAppConstants.IS_LOCAL_LOOKUP);
						progressiveBO.resetMeterAndPostLiability(processSession.getAcnfNumber(),
								((Long)results[0]), results[1].toString(),processSession.getJpHitAmount(), processSession.getSiteId(),true);
						if (log.isInfoEnabled()) {
							log.info("End of posting TO RESET METER AND LIABILITY for Progressive JP");
						}
					}
				}	
				else if(cashierDeskEnabled != null && cashierDeskEnabled.equalsIgnoreCase("Yes")){
					////add prog chgs here - resrt meter 
					// POST TO PROGRESSIVE AFTER JACKPOT SLIP PRINTING
					if (processSession.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
							|| processSession.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE)  {
						if (log.isInfoEnabled()) {
							log.info("Posting TO RESET METER for Progressive JP");
						}
						try{
							
							IProgressiveBO progressiveBO = (IProgressiveBO) ServiceLocator.fetchService(LOOKUP_SDS_PROGRSSIVE_BO,
									IAppConstants.IS_LOCAL_LOOKUP);
							progressiveBO.resetMeter( processSession.getAcnfNumber(),((Long)results[0]), results[1].toString(),
											processSession.getJpHitAmount(), processSession.getSiteId(),true);
						}catch(Exception e){
							log.error("Exception while sending resetMeter  :", e);
						}
						if (log.isInfoEnabled()) {
							log.info("End of posting TO RESET METER AND LIABILITY for Progressive JP");
						}
					}
					
				}
				
				//Post to CMP if the amount is altered.
				if (processSession.getJpOriginalAmount() != null
						&& processSession.getJpHitAmount() != null				
						&& processSession.getJpOriginalAmount().longValue() != processSession
								.getJpHitAmount().longValue()) {
					KeypadUtil.postJackpotAdjustementToMarketing(processSession,((Long)results[0]));
				}

				// Need to process Slip printing here and then construct the
				// finaliation object
				FinalizationMessageObject finalizationMessageObject = getFinalizationResponse(siteNumber,
						false, "");
				if (finalizationMessageObject.isErrorPresent()) {
					constructFinalizationNAKObject(jPMsg, finalizationMessageObject.getTextMessage());
					return;
				}
				
				if(log.isDebugEnabled()){
					log.debug("JP processed through Keypad");
				}
				
				/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT*/
				String key = siteNumber + "_" + processSession.getSequenceNumber();				
				JackpotUtil.removePendingJPFromCache(key);
				
				KeypadUtil.copyFinalizationObjectToSend(finalizationMessageObject, jPMsg);

				// deleting session on successful completion.
				if (processSession != null) {
					// delete the process session table for the asset number and site id
					SessionManagerDAO.deleteSession(processSession.getAcnfNumber(), processSession.getSiteId());
				}
			}
		} catch (Exception e) {
			log.error("Error in processFinalMessage", e);
		}
	}

	public boolean processKeyPadJackpot(ProcessSession processSession, Integer siteNumber, String cashierDeskEnabled,Object[] results){		
		log.info("Inside processKeyPadJackpot method");
		/**
		 * getUserDetails
		 * updateProcessJackpot
		 * sendAlert
		 * sendAlert
		 * getJpSiteConfigurationValue
		 * directedResetHandPaidJackpot
		 * sendSystemGeneratedException
		 * getPrinterAndSchemaXmlFiles
		 */	
		boolean processPassed=true;

		try {
			boolean existenceStatus = JackpotDAO.updateKeyPadProcessJackpot(processSession, siteNumber, cashierDeskEnabled,results);
			if(!existenceStatus){
				return false;
			}
		} catch (JackpotDAOException e2) {			
			e2.printStackTrace();
		}
		//TODO need to check whether the pouch pay needs to insert a new record in transaction - not required since pouch pay auth is not required.
		//boolean response = jackpotBO.insertPouchPayAttendant(rtnJackpotDTO);		


		// NEW MSG ID REQUIRED FOR SYS GEN XC AND ALERTS (NOT THE XC 10 MSG ID)
		long messageId = MessageUtil.getMessageId();

		char exceptionCode = 100;

		try {
			/**
			 * JACKPOT PRINTED (103) to be sent to the AlertsEngine
			 */
			JackpotAlertObject jpAlertsJpPrinted = null; 
			jpAlertsJpPrinted = getJPAlertObject(processSession, siteNumber, messageId, exceptionCode,
														IAlertMessageConstants.JACKPOT_PRINTED,IAlertMessageConstants.JACKPOT_PRINTED_MSG);			
			log.info("Before sending the msg to Alerts for "+jpAlertsJpPrinted.getTerminalMessage()+" : "+jpAlertsJpPrinted.getTerminalMessageNumber());
			KeypadUtil.sendAlertMessage(jpAlertsJpPrinted);
			log.info("After sending the msg to Alerts for "+jpAlertsJpPrinted.getTerminalMessage()+" : "+jpAlertsJpPrinted.getTerminalMessageNumber());
			
			//Sending Alert 104 if the HPJP amount is altered from the JP Amount.
			if (processSession.getJpOriginalAmount() != null
					&& processSession.getJpHitAmount() != null
					&& processSession.getJpOriginalAmount().longValue() != processSession
							.getJpHitAmount().longValue()) {
				jpAlertsJpPrinted = getJPAlertObject(processSession, siteNumber, messageId, exceptionCode,
						IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT,IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT_MESG);	
			log.info("Before sending the msg to Alerts for "+jpAlertsJpPrinted.getTerminalMessage()+" : "+jpAlertsJpPrinted.getTerminalMessageNumber());
			KeypadUtil.sendAlertMessage(jpAlertsJpPrinted);
			log.info("After sending the msg to Alerts for "+jpAlertsJpPrinted.getTerminalMessage()+" : "+jpAlertsJpPrinted.getTerminalMessageNumber());
			}
		} catch (Exception e) {
			log.error("Exception in send alert of JackpotFacade ", e);
		}



		/** SEND SYSTEM GENERATED EXCEPTION - 100 - JACKPOT POSTED */
		log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP POSTED");

		try {
			SDSMessage sdsMessage = new SDSMessage();
			SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
			slipPostedObj.setAssetConfigNumber(processSession.getAcnfNumber());
			slipPostedObj.setDateTime(Calendar.getInstance());
			
			slipPostedObj.setMessageId(messageId);// NEW MSG ID IS REQUIRED FOR THE SYSTEM GEN XC NOT THE MSG ID FOR XC 10
			slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
			slipPostedObj.setSiteId(siteNumber);				
			slipPostedObj.setJackpotId(processSession.getJackpotId().toUpperCase());			
			
			if ((processSession.getPouchPay() != null && processSession.getPouchPay().intValue() > 0)
					|| (cashierDeskEnabled != null && cashierDeskEnabled.equalsIgnoreCase("No"))) {
				
				slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				
			}else{
				slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
			}
			slipPostedObj.setAlertNo(IAlertMessageConstants.JACKPOT_PRINTED);
			if(processSession.getAssetLineAddress()!=null && 
					!((processSession.getAssetLineAddress().trim()+IKeypadProcessConstants.SIGLE_SPACE).equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE)) ){
				slipPostedObj.setLineAddress(processSession.getAssetLineAddress());
			}				
			if(processSession.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
				slipPostedObj.setCcJpAmt(processSession.getJpHitAmount());
			}else if(processSession.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
					|| processSession.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
				slipPostedObj.setProgJpAmt(processSession.getJpHitAmount());
			}else{
				slipPostedObj.setNormalJpAmt(processSession.getJpHitAmount());
			}	
			sdsMessage.setMessage(slipPostedObj);		

			KeypadUtil.sendMessagingEngine(sdsMessage);
		} catch (Exception e1) {
			log.error("Exception while sending sendSystemGeneratedException - JP POSTED",e1);
		}		
		log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP POSTED");

		/** GET SLIP AND PRINTER XML FILES */

		//GET AND SET BARCODE ENCODE FORMAT
		//rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(IAppConstants.BARCODE_ENCODE_FORMAT));

		//Commenting this since schema file string format is not necessary.
		/*XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				log.debug("printerSchema : "+printerSchema);
				log.debug("slipSchema : "+slipSchema);
				log.debug("inside the method processJackpot and xmlSeriveUtil returned the xml files as strings");
				processSession.setSlipSchema(slipSchema);
				processSession.setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				e.printStackTrace();
			}*/

		//prev sent to accounting here.

		return processPassed;
	}


	private JackpotAlertObject getJPAlertObject(ProcessSession processSession,Integer siteNumber, long messageId, char exceptionCode,
			int terminalMessageNbr,String terminalMessage) {
		JackpotAlertObject jpAlertsJpPrinted = new JackpotAlertObject();	
		jpAlertsJpPrinted.setTerminalMessageNumber(terminalMessageNbr);
		jpAlertsJpPrinted.setTerminalMessage(terminalMessage);
		if (processSession.getAcnfNumber() != null) {
			jpAlertsJpPrinted.setAssetConfigNumber(processSession.getAcnfNumber());
		}
		if (processSession.getAcnfLocation() != null) {
			jpAlertsJpPrinted.setStandNumber(processSession.getAcnfLocation());
		}
		jpAlertsJpPrinted.setEngineId(IAppConstants.JACKPOT_ALERTS_ENGINE_ID);
		if (processSession.getJpHitAmount() != null && processSession.getJpHitAmount().longValue()!= 0){
			jpAlertsJpPrinted.setJackpotAmount(processSession.getJpHitAmount());
		}
		jpAlertsJpPrinted.setExceptionCode(exceptionCode);			
		jpAlertsJpPrinted.setMessageId(messageId);						
		jpAlertsJpPrinted.setSendingEngineName(IAppConstants.MODULE_NAME);
		jpAlertsJpPrinted.setSiteId(siteNumber);
		jpAlertsJpPrinted.setSlotAreaId(processSession.getAreaLongName());
		jpAlertsJpPrinted.setEmployeeId(String.valueOf(processSession.getEmployeeId()));
		return jpAlertsJpPrinted;
	}

	public boolean postInfoToAccounting(ProcessSession processSession,Integer siteNumber){
		boolean posted=false;
		try{

//			Getting the currency symbol from SiteConfig.
			String currencySymbol = null;
			try {
				currencySymbol = JackpotUtil.getSiteParamValue(SITE_CURRENCY_SYMBOL, siteNumber);
				if (currencySymbol != null
						&& !((currencySymbol.trim() + IKeypadProcessConstants.SIGLE_SPACE)
								.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					currencySymbol = CURRENCY_SYMBOL_DEFAULT;
				}
			} catch (Exception e) {
				currencySymbol=CURRENCY_SYMBOL_DEFAULT;
			}

			/**
			 * Calling Jackpot's AccountingUtil method to push info to Accounting Engine 
			 * based on the below flag check
			 * 
			 */	
			//TODO NEED to call Accounting here for posting it.

			try {
				String postToAccounting = AppPropertiesReader.getInstance().getValue(
						PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
				if (log.isDebugEnabled()) {
					log.debug("postToAccounting: " + postToAccounting);
				}
				if (postToAccounting != null && postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG)
						&& processSession != null) {
					SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTOForKeypad(processSession,
							currencySymbol);
					if (log.isDebugEnabled()) {
						log.debug("BEFORE calling ISlipProcessorBO.process");
					}
					if (slipInfoDTO != null) {
						boolean accntPosted = KeypadUtil.sendAccountingPost(slipInfoDTO);
						if (log.isInfoEnabled()) {
							log.info("***Slip Print Accounting Posted Status for keypad process*** :"
									+ accntPosted);
						}
					} else if (log.isDebugEnabled()) {
						log.debug("slipInfoDTO is NULL");
					}
					if (log.isDebugEnabled()) {
						log.debug("AFTER calling ISlipProcessorBO.process");
					}
				}
			} catch (Exception e) {
				log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
			}

		} catch (Exception e) {
			log.error("Exception when calling postInfoToAccounting method ", e);
		}
		return posted;
	}

	public boolean processKeypadSlip(Integer siteNumber, ProcessSession processSession, IHeader msgObj, String enableCheckPrint){
		boolean slipPrintedSuccessfully = false;
		try {
			PrintDTO printDTO = new PrintDTO();
			if (printDTO != null) {
				SlipImageFactory slipImageFactory = new SlipImageFactory();
				String imageToProcess = null;
				String slipPrintType = null;		
				String printerSelected = null;
				if (processSession.getPrinterSelName() != null
						&& !((processSession.getPrinterSelName().trim() + IKeypadProcessConstants.SIGLE_SPACE)
								.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					printerSelected = processSession.getPrinterSelName();
				}
				String langSelected = printDTO.getLangSelected();
				if (!(langSelected != null && !((langSelected.trim() + IKeypadProcessConstants.SIGLE_SPACE)
						.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE)))) {
					langSelected = IKeypadProcessConstants.JACKPOT_ENGINE_EXT_LANGUAGE_DEFAULT;
				}
				if(enableCheckPrint.equalsIgnoreCase("No") 
						|| printerSelected.toUpperCase().contains(IKeypadProcessConstants.EPSON_PRINTER_SUBSTRING)) {
					slipPrintType = IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE;
					printDTO = KeypadUtil.setPrintDTOValues(siteNumber, processSession, msgObj);
				} else if(enableCheckPrint.equalsIgnoreCase("Yes")) {
					slipPrintType = IKeypadProcessConstants.CHECK_PRINT_SLIP_TYPE;
					printDTO = KeypadUtil.setCheckDTOValues(siteNumber, processSession);
					
				}
				
				Map<String, String> slipI18NMap = ExternalizationUtil.getInstance().getExternalizationMap(
						langSelected, slipPrintType);
				printDTO.setKeyboard((slipI18NMap.get(IKeypadProcessConstants.SLOT_EXTERNALIZED_KEY) != null) 
						? (slipI18NMap.get(IKeypadProcessConstants.SLOT_EXTERNALIZED_KEY))
								: (IKeypadProcessConstants.SLOT_EXTERNALIZED_KEY));
				boolean isEpsonPrinter = false;
				
				
				if (printerSelected != null
						&& !((printerSelected.trim() + IKeypadProcessConstants.SIGLE_SPACE)
								.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					if (printerSelected.toUpperCase().contains(IKeypadProcessConstants.EPSON_PRINTER_SUBSTRING)) {
						if(log.isInfoEnabled()) {
							log.info("Epson printer is selected, calling buildSlipImage method");
						}
						isEpsonPrinter = true;
						imageToProcess = slipImageFactory.buildSlipImage(printDTO, JACKPOT_PRINT_SLIP_TYPE,
								slipI18NMap, processSession.getJpHitAmount(), langSelected, siteNumber);
					} else if (printerSelected.contains(IKeypadProcessConstants.LASER_JET_PRINTER_SUBSTRING)) {
						if(log.isInfoEnabled()) {
							log.info("Laser Jet printer is selected, calling getLaserPrinterImage method");
						}
						if (enableCheckPrint.equalsIgnoreCase("Yes")) {
							imageToProcess = slipImageFactory.getCheckLaserPrinterImage(printDTO,
									CHECK_PRINT_SLIP_TYPE, slipI18NMap, processSession.getJpHitAmount(),
									langSelected);
						} else {
							imageToProcess = slipImageFactory.getLaserPrinterImage(printDTO,
									JACKPOT_PRINT_SLIP_TYPE, slipI18NMap, processSession.getJpHitAmount(),
									langSelected, siteNumber);
						}
					} else {
						if (log.isInfoEnabled()) {
							log.info("Default is selected, calling getLaserPrinterImage method");
						}
						if (enableCheckPrint.equalsIgnoreCase("Yes")) {
							imageToProcess = slipImageFactory.getCheckLaserPrinterImage(printDTO,
									CHECK_PRINT_SLIP_TYPE, slipI18NMap, processSession.getJpHitAmount(),
									langSelected);
						} else {
							imageToProcess = slipImageFactory.getLaserPrinterImage(printDTO,
								JACKPOT_PRINT_SLIP_TYPE, slipI18NMap, processSession.getJpHitAmount(),
								langSelected, siteNumber);
						}
					}
				}				

				if (imageToProcess != null
						&& !((imageToProcess.trim() + IKeypadProcessConstants.SIGLE_SPACE)
								.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
					if(log.isInfoEnabled()) {
						log.info("Slip Printing Initiated......");
					}
					if (isEpsonPrinter) {
						if(log.isInfoEnabled()) {
							log.info("Print for Epson processing.");
						}
						SlipPrintUtil slipPrintUtil = new SlipPrintUtil();
						slipPrintUtil.printSlip(imageToProcess, printerSelected);
					} else {
						//this loop is for printers other than epson printer.
						if(log.isInfoEnabled()) {
							log.info("Print for printers other than epson started.");
						}
						PrintWithGraphicsForKeypad printWithGraphicsForKeypad = new PrintWithGraphicsForKeypad();
						Image barcodeImg = null;
						if (printDTO.getBarcode() != null
								&& printDTO.getBarcode().trim().equalsIgnoreCase("TRUE")) {
							if (log.isInfoEnabled()) {
								log.info("Barcode print true");
							}
							String encodeFormat = null;
							try {
								encodeFormat = AppPropertiesReader.getInstance().getValue(
										PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT);
							} catch (Exception e) {
								encodeFormat = IAppConstants.CODE128_ENCODE_FORMAT;// default encode format.
							}
							if (log.isInfoEnabled()) {
								log.info("Started....");
							}
							Barcode barcode = JackpotUtil.createBarcode(printDTO.getSiteId(), processSession.getJpHitAmount(),
									(processSession.getSequenceNumber().longValue()), encodeFormat);
							
							String initialBarFile = encodeFormat + ".jpg";
							barcode.createBarcodeImage(initialBarFile);

							File barcodeFile = new File(initialBarFile);
							barcodeImg = ImageIO.read(barcodeFile); 
							
						} else if (log.isInfoEnabled()) {
							log.info("Barcode print false");
						}
						printWithGraphicsForKeypad.printTextImage(imageToProcess, barcodeImg, printerSelected, slipPrintType, langSelected);
						
					}
					if(log.isInfoEnabled()) {
						log.info("Slip Printing Complete......");
					}
				}
			}
		}catch (Exception e) {
			log.error("Exception in print .",e);
		}
		return slipPrintedSuccessfully;
	}


	public FinalizationMessageObject getFinalizationResponse(int siteNumber,boolean errorPresentIfAny,String errorMessageIfAny){
		FinalizationMessageObject finalizationMessageObject=null;
		try{
			if (siteNumber != 0) {
				boolean clearJackpot = false;
				try {
					clearJackpot = (JackpotUtil.getSiteParamValue(KP_CLEAR_JACKPOT_FLAG, siteNumber)
							.equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
				} catch (Exception e) {
					log.error("Error in getFinalizationResponse", e);
				}

				finalizationMessageObject = new FinalizationMessageObject();
				finalizationMessageObject.setClearJackpot(clearJackpot);

				if(errorPresentIfAny){
					finalizationMessageObject.setErrorPresent(errorPresentIfAny);
					if (errorMessageIfAny != null
							&& !((errorMessageIfAny.trim() + IKeypadProcessConstants.SIGLE_SPACE)
									.equalsIgnoreCase(IKeypadProcessConstants.SIGLE_SPACE))) {
						finalizationMessageObject.setTextMessage(errorMessageIfAny);
					} else {
						finalizationMessageObject.setTextMessage("Error in Printing Slip.");// default error message.
					}
				} else {
					finalizationMessageObject.setErrorPresent(false);
					finalizationMessageObject.setTextMessage("Slip Printed Successfully.");
				}
			}
		} catch (Exception e) {
			log.error("Error in getFinalizationResponse", e);
		}
		return finalizationMessageObject;
	}


	public PrinterDetailsObject getAuthorizationResponse(){
		PrinterDetailsObject printerDetailsObject=null;
		try{
			log.info("Inside getAuthorizationResponse method ");
			printerDetailsObject=new PrinterDetailsObject();
			PrinterManager printerManager=new PrinterManager();
			int printerCountGot=printerManager.getPrinterCount();
			log.info("Printer Count Got :"+printerCountGot);
			if(printerCountGot>0){
				log.info("Inside printers sending");
				int[] printerLocationArray=printerManager.getAllAvailablePrinterIndex();
				printerDetailsObject.setPrinterLocationCount(printerCountGot);
				printerDetailsObject.setPrinterLocations(printerLocationArray);
			}else{
				log.info("Inside printers not sending");
				printerDetailsObject.setErrorPresent(true);
				printerDetailsObject.setErrorText("Printers not available.");
			}			
		}catch (Exception e) {
			log.info("Exception in getAuthorizationResponse ",e);
		}
		return printerDetailsObject;
	}



	public InitiationResponseObject getInitiationResponse(int siteNumber) {
		InitiationResponseObject initiationResponseObject = null;
		try {
			if (siteNumber != 0) {
				boolean processFlag = false;
				boolean promptJackpotAmount = false;
				boolean promptPayLine = false;
				boolean promptWinningCombination = false;
				boolean promptForCoinsPlayed = false;
				boolean promptShift = false;
				boolean promptPouchPay = false;
				boolean promptEmployeeAuth = false;

				try {
					processFlag = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_PROCESS, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptJackpotAmount = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_AMOUNT, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptPayLine = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_PAYLINE, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptWinningCombination = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_WINCOM, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptForCoinsPlayed = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_COINS, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptShift = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_SHIFT, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					promptPouchPay = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_POUCH_PAY, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					
					if(AppPropertiesReader.getInstance().getBooleanValue(PropertyKeyConstant.PROPS_JP_READ_KPJP_PROMPT_FOR_AUTH)){
						promptEmployeeAuth = (JackpotUtil.getSiteParamValue(PROMPT_KP_JP_EMP_AUTH, siteNumber).equalsIgnoreCase(SITE_CONFIG_BOOLEAN_VALID) ? true : false);
					}
					else{
						promptEmployeeAuth = false;
					}		
				} catch (Exception e) {
					log.error("Error in getInitiationResponse", e);
				}
				initiationResponseObject = new InitiationResponseObject();
				initiationResponseObject.setProcessFlag(processFlag);
				initiationResponseObject.setPromptJackpotAmount(promptJackpotAmount);
				initiationResponseObject.setPromptPayLine(promptPayLine);
				initiationResponseObject.setPromptWinningCombination(promptWinningCombination);
				initiationResponseObject.setPromptForCoinsPlayed(promptForCoinsPlayed);
				initiationResponseObject.setPromptShift(promptShift);
				initiationResponseObject.setPromptPouchPay(promptPouchPay);
				initiationResponseObject.setPromptEmployeeAuth(promptEmployeeAuth);
			}
		} catch (Exception e) {
			log.error("Error in getInitiationResponse", e);
		}
		return initiationResponseObject;
	}
}
