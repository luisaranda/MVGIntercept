/*****************************************************************************
 * $Id: ServiceCall.java,v 1.0, 2008-04-03 15:51:46Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:51:46 AM$
 * $Log:
 *  65   SDS11     1.64        3/6/2008 11:31:43 AM   Ambereen Drewitt Altered
 *       voidPendingJackpotSlipsForSlotNo and voidAllPendingJackpotSlips web
 *       methods for printerUsed field 
 *  64   SDS11     1.63        2/27/2008 7:08:12 PM   Ambereen Drewitt Altered
 *       adjustmet method to accept SlotNo instead of Acnf_Id
 *  63   SDS11     1.62        1/8/2008 4:58:35 PM    Ambereen Drewitt Handled
 *       service down exception and removed the WebService and
 *       SoapFaultExcepions that were caught.
 *  62   SDS11     1.61        1/3/2008 6:47:23 PM    Ambereen Drewitt Added
 *       labelLoader code for msg dialogs
 *  61   SDS11     1.60        12/20/2007 6:56:19 PM  Ambereen Drewitt Added
 *       web method for reprint of prior slip
 *  60   SDS11     1.59        12/19/2007 4:36:28 PM  Ambereen Drewitt Added
 *       web method to getJpReportDetails for date and employee id.
 *  59   SDS11     1.58        12/12/2007 3:14:37 PM  Ambereen Drewitt Added
 *       msg for service down and threw exception
 *  58   SDS11     1.57        12/3/2007 7:55:36 PM   Ambereen Drewitt Added
 *       methosd getDetailsToPrintJpSlipReportForDate,
 *       voidAllPendingJackpotSlips and voidPendingJackpotSlipsForSlotNo.
 *  57   SDS11     1.56        11/27/2007 12:05:11 PM Rajesh Anantharaj
 *       printing I18n implemented
 *  56   SDS11     1.55        11/17/2007 4:42:22 PM  Ambereen Drewitt Added
 *       javadoc and solved pmd violation
 *  55   SDS11     1.54        11/14/2007 5:18:49 PM  Rajesh Anantharaj reg
 *       update
 *  54   SDS11     1.53        11/14/2007 11:22:42 AM Rajesh Anantharaj reg
 *       update
 *  53   SDS11     1.52        11/5/2007 2:33:34 PM   Ambereen Drewitt Added
 *       sendAlert web method
 *  52   SDS11     1.51        10/31/2007 7:21:33 PM  Ambereen Drewitt Added
 *       siiteId as first arg for both the adjustment methods
 *  51   SDS11     1.50        10/4/2007 11:55:29 AM  Ambereen Drewitt Added
 *       javadoc and removed unnecessary code.
 *  50   SDS11     1.49        9/28/2007 11:15:19 AM  Rajesh Anantharaj removed
 *         fetching marketing service
 *  49   SDS11     1.48        9/23/2007 4:46:56 PM   Rajesh Anantharaj added
 *       method for post reprint transaction
 *  48   SDS11     1.47        9/11/2007 5:33:56 PM   Ambereen Drewitt Removed
 *       code to get the asset service.
 *  47   SDS11     1.46        9/11/2007 5:27:42 PM   Ambereen Drewitt Added jp
 *        new web method to call and altered the site web method
 *  46   SDS11     1.45        9/7/2007 11:13:14 AM   Rajesh Anantharaj changed
 *        the package name to framework standards
 *  45   SDS11     1.44        8/31/2007 5:47:53 PM   Rajesh Anantharaj reg
 *       update
 *  44   SDS11     1.43        8/27/2007 12:37:44 PM  Rajesh Anantharaj changed
 *        the arguement of postJackpotVoid
 *  43   SDS11     1.42        8/25/2007 11:41:57 AM  Rajesh Anantharaj code
 *       clean up
 *  42   SDS11     1.41        8/25/2007 11:40:40 AM  Rajesh Anantharaj code
 *       changed for process jackpot adjustment
 *  41   SDS11     1.40        8/24/2007 5:37:43 PM   Rajesh Anantharaj added
 *       siteId
 *  40   SDS11     1.39        8/24/2007 12:03:26 PM  Ambereen Drewitt Added
 *       web method for no of minutes function.
 *  39   SDS11     1.38        8/22/2007 11:31:58 AM  Ambereen Drewitt
 *       Implmented asset's new method.
 *  38   SDS11     1.37        8/20/2007 4:30:00 PM   Ambereen Drewitt Added
 *       message dialogs when exceptions occur while getting the engine
 *       services.
 *  37   SDS11     1.36        8/16/2007 10:50:52 AM  Rajesh Anantharaj added
 *       method getVoidSlipsDetails
 *  36   SDS11     1.35        8/15/2007 2:37:20 PM   Ambereen Drewitt Added
 *       assets new web method for remote calling.
 *  35   SDS11     1.34        8/10/2007 7:57:36 PM   Rajesh Anantharaj regular
 *        update
 *  34   SDS11     1.33        8/7/2007 9:32:59 AM    Ambereen Drewitt Added
 *       site id arg to de getJpStatus().
 *  33   SDS11     1.32        8/2/2007 3:27:50 PM    Rajesh Anantharaj added
 *       slip printing 
 *  32   SDS11     1.31        7/26/2007 11:13:59 AM  Ambereen Drewitt Added
 *       classes and alterations for the reduction in the no of Manual Jackpot
 *        Screens.
 *  31   SDS11     1.30        7/16/2007 6:26:29 PM   Ambereen Drewitt Check
 *       for invalid slot/location no for a manual jp.
 *  30   SDS11     1.29        7/5/2007 12:41:11 PM   Ambereen Drewitt Added
 *       method to get all the pending slips for the ALL functionality.
 *  29   SDS11     1.28        7/4/2007 5:05:40 PM    Ambereen Drewitt Handled
 *       exception while getting the services.
 *  28   SDS11     1.27        7/4/2007 4:21:21 PM    Rajesh Anantharaj added
 *       old amount in the processJackpotAdjustment method
 *  27   SDS11     1.26        7/4/2007 4:13:10 PM    Ambereen Drewitt Changed
 *       return type of mkt method -- processJPAdjustment.
 *  26   SDS11     1.25        7/3/2007 4:13:22 PM    Ambereen Drewitt Removed
 *       the comment for the ProgressUtil code for manual.
 *       
 *  25   SDS11     1.24        7/3/2007 3:34:01 PM    Rajesh Anantharaj regular
 *        update
 *  24   SDS11     1.23        7/2/2007 12:57:21 PM   Ambereen Drewitt Added
 *       copyright text.
 *  23   SDS11     1.22        7/2/2007 11:07:31 AM   Ambereen Drewitt Added
 *       marketing engine process jp adjustment code.
 *  22   SDS11     1.21        6/28/2007 4:10:29 PM   Ambereen Drewitt Added
 *       exception handling code.
 *       
 *  21   SDS11     1.20        6/27/2007 12:58:29 PM  Ambereen Drewitt altered
 *       module name const.
 *  20   SDS11     1.19        6/27/2007 10:46:50 AM  Ambereen Drewitt Changed
 *       the name of General constants interface to IAppConstants.
 *  19   SDS11     1.18        6/25/2007 4:02:55 PM   Rajesh Anantharaj changed
 *        into new ServiceFactory
 *  18   SDS11     1.17        6/25/2007 3:18:38 PM   Rajesh Anantharaj added
 *       logging
 *  17   SDS11     1.16        6/22/2007 5:38:40 PM   Rajesh Anantharaj changed
 *        the processmanualJP method to return sequnce number
 *  16   SDS11     1.15        6/21/2007 7:30:01 PM   Rajesh Anantharaj added
 *       the getJackpotXmlInfo method
 *  15   SDS11     1.14        6/21/2007 2:39:22 PM   Ambereen Drewitt Made the
 *        instances private.
 *  14   SDS11     1.13        6/21/2007 12:06:00 PM  Ambereen Drewitt Added
 *       the getJackpotDetails webmethod and the progress indicator code.
 *  13   SDS11     1.12        6/5/2007 12:08:27 PM   Ambereen Drewitt Removed
 *       sysout and added log.
 *  12   SDS11     1.11        5/27/2007 5:26:45 PM   Rajesh Anantharaj added
 *       methods processManualJackpot and nonCardedBlindAttempts.
 *  11   SDS11     1.10        5/24/2007 7:21:49 PM   Rajesh Anantharaj added
 *       postexpress jp
 *  10   SDS11     1.9         5/18/2007 11:55:44 AM  Vijayraj Magenthrarajan
 *       Added new method getReprintJackpotSlipDetails. Vijayraj.
 *  9    SDS11     1.8         5/16/2007 4:48:20 PM   Vijayraj Magenthrarajan
 *       Added Progress Indicator. Vijayraj.
 *  8    SDS11     1.7         5/16/2007 3:21:17 PM   Rajesh Anantharaj used
 *       fetchService method of SDSServiceFactory instead of hardcoding the
 *       URL
 *  7    SDS11     1.6         5/15/2007 8:46:36 PM   Vijayraj Magenthrarajan
 *       JackpotEngineServiceException is handled for all the web methods.
 *       Vijayraj.
 *  6    SDS11     1.5         5/14/2007 4:52:51 PM   Ambereen Drewitt Removed
 *       unnecessary variable.
 *  5    SDS11     1.4         5/14/2007 4:31:50 PM   Ambereen Drewitt Added
 *       javadoc.
 *  4    SDS11     1.3         5/10/2007 1:12:05 AM   Rajesh Anantharaj regular
 *        update
 *  3    SDS11     1.2         5/9/2007 10:05:18 PM   Vijayraj Magenthrarajan
 *       Added getJackpotStatus and postJackpotStatus methods. Vijayraj.
 *  2    SDS11     1.1         5/9/2007 6:42:58 PM    Vijayraj Magenthrarajan
 *       calling webservice methods.
 *  1    SDS11     1.0         4/24/2007 8:59:30 PM   Ambereen Drewitt added
 *       Jackpotui project - Amber
 * $
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
package com.ballydev.sds.jackpotui.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
//
//import com.ballydev.sds.framework.LabelLoader;
//import com.ballydev.sds.framework.service.SDSServiceFactory;
//import com.ballydev.sds.framework.util.MessageDialogUtil;
//import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
//import com.ballydev.sds.jackpotengine.client.JackpotAssetInfoDTO;
//import com.ballydev.sds.jackpotengine.client.JackpotAssetParamType;
//import com.ballydev.sds.jackpotengine.client.JackpotDTO;
//import com.ballydev.sds.jackpotengine.client.JackpotEngine;
//import com.ballydev.sds.jackpotengine.client.JackpotEngineAlertObject;
//import com.ballydev.sds.jackpotengine.client.JackpotEngineService;
//import com.ballydev.sds.jackpotengine.client.JackpotEngineServiceException;
//import com.ballydev.sds.jackpotengine.client.JackpotEngineServiceException_Exception;
//import com.ballydev.sds.jackpotengine.client.JackpotFilter;
//import com.ballydev.sds.jackpotui.constants.IAppConstants;
//import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
//import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Class that contains all the webmethods and establishes connection with the Jackpot Engine Service
 * @author dambereen
 * @version $Revision: 1$
 */
public class ServiceCall {/*
	
	*//**
	 * JackpotEngineService Instance 
	 *//*
	private JackpotEngineService jackpotEngineService;	
	
	*//**
	 * JackpotEngine Instance that contains the port of the Jackpot Engine Service 
	 * and used to call the web methods of the Jackpot Engine Service
	 *//*
	private JackpotEngine jackpotEngine;
	
	*//**
	 * Logger Instance
	 *//*
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	*//**
	 * ServiceCall Instance
	 *//*
	private static ServiceCall service;
	
	*//**
	 * Class Constructor 
	 * Here the Jackpot Engine Service is got from the Server 
	 *//*
	private ServiceCall()throws Exception{
		try {
			SDSServiceFactory<JackpotEngineService> jackpotServiceFactory = new SDSServiceFactory<JackpotEngineService>();
			jackpotEngineService = jackpotServiceFactory.fetchService(IAppConstants.JACKPOT_SERVICE_NAME);
					
			*//** Gets the Jackpot Engine Port *//*
			jackpotEngine = jackpotEngineService.getJackpotEnginePort();
		}catch (InvocationTargetException e) {
			log.info("Not able to get the Jackpot service", e);
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SERVICE_DOWN));
			return;
		}
		catch (Exception e) {
			log.info("Not able to get the Jackpot service", e);
			return;
		}
	}	
	
	*//**
	 * Method to get the Service Call instance
	 * @return the service instance
	 *//*
	public static ServiceCall getInstance()throws Exception{
		try {
			if(service == null){
				ProgressIndicatorUtil.openInProgressWindow();
				service = new ServiceCall();
				ProgressIndicatorUtil.closeInProgressWindow();
			}
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return service;
	}	
	
	*//**
	 * Returns the status flag description for a sequence number entered
	 * @param sequenceNumber
	 * @return the status
	 * @throws JackpotEngineServiceException_Exception 
	 *//*
	public String getJackpotStatus(long sequenceNumber, int siteId) throws JackpotEngineServiceException_Exception
	{
		String statusDescription =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			statusDescription = jackpotEngine.getJackpotStatus(sequenceNumber, siteId);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return statusDescription;
	}
	
		
	*//**
	 * This method is to update the status of a particular jackpot
	 * @param sequenceNumber
	 * @return true,  if the status is updated successfully
	 *         false, if the status updation fails
	 * @throws JackpotEngineServiceException_Exception 
	 *//*
	public JackpotDTO postJackpotStatus(JackpotDTO jackpotDT0) throws JackpotEngineServiceException_Exception
	{		
		JackpotDTO jackpotDTO=null;
		//boolean status =false;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			jackpotDTO = jackpotEngine.postJackpotStatus(jackpotDT0);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return jackpotDTO;
	}
	

	*//**
	 * This method is to log the transaction for a reprint process.
	 * @param sequenceNumber
	 * @return true,  if the status is updated successfully
	 *         false, if the status updation fails
	 * @throws JackpotEngineServiceException_Exception 
	 *//*
	public JackpotDTO postJackpotReprint(JackpotDTO jackpotDT0) throws JackpotEngineServiceException_Exception
	{		
		JackpotDTO jackpotDTO=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			jackpotDTO = jackpotEngine.postJackpotReprint(jackpotDT0);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return jackpotDTO;
	}
		
	
	*//**
	 * Processes the jackpot details
	 * @param jackpotDTO
	 * @param siteId
	 * @return true- if processed
	 * 		   false- if not processed
	 * @throws JackpotEngineServiceException_Exception 
	 *//*
	public JackpotDTO processJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException_Exception
	{
		JackpotDTO processedJackpotDTO=null;
		//boolean response=false;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			 processedJackpotDTO= jackpotEngine.processJackpot(jackpotDTO);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return processedJackpotDTO;
	}	
	
	
	*//**
	 * Processes the manual jackpot details
	 * @param jackpotDTO
	 * @param siteId
	 * @return true- if processed
	 * 		   false- if not processed
	 * @throws JackpotEngineServiceException_Exception 
	 *//*
	public JackpotDTO processManualJackpot(JackpotDTO jackpotDTO, JackpotFilter filter) throws JackpotEngineServiceException_Exception
	{
		JackpotDTO processedMJPDTO=null;
		//long sequenceNumber;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			processedMJPDTO = jackpotEngine.processManualJackpot(jackpotDTO, filter);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return processedMJPDTO;
	}	
	
	
	*//**
	 * Method to get the reprint jackpot slip details
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber, int siteId) throws JackpotEngineServiceException_Exception
	{
		JackpotDTO reprintJackpotSlipDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			reprintJackpotSlipDTO =  jackpotEngine.getReprintJackpotSlipDetails(sequenceNumber, siteId);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return reprintJackpotSlipDTO;
	}
	
	
	*//**
	 * Method to get the void jackpot slip details
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	*//**
	 * Method to get the reprint jackpot slip details
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId) throws JackpotEngineServiceException_Exception
	{
		JackpotDTO reprintJackpotSlipDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			reprintJackpotSlipDTO =  jackpotEngine.getVoidJackpotSlipDetails(sequenceNumber, siteId);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return reprintJackpotSlipDTO;
	}
	
	
	*//**
	 * Method to get the express jackpot blind attempt
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public short getBlindAttemptsCount(long sequenceNumber) throws JackpotEngineServiceException_Exception
	{
		short blindAttemptCount =0 ;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			blindAttemptCount = jackpotEngine.getExpressJackpotBlindAttempts(sequenceNumber);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return blindAttemptCount;
	}
	
	
	*//**
	 * Method to update the blind attempt as 2 for a carded express jackpot.
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public boolean postBlindAttempst(long sequenceNumber) throws JackpotEngineServiceException_Exception
	{
		boolean success=false;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			success = jackpotEngine.postExpressJackpotBlindAttempts(sequenceNumber);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}		
		return success;
	}	
	
	
	*//**
	 * Method to update the blind attempt as 2 for a non carded express jackpot.
	 * @param sequenceNumber
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public boolean postNonCardedExpressJPBlindAttempt(long sequenceNumber) throws JackpotEngineServiceException_Exception
	{
		ProgressIndicatorUtil.openInProgressWindow();
		boolean success = false;
		try{
			success = jackpotEngine.postNonCardedExpressJackpotBlindAttempts(sequenceNumber);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return success;
	}	
	
	
	*//**
	 * Method to get the pending jackpot slips based on slot number or  
	 * stand number or sequence number
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public List<Object> getJackpotDetails(JackpotFilter filter)throws JackpotEngineServiceException_Exception
	{		
		List<Object> jackpotDTOList = null; 
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			jackpotDTOList=jackpotEngine.getJackpotDetails(filter);	
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotDTOList;
	}	
	
	
	*//**
	 * Method to retrieve all the pending jackpot slip details
	 * @return List of JackpotDTOs
	 * @throws JackpotEngineServiceException
	 *//*
	public List<Object> getAllPendingJackpotSlipDetails(int siteId) throws JackpotEngineServiceException_Exception 
	{	
		List<Object> objList=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objList=jackpotEngine.getAllPendingJackpotSlipDetails(siteId);	
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objList;
	}
	
	*//**
	 * Method to get the Slip schema XML in String format.
	 * @return
	 * @throws IOException_Exception
	 *//*
	public List<String> getJackpotXMLInfo() throws JackpotEngineServiceException_Exception
	{
		ProgressIndicatorUtil.openInProgressWindow();
		List<String> listOfXMLFiles=null;
		try{
			listOfXMLFiles = jackpotEngine.getJackpotXMLInfo();
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return listOfXMLFiles;
	}	
	
	*//**
	 * Method to update the original card no, new card no, new jackpot amount, acnf_id and the event time in the Marketing Engine
	 * when the jackpot orginal amount is altered.
	 * @param origCardNo
	 * @param newCardNo
	 * @param oldAmount
	 * @param newJackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public int processJackpotAdjustment(int siteId, String origCardNo, String newCardNo,long oldAmount, long newJackpotAmount, String slotNo, long msgiD) throws JackpotEngineServiceException_Exception 
	{	log.debug("Before PROCESS.JACKPOT.ADJUSTMENT is called.. ");
		int response=0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			response = jackpotEngine.processJackpotAdjustment(siteId, origCardNo, newCardNo,oldAmount, newJackpotAmount, slotNo, msgiD);
		}
		catch (JackpotEngineServiceException_Exception exception) {
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		log.debug("After processJackpotAdjustment Method is called in REMOTE");
		return response;
	}	
	
	
	*//**
	 * Method to update the player card no, jackpot amount, acnf_id and msg id in the Marketing Engine
	 * for a Manual Jackpot
	 * @param playerCardNumber
	 * @param jackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public int postManualJackpot(int siteId, String playerCardNumber,long jackpotAmount, String slotNo, long msgiD) throws JackpotEngineServiceException_Exception 
	{	log.debug("Before PROCESS.MANUAL.JACKPOT is called.. ");
		int response=0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			response = jackpotEngine.postManualJackpot(siteId, playerCardNumber,jackpotAmount,slotNo,msgiD);
		}
		catch (JackpotEngineServiceException_Exception exception) {
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		log.debug("After processJackpotAdjustment Method is called in REMOTE");
		return response;
	}	
		
	
	*//**
	 * Web method that returns the asset details based on the type of Object and JackpotAssetParamType 
	 * (ASSET_CONFIG_LOCATION, ASSET_CONFIG_NUMBER or ASSET_CONFIG_ID) thats passed
	 * @param object
	 * @param jackpotAssetParamType
	 * @return JackpotAssetInfoDTO
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public JackpotAssetInfoDTO getJackpotAssetInfo(Object object, JackpotAssetParamType jackpotAssetParamType)throws JackpotEngineServiceException_Exception 
	{
		ProgressIndicatorUtil.openInProgressWindow();
		JackpotAssetInfoDTO jackpotAssetInfoDTO =null;
		try {	
			jackpotAssetInfoDTO = jackpotEngine.getJackpotAssetInfo(object, jackpotAssetParamType);			
		} 
		catch (JackpotEngineServiceException_Exception exception) {
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotAssetInfoDTO;
	}
	
	
	*//**
	 * This method returns all the site config details for jackpot
	 * @return List<Object>
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public List<Object> getJPSiteConfigDetails()throws JackpotEngineServiceException_Exception
	{
		List<Object> list=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			MainMenuController.jackpotForm.getSiteId();
			list = jackpotEngine.getJackpotConfigDetails("SITE.JACKPOT.PARAMETERS",MainMenuController.jackpotForm.getSiteId());
		} catch (JackpotEngineServiceException_Exception exception) {
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return list;
	}
	
	
	*//**
	 * Method to retrieve pending jackpot slip details occured after the specified number of minutes
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public List<Object> getJackpotSlipsforLastXXMinutes(JackpotFilter filter)
	throws JackpotEngineServiceException_Exception {
		List<Object> objList=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objList=jackpotEngine.getJackpotSlipsforLastXXMinutes(filter);	
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objList;		
	}
		
	
	*//**
	 * Method to send alerts to the AlertsEngine 
	 * @param jpAlertObj
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public void sendAlert(JackpotEngineAlertObject jpAlertObj)throws JackpotEngineServiceException_Exception
	{
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			jackpotEngine.sendAlert(jpAlertObj);
		} catch (JackpotEngineServiceException_Exception exception) {
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}
	
	*//**
	 * Method to void all the pending jackpot slips based on the site id
	 * @param siteId
	 * @param voidEmpId
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public List<Object> voidAllPendingJackpotSlips(int siteId, String voidEmpId, String printreUsed) throws JackpotEngineServiceException_Exception 
	{	
		List<Object> objList=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objList=jackpotEngine.voidAllPendingJackpotSlips(siteId, voidEmpId, printreUsed);	
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objList;
	}
	
	
	
	*//**
	 * Method to void a list of pending jackpot slips for the slot number entered
	 * @param slotNo
	 * @param siteId
	 * @param voidEmpId
	 * @return List<Object>
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public List<Object> voidPendingJackpotSlipsForSlotNo(String slotNo, int siteId, String voidEmpId, String printreUsed) throws JackpotEngineServiceException_Exception 
	{	
		List<Object> objList=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objList=jackpotEngine.voidPendingJackpotSlipsForSlotNo(slotNo, siteId, voidEmpId, printreUsed);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objList;
	}
	
	
	*//**
	 * Method to get the Jackpot Details based on the site Id, from date and to date
	 * to print the Jackpot Slip Report 
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return objectDTO
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public Object getDetailsToPrintJpSlipReportForDate(int siteId, long fromDate, long toDate)  throws JackpotEngineServiceException_Exception
	{ 	
		Object objectDTO=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objectDTO=jackpotEngine.getDetailsToPrintJpSlipReportForDate(siteId, fromDate, toDate);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objectDTO;
	}
	
	
	*//**
	 * Method to get the Jackpot Details based on the site Id, employeeId, from date and to date
	 * to print the Jackpot Slip Report 
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public Object getDetailsToPrintJpSlipReportForDateEmployee(int siteId,String employeeId, long fromDate, long toDate)  throws JackpotEngineServiceException_Exception
	{ 	
		Object objectDTO=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objectDTO=jackpotEngine.getDetailsToPrintJpSlipReportForDateEmployee(siteId,employeeId, fromDate, toDate);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objectDTO;
	}
	
	*//**
	 * Method to get the details to reprint the PRIOR (P) slip (the latest processed slip)
	 * @param siteId
	 * @return 
	 * @throws JackpotEngineServiceException_Exception
	 *//*
	public Object getReprintPriorSlipDetails(int siteId)  throws JackpotEngineServiceException_Exception
	{ 	
		Object objectDTO=null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			objectDTO=jackpotEngine.getReprintPriorSlipDetails(siteId);
		}
		catch(JackpotEngineServiceException_Exception exception){
			throw(exception);
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return objectDTO;
	}
	
	
*/}
