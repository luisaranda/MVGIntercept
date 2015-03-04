/*****************************************************************************
 * $Id: UserFunctionsUtil.java,v 1.7, 2011-03-03 15:48:44Z, Subha Viswanathan$
 * $Date: 3/3/2011 9:48:44 AM$
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
package com.ballydev.sds.jackpotui.util;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.dto.ParameterDTO;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.UserFunctionsConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;

/**
 * This class will is having the methods which return yes if a user function is
 * available for a particular actor.
 * 
 * @author anantharajr
 * @version $Revision: 8$
 */
public class UserFunctionsUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Method that returns true if the user contains the Process Pending Jp Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isPendingJackpotAllowed(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.PROCESS_PENDING_JACKPOT_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
		
	/**
	 * Method that returns true if the user contains the Process Manual Jp Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isManualJackpotAllowed(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.PROCESS_MANUAL_JACKPOT_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
		
	/**
	 * Method that returns true if the user contains the Process Void Jp Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isVoidAllowed(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.VOID_JACKPOT_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Method that returns true if the user contains the Process Reprint Jp Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isReprintAllowed(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.REPRINT_JACKPOT_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Method that returns true if the user contains the Process Display Jp Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static boolean isDisplayAllowed(String userId, int siteId) {
		HashMap<String, String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.DISPLAY_JACKPOT_FUNCTION) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method that returns yes if the user contains the Jackpot - Supervisory Authority Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static String isSupervisoryAuthority(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.SUPERVISORY_AUTHORITY_FUNCTION) != null) {
			return "yes";
		} else {
			return "no";
		}

	}

	
	/**
	 * Method that returns yes if the user contains the Jackpot - Jackpot One Person Auth Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static String isOnePersonAllowedToAuthorize(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.JACKPOT_ONE_PERSON_AUTH) != null) {
			return "yes";
		} else {
			return "no";
		}

	}
	
	/**
	 * Method that returns yes if the user contains the Jackpot - Jackpot Override Function
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static String isJackpotOverrideAuthority(String userId, int siteId) {
		HashMap<String,String> hashMap = null;
		hashMap = getUserFunctions(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.JACKPOT_OVERRIDE_FUNCTION) != null) {
			return "yes";
		} else {
			return "no";
		}
	}

	/**
	 * method returns the user parameter
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public static double getJackpotAmount(String userId, int siteId) {
		HashMap hashMap = null;
		hashMap = getRoleParameters(userId, siteId);
		if (hashMap.get(UserFunctionsConstants.JACKPOT_AMOUNT_USER_PARAMETER) != null) {
			return Double.parseDouble(
					CurrencyUtil.getStrUnformattedAmt((String) hashMap
					.get(UserFunctionsConstants.JACKPOT_AMOUNT_USER_PARAMETER), 2)
					);
		} else {
			return 0;
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
		HashMap<String, String> jackptotUserFunctions = null;
		try {
			List<FunctionDTO> functionList = sessionUtility.getUserDetails(userId, siteId).getFunctionList();
			jackptotUserFunctions = new HashMap<String, String>();
			if(functionList!=null){
				for (FunctionDTO tempList : functionList) {
					jackptotUserFunctions.put(tempList.getFunctionName(), tempList
							.getFunctionName());
				}
			}else{
				log.debug("Function list is null");
			}			
		} catch (Exception e) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception: ",e);
		}
		return jackptotUserFunctions;
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
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception: ",e);
		}
		return jackptotRoleFunctions;
	}
}
