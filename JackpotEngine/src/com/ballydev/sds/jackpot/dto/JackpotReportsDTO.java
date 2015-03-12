/*****************************************************************************
 * $Id: JackpotReportsDTO.java,v 1.7, 2010-01-25 09:01:36Z, Subha Viswanathan$
 * $Date: 1/25/2010 3:01:36 AM$
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
 * DTO class to hold the Jackpot Details to print the Jackpot Slip Balancing Report
 * @author dambereen
 * @version $Revision: 8$
 *
 */
public class JackpotReportsDTO extends JackpotBaseDTO{

	
	private static final long serialVersionUID = -6589798681330895352L;

	/**
	 * NET amount 
	 */
	private long netAmt =0;
	
	/**
	 * HPJP amount without rounding
	 */
	private long hpjpAmt =0;
	
	/**
	 * Slot No instance
	 */
	private String slotNo= null;
	
	/**
	 * Sequence Number instance
	 */
	private long sequenceNo=0;
	
	/**
	 * Flag that used to check if the data was posted successfully
	 */
	private boolean postedSuccessfully;
	
	/**
	 * Slot Attendant Id instance
	 */
	private String slotAttendantId = null;
	
	/**
	 * SlotAttendantFirstName
	 */
	private String slotAttendantFirstName;	
	
	/**
	 * slotAttendantLastName
	 */
	private String slotAttendantLastName;
	
	/**
	 * total Jackpot Orginal Amount
	 */
	private long jackpotOrgAmt=0;
	
	/**
	 * Total Jackpot Tax Amount
	 */
	private long jackpotTaxAmt=0;
	
	/**
	 * Total Jackpot Net Amount 
	 */
	private long jackpotNetAmt=0;	
	
	/**
	 * Total Voided Jackpots Amount
	 */
	private long voidedJackpotAmt =0;

	/**
	 * Field that holds the printer schema
	 */
	private String printerSchema;	
	/**
	 * Field that holds the slip schema
	 */
	private String slipSchema;
	
	/**
	 * @return the jackpotNetAmt
	 */
	public long getJackpotNetAmt() {
		return jackpotNetAmt;
	}

	/**
	 * @param jackpotNetAmt the jackpotNetAmt to set
	 */
	public void setJackpotNetAmt(long jackpotNetAmt) {
		this.jackpotNetAmt = jackpotNetAmt;
	}

	/**
	 * @return the voidedJackpotAmt
	 */
	public long getVoidedJackpotAmt() {
		return voidedJackpotAmt;
	}

	/**
	 * @param voidedJackpotAmt the voidedJackpotAmt to set
	 */
	public void setVoidedJackpotAmt(long voidedJackpotAmt) {
		this.voidedJackpotAmt = voidedJackpotAmt;
	}

	/**
	 * @return the printerSchema
	 */
	public String getPrinterSchema() {
		return printerSchema;
	}

	/**
	 * @param printerSchema the printerSchema to set
	 */
	public void setPrinterSchema(String printerSchema) {
		this.printerSchema = printerSchema;
	}

	/**
	 * @return the slipSchema
	 */
	public String getSlipSchema() {
		return slipSchema;
	}

	/**
	 * @param slipSchema the slipSchema to set
	 */
	public void setSlipSchema(String slipSchema) {
		this.slipSchema = slipSchema;
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
	 * @return the slotAttendantFirstName
	 */
	public String getSlotAttendantFirstName() {
		return slotAttendantFirstName;
	}

	/**
	 * @param slotAttendantFirstName the slotAttendantFirstName to set
	 */
	public void setSlotAttendantFirstName(String slotAttendantFirstName) {
		this.slotAttendantFirstName = slotAttendantFirstName;
	}

	/**
	 * @return the slotAttendantLastName
	 */
	public String getSlotAttendantLastName() {
		return slotAttendantLastName;
	}

	/**
	 * @param slotAttendantLastName the slotAttendantLastName to set
	 */
	public void setSlotAttendantLastName(String slotAttendantLastName) {
		this.slotAttendantLastName = slotAttendantLastName;
	}

	/**
	 * @return the slotAttendantId
	 */
	public String getSlotAttendantId() {
		return slotAttendantId;
	}

	/**
	 * @param slotAttendantId the slotAttendantId to set
	 */
	public void setSlotAttendantId(String slotAttendantId) {
		this.slotAttendantId = slotAttendantId;
	}

	/**
	 * @return the postedSuccessfully
	 */
	public boolean isPostedSuccessfully() {
		return postedSuccessfully;
	}

	/**
	 * @param postedSuccessfully the postedSuccessfully to set
	 */
	public void setPostedSuccessfully(boolean postedSuccessfully) {
		this.postedSuccessfully = postedSuccessfully;
	}

	/**
	 * @return the jackpotOrgAmt
	 */
	public long getJackpotOrgAmt() {
		return jackpotOrgAmt;
	}

	/**
	 * @param jackpotOrgAmt the jackpotOrgAmt to set
	 */
	public void setJackpotOrgAmt(long jackpotOrgAmt) {
		this.jackpotOrgAmt = jackpotOrgAmt;
	}

	/**
	 * @return the jackpotTaxAmt
	 */
	public long getJackpotTaxAmt() {
		return jackpotTaxAmt;
	}

	/**
	 * @param jackpotTaxAmt the jackpotTaxAmt to set
	 */
	public void setJackpotTaxAmt(long jackpotTaxAmt) {
		this.jackpotTaxAmt = jackpotTaxAmt;
	}

	/**
	 * @return the sequenceNo
	 */
	public long getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	/**
	 * @return the hpjpAmt
	 */
	public long getHpjpAmt() {
		return hpjpAmt;
	}

	/**
	 * @param hpjpAmt the hpjpAmt to set
	 */
	public void setHpjpAmt(long hpjpAmt) {
		this.hpjpAmt = hpjpAmt;
	}

	/**
	 * @return the slotNo
	 */
	public String getSlotNo() {
		return slotNo;
	}

	/**
	 * @param slotNo the slotNo to set
	 */
	public void setSlotNo(String slotNo) {
		this.slotNo = slotNo;
	}

	/**
	 * @return the netAmt
	 */
	public long getNetAmt() {
		return netAmt;
	}

	/**
	 * @param netAmt the netAmt to set
	 */
	public void setNetAmt(long netAmt) {
		this.netAmt = netAmt;
	}
	
	
}
