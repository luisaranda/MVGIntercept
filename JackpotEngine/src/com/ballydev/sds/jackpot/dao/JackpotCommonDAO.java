/*****************************************************************************
 * $Id: JackpotCommonDAO.java,v 1.54.1.1.1.0, 2011-07-25 16:05:18Z, SDS 12.3.3$
 * $Date: 7/25/2011 11:05:18 AM$
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ballydev.sds.db.HibernateUtil;
import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.IJackpotIds;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpot.db.slip.Jackpot;
import com.ballydev.sds.jackpot.db.slip.JackpotSequence;
import com.ballydev.sds.jackpot.db.slip.SlipData;
import com.ballydev.sds.jackpot.db.slip.SlipReference;
import com.ballydev.sds.jackpot.db.slip.Transaction;
import com.ballydev.sds.jackpot.dto.BarcodeDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotEmployeeInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotResetDTO;
import com.ballydev.sds.jackpot.dto.NewWaveRequestDTO;
import com.ballydev.sds.jackpot.dto.NewWaveResponseDTO;
import com.ballydev.sds.jackpot.dto.SequenceWithTypeDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.ConversionUtil;
import com.ballydev.sds.jackpot.util.JackpotHelper;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;
import com.ballydev.sds.jackpot.util.JpResetDTOComparator;
import com.ballydev.sds.jackpot.util.PadderUtil;

/**
 * DTO used to populate the jackpot information to be sent to EA
 * 
 * @author dambereen
 * @version $Revision: 58$
 */
public class JackpotCommonDAO implements ILookupTableConstants {

	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * blind attempt value initialized to zero
	 */
	private static final short BLIND_ATTEMPT = 0;
	
	/**
	 * Method to void all the pending jackpot slips for the SiteId
	 * 
	 * @param siteId
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidAllPendingJackpotSlipsForAuditProcess(int siteId, String employeeId,
			String kioskProcessed, String empFirstName, String empLastName) throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = null;
		JackpotDTO jackpotDTO = null;
		try {
			jackpotDTOList = new ArrayList<JackpotDTO>();
			if (log.isInfoEnabled()) {
				log.info("Inside updateVoidAllPendingJackpotSlipsForAuditProcess DAO");
			}

			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			Criterion crtStatusFlag = Restrictions.eq("statusFlagId", PENDING_STATUS_ID);
			Criterion crtSlipTypeid = Restrictions.and(crtStatusFlag,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid, Restrictions.eq("siteId", siteId));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria SlipReferenceCrita = sessionObj.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			List<SlipReference> slipReferenceList = SlipReferenceCrita.list();

			Timestamp currentLocalTime = new Timestamp(System.currentTimeMillis());

			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("slipReferenceList : " + slipReferenceList.size());
				}

				int numberOfIncrements = slipReferenceList.size();

				// TODO check what needs to be done for the Slot Number for the
				// below when slot number is passed as null
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(null, siteId, numberOfIncrements);

				for (int i = 0; i < slipReferenceList.size(); i++) {

					SlipReference slipReference = (SlipReference) slipReferenceList.get(i);
					// String slipTransDate =
					// DateUtil.getStringDate(DateUtil.getMilliSeconds(DateUtil.getLocalTimeFromUTC(slipReference.getTransactionDate())));
					if (log.isDebugEnabled()) {
						log.debug("Seq No: " + slipReference.getSequenceNumber());
					}
					slipReference.setTransactionId(slipPrimaryKey);
					// if(!currentGamingDay.equalsIgnoreCase(slipTransDate)){
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setUpdatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipReference);

					/** CREATE THE ROW IN SLIP_DATA TABLE * */
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(kioskProcessed);
					if (employeeId != null) {
						slipData.setActrLogin(employeeId);
					} else {
						// In case of auto voiding of slip using accounting site
						// configuration parameter
						// the employee id value will come as null. In order to
						// avoid null insertion
						// to the column SLPD_ACTR_LOGIN ( it is a non nullable
						// field)
						slipData.setActrLogin(IAppConstants.EMPTY_STRING);
					}
					slipData.setCreatedUser(employeeId);
					slipData.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);

					/** Create a record in the transaction TABLE */

					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(VOID_STATUS_ID);
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					transaction.setEmployeeId(employeeId);
					transaction.setEmployeeFirstName(empFirstName);
					transaction.setEmployeeLastName(empLastName);
					transaction.setCreatedUser(employeeId);
					sessionObj.save(transaction);

					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The slip status has been changed to Void for the sequence: "
								+ slipReference.getSequenceNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}

					jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);

					if (jackpotDTO != null) {
						jackpotDTOList.add(jackpotDTO);
					}

					// Increment the primary Key value
					slipPrimaryKey++;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no pending records for the Site ID : " + siteId);
				}
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
				return jackpotDTOList;
			}
		} catch (HibernateException e) {
			log.error("HibernateException in updateVoidAllPendingJackpotSlipsForAuditProcess", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in updateVoidAllPendingJackpotSlipsForAuditProcess", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method to void all the pending jackpot slips for the SiteId and
	 * Transaction Date being passed
	 * 
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidPendingJackpotSlipsForAuditProcess(int siteId, long startTime,
			long endTime, String employeeId, String kioskProcessed, String empFirstName, String empLastName)
			throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = null;
		JackpotDTO jackpotDTO = null;
		try {
			jackpotDTOList = new ArrayList<JackpotDTO>();
			if (log.isInfoEnabled()) {
				log.info("Inside updateVoidPendingJackpotSlipsForAuditProcess DAO");
				log.info("startTime: " + new Timestamp(startTime));
				log.info("endTime: " + new Timestamp(endTime));
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			Criterion crtDate = Restrictions.between("transactionDate", new Timestamp(startTime),
					new Timestamp(endTime));
			Criterion crtStatusFlag = Restrictions.and(crtDate,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion crtSlipTypeid = Restrictions.and(crtStatusFlag,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid, Restrictions.eq("siteId", siteId));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria SlipReferenceCrita = sessionObj.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			List<SlipReference> slipReferenceList = SlipReferenceCrita.list();

			Timestamp currentLocalTime = new Timestamp(System.currentTimeMillis());

			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("slipReferenceList : " + slipReferenceList.size());
				}

				int numberOfIncrements = slipReferenceList.size();

				// TODO check what needs to be done for the Slot Number fro the
				// below
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(null, siteId, numberOfIncrements);

				for (int i = 0; i < slipReferenceList.size(); i++) {

					SlipReference slipReference = (SlipReference) slipReferenceList.get(i);
					// String slipTransDate =
					// DateUtil.getStringDate(DateUtil.getMilliSeconds(DateUtil.getLocalTimeFromUTC(slipReference.getTransactionDate())));
					if (log.isDebugEnabled()) {
						log.debug("Seq No: " + slipReference.getSequenceNumber());
					}
					slipReference.setTransactionId(slipPrimaryKey);
					// if(!currentGamingDay.equalsIgnoreCase(slipTransDate)){
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setUpdatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipReference);

					/** CREATE THE ROW IN SLIP_DATA TABLE * */
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(kioskProcessed);
					if (employeeId != null) {
						slipData.setActrLogin(employeeId);
					} else {
						// In case of auto voiding of slip using accounting site
						// configuration parameter
						// the employee id value will come as null. In order to
						// avoid null insertion
						// to the column SLPD_ACTR_LOGIN ( it is a non nullable
						// field)
						slipData.setActrLogin(IAppConstants.EMPTY_STRING);
					}
					slipData.setCreatedUser(employeeId);
					slipData.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);

					/** Create a record in the transaction TABLE */

					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(VOID_STATUS_ID);
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setEmployeeId(employeeId);
					transaction.setEmployeeFirstName(empFirstName);
					transaction.setEmployeeLastName(empLastName);
					transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					transaction.setCreatedUser(employeeId);
					sessionObj.save(transaction);

					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The slip status has been changed to Void for the sequence: "
								+ slipReference.getSequenceNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}

					jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);

					/*
					 * }else{ log.debug("Void is not done for the sequence: " +
					 * slipReference
					 * .getSequenceNumber()+" gaming day is the same"); }
					 */
					if (jackpotDTO != null) {
						jackpotDTOList.add(jackpotDTO);
					}
					// Increment the primary Key value
					slipPrimaryKey++;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no pending records for the Site ID : " + siteId + " and StartTime:"
							+ new Timestamp(startTime) + " EndTime: " + new Timestamp(endTime));
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************************************************************************************");
				}
				return jackpotDTOList;
			}
		} catch (HibernateException e) {
			log.error("HibernateException in updateVoidPendingJackpotSlipsForAuditProcess", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in updateVoidPendingJackpotSlipsForAuditProcess", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method to void all the pending jackpot slips for the SiteId and
	 * Transaction Date being passed
	 * 
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param slotNo
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidPendingJackpotSlipsForAuditProcess(int siteId, long startTime,
			long endTime, String slotNo, String employeeId, String kioskProcessed, String empFirstName,
			String empLastName) throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = null;
		JackpotDTO jackpotDTO = null;
		try {
			jackpotDTOList = new ArrayList<JackpotDTO>();
			if (log.isInfoEnabled()) {
				log.info("Inside updateVoidPendingJackpotSlipsForAuditProcess DAO");
				log.info("startTime: " + new Timestamp(startTime));
				log.info("endTime: " + new Timestamp(endTime));
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			Criterion crtDate = Restrictions.between("transactionDate", new Timestamp(startTime),
					new Timestamp(endTime));
			Criterion crtStatusFlag = Restrictions.and(crtDate,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion crtSlotNo = Restrictions.and(crtStatusFlag, Restrictions.eq("acnfNumber", slotNo));
			Criterion crtSlipTypeid = Restrictions.and(crtSlotNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid, Restrictions.eq("siteId", siteId));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria SlipReferenceCrita = sessionObj.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			List<SlipReference> slipReferenceList = SlipReferenceCrita.list();

			Timestamp currentLocalTime = new Timestamp(System.currentTimeMillis());

			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("slipReferenceList : " + slipReferenceList.size());
				}
				int numberOfIncrements = slipReferenceList.size();

				// TODO check what needs to be done for the Slot Number fro the
				// below
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(null, siteId, numberOfIncrements);

				for (int i = 0; i < slipReferenceList.size(); i++) {

					SlipReference slipReference = (SlipReference) slipReferenceList.get(i);
					// String slipTransDate =
					// DateUtil.getStringDate(DateUtil.getMilliSeconds(DateUtil.getLocalTimeFromUTC(slipReference.getTransactionDate())));
					if (log.isDebugEnabled()) {
						log.debug("Seq No: " + slipReference.getSequenceNumber());
					}
					slipReference.setTransactionId(slipPrimaryKey);
					// if(!currentGamingDay.equalsIgnoreCase(slipTransDate)){
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setUpdatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipReference);

					/** CREATE THE ROW IN SLIP_DATA TABLE * */
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(kioskProcessed);
					if (employeeId != null) {
						slipData.setActrLogin(employeeId);
					} else {
						// In case of auto voiding of slip using accounting site
						// configuration parameter
						// the employee id value will come as null. In order to
						// avoid null insertion
						// to the column SLPD_ACTR_LOGIN ( it is a non nullable
						// field)
						slipData.setActrLogin(IAppConstants.EMPTY_STRING);
					}
					slipData.setCreatedUser(employeeId);
					slipData.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);

					/** Create a record in the transaction TABLE */

					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(VOID_STATUS_ID);
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setEmployeeId(employeeId);
					transaction.setEmployeeFirstName(empFirstName);
					transaction.setEmployeeLastName(empLastName);
					transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime));
					transaction.setCreatedUser(employeeId);
					sessionObj.save(transaction);

					/** Create a record in the Slip Transfer TABLE */

					/*
					 * SlipTransfer slipTrans = new SlipTransfer();
					 * slipTrans.setSlipReferenceId(slipReference.getId());
					 * slipTrans
					 * .setCreatedTs(DateHelper.getUTCTimeFromLocal(currentLocalTime
					 * )); slipTrans.setCreatedUser(employeeId);
					 * sessionObj.save(slipTrans);
					 */

					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The slip status has been changed to Void for the sequence: "
								+ slipReference.getSequenceNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}

					jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);

					/*
					 * }else{ log.debug("Void is not done for the sequence: " +
					 * slipReference
					 * .getSequenceNumber()+" gaming day is the same"); }
					 */
					if (jackpotDTO != null) {
						jackpotDTOList.add(jackpotDTO);
					}

					// Increment the primary Key value
					slipPrimaryKey++;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no pending records for the Site ID : " + siteId + " and StartTime:"
							+ new Timestamp(startTime) + " EndTime: " + new Timestamp(endTime));
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************************************************************************");
				}
				return jackpotDTOList;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("HibernateException in updateVoidPendingJackpotSlipsForAuditProcess", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			e2.printStackTrace();
			log.error("Exception in updateVoidPendingJackpotSlipsForAuditProcess", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method that will be called by the Security engine to update the Employee
	 * Id, First and Last name of the Employee who carded the Jackpot
	 * 
	 * @param employeeCardIn
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	public boolean jackpotCardedEmployeeInfo(JackpotEmployeeInfoDTO employeeInfoDTO)
			throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside jackpotCardedEmployeeInfo DAO method");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			String queryString = null;
			if (employeeInfoDTO.getAssetConfigNo() != null) {
				if (log.isInfoEnabled()) {
					log.info("Slot no got for XC 37 is: " + employeeInfoDTO.getAssetConfigNo());
				}
				queryString = "select id from com.ballydev.sds.jackpot.db.slip.SlipReference where sequenceNumber = (select max(sequenceNumber) from com.ballydev.sds.jackpot.db.slip.SlipReference where slipTypeId=3001 and statusFlagId = 4001 and acnfNumber = '"
						+ employeeInfoDTO.getAssetConfigNo()
						+ "' and siteId = "+employeeInfoDTO.getSiteId()+") and slipTypeId=3001 and statusFlagId = 4001 and acnfNumber = '"
						+ employeeInfoDTO.getAssetConfigNo() + "' and siteId = "+employeeInfoDTO.getSiteId();
			} else {
				if (log.isInfoEnabled()) {
					log.info("Slot no got for XC 37 is NULL");
				}
				return false;
			}

			Query query = sessionObj.createQuery(queryString);

			if (query.uniqueResult() != null) {

				long slipRefId = ((Long) query.uniqueResult()).longValue();

				if (log.isInfoEnabled()) {
					log.info("Selected slip ref id: " + slipRefId);
				}

				Criterion crtSlipRefId = Restrictions.eq("id", slipRefId);

				Criterion crtSiteId = Restrictions.and(crtSlipRefId,
						Restrictions.eq("siteId", employeeInfoDTO.getSiteId()));

				Criteria slipReferenceCriteria = sessionObj.createCriteria(SlipReference.class)
						.add(crtSiteId);

				SlipReference slipReference = (SlipReference) slipReferenceCriteria.uniqueResult();

				if (slipReference != null) {
					long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(slipReference.getAcnfNumber(),
							slipReference.getSiteId(), 0);
					/** Create a record in TRANSACTION table */
					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(CARD_IN_STATUS_ID);
					/*
					 * ProcessAutoAuthorization processAutoAuthorization = new
					 * ProcessAutoAuthorization(); String autoAuthorize =
					 * processAutoAuthorization
					 * .getJackpotSiteParamValueForDenomination
					 * (ICommonConstants.GAME_DENOMINATION_IN_CENTS,
					 * employeeCardIn.getSiteId());
					 * if(autoAuthorize.equalsIgnoreCase("yes")) {
					 * transaction.setPostFlagId(AUTO_AUTHORIZE_NOT_POSTED);
					 * }else{
					 */
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					// }

					transaction.setPostFlagId(AUTO_AUTHORIZE_NOT_POSTED);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setExceptionCodeId(EXCEPTION_EMPLOYEE_CARD_IN);
					// transaction.setGmuCoinInMeter(jackpotClear.getBets());
					// transaction.setLastCoinIn(new
					// Long(jackpotClear.getLastBet()));
					// transaction.setDoorStatus(doorStatus);
					transaction.setEmployeeCardNo(employeeInfoDTO.getEmployeeCardNo());
					transaction.setEmployeeId(PadderUtil.lPad(employeeInfoDTO.getEmployeeId(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					transaction.setEmployeeFirstName(employeeInfoDTO.getEmployeeFirstName());
					transaction.setEmployeeLastName(employeeInfoDTO.getEmployeeLastName());
					transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(employeeInfoDTO
							.getDateTimeMilliSecs()));
					transaction.setCreatedUser(PadderUtil.lPad(employeeInfoDTO.getEmployeeId(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(transaction);
					if (log.isDebugEnabled()) {
						log.debug("******************************************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("Slot attendant id updated successfully for the sequence no : "
								+ slipReference.getSequenceNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("******************************************************************************************");
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("*********************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The jackpot slip ref id is NULL");
					}
					if (log.isDebugEnabled()) {
						log.debug("*********************************************");
					}
					return false;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("*********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no pending record");
				}
				if (log.isDebugEnabled()) {
					log.debug("*********************************************");
				}
				return false;
			}
		} catch (HibernateException e) {
			log.error("Hibernate Exception in employeeCardIn", e);
			log.warn("Hibernate Exception in employeeCardIn", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in employeeCardIn", e2);
			log.warn("Exception in employeeCardIn", e2);
			throw new JackpotDAOException(e2);
		}
		return true;
	}

	/**
	 * Method to retrieveSlipsBySequence based on the status ids being passed
	 * 
	 * @param startSeq
	 * @param endSeq
	 * @param reqdStatusFlag
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<SlipReference> retrieveSlipsBySequence(long startSeq, long endSeq, Short[] reqdStatusFlag)
			throws JackpotEngineServiceException {
		if (log.isInfoEnabled()) {
			log.info("Inside retrieveSlipsBySequence DAO method");
		}
		List<SlipReference> slips = null;
		List<SlipReference> slipLst = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			StringBuilder statusIds = new StringBuilder();
			for (int index = 0; index < reqdStatusFlag.length; index++) {
				statusIds.append(reqdStatusFlag[index]);
				if (index != reqdStatusFlag.length - 1) {
					statusIds.append(",");
				}
			}
			String FROM_CLAUSE = "FROM com.ballydev.sds.jackpot.db.slip.SlipReference slpr "
					+ "left join fetch slpr.slipType " + "left join fetch slpr.auditCode "
					+ "left join fetch slpr.processFlag " + "left join fetch slpr.jackpot "
					+ "left join fetch slpr.slip " + "left join fetch slpr.slipData "
					+ "left join fetch slpr.transaction trans " + "left join fetch slpr.slipTransfer sltr ";

			String WHERE_CLAUSE = "WHERE (sltr.seqId BETWEEN " + startSeq + " AND " + endSeq
					+ ") AND trans.statusFlagId IN (" + statusIds.toString() + ") ";

			String ORDER_BY_CLAUSE = "ORDER BY sltr.seqId ASC";
			String sql = FROM_CLAUSE + " " + WHERE_CLAUSE + " " + ORDER_BY_CLAUSE;

			Query query = session.createQuery(sql);
			slips = query.list();
			HashMap<Long, SlipReference> slipMap = new HashMap<Long, SlipReference>();
			slipLst = new ArrayList<SlipReference>();
			for (SlipReference slipReference : slips) {
				if (!slipMap.containsKey(slipReference.getId())) {
					slipMap.put(slipReference.getId(), slipReference);
					slipLst.add(slipReference);
				}
			}

		} catch (HibernateException e) {
			log.error("Exception in retrieveSlipsBySequence DAO method", e);
		}
		return slipLst;
	}

	/**
	 * Method to get the total slip count in the SLIP_TRANSFER table
	 * 
	 * @return
	 * @throws JackpotDAOException
	 */
	public Long retrieveSlipsByCount() throws JackpotDAOException {
		Long count = 0l;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		String sql = "SELECT COUNT(seqId) FROM com.ballydev.sds.jackpot.db.slip.SlipTransfer";
		Query query = session.createQuery(sql);
		count = (Long) query.uniqueResult();
		return count;
	}

	/**
	 * Method to get the Jackpot Details for S2S
	 * 
	 * @param sequenceNo
	 * @param siteId
	 * @return JackpotDetailsDTO
	 * @throws JackpotDAOException
	 */
	public JackpotDetailsDTO getJackpotDetailsForS2S(long sequenceNo, int siteId) throws JackpotDAOException {
		JackpotDetailsDTO jackpotDetDTO = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotDetailsForS2S DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			Criterion crtnSeqNoCrt = Restrictions.eq("sequenceNumber", sequenceNo);
			Criterion crtSlipTypeId = Restrictions.and(crtnSeqNoCrt,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId, Restrictions.eq("siteId", siteId));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);
			SlipReference slipRefDetails = (SlipReference) slipRefCriteria.uniqueResult();

			if (slipRefDetails != null) {

				jackpotDetDTO = JackpotHelper.getJackpotDetailsDTO(slipRefDetails);
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of pending slips for Seq No: " + sequenceNo + " and site id: "
							+ siteId + " have been returned ");
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no jackpot records for the sequence: " + sequenceNo
							+ "  and siteId: " + siteId);
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotDetailsForS2S", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotDetailsForS2S", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDetDTO;
	}

	/**
	 * Method to get the Processed Progressive Jackpot Info
	 * 
	 * @param siteId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings("unchecked")
	public List<JackpotDetailsDTO> getProgressiveJackpotInfo(int siteId, long fromTime, long toTime)
			throws JackpotDAOException {
		List<JackpotDetailsDTO> jackpotDetDTOList = new ArrayList<JackpotDetailsDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getProgressiveJackpotInfo DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			if (log.isInfoEnabled()) {
				log.info("startTime: " + new Timestamp(fromTime));
				log.info("endTime: " + new Timestamp(toTime));
			}

			ArrayList statusFlagList = new ArrayList();
			statusFlagList.add(ILookupTableConstants.CHANGE_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PROCESSED_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.REPRINT_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PRINTED_STATUS_ID); // CASHIER
																			// DESK
																			// FLOW

			ArrayList jackpotTypeList = new ArrayList();
			jackpotTypeList.add(ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE);
			jackpotTypeList.add(ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE);

			Criterion crtSlipCreatedDate = Restrictions.between("slipData.createdTs",
					new Timestamp(fromTime), new Timestamp(toTime));

			Criterion crtJpTypeId = Restrictions.in("jackpot.jackpotTypeId", jackpotTypeList);

			Criterion crtStatus = Restrictions.in("statusFlagId", statusFlagList);
			Criterion crtSlipTypeId = Restrictions.and(crtStatus,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId, Restrictions.eq("siteId", siteId));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jackpot", Criteria.INNER_JOIN)
					.createAlias("slipData", "slipData", Criteria.INNER_JOIN).add(crtSiteId).add(crtJpTypeId)
					.add(crtSlipCreatedDate).addOrder(orderBySeq);

			List<SlipReference> slipRefList = slipRefCriteria.list();

			if (slipRefList.size() > 0) {
				if (log.isInfoEnabled()) {
					log.info("slipRefList.size() is greater than zero: " + slipRefList.size());
				}
				for (int i = 0; i < slipRefList.size(); i++) {

					JackpotDetailsDTO jackpotDetDTO = JackpotHelper.getJackpotDetailsDTO(slipRefList.get(i));
					jackpotDetDTOList.add(jackpotDetDTO);
				}
				if (log.isInfoEnabled()) {
					log.info("*****************************************************************************************************************");
					log.info("The list of processed jackpot details have been returned with: "
							+ slipRefList.size() + " records");
					log.info("*****************************************************************************************************************");
				}
			} else if (log.isInfoEnabled()) {
				log.info("**********************************************");
				log.info("There are no processed progressive jp records");
				log.info("**********************************************");
			}

			/** for pending jackpot records */

			Criterion crtSlipTransDate2 = Restrictions.between("transactionDate", new Timestamp(fromTime),
					new Timestamp(toTime));

			Criterion crtJpTypeId2 = Restrictions.in("jackpot.jackpotTypeId", jackpotTypeList);

			Criterion crtStatus2 = Restrictions.eq("statusFlagId", PENDING_STATUS_ID);
			Criterion crtSlipTypeId2 = Restrictions.and(crtStatus2,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId2 = Restrictions.and(crtSlipTypeId2, Restrictions.eq("siteId", siteId));

			Criteria slipRefCriteria2 = sessionObj.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jackpot", Criteria.INNER_JOIN).add(crtStatus2).add(crtSiteId2)
					.add(crtJpTypeId2).add(crtSlipTransDate2).addOrder(orderBySeq);

			List<SlipReference> slipRefList2 = slipRefCriteria2.list();

			if (slipRefList2.size() > 0) {
				if (log.isInfoEnabled()) {
					log.info("slipRefList2.size() is greater than zero: " + slipRefList2.size());
				}
				for (int i = 0; i < slipRefList2.size(); i++) {

					JackpotDetailsDTO jackpotDetDTO = JackpotHelper.getJackpotDetailsDTO(slipRefList2.get(i));
					jackpotDetDTOList.add(jackpotDetDTO);
				}
				if (log.isInfoEnabled()) {
					log.info("*****************************************************************************************************************");
					log.info("The list of pending jackpot details have been returned with: "
							+ slipRefList.size() + " records");
					log.info("*****************************************************************************************************************");
				}
			} else if (log.isInfoEnabled()) {
				log.info("**********************************************");
				log.info("There are no pending progressive jp records");
				log.info("**********************************************");
			}

		} catch (HibernateException e) {
			log.error("HibernateException in getProgressiveJackpotInfo", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getProgressiveJackpotInfo", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDetDTOList;
	}

	/**
	 * Method to provide the total count of the jackpots that have been
	 * processed and reprinted
	 * 
	 * @param fromDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings("unchecked")
	public int getNumOfJackpots(Date fromDate, int siteId) throws JackpotDAOException {
		int count = 0;
		try {

			if (log.isInfoEnabled()) {
				log.info("Inside getNumOfJackpots DAO");
			}
			if (log.isDebugEnabled()) {
				log.debug("From date: " + DateHelper.getUTCTimeFromLocal(fromDate.getTime()));
				log.debug("To date: " + DateHelper.getUTCTimeFromLocal(System.currentTimeMillis()));
				log.debug("Site Id: " + siteId);
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			List statusFlgIdLst = new ArrayList();
			statusFlgIdLst.add(PROCESSED_STATUS_ID);
			statusFlgIdLst.add(CHANGE_STATUS_ID);
			statusFlgIdLst.add(REPRINT_STATUS_ID);
			statusFlgIdLst.add(PRINTED_STATUS_ID); // CASHIER DESK FLOW

			String queryStr = "select count(*) from com.ballydev.sds.jackpot.db.slip.SlipReference where coalesce(updatedTs,createdTs) >= '"
					+ DateHelper.getUTCTimeFromLocal(fromDate.getTime())
					+ "' and coalesce(updatedTs,createdTs) <= '"
					+ DateHelper.getUTCTimeFromLocal(System.currentTimeMillis())
					+ "' and siteId="
					+ siteId
					+ " and slipTypeId=" + JACKPOT_SLIP_TYPE_ID + " and statusFlagId in (:statusFlgIdLst)";

			Query query = sessionObj.createQuery(queryStr);
			query.setParameterList("statusFlgIdLst", statusFlgIdLst);

			if (log.isDebugEnabled()) {
				log.debug("query: " + query.getQueryString());
			}

			Object rtnCountObj = query.uniqueResult();
			if (rtnCountObj != null) {
				count = ((Long) rtnCountObj).intValue();
				if (log.isDebugEnabled()) {
					log.debug("count: " + count);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no processed/reprinted records for the given Site ID : " + siteId
							+ " and from date");
				}
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getNumOfJackpots", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getNumOfJackpots", e2);
			throw new JackpotDAOException(e2);
		}
		return count;

	}

	/**
	 * Method to provide the total count of the jackpots that have been
	 * processed and reprinted
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings("unchecked")
	public int getNumOfJackpots(Date fromDate, Date toDate, int siteId) throws JackpotDAOException {
		int count = 0;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getNumOfJackpots DAO");
			}
			if (log.isDebugEnabled()) {
				log.debug("From date: " + DateHelper.getUTCTimeFromLocal(fromDate.getTime()));
				log.debug("To date: " + DateHelper.getUTCTimeFromLocal(toDate.getTime()));
				log.debug("Site Id: " + siteId);
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			List statusFlgIdLst = new ArrayList();
			statusFlgIdLst.add(PROCESSED_STATUS_ID);
			statusFlgIdLst.add(CHANGE_STATUS_ID);
			statusFlgIdLst.add(REPRINT_STATUS_ID);
			statusFlgIdLst.add(PRINTED_STATUS_ID); // CASHIER DESK FLOW

			String queryStr = "select count(*) from com.ballydev.sds.jackpot.db.slip.SlipReference where coalesce(updatedTs,createdTs) >= '"
					+ DateHelper.getUTCTimeFromLocal(fromDate.getTime())
					+ "' and coalesce(updatedTs,createdTs) <= '"
					+ DateHelper.getUTCTimeFromLocal(toDate.getTime())
					+ "' and siteId="
					+ siteId
					+ " and slipTypeId=" + JACKPOT_SLIP_TYPE_ID + " and statusFlagId in (:statusFlgIdLst)";

			Query query = sessionObj.createQuery(queryStr);
			query.setParameterList("statusFlgIdLst", statusFlgIdLst);

			if (log.isDebugEnabled()) {
				log.debug("query: " + query.getQueryString());
			}

			Object rtnCountObj = query.uniqueResult();
			if (rtnCountObj != null) {
				count = ((Long) rtnCountObj).intValue();
				if (log.isDebugEnabled()) {
					log.debug("count: " + count);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no processed/reprinted records for the given Site ID : " + siteId
							+ " and time range");
				}
				if (log.isDebugEnabled()) {
					log.debug("***************************************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getNumOfJackpots", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getNumOfJackpots", e2);
			throw new JackpotDAOException(e2);
		}
		return count;

	}

	/**
	 * This Method is exposed for the progressive jackpot engine for it to check
	 * whether the last hit jackpot for this slot is progressive or not.
	 * 
	 * @param siteNumber
	 * @param slotNumber
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean isLastHitProgressiveJackpot(int siteNumber, String slotNumber) throws JackpotDAOException {
		boolean isProgressiveJackpot = false;
		try {
			if (!(siteNumber <= 0) && slotNumber != null
					&& !((slotNumber.trim() + " ").equalsIgnoreCase(" "))) {
				if (log.isInfoEnabled()) {
					log.info("Inside isLastHitProgressiveJackpot method of JackpotCommonDAO");
				}
				Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
				Criterion criterionAssetNumber = Restrictions.eq("acnfNumber", slotNumber.trim());
				Criterion criterionSlipTypeId = Restrictions.and(criterionAssetNumber,
						Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
				Criterion criterionSiteNumber = Restrictions.and(criterionSlipTypeId,
						Restrictions.eq("siteId", siteNumber));
				Criteria criteriaSlipRef = sessionObj.createCriteria(SlipReference.class)
						.add(criterionSiteNumber).addOrder(Order.desc("createdTs"));
				if (criteriaSlipRef != null) {
					List<SlipReference> listSlipReference = criteriaSlipRef.list();
					if (listSlipReference != null && listSlipReference.size() != 0) {

						SlipReference slipReferenceToProcess = listSlipReference.get(0);
						if (slipReferenceToProcess != null) {
							Set<Jackpot> jackpotSetGot = slipReferenceToProcess.getJackpot();
							if (jackpotSetGot != null && jackpotSetGot.size() != 0) {
								Jackpot jackpotToCheckFor = null;
								for (Jackpot jackpotToProcessFor : jackpotSetGot) {
									if (jackpotToProcessFor != null) {
										jackpotToCheckFor = jackpotToProcessFor;
										break;
									}
								}

								if (jackpotToCheckFor != null) {
									if ((Integer.parseInt(jackpotToCheckFor.getJackpotId(), 16) >= 112 && Integer
											.parseInt(jackpotToCheckFor.getJackpotId(), 16) <= 159)
											&& (jackpotToCheckFor.getJackpotTypeId().shortValue() == JACKPOT_TYPE_lINKED_PROGRESSIVE || jackpotToCheckFor
													.getJackpotTypeId().shortValue() == JACKPOT_TYPE_PROGRESSIVE)

									) {
										isProgressiveJackpot = true;
										if (log.isInfoEnabled()) {
											log.info("Check for isLastHitProgressiveJackpot :"
													+ isProgressiveJackpot);
										}
									} else if (log.isInfoEnabled()) {
										log.info("Check for isLastHitProgressiveJackpot :"
												+ isProgressiveJackpot);
									}
								}
							}
						}

					}
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in isLastHitProgressiveJackpot", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in isLastHitProgressiveJackpot", e2);
			throw new JackpotDAOException(e2);
		}

		return isProgressiveJackpot;
	}

	/**
	 * 
	 * This method is used to reterive the maps for new wave requirement.
	 * 
	 * @param mapSlipIdToSlip
	 * @param mapSlipIdToJackpot
	 * @param mapSlipIdToSlipData
	 * @throws JackpotDAOException
	 */
	public void getMapsForNewWave(Map<Long, SlipReference> mapSlipIdToSlip,
			Map<Long, Jackpot> mapSlipIdToJackpot, Map<Long, SlipData> mapSlipIdToSlipData)
			throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside getMapsForNewWave method of JackpotCommonDAO");
		}
		try {
			if (mapSlipIdToSlip != null && mapSlipIdToJackpot != null && mapSlipIdToSlipData != null) {
				Session sessionObject = HibernateUtil.getSessionFactory().getCurrentSession();
				// creating mapSlipIdToSlip for this functionality
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("id"));
				projectionList.add(Projections.property("sequenceNumber"));
				projectionList.add(Projections.property("slipTypeId"));
				projectionList.add(Projections.property("statusFlagId"));
				projectionList.add(Projections.property("processFlagId"));
				projectionList.add(Projections.property("siteId"));
				projectionList.add(Projections.property("slotDenomination"));
				projectionList.add(Projections.property("updatedTs"));
				projectionList.add(Projections.property("createdTs"));
				projectionList.add(Projections.property("acnfNumber"));
				Criteria criteriaToProcess = sessionObject.createCriteria(SlipReference.class).setProjection(
						projectionList);
				if (criteriaToProcess != null) {
					List listToProcess = criteriaToProcess.list();
					if (listToProcess != null && listToProcess.size() != 0) {
						for (int i = 0; i < listToProcess.size(); i++) {
							Object[] objectArray = (Object[]) listToProcess.get(i);
							if (objectArray != null && objectArray.length != 0 && !(objectArray.length < 10)) {
								SlipReference slipReferenceToPutInMap = new SlipReference();
								if (objectArray[0] != null) {
									slipReferenceToPutInMap.setId((Long) objectArray[0]);
								}
								if (objectArray[1] != null) {
									slipReferenceToPutInMap.setSequenceNumber((Long) objectArray[1]);
								}
								if (objectArray[2] != null) {
									slipReferenceToPutInMap.setSlipTypeId((Short) objectArray[2]);
								}
								if (objectArray[3] != null) {
									slipReferenceToPutInMap.setStatusFlagId((Short) objectArray[3]);
								}
								if (objectArray[4] != null) {
									slipReferenceToPutInMap.setProcessFlagId((Short) objectArray[4]);
								}
								if (objectArray[5] != null) {
									slipReferenceToPutInMap.setSiteId((Integer) objectArray[5]);
								}
								if (objectArray[6] != null) {
									slipReferenceToPutInMap.setSlotDenomination((Long) objectArray[6]);
								}
								if (objectArray[7] != null) {
									slipReferenceToPutInMap.setUpdatedTs((Timestamp) objectArray[7]);
								}
								if (objectArray[8] != null) {
									slipReferenceToPutInMap.setCreatedTs((Timestamp) objectArray[8]);
								}
								if (objectArray[9] != null) {
									slipReferenceToPutInMap.setAcnfNumber((String) objectArray[9]);
								}
								if (slipReferenceToPutInMap.getId() != null) {
									mapSlipIdToSlip.put(slipReferenceToPutInMap.getId(),
											slipReferenceToPutInMap);
								}
							}

						}
					}
				}

				// now populating the mapSlipIdToJackpot for this functionality
				ProjectionList jackpotProjectionList = Projections.projectionList();
				jackpotProjectionList.add(Projections.property("id"));
				jackpotProjectionList.add(Projections.property("slipReferenceId"));
				jackpotProjectionList.add(Projections.property("netAmount"));
				jackpotProjectionList.add(Projections.property("originalAmount"));
				Criteria jackpotCriteria = sessionObject.createCriteria(Jackpot.class).setProjection(
						jackpotProjectionList);
				if (jackpotCriteria != null) {
					List jackpotList = jackpotCriteria.list();
					if (jackpotList != null && jackpotList.size() != 0) {
						for (int j = 0; j < jackpotList.size(); j++) {
							Object[] objectArrayForJackpot = (Object[]) jackpotList.get(j);
							if (objectArrayForJackpot != null && objectArrayForJackpot.length != 0
									&& !(objectArrayForJackpot.length < 4)) {
								Jackpot jackpotToProcess = new Jackpot();
								if (objectArrayForJackpot[0] != null) {
									jackpotToProcess.setId((Long) objectArrayForJackpot[0]);
								}
								if (objectArrayForJackpot[1] != null) {
									jackpotToProcess.setSlipReferenceId((Long) objectArrayForJackpot[1]);
								}
								if (objectArrayForJackpot[2] != null) {
									jackpotToProcess.setNetAmount((Long) objectArrayForJackpot[2]);
								}

								if (objectArrayForJackpot[3] != null) {
									jackpotToProcess.setOriginalAmount((Long) objectArrayForJackpot[3]);
								}

								if (jackpotToProcess.getSlipReferenceId() != null) {
									mapSlipIdToJackpot.put(jackpotToProcess.getSlipReferenceId(),
											jackpotToProcess);
								}

							}
						}
					}
				}

				// now populating the mapSlipIdToSlipData for this functionality
				ProjectionList slipDataProjectionList = Projections.projectionList();
				slipDataProjectionList.add(Projections.property("id"));
				slipDataProjectionList.add(Projections.property("slipReferenceId"));
				slipDataProjectionList.add(Projections.property("kioskProcessed"));
				Criteria slipDataCriteria = sessionObject.createCriteria(SlipData.class).setProjection(
						slipDataProjectionList);
				if (slipDataCriteria != null) {
					List slipDataList = slipDataCriteria.list();
					if (slipDataList != null && slipDataList.size() != 0) {
						for (int k = 0; k < slipDataList.size(); k++) {
							Object[] objectArrayForSlipData = (Object[]) slipDataList.get(k);
							if (objectArrayForSlipData != null && objectArrayForSlipData.length != 0
									&& !(objectArrayForSlipData.length < 3)) {
								SlipData slipDataToPut = new SlipData();
								if (objectArrayForSlipData[0] != null) {
									slipDataToPut.setId((Long) objectArrayForSlipData[0]);
								}
								if (objectArrayForSlipData[1] != null) {
									slipDataToPut.setSlipReferenceId((Long) objectArrayForSlipData[1]);
								}
								if (objectArrayForSlipData[2] != null) {
									slipDataToPut.setKioskProcessed((String) objectArrayForSlipData[2]);
								}

								if (slipDataToPut.getSlipReferenceId() != null) {
									mapSlipIdToSlipData
											.put(slipDataToPut.getSlipReferenceId(), slipDataToPut);
								}

							}
						}
					}
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getMapsForNewWave", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getMapsForNewWave", e2);
			throw new JackpotDAOException(e2);
		}
	}

	public static String padLeftZerosForBarcode(String str, int length) {
		if (str == null || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		for (int i = str.length(); i < length; ++i) {
			sb.append("0");
		}
		sb.append(str);
		return sb.toString();
	}

	public String getBarcodeCreated(String siteNumber, Long netAmountInCents, String sequence) {
		String barcodeReturned = null;
		try {
			if (siteNumber != null && netAmountInCents != null && sequence != null) {
				String jpNetAmtStr = new DecimalFormat("0.00").format(new Double(
						centsToDollarForNewWave(netAmountInCents)));
				siteNumber = padLeftZerosForBarcode(siteNumber, 4);
				jpNetAmtStr = padLeftZerosForBarcode(jpNetAmtStr, 17);
				barcodeReturned = new String(siteNumber + jpNetAmtStr
						+ IAppConstants.JACKPOT_SLIP_SEQUENCE_NO_PREFIX + sequence);
				if (log.isInfoEnabled()) {
					log.info("Barcode generated for New wave :" + barcodeReturned);
				}
			}
		} catch (Exception e) {
			log.error("Error in barcode creation for new wave");
		}
		return barcodeReturned;
	}

	public static double centsToDollarForNewWave(long amount) {

		String amountStr = ((Long) amount).toString();
		if (amountStr.length() > 1) {
			String firstStr = amountStr.substring(0, amountStr.length() - 2);
			String secondStr = amountStr.substring(amountStr.length() - 2, amountStr.length());
			String finalStr = firstStr + "." + secondStr;
			// double doubleValue = Double.parseDouble(finalStr);

			return Double.parseDouble(finalStr);
		} else if (amountStr.length() == 1) {

			return (amount / 100d);
		} else {
			return 0;
		}
	}

	public NewWaveResponseDTO getNewWaveResponseDTOWithSeqType(Map<Long, SlipReference> mapSlipIdToSlip,
			Map<Long, Jackpot> mapSlipIdToJackpot, Map<Long, SlipData> mapSlipIdToSlipData,
			String trackingSequence, String slipLookUp, String sequenceGot, String documentType)
			throws JackpotDAOException {
		NewWaveResponseDTO newWaveResponseDTOToSend = null;
		try {

			if (sequenceGot != null && documentType != null) {
				Short documentTypeShort = null;
				Long sequenceNumberLong = null;
				try {
					documentTypeShort = Short.parseShort(documentType);
					sequenceNumberLong = Long.parseLong(sequenceGot);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (documentTypeShort != null && sequenceNumberLong != null) {
					for (Long slipIdToCheck : mapSlipIdToSlip.keySet()) {
						if (slipIdToCheck != null) {
							SlipReference slipReferenceToCheck = mapSlipIdToSlip.get(slipIdToCheck);
							if (slipReferenceToCheck != null
									&& slipReferenceToCheck.getSlipTypeId() != null
									&& slipReferenceToCheck.getSequenceNumber() != null
									&& documentTypeShort.shortValue() == slipReferenceToCheck.getSlipTypeId()
											.shortValue()
									&& sequenceNumberLong.longValue() == slipReferenceToCheck
											.getSequenceNumber().longValue()) {
								newWaveResponseDTOToSend = new NewWaveResponseDTO();

								newWaveResponseDTOToSend.setDenomination(slipReferenceToCheck
										.getSlotDenomination());
								newWaveResponseDTOToSend.setDocumentType(slipReferenceToCheck.getSlipTypeId()
										.toString());
								if (slipReferenceToCheck.getProcessFlagId() != null
										&& slipReferenceToCheck.getProcessFlagId() == ILookupTableConstants.MANUAL_PROCESS_FLAG) {
									newWaveResponseDTOToSend.setIsManual(true);
								} else {
									newWaveResponseDTOToSend.setIsManual(false);
								}

								Jackpot jackpotToUse = mapSlipIdToJackpot.get(slipReferenceToCheck.getId());
								if (jackpotToUse != null) {
									if (jackpotToUse.getNetAmount() != null) {
										newWaveResponseDTOToSend
												.setJackpotAmount(jackpotToUse.getNetAmount());
										String barcodeToSet = getBarcodeCreated(slipReferenceToCheck
												.getSiteId().toString(), jackpotToUse.getNetAmount(),
												slipReferenceToCheck.getSequenceNumber().toString());
										newWaveResponseDTOToSend.setBarcode(barcodeToSet);
									} else {
										newWaveResponseDTOToSend.setJackpotAmount(jackpotToUse
												.getOriginalAmount());
										newWaveResponseDTOToSend.setBarcode(null);
									}

								}

								SlipData slipDataToCheck = mapSlipIdToSlipData.get(slipReferenceToCheck
										.getId());
								if (slipDataToCheck != null && slipDataToCheck.getKioskProcessed() != null) {
									newWaveResponseDTOToSend.setProcessedBoothId(slipDataToCheck
											.getKioskProcessed());
								} else {
									newWaveResponseDTOToSend.setProcessedBoothId(null);
								}

								newWaveResponseDTOToSend.setProcessedSlotId(slipReferenceToCheck
										.getAcnfNumber());
								newWaveResponseDTOToSend.setSiteNumber(slipReferenceToCheck.getSiteId()
										.toString());
								newWaveResponseDTOToSend.setSlipLookUp(slipLookUp);// got
																					// from
																					// request
								newWaveResponseDTOToSend.setSlipSequence(slipReferenceToCheck
										.getSequenceNumber().toString());
								newWaveResponseDTOToSend.setTrackingSequence(trackingSequence);// got
																								// from
																								// request
								newWaveResponseDTOToSend.setTransactionDateTime(slipReferenceToCheck
										.getCreatedTs().toString());
								if (slipReferenceToCheck.getStatusFlagId() != null
										&& slipReferenceToCheck.getStatusFlagId() == ILookupTableConstants.VOID_STATUS_ID
										&& slipReferenceToCheck.getUpdatedTs() != null) {
									newWaveResponseDTOToSend.setVoidedDateTime(slipReferenceToCheck
											.getUpdatedTs().toString());
								} else {
									newWaveResponseDTOToSend.setVoidedDateTime(null);
								}
								break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Exception in getNewWaveResponseDTOWithSeqType", e);
		}
		return newWaveResponseDTOToSend;
	}

	public NewWaveResponseDTO getNewWaveResponseDTO(Map<Long, SlipReference> mapSlipIdToSlip,
			Map<Long, Jackpot> mapSlipIdToJackpot, Map<Long, SlipData> mapSlipIdToSlipData,
			BarcodeDetailsDTO barcodeDetailsDTO, String trackingSequence, String slipLookUp)
			throws JackpotDAOException {
		NewWaveResponseDTO newWaveResponseDTOToSend = null;
		try {
			String siteNumber = barcodeDetailsDTO.getSiteNumber();
			String netAmountInDollar = barcodeDetailsDTO.getNetAmount();
			String sequenceNumber = barcodeDetailsDTO.getSequence();
			if (siteNumber != null && sequenceNumber != null) {
				Integer siteIdInteger = null;
				Long sequenceNumberLong = null;
				try {
					siteIdInteger = Integer.parseInt(siteNumber);
					sequenceNumberLong = Long.parseLong(sequenceNumber);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (siteIdInteger != null && sequenceNumberLong != null) {
					for (Long slipIdToCheck : mapSlipIdToSlip.keySet()) {
						if (slipIdToCheck != null) {
							SlipReference slipReferenceToCheck = mapSlipIdToSlip.get(slipIdToCheck);

							if (slipReferenceToCheck != null
									&& slipReferenceToCheck.getSiteId() != null
									&& slipReferenceToCheck.getSequenceNumber() != null
									&& siteIdInteger.intValue() == slipReferenceToCheck.getSiteId()
											.intValue()
									&& sequenceNumberLong.longValue() == slipReferenceToCheck
											.getSequenceNumber().longValue()
									&& slipReferenceToCheck.getSlipTypeId() != null
									&& slipReferenceToCheck.getSlipTypeId().shortValue() == 3001) {

								Jackpot jackpotToUse = mapSlipIdToJackpot.get(slipReferenceToCheck.getId());
								if (jackpotToUse != null) {
									if (jackpotToUse.getNetAmount() != null) {
										String jpNetAmtStrFromDB = new DecimalFormat("0.00")
												.format(new Double(centsToDollarForNewWave(jackpotToUse
														.getNetAmount())));
										if (log.isInfoEnabled()) {
											log.info("Amount in DB :" + jpNetAmtStrFromDB);
											log.info("Amount Got :" + netAmountInDollar);
										}
										if (jpNetAmtStrFromDB != null
												&& jpNetAmtStrFromDB.trim().equalsIgnoreCase(
														netAmountInDollar)) {

										} else {
											continue;
										}
									}
								}

								newWaveResponseDTOToSend = new NewWaveResponseDTO();

								newWaveResponseDTOToSend.setDenomination(slipReferenceToCheck
										.getSlotDenomination());
								newWaveResponseDTOToSend.setDocumentType(slipReferenceToCheck.getSlipTypeId()
										.toString());
								if (slipReferenceToCheck.getProcessFlagId() != null
										&& slipReferenceToCheck.getProcessFlagId() == ILookupTableConstants.MANUAL_PROCESS_FLAG) {
									newWaveResponseDTOToSend.setIsManual(true);
								} else {
									newWaveResponseDTOToSend.setIsManual(false);
								}

								if (jackpotToUse != null) {
									if (jackpotToUse.getNetAmount() != null) {
										newWaveResponseDTOToSend
												.setJackpotAmount(jackpotToUse.getNetAmount());
										String barcodeToSet = getBarcodeCreated(slipReferenceToCheck
												.getSiteId().toString(), jackpotToUse.getNetAmount(),
												slipReferenceToCheck.getSequenceNumber().toString());
										newWaveResponseDTOToSend.setBarcode(barcodeToSet);
									} else {
										newWaveResponseDTOToSend.setJackpotAmount(jackpotToUse
												.getOriginalAmount());
										newWaveResponseDTOToSend.setBarcode(null);
									}

								}

								SlipData slipDataToCheck = mapSlipIdToSlipData.get(slipReferenceToCheck
										.getId());
								if (slipDataToCheck != null && slipDataToCheck.getKioskProcessed() != null) {
									newWaveResponseDTOToSend.setProcessedBoothId(slipDataToCheck
											.getKioskProcessed());
								} else {
									newWaveResponseDTOToSend.setProcessedBoothId(null);
								}

								newWaveResponseDTOToSend.setProcessedSlotId(slipReferenceToCheck
										.getAcnfNumber());
								newWaveResponseDTOToSend.setSiteNumber(slipReferenceToCheck.getSiteId()
										.toString());
								newWaveResponseDTOToSend.setSlipLookUp(slipLookUp);// got
																					// from
																					// request
								newWaveResponseDTOToSend.setSlipSequence(slipReferenceToCheck
										.getSequenceNumber().toString());
								newWaveResponseDTOToSend.setTrackingSequence(trackingSequence);// got
																								// from
																								// request
								newWaveResponseDTOToSend.setTransactionDateTime(slipReferenceToCheck
										.getCreatedTs().toString());
								if (slipReferenceToCheck.getStatusFlagId() != null
										&& slipReferenceToCheck.getStatusFlagId() == ILookupTableConstants.VOID_STATUS_ID
										&& slipReferenceToCheck.getUpdatedTs() != null) {
									newWaveResponseDTOToSend.setVoidedDateTime(slipReferenceToCheck
											.getUpdatedTs().toString());
								} else {
									newWaveResponseDTOToSend.setVoidedDateTime(null);
								}
								break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Exception in getNewWaveResponseDTO", e);
		}
		return newWaveResponseDTOToSend;
	}

	public List<NewWaveResponseDTO> getNewWaveDetails(NewWaveRequestDTO newWaveRequestDTO)
			throws JackpotDAOException {
		List<NewWaveResponseDTO> listToSend = null;
		if (log.isInfoEnabled()) {
			log.info("Inside getNewWaveDetails method of JackpotCommonDAO");
		}
		try {
			if (newWaveRequestDTO != null) {
				Map<Long, SlipReference> mapSlipIdToSlip = new HashMap<Long, SlipReference>();
				Map<Long, Jackpot> mapSlipIdToJackpot = new HashMap<Long, Jackpot>();
				Map<Long, SlipData> mapSlipIdToSlipData = new HashMap<Long, SlipData>();

				// code to populate db values.
				getMapsForNewWave(mapSlipIdToSlip, mapSlipIdToJackpot, mapSlipIdToSlipData);
				if (log.isDebugEnabled()) {
					log.debug("size 1:" + mapSlipIdToSlip.size());
					log.debug("size 2:" + mapSlipIdToJackpot.size());
					log.debug("size 3:" + mapSlipIdToSlipData.size());
				}
				listToSend = new ArrayList<NewWaveResponseDTO>();
				if (newWaveRequestDTO.getBarcodeList() != null
						|| newWaveRequestDTO.getSequenceWithTypes() != null) {
					List<String> barcodeList = newWaveRequestDTO.getBarcodeList();

					if (barcodeList != null && barcodeList.size() != 0) {
						for (String barcode : barcodeList) {
							if (barcode != null && !((barcode.trim() + " ").equalsIgnoreCase(" "))) {
								BarcodeDetailsDTO barcodeDetailsDTO = new BarcodeDetailsDTO(barcode.trim());
								NewWaveResponseDTO newWaveResponseDTOToAdd = getNewWaveResponseDTO(
										mapSlipIdToSlip, mapSlipIdToJackpot, mapSlipIdToSlipData,
										barcodeDetailsDTO, newWaveRequestDTO.getTrackingSequence(),
										newWaveRequestDTO.getSlipLookUp());
								if (newWaveResponseDTOToAdd != null) {
									listToSend.add(newWaveResponseDTOToAdd);
								}

							}
						}
					}

					List<SequenceWithTypeDTO> listSequenceWithTypes = newWaveRequestDTO
							.getSequenceWithTypes();

					if (listSequenceWithTypes != null && listSequenceWithTypes.size() != 0) {
						for (SequenceWithTypeDTO sequenceWithTypeDTO : listSequenceWithTypes) {
							if (sequenceWithTypeDTO != null) {
								NewWaveResponseDTO newWaveResponseDTOToAddHere = getNewWaveResponseDTOWithSeqType(
										mapSlipIdToSlip, mapSlipIdToJackpot, mapSlipIdToSlipData,
										newWaveRequestDTO.getTrackingSequence(),
										newWaveRequestDTO.getSlipLookUp(),
										sequenceWithTypeDTO.getJackpotSequence(),
										sequenceWithTypeDTO.getDocumentType());
								if (newWaveResponseDTOToAddHere != null) {
									listToSend.add(newWaveResponseDTOToAddHere);
								}
							}
						}
					}
					if (listToSend != null) {
						if (log.isInfoEnabled()) {
							log.info("List of NewWaveResponseDTO size:" + listToSend.size());
						}
					} else if (log.isInfoEnabled()) {
						log.info("List of NewWaveResponseDTO size: 0, since its null");
					}

				} else {
					if (log.isDebugEnabled()) {
						log.debug("Needed barcode and sequence list null");
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Input arg newWaveRequestDTO null");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getNewWaveDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getNewWaveDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return listToSend;
	}

	/**
	 * 
	 * This method is used to manipulate the original amount with Progressive
	 * engine posted amount.
	 * 
	 * @param acnfNumber
	 * @param msgId
	 * @param jackpotId
	 * @param sdsAmount
	 * @param pmuAmount
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean processProgressiveJackpotAmount(String acnfNumber, Long msgId, Long sdsAmount,
			Long pmuAmount, int siteId) throws JackpotDAOException {
		boolean processStatus = false;
		try {
			if (acnfNumber != null && !((acnfNumber.trim() + " ").equalsIgnoreCase(" "))) {
				/** Get the system current date/time * */
				if (log.isInfoEnabled()) {
					log.info("*** Inside processProgressiveJackpotAmount method updating the jackpot amount with amount from PMU ***");
				}

				long currntTime = System.currentTimeMillis();
				Timestamp currentDate = new Timestamp(currntTime);
				if (log.isInfoEnabled()) {
					log.info("Current date " + currentDate);
				}

				Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
				Criterion criterionAssetNumber = Restrictions.eq("acnfNumber", acnfNumber.trim());
				Criterion criterionSlipTypeId = Restrictions.and(criterionAssetNumber,
						Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
				Criterion criterionStatusFlagId = Restrictions.and(criterionSlipTypeId,
						Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
				Criterion criterionProcessFlag = Restrictions.and(criterionStatusFlagId,
						Restrictions.isNull("processFlagId"));
				Criterion crtFinal = Restrictions.and(criterionProcessFlag, Restrictions.eq("siteId", siteId));
				// Criterion
				// criterionSiteNumber=Restrictions.and(criterionProcessFlag,
				// Restrictions.eq("siteId", siteNumber));
				Criteria criteriaSlipRef = sessionObj.createCriteria(SlipReference.class)
						.add(crtFinal).addOrder(Order.desc("createdTs"));
				if (criteriaSlipRef != null) {
					List<SlipReference> listSlipReference = criteriaSlipRef.list();
					if (listSlipReference != null && listSlipReference.size() != 0) {
						// iterating through all slip reference in desc order
						// till we find a progressive jackpot.
						for (SlipReference slipReferenceToProcess : listSlipReference) {
							if (slipReferenceToProcess != null) {
								Set<Jackpot> jackpotSetGot = slipReferenceToProcess.getJackpot();
								if (jackpotSetGot != null && jackpotSetGot.size() != 0) {
									Jackpot jackpotToCheckFor = null;
									for (Jackpot jackpotToProcessFor : jackpotSetGot) {
										if (jackpotToProcessFor != null) {
											jackpotToCheckFor = jackpotToProcessFor;
											break;
										}
									}

									if (jackpotToCheckFor != null) {
										// check for jackpot validity here.
										// check includes check for amount to
										// zero and jackpot id and type matching
										// and jackpot id within range.
										if ((Integer.parseInt(jackpotToCheckFor.getJackpotId(), 16) >= 112 && Integer
												.parseInt(jackpotToCheckFor.getJackpotId(), 16) <= 159)
												&& (jackpotToCheckFor.getJackpotTypeId().shortValue() == JACKPOT_TYPE_lINKED_PROGRESSIVE || jackpotToCheckFor
														.getJackpotTypeId().shortValue() == JACKPOT_TYPE_PROGRESSIVE)
												// &&
												// jackpotId.trim().equalsIgnoreCase(jackpotToCheckFor.getJackpotId().trim())
												&& msgId.longValue() == slipReferenceToProcess.getMessageId()
														.longValue()) {
											if (log.isInfoEnabled()) {
												log.info(" Process Jackpot Record Id :"
														+ jackpotToCheckFor.getId());
											}

											// This flag is used to determine
											// whether a prog jp has hit or a
											// combined jp is hit.
											boolean isACombinedJackpot = false;
											if (jackpotToCheckFor.getOriginalAmount().longValue() > 0) {
												if (log.isInfoEnabled()) {
													log.info("Is a combined jackpot hit occured for this slot, processing....");
												}
												isACombinedJackpot = true;
											}

											// default is pmu amount.
											boolean useSDSAmount = false;
											String valueUseSDSAmount = JackpotUtil.getSiteParamValue(
													ISiteConfigConstants.USE_SDS_AMT_OR_PMU_AMT,
													slipReferenceToProcess.getSiteId());
											if (valueUseSDSAmount != null
													&& !((valueUseSDSAmount.trim() + " ")
															.equalsIgnoreCase(" "))) {
												if (log.isInfoEnabled()) {
													log.info("Site Value Got for USE_SDS_AMT_OR_PMU_AMT :"
															+ valueUseSDSAmount + ".");
												}
												if (valueUseSDSAmount.trim().equalsIgnoreCase("C")) {
													useSDSAmount = false;
												} else if (valueUseSDSAmount.trim().equalsIgnoreCase("S")) {
													useSDSAmount = true;
												}
												if (log.isInfoEnabled()) {
													log.info("Site Value Got for Use SDS Amount(S) Instead of PMU Controller Amount(C) :"
															+ useSDSAmount);
												}
											}
											// This method would be called twice
											// to send the sdsAmount and
											// pmuAmout seperately.
											// Hence the check for not null and
											// not equal to zero is done to
											// avoid updating the
											// existing value of sdsAmount or
											// pmuAmount to zero
											if (sdsAmount != null && sdsAmount.longValue() != 0) {
												jackpotToCheckFor.setCalculatedSDSAmount(sdsAmount);
											}
											if (pmuAmount != null && pmuAmount.longValue() != 0) {
												jackpotToCheckFor.setPromptedPMUAmount(pmuAmount);
											}

											if (useSDSAmount) {
												// Fireball Progressive Change
												if (log.isInfoEnabled()) {
													log.info("Finally Using SDS Calculated Amount....");
													log.info("SDS Amount :" + sdsAmount);
													log.info("Original Amount :"
															+ jackpotToCheckFor.getOriginalAmount()
																	.longValue());
												}
												jackpotToCheckFor.setPmuAmountUsed(new Integer("0"));
												jackpotToCheckFor.setOriginalAmount(jackpotToCheckFor
														.getOriginalAmount().longValue() + sdsAmount);
												jackpotToCheckFor.setUpdatedTs(DateHelper
														.getUTCTimeFromLocal(currentDate));
												jackpotToCheckFor
														.setUpdatedUser(IAppConstants.PROG_ENGINE_ID);
												sessionObj.saveOrUpdate(jackpotToCheckFor);
											} else {
												// usage of PMU amount.
												if (log.isInfoEnabled()) {
													log.info("Finally Using PMU Posted Amount....");
												}
												jackpotToCheckFor.setPmuAmountUsed(new Integer("1"));
												if (isACombinedJackpot) {
													jackpotToCheckFor.setOriginalAmount((jackpotToCheckFor
															.getOriginalAmount().longValue())
															+ (pmuAmount.longValue()));
												} else {
													jackpotToCheckFor.setOriginalAmount(pmuAmount);
												}
												jackpotToCheckFor.setUpdatedTs(DateHelper
														.getUTCTimeFromLocal(currentDate));
												jackpotToCheckFor
														.setUpdatedUser(IAppConstants.PROG_ENGINE_ID);
												sessionObj.saveOrUpdate(jackpotToCheckFor);
											}
											processStatus = true;
											break;
										}
									}
								}
							}
						}
					} else if (log.isInfoEnabled()) {
						log.info("***No Pending Slip Reference For the current criterion selection***");
					}
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in processProgressiveJackpotAmount", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in processProgressiveJackpotAmount", e2);
			throw new JackpotDAOException(e2);
		}
		return processStatus;
	}

	/**
	 * Method to get the Pending/Invalid Jackpots that were not reset and to get
	 * the latest rec for each slot
	 * 
	 * @param siteId
	 * @return List<JackpotResetDTO>
	 * @throws JackpotDAOException
	 */
	public List<JackpotResetDTO> getUnResetPendingJackpots(int siteId) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside getUnResetPendingJackpotsDAO method");
		}
		List<JackpotResetDTO> newList = null;
		try {
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();

			List<Short> statusFlgLst = new ArrayList<Short>();
			statusFlgLst.add(ILookupTableConstants.PENDING_STATUS_ID);
			statusFlgLst.add(ILookupTableConstants.INVALID_STATUS_ID);

			Projection prjId = Projections.property("id");
			Projection prjSlotNo = Projections.property("acnfNumber");
			Projection prjTransDate = Projections.property("transactionDate");
			Projection prjSeqNo = Projections.property("sequenceNumber");

			Projection prjJackpotId = Projections.property("jp.jackpotId");
			Projection prjOrgAmt = Projections.property("jp.originalAmount");
			Projection prjAssPlyCard = Projections.property("jp.associatedPlayerCard");

			Projection prjEmpCard = Projections.property("trans.employeeCardNo");
			Projection prjTrStFlg = Projections.property("trans.statusFlagId");

			ProjectionList prjList = Projections.projectionList();
			prjList.add(prjId);
			prjList.add(prjSlotNo);
			prjList.add(prjTransDate);
			prjList.add(prjSeqNo);
			prjList.add(prjJackpotId);
			prjList.add(prjOrgAmt);
			prjList.add(prjAssPlyCard);
			prjList.add(prjEmpCard);
			prjList.add(prjTrStFlg);

			Order orderBySlprId = Order.asc("id");
			Order orderByStsfId = Order.asc("trans.statusFlagId");
			Order orderBySlotNo = Order.asc("acnfNumber");

			Criterion crtSiteNo = Restrictions.eq("siteId", siteId);
			Criterion crtSlprStatus = Restrictions.and(crtSiteNo,
					Restrictions.in("statusFlagId", statusFlgLst));

			Criteria slipRefCriteria = sessionObj.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.createAlias("transaction", "trans", Criteria.LEFT_JOIN).add(crtSlprStatus)
					.setProjection(prjList).addOrder(orderBySlprId).addOrder(orderByStsfId)
					.addOrder(orderBySlotNo);

			List<Object[]> slipObjAry = (List<Object[]>) slipRefCriteria.list();
			long tmpSlprId = 0l;
			if (slipObjAry != null && slipObjAry.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("slipObjAry.size(): " + slipObjAry.size());
				}
				List<JackpotResetDTO> pendingResetLst = new ArrayList<JackpotResetDTO>();
				JackpotResetDTO jpDto = null;
				for (Object[] obj : slipObjAry) {

					long slprId = ((Long) obj[0]);
					short stsfId = ((Short) obj[8]);
					if (tmpSlprId == slprId && stsfId == ILookupTableConstants.CARD_IN_STATUS_ID) {
						jpDto.setEmployeeCardNo((String) obj[7]);
					} else if (tmpSlprId == slprId && stsfId == ILookupTableConstants.JP_CLEAR_STATUS_ID) {
						pendingResetLst.remove(jpDto);
					} else {
						jpDto = new JackpotResetDTO();
						jpDto.setSlotNo((String) obj[1]);
						jpDto.setTransactionDate(DateHelper.getLocalTimeFromUTC(((Date) obj[2]).getTime()));
						jpDto.setSequenceNumber((Long) obj[3]);
						jpDto.setJackpotId((String) obj[4]);
						jpDto.setJackpotAmount((Long) obj[5]);
						jpDto.setPlayerCard((String) obj[6]);
						jpDto.setEmployeeCardNo((String) obj[7]);
						pendingResetLst.add(jpDto);
						tmpSlprId = slprId;
					}
				}

				if (pendingResetLst != null && pendingResetLst.size() > 0) {
					Collections.sort(pendingResetLst, new JpResetDTOComparator("Slot"));

					JackpotResetDTO dto = null;
					String tmpSlotNo = null;
					JackpotResetDTO jackpotResetDTO = null;
					Date tmpDate = null;

					newList = new ArrayList<JackpotResetDTO>();

					for (int i = 0; i < pendingResetLst.size(); i++) {
						dto = pendingResetLst.get(i);
						String slotNo = dto.getSlotNo();
						Date date = dto.getTransactionDate();

						if (tmpSlotNo == null) {
							tmpSlotNo = slotNo;
							tmpDate = date;
							jackpotResetDTO = dto;
						} else if (tmpSlotNo.equalsIgnoreCase(slotNo)) {
							if (tmpDate.getTime() < date.getTime()) {
								tmpDate = date;
								jackpotResetDTO = dto;
							}
						} else {
							newList.add(jackpotResetDTO);
							tmpSlotNo = slotNo;
							tmpDate = date;
							jackpotResetDTO = dto;
						}

					}

					if (jackpotResetDTO != null) {
						newList.add(jackpotResetDTO);
					}

					if (log.isDebugEnabled() && newList != null && newList.size() > 0) {
						log.debug("newList.size(): " + newList.size());
						for (JackpotResetDTO dto1 : newList) {
							log.debug(dto1.toString());
						}
					}

					if (log.isInfoEnabled()) {
						log.info("The unreset jp records have been returned for the site id: " + siteId);
					}

				} else {
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("There are no unreset jp records for the site id: " + siteId);
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no unreset records for the site id: " + siteId);
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			}

		} catch (HibernateException e) {
			log.error("Hibernate Exception in getUnResetPendingJackpots", e);
			log.warn("Hibernate Exception in getUnResetPendingJackpots", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getUnResetPendingJackpots", e2);
			log.warn("Exception in getUnResetPendingJackpots", e2);
			throw new JackpotDAOException(e2);
		}
		return newList;
	}

	/**
	 * DAO method to create the jackpot slip from an external system 
	 * @param jackpotDTO
	 * @param transExcepCode
	 * @param jpGeneratedBy
	 * @return JackpotDTO
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	public static JackpotDTO createJackpotFromExternalSystem(JackpotDTO jackpotDTO, short transExcepCode, JpGeneratedBy jpGeneratedBy) throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside createJackpotFromExternalSystem DAO method");
			}
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			jackpotDTO.setPostedSuccessfully(true);
			int iJackpotId = Integer.parseInt(jackpotDTO.getJackpotId(), IAppConstants.HEXA_RADIX);
			
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
			
			if(jackpotDTO.getOriginalAmount() == 0
					&& (jackpotDTO.getJackpotId() == IJackpotIds.JACKPOT_ID_252_FC_STR 
							|| jackpotDTO.getJackpotId() == IJackpotIds.JACKPOT_ID_253_FD_STR
							|| jackpotDTO.getJackpotId() == IJackpotIds.JACKPOT_ID_254_FE_STR 
							|| jackpotDTO.getJackpotId() == IJackpotIds.JACKPOT_ID_250_FA_STR)){
				
				slipRefererence.setStatusFlagId(ILookupTableConstants.INVALID_STATUS_ID);
				jackpotDTO.setPostedSuccessfully(false);
				
			}else if (iJackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
					&& iJackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
					&& ((iJackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
									&& iJackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)
									|| iJackpotId == IJackpotIds.JACKPOT_ID_252_FC
									|| iJackpotId == IJackpotIds.JACKPOT_ID_253_FD
									|| iJackpotId == IJackpotIds.JACKPOT_ID_254_FE
									|| iJackpotId == IJackpotIds.JACKPOT_ID_250_FA 
									|| iJackpotId == IJackpotIds.JACKPOT_ID_251_FB )) {
				
				slipRefererence.setStatusFlagId(jackpotDTO.getStatusFlagId());
			}else{
				slipRefererence.setStatusFlagId(ILookupTableConstants.INVALID_STATUS_ID);
				jackpotDTO.setPostedSuccessfully(false);
			}
			
			if (log.isDebugEnabled()) {
				log.debug("Slip Ref Id: " + slipRefererence.getId());
				log.debug("Status: "+jackpotDTO.getStatusFlagId());
			}
			
			//GENERATE THE SEQ NO FIELD
			if (jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("yes")) {
				// Setting sequence number as combination number to make it
				// random for SA - Peermont
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
			slipRefererence.setMessageId(jackpotDTO.getMessageId());			
			slipRefererence.setTransactionDate(DateHelper.getUTCTimeFromLocal(jackpotDTO.getTransactionDate().getTime()));
			slipRefererence.setGmuDenomination(jackpotDTO.getGmuDenom());
			slipRefererence.setSlotDenomination(jackpotDTO.getSlotDenomination());
			if (jackpotDTO.getSlotDescription() != null) {
				slipRefererence.setSlotDescription(jackpotDTO.getSlotDescription());
			}			
			slipRefererence.setSiteId(jackpotDTO.getSiteId());
			slipRefererence.setSiteNo(Integer.toString(jackpotDTO.getSiteId()));			
			slipRefererence.setPostToAccounting(NOT_POSTED_TO_ACCOUNTING);
			slipRefererence.setAcnfNumber(jackpotDTO.getAssetConfigNumber());
			slipRefererence.setAcnfLocation(jackpotDTO.getAssetConfigLocation().toUpperCase());
			slipRefererence.setSealNumber(jackpotDTO.getSealNumber());
			slipRefererence.setAreaShortName(jackpotDTO.getAreaShortName());
			slipRefererence.setAreaLongName(jackpotDTO.getAreaLongName());
			slipRefererence.setChangeFlag(IAppConstants.CHANGE_FLAG_DISABLED);
			slipRefererence.setAccountType(jackpotDTO.getCashlessAccountType());
			slipRefererence.setAccountNumber(jackpotDTO.getAccountNumber());
			slipRefererence.setCreatedTs(DateHelper.getUTCTimeFromLocal(System.currentTimeMillis()));
			sessionObj.save(slipRefererence);

			/** Create a record in the Jackpot table */
			Jackpot jackpot = new Jackpot();
			jackpot.setId(jackpotDTO.getSlipPrimaryKey());
			jackpot.setSlipReferenceId(slipRefererence.getId());
			jackpot.setJackpotId(jackpotDTO.getJackpotId());
			jackpot.setIsSlotOnline(jackpotDTO.isSlotOnline() ? new Short("1") : new Short("0"));
			jackpot.setAssociatedPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
			
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
			} else if ((iJackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
							&& iJackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)) {
				jackpot.setJackpotTypeId(JackpotHelper.getProgressiveType(
						jackpotDTO.getAssetConfigNumber(), jackpotDTO.getSiteId()));
			} else {
				jackpot.setJackpotTypeId(JACKPOT_TYPE_REGULAR);
			}			
			jackpot.setOriginalAmount(jackpotDTO.getOriginalAmount());
			jackpot.setBlindAttempt(BLIND_ATTEMPT);
			jackpot.setGeneratedBy(jpGeneratedBy.getValue());//FOR CONTROLLER CREATED JPS DIFFERENTIATION
			jackpot.setCreatedTs(DateHelper.getUTCTimeFromLocal(System.currentTimeMillis()));
			sessionObj.save(jackpot);

			/** Create a record in TRANSACTION table */
			Transaction transaction = new Transaction();
			transaction.setId(jackpotDTO.getSlipPrimaryKey());
			transaction.setSlipReferenceId(slipRefererence.getId());
			transaction.setStatusFlagId(slipRefererence.getStatusFlagId());	
			transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
			transaction.setMessageId(jackpotDTO.getMessageId());
			transaction.setExceptionCodeId(transExcepCode);
			transaction.setCreatedTs(DateHelper.getUTCTimeFromLocal(System.currentTimeMillis()));
			sessionObj.save(transaction);
			log.debug("*************************************************************************************");
			if (log.isInfoEnabled()) {
				log.info("Jackpot slip created successfully with the sequence no : "+ slipRefererence.getSequenceNumber());
			}
			log.debug("*************************************************************************************");
			
		} catch (HibernateException e) {
			log.error("HibernateException in createJackpotFromExternalSystem DAO: ", e);
			log.warn("HibernateException in createJackpotFromExternalSystem DAO: ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in createJackpotFromExternalSystem DAO: ", e2);
			log.warn("Exception in createJackpotFromExternalSystemDAO: ", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}
	
	
	/**
	 * Main method of the class
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();

		JackpotDTO jackpotDTO = new JackpotDTO();

		Timestamp createDate = new Timestamp(System.currentTimeMillis());

		Date date = new Date();

		try {
			Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
			org.hibernate.Transaction transObj = sessionObj.beginTransaction();

			List<JackpotResetDTO> dtoLst = jackpotCommonDAO.getUnResetPendingJackpots(512);
			// for(JackpotResetDTO dto:dtoLst)
			// log.debug(dto.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** test for getJackpotDetailsForS2S **/

		/*
		 * JackpotDetailsDTO jpDetDTO =
		 * jackpotCommonDAO.getJackpotDetailsForS2S(1, 1); if(jpDetDTO!=null){
		 * log.debug("Status Flag id: "+jpDetDTO.getStatusFlagDesc());
		 * log.debug("Slot No: "+jpDetDTO.getAssetConfigNumber());
		 * log.debug("Jp Type Id: "+jpDetDTO.getJackpotTypeId()); }
		 *//** test for getProgressiveJackpotInfo **/
		/*
		 * Date date1 = new Date("7/15/2008");
		 * 
		 * Date date2 = new Date("7/25/2008");
		 * 
		 * long startTime = date1.getTime()-99999777; long endTime =
		 * date2.getTime();
		 * 
		 * List<JackpotDetailsDTO> rtnList =
		 * jackpotCommonDAO.getProgressiveJackpotInfo(1, startTime, endTime);
		 * log.debug(rtnList.get(0).toString());
		 * log.debug(rtnList.get(1).toString());
		 * //log.debug(rtnList.get(0).getSequenceNumber());
		 * //log.debug(rtnList.get(0).getJackpotId());
		 *//** test for voidPendingJackpotSlipsForAuditForDate **/
		/*
		 * List<JackpotDTO> dtoList =
		 * jackpotCommonDAO.updateVoidPendingJackpotSlipsForAuditProcess(1,
		 * endTime); log.debug("DTO list size: "+dtoList.size()); //
		 * transObj.commit();
		 *//** test for voidPendingJackpotSlipsForAuditForDate **/
		/*
		 * Date gamingDate = new Date("6/28/2008"); List<JackpotDTO> dtoList =
		 * jackpotCommonDAO.updateVoidPendingJackpotSlipsForAuditProcess(1,
		 * gamingDate.getTime(), "2000");
		 * log.debug("DTO list size: "+dtoList.size()); // transObj.commit();
		 *//** test for getNumOfJackpots */
		/*
		 * Date fromDate = new Date("11/28/2008"); long count =
		 * jackpotCommonDAO.getNumOfJackpots(fromDate, 1);
		 * log.debug("Count: "+count);
		 *//** test for getNumOfJackpots 2nd method */
		/*
		 * Date fromDate = new Date("11/28/2008"); Date toDate = new
		 * Date("02/12/2008"); long count =
		 * jackpotCommonDAO.getNumOfJackpots(fromDate,toDate, 1);
		 * log.debug("Count: "+count);
		 * 
		 * 
		 * JackpotCommonDAO jackpotCommonDAO2=new JackpotCommonDAO();
		 * NewWaveRequestDTO newWaveRequestDTO = new NewWaveRequestDTO();
		 * newWaveRequestDTO.setSlipLookUp("CSS");
		 * newWaveRequestDTO.setTrackingSequence("TRS");
		 * newWaveRequestDTO.setSequenceWithTypes(null); List<String>
		 * listOfBarcode=new ArrayList<String>();
		 * listOfBarcode.add("051200000999999999.00J4");
		 * newWaveRequestDTO.setBarcodeList(listOfBarcode);
		 * List<NewWaveResponseDTO> list=
		 * jackpotCommonDAO2.getNewWaveDetails(newWaveRequestDTO);
		 * log.debug("List Size :"+list.size());
		 */

	}

	/*
	 * public static void main(String[] args) { try{ JackpotCommonDAO
	 * jackpotCommonDAO2=new JackpotCommonDAO(); NewWaveRequestDTO
	 * newWaveRequestDTO = new NewWaveRequestDTO();
	 * newWaveRequestDTO.setSlipLookUp("CSS");
	 * newWaveRequestDTO.setTrackingSequence("TRS");
	 * newWaveRequestDTO.setSequenceWithTypes(null); List<String>
	 * listOfBarcode=new ArrayList<String>();
	 * listOfBarcode.add("051200000999999999.00J4");
	 * listOfBarcode.add("000100000000000014.00J6");
	 * newWaveRequestDTO.setBarcodeList(listOfBarcode);
	 * List<SequenceWithTypeDTO> listss=new ArrayList<SequenceWithTypeDTO>();
	 * SequenceWithTypeDTO sequenceWithTypeDTO=new SequenceWithTypeDTO();
	 * sequenceWithTypeDTO.setDocumentType("3001");
	 * sequenceWithTypeDTO.setJackpotSequence("1");
	 * listss.add(sequenceWithTypeDTO);
	 * newWaveRequestDTO.setSequenceWithTypes(listss); List<NewWaveResponseDTO>
	 * list= jackpotCommonDAO2.getNewWaveDetails(newWaveRequestDTO);
	 * log.debug("List Size :"+list.size()); for(NewWaveResponseDTO
	 * newWaveResponseDTOHere:list ){ log.debug("-----------------------");
	 * log.debug(""+newWaveResponseDTOHere.getBarcode());
	 * log.debug(""+newWaveResponseDTOHere.getDocumentType());
	 * log.debug(""+newWaveResponseDTOHere.getProcessedBoothId());
	 * log.debug(""+newWaveResponseDTOHere.getProcessedSlotId());
	 * log.debug(""+newWaveResponseDTOHere.getSiteNumber());
	 * log.debug(""+newWaveResponseDTOHere.getSlipLookUp());
	 * log.debug(""+newWaveResponseDTOHere.getSlipSequence());
	 * log.debug(""+newWaveResponseDTOHere.getTrackingSequence());
	 * log.debug(""+newWaveResponseDTOHere.getDenomination());
	 * log.debug(""+newWaveResponseDTOHere.getVoidedDateTime());
	 * log.debug(""+newWaveResponseDTOHere.getTransactionDateTime());
	 * log.debug(""+newWaveResponseDTOHere.getJackpotAmount());
	 * log.debug(""+newWaveResponseDTOHere.getIsManual());
	 * log.debug("-----------------------"); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }catch (Exception e) { e.printStackTrace(); } }
	 */

}
