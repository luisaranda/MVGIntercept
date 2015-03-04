/*****************************************************************************
 * $Id: VoucherUtil.java,v 1.13, 2010-12-07 10:00:46Z, Verma, Nitin Kumar$
 * $Date: 12/7/2010 4:00:46 AM$
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ITSConstants;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.dto.ParameterDTO;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.voucher.dto.BaseDTO;
import com.ballydev.sds.voucher.dto.FunctionInfoDTO;
import com.ballydev.sds.voucher.dto.ParameterInfoDTO;
import com.ballydev.sds.voucher.dto.UserInfoDTO;
import com.ballydev.sds.voucher.enumconstants.TicketTypeEnum;
import com.ballydev.sds.voucher.enumconstants.UserRoleEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucher.util.Message;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;


public class VoucherUtil 
{

	private static int count = 0;
	private static String brCode = null;

	public static Color getDefaultBackGroudColor()
	{
		return new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B);
	}

	public static Color getHPDefaultBackGroudColor()
	{
		return new Color(Display.getCurrent(),255,255,255);
	}

	public static String getIdFromEnumName(String enumName){
		int enumIndex = enumName!=null?enumName.indexOf("_"):-1;
		if( enumIndex != -1 ){
			return enumName.substring(enumIndex+1,enumName.length());
		}
		return null;
	}

	public static String getStringFromEnumName(String enumName){
		int enumIndex = enumName!=null?enumName.indexOf("_"):-1;
		if( enumIndex != -1 ){
			return enumName.substring(0,enumIndex);
		}
		return null;
	}

	public static void main1(String[] args) {
		System.out.println(getStringFromEnumName("CASHIER_2134"));
		System.out.println(getIdFromEnumName("CASHIER_2134"));
	}

	public static String getI18nMessageForDisplay(BaseDTO baseForm)
	{
		String UNKNOWN_MESSAGE_KEY = "";
		StringBuilder i18nMessage =new StringBuilder("");
		if(baseForm!=null){
			Message[] messages = baseForm.getMessages();
			String[] msgParams =null;

			if(messages!=null && messages.length!=0)
			{
				String msgFromDB = null;
				for (Message message : messages) 
				{
					msgFromDB = LabelLoader.getLabelValue(message.getMessageKey(), new String[] {AppContextValues.getInstance().getTicketText()});
					if(msgFromDB!=null && !"".equalsIgnoreCase(msgFromDB))
					{
						msgParams = message.getMsgParameters(); 
						if(msgParams!=null && msgParams.length!=0)
						{
							MessageFormat format = new MessageFormat(escape(msgFromDB));
							msgFromDB = format.format(msgParams);
						}

						i18nMessage.append(msgFromDB).append("\n");
					}
				}
				i18nMessage.deleteCharAt(i18nMessage.length()-1);
				if("".equalsIgnoreCase(i18nMessage.toString().trim()))
				{
					i18nMessage.append(messages[0].getMessageKey());
				}
			}
			else
			{
				i18nMessage.append(UNKNOWN_MESSAGE_KEY);
			}
		}
		return i18nMessage.toString();
	}

	public static String getExMessageForDisplay(VoucherEngineServiceException serviceException)
	{
		String UNKNOWN_MESSAGE_KEY = "";
		StringBuilder i18nMessage =new StringBuilder("");
		List<Message> messages = serviceException.getErrorMessages();
		String[] msgParams =null;

		if(messages!=null && messages.size()!=0)
		{
			String msgFromDB = null;
			for (Message message : messages) 
			{
				//Change this to allow parameters that come from the Engine to be used
				//msgFromDB = LabelLoader.getLabelValue(message.getMessageKey(), new String[] {AppContextValues.getInstance().getTicketText()});
				msgFromDB = LabelLoader.getLabelValue(message.getMessageKey());
				
				if(msgFromDB!=null && !"".equalsIgnoreCase(msgFromDB))
				{
					msgParams = message.getMsgParameters(); 

					if (msgParams == null || msgParams.length == 0) {
						msgParams = new String[] { AppContextValues
								.getInstance().getTicketText() };
					}
					
					MessageFormat format = new MessageFormat(escape(msgFromDB));
					msgFromDB = format.format(msgParams);
					if( !i18nMessage.toString().equals(msgFromDB.concat("\n")) ) {
						i18nMessage.append(msgFromDB).append("\n");
					}
				}
			}
			i18nMessage.deleteCharAt(i18nMessage.length()-1);
			if("".equalsIgnoreCase(i18nMessage.toString().trim()))
			{
				i18nMessage.append(messages.get(0).getMessageKey());
			}
		}
		else
		{
			i18nMessage.append(UNKNOWN_MESSAGE_KEY);
		}
		return i18nMessage.toString();
	}

	public static boolean isValidUserForThisFucntion(UserDTO userDTO,final String fucntion) {		   
		List<String> funList = new ArrayList<String>();
		if(userDTO.getFunctionList()!=null) 
		{
			for (FunctionDTO functionDTO : userDTO.getFunctionList()) 
			{
				funList.add(functionDTO.getFunctionName());
			}
		}
		return funList.contains(fucntion) ;

	}

	/**
	 * Populates and retrives the Parameter Map for the User
	 * 
	 * @param userForm
	 * @return map holds the parameter name as Key and the values
	 */
	public static Map<String, String> getParameterMapOfUser(UserInfoDTO userFormDTO)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		if(userFormDTO.getParameterForms()!=null){
			for(int i=0;i<userFormDTO.getParameterForms().size();i++){
				ParameterInfoDTO parameterDTO =userFormDTO.getParameterForms().get(i) ;
				paramMap.put(parameterDTO.getParameterName(),parameterDTO.getParameterValue());
			}
		}
		return paramMap;
	}

	public static boolean isValidUserForThisFucntion(final String fucntion) {
		com.ballydev.sds.framework.dto.UserDTO userDTO = SDSApplication.getUserDetails();        
		List<String> funList = new ArrayList<String>();
		if(userDTO.getFunctionList()!=null) 
		{
			for (com.ballydev.sds.framework.dto.FunctionDTO functionDTO : userDTO.getFunctionList()) 
			{
				funList.add(functionDTO.getFunctionName());
			}
		}if(fucntion.equalsIgnoreCase(IVoucherConstants.USER_FUNCTION_TKT_RECONCILATION)){
			Map<String, String> userMap = getParameterMapOfUser(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
			if(userMap.get(IVoucherConstants.STC_RECONCILIATION)!=null){
				String stcReconcillationLevel = userMap.get(IVoucherConstants.STC_RECONCILIATION);			
				if(stcReconcillationLevel!=null){
					int stcReconcillationLevelInt = (Integer.parseInt(stcReconcillationLevel));
					if(stcReconcillationLevelInt==0){
						return false;
					}
				}				
			}else{
				return false;
			}
		}
		return funList.contains(fucntion) ;

	}

	public static int getReconLevel(){
		int stcReconcillationLevelInt =0;
		Map<String, String> userMap = getParameterMapOfUser(VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails()));
		if(userMap.get(IVoucherConstants.STC_RECONCILIATION)!=null){
			String stcReconcillationLevel = userMap.get(IVoucherConstants.STC_RECONCILIATION);			
			if(stcReconcillationLevel!=null){
				stcReconcillationLevelInt = (Integer.parseInt(stcReconcillationLevel));				
			}			
		}	
		return stcReconcillationLevelInt;
	}

	@SuppressWarnings("unchecked")
	public static UserInfoDTO convertUserDTOToUserForm(UserDTO userDTO)
	{
		UserInfoDTO userForm = new UserInfoDTO();
		if(userDTO!=null)
		{

			ObjectMapping.copyAlikeFields(userDTO, userForm);
			List<FunctionDTO> functionDTOList = userDTO.getFunctionList();
			List<FunctionInfoDTO> functionFormList = ObjectMapping.copyArrayOfObjectstToList(functionDTOList, FunctionInfoDTO.class);
			if(functionFormList!=null)
				userForm.setFunctionForms(functionFormList); 
			List<ParameterDTO> parameterDTOList =  userDTO.getParameterList();
			List<ParameterInfoDTO> parameterList = ObjectMapping.copyArrayOfObjectstToList(parameterDTOList, ParameterInfoDTO.class);
			if(parameterList!=null)
				userForm.setParameterForms(parameterList);			
		}
		return userForm;
	}

	/**
	 * Escape any single quote characters that are included in the specified
	 * message string.
	 *
	 * @param string The string to be escaped
	 */
	public static String escape(String string) 
	{
		if ((string == null) || (string.indexOf('\'') < 0)) {
			return string;
		}

		int n = string.length();
		StringBuffer sb = new StringBuffer(n);

		for (int i = 0; i < n; i++) 
		{
			char ch = string.charAt(i);

			if (ch == '\'') 
			{
				sb.append('\'');
			}
			sb.append(ch);
		}

		return sb.toString();
	}

	public static String removeZeros(String str) {
		if(str == null ) {
			return str;
		}		
		for(int i = 0; i < str.length(); i++) {			
			if(str.charAt(0)=='0'){				
				str = str.replaceFirst("0", "");
			}else{
				break;
			}			
		}		
		return str;
	}

	public static int getUserRole(){
		int userRole=UserRoleEnum.CASHIER_WORK_STATION.getUserRoleTypeId();
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		boolean retValue = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR);
		if(retValue){
			userRole = UserRoleEnum.AUDITOR_WORK_STATION.getUserRoleTypeId();
		}else{
			userRole =UserRoleEnum.CASHIER_WORK_STATION.getUserRoleTypeId();
		}
		return userRole;
	}
	
	
	
	public static boolean isDBManipulationMessage(VoucherEngineServiceException ex){
		boolean isDBManipulationMessage = false;
		List<Message> msgs = ex.getErrorMessages();
		
		for(Message msg : msgs){
			if(msg.getMessageKey().equalsIgnoreCase("VOUCHER.DB.MANIPULATION.DETECTED"))
				isDBManipulationMessage = true;
		}
		
		return isDBManipulationMessage;
	}
	
	
	public static void main(String[] args) {
		System.out.println(formatBarcode("765476547654765432" , 6l, 1));
		System.out.println(formatBarcode("765476547654765432" , 7l, 1));
		System.out.println(formatBarcode("765476547654765432" , 3l, 1));
		System.out.println(formatBarcode("765476547654765432" , 1l, 1));
	}

	
	public static String formatBarcode(String barcode, Long ticketType, Integer status ) {
		String formattedBarcode = barcode;
		
		if( barcode.matches("[0-9]{" + IVoucherConstants.BARCODE_LENGTH + "}") ) {

			if( ticketType == null ) {
				if ( VoucherUtil.isGameBarcode(barcode) == 1 ) {
					ticketType = Long.valueOf(TicketTypeEnum.STANDARD_VALIDATION_TICKET_TYPE.getTicketType());
				}else if ( VoucherUtil.isGameBarcode(barcode) == 2 ) {
					ticketType = Long.valueOf(TicketTypeEnum.ENHANCED_VALIDATION_TICKET_TYPE.getTicketType());
				} 
				else{
					ticketType = Long.valueOf(barcode.substring(0,1));
				}
			}
			
			if( status == null ) {
				status = 1;
			}

			if(status != 3) {				
				if (ticketType == 1 || ticketType == 2 ||ticketType == 3 || ticketType == 4) {
					formattedBarcode = barcode.substring(0,15) + "***";
				} else if( ticketType == 5 || ticketType == 7 || ticketType == 90 || ticketType == 91 ) {
					formattedBarcode = barcode.substring(0,14) + "****";
				} else if( ticketType == 6 ) {
					formattedBarcode = barcode.substring(0,10) + "***" + barcode.substring(13);
				}
			}
			formattedBarcode = formatBarcode(formattedBarcode, formattedBarcode);
		}
		
		return formattedBarcode;
	}
	
	public static String formatBarcode(String barcode, String No_Data) {
		try {
			if (barcode == null)
				return No_Data;
			else {
				int index = barcode.lastIndexOf('-');
				if (index == -1 && count == 0) {
					++count;
					String fString = barcode.substring(0, 4);
					String sString = barcode.substring(4, barcode.length());
					brCode = fString + "-" + sString;
					formatBarcode(brCode, No_Data);
				} else if (count < 3) {
					++count;
					String fString = barcode.substring(0, index + 6);
					String sString = barcode.substring(index + 6, barcode
							.length());
					brCode = fString + "-" + sString;
					formatBarcode(brCode, No_Data);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		count = 0;
		return brCode;
	}
	
	public static int isGameBarcode(String barcode) {
		int response 	= 0;
		SessionUtility utility = new SessionUtility();
		String strdEnhancedValidationBarcodeValue = utility.getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
		String gameBarcodeIndicator = barcode.substring(0, 2);
		
		System.out.println("Standard Enhanced Validation Barcode Value from UI: " + strdEnhancedValidationBarcodeValue);

		if( strdEnhancedValidationBarcodeValue.equalsIgnoreCase(IVoucherConstants.VCH_BARCODE_SYSTEM_VALIDATION) ) {
			response = 0;
		} else if( strdEnhancedValidationBarcodeValue.equalsIgnoreCase(IVoucherConstants.VCH_BARCODE_STANDARD_VALIDATION) 
				&& IVoucherConstants.STANDARD_ENHANCED_VALIDATION_BARCODE_INDICATOR.equals(gameBarcodeIndicator) ) {
			response = 1;
		} else if( strdEnhancedValidationBarcodeValue.equalsIgnoreCase(IVoucherConstants.VCH_BARCODE_ENHANCED_VALIDATION) 
				&& IVoucherConstants.STANDARD_ENHANCED_VALIDATION_BARCODE_INDICATOR.equals(gameBarcodeIndicator) ) {
			response = 2;
		}
		return response;
	}
}
