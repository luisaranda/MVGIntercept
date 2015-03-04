/*****************************************************************************
 * $Id: IExceptionConstants.java,v 1.1, 2008-09-25 12:03:16Z, Ambereen Drewitt$
 * $Date: 9/25/2008 7:03:16 AM$
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
package com.ballydev.sds.jackpotui.constants;

/**
 * Error constants used in the Jackpot Engine class if an exception occurs
 * @author dambereen
 * @version $Revision: 2$
 */
public interface IExceptionConstants {

	public String GET_JACKPOT_DETAILS_ERROR="JACKPOT.ERROR.GET.JACKPOT.PENDING.SLIPS.DETAILS";
	
	public String GET_JACKPOT_STATUS_ERROR="JACKPOT.ERROR.GET.JACKPOT.STATUS";
	
	public String POST_JACKPOT_STATUS_ERROR="JACKPOT.ERROR.POST.JACKPOT.STATUS";
	
	public String PROCESS_JACKPOT_ERROR="JACKPOT.ERROR.PROCESS.JACKPOT";
	
	public String PROCESS_MANUAL_JACKPOT_ERROR="JACKPOT.ERROR.PROCESS.MANUAL.JACKPOT";
	
	public String GET_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.GET.BLIND.ATTEMPTS";
	
	public String POST_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.POST.BLIND.ATTEMPT";
	
	public String POST_NON_CARDED_BLIND_ATTEMPTS_ERROR="JACKPOT.ERROR.POST.BLIND.ATTEMPT.NON.CARDED";
	
	public String GET_REPRINT_JACKPOT_SLIP_DETAILS_ERROR="JACKPOT.ERROR.GET.REPRINT.JACKPOT.SLIP.DETAILS";
	
	public String GET_JACKPOT_XML_INFO_ERROR="JACKPOT.ERROR.GET.JACKPOT.XML.INFO";
	
	public String GET_PLAYER_DETAILS_ERROR="JACKPOT.ERROR.GET.PLAYER.DETAILS";	
	
	public String RUNTIME_ERROR_UNREACH_SERVICE_MSG = "Unreachable?: Service unavailable";
	
	public String RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG = "cluster invocation failed,";

}
