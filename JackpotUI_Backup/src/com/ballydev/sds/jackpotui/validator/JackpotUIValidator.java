/*****************************************************************************
 * $Id: JackpotUIValidator.java,v 1.13.1.1, 2011-05-17 20:01:53Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 3:01:53 PM$
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
package com.ballydev.sds.jackpotui.validator;

import java.util.Map;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;

import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * This utility class is used to provide validations at the plugin level
 * @author anantharajr
 * @version $Revision: 16$
 */
public class JackpotUIValidator {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * This method checks for the length of the employeeId
	 * 
	 * @param employeeId
	 * @return
	 */
	public boolean checkEmployeeId(Object object, Field field) {
		String value = ValidatorUtils.getValueAsString(object, field
				.getProperty());
		if (Util.isEmpty(value)) {
			return true;
		}
		int len = value.length();
		if (len == 10) {
			log.debug("validation of employee Id. is passed");
			return true;
		}
		log.debug("validation of employee Id is failed");
		return false;

	}
		
	/**
	 * Method to validate fields that contain numeric values with commas and spaces
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateNumericSpacesDoubleCommas(Object bean, Field field)
	throws Exception {
		String value = getValue(bean, field);
		if (!(Util.isEmpty(value))){
			String pattern = "[0-9,]+";
			boolean doubleCommas = true;
			
			if(value.contains(",,")){
				doubleCommas = false;
				return doubleCommas;
			}else if(value.endsWith(",")){
				doubleCommas = false;
				return doubleCommas;
			}else if(value.startsWith(",")){
				doubleCommas = false;
				return doubleCommas;
			}else{				
				return value.matches(pattern);
			}				
		}	
		else{
			return true;
		}
	}
	
	/**
	 * Method to validate fields that contain duplicate numeric values with commas
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean  validateDuplicateValuesBetweenCommas(Object bean, Field field)throws Exception
	{
		boolean returnValue = true;
		String value = getValue(bean, field);
		
		if (!(Util.isEmpty(value)))
		{
			String[] stringArray =value.split(",");
			for(int i=0;i< stringArray.length ; i++)
			{ 
				char[] chars = stringArray[i].toCharArray();
				int index = 0;
				for (; index < stringArray[i].length(); index++) 
				{       
					if (chars[index] != '0') 
					{            
						break;       
					}    
				}    
				stringArray[i]= (index == 0) ? stringArray[i] : stringArray[i].substring(index);
			}
			for(int i=0;i< stringArray.length ; i++)
			{	
				for(int j=i+1;j< stringArray.length ;j++)
				{
					if(stringArray[i].trim().equalsIgnoreCase(stringArray[j].trim()))
					{
						return returnValue = false;
					}
				}
			}
		}else{
			return returnValue;
		}
		return returnValue;
	}
	
	/**
	 * Method to validate max value between the commas to be 999
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public boolean validateMinMaxValuesBetweenCommas(Object bean, Field field) throws Exception {
		boolean returnValue = true;
		String value = getValue(bean, field);
		
		if (!(Util.isEmpty(value))) {
			String[] stringArray = value.split(",");
			for (int i = 0; i < stringArray.length; i++) {
				char[] chars = stringArray[i].toCharArray();
				int index = 0;
				for (; index < stringArray[i].length(); index++) {
					if (chars[index] != '0') {
						break;
					}
				}
				stringArray[i] = (index == 0) ? stringArray[i] : stringArray[i].substring(index);
			}
			for (int i = 0; i < stringArray.length; i++) {
				if (stringArray[i] != null && !stringArray[i].equalsIgnoreCase("")
						&& (Long.parseLong(stringArray[i]) > 999 || Long.parseLong(stringArray[i]) == 0)) {
					return returnValue = false;
				} else if (stringArray[i] != null && stringArray[i].equalsIgnoreCase("")) {
					return returnValue = false;
				}
			}
		} else {
			return returnValue;
		}
		return returnValue;
	}
	
	/**
	 * Method to validate max value between the commas to be 46
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 * @author vsubha
	 */
	public boolean validateMinMaxValuesBetweenCommasForProgLevel(Object bean, Field field) throws Exception {
		boolean returnValue = true;
		String value = getValue(bean, field);
		
		if (!(Util.isEmpty(value))) {
			String[] stringArray = value.split(",");
			for (int i = 0; i < stringArray.length; i++) {
				char[] chars = stringArray[i].toCharArray();
				int index = 0;
				for (; index < stringArray[i].length(); index++) {
					if (chars[index] != '0') {
						break;
					}
				}
				stringArray[i] = (index == 0) ? stringArray[i] : stringArray[i].substring(index);
			}
			for (int i = 0; i < stringArray.length; i++) {
				if (stringArray[i] != null && !stringArray[i].equalsIgnoreCase("")
						&& (Long.parseLong(stringArray[i]) > 46 || Long.parseLong(stringArray[i]) == 0)) {
					return returnValue = false;
				} else if (stringArray[i] != null && stringArray[i].equalsIgnoreCase("")) {
					return returnValue = false;
				}
			}
		} else {
			return returnValue;
		}
		return returnValue;
	}
	
	
	/**
	 * Method to get the value in the form object
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static String getValue(Object bean, Field field) throws Exception {
		String value;
		if (bean instanceof Map) {
			Map map = (Map) bean;
			value = (String) map.get(field.getProperty());
		} else {
			value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		}
		return value;
	}
		
	/**
	 * Method to validate spaces for a password field 
	 * @param bean
	 * @param field
	 * @return
	 */
	public static boolean validateSpaces(Object bean,Field field) 
	{
		String password = ValidatorUtils.getValueAsString(bean, field.getProperty());

		if (password!=null && password.contains(" ")){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Method to check if the account numbers are matching
	 * 
	 * @param bean
	 * @param field
	 * @return
	 * @author vsubha
	 */
	public static boolean validateAccountNumbers(Object bean, Field field) {
		String accountNumber = ValidatorUtils.getValueAsString(bean, "accountNumber");
		String confirmAccountNumber = ValidatorUtils.getValueAsString(bean, field.getProperty());
		if(accountNumber!=null && accountNumber.trim().length()>0
				&& confirmAccountNumber!=null && confirmAccountNumber.trim().length()>0
				&& (Double.parseDouble(accountNumber) != Double.parseDouble(confirmAccountNumber))) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to validate if the amount field contains the MASKED STRING
	 * @param bean
	 * @param field
	 * @return  boolean
	 * @author dambereen
	 */
	public static boolean maskedDataEnterAmountRequired(Object bean, Field field) {
		String maskedAmtField = ValidatorUtils.getValueAsString(bean, field.getProperty());

		if (maskedAmtField!=null && maskedAmtField.contains(IAppConstants.MASKED_AMOUNT_STRING)){
			return false;
		}
		return true;		
	}
	
	/**
	 * Method to validate if the accountNumber field has been entered without entering the confirm acc no field
	 * @param bean
	 * @param field
	 * @return boolean
	 * @author dambereen
	 */
	public static boolean confirmAccountNumberRequired(Object bean, Field field) {
		String accountNumber = ValidatorUtils.getValueAsString(bean, "accountNumber");
		String confirmAccountNumber = ValidatorUtils.getValueAsString(bean, field.getProperty());
		if(accountNumber!=null && accountNumber.trim().length()>0 
				&& confirmAccountNumber!=null && confirmAccountNumber.trim().length()==0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to validate if the confirmAccountNumber field has been entered without entering the acc no field
	 * @param bean
	 * @param field
	 * @return boolean
	 * @author dambereen
	 */
	public static boolean accountNumberRequired(Object bean, Field field) {
		String confirmAccountNumber = ValidatorUtils.getValueAsString(bean, "confirmAccountNumber");
		String accountNumber = ValidatorUtils.getValueAsString(bean, field.getProperty());
		if(confirmAccountNumber!=null && confirmAccountNumber.trim().length()>0 
				&& accountNumber!=null && accountNumber.trim().length()==0) {
			return false;
		}
		return true;
	}
	
}
