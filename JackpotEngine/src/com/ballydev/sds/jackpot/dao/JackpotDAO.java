/*****************************************************************************
 * $Id: JackpotDAO.java,v 1.163.1.2.1.0.2.7, 2013-10-12 08:39:47Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 3:39:47 AM$
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import com.ballydev.sds.framework.util.DateEngineUtil;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ILookupTableConstants;
import com.ballydev.sds.jackpot.db.slip.Jackpot;
import com.ballydev.sds.jackpot.db.slip.JackpotSequence;
import com.ballydev.sds.jackpot.db.slip.ProcessSession;
import com.ballydev.sds.jackpot.db.slip.SlipData;
import com.ballydev.sds.jackpot.db.slip.SlipReference;
import com.ballydev.sds.jackpot.db.slip.Transaction;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotLiabilityDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpot.keypad.IKeypadProcessConstants;
import com.ballydev.sds.jackpot.keypad.KeypadLookUpConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.ConversionUtil;
import com.ballydev.sds.jackpot.util.DateUtil;
import com.ballydev.sds.jackpot.util.JackpotHelper;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;
import com.ballydev.sds.jackpot.util.MessageUtil;
import com.ballydev.sds.jackpot.util.PadderUtil;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;

/**
 * Class to communicate with the Data Access Layer of the Jackpot Engine
 * 
 * @author vijayrajm
 * @version $Revision: 176$
 */
public class JackpotDAO implements ILookupTableConstants {/*

 */
	/**
	 * jackpotSiteDTO instance.
	 */
	/*
	 * private JackpotSiteDTO jackpotSiteDTO;
	 *//**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * blind attempt value initialized to zero
	 */
	private static final short BLIND_ATTEMPT = 0;

	private static HashMap mapStatusFlag = new HashMap();
	private static HashMap mapSlipType = new HashMap();
	private static HashMap mapJackpotType = new HashMap();
	private static HashMap mapProcessFlag = new HashMap();

	public JackpotDAO() {
		getStatusFlagMapValues();
		getSlipTypeMapValues();
		getJackpotTypeMapValues();
		getProcessFlagMapValues();
	}

	public static HashMap getStatusFlagMapValues() {
		if (mapStatusFlag.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.info("mapStatusFlag is NULL");
			}

			ArrayList<Object[]> listStatusFlag = new ArrayList<Object[]>();

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String strStatusFlag = "Select id, flagDescription from com.ballydev.sds.jackpot.db.slip.StatusFlag order by id asc";
			Query queryStatusFlag = sessionObj.createQuery(strStatusFlag);

			if (log.isInfoEnabled()) {
				log.info("queryStatusFlag: " + queryStatusFlag.getQueryString());
			}
			listStatusFlag = (ArrayList<Object[]>) queryStatusFlag.list();

			if (listStatusFlag != null && listStatusFlag.size() > 0) {

				if (log.isInfoEnabled()) {
					log.info("listStatusFlag.size(): " + listStatusFlag.size());
				}
				for (int i = 0; i < listStatusFlag.size(); i++) {
					log.info("listStatusFlag: " + listStatusFlag.get(i)[0]);
					mapStatusFlag.put(listStatusFlag.get(i)[0],
							listStatusFlag.get(i)[1]);
				}
				if (log.isInfoEnabled()) {
					log.info("mapStatusFlag: " + mapStatusFlag);
					log.info("mapStatusFlag.size(): " + mapStatusFlag.size());
				}
			}
			return mapStatusFlag;
		} else {
			if (log.isInfoEnabled()) {
				log.info("mapStatusFlag: " + mapStatusFlag);
			}
			return mapStatusFlag;
		}
	}

	public static HashMap getSlipTypeMapValues() {
		if (mapSlipType.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.info("mapSlipType is NULL");
			}

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String strSlipType = "Select description from com.ballydev.sds.jackpot.db.slip.SlipType where id="
					+ ILookupTableConstants.JACKPOT_SLIP_TYPE_ID;
			Query querySlipType = sessionObj.createQuery(strSlipType);

			if (log.isInfoEnabled()) {
				log.info("querySlipType: " + querySlipType.getQueryString());
			}
			Object objSlipType = querySlipType.uniqueResult();

			if (objSlipType != null) {

				if (log.isInfoEnabled()) {
					log.info("objSlipType: " + objSlipType);
				}
				mapSlipType.put(ILookupTableConstants.JACKPOT_SLIP_TYPE_ID,
						objSlipType.toString());
				if (log.isInfoEnabled()) {
					log.info("mapSlipType: " + mapStatusFlag);
					log.info("mapSlipType.size(): " + mapStatusFlag.size());
				}
			}
			return mapSlipType;
		} else {
			if (log.isInfoEnabled()) {
				log.info("mapSlipType: " + mapStatusFlag);
			}
			return mapSlipType;
		}
	}

	/**
	 * Method to query the JackpotType table to get the jackpto ids and its
	 * description and store it in the HashMap
	 * 
	 * @return HashMap
	 */
	public static HashMap getJackpotTypeMapValues() {
		if (mapJackpotType.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.info("mapJackpotType is NULL");
			}

			ArrayList<Object[]> listJackpotType = new ArrayList<Object[]>();

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String strJackpotType = "Select id, flagDescription from com.ballydev.sds.jackpot.db.slip.JackpotType order by id asc";
			Query queryJackpotType = sessionObj.createQuery(strJackpotType);

			if (log.isInfoEnabled()) {
				log.info("queryStatusFlag: "
						+ queryJackpotType.getQueryString());
			}
			listJackpotType = (ArrayList<Object[]>) queryJackpotType.list();

			if (listJackpotType != null && listJackpotType.size() > 0) {

				if (log.isInfoEnabled()) {
					log.info("listJackpotType.size(): "
							+ listJackpotType.size());
				}
				for (int i = 0; i < listJackpotType.size(); i++) {
					if (log.isInfoEnabled()) {
						log.info("listJackpotType: "
								+ listJackpotType.get(i)[0]);
					}
					mapJackpotType.put(listJackpotType.get(i)[0],
							listJackpotType.get(i)[1]);
				}
				if (log.isInfoEnabled()) {
					log.info("mapJackpotType: " + mapJackpotType);
					log.info("mapJackpotType.size(): " + mapJackpotType.size());
				}
			}
			return mapJackpotType;
		} else {
			if (log.isInfoEnabled()) {
				log.info("mapJackpotType: " + mapJackpotType);
			}
			return mapJackpotType;
		}
	}

	/**
	 * Method to query the ProcessFlag table to get the jackpto ids and its
	 * description and store it in the HashMap
	 * 
	 * @return HashMap
	 */
	public static HashMap getProcessFlagMapValues() {
		if (mapProcessFlag.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.info("mapProcessFlag is NULL");
			}

			ArrayList<Object[]> listProcessFlag = new ArrayList<Object[]>();

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String strProcessFlag = "Select id, flagDescription from com.ballydev.sds.jackpot.db.slip.ProcessFlag order by id asc";
			Query queryProcessFlag = sessionObj.createQuery(strProcessFlag);

			if (log.isInfoEnabled()) {
				log.info("queryStatusFlag: "
						+ queryProcessFlag.getQueryString());
			}
			listProcessFlag = (ArrayList<Object[]>) queryProcessFlag.list();

			if (listProcessFlag != null && listProcessFlag.size() > 0) {

				if (log.isInfoEnabled()) {
					log.info("listProcessFlag.size(): "
							+ listProcessFlag.size());
				}
				for (int i = 0; i < listProcessFlag.size(); i++) {
					if (log.isInfoEnabled()) {
						log.info("listProcessFlag: "
								+ listProcessFlag.get(i)[0]);
					}
					mapProcessFlag.put(listProcessFlag.get(i)[0],
							listProcessFlag.get(i)[1]);
				}
				if (log.isInfoEnabled()) {
					log.info("mapProcessFlag: " + mapProcessFlag);
					log.info("mapProcessFlag.size(): " + mapProcessFlag.size());
				}
			}
			return mapProcessFlag;
		} else {
			if (log.isInfoEnabled()) {
				log.info("mapProcessFlag: " + mapProcessFlag);
			}
			return mapProcessFlag;
		}
	}

	/**
	 * Method to retrieve all the pending jackpot slip details
	 * 
	 * @param assetId
	 * @return List<JackpotDTO>
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId)
			throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getAllPendingJackpotSlipDetails DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtStatusId = Restrictions.eq("statusFlagId",
					PENDING_STATUS_ID);
			Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));

			Criterion finalCriterion = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", siteId));

			if (siteId == -1) {
				finalCriterion = crtSlipTypeId;
			}

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class).add(finalCriterion)
					.addOrder(orderBySeq);

			List<SlipReference> pendingSlips = slipRefCriteria.list();
			if (pendingSlips != null) {
				if (pendingSlips.size() > 0) {
					if (log.isInfoEnabled()) {
						log.info("pendingSlips.size() is "
								+ pendingSlips.size());
					}
					for (int i = 0; i < pendingSlips.size(); i++) {

						JackpotDTO jackpotDTO = JackpotHelper
								.getJackpotDTO(pendingSlips.get(i));
						jackpotDTOList.add(jackpotDTO);
					}
					if (log.isDebugEnabled()) {
						log.debug("**************************************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The list of pending records as been returned , Record count: "
								+ pendingSlips.size());
					}
					if (log.isDebugEnabled()) {
						log.debug("**************************************************************************************");
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("**********************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("There are no pending records");
					}
					if (log.isDebugEnabled()) {
						log.debug("**********************************************");
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no pending records");
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in getAllPendingJackpotSlipDetails",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getAllPendingJackpotSlipDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method to get all the pending jackpot data to cache for sending alerts
	 * 
	 * @return
	 * @throws JackpotDAOException
	 */
	public static List<JackpotDTO> getAllPendingJackpotForCache()
			throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getAllPendingJackpotForCache DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			ProjectionList prjLst = Projections.projectionList();

			Projection prjSeqNo = Projections.property("sequenceNumber");
			Projection prjJackpotId = Projections.property("jp.jackpotId");
			Projection prjMsgId = Projections.property("messageId");
			Projection prjAcnfNo = Projections.property("acnfNumber");
			Projection prjAcnfLocation = Projections.property("acnfLocation");
			Projection prjTransDate = Projections.property("transactionDate");
			Projection prjOriginalAmount = Projections
					.property("jp.originalAmount");
			Projection prjAreaShortName = Projections.property("areaShortName");
			Projection prjSiteId = Projections.property("siteId");

			prjLst.add(prjSeqNo);
			prjLst.add(prjJackpotId);
			prjLst.add(prjMsgId);
			prjLst.add(prjAcnfNo);
			prjLst.add(prjAcnfLocation);
			prjLst.add(prjTransDate);
			prjLst.add(prjOriginalAmount);
			prjLst.add(prjAreaShortName);
			prjLst.add(prjSiteId);

			Criterion crtStatusId = Restrictions.eq("statusFlagId",
					PENDING_STATUS_ID);
			Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.setProjection(prjLst).add(crtSlipTypeId)
					.addOrder(orderBySeq);

			List<Object[]> pendingSlips = slipRefCriteria.list();

			if (pendingSlips != null && pendingSlips.size() > 0) {
				for (Object[] jackpotData : pendingSlips) {
					JackpotDTO jackpotDTO = new JackpotDTO();
					jackpotDTO.setSequenceNumber((Long) jackpotData[0]);
					jackpotDTO.setJackpotId((String) jackpotData[1]);
					jackpotDTO.setMessageId((Long) jackpotData[2]);
					jackpotDTO.setAssetConfigNumber((String) jackpotData[3]);
					jackpotDTO.setAssetConfigLocation((String) jackpotData[4]);
					jackpotDTO.setTransactionDate((Date) jackpotData[5]);
					log.info("**** Trans Date: "
							+ jackpotDTO.getTransactionDate());
					jackpotDTO.setPendingAlertTxnDate(DateHelper
							.getLocalTimeFromUTC(
									((Date) jackpotData[5]).getTime())
							.getTime());
					log.info("**** PendingAlertTxnDate: "
							+ jackpotDTO.getPendingAlertTxnDate());
					jackpotDTO.setOriginalAmount((Long) jackpotData[6]);
					jackpotDTO.setAreaShortName((String) jackpotData[7]);
					jackpotDTO.setSiteId((Integer) jackpotData[8]);
					jackpotDTOList.add(jackpotDTO);
				}

				if (log.isInfoEnabled()) {
					log.info("The list of pending records as been returned , Record count: "
							+ pendingSlips.size());
				}

			} else {
				if (log.isInfoEnabled()) {
					log.info("There are no pending records");
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in getAllPendingJackpotForCache", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getAllPendingJackpotForCache", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method to the pending jackpot slip details for the filter TYPE
	 * (SlotNo/SlotLoc/SeqNo/Minutes)
	 * 
	 * @param assetId
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotDTO> getJackpotDetails(JackpotFilter filter)
			throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotDetails DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criterion crtnMainCrt = null;
			if (log.isInfoEnabled()) {
				log.info("Site Id: " + filter.getSiteId());
			}
			if (log.isInfoEnabled()) {
				log.info("Method type: " + filter.getType());
			}
			if (filter.getType().equals("getJackpotSlipsforLastXXMinutes")) {
				if (log.isInfoEnabled()) {
					log.info("TimeOfXXMinutesBack: "
							+ DateUtil.getTimeOfXXMinutesBack(filter
									.getNumOfMinutes()));
				}
				crtnMainCrt = Restrictions.gt("createdTs", DateUtil
						.getTimeOfXXMinutesBack(filter.getNumOfMinutes()));
			} else if (filter.getType()
					.equals("getJackpotDetailsForSlotNumber")) {
				if (log.isInfoEnabled()) {
					log.info("Slot No: " + filter.getSlotNumber());
				}
				crtnMainCrt = Restrictions.eq("acnfNumber", PadderUtil.lPad(
						filter.getSlotNumber(),
						IAppConstants.ASSET_NO_PAD_LENGTH));
			} else if (filter.getType().equals(
					"getJackpotDetailsForSlotLocation")) {
				if (log.isInfoEnabled()) {
					log.info("Slot Loc No: " + filter.getStandNumber());
				}
				Criterion crtnSlotNoLst = Restrictions.in("acnfNumber",
						filter.getLstSlotNo());
				crtnMainCrt = Restrictions.and(crtnSlotNoLst, Restrictions.eq(
						"acnfLocation", filter.getStandNumber()));
			} else if (filter.getType().equals(
					"getJackpotDetailsForSequenceNumber")) {
				if (log.isInfoEnabled()) {
					log.info("Seq No: " + filter.getSequenceNumber());
				}
				crtnMainCrt = Restrictions.eq("sequenceNumber",
						filter.getSequenceNumber());
			}

			Criterion crtStatus = Restrictions.and(crtnMainCrt,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion crtSlipTypeId = Restrictions.and(crtStatus,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", filter.getSiteId()));

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			// Criteria slipRefCriteria = sessionObj.createCriteria(
			// SlipReference.class).add(crtSiteId);

			List<SlipReference> pendingSlips = slipRefCriteria.list();

			if (pendingSlips.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("pendingSlips.size() is greater than zero: "
							+ pendingSlips.size());
				}
				for (int i = 0; i < pendingSlips.size(); i++) {

					JackpotDTO jackpotDTO = JackpotHelper
							.getJackpotDTO(pendingSlips.get(i));

					jackpotDTOList.add(jackpotDTO);
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of pending slips for "
							+ filter.getType() + " have been returned with: "
							+ pendingSlips.size() + " records");
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no pending records");
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	/**
	 * Method to get the reprint jackpot slip details
	 * 
	 * @param sequenceNumber
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if reprint jackpot slip details retrieval
	 *             failed
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber,
			int siteId, String cashierDeskEnabled) throws JackpotDAOException {

		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside get reprint jackpot slip details DAO");
			}
			if (log.isDebugEnabled()) {
				log.debug("Sequence Number..." + sequenceNumber);
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			ArrayList statusFlagList = new ArrayList();
			if (cashierDeskEnabled != null
					&& cashierDeskEnabled.equalsIgnoreCase("No")) {
				statusFlagList.add(PROCESSED_STATUS_ID); // OLD FLOW
			}
			statusFlagList.add(CHANGE_STATUS_ID);
			statusFlagList.add(REPRINT_STATUS_ID);
			if (cashierDeskEnabled != null
					&& cashierDeskEnabled.equalsIgnoreCase("Yes")) {
				statusFlagList.add(PRINTED_STATUS_ID); // CASHIER DESK FLOW
			}

			Criterion crtStatusId = Restrictions.in("statusFlagId",
					statusFlagList);
			Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSeqNumber = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("sequenceNumber", sequenceNumber));
			Criterion crtSiteId = Restrictions.and(crtSeqNumber,
					Restrictions.eq("siteId", siteId));

			Criteria slipRefCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference reprintJackpotSlip = (SlipReference) slipRefCriteria
					.uniqueResult();
			if (reprintJackpotSlip != null) {

				jackpotDTO = JackpotHelper
						.getJackpotDTOOnStatusId(reprintJackpotSlip);

				jackpotDTO.setPostedSuccessfully(true);
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("JackpotDTO of reprint jackpot slip details has been returned for sequence number..."
							+ sequenceNumber);
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
			} else {
				jackpotDTO.setPostedSuccessfully(false);
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number: "
							+ sequenceNumber);
				}
				log.debug("*************************************************************");
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getReprintJackpotSlipDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getReprintJackpotSlipDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to get the Void jackpot slip details
	 * 
	 * @param sequenceNumber
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if VOID jackpot slip details retrieval
	 *             failed
	 */
	public static JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber,
			int siteId) throws JackpotDAOException {

		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside get Void jackpot slip details DAO");
			}
			if (log.isDebugEnabled()) {
				log.debug("Sequence Number..." + sequenceNumber);
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtStatusId = Restrictions.eq("statusFlagId",
					VOID_STATUS_ID);
			Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSeqNumber = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("sequenceNumber", sequenceNumber));
			Criterion crtSiteId = Restrictions.and(crtSeqNumber,
					Restrictions.eq("siteId", siteId));

			Criteria slipRefCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference voidJackpotSlip = (SlipReference) slipRefCriteria
					.uniqueResult();
			if (voidJackpotSlip != null) {
				jackpotDTO = JackpotHelper.getJackpotDTO(voidJackpotSlip);
				jackpotDTO.setPostedSuccessfully(true);
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("JackpotDTO of void jackpot slip details has been returned for sequence number..."
							+ sequenceNumber);
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
			} else {
				jackpotDTO.setPostedSuccessfully(false);
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number: "
							+ sequenceNumber);
				}
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getVoidJackpotSlipDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getVoidJackpotSlipDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to get the express jackpot blind attempt
	 * 
	 * @param sequenceNumber
	 * @return blindAttempt
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if express jackpot blind attempt retrieval
	 *             failed
	 */
	public static short getExpressJackpotBlindAttempts(long sequenceNumber,
			int siteId) throws JackpotDAOException {

		short blindAttempt = 0;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside get express jackpot blind attempt DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNumber = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {
				Set<Jackpot> jackpotSet = slipReference.getJackpot();

				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					Jackpot jackpot = (Jackpot) iter.next();
					blindAttempt = jackpot.getBlindAttempt();
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Blind attempt..." + blindAttempt);
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getExpressJackpotBlindAttempts", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getExpressJackpotBlindAttempts", e2);
			throw new JackpotDAOException(e2);
		}
		return blindAttempt;

	}

	/**
	 * Method to update the blind attempt as 2 for a non carded express jackpot.
	 * 
	 * @param sequenceNumber
	 * @param siteId
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if express jackpot blind attempt updation
	 *             failed
	 */
	public static boolean postNonCardedExpressJackpotBlindAttempts(
			long sequenceNumber, int siteId) throws JackpotDAOException {

		try {
			if (log.isInfoEnabled()) {
				log.info("Inside post non carded express jackpot blind attempt DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNumber = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {

				Set<Jackpot> jackpotSet = slipReference.getJackpot();

				Jackpot jackpot = null;
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					jackpot = (Jackpot) iter.next();
				}
				jackpot.setBlindAttempt((short) 2);
				Timestamp createDate = new Timestamp(Calendar.getInstance()
						.getTimeInMillis());
				jackpot.setUpdatedTs(DateHelper.getUTCTimeFromLocal(createDate));
				sessionObj.save(jackpot);
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("postNonCardedExpressJackpotBlindAttempts is done successfully");
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				return true;

			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				return false;
			}

		} catch (HibernateException e) {
			log.error(
					"HibernateException in postNonCardedExpressJackpotBlindAttempts",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postNonCardedExpressJackpotBlindAttempts",
					e2);
			throw new JackpotDAOException(e2);
		}
	}

	/**
	 * Method to update the blind attempt as 2 for a carded express jackpot.
	 * 
	 * @param sequenceNumber
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	public static boolean postExpressJackpotBlindAttempts(long sequenceNumber,
			int siteId) throws JackpotDAOException {

		short blindAttempt = 0;

		try {
			if (log.isInfoEnabled()) {
				log.info("Inside post express jackpot blind attempt DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNumber = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {

				Set<Jackpot> jackpotSet = slipReference.getJackpot();

				Jackpot jackpot = null;
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					jackpot = (Jackpot) iter.next();
					blindAttempt = jackpot.getBlindAttempt();
				}
				blindAttempt++;
				jackpot.setBlindAttempt(blindAttempt);
				Timestamp createDate = new Timestamp(Calendar.getInstance()
						.getTimeInMillis());
				jackpot.setUpdatedTs(DateHelper.getUTCTimeFromLocal(createDate));
				sessionObj.save(jackpot);

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Blind attempt..." + blindAttempt);
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				return true;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				return false;
			}
		} catch (HibernateException e) {
			log.error("HibernateException in postExpressJackpotBlindAttempts",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postExpressJackpotBlindAttempts", e2);
			throw new JackpotDAOException(e2);
		}
	}

	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp
	 * processing that is aborted
	 * 
	 * @param sequenceNumber
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if express jackpot blind attempt updation
	 *             failed
	 */
	public static boolean postUnsuccessfulExpJpBlindAttemptsAbort(
			long sequenceNumber, int siteId) throws JackpotDAOException {

		try {
			if (log.isInfoEnabled()) {
				log.info("Inside postUnsuccessfulExpJpBlindAttemptsAbort DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNumber = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSlipTypeid);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {

				Set<Jackpot> jackpotSet = slipReference.getJackpot();

				Jackpot jackpot = null;
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					jackpot = (Jackpot) iter.next();
				}
				jackpot.setBlindAttempt((short) IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT);
				Timestamp createDate = new Timestamp(Calendar.getInstance()
						.getTimeInMillis());
				jackpot.setUpdatedTs(DateHelper.getUTCTimeFromLocal(createDate));
				sessionObj.save(jackpot);
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("postUnsuccessfulExpJpBlindAttemptsAbort is done successfully");
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************");
				}
				return true;

			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				return false;
			}

		} catch (HibernateException e) {
			log.error(
					"HibernateException in postUnsuccessfulExpJpBlindAttemptsAbort",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postUnsuccessfulExpJpBlindAttemptsAbort",
					e2);
			throw new JackpotDAOException(e2);
		}
	}

	/**
	 * Method to get the status of a particular Jackpot
	 * 
	 * @param sequenceNumber
	 * @return statusDesc
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot status retrieval failed
	 */
	public static short getJackpotStatus(long sequenceNumber, int siteId)
			throws JackpotDAOException {

		short statusId = 0;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside get jackpot slip status DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {
				statusId = slipReference.getStatusFlagId();

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Status flag Id... has been returned" + statusId);
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotStatus", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotStatus", e2);
			throw new JackpotDAOException(e2);
		}
		return statusId;
	}

	/**
	 * Method to get details whether the JP is posted to accounting or not
	 * 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public static String getJackpotPostToAccountDetail(long sequenceNumber,
			int siteId) throws JackpotDAOException {

		String postToAccounting = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotPostToAccountDetail DAO method");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {
				postToAccounting = slipReference.getPostToAccounting() != null ? slipReference
						.getPostToAccounting() : "N";

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
					log.debug("Post to accounting details... has been returned : "
							+ postToAccounting);
					log.debug("********************************************************************");
				}
			} else if (log.isDebugEnabled()) {
				log.debug("**************************************************");
				log.debug("There is no record found for this sequence number");
				log.debug("**************************************************");
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotStatus", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotStatus", e2);
			throw new JackpotDAOException(e2);
		}
		return postToAccounting;
	}

	/**
	 * Method to get the Jackpot slip status for a void method that will return
	 * the Transaction dtae based one the status and the current day
	 * 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public static JackpotDTO getJackpotStatusForVoid(long sequenceNumber,
			int siteId) throws JackpotDAOException {
		JackpotDTO jackpotRtnDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside get jackpot slip status for void DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			jackpotRtnDTO.setCurrentDateTime(new Date());

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {
				jackpotRtnDTO.setStatusFlagId(slipReference.getStatusFlagId());
				jackpotRtnDTO.setAssetConfigNumber(slipReference
						.getAcnfNumber());
				jackpotRtnDTO
						.setAccountNumber(slipReference.getAccountNumber());
				jackpotRtnDTO.setCashlessAccountType(slipReference
						.getAccountType());
				jackpotRtnDTO.setPostToAccounting(slipReference
						.getPostToAccounting());
				if (slipReference.getStatusFlagId().equals(
						ILookupTableConstants.PENDING_STATUS_ID)) {
					jackpotRtnDTO.setTransactionDate(new Date(DateHelper
							.getLocalTimeFromUTC(
									slipReference.getTransactionDate())
							.getTime()));
				} else if (slipReference.getStatusFlagId().equals(
						ILookupTableConstants.PROCESSED_STATUS_ID)
						|| slipReference.getStatusFlagId().equals(
								ILookupTableConstants.CHANGE_STATUS_ID)
						|| slipReference.getStatusFlagId().equals(
								ILookupTableConstants.REPRINT_STATUS_ID)
						|| slipReference.getStatusFlagId().equals(
								ILookupTableConstants.PRINTED_STATUS_ID)) {

					Set<Jackpot> jackpotSet = slipReference.getJackpot();
					for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
						Jackpot jackpot = (Jackpot) iter.next();
						jackpotRtnDTO.setHpjpAmount(jackpot.getHpjpAmount());
						jackpotRtnDTO.setJackpotNetAmount(jackpot
								.getNetAmount());
					}
					Set<Transaction> transactionSet = slipReference
							.getTransaction();
					for (Iterator iter = transactionSet.iterator(); iter
							.hasNext();) {
						Transaction transaction = (Transaction) iter.next();
						if (transaction.getStatusFlagId() != null
								&& (transaction.getStatusFlagId() == ILookupTableConstants.PROCESSED_STATUS_ID
										|| transaction.getStatusFlagId() == ILookupTableConstants.CHANGE_STATUS_ID
										|| transaction.getStatusFlagId() == ILookupTableConstants.REPRINT_STATUS_ID || transaction
										.getStatusFlagId() == ILookupTableConstants.PRINTED_STATUS_ID)) {
							jackpotRtnDTO.setTransactionDate(new Date(
									DateHelper.getLocalTimeFromUTC(
											transaction.getCreatedTs())
											.getTime()));
							break;
						}
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Status flag id... has been returned: "
							+ jackpotRtnDTO.getStatusFlagId());
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number");
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotStatusForVoid", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotStatusForVoid", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotRtnDTO;
	}

	/**
	 * Method to get the details to reprint the PRIOR (P) slip (the latest
	 * processed slip)
	 * 
	 * @param siteId
	 * @return JackpotDTO
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static JackpotDTO getReprintPriorSlipDetails(int siteId,
			String cashierDeskEnabled) throws JackpotDAOException {

		log.info("Inside getReprintPriorSlipDetails DAO");
		JackpotDTO jackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getReprintPriorSlipDetails DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String queryString = "select max(coalesce(updatedTs,createdTs)) from com.ballydev.sds.jackpot.db.slip.SlipReference where slipTypeId = 3001";// group
																																							// by
																																							// slipTypeId";

			Query query = sessionObj.createQuery(queryString);
			Timestamp maxTransactionDate = (Timestamp) query.uniqueResult();

			if (maxTransactionDate != null) {

				if (log.isInfoEnabled()) {
					log.info("maxTransactionDate: " + maxTransactionDate);
				}

				ArrayList statusFlagList = new ArrayList();
				statusFlagList.add(ILookupTableConstants.CHANGE_STATUS_ID);
				statusFlagList.add(ILookupTableConstants.PROCESSED_STATUS_ID);
				statusFlagList.add(ILookupTableConstants.VOID_STATUS_ID);
				statusFlagList.add(ILookupTableConstants.REPRINT_STATUS_ID);
				statusFlagList.add(ILookupTableConstants.PRINTED_STATUS_ID);
				statusFlagList
						.add(ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID);

				Criterion crtStatusId = Restrictions.in("statusFlagId",
						statusFlagList);
				Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
						Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
				Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
						Restrictions.eq("siteId", siteId));

				Criterion crtMaxUpdatedTs = Restrictions.eq("updatedTs",
						maxTransactionDate);
				Criterion crtMaxCreatedTs = Restrictions.or(crtMaxUpdatedTs,
						Restrictions.eq("createdTs", maxTransactionDate));

				Criterion crtFinal = Restrictions.and(crtSiteId,
						crtMaxCreatedTs);

				Criteria slipRefCriteria = sessionObj.createCriteria(
						SlipReference.class).add(crtFinal);

				SlipReference reprintJackpotSlip = (SlipReference) slipRefCriteria
						.uniqueResult();
				if (reprintJackpotSlip != null) {
					jackpotDTO = JackpotHelper
							.getJackpotDTOOnStatusId(reprintJackpotSlip);

					jackpotDTO.setPostedSuccessfully(true);
					if (log.isDebugEnabled()) {
						log.debug("*****************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("JackpotDTO of reprint jackpot slip details has been returned");
					}
					if (log.isDebugEnabled()) {
						log.debug("*****************************************************************");
					}
				}
			} else {
				jackpotDTO.setPostedSuccessfully(false);
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no prior record found for this site: "
							+ siteId);
				}
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getReprintPriorSlipDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getReprintPriorSlipDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to get the Jackpot Details based on the site Id, from date and to
	 * date to print the Jackpot Slip Report
	 * 
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return JackpotReportsDTO
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(
			int siteId, String fromDate, String toDate)
			throws JackpotDAOException {
		List<JackpotReportsDTO> jpReportsDTOList = new ArrayList<JackpotReportsDTO>();
		JackpotReportsDTO jpReportsDTO = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getDetailsToPrintJpSlipReportForDate DAO");
			}

			long jackpotNetAmt = 0, jpVoidedAmt = 0, jpTaxAmt = 0, hpjpAmt = 0;

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			ArrayList statusFlagList = new ArrayList();
			statusFlagList.add(ILookupTableConstants.CHANGE_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PROCESSED_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.VOID_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PRINTED_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.REPRINT_STATUS_ID);

			if (log.isInfoEnabled()) {
				log.info("From date: "
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(fromDate).getTime()));
				log.info("To date: "
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(toDate).getTime()));
				log.info("Site id: " + siteId);
			}

			Projection prjHPJPAmt = Projections.property("jp.hpjpAmount");
			Projection prjNetAmt = Projections.property("jp.netAmount");
			Projection prjTaxAmt = Projections.property("jp.taxAmount");

			Projection prjTrStatusId = Projections
					.property("trans.statusFlagId");

			Projection prjSlRefStatusId = Projections.property("statusFlagId");
			Projection prjSeqNo = Projections.property("sequenceNumber");
			Projection prjSlotNo = Projections.property("acnfNumber");

			ProjectionList prjList = Projections.projectionList();
			prjList.add(prjHPJPAmt);
			prjList.add(prjNetAmt);
			prjList.add(prjTrStatusId);
			prjList.add(prjSlRefStatusId);
			prjList.add(prjTaxAmt);
			prjList.add(prjSeqNo);
			prjList.add(prjSlotNo);

			Criterion crtTransStId = Restrictions.in("trans.statusFlagId",
					statusFlagList);
			Criterion crtTransCreateTs = Restrictions.and(crtTransStId,
					Restrictions.between("trans.createdTs", DateHelper
							.getUTCTimeFromLocal(DateEngineUtil
									.convertStringToDate(fromDate).getTime()),
							DateHelper.getUTCTimeFromLocal(DateEngineUtil
									.convertStringToDate(toDate).getTime())));

			Criterion crtSlipTypeId = Restrictions.and(crtTransCreateTs,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", siteId));
			Criterion crtStatusCheck = Restrictions.eqProperty(
					"trans.statusFlagId", "statusFlagId");
			Order orderByCreatedTs = Order.asc("trans.createdTs");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.createAlias("transaction", "trans", Criteria.LEFT_JOIN)
					.add(crtStatusCheck).add(crtSiteId).setProjection(prjList)
					.addOrder(orderByCreatedTs);

			List<Object[]> objArrayList = (List<Object[]>) slipRefCriteria
					.list();
			if (objArrayList != null && objArrayList.size() > 0) {

				if (log.isInfoEnabled()) {
					log.info("slipRefList size: " + objArrayList.size());
				}
				List<Long> sequenceNumberList = new ArrayList();
				for (int j = 0; j < objArrayList.size(); j++) {
					Long sequenceNumber = (Long) objArrayList.get(j)[5];
					if (!sequenceNumberList.contains(sequenceNumber)) {
						sequenceNumberList.add(sequenceNumber);
						if (log.isDebugEnabled()) {
							// HPJP Amt
							log.debug("HPJP Amt: " + objArrayList.get(j)[0]);
							// Net Amt
							log.debug("Net Amt: " + objArrayList.get(j)[1]);
							// Trans Status Id
							log.debug("Trans Status Id :"
									+ objArrayList.get(j)[2]);
							// Slip ref Status Id
							log.debug("Slip ref Status Id :"
									+ objArrayList.get(j)[3]);
							// Tax Amt
							log.debug("Tax Amt :" + objArrayList.get(j)[4]);
							// Seq No
							log.debug("Seq No :" + objArrayList.get(j)[5]);
							// Slot No
							log.debug("Slot No :" + objArrayList.get(j)[6]);
						}

						if (((Short) objArrayList.get(j)[2])
								.equals((Short) objArrayList.get(j)[3])
								&& (Short) objArrayList.get(j)[2] != ILookupTableConstants.VOID_STATUS_ID
								&& (Long) objArrayList.get(j)[1] != null) {
							if (log.isDebugEnabled()) {
								log.debug("Proc / Change / Print");
							}
							jackpotNetAmt = (Long) objArrayList.get(j)[1]
									+ jackpotNetAmt;
							if ((Long) objArrayList.get(j)[4] != null) {
								jpTaxAmt = (Long) objArrayList.get(j)[4]
										+ jpTaxAmt;
							}
							if ((Long) objArrayList.get(j)[0] != null) {
								hpjpAmt = (Long) objArrayList.get(j)[0]
										+ hpjpAmt;
							}
						} else if ((Short) objArrayList.get(j)[2] == ILookupTableConstants.VOID_STATUS_ID
								&& (Short) objArrayList.get(j)[3] == ILookupTableConstants.VOID_STATUS_ID
								&& (Long) objArrayList.get(j)[1] != null) {
							if (log.isDebugEnabled()) {
								log.debug("void");
							}
							jpVoidedAmt = (Long) objArrayList.get(j)[1]
									+ jpVoidedAmt;
						}

						jpReportsDTO = new JackpotReportsDTO();

						if (objArrayList.get(j)[0] != null) {
							jpReportsDTO
									.setHpjpAmt((Long) objArrayList.get(j)[0]);
						}
						if (objArrayList.get(j)[1] != null) {
							jpReportsDTO
									.setNetAmt((Long) objArrayList.get(j)[1]);
						}
						if (objArrayList.get(j)[5] != null) {
							jpReportsDTO.setSequenceNo((Long) objArrayList
									.get(j)[5]);
						}
						if (objArrayList.get(j)[6] != null) {
							jpReportsDTO
									.setSlotNo((String) objArrayList.get(j)[6]);
						}
						jpReportsDTO.setJackpotNetAmt(jackpotNetAmt);
						jpReportsDTO.setVoidedJackpotAmt(jpVoidedAmt);
						jpReportsDTO.setJackpotTaxAmt(jpTaxAmt);
						jpReportsDTO.setJackpotOrgAmt(hpjpAmt);
						jpReportsDTO.setPostedSuccessfully(true);
						jpReportsDTOList.add(jpReportsDTO);
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of processed and voided records as been returned From "
							+ DateEngineUtil.convertStringToDate(fromDate)
							+ " to "
							+ DateEngineUtil.convertStringToDate(toDate));
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no processed and voided records from "
							+ DateEngineUtil.convertStringToDate(fromDate)
							+ " to "
							+ DateEngineUtil.convertStringToDate(toDate));
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}
		} catch (HibernateException e) {
			log.error(
					"HibernateException in getDetailsToPrintJpSlipReportForDate",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getDetailsToPrintJpSlipReportForDate", e2);
			throw new JackpotDAOException(e2);
		}
		return jpReportsDTOList;
	}

	/**
	 * Method to get the Jackpot Details based on the site Id, employeeId, from
	 * date and to date to print the Jackpot Slip Report
	 * 
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(
			int siteId, String employeeId, String fromDate, String toDate)
			throws JackpotDAOException {
		List<JackpotReportsDTO> jpReportsDTOList = new ArrayList<JackpotReportsDTO>();
		JackpotReportsDTO jpReportsDTO = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getDetailsToPrintJpSlipReportForDateEmployee DAO");
			}
			long jackpotNetAmt = 0, jpVoidedAmt = 0, jpTaxAmt = 0, hpjpAmt = 0;

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			ArrayList statusFlagList = new ArrayList();
			statusFlagList.add(ILookupTableConstants.CHANGE_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PROCESSED_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.VOID_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.PRINTED_STATUS_ID);
			statusFlagList.add(ILookupTableConstants.REPRINT_STATUS_ID);

			if (log.isInfoEnabled()) {
				log.info("From date: "
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(fromDate).getTime()));
				log.info("To date: "
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(toDate).getTime()));
				log.info("Site id: " + siteId);
				log.info("Employee Id: " + employeeId);
			}

			Projection prjHPJPAmt = Projections.property("jp.hpjpAmount");
			Projection prjNetAmt = Projections.property("jp.netAmount");
			Projection prjTaxAmt = Projections.property("jp.taxAmount");

			Projection prjTrEmpFirstName = Projections
					.property("trans.employeeFirstName");
			Projection prjTrEmpLastName = Projections
					.property("trans.employeeLastName");
			Projection prjTrStatusId = Projections
					.property("trans.statusFlagId");

			Projection prjSlRefStatusId = Projections.property("statusFlagId");
			Projection prjSeqNo = Projections.property("sequenceNumber");
			Projection prjSlotNo = Projections.property("acnfNumber");

			ProjectionList prjList = Projections.projectionList();
			prjList.add(prjHPJPAmt);
			prjList.add(prjNetAmt);
			prjList.add(prjTrEmpFirstName);
			prjList.add(prjTrEmpLastName);
			prjList.add(prjTrStatusId);
			prjList.add(prjSlRefStatusId);
			prjList.add(prjTaxAmt);
			prjList.add(prjSeqNo);
			prjList.add(prjSlotNo);

			Criterion crtTransStId = Restrictions.in("trans.statusFlagId",
					statusFlagList);
			Criterion crtTransEmpId = Restrictions.and(crtTransStId,
					Restrictions.eq("trans.employeeId", employeeId));
			Criterion crtTransCreateTs = Restrictions.and(crtTransEmpId,
					Restrictions.between("trans.createdTs", DateHelper
							.getUTCTimeFromLocal(DateEngineUtil
									.convertStringToDate(fromDate).getTime()),
							DateHelper.getUTCTimeFromLocal(DateEngineUtil
									.convertStringToDate(toDate).getTime())));

			Criterion crtSlipTypeId = Restrictions.and(crtTransCreateTs,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", siteId));
			Criterion crtStatusCheck = Restrictions.eqProperty(
					"trans.statusFlagId", "statusFlagId");
			Order orderByCreatedTs = Order.asc("trans.createdTs");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.createAlias("transaction", "trans", Criteria.LEFT_JOIN)
					.add(crtStatusCheck).add(crtSiteId).setProjection(prjList)
					.addOrder(orderByCreatedTs);

			List<Object[]> objArrayList = (List<Object[]>) slipRefCriteria
					.list();
			if (objArrayList != null && objArrayList.size() > 0) {

				if (log.isDebugEnabled()) {
					log.debug("slipRefList size: " + objArrayList.size());
				}
				List<Long> sequenceNumberList = new ArrayList();
				for (int j = 0; j < objArrayList.size(); j++) {
					Long sequenceNumber = (Long) objArrayList.get(j)[7];
					if (!sequenceNumberList.contains(sequenceNumber)) {
						sequenceNumberList.add(sequenceNumber);

						if (log.isDebugEnabled()) {
							// HPJP Amt
							log.debug("HPJP Amt: " + objArrayList.get(j)[0]);
							// Net Amt
							log.debug("Net Amt: " + objArrayList.get(j)[1]);
							// First Name
							log.debug("First Name: " + objArrayList.get(j)[2]);
							// Last Name
							log.debug("Last Name :" + objArrayList.get(j)[3]);
							// Trans Status Id
							log.debug("Trans Status Id :"
									+ objArrayList.get(j)[4]);
							// Slip ref Status Id
							log.debug("Slip ref Status Id :"
									+ objArrayList.get(j)[5]);
							// Tax Amt
							log.debug("Tax Amt: " + objArrayList.get(j)[6]);
							// Seq no
							log.debug("Seq No: " + objArrayList.get(j)[7]);
							// Slot No
							log.debug("Slot No: " + objArrayList.get(j)[8]);
						}
						if (((Short) objArrayList.get(j)[4])
								.equals((Short) objArrayList.get(j)[5])
								&& (Short) objArrayList.get(j)[4] != ILookupTableConstants.VOID_STATUS_ID
								&& (Long) objArrayList.get(j)[1] != null) {
							if (log.isDebugEnabled()) {
								log.debug("Proc / Change");
							}
							jackpotNetAmt = (Long) objArrayList.get(j)[1]
									+ jackpotNetAmt;
							if ((Long) objArrayList.get(j)[6] != null) {
								jpTaxAmt = (Long) objArrayList.get(j)[6]
										+ jpTaxAmt;
							}
							if ((Long) objArrayList.get(j)[0] != null) {
								hpjpAmt = (Long) objArrayList.get(j)[0]
										+ hpjpAmt;
							}
						} else if ((Short) objArrayList.get(j)[4] == ILookupTableConstants.VOID_STATUS_ID
								&& (Short) objArrayList.get(j)[5] == ILookupTableConstants.VOID_STATUS_ID
								&& (Long) objArrayList.get(j)[1] != null) {
							if (log.isDebugEnabled()) {
								log.debug("void");
							}
							jpVoidedAmt = (Long) objArrayList.get(j)[1]
									+ jpVoidedAmt;
						}

						jpReportsDTO = new JackpotReportsDTO();
						jpReportsDTO.setSlotAttendantFirstName((objArrayList
								.get(0)[2]).toString());
						jpReportsDTO.setSlotAttendantLastName((objArrayList
								.get(0)[3]).toString());
						jpReportsDTO.setJackpotNetAmt(jackpotNetAmt);
						jpReportsDTO.setVoidedJackpotAmt(jpVoidedAmt);
						jpReportsDTO.setSlotAttendantId(employeeId);
						jpReportsDTO.setJackpotTaxAmt(jpTaxAmt);
						jpReportsDTO.setJackpotOrgAmt(hpjpAmt);

						if (objArrayList.get(j)[0] != null) {
							jpReportsDTO
									.setHpjpAmt((Long) objArrayList.get(j)[0]);
						}
						if (objArrayList.get(j)[1] != null) {
							jpReportsDTO
									.setNetAmt((Long) objArrayList.get(j)[1]);
						}
						if (objArrayList.get(j)[7] != null) {
							jpReportsDTO.setSequenceNo((Long) objArrayList
									.get(j)[7]);
						}
						if (objArrayList.get(j)[8] != null) {
							jpReportsDTO
									.setSlotNo((String) objArrayList.get(j)[8]);
						}
						jpReportsDTO.setPostedSuccessfully(true);
						jpReportsDTOList.add(jpReportsDTO);
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of processed and voided records as been returned From "
							+ DateEngineUtil.convertStringToDate(fromDate)
							+ " to "
							+ DateEngineUtil.convertStringToDate(toDate));
				}

				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no processed and voided records from "
							+ DateEngineUtil.convertStringToDate(fromDate)
							+ " to "
							+ DateEngineUtil.convertStringToDate(toDate));
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}
		} catch (HibernateException e) {
			log.error(
					"HibernateException in getDetailsToPrintJpSlipReportForDateEmployee",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error(
					"Exception in getDetailsToPrintJpSlipReportForDateEmployee",
					e2);
			throw new JackpotDAOException(e2);
		}
		return jpReportsDTOList;
	}

	/**
	 * Method used for updating the slip info for a Jp Keypad Process
	 * 
	 * @param processSession
	 * @param siteNumber
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings("unchecked")
	public static boolean updateKeyPadProcessJackpot(
			ProcessSession processSession, Integer siteNumber,
			String cashierDeskEnabled, Object[] results)
			throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside updateKeyPadProcessJackpot method");
		}
		boolean checkForExistence = true;
		if (processSession != null && siteNumber != null
				&& siteNumber.intValue() != 0) {
			try {
				/** Get the system current date/time * */
				long currntTime = System.currentTimeMillis();
				Timestamp currentDate = new Timestamp(currntTime);
				if (log.isInfoEnabled()) {
					log.info("Current date " + currentDate);
				}
				if (log.isDebugEnabled()) {
					log.debug("acnfNumber: " + processSession.getAcnfNumber());
					log.debug("siteNumber: " + siteNumber);
				}

				Session sessionObj = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				Criterion criterionAssetNumber = Restrictions.eq("acnfNumber",
						processSession.getAcnfNumber());
				Criterion criterionSlipTypeId = Restrictions.and(
						criterionAssetNumber,
						Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
				Criterion criterionStatusFlagId = Restrictions.and(
						criterionSlipTypeId,
						Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
				Criterion criterionProcessFlag = Restrictions.and(
						criterionStatusFlagId,
						Restrictions.isNull("processFlagId"));
				Criterion criterionSiteNumber = Restrictions.and(
						criterionProcessFlag,
						Restrictions.eq("siteId", siteNumber));
				Criteria criteriaSlipRef = sessionObj
						.createCriteria(SlipReference.class)
						.add(criterionSiteNumber)
						.addOrder(Order.desc("createdTs"));
				if (criteriaSlipRef != null) {
					List listSlipReference = criteriaSlipRef.list();
					if (listSlipReference != null
							&& listSlipReference.size() != 0) {
						// taking the first element only since it will be the
						// latest.
						SlipReference slipReferenceToProcess = (SlipReference) listSlipReference
								.get(0);
						/** Updating the Slip Reference Table */
						if (slipReferenceToProcess != null) {
							// check for pending jackpots that are not processed
							// and are pending due to clear jackpot.
							results[0] = slipReferenceToProcess.getMessageId();
							Set<Transaction> transactionSet = slipReferenceToProcess
									.getTransaction();
							if (transactionSet != null
									&& transactionSet.size() != 0) {
								for (Transaction transactionToCheckFor : transactionSet) {
									if (transactionToCheckFor != null) {
										if (transactionToCheckFor
												.getStatusFlagId() != null
												&& transactionToCheckFor
														.getStatusFlagId()
														.shortValue() == JP_CLEAR_STATUS_ID) {
											// since there is a jackpot clear
											// for this slip ref and
											// jackpot.
											return false;
										}
									}
								}
							}
							long slipPrimaryKey = JackpotUtil
									.getSlipPrimaryKey(slipReferenceToProcess
											.getAcnfNumber(), siteNumber, 0);

							slipReferenceToProcess
									.setTransactionId(slipPrimaryKey);

							if (processSession.getPouchPay() != null
									&& processSession.getPouchPay().intValue() > 0) {
								slipReferenceToProcess
										.setProcessFlagId(KeypadLookUpConstants.POUCH_PAY_PROCESS_FLAG);
							} else {
								slipReferenceToProcess
										.setProcessFlagId(KeypadLookUpConstants.NORMAL_PROCESS_FLAG);
							}
							if (cashierDeskEnabled != null) {
								if (cashierDeskEnabled.equalsIgnoreCase("No")
										|| slipReferenceToProcess
												.getProcessFlagId() == KeypadLookUpConstants.POUCH_PAY_PROCESS_FLAG) {
									// FOR OLD FLOW
									slipReferenceToProcess
											.setStatusFlagId(PROCESSED_STATUS_ID);
									processSession
											.setStatusFlagId(PROCESSED_STATUS_ID);
									slipReferenceToProcess
											.setPostToAccounting(ILookupTableConstants.POSTED_TO_ACCOUNTING);
									processSession
											.setStatusFlagDesc(getStatusDescription(PROCESSED_STATUS_ID));
								} else if (cashierDeskEnabled
										.equalsIgnoreCase("Yes")) {
									// FOR CASHIER DESK FLOW
									slipReferenceToProcess
											.setStatusFlagId(PRINTED_STATUS_ID);
									processSession
											.setStatusFlagId(PRINTED_STATUS_ID);
									slipReferenceToProcess
											.setPostToAccounting(ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING);
									processSession
											.setStatusFlagDesc(getStatusDescription(PRINTED_STATUS_ID));
								}
							} else if (log.isDebugEnabled()) {
								log.debug("IS_CASHIER_DESK_ENABLED site param value is null");
							}

							slipReferenceToProcess.setUpdatedTs(DateHelper
									.getUTCTimeFromLocal(currentDate));
							slipReferenceToProcess
									.setUpdatedUser(PadderUtil.lPad(
											String.valueOf(processSession
													.getEmployeeId().intValue()),
											IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
							sessionObj.saveOrUpdate(slipReferenceToProcess);

							// set the jp hit date
							processSession
									.setTransactionDate(slipReferenceToProcess
											.getTransactionDate().getTime());
							if (log.isInfoEnabled()) {
								log.info("slipReferenceToProcess.getSequenceNumber() : "
										+ slipReferenceToProcess
												.getSequenceNumber());
							}
							processSession
									.setSequenceNumber(slipReferenceToProcess
											.getSequenceNumber());
							if (log.isInfoEnabled()) {
								log.info("processSession.getSequenceNumber() : "
										+ processSession.getSequenceNumber());
							}
							processSession
									.setJackpotProcessFlagId(slipReferenceToProcess
											.getProcessFlagId());
							processSession
									.setAuditCodeId(slipReferenceToProcess
											.getAuditCodeId());
							if (slipReferenceToProcess.getProcessFlagId() != null) {
								processSession
										.setJackpotProcessDesc(getProcessFlagDescription(slipReferenceToProcess
												.getProcessFlagId()));
							}
							if (slipReferenceToProcess.getAuditCode() != null) {
								processSession
										.setAuditCodeDesc(slipReferenceToProcess
												.getAuditCode()
												.getDescription());
							}
							processSession
									.setSlipReferenceId(slipReferenceToProcess
											.getId());
							processSession.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
							if (slipReferenceToProcess.getSlipType() != null) {
								processSession
										.setSlipTypeDesc(slipReferenceToProcess
												.getSlipType().getDescription());
							}
							Set<Jackpot> jackpotSet = slipReferenceToProcess
									.getJackpot();
							/** Updating the Jackpot Table */
							if (jackpotSet != null && jackpotSet.size() != 0) {
								Jackpot jackpotToUpdate = null;
								for (Jackpot jackpot : jackpotSet) {
									jackpotToUpdate = jackpot;
									break;
								}

								if (jackpotToUpdate != null) {
									results[1] = jackpotToUpdate.getJackpotId();
									jackpotToUpdate
											.setTaxTypeId(KeypadLookUpConstants.TAX_NO);

									if (processSession.getPlayerCard() != null
											&& !processSession.getPlayerCard()
													.trim().isEmpty()) {
										jackpotToUpdate
												.setPlayerCard(processSession
														.getPlayerCard());
									} else {
										jackpotToUpdate
												.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
									}

									// jackpotToUpdate.setMachinePaidAmount(0l);
									// Making all these same as original amount
									// as there are no rounding supported.
									// jackpotToUpdate.setHpjpAmount(jackpotToUpdate.getOriginalAmount());
									jackpotToUpdate
											.setHpjpAmount(processSession
													.getJpHitAmount());
									// jackpotToUpdate.setHpjpAmountRounded(jackpotToUpdate.getOriginalAmount());
									// jackpotToUpdate.setHpjpAmountRounded(processSession.getJpHitAmount());
									jackpotToUpdate.setNetAmount(processSession
											.getJpHitAmount());
									// jackpotToUpdate.setTaxAmount(0l);
									jackpotToUpdate.setBlindAttempt((short) 2);
									if (processSession.getCoinsPlayed() != null
											&& processSession.getCoinsPlayed()
													.intValue() != 0) {
										jackpotToUpdate
												.setCoinsPlayed(processSession
														.getCoinsPlayed()
														.intValue());
									}

									if (processSession.getWinningCombination() != null
											&& !((processSession
													.getWinningCombination()
													.trim() + " ")
													.equalsIgnoreCase(" "))) {
										jackpotToUpdate
												.setWinningCombination(processSession
														.getWinningCombination());
									}

									if (processSession.getPayline() != null
											&& !((processSession.getPayline()
													.trim() + " ")
													.equalsIgnoreCase(" "))) {
										jackpotToUpdate
												.setPayline(processSession
														.getPayline());
									}

									jackpotToUpdate.setUpdatedTs(DateHelper
											.getUTCTimeFromLocal(currentDate));
									jackpotToUpdate
											.setUpdatedUser(PadderUtil.lPad(
													String.valueOf(processSession
															.getEmployeeId()
															.intValue()),
													IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
									sessionObj.saveOrUpdate(jackpotToUpdate);

									// setting jackpot id and jackpot type id
									if (jackpotToUpdate.getId() != null) {
										processSession.setSlipIdUsed(String
												.valueOf(jackpotToUpdate
														.getId()));
									}

									processSession.setUpdatedTs(DateHelper
											.getUTCTimeFromLocal(currentDate));
									processSession.setJackpotId(jackpotToUpdate
											.getJackpotId());
									processSession
											.setJackpotTypeId(jackpotToUpdate
													.getJackpotTypeId());
									if (processSession.getJpHitAmount() == null) {
										processSession
												.setJpHitAmount(jackpotToUpdate
														.getOriginalAmount()
														.longValue());
									}

									/** NEW ROW IN SLIP_DATA TABLE * */
									SlipData slipData = new SlipData();
									slipData.setId(slipPrimaryKey);
									slipData.setSlipReferenceId(slipReferenceToProcess
											.getId());

									if (processSession.getAuthEmployeeCardId() != null
											&& processSession
													.getAuthEmployeeCardId()
													.intValue() != 0) {
										slipData.setAuthEmployeeId1(PadderUtil.lPad(
												String.valueOf(processSession
														.getAuthEmployeeCardId()),
												IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
									}

									if (processSession.getShift() != null
											&& processSession.getShift()
													.intValue() > 0) {
										if (processSession.getShift()
												.intValue() == IKeypadProcessConstants.JACKPOT_SHIFT_DAY) {
											slipData.setShift(IKeypadProcessConstants.DEFAULT_JACKPOT_SHIFT_DAY_SHORT);
										} else if (processSession.getShift()
												.intValue() == IKeypadProcessConstants.JACKPOT_SHIFT_SWING) {
											slipData.setShift(IKeypadProcessConstants.DEFAULT_JACKPOT_SHIFT_SWING_SHORT);
										} else if (processSession.getShift()
												.intValue() == IKeypadProcessConstants.JACKPOT_SHIFT_GRAVEYARD) {
											slipData.setShift(IKeypadProcessConstants.DEFAULT_JACKPOT_SHIFT_GRAVEYARD_SHORT);
										}
									}

									slipData.setKioskProcessed(processSession
											.getAcnfNumber());
									slipData.setActrLogin(PadderUtil.lPad(
											String.valueOf(processSession
													.getEmployeeId().intValue()),
											IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
									slipData.setCreatedUser(PadderUtil.lPad(
											String.valueOf(processSession
													.getEmployeeId().intValue()),
											IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
									slipData.setCreatedTs(DateHelper
											.getUTCTimeFromLocal(currentDate));
									sessionObj.save(slipData);

									/** NEW RECORD IN THE TRANSACTION TABLE */
									Transaction transaction = new Transaction();
									transaction.setId(slipPrimaryKey);
									transaction
											.setSlipReferenceId(slipReferenceToProcess
													.getId());
									transaction
											.setMessageId(slipReferenceToProcess
													.getMessageId());
									transaction
											.setStatusFlagId(slipReferenceToProcess
													.getStatusFlagId());
									transaction
											.setPostFlagId(NOT_POSTED_FLAG_ID);
									if (slipReferenceToProcess
											.getStatusFlagId() == PROCESSED_STATUS_ID) {
										transaction
												.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
									} else {
										transaction
												.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
									}
									transaction
											.setEmployeeId(PadderUtil.lPad(
													String.valueOf(processSession
															.getEmployeeId()
															.intValue()),
													IAppConstants.EMPLOYEE_ID_PAD_LENGTH));

									if (processSession.getEmployeeFirstName() != null
											&& !((processSession
													.getEmployeeFirstName()
													.trim() + " ")
													.equalsIgnoreCase(" "))) {
										transaction
												.setEmployeeFirstName(processSession
														.getEmployeeFirstName());
									}
									if (processSession.getEmployeeLastName() != null
											&& !((processSession
													.getEmployeeLastName()
													.trim() + " ")
													.equalsIgnoreCase(" "))) {
										transaction
												.setEmployeeLastName(processSession
														.getEmployeeLastName());
									}

									if (processSession.getPrinterSelName() != null
											&& !((processSession
													.getPrinterSelName().trim() + " ")
													.equalsIgnoreCase(" "))) {

										if (processSession.getPrinterSelName()
												.length() > 200) {
											transaction
													.setPrinterUsed(processSession
															.getPrinterSelName()
															.substring(0, 200));
										} else {
											transaction
													.setPrinterUsed(processSession
															.getPrinterSelName());
										}
									}

									transaction.setCreatedTs(DateHelper
											.getUTCTimeFromLocal(currentDate));
									transaction
											.setCreatedUser(PadderUtil.lPad(
													String.valueOf(processSession
															.getEmployeeId()
															.intValue()),
													IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
									sessionObj.save(transaction);
								}
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (HibernateException e) {
				log.error("HibernateException in ", e);
				throw new JackpotDAOException(e);
			} catch (Exception e2) {
				log.error("Exception in processJackpot", e2);
				throw new JackpotDAOException(e2);
			}
		}
		return checkForExistence;
	}

	/**
	 * Method to get the slipReferenceId for inserting into ProcessSession for
	 * keypad processing
	 * 
	 * @param processSession
	 * @param siteNumber
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	@SuppressWarnings("unchecked")
	public static long getLatestSlipReferenceId(String assetConfigNumber,
			int siteId) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside getSlipReferenceId DAO");
		}
		long slipReferenceId = 0;
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criterion criterionAssetNumber = Restrictions.eq("acnfNumber",
					assetConfigNumber);
			Criterion criterionSlipTypeId = Restrictions.and(
					criterionAssetNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion criterionStatusFlagId = Restrictions.and(
					criterionSlipTypeId,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion criterionProcessFlag = Restrictions
					.and(criterionStatusFlagId,
							Restrictions.isNull("processFlagId"));
			Criterion criterionSiteNumber = Restrictions.and(
					criterionProcessFlag, Restrictions.eq("siteId", siteId));
			Criteria criteriaSlipRef = sessionObj
					.createCriteria(SlipReference.class)
					.add(criterionSiteNumber).addOrder(Order.desc("createdTs"));

			if (criteriaSlipRef != null) {
				List listSlipReference = criteriaSlipRef.list();
				if (listSlipReference != null && listSlipReference.size() != 0) {
					// taking the first element only since it will be the
					// latest.
					SlipReference slipReference = (SlipReference) listSlipReference
							.get(0);
					/** Getting the Slip Reference Table */
					if (slipReference != null) {
						slipReferenceId = slipReference.getId();
						if (log.isDebugEnabled()) {
							log.debug("slipReference.getId() : "
									+ slipReferenceId);
							log.debug("slipReference.getAcnfNumber() : "
									+ slipReference.getAcnfNumber());
						}
					} else {
						if (log.isInfoEnabled()) {
							log.info("slipReference is null");
						}
					}
				} else {
					if (log.isInfoEnabled()) {
						log.info("listSlipReference is null or size is 0");
					}
				}
			} else {
				if (log.isInfoEnabled()) {
					log.info("criteriaSlipRef is null/ no results available");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in processJackpot", e2);
			throw new JackpotDAOException(e2);
		}
		return slipReferenceId;
	}

	/**
	 * Method to retrieve the slip ref id and jackpot id
	 * 
	 * @param assetConfigNumber
	 * @param siteId
	 * @return Object[]
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	public static Object[] getLatestSlipReferenceIdJpId(
			String assetConfigNumber, int siteId) throws JackpotDAOException {

		if (log.isInfoEnabled()) {
			log.info("Inside getLatestSlipReferenceIdJpId DAO");
		}
		Object[] slipRefAry = null;
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Projection prjSlipRefId = Projections.property("id");
			Projection prjJpId = Projections.property("jp.jackpotId");
			Projection prjStatusFlagId = Projections.property("statusFlagId");
			Projection prjJPOrgAmt = Projections.property("jp.originalAmount");
			ProjectionList prjList = Projections.projectionList();
			prjList.add(prjSlipRefId);
			prjList.add(prjJpId);
			prjList.add(prjStatusFlagId);
			prjList.add(prjJPOrgAmt);

			Criterion criterionAssetNumber = Restrictions.eq("acnfNumber",
					assetConfigNumber);
			Criterion criterionSlipTypeId = Restrictions.and(
					criterionAssetNumber,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			/*
			 * Not Looking for Slip with Pending Status, because the latest
			 * Jackpot Hit at a Slot may not be in pending status and we wouldnt
			 * be picking a wrong JP
			 */
			// Criterion criterionStatusFlagId =
			// Restrictions.and(criterionSlipTypeId,
			// Restrictions.eq("statusFlagId", PENDING_STATUS_ID));

			Criterion criterionProcessFlag = Restrictions.and(
					criterionSlipTypeId, Restrictions.isNull("processFlagId"));
			Criterion criterionSiteNumber = Restrictions.and(
					criterionProcessFlag, Restrictions.eq("siteId", siteId));

			Criteria criteriaSlipRef = sessionObj
					.createCriteria(SlipReference.class)
					.add(criterionSiteNumber)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.setProjection(prjList).addOrder(Order.desc("createdTs"));

			List<Object[]> listSlipReference = criteriaSlipRef.list();
			if (listSlipReference != null && listSlipReference.size() > 0) {
				Object[] obj = listSlipReference.get(0);
				if (obj != null
						&& ((Short) obj[2]).shortValue() == PENDING_STATUS_ID) {
					slipRefAry = new Object[4];
					// TAKE ONLY THE FIRST LATEST REC
					slipRefAry[0] = obj[0];
					slipRefAry[1] = obj[1];
					slipRefAry[2] = obj[2];
					slipRefAry[3] = obj[3];

				} else {
					if (obj != null && log.isInfoEnabled())
						log.info("Latest retrieved SlipId  " + obj[0]
								+ " and its status is " + obj[2]);
					log.error("There is no matching Pending status record to do keypad processing");
				}
			} else {
				log.info("Slip Ref Lst is NULL/ NO results available");
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getLatestSlipReferenceIdJpId: ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getLatestSlipReferenceIdJpId: ", e2);
			throw new JackpotDAOException(e2);
		}
		return slipRefAry;
	}

	/**
	 * Method to post the jackpot details after printing the slip
	 * 
	 * @param jackpotDTO
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot details proccessing failed
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO updateProcessJackpot(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		JackpotDTO returnJackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside updateProcessJackpot DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			if (log.isInfoEnabled()) {
				log.info("Seq no: " + jackpotDTO.getSequenceNumber());
			}

			Criterion crtnSeqNo = Restrictions.eq("sequenceNumber",
					jackpotDTO.getSequenceNumber());
			Criterion crtSlipTypeId = Restrictions.and(crtnSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtStatusFlagId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion crtSiteId = Restrictions.and(crtStatusFlagId,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));

			if (log.isInfoEnabled()) {
				log.info("Getting the slipReferenceCriteria");
			}

			/**
			 * OBTAIN THE SLIP REFERENCE OBJECT FROM THE SLIP_REFERENCE TABLE
			 * USING THE SEQ NUMBER AND SLIP TYPE ID
			 */
			Criteria slipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			/** USE UNIQUERESULT FOR SINGLE VALUE * */
			SlipReference slipReference = (SlipReference) slipReferenceCriteria
					.uniqueResult();

			/** Get the system current date/time * */
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			if (log.isInfoEnabled()) {
				log.info("Current date " + currentDate);
			}

			/** FLAG FOR ALLOW PROCESS FOR CASHIER DESK */
			String cashierDeskEnabled = jackpotDTO.getCashierDeskEnabled();

			if (slipReference != null) {

				boolean isS2SErrorPresent = false;
				if (jackpotDTO.isS2SJackpotProcess()) {
					long orgAmount = 0;
					Iterator it = slipReference.getJackpot().iterator();
					if (it.hasNext()) {
						Jackpot jackpot = (Jackpot) it.next();
						orgAmount = jackpot.getOriginalAmount() != null ? jackpot
								.getOriginalAmount() : 0;
					}

					String responseCode = JackpotUtil.validateS2SJackpot(
							jackpotDTO.getSiteId(), jackpotDTO.getHpjpAmount(),
							orgAmount);
					isS2SErrorPresent = JackpotUtil
							.isS2SErrorPresent(responseCode);
					if (isS2SErrorPresent) {
						returnJackpotDTO = new JackpotDTO();
						returnJackpotDTO.setResponseCode(responseCode);
						return returnJackpotDTO;
					}
				}

				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						jackpotDTO.getAssetConfigNumber(),
						jackpotDTO.getSiteId(), 0);

				/** UPDATE THE ROW IN SLIP_REFERENCE TABLE * */

				slipReference.setTransactionId(slipPrimaryKey);
				if (jackpotDTO.getProcessFlagId() != 0) {
					slipReference.setProcessFlagId(jackpotDTO
							.getProcessFlagId());
				}
				if (jackpotDTO.getOriginalAmount() != 0
						&& jackpotDTO.getHpjpAmount() != 0
						&& (jackpotDTO.getOriginalAmount() != jackpotDTO
								.getHpjpAmount())) {
					// JACKPOT AMOUNT CHANGED
					slipReference.setStatusFlagId(CHANGE_STATUS_ID);
					slipReference
							.setChangeFlag(IAppConstants.CHANGE_FLAG_ENABLED);
					if (cashierDeskEnabled.equalsIgnoreCase("No")
							|| jackpotDTO.getProcessFlagId() == ILookupTableConstants.POUCH_PAY_PROCESS_FLAG
							|| jackpotDTO.getProcessFlagId() == ILookupTableConstants.EXPRESS_PROCESS_FLAG
							|| jackpotDTO.isS2SJackpotProcess()) {
						slipReference.setPostToAccounting(POSTED_TO_ACCOUNTING);
					}
				} else {
					if (cashierDeskEnabled.equalsIgnoreCase("No")
							|| jackpotDTO.getProcessFlagId() == ILookupTableConstants.POUCH_PAY_PROCESS_FLAG
							|| jackpotDTO.getProcessFlagId() == ILookupTableConstants.EXPRESS_PROCESS_FLAG
							|| jackpotDTO.isS2SJackpotProcess()) {
						// OLD FLOW OR POUCH PAY, EXPRESS JACKPOTS
						slipReference.setStatusFlagId(PROCESSED_STATUS_ID);
						slipReference.setPostToAccounting(POSTED_TO_ACCOUNTING);
					} else if (cashierDeskEnabled.equalsIgnoreCase("Yes")) {
						// CASHIER DESK FLOW
						slipReference.setStatusFlagId(PRINTED_STATUS_ID);
					}
				}
				if (jackpotDTO.getCashlessAccountType() != null) {
					slipReference.setAccountType(jackpotDTO
							.getCashlessAccountType());
				}
				if (jackpotDTO.getAccountNumber() != null) {
					slipReference.setAccountNumber(jackpotDTO
							.getAccountNumber());
				}
				slipReference.setSiteNo(jackpotDTO.getSiteNo());
				slipReference.setUpdatedTs(DateHelper
						.getUTCTimeFromLocal(currentDate));
				slipReference.setUpdatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				sessionObj.save(slipReference);

				/** UPDATE THE ROW IN JACKPOT TABLE * */
				Set<Jackpot> jackpotSet = slipReference.getJackpot();
				Jackpot jackpot = null;
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {

					jackpot = (Jackpot) iter.next();
					if (jackpotDTO.getJackpotId() != null
							&& jackpotDTO.getJackpotId().equalsIgnoreCase("FE")) {
						if (log.isDebugEnabled()) {
							log.debug("Jackpot Id is reset to FE - CANCEL CREDIT");
						}
						jackpot.setJackpotId(jackpotDTO.getJackpotId());
					}
					if (jackpotDTO.getJackpotTypeId() != 0
							&& jackpotDTO.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
						if (log.isDebugEnabled()) {
							log.debug("Jackpot Type is reset to 7006 - CANCEL CREDIT");
						}
						jackpot.setJackpotTypeId(jackpotDTO.getJackpotTypeId());
					}
					if (jackpotDTO.getTaxTypeId() != 0) {
						jackpot.setTaxTypeId(jackpotDTO.getTaxTypeId());
					}
					if (jackpotDTO.getAssociatedPlayerCard() != null
							&& !jackpotDTO.getAssociatedPlayerCard().trim()
									.isEmpty()) {
						jackpot.setAssociatedPlayerCard(jackpotDTO
								.getAssociatedPlayerCard());
					}
					if (jackpotDTO.getPlayerCard() != null
							&& !jackpotDTO.getPlayerCard().trim().isEmpty()) {
						jackpot.setPlayerCard(jackpotDTO.getPlayerCard());
					} else {
						jackpot.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
					}
					jackpot.setPlayerName(jackpotDTO.getPlayerName());
					jackpot.setHpjpAmount(jackpotDTO.getHpjpAmount());
					if (jackpotDTO.getHpjpAmountRounded() != 0) {
						jackpot.setHpjpAmountRounded(jackpotDTO
								.getHpjpAmountRounded());
					}
					jackpot.setMachinePaidAmount(jackpotDTO
							.getMachinePaidAmount());
					jackpot.setNetAmount(jackpotDTO.getJackpotNetAmount());
					jackpot.setTaxAmount(jackpotDTO.getTaxAmount());
					jackpot.setTaxRateAmount(jackpotDTO.getTaxRateAmount());
					if (jackpotDTO.getExpiryDate() != null) {
						jackpot.setExpiryDate(DateHelper
								.getUTCTimeFromLocal(DateUtil.getTimeStamp(
										jackpotDTO.getExpiryDate(),
										IAppConstants.EXPIRY_DATE_FORMAT)));
					}
					if (jackpotDTO.getCoinsPlayed() != 0) {
						jackpot.setCoinsPlayed(jackpotDTO.getCoinsPlayed());
					}
					jackpot.setWinningCombination(jackpotDTO.getWinningComb());
					jackpot.setPayline(jackpotDTO.getPayline());
					jackpot.setInterceptAmount(jackpotDTO.getInterceptAmount());
					jackpot.setUpdatedTs(DateHelper
							.getUTCTimeFromLocal(currentDate));
					jackpot.setUpdatedUser(PadderUtil.lPad(
							jackpotDTO.getPrintEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(jackpot);
				}

				/** UPDATE THE ROW IN SLIP_DATA TABLE * */
				SlipData slipData = new SlipData();
				slipData.setId(slipPrimaryKey);
				slipData.setSlipReferenceId(slipReference.getId());
				slipData.setAuthEmployeeId1(PadderUtil.lPad(
						jackpotDTO.getAuthEmployeeId1(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				slipData.setAuthEmployeeId2(PadderUtil.lPad(
						jackpotDTO.getAuthEmployeeId2(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				slipData.setAmountAuthEmpId(PadderUtil.lPad(
						jackpotDTO.getAmountAuthEmpId(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				slipData.setShift(jackpotDTO.getShift());
				slipData.setWindowNumber(jackpotDTO.getWindowNumber());
				slipData.setKioskProcessed(jackpotDTO.getAssetConfNumberUsed());
				slipData.setActrLogin(PadderUtil.lPad(
						jackpotDTO.getActorLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				slipData.setCreatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				slipData.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(currentDate));
				sessionObj.save(slipData);

				if (slipReference.getSlipData() != null) {
					Set<SlipData> slipDataSet = new HashSet<SlipData>();
					slipDataSet.add(slipData);
					slipReference.setSlipData(slipDataSet);
				} else {
					slipReference.getSlipData().add(slipData);
				}

				/** CREATE A ROW IN THE TRANSACTION TABLE */
				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId(slipReference.getId());
				transaction.setMessageId(slipReference.getMessageId());
				transaction.setStatusFlagId(slipReference.getStatusFlagId());
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				if (POSTED_TO_ACCOUNTING.equals(slipReference
						.getPostToAccounting())) {
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
				} else {
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
				}
				transaction.setEmployeeId(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				transaction.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				transaction.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				// transaction.setCashDeskLocation(jackpotDTO.getCashDeskLocation());
				if (jackpotDTO.getPrinterUsed() != null) {
					if (jackpotDTO.getPrinterUsed().length() > 200) {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed()
								.substring(0, 200));
					} else {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed());
					}
				}
				transaction.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(currentDate));
				transaction.setCreatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				sessionObj.save(transaction);

				if (slipReference.getTransaction() != null) {
					Set<Transaction> transactionSet = new HashSet<Transaction>();
					transactionSet.add(transaction);
					slipReference.setTransaction(transactionSet);
				} else {
					slipReference.getTransaction().add(transaction);
				}

				returnJackpotDTO = JackpotHelper.getJackpotDTO(slipReference);
				returnJackpotDTO.setPrintEmployeeLogin(jackpotDTO
						.getPrintEmployeeLogin());
				returnJackpotDTO.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				returnJackpotDTO.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				returnJackpotDTO.setActorLogin(jackpotDTO.getActorLogin());
				returnJackpotDTO.setSiteNo(jackpotDTO.getSiteNo());
				returnJackpotDTO.setSlipReferenceId(slipReference.getId());
				returnJackpotDTO.setStatusFlagId(slipReference
						.getStatusFlagId());
				returnJackpotDTO.setJackpotTypeId(jackpot.getJackpotTypeId());
				// Setting the Site Config param for process flow
				returnJackpotDTO.setCashierDeskEnabled(jackpotDTO
						.getCashierDeskEnabled() != null ? jackpotDTO
						.getCashierDeskEnabled() : "");

				returnJackpotDTO
						.setStatusFlagDesc(getStatusDescription(slipReference
								.getStatusFlagId()));
				returnJackpotDTO
						.setJackpotTypeDesc(getJackpotTypeDescription(jackpot
								.getJackpotTypeId()));
				returnJackpotDTO
						.setProcessFlagId(jackpotDTO.getProcessFlagId());
				returnJackpotDTO
						.setProcessFlagDesc(getProcessFlagDescription(jackpotDTO
								.getProcessFlagId()));
				returnJackpotDTO.setPostToAccounting(slipReference
						.getPostToAccounting());
				returnJackpotDTO.setS2SJackpotProcess(jackpotDTO
						.isS2SJackpotProcess());
				returnJackpotDTO.setInterceptAmount(jackpotDTO
						.getInterceptAmount());

				if (log.isDebugEnabled()) {
					log.debug("Status Flag Desc:"
							+ returnJackpotDTO.getStatusFlagDesc());
					log.debug("returnJackpotDTO.getSlipReferenceId(): "
							+ returnJackpotDTO.getSlipReferenceId());
				}
				returnJackpotDTO.setPostedSuccessfully(true);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Record is not updated");
				}
				if (log.isDebugEnabled()) {
					log.debug("************************************************************");
				}
				return returnJackpotDTO;
			}
		} catch (HibernateException e) {
			log.error("HibernateException in ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in processJackpot", e2);
			throw new JackpotDAOException(e2);
		}
		if (log.isInfoEnabled()) {
			log.info("END OF updateProcessJackpot");
		}
		return returnJackpotDTO;
	}

	/**
	 * Method to post the jackpot details after printing a manual slip
	 * 
	 * @param jackpotDTO
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot details proccessing failed
	 */
	public static JackpotDTO updateProcessManualJackpot(JackpotDTO jackpotDTO)
			throws JackpotDAOException {

		// long sequenceNumber = 0;
		long messageId = MessageUtil.getMessageId();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside updateProcessManualJackpot DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			if (log.isDebugEnabled()) {
				log.debug("************************************************************");
			}

			/** Create a record in the JackpotSequence table */
			JackpotSequence jpSequence = new JackpotSequence();
			sessionObj.save(jpSequence);
			if (log.isDebugEnabled()) {
				log.debug("Sequence Number..." + jpSequence.getJpseId());
			}
			long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
					jackpotDTO.getAssetConfigNumber(), jackpotDTO.getSiteId(),
					0);

			/** Create a record in the SlipReference table */
			SlipReference slipReference = new SlipReference();
			slipReference.setId(slipPrimaryKey);
			slipReference.setTransactionId(slipPrimaryKey);
			slipReference.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);

			if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("No")) {
				slipReference.setStatusFlagId(PROCESSED_STATUS_ID); // OLD FLOW
				slipReference.setPostToAccounting(POSTED_TO_ACCOUNTING);
			} else if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase(
					"Yes")) {
				slipReference.setStatusFlagId(PRINTED_STATUS_ID); // CASHIER
																	// DESK FLOW
				slipReference.setPostToAccounting(NOT_POSTED_TO_ACCOUNTING);
			}
			if (log.isInfoEnabled()) {
				log.info("slipRef.getStatusFlagId() : "
						+ slipReference.getStatusFlagId());
			}
			if (jackpotDTO.getCashlessAccountType() != null) {
				slipReference.setAccountType(jackpotDTO
						.getCashlessAccountType());
			}
			if (jackpotDTO.getAccountNumber() != null) {
				slipReference.setAccountNumber(jackpotDTO.getAccountNumber());
			}

			if (jackpotDTO.getProcessFlagId() != 0) {
				slipReference.setProcessFlagId(jackpotDTO.getProcessFlagId());
			}
			slipReference.setSiteId(jackpotDTO.getSiteId());
			slipReference.setSiteNo(jackpotDTO.getSiteNo());
			slipReference.setAreaShortName(jackpotDTO.getAreaShortName());
			slipReference.setAreaLongName(jackpotDTO.getAreaLongName());
			slipReference.setAcnfNumber(jackpotDTO.getAssetConfigNumber());
			slipReference.setAcnfLocation(jackpotDTO.getAssetConfigLocation()
					.toUpperCase());
			slipReference.setSealNumber(jackpotDTO.getSealNumber());
			slipReference.setSlotDenomination(jackpotDTO.getSlotDenomination());
			slipReference.setSlotDescription(jackpotDTO.getSlotDescription());
			slipReference.setGmuDenomination(jackpotDTO.getGmuDenom());
			if (jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("yes")) {
				// Setting sequence number as combination number to make it
				// random for SA
				slipReference.setSequenceNumber(Long.parseLong(ConversionUtil
						.getRandomSequenceNumber() + jpSequence.getJpseId()));
			} else {
				slipReference.setSequenceNumber(jpSequence.getJpseId());
			}
			slipReference.setMessageId(messageId);
			// Get the system current time
			Timestamp createDate = new Timestamp(Calendar.getInstance()
					.getTimeInMillis());
			if (log.isDebugEnabled()) {
				log.debug("************************ created date = "
						+ createDate);
			}
			slipReference.setTransactionDate(DateHelper
					.getUTCTimeFromLocal(createDate));
			slipReference.setChangeFlag(IAppConstants.CHANGE_FLAG_DISABLED);
			slipReference.setCreatedTs(DateHelper
					.getUTCTimeFromLocal(createDate));
			slipReference.setCreatedUser(PadderUtil.lPad(
					jackpotDTO.getPrintEmployeeLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			sessionObj.save(slipReference);

			// Create a record in the Jackpot table
			Jackpot jackpot = new Jackpot();
			jackpot.setId(slipPrimaryKey);

			jackpot.setSlipReferenceId(slipReference.getId());
			jackpot.setJackpotId(jackpotDTO.getJackpotId().toUpperCase());
			jackpot.setJackpotTypeId(jackpotDTO.getJackpotTypeId());

			if (jackpotDTO.getTaxTypeId() != 0) {
				jackpot.setTaxTypeId(jackpotDTO.getTaxTypeId());
			}
			if (jackpotDTO.getAssociatedPlayerCard() != null
					&& !jackpotDTO.getAssociatedPlayerCard().trim().isEmpty()) {
				jackpot.setAssociatedPlayerCard(jackpotDTO
						.getAssociatedPlayerCard());
			} else {
				jackpot.setAssociatedPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
			}
			if (jackpotDTO.getPlayerCard() != null
					&& !jackpotDTO.getPlayerCard().trim().isEmpty()) {
				jackpot.setPlayerCard(jackpotDTO.getPlayerCard());
			} else {
				jackpot.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
			}
			jackpot.setPlayerName(jackpotDTO.getPlayerName());
			jackpot.setOriginalAmount(0l);
			jackpot.setHpjpAmount(jackpotDTO.getHpjpAmount());
			if (jackpotDTO.getHpjpAmountRounded() != 0) {
				jackpot.setHpjpAmountRounded(jackpotDTO.getHpjpAmountRounded());
			}
			if (log.isDebugEnabled()) {
				log.debug("Jackpot Amount..." + jackpotDTO.getHpjpAmount());
			}
			jackpot.setMachinePaidAmount(jackpotDTO.getMachinePaidAmount());
			jackpot.setNetAmount(jackpotDTO.getJackpotNetAmount());
			jackpot.setTaxRateAmount(jackpotDTO.getTaxRateAmount());
			jackpot.setTaxAmount(jackpotDTO.getTaxAmount());
			if (jackpotDTO.getExpiryDate() != null) {
				jackpot.setExpiryDate(DateHelper.getUTCTimeFromLocal(DateUtil
						.getTimeStamp(jackpotDTO.getExpiryDate(),
								IAppConstants.EXPIRY_DATE_FORMAT)));
			}
			if (jackpotDTO.getCoinsPlayed() != 0) {
				jackpot.setCoinsPlayed(jackpotDTO.getCoinsPlayed());
			}
			jackpot.setWinningCombination(jackpotDTO.getWinningComb());
			jackpot.setBlindAttempt(BLIND_ATTEMPT); // This is to make sure that
			// always the blind attempt
			// field is not null.
			jackpot.setPayline(jackpotDTO.getPayline());
			jackpot.setInterceptAmount(jackpotDTO.getInterceptAmount());
			jackpot.setIsSlotOnline(new Short("0")); // SETTING AS OFFLINE
														// MESSAGE
			jackpot.setProgressiveLevel(jackpotDTO.getProgressiveLevel()); // SETTING
																			// THE
																			// PROGRESSIVE
																			// LEVEL
																			// VALUE
																			// FOR
																			// POSTING
																			// TO
																			// LIABILITY
			jackpot.setGeneratedBy(JpGeneratedBy.SYSTEM.getValue());// FOR
																	// CONTROLLER
																	// CREATED
																	// JPS
																	// DIFFERENTIATION
			jackpot.setCreatedTs(DateHelper.getUTCTimeFromLocal(createDate));
			jackpot.setCreatedUser(PadderUtil.lPad(
					jackpotDTO.getPrintEmployeeLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			if (log.isDebugEnabled()) {
				log.debug("Player Card Number..." + jackpotDTO.getPlayerCard());
				log.debug("************************************************************");
			}
			sessionObj.save(jackpot);

			/** Create a record in the SLIP DATA table */

			SlipData slipData = new SlipData();
			slipData.setId(slipPrimaryKey);
			slipData.setSlipReferenceId(slipReference.getId());
			slipData.setAuthEmployeeId1(PadderUtil.lPad(
					jackpotDTO.getAuthEmployeeId1(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			slipData.setAuthEmployeeId2(PadderUtil.lPad(
					jackpotDTO.getAuthEmployeeId2(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			slipData.setShift(jackpotDTO.getShift());
			slipData.setWindowNumber(jackpotDTO.getWindowNumber());
			slipData.setKioskProcessed(jackpotDTO.getAssetConfNumberUsed());
			slipData.setActrLogin(PadderUtil.lPad(jackpotDTO.getActorLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			slipData.setCreatedTs(DateHelper.getUTCTimeFromLocal(createDate));
			slipData.setCreatedUser(PadderUtil.lPad(
					jackpotDTO.getPrintEmployeeLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			sessionObj.save(slipData);

			/** Create a record in the TRANSACTION table */

			Transaction transaction = new Transaction();
			transaction.setId(slipPrimaryKey);
			transaction.setSlipReferenceId(slipReference.getId());
			transaction.setStatusFlagId(slipReference.getStatusFlagId());
			transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
			transaction.setMessageId(messageId);
			if (POSTED_TO_ACCOUNTING
					.equals(slipReference.getPostToAccounting())) {
				transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
			} else {
				transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
			}
			transaction.setEmployeeId(PadderUtil.lPad(
					jackpotDTO.getPrintEmployeeLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			if (jackpotDTO.getPrinterUsed() != null) {
				if (jackpotDTO.getPrinterUsed().length() > 200) {
					transaction.setPrinterUsed(jackpotDTO.getPrinterUsed()
							.substring(0, 200));
				} else {
					transaction.setPrinterUsed(jackpotDTO.getPrinterUsed());
				}

			}
			transaction.setEmployeeFirstName(jackpotDTO.getEmployeeFirstName());
			transaction.setEmployeeLastName(jackpotDTO.getEmployeeLastName());
			// transaction.setCashDeskLocation(jackpotDTO.getCashDeskLocation());
			transaction
					.setCreatedTs(DateHelper.getUTCTimeFromLocal(createDate));

			transaction.setCreatedUser(PadderUtil.lPad(
					jackpotDTO.getPrintEmployeeLogin(),
					IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			sessionObj.save(transaction);

			// SETTING RETURN JACKPOTDTO OBJECT
			jackpotDTO.setSlipReferenceId(slipReference.getId());
			jackpotDTO.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
			jackpotDTO.setSlipTypeDesc(mapSlipType.get(
					ILookupTableConstants.JACKPOT_SLIP_TYPE_ID).toString());
			jackpotDTO.setStatusFlagId(slipReference.getStatusFlagId());
			jackpotDTO.setStatusFlagDesc(mapStatusFlag.get(
					jackpotDTO.getStatusFlagId()).toString());
			jackpotDTO.setPostToAccounting(slipReference.getPostToAccounting());

			jackpotDTO.setTransactionDate(DateHelper
					.getLocalTimeFromUTC(slipReference.getTransactionDate()));
			jackpotDTO.setSequenceNumber(slipReference.getSequenceNumber());
			jackpotDTO.setSlipId(Long.toString(jackpot.getId()));
			jackpotDTO.setJackpotTypeDesc(mapJackpotType.get(
					jackpotDTO.getJackpotTypeId()).toString());
			jackpotDTO.setProcessFlagDesc(mapProcessFlag.get(
					jackpotDTO.getProcessFlagId()).toString());

			jackpotDTO.setPrintDate(transaction.getCreatedTs());
			jackpotDTO.setPostFlagId(NOT_POSTED_FLAG_ID);
			jackpotDTO.setTransMessageId(messageId);
			jackpotDTO.setMessageId(messageId);
			jackpotDTO.setSlotOnline(false);
			jackpotDTO.setLstProgressiveLevel(jackpotDTO
					.getLstProgressiveLevel());
			jackpotDTO.setProgressiveLevel(jackpotDTO.getProgressiveLevel());

			if (log.isDebugEnabled()) {
				log.debug("************************************************************************");
			}
			if (log.isInfoEnabled()) {
				log.info("Jackpot manual slip created successfully with the sequence no : "
						+ jackpotDTO.getSequenceNumber());
			}
			if (log.isDebugEnabled()) {
				log.debug("************************************************************************");
			}
			jackpotDTO.setPostedSuccessfully(true);

		} catch (HibernateException e) {
			log.error("HibernateException in updateProcessManualJackpot", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in updateProcessManualJackpot", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method is to update the status of a particular jackpot
	 * 
	 * @param sequenceNumber
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot status updation failed
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public static JackpotDTO postUpdateJackpotVoidStatus(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside postUpdateJackpotVoidStatus DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					jackpotDTO.getSequenceNumber());
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();
			Timestamp currentLocalTime = new Timestamp(
					System.currentTimeMillis());
			if (slipReference != null) {
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						slipReference.getAcnfNumber(), jackpotDTO.getSiteId(),
						0);
				slipReference.setTransactionId(slipPrimaryKey);
				slipReference.setStatusFlagId(VOID_STATUS_ID);
				slipReference.setChangeFlag(IAppConstants.CHANGE_FLAG_DISABLED);
				slipReference.setUpdatedTs(DateHelper
						.getUTCTimeFromLocal(currentLocalTime));
				if (jackpotDTO.getVoidEmployeeLogin() != null) {
					slipReference.setUpdatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				sessionObj.save(slipReference);

				/** Create a record in the transaction TABLE */

				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId(slipReference.getId());
				transaction.setStatusFlagId(VOID_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setMessageId(slipReference.getMessageId());
				if (POSTED_TO_ACCOUNTING.equals(slipReference
						.getPostToAccounting())) {
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
				} else {
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
				}
				if (jackpotDTO.getVoidEmployeeLogin() != null) {
					transaction.setEmployeeId(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				if (jackpotDTO.getPrinterUsed() != null) {
					if (jackpotDTO.getPrinterUsed().length() > 200) {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed()
								.substring(0, 200));
					} else {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed());
					}

				}
				transaction.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				transaction.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				transaction.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(currentLocalTime));
				if (jackpotDTO.getVoidEmployeeLogin() != null) {
					transaction.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				// SETTING CASH DESK LOCATION FROM WHERE JACKPOT VOID IS
				// PROCESSED
				// transaction.setCashDeskLocation(jackpotDTO.getCashDeskLocation());
				sessionObj.save(transaction);

				if (log.isDebugEnabled()) {
					log.debug("S2SPendingVoid Enabled : "
							+ jackpotDTO.isS2SPendingVoid());
				}

				// FOR PENDING VOID FROM S2S INSERT A SLIP_DATA REC
				if (jackpotDTO.isS2SPendingVoid()) {
					if (log.isDebugEnabled()) {
						log.debug("BEFORE inserting SLIP_DATA");
					}
					// CREATE THE ROW IN SLIP_DATA TABLE
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(jackpotDTO
							.getAssetConfNumberUsed());
					if (jackpotDTO.getVoidEmployeeLogin() != null) {
						slipData.setActrLogin(PadderUtil.lPad(
								jackpotDTO.getVoidEmployeeLogin(),
								IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
						slipData.setCreatedUser(PadderUtil.lPad(
								jackpotDTO.getVoidEmployeeLogin(),
								IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					}
					slipData.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);
					if (log.isDebugEnabled()) {
						log.debug("AFTER inserting SLIP_DATA");
					}
				}

				// FOR A NORMAL PROCESSED VOID SLIP
				// SLIP INFO SHOULD BE SENT TO ACCOUNTING AND ALERT SHOULD BE
				// SENT
				if (!jackpotDTO.isS2SPendingVoid()) {

					/** QUERY TO GET THE JACKPOT TABLE INFO */

					if (log.isDebugEnabled()) {
						log.debug("Query to get the Jackpot and SlipData info for VOID rec");
					}

					String jackpotStr = "Select jp.id,jp.jackpotTypeId,jp.originalAmount,jp.hpjpAmount,jp.hpjpAmountRounded"
							+ ",jp.netAmount,sd.actrLogin,sd.authEmployeeId1,sd.authEmployeeId2,jp.jackpotId,sd.kioskProcessed"
							+ " from com.ballydev.sds.jackpot.db.slip.Jackpot jp,"
							+ "com.ballydev.sds.jackpot.db.slip.SlipData sd where jp.slipReferenceId="
							+ slipReference.getId()
							+ " and sd.slipReferenceId="
							+ slipReference.getId();

					Query queryJackpot = sessionObj.createQuery(jackpotStr);

					ArrayList<Object[]> listJackpot = (ArrayList<Object[]>) queryJackpot
							.list();
					if (listJackpot != null && listJackpot.size() > 0) {
						if (log.isDebugEnabled()) {
							log.debug("JackpotList size: " + listJackpot.size());
						}
						jackpotDTO
								.setSlipId((listJackpot.get(0)[0]).toString());
						jackpotDTO
								.setJackpotTypeId((Short) listJackpot.get(0)[1]);
						jackpotDTO
								.setOriginalAmount((Long) listJackpot.get(0)[2]);
						jackpotDTO.setHpjpAmount((Long) listJackpot.get(0)[3]);
						if ((Long) listJackpot.get(0)[4] != null) {
							jackpotDTO.setHpjpAmountRounded((Long) listJackpot
									.get(0)[4]);
						} else {
							jackpotDTO.setHpjpAmountRounded(0);
						}
						jackpotDTO.setJackpotNetAmount((Long) listJackpot
								.get(0)[5]);
						if (listJackpot.get(0)[6] != null) {
							jackpotDTO.setActorLogin(listJackpot.get(0)[6]
									.toString());
						}
						if (listJackpot.get(0)[7] != null) {
							jackpotDTO.setAuthEmployeeId1(listJackpot.get(0)[7]
									.toString());
						}
						if (listJackpot.get(0)[8] != null) {
							jackpotDTO.setAuthEmployeeId2(listJackpot.get(0)[8]
									.toString());
						}
						jackpotDTO.setJackpotId(listJackpot.get(0)[9]
								.toString());
						jackpotDTO
								.setAssetConfNumberUsed(listJackpot.get(0)[10]
										.toString());
					}

					/** QUERY TO GET THE SLOT ATTND ID INFO */

					JackpotDTO jpSlotAttDTORtn = JackpotHelper
							.getJackpotDTOSlotAttendantInfo(slipReference);

					if (jpSlotAttDTORtn != null) {
						jackpotDTO.setSlotAttendantId(jpSlotAttDTORtn
								.getSlotAttendantId());
					}

					jackpotDTO.setPrintEmployeeLogin(jackpotDTO
							.getVoidEmployeeLogin());
					jackpotDTO.setPrintDate(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					jackpotDTO.setAssetConfigNumber(slipReference
							.getAcnfNumber());
					jackpotDTO.setAssetConfigLocation(slipReference
							.getAcnfLocation());
					jackpotDTO.setMessageId(slipReference.getMessageId());
					jackpotDTO.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
					if (mapSlipType != null && mapSlipType.size() == 0) {
						getSlipTypeMapValues();
						jackpotDTO.setSlipTypeDesc(mapSlipType.get(
								ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
								.toString());
					} else {
						jackpotDTO.setSlipTypeDesc(mapSlipType.get(
								ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
								.toString());
					}
					jackpotDTO
							.setStatusFlagDesc(getStatusDescription(ILookupTableConstants.VOID_STATUS_ID));
					jackpotDTO
							.setStatusFlagId(ILookupTableConstants.VOID_STATUS_ID);
					jackpotDTO
							.setJackpotTypeDesc(getJackpotTypeDescription(jackpotDTO
									.getJackpotTypeId()));

					if (slipReference.getProcessFlagId() != null) {
						jackpotDTO.setProcessFlagId(slipReference
								.getProcessFlagId());
						jackpotDTO.setProcessFlagDesc(slipReference
								.getProcessFlag().getFlagDescription());
					}

					// jackpotDTO.setProcessFlagId(slipReference.getProcessFlagId());
					// if(mapProcessFlag!=null && mapProcessFlag.size()== 0 &&
					// slipReference.getProcessFlagId()!=0){
					// getProcessFlagMapValues();
					// jackpotDTO.setProcessFlagDesc(mapProcessFlag.get(slipReference.getProcessFlagId()).toString());
					// }else if(slipReference.getProcessFlagId()!=0){
					// jackpotDTO.setProcessFlagDesc(mapProcessFlag.get(slipReference.getProcessFlagId()).toString());
					// }

					if (slipReference.getAuditCodeId() != null) {
						jackpotDTO.setAuditCodeId(slipReference
								.getAuditCodeId());
						jackpotDTO.setAuditCodeDesc(slipReference
								.getAuditCode().getDescription());
					}

					jackpotDTO.setGmuDenom(slipReference.getGmuDenomination());
					jackpotDTO.setSlipReferenceId(slipReference.getId());
					jackpotDTO.setTransactionDate(DateHelper
							.getLocalTimeFromUTC(slipReference
									.getTransactionDate()));
					jackpotDTO.setSiteNo(slipReference.getSiteNo());

					jackpotDTO.setSendAlert(true);
				}
				jackpotDTO.setPostToAccounting(slipReference
						.getPostToAccounting());
				jackpotDTO.setAccountNumber(slipReference.getAccountNumber());
				jackpotDTO.setCashlessAccountType(slipReference
						.getAccountType());
				jackpotDTO.setPostedSuccessfully(true);
				jackpotDTO.setSlipRefUpdatedTs(slipReference.getUpdatedTs());

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The slip status has been changed to Void for the sequence: "
							+ jackpotDTO.getSequenceNumber());
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record to void ");
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				jackpotDTO.setSendAlert(false);
				jackpotDTO.setPostedSuccessfully(false);
			}
		} catch (HibernateException e) {
			log.error("HibernateException in postUpdateJackpotVoidStatus", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postUpdateJackpotVoidStatus", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method is to update the status of a particular jackpot
	 * 
	 * @param sequenceNumber
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot status updation failed
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO postUpdateJackpotReprint(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside postUpdateJackpotReprint DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			ArrayList statusFlagList = new ArrayList();
			statusFlagList.add(ILookupTableConstants.CHANGE_STATUS_ID);
			if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("No")) { // Old
																				// flow
				statusFlagList.add(ILookupTableConstants.PROCESSED_STATUS_ID);
			}
			statusFlagList.add(ILookupTableConstants.REPRINT_STATUS_ID);
			if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("Yes")) { // New
																				// Flow
																				// for
																				// cashier
																				// Desk
				statusFlagList.add(ILookupTableConstants.PRINTED_STATUS_ID);
			}

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					jackpotDTO.getSequenceNumber());
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtStatusId = Restrictions.and(crtSlipTypeid,
					Restrictions.in("statusFlagId", statusFlagList));
			Criterion crtSiteId = Restrictions.and(crtStatusId,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();
			Timestamp currentLocalTime = new Timestamp(
					System.currentTimeMillis());
			if (slipReference != null) {
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						slipReference.getAcnfNumber(), jackpotDTO.getSiteId(),
						0);
				String autoVoidReprintJackpot = jackpotDTO
						.getAutoVoidReprintedJp();

				if (autoVoidReprintJackpot.equalsIgnoreCase("Yes")) {
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setTransactionId(slipPrimaryKey);// SET TRANS
																	// ID FOR
																	// VOID

					jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);
					jackpotDTO.setPostToAccounting(slipReference
							.getPostToAccounting());

					jackpotDTO.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
					if (mapSlipType != null && mapSlipType.size() == 0) {
						getSlipTypeMapValues();
						jackpotDTO.setSlipTypeDesc(mapSlipType.get(
								ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
								.toString());
					} else {
						jackpotDTO.setSlipTypeDesc(mapSlipType.get(
								ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
								.toString());
					}
					jackpotDTO
							.setStatusFlagDesc(getStatusDescription(ILookupTableConstants.VOID_STATUS_ID));
					jackpotDTO
							.setStatusFlagId(ILookupTableConstants.VOID_STATUS_ID);
					jackpotDTO
							.setJackpotTypeDesc(getJackpotTypeDescription(jackpotDTO
									.getJackpotTypeId()));

					if (slipReference.getProcessFlagId() != null) {
						jackpotDTO.setProcessFlagId(slipReference
								.getProcessFlagId());
						jackpotDTO.setProcessFlagDesc(slipReference
								.getProcessFlag().getFlagDescription());
					}

				} else {
					slipReference.setStatusFlagId(REPRINT_STATUS_ID);
				}
				slipReference.setUpdatedTs(DateHelper
						.getUTCTimeFromLocal(currentLocalTime));
				slipReference.setUpdatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));

				if (autoVoidReprintJackpot.equalsIgnoreCase("No")
						&& jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase(
								"Yes")) {
					// Setting sequence number as combination number to make it
					// random for SA
					// Generate a new random number and include the sequence
					// number to create a new combination number
					String sequenceNumber = slipReference.getSequenceNumber()
							.toString();
					// Checking if the existing sequence number is not a random
					// number
					if (sequenceNumber.length() > IAppConstants.RANDOM_NUMBER_LENGTH) {
						/*
						 * As the length of sequence number digits is greater
						 * than random digits, we are assuming it to be random
						 * number & and changing the random digits
						 */
						String randomNumber = sequenceNumber.substring(0,
								IAppConstants.RANDOM_NUMBER_LENGTH);
						String seqNumber = sequenceNumber
								.substring(IAppConstants.RANDOM_NUMBER_LENGTH);
						String newRandomNumber = ConversionUtil
								.getRandomSequenceNumber();
						while (randomNumber.equals(newRandomNumber)) {
							newRandomNumber = ConversionUtil
									.getRandomSequenceNumber();
						}
						slipReference.setSequenceNumber(Long
								.parseLong(newRandomNumber + seqNumber));
						jackpotDTO.setSequenceNumber(slipReference
								.getSequenceNumber());
					} else {
						/*
						 * As the length of the sequence number is less than
						 * random digits, it is not a random number, hence we
						 * are adding random number before the sequence number
						 */
						long newSequenceNumber = getNewSequenceNumberForReprint(sequenceNumber);
						slipReference.setSequenceNumber(newSequenceNumber);
						jackpotDTO.setSequenceNumber(slipReference
								.getSequenceNumber());
					}
				}
				sessionObj.save(slipReference);

				/** Create a record in the transaction TABLE */
				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId(slipReference.getId());
				if (autoVoidReprintJackpot.equalsIgnoreCase("Yes")) {
					transaction.setStatusFlagId(VOID_STATUS_ID);
				} else {
					transaction.setStatusFlagId(REPRINT_STATUS_ID);
					// SET PRINTER USED FIELD AS SLIP IS PRINTED FOR REPRINT
					if (jackpotDTO.getPrinterUsed() != null) {
						if (jackpotDTO.getPrinterUsed().length() > 200) {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed().substring(0, 200));
						} else {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed());
						}
					}
				}
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setMessageId(slipReference.getMessageId());
				transaction.setEmployeeId(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				transaction.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				transaction.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				transaction.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(currentLocalTime));
				transaction.setCreatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				sessionObj.save(transaction);

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The postJackpotReprint for the slip sequence "
							+ slipReference.getSequenceNumber()
							+ " has been executed successfully");
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				jackpotDTO.setAccountNumber(slipReference.getAccountNumber());
				jackpotDTO.setCashlessAccountType(slipReference
						.getAccountType());
				jackpotDTO.setPostedSuccessfully(true);
				jackpotDTO.setSlipReferenceId(slipReference.getId());
			} else {
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record to reprint ");
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				jackpotDTO.setPostedSuccessfully(false);
			}

		} catch (HibernateException e) {
			log.error("HibernateException in postUpdateJackpotReprint", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postUpdateJackpotReprint", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to auto void reprinted sequence and create a new slip seq in Slip
	 * Ref table
	 * 
	 * @param sequenceNumber
	 * @return JackpotDTO
	 * @throws HibernateException
	 *             , Exception
	 * @author dambereen
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO postUpdateJpAutoVoidReprint(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		try {
			log.info("Inside postUpdateJpAutoVoidReprint DAO");

			short newStatusFlagId = 0;
			String accountingPost = null;
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("No")) { // Old
																				// flow
				newStatusFlagId = PROCESSED_STATUS_ID;
				accountingPost = "Y";
			}
			if (jackpotDTO.getCashierDeskEnabled().equalsIgnoreCase("Yes")) { // New
																				// Flow
																				// for
																				// cashier
																				// Desk
				newStatusFlagId = PRINTED_STATUS_ID;
				accountingPost = "N";
			}

			Criterion crtSlipRefId = Restrictions.eq("id",
					jackpotDTO.getSlipReferenceId());
			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSlipRefId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {

				// FOR AUTO VOID UPDATE LOGIC
				Timestamp utcTimeFromLocal = DateHelper
						.getUTCTimeFromLocal(new Timestamp(System
								.currentTimeMillis()));
				long newSlipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						slipReference.getAcnfNumber(), jackpotDTO.getSiteId(),
						0);

				long messageId = MessageUtil.getMessageId();

				JackpotSequence newJpSeq = new JackpotSequence();
				sessionObj.save(newJpSeq);

				long newSequenceNo = newJpSeq.getJpseId();

				if (jackpotDTO.getGenerateRandSeqNum().equalsIgnoreCase("Yes")) {
					// Setting sequence number as combination number to make it
					// random for SA
					// Generate a new random number and include the sequence
					// number to create a new combination number
					String sequenceNumber = Long.valueOf(newSequenceNo)
							.toString();
					// Checking if the existing sequence number is not a random
					// number
					if (sequenceNumber.length() > IAppConstants.RANDOM_NUMBER_LENGTH) {

						/*
						 * As the length of sequence number digits is greater
						 * than random digits, we are assuming it to be random
						 * number & and changing the random digits
						 */

						String randomNumber = sequenceNumber.substring(0,
								IAppConstants.RANDOM_NUMBER_LENGTH);
						String seqNumber = sequenceNumber
								.substring(IAppConstants.RANDOM_NUMBER_LENGTH);
						String newRandomNumber = ConversionUtil
								.getRandomSequenceNumber();
						while (randomNumber.equals(newRandomNumber)) {
							newRandomNumber = ConversionUtil
									.getRandomSequenceNumber();
						}
						newSequenceNo = Long.parseLong(newRandomNumber
								+ seqNumber);
					} else {
						/*
						 * As the length of the sequence number is less than
						 * random digits, it is not a random number, hence we
						 * are adding random number before the sequence number
						 */
						newSequenceNo = getNewSequenceNumberForReprint(sequenceNumber);
					}
				}

				// CREATE NEW SLIP REF REC
				SlipReference sliprefNew = new SlipReference();
				sliprefNew.setId(newSlipPrimaryKey);
				sliprefNew.setTransactionId(newSlipPrimaryKey);
				sliprefNew.setMessageId(messageId);
				sliprefNew.setSequenceNumber(newSequenceNo);
				sliprefNew.setPostToAccounting(accountingPost);
				sliprefNew
						.setSlipTypeId(ILookupTableConstants.JACKPOT_SLIP_TYPE_ID);
				sliprefNew.setStatusFlagId(newStatusFlagId);
				sliprefNew.setProcessFlagId(slipReference.getProcessFlagId());
				sliprefNew.setAuditCodeId(slipReference.getAuditCodeId());
				sliprefNew.setAcnfNumber(slipReference.getAcnfNumber());
				sliprefNew.setAcnfLocation(slipReference.getAcnfLocation());
				sliprefNew.setSealNumber(slipReference.getSealNumber());
				sliprefNew.setAreaShortName(slipReference.getAreaShortName());
				sliprefNew.setAreaLongName(slipReference.getAreaLongName());
				sliprefNew.setSiteId(slipReference.getSiteId());
				sliprefNew.setSiteNo(slipReference.getSiteNo());
				sliprefNew.setAccountNumber(slipReference.getAccountNumber());
				sliprefNew.setAccountType(slipReference.getAccountType());
				sliprefNew.setChangeFlag(slipReference.getChangeFlag());
				sliprefNew.setGameComboId(slipReference.getGameComboId());
				sliprefNew.setGmuDenomination(slipReference
						.getGmuDenomination());
				sliprefNew.setSlotDenomination(slipReference
						.getSlotDenomination());
				sliprefNew.setSlotDescription(slipReference
						.getSlotDescription());
				sliprefNew.setTransactionDate(utcTimeFromLocal);
				sliprefNew.setCreatedTs(utcTimeFromLocal);
				sliprefNew.setCreatedUser(slipReference.getCreatedUser());
				sessionObj.save(sliprefNew);

				Set<Jackpot> jackpotSetNew = new TreeSet<Jackpot>();
				// CREATE NEW JACKPOT REC
				Set<Jackpot> jackpotSet = slipReference.getJackpot();
				short jackpotTypeId = 0;
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					Jackpot oldJackpot = (Jackpot) iter.next();
					Jackpot newJackpot = new Jackpot();
					newJackpot.setId(newSlipPrimaryKey);
					newJackpot.setSlipReferenceId(newSlipPrimaryKey);
					jackpotTypeId = oldJackpot.getJackpotTypeId();
					newJackpot.setJackpotTypeId(jackpotTypeId);
					newJackpot.setTaxTypeId(oldJackpot.getTaxTypeId());
					newJackpot.setJackpotId(oldJackpot.getJackpotId());
					newJackpot.setAssociatedPlayerCard(oldJackpot
							.getAssociatedPlayerCard());
					newJackpot.setPlayerCard(oldJackpot.getPlayerCard());
					newJackpot.setPlayerName(oldJackpot.getPlayerName());
					newJackpot.setBlindAttempt(oldJackpot.getBlindAttempt());
					newJackpot
							.setOriginalAmount(oldJackpot.getOriginalAmount());
					newJackpot.setHpjpAmount(oldJackpot.getHpjpAmount());
					newJackpot.setHpjpAmountRounded(oldJackpot
							.getHpjpAmountRounded());
					newJackpot.setMachinePaidAmount(oldJackpot
							.getMachinePaidAmount());
					newJackpot.setTaxAmount(oldJackpot.getTaxAmount());
					newJackpot.setCalculatedSDSAmount(oldJackpot
							.getCalculatedSDSAmount());
					newJackpot.setPmuAmountUsed(oldJackpot.getPmuAmountUsed());
					newJackpot.setPromptedPMUAmount(oldJackpot
							.getPromptedPMUAmount());
					newJackpot.setNetAmount(oldJackpot.getNetAmount());
					newJackpot.setPayline(oldJackpot.getPayline());
					newJackpot.setWinningCombination(oldJackpot
							.getWinningCombination());
					newJackpot.setCoinsPlayed(oldJackpot.getCoinsPlayed());
					newJackpot.setExpiryDate(oldJackpot.getExpiryDate());
					newJackpot.setCreatedTs(utcTimeFromLocal);
					newJackpot.setCreatedUser(oldJackpot.getCreatedUser());
					sessionObj.save(newJackpot);

					jackpotSetNew.add(newJackpot);
					break;
				}

				Set<SlipData> slipDataSetNew = new TreeSet<SlipData>();
				// CREATE NEW JACKPOT REC
				Set<SlipData> slipDataSet = slipReference.getSlipData();
				for (Iterator iter = slipDataSet.iterator(); iter.hasNext();) {
					SlipData oldSlipData = (SlipData) iter.next();
					SlipData newSlipData = new SlipData();
					newSlipData.setId(newSlipPrimaryKey);
					newSlipData.setSlipReferenceId(newSlipPrimaryKey);
					newSlipData.setActrLogin(oldSlipData.getActrLogin());
					newSlipData.setAmountAuthEmpId(oldSlipData
							.getAmountAuthEmpId());
					newSlipData.setAuthEmployeeId1(oldSlipData
							.getAuthEmployeeId1());
					newSlipData.setAuthEmployeeId2(oldSlipData
							.getAuthEmployeeId2());
					newSlipData.setKioskProcessed(oldSlipData
							.getKioskProcessed());
					newSlipData.setShift(oldSlipData.getShift());
					newSlipData.setWindowNumber(oldSlipData.getWindowNumber());
					newSlipData.setCreatedTs(utcTimeFromLocal);
					newSlipData.setCreatedUser(oldSlipData.getCreatedUser());
					sessionObj.save(newSlipData);
					slipDataSetNew.add(newSlipData);
					break;
				}

				Set<Transaction> transactionSetNew = new TreeSet<Transaction>();
				// CREATE NEW TRANSACTION REC FOR PRINT/PROCESS OF NEW SLIP
				Transaction transaction1 = new Transaction();
				transaction1.setId(newSlipPrimaryKey);
				transaction1.setSlipReferenceId(newSlipPrimaryKey);
				transaction1.setStatusFlagId(newStatusFlagId);
				transaction1.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction1.setMessageId(messageId);
				transaction1.setEmployeeId(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				if (jackpotDTO.getPrinterUsed() != null) {
					if (jackpotDTO.getPrinterUsed().length() > 200) {
						transaction1.setPrinterUsed(jackpotDTO.getPrinterUsed()
								.substring(0, 200));
					} else {
						transaction1
								.setPrinterUsed(jackpotDTO.getPrinterUsed());
					}
				}
				transaction1.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				transaction1.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				transaction1.setCreatedTs(utcTimeFromLocal);
				transaction1.setCreatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				sessionObj.save(transaction1);
				transactionSetNew.add(transaction1);

				sliprefNew.setJackpot(jackpotSetNew);
				sliprefNew.setSlipData(slipDataSetNew);
				sliprefNew.setTransaction(transactionSetNew);

				jackpotDTO = JackpotHelper.getJackpotDTO(sliprefNew);
				jackpotDTO.setPostToAccounting(slipReference
						.getPostToAccounting());

				jackpotDTO.setSlipTypeId(JACKPOT_SLIP_TYPE_ID);
				if (mapSlipType != null && mapSlipType.size() == 0) {
					getSlipTypeMapValues();
					jackpotDTO.setSlipTypeDesc(mapSlipType.get(
							ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
							.toString());
				} else {
					jackpotDTO.setSlipTypeDesc(mapSlipType.get(
							ILookupTableConstants.JACKPOT_SLIP_TYPE_ID)
							.toString());
				}
				jackpotDTO
						.setStatusFlagDesc(getStatusDescription(newStatusFlagId));
				jackpotDTO.setStatusFlagId(newStatusFlagId);
				jackpotDTO
						.setJackpotTypeDesc(getJackpotTypeDescription(jackpotTypeId));
				if (sliprefNew.getProcessFlagId() != null) {
					jackpotDTO.setProcessFlagId(sliprefNew.getProcessFlagId());
					jackpotDTO
							.setProcessFlagDesc(getProcessFlagDescription(sliprefNew
									.getProcessFlagId()));
				}
				jackpotDTO.setPostedSuccessfully(true);
				log.debug("********************************************************************");
				if (log.isInfoEnabled()) {
					log.info("The postUpdateJpAutoVoidReprint for the slip sequence "
							+ slipReference.getSequenceNumber()
							+ " has been executed successfully");
				}
				log.debug("********************************************************************");

			} else {
				log.debug("********************************************************************");
				if (log.isInfoEnabled()) {
					log.info("There is no record to reprint ");
				}
				log.debug("********************************************************************");
				jackpotDTO.setPostedSuccessfully(false);
			}

		} catch (HibernateException e) {
			log.error("HibernateException in postUpdateJackpotReprint", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in postUpdateJackpotReprint", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to generate a new random unique sequence number when reprinting a
	 * jackpot slip
	 * 
	 * @param sequenceNumber
	 * @return
	 * @author vsubha
	 */
	private static long getNewSequenceNumberForReprint(String sequenceNumber) {
		Session sessionObj = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		// To check if the generated random number combination already exist in
		// the SLpReference table
		long newSequenceNumber = Long.parseLong(ConversionUtil
				.getRandomSequenceNumber() + sequenceNumber);
		SlipReference slipRefSeq = null;
		do {
			Criterion crtSeqNumber = Restrictions.eq("sequenceNumber",
					newSequenceNumber);
			Criteria slipRefSeqNumCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSeqNumber);
			slipRefSeq = (SlipReference) slipRefSeqNumCriteria.uniqueResult();
			if (slipRefSeq != null) {
				if (log.isDebugEnabled()) {
					log.debug("Sequence Number Generated " + newSequenceNumber
							+ " already exists");
				}
			}
			newSequenceNumber = Long.parseLong(ConversionUtil
					.getRandomSequenceNumber() + sequenceNumber);
		} while (slipRefSeq != null); // If sliprefSeq == null means no results,
										// hence the generated number is unique
		return newSequenceNumber;
	}

	/**
	 * Method to void all the pending jackpot slips
	 * 
	 * @param jackpotDTOList
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotDTO> updateVoidAllPendingJackpotSlips(
			JackpotDTO jackpotDTO) throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside voidAllPendingJackpotSlips DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtStatusId = Restrictions.eq("statusFlagId",
					PENDING_STATUS_ID);
			Criterion crtSlipTypeId = Restrictions.and(crtStatusId,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));

			Order orderBySeq = Order.asc("transactionDate");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			List<SlipReference> pendingSlips = slipRefCriteria.list();
			Timestamp currentLocalTime = new Timestamp(
					System.currentTimeMillis());
			JackpotDTO jackpotDTOHelper = null;
			if (pendingSlips != null && pendingSlips.size() > 0) {
				if (log.isInfoEnabled()) {
					log.info("pendingSlips.size() is " + pendingSlips.size());
				}
				int numberOfIncrements = pendingSlips.size();

				// As no Slot details are available, it is send as null for
				// generating primary key
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(null,
						jackpotDTO.getSiteId(), numberOfIncrements);

				for (int i = 0; i < pendingSlips.size(); i++) {

					SlipReference slipReference = (SlipReference) pendingSlips
							.get(i);

					// String currentGamingDay =
					// DateUtil.getStringDate(System.currentTimeMillis());
					// String slipTransDate =
					// DateUtil.getStringDate(DateUtil.getMilliSeconds(DateUtil.getLocalTimeFromUTC(slipReference.getTransactionDate())));
					// log.info("currentGamingDay: "+currentGamingDay);
					// log.info("slipTransDate: "+slipTransDate);
					// if(!currentGamingDay.equalsIgnoreCase(slipTransDate)){
					slipReference.setTransactionId(slipPrimaryKey);
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setUpdatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					slipReference.setUpdatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(slipReference);

					/** CREATE THE ROW IN SLIP_DATA TABLE * */
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(jackpotDTO
							.getAssetConfNumberUsed());
					slipData.setActrLogin(PadderUtil.lPad(
							jackpotDTO.getActorLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					slipData.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					slipData.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);

					/** Create a record in the transaction TABLE */

					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(VOID_STATUS_ID);
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
					transaction.setEmployeeId(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					if (jackpotDTO.getPrinterUsed() != null) {
						if (jackpotDTO.getPrinterUsed().length() > 200) {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed().substring(0, 200));
						} else {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed());
						}
					}
					transaction.setEmployeeFirstName(jackpotDTO
							.getEmployeeFirstName());
					transaction.setEmployeeLastName(jackpotDTO
							.getEmployeeLastName());
					transaction.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					transaction.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(transaction);

					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The slip status has been changed to Void for the sequence: "
								+ pendingSlips.get(i).getSequenceNumber());
						// + "-" +
						// pendingSlips.get(i).getRandomSequenceNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					jackpotDTOHelper = JackpotHelper.getJackpotDTO(pendingSlips
							.get(i));
					if (log.isInfoEnabled()) {
						log.info("Getting the values from the Jackpot Helper");
					}
					jackpotDTOList.add(jackpotDTOHelper);

					// } else if (log.isDebugEnabled()) {
					// log.debug("Void is not done for the sequence: " +
					// pendingSlips.get(i).getSequenceNumber()+" gaming day is the same");
					// }

					// Increment the primary Key value
					slipPrimaryKey++;
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of pending records as been returned , Record count: "
							+ pendingSlips.size());
				}
				if (log.isDebugEnabled()) {
					log.debug("**************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no pending records to void");
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}
		} catch (HibernateException e) {
			log.error(
					"HibernateException in voidListPendingJackpotSlipForSlotNo",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in voidListPendingJackpotSlipForSlotNo", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}

	// NON DAO METHODS

	/**
	 * @param jackpotSiteDTO
	 *            the jackpotSiteDTO to set
	 */
	/*
	 * public void setJackpotSiteDTO(JackpotSiteDTO jackpotSiteDTO) {
	 * this.jackpotSiteDTO = jackpotSiteDTO; }
	 *//**
	 * @return the jackpotSiteDTO
	 */
	/*
	 * public JackpotSiteDTO getJackpotSiteDTO() { return jackpotSiteDTO; }
	 */

	/**
	 * Method to void a list of pending jackpot slips for the slot number
	 * entered
	 * 
	 * @param slotNo
	 * @param siteId
	 * @param voidEmpId
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	public static List<JackpotDTO> updateVoidPendingJackpotSlipsForSlotNo(
			JackpotDTO jackpotDTO) throws JackpotDAOException {
		List<JackpotDTO> jackptoDTOList = null;
		try {
			jackptoDTOList = new ArrayList<JackpotDTO>();
			if (log.isInfoEnabled()) {
				log.info("Inside voidPendingJackpotSlipsForSlotNo DAO");
			}

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSlotNumber = Restrictions.eq("acnfNumber",
					jackpotDTO.getAssetConfigNumber());
			Criterion crtStatusFlag = Restrictions.and(crtSlotNumber,
					Restrictions.eq("statusFlagId", PENDING_STATUS_ID));
			Criterion crtSlipTypeid = Restrictions.and(crtStatusFlag,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));

			Order orderBySeq = Order.asc("transactionDate");

			Criteria SlipReferenceCrita = sessionObj
					.createCriteria(SlipReference.class).add(crtSiteId)
					.addOrder(orderBySeq);

			List<SlipReference> slipReferenceList = SlipReferenceCrita.list();

			Timestamp currentLocalTime = new Timestamp(
					System.currentTimeMillis());
			JackpotDTO jackpotDTOHelper = null;
			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				if (log.isInfoEnabled()) {
					log.info("slipReferenceList : " + slipReferenceList.size());
				}
				int numberOfIncrements = slipReferenceList.size();

				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						jackpotDTO.getAssetConfigNumber(),
						jackpotDTO.getSiteId(), numberOfIncrements);

				for (int i = 0; i < slipReferenceList.size(); i++) {
					SlipReference slipReference = (SlipReference) slipReferenceList
							.get(i);
					// String currentGamingDay =
					// DateUtil.getStringDate(System.currentTimeMillis());
					// String slipTransDate =
					// DateUtil.getStringDate(DateUtil.getMilliSeconds(DateUtil.getLocalTimeFromUTC(slipReference.getTransactionDate())));
					// log.info("currentGamingDay: "+currentGamingDay);
					// log.info("slipTransDate: "+slipTransDate);
					// if(!currentGamingDay.equalsIgnoreCase(slipTransDate)){
					slipReference.setTransactionId(slipPrimaryKey);
					slipReference.setStatusFlagId(VOID_STATUS_ID);
					slipReference.setUpdatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					slipReference.setUpdatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(slipReference);

					/** CREATE THE ROW IN SLIP_DATA TABLE * */
					SlipData slipData = new SlipData();
					slipData.setId(slipPrimaryKey);
					slipData.setSlipReferenceId(slipReference.getId());
					slipData.setKioskProcessed(jackpotDTO
							.getAssetConfNumberUsed());
					slipData.setActrLogin(PadderUtil.lPad(
							jackpotDTO.getActorLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					slipData.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					slipData.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					sessionObj.save(slipData);

					/** Create a record in the transaction TABLE */

					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setStatusFlagId(VOID_STATUS_ID);
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setMessageId(slipReference.getMessageId());
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_PRINTED);
					transaction.setEmployeeId(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					if (jackpotDTO.getPrinterUsed() != null) {
						if (jackpotDTO.getPrinterUsed().length() > 200) {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed().substring(0, 200));
						} else {
							transaction.setPrinterUsed(jackpotDTO
									.getPrinterUsed());
						}

					}
					transaction.setEmployeeFirstName(jackpotDTO
							.getEmployeeFirstName());
					transaction.setEmployeeLastName(jackpotDTO
							.getEmployeeLastName());
					transaction.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentLocalTime));
					transaction.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getVoidEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					sessionObj.save(transaction);

					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					if (log.isInfoEnabled()) {
						log.info("The slip status has been changed to Void for the sequence: "
								+ slipReferenceList.get(i).getSequenceNumber()
								// + "-" +
								// slipReferenceList.get(i).getRandomSequenceNumber()
								+ "For the slot no : "
								+ jackpotDTO.getAssetConfigNumber());
					}
					if (log.isDebugEnabled()) {
						log.debug("********************************************************************");
					}
					jackpotDTOHelper = JackpotHelper
							.getJackpotDTO((SlipReference) slipReferenceList
									.get(i));
					if (log.isInfoEnabled()) {
						log.info("Getting the values from the Jackpot Helper");
					}
					jackptoDTOList.add(jackpotDTOHelper);
					// }else{
					// log.debug("Void is not done for the sequence: " +
					// slipReferenceList.get(i).getSequenceNumber()+" gaming day is the same");
					// }

					// Increment the primary Key value
					slipPrimaryKey++;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no pending records to void for the slot no : "
							+ jackpotDTO.getAssetConfigNumber());
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				return jackptoDTOList;
			}

		} catch (HibernateException e) {
			log.error("HibernateException in voidAllPendingJackpotSlips", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in voidAllPendingJackpotSlips", e2);
			throw new JackpotDAOException(e2);
		}
		return jackptoDTOList;
	}

	/**
	 * Method to insert the pouch pay attendant who did the Pouch Pay
	 * 
	 * @param slipReference
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public static boolean insertPouchPayAttendant(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside jackpotCardedEmployeeInfo");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			if (jackpotDTO != null) {

				/** Create a record in TRANSACTION table */
				Transaction transaction = new Transaction();
				transaction.setId(JackpotUtil.getSlipPrimaryKey(
						jackpotDTO.getAssetConfigNumber(),
						jackpotDTO.getSiteId(), 0));
				transaction.setSlipReferenceId(jackpotDTO.getSlipReferenceId());
				transaction.setStatusFlagId(POUCH_PAY_ATTN_STATUS_ID);
				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				transaction.setMessageId(jackpotDTO.getMessageId());
				transaction.setEmployeeId(PadderUtil.lPad(
						jackpotDTO.getSlotAttendantId(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				transaction.setEmployeeFirstName(jackpotDTO
						.getSlotAttendantFirstName());
				transaction.setEmployeeLastName(jackpotDTO
						.getSlotAttendantLastName());
				Timestamp createDate = new Timestamp(Calendar.getInstance()
						.getTimeInMillis());
				transaction.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(createDate));
				transaction.setCreatedUser(PadderUtil.lPad(
						jackpotDTO.getPrintEmployeeLogin(),
						IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				sessionObj.save(transaction);
				if (log.isDebugEnabled()) {
					log.debug("******************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Pouch Pay attendant id updated successfully for the sequence no : "
							+ jackpotDTO.getSequenceNumber());
				}
				// + "-" + jackpotDTO.getRandomSequenceNumber());
				if (log.isDebugEnabled()) {
					log.debug("******************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("*********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("*******the jackpot slip ref id is NULL");
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
	 * Method to return the slip reference id for a particular slip seq no and
	 * site no
	 * 
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotDAOException
	 */
	public static long getSlipReferenceIdForSlipSeq(int siteNo, long sequenceNo)
			throws JackpotDAOException {
		long rtnSlipRefId = 0;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getSlipReferenceIdForSlipSeq");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber", sequenceNo);
			Criterion crtSlpTypId = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlpTypId,
					Restrictions.eq("siteId", siteNo));

			Criteria slipRefCrt = sessionObj
					.createCriteria(SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) slipRefCrt
					.uniqueResult();

			if (slipReference != null) {

				rtnSlipRefId = slipReference.getId();

				if (log.isDebugEnabled()) {
					log.debug("******************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("SlipRef Id returned successfully for the slip seq no: "
							+ sequenceNo);
				}
				if (log.isDebugEnabled()) {
					log.debug("******************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("*********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("no slipref id for the slip seq no: " + sequenceNo);
				}
				if (log.isDebugEnabled()) {
					log.debug("*********************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("Hibernate Exception in getSlipReferenceIdForSlipSeq", e);
			log.warn("Hibernate Exception in getSlipReferenceIdForSlipSeq", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getSlipReferenceIdForSlipSeq", e2);
			log.warn("Exception in getSlipReferenceIdForSlipSeq", e2);
			throw new JackpotDAOException(e2);
		}
		return rtnSlipRefId;
	}

	/**
	 * Method to get the slip reference ID for the given sequence number and
	 * Site ID
	 * 
	 * @param sequenceNumber
	 * @return statusDesc
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot amount retrieval failed
	 * @author vsubha
	 */
	@SuppressWarnings({ "unchecked" })
	public static JPCashlessProcessInfoDTO getJackpotStatusAmount(
			long sequenceNumber, int siteId) throws JackpotDAOException {

		JPCashlessProcessInfoDTO jPCashlessProcessInfoDTO = new JPCashlessProcessInfoDTO();

		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotStatusAmount DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteId);

			SlipReference slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			jPCashlessProcessInfoDTO.setSequenceNumber(sequenceNumber);
			jPCashlessProcessInfoDTO.setSiteId(siteId);

			if (slipReference != null) {
				jPCashlessProcessInfoDTO.setSequenceNumber(sequenceNumber);
				jPCashlessProcessInfoDTO.setSiteId(siteId);
				jPCashlessProcessInfoDTO.setAssetConfigNumber(slipReference
						.getAcnfNumber());
				jPCashlessProcessInfoDTO.setStatusFlagId(slipReference
						.getStatusFlagId());
				jPCashlessProcessInfoDTO.setProcessFlagId(slipReference
						.getProcessFlagId());
				jPCashlessProcessInfoDTO.setSiteNo(slipReference.getSiteNo());
				jPCashlessProcessInfoDTO.setAccountNumber(slipReference
						.getAccountNumber());
				jPCashlessProcessInfoDTO.setAccountType(slipReference
						.getAccountType());
				Set<Jackpot> jackpotSet = slipReference.getJackpot();
				for (Iterator iter = jackpotSet.iterator(); iter.hasNext();) {
					Jackpot jackpot = (Jackpot) iter.next();
					jPCashlessProcessInfoDTO.setHpjpAmount(jackpot
							.getHpjpAmount() != null ? jackpot.getHpjpAmount()
							: 0l);
					jPCashlessProcessInfoDTO.setJackpotNetAmount(jackpot
							.getNetAmount() != null ? jackpot.getNetAmount()
							: 0l);
					jPCashlessProcessInfoDTO.setJackpotTypeId(jackpot
							.getJackpotTypeId() != null ? jackpot
							.getJackpotTypeId() : 0);
				}
				jPCashlessProcessInfoDTO
						.setErrCode(IAppConstants.JP_SLIP_STATUS_RETURN_SUCCESS_CODE);
				jPCashlessProcessInfoDTO
						.setErrMessage(IAppConstants.JP_SLIP_STATUS_RETURN_SUCCESS);
				jPCashlessProcessInfoDTO.setProcessSuccessful(true);
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("Slip reference ID... has been returned ->"
							+ slipReference.getId());
				}
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no record found for this sequence number : "
							+ sequenceNumber + " and site number : " + siteId);
				}
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************");
				}
				jPCashlessProcessInfoDTO
						.setErrCode(IAppConstants.JP_SLIP_NOT_FOUND_CODE);
				jPCashlessProcessInfoDTO
						.setErrMessage(IAppConstants.JP_SLIP_NOT_FOUND);
			}
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotStatusAmount", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotStatusAmount", e2);
			throw new JackpotDAOException(e2);
		}
		return jPCashlessProcessInfoDTO;
	}

	/**
	 * Method to retrieve Slip By Sequence number and site ID
	 * 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public static JackpotDTO retrieveSlipBySequenceNumber(long sequenceNumber,
			int siteId) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside retrieveSlipBySequenceNumber DAO");
		}
		SlipReference slipReference = null;
		JackpotDTO jackpotDTO = null;
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criterion crtSeqNo = Restrictions.eq("sequenceNumber",
					sequenceNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteNo = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));

			Criteria SlipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtSiteNo);

			slipReference = (SlipReference) SlipReferenceCriteria
					.uniqueResult();

			if (slipReference != null) {

				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}
				log.info("Slip reference ID... has been returned ->"
						+ slipReference.getId());
				jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);
				if (log.isDebugEnabled()) {
					log.debug("********************************************************************");
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There is no slip reference record found for this sequence number : "
							+ sequenceNumber + " and site ID : " + siteId);
				}

				if (log.isDebugEnabled()) {
					log.debug("*************************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in retrieveSlipBySequenceNumber", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in retrieveSlipBySequenceNumber", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTO;
	}

	/**
	 * Method to change status of the jackpot details to PROCESSED after
	 * printing the slip
	 * 
	 * @param jackpotDTO
	 * @return True/False
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot details printing failed
	 * @author vsubha
	 */
	@SuppressWarnings("unchecked")
	public static JackpotDTO updatePrintJackpot(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		JackpotDTO returnJackpotDTO = new JackpotDTO();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside updatePrintJackpot DAO method");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			if (log.isInfoEnabled()) {
				log.info("Seq no: " + jackpotDTO.getSequenceNumber());
			}

			Criterion crtnSeqNo = Restrictions.eq("sequenceNumber",
					jackpotDTO.getSequenceNumber());
			Criterion crtSlipTypeId = Restrictions.and(crtnSeqNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtnSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", jackpotDTO.getSiteId()));
			// STATUS FLAG LIST
			ArrayList statusFlagIdList = new ArrayList();
			statusFlagIdList.add(PRINTED_STATUS_ID);
			statusFlagIdList.add(REPRINT_STATUS_ID);
			statusFlagIdList.add(CHANGE_STATUS_ID);

			Criterion crtStatusFlagId = Restrictions.and(crtnSiteId,
					Restrictions.in("statusFlagId", statusFlagIdList));

			/**
			 * OBTAIN THE SLIP REFERENCE OBJECT FROM THE SLIP_REFERENCE TABLE
			 * USING THE SEQ NUMBER AND SLIP TYPE ID
			 */
			Criteria slipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtStatusFlagId);

			/** USE UNIQUERESULT FOR SINGLE VALUE * */
			SlipReference slipReference = (SlipReference) slipReferenceCriteria
					.uniqueResult();

			/** Get the system current date/time * */
			Timestamp currentDate = new Timestamp(System.currentTimeMillis());
			if (log.isInfoEnabled()) {
				log.info("Current date " + currentDate);
			}

			if (slipReference != null) {
				if (log.isInfoEnabled()) {
					log.info("Updating the SLip Ref table");
				}

				/** UPDATE THE ROW IN SLIP_REFERENCE TABLE * */
				if (log.isInfoEnabled()) {
					log.info("Setting PROCESSED_STATUS_ID in SlipReference");
				}
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(
						jackpotDTO.getAssetConfigNumber(),
						jackpotDTO.getSiteId(), 0);
				slipReference.setTransactionId(slipPrimaryKey);
				slipReference.setStatusFlagId(PROCESSED_STATUS_ID);
				slipReference.setPostToAccounting(POSTED_TO_ACCOUNTING);

				// slipReference.setSiteNo(jackpotDTO.getSiteNo());
				slipReference.setUpdatedTs(DateHelper
						.getUTCTimeFromLocal(currentDate));
				if (jackpotDTO.getPrintEmployeeLogin() != null) {
					slipReference.setUpdatedUser(PadderUtil.lPad(
							jackpotDTO.getPrintEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				sessionObj.save(slipReference);

				// short jackpotTypeId = ((Jackpot)
				// slipReference.getJackpot().iterator().next()).getJackpotTypeId();

				/** CREATE A ROW IN THE TRANSACTION TABLE */
				Transaction transaction = new Transaction();
				transaction.setId(slipPrimaryKey);
				transaction.setSlipReferenceId(slipReference.getId());
				transaction.setMessageId(slipReference.getMessageId());

				transaction.setStatusFlagId(slipReference.getStatusFlagId());

				transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
				if (jackpotDTO.getPrintEmployeeLogin() != null) {
					transaction.setEmployeeId(PadderUtil.lPad(
							jackpotDTO.getPrintEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				transaction.setEmployeeFirstName(jackpotDTO
						.getEmployeeFirstName());
				transaction.setEmployeeLastName(jackpotDTO
						.getEmployeeLastName());
				if (jackpotDTO.getPrinterUsed() != null) {
					if (jackpotDTO.getPrinterUsed().length() > 200) {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed()
								.substring(0, 200));
					} else {
						transaction.setPrinterUsed(jackpotDTO.getPrinterUsed());
					}
				}
				transaction.setCreatedTs(DateHelper
						.getUTCTimeFromLocal(currentDate));
				if (jackpotDTO.getPrintEmployeeLogin() != null) {
					transaction.setCreatedUser(PadderUtil.lPad(
							jackpotDTO.getPrintEmployeeLogin(),
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				// SETTING EXCEPTION CODE FOR JP POSTED
				transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
				// SETTING CASH DESK LOCATION FROM WHERE JACKPOT IS PROCESSED
				transaction.setCashDeskLocation(jackpotDTO
						.getCashDeskLocation());

				// slipReference.getTransaction().add(transaction);
				// sessionObj.save(slipReference);
				sessionObj.save(transaction);

				returnJackpotDTO = JackpotHelper.getJackpotDTO(slipReference);
				returnJackpotDTO.setPostedSuccessfully(true);
				// returnJackpotDTO.setPrintDate(returnJackpotDTO.getSlipRefUpdatedTs());

				returnJackpotDTO.setPrintEmployeeLogin(jackpotDTO
						.getPrintEmployeeLogin());

				returnJackpotDTO
						.setStatusFlagDesc(getStatusDescription(slipReference
								.getStatusFlagId()));
				returnJackpotDTO
						.setProcessFlagId(jackpotDTO.getProcessFlagId());
				returnJackpotDTO
						.setProcessFlagDesc(getProcessFlagDescription(jackpotDTO
								.getProcessFlagId()));

			} else {
				if (log.isDebugEnabled()) {
					log.debug("************************************************************");
					log.debug("Record is not updated");
					log.debug("************************************************************");
				}
			}
		} catch (HibernateException e) {
			log.error("HibernateException in ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in updatePrintJackpot", e2);
			throw new JackpotDAOException(e2);
		}
		log.info("END OF updatePrintJackpot DAO method");
		return returnJackpotDTO;
	}

	/**
	 * Method to process all the jackpots before closing the cashless account
	 * from cage.
	 * 
	 * @param accountNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @param validateEmpSession
	 * @return
	 * @throws JackpotDAOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<JackpotDTO> updatePrintJackpots(String accountNumber,
			Integer siteId, String employeeId, String employeeFirstName,
			String employeeLastName, String cashDeskLocation,
			boolean validateEmpSession) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside updatePrintJackpots DAO method");
		}
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			Criterion crtnAccNo = Restrictions.eq("accountNumber",
					accountNumber);
			Criterion crtSlipTypeId = Restrictions.and(crtnAccNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtnSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", siteId));
			// STATUS FLAG LIST
			ArrayList statusFlagIdList = new ArrayList();
			statusFlagIdList.add(PRINTED_STATUS_ID);
			statusFlagIdList.add(REPRINT_STATUS_ID);
			statusFlagIdList.add(CHANGE_STATUS_ID);

			Criterion crtStatusFlagId = Restrictions.and(crtnSiteId,
					Restrictions.in("statusFlagId", statusFlagIdList));
			Criterion crtPostToAcc = Restrictions.and(crtStatusFlagId,
					Restrictions.eq("postToAccounting",
							NOT_POSTED_TO_ACCOUNTING));

			Criteria slipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtPostToAcc);

			List<SlipReference> slipReferenceList = slipReferenceCriteria
					.list();

			List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();

			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				/** Get the system current date/time * */
				Timestamp currentDate = new Timestamp(
						System.currentTimeMillis());
				if (log.isInfoEnabled()) {
					log.info("Current date " + currentDate);
					log.info("Account Number : " + accountNumber);
					log.info("Processed Location : " + cashDeskLocation);
				}
				int numberOfIncrements = slipReferenceList.size();

				// TODO check what needs to be done for the Slot Number for the
				// below
				long slipPrimaryKey = JackpotUtil.getSlipPrimaryKey(null,
						siteId, numberOfIncrements);

				for (SlipReference slipReference : slipReferenceList) {
					slipReference.setTransactionId(slipPrimaryKey);
					slipReference.setStatusFlagId(PROCESSED_STATUS_ID);
					slipReference.setPostToAccounting(POSTED_TO_ACCOUNTING);
					slipReference.setUpdatedTs(DateHelper
							.getUTCTimeFromLocal(currentDate));
					slipReference.setUpdatedUser(PadderUtil.lPad(employeeId,
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));

					/** CREATE A ROW IN THE TRANSACTION TABLE */
					Transaction transaction = new Transaction();
					transaction.setId(slipPrimaryKey);
					transaction.setSlipReferenceId(slipReference.getId());
					transaction.setMessageId(slipReference.getMessageId());
					transaction
							.setStatusFlagId(slipReference.getStatusFlagId());
					transaction.setPostFlagId(NOT_POSTED_FLAG_ID);
					transaction.setEmployeeId(PadderUtil.lPad(employeeId,
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					transaction.setEmployeeFirstName(employeeFirstName);
					transaction.setEmployeeLastName(employeeLastName);
					transaction.setCreatedTs(DateHelper
							.getUTCTimeFromLocal(currentDate));
					transaction.setCreatedUser(PadderUtil.lPad(employeeId,
							IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
					// SETTING EXCEPTION CODE FOR JP POSTED
					transaction.setExceptionCodeId(EXCEPTION_JP_SLIP_POSTED);
					// SETTING CASH DESK LOCATION FROM WHERE JACKPOT IS
					// PROCESSED
					transaction.setCashDeskLocation(cashDeskLocation);
					// sessionObj.save(transaction);

					if (log.isInfoEnabled()) {
						log.info("Slip Sequence Number -->"
								+ slipReference.getSequenceNumber()
								+ "--> processed");
					}

					slipReference.getTransaction().add(transaction);
					sessionObj.save(slipReference);
					Set transactionSet = slipReference.getTransaction();
					for (Iterator iter = transactionSet.iterator(); iter
							.hasNext();) {
						Transaction transaction1 = (Transaction) iter.next();
						if (log.isInfoEnabled()) {
							log.info(transaction1.toString());
						}
					}

					JackpotDTO jackpotDTO = JackpotHelper
							.getJackpotDTO(slipReference);
					// Setting Print Date to the Slip reference updated TS
					jackpotDTO.setPrintDate(jackpotDTO.getSlipRefUpdatedTs());
					jackpotDTO.setPostedSuccessfully(true);
					log.info(jackpotDTO.toString());

					jackpotDTOList.add(jackpotDTO);

					// Increment the primary Key value
					slipPrimaryKey++;

				}
			} else if (log.isInfoEnabled()) {
				log.info("Account Number : " + accountNumber);
				log.info("No printed jackpot slips available for processing.");
				log.info("Check if any PENDING jackpots are available.");
			}
			return jackpotDTOList;
		} catch (HibernateException e) {
			log.error("HibernateException in ", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in updatePrintJackpot", e2);
			throw new JackpotDAOException(e2);
		} finally {
			if (log.isInfoEnabled()) {
				log.info("END OF updatePrintJackpots DAO method");
			}
		}
	}

	/**
	 * Method to get the status description for the status flag Id from the
	 * mapStatusFlag
	 * 
	 * @param statusFlagId
	 * @return
	 * @author vsubha
	 */
	private static String getStatusDescription(Short statusFlagId) {
		if (mapStatusFlag != null && mapStatusFlag.size() == 0) {
			getStatusFlagMapValues();
			return mapStatusFlag.get(statusFlagId).toString();
		} else {
			return mapStatusFlag.get(statusFlagId).toString();
		}
	}

	/**
	 * Method to get the process description for the process flag Id from the
	 * mapProcessFlag
	 * 
	 * @param processFlagId
	 * @return
	 * @author vsubha
	 */
	private static String getProcessFlagDescription(Short processFlagId) {
		if (processFlagId != 0 && mapProcessFlag != null
				&& mapProcessFlag.size() == 0) {
			getProcessFlagMapValues();
			return mapProcessFlag.get(processFlagId).toString();
		} else {
			return mapProcessFlag.get(processFlagId).toString();
		}
	}

	/**
	 * Method to get the jackpot type description for the jackpot type Id from
	 * the mapJackpotType
	 * 
	 * @param jackpotTypeId
	 * @return
	 * @author vsubha
	 */
	private static String getJackpotTypeDescription(Short jackpotTypeId) {
		if (mapJackpotType != null && mapJackpotType.size() == 0) {
			getJackpotTypeMapValues();
			return mapJackpotType.get(jackpotTypeId).toString();
		} else {
			return mapJackpotType.get(jackpotTypeId).toString();
		}
	}

	/**
	 * Method to show if all jackpots are processed or not
	 * 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isAllJackpotProcessed(String accountNumber, int siteId)
			throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside isAlljackpotProcessed DAO method");
		}
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.openSession();
			Criterion crtAccNo = Restrictions
					.eq("accountNumber", accountNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtAccNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteNo = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));
			Criterion crtAccPost = Restrictions.and(crtSiteNo, Restrictions.eq(
					"postToAccounting", NOT_POSTED_TO_ACCOUNTING));

			// STATUS FLAG LIST
			ArrayList statusFlagIdList = new ArrayList();
			statusFlagIdList.add(VOID_STATUS_ID);
			statusFlagIdList.add(MECHANICS_DELTA_STATUS_ID);
			statusFlagIdList.add(INVALID_STATUS_ID);
			statusFlagIdList.add(CREDIT_KEY_OFF_STATUS_ID);

			Criterion crtStatNotToChk = Restrictions.and(crtAccPost,
					Restrictions.not(Restrictions.in("statusFlagId",
							statusFlagIdList)));

			Criteria slipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtStatNotToChk);

			List<SlipReference> slipReferenceList = slipReferenceCriteria
					.list();

			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				// Still some jackpots available to be processed
				return false;
			} else {
				// No Jackpots available for processing
				return true;
			}
		} catch (HibernateException e) {
			log.error("HibernateException in isAllJackpotProcessed", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in isAllJackpotProcessed", e2);
			throw new JackpotDAOException(e2);
		}
	}

	/**
	 * Method to show if all jackpots are processed or not
	 * 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<JPCashlessProcessInfoDTO> getAllUnProcessedJackpot(
			String accountNumber, int siteId) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside isAlljackpotProcessed DAO method");
		}
		try {
			Session sessionObj = HibernateUtil.getSessionFactory()
					.openSession();
			Criterion crtAccNo = Restrictions
					.eq("accountNumber", accountNumber);
			Criterion crtSlipTypeid = Restrictions.and(crtAccNo,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteNo = Restrictions.and(crtSlipTypeid,
					Restrictions.eq("siteId", siteId));
			Criterion crtAccPost = Restrictions.and(crtSiteNo, Restrictions.eq(
					"postToAccounting", NOT_POSTED_TO_ACCOUNTING));

			// STATUS FLAG LIST
			ArrayList statusFlagIdList = new ArrayList();
			statusFlagIdList.add(VOID_STATUS_ID);
			statusFlagIdList.add(MECHANICS_DELTA_STATUS_ID);
			statusFlagIdList.add(INVALID_STATUS_ID);
			statusFlagIdList.add(CREDIT_KEY_OFF_STATUS_ID);

			Criterion crtStatNotToChk = Restrictions.and(crtAccPost,
					Restrictions.not(Restrictions.in("statusFlagId",
							statusFlagIdList)));

			Criteria slipReferenceCriteria = sessionObj.createCriteria(
					SlipReference.class).add(crtStatNotToChk);

			List<SlipReference> slipReferenceList = slipReferenceCriteria
					.list();
			List<JPCashlessProcessInfoDTO> jackpotProcessInfoDTOLst = new ArrayList<JPCashlessProcessInfoDTO>();
			if (slipReferenceList != null && slipReferenceList.size() > 0) {
				// Still some jackpots available to be processed
				for (SlipReference slipRef : slipReferenceList) {
					jackpotProcessInfoDTOLst.add(JackpotHelper
							.getSlipDetailsDTO(slipRef));
				}
				return jackpotProcessInfoDTOLst;
			} else if (log.isInfoEnabled()) {
				// No Jackpots available for processing
				log.info("All jackpots are processed");
			}
			return jackpotProcessInfoDTOLst;
		} catch (HibernateException e) {
			log.error("HibernateException in isAllJackpotProcessed", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in isAllJackpotProcessed", e2);
			throw new JackpotDAOException(e2);
		}

	}

	/**
	 * Method to get Jackpot Liability details for DEG
	 * 
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	@SuppressWarnings("unchecked")
	public static JackpotLiabilityDetailsDTO getJackpotLiabilityDetailsForDEG(
			int siteId, String fromDate, String toDate)
			throws JackpotDAOException {
		JackpotLiabilityDetailsDTO jpLiabilityDetDTO = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotLiabilityDetailsForDEG DAO");
				log.info("from date : " + fromDate);
				log.info("DateEngineUtil.convertStringToDate(fromDate).getTime() : "
						+ DateEngineUtil.convertStringToDate(fromDate)
								.getTime());
				log.info("DateHelper.getUTCTimeFromLocal(DateEngineUtil.convertStringToDate(fromDate).getTime())-->"
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(fromDate).getTime()));
				log.info("To date : " + toDate);
				log.info("DateEngineUtil.convertStringToDate(toDate).getTime() : "
						+ DateEngineUtil.convertStringToDate(toDate).getTime());
				log.info("DateHelper.getUTCTimeFromLocal(DateEngineUtil.convertStringToDate(toDate).getTime())-->"
						+ DateHelper.getUTCTimeFromLocal(DateEngineUtil
								.convertStringToDate(toDate).getTime()));
			}

			boolean includePendStat = Boolean
					.valueOf(AppPropertiesReader
							.getInstance()
							.getValue(
									PropertyKeyConstant.PROPS_JP_INCLUDE_PENDING_STAT_FOR_DEG));
			boolean includePrintStat = Boolean
					.valueOf(AppPropertiesReader
							.getInstance()
							.getValue(
									PropertyKeyConstant.PROPS_JP_INCLUDE_PRINT_STAT_FOR_DEG));

			log.info(PropertyKeyConstant.PROPS_JP_INCLUDE_PENDING_STAT_FOR_DEG
					+ " : " + includePendStat);
			log.info(PropertyKeyConstant.PROPS_JP_INCLUDE_PRINT_STAT_FOR_DEG
					+ " : " + includePrintStat);

			ArrayList<Short> transStatusFlagIdList = new ArrayList<Short>();
			transStatusFlagIdList.add(PENDING_STATUS_ID);
			transStatusFlagIdList.add(VOID_STATUS_ID);
			transStatusFlagIdList.add(PRINTED_STATUS_ID);
			transStatusFlagIdList.add(CHANGE_STATUS_ID);
			transStatusFlagIdList.add(PROCESSED_STATUS_ID);
			// String PROPS_JP_INCLUDE_NOT_POSTED_TO_ACC_FOR_DEG =
			// "jp.include.not.post.to.acc.for.deg";

			Projection prjSeqNo = Projections.property("sequenceNumber");
			Projection prjStatusId = Projections.property("statusFlagId");
			Projection prjTrStatusId = Projections
					.property("trans.statusFlagId");
			Projection prjNetAmt = Projections.property("jp.netAmount");
			Projection prjOriginalAmt = Projections
					.property("jp.originalAmount");
			Projection prjExpiryTS = Projections.property("jp.expiryDate");
			Projection prjPostToAcc = Projections.property("postToAccounting");

			ProjectionList prjList = Projections.projectionList();
			prjList.add(prjSeqNo);
			prjList.add(prjStatusId);
			prjList.add(prjTrStatusId);
			prjList.add(prjOriginalAmt);
			prjList.add(prjNetAmt);
			prjList.add(prjExpiryTS);
			prjList.add(prjPostToAcc);

			Criterion crtTransStId = Restrictions.in("trans.statusFlagId",
					transStatusFlagIdList);
			Criterion crtSlipCreateTs = Restrictions.between("createdTs",
					DateHelper.getUTCTimeFromLocal(DateEngineUtil
							.convertStringToDate(fromDate).getTime()),
					DateHelper.getUTCTimeFromLocal(DateEngineUtil
							.convertStringToDate(toDate).getTime()));
			Criterion crtSlipTypeId = Restrictions.and(crtSlipCreateTs,
					Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
			Criterion crtSiteId = Restrictions.and(crtSlipTypeId,
					Restrictions.eq("siteId", siteId));

			Session sessionObj = HibernateUtil.getSessionFactory()
					.openSession();

			Criteria liabilityDetailsCriteria = sessionObj
					.createCriteria(SlipReference.class)
					.add(crtSiteId)
					.createAlias("jackpot", "jp", Criteria.LEFT_JOIN)
					.createAlias("transaction", "trans", Criteria.LEFT_JOIN,
							crtTransStId).setProjection(prjList)
					.addOrder(Order.asc("sequenceNumber"))
					.addOrder(Order.desc("createdTs"));

			long newSeqNumber = 0, oldSeqNumber = 0;
			long slipsAddedCount = 0, slipsRedeemedCount = 0, slipsExpiredCount = 0, slipsVoidedCount = 0;
			long slipsAddedAmtSum = 0, slipsRedeemedAmtSum = 0, slipsExpiredAmtSum = 0, slipsVoidedAmtSum = 0;
			boolean isAddCountAdded = false, isVoidedCountAdded = false, isExpiredCountAdded = false;
			List<Object[]> liabilityDetailsList = liabilityDetailsCriteria
					.list();
			if (liabilityDetailsList != null && liabilityDetailsList.size() > 0) {
				log.info("##################################################################################################");
				for (Object[] liabilityDetail : liabilityDetailsList) {
					long sequenceNumber = liabilityDetail[0] != null ? ((Long) liabilityDetail[0])
							.longValue() : 0;
					short statusId = liabilityDetail[1] != null ? ((Short) liabilityDetail[1])
							.shortValue() : 0;
					short transStatusId = liabilityDetail[2] != null ? ((Short) liabilityDetail[2])
							.shortValue() : 0;
					long originalAmount = liabilityDetail[3] != null ? ((Long) liabilityDetail[3])
							.longValue() : 0;
					long netAmount = liabilityDetail[4] != null ? ((Long) liabilityDetail[4])
							.longValue() : 0;
					Timestamp expiryTS = liabilityDetail[5] != null ? (Timestamp) liabilityDetail[5]
							: null;
					String postedToAcc = liabilityDetail[6] != null ? (String) liabilityDetail[6]
							: IAppConstants.EMPTY_STRING;

					newSeqNumber = sequenceNumber;
					if (newSeqNumber != oldSeqNumber) {
						log.info("##################################################################################################");
						log.info("Sequence Number Changed : " + newSeqNumber);
						oldSeqNumber = newSeqNumber;
						isAddCountAdded = isVoidedCountAdded = isExpiredCountAdded = false;
						log.info("Count Flags are set to false");
					}
					log.info("Sequence Number : " + sequenceNumber);
					log.info("SR Status : " + statusId + "****Trans Status : "
							+ transStatusId);
					log.info("Orig Amount : " + originalAmount
							+ "****Net Amount : " + netAmount);
					log.info("Expiry TS : " + expiryTS);
					log.info("Is slip Details posted to Accounting : "
							+ postedToAcc);

					Timestamp fromUTCDate = DateHelper
							.getUTCTimeFromLocal(DateEngineUtil
									.convertStringToDate(fromDate).getTime());

					if (newSeqNumber == oldSeqNumber) {
						/**
						 * CALCULATING EXPIRED SLIPS COUNT AND EXPIRED SLIPS
						 * AMOUNT
						 */
						if (!isExpiredCountAdded
								&& expiryTS != null
								&& expiryTS.before(fromUTCDate)
								&& postedToAcc
										.equalsIgnoreCase(NOT_POSTED_TO_ACCOUNTING)) {
							slipsExpiredCount++;
							slipsExpiredAmtSum += netAmount;
							isExpiredCountAdded = true;
						}
						/** CALCULATING VOIDED SLIPS AND VOIDED SLIP AMOUNT */
						if (statusId == VOID_STATUS_ID) {
							if (postedToAcc
									.equalsIgnoreCase(POSTED_TO_ACCOUNTING)) {
								slipsVoidedCount++;
								isVoidedCountAdded = true;
								slipsAddedAmtSum += netAmount;
							} else if (postedToAcc
									.equalsIgnoreCase(NOT_POSTED_TO_ACCOUNTING)) {
								if (!isVoidedCountAdded) {
									if (includePrintStat
											&& (transStatusId == PRINTED_STATUS_ID || transStatusId == CHANGE_STATUS_ID)) {
										slipsVoidedCount++;
										isVoidedCountAdded = true;
										slipsAddedAmtSum += netAmount;
									}
									if (includePendStat
											&& transStatusId == PENDING_STATUS_ID) {
										slipsVoidedCount++;
										isVoidedCountAdded = true;
										slipsAddedAmtSum += originalAmount;
									}
								}
							}
						}

						/** CALCULATING ADDED SLIPS COUNT AND ADDED SLIP AMOUNT */
						/**
						 * CALCULATING REDEEMED SLIPS COUNT AND REDEEMED SLIP
						 * AMOUNT
						 */
						if (transStatusId == PROCESSED_STATUS_ID
								|| (transStatusId == CHANGE_STATUS_ID && postedToAcc
										.equalsIgnoreCase(POSTED_TO_ACCOUNTING))) {
							slipsAddedCount++;
							slipsRedeemedCount++;
							isAddCountAdded = true;
							slipsAddedAmtSum += netAmount;
							slipsRedeemedAmtSum += netAmount;
						}
						if (!isAddCountAdded) {
							if (includePrintStat
									&& transStatusId == PRINTED_STATUS_ID
									|| (transStatusId == CHANGE_STATUS_ID && postedToAcc
											.equalsIgnoreCase(NOT_POSTED_TO_ACCOUNTING))) {
								// INCLUDE PRINTED/CHANGED STATUS FOR
								// CALCULATING THE NUMBER OF SLIPS AND TOTAL
								// AMOUNT
								// FOR CHANGED STATUS THE SLIP PROC POST TO
								// ACCOUNTING FLAG SHOULD BE "NO"
								slipsAddedCount++;
								isAddCountAdded = true;
								// TOTAL AMOUNT SHOULD BE DONE BASED ON JACKPOT
								// NET AMOUNT
								slipsAddedAmtSum += netAmount;
							}
							if (includePendStat
									&& transStatusId == PENDING_STATUS_ID) {
								// INCLUDE PENDING STATUS FOR CALCULATING THE
								// NUMBER OF SLIPS AND TOTAL AMOUNT
								slipsAddedCount++;
								isAddCountAdded = true;
								// TOTAL AMOUNT SHOULD BE DONE BASED ON JACKPOT
								// ORIGINAL AMOUNT
								slipsAddedAmtSum += originalAmount;
							}
						}
						log.info("Slips Added : " + slipsAddedCount
								+ "---Slips Added Amount : " + slipsAddedAmtSum);
						log.info("Slips Redeemed : " + slipsRedeemedCount
								+ "----Slips Redeemed Amount : "
								+ slipsRedeemedAmtSum);
						log.info("Slips Voided : " + slipsVoidedCount
								+ "----Slips Voided Amount : "
								+ slipsVoidedAmtSum);
						log.info("Expired Count : " + slipsExpiredCount
								+ "---Expired Amount : " + slipsExpiredAmtSum);
					}
				}
				jpLiabilityDetDTO = new JackpotLiabilityDetailsDTO();
				jpLiabilityDetDTO.setSlipsAdded(slipsAddedCount);
				jpLiabilityDetDTO.setSlipsRedeemed(slipsRedeemedCount);
				jpLiabilityDetDTO.setSlipsExpired(slipsExpiredCount);
				jpLiabilityDetDTO.setSlipsVoided(slipsVoidedCount);
				jpLiabilityDetDTO.setSlipsAddedAmt(slipsAddedAmtSum);
				jpLiabilityDetDTO.setSlipsRedeemed(slipsRedeemedAmtSum);
				jpLiabilityDetDTO.setSlipsExpired(slipsExpiredAmtSum);
				jpLiabilityDetDTO.setSlipsVoided(slipsVoidedAmtSum);
				jpLiabilityDetDTO.toString();
			} else if (log.isInfoEnabled()) {
				log.info("No records Available for DEG Liability");
			}
			log.info("liabilityDetailsList object-->" + liabilityDetailsList);
			log.info("liabilityDetailsList size-->"
					+ liabilityDetailsList.size());
			log.info("liabilityDetailsList toString()-->"
					+ liabilityDetailsList.toString());
		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotLiabilityDetailsForDEG",
					e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotLiabilityDetailsForDEG", e2);
			throw new JackpotDAOException(e2);
		}
		return jpLiabilityDetDTO;
	}

	/**
	 * Added for Seminoles ACI Kiosk modifications
	 * @param empId
	 * @param siteId
	 * @param isSupervisor if true, results are not filtered by employee
	 * 					   if false, results are filtered by employee id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<JackpotDTO> getAllActiveJackpotsForEmpId(String empId, int siteId, boolean isSupervisor) {

		log.info("JackpotDAO---> Inside getAllActiveJackpotsForEmpId : ACI Kiosk version. isSupervisor:" + isSupervisor);

		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();

		ArrayList<Short> jackpotStatusFlagIdList = new ArrayList<Short>();
		ArrayList<Short> transStatusFlagIdList = new ArrayList<Short>();

		jackpotStatusFlagIdList.add(PENDING_STATUS_ID);
		jackpotStatusFlagIdList.add(REPRINT_STATUS_ID);
		jackpotStatusFlagIdList.add(CHANGE_STATUS_ID);
		jackpotStatusFlagIdList.add(PRINTED_STATUS_ID);
		
		transStatusFlagIdList.add(PRINTED_STATUS_ID);
		transStatusFlagIdList.add(CHANGE_STATUS_ID);

		Session sessionObj = HibernateUtil.getSessionFactory().openSession();
		Criterion crtSiteId = Restrictions.and(
				Restrictions.eq("siteId", siteId),
				Restrictions.in("statusFlagId", jackpotStatusFlagIdList));
		Criterion crtPostToAcc = Restrictions.or(Restrictions.isNull("postToAccounting"), Restrictions.ne("postToAccounting", "Y"));
		Criterion crtSlipTypeId = Restrictions.and(crtPostToAcc, Restrictions.eq("slipTypeId", JACKPOT_SLIP_TYPE_ID));
		Criterion crtFinal = Restrictions.and(crtSiteId, crtSlipTypeId);
		Criterion crtEmpId = Restrictions.eq("trans.employeeId", empId);
		Criterion crtTransStatus = null;
		Criteria slipRefCriteria = null;
		
		if(isSupervisor) {
			//Transaction status check removed by Bala, to allow showing of pending jackpots that have been cleared from the machine
			slipRefCriteria = sessionObj.createCriteria(SlipReference.class).add(crtFinal);
			if(log.isDebugEnabled()) {
				log.debug("Not adding employee & transaction status check as employee is a supervisor");
			}
		} else if(!isSupervisor) {
			crtTransStatus = Restrictions.and(crtEmpId, Restrictions.in("trans.statusFlagId", transStatusFlagIdList));
			slipRefCriteria = sessionObj.createCriteria(SlipReference.class).createAlias("transaction", "trans", Criteria.INNER_JOIN)
					.add(crtFinal).add(crtTransStatus);
			if(log.isDebugEnabled()) {
				log.debug("Adding employee check as employee is not a supervisor");
			}
		}

		List<SlipReference> slipRefCriteriaList = slipRefCriteria.list();

		if (slipRefCriteriaList != null && !slipRefCriteriaList.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.info("pendingSlips.size() is " + slipRefCriteriaList.size());
			}
			for (SlipReference slipRef : slipRefCriteriaList) {

				JackpotDTO jackpotDTO = JackpotHelper.getJackpotDTO(slipRef);
				jackpotDTOList.add(jackpotDTO);
			}
			if (log.isInfoEnabled()) {
				log.info("The list of pending records as been returned , Record count: " + slipRefCriteriaList.size());
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("There are no pending records");
			}
		}

		log.info("JackpotDAO---> End of getAllActiveJackpotsForEmpId");

		return jackpotDTOList;
	}

	public static List<JackpotDTO> getJackpotDetails(JackpotDTO jackpotDTO)
			throws JackpotDAOException {
		List<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();
		try {
			if (log.isInfoEnabled()) {
				log.info("Inside getJackpotDetails DAO");
			}
			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criterion crtnMainCrt = null;
			if (log.isInfoEnabled()) {
				log.info("Site Id: " + jackpotDTO.getSiteId());
			}

			// -------------Added for ACI - Added Printed Status
			// Add List of Values and use "in" clause
			// SLIP_REFERENCE - > SLPR_ACC_POST = N, then add PRINTED_STATUS_ID,
			// CHANGE_STATUS_ID, REPRINT_STATUS_ID
			// Allow Processing of Slips with printed Status = true,
			List<Short> statusParamList = new ArrayList<Short>();
			statusParamList.add(PENDING_STATUS_ID);

			if (jackpotDTO.isS2SJackpotProcess()) {
				statusParamList.add(PRINTED_STATUS_ID);
				statusParamList.add(CHANGE_STATUS_ID);
				statusParamList.add(REPRINT_STATUS_ID);
			}

			Criterion crtStatus = Restrictions.in("statusFlagId",
					statusParamList);
			// -------------End of ACI

			Criterion crtSiteId = Restrictions.eq("siteId", jackpotDTO.getSiteId());
			Criterion crtSiteSeq =Restrictions.and(Restrictions.eq("acnfNumber", jackpotDTO.getAssetConfigNumber()), crtSiteId);
			crtnMainCrt = Restrictions.and(crtStatus, crtSiteSeq);

			Order orderBySeq = Order.asc("sequenceNumber");

			Criteria slipRefCriteria = sessionObj
					.createCriteria(SlipReference.class).add(crtnMainCrt)
					.addOrder(orderBySeq);

			// Criteria slipRefCriteria = sessionObj.createCriteria(
			// SlipReference.class).add(crtSiteId);

			List<SlipReference> pendingSlips = slipRefCriteria.list();

			if (pendingSlips != null && pendingSlips.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("pendingSlips.size() is greater than zero: "
							+ pendingSlips.size());
				}
				for (int i = 0; i < pendingSlips.size(); i++) {

					JackpotDTO jackpotDTOtemp = JackpotHelper
							.getJackpotDTO(pendingSlips.get(i));

					jackpotDTOList.add(jackpotDTOtemp);
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("The list of pending slips for Slot Number have been returned with: "
							+ pendingSlips.size() + " records");
				}
				if (log.isDebugEnabled()) {
					log.debug("*****************************************************************************************************************");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
				if (log.isInfoEnabled()) {
					log.info("There are no pending records");
				}
				if (log.isDebugEnabled()) {
					log.debug("**********************************************");
				}
			}

		} catch (HibernateException e) {
			log.error("HibernateException in getJackpotDetails", e);
			throw new JackpotDAOException(e);
		} catch (Exception e2) {
			log.error("Exception in getJackpotDetails", e2);
			throw new JackpotDAOException(e2);
		}
		return jackpotDTOList;
	}
	
	// @SuppressWarnings("static-access")
	// public static void main(String[] args) {
	// System.out.println("inside Main of JackpotDAO");
	// Session sessionObj = HibernateUtil.getSessionFactory().openSession();
	// System.out.println("After sessionObj");
	// org.hibernate.Transaction transObj = sessionObj.beginTransaction();
	// JackpotDAO jpDAO = new JackpotDAO();
	//
	//
	// System.out.println(DateHelper.getUTCTimeFromLocal(-19800000));
	//
	// long toDate = System.currentTimeMillis();
	// log.debug("To Date: "+DateUtil.getStringDate(toDate));
	// long fromDate = toDate - (99999998);
	//
	// //long fromDate = new java.util.Date("9/02/2008").getTime();
	//
	// log.debug("From Date: "+DateUtil.getStringDate(fromDate));
	//
	// try{
	//
	// JPCashlessProcessInfoDTO jprocDTo = getJackpotStatusAmount(12, 512);
	// System.out.println(jprocDTo.toString());
	// boolean success = JackpotDAO.isAllJackpotProcessed("512000000001115046",
	// 512);
	// if(success) {
	// System.out.println("No jackpot available for processing");
	// } else {
	// System.out.println("Jackpots still not processed");
	// }
	// JackpotDTO jackpotDTO = JackpotHelper.getJackpotDTO(slipReference);
	// jackpotDTO.setPrintEmployeeLogin("10000");

	// System.out.println("JP Status beforeupdating : " +
	// jackpotDTO.getStatusFlagId());
	// JackpotDTO rtnJPDTO = jpDAO.updatePrintJackpot(jackpotDTO);
	// System.out.println("JP Updated : " + rtnJPDTO.getSlipReferenceId());
	// System.out.println("JP Status after updating : " +
	// rtnJPDTO.getStatusFlagId());
	// System.out.println("----------------------------------------------------------------");
	// System.out.println("rtnJPDTO : " + rtnJPDTO.toString());
	// System.out.println("----------------------------------------------------------------");

	// log.debug("===="+JackpotDAO.postUpdateJackpotVoidStatus(jackpotDTO).getAssetConfNumberUsed());

	// }catch (Exception e) {
	// System.out.println("*****************************************");
	// System.out.println("Exception in main method : " + e.getMessage());
	// e.printStackTrace();
	// System.out.println("Exception in main method ends here");
	// System.out.println("*****************************************");
	//
	// }

	/*
	 * try { //JackpotReportsDTO rpDTO =
	 * JackpotDAO.getDetailsToPrintJpSlipReportForDateEmployee(1,"10002",
	 * fromDate, toDate);
	 * 
	 * JackpotReportsDTO rpDTO =
	 * JackpotDAO.getDetailsToPrintJpSlipReportForDate(1, fromDate, toDate);
	 * 
	 * log.debug("----: "+rpDTO.toString()); if(rpDTO!=null){
	 * 
	 * log.debug("Net Amt: "+rpDTO.getJackpotNetAmt());
	 * log.debug("Void Amt: "+rpDTO.getVoidedJackpotAmt());
	 * log.debug("First Name: "+rpDTO.getSlotAttendantFirstName());
	 * log.debug("Last Name: "+rpDTO.getSlotAttendantLastName());
	 * log.debug("Slot At Id: "+rpDTO.getSlotAttendantId());
	 * log.debug("Tax Amt: "+rpDTO.getJackpotTaxAmt());
	 * log.debug("HPJP Amt: "+rpDTO.getJackpotOrgAmt());
	 * 
	 * }else{ log.debug("JackpotReportsDTO is null"); }
	 * 
	 * 
	 * } catch (JackpotDAOException e) { e.printStackTrace(); }
	 */

	// transObj.commit();

	// }

	public static void main(String[] args) {
		try {

			Session sessionObj = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			org.hibernate.Transaction transObj = sessionObj.beginTransaction();

			JackpotDAO dao = new JackpotDAO();
			long toDate = System.currentTimeMillis();
			long fromDate = toDate - (99999998);
			JackpotLiabilityDetailsDTO dto = dao
					.getJackpotLiabilityDetailsForDEG(512,
							DateUtil.getStringDate(fromDate),
							DateUtil.getStringDate(toDate));
			log.info(dto.toString());
			// System.out.println("dto: "+dto.getSequenceNumber());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}