/*****************************************************************************
 * $Id: AccountingUtil.java,v 1.14.3.0, 2011-09-12 18:36:53Z, Duenas, Samuel$
 * $Date: 9/12/2011 1:36:53 PM$
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.db.slip.ProcessSession;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.SlipInfoDTO;
import com.ballydev.sds.jackpot.keypad.KeypadUtil;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;

/**
 * Utility class to populate the SlipInfoDTO that should be posted to Accounting
 * @author dambereen
 *
 */
public class AccountingUtil {

	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);




	public static SlipInfoDTO populateSlipInfoDTOForKeypad(ProcessSession processSession, String currency) {

		log.info("Inside populateSlipInfoDTOForKeypad method");
		SlipInfoDTO slipInfoDTO = new SlipInfoDTO();
		try {

			slipInfoDTO.setAssetNumber(processSession.getAcnfNumber());
			slipInfoDTO.setAuditCodeId(processSession.getAuditCodeId());
			if (processSession.getAuditCodeDesc() != null && processSession.getLanguageSelectedHere() != null) {
				slipInfoDTO.setAuditCodeDesc(KeypadUtil.getLabelValue(processSession.getAuditCodeDesc(),
						processSession.getLanguageSelectedHere()));
			}
			if (processSession.getEmployeeId() != null) {
				slipInfoDTO.setAuthorizingEmployeeId(String.valueOf(processSession.getEmployeeId()));
			}
			slipInfoDTO.setCurrency(currency);
			if (log.isDebugEnabled()) {
				log.debug("jackpotDTO.getSlipReferenceId(): " + processSession.getSlipReferenceId());
			}
			if (processSession.getSlipReferenceId() != null
					&& processSession.getSlipReferenceId().longValue() != 0l) {
				slipInfoDTO.setExtTransId(Long.toString(processSession.getSlipReferenceId()));
				if (log.isDebugEnabled()) {
					log.debug("slipInfoDTO.getExtTransId(): " + slipInfoDTO.getExtTransId());
				}
			}

			if (processSession.getAuthEmployeeCardId() != null) {
				slipInfoDTO.setFirstAuthorizingEmployeeId(String.valueOf(processSession
						.getAuthEmployeeCardId()));
			}
			slipInfoDTO.setGmuDenomination(processSession.getGmuDenomination());
			slipInfoDTO.setHandPaidAmount(processSession.getJpHitAmount());
			slipInfoDTO.setHandPaidAmountRounded(processSession.getJpHitAmount());

			slipInfoDTO.setJackpotNetAmount(processSession.getJpHitAmount());
			slipInfoDTO.setJackpotTypeId((short) processSession.getJackpotTypeId());
			slipInfoDTO.setJackpotTypeDesc(processSession.getJackpotTypeDesc());// no
																				// need
																				// externalization
																				// for
																				// this.

			slipInfoDTO.setMachinePaidAmount(0l);
			slipInfoDTO.setOriginalAmount(processSession.getJpOriginalAmount());
			slipInfoDTO.setProcessFlagId((short) processSession.getJackpotProcessFlagId());
			if (processSession.getJackpotProcessDesc() != null
					&& processSession.getLanguageSelectedHere() != null) {
				slipInfoDTO.setProcessFlagDesc(KeypadUtil.getLabelValue(
						processSession.getJackpotProcessDesc(), processSession.getLanguageSelectedHere()));
			}
			slipInfoDTO.setSequenceNumber(0l);
			slipInfoDTO.setSlipId(processSession.getSlipIdUsed());
			slipInfoDTO.setSlipRevenueType((short) processSession.getJackpotProcessFlagId());

			slipInfoDTO.setSlipTypeId(processSession.getSlipTypeId());
			if (processSession.getSlipTypeDesc() != null && processSession.getLanguageSelectedHere() != null) {
				slipInfoDTO.setSlipTypeDesc(KeypadUtil.getLabelValue(processSession.getSlipTypeDesc(),
						processSession.getLanguageSelectedHere()));
			}
			slipInfoDTO.setStatusFlagId(processSession.getStatusFlagId());
			if (processSession.getStatusFlagDesc() != null
					&& processSession.getLanguageSelectedHere() != null) {
				slipInfoDTO.setStatusFlagDesc(KeypadUtil.getLabelValue(processSession.getStatusFlagDesc(),
						processSession.getLanguageSelectedHere()));
			}
			if (processSession.getUpdatedTs() != null) {
				slipInfoDTO.setTransDate(new Date(processSession.getUpdatedTs().getTime()));
			}
			if (processSession.getTransactionDate() != null) {
				slipInfoDTO.setJackpotHitDate(new Date(processSession.getTransactionDate()));
			}
			if (processSession.getEmployeeId() != null) {
				slipInfoDTO.setLoggedInEmployeeId(String.valueOf(processSession.getEmployeeId()));
				slipInfoDTO.setTransEmployeeId(String.valueOf(processSession.getEmployeeId()));
				slipInfoDTO.setSlipTransEmployeeId(String.valueOf(processSession.getEmployeeId()));
			}
			slipInfoDTO.setSiteId(processSession.getSiteId());
			slipInfoDTO.setSiteNo(processSession.getSiteNumberUsed());

			if (log.isInfoEnabled()) {
				log.info("SlipInfoDTO: " + slipInfoDTO.toString());
			}
		} catch (Exception e) {
			log.error("Exception in populateSlipInfoDTOForKeypad", e);
		}
		return slipInfoDTO;

	}

	public static SlipInfoDTO populateSlipInfoDTO(JackpotDTO jackpotDTO, String currency) {
		log.info("Inside populateSlipInfoDTO method");
		SlipInfoDTO slipInfoDTO = new SlipInfoDTO();
		try {

			slipInfoDTO.setAssetNumber(jackpotDTO.getAssetConfigNumber());
			slipInfoDTO.setAuditCodeId(jackpotDTO.getAuditCodeId());
			slipInfoDTO.setAuditCodeDesc(jackpotDTO.getAuditCodeDesc());
			slipInfoDTO.setAuthorizingEmployeeId(jackpotDTO.getSlotAttendantId());
			slipInfoDTO.setCurrency(currency);
			if (log.isDebugEnabled()) {
				log.debug("jackpotDTO.getSlipReferenceId(): " + jackpotDTO.getSlipReferenceId());
			}
			if (jackpotDTO.getSlipReferenceId() != 0) {
				slipInfoDTO.setExtTransId(Long.toString(jackpotDTO.getSlipReferenceId()));
				if (log.isDebugEnabled()) {
					log.debug("slipInfoDTO.getExtTransId(): " + slipInfoDTO.getExtTransId());
				}
			}
			slipInfoDTO.setFirstAuthorizingEmployeeId(jackpotDTO.getAuthEmployeeId1());
			slipInfoDTO.setGmuDenomination(jackpotDTO.getGmuDenom());
			slipInfoDTO.setHandPaidAmount(jackpotDTO.getHpjpAmount());
			slipInfoDTO.setHandPaidAmountRounded(jackpotDTO.getHpjpAmountRounded());
			if (jackpotDTO.getTransactionDate() != null) {
				slipInfoDTO.setJackpotHitDate(DateHelper.getUTCTimeFromLocal(jackpotDTO.getTransactionDate()
						.getTime()));
			}
			slipInfoDTO.setJackpotNetAmount(jackpotDTO.getJackpotNetAmount());
			slipInfoDTO.setJackpotTypeId(jackpotDTO.getJackpotTypeId());
			slipInfoDTO.setJackpotTypeDesc(jackpotDTO.getJackpotTypeDesc());
			slipInfoDTO.setLoggedInEmployeeId(jackpotDTO.getActorLogin());
			slipInfoDTO.setMachinePaidAmount(0l);
			slipInfoDTO.setOriginalAmount(jackpotDTO.getOriginalAmount());
			slipInfoDTO.setProcessFlagId(jackpotDTO.getProcessFlagId());
			slipInfoDTO.setProcessFlagDesc(jackpotDTO.getProcessFlagDesc());
			slipInfoDTO.setSecondAuthorizingEmployeeId(jackpotDTO.getAuthEmployeeId2());
			slipInfoDTO.setSequenceNumber(0l);
			slipInfoDTO.setSlipId(jackpotDTO.getSlipId());
			slipInfoDTO.setSlipRevenueType(jackpotDTO.getProcessFlagId());
			slipInfoDTO.setSlipTransEmployeeId(jackpotDTO.getPrintEmployeeLogin());
			slipInfoDTO.setSlipTypeId(jackpotDTO.getSlipTypeId());
			slipInfoDTO.setSlipTypeDesc(jackpotDTO.getSlipTypeDesc());
			slipInfoDTO.setStatusFlagId(jackpotDTO.getStatusFlagId());
			slipInfoDTO.setStatusFlagDesc(jackpotDTO.getStatusFlagDesc());
			if (jackpotDTO.getPrintDate() != null) {
				slipInfoDTO.setTransDate(jackpotDTO.getPrintDate());
			}
			slipInfoDTO.setTransEmployeeId(jackpotDTO.getPrintEmployeeLogin());
			slipInfoDTO.setSiteId(jackpotDTO.getSiteId());
			slipInfoDTO.setSiteNo(jackpotDTO.getSiteNo());
			if (log.isInfoEnabled()) {
				log.info("SlipInfoDTO: " + slipInfoDTO.toString());
			}
		} catch (Exception e) {
			log.error("Exception in postAccountingInfo", e);
		}
		return slipInfoDTO;

	}
	
	public static List<SlipInfoDTO> populateSlipInfoDTO(List<JackpotDTO> jackpotDTOList, String currency) {
		List<SlipInfoDTO> slipInfoDTOList = new ArrayList<SlipInfoDTO>();
		if(jackpotDTOList != null && jackpotDTOList.size() > 0) {
			for(JackpotDTO jackpotDTO: jackpotDTOList) {
				slipInfoDTOList.add(populateSlipInfoDTO(jackpotDTO, currency));
			}			
		}
		return slipInfoDTOList;
	}

}
