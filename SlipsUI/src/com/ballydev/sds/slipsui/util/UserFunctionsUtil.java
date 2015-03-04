/*****************************************************************************
 * $Id: UserFunctionsUtil.java,v 1.3, 2009-11-16 10:41:15Z, Ambereen Drewitt$
 * $Date: 11/16/2009 4:41:15 AM$
/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui.util;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.dto.ParameterDTO;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.UserFunctionsConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;

/**
 * This class will is having the methods which return yes if a user function is
 * available for a particular actor.
 * 
 * @author anantharajr
 * @version $Revision: 4$
 */
public class UserFunctionsUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Method returns true if the user contains the Process Dispute function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isBeefAllowed(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.PROCESS_BEEF) != null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Method returns true if the user contains the Process Void function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isVoidAllowed(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.VOID_SLIP) != null) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * Method returns true if the user contains the Process Reprint function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isReprintAllowed(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.REPRINT_SLIP) != null) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * method returns the user function 
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isSupervisoryAuthority(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.SUPERVISORY_AUTHORITY_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * method returns the user parameter
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static int getOverrideAuthLevel(String userId, int siteId) 
	{
		HashMap hashMap = null;
		hashMap = getRoleParameters(userId, siteId);
		if(hashMap!=null){
			if (hashMap.get(UserFunctionsConstants.AUTH_LEVEL_USER_PARAMETER) != null) {
				return Integer.parseInt((String) hashMap.get(UserFunctionsConstants.AUTH_LEVEL_USER_PARAMETER));
			} else {
				return 0;
			}
		}else{
			return 0;
		}		
	}

	/**
	 * method returns the user functions
	 * @param userId
	 * @param siteId
	 * @return
	 */
	private static HashMap<String, String> getUserFunctions(String userId, int siteId) 
	{
		SessionUtility sessionUtility = new SessionUtility();
		HashMap<String, String> slipsUserFunctions = null;
		try {			
			List<FunctionDTO> functionList = sessionUtility.getUserDetails(userId, siteId).getFunctionList();
			slipsUserFunctions = new HashMap<String, String>();
			if(functionList!=null){
				for (FunctionDTO tempList : functionList) {
					slipsUserFunctions.put(tempList.getFunctionName(), tempList
							.getFunctionName());
				}
			}else{
				log.debug("Function list is null");
			}			
		} catch (Exception e) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception: ",e);
		}
		return slipsUserFunctions;
	}

	/**
	 * method returns the role parameter
	 * @param userId
	 * @param siteId
	 * @return
	 */
	private static HashMap getRoleParameters(String userId, int siteId) 
	{
		SessionUtility sessionUtility = new SessionUtility();
		HashMap<String, String> jackptotRoleFunctions = null;
		try {
			jackptotRoleFunctions = new HashMap<String, String>();
		
			//List<ParameterDTO> paramList = FrameworkServiceLocator.getService().getUserDetails(userId,siteId).getParameterList();
			List<ParameterDTO> paramList = sessionUtility.getUserDetails(userId,siteId).getParameterList();
			if(paramList!=null){
				for(ParameterDTO tempList:paramList)
				{
					jackptotRoleFunctions.put(tempList.getParameterName(),tempList.getParameterValue());
					log.info("Param Name : "+tempList.getParameterName()+" for user "+userId);
					log.info("Param Value : "+tempList.getParameterValue()+" for user "+userId);
				}
			}else{
				log.debug("Parameter list is null");
			}					
		} catch (Exception e) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception: ",e);
		}
		return jackptotRoleFunctions;
	}
}
