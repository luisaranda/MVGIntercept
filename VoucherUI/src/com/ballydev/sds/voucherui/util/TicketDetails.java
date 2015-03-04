/*****************************************************************************
 * $Id: TicketDetails.java,v 1.4, 2010-06-22 16:01:18Z, Verma, Nitin Kumar$Date: 
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

/**
 * Class that represents the ticket information for offline tickets that will be stored in files.
 * 
 * NOTE: This class was taken from existing versions of SDS. 
 * Some changes may be need to meet SDS 11.0 requirements.
 */
public class TicketDetails implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String command;

    private String response;

    private String barcode;

    private String location;

    private double amount;

    private String amountType;

    private String status;

    private String playerID;

    private long effective;

    private long expire;

    private String employee;

    private String time;
    
    private String cardRequired;
    
    private String origLocation;
    
    private String cashierName;

    private String overrideUserName;
    
    public TicketDetails(){
    }

    public TicketDetails(String command, String response, String barcode) {
	this(command, response, barcode, null, 0, null, null, null, 0, 
		0, null, null, null);
    }

    public TicketDetails(String command, String response, String barcode, String location, 
    		double amount, String status, String employee, String cashierName, String overrideUserName, String playerID) {
       	this.command = command;
    	this.response = response;
        this.barcode = barcode;
        this.location = location;
        this.amount = amount;
        this.status = status;
        this.employee = employee;
        this.cashierName = cashierName;
        this.overrideUserName = overrideUserName;
        this.playerID = playerID;
    }

    public TicketDetails(String command, String response, String barcode, String location, double amount, String amountType, 
		String status, String player, long effective, long expire, String employee, String time, String cardRequired) {

    	this.command = command;
    	this.response = response;
        this.barcode = barcode;
        this.location = location;
        this.amount = amount;
        this.amountType = amountType;
        this.status = status;
        this.playerID = player;
        this.effective = effective;
        this.expire = expire;
        this.employee = employee;
        this.time = time;
        this.cardRequired = cardRequired;
    }

    public String getCommand() {
    	return command;
    }

    public String getResponse() {
    	return response;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getLocation() {
        return location;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount=amount;
    }    

    public String getAmountType() {
        return amountType;
    }

    public String getStatus() {
        return status;
    }

    public String getPlayerID() {
        return playerID;
    }

    public long getEffective() {
        return effective;
    }

    public long getExpire() {
        return expire;
    }

    public String getEmployee() {
    	return employee;
    }

    public String getTime() {
    	return time;
    }

    public boolean getCardRequired() {
        if(cardRequired != null && cardRequired.equals("true")) {
            return true;
        }
        return false;
    }
    
    public String getOrigLocation() {
        return origLocation;
    }
    
    public void setOrigLocation(String loc) {
        origLocation = loc;
    }

	/**
	 * @return the cashierName
	 */
	public String getCashierName() {
		return cashierName;
	}

	/**
	 * @param cashierName the cashierName to set
	 */
	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	/**
	 * @return the overrideUserName
	 */
	public String getOverrideUserName() {
		return overrideUserName;
	}

	/**
	 * @param overrideUserName the overrideUserName to set
	 */
	public void setOverrideUserName(String overrideUserName) {
		this.overrideUserName = overrideUserName;
	}

}
