package com.ballydev.sds.jackpot.keypad;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.ballydev.sds.db.HibernateUtil;
import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.db.slip.ProcessSession;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.JackpotUtil;

/**
 * 
 * This Utility will maintain the session for the keypad process.
 * 
 * @author ranjithkumarm
 * 
 */
public class SessionManagerDAO implements IKeypadProcessConstants {

	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	private final static String DELETE_PROCESS_SESSION_FOR_ASSET = "delete from com.ballydev.sds.jackpot.db.slip.ProcessSession processSession "
			+ "where processSession.acnfNumber = :acnfNumber "
			+ "and processSession.siteId = :siteId";

	/**
	 * @param sessionObject
	 * @return
	 */
	public static synchronized boolean createSession(ProcessSession sessionObject) {
		boolean sessionCreated = false;
		try {
			if (sessionObject != null && sessionObject.getAcnfNumber() != null
					&& !((sessionObject.getAcnfNumber().trim() + " ").equalsIgnoreCase(" "))
					&& sessionObject.getSiteId() != null && sessionObject.getSiteId().intValue() != 0) {
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				if (session != null) {
					Timestamp createdTS = new Timestamp(System.currentTimeMillis());
					sessionObject.setCreatedTs(DateHelper.getUTCTimeFromLocal(createdTS));
					sessionObject.setCreatedUser(JACKPOT_ENGINE_KEYPAD_NAME);
					session.save(sessionObject);
					sessionCreated = true;
				}
			} else {
				log.error("Session Creation failed due to improper data in createSession of SessionManagerUtil");
			}
		} catch (Exception e) {
			log.error("Session Creation failed due to improper data in createSession of SessionManagerUtil", e);
		}
		return sessionCreated;
	}

	/**
	 * Method to delete process session for the asset
	 * 
	 * @param assetNumber
	 * @param siteId
	 * @return
	 * @author vsubha
	 */
	public static synchronized int deleteSession(String assetNumber, Integer siteId) {
		int rowsDeleted = 0;
		try {
			if (assetNumber != null && !assetNumber.trim().isEmpty() && siteId != null && siteId.intValue() != 0) {
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				if (session != null) {
					Query query = session.createQuery(DELETE_PROCESS_SESSION_FOR_ASSET);
					query.setParameter("acnfNumber", assetNumber);
					query.setParameter("siteId", siteId);
					rowsDeleted = query.executeUpdate();
				}
			}
		} catch (Exception e) {
			log.error("Session Deletion failed due to improper data in deleteSession of SessionManagerDAO", e);
		}
		return rowsDeleted;
	}

	/**
	 * @param sessionObject
	 * @return
	 */
	public static synchronized boolean storeSession(ProcessSession sessionObject) {
		boolean sessionStored = false;
		try {
			if (sessionObject != null && sessionObject.getAcnfNumber() != null
					&& !sessionObject.getAcnfNumber().trim().isEmpty()
					&& sessionObject.getSiteId() != null && sessionObject.getSiteId().intValue() != 0) {
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				if (session != null) {
					Timestamp updatedTS = new Timestamp(System.currentTimeMillis());
					sessionObject.setUpdatedTs(DateHelper.getUTCTimeFromLocal(updatedTS));
					sessionObject.setUpdatedUser(JACKPOT_ENGINE_KEYPAD_NAME);
					// changed to update from saveOrUpdate from 12.3.3 seminoles issue
					session.update(sessionObject);
					sessionStored = true;
				}
			} else {
				log.error("No session Object available for creating");
			}
		} catch (Exception e) {
			log.error("Store Session failed due to improper data in Session-",e);
		}
		return sessionStored;
	}

	/**
	 * @param assetNumber
	 * @param siteId
	 * @return
	 */
	public static synchronized ProcessSession loadSession(String assetNumber, Integer siteId) {
		ProcessSession sessionObject = null;
		try {
			if (assetNumber != null && !assetNumber.trim().isEmpty() && siteId != null && siteId.intValue() != 0) {
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				if (session != null) {
					Criterion assetNumberCriterion = Restrictions.eq("acnfNumber", assetNumber.trim());
					Criterion siteIdCriterion = Restrictions.and(assetNumberCriterion, Restrictions.eq("siteId", siteId.intValue()));
					Criteria criteriaSession = session.createCriteria(ProcessSession.class).add(siteIdCriterion);
					ProcessSession processSession = (ProcessSession) criteriaSession.uniqueResult();
					if (processSession != null) {
						return processSession;
					}
				}
			}
		} catch(HibernateException e) {
			log.error("HibernateException in loadSession of SessionManagerDAO : ", e);
		}
		catch (Exception e) {
			log.error("Exception in loadSession : ", e);
		}
		return sessionObject;
	}

	/**
	 * This method is called in employee card in event for updating the session
	 * object for that slot.
	 * 
	 * @param assetNumber
	 * @param siteId
	 */
	public static synchronized void checkAndUpdateSessionObject(String assetNumber, Integer siteNumberGot) {

		try {
			if (assetNumber != null && !((assetNumber.trim() + " ").equalsIgnoreCase(" "))
					&& siteNumberGot != null && siteNumberGot.intValue() != 0) {
				if(log.isInfoEnabled()) {
					log.info("Updating Session Status....");
				}
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				if (session != null) {
					//Integer siteIdToPass = KeypadUtil.getSiteIdForNumber(siteNumberGot);
					Criterion assetNumberCriterion = Restrictions.eq("acnfNumber", assetNumber.trim());
					Criterion siteIdCriterion = Restrictions.and(assetNumberCriterion, Restrictions.eq(
							"siteId", siteNumberGot));
					Criteria criteriaSession = session.createCriteria(ProcessSession.class).add(
							siteIdCriterion);
					List queryResult = criteriaSession.list();
					if (queryResult != null && queryResult.size() != 0) {
						ProcessSession processSession = (ProcessSession) queryResult.get(0); // getting zero since only one
					     																	 // result would be available.
						if (processSession != null) {
							Timestamp currentTS = new Timestamp(System.currentTimeMillis());
							Long currentTSLong = DateHelper.getUTCTimeFromLocal(currentTS).getTime();
							Long sessionCTS = processSession.getCreatedTs().getTime();
							int currentTSSecs = ((int) (currentTSLong / 1000));
							int sessionCTSSecs = ((int) (sessionCTS / 1000));
							int actualTimeDiffInSecs = ((currentTSSecs) - (sessionCTSSecs));
							if (actualTimeDiffInSecs < 0) {
								actualTimeDiffInSecs = 0;
							}

							int sessionLengthToProcess = 0;
							try {
								String sessionLength = JackpotUtil.getSiteParamValue(
										IKeypadProcessConstants.JP_KP_VALID_SESSION_LENGTH, siteNumberGot);
								sessionLengthToProcess = Integer.parseInt(sessionLength.trim());
							} catch (Exception e) {
								sessionLengthToProcess = IKeypadProcessConstants.DEFAULT_SESSION_LENGTH_IN_MINS;
							}
							if(log.isInfoEnabled()) {
								log.info("Session Length Site Parameter value (in mins):"
									+ sessionLengthToProcess);
							}

							int sessionLengthInSecs = (sessionLengthToProcess * 60);

							if (sessionLengthInSecs < 0) {
								sessionLengthInSecs = 0;
							}

							if (actualTimeDiffInSecs > sessionLengthInSecs) {
								session.delete(processSession);
								if(log.isInfoEnabled()) {
									log.info("Deleting previous session since the time has expired than default session time length.");
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {

		}
	}
}