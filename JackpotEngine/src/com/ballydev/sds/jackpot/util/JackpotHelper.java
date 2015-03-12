/*****************************************************************************
 * $Id: JackpotHelper.java,v 1.67.1.0.1.0.3.1, 2013-10-16 07:40:43Z, SDS12.3.3 Checkin User$
 * $Date: 10/16/2013 2:40:43 AM$
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
package com.ballydev.sds.jackpot.util;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_PROGRSSIVE_BO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ballydev.sds.common.IHeader;
import com.ballydev.sds.common.jackpot.IJackpotToCreditMeter;
import com.ballydev.sds.common.message.SDSMessage;
import com.ballydev.sds.common.message.SystemGeneratedMessage;
import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.ecash.dto.account.SDSAccessDTO;
import com.ballydev.sds.jackpot.constants.IAlertMessageConstants;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.db.slip.Jackpot;
import com.ballydev.sds.jackpot.db.slip.SlipData;
import com.ballydev.sds.jackpot.db.slip.SlipReference;
import com.ballydev.sds.jackpot.db.slip.StatusFlag;
import com.ballydev.sds.jackpot.db.slip.Transaction;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotDetailsDTO;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.progressive.bo.IProgressiveBO;

/**
 * This class will copy all the properties from messaging instance to JackpotDTO
 * 
 * @author vijayrajm
 * @version $Revision: 72$
 */
public class JackpotHelper implements IAppConstants{

	/**
	 * Logger instance
	 */
	private static Logger log = JackpotEngineLogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * This will convert String into Long
	 * 
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static long convertStringToLong(String str)
			throws NumberFormatException {

		return Long.parseLong(str, 16);

	}

	/**
	 * This will copy the fields from SlipReference POJO to JackpotDTO
	 * 
	 * @param slipRef
	 * @return jackpotDTO
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO getJackpotDTO(SlipReference slipRef) {

		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotDTO method of JackpotHelper Util");
			log.info("Inside set fields from SlipReference POJO to JackpotDTO");
			log.info("*********************************************************************");
			log.info("slipRef.getId(): " + slipRef.getId());
		}
		JackpotDTO jackpotDTO = new JackpotDTO();
		if (slipRef.getId() != null) {
			jackpotDTO.setSlipReferenceId(slipRef.getId());
			if(log.isDebugEnabled()) {
				log.debug("SlipReference Id :" + jackpotDTO.getSlipReferenceId());
			}
		}
		if (slipRef.getSlipType() != null) {
			jackpotDTO.setSlipTypeId(slipRef.getSlipTypeId());
			jackpotDTO.setSlipTypeDesc(slipRef.getSlipType().getDescription());
		}
		if (slipRef.getSequenceNumber() != null) {
			jackpotDTO.setSequenceNumber(slipRef.getSequenceNumber());
			if(log.isDebugEnabled()) {
				log.debug("Sequence Number..." + jackpotDTO.getSequenceNumber());
			}
		}
		if (slipRef.getProcessFlag() != null) {
			jackpotDTO.setProcessFlagId(slipRef.getProcessFlagId());
			jackpotDTO.setProcessFlagDesc(slipRef.getProcessFlag().getFlagDescription());
		}
		if (slipRef.getAuditCode() != null) {
			jackpotDTO.setAuditCodeId(slipRef.getAuditCodeId());
			jackpotDTO.setAuditCodeDesc(slipRef.getAuditCode().getDescription());
		}
		if (slipRef.getAreaLongName() != null) {
			jackpotDTO.setAreaLongName(slipRef.getAreaLongName());
		}
		if (slipRef.getAreaShortName() != null) {
			jackpotDTO.setAreaShortName(slipRef.getAreaShortName());
		}
		if (slipRef.getStatusFlag() != null) {
			jackpotDTO.setStatusFlagId(slipRef.getStatusFlagId());
			jackpotDTO.setStatusFlagDesc(slipRef.getStatusFlag().getFlagDescription());
		}
		if (slipRef.getSlotDenomination() != null) {
			jackpotDTO.setSlotDenomination(slipRef.getSlotDenomination());
		}
		if (slipRef.getSlotDescription() != null) {
			jackpotDTO.setSlotDescription(slipRef.getSlotDescription());
		}
		if (slipRef.getGmuDenomination() != null) {
			jackpotDTO.setGmuDenom(slipRef.getGmuDenomination());
		}
		if (slipRef.getAcnfNumber() != null) {
			jackpotDTO.setAssetConfigNumber(slipRef.getAcnfNumber());
			if(log.isDebugEnabled()) {
				log.debug("Asset No..." + jackpotDTO.getAssetConfigNumber());
			}
		}
		if (slipRef.getAcnfLocation() != null) {
			jackpotDTO.setAssetConfigLocation(slipRef.getAcnfLocation());
		}
		if (slipRef.getSealNumber() != null) {
			jackpotDTO.setSealNumber(slipRef.getSealNumber());
		}
		if (slipRef.getTransactionDate() != null) {
			jackpotDTO.setTransactionDate(DateHelper.getLocalTimeFromUTC(slipRef.getTransactionDate()));
			if(log.isDebugEnabled()) {
				log.debug("Date/Time..." + jackpotDTO.getTransactionDate());
			}
		}
		if (slipRef.getSiteId() != null) {
			jackpotDTO.setSiteId(slipRef.getSiteId());
			if(log.isDebugEnabled()) {
				log.debug("Site Id..." + jackpotDTO.getSiteId());
			}
		}
		// Setting SIte No to JACKPOTDTO
		if(slipRef.getSiteNo() != null) {
			jackpotDTO.setSiteNo(slipRef.getSiteNo());
			if(log.isDebugEnabled()) {
				log.debug("Site No..." + jackpotDTO.getSiteNo());
			}
		}
		if (slipRef.getMessageId() != null) {
			jackpotDTO.setMessageId(slipRef.getMessageId());
		}
		
		if(slipRef.getUpdatedTs() != null){
			jackpotDTO.setSlipRefUpdatedTs(slipRef.getUpdatedTs());	
		}
		if(slipRef.getPostToAccounting() != null) {
			jackpotDTO.setPostToAccounting(slipRef.getPostToAccounting());
		}
		if(slipRef.getAccountNumber() != null) {
			jackpotDTO.setAccountNumber(slipRef.getAccountNumber());
		}
		if(slipRef.getAccountType() != null) {
			jackpotDTO.setCashlessAccountType(slipRef.getAccountType());
		}

		Set<Jackpot> jackpotSet = slipRef.getJackpot();

		for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
			Jackpot jackpot = (Jackpot) iter.next();
			if(jackpot.getId() != null){
				jackpotDTO.setSlipId(Long.toString(jackpot.getId()));
			}
			if (jackpot.getJackpotTypeId() != null) {
				jackpotDTO.setJackpotTypeId(jackpot.getJackpotTypeId());
			}
			if (jackpot.getJackpotType()!= null && jackpot.getJackpotType().getFlagDescription() != null) {
				jackpotDTO.setJackpotTypeDesc(jackpot.getJackpotType().getFlagDescription());
			}
			if (jackpot.getJackpotId() != null) {
				jackpotDTO.setJackpotId(jackpot.getJackpotId());
			}
			if (jackpot.getOriginalAmount() != null) {
				jackpotDTO.setOriginalAmount(jackpot.getOriginalAmount());
				if(log.isDebugEnabled()) {
					log.debug("Jackpot Org Amount..." + jackpotDTO.getOriginalAmount());
				}
			}
			
			if(jackpot.getCalculatedSDSAmount() != null){
				jackpotDTO.setSdsCalculatedAmount(jackpot.getCalculatedSDSAmount().longValue());
				if(log.isInfoEnabled()) {
					log.info("Calculated SDS Amount from progressive engine for reference while printing slip :"+jackpot.getCalculatedSDSAmount().longValue());
				}
			}			
			if (jackpot.getHpjpAmount() != null) {
				jackpotDTO.setHpjpAmount(jackpot.getHpjpAmount());
			}
			if (jackpot.getHpjpAmountRounded() != null) {
				jackpotDTO.setHpjpAmountRounded(jackpot.getHpjpAmountRounded());
			}
			if (jackpot.getNetAmount() != null) {
				jackpotDTO.setJackpotNetAmount(jackpot.getNetAmount());
			}
			if (jackpot.getTaxAmount() != null) {
				jackpotDTO.setTaxAmount(jackpot.getTaxAmount());
			}
			if (jackpot.getTaxRateAmount() != null) {
				jackpotDTO.setTaxRateAmount(jackpot.getTaxRateAmount());
			}
			if (jackpot.getBlindAttempt() != null) {
				jackpotDTO.setBlindAttempt(jackpot.getBlindAttempt());
				if(log.isDebugEnabled()) {
					log.debug("Blind Att..." + jackpotDTO.getBlindAttempt());
				}
			}
			if (jackpot.getMachinePaidAmount() != null) {
				jackpotDTO.setMachinePaidAmount(jackpot.getMachinePaidAmount());
			}
			if (jackpot.getCoinsPlayed() != null) {
				jackpotDTO.setCoinsPlayed(jackpot.getCoinsPlayed());
			}
			if (jackpot.getPayline() != null) {
				jackpotDTO.setPayline(jackpot.getPayline());
			}
			if (jackpot.getAssociatedPlayerCard() != null) {
				jackpotDTO.setAssociatedPlayerCard(jackpot
						.getAssociatedPlayerCard());
			}
			if (jackpot.getPlayerCard() != null) {
				jackpotDTO.setPlayerCard(jackpot.getPlayerCard());
			}
			if (jackpot.getPlayerName() != null) {
				jackpotDTO.setPlayerName(jackpot.getPlayerName());
			}
			if (jackpot.getWinningCombination() != null) {
				jackpotDTO.setWinningComb(jackpot.getWinningCombination());
			}
			if (jackpot.getIsSlotOnline() != null) {
				if(jackpot.getIsSlotOnline() == 1) {
					jackpotDTO.setSlotOnline(true);
				} else if(jackpot.getIsSlotOnline() == 0) {
					jackpotDTO.setSlotOnline(false);
				}
				if(log.isInfoEnabled()) {
					log.info("Slot Online message value : " + jackpot.getIsSlotOnline());
					log.info("Is Slot Online : " + jackpotDTO.isSlotOnline());
				}
			}
			if (jackpot.getProgressiveLevel() != null) {
				jackpotDTO.setProgressiveLevel(jackpot.getProgressiveLevel());
				if(log.isDebugEnabled()) {
					log.debug("Progressive Levels : " + jackpotDTO.getProgressiveLevel());
				}
				jackpotDTO.setLstProgressiveLevel(JackpotUtil.getProgressiveLevelValues(jackpotDTO
						.getProgressiveLevel()));
			}
			
			if(jackpot.getGeneratedBy()!=null 
					&& jackpot.getGeneratedBy().equalsIgnoreCase(JpGeneratedBy.CONTROLLER.getValue())){
				jackpotDTO.setJpProgControllerGenerated(true);
			}	
		}
		Set<SlipData> slipDataSet = slipRef.getSlipData();

		for (Iterator iter = slipDataSet.iterator(); iter.hasNext();) {

			SlipData slipData = (SlipData) iter.next();
			if (slipData.getShift() != null) {
				jackpotDTO.setShift(slipData.getShift());
			}
			if (slipData.getWindowNumber() != null) {
				jackpotDTO.setWindowNumber(slipData.getWindowNumber());
			}
			if (slipData.getKioskProcessed() != null) {
				jackpotDTO.setAssetConfNumberUsed(slipData.getKioskProcessed());
			}
			if (slipData.getActrLogin() != null) {
				jackpotDTO.setActorLogin(slipData.getActrLogin());
			}
			if (slipData.getAuthEmployeeId1() != null) {
				jackpotDTO.setAuthEmployeeId1(slipData.getAuthEmployeeId1());
			}
			if (slipData.getAuthEmployeeId2() != null) {
				jackpotDTO.setAuthEmployeeId2(slipData.getAuthEmployeeId2());
			}
			log.info("slipData.getAmountAuthEmpId() : " + slipData.getAmountAuthEmpId());
			if(slipData.getAmountAuthEmpId() != null) {
				jackpotDTO.setAmountAuthEmpId(slipData.getAmountAuthEmpId());
			}
		}

		Set<Transaction> transactionSet = slipRef.getTransaction();
		for (Iterator iter = transactionSet.iterator(); iter.hasNext();) {
			Transaction transaction = (Transaction) iter.next();
			if (transaction.getStatusFlagId() != null
					&& (transaction.getStatusFlagId() == ILookupTableConstants.CARD_IN_STATUS_ID || transaction
							.getStatusFlagId() == ILookupTableConstants.POUCH_PAY_ATTN_STATUS_ID)) {
				if (transaction.getEmployeeId() != null) {
					jackpotDTO.setSlotAttendantId(transaction.getEmployeeId());
				}
				if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setSlotAttendantFirstName(transaction.getEmployeeFirstName());
				}
				if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setSlotAttendantLastName(transaction.getEmployeeLastName());
				}
				if (transaction.getEmployeeCardNo() != null) {
					jackpotDTO.setEmployeeCard(transaction.getEmployeeCardNo());
				}

			}
			if (transaction.getStatusFlagId() != null
					&& (transaction.getStatusFlagId() != ILookupTableConstants.CARD_IN_STATUS_ID && transaction
							.getStatusFlagId() != ILookupTableConstants.POUCH_PAY_ATTN_STATUS_ID)) {
				if (transaction.getEmployeeId() != null) {
					jackpotDTO.setPrintEmployeeLogin(transaction.getEmployeeId());
					if (log.isDebugEnabled()) {
						log.debug("transaction - PrintEmployeeLoginPrintEmployeeLogin: "
								+ transaction.getEmployeeId());
					}
				}
				if (transaction.getEmployeeFirstName() != null && transaction.getEmployeeLastName() != null) {
					jackpotDTO.setEmployeeName(transaction.getEmployeeFirstName() + " "
							+ transaction.getEmployeeLastName());
				} else if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setEmployeeName(transaction.getEmployeeFirstName());
				} else if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setEmployeeName(transaction.getEmployeeLastName());
				}
				
				if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setEmployeeFirstName(transaction.getEmployeeFirstName());
				} 
				if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setEmployeeLastName(transaction.getEmployeeLastName());
				}
				
				if (transaction.getCreatedTs() != null) {
					jackpotDTO.setPrintDate(transaction.getCreatedTs());
				}

			}
			if (transaction.getResponseCode() != null) {
				jackpotDTO.setResponseCode(transaction.getResponseCode());
			}
			if (transaction.getMessageId() != null) {
				jackpotDTO.setTransMessageId(transaction.getMessageId());
			}
			if (transaction.getPrinterUsed() != null) {
				jackpotDTO.setPrinterUsed(transaction.getPrinterUsed());
			}
		}
		if(log.isDebugEnabled()) {
			log.debug("*********************************************************************");
		}
		return jackpotDTO;
	}

	/**
	 * This will copy the fields from SlipReference POJO to JackpotDTO
	 * 
	 * @param slipRef
	 * @return jackpotDTO
	 */
	public static JackpotDTO getJackpotDTOOnStatusId(SlipReference slipRef){
			//,short statusFlagIdChk) {
		if(log.isDebugEnabled()) {
			log.info("Inside set fields from SlipReference POJO to JackpotDTO");
			log.info("*********************************************************************");
		}
		JackpotDTO jackpotDTO = new JackpotDTO();
		if (slipRef.getSequenceNumber() != null) {
			jackpotDTO.setSequenceNumber(slipRef.getSequenceNumber());
			if(log.isDebugEnabled()) {
				log.debug("Sequence Number..." + jackpotDTO.getSequenceNumber());
			}
		}
		if (slipRef.getProcessFlagId() != null) {
			jackpotDTO.setProcessFlagId(slipRef.getProcessFlagId());
		}
		if (slipRef.getAreaLongName() != null) {
			jackpotDTO.setAreaLongName(slipRef.getAreaLongName());
		}
		if (slipRef.getAreaShortName() != null) {
			jackpotDTO.setAreaShortName(slipRef.getAreaShortName());
		}
		if (slipRef.getStatusFlagId() != null) {
			jackpotDTO.setStatusFlagId(slipRef.getStatusFlagId());
		}
		if (slipRef.getSlotDenomination() != null) {
			jackpotDTO.setSlotDenomination(slipRef.getSlotDenomination());
		}
		if (slipRef.getSlotDescription() != null) {
			jackpotDTO.setSlotDescription(slipRef.getSlotDescription());
		}
		if (slipRef.getGmuDenomination() != null) {
			jackpotDTO.setGmuDenom(slipRef.getGmuDenomination());
		}
		if (slipRef.getAcnfNumber() != null) {
			jackpotDTO.setAssetConfigNumber(slipRef.getAcnfNumber());
			if(log.isDebugEnabled()) {
				log.debug("Asset No..." + jackpotDTO.getAssetConfigNumber());
			}
		}
		if (slipRef.getAcnfLocation() != null) {
			jackpotDTO.setAssetConfigLocation(slipRef.getAcnfLocation());
		}
		if (slipRef.getSealNumber() != null) {
			jackpotDTO.setSealNumber(slipRef.getSealNumber());
		}
		if (slipRef.getTransactionDate() != null) {
			jackpotDTO.setTransactionDate(DateHelper.getLocalTimeFromUTC(slipRef.getTransactionDate()));
			if(log.isDebugEnabled()) {
				log.debug("Date/Time..." + jackpotDTO.getTransactionDate());
			}
		}
		if (slipRef.getSiteId() != null) {
			jackpotDTO.setSiteId(slipRef.getSiteId());
			if(log.isDebugEnabled()) {
				log.debug("Site Id..." + jackpotDTO.getSiteId());
			}
		}
		if (slipRef.getMessageId() != null) {
			jackpotDTO.setMessageId(slipRef.getMessageId());
		}
		if(slipRef.getPostToAccounting() != null) {
			jackpotDTO.setPostToAccounting(slipRef.getPostToAccounting());
		}

		Set<Jackpot> jackpotSet = slipRef.getJackpot();

		for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
			Jackpot jackpot = (Jackpot) iter.next();
			if (jackpot.getJackpotTypeId() != null) {
				jackpotDTO.setJackpotTypeId(jackpot.getJackpotTypeId());
			}
			if (jackpot.getJackpotId() != null) {
				jackpotDTO.setJackpotId(jackpot.getJackpotId());
			}
			if (jackpot.getOriginalAmount() != null) {
				jackpotDTO.setOriginalAmount(jackpot.getOriginalAmount());
				if(log.isDebugEnabled()) {
					log.debug("Jackpot Org Amount..." + jackpotDTO.getOriginalAmount());
				}
			}
			if (jackpot.getHpjpAmount() != null) {
				jackpotDTO.setHpjpAmount(jackpot.getHpjpAmount());
			}
			if (jackpot.getHpjpAmountRounded() != null) {
				jackpotDTO.setHpjpAmountRounded(jackpot.getHpjpAmountRounded());
			}
			if (jackpot.getNetAmount() != null) {
				jackpotDTO.setJackpotNetAmount(jackpot.getNetAmount());
			}
			if (jackpot.getTaxAmount() != null) {
				jackpotDTO.setTaxAmount(jackpot.getTaxAmount());
			}
			if (jackpot.getTaxRateAmount() != null) {
				jackpotDTO.setTaxRateAmount(jackpot.getTaxRateAmount());
			}
			if (jackpot.getBlindAttempt() != null) {
				jackpotDTO.setBlindAttempt(jackpot.getBlindAttempt());
				if(log.isDebugEnabled()) {
					log.debug("Blind Att..." + jackpotDTO.getBlindAttempt());
				}
			}
			if (jackpot.getMachinePaidAmount() != null) {
				jackpotDTO.setMachinePaidAmount(jackpot.getMachinePaidAmount());
			}
			if (jackpot.getCoinsPlayed() != null) {
				jackpotDTO.setCoinsPlayed(jackpot.getCoinsPlayed());
			}
			if (jackpot.getPayline() != null) {
				jackpotDTO.setPayline(jackpot.getPayline());
			}
			if (jackpot.getAssociatedPlayerCard() != null) {
				jackpotDTO.setAssociatedPlayerCard(jackpot
						.getAssociatedPlayerCard());
			}
			if (jackpot.getPlayerCard() != null) {
				jackpotDTO.setPlayerCard(jackpot.getPlayerCard());
			}
			if (jackpot.getPlayerName() != null) {
				jackpotDTO.setPlayerName(jackpot.getPlayerName());
			}
			if (jackpot.getWinningCombination() != null) {
				jackpotDTO.setWinningComb(jackpot.getWinningCombination());
			}
			if (jackpot.getIsSlotOnline() != null) {
				if (jackpot.getIsSlotOnline() == 1) {
					jackpotDTO.setSlotOnline(true);
				} else if (jackpot.getIsSlotOnline() == 0) {
					jackpotDTO.setSlotOnline(false);
				}
			}
			
			if(jackpot.getGeneratedBy()!=null 
					&& jackpot.getGeneratedBy().equalsIgnoreCase(JpGeneratedBy.CONTROLLER.getValue())){
				jackpotDTO.setJpProgControllerGenerated(true);
			}
		}

		Set<SlipData> slipDataSet = slipRef.getSlipData();

		for (Iterator iter = slipDataSet.iterator(); iter.hasNext();) {

			SlipData slipData = (SlipData) iter.next();
			if (slipData.getShift() != null) {
				jackpotDTO.setShift(slipData.getShift());
			}
			if (slipData.getWindowNumber() != null) {
				jackpotDTO.setWindowNumber(slipData.getWindowNumber());
			}
			if (slipData.getKioskProcessed() != null) {
				jackpotDTO.setAssetConfNumberUsed(slipData.getKioskProcessed());
			}
			if (slipData.getActrLogin() != null) {
				jackpotDTO.setActorLogin(slipData.getActrLogin());
			}
			if (slipData.getAuthEmployeeId1() != null) {
				jackpotDTO.setAuthEmployeeId1(slipData.getAuthEmployeeId1());
			}
			if (slipData.getAuthEmployeeId2() != null) {
				jackpotDTO.setAuthEmployeeId2(slipData.getAuthEmployeeId2());
			}
		}

		Set<Transaction> transactionSet = slipRef.getTransaction();
		for (Iterator iter = transactionSet.iterator(); iter.hasNext();) {
			Transaction transaction = (Transaction) iter.next();
			if (transaction.getStatusFlagId() != null
					&& (transaction.getStatusFlagId() == ILookupTableConstants.CARD_IN_STATUS_ID 
							|| transaction.getStatusFlagId() == ILookupTableConstants.POUCH_PAY_ATTN_STATUS_ID)) {
				if (transaction.getEmployeeId() != null) {
					jackpotDTO.setSlotAttendantId(transaction.getEmployeeId());
				}
				if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setSlotAttendantFirstName(transaction
							.getEmployeeFirstName());
				}
				if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setSlotAttendantLastName(transaction
							.getEmployeeLastName());
				}
				if (transaction.getEmployeeCardNo() != null) {
					jackpotDTO.setEmployeeCard(transaction.getEmployeeCardNo());
				}

			}
			if (transaction.getStatusFlagId() != null
					&& (transaction.getStatusFlagId() == ILookupTableConstants.PROCESSED_STATUS_ID 
							|| transaction.getStatusFlagId() == ILookupTableConstants.CHANGE_STATUS_ID
							|| transaction.getStatusFlagId() == ILookupTableConstants.PRINTED_STATUS_ID)) {
				if (transaction.getEmployeeId() != null) {
					jackpotDTO.setPrintEmployeeLogin(transaction.getEmployeeId());
					if(log.isDebugEnabled()) {
						log.debug("transaction - PrintEmployeeLoginPrintEmployeeLogin: "
									+ transaction.getEmployeeId());
					}
				}
				if (transaction.getEmployeeFirstName() != null
						&& transaction.getEmployeeLastName() != null) {
					jackpotDTO.setEmployeeName(transaction
							.getEmployeeFirstName()
							+ " " + transaction.getEmployeeLastName());
				} else if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setEmployeeName(transaction
							.getEmployeeFirstName());
				} else if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setEmployeeName(transaction
							.getEmployeeLastName());
				}
			}
			if (transaction.getResponseCode() != null) {
				jackpotDTO.setResponseCode(transaction.getResponseCode());
			}
			if (transaction.getMessageId() != null) {
				jackpotDTO.setTransMessageId(transaction.getMessageId());
			}
			if (transaction.getPrinterUsed() != null) {
				jackpotDTO.setPrinterUsed(transaction.getPrinterUsed());
			}
		}
		if(log.isDebugEnabled()) {
			log.debug("*********************************************************************");
		}
		return jackpotDTO;
	}

	/**
	 * Method to get the Slot Attendant info
	 * 
	 * @param slipRef
	 * @return JackpotDTO
	 */
	public static JackpotDTO getJackpotDTOSlotAttendantInfo(
			SlipReference slipRef) {
		log.info("Inside set fields from SlipReference POJO to JackpotDTO");
		log.debug("*********************************************************************");
		JackpotDTO jackpotDTO = new JackpotDTO();
		Set<Transaction> transactionSet = slipRef.getTransaction();
		for (Iterator iter = transactionSet.iterator(); iter.hasNext();) {
			Transaction transaction = (Transaction) iter.next();
			if (transaction.getStatusFlagId() != null
					&& transaction.getStatusFlagId() == ILookupTableConstants.CARD_IN_STATUS_ID) {
				log.info("inside if cond for CARD_IN_STATUS_ID");
				if (transaction.getEmployeeId() != null) {
					jackpotDTO.setSlotAttendantId(transaction.getEmployeeId());
				}
				if (transaction.getEmployeeFirstName() != null) {
					jackpotDTO.setSlotAttendantFirstName(transaction
							.getEmployeeFirstName());
				}
				if (transaction.getEmployeeLastName() != null) {
					jackpotDTO.setSlotAttendantLastName(transaction
							.getEmployeeLastName());
				}
				if (transaction.getEmployeeCardNo() != null) {
					jackpotDTO.setEmployeeCard(transaction.getEmployeeCardNo());
				}
				break;
			}
		}
		log.debug("*********************************************************************");
		return jackpotDTO;
	}

	/**
	 * Method used to get the Jackpot details for the Jackpot Slip Report
	 * 
	 * @param slipRef
	 * @param rptEmpId
	 * @return
	 */
	public static JackpotDTO getJackpotDTOForReport(SlipReference slipRef,
			String rptEmpId) {

		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			System.out
					.println("Inside set fields from SlipReference POJO to JackpotDTO");
			log
					.debug("*********************************************************************");

			if (slipRef.getStatusFlagId() != 0) {
				jackpotDTO.setStatusFlagId(slipRef.getStatusFlagId());
			}
			Set<Transaction> transactionSet = slipRef.getTransaction();
			for (Iterator iter = transactionSet.iterator(); iter.hasNext();) {
				Transaction transaction = (Transaction) iter.next();

				if (transaction.getStatusFlagId() != null
						&& (transaction.getStatusFlagId() != ILookupTableConstants.CARD_IN_STATUS_ID
								&& transaction.getStatusFlagId() != ILookupTableConstants.POUCH_PAY_ATTN_STATUS_ID)) {

					if (transaction.getEmployeeId() != null
							&& transaction.getEmployeeId().equalsIgnoreCase(
									PadderUtil.lPad(rptEmpId, IAppConstants.EMPLOYEE_ID_PAD_LENGTH))) {
						jackpotDTO.setPrintEmployeeLogin(transaction
								.getEmployeeId());
						System.out
								.println("transaction - PrintEmployeeLoginPrintEmployeeLogin: "
										+ transaction.getEmployeeId());
						jackpotDTO.setTransStatusFlagId(transaction
								.getStatusFlagId());

						if (transaction.getEmployeeFirstName() != null
								&& transaction.getEmployeeLastName() != null) {
							jackpotDTO.setEmployeeName(transaction
									.getEmployeeFirstName()
									+ " " + transaction.getEmployeeLastName());
						} else if (transaction.getEmployeeFirstName() != null) {
							jackpotDTO.setEmployeeName(transaction
									.getEmployeeFirstName());
						} else if (transaction.getEmployeeLastName() != null) {
							jackpotDTO.setEmployeeName(transaction
									.getEmployeeLastName());
						}

						Set<Jackpot> jackpotSet = slipRef.getJackpot();

						for (Iterator iter1 = jackpotSet.iterator(); iter1
								.hasNext();) {
							Jackpot jackpot = (Jackpot) iter1.next();
							if (jackpot.getJackpotTypeId() != null) {
								jackpotDTO.setJackpotTypeId(jackpot
										.getJackpotTypeId());
							}
							if (jackpot.getOriginalAmount() != null) {
								jackpotDTO.setOriginalAmount(jackpot
										.getOriginalAmount());
								if(log.isDebugEnabled()) {
								log.debug("Jackpot Org Amount..."
										+ jackpotDTO.getOriginalAmount());
								}
							}
							if (jackpot.getHpjpAmount() != null) {
								jackpotDTO.setHpjpAmount(jackpot
										.getHpjpAmount());
							}
							if (jackpot.getHpjpAmountRounded() != null) {
								jackpotDTO.setHpjpAmountRounded(jackpot
										.getHpjpAmountRounded());
							}
							if (jackpot.getNetAmount() != null) {
								jackpotDTO.setJackpotNetAmount(jackpot
										.getNetAmount());
							}
						}

					}
				}
			}
			log.debug("*********************************************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jackpotDTO;
	}

	/**
	 * Method to getJackpotDetailsDTO 
	 * @param slipRef
	 * @return
	 */
	public static JackpotDetailsDTO getJackpotDetailsDTO(SlipReference slipRef) {

		log.info("Inside set fields from SlipReference POJO to JackpotDTO");
		log
				.debug("*********************************************************************");
		JackpotDetailsDTO jackpotDetDTO = new JackpotDetailsDTO();
		if (slipRef.getSequenceNumber() != null) {
			jackpotDetDTO.setSequenceNumber(slipRef.getSequenceNumber());
			if(log.isDebugEnabled()) {
			log.debug("Sequence Number..." + jackpotDetDTO.getSequenceNumber());
			}
		}
		if (slipRef.getProcessFlagId() != null) {
			jackpotDetDTO.setProcessFlagId(slipRef.getProcessFlagId());
		}
		if (slipRef.getAreaLongName() != null) {
			jackpotDetDTO.setAreaLongName(slipRef.getAreaLongName());
		}
		if (slipRef.getAreaShortName() != null) {
			jackpotDetDTO.setAreaShortName(slipRef.getAreaShortName());
		}
		if (slipRef.getStatusFlagId() != null) {
			jackpotDetDTO.setStatusFlagId(slipRef.getStatusFlagId());
		}
		if (slipRef.getAuditCodeId() != null) {
			jackpotDetDTO.setAuditCodeId(slipRef.getAuditCodeId());
		}
		StatusFlag statusFlagSet = slipRef.getStatusFlag();
		if(statusFlagSet!=null){
			jackpotDetDTO.setStatusFlagDesc(statusFlagSet.getFlagDescription());
		}
		
		if (slipRef.getSlotDenomination() != null) {
			jackpotDetDTO.setSlotDenomination(slipRef.getSlotDenomination());
		}
		if (slipRef.getSlotDescription() != null) {
			jackpotDetDTO.setSlotDescription(slipRef.getSlotDescription());
		}
		if (slipRef.getGmuDenomination() != null) {
			jackpotDetDTO.setGmuDenom(slipRef.getGmuDenomination());
		}
		if (slipRef.getAcnfNumber() != null) {
			jackpotDetDTO.setAssetConfigNumber(slipRef.getAcnfNumber());
			if(log.isDebugEnabled()) {
			log.debug("Asset No..." + jackpotDetDTO.getAssetConfigNumber());
			}
		}
		if (slipRef.getAcnfLocation() != null) {
			jackpotDetDTO.setAssetConfigLocation(slipRef.getAcnfLocation());
		}
		if (slipRef.getSealNumber() != null) {
			jackpotDetDTO.setSealNumber(slipRef.getSealNumber());
		}
		if (slipRef.getTransactionDate() != null) {
			jackpotDetDTO.setTransactionDate(DateUtil.getMilliSeconds(DateHelper
					.getLocalTimeFromUTC(slipRef.getTransactionDate())));
			{
			log.debug("Date/Time..."
					+ DateUtil.getTime(jackpotDetDTO.getTransactionDate()));
			}
		}
		if (slipRef.getSiteId() != null) {
			jackpotDetDTO.setSiteId(slipRef.getSiteId());
			if(log.isDebugEnabled()) {
			log.debug("Site Id..." + jackpotDetDTO.getSiteId());
			}
		}
		if (slipRef.getMessageId() != null) {
			jackpotDetDTO.setMessageId(slipRef.getMessageId());
		}

		Set<Jackpot> jackpotSet = slipRef.getJackpot();

		for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
			Jackpot jackpot = (Jackpot) iter.next();
			if (jackpot.getJackpotTypeId() != null) {
				jackpotDetDTO.setJackpotTypeId(jackpot.getJackpotTypeId());
			}
			if (jackpot.getJackpotId() != null) {
				jackpotDetDTO.setJackpotId(jackpot.getJackpotId());
			}
			if (jackpot.getOriginalAmount() != null) {
				jackpotDetDTO.setOriginalAmount(jackpot.getOriginalAmount());
				if(log.isDebugEnabled()) {
				log.debug("Jackpot Org Amount..."
						+ jackpotDetDTO.getOriginalAmount());
				}
			}
			if (jackpot.getHpjpAmount() != null) {
				jackpotDetDTO.setHpjpAmount(jackpot.getHpjpAmount());
			}
			if (jackpot.getHpjpAmount() != null) {
				jackpotDetDTO.setHpjpAmount(jackpot.getHpjpAmount());
			}
			if (jackpot.getHpjpAmountRounded() != null) {
				jackpotDetDTO.setHpjpAmountRounded(jackpot.getHpjpAmountRounded());
			}
			if (jackpot.getNetAmount() != null) {
				jackpotDetDTO.setJackpotNetAmount(jackpot.getNetAmount());
			}
			if (jackpot.getTaxAmount() != null) {
				jackpotDetDTO.setTaxAmount(jackpot.getTaxAmount());
			}
			if (jackpot.getBlindAttempt() != null) {
				jackpotDetDTO.setBlindAttempt(jackpot.getBlindAttempt());
				if(log.isDebugEnabled()) {
				log.debug("Blind Att..." + jackpotDetDTO.getBlindAttempt());
				}
			}
			if (jackpot.getMachinePaidAmount() != null) {
				jackpotDetDTO.setMachinePaidAmount(jackpot.getMachinePaidAmount());
			}
			if (jackpot.getCoinsPlayed() != null) {
				jackpotDetDTO.setCoinsPlayed(jackpot.getCoinsPlayed());
			}
			if (jackpot.getPayline() != null) {
				jackpotDetDTO.setPayline(jackpot.getPayline());
			}
			if (jackpot.getAssociatedPlayerCard() != null) {
				jackpotDetDTO.setAssociatedPlayerCard(jackpot
						.getAssociatedPlayerCard());
			}
			if (jackpot.getPlayerCard() != null) {
				jackpotDetDTO.setPlayerCard(jackpot.getPlayerCard());
			}
			if (jackpot.getPlayerName() != null) {
				jackpotDetDTO.setPlayerName(jackpot.getPlayerName());
			}
			if (jackpot.getWinningCombination() != null) {
				jackpotDetDTO.setWinningComb(jackpot.getWinningCombination());
			}
			if (jackpot.getIsSlotOnline() != null) {
				if (jackpot.getIsSlotOnline() == 1) {
					jackpotDetDTO.setSlotOnline(true);
				} else if (jackpot.getIsSlotOnline() == 0) {
					jackpotDetDTO.setSlotOnline(false);
				}
			}
		}

		Set<SlipData> slipDataSet = slipRef.getSlipData();

		for (Iterator iter = slipDataSet.iterator(); iter.hasNext();) {

			SlipData slipData = (SlipData) iter.next();
			if (slipData.getShift() != null) {
				jackpotDetDTO.setShift(slipData.getShift());
			}
			if (slipData.getWindowNumber() != null) {
				jackpotDetDTO.setWindowNumber(slipData.getWindowNumber());
			}
			if (slipData.getKioskProcessed() != null) {
				jackpotDetDTO.setAssetConfNumberUsed(slipData.getKioskProcessed());
			}
			if (slipData.getActrLogin() != null) {
				jackpotDetDTO.setActorLogin(slipData.getActrLogin());
			}
			if (slipData.getAuthEmployeeId1() != null) {
				jackpotDetDTO.setAuthEmployeeId1(slipData.getAuthEmployeeId1());
			}
			if (slipData.getAuthEmployeeId2() != null) {
				jackpotDetDTO.setAuthEmployeeId2(slipData.getAuthEmployeeId2());
			}
		}

		Set<Transaction> transactionSet = slipRef.getTransaction();
		for (Iterator iter = transactionSet.iterator(); iter.hasNext();) {
			Transaction transaction = (Transaction) iter.next();
			if (transaction.getStatusFlagId() != null){
//					&& transaction.getStatusFlagId() != ILookupTableConstants.CARD_IN_STATUS_ID) {

				if (transaction.getStatusFlagId() != ILookupTableConstants.PROCESSED_STATUS_ID 
						|| transaction.getStatusFlagId() != ILookupTableConstants.CHANGE_STATUS_ID
						|| transaction.getStatusFlagId() != ILookupTableConstants.PRINTED_STATUS_ID) {
					if (transaction.getEmployeeId() != null) {
						jackpotDetDTO.setProcessEmployeeId(transaction
								.getEmployeeId());
						if(log.isDebugEnabled()) {
						log.debug("transaction - ProcessEmployeeId: "
								+ transaction.getEmployeeId());
						}
					}
					if (transaction.getEmployeeFirstName() != null
							&& transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setProcessEmployeeId(transaction
								.getEmployeeFirstName()
								+ " " + transaction.getEmployeeLastName());
					} else if (transaction.getEmployeeFirstName() != null) {
						jackpotDetDTO.setProcessEmployeeId(transaction
								.getEmployeeFirstName());
					} else if (transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setProcessEmployeeId(transaction
								.getEmployeeLastName());
					}
					if (transaction.getPrinterUsed() != null) {
						jackpotDetDTO.setPrinterUsed(transaction.getPrinterUsed());
					}
					if (transaction.getCreatedTs() != null) {
						jackpotDetDTO.setPrintDate(DateUtil.getMilliSeconds(DateHelper
								.getLocalTimeFromUTC(transaction.getCreatedTs())));
					}
				} else if (transaction.getStatusFlagId() != ILookupTableConstants.REPRINT_STATUS_ID) {
					if (transaction.getEmployeeId() != null) {
						jackpotDetDTO.setReprintEmployeeId(transaction
								.getEmployeeId());
						if(log.isDebugEnabled()) {
						log.debug("transaction - ReprintEmployeeId: "
								+ transaction.getEmployeeId());
						}
					}
					if (transaction.getEmployeeFirstName() != null
							&& transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setReprintEmployeeName(transaction
								.getEmployeeFirstName()
								+ " " + transaction.getEmployeeLastName());
					} else if (transaction.getEmployeeFirstName() != null) {
						jackpotDetDTO.setReprintEmployeeName(transaction
								.getEmployeeFirstName());
					} else if (transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setReprintEmployeeName(transaction
								.getEmployeeLastName());
					}
				} else if (transaction.getStatusFlagId() != ILookupTableConstants.VOID_STATUS_ID) {
					if (transaction.getEmployeeId() != null) {
						jackpotDetDTO.setVoidEmployeeId(transaction
								.getEmployeeId());
						if(log.isDebugEnabled()) {
						log.debug("transaction - ReprintEmployeeId: "
								+ transaction.getEmployeeId());
						}
					}
					if (transaction.getEmployeeFirstName() != null
							&& transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setVoidEmployeeName(transaction
								.getEmployeeFirstName()
								+ " " + transaction.getEmployeeLastName());
					} else if (transaction.getEmployeeFirstName() != null) {
						jackpotDetDTO.setVoidEmployeeName(transaction
								.getEmployeeFirstName());
					} else if (transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setVoidEmployeeName(transaction
								.getEmployeeLastName());
					}
				} else if (transaction.getStatusFlagId() == ILookupTableConstants.CARD_IN_STATUS_ID) {
					if (transaction.getEmployeeId() != null) {
						jackpotDetDTO.setSlotAttendantId(transaction
								.getEmployeeId());
						if(log.isDebugEnabled()) {
						log.debug("transaction - SlotAttendantId: "
								+ transaction.getEmployeeId());
						}
					}
					if (transaction.getEmployeeFirstName() != null) {
						jackpotDetDTO.setSlotAttendantFirstName(transaction
								.getEmployeeFirstName());
					}
					if (transaction.getEmployeeLastName() != null) {
						jackpotDetDTO.setSlotAttendantLastName(transaction
								.getEmployeeLastName());
					}
					if (transaction.getEmployeeCardNo() != null) {
						jackpotDetDTO.setEmployeeCard(transaction
								.getEmployeeCardNo());
					}
				} else if (transaction.getStatusFlagId() == ILookupTableConstants.PENDING_STATUS_ID
						|| transaction.getStatusFlagId() == ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID) {
					if (transaction.getResponseCode() != null) {
						jackpotDetDTO.setResponseCode(transaction
								.getResponseCode());
					}
					if (transaction.getMessageId() != null) {
						jackpotDetDTO
								.setTransMessageId(transaction.getMessageId());
					}
				}
			}
		}
		log.debug("*********************************************************************");
		return jackpotDetDTO;
	}
	
	//Fireball Progressive
	
	/**
	 * getProgressiveType - This method decides the progressive type 
	 * based on Progressive type in PROGRESSIVE.CONTROLLER
	 *
	 * @param acnfNumber
	 * @param siteId
	 * @return
	 */	
	public static short getProgressiveType(String acnfNumber, int siteId){
		String controllerType = ILookupTableConstants.EXTERNAL_CONTROLLER_TYPE;
		short progressiveType = ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE;
		try{
			
			IProgressiveBO progressiveBO  = (IProgressiveBO)ServiceLocator.fetchService(LOOKUP_SDS_PROGRSSIVE_BO, IS_LOCAL_LOOKUP);
			
			if(progressiveBO!=null){				
				controllerType = progressiveBO.getControllerByPool(acnfNumber, siteId);		
				if(log.isDebugEnabled()){
					log.debug("Controller Type:"+ controllerType);
				}
			}else{
				log.error("Progressive Engine Look up failed for BO local look up");
			}
		}catch(Exception e){
			log.error("Error while getting controller type from ProgressiveEngine", e);
		}
		if(controllerType == null || controllerType.trim().equalsIgnoreCase("")){
			if(log.isDebugEnabled()){
				log.debug("Controller Type is null:"+ controllerType);
			}
			controllerType = ILookupTableConstants.EXTERNAL_CONTROLLER_TYPE;
		}
		
		if (controllerType.equalsIgnoreCase(ILookupTableConstants.INTERNAL_CONTROLLER_TYPE)) {
			progressiveType = ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE;
		} else {
			progressiveType = ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE;
		}
		
		if(log.isDebugEnabled()){
			log.debug("Progressive Type:"+ progressiveType);
		}
		
		return progressiveType;
	}
	
	
	/**
	 * Method to get slip details DTO from jackpot DTO
	 * 
	 * @param jackpotDTO
	 * @return JPCashlessProcessInfoDTO
	 * @author vsubha
	 */
	public static JPCashlessProcessInfoDTO getSlipDetailsDTO(JackpotDTO jackpotDTO) {
		if(log.isInfoEnabled()) {
			log.info("Inside set fields from JackpotDTO to JPCashlessProcessInfoDTO");
		}
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();
		if (jackpotDTO != null) {
			jPCashlessProcessInfoDTO.setSequenceNumber(jackpotDTO.getSequenceNumber());
			jPCashlessProcessInfoDTO.setSiteNo(jackpotDTO.getSiteNo());
			jPCashlessProcessInfoDTO.setSiteId(jackpotDTO.getSiteId());
			jPCashlessProcessInfoDTO.setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
			jPCashlessProcessInfoDTO.setSlotLocation(jackpotDTO.getAssetConfigLocation());
			jPCashlessProcessInfoDTO.setJackpotTypeId(jackpotDTO.getJackpotTypeId());
			jPCashlessProcessInfoDTO.setStatusFlagId(jackpotDTO.getStatusFlagId());
			jPCashlessProcessInfoDTO.setProcessFlagId(jackpotDTO.getProcessFlagId());	
			jPCashlessProcessInfoDTO.setOriginalHPJPAmount(jackpotDTO.getOriginalAmount());
			jPCashlessProcessInfoDTO.setHpjpAmount(jackpotDTO.getHpjpAmount());
			jPCashlessProcessInfoDTO.setJackpotNetAmount(jackpotDTO.getJackpotNetAmount());
			jPCashlessProcessInfoDTO.setAccountNumber(jackpotDTO.getAccountNumber());
			jPCashlessProcessInfoDTO.setAccountType(jackpotDTO.getCashlessAccountType());
			jPCashlessProcessInfoDTO.setTransactionDate(jackpotDTO.getTransactionDate());
			if(jackpotDTO.isPostedSuccessfully()) {
				jPCashlessProcessInfoDTO.setProcessSuccessful(true);
			} else {
				jPCashlessProcessInfoDTO.setProcessSuccessful(false);
			}
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Method to get slip details DTO from Slip reference
	 * 
	 * @param jackpotDTO
	 * @return JPCashlessProcessInfoDTO
	 * @author vsubha
	 */
	@SuppressWarnings("rawtypes")
	public static JPCashlessProcessInfoDTO getSlipDetailsDTO(SlipReference slipReference) {
		if(log.isInfoEnabled()) {
			log.info("Inside set fields from SlipReference to JPCashlessProcessInfoDTO");
		}
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();
		if (slipReference != null) {
			jPCashlessProcessInfoDTO.setSequenceNumber(slipReference.getSequenceNumber());
			jPCashlessProcessInfoDTO.setSiteNo(slipReference.getSiteNo());
			jPCashlessProcessInfoDTO.setSiteId(slipReference.getSiteId());
			jPCashlessProcessInfoDTO.setAssetConfigNumber(slipReference.getAcnfNumber());	
			jPCashlessProcessInfoDTO.setSlotLocation(slipReference.getAcnfLocation());
			jPCashlessProcessInfoDTO.setStatusFlagId(slipReference.getStatusFlagId());
			jPCashlessProcessInfoDTO.setProcessFlagId(slipReference.getProcessFlagId());	
			jPCashlessProcessInfoDTO.setAccountNumber(slipReference.getAccountNumber());
			jPCashlessProcessInfoDTO.setAccountType(slipReference.getAccountType());
			jPCashlessProcessInfoDTO.setTransactionDate(DateHelper.getLocalTimeFromUTC(slipReference.getTransactionDate()));
			@SuppressWarnings("unchecked")
			Set<Jackpot> jackpotSet = slipReference.getJackpot();
			for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
				Jackpot jackpot = (Jackpot) iter.next();
				jPCashlessProcessInfoDTO.setHpjpAmount(jackpot.getHpjpAmount());
				jPCashlessProcessInfoDTO.setJackpotNetAmount(jackpot.getNetAmount());
				jPCashlessProcessInfoDTO.setJackpotTypeId(jackpot.getJackpotTypeId());
			}
		} 
		return jPCashlessProcessInfoDTO;
	}
	
	public static JPCashlessProcessInfoDTO getJackpotsProcessedDetails(List<JackpotDTO> jackpotDTOList) {
		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();
		if(jackpotDTOList != null && jackpotDTOList.size() > 0) {
			for(JackpotDTO jackpotDTO : jackpotDTOList) {
				jPCashlessProcessInfoDTO.setJackpotTotalAmount(jPCashlessProcessInfoDTO.getJackpotTotalAmount() 
						+ jackpotDTO.getJackpotNetAmount());
				jPCashlessProcessInfoDTO.setProcessSuccessful(jackpotDTO.isPostedSuccessfully());
				jPCashlessProcessInfoDTO.setAccountNumber(jackpotDTO.getAccountNumber());
			}
		}
		return jPCashlessProcessInfoDTO;
	}
	
	/**
	 * Gets the account access details from SDSAccessDTO
	 * 
	 * @param sdsAccessDTO
	 * @return CashlessAccountDTO
	 * @author vsubha
	 */
	public static CashlessAccountDTO getAccountDetails(SDSAccessDTO sdsAccessDTO) {
		if(log.isInfoEnabled()) {
			log.info("Inside set fields from SDSAccessDTO to CashlessAccountDTO");
		}
		CashlessAccountDTO cashlessAccountDTO = new CashlessAccountDTO();
		if(sdsAccessDTO != null) {
			cashlessAccountDTO.setAcntNo(sdsAccessDTO.getAxcsAcntNo());
			cashlessAccountDTO.setAcntAccessTypeId(sdsAccessDTO.getAxcsTypeId());
			if(cashlessAccountDTO.getAcntAccessTypeId() != null) {
				cashlessAccountDTO.setAcntAccessType(getECashAccountAccessType(cashlessAccountDTO.getAcntAccessTypeId()));
			}			
			if(sdsAccessDTO.getAccountDTOList() != null && sdsAccessDTO.getAccountDTOList().size() > 0) {
				cashlessAccountDTO.setAcntStateId(sdsAccessDTO.getAccountDTOList().get(0).getAcntStateId());
				cashlessAccountDTO.setPlayerCardNumber(sdsAccessDTO.getAccountDTOList().get(0).getAcntCmsPlayerId());
			}
			cashlessAccountDTO.setErrorCode(sdsAccessDTO.getErrorCode());
			cashlessAccountDTO.setErrorCodeDesc(sdsAccessDTO.getErrorCodeDesc());
			cashlessAccountDTO.setSuccess(sdsAccessDTO.isSuccess());
		} else if(log.isDebugEnabled()) {
			log.debug("NO SDS Account Access Details are returned");
		}
		return cashlessAccountDTO;
	}
	
	/**
	 * Method to get the ecash account type
	 * 
	 * @param cashlessAccountType
	 * @return String
	 * @author vsubha
	 */
	public static String getECashAccountAccessType(int cashlessAccountType) {
		String accountType = null;
		switch(cashlessAccountType) {
			case ILookupTableConstants.ACCOUNT_TYPE_CASHLESS_ID:
				// CASHLESS ACCOUNT TYPE
				accountType = ILookupTableConstants.ACCOUNT_TYPE_CARDLESS;
				break;
			case ILookupTableConstants.ACCOUNT_TYPE_SMART_CARD_ID:
				// SMART CARD ACCOUNT TYPE
				accountType = ILookupTableConstants.ACCOUNT_TYPE_SMART_CARD;
				break;				
			case ILookupTableConstants.ACCOUNT_TYPE_MAGSTRIPE_CARD_ID:
				//MAGSTRIPE CARD
				accountType = ILookupTableConstants.ACCOUNT_TYPE_MAGSTRIPE_CARD;
				break;
//			case 3:
//				// EACSH ACCOUNT TYPE
//				accountType = ILookupTableConstants.ACCOUNT_TYPE_ECASH;
//				break;			
//			case 4: 
//				// IBUTTON ACCOUNT TYPE
//				accountType = ILookupTableConstants.ACCOUNT_TYPE_IBUTTON;
//				break;
//			default:
//				// INVALID VALUE
//				accountType = "";
//				break;
		}
		return accountType;		
	}
	
	/**
	 * Gets the SDSmessage that has to be posted to Messaging Engine
	 * @param jackpotDTO
	 * @return SDSMessage
	 * @author vsubha
	 */
	public static SDSMessage getExceptionMessageDetails(JackpotDTO jackpotDTO, char exceptionCode) {
		SDSMessage sdsMessage = new SDSMessage();
		SystemGeneratedMessage slipPostedObj = new SystemGeneratedMessage();
		slipPostedObj.setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
		slipPostedObj.setDateTime(Calendar.getInstance());
		slipPostedObj.setExceptionCode(exceptionCode);
		slipPostedObj.setMessageId(MessageUtil.getMessageId());// NEW MSG ID IS REQUIRED FOR THE SYSTEM GEN XC NOT THE MSG ID FOR XC 10
		slipPostedObj.setEngineId(IAppConstants.ENGINE_ID);
		slipPostedObj.setSiteId(jackpotDTO.getSiteId());
		slipPostedObj.setJackpotId(jackpotDTO.getJackpotId().toUpperCase());
		slipPostedObj.setAlertNo(IAlertMessageConstants.JACKPOT_PRINTED);
		if (jackpotDTO.getAssetlineAddr() != null) {
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
		if (jackpotDTO.getAuthEmployeeId1() != null
				&& jackpotDTO.getAuthEmployeeId1().length() > 0) {
			slipPostedObj.setOverRide(true);
		}
		sdsMessage.setMessage(slipPostedObj);

		return sdsMessage;	
	}
	
	/**
	 * Gets the SDSMessage List that has to be posted to Messaging Engine
	 * @param jackpotDTO
	 * @return SDSMessage
	 * @author vsubha
	 */
	public static List<SDSMessage> getExceptionMessageDetails(List<JackpotDTO> jackpotDTOList, char exceptionCode) {
		List<SDSMessage> sdsMessageList = new ArrayList<SDSMessage>();
		
		if(jackpotDTOList != null && jackpotDTOList.size() > 0) {
			for(JackpotDTO jackpotDTO : jackpotDTOList) {
				sdsMessageList.add(getExceptionMessageDetails(jackpotDTO, exceptionCode));
			}			
		}
		return sdsMessageList;
	}
	
	/**
	 * Method to get Creditkey-off alert details
	 * @param pcObj
	 * @return
	 * @author vsubha
	 */
	public static JackpotDTO getAlertDetails(IHeader pcObj) {
		JackpotDTO jackpotDTO = new JackpotDTO();
		
		jackpotDTO.setAssetConfigNumber(((IJackpotToCreditMeter)pcObj).getAssetConfigNumber());
		jackpotDTO.setHpjpAmount(((IJackpotToCreditMeter)pcObj).getJackpotAmount());
		jackpotDTO.setTransactionDate(((IJackpotToCreditMeter)pcObj).getDateTime().getTime());
		jackpotDTO.setMessageId(((IJackpotToCreditMeter)pcObj).getMessageId());
		jackpotDTO.setJpId(((IJackpotToCreditMeter)pcObj).getJackpotId());
		jackpotDTO.setSiteId(((IJackpotToCreditMeter)pcObj).getSiteId());
		jackpotDTO.setAreaShortName(((IJackpotToCreditMeter)pcObj).getAssetInfo().getAreaName());
		return jackpotDTO;
	}
}
