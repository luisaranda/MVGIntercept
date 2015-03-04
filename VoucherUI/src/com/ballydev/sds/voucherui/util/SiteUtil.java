package com.ballydev.sds.voucherui.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class calls the session utility class of the framework and
 * retrives the needed values from the site configuration
 * @author Nithya kalyani
 * @version $Revision: 16$ 
 */
public class SiteUtil {

	/**
	 * This method returns a value which will state whether the
	 * voided tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowVoidedTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_VOIDED_TKTS));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * voided tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowCashierToRedeemTheirOwnTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_CASHIER_TO_REDEEM_THEIR_OWN_TKTS));		
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * expired tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowExpiredTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_EXPIRED_TKTS));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * pending tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowPendingTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_PENDING_TKTS));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * not effective tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowNotEffTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_TKTS_NOT_YET_EFF));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * cashable promo voided tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowCPVoidedTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_VOIDED_CP_TKTS));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * CP Expired tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowCPExpiredTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_EXPIRED_CP_TKTS));
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * CP Pending tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowCPPendingTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_PENDING_CP_TKTS));
		return response;
	}
	/**
	 * This method returns a value which will state whether the
	 * CP Not effective tickets can be redeemed or not
	 * 0 = Do Not Allow Redemption of Ticket Status;
	 * 1= Override Authorization Required;
	 * 2 = Allow Normal Redemption of Ticket Status.
	 * @return
	 */
	public static int allowCPNotEffTkts(){
		int response = 0;
		SessionUtility sessionUtility = new SessionUtility();	
		response = getSiteResponse(sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_CP_TKTS_NOT_YET_EFF));
		return response;
	}

	/**
	 * This method returns the no of days in which the
	 * ticket created will expire
	 * @return
	 */
	public static Date getNoOfDaysToExpireTkt(){
		String response = "";
		Date date = DateUtil.getCurrentServerDate();
		SessionUtility sessionUtility = new SessionUtility();	
		response = sessionUtility.getSiteConfigurationValue(IVoucherConstants.NUMBER_DAYS_TO_EXPIRE);		
		if(response!=null){		
			int days = Integer.parseInt(response);
			long oneDayTs = 1000*60*60*24;
			long ds = days*oneDayTs;
			Timestamp ts = new Timestamp(DateUtil.getCurrentServerDate().getTime()+ds);
			Date d = new Date(ts.getTime());
			SimpleDateFormat format = new SimpleDateFormat(IVoucherConstants.DATE_FORMAT);	
			String dateToParse = format.format(d);     	
			String pattern = IVoucherConstants.DATE_FORMAT;
			SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(dateFormatter.parse(dateToParse));
				date = calendar.getTime();
			} catch (ParseException e) {			
				e.printStackTrace();
			}	
		}
		return date;
	}	

	/**
	 * This method returns the maximum ticket redemption amount
	 * that is set in the site config parameter
	 * @return
	 */
	public static String getMaxTktRedemptionAmt(){
		String redemptionAmt = "";
		SessionUtility sessionUtility = new SessionUtility();	
		redemptionAmt= sessionUtility.getSiteConfigurationValue(IVoucherConstants.MAX_REDEMPTION_AMT);	
		return redemptionAmt;
	}

	/**
	 * This method returns the no of tickets that can be printed
	 * that is set in the site config parameter
	 * @return
	 */
	public static int getNoOfTktsToPrint(){
		int retValue =0;
		String noOfTkts = "";
		SessionUtility sessionUtility = new SessionUtility();	
		noOfTkts= sessionUtility.getSiteConfigurationValue(IVoucherConstants.NO_OF_TKTS_TO_PRINT);		
		if(noOfTkts!=null){
			retValue = Integer.valueOf(noOfTkts);
		}
		return retValue;
	}

	/**
	 * This method returns the no of tickets that can be printed
	 * that is set in the site config parameter
	 * @return
	 */
	public static int getMinLength(){
		int retValue =0;
		String minLen = "";
		SessionUtility sessionUtility = new SessionUtility();	
		minLen= sessionUtility.getSiteConfigurationValue(IVoucherConstants.MIN_LEN_BARCODE_ENQUIRY);		
		if(minLen!=null){
			retValue = Integer.valueOf(minLen);
		}
		return retValue;
	}

	/**
	 * This method returns the yes/no flag set in the site configuration
	 * for ticket printing enabled or not
	 * @return
	 */
	public static boolean isTktPrintEnabled(){
		boolean response = true;
		SessionUtility sessionUtility = new SessionUtility();
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_TICKET_PRINT);
		if(value!= null && value.equalsIgnoreCase("YES")){
			response = true;			
		}else{
			response = false;
		}		
		return response;
	}

	/**
	 * This method returns the yes/no flag set in the site configuration
	 * for voucher enable parameter
	 * @return
	 */
	public static boolean isVoucherEnabled(){
		boolean response = true;
		SessionUtility sessionUtility = new SessionUtility();
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.VOU_LIC_ENABLED);
		if(value!= null && value.equalsIgnoreCase("YES")){
			response = true;			
		}else{
			response = false;
		}		
		return response;
	}

	/**
	 * This method returns the yes/no flag set in the site configuration
	 * for ticket voiding after any exception in the print ticket
	 * @return
	 */
	public static boolean isTktVoidAllowed(){
		boolean response = false;
		SessionUtility sessionUtility = new SessionUtility();
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_TICKET_VOID);
		if(value!= null && value.equalsIgnoreCase("YES")){
			response = true;			
		}else{
			response = false;
		}		
		return response;
	}
	/**
	 * This method returns the yes/no flag set in the site configuration
	 * for ticket printing enabled or not
	 * @return
	 */
	public static boolean isStcRestictedForCashering(){
		boolean response = true;
		SessionUtility sessionUtility = new SessionUtility();
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.RESTRICTED_CASHERING_STC);
		if(value!=null && value.equalsIgnoreCase("YES")){
			response = true;			
		}else{
			response = false;
		}		
		return response;
	}

	/**
	 * This method checks for the response from the site parameter and returns 
	 * a corresponding int value
	 * @param siteValue
	 * @return
	 */
	public static int getSiteResponse(String siteValue){
		int response = 0;	
		if(siteValue!=null){
			if(siteValue.equalsIgnoreCase(IVoucherConstants.REDEMPTION_NOT_ALLOWED)){
				response =0;
			}else if(siteValue.equalsIgnoreCase(IVoucherConstants.REDEMPTION_OVERRIRDE)){
				response =1;
			}else if(siteValue.equalsIgnoreCase(IVoucherConstants.NORMAL_REDEMPTION)){
				response =2;
			}		
		}
		return response;
	}

	/**
	 * This method returns the yes/no flag set in the site configuration
	 * for multi area enabled or not
	 * @return
	 */
	public static boolean isMultiAreaEnabled(){
		boolean response = true;
		SessionUtility sessionUtility = new SessionUtility();
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.MULTI_AREA_ENABLED);		
		if(value!= null && value.equalsIgnoreCase("YES")){			
			response = true;			
		}else{			
			response = false;
		}		
		return response;
	}

	/**
	 * This method returns a value which will state whether the
	 * overirde is allowed for print ticket or not
	 *  @return
	 */
	public static boolean allowOverrideForPrint(){
		boolean response = false;
		SessionUtility sessionUtility = new SessionUtility();	
		String value = sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_PRINT_TKT_OVERRIDE);		
		if(value!= null && value.equalsIgnoreCase("YES")){			
			response = true;			
		}else{			
			response = false;
		}		
		return response;
	}

	/**
	 * This API checks if we have to print cashier location on ticket or not
	 * based on the siteconfig parameter.
	 * @return
	 */
	public static boolean isWashingtonTktCriteriaEnabled(){
		boolean isEnabled = false;
		SessionUtility sessionUtility = new SessionUtility();	
		String cashierStation = sessionUtility.getSiteConfigurationValue(IVoucherConstants.ALLOW_CASHIER_LOC_ON_TICKET);	
		if(cashierStation!= null && cashierStation.equalsIgnoreCase("YES")){			
			isEnabled = true;			
		}		
		return isEnabled;
	}

}
