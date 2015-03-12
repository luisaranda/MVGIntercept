package com.ballydev.sds.jackpot.keypad;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_ALERTS_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_FRAMEWORK_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_MARKETING_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_MESSAGING_FACADE;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_SITE_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_SLIPPROCESS_BO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.accounting.bo.ISlipProcessorBO;
import com.ballydev.sds.alertsengine.bo.IAlertsBO;
import com.ballydev.sds.common.IHeader;
import com.ballydev.sds.common.alerts.IAlertMessage;
import com.ballydev.sds.common.jackpot.IJackPotSlipProcessingMessage;
import com.ballydev.sds.common.message.AssetInfo;
import com.ballydev.sds.common.message.SDSMessage;
import com.ballydev.sds.framework.bo.IFrameworkBO;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.IJackpotIds;
import com.ballydev.sds.jackpot.db.slip.ProcessSession;
import com.ballydev.sds.jackpot.dto.SlipInfoDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.marketing.bo.IMarketingBO;
import com.ballydev.sds.marketing.dto.PlayerDetailsDTO;
import com.ballydev.sds.marketing.exception.MarketingDAOException;
import com.ballydev.sds.messaging.service.IMessagingEngineFacade;
import com.ballydev.sds.siteconfig.bo.ISiteConfigBO;
import com.ballydev.sds.siteconfig.dto.SiteDTO;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;

/**
 * Keypad Process Utilitity Class.
 * 
 * @author ranjithkumarm
 * 
 */
public class KeypadUtil implements IKeypadProcessConstants, IAppConstants {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	public static synchronized Integer getSiteNumberForId(Integer siteId) {
		Integer siteNumber = null;
		try {
			if (siteId != null && siteId.intValue() != 0) {
				ISiteConfigBO siteConfigBO = (ISiteConfigBO) ServiceLocator
						.fetchService(LOOKUP_SDS_SITE_BO, IS_LOCAL_LOOKUP);
				if (siteConfigBO != null) {
					siteNumber = siteConfigBO.getSiteNumberForId(siteId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return siteNumber;

	}

	public static synchronized Integer getSiteIdForNumber(Integer siteNumber) {
		Integer siteId = null;
		try {
			if (siteNumber != null && siteNumber.intValue() != 0) {
				ISiteConfigBO siteConfigBO = (ISiteConfigBO) ServiceLocator
						.fetchService(LOOKUP_SDS_SITE_BO, IS_LOCAL_LOOKUP);
				if (siteConfigBO != null) {
					siteId = siteConfigBO.getSiteIdForNumber(siteNumber);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return siteId;

	}

	public static synchronized boolean sendMessagingEngine(SDSMessage sdsMessage) {
		boolean sent = false;
		try {
			if (sdsMessage != null) {
				IMessagingEngineFacade messagingEngineFacade = (IMessagingEngineFacade) ServiceLocator
						.fetchService(LOOKUP_SDS_MESSAGING_FACADE, IS_LOCAL_LOOKUP);
				if (messagingEngineFacade != null) {
					messagingEngineFacade.processSystemMessage(sdsMessage);
					sent = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sent;
	}

	public static synchronized boolean sendAccountingPost(SlipInfoDTO slipInfoDTO) {
		boolean sent = false;
		try {
			if (slipInfoDTO != null) {
				ISlipProcessorBO slipProcessBO = (ISlipProcessorBO) ServiceLocator
						.fetchService(LOOKUP_SDS_SLIPPROCESS_BO, IS_LOCAL_LOOKUP);
				if (slipProcessBO != null) {
					slipProcessBO.process(slipInfoDTO);
					sent = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sent;
	}

	public static synchronized boolean sendAlertMessage(IAlertMessage alertMessage) {
		boolean alertSent = false;
		try {
			if (alertMessage != null) {
				IAlertsBO alertBO = (IAlertsBO) ServiceLocator
						.fetchService(LOOKUP_SDS_ALERTS_BO, IS_LOCAL_LOOKUP);
				if (alertBO != null) {
					alertBO.sendAlert(alertMessage);
					alertSent = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alertSent;
	}

	public static synchronized String getLabelValue(String key, String extn_Language) {
		String value = null;
		try {
			if (key != null && !((key.trim() + " ").equalsIgnoreCase(" ")) && extn_Language != null
					&& !((extn_Language.trim() + " ").equalsIgnoreCase(" "))) {
				IFrameworkBO frameworkBO = (IFrameworkBO) ServiceLocator
						.fetchService(LOOKUP_SDS_FRAMEWORK_BO, IS_LOCAL_LOOKUP);
				if (frameworkBO != null) {
					value = frameworkBO.getLabelValue(extn_Language, key);
					if (log.isDebugEnabled()) {
						log.debug("Value from Framework BO : for key :" + key);
						log.debug("Value :" + value);
					}
				}
			}

			if (value == null) {
				if (log.isDebugEnabled()) {
					log.debug("Extrn value null");
				}
				value = key;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getDateString(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String strDate = simpleDateFormat.format(date);
		return strDate;
	}

	/**
	 * Formats the given String
	 * 
	 * @param fieldStr
	 * @return
	 */
	public static synchronized String formatField(String fieldStr, int formatLength) {

		StringBuffer retStr = null;
		if (fieldStr != null) {
			retStr = new StringBuffer(fieldStr);
			int length = fieldStr.length();
			if (length < formatLength) {
				for (int i = length; i < formatLength; i++) {

					retStr.append(" ");
				}
			}
			return retStr.toString();
		} else {
			return IKeypadProcessConstants.EMPTY_STRING;
		}
	}

	/**
	 * 
	 * Order in which the flag is processed.
	 * 
	 * setPromptJackpotAmount setPromptPayLine setPromptWinningCombination
	 * setPromptForCoinsPlayed setPromptShift setPromptPouchPay
	 * setPromptEmployeeAuth
	 * 
	 * @param flag
	 * @return
	 */
	public static synchronized InitiationResponseObject getRequestedFlagStatus(String flag) {
		InitiationResponseObject initiationResponseObject = null;
		try {
			if (flag != null && !((flag.trim() + " ").equalsIgnoreCase(" ")) && flag.length() >= 9) {
				initiationResponseObject = new InitiationResponseObject();

				if (flag.charAt(0) == '1') {
					initiationResponseObject.setPromptJackpotAmount(true);
				} else {
					initiationResponseObject.setPromptJackpotAmount(false);
				}

				if (flag.charAt(1) == '1') {
					initiationResponseObject.setPromptPayLine(true);
				} else {
					initiationResponseObject.setPromptPayLine(false);
				}

				if (flag.charAt(2) == '1') {
					initiationResponseObject.setPromptWinningCombination(true);
				} else {
					initiationResponseObject.setPromptWinningCombination(false);
				}

				if (flag.charAt(3) == '1') {
					initiationResponseObject.setPromptForCoinsPlayed(true);
				} else {
					initiationResponseObject.setPromptForCoinsPlayed(false);
				}

				if (flag.charAt(4) == '1') {
					initiationResponseObject.setPromptShift(true);
				} else {
					initiationResponseObject.setPromptShift(false);
				}

				if (flag.charAt(5) == '1') {
					initiationResponseObject.setPromptPouchPay(true);
				} else {
					initiationResponseObject.setPromptPouchPay(false);
				}

				if (flag.charAt(6) == '1') {
					initiationResponseObject.setPromptEmployeeAuth(true);
				} else {
					initiationResponseObject.setPromptEmployeeAuth(false);
				}

				try {
					initiationResponseObject.setAuthLevel((new String(" " + flag.charAt(7))).trim());
					initiationResponseObject.setAuthAmount((flag.substring(8)).trim());
				} catch (Exception e) {

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return initiationResponseObject;
	}

	public static synchronized void copyFinalizationObjectToSend(
			FinalizationMessageObject finalizationMessageObject,
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage) {
		try {
			if (finalizationMessageObject != null && jackPotSlipProcessingMessage != null) {
				jackPotSlipProcessingMessage.setResponseToFinalMessage(true);
				jackPotSlipProcessingMessage.setClearJackPotFlag(finalizationMessageObject.isClearJackpot());
				jackPotSlipProcessingMessage.setFinalMessage("Slip Print Success.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized String getPrintersFormat(int[] printerArray) {
		String printersFormat = null;
		StringBuffer bufferToAppend = null;
		try {
			if (printerArray != null && printerArray.length > 1) {
				for (int i = 1; i < printerArray.length; i++) {
					if (bufferToAppend == null) {
						bufferToAppend = new StringBuffer();
					}
					int printerIndex = printerArray[i];
					bufferToAppend.append(printerIndex);
				}

				if (bufferToAppend != null) {
					printersFormat = bufferToAppend.toString();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return printersFormat;
	}

	public static synchronized void copyAuthorizationObjectToSend(PrinterDetailsObject printerDetailsObject,
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage) {
		try {
			if (printerDetailsObject != null && jackPotSlipProcessingMessage != null) {
				jackPotSlipProcessingMessage.setNumberOfPrinters(printerDetailsObject
						.getPrinterLocationCount());
				jackPotSlipProcessingMessage.setResponseToAuthorizationMessage(true);
				jackPotSlipProcessingMessage.setPrinterLocations(getPrintersFormat(printerDetailsObject
						.getPrinterLocations()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized boolean isAttemptsExceeded(Integer attemptsHappened, int siteNumber) {
		boolean attemptsExceeded = false;
		try {
			if (attemptsHappened != null && attemptsHappened.intValue() != -1) {
				String attemptsAllowed = JackpotUtil.getSiteParamValue(
						IKeypadProcessConstants.ATTMPTS_FOR_JP_KEYPAD, siteNumber);
				int attemptsAllowedFromSite = 0;
				attemptsAllowedFromSite = Integer.parseInt(attemptsAllowed.trim());
				log.info("Checking Attempts for the session.");
				log.info("Occurred Attempts :" + attemptsHappened);
				log.info("Attempts Allowed for a session :" + attemptsAllowedFromSite);
				if (attemptsHappened >= attemptsAllowedFromSite) {
					log.info("Attempts has Exceeded....");
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attemptsExceeded;
	}

	public static synchronized String copyInitiationObjectToSend(
			InitiationResponseObject initiationResponseObject,
			IJackPotSlipProcessingMessage jackPotSlipProcessingMessage) {
		String requestedFlag = null;
		StringBuffer flags = new StringBuffer();
		try {
			if (jackPotSlipProcessingMessage != null && initiationResponseObject != null) {
				jackPotSlipProcessingMessage.setProcessSlip(initiationResponseObject.isProcessFlag());
				jackPotSlipProcessingMessage.setPromptJackPotAmount(initiationResponseObject
						.isPromptJackpotAmount());
				if (initiationResponseObject.isPromptJackpotAmount()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptPayLine(initiationResponseObject.isPromptPayLine());
				if (initiationResponseObject.isPromptPayLine()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptWinningCombination(initiationResponseObject
						.isPromptWinningCombination());
				if (initiationResponseObject.isPromptWinningCombination()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptCoinsPlayed(initiationResponseObject
						.isPromptForCoinsPlayed());
				if (initiationResponseObject.isPromptForCoinsPlayed()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptShift(initiationResponseObject.isPromptShift());
				if (initiationResponseObject.isPromptShift()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptCreditMeterHandPay(false);
				jackPotSlipProcessingMessage.setPromptPouchPay(initiationResponseObject.isPromptPouchPay());
				if (initiationResponseObject.isPromptPouchPay()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setPromptEmployeeAuthorization(initiationResponseObject
						.isPromptEmployeeAuth());
				if (initiationResponseObject.isPromptEmployeeAuth()) {
					flags.append("1");
				} else {
					flags.append("0");
				}
				jackPotSlipProcessingMessage.setResponseToInitMessage(true);

				if (flags != null && flags.length() != 0) {
					requestedFlag = flags.toString();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestedFlag;
	}

	public static short getJackpotTypeForId(Integer jackpotId, long jackpotAmount) {
		short jackpotType = 0;
		try {
			if (jackpotId != null && jackpotId.intValue() != 0) {
				if (jackpotId == IJackpotIds.JACKPOT_ID_252_FC 
						|| jackpotId == IJackpotIds.JACKPOT_ID_253_FD) {
					return KeypadLookUpConstants.JACKPOT_TYPE_REGULAR;
				} else if (jackpotId == IJackpotIds.JACKPOT_ID_INVALID_186_BA) {
					return KeypadLookUpConstants.JACKPOT_TYPE_PROMOTIONAL;
				} else if (jackpotId == IJackpotIds.JACKPOT_ID_250_FA) {
					return KeypadLookUpConstants.JACKPOT_TYPE_SYSTEM_GAME;
				} else if (jackpotId == IJackpotIds.JACKPOT_ID_254_FE) {
					return KeypadLookUpConstants.JACKPOT_TYPE_CANCEL_CREDIT;
				} else if(jackpotId == IJackpotIds.JACKPOT_ID_251_FB) {
					return KeypadLookUpConstants.JACKPOT_TYPE_MYSTERY;
				} else if (jackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
						&& jackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
						&& (jackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
								&& jackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)
						&& jackpotAmount > 0) {
					return KeypadLookUpConstants.JACKPOT_TYPE_PROGRESSIVE;
				} else if (jackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
						&& jackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
						&& (jackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
								&& jackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)
						&& jackpotAmount == 0) {
					return KeypadLookUpConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE;
				} else {
					return KeypadLookUpConstants.JACKPOT_TYPE_REGULAR;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jackpotType;

	}

	public static PrintDTO setPrintDTOValues(Integer siteNumber, ProcessSession processSession, IHeader msgObj)
			throws JackpotEngineServiceException {
		PrintDTO printDTO = new PrintDTO();
		String langCode = null;
		try {
			langCode = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_LANG_CODE);
		} catch (Exception e) {
			langCode = null;
		}

		if (!(langCode != null && !((langCode.trim() + " ").equalsIgnoreCase(" ")))) {
			langCode = JACKPOT_ENGINE_EXT_LANGUAGE_DEFAULT;// default language;
		}

		printDTO.setLangSelected(langCode);
		processSession.setLanguageSelectedHere(langCode);

		if (processSession.getJpHitAmount() != null && processSession.getJpHitAmount().longValue() != 0) {
			String currencySymbol = JackpotUtil.getSiteParamValue(SITE_CURRENCY_SYMBOL, siteNumber);
			if (currencySymbol != null && !((currencySymbol.trim() + " ").equalsIgnoreCase(" "))) {
				currencySymbol = CURRENCY_SYMBOL_DEFAULT;
			}
			printDTO.setActualAmount(currencySymbol
					+ new DecimalFormat("0.00").format(KeypadConvertUtil.centsToDollar(processSession
							.getJpHitAmount())));
			printDTO.setHpjpAmount(currencySymbol
					+ new DecimalFormat("0.00").format(KeypadConvertUtil.centsToDollar(processSession
							.getJpHitAmount())));
			printDTO.setJackpotNetAmount(currencySymbol
					+ new DecimalFormat("0.00").format(KeypadConvertUtil.centsToDollar(processSession
							.getJpHitAmount())));
			printDTO.setOriginalAmount(currencySymbol
					+ new DecimalFormat("0.00").format(KeypadConvertUtil.centsToDollar(processSession.getJpOriginalAmount())));
			printDTO.setJackpotTaxAmount("0.00");
		}
		if (processSession.getAreaLongName() != null
				&& !((processSession.getAreaLongName().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setArea(processSession.getAreaLongName());
		}
		if (processSession.getAcnfLocation() != null
				&& !((processSession.getAcnfLocation().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setAssetConfigLocation(processSession.getAcnfLocation());
		}
		if (processSession.getAcnfNumber() != null
				&& !((processSession.getAcnfNumber().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setAssetConfigNumber(KeypadConvertUtil.trimAcnfNo(processSession.getAcnfNumber()));

		}
		// printDTO.setAssociatedPlayerCard(associatedPlayerCard)
		if (processSession.getAuthEmployeeCardId() != null) {
			try {
				printDTO.setAuthEmpNameOne(processSession.getAuthEmployeeFirstName() + " "
						+ processSession.getAuthEmployeeLastName());
				printDTO.setAuthEmpIdOne(PadderUtil.lPad(String.valueOf(processSession
						.getAuthEmployeeCardId().intValue()), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			} catch (Exception e) {
				log.error("Unable to get the First Auth Emp Name", e);
			}
		}

		if (processSession.getSealNumberUsed() != null
				&& !((processSession.getSealNumberUsed().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setSeal(processSession.getSealNumberUsed());
		}

		if (processSession.getPlayerCard() != null
				&& !((processSession.getPlayerCard().trim() + " ").equalsIgnoreCase(" "))
				&& !(processSession.getPlayerCard().trim().matches("0+"))) {
			try {
				log.info("Getting player name for for player card :" + processSession.getPlayerCard().trim());
				IMarketingBO marketingBO = (IMarketingBO) ServiceLocator
						.fetchService(LOOKUP_SDS_MARKETING_BO, IS_LOCAL_LOOKUP);
				if (marketingBO != null) {
					List<PlayerDetailsDTO> playerList = marketingBO.getPlayersPersonalInfo(processSession
							.getPlayerCard().trim(), siteNumber);
					if (playerList != null && playerList.size() != 0) {
						PlayerDetailsDTO playerDetailsDTOGot = playerList.get(0);
						log.info("Setting Player First Name :" + playerDetailsDTOGot.getFirstName());
						log.info("Setting Player Last Name :" + playerDetailsDTOGot.getLastName());
						printDTO.setPlayerName(playerDetailsDTOGot.getFirstName() + " "
								+ playerDetailsDTOGot.getLastName());
					} else {
						log.info("Player List null from Marketing BO for player card :"
								+ processSession.getPlayerCard().trim());
					}

				} else {
					log.error("Marketing BO null in calling to get player name for player card.");
				}
			} catch (Exception e) {
				log.error("Error Calling Marketing BO for getting player name for the card "
						+ processSession.getPlayerCard() + " ", e);
			}

		}

		String casinoNameToSend = null;
		try {
			// casinoNameToSend=KeypadUtil.getSiteConfigurationData(SITE_CASINO_NAME,
			// siteNumber);

			ISiteConfigBO siteConfigBO = (ISiteConfigBO) ServiceLocator
					.fetchService(LOOKUP_SDS_SITE_BO, IS_LOCAL_LOOKUP);
			if (siteConfigBO != null) {
				SiteDTO siteDTOToProcess = siteConfigBO.getSite(siteNumber);
				if (siteDTOToProcess != null) {
					casinoNameToSend = siteDTOToProcess.getShortName();
				}

			}

		} catch (Exception e) {
			casinoNameToSend = null;
		}

		if (casinoNameToSend != null && !((casinoNameToSend.trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setCasinoName(casinoNameToSend);
		} else {
			String extnDefaultCasinoName = null;

			try {
				extnDefaultCasinoName = ExternalizationUtil.getInstance().getExternalizationValue(
						PRINT_CASINO_NAME, langCode);
			} catch (Exception e) {
				extnDefaultCasinoName = PRINT_CASINO_NAME;// default key value
															// shown.
			}

			printDTO.setCasinoName(extnDefaultCasinoName);
		}

		if (processSession.getCoinsPlayed() != null && processSession.getCoinsPlayed().intValue() != 0) {
			printDTO.setCoinsPlayed(processSession.getCoinsPlayed().intValue());
		}

		// printDTO.setJackpotType(jackpotDTO.getJackpotId());

		// printDTO.setJpDescription(jpDescription)

		if (processSession.getPayline() != null
				&& !((processSession.getPayline().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setPayline(processSession.getPayline());
		}
		if (processSession.getPrinterSelName() != null
				&& !((processSession.getPrinterSelName().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setPKeyboard(processSession.getPrinterSelName());
		}
		if (processSession.getPlayerCard() != null
				&& !((processSession.getPlayerCard().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setPlayerCard(processSession.getPlayerCard());
		} else {
			printDTO.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
		}
		String transactionDate = getDateString(new Date(), SLIP_DATE_FORMAT);
		printDTO.setPrintDate(transactionDate);
		printDTO.setTransactionDate(transactionDate);

		/*
		 * if(processSession.getPrinterSchema()!=null &&
		 * !((processSession.getPrinterSchema().trim()+" ").equalsIgnoreCase("
		 * ")) ){ printDTO.setPrinterSchema(processSession.getPrinterSchema()); }
		 * if(processSession.getSlipSchema()!=null &&
		 * !((processSession.getSlipSchema().trim()+" ").equalsIgnoreCase(" ")) ){
		 * printDTO.setSlipSchema(processSession.getSlipSchema()); }
		 */

		if (processSession.getEmployeeId() != null && processSession.getEmployeeId().intValue() != 0) {
			printDTO.setProcessEmpId(PadderUtil
					.lPad(String.valueOf(processSession.getEmployeeId().intValue()),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			printDTO.setSlotAttendantId(PadderUtil.lPad(String.valueOf(processSession.getEmployeeId()
					.intValue()), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
		}

		if (processSession.getEmployeeFirstName() != null
				&& !((processSession.getEmployeeFirstName().trim() + " ").equalsIgnoreCase(" "))
				&& processSession.getEmployeeLastName() != null
				&& !((processSession.getEmployeeLastName().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setProcessEmpName(processSession.getEmployeeFirstName() + " "
					+ processSession.getEmployeeLastName());
			printDTO.setSlotAttendantName(processSession.getEmployeeFirstName() + " "
					+ processSession.getEmployeeLastName());
		}

		if (processSession.getSequenceNumber() != null && processSession.getSequenceNumber().longValue() != 0) {
			printDTO.setSequenceNumber(Long.toString(processSession.getSequenceNumber().longValue()));
		}

		if (processSession.getShift() != null && processSession.getShift().intValue() > 0) {
			if (processSession.getShift().intValue() == JACKPOT_SHIFT_DAY) {

				String dayValue = null;
				try {
					dayValue = KeypadUtil.getLabelValue(IKeypadProcessConstants.DAY_SHIFT_LABEL, langCode);
				} catch (Exception e) {
					dayValue = null;
				}
				if (!(dayValue != null && !((dayValue.trim() + " ").equalsIgnoreCase(" ")))) {
					dayValue = DEFAULT_JACKPOT_SHIFT_DAY;// default day
															// value.
				}

				printDTO.setShift(dayValue);
			} else if (processSession.getShift().intValue() == JACKPOT_SHIFT_SWING) {

				String swingValue = null;
				try {
					swingValue = KeypadUtil
							.getLabelValue(IKeypadProcessConstants.SWING_SHIFT_LABEL, langCode);
				} catch (Exception e) {
					swingValue = null;
				}
				if (!(swingValue != null && !((swingValue.trim() + " ").equalsIgnoreCase(" ")))) {
					swingValue = DEFAULT_JACKPOT_SHIFT_SWING;// default
																// swingValue
																// value.
				}

				printDTO.setShift(swingValue);
			} else if (processSession.getShift().intValue() == JACKPOT_SHIFT_GRAVEYARD) {

				String graveYrdValue = null;
				try {
					graveYrdValue = KeypadUtil.getLabelValue(IKeypadProcessConstants.GRAVEYARD_SHIFT_LABEL,
							langCode);
				} catch (Exception e) {
					graveYrdValue = null;
				}
				if (!(graveYrdValue != null && !((graveYrdValue.trim() + " ").equalsIgnoreCase(" ")))) {
					graveYrdValue = DEFAULT_JACKPOT_SHIFT_GRAVEYARD;// default
																	// graveYrd
																	// value.
				}

				printDTO.setShift(graveYrdValue);
			}
		}

		if (msgObj != null) {
			//SETTING CARDLESS ACCOUNT NUMBER AND ACCOUNT TYPE TO THE PRINTDTO
			printDTO.setAccountNumber(((IJackPotSlipProcessingMessage) msgObj).getTaggedStatusCardlessAccountNumber());
			AssetInfo assetInfoGot = msgObj.getAssetInfo();
			
			if (assetInfoGot != null) {
				/*
				 * int denomInCents=0; try{ log.info("Denom
				 * Got....:"+assetInfoGot.getDenominaton());
				 * denomInCents=Integer.parseInt(assetInfoGot.getDenominaton().trim());
				 * }catch (Exception e) { log.error("Error in getting denom
				 * ..."); denomInCents=0; }
				 * printDTO.setSlotDenomination(KeypadConvertUtil.centsToDollar(denomInCents));
				 */
				try {
					String denomGotInDollars = assetInfoGot.getDenominaton().trim();
					log.info("Denom got from Asset Info for slot :" + denomGotInDollars);
					Double denomInDouble = null;
					if (denomGotInDollars != null
							&& !((denomGotInDollars.trim() + " ").equalsIgnoreCase(" "))) {
						denomInDouble = new Double(denomGotInDollars);
					} else {
						log.info("Denom null from asset info for slot denom so setting default 0.00 ");
						denomInDouble = new Double("0.00");
					}
					printDTO.setSlotDenomination(denomInDouble);
					try {
						processSession.setGmuDenomination(KeypadConvertUtil.dollarToCents(denomInDouble));
					} catch (Exception e) {

					}

					log.info("Slot denom in dollar:" + printDTO.getSlotDenomination());
				} catch (Exception e) {
					log.info("Error in Denom getting from asset info....");
				}
			} else {
				log.info("Asset Info null in getting denomination.");
			}
		}

		printDTO.setSiteId(siteNumber);

		if (processSession.getWinningCombination() != null
				&& !((processSession.getWinningCombination().trim() + " ").equalsIgnoreCase(" "))) {
			printDTO.setWinningComb(processSession.getWinningCombination());
		}

		printDTO.setJackpotType(getJackpotType(processSession.getJackpotProcessFlagId(), processSession.getJackpotTypeId(), langCode));

		if (printDTO.getJackpotType() != null) {
			processSession.setJackpotTypeDesc(printDTO.getJackpotType());
		}
		return printDTO;
	}

	/**
	 * Method to set the field values in JackpotDTO to the PrintDTO fields for CHECK schema 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	public static PrintDTO setCheckDTOValues(Integer siteNumber, ProcessSession processSession)
			throws JackpotEngineServiceException {
		PrintDTO printDTO = new PrintDTO();
		String langCode = null;
		try {
			langCode = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_LANG_CODE);
		} catch (Exception e) {
			langCode = null;
		}

		if (!(langCode != null && !((langCode.trim() + " ").equalsIgnoreCase(" ")))) {
			langCode = JACKPOT_ENGINE_EXT_LANGUAGE_DEFAULT;// default language;
		}

		printDTO.setSiteId(siteNumber);
		printDTO.setLangSelected(langCode);
		processSession.setLanguageSelectedHere(langCode);
		// Setting jackpot slip/sequence number
		printDTO.setJackpotSlipNumber(Long.toString(processSession.getSequenceNumber()));
		String transactionDate = getDateString(new Date(), SLIP_DATE_FORMAT);
		printDTO.setDate(transactionDate);
		if(processSession.getPlayerName() != null) {
			printDTO.setPay(processSession.getPlayerName());
		}
		String currencySymbol = JackpotUtil.getSiteParamValue(SITE_CURRENCY_SYMBOL, siteNumber);
		if(processSession.getJpHitAmount() != 0) {
			printDTO.setAmount(currencySymbol
					+ new DecimalFormat("0.00").format(KeypadConvertUtil.centsToDollar(processSession
							.getJpHitAmount())));
		}
		if(processSession.getAcnfNumber() != null) {
			printDTO.setMachineNo(processSession.getAcnfNumber());
		}
		if(processSession.getAcnfLocation() != null) {
			printDTO.setStandNo(processSession.getAcnfLocation());
		}
		
		printDTO.setPayoutType(getJackpotType(processSession.getJackpotProcessFlagId(), processSession.getJackpotTypeId(), langCode));
		if (printDTO.getJackpotType() != null) {
			processSession.setJackpotTypeDesc(printDTO.getJackpotType());
		}
		return printDTO;
	}
	
	/**
	 * Convert three digits to a text string
	 * 
	 * @param amount
	 *            value to convert
	 * 
	 * @return text of the converted digits
	 */
	private static String convertThreeDigits(int amount, String langSelected) {
		StringBuffer sb = new StringBuffer();
		int tmp = amount / KeypadLookUpConstants.HUNDREDS;

		if (tmp > 0) {
			sb.append(convertOneDigit(tmp, langSelected) + " "
					+ KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_HUNDRED, langSelected));
			amount -= (tmp * KeypadLookUpConstants.HUNDREDS);

			if (amount > 0) {
				sb.append(" ");
			}
		}

		if (amount > 0) {
			sb.append(convertTwoDigits(amount, langSelected));
		}

		return sb.toString();
	}

	/**
	 * convert one digit amount to a text representation.
	 * 
	 * @param amount
	 *            digit to convert
	 * 
	 * @return string representation of the digit
	 */
	private static String convertOneDigit(int amount, String extrnLanguage) {
		String text = null;
		switch (amount) {
		case 0:
			break;

		case 1:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_ONE, extrnLanguage);

			break;

		case 2:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TWO, extrnLanguage);

			break;

		case 3:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THREE, extrnLanguage);

			break;

		case 4:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FOUR, extrnLanguage);

			break;

		case 5:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FIVE, extrnLanguage);

			break;

		case 6:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SIX, extrnLanguage);

			break;

		case 7:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SEVEN, extrnLanguage);

			break;

		case 8:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_EIGHT, extrnLanguage);

			break;

		case 9:
			text = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_NINE, extrnLanguage);

			break;
		}

		return text;
	}

	/**
	 * Convert two digits to text representation
	 * 
	 * @param amount
	 *            value to convert
	 * 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(int amount, String langSelected) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit(amount, langSelected);
		} else if (amount == 10) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TEN, langSelected);
		} else if (amount == 11) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_ELEVEN, langSelected);
		} else if (amount == 12) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TWELVE, langSelected);
		} else if (amount == 13) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THIRTEEN, langSelected);
		} else if (amount == 14) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FOURTEEN, langSelected);
		} else if (amount == 15) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FIFTEEN, langSelected);
		} else if (amount == 16) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SIXTEEN, langSelected);
		} else if (amount == 17) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SEVENTEEN, langSelected);
		} else if (amount == 18) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_EIGHTEEN, langSelected);
		} else if (amount == 19) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_NINETEEN, langSelected);
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TWENTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THIRTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FORTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FIFTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SIXTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SEVENTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_EIGHTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_NINETY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10, langSelected));
			}
		}

		return sb.toString();
	}

	/**
	 * Convert two digits to text representation
	 * 
	 * @param amount
	 *            value to convert
	 * 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(long amount, String langSelected) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit(((int) amount), langSelected);
		} else if (amount == 10) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TEN, langSelected);
		} else if (amount == 11) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_ELEVEN, langSelected);
		} else if (amount == 12) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TWELVE, langSelected);
		} else if (amount == 13) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THIRTEEN, langSelected);
		} else if (amount == 14) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FOURTEEN, langSelected);
		} else if (amount == 15) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FIFTEEN, langSelected);
		} else if (amount == 16) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SIXTEEN, langSelected);
		} else if (amount == 17) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SEVENTEEN, langSelected);
		} else if (amount == 18) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_EIGHTEEN, langSelected);
		} else if (amount == 19) {
			return KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_NINETEEN, langSelected);
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TWENTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THIRTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FORTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_FIFTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SIXTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_SEVENTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_EIGHTY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_NINETY, langSelected));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(((int) (amount % 10)), langSelected));
			}
		}

		return sb.toString();
	}

	/**
	 * Given an amount in pennies convert it to dollar string
	 * 
	 */
	public static String convertAmount(long amount, String langSelected) {
		String dollarsText = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_DOLLARS, langSelected);
		StringBuffer sb = new StringBuffer();
		int tmp = 0;

		if ((amount >= 100) & (amount < 200)) {
			dollarsText = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_DOLLAR, langSelected);
		}

		if (amount >= KeypadLookUpConstants.ONES) {

			/* Determine how many trillions */
			tmp = (int) (amount / KeypadLookUpConstants.TRILLIONS);
			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp, langSelected));
				sb.append(" " + KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_TRILLION, langSelected)
						+ " ");
				amount -= (tmp * KeypadLookUpConstants.TRILLIONS);
			}

			/* Determine how many billions */
			tmp = (int) (amount / KeypadLookUpConstants.BILLIONS);
			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp, langSelected));
				sb.append(" " + KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_BILLION, langSelected)
						+ " ");
				amount -= (tmp * KeypadLookUpConstants.BILLIONS);
			}

			/* Determine how many millions */
			tmp = (int) (amount / KeypadLookUpConstants.MILLIONS);
			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp, langSelected));
				sb.append(" " + KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_MILLION, langSelected)
						+ " ");
				amount -= (tmp * KeypadLookUpConstants.MILLIONS);
			}

			/* Determine how many 100's of thousands */
			tmp = (int) (amount / KeypadLookUpConstants.THOUSANDS);

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp, langSelected));
				sb.append(" " + KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_THOUSAND, langSelected)
						+ " ");
				amount -= (tmp * KeypadLookUpConstants.THOUSANDS);
			}

			tmp = (int) (amount / KeypadLookUpConstants.ONES);

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp, langSelected));
				amount -= (tmp * KeypadLookUpConstants.ONES);
			}
		} else {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_ZERO, langSelected));
		}

		sb.append(" " + dollarsText + " ");

		if (amount == 0) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_AND_NO, langSelected) + " "
					+ KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_CENTS, langSelected));
		} else if (amount == 1) {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_AND_ONE, langSelected) + " "
					+ KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_CENT, langSelected));
		} else {
			sb.append(KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_AND, langSelected) + " "
					+ convertTwoDigits(amount, langSelected) + " "
					+ KeypadUtil.getLabelValue(KeypadLabelKeyConstants.LBL_CENTS, langSelected));
		}

		return sb.toString();
	}

	/**
	 * Gets the jackpot type for the process flag Id and jackpot type Id
	 * @param processFlagId
	 * @param jackpotTypeId
	 * @return
	 * @author vsubha
	 */
	public static String getJackpotType(int processFlagId, int jackpotTypeId, String langCode) {
		
		log.info("Jackpot Keypad Util-----> Inside getJackpotType");
		String jackpotType = IKeypadProcessConstants.EMPTY_STRING;
		
		log.info("The Process id ====> "+processFlagId);
		log.info("The jackpotTypeId id ====> "+jackpotTypeId);
		log.info("The langCode ====> "+langCode);
		if(processFlagId != 0 && jackpotTypeId != 0) {
			if(processFlagId == KeypadLookUpConstants.NORMAL_PROCESS_FLAG) {
				if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_REGULAR) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_NORMAL_REGULAR, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_NORMAL_PROGRESSIVE, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_NORMAL_LINKED_PROGRESSIVE, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_SYSTEM_GAME) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_NORMAL_SYSTEM_GAME, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_NORMAL_CANCEL_CREDIT, langCode);
				}
			}  else if(processFlagId == KeypadLookUpConstants.POUCH_PAY_PROCESS_FLAG) {
				if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_REGULAR) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_REGULAR, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_SYSTEM_GAME) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_SYSTEM_GAME, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_CANCEL_CREDIT, langCode);
				} else if(jackpotTypeId== KeypadLookUpConstants.JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = KeypadUtil.getLabelValue(KeypadLabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_PROGRESSIVE, langCode);
				}
			} 
		}
		
		log.info("Jackpot Keypad Util-----> End of getJackpotType");
		return jackpotType;
	}
	
	public static String getUnderLineCharForStringLength(int strLength) {
		String str = IKeypadProcessConstants.EMPTY_STRING;
		int i = 0;
		while(i < strLength) {
			str += IKeypadProcessConstants.UNDERLINE_CHAR;
			i++;
		}
		return str;
	}
	
	public static String getSpacesForStringLength(int strLength) {
		String str = IKeypadProcessConstants.EMPTY_STRING;
		int i = 0;
		while(i < strLength) {
			str += IKeypadProcessConstants.SIGLE_SPACE;
			i++;
		}
		return str;
	}
	
	public static int postJackpotAdjustementToMarketing(ProcessSession processSession,Long msgId) throws JackpotEngineServiceException{
	{
		//BY DEFAULT PT 11 SHOULD BE SENT FOR A VOID TRANSACTION
		int response = 0;
		try {
			if(log.isInfoEnabled()) {
				log.info("BEFORE SENDING processJackpotAdjust FACADE to marketing engine");
				log.info("Site Id    : " + processSession.getSiteId());
				log.info("Org Card no: " + processSession.getPlayerCard());
				log.info("New Card no: " + processSession.getPlayerCard());
				log.info("Old Amt    : " + processSession.getJpOriginalAmount());
				log.info("New Amt    : " + processSession.getJpHitAmount());
				log.info("Slot no    : " + processSession.getAcnfNumber());
				log.info("Msg Id     : " + msgId);	
			}
			IMarketingBO marketingBO = (IMarketingBO) ServiceLocator.fetchService(LOOKUP_SDS_MARKETING_BO, IS_LOCAL_LOOKUP);
			response = marketingBO.processJackpotAdjust(processSession.getSiteId(), processSession.getPlayerCard(),
					processSession.getPlayerCard(), processSession.getJpOriginalAmount(), processSession.getJpHitAmount(),
					processSession.getAcnfNumber(), msgId);
			if(log.isInfoEnabled()) {
				log.info("AFTER SENDING processJackpotAdjust + RESPONSE: "+response);
			}
		} catch (MarketingDAOException e) {
			log.error("Exception in processJackpotAdjustment Marketing Method",e);
			throw new JackpotEngineServiceException(e);
		} catch (Exception e) {
			log.error("Exception in processJackpotAdjustment Marketing Method",e);
			throw new JackpotEngineServiceException(e);
		}	
		return response;
			
	}
}
	
}
