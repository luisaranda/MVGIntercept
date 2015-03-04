/**
 * 
 */
package com.ballydev.sds.slipsui.util;

/**
 * @author gsrinivasulu
 *
 */
public class ReportPrintDTO {
	
	/**
	 * Casino Name
	 */
	private String casinoName;
	
	private int siteId;
	
	private String slotAttendantId;
	
	private String slotAttendantName;
	
	private String printerSchema;
	
	private String slipSchema;
	
	private String fillAmount;
	
	private String bleedAmount;
	
	private String beefAmount;
	
	private String voidFillAmount;
	
	private String voidBeefAmount;
	
	private String voidBleedAmount;
	
	private String printDate;
	
	private String fromDate;
	
	private String toDate;
	
	
	private String empID = null;
	
	private String empName;

	public String getPrinterSchema() {
		return printerSchema;
	}

	public void setPrinterSchema(String printerSchema) {
		this.printerSchema = printerSchema;
	}

	public String getSlipSchema() {
		return slipSchema;
	}

	public void setSlipSchema(String slipSchema) {
		this.slipSchema = slipSchema;
	}

	public String getSlotAttendantId() {
		return slotAttendantId;
	}

	public void setSlotAttendantId(String slotAttendantId) {
		this.slotAttendantId = slotAttendantId;
	}

	public String getSlotAttendantName() {
		return slotAttendantName;
	}

	public void setSlotAttendantName(String slotAttendantName) {
		this.slotAttendantName = slotAttendantName;
	}

	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the bleedAmount
	 */
	public String getBleedAmount() {
		return bleedAmount;
	}

	/**
	 * @param bleedAmount the bleedAmount to set
	 */
	public void setBleedAmount(String bleedAmount) {
		this.bleedAmount = bleedAmount;
	}

	/**
	 * @return the fillAmount
	 */
	public String getFillAmount() {
		return fillAmount;
	}

	/**
	 * @param fillAmount the fillAmount to set
	 */
	public void setFillAmount(String fillAmount) {
		this.fillAmount = fillAmount;
	}

	/**
	 * @return the voidBeefAmount
	 */
	public String getVoidBeefAmount() {
		return voidBeefAmount;
	}

	/**
	 * @param voidBeefAmount the voidBeefAmount to set
	 */
	public void setVoidBeefAmount(String voidBeefAmount) {
		this.voidBeefAmount = voidBeefAmount;
	}

	/**
	 * @return the voidBleedAmount
	 */
	public String getVoidBleedAmount() {
		return voidBleedAmount;
	}

	/**
	 * @param voidBleedAmount the voidBleedAmount to set
	 */
	public void setVoidBleedAmount(String voidBleedAmount) {
		this.voidBleedAmount = voidBleedAmount;
	}

	/**
	 * @return the voidFillAmount
	 */
	public String getVoidFillAmount() {
		return voidFillAmount;
	}

	/**
	 * @param voidFillAmount the voidFillAmount to set
	 */
	public void setVoidFillAmount(String voidFillAmount) {
		this.voidFillAmount = voidFillAmount;
	}

	/**
	 * @return the beefAmount
	 */
	public String getBeefAmount() {
		return beefAmount;
	}

	/**
	 * @param beefAmount the beefAmount to set
	 */
	public void setBeefAmount(String beefAmount) {
		this.beefAmount = beefAmount;
	}

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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
