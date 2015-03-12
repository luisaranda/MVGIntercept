/*****************************************************************************
 * $Id: JackpotBO.java,v 1.66.1.3.1.0.2.1, 2013-08-16 11:00:32Z, Anbuselvi, Balasubramanian$
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
package com.ballydev.sds.jackpot.bo;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ballydev.sds.ana.bo.IAnABO;
import com.ballydev.sds.ana.exception.AnAException;
import com.ballydev.sds.common.jackpot.ICreditKeyOffBlock;
import com.ballydev.sds.common.jackpot.IHandPaidJackpot;
import com.ballydev.sds.common.jackpot.IJackpotClear;
import com.ballydev.sds.common.jackpot.IJackpotToCreditMeter;
import com.ballydev.sds.db.HibernateUtil;
import com.ballydev.sds.employee.web.form.AnAUserForm;
import com.ballydev.sds.employee.web.form.FunctionForm;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.dao.JackpotCommonDAO;
import com.ballydev.sds.jackpot.dao.JackpotDAO;
import com.ballydev.sds.jackpot.dao.JackpotEventsDAO;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotEmployeeInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotLiabilityDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.dto.JackpotResetDTO;
import com.ballydev.sds.jackpot.dto.NewWaveRequestDTO;
import com.ballydev.sds.jackpot.dto.NewWaveResponseDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;

/**
 * This Business Object Interface is also an EJB 
 * which is used for InterEngine Communication. 
 * @author dambereen
 */
@Stateless
@Local(IJackpotBO.class)
public class JackpotBO implements IJackpotBO {
	
	/**
	 *Instance of the Logger 
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
			
	/**
	 * Employee Engine BO interface
	 */
	@EJB
	private IAnABO anaBO;
		
	/**
	 * Method to create a pending jp slip when an XC -10 event occurs
	 * @param handPaidJackpot
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO createPendingJackpotSlips(IHandPaidJackpot handPaidJackpot, JackpotDTO jackpotDTO, boolean slotDoorOpenStatus) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside createPendingJackpotSlips BO method");
		}
		return JackpotEventsDAO.createPendingJackpotSlips(handPaidJackpot, jackpotDTO, slotDoorOpenStatus);
	}

	/**
	 * Method to Credit Key Off a pending slip when a XC-30 event occurs
	 * @param jackpotToCreditMeter
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO createJackpotToCreditMeterSlips(IJackpotToCreditMeter jackpotToCreditMeter, JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside createJackpotToCreditMeterSlips BO method");
		}
		return JackpotEventsDAO.createJackpotToCreditMeterSlips(jackpotToCreditMeter, jackpotDTO);
	}

	/**
	 * Method to log the Jp Clear event when a XC-52 event occurs
	 * @param jackpotClear
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO jackpotClearEvent(IJackpotClear jackpotClear, long slipPrimaryKey) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside jackpotClearEvent BO method");
		}
		return JackpotEventsDAO.jackpotClearEvent(jackpotClear, slipPrimaryKey);
	}
		
	/**
	 * Method to check the duplicate message id for the event that occured
	 * @param messageId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO eventDuplicateMsgCheckGetResponse(long messageId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside eventDuplicateMsgCheckGetResponse BO method");
		}
		return JackpotEventsDAO.eventDuplicateMsgCheckGetResponse(messageId);
	}
	
	/**
	 * Method to process the Credit key Off Auth Block
	 * @param creditKeyOffBlk
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<String> creditKeyOffAuthBlockProcessing(ICreditKeyOffBlock creditKeyOffBlk, long slipPrimaryKey)throws JackpotDAOException{
		if(log.isInfoEnabled()) {
			log.info("Inside eventDuplicateMsgCheckGetResponse BO method");
		}
		return JackpotEventsDAO.creditKeyOffAuthBlockProcessing(creditKeyOffBlk, slipPrimaryKey);
	}
	
	/**
	 * Method to lock the Jackpot Slot table on a particular slot no
	 * @param acnfNo
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean lockJackpotSlotTable(String acnfNo, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside lockJackpotSlotTable BO method");
		}
		return JackpotEventsDAO.lockJackpotSlotTable(acnfNo, siteId);
	}
	
	/**
	 * Method to get all the pending jackport data to cache for sending alerts
	 * 
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getAllPendingJackpotForCache() throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getAllPendingJackpotForCache BO method");
		}
		return JackpotDAO.getAllPendingJackpotForCache();
	}

		
	/**
	 * Method to get all the pending jp slips for the site id passed
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId) throws JackpotDAOException {
		log.info("Inside getAllPendingJackpotSlipDetails BO method");
		return JackpotDAO.getAllPendingJackpotSlipDetails(siteId);
	}

	/**
	 * Method to get the pending jp slips for the site id passed based one the Filter - Type (SlotNo/SlotLoc/SeqNo/Minutes)
	 * @param filter
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getJackpotDetails(JackpotFilter filter) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotDetails BO method");
		}
		return JackpotDAO.getJackpotDetails(filter);
	}
	
	/**
	 * Method to get the reprint jp details for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber, int siteId, String cashierDeskEnabled) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getReprintJackpotSlipDetails BO method");
		}
		return JackpotDAO.getReprintJackpotSlipDetails(sequenceNumber, siteId, cashierDeskEnabled);
	}
	
	/**
	 * Method to get the Void Jp details for the Seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getVoidJackpotSlipDetails BO method");
		}
		return JackpotDAO.getVoidJackpotSlipDetails(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Express Jp Blind Attempts for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getExpressJackpotBlindAttempts BO method");
		}
		return JackpotDAO.getExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to post the Express Jp Blind Attempts for a NonCarded Jp for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside postNonCardedExpressJackpotBlindAttempts BO method");
		}
		return JackpotDAO.postNonCardedExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to post the Express Jp Blind Attempts for the Seq No passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside postExpressJackpotBlindAttempts BO method");
		}
		return JackpotDAO.postExpressJackpotBlindAttempts(sequenceNumber, siteId);
	}
	
	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp processing that is aborted
	 * @param sequenceNumber
	 * @param siteId
	 * @return boolean
	 * @throws JackpotDAOException
	 */
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside postUnsuccessfulExpJpBlindAttemptsAbort BO method");
		}
		return JackpotDAO.postUnsuccessfulExpJpBlindAttemptsAbort(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jp Slip status
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public short getJackpotStatus(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotStatus BO method");
		}
		return JackpotDAO.getJackpotStatus(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId)throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotPostToAccountDetail BO method");
		}
		return JackpotDAO.getJackpotPostToAccountDetail(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotStatusForVoid BO method");
		}
		return JackpotDAO.getJackpotStatusForVoid(sequenceNumber, siteId);
	}
	
	/**
	 * Method to get the Jp Slip details that was last processed
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	@Deprecated
	public JackpotDTO getReprintPriorSlipDetails(int siteId, String cashierDeskEnabled) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getReprintPriorSlipDetails BO method");
		}
		return JackpotDAO.getReprintPriorSlipDetails(siteId, cashierDeskEnabled);
	}
	

	/**
	 * Method to get the Jp details to print the report based on the Date and Site id
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId, String fromDate, String toDate) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getDetailsToPrintJpSlipReportForDate BO method");
		}
		return JackpotDAO.getDetailsToPrintJpSlipReportForDate(siteId, fromDate, toDate);
	}

	/**
	 * Method to get the Jp details to print the report based on the Date,Employee and Site id
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getDetailsToPrintJpSlipReportForDateEmployee BO method");
		}
		return JackpotDAO.getDetailsToPrintJpSlipReportForDateEmployee(siteId, employeeId, fromDate, toDate);
	}
	
	/**
	 * Method to process a pending jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO updateProcessJackpot(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updateProcessJackpot BO method");
		}
		return JackpotDAO.updateProcessJackpot(jackpotDTO);
	}
	
	/**
	 * Method to process a printed jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	public JackpotDTO updatePrintJackpot(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updatePrintJackpot BO method");
		}
		return JackpotDAO.updatePrintJackpot(jackpotDTO);
	}
	
	/**
	 * Method to process all the jackpots before closing the cashless account from cage.
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
	public List<JackpotDTO> updatePrintJackpots(String accountNumber, Integer siteId, String employeeId, String employeeFirstName, String employeeLastName, String cashDeskLocation, boolean validateEmpSession) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updatePrintJackpots BO method");
		}
		return JackpotDAO.updatePrintJackpots(accountNumber, siteId, employeeId, employeeFirstName, employeeLastName, cashDeskLocation, validateEmpSession);
	}
	
	/**
	 * Method to process a manual jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO updateProcessManualJackpot(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updateProcessManualJackpot BO method");
		}
		JackpotDAO jackpotDAO = new JackpotDAO();
		return jackpotDAO.updateProcessManualJackpot(jackpotDTO);
	}
	
	/**
	 * Method to update the jackpot status on a void
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO postUpdateJackpotVoidStatus(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside postUpdateJackpotVoidStatus BO method");
		}
		return JackpotDAO.postUpdateJackpotVoidStatus(jackpotDTO);
	}
	
	/**
	 * Method to update the jackpot status on a reprint
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO postUpdateJackpotReprint(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside postUpdateJackpotReprint BO method");
		}
		return JackpotDAO.postUpdateJackpotReprint(jackpotDTO);
	}
	
	/**
	 * Method to void all the pending jackpot slips based on the site id
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidAllPendingJackpotSlips(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updateVoidAllPendingJackpotSlips BO method");
		}
		return JackpotDAO.updateVoidAllPendingJackpotSlips(jackpotDTO);
	}
	
	/**
	 * Method to void all the pending jackpot slips based on the slot and site id
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidPendingJackpotSlipsForSlotNo(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside updateVoidPendingJackpotSlipsForSlotNo BO method");
		}
		return JackpotDAO.updateVoidPendingJackpotSlipsForSlotNo(jackpotDTO);
	}
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> voidPendingJackpotSlipsForAuditProcess(int siteId, long startTime, long endTime, String employeeId, String kioskProcessed) throws JackpotDAOException {		
		if(log.isInfoEnabled()) {
			log.info("Inside voidPendingJackpotSlipsForAuditProcess BO method");
		}
		String empFirstName = null;
		String empLastName = null;
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		if (employeeId != null) {
			AnAUserForm userForm = null;
			try {
				userForm = anaBO.getUserDetails(employeeId, siteId);
			} catch (AnAException e) {
				log.error("Exception while getUserDetails from ANA ", e);
				throw new JackpotDAOException(e);
			}
			if (userForm != null) {
				empFirstName = userForm.getFirstName();
				empLastName = userForm.getLastName();
			}
		}
		
		/** VOID PENDING JACKPOT SLIPS FOR AUDIT*/
		JackpotCommonDAO jpCommonDAO = new JackpotCommonDAO();
		
		List<JackpotDTO> voidedPendingJP=jpCommonDAO.updateVoidPendingJackpotSlipsForAuditProcess(siteId, startTime, endTime, employeeId, kioskProcessed, empFirstName,empLastName);
		/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
		for(JackpotDTO jackpotDTO:voidedPendingJP){
			String key = jackpotDTO.getSiteId() + "_" + jackpotDTO.getSequenceNumber();			
			JackpotUtil.removePendingJPFromCache( key);
		}	
		return voidedPendingJP;
		
	}
	

	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id 
	 * @param siteId
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> voidAllPendingJackpotSlipsForAuditProcess(int siteId, String employeeId, String kioskProcessed) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidAllPendingJackpotSlipsForAuditProcess BO method");
		}
		
		String empFirstName = null;
		String empLastName = null;
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		// In case of auto voiding of slip using accounting site configuration parameter
		// the employee id value will come as null. 
		if(employeeId != null) {
			AnAUserForm userForm = null;
			try {
				userForm = anaBO.getUserDetails(employeeId, siteId);
			} catch (AnAException e) {
				log.error("Exception while getUserDetails from ANA ",e);
				throw new JackpotDAOException(e);
			}
			if(userForm!=null){
				empFirstName = userForm.getFirstName();
				empLastName = userForm.getLastName();
			}
		}
		/** VOID PENDING JACKPOT SLIPS FOR AUDIT*/
		JackpotCommonDAO jpCommonDAO = new JackpotCommonDAO();
		
		List<JackpotDTO> voidedPendingJP=jpCommonDAO.updateVoidAllPendingJackpotSlipsForAuditProcess(siteId, employeeId, kioskProcessed,empFirstName,empLastName);
		
		/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */				
		for(JackpotDTO jackpotDTO:voidedPendingJP){
			String key = jackpotDTO.getSiteId() + "_" + jackpotDTO.getSequenceNumber();
			JackpotUtil.removePendingJPFromCache( key);			
		}
		return voidedPendingJP;
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
	public List<JackpotDTO> voidPendingJackpotSlipsWithSlotForAuditProcess(int siteId, long startTime, long endTime, String slotNo, String employeeId, String kioskProcessed) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside voidPendingJackpotSlipsWithSlotForAuditProcess BO method");
		}
		
		String empFirstName = null;
		String empLastName = null;
		
		/** CALL TO ANA TO GET THE EMPLOYEE DETAILS */
		AnAUserForm userForm = null;
		try {
			userForm = anaBO.getUserDetails(employeeId, siteId);
		} catch (AnAException e) {
			log.error("Exception while getUserDetails from ANA ",e);
			throw new JackpotDAOException(e);
		}
		if(userForm!=null){
			empFirstName = userForm.getFirstName();
			empLastName = userForm.getLastName();
		}
		
		/** VOID PENDING JACKPOT SLIPS FOR AUDIT*/
		JackpotCommonDAO jpCommonDAO = new JackpotCommonDAO();
		
		List<JackpotDTO> voidedPendingJP=jpCommonDAO.updateVoidPendingJackpotSlipsForAuditProcess(siteId, startTime, endTime, slotNo, employeeId, kioskProcessed, empFirstName, empLastName);
		
		/* remove processed Pending JP from cache to stop sending alert 92 - PENDING JP FOR SLOT */
		for(JackpotDTO jackpotDTO:voidedPendingJP){
			String key = jackpotDTO.getSiteId() + "_" + jackpotDTO.getSequenceNumber();
			JackpotUtil.removePendingJPFromCache( key);			
		}
		return voidedPendingJP;
	}
	
	/**
	 * Method to insert a record on an employee card on when a jp is hit in the slot machine
	 * @param employeeInfoDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean jackpotCardedEmployeeInfo(JackpotEmployeeInfoDTO employeeInfoDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside jackpotCardedEmployeeInfo BO method");
		}
		JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
		return jackpotCommonDAO.jackpotCardedEmployeeInfo(employeeInfoDTO);
	}
	
	/**
	 * Method to get the slip transfer table's slip count 
	 * @return
	 * @throws JackpotDAOException
	 */
	public Long getSlipsByCount() throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getSlipsByCount BO method");
		}
		JackpotCommonDAO jpCommonDAO = new JackpotCommonDAO();
		return jpCommonDAO.retrieveSlipsByCount();		
	}
		
	/**
	 * Method that returns the Processed Progressive Jackpot Info
	 * @param siteId
	 * @param gamingDate
	 * @return JackpotDetailsDTO
	 * @throws JackpotDAOException
	 */
	public List<JackpotDetailsDTO> getProgressiveJackpotInfo(int siteId, long fromTime, long toTime) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getProgressiveJackpotInfo BO method");
		}
		JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
		return jackpotCommonDAO.getProgressiveJackpotInfo(siteId, fromTime, toTime);
	}	
	
	/**
	 * Method to get the Jackpot Details for S2S
	 * @param sequenceNo
	 * @param siteId
	 * @return JackpotDetailsDTO
	 * @throws JackpotDAOException
	 */
	public JackpotDetailsDTO getJackpotDetailsForS2S(long sequenceNo, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotDetailsForS2S BO method");
		}
		JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
		return jackpotCommonDAO.getJackpotDetailsForS2S(sequenceNo, siteId);
	}
	
	/**
	 * Method to insert the pouch pay attendant who did the Pouch Pay
	 * @param slipReference
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean insertPouchPayAttendant(JackpotDTO jackpotDTO) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside insertPouchPayAttendant BO method");
		}
		return JackpotDAO.insertPouchPayAttendant(jackpotDTO);
	}	

	/**
	 * Method to provide the total count of the jackpots that have been processed and reprinted
	 * @param fromDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public int getNumOfJackpots(Date fromDate, int siteId) throws JackpotDAOException{
		if(log.isInfoEnabled()) {
			log.info("Inside getNumOfJackpots 2 arg BO method");
		}
		return new JackpotCommonDAO().getNumOfJackpots(fromDate, siteId);
	}
	
	/**
	 * Method to provide the total count of the jackpots that have been processed and reprinted
	 * @param fromDate
	 * @param toDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public int getNumOfJackpots(Date fromDate,Date toDate, int siteId) throws JackpotDAOException{
		if(log.isInfoEnabled()) {
			log.info("Inside getNumOfJackpots 3 arg BO method");
		}
		return new JackpotCommonDAO().getNumOfJackpots(fromDate,toDate, siteId);
	}
	
	
	/**
	 * Method used to post the latest progressive jackpot amount from the progressive engine.
	 * @param acnf_number
	 * @param msgId
	 * @param jackpotId
	 * @param sdsAmount
	 * @param pmuAmount
	 */
	public void postProgressiveJackpotAmount(String acnf_number, Long msgId, Long sdsAmount, Long pmuAmount, int siteId)
			throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside postProgressiveJackpotAmount BO method ");
			log.info("***Parameters Received***");
			log.info("Asset Config Number Received :" + acnf_number);
			log.info("Message Id Received :" + msgId);
			// log.info("Jackpot Id Received :"+jackpotId);
			log.info("SDS Calculated Amount Received :" + sdsAmount);
			log.info("PMU Amount Received :" + pmuAmount);
		}
		if (acnf_number != null && !((acnf_number.trim() + " ").equalsIgnoreCase(" ")) && msgId != null
				&& msgId.longValue() != 0
				// && jackpotId!=null && !((jackpotId.trim()+"
				// ").equalsIgnoreCase(" "))
				&& sdsAmount != null && pmuAmount != null) {
			log.info("Inside postProgressiveJackpotAmount BO method , calling DAO method....");
			JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
			boolean processStatus = jackpotCommonDAO.processProgressiveJackpotAmount(acnf_number, msgId,
					sdsAmount, pmuAmount, siteId);
			if (log.isInfoEnabled()) {
				log.info("Process Status for Progressive Engine Post in postProgressiveJackpotAmount method in JackpotBO :"
						+ processStatus);
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("Exiting postProgressiveJackpotAmount BO method , not calling dao method since required fields are missing....");
			}
		}
		if (log.isInfoEnabled()) {
			log.info("End of postProgressiveJackpotAmount BO method ");
		}
	}
	
	/* WEB METHODS TO CAGE */
	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean isAllJackpotProcessed(String accountNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside isAllJackpotProcessed BO method");
		}
		try {
			return JackpotDAO.isAllJackpotProcessed(accountNumber, siteId);
		} catch(JackpotDAOException e) {
			throw new JackpotDAOException(e);
		} catch(Exception e) {
			throw new JackpotDAOException(e);
		}
	}
	
	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public List<JPCashlessProcessInfoDTO> getAllUnProcessedJackpot(String accountNumber, int siteId) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getAllUnProcessedJackpot BO method");
		}
		try {
			return JackpotDAO.getAllUnProcessedJackpot(accountNumber, siteId);
		} catch(JackpotDAOException e) {
			throw new JackpotDAOException(e);
		} catch(Exception e) {
			throw new JackpotDAOException(e);
		}
	}
	
	/**
	 * Main method of class
	 * @param args
	 */
	public static void main(String[] args) {
		
		Session sessionObj = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction trans = sessionObj.beginTransaction();
		
		JackpotBO jpBO = new JackpotBO();		
		try {
			if(log.isDebugEnabled()) {
				log.debug("Slip Count: "+jpBO.getSlipsByCount());
			}
		} catch (JackpotDAOException e) {			
			e.printStackTrace();
		}		
		/*try {
			SlipInfoDTO [] dtoAray = null; 			
			dtoAray = jpBO.getSlipsBySequence(0,100);			
			log.debug("Array size: "+dtoAray.length);
			
			for(int i=0; i<dtoAray.length ; i++){
				log.debug("***********************************");
				log.debug("Record: "+i
						+"SEQ NO: "+dtoAray[i].getSequenceNumber()+"\n"
						+"Slot NO: "+dtoAray[i].getAssetNumber()+"\n"
						+"AUDIT: "+dtoAray[i].getAuditCodeId()+"\n"
						+"AUTH EMP: "+dtoAray[i].getAuthorizingEmployeeId()+"\n"
						+"CURRENCY: "+dtoAray[i].getCurrency()+"\n"
						+"EXT TRANS ID: "+dtoAray[i].getExtTransId()+"\n"
						+"1 AUTH EMP: "+dtoAray[i].getFirstAuthorizingEmployeeId()+"\n"
						+"JP TYPE: "+dtoAray[i].getJackpotTypeId()+"\n"
						+"LOGGED EMP: "+dtoAray[i].getLoggedInEmployeeId()+"\n"
						+"2 AUTH EMP: "+dtoAray[i].getSecondAuthorizingEmployeeId()+"\n"
						+"SLIP ID: "+dtoAray[i].getSlipId()+"\n"
						+"SLIP TRANS EMP: "+dtoAray[i].getSlipTransEmployeeId()+"\n"
						+"SLIP TYPE: "+dtoAray[i].getSlipTypeId()+"\n"
						+"STATUS: "+dtoAray[i].getStatusFlagId()+"\n"
						+"TRANS EMP: "+dtoAray[i].getTransEmployeeId()+"\n"
						+"BEEF AMT: "+dtoAray[i].getBeefAmount()+"\n"
						+"GMU DENOM: "+dtoAray[i].getGmuDenomination()+"\n"
						+"HPJP AMT; "+dtoAray[i].getHandPaidAmount()+"\n"
						+"HPJP RND AMT: "+dtoAray[i].getHandPaidAmountRounded()+"\n"
						+"JP NET AMT: "+dtoAray[i].getJackpotNetAmount()+"\n"
						+"MACH AMT: "+dtoAray[i].getMachinePaidAmount()+"\n"
						+"ORG AMT: "+dtoAray[i].getOriginalAmount()+"\n"
						+"PROCESS FLAG: "+dtoAray[i].getProcessFlagId()+"\n"
						+"SLIP REV FLAG: "+dtoAray[i].getSlipRevenueType()+"\n"
						+"TRANS DATE: "+dtoAray[i].getTransDate()+"\n");
				log.debug("***********************************");				
			}			
		} catch (JackpotDAOException e) {
			e.printStackTrace();
		}*/
		
		/*try {
			List<JackpotDTO> jackpotDTOList = jpBO.voidPendingJackpotSlipsForAuditProcess(1);
			if(jackpotDTOList!=null){
				log.debug("Array size: "+jackpotDTOList.size());
				for(int i=0; i<jackpotDTOList.size() ; i++){
					log.debug("Record: "+i+" Seq NO: "+jackpotDTOList.get(i).getSequenceNumber()+" "+jackpotDTOList.get(i).getOriginalAmount()
							+" "+jackpotDTOList.get(i).getAssetConfigNumber());
				}	
			}
		
			
					
		} catch (JackpotDAOException e) {
			e.printStackTrace();
		}*/
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.jackpot.bo.IJackpotBO#isLastHitProgressive(int, java.lang.String)
	 */
	public boolean isLastHitProgressive(int siteNumber, String slotNumber) throws JackpotDAOException {
		if (log.isInfoEnabled()) {
			log.info("Inside isLastHitProgressive method of JackpotBO with siteNumber :" + siteNumber);
			log.info("Inside isLastHitProgressive method of JackpotBO with slot Number :" + slotNumber);
		}
		if(!(siteNumber<=0) && slotNumber!=null && !((slotNumber.trim()+" ").equalsIgnoreCase(" ")) ){
			JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
			return jackpotCommonDAO.isLastHitProgressiveJackpot(siteNumber, slotNumber);
		}
		return false;
	}

	public List<NewWaveResponseDTO> getNewWaveJackpotDetails(NewWaveRequestDTO newWaveRequestDTO) throws JackpotDAOException {		
		if(log.isInfoEnabled()) {
			log.info("Inside getNewWaveJackpotDetails BO method");
		}
		JackpotCommonDAO jackpotCommonDAO = new JackpotCommonDAO();
		return jackpotCommonDAO.getNewWaveDetails(newWaveRequestDTO);		
	}

	/**
	 * Method to get the Pending Jackpots that were not reset
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotResetDTO> getUnResetPendingJackpots(int siteId)throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getUnResetPendingJackpots BO method");
		}
		return new JackpotCommonDAO().getUnResetPendingJackpots(siteId);
	}

	/**
	 * Method to return the slip reference id for a particular slip seq no and site no
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotDAOException
	 */
	public long getSlipReferenceIdForSlipSeq(int siteNo, long sequenceNo) throws JackpotDAOException {	
		return JackpotDAO.getSlipReferenceIdForSlipSeq(siteNo, sequenceNo);
	}

	/**
	 * Method to get the slip reference ID for the given sequence number and Site ID
	 * 
	 * @param sequenceNumber
	 * @return statusDesc
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot amount retrieval failed
	 * @author vsubha
	 */
	public JPCashlessProcessInfoDTO getJackpotStatusAmount(long sequenceNumber, int siteId)
			throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotStatusAmount BO method");
		}
		return JackpotDAO.getJackpotStatusAmount(sequenceNumber, siteId);
	}
	
	/**
	 * Method to retrieve Slip details By Sequence number and site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotDTO retrieveSlipBySequenceNumber(long sequenceNumber, int siteId ) 
		throws JackpotEngineServiceException {	
		if(log.isInfoEnabled()) {
			log.info("Inside retrieveSlipBySequenceNumber BO method");
		}
		 return JackpotDAO.retrieveSlipBySequenceNumber(sequenceNumber, siteId);
	
	}
	
	/**
	 * Method to auto void reprinted sequence and create a new slip seq in Slip Ref table
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	public JackpotDTO postUpdateJpAutoVoidReprint(JackpotDTO jackpotDTO) throws JackpotDAOException{
		if(log.isInfoEnabled()) {
			log.info("Inside postUpdateJpAutoVoidReprint BO method");
		}
		 return JackpotDAO.postUpdateJpAutoVoidReprint(jackpotDTO);
	}
	
	/**
	 * Method to get Jackpot Liability details for DEG
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	public JackpotLiabilityDetailsDTO getJackpotLiabilityDetailsForDEG(int siteId, String fromDate, String toDate) throws JackpotDAOException {
		if(log.isInfoEnabled()) {
			log.info("Inside getJackpotLiabilityDetailsForDEG BO method");
		}
		return JackpotDAO.getJackpotLiabilityDetailsForDEG(siteId, fromDate, toDate);
	}

	/**
	 * Method to create a jackpot slip in the db from an external system
	 * @param jackpotDTO
	 * @param transExcepCode
	 * @param jpGeneratedBy
	 * @return JackpotDTO
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JackpotDTO createJackpotFromExternalSystem(JackpotDTO jackpotDTO, short transExcepCode,JpGeneratedBy jpGeneratedBy) throws JackpotDAOException {
		log.info("Inside createJackpotFromExternalSystem BO method");
		return JackpotCommonDAO.createJackpotFromExternalSystem(jackpotDTO, transExcepCode, jpGeneratedBy);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> getAllActiveJackpotsForEmpId(String empId, int siteId) throws JackpotEngineServiceException{
		log.info("Inside getAllActiveJackpotsForEmpId BO method");
		
		boolean isSupervisor = false;
		String allowAllJackpotsforSupervisor = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JACKPOT_ALLOW_ALL_FOR_SUPERVISOR);
		if(log.isDebugEnabled()) {  
			log.debug("Allow all jackpots for supervisor set to:" + allowAllJackpotsforSupervisor);
		}
		if(allowAllJackpotsforSupervisor!= null && allowAllJackpotsforSupervisor.equalsIgnoreCase("Y")){
			try {
				AnAUserForm userDetails = anaBO.getUserDetails(empId, siteId);
				if (userDetails != null) {
					FunctionForm[] funcForm = userDetails.getFunctionForms();
					if (funcForm != null && funcForm.length > 0) {
						for (FunctionForm funcFormObj : funcForm) {
							if (funcFormObj.getFunctionName() != null
									&& funcFormObj.getFunctionName().equalsIgnoreCase(IAppConstants.JACKPOT_SUPERVISOR_EMP_FUNCTION)) {
								isSupervisor = true;
								break;
							}
						}
					}
				}
			} catch (AnAException e) {
				throw new JackpotEngineServiceException(e);
			}
		}
		if(log.isDebugEnabled()){ 
			log.debug("User details obtained, isSupervisor:" + isSupervisor);
		}
		
		return JackpotDAO.getAllActiveJackpotsForEmpId(empId, siteId, isSupervisor);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JackpotDTO> getJackpotDetails(JackpotDTO jackpotDTO) throws JackpotDAOException{ 
		log.info("Inside getJackpotDetails BO method");
		return JackpotDAO.getJackpotDetails(jackpotDTO);
	}
}