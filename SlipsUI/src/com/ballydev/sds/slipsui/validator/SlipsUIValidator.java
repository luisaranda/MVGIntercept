/*****************************************************************************
 * $Id: SlipsUIValidator.java,v 1.3, 2010-09-20 10:49:38Z, Ambereen Drewitt$
 * $Date: 9/20/2010 5:49:38 AM$
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
package com.ballydev.sds.slipsui.validator;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;

import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * This utility class is used to provide validations at the plugin level
 * 
 */
public class SlipsUIValidator {

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
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
	 */
	public static boolean validateAccountNumberMismatch(Object bean, Field field) {
		String accountNumber = ValidatorUtils.getValueAsString(bean, "accountNo");
		String confirmAccountNumber = ValidatorUtils.getValueAsString(bean, field.getProperty());
		if(Double.parseDouble(accountNumber) != Double.parseDouble(confirmAccountNumber)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to check if the field contains a zero value
	 * 
	 * @param bean
	 * @param field
	 * @return
	 */
	public static boolean validateNonZeroValue(Object bean, Field field) {
		String fieldValue = ValidatorUtils.getValueAsString(bean, field.getProperty());
		if(Double.parseDouble(fieldValue) == 0) {
			return false;
		}
		return true;
	}
	
}
