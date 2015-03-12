/*****************************************************************************
 * $Id: IExceptionConstants.java,v 1.3, 2010-02-12 07:31:36Z, Subha Viswanathan$
 * $Date: 2/12/2010 1:31:36 AM$
 * $Log$
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
package com.ballydev.sds.jackpot.constants;

/**
 * Error constants used in the Jackpot Engine class if an exception occurs
 * @author dambereen
 * @version $Revision: 4$
 */
public interface IExceptionConstants {

	public String GET_JACKPOT_DETAILS_ERROR="JACKPOT.ERROR.GET.JACKPOT.PENDING.SLIPS.DETAILS";
	
	public String GET_JACKPOT_STATUS_ERROR="JACKPOT.ERROR.GET.JACKPOT.STATUS";
	
	public String POST_JACKPOT_STATUS_ERROR="JACKPOT.ERROR.POST.JACKPOT.STATUS";
	
	public String PROCESS_JACKPOT_ERROR="JACKPOT.ERROR.PROCESS.JACKPOT";
	
	public String PRINT_JACKPOT_ERROR="JACKPOT.ERROR.PRINT.JACKPOT"; // TO BE ADDED
	
	public String PROCESS_MANUAL_JACKPOT_ERROR="JACKPOT.ERROR.PROCESS.MANUAL.JACKPOT";
	
	public String GET_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.GET.BLIND.ATTEMPTS";
	
	public String POST_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.POST.BLIND.ATTEMPT";
	
	public String POST_NON_CARDED_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.POST.BLIND.ATTEMPT.NON.CARDED";
	
	public String GET_REPRINT_JACKPOT_SLIP_DETAILS_ERROR="JACKPOT.ERROR.GET.REPRINT.JACKPOT.SLIP.DETAILS";
		
	public String GET_VOID_JACKPOT_SLIP_DETAILS_ERROR="JACKPOT.ERROR.GET.VOID.JACKPOT.SLIP.DETAILS";
	
	public String GET_JACKPOT_XML_INFO_ERROR="JACKPOT.ERROR.GET.JACKPOT.XML.INFO";
	
	public String GET_PLAYER_DETAILS_ERROR="JACKPOT.ERROR.GET.PLAYER.DETAILS";	
	
	public String GET_ALL_PENDING_JACKPOT_SLIP_DETAILS_ERROR = "JACKPOT.ERROR.GET.ALL.PENDING.JACKPOT.SLIP.DETAILS";	
	
	public String GET_JACKPOT_INFO_FOR_EA = "JACKPOT.ERROR.GET.JACKPOT.INFO.FOR.EA";
	
	public String GET_JACKPOT_INFO_FOR_EA_WITH_LIMIT = "JACKPOT.ERROR.GET.JACKPOT.INFO.FOR.EA.WITH.LIMIT";	
	
	public String POST_PROGRESSIVE_JP_AMOUNT = "JACKPOT.ERROR.POST.PROGRESSIVE.JP.AMOUNT";	
	
	public String GET_JP_SITE_CONFIG_DETAILS = "JACKPOT.ERROR.GET.SITE.CONFIG.DETAILS";	
	
	public String SEND_ALERT = "JACKPOT.ERROR.SEND.ALERT";
	
	public String VOID_PENDING_JACKPOT_SLIPS_FOR_SLOT = "JACKPOT.ERROR.VOID.PENDING.JACKPOT.SLIPS.FOR.SLOT";
	
	public String VOID_ALL_PENDING_JACKPOT_SLIPS = "JACKPOT.ERROR.VOID.ALL.PENDING.JACKPOT.SLIPS";
	
	public String GET_JACKPOT_DETAILS_TO_PRINT_REPORT = "JACKPOT.ERROR.GET.DETAILS.TO.PRINT.REPORT";// TO BE ADDED
	
	public String GET_JACKPOT_DETAILS_TO_PRINT_REPORT_EMPLOYEE = "JACKPOT.ERROR.GET.DETAILS.TO.PRINT.REPORT.EMPLOYEE";// TO BE ADDED 
	
	public String GET_REPRINT_PRIOR_SLIP_DETAILS = "JACKPOT.ERROR.GET.REPRINT.PRIOR.SLIP.DETAILS";// TO BE ADDED
	
	public String POST_JACKPOT_EMPLOYEE_CARDED_INFO = "JACKPOT.ERROR.POST.JACKPOT.EMPLOYEE.CARDED.INFO";// TO BE ADDED 
	
	public String VOID_ALL_PENDING_JACKPOT_SLIPS_FOR_AUDIT = "Exception while voiding the pending jackpot slips for the gaming day";// Text to remain de same, as it is not used in the UI
	 
	public String GET_PENDING_JP_DETAILS_FOR_LAST_MINS_ERROR = "JACKPOT.ERROR.GET.PENDING.JACKPOT.DETAILS.LAST.MINS";	// TO BE ADDED 
	
	public String POST_VOID_STATUS = "JACKPOT.ERROR.POST.VOID.STATUS"; // TO BE ADDED 
	
	public String POST_JACKPOT_REPRINT = "JACKPOT.ERROR.POST.JACKPOT.REPRINT"; // TO BE ADDED 
	
	public String UNABLE_TO_FETCH_ASSET_DETAILS = "JACKPOT.ERROR.FETCH.ASSET.DETAILS"; // TO BE ADDED 
	
	public String UNABLE_TO_PROCESS_JP_ADJUST = "JACKPOT.ERROR.PROCESS.ADJUST"; // TO BE ADDED 
	
	public String GET_JACKPOT_POST_ACC_DETAIL_ERROR = "GET.JACKPOT.POST.ACC.DETAIL.ERROR"; // Error message when "Unable to get the post to accounting jackpot status for the current process."
}
