/*****************************************************************************
 * $Id: JackpotEventsDAO.java,v 1.72.1.2.1.1, 2011-07-28 07:01:47Z, SDS 12.3.3$
 * $Date: 7/28/2011 2:01:47 AM$
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
package com.ballydev.sds.jackpot.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ballydev.sds.common.jackpot.ICreditKeyOffBlock;
import com.ballydev.sds.common.jackpot.IHandPaidJackpot;
import com.ballydev.sds.common.jackpot.IJackpotClear;
import com.ballydev.sds.common.jackpot.IJackpotToCreditMeter;
import com.ballydev.sds.common.message.JackpotResetMessage;
import com.ballydev.sds.db.HibernateUtil;
import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ICommonConstants;
import com.ballydev.sds.jackpot.constants.IJackpotIds;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.db.slip.Jackpot;
import com.ballydev.sds.jackpot.db.slip.JackpotSequence;
import com.ballydev.sds.jackpot.db.slip.JackpotSlot;
import com.ballydev.sds.jackpot.db.slip.SlipReference;
import com.ballydev.sds.jackpot.db.slip.Transaction;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.keypad.PadderUtil;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.ConversionUtil;
import com.ballydev.sds.jackpot.util.DateUtil;
import com.ballydev.sds.jackpot.util.JackpotHelper;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;
import com.ballydev.sds.jackpot.util.MessageUtil;

/**
 * DAO class to handle the events (XC-10, XC-37, XC-30 and XC-30 Auth Blk,
 * XC-52) from the slot floor
 * 
 * @author dambereen
 * @version $Revision: 78$
 */
public class JackpotEventsDAO implements ILookupTableConstants {

	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * blind attempt value initialized to zero
	 */
	private static final short BLIND_ATTEMPT = 0;

	/**
	 * Method to create pending jackpot slips
	 * 
	 * @param handPaidJackpot
	 * @return JackpotDTO
	 * @throws HibernateException
	 * @throws Exception
	 *             if pending slip creation failed
	 */
	public static JackpotDTO createPendingJackpotSlips(IHandPaidJackpot handPaidJackpot,
			JackpotDTO jackpotDTO, boolean slotDoorOpenStatus) throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside create pending jackpot slip (XC 10)");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			jackpotDTO.setResponseCode(IAppConstants.XC10_POSITIVE_RESPONSE_CODE);
			jackpotDTO.setSendAlert(true);
			jackpotDTO.setPostedSuccessfully(true);
			long jpAmtToStoreInCents = 0;
			/*
			 * if(handPaidJackpot.getJackpotAmount()!=0 &&
			 * handPaidJackpot.getJackpotId
			 * ()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_149 &&
			 * handPaidJackpot.getJackpotId
			 * ()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150 &&
			 * (handPaidJackpot.getJackpotId
			 * ()>=IJackpotIds.JACKPOT_ID_PROG_START_112 &&
			 * handPaidJackpot.getJackpotId
			 * ()<=IJackpotIds.JACKPOT_ID_PROG_END_159)){
			 * 
			 * jpAmtToStoreInCents = handPaidJackpot.getJackpotAmount(); } else
			 * if(handPaidJackpot.getJackpotAmount()!=0){ jpAmtToStoreInCents =
			 * (handPaidJackpot.getJackpotAmount() /
			 * handPaidJackpot.getGmuDenom()); }
			 */

			jpAmtToStoreInCents = handPaidJackpot.getJackpotAmount();

			if (log.isInfoEnabled()) {
				log.info("Org amt in cents: " + jpAmtToStoreInCents);
			}

			/** Create a record in the JackpotSequence table */
			JackpotSequence jpSequence = new JackpotSequence();
			sessionObj.save(jpSequence);
			if (log.isDebugEnabled()) {
				log.debug("Sequence Number created by JackpotSequence : " + jpSequence.getJpseId());
			}

			/** Create a record in the SlipReference table */
			SlipReference slipRefererence = new SlipReference();

			slipRefererence.setId(jackpotDTO.getSlipPrimaryKey());
			slipRefererence.setTransactionId(jackpotDTO.getSlipPrimaryKey());
			slipRefererence.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
			String jackpotId = Integer.toHexString((int) handPaidJackpot.getJackpotId()).toUpperCase();

			if (!handPaidJackpot.getEmployeeCardNumber().matches("0+")) {
				slipRefererence.setStatusFlagId(MECHANICS_DELTA_STATUS_ID);
				if (log.isInfoEnabled()) {
					log.info("MECHANICS_DELTA_STATUS_ID");
				}
			} else if (slotDoorOpenStatus || jackpotDTO.isInvalidPendingJP()) {
				slipRefererence.setStatusFlagId(INVALID_STATUS_ID);
				jackpotDTO.setPostedSuccessfully(false);
				if (log.isInfoEnabled()) {
					log.info("INVALID JACKPOT - SLOT DOOR OPEN / AMT is 0 for VALID NON PROG JPID");
				}
				jackpotDTO.setResponseCode(IAppConstants.GEN_NEGATIVE_RESPONSE_CODE);
			} else if (handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
					&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
					&& (handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA 
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB 
							|| (handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 
									&& handPaidJackpot.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159))) {
				slipRefererence.setStatusFlagId(PENDING_STATUS_ID);
				if (log.isInfoEnabled()) {
					log.info("PENDING_STATUS_ID");
				}
			} else {
				slipRefererence.setStatusFlagId(INVALID_STATUS_ID);
				jackpotDTO.setPostedSuccessfully(false);
				if (log.isInfoEnabled()) {
					log.info("INVALID JACKPOT ID- B0 resp is set");
				}
				jackpotDTO.setResponseCode(IAppConstants.GEN_NEGATIVE_RESPONSE_CODE);
			}
			if (log.isDebugEnabled()) {
				log.debug("Slip Ref Id: " + slipRefererence.getId());
			}

			if (jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("yes")) {
				// Setting sequence number as combination number to make it
				// random for SA
				slipRefererence.setSequenceNumber(Long.parseLong(ConversionUtil.getRandomSequenceNumber()
						+ jpSequence.getJpseId()));
				if (log.isDebugEnabled()) {
					log.debug("Combination Sequence Number set to SlipReference: "
							+ slipRefererence.getSequenceNumber());
				}
			} else {
				slipRefererence.setSequenceNumber(jpSequence.getJpseId());
				if (log.isDebugEnabled()) {
					log.debug("Sequence Number set to Slipreference : " + slipRefererence.getSequenceNumber());
				}
			}

			jackpotDTO.setSequenceNumber(slipRefererence.getSequenceNumber());
			slipRefererence.setMessageId(handPaidJackpot.getMessageId());
			if (log.isDebugEnabled()) {
				log.debug("Slip Ref Seq(Combi) No: " + slipRefererence.getSequenceNumber());
				log.debug("Slip Ref Msg Id: " + slipRefererence.getMessageId());
				log.info("Trans Datetime: "+handPaidJackpot.getDateTime());
				log.info("Trans Datetime in UTC: "+DateHelper.getUTCTimeFromLocal(handPaidJackpot.getDateTime()
						.getTimeInMillis()));
			}
			slipRefererence.setTransactionDate(DateHelper.getUTCTimeFromLocal(handPaidJackpot.getDateTime()
					.getTimeInMillis()));
			slipRefererence.setGmuDenomination(new Long(handPaidJackpot.getGmuDenom()));
			slipRefererence.setSlotDenomination(jackpotDTO.getSlotDenomination());
			if (jackpotDTO.getSlotDescription() != null) {
				slipRefererence.setSlotDescription(jackpotDTO.getSlotDescription());
			}
			slipRefererence.setCreatedTs(DateHelper.getUTCTimeFromLocal(handPaidJackpot.getDateTime()
					.getTimeInMillis()));
			slipRefererence.setSiteId(handPaidJackpot.getSiteId());
			if (handPaidJackpot.getSiteId() != 0) {
				slipRefererence.setSiteNo(Integer.toString(handPaidJackpot.getSiteId()));
			}
			slipRefererence.setPostToAccounting(NOT_POSTED_TO_ACCOUNTING);
			slipRefererence.setAcnfNumber(handPaidJackpot.getAssetConfigNumber());
			slipRefererence.setAcnfLocation(jackpotDTO.getAssetConfigLocation().toUpperCase());
			slipRefererence.setSealNumber(jackpotDTO.getSealNumber());
			slipRefererence.setAreaShortName(jackpotDTO.getAreaShortName());
			slipRefererence.setAreaLongName(jackpotDTO.getAreaLongName());
			slipRefererence.setChangeFlag(IAppConstants.CHANGE_FLAG_DISABLED);
			// SETTING ECASH ACCOUNT TYPE AND ACCOUNT NUMBER
			slipRefererence.setAccountType(jackpotDTO.getCashlessAccountType());
			slipRefererence.setAccountNumber(jackpotDTO.getAccountNumber());
			slipRefererence.setCreatedTs(DateUtil.getTime(handPaidJackpot.getDateTime().getTimeInMillis()));
			sessionObj.save(slipRefererence);

			/** Create a record in the Jackpot table */
			Jackpot jackpot = new Jackpot();
			jackpot.setId(jackpotDTO.getSlipPrimaryKey());
			jackpot.setSlipReferenceId(slipRefererence.getId());
			jackpot.setJackpotId(jackpotId);
			jackpot.setIsSlotOnline(jackpotDTO.isSlotOnline() ? new Short("1") : new Short("0"));
			if (jackpot.getJackpotId().equalsIgnoreCase(IJackpotIds.JACKPOT_ID_252_FC_STR)) {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_REGULAR);
			} else if (jackpot.getJackpotId().equalsIgnoreCase(IJackpotIds.JACKPOT_ID_INVALID_186_BA_STR)) {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_PROMOTIONAL);
			} else if (jackpot.getJackpotId().equalsIgnoreCase(IJackpotIds.JACKPOT_ID_250_FA_STR)) {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_SYSTEM_GAME);
			} else if (jackpot.getJackpotId().equalsIgnoreCase(IJackpotIds.JACKPOT_ID_254_FE_STR)) {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_CANCEL_CREDIT);
			} else if (jackpot.getJackpotId().equalsIgnoreCase(IJackpotIds.JACKPOT_ID_251_FB_STR)) {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_MYSTERY);
				// Fireball Progressive
			} else if ((Integer.parseInt(jackpot.getJackpotId(), 16) >= 112 && Integer.parseInt(
					jackpot.getJackpotId(), 16) <= 159)) {
				jackpot.setJackpotTypeId(JackpotHelper.getProgressiveType(
						handPaidJackpot.getAssetConfigNumber(), handPaidJackpot.getSiteId()));
			} else {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_REGULAR);
			}
			if (handPaidJackpot.getJackpotAmount() == 0
					&& (handPaidJackpot.getJackpotId() == 252 || handPaidJackpot.getJackpotId() == 253
							|| handPaidJackpot.getJackpotId() == 254 || handPaidJackpot.getJackpotId() == 250)) {
				if (log.isInfoEnabled()) {
					log.info("INVALID JACKPOT AMOUNT OF ZERO- so B0 resp is set");
				}
				jackpotDTO.setResponseCode(IAppConstants.GEN_NEGATIVE_RESPONSE_CODE);
				// jackpotDTO.setPostedSuccessfully(false);
			}
			jackpot.setOriginalAmount(jpAmtToStoreInCents);
			jackpot.setAssociatedPlayerCard(jackpotDTO.getAssociatedPlayerCard());
			jackpot.setBlindAttempt(BLIND_ATTEMPT);
			jackpot.setGeneratedBy(JpGeneratedBy.SLOT.getValue());//FOR CONTROLLER CREATED JPS DIFFERENTIATION
			jackpot.setCreatedTs(DateHelper.getUTCTimeFromLocal(handPaidJackpot.getDateTime()
					.getTimeInMillis()));
			sessionObj.save(jackpot);

			/** Create a record in TRANSACTION table */
			Transaction transaction = new Transaction();
			transaction.setId(jackpotDTO.getSlipPrimaryKey());
			transaction.setSlipReferenceId(slipRefererence.getId());
			if (!handPaidJackpot.getEmployeeCardNumber().matches("0+")) {
				transaction.setStatusFlagId(MECHANICS_DELTA_STATUS_ID);
			} else if (slotDoorOpenStatus || jackpotDTO.isInvalidPendingJP()) {
				transaction.setStatusFlagId(INVALID_STATUS_ID);
			} else if (handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
					&& handPaidJackpot.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
					&& (handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB
							|| handPaidJackpot.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA 
							|| (handPaidJackpot.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 
									&& handPaidJackpot.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159))) {
				transaction.setStatusFlagId(PENDING_STATUS_ID);
			} else {
				transaction.setStatusFlagId(INVALID_STATUS_ID);
			}
			transaction.setResponseCode(jackpotDTO.getResponseCode());
			transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
			transaction.setMessageId(handPaidJackpot.getMessageId());
			transaction.setExceptionCodeId(EXCEPTION_HAND_PAID_JP);
			transaction.setGmuCoinInMeter(handPaidJackpot.getBets());
			transaction.setLastCoinIn(new Long(handPaidJackpot.getLastBet()));
			if (slotDoorOpenStatus) {
				transaction.setDoorStatus(ICommonConstants.SLOT_DOOR_OPEN);
			} else {
				transaction.setDoorStatus(ICommonConstants.SLOT_DOOR_CLOSED);
			}
			transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(handPaidJackpot.getDateTime()
					.getTimeInMillis()));
			sessionObj.save(transaction);

			if (log.isDebugEnabled()) {
				log.debug("*************************************************************************************");
			}
			if (log.isInfoEnabled()) {
				log.info("Jackpot pending slip created successfully with the sequence no : "
						+ slipRefererence.getSequenceNumber());
			}
			if (log.isDebugEnabled()) {
				log.debug("*************************************************************************************");
			}

		} catch (HibernateException e) {
			jackpotDTO.setSendAlert(false);
			log.error("HibernateException in createPendingJackpotSlips", e);
			log.warn("HibernateException in createPendingJackpotSlips", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			jackpotDTO.setSendAlert(false);
			log.error("Exception in createPendingJackpotSlips", e2);
			log.warn("Exception in createPendingJackpotSlips", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to create Jackpot to Credit meter slips
	 * 
	 * @param jackpotToCreditMeter
	 * @return JackpotDTO
	 * @throws HibernateException
	 * @throws Exception
	 *             if jackpot to credit meter slip creation failed
	 */
	public static JackpotDTO createJackpotToCreditMeterSlips(IJackpotToCreditMeter jackpotToCreditMeter,
			JackpotDTO jackpotDTO) throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside create Jackpot to Credit meter (XC 30) slip");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			String queryString, jackpotId = null;
			long jackpotAmount = 0, sequenceNo = 0;
			Timestamp transactionDate = null;
			long slipRefId = 0;
			short statusFlag = 0, auditCode = 0;
			String empId = null, empFirstName = null, empLastName = null;

			/**
			 * Query to get the slip ref id based on the max transaction date,
			 * jackpot slip type, processFlg is not Manual and slot no
			 */

			queryString = "select slipRef.id, slipRef.statusFlagId, slipRef.transactionDate, jackpot.jackpotId,"
				+ "jackpot.originalAmount , slipRef.sequenceNumber "
				+ " from com.ballydev.sds.jackpot.db.slip.SlipReference slipRef"
				+ " , com.ballydev.sds.jackpot.db.slip.Jackpot as jackpot"
				+ " where jackpot.slipReferenceId = slipRef.id and  slipRef.transactionDate " 
				+ "= (select max(slipRef1.transactionDate) from com.ballydev.sds.jackpot.db.slip.SlipReference slipRef1 "
				+ "where slipRef1.slipTypeId="
				+ JACKPOT_SLIP_TYPE_ID
				+ " and (slipRef1.processFlagId is null or slipRef1.processFlagId!="
				+ MANUAL_PROCESS_FLAG
				+ ") "
				+ " and slipRef1.acnfNumber = '"
				+ jackpotToCreditMeter.getAssetConfigNumber()
				+ "' and slipRef1.siteId = "+jackpotToCreditMeter.getSiteId()+") and slipRef.slipTypeId="
				+ JACKPOT_SLIP_TYPE_ID // extra chk for jp slip type is
										// incase there is the same
										// transaction date for a different
										// slip type
				+ " and slipRef.acnfNumber = '" + jackpotToCreditMeter.getAssetConfigNumber() 
				+ "' and slipRef.siteId = "+jackpotToCreditMeter.getSiteId();

			Query query = sessionObj.createQuery(queryString);
			if (log.isDebugEnabled()) {
				log.debug("Query String: " + query.getQueryString());
			}

			ArrayList<Object[]> queryRtnList = new ArrayList<Object[]>();

			queryRtnList = (ArrayList) query.list();
			if (queryRtnList != null && queryRtnList.size() > 0) {

				if (log.isDebugEnabled()) {
					log.debug("Size: " + queryRtnList.size());
					log.debug("queryRtnList.toString(): " + queryRtnList.get(0)[0]);
				}

				slipRefId = (Long) queryRtnList.get(0)[0];
				statusFlag = (Short) queryRtnList.get(0)[1];
				transactionDate = (Timestamp) queryRtnList.get(0)[2];
				jackpotId = queryRtnList.get(0)[3].toString();
				jackpotAmount = (Long) queryRtnList.get(0)[4];
				sequenceNo = (Long) queryRtnList.get(0)[5];

				if (log.isDebugEnabled()) {
					log.debug("slipRefId: " + slipRefId);
					log.debug("statusFlag: " + statusFlag);
					log.debug("jackpotId: " + jackpotId);
					log.debug("jackpotAmount: " + jackpotAmount);
					log.debug("seq no: " + sequenceNo);
				}
				
				if (statusFlag != PENDING_STATUS_ID){
					log.info("*******The status of the latest transaction is not in pending state , ignore the XC 30 event*******");
					jackpotDTO.setPostedSuccessfully(false);
					jackpotDTO.setSendAlert(false);
					return jackpotDTO;
				}

				/**
				 * SELECT QUERY TO GET THE SLOT ATTENDANT INFO FROM THE
				 * TRANSACTION TABLE
				 */
				String transactionQueryStr = "select employeeId,employeeFirstName, employeeLastName from com.ballydev.sds.jackpot.db.slip.Transaction trans "
						+ "where trans.slipReferenceId = " + slipRefId + "and trans.statusFlagId=4007";

				Query transQuery = sessionObj.createQuery(transactionQueryStr);
				ArrayList<Object[]> transQueryRtnList = new ArrayList<Object[]>();
				transQueryRtnList = (ArrayList<Object[]>) transQuery.list();

				if (log.isDebugEnabled()) {
					log.debug("transQueryRtnList: " + transQueryRtnList);
				}

				if (transQueryRtnList != null && transQueryRtnList.size() > 0) {
					empId = transQueryRtnList.get(0)[0].toString();
					empFirstName = transQueryRtnList.get(0)[1].toString();
					empLastName = transQueryRtnList.get(0)[2].toString();
				}

				long jackpotCredits = 0;
				/*
				 * if(jackpotToCreditMeter.getJackpotAmount()!=0){
				 * jackpotCredits = jackpotToCreditMeter.getJackpotAmount() /
				 * jackpotToCreditMeter.getGmuDenom(); }
				 */

				jackpotCredits = jackpotToCreditMeter.getJackpotAmount();

				if (log.isDebugEnabled()) {
					log.debug("Slip Ref transaction Date: "
							+ DateUtil.getStringDate(DateUtil.getMilliSeconds(DateHelper
									.getLocalTimeFromUTC(transactionDate))));
					log.debug("XC 30 Event Date: "
							+ DateUtil.getStringDate(jackpotToCreditMeter.getDateTime().getTimeInMillis()));

					log.debug("SPLR Status flag id: " + statusFlag);
				}
				if (log.isInfoEnabled()) {
					log.info(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
							.getTimeInMillis()));
				}

				if (jackpotToCreditMeter.getEmployeeCardNumber().matches("0+")) {
					auditCode = AUDIT_CODE_CANCELLED_BY_UNKNOWN_EMP;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_CANCELLED_BY_UNKNOWN_EMP--- Emp card is not used");
					}
				} else if (empId == null) {
					auditCode = AUDIT_CODE_CANCELLED_BY_UNAUTHORIZED_EMP;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_CANCELLED_BY_UNAUTHORIZED_EMP--- No emp authorized the credit key off");
					}
				} else if (statusFlag == PROCESSED_STATUS_ID || statusFlag == VOID_STATUS_ID
						|| statusFlag == CHANGE_STATUS_ID || statusFlag == PRINTED_STATUS_ID) {
					if (log.isDebugEnabled()) {
						log.debug("PROCESSED/VOID/CHANGE/PRINTED RECORD");
					}
					auditCode = AUDIT_CODE_SLIP_WAS_PRINTED;// send a nack
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_SLIP_WAS_PRINTED");
					}
				} else if (statusFlag == PENDING_STATUS_ID
						&& (Integer.toHexString((int) jackpotToCreditMeter.getJackpotId()).toUpperCase()
								.equalsIgnoreCase("FA") || Integer
								.toHexString((int) jackpotToCreditMeter.getJackpotId()).toUpperCase()
								.equalsIgnoreCase("BA"))) {
					if (log.isDebugEnabled()) {
						log.debug("PENDING RECORD");
					}
					auditCode = AUDIT_CODE_INVALID_JP_RECORD;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_INVALID_JP_RECORD");
					}
				} else if (!DateUtil.getStringDate(
						DateUtil.getMilliSeconds(DateHelper.getLocalTimeFromUTC(transactionDate)))
						.equalsIgnoreCase(
								DateUtil.getStringDate(jackpotToCreditMeter.getDateTime().getTimeInMillis()))) {
					if (log.isDebugEnabled()) {
						log.debug("GAME DATE DOES NOT MATCH");
					}
					auditCode = AUDIT_CODE_GAME_DATE_NOT_MATCHING_JP_REC;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_GAME_DATE_NOT_MATCHING_JP_REC");
					}
				} else if (statusFlag == PENDING_STATUS_ID && (jackpotAmount != (jackpotCredits))) {
					if (log.isDebugEnabled()) {
						log.debug("PENDING RECORD");
					}
					auditCode = AUDIT_CODE_AMT_NOT_MATCHING;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_AMT_NOT_MATCHING");
					}
				} else if (statusFlag == PENDING_STATUS_ID
						&& !(jackpotId.equalsIgnoreCase(Integer.toHexString(
								(int) jackpotToCreditMeter.getJackpotId()).toUpperCase()))) {
					if (log.isDebugEnabled()) {
						log.debug("PENDING RECORD");
					}
					auditCode = AUDIT_CODE_JPID_NOT_MATCHING;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_JPID_NOT_MATCHING");
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("last else condition rec");
					}
					auditCode = AUDIT_CODE_SUCCESS;
					if (log.isInfoEnabled()) {
						log.info("AUDIT_CODE_SUCCESS");
					}
				}

				/** UPDATE QUERY FOR SLIP REFERENCE TABLE */
				String updateQueryStr = "update com.ballydev.sds.jackpot.db.slip.SlipReference slipRef"
						+ " set slipRef.statusFlagId= "
						+ CREDIT_KEY_OFF_STATUS_ID
						+ ", slipRef.transactionId= "
						+ jackpotDTO.getSlipPrimaryKey()
						+ ", slipRef.auditCodeId= "
						+ auditCode
						+ ", slipRef.messageId= "
						+ jackpotToCreditMeter.getMessageId()
						+ ", slipRef.updatedTs= '"
						+ DateHelper
								.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime().getTimeInMillis())
						+ "'" + " where slipRef.id= " + slipRefId;

				Query updateQuery = sessionObj.createQuery(updateQueryStr);
				updateQuery.executeUpdate();

				if (log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Jackpot To Credit Meter slip status updated successfully for the sequence no : "
							+ sequenceNo + "for XC 30");
				}
				if (log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
				jackpotDTO.setPostedSuccessfully(true);
				jackpotDTO.setSendAlert(true);
				// SEQ NO IS SET ONLY FOR A XC30 WHERE THERE IS A PRECEEDING XC
				// 10 (PENDING) JP, USED FOR REMOVING ALERT 92 FROM CACHE
				jackpotDTO.setSequenceNumber(sequenceNo);

				/** Create a record in TRANSACTION table */
				Transaction transaction = new Transaction();
				transaction.setId(jackpotDTO.getSlipPrimaryKey());
				transaction.setSlipReferenceId(slipRefId);
				transaction.setStatusFlagId(CREDIT_KEY_OFF_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setResponseCode(IAppConstants.GEN_POSITIVE_RESPONSE_CODE);
				transaction.setMessageId(jackpotToCreditMeter.getMessageId());
				transaction.setExceptionCodeId(EXCEPTION_JP_TO_CREDIT_MTR);
				transaction.setGmuCoinInMeter(jackpotToCreditMeter.getBets());
				transaction.setLastCoinIn(new Long(jackpotToCreditMeter.getLastBet()));
				transaction.setEmployeeCardNo(jackpotToCreditMeter.getEmployeeCardNumber());
				transaction.setEmployeeId(empId);
				transaction.setEmployeeFirstName(empFirstName);
				transaction.setEmployeeLastName(empLastName);
				transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
						.getTimeInMillis()));
				sessionObj.save(transaction);
			} else {
				if (log.isInfoEnabled()) {
					log.info("For no pending matching record for an XC 30");
				}

				/** Create a record in the JackpotSequence table */
				JackpotSequence jpSequence = new JackpotSequence();
				sessionObj.save(jpSequence);
				if (log.isInfoEnabled()) {
					log.info("Seq no created in JackpotSequence: " + jpSequence.getJpseId());
				}

				long jpAmtToStoreInCents = 0;

				/*
				 * if(jackpotToCreditMeter.getJackpotAmount()!=0 &&
				 * jackpotToCreditMeter
				 * .getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_149 &&
				 * jackpotToCreditMeter
				 * .getJackpotId()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150 &&
				 * (jackpotToCreditMeter
				 * .getJackpotId()>=IJackpotIds.JACKPOT_ID_PROG_START_112 &&
				 * jackpotToCreditMeter
				 * .getJackpotId()<=IJackpotIds.JACKPOT_ID_PROG_END_159)){
				 * 
				 * jpAmtToStoreInCents =
				 * jackpotToCreditMeter.getJackpotAmount(); } else
				 * if(jackpotToCreditMeter.getJackpotAmount()!=0){
				 * jpAmtToStoreInCents =
				 * (jackpotToCreditMeter.getJackpotAmount() /
				 * jackpotToCreditMeter.getGmuDenom()); }
				 */
				jpAmtToStoreInCents = jackpotToCreditMeter.getJackpotAmount();

				if (log.isInfoEnabled()) {
					log.info("Org amt in cents: " + jpAmtToStoreInCents);
				}

				/** Create a record in the SlipReference table */

				SlipReference slipReference = new SlipReference();
				slipReference.setId(jackpotDTO.getSlipPrimaryKey());
				slipReference.setTransactionId(jackpotDTO.getSlipPrimaryKey());
				slipReference.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
				String jackpotIdNew = Integer.toHexString((int) jackpotToCreditMeter.getJackpotId())
						.toUpperCase();
				slipReference.setStatusFlagId(CREDIT_KEY_OFF_STATUS_ID);
				slipReference.setAuditCodeId(AUDIT_CODE_NO_MATCHING_JP_RECORD);
				if (jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("yes")) {
					// Setting sequence number as combination number to make it
					// random for SA
					slipReference.setSequenceNumber(Long.parseLong(ConversionUtil.getRandomSequenceNumber()
							+ jpSequence.getJpseId()));
					if (log.isDebugEnabled()) {
						log.debug("Combination Sequence Number set to SlipReference: "
								+ slipReference.getSequenceNumber());
					}
				} else {
					slipReference.setSequenceNumber(jpSequence.getJpseId());
					if (log.isDebugEnabled()) {
						log.debug("Sequence Number set to SlipReference: "
								+ slipReference.getSequenceNumber());
					}
				}
				slipReference.setMessageId(jackpotToCreditMeter.getMessageId());
				slipReference.setTransactionDate(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter
						.getDateTime().getTimeInMillis()));
				slipReference.setGmuDenomination(new Long(jackpotToCreditMeter.getGmuDenom()));
				if (jackpotDTO.getSlotDescription() != null) {
					slipReference.setSlotDescription(jackpotDTO.getSlotDescription());
				}
				slipReference.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
						.getTimeInMillis()));
				slipReference.setSiteId(jackpotToCreditMeter.getSiteId());
				slipReference.setAcnfNumber(jackpotToCreditMeter.getAssetConfigNumber());
				slipReference.setAcnfLocation(jackpotDTO.getAssetConfigLocation().toUpperCase());
				slipReference.setSealNumber(jackpotDTO.getSealNumber());
				slipReference.setAreaShortName(jackpotDTO.getAreaShortName());
				slipReference.setAreaLongName(jackpotDTO.getAreaLongName());
				slipReference.setChangeFlag(IAppConstants.CHANGE_FLAG_DISABLED);
				slipReference.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
						.getTimeInMillis()));
				sessionObj.save(slipReference);

				/** Create a record in the Jackpot table */
				Jackpot jackpot = new Jackpot();
				jackpot.setId(jackpotDTO.getSlipPrimaryKey());
				jackpot.setSlipReferenceId(slipReference.getId());
				jackpot.setJackpotId(jackpotIdNew);
				jackpot.setIsSlotOnline(jackpotDTO.isSlotOnline() ? new Short("1") : new Short("0"));

				if (jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC
						|| jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD) {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_REGULAR);
				} else if (jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_INVALID_186_BA) {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_PROMOTIONAL);
				} else if (jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA) {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_SYSTEM_GAME);
				} else if (jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE) {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_CANCEL_CREDIT);
				} else if(jackpotToCreditMeter.getJackpotId() == IJackpotIds.JACKPOT_ID_251_FB) {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_MYSTERY);
				} else if (jackpotToCreditMeter.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
						&& jackpotToCreditMeter.getJackpotId() != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
						&& (jackpotToCreditMeter.getJackpotId() >= IJackpotIds.JACKPOT_ID_PROG_START_112 && jackpotToCreditMeter
								.getJackpotId() <= IJackpotIds.JACKPOT_ID_PROG_END_159)) {
					// Fireball Progressive
					jackpot.setJackpotTypeId(JackpotHelper.getProgressiveType(
							jackpotToCreditMeter.getAssetConfigNumber(), jackpotToCreditMeter.getSiteId()));
				}/*
				 * else if(jackpotToCreditMeter.getJackpotId()!=IJackpotIds.
				 * JACKPOT_ID_INVALID_PROG_149 &&
				 * jackpotToCreditMeter.getJackpotId
				 * ()!=IJackpotIds.JACKPOT_ID_INVALID_PROG_150 &&
				 * (jackpotToCreditMeter
				 * .getJackpotId()>=IJackpotIds.JACKPOT_ID_PROG_START_112 &&
				 * jackpotToCreditMeter
				 * .getJackpotId()<=IJackpotIds.JACKPOT_ID_PROG_END_159) &&
				 * jackpotToCreditMeter.getJackpotAmount()==0){
				 * jackpot.setJackpotTypeId(JACKPOT_TYPE_lINKED_PROGRESSIVE); }
				 */
				else {
					jackpot.setJackpotTypeId(JACKPOT_TYPE_REGULAR);
				}
				jackpot.setOriginalAmount(jpAmtToStoreInCents);
				jackpot.setBlindAttempt(BLIND_ATTEMPT);
				jackpot.setGeneratedBy(JpGeneratedBy.SLOT.getValue());//FOR CONTROLLER CREATED JPS DIFFERENTIATION
				jackpot.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
						.getTimeInMillis()));
				sessionObj.save(jackpot);

				/** Create a record in TRANSACTION table */
				Transaction transaction = new Transaction();
				transaction.setId(jackpotDTO.getSlipPrimaryKey());
				transaction.setSlipReferenceId(slipReference.getId());
				transaction.setStatusFlagId(CREDIT_KEY_OFF_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setResponseCode(IAppConstants.GEN_POSITIVE_RESPONSE_CODE);
				transaction.setMessageId(jackpotToCreditMeter.getMessageId());
				transaction.setExceptionCodeId(EXCEPTION_JP_TO_CREDIT_MTR);
				transaction.setGmuCoinInMeter(jackpotToCreditMeter.getBets());
				transaction.setLastCoinIn(new Long(jackpotToCreditMeter.getLastBet()));
				transaction.setEmployeeCardNo(jackpotToCreditMeter.getEmployeeCardNumber());
				transaction.setEmployeeId(empId);
				transaction.setEmployeeFirstName(empFirstName);
				transaction.setEmployeeLastName(empLastName);
				transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotToCreditMeter.getDateTime()
						.getTimeInMillis()));
				sessionObj.save(transaction);

				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Jackpot Credit Key Off Slip created successfully with the sequence no : "
							+ slipReference.getSequenceNumber() + " For no matching pending record for XC 30");
				}
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************************************************************************");
				}
				jackpotDTO.setPostedSuccessfully(false);
			}

		} catch (HibernateException e) {
			jackpotDTO.setSendAlert(false);
			log.error("HibernateException in createJackpotToCreditMeterSlips", e);
			log.warn("HibernateException in createJackpotToCreditMeterSlips", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			jackpotDTO.setSendAlert(false);
			log.error("Exception in createJackpotToCreditMeterSlips", e2);
			log.warn("Exception in createJackpotToCreditMeterSlips", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to log the transaction on a Jackpot Clear Event (XC 52)
	 * 
	 * @param jackpotClear
	 * @return JackpotDTO
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO jackpotClearEvent(IJackpotClear jackpotClear, long slipPrimaryKey) throws JackpotDAOException {
		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside jackpotClearEvent (XC 52)");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			ArrayList statusFlagList = new ArrayList();
			statusFlagList.add(PENDING_STATUS_ID);
			statusFlagList.add(CREDIT_KEY_OFF_STATUS_ID);
			statusFlagList.add(PROCESSED_STATUS_ID);
			statusFlagList.add(CHANGE_STATUS_ID);
			statusFlagList.add(VOID_STATUS_ID);
			statusFlagList.add(REPRINT_STATUS_ID);
			statusFlagList.add(INVALID_STATUS_ID);
			statusFlagList.add(MECHANICS_DELTA_STATUS_ID);
			statusFlagList.add(PRINTED_STATUS_ID); // INCLUDED FOR CASHIER DESK
													// FLOW

			/** SET PROJECTIONS FOR SLIP_REF ID AND SEQUENCE NO */
			Projection projSlipRefId = Projections.property("id");
			Projection projSeqNo = Projections.property("sequenceNumber");

			ProjectionList projList = Projections.projectionList();
			projList.add(projSlipRefId);
			projList.add(projSeqNo);

			/** SET THE CRITERIA BELOW */
			Criterion crtAcnf_No = Restrictions.eq("acnfNumber", jackpotClear.getAssetConfigNumber());
			Criterion crtSlipType = Restrictions.and(crtAcnf_No,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtStatusFlag = Restrictions.and(crtSlipType,
					Restrictions.in("statusFlagId", statusFlagList));
			Criterion crtSiteId = Restrictions.and(crtStatusFlag,
					Restrictions.eq("siteId", jackpotClear.getSiteId()));

			Criterion crtProcFlgNotMan = Restrictions.ne("processFlagId", MANUAL_PROCESS_FLAG);
			
			Criterion crtProcFlgIsNull = Restrictions.or(crtProcFlgNotMan,
					Restrictions.isNull("processFlagId"));
			
			Criterion crtFinal = Restrictions.and(crtSiteId,(crtProcFlgIsNull));

			Order orderByTransDate = Order.desc("transactionDate");

			Criteria jpClearCriteria = sessionObj.createCriteria(SlipReference.class).add(crtFinal)
					.addOrder(orderByTransDate).setProjection(projSlipRefId)
					.setProjection(projList).setMaxResults(1);

			if (log.isDebugEnabled()) {
				log.debug("jpClearCriteria : " + jpClearCriteria);
			}
			Object[] objectArray = (Object[]) jpClearCriteria.uniqueResult();

			if (objectArray != null) {
				if (log.isDebugEnabled()) {
					log.debug("Slpr_id : " + objectArray[0]);
					log.debug("Seq no : " + objectArray[1]);
				}
				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId((Long) objectArray[0]);
				transaction.setStatusFlagId(JP_CLEAR_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setResponseCode(IAppConstants.GEN_POSITIVE_RESPONSE_CODE);
				transaction.setMessageId(jackpotClear.getMessageId());
				transaction.setExceptionCodeId(EXCEPTION_JP_CLEAR);
				transaction.setGmuCoinInMeter(jackpotClear.getBets());
				transaction.setLastCoinIn(new Long(jackpotClear.getLastBet()));
				// transaction.setDoorStatus(doorStatus);
				transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(jackpotClear.getDateTime()
						.getTimeInMillis()));
				sessionObj.save(transaction);
				if(log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The jackpot has been cleared for the slpr_id: " + objectArray[0]
							+ " and seqNo: " + objectArray[1] + "for XC 52");
				}
				if(log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
				jackpotDTO.setPostedSuccessfully(true);
			} else {
				jackpotDTO.setPostedSuccessfully(false);
				if(log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
				if(log.isInfoEnabled()) {
					log.info("There is jackpot NO slip to clear, hence sending a B0 response");
				}
				if(log.isDebugEnabled()) {
					log.debug("*******************************************************************************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in jackpotClearEvent", e);
			log.warn("HibernateException in jackpotClearEvent", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in jackpotClearEvent", e2);
			log.warn("Exception in jackpotClearEvent", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to check if event is a duplicate based one the Message id and Site
	 * Id passed
	 * 
	 * @param messageId
	 * @param siteId
	 * @param exceptionStr
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	public static JackpotDTO eventDuplicateMsgCheckGetResponse(long messageId) throws JackpotDAOException {

		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside eventDuplicateMsgCheckGetResponse");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			if (log.isInfoEnabled()) {
				log.info("Duplicate Msg Id: " + messageId);
			}

			ArrayList<Short> statusFlagLst = new ArrayList<Short>();
			statusFlagLst.add(PENDING_STATUS_ID);
			statusFlagLst.add(INVALID_STATUS_ID);
			statusFlagLst.add(MECHANICS_DELTA_STATUS_ID);
			statusFlagLst.add(JP_CLEAR_STATUS_ID);
			statusFlagLst.add(CREDIT_KEY_OFF_STATUS_ID);

			Projection prjRespCode = Projections.property("responseCode");
			Criterion crtMessageId = Restrictions.eq("messageId", messageId);
			Criterion crtStatusFlagId = Restrictions.and(crtMessageId,
					Restrictions.in("statusFlagId", statusFlagLst));
			Criteria transCriteria = sessionObj.createCriteria(Transaction.class).add(crtStatusFlagId)
					.setProjection(prjRespCode);

			List<String> transList = transCriteria.list();
			if (transList != null && transList.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("Trans size: " + transList.size());
				}
				for (int i = 0; i < transList.size(); i++) {
					if (log.isDebugEnabled()) {
						log.debug("Transaction is not null, there is a duplicate");
					}

					if (transList.get(i) != null) {
						jackpotDTO.setResponseCode(transList.get(i));
						if (log.isInfoEnabled()) {
							log.info("Duplicate Response returned from Transaction : "
									+ jackpotDTO.getResponseCode());
						}
					}
					jackpotDTO.setDuplicateMsg(true);
					break;
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Duplicate pending slip present for msg id: " + messageId);
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("***********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("No duplicate pending slip for duplicate msg id: " + messageId);
				}
				if (log.isDebugEnabled()) {
					log.debug("***********************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in eventDuplicateMsgCheckGetResponse", e);
			log.warn("HibernateException in eventDuplicateMsgCheckGetResponse", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in eventDuplicateMsgCheckGetResponse", e2);
			log.warn("Exception in eventDuplicateMsgCheckGetResponse", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to process the Credit key Off Auth Block
	 * 
	 * @param creditKeyOffBlk
	 * @param slipprimaryKey
	 * @return
	 * @throws JackpotDAOException
	 */
	public static List<String> creditKeyOffAuthBlockProcessing(ICreditKeyOffBlock creditKeyOffBlk,
			long slipPrimaryKey) throws JackpotDAOException {
		List<String> respStrList = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside creditKeyOffAuthBlockProcessing (XC 30 Credit Key Off Block)");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			/**
			 * Query to get the slip ref id based on the max sequence no, asset
			 * config no and the jackpot slip type
			 */

			String queryString = "select slipRef.id, slipRef.statusFlagId, slipRef.sequenceNumber "
					+ " from com.ballydev.sds.jackpot.db.slip.SlipReference slipRef"
					+ " , com.ballydev.sds.jackpot.db.slip.Jackpot as jackpot"
					+ " where jackpot.slipReferenceId = slipRef.id and  slipRef.transactionDate = (select max(slipRef1.transactionDate) from com.ballydev.sds.jackpot.db.slip.SlipReference slipRef1 where slipRef1.slipTypeId="
					+ JACKPOT_SLIP_TYPE_ID + " and slipRef1.acnfNumber = '"
					+ creditKeyOffBlk.getAssetConfigNumber() + "'"
					+ " and (slipRef1.processFlagId is null or slipRef1.processFlagId!="
					+ MANUAL_PROCESS_FLAG + ") " + " and slipRef1.siteId=" + creditKeyOffBlk.getSiteId()
					// extra chk for jp slip type is incase there is the same transaction date
					// for a different slip type
					+ ") and slipRef.slipTypeId=" + JACKPOT_SLIP_TYPE_ID 
					+ " and slipRef.acnfNumber = '" + creditKeyOffBlk.getAssetConfigNumber() 
					+ "' and slipRef.siteId=" + creditKeyOffBlk.getSiteId();

			// + " and slipRef1.processFlagId!=" + MANUAL_PROCESS_FLAG

			Query query = sessionObj.createQuery(queryString);
			if (log.isDebugEnabled()) {
				log.debug("Query String: " + query.getQueryString());
			}

			ArrayList<Object[]> queryRtnList = new ArrayList<Object[]>();

			queryRtnList = (ArrayList) query.list();
			if (queryRtnList != null && queryRtnList.size() > 0) {

				long slipRefId = (Long) queryRtnList.get(0)[0];
				short statusFlagId = (Short) queryRtnList.get(0)[1];
				long sequenceNo = (Long) queryRtnList.get(0)[2];

				if (log.isInfoEnabled()) {
					log.info("slipRefId: " + slipRefId);
					log.info("statusFlagId: " + statusFlagId);
					log.info("sequenceNo: " + sequenceNo);
				}

				respStrList = new ArrayList<String>();
				// respStrList(0) - Display Text Message
				// respStrList(1) - Response Action Code

				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId(slipRefId);
				transaction.setStatusFlagId(CREDIT_KEY_OFF_AUTH_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setMessageId(creditKeyOffBlk.getMessageId());
				transaction.setExceptionCodeId(EXCEPTION_JP_CREDIT_KEY_OFF_AUTH);
				transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(creditKeyOffBlk.getDateTime()
						.getTimeInMillis()));

				if (statusFlagId == PENDING_STATUS_ID) {
					// allow the process
					transaction.setStatusFlagId(CREDIT_KEY_OFF_AUTH_STATUS_ID);
					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP));

					respStrList.add(0, IAppConstants.SUCCESS);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_POSITIVE_RESP));

				} else if (statusFlagId == MECHANICS_DELTA_STATUS_ID) {

					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
					respStrList.add(0, IAppConstants.SLIP_IS_A_MECHANICS_DELTA);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));

				} else if (statusFlagId == INVALID_STATUS_ID) {

					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
					respStrList.add(0, IAppConstants.SLIP_IS_INVALID);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));

				} else if (statusFlagId == PROCESSED_STATUS_ID || statusFlagId == CHANGE_STATUS_ID
						|| statusFlagId == REPRINT_STATUS_ID || statusFlagId == PRINTED_STATUS_ID) {

					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
					respStrList.add(0, IAppConstants.SLIP_IS_ALREADY_PRINTED);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));

				} else if (statusFlagId == VOID_STATUS_ID) {

					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
					respStrList.add(0, IAppConstants.SLIP_IS_ALREADY_VOIDED);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));

				} else if (statusFlagId == CREDIT_KEY_OFF_STATUS_ID) {

					transaction.setResponseCode(String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
					respStrList.add(0, IAppConstants.SLIP_IS_ALREADY_CREDIT_KEYED_OFF);
					respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
				}
				sessionObj.save(transaction);
				if (log.isInfoEnabled()) {
					log.info("The data packet info has been saved and returned with the msg: "
							+ respStrList.get(0));
				}
			} else {
				respStrList = new ArrayList<String>();
				respStrList.add(0, IAppConstants.SLIP_NO_JACKPOT_SLIP);
				respStrList.add(1, String.valueOf(IAppConstants.XC_30_AUTH_BLOCK_NEGATIVE_RESP));
				if (log.isInfoEnabled()) {
					log.info("There is no slip for this data packet");
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in creditKeyOffAuthBlockProcessing", e);
			log.warn("HibernateException in creditKeyOffAuthBlockProcessing", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in creditKeyOffAuthBlockProcessing", e2);
			log.warn("Exception in creditKeyOffAuthBlockProcessing", e2);
			throw new JackpotDAOException(e2);
		}
		return respStrList;
	}

	/**
	 * Method to lock the Jackpot Slot table on a particular slot no
	 * 
	 * @param acnfNoSiteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public static boolean lockJackpotSlotTable(String acnfNo, int siteId) throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside lockJackpotSlotTable");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			if (log.isInfoEnabled()) {
				log.info("acnfNo/Employee Id: " + acnfNo);
				log.info("siteId: " + siteId);
			}
			String lockIdValue = null;

			if (acnfNo.startsWith("E")) {
				if (log.isDebugEnabled()) {
					log.debug("Lock based on EMPID");
				}
				lockIdValue = PadderUtil.lPad(acnfNo, IAppConstants.LOCK_EMPLOYEE_ID_PAD_LENGTH) + "_"
						+ PadderUtil.lPad(String.valueOf(siteId), IAppConstants.SITE_ID_PAD_LENGTH);
				if (log.isInfoEnabled()) {
					log.info("EMPID_SITEID: " + lockIdValue);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Lock based on SLOT");
				}
				lockIdValue = PadderUtil.lPad(acnfNo, IAppConstants.ASSET_NO_PAD_LENGTH) + "_"
						+ PadderUtil.lPad(String.valueOf(siteId), IAppConstants.SITE_ID_PAD_LENGTH);
				if (log.isInfoEnabled()) {
					log.info("ACNFNO_SITEID: " + lockIdValue);
				}
			}
			
			
			LockOptions lockOptions = new LockOptions(LockMode.PESSIMISTIC_WRITE);
			JackpotSlot jackpotSlot = (JackpotSlot) sessionObj.get(JackpotSlot.class, lockIdValue,
					lockOptions);

			if (jackpotSlot == null) {
				jackpotSlot = new JackpotSlot();
				jackpotSlot.setAcnfNumber(lockIdValue);
				jackpotSlot.setCreatedTs(DateHelper.getUTCTimeFromLocal(System.currentTimeMillis()));
				sessionObj.save(jackpotSlot);
				if (log.isInfoEnabled()) {
					log.info("The Jackpot Slot table has been updated with :- " + lockIdValue);
				}
			} else {
				if (log.isInfoEnabled()) {
					log.info("The Jackpot Slot table already has a record for :- " + lockIdValue);
				}
			}
			return true;
		} catch (HibernateException e) {
			log.error("HibernateException in lockJackpotSlotTable", e);
			log.warn("HibernateException in lockJackpotSlotTable", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in lockJackpotSlotTable", e2);
			log.warn("Exception in lockJackpotSlotTable", e2);
			throw new JackpotDAOException(e2);
		}
	}

	public static void main(String[] args) {

		Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
		org.hibernate.Transaction trans = sessionObj.beginTransaction();
		JackpotEventsDAO dao = new JackpotEventsDAO();
		/*
		 * JackpotToCreditMeterMsg jackpotToCreditMeter = new
		 * JackpotToCreditMeterMsg();
		 * 
		 * jackpotToCreditMeter.setJackpotAmount(50);
		 * jackpotToCreditMeter.setAssetConfigNumber("2000");
		 * jackpotToCreditMeter.setJackpotId((char)252);
		 * jackpotToCreditMeter.setGmuDenom(2);
		 * jackpotToCreditMeter.setDateTime(Calendar.getInstance());
		 * jackpotToCreditMeter.setMessageId(MessageUtil.getMessageId());
		 * 
		 * JackpotDTO jackpotDTO = new JackpotDTO();
		 * 
		 * try { dao.createJackpotToCreditMeterSlips(jackpotToCreditMeter,
		 * jackpotDTO); } catch (JackpotDAOException e) { e.printStackTrace(); }
		 */

		try {

			JackpotResetMessage jpResetMsg = new JackpotResetMessage();

			jpResetMsg.setAssetConfigNumber("00005");
			// jpResetMsg.setJackpotId((char)252);
			jpResetMsg.setGmuDenom(1);
			jpResetMsg.setDateTime(Calendar.getInstance());
			jpResetMsg.setMessageId(MessageUtil.getMessageId());
			jpResetMsg.setSiteId(512);

			dao.jackpotClearEvent(jpResetMsg, 1111111111L);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// trans.commit();

	}

}