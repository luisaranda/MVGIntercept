/*****************************************************************************
 * $Id: JackpotFacade.java,v 1.236.1.1.1.6.1.0.3.5, 2013-08-16 11:00:32Z, Anbuselvi, Balasubramanian$
 * $Date: 8/16/2013 6:00:32 AM$
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
package com.ballydev.sds.jackpot.facade;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_DAEMON_FACADE;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_MARKETING_FACADE;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_PROGRSSIVE_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_SLIPPROCESS_BO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.ballydev.sds.accounting.bo.ISlipProcessorBO;
import com.ballydev.sds.accounting.facade.IDaemonFacade;
import com.ballydev.sds.alertsengine.bo.IAlertsBO;
import com.ballydev.sds.alertsengine.exception.AlertsEngineBOException;
import com.ballydev.sds.ana.bo.IAnABO;
import com.ballydev.sds.ana.exception.AnAException;
import com.ballydev.sds.asset.bo.IAssetBO;
import com.ballydev.sds.asset.dto.AssetInfoDTO;
import com.ballydev.sds.asset.dto.AssetParamType;
import com.ballydev.sds.asset.dto.GameDetails;
import com.ballydev.sds.asset.exception.AssetEngineServiceException;
import com.ballydev.sds.asset.util.AssetSearchFilter;
import com.ballydev.sds.common.IHeader;
import com.ballydev.sds.common.IRequest;
import com.ballydev.sds.common.IStandardData;
import com.ballydev.sds.common.jackpot.ICreditKeyOffBlock;
import com.ballydev.sds.common.jackpot.IHandPaidJackpot;
import com.ballydev.sds.common.jackpot.IJackPotSlipProcessingMessage;
import com.ballydev.sds.common.jackpot.IJackpotClear;
import com.ballydev.sds.common.jackpot.IJackpotToCreditMeter;
import com.ballydev.sds.common.message.AssetInfo;
import com.ballydev.sds.common.message.JackpotAlertObject;
import com.ballydev.sds.common.message.SDSMessage;
import com.ballydev.sds.common.message.SystemGeneratedMessage;
import com.ballydev.sds.common.response.IStandardResponse;
import com.ballydev.sds.common.security.IEmployeeCardIn;
import com.ballydev.sds.common.systemtogmu.ResetHandPaidJackpotMessage;
import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.ecash.dto.account.SDSAccessDTO;
import com.ballydev.sds.ecash.facade.IEcashFacade;
import com.ballydev.sds.employee.biz.dto.SessionInfoDTO;
import com.ballydev.sds.employee.bo.IEmployeeBO;
import com.ballydev.sds.employee.enumconstants.SessionStateEnum;
import com.ballydev.sds.employee.web.form.AnAUserForm;
import com.ballydev.sds.employee.web.form.FunctionForm;
import com.ballydev.sds.employee.web.form.ParameterForm;
import com.ballydev.sds.jackpot.bo.IJackpotBO;
import com.ballydev.sds.jackpot.constants.IAlertMessageConstants;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ICommonConstants;
import com.ballydev.sds.jackpot.constants.IJackpotIds;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotEngineAlertObject;
import com.ballydev.sds.jackpot.dto.JackpotGameDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotLiabilityDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.dto.JackpotResetDTO;
import com.ballydev.sds.jackpot.dto.NewWaveRequestDTO;
import com.ballydev.sds.jackpot.dto.NewWaveResponseDTO;
import com.ballydev.sds.jackpot.dto.SlipInfoDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpot.keypad.KeypadJackpotProcessor;
import com.ballydev.sds.jackpot.keypad.SessionManagerDAO;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.AccountingUtil;
import com.ballydev.sds.jackpot.util.ConversionUtil;
import com.ballydev.sds.jackpot.util.JackpotHelper;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;
import com.ballydev.sds.jackpot.util.MessageUtil;
import com.ballydev.sds.jackpot.util.XMLServerUtil;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.marketing.bo.IMarketingBO;
import com.ballydev.sds.marketing.dto.PlayerDetailsDTO;
import com.ballydev.sds.marketing.exception.MarketingDAOException;
import com.ballydev.sds.marketing.facade.IMarketingFacade;
import com.ballydev.sds.messaging.service.IMessagingEngineFacade;
import com.ballydev.sds.progressive.bo.IProgressiveBO;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.SDSPrimaryKey;
import com.ballydev.sds.utils.common.props.IPrimaryKeyConstants;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;
import com.ballydev.sds.voucher.facade.IVoucherFacade;
import com.barcodelib.barcode.Barcode;

/**
 * Stateless Session Bean Implementation for JackpotFacade Interface
 * @author dambereen
 * @version $Revision: 253$
 */
@Stateless
@Local(IJackpotFacade.class)
public class JackpotFacade implements IJackpotFacade, IAppConstants{
	
	/**
	 * Container injects the IJackpotFacade Object
	 * using internal JNDI lookup
	 */
	@EJB
	private IJackpotBO jackpotBO;
	
	/**
	 * Asset Engine BO interface
	 */
	@EJB
	private IAssetBO assetBO;	
	
	/**
	 * Employee Engine BO interface
	 */
	@EJB
	private IAnABO anaBO;
	
	/**
	 * Employee Engine BO interface
	 */
	@EJB
	private IEmployeeBO employeeBO;
		
	/**
	 * Marketing Engine BO interface
	 */
	@EJB
	private IMarketingBO marketingBO;
	
	/**
	 * Marketing Engine Facade interface
	 */
	@EJB
	private IMarketingFacade marketingFacade = (IMarketingFacade) ServiceLocator.fetchService(LOOKUP_SDS_MARKETING_FACADE, IS_LOCAL_LOOKUP);
	
	/**
	 * Alerts Engine BO interface
	 */
	@EJB
	private IAlertsBO alertsBO;
	
	/**
	 * Messaging Engine facade
	 */
	@EJB
	private IMessagingEngineFacade messagingEngineFacade;
	
	@EJB
	private IVoucherFacade voucherEngineFacade;
		
	/**
	 * Accounting Daemon BO instance
	 */
	private ISlipProcessorBO slipProcBO = (ISlipProcessorBO) ServiceLocator.fetchService(LOOKUP_SDS_SLIPPROCESS_BO, IS_LOCAL_LOOKUP);
	
	/**
	 * eCash BO instance
	 */
	@EJB
	IEcashFacade eCashFacade;
	
	/**
	 * Proogressive Engine BO instance
	 */
	IProgressiveBO progressiveBO = (IProgressiveBO) ServiceLocator.fetchService(LOOKUP_SDS_PROGRSSIVE_BO, IS_LOCAL_LOOKUP);
	
	/**
	 * General Logger instance
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Process statistics logger instance
	 */
	private static Logger logger = JackpotEngineLogger.getProcessLogger(IAppConstants.LOGGER_PROCESS_STATISTICS);
	
	/**
	 * BO statistics logger instance
	 */
	private static Logger logBOStatistics = JackpotEngineLogger.getBOLogger(IAppConstants.LOGGER_BO_STATISTICS);
		
	/**
	 * Method to that is called by the Messaging Server when a JP Exception code arrives
	 * @param sdsMessage
	 * @return SDSMessage
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public SDSMessage preProcessMessage(SDSMessage sdsMessage) throws Exception {
		long startTime, endTime;
		startTime = Calendar.getInstance().getTime().getTime();

		IHeader iHeaderObj = sdsMessage.getMessage();
		log.info("TestEngineProcessor: processMessage() invoked, iHeaderObj="+iHeaderObj);
		if (iHeaderObj instanceof IHandPaidJackpot){
			log.info("********START OF HAND PAID JACKPOT EVENT********");
			preProcessHandPaidJackpotMsg(sdsMessage);
		} else if (iHeaderObj instanceof IJackpotToCreditMeter) {
			log.info("********START OF JACKPOT TO CREDIT METER********");
			preProcessJackpotToCreditMeterMsg(sdsMessage);
		} else if (iHeaderObj instanceof ICreditKeyOffBlock) {
			log.info("********START OF CREDIT KEY OFF AUTH BLOCK********");
			preProcessCreditKeyOffAuthBlock(sdsMessage);
			log.info("********END OF CREDIT KEY OFF AUTH BLOCK********");
		} else if (iHeaderObj instanceof IJackpotClear) {
			log.info("********START OF JACKPOT CLEAR********");
			preProcessJackpotClearMsg(sdsMessage);
			log.info("********END OF JACKPOT CLEAR********");
		} else if (iHeaderObj instanceof IJackPotSlipProcessingMessage) {
			log.info("********START OF JACKPOT KEYPAD PROCESS********");
			preProcessJackpotKeypadProcess(sdsMessage);
			// preProcessJackpotKeypadProcessDummy(sdsMessage);
			log.info("********END OF JACKPOT KEYPAD PROCESS********");
		}
		if(logger.isDebugEnabled()) {
			endTime = Calendar.getInstance().getTime().getTime(); 
			logger.debug("PRE-PROCESS TIME FOR XC:"+ Integer.toHexString(((IRequest)sdsMessage.getMessage()).getExceptionCode())+", MESSAGE ID:"+sdsMessage.getMessage().getMessageId()+" = "+((double)endTime-(double)startTime)/1000);
		}
		return sdsMessage;
	}
	
	/**
	 * Method for processing jackpot using keypad
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void  preProcessJackpotKeypadProcess(SDSMessage sdsMessage)throws Exception{
		IHeader msgObj = (IHeader) sdsMessage.getMessage();
		IRequest requestObj = (IRequest) msgObj; 
		if(requestObj.isDuplicateMessage()) {
			if(log.isInfoEnabled()) {
				log.info("Duplicate Message in Keypad processing, hence returned");				
			}
			IHeader iHeaderObj = sdsMessage.getMessage();
			IHeader pcObj = iHeaderObj;

			((IStandardResponse) pcObj).setResponseCode((char) 0xA0);
			if (log.isInfoEnabled()) {
				log.info("Success Response... " + Integer.toHexString((int) 0xA0));
			}
			((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
			return;
		} 
		KeypadJackpotProcessor keypadJackpotProcessor = new KeypadJackpotProcessor();
		if (requestObj.isJackPotSlipInitMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 1 INIT MESSAGE ********");			
			keypadJackpotProcessor.processInitMessage(msgObj);			
		}else if (requestObj.isJackPotSlipAuthorizationMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 2 SLIP DETAILS MESSAGE********");
			keypadJackpotProcessor.processAuthorizationMessage(msgObj);			
		} else if (requestObj.isJackPotSlipFinalMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 3 SLIP PRINTER DETAIL MESSAGE********");
			// SITE CONFIG PARAMETER FOR CASHIER DESK PROCESS
			String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
					ISiteConfigConstants.IS_CASHIER_DESK_ENABLED,
					ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP }), msgObj.getSiteId());
			String cashierDeskEnabled = siteParamValues[0];
			String enableCheckPrint = siteParamValues[1];
			keypadJackpotProcessor.processFinalMessage(msgObj, cashierDeskEnabled, enableCheckPrint);
		}		
	}
	
	/**
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void  preProcessJackpotKeypadProcessDummy(SDSMessage sdsMessage)throws Exception{
		IHeader msgObj = (IHeader) sdsMessage.getMessage();
		IRequest requestObj = (IRequest) msgObj;
		KeypadJackpotProcessor keypadJackpotProcessor = new KeypadJackpotProcessor();
		if (requestObj.isJackPotSlipInitMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 1 INIT MESSAGE ********");			
			keypadJackpotProcessor.processInitMessageDummy(msgObj);			
		}else if (requestObj.isJackPotSlipAuthorizationMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 2 SLIP DETAILS MESSAGE********");
			keypadJackpotProcessor.processAuthorizationMessageDummy(msgObj);
		}else if (requestObj.isJackPotSlipFinalMessage()) {
			log.info("********JACKPOT KEYPAD PROCESS - 3 SLIP PRINTER DETAIL MESSAGE********");
			keypadJackpotProcessor.processFinalMessageDummy(msgObj);
		}
	}
		
	/**
	 * Method to that is called by the Messaging Server after the JP Exception code's response is sent back
	 * @param sdsMessage
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void postProcessMessage(SDSMessage sdsMessage)throws Exception 
	{
		long startTime, endTime;
		startTime = Calendar.getInstance().getTime().getTime();	
		IHeader iHeaderObj = sdsMessage.getMessage();
		if(iHeaderObj.isDuplicateMessage()){
			return;
		}
		else if (iHeaderObj instanceof IHandPaidJackpot){
			postProcessHandPaidJackpotMsg(sdsMessage);
			log.info("********END OF HAND PAID JACKPOT EVENT********");
		}
		else if (iHeaderObj instanceof IJackpotToCreditMeter){
			postProcessJackpotToCreditMeterMsg(sdsMessage);
			log.info("********END OF JACKPOT TO CREDIT METER********");
		}
		else if(iHeaderObj instanceof IEmployeeCardIn){
			log.info("********POST PROCESS EMPLOYEE CARD IN EVENT ON JACKPOT ENGINE********");
			postProcessEmployeeCardInMsg(sdsMessage);
			log.info("********END OF POST PROCESS EMPLOYEE CARD IN EVENT ON JACKPOT ENGINE********");
		}
		if(logger.isDebugEnabled()) {
			endTime = Calendar.getInstance().getTime().getTime();
			logger.debug("POST-PROCESS TIME FOR XC:"+ Integer.toHexString(((IRequest)sdsMessage.getMessage()).getExceptionCode())+", MESSAGE ID:"+sdsMessage.getMessage().getMessageId()+" = "+((double)endTime-(double)startTime)/1000);
		}
	}

	/**
	 * Method that does the pre processing for a HandPaidJackpotMsg
	 * @param sdsMessage
	 * @return
	 * @throws Exception
	 */
	public SDSMessage preProcessHandPaidJackpotMsg(SDSMessage sdsMessage) throws Exception {
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj=iHeaderObj;		
		JackpotDTO jackpotDTO = new JackpotDTO();
		
		char successJackpotE0 = 0xE0;
		char failure = 0xB0;
		
		try {
			if(log.isInfoEnabled()){
				log.info("Duplicate Msg Check : "+((IHandPaidJackpot)pcObj).isDuplicateMessage());
				log.info("Duplicate Msg Id : "+((IHandPaidJackpot)pcObj).getDuplicateMessageId());
				log.info("Asset Config No : "+((IHandPaidJackpot)pcObj).getAssetConfigNumber());	
				log.info("Msg Id : " +((IHandPaidJackpot)pcObj).getMessageId());
				log.info("Player card no : " +((IHandPaidJackpot)pcObj).getPlayerCardNumber());
				log.info("Jackpot Amt : " +((IHandPaidJackpot)pcObj).getJackpotAmount());
				log.info("Jackpot Id Hex Str: " +Integer.toHexString((int)((IHandPaidJackpot)pcObj).getJackpotId()));
				log.info("Jackpot Id : " +(int)((IHandPaidJackpot)pcObj).getJackpotId());
				log.info("GMU denomination : " +((IHandPaidJackpot)pcObj).getGmuDenom());		
				log.info("Site Id : " +((IHandPaidJackpot)pcObj).getSiteId());
				log.info("Employee Card: " +((IHandPaidJackpot)pcObj).getEmployeeCardNumber());
				log.info("Door Status: " +((IHandPaidJackpot)pcObj).getDoorStatus());
			}
			
			//LOGIC CHK WHETHER PROG CONTROLLER NOTIFICATION IS ENABLED FOR CREATING THE PROG JP
			//IF ENABLED IGNORE THE CURRENT PROG JP AND SET DEF +VE RESP, ELSE CARRY ON
			if(!JackpotUtil.isJackpotSlotFloorEventHandlingRequired(((IHandPaidJackpot)pcObj).getAssetConfigNumber(), 
					((IHandPaidJackpot)pcObj).getSiteId(), ((IHandPaidJackpot) pcObj).getJackpotId())){
				
				log.info("PROGRESSIVE CONTROLLER JP NOTIFICATION ENABLED - IGNORE CURRENT JACKPOT");				
				((IStandardResponse)pcObj).setResponseCode(successJackpotE0);
				((IStandardResponse)pcObj).setResponseData(setJackpotResponseData(((IHandPaidJackpot) pcObj)));
				return sdsMessage;
			}
						
			if (((IHandPaidJackpot) pcObj).isDuplicateMessage()) {
				if (log.isInfoEnabled()) {
					log.info("DUPLICATE MSG - XC 10");
				}
				long startTime, endTime = 0;
				startTime = Calendar.getInstance().getTime().getTime();

				// LOCK THE JACKPOT SLOT TABLE
				boolean jpSlotLockResp = jackpotBO.lockJackpotSlotTable(
						((IHandPaidJackpot) pcObj).getAssetConfigNumber(),
						((IHandPaidJackpot) pcObj).getSiteId());
				log.debug("jpSlotLockResp :" + jpSlotLockResp);

				// CHECK FOR DUPLICATE RESPONSE
				jackpotDTO = jackpotBO.eventDuplicateMsgCheckGetResponse(((IHandPaidJackpot) pcObj)
						.getDuplicateMessageId());
				if (logBOStatistics.isDebugEnabled()) {
					endTime = Calendar.getInstance().getTime().getTime();
					logBOStatistics
							.debug("TIME TAKEN TO PROCESS eventDuplicateMsgCheckGetResponse() FOR XC 10 = "
									+ ((double) endTime - (double) startTime) / 1000 + " for MESSAGE ID:"
									+ sdsMessage.getMessage().getMessageId());
				}
				if (jackpotDTO != null) {
					if (jackpotDTO.isDuplicateMsg()) {
						if (log.isInfoEnabled()) {
							log.info("Transaction resp for XC 10: " + jackpotDTO.getResponseCode());
						}
						if (jackpotDTO.getResponseCode().equalsIgnoreCase(XC10_POSITIVE_RESPONSE_CODE)) {
							if (log.isDebugEnabled()) {
								log.debug("Setting DUP +ve response code: " + successJackpotE0);
							}
							((IStandardResponse) pcObj).setResponseCode(successJackpotE0);
							((IStandardResponse) pcObj)
									.setResponseData(setJackpotResponseData(((IHandPaidJackpot) pcObj)));
						} else if (jackpotDTO.getResponseCode().equalsIgnoreCase(GEN_NEGATIVE_RESPONSE_CODE)) {
							((IStandardResponse) pcObj).setResponseCode(failure);
							((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
						}
					} else {
						if (log.isInfoEnabled()) {
							log.info("No Duplicate Event logged for XC 10, calling createPendingJpSlips method");
						}
						jackpotDTO = createPendingJackpotSlip(jackpotDTO, sdsMessage);
						if (jackpotDTO != null) {
							if (jackpotDTO.isPostedSuccessfully()) {
								/** SET THE SUCCESS RESPONSE */
								if (jackpotDTO.getResponseCode()
										.equalsIgnoreCase(XC10_POSITIVE_RESPONSE_CODE)) {
									if (log.isDebugEnabled()) {
										log.debug("Setting +ve response code: " + successJackpotE0);
									}
									((IStandardResponse) pcObj).setResponseCode(successJackpotE0);
								} else {
									((IStandardResponse) pcObj).setResponseCode(failure);
								}
								((IStandardResponse) pcObj)
										.setResponseData(setJackpotResponseData(((IHandPaidJackpot) pcObj)));
								if (log.isInfoEnabled()) {
									log.info("Success Response For XC 10...Updated the DB: "
											+ Integer.toHexString((int) successJackpotE0));
								}
							} else {
								/** SET THE FAILURE RESPONSE */
								((IStandardResponse) pcObj).setResponseCode(failure);
								((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
								if (log.isInfoEnabled()) {
									log.info("Failure Response For XC 10... "
											+ Integer.toHexString((int) failure));
								}
							}
						}
					}
				}
			} else {
				if(log.isInfoEnabled()) {
					log.info("Normal Event, no duplicates");
				}
				
				//LOCK THE JACKPOT SLOT TABLE				
				boolean jpSlotLockResp = jackpotBO.lockJackpotSlotTable(((IHandPaidJackpot)pcObj).getAssetConfigNumber(), ((IHandPaidJackpot)pcObj).getSiteId());
				if(log.isDebugEnabled()) {
					log.debug("jpSlotLockResp :" + jpSlotLockResp);
				}
				
				//CREATE THE PENDING JACKPOT
				jackpotDTO = createPendingJackpotSlip(jackpotDTO, sdsMessage);	
				if(jackpotDTO!=null){
					if (jackpotDTO.isPostedSuccessfully()){
						/** SET THE SUCCESS RESPONSE */
						if(jackpotDTO.getResponseCode().equalsIgnoreCase(XC10_POSITIVE_RESPONSE_CODE)){
							if(log.isDebugEnabled()) {
								log.debug("Setting +ve response code: "+successJackpotE0);
							}
							((IStandardResponse)pcObj).setResponseCode(successJackpotE0);
						}							
						else{
							((IStandardResponse)pcObj).setResponseCode(failure);
						}
				
						((IStandardResponse)pcObj).setResponseData(setJackpotResponseData(((IHandPaidJackpot)pcObj)));
						if(log.isInfoEnabled()) {
							log.info("Success Response For XC 10...Updated the DB: " + Integer.toHexString((int)successJackpotE0));
						}
					}
					else{
						/** SET THE FAILURE RESPONSE */
						((IStandardResponse)pcObj).setResponseCode(failure);					
						((IStandardResponse)pcObj).setResponseData(new char[]{0x00,0x00});
						if(log.isInfoEnabled()) {
							log.info("Failure Response For XC 10... " + Integer.toHexString((int)failure));
						}
					}
				}				
			}
			
			/** CALLING MARKETING ENGINE'S METHOD TO STORE THE XC - 10 INFO 
			 * EXCEPT FOR A CANCEL CREDIT JP DO NOT SEND PT10*/			
			if(((int)((IHandPaidJackpot)pcObj).getJackpotId())!=IJackpotIds.JACKPOT_ID_254_FE) {
				if(log.isInfoEnabled()) {
					log.info("Before calling Marketing's preProcessHandPaidJackpot method");
				}
				marketingFacade.preProcessHandPaidJackpot(sdsMessage);			
				if(log.isInfoEnabled()) {
					log.info("After calling Marketing's preProcessHandPaidJackpot method");
				}			
			}			
			
		} catch (Exception e) {
			/** SET THE FAILURE RESPONSE */
			((IStandardResponse)pcObj).setResponseCode(failure);
			((IStandardResponse)pcObj).setResponseData(new char[]{0x00,0x00});
			log.error("EXCEPTION : Failure Response For XC 10..." + Integer.toHexString((int)failure));
			log.error("Exception in the jackpot Processor for HAND PAID JACKPOT", e);
		}
		
		if(jackpotDTO!=null && jackpotDTO.isSendAlert()){
			if(log.isInfoEnabled()) {
				log.info("Session is set for asset loc and post success");
			}
			sdsMessage.getSession().put(IAppConstants.SESS_ASSET_LOC, jackpotDTO.getAssetConfigLocation());
			
			if(jackpotDTO.isSlotDoorOpen()){
				if(log.isInfoEnabled()) {
					log.info("Session is set for Slot Door Open");
				}
				sdsMessage.getSession().put(IAppConstants.SESS_SLOT_DOOR_OPEN, "true");
			}			
			if(jackpotDTO.isPostedSuccessfully()){
				sdsMessage.getSession().put(IAppConstants.SESS_JP_POST_SUCCESS, "true");
				sdsMessage.getSession().put(IAppConstants.SESS_JP_SEQUENCE_NO, jackpotDTO.getSequenceNumber());
				if(log.isInfoEnabled()) {
					log.info("SESS_JP_POST_SUCCESS is set as true");
				}			
			}		
		}	
		
		//Void Voucher When Cancel Credit and ALLOW.VOID.VOUCHER.OF.FE.JACKPOT is enabled
		try{
			String voidVoucherEnabled = JackpotUtil.getSiteParamValue(
					ISiteConfigConstants.IS_ALLOW_VOID_VOUCHER_ENABLED,
					((IHandPaidJackpot) pcObj).getSiteId());
			if(log.isInfoEnabled()){
				log.info("voidVoucherEnabled:" + voidVoucherEnabled);
				log.info("Jackpot Id:" + ((int)((IHandPaidJackpot)pcObj).getJackpotId()));
			}
			if(voidVoucherEnabled != null && voidVoucherEnabled.equalsIgnoreCase("Yes")
					&& ((int)((IHandPaidJackpot)pcObj).getJackpotId()) == IJackpotIds.JACKPOT_ID_254_FE){
				if(log.isInfoEnabled()){
					log.info("*********Before Calling voidHandpayDuplicateVoucher*********");
					log.info("Slot Number:" + ((IHandPaidJackpot)pcObj).getAssetConfigNumber());
					log.info("Jackpot Amount:" + ((IHandPaidJackpot)pcObj).getJackpotAmount());
					log.info("Site Id:" + ((IHandPaidJackpot) pcObj).getSiteId());
				}
				boolean voidVoucher = false;
				voidVoucher = voucherEngineFacade.voidHandpayDuplicateVoucher(((IHandPaidJackpot)pcObj).
							getAssetConfigNumber(), ((IHandPaidJackpot)pcObj).getJackpotAmount(), ((IHandPaidJackpot) pcObj).getSiteId());
				
				if(log.isInfoEnabled()){
					log.info("voidVoucher Result:" + voidVoucher);
					log.info("*********End of Calling voidHandpayDuplicateVoucher*********");
				}
				
			}
		}catch (Exception e) {
			log.error("Exception while calling voucherEngineFacade.voidHandpayDuplicateVoucher " + e);
		}
		
		if(log.isInfoEnabled()) {
			log.info("XC-10 ReponseCode to Transaction: "+Integer.toHexString((int)((IStandardResponse)pcObj).getResponseCode()));		
			log.info("XC-10 ReponseData to Transaction: "+(int)(((IStandardResponse)pcObj).getResponseData()[0])+" , "+(int)(((IStandardResponse)pcObj).getResponseData()[1]));
		}
		return sdsMessage;
	}
	
	/**
	 * Method that does an asset call to get the asset config loc and checks the slot status is online/cet, also checks if the
	 * slot door is open, if yes - send a b0 resp
	 * else a pending jp slip is created 
	 * @param jackpotDTO
	 * @param sdsMessage
	 * @return JackpotDTO
	 */
	public JackpotDTO createPendingJackpotSlip(JackpotDTO jackpotDTO, SDSMessage sdsMessage) {		
		if(log.isInfoEnabled()) {
			log.info("Inside createPendingJackpotSlip method");
		}
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj = iHeaderObj;
		try{
			
			long startTime, endTime =0;
			AssetInfo assetInfoDTO = ((IHandPaidJackpot)pcObj).getAssetInfo();
						
			String slotStatus = null;
			if(assetInfoDTO != null){
				if(log.isDebugEnabled()) {
					log.debug("AssetInfoDTO is not null");
					/** SET THE ASSET DETAILS TO PASS IT TO CREATE THE PENDING SLIP */
					log.debug("slot no: " + ((IHandPaidJackpot) pcObj).getAssetConfigNumber());
					log.debug("Stand no: " + assetInfoDTO.getAssetConfigLocation());
					log.debug("Regulatory Id : " + assetInfoDTO.getRegulatoryId());
					log.debug("Acnf id: " + assetInfoDTO.getAssetConfigId());
					log.debug("Area Long Name: " + assetInfoDTO.getAreaLongName());
					log.debug("Slot Denom: " + assetInfoDTO.getDenominaton());
					log.debug("Type Desc: " + assetInfoDTO.getTypeDesc());
					log.debug("Status: " + assetInfoDTO.getAssetConfigStatus());
					log.debug("Is Slot Online : " + pcObj.isSlotOnline());
					log.debug("Cardless Account Number : " + ((IHandPaidJackpot)pcObj).getTaggedStatusCardlessAccountNumber());
					log.debug("Cashless Account Type : " + JackpotHelper.getECashAccountAccessType(
							((IHandPaidJackpot)pcObj).getTaggedStatusCardlessAccountType()));
				}
				jackpotDTO.setAssetConfigNumber(((IHandPaidJackpot)pcObj).getAssetConfigNumber());
				jackpotDTO.setAssetConfigLocation(assetInfoDTO.getAssetConfigLocation());
				jackpotDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				sdsMessage.getSession().put(IAppConstants.SESS_ASSET_LOC, jackpotDTO.getAssetConfigLocation());
				jackpotDTO.setAreaLongName(assetInfoDTO.getAreaLongName());
				jackpotDTO.setAreaShortName(assetInfoDTO.getAreaName());		
				jackpotDTO.setSlotDenomination(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getDenominaton()));
				jackpotDTO.setSlotDescription(assetInfoDTO.getTypeDesc());	
				jackpotDTO.setSlotOnline(pcObj.isSlotOnline());
				jackpotDTO.setAccountNumber(((IHandPaidJackpot)pcObj).getTaggedStatusCardlessAccountNumber());
				// GETING ASSOCIATED PLAYER CARD NUMBER 
				String playerCardNumber = ((IHandPaidJackpot)pcObj).getPlayerCardNumber();
				if(log.isDebugEnabled()) {
					log.debug("Player Card Number : " + playerCardNumber);
				}
				if (jackpotDTO.getAccountNumber() != null && !jackpotDTO.getAccountNumber().trim().isEmpty()
						&& !jackpotDTO.getAccountNumber().trim().equals("0")) {
					jackpotDTO.setAssociatedPlayerCard(getPlayerCardForCashlessAccNo(
							((IHandPaidJackpot) pcObj).getSiteId(), jackpotDTO.getAccountNumber()));
					jackpotDTO.setCashlessAccountType(JackpotHelper
							.getECashAccountAccessType(((IHandPaidJackpot) pcObj)
									.getTaggedStatusCardlessAccountType()));
				} else {
					jackpotDTO.setAssociatedPlayerCard(playerCardNumber);
				}
				
				jackpotDTO.setSiteId(((IHandPaidJackpot)pcObj).getSiteId());
												
				slotStatus = assetInfoDTO.getAssetConfigStatus();
				
				boolean doorStResp = false;	 
				
				if (sdsMessage.getS2sMessage() != null) {
					if(log.isDebugEnabled()) {
						log.debug("createPendingJackpotSlip(): Got a S2SMessage.");
					}
					// Door Status field doesn't come with S2S Message
					// Always set the door status to closed
					doorStResp = false;
				} else {
					/** SLOT DOOR STATUS - OPEN CALCULATION */
					String doorStatusStr = Integer.toHexString((int)((IHandPaidJackpot)pcObj).getDoorStatus());				
					if(doorStatusStr.length()==1) {					
						// Make sure that the DoorStatus field is 2 digits
						// Example: Change "1" to "01"
						doorStatusStr = "0" + doorStatusStr;					
					}
					if(log.isDebugEnabled()) {
						log.debug("DoorStatusStr : " +doorStatusStr );
						log.debug("DoorStatusStr subStr: "+doorStatusStr.substring(0,1));
					}
					int doorStatusIntVal = Integer.parseInt(doorStatusStr.substring(0,1),16);
					
					doorStResp = isSlotDoorOpen(doorStatusIntVal);
				}
				
				/**
				 * Check if the slot status is ONLINE/CET in asset db
				 * Else send a failure response
				 */
				if(slotStatus!=null && (!slotStatus.equalsIgnoreCase(IAppConstants.ASSET_ONLINE_STATUS)
						&& !slotStatus.equalsIgnoreCase(IAppConstants.ASSET_CET_STATUS)))
				{		
					if(log.isInfoEnabled()) {
						log.info("SLOT STATUS not online/cet");
					}
					jackpotDTO.setInvalidPendingJP(true);					
					sdsMessage.getSession().put(IAppConstants.SESS_SLOT_STATUS, slotStatus);
				}
				else if(doorStResp){
					/** CHECK IF THE SLOT DOOR IS OPEN */ 
					if(log.isInfoEnabled()) {
						log.info("SLOT DOOR IS OPEN");
					}
					jackpotDTO.setSlotDoorOpen(true);
					jackpotDTO.setInvalidPendingJP(true);	
					
				} else if (((IHandPaidJackpot) pcObj).getJackpotAmount() == 0
						&& (((IHandPaidJackpot) pcObj).getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
								&& ((IHandPaidJackpot) pcObj).getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150 
								&& (((IHandPaidJackpot) pcObj).getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
								|| ((IHandPaidJackpot) pcObj).getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB
								|| ((IHandPaidJackpot) pcObj).getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD
								|| ((IHandPaidJackpot) pcObj).getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE 
								|| ((IHandPaidJackpot) pcObj).getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA))) {
					
					/** CHECK IF THE JACKPOT AMOUNT IS ZERO FOR A VALID NON PROGRESSIVE JPID **/
					if(log.isInfoEnabled()) {
						log.info("JACKPOT AMOUNT IS ZERO for a valid non progressive JPID");					
					}
					jackpotDTO.setInvalidPendingJP(true);	
				}
				/*else if(((IHandPaidJackpot)pcObj).getLastBet()==0 &&
						(((IHandPaidJackpot)pcObj).getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_149 && ((IHandPaidJackpot)pcObj).getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150 &&
						(((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_252_FC || ((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_253_FD 
								||((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_254_FE || ((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_250_FA
								|| (((IHandPaidJackpot)pcObj).getJackpotId()>=IJackpotIds.JACKPOT_ID_PROG_START_112 && ((IHandPaidJackpot)pcObj).getJackpotId()<=IJackpotIds.JACKPOT_ID_PROG_END_159)))){
					
					*//** CHECK IF THE LAST BET VALUE IS ZERO FOR A VALID JPID **//*
					log.info("LAST BET VALUE IS ZERO for a valid JPID");
					jackpotDTO.setInvalidPendingJP(true);
					
				}*/
				/**
				 * COMMENTED FOR US QA PATCH
				 * 
				 */
				/*else if(((IHandPaidJackpot)pcObj).getJackpotAmount()!=0 && !((((IHandPaidJackpot)pcObj).getJackpotAmount() / ((IHandPaidJackpot)pcObj).getGmuDenom())>0) &&
						(((IHandPaidJackpot)pcObj).getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_149 && ((IHandPaidJackpot)pcObj).getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150 &&
						(((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_252_FC || ((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_253_FD 
								||((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_254_FE || ((IHandPaidJackpot)pcObj).getJackpotId()==IJackpotIds.JACKPOT_ID_250_FA
								|| (((IHandPaidJackpot)pcObj).getJackpotId()>=IJackpotIds.JACKPOT_ID_PROG_START_112 && ((IHandPaidJackpot)pcObj).getJackpotId()<=IJackpotIds.JACKPOT_ID_PROG_END_159)))){
					
					*//** CHECK IF THE JACKPOT AMOUNT TO BE STORED IS NEGATIVE **//*
					log.info("CALCULATED JACKPOT AMOUNT is negative");
					jackpotDTO.setInvalidPendingJP(true);				
				}*/

				if(log.isInfoEnabled()) {
					log.info("calling create pending slip");
				}
				startTime = Calendar.getInstance().getTime().getTime();
				// Setting site param GENERATE_RANDOM_SEQUENCE_NUMBER to JackpotDTO to enable random sequence numbering 
				String generateRandSeqNum = JackpotUtil.getSiteParamValue(
						ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER, jackpotDTO.getSiteId());
				jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
				// GETTING PRIMARY KEY OBJECT
				SDSPrimaryKey sdsPrimaryKey = sdsMessage.getSdsPrimaryKey();
				jackpotDTO.setSlipPrimaryKey(sdsPrimaryKey.getKey(IPrimaryKeyConstants.SLIP_SLIP_REFERENCE));
				jackpotDTO = jackpotBO.createPendingJackpotSlips(((IHandPaidJackpot)pcObj), jackpotDTO, doorStResp);
				if(logBOStatistics.isDebugEnabled()) {		
					endTime = Calendar.getInstance().getTime().getTime();	
					logBOStatistics.debug("TIME TAKEN TO PROCESS createPendingJackpotSlips() FOR XC 10= "+((double)endTime-(double)startTime)/1000+" for MESSAGE ID:"+sdsMessage.getMessage().getMessageId());
				}								
			}	
			/*else {
				*//** SET THE FAILURE RESPONSE AS THE SLOT IS NOT AVAILABLE IN ASSET*//*
				log.info("AssetInfoDTO is null");
				((IStandardResponse)pcObj).setResponseCode(failure);
				((IStandardResponse)pcObj).setResponseData(new char[]{0x00,0x00});
				log.info("Failure Response For XC 10... " + Integer.toHexString((int)failure));
			}*/			
		} catch (Exception e) {
			log.error("Exception in createPendingJackpotSlip method: ",e);
		}
		return jackpotDTO;
	}
	
	/**
	 * Method to check if the slot door is opened
	 * @param doorStatus
	 * @return boolean
	 */
	private boolean isSlotDoorOpen(int doorStatus) {
		if(log.isInfoEnabled()) {
			log.info("Inside isSlotDoorOpen method");
		}
		boolean isSlotDoorOpen = false;
		if (doorStatus == 4 || doorStatus == 5 || doorStatus == 6 || doorStatus == 7 ||
				doorStatus == 12 || doorStatus == 13 || doorStatus == 14 || doorStatus == 15){
			isSlotDoorOpen = true;
		}        
		return isSlotDoorOpen;        
	}
		
	/**
	 * This method is used to check for whether the JP Keypad session needs to be cleared for the slot
	 * on a employee card in event.
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void postProcessEmployeeCardInMsg(SDSMessage sdsMessage) throws Exception {
		try {
			log.info("Inside postProcessEmployeeCardInMsg of Jackpot Facade of Jackpot Engine.");
			IHeader iHeaderObj = sdsMessage.getMessage();
			
			if (sdsMessage == null || iHeaderObj == null) {
				log.error("Aborting postProcessEmployeeCardInMsg in Jackpot Facade. iHeaderObj is null");
				return;
			}
			IEmployeeCardIn employeeCardIn = (IEmployeeCardIn)iHeaderObj;
			/**IF THE JP HAS OCCURED**/
			if (employeeCardIn.getJackpotId() != 0) {				
				if(log.isDebugEnabled()) {
					log.debug("JP id from the header object: " + employeeCardIn.getJackpotId());
				}
				int siteNumberToCheck = iHeaderObj.getSiteId();
				AssetInfo assetInfoToCheck = iHeaderObj.getAssetInfo();
				if (log.isInfoEnabled()) {
					log.info("Inside Checking Slot Session Update for Employee Card-in event for Keypad process.");
					log.info("Site Number To Check :" + siteNumberToCheck);
					log.info("Asset Config Number :" + assetInfoToCheck.getAssetConfigNumber());
				}
				SessionManagerDAO.checkAndUpdateSessionObject(assetInfoToCheck.getAssetConfigNumber(),
						siteNumberToCheck);
			} else if (log.isDebugEnabled()) {
				log.debug("No Jackpot available. Jackpot Id from the header object is 0");
			}
		} catch (Exception e) {
			log.error("Exception in postProcessEmployeeCardInMsg of Jackpot Facade. ", e);
			throw (e);
		}
	}
		
	/**
	 * Method that does the post processing for a HandPaidJackpotMsg
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void postProcessHandPaidJackpotMsg(SDSMessage sdsMessage)throws Exception
	{		
		/** 
		 * CREATING A LIST OF JackpotAlertObjects 
		*/		
		List<IHeader> alertObjectList = new ArrayList<IHeader>();
		
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj=iHeaderObj;
		char exceptionCode = 10;
				
		if(sdsMessage.getSession().get(IAppConstants.SESS_ASSET_LOC)!=null){
			/**Messages to be sent to the AlertsEngine for XC-10 */
			JackpotDTO jackpotDTO = new JackpotDTO(); 
			
			jackpotDTO.setAssetConfigLocation((String)sdsMessage.getSession().get(IAppConstants.SESS_ASSET_LOC));
			
			if(log.isInfoEnabled()) {
				log.info("AssetConfigLocation: "+jackpotDTO.getAssetConfigLocation());
			}
			
			if(sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS)!=null && ((String)sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS)).equalsIgnoreCase("true"))
			{
				jackpotDTO.setPostedSuccessfully(true);
				long sequenceNo = (sdsMessage.getSession().get(IAppConstants.SESS_JP_SEQUENCE_NO)!=null) ? ((Long) sdsMessage.getSession().get(IAppConstants.SESS_JP_SEQUENCE_NO)): 0;
				jackpotDTO.setSequenceNumber(sequenceNo);
				log.info("Posted Successfully: "+jackpotDTO.isPostedSuccessfully());
				if(log.isDebugEnabled()) {
					log.debug("Seq No: "+jackpotDTO.getSequenceNumber());
				}
			}		
			if(sdsMessage.getSession().get(IAppConstants.SESS_SLOT_DOOR_OPEN)!=null && ((String)sdsMessage.getSession().get(IAppConstants.SESS_SLOT_DOOR_OPEN)).equalsIgnoreCase("true"))
			{
				jackpotDTO.setSlotDoorOpen(true);
			}			
			alertObjectList = handPaidJackpotAlertMessages(((IHandPaidJackpot)pcObj), jackpotDTO);
		}
				
		if (sdsMessage.getSession().get(IAppConstants.SESS_SLOT_STATUS) != null) {
			/** 
			 *  JACKPOT_SLOT_STATUS_INVALID (4) to be sent to the AlertsEngine 
			 *  if the asset is NOT ONLINE in the asset DB
			 *  
			 */	
			/**sendAlert(JackpotHelper.getAlertDetails(pcObj),
					IAlertMessageConstants.JACKPOT_SLOT_STATUS_INVALID);*/
			JackpotAlertObject jpAlertsUnknownSlot = new JackpotAlertObject();
							
			jpAlertsUnknownSlot.setTerminalMessageNumber(IAlertMessageConstants.JACKPOT_SLOT_STATUS_INVALID);
			jpAlertsUnknownSlot.setTerminalMessage("JACKPOT_SLOT_STATUS_INVALID");
			jpAlertsUnknownSlot.setJackpotId(((IHandPaidJackpot)pcObj).getJackpotId());
			jpAlertsUnknownSlot.setAssetConfigNumber(((IHandPaidJackpot)pcObj).getAssetConfigNumber());
			jpAlertsUnknownSlot.setEngineId(IAppConstants.JACKPOT_ALERTS_ENGINE_ID);
			
			/**
			 * COMMENTED FOR US QA PATCH
			 * 
			 */
			
			/*if(((IHandPaidJackpot)pcObj).getJackpotAmount()!=0){
				jpAlertsUnknownSlot.setJackpotAmount(((IHandPaidJackpot)pcObj).getJackpotAmount() / ((IHandPaidJackpot)pcObj).getGmuDenom());
			}else{
				jpAlertsUnknownSlot.setJackpotAmount(((IHandPaidJackpot)pcObj).getJackpotAmount());
			}*/		
			
			jpAlertsUnknownSlot.setJackpotAmount(((IHandPaidJackpot)pcObj).getJackpotAmount());
			
			jpAlertsUnknownSlot.setDateTime(((IHandPaidJackpot)pcObj).getDateTime());
			jpAlertsUnknownSlot.setExceptionCode(exceptionCode);
			jpAlertsUnknownSlot.setMessageId(((IHandPaidJackpot)pcObj).getMessageId());
			jpAlertsUnknownSlot.setSendingEngineName(IAppConstants.MODULE_NAME);
			jpAlertsUnknownSlot.setSiteId(((IHandPaidJackpot)pcObj).getSiteId());
			jpAlertsUnknownSlot.setSlotAreaId(((IHandPaidJackpot)pcObj).getAssetInfo().getAreaName());
			log.info("Sending the msg to Alerts for "+jpAlertsUnknownSlot.getTerminalMessage()+" : "+jpAlertsUnknownSlot.getTerminalMessageNumber());
						
			/** SENDING MSG TO ALERTS ENGINE */
			alertObjectList.add(jpAlertsUnknownSlot);			
		}
		
		if(alertObjectList.size()>0){
			/** CALLING THE ALERTS ENGINE BO METHOD TO SEND THE LIST OF ALERT OBJECTS*/
			long startTime, endTime;
			startTime = Calendar.getInstance().getTime().getTime();	
			try {
				log.info("Before sending the msg to Alerts for XC - 10");
				alertsBO.sendAlert(alertObjectList);
				log.info("After sending the msg to Alerts for XC - 10");
				if(logBOStatistics.isDebugEnabled()) {	
					endTime = Calendar.getInstance().getTime().getTime();	
					logBOStatistics.debug("TIME TAKEN TO PROCESS sendAlert() FOR XC 10 = "+((double)endTime-(double)startTime)/1000+" for MESSAGE ID:"+sdsMessage.getMessage().getMessageId());
				}
			} catch (AlertsEngineBOException e) {
				log.error("Exception in send alert of JackpotFacade for XC - 10"+e);
				throw (e);
			}
		}			
	}
	
	/**
	 * Method to set the Response Data for an XC -10
	 * @param pcObj
	 */
	public char[] setJackpotResponseData (IHandPaidJackpot pcObj){
		
		log.info("Inside setJackpotResponseData method");
		
		/***
		 * COMMENTED FOR US QA PATCH
		 * 
		 */
		//long jackPotAmount = (((IStandardData)pcObj).getJackpotAmount() / ((IStandardData)pcObj).getGmuDenom()) ;
		
		long jackPotAmount = ((IStandardData)pcObj).getJackpotAmount();
		
		log.info("jackPotAmount: "+jackPotAmount);
		
		long jpAmtWithoutDecimal = jackPotAmount / 100;
		
		log.info("jpAmtWithoutDecimal: "+jpAmtWithoutDecimal);
		
		String sJackPotAmount = String.valueOf(jpAmtWithoutDecimal);	
		// Length is set as 4 as the GMU allows a length of only 4 characters so setting the
		// setting the string jackpot amount to 9999 so that the GMU will read this value and automatically update the 
		// EPI display with the jackpot amount		
		if(sJackPotAmount!=null && sJackPotAmount.length() > 4){				
			//sJackPotAmount = sJackPotAmount.substring(0, 4);
			sJackPotAmount = EPI_JP_AMOUNT_LIMIT;
		}	
		if(log.isDebugEnabled()) {
			log.debug("String Amount: "+sJackPotAmount);
		}
		
		byte [] bResponseData = new byte[2];
		byte [] bResponseData1 = new byte[2];
		sJackPotAmount = ConversionUtil.leftPad(sJackPotAmount, bResponseData.length * ICommonConstants.JACKPOT_RESPONSE_DATA_LENGTH, '0');
		if(log.isDebugEnabled()) {
			log.debug("Jackpot Amount in String :"+sJackPotAmount);
		}
		int j = 0;
		int z = 0;
        for (int i = 0; i < sJackPotAmount.length(); i+= ICommonConstants.JACKPOT_RESPONSE_DATA_LENGTH) {
              bResponseData[j++] = (byte)Integer.parseInt(sJackPotAmount.substring(i, i+ICommonConstants.JACKPOT_RESPONSE_DATA_LENGTH), 16);
              bResponseData1[z++] = (byte)Integer.parseInt(sJackPotAmount.substring(i, i+ICommonConstants.JACKPOT_RESPONSE_DATA_LENGTH));
              if(log.isDebugEnabled()) {
              	log.debug("bResponseData[j]  HEX: "+ (int) bResponseData[j-1]);
             	log.debug("bResponseData1[z] INT: "+ (int) bResponseData1[z-1]);
              }
        }
        if(log.isDebugEnabled()) {
        	log.debug("bResponseData.length: "+ bResponseData.length);
        }
        
        char [] cResponseData = ConversionUtil.convertByteArrToCharArr(bResponseData, ICommonConstants.JACKPOT_RESPONSE_DATA_LENGTH);
        log.info("Response Data: "+(int)cResponseData[0] + ","+(int)cResponseData[1]);          
        return cResponseData;        
	}
	
	/**
	 * Method handles all the Alert messages for a Hand Paid Jackpot Event(XC-10)
	 * @param handPaidJackpot
	 * @param jackpotDTO
	 * @throws Exception
	 */
	public List<IHeader> handPaidJackpotAlertMessages(IHandPaidJackpot handPaidJackpot, JackpotDTO jackpotDTO)throws Exception
	{		
		/** 
		 * CREATING A LIST OF JackpotAlertObjects 
		*/		
		List<IHeader> alertObjectList = new ArrayList<IHeader>();
		
		log.info("inside handPaidJackpotAlertMessages");
		char exceptionCode = 10;
		
		if(jackpotDTO!=null){
			/** 
			 *  INVALID JACKPOT (3) to be sent to the AlertsEngine if JP ID = 0  / if the SLOT DOOR IS OPEN
			 *  
			 */			
			if(handPaidJackpot.getJackpotId()==IJackpotIds.JACKPOT_ID_INVALID_0
					|| (jackpotDTO.isSlotDoorOpen())){
				JackpotAlertObject jpAlertsInvalid = new JackpotAlertObject();
				jpAlertsInvalid.setTerminalMessageNumber(IAlertMessageConstants.INVALID_JACKPOT);
				jpAlertsInvalid.setTerminalMessage("INVALID JACKPOT");
				jpAlertsInvalid.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsInvalid.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsInvalid.setStandNumber(jackpotDTO.getAssetConfigLocation());
				//jpAlertsInvalid.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
			/*	if(handPaidJackpot.getJackpotAmount()!=0){
					jpAlertsInvalid.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
				}else{*/
					jpAlertsInvalid.setJackpotAmount(handPaidJackpot.getJackpotAmount());
				//}
				jpAlertsInvalid.setDateTime(handPaidJackpot.getDateTime());
				jpAlertsInvalid.setExceptionCode(exceptionCode);
				jpAlertsInvalid.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsInvalid.setSendingEngineName(MODULE_NAME);
				jpAlertsInvalid.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsInvalid.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				log.info("Sending the msg to Alerts for "+jpAlertsInvalid.getTerminalMessage()+" : "+jpAlertsInvalid.getTerminalMessageNumber());
				/** SENDING MSG TO ALERTS ENGINE */
				alertObjectList.add(jpAlertsInvalid);							
			}	
						
		
			/** 
			 * PENDING JP SLIP FOR SLOT XXXX (92) to be sent to the AlertsEngine
			 */
			if(jackpotDTO.isPostedSuccessfully() && handPaidJackpot.getEmployeeCardNumber().matches("0+")){

				// SET THE JACKPOT DTO FIELDS FOR SENDING THE BELOW ALERT MSG 92
				//jackpotDTO.setSequenceNumber(sequenceNumber)
				jackpotDTO.setJackpotId(Integer.toHexString((int) handPaidJackpot.getJackpotId()).toUpperCase());				
				jackpotDTO.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jackpotDTO.setOriginalAmount(handPaidJackpot.getJackpotAmount());
				if(log.isDebugEnabled()) {
					log.debug("handPaidJackpot.getDateTime().getTime(): "+handPaidJackpot.getDateTime().getTime());
					log.debug("PendingAlertTxnDate Long: "+handPaidJackpot.getDateTime().getTime().getTime());
				}				
				jackpotDTO.setPendingAlertTxnDate(handPaidJackpot.getDateTime().getTime().getTime());
				jackpotDTO.setTransactionDate(handPaidJackpot.getDateTime().getTime());
				jackpotDTO.setMessageId(handPaidJackpot.getMessageId());
				jackpotDTO.setSiteId(handPaidJackpot.getSiteId());
				jackpotDTO.setAreaShortName(handPaidJackpot.getAssetInfo().getAreaName());
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jackpotDTO.setOriginalAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jackpotDTO.setOriginalAmount(handPaidJackpot.getJackpotAmount());
//				}	
				
				// Add new pending JP in cache for sending pending JP alert Msg.
				log.info("BEFORE Calling the start of the jp job for sending the pending jp alert msg in JakpotFacade");
				
				JackpotUtil.addNewPendingJackpotInCache(jackpotDTO);
				log.info("AFTER Calling the start of the jp job for sending the pending jp alert msg in JakpotFacade");					
			}		
			
			/** 
			 * JACKPOT - INVALID AMOUNT OF ZERO (161) to be sent to the AlertsEngine
			 */
			if(handPaidJackpot.getJackpotAmount() == 0 && 
					(handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA 
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB 
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE)){
								
				JackpotAlertObject jpAlertsInvalidAmt = new JackpotAlertObject();
				
				jpAlertsInvalidAmt.setTerminalMessageNumber(IAlertMessageConstants.JP_INVALID_AMT_ZERO);
				jpAlertsInvalidAmt.setTerminalMessage("JACKPOT - INVALID AMOUNT OF ZERO");
				jpAlertsInvalidAmt.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsInvalidAmt.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsInvalidAmt.setStandNumber(jackpotDTO.getAssetConfigLocation());
				jpAlertsInvalidAmt.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jpAlertsInvalidAmt.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jpAlertsInvalidAmt.setJackpotAmount(handPaidJackpot.getJackpotAmount());
				//}
				jpAlertsInvalidAmt.setDateTime(handPaidJackpot.getDateTime());
				jpAlertsInvalidAmt.setExceptionCode(exceptionCode);
				jpAlertsInvalidAmt.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsInvalidAmt.setSendingEngineName(MODULE_NAME);
				jpAlertsInvalidAmt.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsInvalidAmt.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				/** SENDING MSG TO ALERTS ENGINE */
				log.info("Sending the msg to Alerts for "+jpAlertsInvalidAmt.getTerminalMessage()+" : "+jpAlertsInvalidAmt.getTerminalMessageNumber());
				
				alertObjectList.add(jpAlertsInvalidAmt);				
			}
			
			/** 
			 *  JACKPOT - INVALID JACKPOT ID (163) to be sent to the AlertsEngine if JP ID = 0
			 *  
			 */
			JackpotAlertObject jpAlertsInvalidJpId = new JackpotAlertObject();
			
			if ((handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_INVALID_PROG_149 
					|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_INVALID_PROG_150)
					|| (handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_252_FC
							&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_253_FD
							&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_251_FB
							&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_254_FE
							&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_250_FA 
							&& !(handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 
									&& handPaidJackpot.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159))) {
			
				jpAlertsInvalidJpId.setTerminalMessageNumber(IAlertMessageConstants.JP_INVALID_JP_ID);
				jpAlertsInvalidJpId.setTerminalMessage("JACKPOT - INVALID JACKPOT ID");
				jpAlertsInvalidJpId.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsInvalidJpId.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsInvalidJpId.setStandNumber(jackpotDTO.getAssetConfigLocation());
				jpAlertsInvalidJpId.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jpAlertsInvalidJpId.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jpAlertsInvalidJpId.setJackpotAmount(handPaidJackpot.getJackpotAmount());
//				}
				jpAlertsInvalidJpId.setDateTime(handPaidJackpot.getDateTime());
				jpAlertsInvalidJpId.setExceptionCode(exceptionCode);
				jpAlertsInvalidJpId.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsInvalidJpId.setSendingEngineName(MODULE_NAME);
				jpAlertsInvalidJpId.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsInvalidJpId.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				/** SENDING MSG TO ALERTS ENGINE */
				log.info("Sending the msg to Alerts for "+jpAlertsInvalidJpId.getTerminalMessage()+" : "+jpAlertsInvalidJpId.getTerminalMessageNumber());
				
				alertObjectList.add(jpAlertsInvalidJpId);				
			}		
			
			/** 
			 *  FORTUNE JACKPOT  (49) to be sent to the AlertsEngine if JP ID = 1  
			 *  
			 */
			JackpotAlertObject jpAlertsFortune = new JackpotAlertObject();
			if (handPaidJackpot.getJackpotId()== IJackpotIds.JACKPOT_ID_INVALID_1_FORTUNE) {
				jpAlertsFortune.setTerminalMessageNumber(IAlertMessageConstants.FORTUNE_JACKPOT);
				jpAlertsFortune.setTerminalMessage("FORTUNE JACKPOT");			
				log.debug("FORTUNE JACKPOT");
				jpAlertsFortune.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsFortune.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsFortune.setStandNumber(jackpotDTO.getAssetConfigLocation());
				jpAlertsFortune.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jpAlertsFortune.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jpAlertsFortune.setJackpotAmount(handPaidJackpot.getJackpotAmount());
//				}
				jpAlertsFortune.setDateTime(handPaidJackpot.getDateTime());
				jpAlertsFortune.setExceptionCode(exceptionCode);
				jpAlertsFortune.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsFortune.setSendingEngineName(MODULE_NAME);
				jpAlertsFortune.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsFortune.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				/** SENDING MSG TO ALERTS ENGINE */
				log.info("Sending the msg to Alerts for "+jpAlertsFortune.getTerminalMessage()+" : "+jpAlertsFortune.getTerminalMessageNumber());
				
				alertObjectList.add(jpAlertsFortune);				
			}
			
			/** 
			 *  JACKPOT  (1) to be sent to the AlertsEngine if JP ID is FA, FC, FE
			 *  PROGRESSIVE JACKPOT  (2) to be sent to the AlertsEngine if JP ID between 112 and 159 
			 *  
			 */
			if(jackpotDTO.isPostedSuccessfully()){
				JackpotAlertObject jpAlertsJackpotId = new JackpotAlertObject();
				
				if(handPaidJackpot.getJackpotId()== IJackpotIds.JACKPOT_ID_250_FA
						|| handPaidJackpot.getJackpotId()== IJackpotIds.JACKPOT_ID_252_FC
						|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB
						|| handPaidJackpot.getJackpotId()== IJackpotIds.JACKPOT_ID_253_FD
						|| handPaidJackpot.getJackpotId()== IJackpotIds.JACKPOT_ID_254_FE){
					jpAlertsJackpotId.setTerminalMessageNumber(IAlertMessageConstants.JACKPOT);
					jpAlertsJackpotId.setTerminalMessage("JACKPOT");
					log.debug("JACKPOT");
				}
				else if ((handPaidJackpot.getJackpotId()!= IJackpotIds.JACKPOT_ID_INVALID_PROG_149 || handPaidJackpot.getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150) 
						&& handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 && handPaidJackpot.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159) {
					jpAlertsJackpotId.setTerminalMessageNumber(IAlertMessageConstants.PROGRESSIVE_JACKPOT);
					jpAlertsJackpotId.setTerminalMessage("PROGRESSIVE JACKPOT");
					log.debug("PROGRESSIVE JACKPOT");
				}
				if(jpAlertsJackpotId.getTerminalMessageNumber()!=0){
					jpAlertsJackpotId.setJackpotId(handPaidJackpot.getJackpotId());
					jpAlertsJackpotId.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
					jpAlertsJackpotId.setStandNumber(jackpotDTO.getAssetConfigLocation());
					jpAlertsJackpotId.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//					if(handPaidJackpot.getJackpotAmount()!=0){
//						jpAlertsJackpotId.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//					}else{
						jpAlertsJackpotId.setJackpotAmount(handPaidJackpot.getJackpotAmount());
//					}
					jpAlertsJackpotId.setDateTime(handPaidJackpot.getDateTime());
					jpAlertsJackpotId.setExceptionCode(exceptionCode);
					jpAlertsJackpotId.setMessageId(handPaidJackpot.getMessageId());
					jpAlertsJackpotId.setSendingEngineName(MODULE_NAME);
					jpAlertsJackpotId.setSiteId(handPaidJackpot.getSiteId());
					jpAlertsJackpotId.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
					/** SENDING MSG TO ALERTS ENGINE */
					
					log.info("Sending the msg to Alerts for "+jpAlertsJackpotId.getTerminalMessage()+" : "+jpAlertsJackpotId.getTerminalMessageNumber());
					
					alertObjectList.add(jpAlertsJackpotId);					
				}				
			}		
		
			/** 
			 *  PROGRESSIVE JACKPOT, UNKNOWN AMOUNT (101) to be sent to the AlertsEngine if JP ID between 112 and 159 
			 *  and the Jackpot Amount is zero
			 */
			
			JackpotAlertObject jpAlertsProgUnknownAmt = new JackpotAlertObject();
			//Fireball Progressive - Condition change
			if ((handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149 && handPaidJackpot
					.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150)
					&& (handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 && handPaidJackpot
							.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159)
					&& ((handPaidJackpot.getisProgressiveHit() && handPaidJackpot
							.getTotalProgressiveAward() == 0) || (!handPaidJackpot
							.getisProgressiveHit()
							&& handPaidJackpot.getJackpotAmount() == 0))) {
				
				
				jpAlertsProgUnknownAmt.setTerminalMessageNumber(IAlertMessageConstants.PROGRESSIVE_JP_UNKNOWN_AMT);
				jpAlertsProgUnknownAmt.setTerminalMessage("PROGRESSIVE JACKPOT, UNKNOWN AMOUNT");
				jpAlertsProgUnknownAmt.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsProgUnknownAmt.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsProgUnknownAmt.setStandNumber(jackpotDTO.getAssetConfigLocation());
				jpAlertsProgUnknownAmt.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jpAlertsProgUnknownAmt.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jpAlertsProgUnknownAmt.setJackpotAmount(handPaidJackpot.getJackpotAmount());
//				}					
				jpAlertsProgUnknownAmt.setDateTime(handPaidJackpot.getDateTime());
				//char exceptionCode = 10;
				jpAlertsProgUnknownAmt.setExceptionCode(exceptionCode);
				jpAlertsProgUnknownAmt.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsProgUnknownAmt.setSendingEngineName(MODULE_NAME);
				jpAlertsProgUnknownAmt.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsProgUnknownAmt.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				/** SENDING MSG TO ALERTS ENGINE */
				
				log.info("Sending the msg to Alerts for "+jpAlertsProgUnknownAmt.getTerminalMessage()+" : "+jpAlertsProgUnknownAmt.getTerminalMessageNumber());
				
				alertObjectList.add(jpAlertsProgUnknownAmt);
				
			}
			
			/** 
			 *  JACKPOT, INVALID COIN - IN (162) to be sent to the AlertsEngine if the Last Bet Value(Jackpot Multiplier) is zero
			 *  
			 */			
			JackpotAlertObject jpAlertsInvalidCoinIn = new JackpotAlertObject();
			if(log.isDebugEnabled()) {
				log.debug("Last bet value: "+handPaidJackpot.getLastBet());
			}
			if ((handPaidJackpot.getLastBet() == 0)
					&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
					&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
					&& (handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE 
							|| (handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 
									&& handPaidJackpot.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159))) {
				
				jpAlertsInvalidCoinIn.setTerminalMessageNumber(IAlertMessageConstants.JP_INVALID_COIN_IN);
				jpAlertsInvalidCoinIn.setTerminalMessage("JACKPOT, INVALID COIN - IN");
				jpAlertsInvalidCoinIn.setJackpotId(handPaidJackpot.getJackpotId());
				jpAlertsInvalidCoinIn.setAssetConfigNumber(handPaidJackpot.getAssetConfigNumber());
				jpAlertsInvalidCoinIn.setStandNumber(jackpotDTO.getAssetConfigLocation());
				jpAlertsInvalidCoinIn.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
//				if(handPaidJackpot.getJackpotAmount()!=0){
//					jpAlertsInvalidCoinIn.setJackpotAmount(handPaidJackpot.getJackpotAmount() / handPaidJackpot.getGmuDenom());
//				}else{
					jpAlertsInvalidCoinIn.setJackpotAmount(handPaidJackpot.getJackpotAmount());
//				}				
				jpAlertsInvalidCoinIn.setDateTime(handPaidJackpot.getDateTime());
				//char exceptionCode = 10;
				jpAlertsInvalidCoinIn.setExceptionCode(exceptionCode);
				jpAlertsInvalidCoinIn.setMessageId(handPaidJackpot.getMessageId());
				jpAlertsInvalidCoinIn.setSendingEngineName(MODULE_NAME);
				jpAlertsInvalidCoinIn.setSiteId(handPaidJackpot.getSiteId());
				jpAlertsInvalidCoinIn.setSlotAreaId(handPaidJackpot.getAssetInfo().getAreaName());
				
				log.info("Sending the msg to Alerts for "+jpAlertsInvalidCoinIn.getTerminalMessage()+" : "+jpAlertsInvalidCoinIn.getTerminalMessageNumber());
				
				/** SENDING MSG TO ALERTS ENGINE */
				alertObjectList.add(jpAlertsInvalidCoinIn);				
			}	
		}else{
			log.debug("The alert msgs in handPaidJackpotAlertMessages() were not sent as the jackpotDTO returned was null");
		}	
		return alertObjectList;
	}
		
	/**
	 * Method to perform the pre processing for a Jackpot To Credit Meter Msg
	 * @param sdsMessage
	 * @return
	 * @throws Exception
	 */
	public SDSMessage preProcessJackpotToCreditMeterMsg(SDSMessage sdsMessage) throws Exception {
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj=iHeaderObj;		
		JackpotDTO jackpotDTO = new JackpotDTO();
		char successA0 = 0xA0;
		char failure = 0xB0;
		log.debug("JACKPOT TO CREDIT METER - Msg id: "+((IJackpotToCreditMeter)pcObj).getMessageId());
							
		try {
			if (log.isInfoEnabled()) {
				log.info("Duplicate Msg Check : " + ((IJackpotToCreditMeter) pcObj).isDuplicateMessage());
				log.info("Duplicate Msg Id : " + ((IJackpotToCreditMeter) pcObj).getDuplicateMessageId());
				log.info("Slot No : " + ((IJackpotToCreditMeter) pcObj).getAssetConfigNumber());
				log.info("Msg Id : " + ((IJackpotToCreditMeter) pcObj).getMessageId());
				log.info("Jackpot to Credit meter HPJP : "
						+ ((IJackpotToCreditMeter) pcObj).getHandPaidJackpot());
				log.info("Player card no : " + ((IJackpotToCreditMeter) pcObj).getPlayerCardNumber());
				log.info("Jackpot Amt : " + ((IJackpotToCreditMeter) pcObj).getJackpotAmount());
				log.info("Jackpot Id : " + ((IJackpotToCreditMeter) pcObj).getJackpotId());
				log.info("Site Id : " + ((IJackpotToCreditMeter) pcObj).getSiteId());
				log.info("Employee card : " + ((IJackpotToCreditMeter) pcObj).getEmployeeCardNumber());
				log.info("Is Slot Online : " + pcObj.isSlotOnline());
			}
				
			//LOGIC CHK WHETHER PROG CONTROLLER NOTIFICATION IS ENABLED FOR CREATING THE PROG JP
			//IF ENABLED IGNORE THE CURRENT PROG JP AND SET DEF +VE RESP,  ELSE CARRY ON
			if(!JackpotUtil.isJackpotSlotFloorEventHandlingRequired(((IJackpotToCreditMeter)pcObj).getAssetConfigNumber(), 
					((IJackpotToCreditMeter)pcObj).getSiteId(), ((IJackpotToCreditMeter) pcObj).getJackpotId())){
				
				log.info("PROGRESSIVE CONTROLLER JP NOTIFICATION ENABLED - IGNORE CURRENT XC30");				
				((IStandardResponse)pcObj).setResponseCode(successA0);
				((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
				return sdsMessage;
			}
						
			AssetInfo assetInfoDTO = ((IJackpotToCreditMeter)pcObj).getAssetInfo();
			if (assetInfoDTO != null) {
				if (log.isInfoEnabled()) {
					log.info("AssetInfoDTO is not null");
				}
				if(log.isDebugEnabled()) {
					log.debug("Asset Config Location : " + assetInfoDTO.getAssetConfigLocation());
					log.debug("Regulatory ID : " + assetInfoDTO.getRegulatoryId());
				}
				jackpotDTO.setAssetConfigLocation(assetInfoDTO.getAssetConfigLocation());
				jackpotDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				sdsMessage.getSession().put(IAppConstants.SESS_ASSET_LOC, jackpotDTO.getAssetConfigLocation());									
				if (assetInfoDTO.getDenominaton() != null){
					jackpotDTO.setSlotDenomination(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getDenominaton()));
					if(log.isDebugEnabled()) {
						log.debug("Asset Slot Denom: "+jackpotDTO.getSlotDenomination());
					}
				}						
				if (assetInfoDTO.getGmuDenominaton() != null){
					jackpotDTO.setGmuDenom(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getGmuDenominaton()));
				}			
				jackpotDTO.setAreaLongName(assetInfoDTO.getAreaLongName());
				jackpotDTO.setSlotDescription(assetInfoDTO.getTypeDesc());
				jackpotDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				jackpotDTO.setSiteId(((IJackpotToCreditMeter)pcObj).getSiteId());
				jackpotDTO.setSlotOnline(pcObj.isSlotOnline());
				
				// GETTING PRIMARY KEY OBJECT
				SDSPrimaryKey sdsPrimaryKey = sdsMessage.getSdsPrimaryKey();
				long slipPrimaryKey = sdsPrimaryKey.getKey(IPrimaryKeyConstants.SLIP_SLIP_REFERENCE);
								
				if (((IJackpotToCreditMeter) pcObj).isDuplicateMessage()) {
					log.info("DUPLICATE MSG - XC 30");
					log.debug("Duplicate Msg Id is present from IHeader - XC 30");

					long startTime, endTime = 0;
					startTime = Calendar.getInstance().getTime().getTime();

					// LOCK THE JACKPOT SLOT TABLE
					boolean jpSlotLockResp = jackpotBO.lockJackpotSlotTable(
							((IJackpotToCreditMeter) pcObj).getAssetConfigNumber(),
							((IJackpotToCreditMeter) pcObj).getSiteId());
					log.debug("jpSlotLockResp :" + jpSlotLockResp);

					// DUPLICATE MSG CHECK
					jackpotDTO = jackpotBO.eventDuplicateMsgCheckGetResponse(((IJackpotToCreditMeter) pcObj)
							.getDuplicateMessageId());
					if (logBOStatistics.isDebugEnabled()) {
						endTime = Calendar.getInstance().getTime().getTime();
						logBOStatistics
								.debug("TIME TAKEN TO PROCESS eventDuplicateMsgCheckGetResponse() FOR XC 30 = "
										+ ((double) endTime - (double) startTime)
										/ 1000
										+ " for MESSAGE ID:"
										+ sdsMessage.getMessage().getMessageId());
					}
					if (jackpotDTO != null) {
						if (jackpotDTO.isDuplicateMsg()) {
							log.info("Transaction resp for XC 30: " + jackpotDTO.getResponseCode());
							if (jackpotDTO.getResponseCode().equalsIgnoreCase(GEN_POSITIVE_RESPONSE_CODE))
								((IStandardResponse) pcObj).setResponseCode(successA0);
							else if (jackpotDTO.getResponseCode()
									.equalsIgnoreCase(GEN_NEGATIVE_RESPONSE_CODE))
								((IStandardResponse) pcObj).setResponseCode(failure);
						} else {
							log.info("No Duplicate Event logged for XC 30, calling createJackpotToCreditMeterSlips method");
							startTime = Calendar.getInstance().getTime().getTime();
							String generateRandSeqNum = JackpotUtil.getSiteParamValue(
									ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER,
									jackpotDTO.getSiteId());
							jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
							
							// SETTING SLIP PRIMARY KEY TO JACKPOTDTO
							jackpotDTO.setSlipPrimaryKey(slipPrimaryKey);
							
							jackpotDTO = jackpotBO.createJackpotToCreditMeterSlips(
									((IJackpotToCreditMeter) pcObj), jackpotDTO);
							if (logBOStatistics.isDebugEnabled()) {
								endTime = Calendar.getInstance().getTime().getTime();
								logBOStatistics
										.debug("TIME TAKEN TO PROCESS createJackpotToCreditMeterSlips() FOR XC 30 = "
												+ ((double) endTime - (double) startTime)
												/ 1000
												+ " for MESSAGE ID:" + sdsMessage.getMessage().getMessageId());
							}
							if (jackpotDTO.isPostedSuccessfully() == true) {
								((IStandardResponse) pcObj).setResponseCode(successA0);
								log.info("Success Response For XC 30...Updated the DB"
										+ Integer.toHexString((int) successA0));
								/*
								 * remove processed Pending JP from cache to
								 * stop sending alert 92 - PENDING JP FOR SLOT
								 */
								// SEQ NO WILL BE ZERO IF THERE WAS NO
								// PRECEEDING XC10 AND THIS XC 30 WAS THE FIRST
								// JP XC FROM GMU
								if (jackpotDTO.getSequenceNumber() != 0) {
									String key = ((IJackpotToCreditMeter) pcObj).getSiteId() + "_"
											+ jackpotDTO.getSequenceNumber();
									JackpotUtil.removePendingJPFromCache(key);
								}
							} else {
								((IStandardResponse) pcObj).setResponseCode(failure);
								log.info("Failure Response For XC 30... "
										+ Integer.toHexString((int) failure));
							}
						}
						((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
					}
				} else {
					long startTime, endTime =0;			 
					log.info("Calling createJackpotToCreditMeterSlips");
					startTime = Calendar.getInstance().getTime().getTime();	
					
					//LOCK THE JACKPOT SLOT TABLE							
					boolean jpSlotLockResp = jackpotBO.lockJackpotSlotTable(((IJackpotToCreditMeter)pcObj).getAssetConfigNumber(), ((IJackpotToCreditMeter)pcObj).getSiteId());
					if (log.isDebugEnabled()) {
						log.debug("jpSlotLockResp :" + jpSlotLockResp);
					}
					
					// SETTING SLIP PRIMARY KEY TO JACKPOTDTO
					jackpotDTO.setSlipPrimaryKey(slipPrimaryKey);
					
					//Added SA's random jackpot generator seq. configuration
					String generateRandSeqNum = JackpotUtil.getSiteParamValue(ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER,
							jackpotDTO.getSiteId());
					jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
					
					//CREATE THE JP TO CREDIT METER
					jackpotDTO = jackpotBO.createJackpotToCreditMeterSlips(((IJackpotToCreditMeter)pcObj), jackpotDTO);
					if(logBOStatistics.isDebugEnabled()) {	
						endTime = Calendar.getInstance().getTime().getTime();	
						logBOStatistics.debug("TIME TAKEN TO PROCESS createJackpotToCreditMeterSlips() FOR XC 30 = "+((double)endTime-(double)startTime)/1000+" for MESSAGE ID:"+sdsMessage.getMessage().getMessageId());
					}
					log.debug("******** Within JP Processor class before checking the if condition for the response");
					if (jackpotDTO.isPostedSuccessfully() == true){
						((IStandardResponse)pcObj).setResponseCode(successA0);
						if(log.isInfoEnabled()) {
							log.info("Success Response For XC 30... " + Integer.toHexString((int)successA0));
						}
						
						/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
						//SEQ NO WILL BE ZERO IF THERE WAS NO PRECEEDING XC10 AND THIS XC 30 WAS THE FIRST JP XC FROM GMU 
						if (jackpotDTO.getSequenceNumber() != 0) {
							String key = ((IJackpotToCreditMeter)pcObj).getSiteId() + "_" + jackpotDTO.getSequenceNumber();			
							JackpotUtil.removePendingJPFromCache(key);
						}	
					}
					else {
						((IStandardResponse)pcObj).setResponseCode(failure);
						log.info("Failure Response For XC 30... " + Integer.toHexString((int)failure));
					}
					((IStandardResponse)pcObj).setResponseData(new char[]{0x00,0x00});
				}
			}
		}catch (Exception e) {
			((IStandardResponse)pcObj).setResponseCode(failure);
			((IStandardResponse)pcObj).setResponseData(new char[]{0x00,0x00});
			log.error("EXCEPTION : Failure Response For XC 30..." + Integer.toHexString((int)failure));
			log.error("Exception in the jackpot Processor for JACKPOT TO CREDIT METER", e);
		}
		if (jackpotDTO != null && jackpotDTO.isSendAlert()) {
			if (jackpotDTO.isPostedSuccessfully()) {
				sdsMessage.getSession().put(IAppConstants.SESS_JP_POST_SUCCESS, "true");
			}
		}	
		/** CALL TO MARKETING TO POST JP ADJUSTMENT FOR A CREDIT KEY OFF*/
		
		//WHEN CREDIT KEY OFF OCCURS DO NOT SEND PT 10 AS THE PLAYER AMT WOULD GET DOUBLED
		//AS IT IS ALREADY SENT FOR THE PENDING JP HENCE COMMENTING THE BELOW CODE ON CONFIRMATION WITH QA
		/*try {
			if(sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS)!=null && sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS).equals("true"))
			{
				marketingBO.processManualJackpot(((IJackpotToCreditMeter)pcObj).getSiteId(), ((IJackpotToCreditMeter)pcObj).getPlayerCardNumber(), ((IJackpotToCreditMeter)pcObj).getJackpotAmount(), ((IJackpotToCreditMeter)pcObj).getAssetConfigNumber(), ((IJackpotToCreditMeter)pcObj).getMessageId());
				log.info("Adjustment was posted to Marketing for XC 30");
			}else{
				log.info("Adjustment was not posted to Marketing for XC 30");
			}					
		} catch (MarketingDAOException e) {
			log.error("Exception occured in postProcessJackpotToCreditMeterMsg of JackpotFacade in processManualJackpot of MARKETING BO", e);
			throw new Exception("Exception occured in postProcessJackpotToCreditMeterMsg of JackpotFacade in processManualJackpot of MARKETING BO", e);
		}*/
		return sdsMessage;
	}
	
	/**
	 * Method that does the post processing for a JackpotToCreditMeterMsg - XC 30
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void postProcessJackpotToCreditMeterMsg(SDSMessage sdsMessage)throws Exception
	{
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj=iHeaderObj;
		/** CALL TO SEND AN ALERT FOR XC-30*/
		if(sdsMessage.getSession().get(IAppConstants.SESS_ASSET_LOC)!=null){
			//**Messages to be sent to the AlertsEngine for XC-30 *//*
			log.debug("Before sending the msgs to the AlertsEngine for XC 30");
			JackpotDTO jackpotDTO = new JackpotDTO();
			
			jackpotDTO.setAssetConfigLocation((String)sdsMessage.getSession().get(IAppConstants.SESS_ASSET_LOC));
			if (sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS) != null
					&& ((String) sdsMessage.getSession().get(IAppConstants.SESS_JP_POST_SUCCESS))
							.equalsIgnoreCase("true")) {
				jackpotDTO.setPostedSuccessfully(true);				
				char exceptionCode = 30;
				/** 
				 * JACKPOT TO CREDIT METER (204) to be sent to the AlertsEngine
				 */				
				if (jackpotDTO != null) {
					sendAlert(JackpotHelper.getAlertDetails(pcObj),
							IAlertMessageConstants.JP_TO_CREDIT_METER);
				}
			}
		}	
	}
			
	/**
	 * Method to perform the pre processing for a Jackpot To Credit Meter Authorization Msg
	 * @param sdsMessage
	 * @return
	 * @throws Exception
	 */
	public SDSMessage preProcessCreditKeyOffAuthBlock(SDSMessage sdsMessage) throws Exception {
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj = iHeaderObj;
		JackpotDTO jackpotDTO = new JackpotDTO();
		List<String> respStrList = null;
		log.debug("CREDIT KEY OFF - XC 30 AUTH BLOCK - Msg id: "
				+ ((ICreditKeyOffBlock) pcObj).getMessageId());

		try {
			if (log.isInfoEnabled()) {
				log.info("Duplicate Msg Check : " + ((ICreditKeyOffBlock) pcObj).isDuplicateMessage());
				log.info("Duplicate Msg Id : " + ((ICreditKeyOffBlock) pcObj).getDuplicateMessageId());
				log.info("Slot No : " + ((ICreditKeyOffBlock) pcObj).getAssetConfigNumber());
				log.info("Msg Id : " + ((ICreditKeyOffBlock) pcObj).getMessageId());
				log.info("Site Id : " + ((ICreditKeyOffBlock) pcObj).getSiteId());
			}
			
			// GETTING PRIMARY KEY OBJECT
			SDSPrimaryKey sdsPrimaryKey = sdsMessage.getSdsPrimaryKey();
			long slipPrimaryKey = sdsPrimaryKey.getKey(IPrimaryKeyConstants.SLIP_SLIP_REFERENCE);

			if (((ICreditKeyOffBlock) pcObj).isDuplicateMessage()) {
				log.info("DUPLICATE MSG - XC 30 AUTH BLOCK");
				log.debug("Duplicate Msg Id is present from IHeader - XC 30 AUTH BLOCK");

				long startTime, endTime = 0;
				startTime = Calendar.getInstance().getTime().getTime();

				// DUPLICATE MSG CHECK
				jackpotDTO = jackpotBO.eventDuplicateMsgCheckGetResponse(((ICreditKeyOffBlock) pcObj)
						.getDuplicateMessageId());
				if (logBOStatistics.isDebugEnabled()) {
					endTime = Calendar.getInstance().getTime().getTime();
					logBOStatistics
							.debug("TIME TAKEN TO PROCESS eventDuplicateMsgCheckGetResponse() FOR XC 30 AUTH BLOCK = "
									+ ((double) endTime - (double) startTime)
									/ 1000
									+ " for MESSAGE ID:"
									+ sdsMessage.getMessage().getMessageId());
				}
				if (jackpotDTO != null) {
					if (jackpotDTO.isDuplicateMsg()) {
						log.info("Transaction resp for XC 30 AUTH BLOCK: " + jackpotDTO.getResponseCode());

						if (jackpotDTO != null
								&& jackpotDTO.getResponseCode() != null
								&& jackpotDTO.getResponseCode().equalsIgnoreCase(
										String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP))) {

							((ICreditKeyOffBlock) pcObj)
									.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP);
							log.info("Success Response For XC 30 AUTH BLOCK... "
									+ IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP);
						} else {
							((ICreditKeyOffBlock) pcObj)
									.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
							log.info("Failure Response For XC 30 AUTH BLOCK... "
									+ IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
						}
					} else {
						log.info("No Duplicate Event logged for XC 30 AUTH BLOCK, calling creditKeyOffAuthBlockProcessing method");
						startTime = Calendar.getInstance().getTime().getTime();

						// CREATE THE CREDIT KEY OFF AUTH TRANSACTION
						respStrList = jackpotBO.creditKeyOffAuthBlockProcessing(((ICreditKeyOffBlock) pcObj), slipPrimaryKey);

						if (logBOStatistics.isDebugEnabled()) {
							endTime = Calendar.getInstance().getTime().getTime();
							logBOStatistics.debug("TIME TAKEN TO PROCESS creditKeyOffAuthBlockProcessing() "
									+ "FOR XC 30 AUTH BLOCK = " + ((double) endTime - (double) startTime)
									/ 1000 + " for MESSAGE ID:" + sdsMessage.getMessage().getMessageId());
						}
						if (respStrList != null
								&& respStrList.size() > 0
								&& respStrList.get(1).equalsIgnoreCase(
										String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP))) {

							((ICreditKeyOffBlock) pcObj)
									.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP);
							((ICreditKeyOffBlock) pcObj).setResponseTextData(respStrList.get(0));
							log.info("Success Response For XC 30 AUTH BLOCK... " + respStrList.get(1));
							log.info("Success Text Resp For XC 30 AUTH BLOCK... " + respStrList.get(0));
						} else if (respStrList != null
								&& respStrList.size() > 0
								&& respStrList.get(1).equalsIgnoreCase(
										String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP))) {

							((ICreditKeyOffBlock) pcObj)
									.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
							((ICreditKeyOffBlock) pcObj).setResponseTextData(respStrList.get(0));
							log.info("Failure Response For XC 30 AUTH BLOCK... " + respStrList.get(1));
							log.info("Failure Text Resp For XC 30 AUTH BLOCK... " + respStrList.get(0));
						}
					}
				}
			} else {
				long startTime, endTime = 0;
				if(log.isInfoEnabled()) {
					log.info("Calling creditKeyOffAuthBlockProcessing");
				}
				startTime = Calendar.getInstance().getTime().getTime();

				// CREATE THE CREDIT KEY OFF AUTH TRANSACTION
				
				respStrList = jackpotBO.creditKeyOffAuthBlockProcessing(((ICreditKeyOffBlock) pcObj), slipPrimaryKey);

				if (logBOStatistics.isDebugEnabled()) {
					endTime = Calendar.getInstance().getTime().getTime();
					logBOStatistics
							.debug("TIME TAKEN TO PROCESS creditKeyOffAuthBlockProcessing() FOR XC 30 AUTH BLOCK = "
									+ ((double) endTime - (double) startTime)
									/ 1000
									+ " for MESSAGE ID:"
									+ sdsMessage.getMessage().getMessageId());
				}
				log.debug("******** Within JP Processor class before checking the if condition for the response");
				if (respStrList != null
						&& respStrList.size() > 0
						&& respStrList.get(1).equalsIgnoreCase(
								String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP))) {

					((ICreditKeyOffBlock) pcObj)
							.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP);
					((ICreditKeyOffBlock) pcObj).setResponseTextData(respStrList.get(0));
					log.info("Success Response For XC 30 AUTH BLOCK... " + respStrList.get(1));
					log.info("Success Text Resp For XC 30 AUTH BLOCK... " + respStrList.get(0));
				} else {

					((ICreditKeyOffBlock) pcObj)
							.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
					((ICreditKeyOffBlock) pcObj).setResponseTextData(respStrList.get(0));
					log.info("Failure Response For XC 30 AUTH BLOCK... " + respStrList.get(1));
					log.info("Failure Text Resp For XC 30 AUTH BLOCK... " + respStrList.get(0));
				}
			}
		} catch (Exception e) {
			((ICreditKeyOffBlock) pcObj)
					.setResponseAuthrizationAction(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
			((ICreditKeyOffBlock) pcObj)
					.setResponseTextData(IAppConstants.SLIP_PROBLEM_OCCURED_DURING_CREDIT_KEY_OFF_AUTH);
			log.error("EXCEPTION : Failure Response For XC 30 AUTH BLOCK..."
					+ IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP);
			log.error("EXCEPTION : Failure Text Resp For XC 30 AUTH BLOCK..."
					+ IAppConstants.SLIP_PROBLEM_OCCURED_DURING_CREDIT_KEY_OFF_AUTH);
			log.error("Exception in the jackpot Processor for XC 30 AUTH BLOCK", e);
		}
		return sdsMessage;
	}
	
	/**
	 * Method that does the pre processing for a JackpotClearMsg - XC 52
	 * @param sdsMessage
	 * @return
	 * @throws Exception
	 */
	public SDSMessage preProcessJackpotClearMsg(SDSMessage sdsMessage) throws Exception {
		IHeader iHeaderObj = sdsMessage.getMessage();
		IHeader pcObj = iHeaderObj;
		JackpotDTO jackpotDTO = new JackpotDTO();
		char successA0 = 0xA0;
		char failure = 0xB0;

		if(log.isInfoEnabled()) {
			log.info("JACKPOT CLEAR - Msg id: " + ((IJackpotClear) pcObj).getMessageId());
		}
		try {
			if (log.isInfoEnabled()) {
				log.info("Duplicate Msg Check: " + ((IJackpotClear) pcObj).isDuplicateMessage());
				log.info("Duplicate Msg Id: " + ((IJackpotClear) pcObj).getDuplicateMessageId());
				log.info("Slot No: " + ((IJackpotClear) pcObj).getAssetConfigNumber());
				log.info("Msg Id: " + ((IJackpotClear) pcObj).getMessageId());
				log.info("Jackpot Amt : " + ((IJackpotClear) pcObj).getJackpotAmount());
				log.info("Jackpot Id : " + ((IJackpotClear) pcObj).getJackpotId());
				log.info("Site Id : " + ((IJackpotClear) pcObj).getSiteId());
				log.info("Employee card : " + ((IJackpotClear) pcObj).getEmployeeCardNumber());
			}

			//LOGIC CHK WHETHER PROG CONTROLLER NOTIFICATION IS ENABLED FOR CREATING THE PROG JP	
			//IF ENABLED IGNORE THE CURRENT PROG JP AND SET DEF +VE RESP, ELSE CARRY ON
			if(!JackpotUtil.isJackpotSlotFloorEventHandlingRequired(((IJackpotClear)pcObj).getAssetConfigNumber(), 
					((IJackpotClear)pcObj).getSiteId(), ((IJackpotClear) pcObj).getJackpotId())){
				
				log.info("PROGRESSIVE CONTROLLER JP NOTIFICATION ENABLED - IGNORE CURRENT XC52");				
				((IStandardResponse)pcObj).setResponseCode(successA0);
				((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
				return sdsMessage;
			}
						
			// GETTING PRIMARY KEY OBJECT
			SDSPrimaryKey sdsPrimaryKey = sdsMessage.getSdsPrimaryKey();
			long slipPrimaryKey = sdsPrimaryKey.getKey(IPrimaryKeyConstants.SLIP_SLIP_REFERENCE);

			if (((IJackpotClear) pcObj).isDuplicateMessage()) {
				if (log.isInfoEnabled()) {
					log.info("DUPLICATE MSG - XC 52");
				}
				if (log.isDebugEnabled()) {
					log.debug("Duplicate Msg Id is present from IHeader - XC 52");
				}
				long startTime, endTime = 0;
				startTime = Calendar.getInstance().getTime().getTime();
				jackpotDTO = jackpotBO.eventDuplicateMsgCheckGetResponse(((IJackpotClear) pcObj)
						.getDuplicateMessageId());
				if (logBOStatistics.isDebugEnabled()) {
					endTime = Calendar.getInstance().getTime().getTime();
					logBOStatistics
							.debug("TIME TAKEN TO PROCESS eventDuplicateMsgCheckGetResponse() FOR XC 52 = "
									+ ((double) endTime - (double) startTime) / 1000 + " for MESSAGE ID:"
									+ sdsMessage.getMessage().getMessageId());
				}
				if (jackpotDTO != null) {
					if (jackpotDTO.isDuplicateMsg()) {
						if(log.isInfoEnabled()) {
							log.info("Transaction resp for XC 52: " + jackpotDTO.getResponseCode());
						}
						if (jackpotDTO.getResponseCode().equalsIgnoreCase(GEN_POSITIVE_RESPONSE_CODE))
							((IStandardResponse) pcObj).setResponseCode(successA0);
						else if (jackpotDTO.getResponseCode().equalsIgnoreCase(GEN_NEGATIVE_RESPONSE_CODE))
							((IStandardResponse) pcObj).setResponseCode(failure);
					} else {
						if(log.isInfoEnabled()) {
							log.info("No Duplicate Event logged for XC 52, calling jackpotClearEvent method");
						}
						startTime = Calendar.getInstance().getTime().getTime();

						jackpotDTO = jackpotBO.jackpotClearEvent(((IJackpotClear) pcObj), slipPrimaryKey);
						if (logBOStatistics.isDebugEnabled()) {
							endTime = Calendar.getInstance().getTime().getTime();
							logBOStatistics.debug("TIME TAKEN TO PROCESS jackpotClearEvent() FOR XC 52 = "
									+ ((double) endTime - (double) startTime) / 1000 + " for MESSAGE ID:"
									+ sdsMessage.getMessage().getMessageId());
						}
						if (jackpotDTO.isPostedSuccessfully() == true) {
							((IStandardResponse) pcObj).setResponseCode(successA0);
							if (log.isInfoEnabled()) {
								log.info("Success Response For XC 52...Updated the DB"
										+ Integer.toHexString((int) successA0));
							}
						} else {
							((IStandardResponse) pcObj).setResponseCode(failure);
							if (log.isInfoEnabled()) {
								log.info("Failure Response For XC 52... "
										+ Integer.toHexString((int) failure));
							}
						}
					}
					((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
				}
			} else {
				long startTime, endTime = 0;
				startTime = Calendar.getInstance().getTime().getTime();
				jackpotDTO = jackpotBO.jackpotClearEvent(((IJackpotClear) pcObj), slipPrimaryKey);
				if (logBOStatistics.isDebugEnabled()) {
					endTime = Calendar.getInstance().getTime().getTime();
					logBOStatistics.debug("TIME TAKEN TO PROCESS jackpotClearEvent() FOR XC 52 = "
							+ ((double) endTime - (double) startTime) / 1000 + " for MESSAGE ID:"
							+ sdsMessage.getMessage().getMessageId());
				}
				if (log.isDebugEnabled()) {
					log.debug("******** Within JP Processor class before checking the if condition for XC 52 response");
				}
				if (jackpotDTO.isPostedSuccessfully() == true) {
					((IStandardResponse) pcObj).setResponseCode(successA0);
					if (log.isInfoEnabled()) {
						log.info("Success Response For XC 52... " + Integer.toHexString((int) successA0));
					}
				} else {
					((IStandardResponse) pcObj).setResponseCode(failure);
					if (log.isInfoEnabled()) {
						log.info("Failure Response For XC 52... " + Integer.toHexString((int) failure));
					}
				}
				((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
			}
		} catch (Exception e) {
			((IStandardResponse) pcObj).setResponseCode(failure);
			((IStandardResponse) pcObj).setResponseData(new char[] { 0x00, 0x00 });
			log.error("EXCEPTION : Failure Response For XC 52..." + Integer.toHexString((int) failure));
			log.error("Exception in the jackpot Processor for JACKPOT CLEAR", e);
		}
		return sdsMessage;
	}
			
	/**
	 * Method to get all the pending jp slips for the site id passed
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)	
	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId) throws JackpotEngineServiceException {
		log.info("Inside getAllPendingJackpotSlipDetails FACADE method");
		return jackpotBO.getAllPendingJackpotSlipDetails(siteId);
	}

	/**
	 * Method to get the pending jp slips for the site id passed based one the Filter - Type (SlotNo/SlotLoc/SeqNo/Minutes)
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> getJackpotDetails(JackpotFilter filter) throws JackpotEngineServiceException {
		log.info("Inside getJackpotDetails FACADE method");
//		List<JackpotDTO> jackpotDTOList = null;
//		try {
////			Class myClass = Class.forName("com.ballydev.sds.jackpot.bo.JackpotBO");
////			Method method = myClass.getMethod(filter.getType(), new Class[]{JackpotFilter.class});
////			
////			jackpotDTOList = (List<JackpotDTO>) method.invoke(myClass.newInstance(), new Object[]{filter});
//			
//			
//			
//		} catch (Exception e) {
//			throw new JackpotEngineServiceException(e);
//		}	
		return jackpotBO.getJackpotDetails(filter);
	}
	
	/**
	 * Method to get the reprint jp details for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside getReprintJackpotSlipDetails FACADE method");
		
		/** GETTING SITE CONFIG VALUE FOR CASHIER DESK FLOW */
		String cashierDeskEnabled = JackpotUtil.getSiteParamValue(
				ISiteConfigConstants.IS_CASHIER_DESK_ENABLED, siteId);
				
		JackpotDTO rtnJackpotDTO = null;
		try {
			rtnJackpotDTO = jackpotBO.getReprintJackpotSlipDetails(sequenceNumber, siteId, cashierDeskEnabled);
		} catch (Exception e1) {
			log.error("Exception in getReprintJackpotSlipDetails",e1);
		}
		if (rtnJackpotDTO.isPostedSuccessfully()) {
			//GET AND SET BARCODE ENCODE FORMAT
			rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
				log.debug("printerSchema : " + printerSchema);
				log.debug("slipSchema : " + slipSchema);
				log.debug("inside the method getReprintJackpotSlipDetails and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTO.setSlipSchema(slipSchema);
				rtnJackpotDTO.setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}
		return rtnJackpotDTO;
	}

	/**
	 * Method to get the Void Jp details for the Seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId) throws JackpotEngineServiceException {		
		log.info("Inside getVoidJackpotSlipDetails FACADE method");
		JackpotDTO rtnJackpotDTO = null;
		try {
			rtnJackpotDTO = jackpotBO.getVoidJackpotSlipDetails(sequenceNumber, siteId);
		} catch (Exception e1) {
			log.error("Exception in getVoidJackpotSlipDetails",e1);
		}
		
		if (rtnJackpotDTO.isPostedSuccessfully()) {
			//GET AND SET BARCODE ENCODE FORMAT
			rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
				log.debug("printerSchema : "+printerSchema);
				log.debug("slipSchema : "+slipSchema);
				log.debug("inside the method getVoidJackpotSlipDetails and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTO.setSlipSchema(slipSchema);
				rtnJackpotDTO.setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to get the Express Jp Blind Attempts for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside getExpressJackpotBlindAttempts FACADE method");
		return jackpotBO.getExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to post the Express Jp Blind Attempts for a NonCarded Jp for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside postNonCardedExpressJackpotBlindAttempts FACADE method");
		return jackpotBO.postNonCardedExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to post the Express Jp Blind Attempts for the Seq No passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside postExpressJackpotBlindAttempts FACADE method");
		return jackpotBO.postExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp processing that is aborted
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside postUnsuccessfulExpJpBlindAttemptsAbort FACADE method");
		return jackpotBO.postUnsuccessfulExpJpBlindAttemptsAbort(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jp Slip status
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public short getJackpotStatus(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside getJackpotStatus FACADE method");
		return jackpotBO.getJackpotStatus(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId)throws JackpotEngineServiceException {
		log.info("Inside getJackpotPostToAccountDetail FACADE method");
		return jackpotBO.getJackpotPostToAccountDetail(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		log.info("Inside getJackpotStatusForVoid FACADE method");
		return jackpotBO.getJackpotStatusForVoid(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jp Slip details that was last processed
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Deprecated
	public JackpotDTO getReprintPriorSlipDetails(int siteId) throws JackpotEngineServiceException {
		log.info("Inside getReprintPriorSlipDetails FACADE method");
		
		/** GETTING SITE CONFIG VALUE FOR CASHIER DESK FLOW */
		String cashierDeskEnabled = JackpotUtil.getSiteParamValue(
				ISiteConfigConstants.IS_CASHIER_DESK_ENABLED, siteId);
				
		JackpotDTO rtnJackpotDTO = null;
		try {
			rtnJackpotDTO = jackpotBO.getReprintPriorSlipDetails(siteId, cashierDeskEnabled);
		} catch (Exception e1) {
			log.error("Exception in getReprintPriorSlipDetails",e1);
		}
		
		if (rtnJackpotDTO.isPostedSuccessfully()) {			
			//GET AND SET BARCODE ENCODE FORMAT
			rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));			
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
				log.debug("printerSchema : "+printerSchema);
				log.debug("slipSchema : "+slipSchema);
				log.debug("inside the method getReprintJackpotSlipDetails and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTO.setSlipSchema(slipSchema);
				rtnJackpotDTO.setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}		
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to get the Jp details to print the report based on the Date and Site id
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId, String fromDate, String toDate) throws JackpotEngineServiceException {
		log.info("Inside getDetailsToPrintJpSlipReportForDate FACADE method");
		List<JackpotReportsDTO> rtnJackpotDTOList = null;
		try {
			rtnJackpotDTOList = jackpotBO.getDetailsToPrintJpSlipReportForDate(siteId, fromDate, toDate);
		} catch (Exception e1) {
			log.error("Exception in getDetailsToPrintJpSlipReportForDate",e1);
			throw new JackpotEngineServiceException(e1);
		}
				
		if (rtnJackpotDTOList!=null && rtnJackpotDTOList.size()>0 && rtnJackpotDTOList.get(0).isPostedSuccessfully()) {
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
				log.debug("printerSchema : "+printerSchema);
				log.debug("slipSchema : "+slipSchema);
				log.debug("inside the method getDetailsToPrintJpSlipReportForDate and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTOList.get(0).setSlipSchema(slipSchema);
				rtnJackpotDTOList.get(0).setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}		
		return rtnJackpotDTOList;
	}

	/**
	 * Method to get the Jp details to print the report based on the Date,Employee and Site id
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate) throws JackpotEngineServiceException {
		log.info("Inside getDetailsToPrintJpSlipReportForDateEmployee FACADE method");
		List<JackpotReportsDTO> rtnJackpotDTOList = null;
		try {
			rtnJackpotDTOList = jackpotBO.getDetailsToPrintJpSlipReportForDateEmployee(siteId, employeeId, fromDate, toDate);
		} catch (Exception e1) {
			log.error("Exception in getDetailsToPrintJpSlipReportForDateEmployee",e1);
			throw new JackpotEngineServiceException(e1);
		}
		if (rtnJackpotDTOList!=null && rtnJackpotDTOList.size()>0 && rtnJackpotDTOList.get(0).isPostedSuccessfully()) {
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
				log.debug("printerSchema : "+printerSchema);
				log.debug("slipSchema : "+slipSchema);
				log.debug("inside the method getDetailsToPrintJpSlipReportForDateEmployee and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTOList.get(0).setSlipSchema(slipSchema);
				rtnJackpotDTOList.get(0).setPrinterSchema(printerSchema);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}
		return rtnJackpotDTOList;
	}

	/**
	 * Methot to get the Printer and Slip Schema xml files
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String[] getJackpotXMLInfo() throws JackpotEngineServiceException {
		log.info("Inside getJackpotXMLInfo FACADE method");
		String[] xmlFilesString={"",""};
		XMLServerUtil xmlServerUtil = new XMLServerUtil();
		try {
			String printerSchema = xmlServerUtil
					.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
			String slipSchema = xmlServerUtil
					.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
			xmlFilesString[0]=printerSchema;
			xmlFilesString[1]=slipSchema;
		} catch (IOException e) {
			log.error("exception ingetJackpotXMLInfo");
		}
		return xmlFilesString;
	}
	
	/**
	 * Method to process a pending jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO processJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {
		if (log.isInfoEnabled()) {
			log.info("Inside process Jackpot FACADE method");
		}
		JackpotDTO rtnJackpotDTO = null;
		try {
			/** GET SITE CONFIG PARAMETER */
			String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
					ISiteConfigConstants.SYSTEM_CLEARS_JACKPOT_WHEN_SLIP_PRINTS,
					ISiteConfigConstants.CURRENCY_SYMBOL, ISiteConfigConstants.IS_CASHIER_DESK_ENABLED,
					ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT,
					ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP,
					ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER }), jackpotDTO.getSiteId());
			
			String siteValue = siteParamValues[0];
			String currency = siteParamValues[1];
			String cashierDeskEnabled = siteParamValues[2];
			String postToeCashSiteParam = siteParamValues[3];
			String enableCheckPrint = siteParamValues[4];
			String generateRandSeqNum = siteParamValues[5];

			// Setting the value for Old/New Flow for Jackpot processing
			// based on the Status Code Change in the jackpotDTo object
			jackpotDTO.setCashierDeskEnabled(cashierDeskEnabled);

			jackpotDTO.setEnableCheckPrint(enableCheckPrint);
			
			jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);

			/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
			AnAUserForm userForm = getUserDetails(jackpotDTO.getPrintEmployeeLogin(), jackpotDTO.getSiteId());
			
			if (userForm != null) {
				jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
				jackpotDTO.setEmployeeLastName(userForm.getLastName());
			}
			try {
				rtnJackpotDTO = jackpotBO.updateProcessJackpot(jackpotDTO);
				String responseCode = rtnJackpotDTO.getResponseCode();
				boolean isS2SErrorPresent = JackpotUtil.isS2SErrorPresent(responseCode);
				if(isS2SErrorPresent) {
					rtnJackpotDTO = new JackpotDTO(); 
					rtnJackpotDTO.setResponseCode(responseCode);
					return rtnJackpotDTO;
				}
				
				/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
				String key = jackpotDTO.getSiteId() + "_" + jackpotDTO.getSequenceNumber();				
				JackpotUtil.removePendingJPFromCache(key);

				if (log.isDebugEnabled()) {
					log.debug("rtnJackpotDTO.getStatusFlagId() : " + rtnJackpotDTO.getStatusFlagId());
					log.debug("jackpotDTO.getProcessFlagId() : " + jackpotDTO.getProcessFlagId());
					log.debug("rtnJackpotDTO.getSlipReferenceId() : " + rtnJackpotDTO.getSlipReferenceId());
				}
				if (jackpotDTO.getProcessFlagId() == ILookupTableConstants.POUCH_PAY_PROCESS_FLAG
						&& jackpotDTO.isInsertPouchPayAttn() && rtnJackpotDTO.getSlipReferenceId() != 0) {
					rtnJackpotDTO.setSlotAttendantId(jackpotDTO.getSlotAttendantId());
					rtnJackpotDTO.setSlotAttendantFirstName(jackpotDTO.getSlotAttendantFirstName());
					rtnJackpotDTO.setSlotAttendantLastName(jackpotDTO.getSlotAttendantLastName());
					boolean response = jackpotBO.insertPouchPayAttendant(rtnJackpotDTO);

					if (log.isInfoEnabled()) {
						log.info("Response for insertPouchPayAttendant: " + response);
					}
				}
				rtnJackpotDTO.setPrintDate(rtnJackpotDTO.getSlipRefUpdatedTs());
			} catch (Exception e) {
				log.error("Exception during updateProcessJackpot OR insertPouchPayAttendant", e);
				throw new JackpotEngineServiceException(e);
			}

			/**
			 * JACKPOT AMOUNT ADJUSTED, NEW AMOUNT (104) to be sent to the
			 * AlertsEngine
			 */
			if (rtnJackpotDTO.getOriginalAmount() != 0 && rtnJackpotDTO.getHpjpAmount() != 0
					&& (rtnJackpotDTO.getOriginalAmount() != rtnJackpotDTO.getHpjpAmount())) {
				sendAlert(rtnJackpotDTO, IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT);
			}
			/**
			 * JACKPOT PRINTED (103) to be sent to the AlertsEngine
			 */
			sendAlert(rtnJackpotDTO, IAlertMessageConstants.JACKPOT_PRINTED);
				

			/** SEND DIRECTED MSG TO MESSAGING TO CLEAR THE SLOT */
			if (siteValue != null && siteValue.equalsIgnoreCase("Yes")) {
				if (log.isInfoEnabled()) {
					log.info("BEFORE CALLING JACKPOT RESET METHOD");
				}
				try {
					directedResetHandPaidJackpot(rtnJackpotDTO.getAssetConfigNumber(), jackpotDTO.getSiteId());
				} catch (Exception e) {
					log.error("Exception while sending directedResetHandPaidJackpot" + e);
				}
				if (log.isInfoEnabled()) {
					log.info("AFTER CALLING JACKPOT RESET METHOD");
				}
			}

			/** ASSET CALL TO GET THE ASSET LINE ADDRESS */
			AssetInfoDTO assetInfoDTO = null;
			try {
				assetInfoDTO = assetBO.getAssetInfo(rtnJackpotDTO.getAssetConfigNumber(),
						AssetParamType.ASSET_CONFIG_NUMBER, null, jackpotDTO.getSiteId(), null);
				if (assetInfoDTO != null && assetInfoDTO.getLineAddress() != null) {
					rtnJackpotDTO.setAssetlineAddr(assetInfoDTO.getLineAddress());
				}
			} catch (AssetEngineServiceException e) {
				log.error("Exception in getAssetInfo", e);
				throw new JackpotEngineServiceException(e);
			} catch (Exception e2) {
				log.error("Exception in getAssetInfo", e2);
				throw new JackpotEngineServiceException(e2);
			}

			/** GET SLIP AND PRINTER XML FILES */
			if (rtnJackpotDTO.isPostedSuccessfully()) {
				// GET AND SET BARCODE ENCODE FORMAT
				rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(
						PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));
				XMLServerUtil xmlServerUtil = new XMLServerUtil();
				try {
					String printerSchema = null;
					String slipSchema = null;
					printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
					slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
					if (log.isDebugEnabled()) {
						log.debug("printerSchema : " + printerSchema);
						log.debug("slipSchema : " + slipSchema);
						log.debug("inside the method processJackpot and xmlSeriveUtil returned the xml files as strings");
					}
					rtnJackpotDTO.setSlipSchema(slipSchema);
					rtnJackpotDTO.setPrinterSchema(printerSchema);
					rtnJackpotDTO.setEnableCheckPrint(enableCheckPrint);
				} catch (IOException e) {
					log.error(e);
					log.warn(e);
					throw new JackpotEngineServiceException(e);
				}
			}
			
			// NEW MSG ID REQUIRED FOR SYS GEN XC (NOT THE XC 10 MSG ID)
			long messageId = MessageUtil.getMessageId();

			/**
			 * SEND TO ACCOUNTING IF PROCESSING A JACKPOT FOLLOWS OLD FLOW
			 * STATUS CHANGE PENDING TO PROCESS
			 */
			// TODO this condition needs to be changed to check for "Post To Accounting" value.
			if (rtnJackpotDTO.getPostToAccounting().equalsIgnoreCase(
					ILookupTableConstants.POSTED_TO_ACCOUNTING)) {
				/**
				 * Calling Jackpot's AccountingUtil method to push info to
				 * Accounting Engine based on the below flag check
				 * 
				 */
				try {
					String postToAccounting = AppPropertiesReader.getInstance().getValue(
							PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
					if (log.isDebugEnabled()) {
						log.debug("postToAccounting: " + postToAccounting);
					}
					if (postToAccounting != null
							&& postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG)
							&& rtnJackpotDTO != null) {
						SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnJackpotDTO, currency);
						log.debug("BEFORE calling ISlipProcessorBO.process");
						if (slipInfoDTO != null && slipProcBO != null) {
							slipProcBO.process(slipInfoDTO);
						} else if (log.isDebugEnabled()) {
							log.debug("slipProcBO is NULL");
						}
						if (log.isDebugEnabled()) {
							log.debug("AFTER calling ISlipProcessorBO.process");
						}
					}
				} catch (Exception e) {
					log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
				}

				/** SEND SYSTEM GENERATED EXCEPTION - 100 - JACKPOT POSTED */
				if (log.isInfoEnabled()) {
					log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP POSTED");
				}

				try {
					SDSMessage sdsMessage = new SDSMessage();
					SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
					slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());
					slipPostedObj.setDateTime(Calendar.getInstance());
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
					slipPostedObj.setMessageId(messageId);// NEW MSG ID IS REQUIRED FOR THE SYSTEM GEN XC NOT THE MSG ID FOR XC 10
					slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
					slipPostedObj.setSiteId(jackpotDTO.getSiteId());
					slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());
					slipPostedObj.setAlertNo(IAlertMessageConstants.JACKPOT_PRINTED);
					if (rtnJackpotDTO.getAssetlineAddr() != null) {
						slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());
					}
					if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
						slipPostedObj.setCcJpAmt(jackpotDTO.getJackpotNetAmount());
					} else if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
							|| jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE) {
						slipPostedObj.setProgJpAmt(jackpotDTO.getJackpotNetAmount());
					} else {
						slipPostedObj.setNormalJpAmt(jackpotDTO.getJackpotNetAmount());
					}
					if (rtnJackpotDTO.getAuthEmployeeId1() != null
							&& rtnJackpotDTO.getAuthEmployeeId1().length() > 0) {
						slipPostedObj.setOverRide(true);
					}
					sdsMessage.setMessage(slipPostedObj);

					messagingEngineFacade.processSystemMessage(sdsMessage);
				} catch (Exception e1) {
					log.error("Exception while sending sendSystemGeneratedException - JP POSTED", e1);
				}
				if (log.isInfoEnabled()) {
					log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP POSTED");
				}
				if (postToeCashSiteParam.equalsIgnoreCase("Yes")) {
					rtnJackpotDTO.setValidateEmpSession(false);
					// POSTING TO ECASH AFTER JACKPOT PROCESSING
					postToECash(rtnJackpotDTO);
				}
				
				// POST TO PROGRESSIVE AFTER JACKPOT PROCESSING
				if (rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
						|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
					if (log.isInfoEnabled()) {
						log.info("Posting TO RESET METER AND LIABILITY for Progressive JP");
					}
					progressiveBO.resetMeterAndPostLiability(rtnJackpotDTO.getAssetConfigNumber(),
							rtnJackpotDTO.getMessageId(), rtnJackpotDTO.getJackpotId(),
							rtnJackpotDTO.getJackpotNetAmount(), rtnJackpotDTO.getSiteId(),
							rtnJackpotDTO.isSlotOnline());
					if (log.isInfoEnabled()) {
						log.info("End of posting TO RESET METER AND LIABILITY for Progressive JP");
					}
				}
			} else {
				/** SEND SYSTEM GENERATED EXCEPTION - 100 - JACKPOT POSTED */
				if (log.isInfoEnabled()) {
					log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP PRINTED");
				}

				try {
					SDSMessage sdsMessage = new SDSMessage();
					SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
					slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());
					slipPostedObj.setDateTime(Calendar.getInstance());
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
					slipPostedObj.setMessageId(messageId);// NEW MSG ID IS REQUIRED FOR THE SYSTEM GEN XC NOT THE MSG ID FOR XC 10
					slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
					slipPostedObj.setSiteId(jackpotDTO.getSiteId());
					slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());
					slipPostedObj.setAlertNo(IAlertMessageConstants.JACKPOT_PRINTED);
					if (rtnJackpotDTO.getAssetlineAddr() != null) {
						slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());
					}
					if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
						slipPostedObj.setCcJpAmt(jackpotDTO.getJackpotNetAmount());
					} else if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
							|| jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE) {
						slipPostedObj.setProgJpAmt(jackpotDTO.getJackpotNetAmount());
					} else {
						slipPostedObj.setNormalJpAmt(jackpotDTO.getJackpotNetAmount());
					}
					if (rtnJackpotDTO.getAuthEmployeeId1() != null
							&& rtnJackpotDTO.getAuthEmployeeId1().length() > 0) {
						slipPostedObj.setOverRide(true);
					}
					sdsMessage.setMessage(slipPostedObj);

					messagingEngineFacade.processSystemMessage(sdsMessage);
				} catch (Exception e1) {
					log.error("Exception while sending sendSystemGeneratedException - JP PRINTED", e1);
				}
				if (log.isInfoEnabled()) {
					log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP PRINTED");
				}
				
				// POST TO PROGRESSIVE AFTER JACKPOT SLIP PRINTING
				if (rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
						|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
					if (log.isInfoEnabled()) {
						log.info("Posting TO RESET METER for Progressive JP");
					}
					progressiveBO.resetMeter( rtnJackpotDTO.getAssetConfigNumber(),
							rtnJackpotDTO.getMessageId(), rtnJackpotDTO.getJackpotId(),
							rtnJackpotDTO.getJackpotNetAmount(), rtnJackpotDTO.getSiteId(),
							rtnJackpotDTO.isSlotOnline());
					if (log.isInfoEnabled()) {
						log.info("End of posting TO RESET METER AND LIABILITY for Progressive JP");
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("End of processJackpot FACADE method");
			}
		} catch (Exception e) {
			log.error("Exception while processing pending JP", e);
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to process a printed jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotDTO processPrintJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside processPrintJackpot FACADE Method");
		}
		JackpotDTO rtnJackpotDTO = null;
		
		/** GETTING SITE CONFIG VALUE FOR CURRENCY */
		String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
				ISiteConfigConstants.CURRENCY_SYMBOL,
				ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT }), jackpotDTO.getSiteId());
		String currency = siteParamValues[0];
		String postToeCashSiteParam = siteParamValues[1];
				
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		AnAUserForm userForm = getUserDetails(jackpotDTO.getPrintEmployeeLogin(), jackpotDTO.getSiteId());
		
		if(userForm != null){
			jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
			jackpotDTO.setEmployeeLastName(userForm.getLastName());
		}	
		
		/** UPDATE THE STATUS TO PROCESSED FOR PRINTED JACKPOTS */
		try {
			rtnJackpotDTO = jackpotBO.updatePrintJackpot(jackpotDTO);
			rtnJackpotDTO.setValidateEmpSession(jackpotDTO.isValidateEmpSession());
			if(log.isDebugEnabled()) {
				log.debug("rtnJackpotDTO.getProcessFlagId()   : " + rtnJackpotDTO.getProcessFlagId());
				log.debug("rtnJackpotDTO.getSlipReferenceId() : " + rtnJackpotDTO.getSlipReferenceId());
				log.debug("rtnJackpotDTO.getStatusFlagId()    : " + rtnJackpotDTO.getStatusFlagId());
			}			
		} catch (Exception e) {
			log.error("Exception during updateProcessJackpot OR insertPouchPayAttendant", e);
			throw new JackpotEngineServiceException(e);
		}
		// Setting Print Date to the Slip reference updated TS
		rtnJackpotDTO.setPrintDate(rtnJackpotDTO.getSlipRefUpdatedTs());
		
		/** ASSET CALL TO GET THE ASSET LINE ADDRESS */
		AssetInfoDTO assetInfoDTO = null;
		try {
			assetInfoDTO = assetBO.getAssetInfo(rtnJackpotDTO.getAssetConfigNumber(),AssetParamType.ASSET_CONFIG_NUMBER, null, jackpotDTO.getSiteId(), null);					
			if(assetInfoDTO!=null && assetInfoDTO.getLineAddress()!=null){
				rtnJackpotDTO.setAssetlineAddr(assetInfoDTO.getLineAddress());
			}
		}catch (AssetEngineServiceException e) {
			log.error("Exception in getAssetInfo",e);
			throw new JackpotEngineServiceException(e);
		} catch (Exception e2) {
			log.error("Exception in getAssetInfo",e2);
			throw new JackpotEngineServiceException(e2);
		}
		
		// NEW MSG ID REQUIRED FOR SYS GEN XC AND ALERTS (NOT THE XC 10 MSG ID)
		long messageId = MessageUtil.getMessageId();
		
		/** SEND SYSTEM GENERATED EXCEPTION - 100 - JACKPOT POSTED */ // 100 to be changed as per new sys gen XC
		
		if(log.isInfoEnabled()) {
			log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP POSTED");
		}
		
		try {
			SDSMessage sdsMessage = new SDSMessage();
			SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
			slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());
			slipPostedObj.setDateTime(Calendar.getInstance());
			slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION); 
			slipPostedObj.setMessageId(messageId);// NEW MSG ID IS REQUIRED FOR THE SYSTEM GEN XC NOT THE MSG ID FOR XC 10
			slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
			slipPostedObj.setSiteId(jackpotDTO.getSiteId());				
			slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());				
			if(rtnJackpotDTO.getAssetlineAddr() != null){
				slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());
			}				
			if(jackpotDTO.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
				slipPostedObj.setCcJpAmt(jackpotDTO.getJackpotNetAmount());
			}else if(jackpotDTO.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
					|| jackpotDTO.getJackpotTypeId()== ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
				slipPostedObj.setProgJpAmt(jackpotDTO.getJackpotNetAmount());
			}else{
				slipPostedObj.setNormalJpAmt(jackpotDTO.getJackpotNetAmount());
			}	
			if(rtnJackpotDTO.getAuthEmployeeId1()!=null && rtnJackpotDTO.getAuthEmployeeId1().length()>0) {
				slipPostedObj.setOverRide(true);
			}
			sdsMessage.setMessage(slipPostedObj);		
			
			messagingEngineFacade.processSystemMessage(sdsMessage);
		} catch (Exception e1) {
			log.error("Exception while sending sendSystemGeneratedException - JP POSTED",e1);
		}		
		
		if(log.isInfoEnabled()) {
			log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP POSTED");
		}
		
		/**
		 * Calling Jackpot's AccountingUtil method to push info to Accounting Engine 
		 * based on the below flag check
		 * 
		 * POSTED TO ACCOUNTING FOR STATUS CHANGE PRINTED TO PROCESSED
		 * 
		 */		
		try
		{
			String postToAccounting = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
			if(log.isDebugEnabled()) {
				log.debug("postToAccounting: " + postToAccounting);
			}
			if(postToAccounting != null && 
					postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG) && 
					rtnJackpotDTO != null) {					
				SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnJackpotDTO, currency);					
				if(log.isDebugEnabled()) {
					log.debug("BEFORE calling ISlipProcessorBO.process");
				}
				if(slipInfoDTO != null && slipProcBO != null){
					slipProcBO.process(slipInfoDTO);
				} else if(log.isDebugEnabled()) {
					log.debug("slipProcBO is NULL");
				}
				if(log.isDebugEnabled()) {
					log.debug("AFTER calling ISlipProcessorBO.process");
				}
			}
		} catch (Exception e) {
			log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
		}
		if(postToeCashSiteParam.equalsIgnoreCase("Yes")) {
			// POSTING TO ECASH AFTER JACKPOT PROCESSING
			postToECash(rtnJackpotDTO);	
		}
		
		// POST TO PROGRESSIVE AFTER JACKPOT SLIP PROCESSING FROM CAGE
		if (rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
				|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
			if (rtnJackpotDTO.getProcessFlagId() == ILookupTableConstants.MANUAL_PROCESS_FLAG) {
				if (log.isInfoEnabled()) {
					log.info("Posting to LIABILITY for Progressive Manual JP");
				}			
				try {
					progressiveBO.postLiabilityForManualJackpot(rtnJackpotDTO.getAssetConfigNumber(),
							rtnJackpotDTO.getLstProgressiveLevel(), rtnJackpotDTO.getSiteId());
				} catch (Exception e) {
					if (log.isInfoEnabled()) {
						log.info("Exception when calling postLiabilityForManualJackpot PROG method");
					}
					throw new JackpotEngineServiceException(e);
				}
				
				if (log.isInfoEnabled()) {
					log.info("End of posting to LIABILITY for Progressive Manual JP");
				}
			} else {

				if (log.isInfoEnabled()) {
					log.info("Posting TO LIABILITY for Progressive JP");
				}
				try {
					progressiveBO.postLiability(rtnJackpotDTO.getAssetConfigNumber(), rtnJackpotDTO.getMessageId(),
									rtnJackpotDTO.getJackpotId(), rtnJackpotDTO.getSiteId(), null);
				} catch (Exception e) {
					if (log.isInfoEnabled()) {
						log.info("Exception when calling postLiability PROG method");
					}
					throw new JackpotEngineServiceException(e);
				}
				if (log.isInfoEnabled()) {
					log.info("End of posting TO LIABILITY for Progressive JP");
				}
			}
		}		
		if(log.isInfoEnabled()) {
			log.info("End of processPrintJackpot FACADE Method");
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to send the Directed Msg to Messaging when a Jackpot is cleared
	 * @param slotNo
	 * @param siteId
	 */
	public void directedResetHandPaidJackpot(String slotNo, int siteId)
	{
		if(log.isInfoEnabled()) {
			log.info("Inside directedResetHandPaidJackpot");
		}
		try {
			ResetHandPaidJackpotMessage jpMsg = new ResetHandPaidJackpotMessage();
			
			jpMsg.setAssetConfigNumber(slotNo);
			jpMsg.setSiteId(siteId);
			
			if(log.isDebugEnabled()) {
				log.debug("Slot No: "+slotNo);	
			}
         /* jpMsg.setPollCode("fc");
			jpMsg.setDmSessionId("16");
			jpMsg.setTransId("1");
			jpMsg.setSegment(1);
			jpMsg.setTotalSegments(1);
			jpMsg.setDataLength("05");
			jpMsg.setTargetId("16");//Note : Target id is 16 and not 15 as mentioned in some old docs
			jpMsg.setBlockLength("3");
			jpMsg.setProtocolType("1");
			jpMsg.setGameResponseExpected("0");
			jpMsg.setGameMessage("94"); */       
			SDSMessage sdsMessage = new SDSMessage();
			sdsMessage.setSiteId(siteId);
			sdsMessage.setMessage(jpMsg);  
			messagingEngineFacade.processDirectedMessage(sdsMessage);
		} catch (Throwable e) {
			log.error("Throwable Exception when sending directedResetHandPaidJackpot to Messaging",e);
		}
	}
	
	/**
	 * Method to process a manual jackpot slip
	 * @param jackpotDTO
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO processManualJackpot(JackpotDTO jackpotDTO, JackpotFilter filter) throws JackpotEngineServiceException 
	{		
		JackpotDTO rtnJackpotDTO = null;
		try {
			if(log.isInfoEnabled()) {
				log.info("Inside processManualJackpot FACADE method");
			}
			/**	TO DO FOR THIS METHOD
			 * getJackpotAssetInfo
			 * getUserDetails
			 * updateProcessManualJackpot
			 * sendAlert
			 * sendSystemGeneratedException
			 * getPrinterAndSchemaXmlFiles
			 */
			if(log.isDebugEnabled()) {
				log.debug("***********************************************************************************");
			}
			/** ASSET CALL TO GET THE ASSET DETAILS */
			AssetSearchFilter assetSearchFilter = setAssetSearchFilter();
			AssetInfoDTO assetInfoDTO = null;
			boolean isXSeriesSlot = false;
			
			try {
				if (filter.getFieldType().equalsIgnoreCase("Slot")) {
					assetInfoDTO = assetBO.getAssetInfo(jackpotDTO.getAssetConfigNumber(),
							AssetParamType.ASSET_CONFIG_NUMBER, assetSearchFilter, jackpotDTO.getSiteId(),
							null);
					if (assetInfoDTO.getAssetConfigLocation() != null) {
						jackpotDTO.setAssetConfigLocation(assetInfoDTO.getAssetConfigLocation());
					}
				} else if (filter.getFieldType().equalsIgnoreCase("Stand")) {
					assetInfoDTO = assetBO.getAssetInfo(jackpotDTO.getAssetConfigLocation(),
							AssetParamType.ASSET_CONFIG_LOCATION, assetSearchFilter, jackpotDTO.getSiteId(),
							null);
					if (assetInfoDTO.getAssetConfigNumber() != null) {
						jackpotDTO.setAssetConfigNumber(assetInfoDTO.getAssetConfigNumber());
						if (log.isDebugEnabled()) {
							log.debug("Asset Config Number..." + assetInfoDTO.getAssetConfigNumber());
						}
					}
				}
				if (assetInfoDTO.getDenominaton() != null){
					jackpotDTO.setSlotDenomination(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getDenominaton()));
					if(log.isDebugEnabled()) {
						log.debug("Asset Slot Denom: "+jackpotDTO.getSlotDenomination());
					}
				}						
				if (assetInfoDTO.getAccountingDenom() != null){
					jackpotDTO.setGmuDenom(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getAccountingDenom()));
				}
				if (assetInfoDTO.getAreaLongName() != null){
					jackpotDTO.setAreaLongName(assetInfoDTO.getAreaLongName());
				}
				if (assetInfoDTO.getAreaName() != null){
					jackpotDTO.setAreaShortName(assetInfoDTO.getAreaName());
				}
				if(assetInfoDTO.getTypeDesc() != null){
					jackpotDTO.setSlotDescription(assetInfoDTO.getTypeDesc());	
				}
				if(assetInfoDTO.getLineAddress() != null){
					jackpotDTO.setAssetlineAddr(assetInfoDTO.getLineAddress());
				}
				if(assetInfoDTO.getRegulatoryId() != null){
					jackpotDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				}
				/** GETTING XSERIES SLOT RELATED INFORMATION */
				String xSeriesSlot = assetInfoDTO.getXSeriesEnum().getXSeriesEnumValue();
				if (log.isDebugEnabled()) {
					log.debug("XSERIES SLOT VALUE : " + xSeriesSlot);
				}
				if (xSeriesSlot != null  && !xSeriesSlot.trim().isEmpty() 
						&& !xSeriesSlot.equalsIgnoreCase(IAppConstants.NORMAL_SLOT)) {
					isXSeriesSlot = true;
				}
				if (log.isDebugEnabled()) {
					log.debug("IS XSERIES SLOT : " + isXSeriesSlot);
				}
			} catch (AssetEngineServiceException e) {
				log.error("Exception in getAssetInfo for " + filter.getFieldType(),e);
				throw new JackpotEngineServiceException(e);
			} catch (Exception e2) {
				log.error("Exception in getAssetInfo for " + filter.getFieldType(),e2);
				throw new JackpotEngineServiceException(e2);
			}
			if (assetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE) {
				if (log.isDebugEnabled()) {
					log.debug("INVALID SLOT---->" + jackpotDTO.getAssetConfigNumber());
				}
				return jackpotDTO;
			}
			
			/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
			AnAUserForm userForm = getUserDetails(jackpotDTO.getPrintEmployeeLogin(), jackpotDTO.getSiteId());
			
			if (userForm != null) {
				jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
				jackpotDTO.setEmployeeLastName(userForm.getLastName());
			}
			
			/** GET SITE CONFIG PARAMETER */
			String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
					ISiteConfigConstants.CURRENCY_SYMBOL, ISiteConfigConstants.IS_CASHIER_DESK_ENABLED,
					ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT,
					ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP,
					ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER }), jackpotDTO.getSiteId());
			
			String currency = siteParamValues[0];
			String cashierDeskEnabled = siteParamValues[1];
			String postToeCashSiteParam = siteParamValues[2];
			String enableCheckPrint = siteParamValues[3];
			String generateRandSeqNum = siteParamValues[4];
			
			jackpotDTO.setCashierDeskEnabled(cashierDeskEnabled);
			jackpotDTO.setEnableCheckPrint(enableCheckPrint);
			jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
			
			/** CALL TO UPDATE MANUAL JACKPOT TO THE SLIP SCHEMA */			
			try {
				rtnJackpotDTO = jackpotBO.updateProcessManualJackpot(jackpotDTO);
			} catch (Exception e1) {
				log.error("Exception during updateProcessManualJackpot",e1);
			}
			
			/** 
			 * SEND ALERT FOR MANUAL JP
			 * MANUAL JACKPOT ENTERED FOR: (105) to be sent to the AlertsEngine
			 */
			sendAlert(jackpotDTO, IAlertMessageConstants.MANUAL_JP_ENTERED_FOR);
							
			/** SEND SYSTEM GENERATED EXCEPTION */			
			if(log.isInfoEnabled()) {
				log.info("BEFORE CALLING sendSystemGeneratedException METHOD");
			}
			
			try {
				SDSMessage sdsMessage = new SDSMessage();
				SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
				if (jackpotDTO.getAssetConfigNumber() != null) {
					slipPostedObj.setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
				}		
				slipPostedObj.setDateTime(Calendar.getInstance());
				if (ILookupTableConstants.POSTED_TO_ACCOUNTING.equalsIgnoreCase(rtnJackpotDTO.getPostToAccounting())) {
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				} else if (ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING.equalsIgnoreCase(rtnJackpotDTO.getPostToAccounting())) {
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
				}
				if(log.isDebugEnabled()) {
					log.debug("slipPostedObj.getExceptionCode() : " + slipPostedObj.getExceptionCode());
				}
				slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
				slipPostedObj.setSiteId(jackpotDTO.getSiteId());
				slipPostedObj.setJackpotId(jackpotDTO.getJackpotId().toUpperCase());				
				slipPostedObj.setAlertNo(IAlertMessageConstants.MANUAL_JP_ENTERED_FOR);
				if(jackpotDTO.getAssetlineAddr() != null){
					slipPostedObj.setLineAddress(jackpotDTO.getAssetlineAddr());
				}					
				if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
					slipPostedObj.setCcJpAmt(jackpotDTO.getJackpotNetAmount());
				} else if (jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
						|| jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE) {
					slipPostedObj.setProgJpAmt(jackpotDTO.getJackpotNetAmount());
				} else {
					slipPostedObj.setNormalJpAmt(jackpotDTO.getJackpotNetAmount());
				}
				if(rtnJackpotDTO.getAuthEmployeeId1() != null && rtnJackpotDTO.getAuthEmployeeId1().length()>0) {
					slipPostedObj.setOverRide(true);
				}
				slipPostedObj.setMessageId(rtnJackpotDTO.getMessageId());
				sdsMessage.setMessage(slipPostedObj);	
				
				messagingEngineFacade.processSystemMessage(sdsMessage);		
				if(log.isInfoEnabled()) {
					log.info("AFTER CALLING sendSystemGeneratedException METHOD");
				}
			} catch (Exception e1) {
				log.error("Exception during sendSystemGeneratedException METHOD",e1);
			}
			
			/** GET THE PRINTER AND SLIP SCHEMA XML FILES */			
			if (rtnJackpotDTO.isPostedSuccessfully()) {
				// GET AND SET BARCODE ENCODE FORMAT
				rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(
						PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));
				XMLServerUtil xmlServerUtil = new XMLServerUtil();
				try {
					String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
					String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
					if (log.isDebugEnabled()) {
						log.debug("printerSchema : " + printerSchema);
						log.debug("slipSchema : " + slipSchema);
						log.debug("inside the method processManualJackpot and xmlServerUtil returned the xml files as strings");
					}
					rtnJackpotDTO.setSlipSchema(slipSchema);
					rtnJackpotDTO.setPrinterSchema(printerSchema);
					rtnJackpotDTO.setEnableCheckPrint(enableCheckPrint != null ? enableCheckPrint : "");
				} catch (IOException e) {
					log.error(e);
					log.warn(e);
					throw new JackpotEngineServiceException(e);
				}
			}
			
			/** POSTED TO ACCOUNTING ONLY IF OLD FLOW PROCESS IS ENABLED IN SITE CONFIG */
			if (ILookupTableConstants.POSTED_TO_ACCOUNTING.equalsIgnoreCase(rtnJackpotDTO.getPostToAccounting())) {
				/**
				 * Calling Jackpot's AccountingUtil method to push info to Accounting Engine 
				 * based on the below flag check
				 */		
				try {
					String postToAccounting = AppPropertiesReader.getInstance().getValue(
							PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
					if (log.isDebugEnabled()) {
						log.debug("postToAccounting: " + postToAccounting);
					}
					if (postToAccounting != null
							&& postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG)
							&& rtnJackpotDTO != null) {					
						SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnJackpotDTO, currency);					
						if(log.isDebugEnabled()) {
							log.debug("BEFORE calling ISlipProcessorBO.process");
						}
						if (slipInfoDTO != null && slipProcBO != null) {
							slipProcBO.process(slipInfoDTO);
						} else if (log.isDebugEnabled()) {
							log.debug("slipProcBO is NULL");
						}
						if(log.isDebugEnabled()) {
							log.debug("AFTER calling ISlipProcessorBO.process");	
						}
					}
				} catch (Exception e) {
					log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
				}
				// POSTING TO ECASH AFTER JACKPOT PROCESSING
				if (postToeCashSiteParam.equalsIgnoreCase("Yes")) {
					rtnJackpotDTO.setValidateEmpSession(false);					
					postToECash(rtnJackpotDTO);
				}
				// POST TO PROGRESSIVE AFTER JACKPOT SLIP PROCESSING
				if (rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
						|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
					if (log.isInfoEnabled()) {
						log.info("Posting to RESET METER and LIABILITY for Progressive Manual JP");
					}
					try {
						progressiveBO.resetMeterAndPostLiabilityForManualJackpot(
								rtnJackpotDTO.getAssetConfigNumber(), rtnJackpotDTO.getLstProgressiveLevel(),
								rtnJackpotDTO.getSiteId(), isXSeriesSlot);
					} catch (Exception e) {
						if (log.isInfoEnabled()) {
							log.info("Exception when calling resetMeterAndPostLiabilityForManualJackpot PROG method");
						}
						throw new JackpotEngineServiceException(e);
					}
					if (log.isInfoEnabled()) {
						log.info("End of posting to RESET METER and LIABILITY for Progressive Manual JP");
					}
				}
			} 
			/** POST TO PROGRESSIVE FOR RESET METER AFTER JACKPOT SLIP PRINTING */
			// SLIP IS PRINTED ONLY AND NO DETAILS POSTED TO ACCOUNTING
			// HENCE POST TO PROGRESSIVE FOR RESET METER ONLY FOR NON-XSERIES SLOT
			if (ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING.equalsIgnoreCase(
					rtnJackpotDTO.getPostToAccounting())) {				
				if (!isXSeriesSlot) {
					if (rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE
							|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE) {
						if (log.isInfoEnabled()) {
							log.info("Posting to RESET METER for Progressive Manual JP");
						}
						try {
							progressiveBO.resetMeterForManualJackpot(rtnJackpotDTO.getAssetConfigNumber(),
									rtnJackpotDTO.getLstProgressiveLevel(), rtnJackpotDTO.getSiteId());

						} catch (Exception e) {
							if (log.isInfoEnabled()) {
								log.info("Exception when calling resetMeterForManualJackpot PROG method");
							}
							throw new JackpotEngineServiceException(e);
						}
						if (log.isInfoEnabled()) {
							log.info("End of posting to RESET METER for Progressive Manual JP");
						}
					}
				} else if (log.isDebugEnabled()) {
					log.debug("SLOT is an XSeries Slot. Hence not send to Progressive for Reset Meter");
				}
			}	

			if (log.isDebugEnabled()) {
				log.debug("***********************************************************************************");
			} 					
		} catch (RuntimeException e) {
			log.error("Exception in processManualJackpot",e);
		}
		if(log.isInfoEnabled()) {
			log.info("End of processManualJackpot FACADE method");
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to update the jackpot status on a void for Processed / Printed slips from JP UI
	 * and Pending slip from S2S Kiosk
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO postJackpotVoidStatus(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {
		log.info("Inside postJackpotVoidStatus FACADE method");
		/** TO DO FOR THIS METHOD
		 * getUserDetails
		 * postUpdateJackpotVoidStatus
		 * sendAlert
		 * sendSystemGeneratedException
		 * getPrinterAndSchemaXmlFiles
		 */
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		AnAUserForm userForm = getUserDetails(jackpotDTO.getVoidEmployeeLogin(), jackpotDTO.getSiteId());
		
		if(userForm!=null){
			jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
			jackpotDTO.setEmployeeLastName(userForm.getLastName());
		}	
		
		/** GET SITE CONFIG PARAMETER */
		String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
				ISiteConfigConstants.CURRENCY_SYMBOL,
				ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT,
				ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP }), jackpotDTO.getSiteId());
		String currency = siteParamValues[0];
		String postToeCashSiteParam = siteParamValues[1];
		String enableCheckPrint = siteParamValues[2];
						
		/** CALL TO UPDATE VOID REC IN SLIP SCHEMA*/
		JackpotDTO rtnJackpotDTO = null;
		short oldJackpotStatus = getJackpotStatus(jackpotDTO.getSequenceNumber(), jackpotDTO.getSiteId());
		log.debug("Status of Jackpot before VOID processing : " + oldJackpotStatus);
		try {
			rtnJackpotDTO = jackpotBO.postUpdateJackpotVoidStatus(jackpotDTO);
			rtnJackpotDTO.setPrintDate(rtnJackpotDTO.getSlipRefUpdatedTs());
		} catch (Exception e1) {
			log.error("Exception in postUpdateJackpotVoidStatus", e1);
		}
		log.debug("Jackpot Status after VOID processing : " + rtnJackpotDTO.getStatusFlagId());
			
		//FOR A NORMAL PROCESSED VOID SLIP
		// SLIP INFO SHOULD BE SENT TO ACCOUNTING, ALERT and SYSTEM GEN XC SHOULD BE SENT 
		if(!jackpotDTO.isS2SPendingVoid()) {
			
			/** ASSET CALL TO GET THE ASSET LINE ADDRESS */
			AssetInfoDTO assetInfoDTO = null;
			try {
				assetInfoDTO = assetBO.getAssetInfo(rtnJackpotDTO.getAssetConfigNumber(),
						AssetParamType.ASSET_CONFIG_NUMBER, null, jackpotDTO.getSiteId(), null);
				if (assetInfoDTO != null && assetInfoDTO.getLineAddress() != null) {
					rtnJackpotDTO.setAssetlineAddr(assetInfoDTO.getLineAddress());
				}
			}catch (AssetEngineServiceException e) {
				log.error("Exception in getAssetInfo: ",e);
				throw new JackpotEngineServiceException(e);
			} catch (Exception e2) {
				log.error("Exception in getAssetInfo: ",e2);
				throw new JackpotEngineServiceException(e2);
			}
			
			// SEND ALERT FOR ONLY PROCESSED VOIDED SLIPS
			if (rtnJackpotDTO.isSendAlert() && jackpotDTO != null) {
				/**
				 * SLIP VOIDED (19) to be sent to the AlertsEngine
				 */
				sendAlert(rtnJackpotDTO, IAlertMessageConstants.SLIP_VOIDED);
			}

			if (rtnJackpotDTO.getPostToAccounting() != null
					&& rtnJackpotDTO.getPostToAccounting().equalsIgnoreCase("Y")) {
				
				// IF SLPR_ACC_POST = "Y" THEN VOIDED JACKPOT HAS TO BE POSTED TO ACCOUNTING
				/**
				 * Calling Jackpot's AccountingUtil method to push info to Accounting Engine 
				 * based on the below flag check
				 */		
				try
				{
					String postToAccounting = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
					if(log.isDebugEnabled()) {
						log.debug("postToAccounting: " + postToAccounting);
					}
					if(postToAccounting != null 
							&& postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG) 
							&& rtnJackpotDTO != null) {					
						SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnJackpotDTO, currency);					
						if(log.isDebugEnabled()) {
							log.debug("BEFORE calling ISlipProcessorBO.process");
						}
						if(slipInfoDTO != null && slipProcBO != null){
							slipProcBO.process(slipInfoDTO);
						} else if(log.isDebugEnabled()) {
							log.debug("slipProcBO is null");
						}
						if(log.isDebugEnabled()) {
							log.debug("AFTER calling ISlipProcessorBO.process");					
						}
					}
				} catch (Exception e) {
					log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
				}
				
				// SEND SYSTEM GENERATED EXCEPTION FOR PROCESSED VOIDED SLIP		
				if(log.isInfoEnabled()) {
					log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP POSTED");
				}
				
				try {
					SDSMessage sdsMessage = new SDSMessage();
					SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
					if(rtnJackpotDTO.getAssetConfigNumber() != null){
						slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());
					}
					slipPostedObj.setDateTime(Calendar.getInstance());
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
					slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
					slipPostedObj.setSiteId(rtnJackpotDTO.getSiteId());				
					slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());				
					slipPostedObj.setAlertNo(IAlertMessageConstants.SLIP_VOIDED);				
					if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
						slipPostedObj.setCcJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					}else if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
							|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
						slipPostedObj.setProgJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					} else {
						slipPostedObj.setNormalJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					}	
					if(rtnJackpotDTO.getAssetlineAddr() != null){
						slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());
					}
					if(rtnJackpotDTO.getAuthEmployeeId1() != null && rtnJackpotDTO.getAuthEmployeeId1().length()>0) {
						slipPostedObj.setOverRide(true);
					}
					slipPostedObj.setMessageId(MessageUtil.getMessageId());
					sdsMessage.setMessage(slipPostedObj);			
					messagingEngineFacade.processSystemMessage(sdsMessage);			
					if(log.isInfoEnabled()) {
						log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP POSTED");
					}
				} catch (Exception e1) {
					log.error("Exception in sendSystemGeneratedException METHOD - JP POSTED",e1);
				}
				if(postToeCashSiteParam.equalsIgnoreCase("Yes")) {
					rtnJackpotDTO.setValidateEmpSession(false);
					// POSTING TO ECASH AFTER JACKPOT PROCESSING
					postToECash(rtnJackpotDTO);	
				}
			} else if(rtnJackpotDTO.getPostToAccounting() !=null 
					&& rtnJackpotDTO.getPostToAccounting().equalsIgnoreCase("N")) {
				// SEND SYSTEM GENERATED EXCEPTION FOR PROCESSED VOIDED SLIP		
				if(log.isInfoEnabled()) {
					log.info("BEFORE CALLING sendSystemGeneratedException METHOD - JP PRINTED");
				}
				
				try {
					SDSMessage sdsMessage = new SDSMessage();
					SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
					if(rtnJackpotDTO.getAssetConfigNumber() != null){
						slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());
					}
					slipPostedObj.setDateTime(Calendar.getInstance());
					slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
					slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
					slipPostedObj.setSiteId(rtnJackpotDTO.getSiteId());				
					slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());				
					slipPostedObj.setAlertNo(IAlertMessageConstants.SLIP_VOIDED);				
					if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
						slipPostedObj.setCcJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					}else if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
							|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
						slipPostedObj.setProgJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					} else {
						slipPostedObj.setNormalJpAmt(rtnJackpotDTO.getJackpotNetAmount());
					}	
					if(rtnJackpotDTO.getAssetlineAddr() != null){
						slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());
					}
					if(rtnJackpotDTO.getAuthEmployeeId1() != null && rtnJackpotDTO.getAuthEmployeeId1().length()>0) {
						slipPostedObj.setOverRide(true);
					}
					slipPostedObj.setMessageId(MessageUtil.getMessageId());
					sdsMessage.setMessage(slipPostedObj);			
					messagingEngineFacade.processSystemMessage(sdsMessage);			
					if(log.isInfoEnabled()) {
						log.info("AFTER CALLING sendSystemGeneratedException METHOD - JP PRINTED");
					}
				} catch (Exception e1) {
					log.error("Exception in sendSystemGeneratedException METHOD - JP PRINTED",e1);
				}
			}			
		}	
		
		// GET THE PRINTER AND SLIP SCHEMA XML FILES
		// PRINT VOID SLIP FOR BOTH TYPES OF VOIDS
		if (rtnJackpotDTO.isPostedSuccessfully()) {
			XMLServerUtil xmlServerUtil = new XMLServerUtil();
			try {
				String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);
				String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
				if(log.isDebugEnabled()) {
					log.debug("printerSchema : " + printerSchema);
					log.debug("slipSchema : " + slipSchema);
					log.debug("inside the method postJackpotVoidStatus and xmlServerUtil returned the xml files as strings");
				}
				rtnJackpotDTO.setSlipSchema(slipSchema);
				rtnJackpotDTO.setPrinterSchema(printerSchema);
				rtnJackpotDTO.setEnableCheckPrint(enableCheckPrint);
			} catch (IOException e) {
				log.error(e);
				log.warn(e);
				throw new JackpotEngineServiceException(e);
			}
		}
		if(log.isInfoEnabled()) {
			log.info("End of postJackpotVoidStatus FACADE method");
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to update the jackpot status on a reprint
	 * @param jackptoDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO postJackpotReprint(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {	
		JackpotDTO rtnJackpotDTO = null;	
		JackpotDTO rtnOldSlipJackpotDTO =null;
		try {
			if(log.isInfoEnabled()) {
				log.info("Inside postJackpotReprint FACADE method");
			}
			/** TO DO FOR THIS METHOD
			 * getUserDetails
			 * postUpdateJackpotReprint
			 * getPrinterAndSchemaXmlFiles
			 */
			
			/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
			AnAUserForm userForm = getUserDetails(jackpotDTO.getPrintEmployeeLogin(), jackpotDTO.getSiteId());
			
			if(userForm!=null){
				jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
				jackpotDTO.setEmployeeLastName(userForm.getLastName());
			}
			
			/** GET SITE CONFIG PARAMETER TO GET THE ALLOW JACKPOT OLD PROCESS FLOW*/
			String[] siteParamValues = JackpotUtil.getSiteParamValueList(JackpotUtil.getSiteKeyList(new String[] {
					ISiteConfigConstants.IS_CASHIER_DESK_ENABLED,
					ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER,
					ISiteConfigConstants.ALLOW_AUTO_VOID_ON_REPRINTED_JP,
					ISiteConfigConstants.CURRENCY_SYMBOL}), jackpotDTO.getSiteId());
			String cashierDeskEnabled 	= siteParamValues[0];
			String generateRandSeqNum 	= siteParamValues[1];
			String autoVoidReprintedJp 	= siteParamValues[2];
			String currency 			= siteParamValues[3];
			
			// Setting Cashier Desk Flow value to JackpotDTO
			jackpotDTO.setCashierDeskEnabled(cashierDeskEnabled);
			jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
			jackpotDTO.setAutoVoidReprintedJp(autoVoidReprintedJp);						
			
			/**
			 * SA PEERMONT REQ: ALLOW AUTO VOID ON REPRINTED JP SLIP
			 * IF Yes, VOID the current reprinted slip and create a new slip in the SLIP REF Table
			 * ELSE legacy Reprint functionality 
			 */
			if(autoVoidReprintedJp.equalsIgnoreCase("YES")) {
				/** 
				 * 1) UPDATE DB SCHEMA
				 * 2) POST TO ACCOUNTING FOR VOID ACTION and FOR NEW PROCESSED SLIP ONLY IF SLIP POST TO ACC FLAG IS Y
				 * 3) SEND VOID ALERT 
				 * 4) SEND PRINT ALERT 
				 * 5) SEND SYSTEM GENERATED XC FOR VOID
				 * 6) SEND SYSTEM GENERATED XC FOR PRINT/PROCESS  
				 * 7) PRINT NEW PROCESS SLIP FORMAT FOR NEW SLIP
				 */
				//UPDATE DB SCHEMA
				/** CALL TO UPDATE REPRINT REC IN SLIP SCHEMA*/
				try {
					rtnOldSlipJackpotDTO = jackpotBO.postUpdateJackpotReprint(jackpotDTO);	
					log.debug("rtnOldSlipJackpotDTO: "+rtnOldSlipJackpotDTO.toString());
					log.debug("rtnOldSlipJackpotDTO.getPostToAccounting()"+rtnOldSlipJackpotDTO.getPostToAccounting());
				
				} catch (Exception e1) {
					log.error("Exception in postUpdateJackpotReprint",e1);
					throw new JackpotEngineServiceException(e1);
				}
				String postToAccounting = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_ACCOUNTING_PUSH_ENABLED);
				log.debug("postToAccounting: "+postToAccounting);
				//IF PostToAccounting IS YES, THEN VOID IS DONE ON A PROCESSED SLIP AND SHOULD BE POSTED TO ACCOUNTING
				//POST TO ACCOUNTING FOR VOID ACTION IF SLIP IS POSTED TO ACCOUNTING
				//AND REPRINT WAS DONE ON A PROCESSED SLIP
				if(rtnOldSlipJackpotDTO != null) {
					if(rtnOldSlipJackpotDTO.getPostToAccounting().equalsIgnoreCase("Y")
							&& postToAccounting !=null && postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG)) {
						try
						{				
							SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnOldSlipJackpotDTO, currency);					
							log.debug("BEFORE calling ISlipProcessorBO.process");				
							if(slipInfoDTO != null && slipProcBO != null){
								slipProcBO.process(slipInfoDTO);
							} else{
								log.debug("slipProcBO is null");
							}
							log.info("Posted info to Accounting");					
							
						} catch (Exception e) {
							log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
						}
					}
				}
							
				
				try {
					log.debug("BEFORE calling postUpdateJpAutoVoidReprint");
					jackpotDTO.setSlipReferenceId(rtnOldSlipJackpotDTO.getSlipReferenceId());
					rtnJackpotDTO = jackpotBO.postUpdateJpAutoVoidReprint(jackpotDTO);	
					log.debug("AFTER calling postUpdateJpAutoVoidReprint");
					log.debug("rtnJackpotDTO: "+toString());
					if(jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("Yes") && log.isDebugEnabled()) {
						log.debug("Jackpot slip is created with a new sequence number " + rtnJackpotDTO.getSequenceNumber() + " after reprint");
					}
				} catch (Exception e1) {
					log.error("Exception in postUpdateJpAutoVoidReprint",e1);
					throw new JackpotEngineServiceException(e1);
				}								
				
				if(rtnOldSlipJackpotDTO != null) {
					//SLIP VOIDED ALERT (19) to be sent to the AlertsEngine				
					sendAlert(rtnOldSlipJackpotDTO, IAlertMessageConstants.SLIP_VOIDED);
					
					//SEND SYSTEM GENERATED XC FOR VOID
					try {
						SDSMessage sdsMessage = new SDSMessage();
						SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
						slipPostedObj.setAssetConfigNumber(rtnOldSlipJackpotDTO.getAssetConfigNumber());				
						slipPostedObj.setDateTime(Calendar.getInstance());
						
						if(rtnOldSlipJackpotDTO != null
								&& rtnOldSlipJackpotDTO.getPostToAccounting().equalsIgnoreCase("Y")) {
							slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
							log.info("SENT sendSystemGeneratedException JP POSTED");
							
						}else if(rtnOldSlipJackpotDTO != null
								&& rtnOldSlipJackpotDTO.getPostToAccounting().equalsIgnoreCase("N")) {
							slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
							log.info("SENT sendSystemGeneratedException JP PRINTED");
						}				
						slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
						slipPostedObj.setSiteId(rtnOldSlipJackpotDTO.getSiteId());				
						slipPostedObj.setJackpotId(rtnOldSlipJackpotDTO.getJackpotId().toUpperCase());				
						slipPostedObj.setAlertNo(IAlertMessageConstants.SLIP_VOIDED);				
						if(rtnOldSlipJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
							slipPostedObj.setCcJpAmt(rtnOldSlipJackpotDTO.getJackpotNetAmount());
						}else if(rtnOldSlipJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
								|| rtnOldSlipJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
							slipPostedObj.setProgJpAmt(rtnOldSlipJackpotDTO.getJackpotNetAmount());
						} else {
							slipPostedObj.setNormalJpAmt(rtnOldSlipJackpotDTO.getJackpotNetAmount());
						}	
						slipPostedObj.setLineAddress(rtnOldSlipJackpotDTO.getAssetlineAddr());				
						if(rtnOldSlipJackpotDTO.getAuthEmployeeId1() != null && rtnOldSlipJackpotDTO.getAuthEmployeeId1().length()>0) {
							slipPostedObj.setOverRide(true);
						}
						slipPostedObj.setMessageId(MessageUtil.getMessageId());
						sdsMessage.setMessage(slipPostedObj);			
						messagingEngineFacade.processSystemMessage(sdsMessage);			
					} catch (Exception e1) {
						log.error("Exception in sendSystemGeneratedException METHOD - JP POSTED/JP PRINTED",e1);
					}
				}
					
				//POST TO ACCOUNTING FOR THE NEW SLIP IF CD IS DISABLED, ONLY FOR PROCESSED STATUS
				if(jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("No") && rtnJackpotDTO!=null) {
					if(postToAccounting !=null && postToAccounting.equalsIgnoreCase(IAppConstants.TRUE_FLAG)) {
						try
						{				
							SlipInfoDTO slipInfoDTO = AccountingUtil.populateSlipInfoDTO(rtnJackpotDTO, currency);					
							log.debug("BEFORE calling ISlipProcessorBO.process");				
							if(slipInfoDTO != null && slipProcBO != null){
								slipProcBO.process(slipInfoDTO);
							} else{
								log.debug("slipProcBO is null");
							}
							log.info("Posted info to Accounting");						
						} catch (Exception e) {
							log.error("Exception when calling AccountingUtil.postAccountingInfo", e);
						}
					}
				}
				
				if(rtnJackpotDTO!=null) {
					//SEND JP PRINTED(103)/MANUAL JP(105) ALERT FOR NEW SLIP
					int alertForNewSlip = 0;
					if(rtnJackpotDTO.getProcessFlagId()== ILookupTableConstants.MANUAL_PROCESS_FLAG) {
						alertForNewSlip = IAlertMessageConstants.MANUAL_JP_ENTERED_FOR;
						sendAlert(rtnJackpotDTO, alertForNewSlip);
					}else {
						alertForNewSlip = IAlertMessageConstants.JACKPOT_PRINTED;
						sendAlert(rtnJackpotDTO, alertForNewSlip);
					}
					
					//SEND SYSTEM GENERATED XC FOR NEW SLIP
					try {
						SDSMessage sdsMessage = new SDSMessage();
						SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
						slipPostedObj.setAssetConfigNumber(rtnJackpotDTO.getAssetConfigNumber());				
						slipPostedObj.setDateTime(Calendar.getInstance());
						
						if(rtnJackpotDTO != null
								&& rtnJackpotDTO.getPostToAccounting().equalsIgnoreCase("Y")) {
							slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
							log.info("SENT sendSystemGeneratedException JP POSTED");
							
						}else if(rtnJackpotDTO != null
								&& rtnJackpotDTO.getPostToAccounting().equalsIgnoreCase("N")) {
							slipPostedObj.setExceptionCode(ICommonConstants.JACKPOT_PRINTED_SYS_EXCEPTION);
							log.info("SENT sendSystemGeneratedException JP PRINTED");
						}				
						slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
						slipPostedObj.setSiteId(rtnJackpotDTO.getSiteId());				
						slipPostedObj.setJackpotId(rtnJackpotDTO.getJackpotId().toUpperCase());				
						slipPostedObj.setAlertNo(alertForNewSlip);				
						if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT){
							slipPostedObj.setCcJpAmt(rtnJackpotDTO.getJackpotNetAmount());
						}else if(rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
								|| rtnJackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE){
							slipPostedObj.setProgJpAmt(rtnJackpotDTO.getJackpotNetAmount());
						} else {
							slipPostedObj.setNormalJpAmt(rtnJackpotDTO.getJackpotNetAmount());
						}	
						slipPostedObj.setLineAddress(rtnJackpotDTO.getAssetlineAddr());				
						if(rtnJackpotDTO.getAuthEmployeeId1() != null && rtnJackpotDTO.getAuthEmployeeId1().length()>0) {
							slipPostedObj.setOverRide(true);
						}
						slipPostedObj.setMessageId(MessageUtil.getMessageId());
						sdsMessage.setMessage(slipPostedObj);			
						messagingEngineFacade.processSystemMessage(sdsMessage);			
					} catch (Exception e1) {
						log.error("Exception in sendSystemGeneratedException METHOD - JP POSTED/JP PRINTED",e1);
					}
				}					
				
			}else{
				/** 
				 * LEGACY REPRINT LOGIC
				 * CALL TO UPDATE REPRINT REC IN SLIP SCHEMA
				 */
				try {
					rtnJackpotDTO = jackpotBO.postUpdateJackpotReprint(jackpotDTO);
					if(jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("Yes") && log.isDebugEnabled()) {
						log.debug("Jackpot slip is created with a new sequence number " + rtnJackpotDTO.getSequenceNumber() + " after reprint");
					}
				} catch (Exception e1) {
					log.error("Exception in postUpdateJackpotReprint",e1);
					throw new JackpotEngineServiceException(e1);
				}
			}
					
			/** GET THE PRINTER AND SLIP SCHEMA XML FILES*/
			
			if (rtnJackpotDTO != null && rtnJackpotDTO.isPostedSuccessfully()) {
				
				//GET AND SET BARCODE ENCODE FORMAT
				rtnJackpotDTO.setBarEncodeFormat(AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_BARCODE_ENCODE_FORMAT));
				XMLServerUtil xmlServerUtil = new XMLServerUtil();
				try {
					String printerSchema = xmlServerUtil.getFileAsString(IAppConstants.PRINTER_CMD_XML_FILE_PATH);				
					String slipSchema = xmlServerUtil.getFileAsString(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);
					if(log.isDebugEnabled()) {
					log.debug("printerSchema : "+printerSchema);
					log.debug("slipSchema : "+slipSchema);
						log.debug("Inside the method postJackpotReprint and xmlSeriveUtil returned the xml files as strings");
					}
					rtnJackpotDTO.setSlipSchema(slipSchema);
					rtnJackpotDTO.setPrinterSchema(printerSchema);
				} catch (IOException e) {
					log.error(e);
					log.warn(e);
					throw new JackpotEngineServiceException(e);
				}
			}				
		} catch (Exception e) {
			log.error("Exception in postJackpotReprint Facade method", e);
			throw new JackpotEngineServiceException(e);
		}
		return rtnJackpotDTO;
	}
	
	/**
	 * Method to void all the pending jackpot slips based on the site id
	 * @param siteId
	 * @param voidEmpId
	 * @param printerUsed
	 * @param actorLogin
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> voidAllPendingJackpotSlips(int siteId,String voidEmpId, String printerUsed,String actorLogin, String kioskProcessed) throws JackpotEngineServiceException {
	
		/**
		 * getUserDetails
		 * updateVoidAllPendingJackpotSlips
		 * sendAlert
		 * processBulkVoidJackpots
		 */
		log.info("Inside voidAllPendingJackpotSlips FACADE method");
		JackpotDTO jackpotDTO = new JackpotDTO();
		jackpotDTO.setVoidEmployeeLogin(voidEmpId);
		jackpotDTO.setSiteId(siteId);
		jackpotDTO.setPrinterUsed(printerUsed);
		jackpotDTO.setAssetConfNumberUsed(kioskProcessed);
		jackpotDTO.setActorLogin(actorLogin);
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		AnAUserForm userForm = getUserDetails(voidEmpId, siteId);
		
		if (userForm != null) {
			jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
			jackpotDTO.setEmployeeLastName(userForm.getLastName());
		}
		
		/** CALL TO UPDATE VOID RECs IN SLIP SCHEMA*/
		List<JackpotDTO> jackpotDTOList = null;
		try {
			jackpotDTOList = jackpotBO.updateVoidAllPendingJackpotSlips(jackpotDTO);			
		
			/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
			for(JackpotDTO voidedjackpotDTO:jackpotDTOList){
				String key = voidedjackpotDTO.getSiteId() + "_" + voidedjackpotDTO.getSequenceNumber();				
				JackpotUtil.removePendingJPFromCache(key);
			}		
			
			
		} catch (Exception e1) {
			log.error("Exception in updateVoidAllPendingJackpotSlips",e1);
			throw new JackpotEngineServiceException(e1);
		}
		
		/** SEND ALERT FOR VOID */
		//SEND ALERT FOR VOID -- HAS TO BE DISCUSSED
		
		/** CALL TO MARKETINMG TO POST BULK VOID ADJUSTMENTS */
		
		/*List<JackpotAdjustDTO> jpAdjDTOList = new ArrayList<JackpotAdjustDTO>();
		
		for(int i=0; i<jpAdjDTOList.size(); i++){
			if(jackpotDTOList!=null){
				JackpotAdjustDTO jpAdjustDTO = new JackpotAdjustDTO();
				jpAdjustDTO.setAcnfNo(jackpotDTO.getAssetConfigNumber());
				if (jackpotDTOList.get(i).getPlayerCard() != null)
					jpAdjustDTO.setCardNumber(jackpotDTOList.get(i).getPlayerCard());
				jpAdjustDTO.setJackpotAmount(0);
				jpAdjustDTO.setMessageId(jackpotDTO.getMessageId());
				jpAdjustDTO.setSiteId(jackpotDTO.getSiteId());
				jpAdjDTOList.add(jpAdjustDTO);				
			}
		}
		
		if (jpAdjDTOList != null && jpAdjDTOList.size() > 0) {
			log.info("BEFORE calling processBulkVoidJackpots");
			try {
				marketingBO.processBulkVoidJackpot(jpAdjDTOList);
			} catch (MarketingDAOException e) {
				log.error("Exception in processBulkVoidJackpot",e);
				throw new JackpotEngineServiceException(e);
			}
			log.info("AFTER calling processBulkVoidJackpots");
		}*/
		
		return jackpotDTOList;
	}
	
	/**
	 * Method to void all the pending jackpot slips based on the slot and site id
	 * @param slotNo
	 * @param siteId
	 * @param voidEmpId
	 * @param printerUsed
	 * @param actorLogin
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> voidPendingJackpotSlipsForSlotNo(String slotNo,int siteId,String voidEmpId, String printerUsed, String actorLogin, String kioskProcessed) throws JackpotEngineServiceException 
	{
		/** TO DO FOR THIS METHOD
		 * getUserDetails
		 * updateVoidPendingJackpotSlipsForSlotNo
		 * processBulkVoidJackpots
		 */
		log.info("Inside voidPendingJackpotSlipsForSlotNo FACADE method");
		JackpotDTO jackpotDTO = new JackpotDTO();
		jackpotDTO.setAssetConfigNumber(slotNo);
		jackpotDTO.setVoidEmployeeLogin(voidEmpId);
		jackpotDTO.setSiteId(siteId);
		jackpotDTO.setPrinterUsed(printerUsed);
		jackpotDTO.setAssetConfNumberUsed(kioskProcessed);
		jackpotDTO.setActorLogin(actorLogin);
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		AnAUserForm userForm = getUserDetails(voidEmpId, siteId);
		
		if (userForm != null) {
			jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
			jackpotDTO.setEmployeeLastName(userForm.getLastName());
		}
		
		/** CALL TO UPDATE VOID RECs IN SLIP SCHEMA*/
		List<JackpotDTO> jackpotDTOList =null;
		try {
			jackpotDTOList = jackpotBO.updateVoidPendingJackpotSlipsForSlotNo(jackpotDTO);
			
			/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
			for(JackpotDTO voidedjackpotDTO:jackpotDTOList){
				String key = voidedjackpotDTO.getSiteId() + "_" + voidedjackpotDTO.getSequenceNumber();				
				JackpotUtil.removePendingJPFromCache(key);
			}			
			
		} catch (Exception e1) {
			log.error("Exception in updateVoidPendingJackpotSlipsForSlotNo",e1);
			throw new JackpotEngineServiceException(e1);
		}
		
		/** SEND ALERT FOR VOID */
		//SEND ALERT FOR VOID -- HAS TO BE DISCUSSED
		
		/** CALL TO MARKETINMG TO POST BULK VOID ADJUSTMENTS */
		
		/*List<JackpotAdjustDTO> jpAdjDTOList = new ArrayList<JackpotAdjustDTO>();
		
		for(int i=0; i<jpAdjDTOList.size(); i++){
			if(jackpotDTOList!=null){
				JackpotAdjustDTO jpAdjustDTO = new JackpotAdjustDTO();
				jpAdjustDTO.setAcnfNo(jackpotDTO.getAssetConfigNumber());
				if (jackpotDTOList.get(i).getPlayerCard() != null)
					jpAdjustDTO.setCardNumber(jackpotDTOList.get(i).getPlayerCard());
				jpAdjustDTO.setJackpotAmount(0);
				jpAdjustDTO.setMessageId(jackpotDTO.getMessageId());
				jpAdjustDTO.setSiteId(jackpotDTO.getSiteId());
				jpAdjDTOList.add(jpAdjustDTO);				
			}
		}
		
		if (jpAdjDTOList != null && jpAdjDTOList.size() > 0) {
			log.info("BEFORE calling processBulkVoidJackpots");			
			try {
				marketingBO.processBulkVoidJackpot(jpAdjDTOList);
			} catch (MarketingDAOException e) {
				log.error("Exception in processBulkVoidJackpot",e);
				throw new JackpotEngineServiceException(e);
			}
			log.info("AFTER calling processBulkVoidJackpots");
		}*/		
		
		return jackpotDTOList;
	}
	
	/**
	 * Method to get the asset details based on the Slot No / Stand No / AcnfId
	 * @param object
	 * @param jackpotAssetParamType
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotAssetInfoDTO getJackpotAssetInfo(Object object,
			JackpotAssetParamType jackpotAssetParamType, int siteId) throws JackpotEngineServiceException {
		log.info("Inside getJackpotAssetInfo FACADE method");
		AssetSearchFilter assetSearchFilter = setAssetSearchFilter();
		AssetInfoDTO assetInfoDTO = null;
		JackpotAssetInfoDTO jackpotAssetInfoDTO = new JackpotAssetInfoDTO();
		try {
			if (jackpotAssetParamType.equals(jackpotAssetParamType.ASSET_CONFIG_ID)) {
				assetInfoDTO = assetBO.getAssetInfo(object, AssetParamType.ASSET_CONFIG_ID,
						assetSearchFilter, siteId, null);
			} else if (jackpotAssetParamType.equals(jackpotAssetParamType.ASSET_CONFIG_NUMBER)) {
				log.info("Slot no");
				assetInfoDTO = assetBO.getAssetInfo(object, AssetParamType.ASSET_CONFIG_NUMBER,
						assetSearchFilter, siteId, null);
			} else if (jackpotAssetParamType.equals(jackpotAssetParamType.ASSET_CONFIG_LOCATION)) {
				log.info("Slot loc no");
				assetInfoDTO = assetBO.getAssetInfo(object, AssetParamType.ASSET_CONFIG_LOCATION,
						assetSearchFilter, siteId, null);
			}
			if (assetInfoDTO != null) {
				if (assetInfoDTO.getAreaLongName() != null)
					jackpotAssetInfoDTO.setAreaLongName(assetInfoDTO.getAreaLongName());
				if (assetInfoDTO.getAreaName() != null)
					jackpotAssetInfoDTO.setAreaShortName(assetInfoDTO.getAreaName());
				if (assetInfoDTO.getAssetConfigNumber() != null) {
					jackpotAssetInfoDTO.setAssetConfigNumber(assetInfoDTO.getAssetConfigNumber());
					if (log.isDebugEnabled()) {
						log.debug("Slot no: " + jackpotAssetInfoDTO.getAssetConfigNumber());
					}
				}
				if (assetInfoDTO.getAssetConfigLocation() != null) {
					jackpotAssetInfoDTO.setAssetConfigLocation(assetInfoDTO.getAssetConfigLocation());
					if (log.isDebugEnabled()) {
						log.debug("Stand no: " + jackpotAssetInfoDTO.getAssetConfigLocation());
					}
				}
				if (assetInfoDTO.getAssetConfigStatus() != null) {
					jackpotAssetInfoDTO.setAssetConfigStatus(assetInfoDTO.getAssetConfigStatus());
				}
				if (assetInfoDTO.getErrorCode() != 0) {
					jackpotAssetInfoDTO.setErrorCode(assetInfoDTO.getErrorCode());
				}
				if (assetInfoDTO.getGameType() != null) {
					jackpotAssetInfoDTO.setGameType(assetInfoDTO.getGameType());
				}
				if (assetInfoDTO.getHopperEnabled() != null) {
					jackpotAssetInfoDTO.setHopperEnabled(assetInfoDTO.getHopperEnabled());
				}
				if (assetInfoDTO.getMultiDenom() != null) {
					jackpotAssetInfoDTO.setMultiDenom(assetInfoDTO.getMultiDenom());
				}
				if (assetInfoDTO.getMultiGame() != null) {
					jackpotAssetInfoDTO.setMultiGame(assetInfoDTO.getMultiGame());
				}
				if (assetInfoDTO.getProgressive() != null) {
					jackpotAssetInfoDTO.setProgressive(assetInfoDTO.getProgressive());
				}
				if (assetInfoDTO.getSiteId() != null) {
					jackpotAssetInfoDTO.setSiteId(assetInfoDTO.getSiteId());
					if (log.isDebugEnabled()) {
						log.debug("Site id: " + jackpotAssetInfoDTO.getSiteId());
					}
				}
				if (assetInfoDTO.getAccountingDenom() != null) {
					jackpotAssetInfoDTO.setAccountingDenom(assetInfoDTO.getAccountingDenom());
				}
				if (assetInfoDTO.getDenominaton() != null) {
					jackpotAssetInfoDTO.setSlotDenomination(assetInfoDTO.getDenominaton());
				}
				if (assetInfoDTO.getTypeDesc() != null) {
					jackpotAssetInfoDTO.setSlotTypeDescription(assetInfoDTO.getTypeDesc());
				}
				if (assetInfoDTO.getRegulatoryId() != null) {
					jackpotAssetInfoDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************");
				}

				List<GameDetails> assetGameDetailsList = assetInfoDTO.getListGameDetails();
				if (assetGameDetailsList != null && assetGameDetailsList.size() > 0) {
					List<JackpotGameDetailsDTO> slipsGameDetailsDTOList = new ArrayList<JackpotGameDetailsDTO>();

					JackpotGameDetailsDTO slipsGameDetailsDTO = new JackpotGameDetailsDTO();

					for (int i = 0; i < assetGameDetailsList.size(); i++) {
						slipsGameDetailsDTO.setDenomAmount(assetGameDetailsList.get(i).getDenomAmount());
						slipsGameDetailsDTO.setDenomId(assetGameDetailsList.get(i).getDenomId());
						slipsGameDetailsDTO.setGameId(assetGameDetailsList.get(i).getGameId());
						slipsGameDetailsDTO.setPaytabeHoldPercent(assetGameDetailsList.get(i)
								.getPaytabeHoldPercent());
						// slipsGameDetailsDTO.setPaytableId(assetGameDetailsList.get(i).getPaytableId());
						slipsGameDetailsDTO.setThemeId(assetGameDetailsList.get(i).getThemeId());
						slipsGameDetailsDTO.setThemeName(assetGameDetailsList.get(i).getThemeName());
						slipsGameDetailsDTO.setWagerId(assetGameDetailsList.get(i).getWagerId());
						slipsGameDetailsDTO.setWagerMaxCreditsBet(assetGameDetailsList.get(i)
								.getWagerMaxCreditsBet());
					}
					slipsGameDetailsDTOList.add(slipsGameDetailsDTO);
					jackpotAssetInfoDTO.setListGameDetails(slipsGameDetailsDTOList);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("The assetInfoDTO returned by asset is null in the JackpotCommonEngineUtil class");
				}
			}
		} catch (AssetEngineServiceException e) {
			log.error("Exception in getJackpotAssetInfo Method", e);
			throw new JackpotEngineServiceException(e);
		}
		return jackpotAssetInfoDTO;
	}
	
	/**
	 * Method to get the list of asset details based on the Slot No / Stand No / AcnfId
	 * @param slotLocationNo
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotAssetInfoDTO> getJackpotListAssetInfoForLocation(
			String slotLocationNo, int siteId)
			throws JackpotEngineServiceException {
		if (log.isInfoEnabled()) {
		log.info("Inside getJackpotAssetInfoList FACADE method");
		}
		List<AssetInfoDTO> astInfoDTOLst = null;
		List<JackpotAssetInfoDTO> jpAstInfoDTOLst = new ArrayList<JackpotAssetInfoDTO>();		
		try{
				astInfoDTOLst = assetBO.getListAssetInfoForLocation(slotLocationNo, siteId);
					
				if(astInfoDTOLst!=null && astInfoDTOLst.size()>0){
					
				if (log.isInfoEnabled()) {
					log.info("astInfoDTOLst.size(): "+astInfoDTOLst.size());
				}
					
					for(int i=0; i< astInfoDTOLst.size() ;i++){
						JackpotAssetInfoDTO jpAstInfoDTO = new JackpotAssetInfoDTO();
						if(astInfoDTOLst.get(i).getAreaLongName()!=null)
							jpAstInfoDTO.setAreaLongName(astInfoDTOLst.get(i).getAreaLongName());
						if(astInfoDTOLst.get(i).getAreaName()!=null)
							jpAstInfoDTO.setAreaShortName(astInfoDTOLst.get(i).getAreaName());
						if(astInfoDTOLst.get(i).getAssetConfigNumber()!=null){
							jpAstInfoDTO.setAssetConfigNumber(astInfoDTOLst.get(i).getAssetConfigNumber());
							if (log.isInfoEnabled()) {
								log.info("Slot no: "+jpAstInfoDTO.getAssetConfigNumber());
							}
						}
						if(astInfoDTOLst.get(i).getAssetConfigLocation()!=null){
							jpAstInfoDTO.setAssetConfigLocation(astInfoDTOLst.get(i).getAssetConfigLocation());
							if (log.isInfoEnabled()) {
								log.info("Stand no: "+jpAstInfoDTO.getAssetConfigLocation());
							}				
						}
						if(astInfoDTOLst.get(i).getAssetConfigStatus()!=null){
							jpAstInfoDTO.setAssetConfigStatus(astInfoDTOLst.get(i).getAssetConfigStatus());
						}					
						if(astInfoDTOLst.get(i).getErrorCode()!=0)
							jpAstInfoDTO.setErrorCode(astInfoDTOLst.get(i).getErrorCode());
						if(astInfoDTOLst.get(i).getGameType()!=null)
							jpAstInfoDTO.setGameType(astInfoDTOLst.get(i).getGameType());
						if(astInfoDTOLst.get(i).getHopperEnabled()!=null)
							jpAstInfoDTO.setHopperEnabled(astInfoDTOLst.get(i).getHopperEnabled());
						if(astInfoDTOLst.get(i).getMultiDenom()!=null)
							jpAstInfoDTO.setMultiDenom(astInfoDTOLst.get(i).getMultiDenom());
						if(astInfoDTOLst.get(i).getMultiGame()!=null)
							jpAstInfoDTO.setMultiGame(astInfoDTOLst.get(i).getMultiGame());
						if(astInfoDTOLst.get(i).getProgressive()!=null)
							jpAstInfoDTO.setProgressive(astInfoDTOLst.get(i).getProgressive());
						if(astInfoDTOLst.get(i).getSiteId()!=null){
							jpAstInfoDTO.setSiteId(astInfoDTOLst.get(i).getSiteId());
							if (log.isInfoEnabled()) {
								log.info("Site id: "+jpAstInfoDTO.getSiteId());
							}			
						}
						if(astInfoDTOLst.get(i).getRegulatoryId()!=null)
							jpAstInfoDTO.setSealNumber(astInfoDTOLst.get(i).getRegulatoryId());
						if(astInfoDTOLst.get(i).getAccountingDenom()!=null)
							jpAstInfoDTO.setAccountingDenom(astInfoDTOLst.get(i).getAccountingDenom());
						if(astInfoDTOLst.get(i).getDenominaton()!=null)
							jpAstInfoDTO.setSlotDenomination(astInfoDTOLst.get(i).getDenominaton());
						if(astInfoDTOLst.get(i).getTypeDesc()!=null)
							jpAstInfoDTO.setSlotTypeDescription(astInfoDTOLst.get(i).getTypeDesc());	
							jpAstInfoDTO.setCreatedTs(astInfoDTOLst.get(i).getCreatedTs());						
							if (log.isDebugEnabled()) {
								log.debug("**********************************************************");
							}
						
						List<GameDetails>  assetGameDetailsList = astInfoDTOLst.get(i).getListGameDetails();
						if(assetGameDetailsList!=null && assetGameDetailsList.size() > 0){
							List<JackpotGameDetailsDTO> jpGameDetDTOLst = new ArrayList<JackpotGameDetailsDTO>();
							
							JackpotGameDetailsDTO jpGameDetDTO = new JackpotGameDetailsDTO();
						
								for(int j=0; j < assetGameDetailsList.size(); j++){
									jpGameDetDTO.setDenomAmount(assetGameDetailsList.get(j).getDenomAmount());
									jpGameDetDTO.setDenomId(assetGameDetailsList.get(j).getDenomId());
									jpGameDetDTO.setGameId(assetGameDetailsList.get(j).getGameId());
									jpGameDetDTO.setPaytabeHoldPercent(assetGameDetailsList.get(j).getPaytabeHoldPercent());
									//jpGameDetDTO.setPaytableId(assetGameDetailsList.get(j).getPaytableId());					
									jpGameDetDTO.setThemeId(assetGameDetailsList.get(j).getThemeId());
									jpGameDetDTO.setThemeName(assetGameDetailsList.get(j).getThemeName());
									jpGameDetDTO.setWagerId(assetGameDetailsList.get(j).getWagerId());
									jpGameDetDTO.setWagerMaxCreditsBet(assetGameDetailsList.get(j).getWagerMaxCreditsBet());
								}								
							jpGameDetDTOLst.add(jpGameDetDTO);
							jpAstInfoDTO.setListGameDetails(jpGameDetDTOLst);
						}					
					if (log.isInfoEnabled()) {
						log.info("jpAstInfoDTO: "+jpAstInfoDTO.toString());
					}
						jpAstInfoDTOLst.add(jpAstInfoDTO);
					}
				} else if (log.isInfoEnabled()) {
					log.info("The assetInfoDTO returned by asset is null in the JackpotCommonEngineUtil class");
				}
			} catch (HibernateException e) {
				log.error("Exception in getJackpotAssetInfo Method",e);
				throw new JackpotEngineServiceException(e);
			}catch (AssetEngineServiceException e) {
				log.error("Exception in getJackpotAssetInfo Method",e);
				throw new JackpotEngineServiceException(e);
		} catch (Exception e) {
				log.error("Exception in getJackpotAssetInfo Method",e);
				throw new JackpotEngineServiceException(e);
		}
		return jpAstInfoDTOLst;
	}
		
	/**
	 * Method to send the jackpot adjustemnt to Marketing for a processed pending jp slip 
	 * @param siteId
	 * @param origCardNo
	 * @param newCardNo
	 * @param oldAmount
	 * @param newJackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int processJackpotAdjustment(int siteId, String origCardNo, 
			String newCardNo, long oldAmount, long newJackpotAmount,
			String slotNo, long msgiD) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside processJackpotAdjustment FACADE method");
		}
		return postJackpotAdjustementToMarketing(siteId, origCardNo, newCardNo,
				oldAmount, newJackpotAmount, slotNo, msgiD);
	}

	/**
	 * Method to send the jackpot adjustemnt to Marketing for a processed manual jp slip 
	 * @param siteId
	 * @param playerCardNumber
	 * @param jackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int postManualJackpot(int siteId, String playerCardNumber,
			long jackpotAmount, String slotNo, long msgiD, String jackpotId)
			throws JackpotEngineServiceException {
		int response =0;
		try {
			if(log.isInfoEnabled()) {
				log.info("BEFORE SENDING processNonSlotJackpot FACADE to marketing engine");
				log.info("Site Id  : "+siteId);
				log.info("Player Card no: "+playerCardNumber);
				log.info("Jp Amt   : "+jackpotAmount);
				log.info("Slot no  : "+slotNo);
				log.info("Msg Id   : "+msgiD);	
			}
						
			response = marketingBO.processNonSlotJackpot(siteId, playerCardNumber, jackpotAmount, slotNo, msgiD, true, Calendar.getInstance(), (char)Integer.parseInt(jackpotId, IAppConstants.HEXA_RADIX));
			if (log.isInfoEnabled()) {
				log.info("AFTER SENDING processNonSlotJackpot + RESPONSE: "
						+ response);
			}
		} catch (MarketingDAOException e) {
			log.error("Exception in postManualJackpot Marketing Method",e);
			throw new JackpotEngineServiceException(e);
		}catch (Exception e) {
			log.error("Exception in postManualJackpot Marketing Method",e);
			throw new JackpotEngineServiceException(e);
		}
		return response;
	}

	/**
	 * Method to send an alert to the Alerts Engine
	 * @param jpAlertObj
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sendAlert(JackpotEngineAlertObject jackpotAlertObj) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside sendAlert FACADE method");
		}
		JackpotAlertObject alertObject = new JackpotAlertObject();
		try {		
			alertObject.setAssetConfigNumber(jackpotAlertObj.getAssetConfigNumber());
			alertObject.setStandNumber(jackpotAlertObj.getStandNumber());
			alertObject.setTerminalMessage(jackpotAlertObj.getTerminalMessage());
			alertObject.setTerminalMessageNumber(jackpotAlertObj.getTerminalMessageNumber());
			alertObject.setExceptionCode(jackpotAlertObj.getExceptionCode());
			alertObject.setEmployeeId(jackpotAlertObj.getEmployeeId());
			alertObject.setEmployeeName(jackpotAlertObj.getEmployeeName());
			alertObject.setJackpotAmount(jackpotAlertObj.getJackpotAmount());
			alertObject.setJackpotId(jackpotAlertObj.getJackpotId());
			alertObject.setSendingEngineName(jackpotAlertObj.getSendingEngineName());
			alertObject.setSiteId(jackpotAlertObj.getSiteId());
			alertObject.setSlotAreaId(jackpotAlertObj.getAreaShortName());	
			
			/** SENDING MSG TO ALERTS ENGINE */
			if(log.isDebugEnabled()) {
				log.debug("***********************************************************************************");
			}
			if(log.isInfoEnabled()) {
				log.info("Before sending the msg to Alerts for "+alertObject.getTerminalMessage()+" : "+alertObject.getTerminalMessageNumber());
			}
			
			alertsBO.sendAlert(alertObject);
		} catch (AlertsEngineBOException e) {
			log.error("Exception in send alert of JackpotFacade "+e);
			throw new JackpotEngineServiceException(e);
		}
		if(log.isInfoEnabled()) {
			log.info("After sending the msg to Alerts for "+alertObject.getTerminalMessage()+" : "+alertObject.getTerminalMessageNumber());
		}
		if(log.isDebugEnabled()) {
			log.debug("***********************************************************************************");
		}
	}
	
	/**
	 * Method to send alerts to Alert Engine
	 * 
	 * @param jackpotDTO
	 * @param alertMessageNumber
	 * @return JackpotAlertObject
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private JackpotAlertObject sendAlert(JackpotDTO jackpotDTO, int alertMessageNumber)
			throws JackpotEngineServiceException {
		if (log.isInfoEnabled()) {
			log.info("Inside sendAlert FACADE method");
		}
		JackpotAlertObject jackpotAlertObject = new JackpotAlertObject();
		try {
			switch (alertMessageNumber) {
			case IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.JP_AMT_ADJUSTED_NEW_AMT_MESG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				break;

			case IAlertMessageConstants.JACKPOT_PRINTED:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.JACKPOT_PRINTED);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.JACKPOT_PRINTED_MSG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				break;

			case IAlertMessageConstants.JP_TO_CREDIT_METER:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.JP_TO_CREDIT_METER);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.JP_TO_CREDIT_METER_MSG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JKPT_CREDIT_MTR_SYS_EXCEPTION);
				jackpotAlertObject.setMessageId(jackpotDTO.getMessageId());
				jackpotAlertObject.setJackpotId(jackpotDTO.getJpId());
				break;
				
			/*case IAlertMessageConstants.JACKPOT_SLOT_STATUS_INVALID:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.JACKPOT_SLOT_STATUS_INVALID);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.JACKPOT_SLOT_STATUS_INVALID_MSG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JKPT_HAND_PAID_EXCEPTION);
				jackpotAlertObject.setMessageId(jackpotDTO.getMessageId());
				jackpotAlertObject.setJackpotId(jackpotDTO.getJpId());
				break;*/
				
			case IAlertMessageConstants.MANUAL_JP_ENTERED_FOR:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.MANUAL_JP_ENTERED_FOR);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.MANUAL_JP_ENTERED_FOR_MSG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				break;
				
			case IAlertMessageConstants.SLIP_VOIDED:
				jackpotAlertObject.setTerminalMessageNumber(IAlertMessageConstants.SLIP_VOIDED);
				jackpotAlertObject.setTerminalMessage(IAlertMessageConstants.SLIP_VOIDED_MSG);
				jackpotAlertObject.setExceptionCode(ICommonConstants.JACKPOT_POSTED_SYS_EXCEPTION);
				break;
			}

			if (jackpotDTO.getAssetConfigNumber() != null) {
				jackpotAlertObject.setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
			}
			if (jackpotDTO.getAssetConfigLocation() != null) {
				jackpotAlertObject.setStandNumber(jackpotDTO.getAssetConfigLocation());
			}
			jackpotAlertObject.setEngineId(IAppConstants.JACKPOT_ALERTS_ENGINE_ID);
			//AMT IS SET AS HPJP RND AMT/ HPJP AMT AS THE SAME AMT IS CONSIDERED AS EXPENSE AT ACCOUNTING
			if (alertMessageNumber != IAlertMessageConstants.SLIP_VOIDED) {
                if (jackpotDTO.getHpjpAmountRounded() != 0) {
                        jackpotAlertObject.setJackpotAmount(jackpotDTO.getHpjpAmountRounded());
                } else if (jackpotDTO.getHpjpAmount() != 0) {
                        jackpotAlertObject.setJackpotAmount(jackpotDTO.getHpjpAmount());
                }
			}
			jackpotAlertObject.setSendingEngineName(IAppConstants.MODULE_NAME);
			jackpotAlertObject.setSiteId(jackpotDTO.getSiteId());
			jackpotAlertObject.setSlotAreaId(jackpotDTO.getAreaShortName());
			if (jackpotDTO.getPrintEmployeeLogin() != null) {
				jackpotAlertObject.setEmployeeId(jackpotDTO.getPrintEmployeeLogin());
			}
			jackpotAlertObject.setEmployeeName(jackpotDTO.getEmployeeFirstName() + ""
					+ jackpotDTO.getEmployeeLastName());

			if (log.isInfoEnabled()) {
				log.info("Before sending the msg to Alerts for " + jackpotAlertObject.getTerminalMessage()
						+ " : " + jackpotAlertObject.getTerminalMessageNumber());
			}
			// Sending Alerts to AlertsBO
			alertsBO.sendAlert(jackpotAlertObject);
			if (log.isInfoEnabled()) {
				log.info("After sending the msg to Alerts for " + jackpotAlertObject.getTerminalMessage()
						+ " : " + jackpotAlertObject.getTerminalMessageNumber());
			}
		} catch (AlertsEngineBOException e) {
			log.error("Exception in send alert of JackpotFacade " + e);
			throw new JackpotEngineServiceException(e);
		}
		return jackpotAlertObject;
	}
	
	/**
	 * Method to get the slip transfer table's slip count 
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long getSlipsByCount() throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getSlipsByCount FACADE method");
		}
		return jackpotBO.getSlipsByCount();
	}
	
	/**
	 * Method to get the slip status and HPJP amount for the given sequence number and Site ID
	 * @param startSeq
	 * @param endSeq
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JPCashlessProcessInfoDTO getJackpotStatusAmount(Long sequenceNumber, Long siteId) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotStatusAmount FACADE method");
		}
		try {
			if(sequenceNumber != null && siteId != null) {
				return jackpotBO.getJackpotStatusAmount(sequenceNumber, siteId.intValue());
			} else if(log.isDebugEnabled()) {				
				log.debug("Either Sequence number or site Id is null");				
			}
		} catch(Exception e) {
			log.error("Exception in getJackpotStatusAmount FACADE: " + e);
			throw new JackpotEngineServiceException("Exception in getJackpotStatusAmount FACADE: " + e);
		}
		return null;
	}
	
	/**
	 * Method to process and pay the jackpot for the given sequence number and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JPCashlessProcessInfoDTO payJackpot(Long sequenceNumber, Long siteId, String employeeId,
			String employeeFirstName, String employeeLastName, String cashDeskLocation,
			boolean validateEmpSession) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside payJackpot FACADE method");
		}
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = null;
		JackpotDTO jackpotDTO = null;
		boolean isEmployeeSessionValid = false;
		
		//LOCK THE JACKPOT SLOT TABLE FOR THE CAGE PROCESS EMPLOYEE		
		if (employeeId != null && siteId != null) {
			boolean jpEmpLockResp = jackpotBO.lockJackpotSlotTable("E" + employeeId.trim(), siteId.intValue());
			if (log.isDebugEnabled()) {
				log.debug("jpEmpLockResp :" + jpEmpLockResp);
			}
		}
		
		try {						
			if(sequenceNumber != null && siteId != null) {
				jackpotDTO = jackpotBO.retrieveSlipBySequenceNumber(sequenceNumber, siteId.intValue());
				if(jackpotDTO == null) {
					return getSlipAvailableDetails(jackpotDTO);
				}
				String postToeCashSiteParam = JackpotUtil.getSiteParamValue(
						ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT, siteId.intValue());
				
				if(log.isDebugEnabled()) {
					log.debug("Is Employee Session has to be Validated : <==validateEmpSession==>" + validateEmpSession);
				}
				if (validateEmpSession) {
					// Check if employee session is available === ONLY FOR CASHLESS TRANSACTIONS
					if(postToeCashSiteParam.equalsIgnoreCase("Yes") 
						&& jackpotDTO.getAccountNumber() != null
						&& !jackpotDTO.getAccountNumber().trim().isEmpty()
							&& Long.parseLong(jackpotDTO.getAccountNumber()) > 0) {
						if(log.isDebugEnabled()) {
							log.debug("Employee Session Validation needed for CASHLESS TRANSACTION");
							// if available then proceed with process of jackpot
							log.debug("Validating Employee Session ===> Calling employeeBO.getCurrentSession");
						}
						SessionInfoDTO sessionInfoDTO = employeeBO.getCurrentSession(employeeId,
								siteId.intValue());
						isEmployeeSessionValid = sessionInfoDTO != null
								&& sessionInfoDTO.getSessStateId() == (short) SessionStateEnum.SIGNON
										.getSessStateId();
						if (log.isDebugEnabled()) {
							log.debug("After validating ==> Employee Session is Valid => "
									+ isEmployeeSessionValid);
						}
					} else {
						// NO EMPLOYEE SESSION VALIDATION FOR NON CASHLESS TRANSACTIONS
						if(log.isDebugEnabled()) {
							log.debug("Employee Session Validation Not needed for NON CASHLESS TRANSACTION");
						}
						isEmployeeSessionValid = true;
					}
				} else if (!validateEmpSession) {
					// if validateEmpSession = false, then JP processing from Cash Desk Process Slips Utility
					// No need to check for employee session availability
					// hence jackpot processing is allowed
					isEmployeeSessionValid = true;
					/**
					 * CHECK IF THE EMPLOYEE HAS THE JOB FUNC => Utility - Cash Desk Process Slips
					 */
					AnAUserForm userForm = getUserDetails(employeeId,
							siteId.intValue());
					if (userForm != null) {
						jPCashlessProcessInfoDTO = validateEmployeeJobFunction(
								jackpotDTO, userForm);
						if (jPCashlessProcessInfoDTO != null
								&& jPCashlessProcessInfoDTO.getErrCode() == JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND_CODE) {
							return jPCashlessProcessInfoDTO;
						}
					}
				}
				if (!isEmployeeSessionValid) {
					if(log.isInfoEnabled()) {
						log.info("Employee Session is not available");
					}
					return getEmployeeSessionSetails(jackpotDTO);
				}
				if(jackpotDTO != null) {
					jackpotDTO.setPrintEmployeeLogin(employeeId);
					jackpotDTO.setEmployeeFirstName(employeeFirstName);
					jackpotDTO.setEmployeeLastName(employeeLastName);
					jackpotDTO.setCashDeskLocation(cashDeskLocation);
					jackpotDTO.setValidateEmpSession(validateEmpSession);
					// GETTING THE JACKPOT STATUS FOR VERIFICATION
					short jackpotStatus = jackpotDTO.getStatusFlagId();				
					
					switch (jackpotStatus) {							
						case ILookupTableConstants.PRINTED_STATUS_ID:
						case ILookupTableConstants.CHANGE_STATUS_ID:
						case ILookupTableConstants.REPRINT_STATUS_ID:
							if(ILookupTableConstants.POSTED_TO_ACCOUNTING.equals(jackpotDTO.getPostToAccounting())) {
								jPCashlessProcessInfoDTO = getCashierDeskJackpotProcessingDetails(jackpotDTO);
							} else  {
								/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
								AnAUserForm userForm = getUserDetails(jackpotDTO.getPrintEmployeeLogin(), jackpotDTO.getSiteId());
								if(userForm != null) {
									jackpotDTO.setEmployeeFirstName(userForm.getFirstName());
									jackpotDTO.setEmployeeLastName(userForm.getLastName());
								}
								// CHECKING FOR HPJP AMT LIMIT FOR EMPLOYEE
								jPCashlessProcessInfoDTO = getEmployeeJackpotLimit(jackpotDTO, userForm);
								if(jPCashlessProcessInfoDTO != null && 
										jPCashlessProcessInfoDTO.getErrCode() != JP_SLIP_EMP_JP_LIMIT_EXCEEDED_CODE) {
									jPCashlessProcessInfoDTO = getJackpotProcessSuccess(jackpotDTO);
								}
							}
							break;
							
						case ILookupTableConstants.VOID_STATUS_ID:
							jPCashlessProcessInfoDTO = getJackpotVoidedDetails(jackpotDTO);
							break;
							
						case ILookupTableConstants.PROCESSED_STATUS_ID:
							jPCashlessProcessInfoDTO = getJackpotProcessedDetails(jackpotDTO);
							break;
							
						case ILookupTableConstants.PENDING_STATUS_ID:
							jPCashlessProcessInfoDTO = getPendingJackpotDetails(jackpotDTO);
							break;
							
						case ILookupTableConstants.MECHANICS_DELTA_STATUS_ID:
							jPCashlessProcessInfoDTO = getJackpotMechanicsDeltaDetails(jackpotDTO);
							break;
							
						case ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID:
							jPCashlessProcessInfoDTO = getJackpotCreditKeyOffDetails(jackpotDTO);
							break;
							
						case ILookupTableConstants.INVALID_STATUS_ID:
							jPCashlessProcessInfoDTO = getInvalidjackpotDetails(jackpotDTO);
							break;
							
						default:
							jPCashlessProcessInfoDTO = getSlipAvailableDetails(jackpotDTO);
							break;
					}
				} 
			} else {
				jPCashlessProcessInfoDTO = getSlipAvailableDetails(jackpotDTO);
			}
		} catch (Exception e) {
			log.error("Exception in payJackpot FACADE", e);
            throw new JackpotEngineServiceException("Exception while processing the payJackpot : ", e);
		}
		if(log.isInfoEnabled()) {
			log.info(jPCashlessProcessInfoDTO.toString());
		}
		return jPCashlessProcessInfoDTO;
	}

	/**
	 * Method to void a jackpot for the given sequence number and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JPCashlessProcessInfoDTO voidPaidJackpot(Long sequenceNumber, Long siteId, String employeeId, String employeeFirstName, String employeeLastName, String cashDeskLocation) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidPaidJackpot FACADE method");
		}
		JackpotDTO jackpotDTO = null;
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = null;
			try {
				if(sequenceNumber != null && siteId != null) {
					jackpotDTO = jackpotBO.retrieveSlipBySequenceNumber(sequenceNumber, siteId.intValue());
					if(jackpotDTO != null) {
						jackpotDTO.setVoidEmployeeLogin(employeeId);
						jackpotDTO.setEmployeeFirstName(employeeFirstName);
						jackpotDTO.setEmployeeLastName(employeeLastName);
						jackpotDTO.setCashDeskLocation(cashDeskLocation);
						// GETTING THE JACKPOT STATUS FOR VERIFICATION
						short jackpotStatus = jackpotDTO.getStatusFlagId();
						switch(jackpotStatus) {
							case ILookupTableConstants.PRINTED_STATUS_ID:
							case ILookupTableConstants.REPRINT_STATUS_ID:
							case ILookupTableConstants.CHANGE_STATUS_ID:
							case ILookupTableConstants.PROCESSED_STATUS_ID:
							case ILookupTableConstants.PENDING_STATUS_ID:
								// Gaming day check TODO
								String currentGamingDate = null;
								String slipTransactionDate = null;
								List<String> strDateLst = getGamingDayInfoForSlipSeq(siteId.intValue(), sequenceNumber);
								if(log.isDebugEnabled()) {
									log.debug("strDateLst: " + strDateLst);
								}
								if(strDateLst != null && strDateLst.size() >= 1){									
									// Current Gaming Date
									currentGamingDate = strDateLst.get(0); 
									if(strDateLst.size() == 2){
										// Slip Transaction Date
										slipTransactionDate = strDateLst.get(1); 
									}
								}
								if(slipTransactionDate == null || currentGamingDate.equalsIgnoreCase(slipTransactionDate)) {
									jPCashlessProcessInfoDTO = getJackpotVoidSuccess(jackpotDTO);
									// POST AMOUNT ADJUSTMENT TO MARKETING AFTER VOIDING JACKPOT
									postJackpotAdjustementToMarketing(jackpotDTO.getSiteId(), 
											jackpotDTO.getAssociatedPlayerCard(), 
											jackpotDTO.getPlayerCard(), 
											jackpotDTO.getJackpotNetAmount(), 
											0, 
											jackpotDTO.getAssetConfigNumber(), 
											jackpotDTO.getMessageId());
								}
								if(slipTransactionDate != null) {
									jPCashlessProcessInfoDTO = getGamingDayProcessDetails(jackpotDTO);
								}								
								break;
								
							case ILookupTableConstants.VOID_STATUS_ID:
								jPCashlessProcessInfoDTO = getJackpotVoidedDetails(jackpotDTO);
								break;
								
							case ILookupTableConstants.MECHANICS_DELTA_STATUS_ID:
								jPCashlessProcessInfoDTO = getJackpotMechanicsDeltaDetails(jackpotDTO);
								break;
								
							case ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID:
								jPCashlessProcessInfoDTO = getJackpotCreditKeyOffDetails(jackpotDTO);
								break;
								
							case ILookupTableConstants.INVALID_STATUS_ID:
								jPCashlessProcessInfoDTO = getInvalidjackpotDetails(jackpotDTO);
								break;
								
							default:
								jPCashlessProcessInfoDTO = getSlipAvailableDetails(jackpotDTO);
								break;
						}	
					} else {
						jPCashlessProcessInfoDTO = getSlipAvailableDetails(jackpotDTO);
					}				
				} else {
					jPCashlessProcessInfoDTO = getSlipAvailableDetails(jackpotDTO);
				}
			} catch(Exception e) {
				log.error("Exception in voidPaidJackpot FACADE", e);
	            throw new JackpotEngineServiceException("Exception while processing the voidPaidJackpot : ", e);
			}
		if(log.isInfoEnabled()) {
			log.info(jPCashlessProcessInfoDTO.toString());
		}
		return jPCashlessProcessInfoDTO;
	}

	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean isAllJackpotProcessed(String accountNumber, int siteId) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside isAllJackpotProcessed FACADE method");
		}
		try {
			if(accountNumber != null ) {
				return jackpotBO.isAllJackpotProcessed(accountNumber, siteId);
			} else if(log.isInfoEnabled()) {				
				log.info("Either Cashless Account number is null");				
			}
		} catch(Exception e) {
			log.error("Exception in isAllJackpotProcessed FACADE: " + e);
			throw new JackpotEngineServiceException("Exception in isAllJackpotProcessed FACADE: " + e);
		}
		return false;
	}
	/**
	 * Method to return the list of all jackpots that are not processed
	 *  
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public List<JPCashlessProcessInfoDTO> getAllUnProcessedJackpot(String accountNumber, int siteId) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getAllUnProcessedJackpot FACADE method");
		}
		if(accountNumber != null ) {
			return jackpotBO.getAllUnProcessedJackpot(accountNumber, siteId);
		} else if(log.isInfoEnabled()) {
			log.info("CardlessAccountNumber value is NULL");
		}
		return null;
	}
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param employeeId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> voidPendingJackpotSlipsForAuditProcess(int siteId, long startTime, long endTime, String employeeId, String kioskProcessed) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidPendingJackpotSlipsForAuditProcess FACADE method");
		}
		return jackpotBO.voidPendingJackpotSlipsForAuditProcess(siteId, startTime, endTime, employeeId, kioskProcessed);
	}
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param gamingDate
	 * @param employeeId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> voidAllPendingJackpotSlipsForAuditProcess(int siteId, String employeeId, String kioskProcessed) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidAllPendingJackpotSlipsForAuditProcess(siteId) FACADE method");
		}
		return jackpotBO.voidAllPendingJackpotSlipsForAuditProcess(siteId, employeeId, kioskProcessed);
	}
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id, gaming day and slot no
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param slotNo
	 * @param employeeId
	 * @return
	 * @throws JackpotDAOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> voidPendingJackpotSlipsWithSlotForAuditProcess(int siteId, long startTime, long endTime, String slotNo, String employeeId, String kioskProcessed) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidPendingJackpotSlipsWithSlotForAuditProcess(int siteId, long gamingDate, String slotNo) FACADE method");
		}
		return jackpotBO.voidPendingJackpotSlipsWithSlotForAuditProcess(siteId, startTime, endTime, slotNo, employeeId, kioskProcessed);
	}
	
	/**
	 * Method to set the ASSET SERACH FILTER
	 * @return
	 */
	public AssetSearchFilter setAssetSearchFilter(){
		AssetSearchFilter assetSearchFilter = new AssetSearchFilter();
		assetSearchFilter.setGameInfoRequired(true);
		assetSearchFilter.setHopperEnabledRequired(true);
		assetSearchFilter.setMultiGameRequired(true);
		assetSearchFilter.setMultiDenomRequired(true);	
		assetSearchFilter.setAccountingDenomRequired(true);
		assetSearchFilter.setTypeDescRequired(true);
		assetSearchFilter.setDenominationRequired(true);
		return assetSearchFilter;
	}
	
	/**
	 * Method to validate the progressive level for the slot
	 * @param siteId
	 * @param lstLevelNo
	 * @param slotNumber
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean validateProgressiveLevel(int siteId, List<Integer> lstLevelNo, String slotNumber) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside validateProgressiveLevel FACADE method");
		}
		try {
			if(log.isDebugEnabled()){
				log.debug("SiteId : " + siteId);
				log.debug("SlotNumber : " + slotNumber);
				log.debug("LevelNo : " + lstLevelNo);
			}
			return progressiveBO.validateLevelNo(siteId, lstLevelNo, slotNumber);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JackpotEngineServiceException(e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<NewWaveResponseDTO> getNewWaveJackpotDetails(NewWaveRequestDTO newWaveRequestDTO) throws JackpotEngineServiceException {		
		if(log.isInfoEnabled()) {
			log.info("Inside getNewWaveJackpotDetails FACADE method");
		}
		try{
			return jackpotBO.getNewWaveJackpotDetails(newWaveRequestDTO);
		}catch (Exception e) {
			throw new JackpotEngineServiceException(e);
		}
		
	}

	/**
	 * Method to get the Pending Jackpots that were not reset
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotResetDTO> getUnResetPendingJackpots(int siteId)throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getUnResetPendingJackpots FACADE method");
		}
		try{
			return jackpotBO.getUnResetPendingJackpots(siteId);
		}catch (Exception e) {
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to return the gaming day info for a processed slip seq no and the current gaming day 
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<String> getGamingDayInfoForSlipSeq(int siteNo, long sequenceNo) throws JackpotEngineServiceException {
		List<String> rtnStringLst = null;
		Date[] dateArray = null;
		long slipRefId = 0l;
		try{
			if(log.isDebugEnabled()) {
				log.debug("Inside getGamingDayInfoForSlipSeq method");
				log.debug("siteNo: "+siteNo);
				log.debug("sequenceNo: "+sequenceNo);
			}
			slipRefId = jackpotBO.getSlipReferenceIdForSlipSeq(siteNo, sequenceNo);
			
		}catch (Exception e) {
			log.error("Exception in getSlipReferenceIdForSlipSeq",e);
			throw new JackpotEngineServiceException(e);
		}
		try{
			log.debug("slipRefId: "+slipRefId);
			log.info("Calling IDaemonFacade's getCurrentSlipGDateAndTransDate method");
			IDaemonFacade accFacade = (IDaemonFacade) ServiceLocator.fetchService(LOOKUP_SDS_DAEMON_FACADE, IS_LOCAL_LOOKUP);
			dateArray = accFacade.getCurrentSlipGDateAndTransDate((short)siteNo, String.valueOf(slipRefId));
			log.debug("Returned date array: "+dateArray);
	
			if(dateArray!=null){
				if(log.isDebugEnabled()) {
					log.debug("Returned date array len: "+dateArray.length);
				}
				rtnStringLst = new ArrayList<String>();
				if(dateArray[0]!=null){
					log.debug("dateArray[0]: "+dateArray[0]);
					rtnStringLst.add(0, dateArray[0].toString());
				}
				if(dateArray[1]!=null){
					log.debug("dateArray[1]: "+dateArray[1]);
					rtnStringLst.add(1, dateArray[1].toString());
				}
			}			
		}catch (Exception e) {
			log.error("Exception in getSlipReferenceIdForSlipSeq whe calling IAccountingFacade.getCurrentSlipGDateAndTransDate",e);
			throw new JackpotEngineServiceException(e);
		}	
		return rtnStringLst;
	}
	
	/**
	 * Method to post to eCash when jackpot is processed or void
	 * 
	 * @param jackpotDTO
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private void postToECash(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {		
		if(log.isInfoEnabled()) {
			log.info("Inside postToECash FACADE method");
		}
		if(jackpotDTO.getAccountNumber() != null
				&& !jackpotDTO.getAccountNumber().trim().isEmpty()
				&& Long.parseLong(jackpotDTO.getAccountNumber()) > 0) {
			try {
				SDSAccessDTO sdsAccessDTO = null;
				if(log.isInfoEnabled()) {
					log.info("jackpotDTO.getAccountNumber()          : " + jackpotDTO.getAccountNumber());
					log.info("jackpotDTO.getSequenceNumber()  		 : " + jackpotDTO.getSequenceNumber() );
					log.info("jackpotDTO.getSiteId() 				 : " + jackpotDTO.getSiteId());
					log.info("jackpotDTO.getJackpotNetAmount() 	 	 : " + jackpotDTO.getJackpotNetAmount());
					log.info("jackpotDTO.getVoidEmployeeLogin() 	 : " + jackpotDTO.getPrintEmployeeLogin());
					log.info("jackpotDTO.getAssetConfigNumber() 	 : " + jackpotDTO.getAssetConfigNumber());
					log.info("jackpotDTO.getStatusFlagId() 	 		 : " + jackpotDTO.getStatusFlagId());
				}
				if(jackpotDTO.getStatusFlagId() == ILookupTableConstants.PROCESSED_STATUS_ID
						|| jackpotDTO.getStatusFlagId() == ILookupTableConstants.CHANGE_STATUS_ID) {
					// PROCESS JACKPOT DETAILS SEND TO ECASH ACCOUNT					
					if(log.isInfoEnabled()) {
						log.info("PROCESS JACKPOT DETAILS SEND TO ECASH ACCOUNT");
					}					
					sdsAccessDTO = eCashFacade.processSlip(jackpotDTO.getAccountNumber(), 
						jackpotDTO.getSequenceNumber(), 
						ILookupTableConstants.JACKPOT_SLIP_TYPE_ID,
						jackpotDTO.getSiteId(),
						jackpotDTO.getJackpotNetAmount(), 
						jackpotDTO.getPrintEmployeeLogin(), 
						jackpotDTO.getAssetConfigNumber(),
						jackpotDTO.isValidateEmpSession());
					if(log.isInfoEnabled()) {
						log.info("Posting Processed to eCash is Successful : " + sdsAccessDTO.isSuccess());
					}					
				} else if(jackpotDTO.getStatusFlagId() == ILookupTableConstants.VOID_STATUS_ID) {
					// VOID JACKPOT DETAILS SEND TO ECASH ACCOUNT
					if(log.isInfoEnabled()) {
						log.info("VOID JACKPOT DETAILS SEND TO ECASH ACCOUNT");	
					}
					sdsAccessDTO = eCashFacade.voidSlip(jackpotDTO.getAccountNumber(),
							jackpotDTO.getSequenceNumber(),
							ILookupTableConstants.JACKPOT_SLIP_TYPE_ID,
							jackpotDTO.getSiteId(),
							jackpotDTO.getJackpotNetAmount(),
							jackpotDTO.getVoidEmployeeLogin(),
							jackpotDTO.getAssetConfigNumber(),
							jackpotDTO.isValidateEmpSession());
					if(log.isInfoEnabled()) {
						log.info("Posting Void to eCash is Successful : " + sdsAccessDTO.isSuccess());
					}
				}
				if(sdsAccessDTO != null) {
					if(log.isDebugEnabled()) {
						log.debug("SDS Account Number : " + sdsAccessDTO.getAxcsAcntNo());
					}
					if(log.isInfoEnabled()) {
						log.info("After Posting to eCash error code mesg : " + sdsAccessDTO.getErrorCodeDesc());
					}
				} else if(log.isDebugEnabled()) {
					log.debug("Posting to eCash Failed");
				}
			} catch(Exception e) {
				log.error("Exception in postToECash method: ", e);
				throw new JackpotEngineServiceException(e);
			}
		} else if(log.isInfoEnabled()) {
			log.info("Account number " + jackpotDTO.getAccountNumber() + " is not available. Hence not posted to eCash");			
		}
	}
	
	/**
	 * This API calls the Ecash engine to get active player session at the slot
	 * @param accessID
	 * @param assetNumber
	 * @param siteID
	 * @return void
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */   
	public PlayerSessionDTO getActivePlayerSession(String accessID, String assetNumber, int siteID) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getActivePlayerSession FACADE method");
		}
		try {
			PlayerSessionDTO playerSessionDTO = eCashFacade.getActivePlayerSession(accessID, assetNumber, siteID);
			if(playerSessionDTO != null) {
				return playerSessionDTO;
			} else {
				log.error("No details avaliable when getting active player session");
			}
		} catch(Exception e) {
			throw new JackpotEngineServiceException(e);
		}
		return null;
	}
	
	/**
	 * Method to validate the Cashless Account Number
	 * 
	 * @param siteNo
	 * @param accountNumber
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO validateCashlessAccountNo(int siteNo, String accountNumber) throws JackpotEngineServiceException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside validateCashlessAccountNo Facade method");							
				log.info("siteNo				: " + siteNo);
				log.info("AccountNumber			: " + accountNumber);
			}
			CashlessAccountDTO cashlessAccountDTO = null;
			SDSAccessDTO sdsAccessDTO = eCashFacade.accountLookup(accountNumber, siteNo, (short)0);
			if (sdsAccessDTO != null) {
				if (sdsAccessDTO.getAccountDTOList() != null && sdsAccessDTO.getAccountDTOList().size() > 0) {
					cashlessAccountDTO = JackpotHelper.getAccountDetails(sdsAccessDTO);				
					if (log.isDebugEnabled()) {
						log.debug("CashlessAccountDTO --- > " + cashlessAccountDTO.toString());
					}	
				}				
				return cashlessAccountDTO;
			} else if(log.isInfoEnabled()) {
				log.debug("No Account Details returned");
			}
			return cashlessAccountDTO;
		} catch (Exception e) {
			log.error("Exception in validateCashlessAccountNo method: ", e);
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to get the CMS player Id for the cashless account number
	 * @param siteNo
	 * @param accountNumber
	 * @return
	 * @author vsubha
	 */
	public String getPlayerCardForCashlessAccNo(int siteNo, String accountNumber) throws JackpotEngineServiceException {
		try {
			if(log.isInfoEnabled()) {
				log.info("Inside getPlayerCardForCashlessAccNo FACADE method");
			}
			SDSAccessDTO sdsAccessDTO = eCashFacade.accountLookup(accountNumber, siteNo, (short)0);
			if (sdsAccessDTO != null) {
				if (sdsAccessDTO.getAccountDTOList() != null && sdsAccessDTO.getAccountDTOList().size() > 0) {
					String cmsPlayerId = sdsAccessDTO.getAccountDTOList().get(0).getAcntCmsPlayerId();			
					if (log.isDebugEnabled()) {
						log.debug("cmsPlayerId for cashless account number " + accountNumber + " --- > " + cmsPlayerId);
					}	
					return cmsPlayerId;
				}	
			}			
		} catch (Exception e) {
			log.error("Exception in getPlayerCardForCashlessAccNo method: ", e);
			throw new JackpotEngineServiceException(e);
		}
		return null;
	}
	
	/**
	 * Method to check if Jackpot Process or Void can be allowed for the Player Cashless Account.
	 * 
	 * @param accountNumber
	 * @param siteID
	 * @param operationType
	 * @return CashlessAccountDTO
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO isJackpotOperationAllowed(String accountNumber, int siteID, short operationType) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside isJackpotOperationAllowed FACADE method");
		}
		try {
			SDSAccessDTO sdsAccessDTO = eCashFacade.allowSlipOperation(accountNumber, siteID, operationType);
			CashlessAccountDTO cashlessAccountDTO = JackpotHelper.getAccountDetails(sdsAccessDTO);
			cashlessAccountDTO.setAcntNo(accountNumber);
			if(log.isInfoEnabled()) {
				log.info(cashlessAccountDTO.toString());
			}
			return cashlessAccountDTO;
		} catch(Exception e) {
			log.error("Exception in isJackpotOperationAllowed FACADE method:",e);
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to check if the player card number entered is valid or not.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public long getPlayerCardId(String playerCardNumber, int siteID) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getPlayerCardId FACADE method");
		}
		try {
			return marketingBO.getCardId(playerCardNumber, siteID);
		} catch(Exception e) {
			log.error("Exception in getPlayerCardId FACADE method: " , e);
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to get the player name for the player card number.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getPlayerCardName(String playerCardNumber, int siteID) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getPlayerCardId FACADE method");
		}
		try {
			List<PlayerDetailsDTO> playerInfo = marketingBO.getPlayersPersonalInfo(playerCardNumber, siteID);
			if (log.isDebugEnabled()) {
				log.debug("playerCardNumber : " + playerCardNumber);
			}
			if (playerInfo != null && playerInfo.size() > 0) {
				String firstName = (playerInfo.get(0).getFirstName() != null && !playerInfo.get(0)
						.getFirstName().trim().isEmpty()) ? playerInfo.get(0).getFirstName().trim()
						: IAppConstants.EMPTY_STRING;
				if (log.isDebugEnabled()) {
					log.debug("First Name : " + firstName);
				}
				String lastName = (playerInfo.get(0).getLastName() != null && !playerInfo.get(0)
						.getLastName().trim().isEmpty()) ? playerInfo.get(0).getLastName().trim()
						: IAppConstants.EMPTY_STRING;
				if (log.isDebugEnabled()) {
					log.debug("Last Name : " + lastName);
				}
				return firstName + " " + lastName;
			} else if (log.isInfoEnabled()) {
				log.info("Player Name not available for the player card number -> " + playerCardNumber);
			}
		} catch(Exception e) {
			log.error("Exception in getPlayerCardId FACADE method: " , e);
			throw new JackpotEngineServiceException(e);
		}
		return null;
	}
	
	/**
	 * Method to post to marketing when a jackpot adjustment happens
	 * 
	 * @param siteId
	 * @param origCardNo
	 * @param newCardNo
	 * @param oldAmount
	 * @param newJackpotAmount
	 * @param slotNo
	 * @param msgId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private int postJackpotAdjustementToMarketing(int siteId, String origCardNo, String newCardNo, long oldAmount, long newJackpotAmount, String slotNo, long msgId) throws JackpotEngineServiceException{
		//BY DEFAULT PT 11 SHOULD BE SENT FOR A VOID TRANSACTION
		int response = 0;
		try {
			if(log.isInfoEnabled()) {
				log.info("BEFORE SENDING processJackpotAdjust FACADE to marketing engine");
				log.info("Site Id    : " + siteId);
				log.info("Org Card no: " + origCardNo);
				log.info("New Card no: " + newCardNo);
				log.info("Old Amt    : " + oldAmount);
				log.info("New Amt    : " + newJackpotAmount);
				log.info("Slot no    : " + slotNo);
				log.info("Msg Id     : " + msgId);	
			}	
			response = marketingBO.processJackpotAdjust(siteId, origCardNo, newCardNo, oldAmount, newJackpotAmount, slotNo, msgId);
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
	
	/**
	 * Method to get the success details of processing the jackpot
	 * 
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotProcessSuccess(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {
		// JACKPOT PROCESSING		
		JackpotDTO jpDTO = processPrintJackpot(jackpotDTO);
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jpDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_PROCESSED_SUCCESSFULLY_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_PROCESSED_SUCCESSFULLY);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_PROCESSED_SUCCESSFULLY);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get User Details for the Employee Id
	 * 
	 * @param employeeId
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private AnAUserForm getUserDetails(String employeeId, int siteId) throws JackpotEngineServiceException {
		try {
			/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
			return anaBO.getUserDetails(employeeId, siteId);
		} catch (AnAException e) {
			log.error("Exception in getUserDetails", e);
			throw new JackpotEngineServiceException(e);
		}		
	}
	
	/**
	 * Method to get the verify the jackpot amount limit for the logged in employee
	 * 
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getEmployeeJackpotLimit(JackpotDTO jackpotDTO, AnAUserForm userForm) throws JackpotEngineServiceException {
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();
		String hpjpValidateChk = JackpotUtil.getSiteParamValue(
				ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT, jackpotDTO.getSiteId());
		
		double empJpAmt = 0;
		
		/** GET THE EMPLOYEE ROLE PARAMETER JP AMOUNT */
		if (userForm != null) {
			ParameterForm[] paramform = userForm.getParameterForms();
			if (paramform != null && paramform.length > 0) {
				for (ParameterForm formObj : paramform) {
					if (formObj.getParameterName() != null
							&& formObj.getParameterName().equalsIgnoreCase(JACKPOT_AMOUNT_USER_PARAMETER)) {
						empJpAmt = Double.parseDouble(formObj.getParameterValue().trim());
						break;
					}
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Employee JP Amount: " + empJpAmt + " HPJP Net Amount : "
					+ ConversionUtil.centsToDollar(jackpotDTO.getJackpotNetAmount()));
		}
		
		/**IF THE JP NET AMOUNT IS GREATER THAN THE EMPLOYEE'S JP LIMIT 
		THEN RESTRICT THE PROCESSING OF THE SLIP */
		if(hpjpValidateChk != null && hpjpValidateChk.equalsIgnoreCase("YES")
				&& ConversionUtil.centsToDollar(jackpotDTO.getJackpotNetAmount()) > empJpAmt) {
			jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_EMP_JP_LIMIT_EXCEEDED_CODE);
			jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_AMT_EXCEEDS_JP_LIMIT);
			if(log.isDebugEnabled()) {
				log.debug(JP_SLIP_AMT_EXCEEDS_JP_LIMIT);
			}
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to validate Employee Job Function => Utility - Cash Desk Process Slips
	 * 
	 * @param jackpotDTO
	 * @param userForm
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO validateEmployeeJobFunction(JackpotDTO jackpotDTO, AnAUserForm userForm) {
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();
		boolean isEmpJobValid = false;
		/*
		 * FUNCTION CHECK DONE ONLY IF PROCESSED FROM SDS CASHIER DESK
		 * UTILITY AND NOT REQUIRED IF PROCESSED FROM CAGE
		 */
		if (!jackpotDTO.isValidateEmpSession()) {
			if(log.isDebugEnabled()) {
				log.debug("CHECKING IF THE EMPLOYEE HAS THE JOB FUNC => Utility - Cash Desk Process Slips");
			}
			if (userForm != null) {
				FunctionForm[] funcForm = userForm.getFunctionForms();
				if (funcForm != null && funcForm.length > 0) {
					for (FunctionForm funcFormObj : funcForm) {
						if (funcFormObj.getFunctionName() != null
								&& funcFormObj.getFunctionName().equalsIgnoreCase(
										EMP_FUNC_CASH_DESK_PROC_SLIPS)) {
							isEmpJobValid = true;
							break;
						}
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("Employee JP Func Chk: " + isEmpJobValid);
			}
			/**
			 * IF THE EMP DOES NOT CONTAIN THE JOB FUNCTION THEN RESTRICT THE
			 * PROCESSING OF THE SLIP
			 */
			if (!isEmpJobValid) {
				jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND_CODE);
				jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND);
				if (log.isDebugEnabled()) {
					log.debug(JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND);
				}
			}
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the success details of voiding the jackpot
	 * 
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotVoidSuccess(JackpotDTO jackpotDTO) throws JackpotEngineServiceException {
		JackpotDTO jpDTO = postJackpotVoidStatus(jackpotDTO);
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jpDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_VOIDED_SUCCESSFULLY_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_VOIDED_SUCCESSFULLY);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_VOIDED_SUCCESSFULLY);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the processed jackpot details
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotProcessedDetails(JackpotDTO jackpotDTO) {
		// JACKPOT ALREADY PROCESSED
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_ALREADY_PROCESSED_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_ALREADY_PROCESSED);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_ALREADY_PROCESSED);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the jackpot processed when cashier desk is enabled
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getCashierDeskJackpotProcessingDetails(JackpotDTO jackpotDTO) {
		// JACKPOT ALREADY PROCESSED WHEN CASHIER DESK DISABLED
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_ALREADY_PROCESSED_CASH_DESK_DISABLED_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_ALREADY_PROCESSED_CASH_DESK_DISABLED);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_ALREADY_PROCESSED_CASH_DESK_DISABLED);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the voided jackpot details
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotVoidedDetails(JackpotDTO jackpotDTO) {
		// VOIDED JACKPOT SLIP
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_ALREADY_VOIDED_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_ALREADY_VOIDED);
		if(log.isDebugEnabled()) {
			log.debug(IAppConstants.JP_SLIP_ALREADY_VOIDED);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the gaming day mismatch details for the slip to be voided
	 * 
	 * @param jackpotDTO
	 * @param slipTransactionDate
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getGamingDayProcessDetails(JackpotDTO jackpotDTO) {
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_GAMING_DAY_MISMATCH_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_GAMING_DAY_MISMATCH);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_GAMING_DAY_MISMATCH);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the invalid jackpot details
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getInvalidjackpotDetails(JackpotDTO jackpotDTO) {
		// JACKPOT - INVALID
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_INVALID_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_INVALID);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_INVALID);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get the pending jackpot details
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getPendingJackpotDetails(JackpotDTO jackpotDTO) {
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_PENDING_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_PENDING);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_PENDING);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to check for credit key off status
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotCreditKeyOffDetails(JackpotDTO jackpotDTO) {
		// JACKPOT CREDIT KEY OFF
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_CREDIT_KEY_OFF_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_CREDIT_KEY_OFF);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_CREDIT_KEY_OFF);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to check for mechanics delta status
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getJackpotMechanicsDeltaDetails(JackpotDTO jackpotDTO) {
		// JACKPOT - MECHANICS DELTA
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_MECHANICS_DELTA_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_MECHANICS_DELTA);
		if(log.isDebugEnabled()) {
			log.debug(JP_SLIP_MECHANICS_DELTA);
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to check if the slip is availabel or not
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getSlipAvailableDetails(JackpotDTO jackpotDTO) {
		// JP SLIP NOT FOUND
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_NOT_FOUND_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_NOT_FOUND);
		if(log.isDebugEnabled()) {
			log.debug(IAppConstants.JP_SLIP_NOT_FOUND);	
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to check if the employee session is availabel or not
	 * 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	private JPCashlessProcessInfoDTO getEmployeeSessionSetails(JackpotDTO jackpotDTO) {
		// EMPLOYEE SESSION NOT VALID
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = JackpotHelper.getSlipDetailsDTO(jackpotDTO);
		jPCashlessProcessInfoDTO.setErrCode(JP_SLIP_EMP_SESSION_NOT_AVAILABLE_CODE);
		jPCashlessProcessInfoDTO.setErrMessage(JP_SLIP_EMP_SESSION_NOT_AVAILABLE);
		if(log.isInfoEnabled()) {
			log.info(JP_SLIP_EMP_SESSION_NOT_AVAILABLE);
		}
		return jPCashlessProcessInfoDTO;
	}	
	
	/**
	 * Mathod to get the site keys into an arraylist
	 * 
	 * @param siteKeys
	 * @return
	 * @author vsubha
	 */
	private List<String> getSiteKeyList(String[] siteKeys) {
		ArrayList<String> siteKeyList = new ArrayList<String>();
		if(siteKeys != null && siteKeys.length > 0) {
			int siteKeyLength = siteKeys.length;
			for(int i = 0; i < siteKeyLength; i++) {
				siteKeyList.add(siteKeys[i]);
			}			
		}
		return siteKeyList;
	}
	
	/**
	 * Method to create the jp barcode
	 * 
	 * @param siteid
	 * @param jpNetAmt
	 * @param sequenceNo
	 * @param encodeFormat
	 * @return barcodeImage
	 * @author vsubha
	 */
	public Barcode createBarcode(int siteid, long jpNetAmt, long sequenceNo, String encodeFormat)
			throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside createBarcode FACADE method");
		}
		try {
			return JackpotUtil.createBarcode(siteid, jpNetAmt, sequenceNo, encodeFormat);
		} catch(Exception e) {
			log.error("Exception in createBarcode FACADE method: " , e);
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to get Jackpot Liability details for DEG
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotLiabilityDetailsDTO getJackpotLiabilityDetailsForDEG(int siteId, String fromDate,
			String toDate) throws JackpotEngineServiceException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotLiabilityDetailsForDEG FACADE method");
		}
		return jackpotBO.getJackpotLiabilityDetailsForDEG(siteId, fromDate, toDate);
	}
	
	
	/**
	 * Method exposed to the Progressive Engine to create a jackpot based on the Controller's notification
	 * @param siteId
	 * @param slotNo
	 * @param jackpotId
	 * @param jpAmount
	 * @param jpHitDate
	 * @return long
	 * @throws JackpotEngineServiceException
	 * @author dambereen
	 */
	public long createJackpotFromController(int siteId, String slotNo, String jackpotId, long jpAmount, Date jpHitDate) throws Exception {
		log.info("Inside createJackpotFromController BO method");	
		return this.createJackpotFromExternalSystem(siteId, slotNo, jackpotId, jpAmount, jpHitDate, ILookupTableConstants.EXCEPTION_JP_HANDPAID_FROM_EXT_SYS, JpGeneratedBy.CONTROLLER);
	}
	
	/**
	 * Method that contains the logic to create a pending slip from an external system,
	 * send alerts,
	 * post to marketing to send PT10 
	 * @param siteId
	 * @param slotNo
	 * @param jackpotId
	 * @param jpAmount
	 * @param jpHitDate
	 * @param transExcepCode
	 * @param jpGeneratedBy
	 * @return long
	 * @throws JackpotEngineServiceException
	 * @author dambereen
	 */
	private long createJackpotFromExternalSystem(int siteId, String slotNo, String jackpotId, long jpAmount, Date jpHitDate,
			short transExcepCode, JpGeneratedBy jpGeneratedBy) throws Exception{
		
		if(log.isInfoEnabled()){
			log.info("createJackpotFromExternalSystem");
		}
		if(log.isDebugEnabled()){
			log.debug("siteId: "+siteId);
			log.debug("slotNo: "+slotNo);
			log.debug("jackpotId: "+jackpotId);
			log.debug("jpAmount: "+jpAmount);
			log.debug("jpHitDate: "+jpHitDate);
		}		
		JackpotDTO jackpotDTO 				= new JackpotDTO();		
		try {
			//populate jackpot dto values
			//call asset to get its details
			//set status to dto , acc type, acc no, jp id, msg id etc. 
			//update db and do jpid validation within DAO method			
			//send alerts
			//post to marketing		
					
			AssetSearchFilter assetSearchFilter = setAssetSearchFilter();
			AssetInfoDTO assetInfoDTO 			= null;
			int iJackpotId 						= Integer.parseInt(jackpotId, IAppConstants.HEXA_RADIX);
			List<IHeader> jpAlertObjLst 		= new ArrayList<IHeader>();
			char exceptionCode 					= 10;				
			Calendar calJpHitDate 				= Calendar.getInstance();
			calJpHitDate.setTime(jpHitDate);
			
			/** ASSET CALL TO GET THE ASSET DETAILS */
			try {
				assetInfoDTO = assetBO.getAssetInfo(slotNo,AssetParamType.ASSET_CONFIG_NUMBER, assetSearchFilter, siteId, null);
				
				if (assetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE) {
					log.warn("INVALID SLOT NO - Throw exception" + slotNo);				
					throw new Exception("INVALID SLOT NO");
				}
				jackpotDTO.setAssetConfigNumber(slotNo);
				if (assetInfoDTO.getAssetConfigLocation() != null) {
					jackpotDTO.setAssetConfigLocation(assetInfoDTO.getAssetConfigLocation());
				}
				if (assetInfoDTO.getDenominaton() != null){
					jackpotDTO.setSlotDenomination(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getDenominaton()));
					if(log.isDebugEnabled()) {
						log.debug("Asset Slot Denom: "+jackpotDTO.getSlotDenomination());
					}
				}						
				if (assetInfoDTO.getAccountingDenom() != null){
					jackpotDTO.setGmuDenom(ConversionUtil.dollarToCentsRtnLong(assetInfoDTO.getAccountingDenom()));
				}
				if (assetInfoDTO.getAssetConfigStatus() != null 
						&& assetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(ASSET_ONLINE_STATUS)){
					jackpotDTO.setSlotOnline(true);
				}else{
					jackpotDTO.setSlotOnline(false);
				}
				jackpotDTO.setAreaLongName(assetInfoDTO.getAreaLongName());
				jackpotDTO.setAreaShortName(assetInfoDTO.getAreaName());
				jackpotDTO.setSlotDescription(assetInfoDTO.getTypeDesc());
				jackpotDTO.setSealNumber(assetInfoDTO.getRegulatoryId());
				
			} catch (AssetEngineServiceException e) {
				log.error("Exception in getAssetInfo for Slot: " + slotNo,e);
				throw new Exception(e);
			} catch (Exception e2) {
				log.error("Exception in getAssetInfo for Slot: " + slotNo,e2);
				throw new Exception(e2);
			}
			
			//SET THE REQUIRED JP DB FIELDS
			jackpotDTO.setSiteId(siteId);
			jackpotDTO.setSlipPrimaryKey(JackpotUtil.getSlipPrimaryKey(slotNo,	siteId, 0));
			jackpotDTO.setMessageId(MessageUtil.getMessageId());	
			jackpotDTO.setStatusFlagId(ILookupTableConstants.PENDING_STATUS_ID);		
			jackpotDTO.setJackpotId(jackpotId.toUpperCase());
			jackpotDTO.setOriginalAmount(jpAmount);
			jackpotDTO.setTransactionDate(jpHitDate);
			jackpotDTO.setPendingAlertTxnDate(jpHitDate.getTime());	
			// Setting site param GENERATE_RANDOM_SEQUENCE_NUMBER to JackpotDTO to enable random sequence numbering 
			String generateRandSeqNum = JackpotUtil.getSiteParamValue(
					ISiteConfigConstants.GENERATE_RANDOM_SEQUENCE_NUMBER, jackpotDTO.getSiteId());
			jackpotDTO.setGenerateRandSeqNum(generateRandSeqNum);
			jackpotDTO.setCashlessAccountType(null);
			jackpotDTO.setAccountNumber(null);
			jackpotDTO.setAssociatedPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
			
			//CALL JACKPOT BO METHOD TO CREATE A SLIP IN THE DB
			jackpotDTO = jackpotBO.createJackpotFromExternalSystem(jackpotDTO, transExcepCode, jpGeneratedBy);
			
			//IF JP WAS CREATED SUCCESSFULY THEN SEND ALERT MSGS
			if(jackpotDTO.isPostedSuccessfully()){
				
				/** 
				 * PENDING JP SLIP FOR SLOT XXXX (92) to be sent to the AlertsEngine
				 */				
				// SET THE JACKPOT DTO FIELDS FOR SENDING THE BELOW ALERT MSG 92						
				// Add new pending JP in cache for sending pending JP alert Msg.
				log.info("BEFORE Calling the start of the jp job for sending the pending jp alert msg in JakpotFacade");					
				JackpotUtil.addNewPendingJackpotInCache(jackpotDTO);
				log.info("AFTER Calling the start of the jp job for sending the pending jp alert msg in JakpotFacade");					
								
				/** 
				 *  JACKPOT  (1) to be sent to the AlertsEngine if JP ID is FA, FC, FE
				 *  PROGRESSIVE JACKPOT  (2) to be sent to the AlertsEngine if JP ID between 112 and 159 
				 *  
				 */
				JackpotAlertObject jpAlertsJackpotId = new JackpotAlertObject();
				
				if(iJackpotId == IJackpotIds.JACKPOT_ID_250_FA
						|| iJackpotId == IJackpotIds.JACKPOT_ID_252_FC
						|| iJackpotId == IJackpotIds.JACKPOT_ID_251_FB
						|| iJackpotId == IJackpotIds.JACKPOT_ID_253_FD
						|| iJackpotId == IJackpotIds.JACKPOT_ID_254_FE){
					
					jpAlertsJackpotId.setTerminalMessageNumber(IAlertMessageConstants.JACKPOT);
					jpAlertsJackpotId.setTerminalMessage("JACKPOT");
					log.debug("JACKPOT");
				}
				else if ((iJackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_149 
						 || iJackpotId !=IJackpotIds.JACKPOT_ID_INVALID_PROG_150) 
						&& (iJackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
								&& iJackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)) {
					
					jpAlertsJackpotId.setTerminalMessageNumber(IAlertMessageConstants.PROGRESSIVE_JACKPOT);
					jpAlertsJackpotId.setTerminalMessage("PROGRESSIVE JACKPOT");
					log.debug("PROGRESSIVE JACKPOT");
				}
				if(jpAlertsJackpotId.getTerminalMessageNumber()!=0){
					jpAlertsJackpotId.setJackpotId((char)iJackpotId);
					jpAlertsJackpotId.setAssetConfigNumber(slotNo);
					jpAlertsJackpotId.setStandNumber(jackpotDTO.getAssetConfigLocation());
					jpAlertsJackpotId.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
					jpAlertsJackpotId.setJackpotAmount(jpAmount);												
					jpAlertsJackpotId.setDateTime(calJpHitDate);
					jpAlertsJackpotId.setExceptionCode(exceptionCode);
					jpAlertsJackpotId.setMessageId(jackpotDTO.getMessageId());
					jpAlertsJackpotId.setSendingEngineName(MODULE_NAME);
					jpAlertsJackpotId.setSiteId(siteId);
					jpAlertsJackpotId.setSlotAreaId(jackpotDTO.getAreaShortName());
					/** SENDING MSG TO ALERTS ENGINE */
					
					log.info("Sending the msg to Alerts for "+jpAlertsJackpotId.getTerminalMessage()+" : "+jpAlertsJackpotId.getTerminalMessageNumber());
					
					jpAlertObjLst.add(jpAlertsJackpotId);					
				}	
				
			}else{
				
				/** 
				 * JACKPOT - INVALID AMOUNT OF ZERO (161) to be sent to the AlertsEngine
				 */
				if(jpAmount == 0 && 
						(iJackpotId == IJackpotIds.JACKPOT_ID_250_FA 								
								|| iJackpotId == IJackpotIds.JACKPOT_ID_251_FB
								|| iJackpotId == IJackpotIds.JACKPOT_ID_252_FC
								|| iJackpotId == IJackpotIds.JACKPOT_ID_253_FD
								|| iJackpotId == IJackpotIds.JACKPOT_ID_254_FE)){
									
					JackpotAlertObject jpAlertsInvalidAmt = new JackpotAlertObject();
					
					jpAlertsInvalidAmt.setTerminalMessageNumber(IAlertMessageConstants.JP_INVALID_AMT_ZERO);
					jpAlertsInvalidAmt.setTerminalMessage("JACKPOT - INVALID AMOUNT OF ZERO");
					jpAlertsInvalidAmt.setJackpotId((char)iJackpotId);
					jpAlertsInvalidAmt.setAssetConfigNumber(slotNo);
					jpAlertsInvalidAmt.setStandNumber(jackpotDTO.getAssetConfigLocation());
					jpAlertsInvalidAmt.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
					jpAlertsInvalidAmt.setJackpotAmount(jpAmount);
					jpAlertsInvalidAmt.setDateTime(calJpHitDate);
					jpAlertsInvalidAmt.setExceptionCode(exceptionCode);
					jpAlertsInvalidAmt.setMessageId(jackpotDTO.getMessageId());
					jpAlertsInvalidAmt.setSendingEngineName(MODULE_NAME);
					jpAlertsInvalidAmt.setSiteId(siteId);
					jpAlertsInvalidAmt.setSlotAreaId(jackpotDTO.getAreaShortName());
					/** SENDING MSG TO ALERTS ENGINE */
					log.info("Sending the msg to Alerts for "+jpAlertsInvalidAmt.getTerminalMessage()+" : "+jpAlertsInvalidAmt.getTerminalMessageNumber());
					
					jpAlertObjLst.add(jpAlertsInvalidAmt);				
				}
				
				/** 
				 *  JACKPOT - INVALID JACKPOT ID (163) to be sent to the AlertsEngine if JP ID = 0
				 *  
				 */
				JackpotAlertObject jpAlertsInvalidJpId = new JackpotAlertObject();
				
				if ((iJackpotId == IJackpotIds.JACKPOT_ID_INVALID_PROG_149 
						|| iJackpotId == IJackpotIds.JACKPOT_ID_INVALID_PROG_150)
						|| (iJackpotId != IJackpotIds.JACKPOT_ID_252_FC
								&& iJackpotId != IJackpotIds.JACKPOT_ID_253_FD
								&& iJackpotId != IJackpotIds.JACKPOT_ID_251_FB
								&& iJackpotId != IJackpotIds.JACKPOT_ID_254_FE
								&& iJackpotId != IJackpotIds.JACKPOT_ID_250_FA 
								&& !(iJackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
										&& iJackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159))) {
				
					jpAlertsInvalidJpId.setTerminalMessageNumber(IAlertMessageConstants.JP_INVALID_JP_ID);
					jpAlertsInvalidJpId.setTerminalMessage("JACKPOT - INVALID JACKPOT ID");
					jpAlertsInvalidJpId.setJackpotId((char)iJackpotId);
					jpAlertsInvalidJpId.setAssetConfigNumber(slotNo);
					jpAlertsInvalidJpId.setStandNumber(jackpotDTO.getAssetConfigLocation());
					jpAlertsInvalidJpId.setEngineId(JACKPOT_ALERTS_ENGINE_ID);
					jpAlertsInvalidJpId.setDateTime(calJpHitDate);
					jpAlertsInvalidJpId.setExceptionCode(exceptionCode);
					jpAlertsInvalidJpId.setMessageId(jackpotDTO.getMessageId());
					jpAlertsInvalidJpId.setSendingEngineName(MODULE_NAME);
					jpAlertsInvalidJpId.setSiteId(siteId);
					jpAlertsInvalidJpId.setSlotAreaId(jackpotDTO.getAreaShortName());
					/** SENDING MSG TO ALERTS ENGINE */
					log.info("Sending the msg to Alerts for "+jpAlertsInvalidJpId.getTerminalMessage()+" : "+jpAlertsInvalidJpId.getTerminalMessageNumber());
					
					jpAlertObjLst.add(jpAlertsInvalidJpId);				
				}
				
			}
			
			if(jpAlertObjLst!=null && jpAlertObjLst.size()>0){
				try {
					log.info("Before sending the msg to Alerts for XC - 10");
					alertsBO.sendAlert(jpAlertObjLst);
					log.info("After sending the msg to Alerts for XC - 10");
				} catch (AlertsEngineBOException e) {
					log.error("Exception in send alert of JackpotFacade for XC - 10"+e);
					throw (e);
				}
			}
			 
			//CALL MARKETING TO SEND PT10 TO CMP
			this.postManualJackpot(siteId, jackpotDTO.getAssociatedPlayerCard(), jpAmount, slotNo, jackpotDTO.getMessageId(), jackpotId);
									
		} catch (Exception e) {
			log.error("Exception when trying to create a jp through external system",e);
			throw (e);		
		}
		
		if(log.isDebugEnabled()){
			log.debug("Returned MsgId: "+jackpotDTO.getMessageId());
		}			
		return jackpotDTO.getMessageId();
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO retrieveSlipBySequenceNumber(long sequenceNumber, int siteId ) throws JackpotEngineServiceException{
		if(log.isInfoEnabled()){
			log.info("retrieveSlipBySequenceNumber Facade");
		}
		return jackpotBO.retrieveSlipBySequenceNumber(sequenceNumber, siteId);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> getAllActiveJackpotsForEmpId(String empId, int siteId) throws JackpotEngineServiceException{
		if(log.isInfoEnabled()){
			log.info("getAllActiveJackpotsForEmpId Facade");
		}
		return jackpotBO.getAllActiveJackpotsForEmpId(empId, siteId);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> getJackpotDetails(JackpotDTO jackpotDTO) throws JackpotDAOException{
		if(log.isInfoEnabled()){
			log.info("getJackpotDetails jackpotDTO Facade");
		}
		return jackpotBO.getJackpotDetails(jackpotDTO);
	}
}