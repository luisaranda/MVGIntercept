/*****************************************************************************
 * $Id: JpReportPrintDTO.java,v 1.5, 2009-01-20 10:57:05Z, Ambereen Drewitt$
 * $Date: 1/20/2009 4:57:05 AM$
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
package com.ballydev.sds.jackpotui.util;

/**
 *This class contains the datas that need to be send to printer.
 * @author dambereen
 * @version $Revision: 6$
 */
public class JpReportPrintDTO {
	
	/**
	 * Casino Name
	 */
	private String casinoName;
	
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
	 * Site Id instance
	 */
	private int siteId;
	/**
	 * Slot Attendant Id instance
	 */
	private String slotAttendantId ;
	
	/**
	 * SlotAttendantFirstName
	 */
	private String slotAttendantName;	
	
	/**
	 * Flag that used to check if the data was posted successfully
	 */
	private String printerSchema;	
	/**
	 * Field that holds the slip schema
	 */
	private String slipSchema;	
	
	/**
	 * Heading field
	 */
	private String heading;
	
	
	/**
	 * From Date
	 */
	private String fromDate;
	
	/**
	 * To date
	 */
	private String toDate;
		
	/**
	 * Total Jackpot Amount (Total HPJP amount)
	 */
	private String jackpotAmount;
	
	/**
	 * Total Tax Amount
	 */
	private String jackpotTaxAmount;
	
	/**
	 * Total voided jackpot amount
	 */
	private String jackpotVoidAmount;
	
	/**
	 * Total Tax Amount
	 */
	private String jackpotNetAmount;
	
	/**
	 * Print Date instance
	 */
	private String printDate;

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * @param heading the heading to set
	 */
	public void setHeading(String heading) {
		this.heading = heading;
	}

	/**
	 * @return the printDate
	 */
	public String getPrintDate() {
		return printDate;
	}

	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
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
	 * @return the slotAttendantName
	 */
	public String getSlotAttendantName() {
		return slotAttendantName;
	}

	/**
	 * @param slotAttendantName the slotAttendantName to set
	 */
	public void setSlotAttendantName(String slotAttendantName) {
		this.slotAttendantName = slotAttendantName;
	}


	/**
	 * @return the jackpotNetAmount
	 */
	public String getJackpotNetAmount() {
		return jackpotNetAmount;
	}

	/**
	 * @param jackpotNetAmount the jackpotNetAmount to set
	 */
	public void setJackpotNetAmount(String jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
	}

	/**
	 * @return the jackpotVoidAmount
	 */
	public String getJackpotVoidAmount() {
		return jackpotVoidAmount;
	}

	/**
	 * @param jackpotVoidAmount the jackpotVoidAmount to set
	 */
	public void setJackpotVoidAmount(String jackpotVoidAmount) {
		this.jackpotVoidAmount = jackpotVoidAmount;
	}

	/**
	 * @return the siteId
	 */
	public int getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the jackpotAmount
	 */
	public String getJackpotAmount() {
		return jackpotAmount;
	}

	/**
	 * @param jackpotAmount the jackpotAmount to set
	 */
	public void setJackpotAmount(String jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}

	/**
	 * @return the jackpotTaxAmount
	 */
	public String getJackpotTaxAmount() {
		return jackpotTaxAmount;
	}

	/**
	 * @param jackpotTaxAmount the jackpotTaxAmount to set
	 */
	public void setJackpotTaxAmount(String jackpotTaxAmount) {
		this.jackpotTaxAmount = jackpotTaxAmount;
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
	 * @return the casinoName
	 */
	public String getCasinoName() {
		return casinoName;
	}

	/**
	 * @param casinoName the casinoName to set
	 */
	public void setCasinoName(String casinoName) {
		this.casinoName = casinoName;
	}
}
