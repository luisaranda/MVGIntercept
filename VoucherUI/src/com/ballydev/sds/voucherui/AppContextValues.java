/*****************************************************************************
 *           Copyright (c) 2003 Bally Gaming Inc.  1977 - 2003
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;


/**
 * This class holds the application context values
 * @author Nithya kalyani
 * @version $Revision: 5$
 * 
 */
public class AppContextValues {

	/**
	 * Instance to hold the value of the current screen
	 */
	private String currentScreen;

	/**
	 * Instance of AppContextvalues
	 */
	private static AppContextValues appContextValues = null;
	
	/**
	 * Instance of the emp id entered during the reconciliation
	 */
	private String reclEmpId;
	
	/**
	 * Instance of the batch id created during the installation
	 */
	private String batchId;
	
	private String slotText;
	
	private String ticketText;
	
	private boolean isVoucherOpen;

	/**
	 * @return the slotText
	 */
	public String getSlotText() {
		if(slotText==null){
			slotText= IVoucherConstants.DEFAULT_SLOT_TEXT;
		}
		return slotText;
	}


	/**
	 * @param slotText the slotText to set
	 */
	public void setSlotText(String slotText) {
		this.slotText = slotText;
	}


	/**
	 * @return the ticketText
	 */
	public String getTicketText() {
		if(ticketText==null){
			ticketText= IVoucherConstants.DEFAULT_TICKET_TEXT;
		}
		return ticketText;
	}


	/**
	 * @param ticketText the ticketText to set
	 */
	public void setTicketText(String ticketText) {
		this.ticketText = ticketText;
	}


	/**
	 * @return the reclEmpId
	 */
	public String getReclEmpId() {
		return reclEmpId;
	}


	/**
	 * @param reclEmpId the reclEmpId to set
	 */
	public void setReclEmpId(String reclEmpId) {
		this.reclEmpId = reclEmpId;
	}


	/**
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}


	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}


	/**
	 * AppContextValues Constructor
	 */
	private AppContextValues(){
	}


	public static AppContextValues getInstance(){
		if (appContextValues == null){
			appContextValues = new AppContextValues();
		}
		return appContextValues;
	}
	/**
	 * @return the currentScreen
	 */
	public String getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * @param currentScreen the currentScreen to set
	 */
	public void setCurrentScreen(String currentScreen) {
		this.currentScreen = currentScreen;
	}


	/**
	 * @return the isVoucherOpen
	 */
	public boolean isVoucherOpen() {
		return isVoucherOpen;
	}


	/**
	 * @param isVoucherOpen the isVoucherOpen to set
	 */
	public void setVoucherOpen(boolean isVoucherOpen) {
		this.isVoucherOpen = isVoucherOpen;
	}
}
