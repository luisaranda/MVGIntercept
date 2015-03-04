/*****************************************************************************
 * $Id: VoucherUIValidator.java,v 1.17, 2010-06-16 14:57:33Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:57:33 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.util;


import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.util.ValidatorUtils;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.form.OverrideForm;
import com.ballydev.sds.voucherui.form.PrintVoucherForm;
import com.ballydev.sds.voucherui.form.RedeemVoucherForm;

/**
 * This utility class is used to provide validations at the plug-in level.
 * 
 */
public class VoucherUIValidator {


	public static boolean isValidBarcodeLength(Object object, Field field) {
		
		RedeemVoucherForm form = (RedeemVoucherForm)object;
		if( form.getBarCode() == null || form.getBarCode().length() != IVoucherConstants.BARCODE_LENGTH ){
			return false;
		}
		return true;
	}

	public static boolean isValidBarcodeLength_O(Object object, Field field) {
		
		OverrideForm form = (OverrideForm)object;
		if( form.getBarCode() == null || form.getBarCode().length() != IVoucherConstants.BARCODE_LENGTH ){
			return false;
		}
		return true;
	}

	public static boolean isValidBarcodeCheckDigit(Object object, Field field) {
		RedeemVoucherForm form = (RedeemVoucherForm)object;
		try {
			if( TicketBarcodeAndCrcHelper.verifyBarcodeCheckDigit(form.getBarCode()) ){
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validatePositiveNumber(Object bean, Field field)
	throws Exception {
		String value = getValue(bean, field);
		if (!(Util.isEmpty(value))){
			String pattern = "[0-9.]+";
			return value.matches(pattern);
		}
		else{
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
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
	 * This method checks if the given date is greater than the current date
	 * @param bean
	 * @param field
	 * @return True if current date is greater, false otherwise
	 */
	public static boolean isTimeLessThanCurrentTime(Object bean, Field field) {
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		boolean validate = false;
		try {
			Timestamp datetime = DateUtil.getTimeStamp(value, "EEE MMM dd HH:mm:ss Z yyyy");
			@SuppressWarnings("unused")
			Timestamp localDateTime = DateUtil.getLocalTimeFromUTC(datetime);
			//if (System.currentTimeMillis()>=localDateTime.getTime()) {
			if( DateUtil.getCurrentServerDate().getTime() >= datetime.getTime() ) {
				validate = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validate;
	}
	/**
	 * This method checks if the given date is greater than the current date
	 * @param bean
	 * @param field
	 * @return True if current date is greater, false otherwise
	 */
	public static boolean isTimeGreaterThanCurrentTime(Object bean, Field field) {
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		boolean validate = false;
		try {
			Timestamp datetime = DateUtil.getTimeStamp(value, "EEE MMM dd HH:mm:ss Z yyyy");
			Timestamp localDateTime = DateUtil.getLocalTimeFromUTC(datetime);
			if( DateUtil.getCurrentServerDate().getTime() <= localDateTime.getTime() ) {
				validate = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validate;
	}

	public static boolean isValidBarcodeCrc(Object object, Field field) {
		RedeemVoucherForm form = (RedeemVoucherForm)object;
		try {
			if( TicketBarcodeAndCrcHelper.verifyBarcodeCrc(form.getBarCode(), new Long(form.getAmount()), form.getTicketSeed().getBytes()) ){
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}	
	/**
	 * This method checks whether the passed number is in the specified size
	 * @param number
	 * @param size
	 * @return
	 */
	public static boolean checkFieldSize(String value, int size){
		boolean response = false;
		if( value.trim().length() != 0 ) {
			if( value.trim().length() == size ){
				response = true;
			}
		} else {
			response = true;
		}
		return response;

	}

	/**
	 * This method checks whether the passed number is in the specified size
	 * @param number
	 * @param size
	 * @return
	 */
	public static boolean checkFieldSize(Object bean, Field field){
		String number = ValidatorUtils.getValueAsString(bean, field.getProperty());
		String size = field.getVarValue("size");
		boolean response = false;
		if( number.trim().length() != 0 ) {
			if( number.trim().length() == (Integer.parseInt(size)) ) {
				response = true;
			}
		} else {
			response = true;
		}
		return response;
	}


	public static boolean isValidCasinoId(String barcodeInput) {
		Barcode barcode = new Barcode(barcodeInput);
		if( barcode.getBarcodeProperty(Barcode.BARCODE_ELEMENT_PROPERTY_IDENTIFIER).equals(getConfiguredCasinoId()) ){
			return true;
		}
		return false;
	}

	public static boolean isValidBarcodeLength(String barcodeInput) {
		if( barcodeInput == null || barcodeInput.length() != IVoucherConstants.BARCODE_LENGTH ){
			return false;
		}
		return true;
	}

	public static boolean isValidCasinoId(Object object, Field field) {
		String value = ValidatorUtils.getValueAsString(object, field.getProperty());
		Barcode barcode = new Barcode(value);
		if( barcode.getBarcodeProperty(Barcode.BARCODE_ELEMENT_PROPERTY_IDENTIFIER).equals(getConfiguredCasinoId()) ){
			return true;
		}
		return false;
	}

	public static boolean isValidPlayerCard(Object object, Field field){
		RedeemVoucherForm form = (RedeemVoucherForm)object;
		if( form.isPlayerCardRequired() && 
				!form.getPlayerId().equals(form.getCreatedPlayerCardId())){
			return false;
		}
		return true;
	}
	
	public static boolean isValidTicketAmount(Object object, Field field) {
		PrintVoucherForm form = (PrintVoucherForm)object;
		if( Double.parseDouble(form.getTicketAmount()) > 0 ) {
			return true;
		}
		else
			return false;
	}

	public static String getConfiguredCasinoId(){
		String casinoID = Integer.toString(SDSApplication.getSiteDetails().getId());
		for(int i = 0; i < 3; i++ ){
			if( casinoID.length() < 3 ) {
				casinoID = "0" + casinoID;
			}
		}
		return casinoID;
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateMinLength(String value,String varValue) throws Exception {
		if( Util.isEmpty(varValue) ) {
			throw new ValidatorException("var-name \"min_len\" not specified");
		}
		if( !(Util.isEmpty(value)) ) {
			int minLength = Integer.parseInt(varValue);
			return (value.length() < minLength) ? false : true;
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateMaxLength(String value,String varValue) throws Exception {
		if( Util.isEmpty(varValue) ) {
			throw new ValidatorException("var-name \"max_len\" not specified");
		}
		if( !(Util.isEmpty(value)) ) {
			int maxLength = Integer.parseInt(varValue);
			return (value.length() > maxLength) ? false : true;
		}
		else {
			return true;
		}
	}

	/**
	 * Checks if the field is required.
	 * 
	 * @param bean
	 * @param field
	 * @return
	 */
	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateRequired(String value) throws Exception {
		return !GenericValidator.isBlankOrNull(value);
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateNumericWithSplChar(Object object, Field field) throws Exception {
		String value = ValidatorUtils.getValueAsString(object, field.getProperty());
		if( !(Util.isEmpty(value)) ) {
			String pattern = "[0-9*]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateNumeric(String value)	throws Exception {		
		if( !(Util.isEmpty(value)) ) {
			String pattern = "[0-9]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}
	
	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateAlphaNumericWithSpaces(String value) throws Exception {
		if( !(Util.isEmpty(value)) ) {
			String pattern = "[0-9a-zA-Z ]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateAlphaNumeric(String value) throws Exception {		
		if( !(Util.isEmpty(value)) ) {
			String pattern = "[0-9a-zA-Z]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateAlphabetic(String value) throws Exception {		
		if ( !(Util.isEmpty(value)) ) {
			String pattern = "[A-Za-z]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static boolean validateAlphabeticWithSpaces(String value) throws Exception {		
		if( !(Util.isEmpty(value)) ) {
			String pattern = "[A-Za-z ]+";
			return value.matches(pattern);
		}
		else {
			return true;
		}
	}

	/**
	 * This method validates the entered barcode
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	public static String validateEnteredBarcode(String barcode) throws Exception {
		String errorMsg ="";
		if( !validateRequired(barcode) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_FIELD_REQD);
		}if( !validateNumeric(barcode)){
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_CONTAIN_ONLY_NUMERIC);
		}
		if( !checkFieldSize(barcode,18) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_TKT_SIZE);
		}
		if( !isValidBarcodeLength(barcode) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_TKT_SIZE);
		}
		return errorMsg;
	}


	/**
	 * This method validates the entered player card id
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	public static boolean validateEnteredPlayerId1(Object object, Field field) throws Exception{
		String value = ValidatorUtils.getValueAsString(object, field.getProperty());
		boolean errorMsg = true;
		if( !validateNumeric(value) ) {
			errorMsg = false;
		} else if( !checkFieldSize(value,10) ) {
			errorMsg = false;
		} else if( checkZeroPlayerId(value) ) {
			errorMsg = false;
		}
		return errorMsg;
	}

	/**
	 * This method validates the entered player card id
	 * @param barcode
	 * @return
	 * @throws Exception
	 */
	public static String validateEnteredPlayerId(String playerId) throws Exception{
		String errorMsg = "";
		if( !validateRequired(playerId) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.PLAYER_ID_FIELD_REQD);
		} else if( !validateNumeric(playerId) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.PLAYER_ID_CONTAIN_ONLY_NUMERIC);
		} else if( !checkFieldSize(playerId,10) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.INVALID_PLAYER_ID_SIZE);
		} else if( checkZeroPlayerId(playerId) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.ZERO_PLAYER_ID);
		}
		return errorMsg;
	}

	private static boolean checkZeroPlayerId(String playerId) {
		if( playerId.equalsIgnoreCase(IVoucherConstants.PLAYER_CARD_ZERO) ) {
			return true;
		}
		return false;
	}

	/**
	 * This method validated the entered employee id
	 * @param employeeId
	 * @return
	 * @throws Exception
	 */
	public static String validateEmployeeId(String employeeId) throws Exception{
		String errorMsg = "";		
		if( !validateRequired(employeeId) ) {
			errorMsg = LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID) +LabelLoader.getLabelValue(IDBLabelKeyConstants.EMP_FIELD_REQD);
		}
		if(!validateNumeric(employeeId)){
			errorMsg = LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID) + LabelLoader.getLabelValue(IDBLabelKeyConstants.EMP_CONTAIN_ONLY_NUMERIC);
		}
		return errorMsg;
	}

	/**
	 * This method validated the entered asset 
	 * @param employeeId
	 * @return
	 * @throws Exception
	 */
	public static String validateAsset(String assetNumber) throws Exception{
		String errorMsg = "";
		if(!validateRequired(assetNumber) ) {
			errorMsg = LabelLoader.getLabelValue(IDBLabelKeyConstants.ASSET_FIELD_REQD);
		} 
		if( !validateAlphaNumericWithSpaces(assetNumber) ) {
			errorMsg= LabelLoader.getLabelValue(IDBLabelKeyConstants.ASSET_FIELD_ONLY_ALPHA_NUMERIC);
		}
		return errorMsg;
	}
}
