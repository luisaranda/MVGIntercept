/*****************************************************************************
 * $Id: JackpotEmployeeInfoDTO.java,v 1.3, 2010-01-25 08:56:28Z, Subha Viswanathan$
 * $Date: 1/25/2010 2:56:28 AM$
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
package com.ballydev.sds.jackpot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class which contains getters and setters for jackpot employee info specific fields  
 * @author dambereen
 * @version $Revision: 4$
 */
public class JackpotEmployeeInfoDTO extends JackpotBaseDTO{	

	
	private static final long serialVersionUID = 5551103884946545783L;

	/**
	 * Employee Card No who carded the Jackpot
	 */
	private String employeeCardNo; 
	
	/**
	 * Date Time in MilliSeconds
	 */
	private long dateTimeMilliSecs;
	
	/**
	 * Jackpot Amount
	 */
	private long jackpotAmount;
	
	/**
	 * Jackpot Id instance
	 */
	private String jackpotId = null;	
	
	/**
	 * Site id
	 */
	private Integer siteId;
	
	/**
	 * Slot No
	 */
	private String assetConfigNo;
	
	/**
	 * Employee Id who carded the Jackpot
	 */
	private String employeeId; 
	
	/**
	 * Employee First name 
	 */
	private String employeeFirstName;
	
	/**
	 * Employee Last Name
	 */
	private String employeeLastName;
	
	/**
	 * @return the employeeFirstName
	 */
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	/**
	 * @param employeeFirstName the employeeFirstName to set
	 */
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeLastName
	 */
	public String getEmployeeLastName() {
		return employeeLastName;
	}

	/**
	 * @param employeeLastName the employeeLastName to set
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Object value = "";
		StringBuilder strBuilder = new StringBuilder();
		Method[] methods = this.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().startsWith("get")){
				try {
					value = methods[i].invoke(this, (Object[])null);
					if(value != null){
						strBuilder.append("Methods available: "+methods[i].getName()+" = "+value.toString());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}		
		return strBuilder.toString();
	}

	/**
	 * @return the jackpotAmount
	 */
	public long getJackpotAmount() {
		return jackpotAmount;
	}

	/**
	 * @return the assetConfigNo
	 */
	public String getAssetConfigNo() {
		return assetConfigNo;
	}

	/**
	 * @param assetConfigNo the assetConfigNo to set
	 */
	public void setAssetConfigNo(String assetConfigNo) {
		this.assetConfigNo = assetConfigNo;
	}

	/**
	 * @param jackpotAmount the jackpotAmount to set
	 */
	public void setJackpotAmount(long jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}

	/**
	 * @return the jackpotId
	 */
	public String getJackpotId() {
		return jackpotId;
	}

	/**
	 * @param jackpotId the jackpotId to set
	 */
	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the dateTimeMilliSecs
	 */
	public long getDateTimeMilliSecs() {
		return dateTimeMilliSecs;
	}

	/**
	 * @param dateTimeMilliSecs the dateTimeMilliSecs to set
	 */
	public void setDateTimeMilliSecs(long dateTimeMilliSecs) {
		this.dateTimeMilliSecs = dateTimeMilliSecs;
	}

	/**
	 * @return the employeeCardNo
	 */
	public String getEmployeeCardNo() {
		return employeeCardNo;
	}

	/**
	 * @param employeeCardNo the employeeCardNo to set
	 */
	public void setEmployeeCardNo(String employeeCardNo) {
		this.employeeCardNo = employeeCardNo;
	}
	
}
