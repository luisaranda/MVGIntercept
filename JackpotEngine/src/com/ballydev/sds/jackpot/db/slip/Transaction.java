/*****************************************************************************
 * $Id: Transaction.java,v 1.3, 2010-10-13 13:27:11Z, Subha Viswanathan$
 * $Date: 10/13/2010 8:27:11 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2008
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.db.slip;

/**
 * This is the POJO representation of SLIP.TRANSACTION
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 4$
 */
public class Transaction implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Transaction.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 4$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 21L;

	/**
	 * The unique identifier defining the transaction.
	 */
	private Long id = null;

	/**
	 * The unique identifier defining the slip reference.
	 */
	private Long slipReferenceId = null;

	/**
	 * The unique identifier defining the slip status.
	 */
	private Short statusFlagId = null;

	/**
	 * The unique identifier defining revenue post flag.
	 */
	private Short postFlagId = null;

	/**
	 * The unique identifier defining exception code.
	 */
	private Short exceptionCodeId = null;

	/**
	 * The unique message identifier for this slip.
	 */
	private Long messageId = null;

	/**
	 * The identifier defining the response code of the event.
	 */
	private String responseCode = null;

	/**
	 * The identifier defining the Last Coin In.
	 */
	private Long lastCoinIn = null;

	/**
	 * The identifier defining the total number of coins in for this slot machine.
	 */
	private Long gmuCoinInMeter = null;

	/**
	 * The identifier defining the slot door status at the time of the event.
	 */
	private String doorStatus = null;

	/**
	 * The printer used to print the Jackpot/Fill/Bleed/Beef Slip.
	 */
	private String printerUsed = null;

	/**
	 * SLIP.TRANSACTION.SLPX_EMPLOYEE_CARD_NO
	 */
	private String employeeCardNo = null;

	/**
	 * The unique identifier defining the employee id.
	 */
	private String employeeId = null;

	/**
	 * The unique identifier defining the employee first name.
	 */
	private String employeeFirstName = null;

	/**
	 * The unique identifier defining the employee last name
	 */
	private String employeeLastName = null;

	/**
	 * Date and time in which this record was created.
	 */
	private java.sql.Timestamp createdTs = null;

	/**
	 * The user responsible for creating this record. If null, this record was created by the system.
	 */
	private String createdUser = null;

	/**
	 * The unique identifier defining exception code.
	 */
	private ExceptionCode exceptionCode = null;

	/**
	 * The unique identifier defining revenue post flag
	 */
	private PostFlag postFlag = null;

	/**
	 * The unique number defining the slip reference table.
	 */
	private SlipReference slipReference = null;

	/**
	 * The unique identifier defining status flag.
	 */
	private StatusFlag statusFlag = null;
	
	/**
	 * Cash Desk Location from where jackpot is processed
	 */
	private String cashDeskLocation;

	/**
	 * The default constructor for POJO Transaction.
	 */
	public Transaction() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.Transaction" );
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return com.ballydev.sds.db.util.StringHelper.beanToString( this );
	}

	/**
	 * @return the id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the slipReferenceId.
	 */
	public Long getSlipReferenceId() {
		return this.slipReferenceId;
	}

	/**
	 * @param slipReferenceId the slipReferenceId to set
	 */
	public void setSlipReferenceId(Long slipReferenceId) {
		this.slipReferenceId = slipReferenceId;
	}

	/**
	 * @return the statusFlagId.
	 */
	public Short getStatusFlagId() {
		return this.statusFlagId;
	}

	/**
	 * @param statusFlagId the statusFlagId to set
	 */
	public void setStatusFlagId(Short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}

	/**
	 * @return the postFlagId.
	 */
	public Short getPostFlagId() {
		return this.postFlagId;
	}

	/**
	 * @param postFlagId the postFlagId to set
	 */
	public void setPostFlagId(Short postFlagId) {
		this.postFlagId = postFlagId;
	}

	/**
	 * @return the exceptionCodeId.
	 */
	public Short getExceptionCodeId() {
		return this.exceptionCodeId;
	}

	/**
	 * @param exceptionCodeId the exceptionCodeId to set
	 */
	public void setExceptionCodeId(Short exceptionCodeId) {
		this.exceptionCodeId = exceptionCodeId;
	}

	/**
	 * @return the messageId.
	 */
	public Long getMessageId() {
		return this.messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the responseCode.
	 */
	public String getResponseCode() {
		return this.responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the lastCoinIn.
	 */
	public Long getLastCoinIn() {
		return this.lastCoinIn;
	}

	/**
	 * @param lastCoinIn the lastCoinIn to set
	 */
	public void setLastCoinIn(Long lastCoinIn) {
		this.lastCoinIn = lastCoinIn;
	}

	/**
	 * @return the gmuCoinInMeter.
	 */
	public Long getGmuCoinInMeter() {
		return this.gmuCoinInMeter;
	}

	/**
	 * @param gmuCoinInMeter the gmuCoinInMeter to set
	 */
	public void setGmuCoinInMeter(Long gmuCoinInMeter) {
		this.gmuCoinInMeter = gmuCoinInMeter;
	}

	/**
	 * @return the doorStatus.
	 */
	public String getDoorStatus() {
		return this.doorStatus;
	}

	/**
	 * @param doorStatus the doorStatus to set
	 */
	public void setDoorStatus(String doorStatus) {
		this.doorStatus = doorStatus;
	}

	/**
	 * @return the printerUsed.
	 */
	public String getPrinterUsed() {
		return this.printerUsed;
	}

	/**
	 * @param printerUsed the printerUsed to set
	 */
	public void setPrinterUsed(String printerUsed) {
		this.printerUsed = printerUsed;
	}

	/**
	 * @return the employeeCardNo.
	 */
	public String getEmployeeCardNo() {
		return this.employeeCardNo;
	}

	/**
	 * @param employeeCardNo the employeeCardNo to set
	 */
	public void setEmployeeCardNo(String employeeCardNo) {
		this.employeeCardNo = employeeCardNo;
	}

	/**
	 * @return the employeeId.
	 */
	public String getEmployeeId() {
		return this.employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeFirstName.
	 */
	public String getEmployeeFirstName() {
		return this.employeeFirstName;
	}

	/**
	 * @param employeeFirstName the employeeFirstName to set
	 */
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	/**
	 * @return the employeeLastName.
	 */
	public String getEmployeeLastName() {
		return this.employeeLastName;
	}

	/**
	 * @param employeeLastName the employeeLastName to set
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	/**
	 * @return the createdTs.
	 */
	public java.sql.Timestamp getCreatedTs() {
		return this.createdTs;
	}

	/**
	 * @param createdTs the createdTs to set
	 */
	public void setCreatedTs(java.sql.Timestamp createdTs) {
		this.createdTs = createdTs;
	}

	/**
	 * @return the createdUser.
	 */
	public String getCreatedUser() {
		return this.createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	/**
	 * @return the exceptionCode.
	 */
	public ExceptionCode getExceptionCode() {
		return this.exceptionCode;
	}

	/**
	 * @param exceptionCode the exceptionCode to set
	 */
	public void setExceptionCode(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	/**
	 * @return the postFlag.
	 */
	public PostFlag getPostFlag() {
		return this.postFlag;
	}

	/**
	 * @param postFlag the postFlag to set
	 */
	public void setPostFlag(PostFlag postFlag) {
		this.postFlag = postFlag;
	}

	/**
	 * @return the slipReference.
	 */
	public SlipReference getSlipReference() {
		return this.slipReference;
	}

	/**
	 * @param slipReference the slipReference to set
	 */
	public void setSlipReference(SlipReference slipReference) {
		this.slipReference = slipReference;
	}

	/**
	 * @return the statusFlag.
	 */
	public StatusFlag getStatusFlag() {
		return this.statusFlag;
	}

	/**
	 * @param statusFlag the statusFlag to set
	 */
	public void setStatusFlag(StatusFlag statusFlag) {
		this.statusFlag = statusFlag;
	}
	/**
	 * Method to get the location from where the jackpot is processed
	 * @return
	 * @author vsubha
	 */
	public String getCashDeskLocation() {
		return cashDeskLocation;
	}
	/**
	 * Method to set the location from where the jackpot is processed
	 * @param cashDeskLocation
	 * @author vsubha
	 */
	public void setCashDeskLocation(String cashDeskLocation) {
		this.cashDeskLocation = cashDeskLocation;
	}

}
