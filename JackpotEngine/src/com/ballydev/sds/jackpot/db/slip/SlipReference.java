/*****************************************************************************
 * $Id: SlipReference.java,v 1.13, 2011-02-22 14:53:13Z, Subha Viswanathan$
 * $Date: 2/22/2011 8:53:13 AM$
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
 * This is the POJO representation of SLIP.SLIP_REFERENCE
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 14$
 */
public class SlipReference implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( SlipReference.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 14$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 32L;

	/**
	 * The unique number defining the slip reference table.
	 */
	private Long id = null;
	
	/**
	 * The unique identifier defining this last transaction.
	 */
	private Long transactionId = null;

	/**
	 * The unique number defining the slip type id.
	 */
	private Short slipTypeId = null;

	/**
	 * The unique number defining the status flag id.
	 */
	private Short statusFlagId = null;

	/**
	 * The unique number defining the process flag id.
	 */
	private Short processFlagId = null;

	/**
	 * The unique number defining the audit code id.
	 */
	private Short auditCodeId = null;

	/**
	 * The unique number defining the area short name.
	 */
	private String areaShortName = null;
	
	/**
	 * The unique number defining the area long name.
	 */
	private String areaLongName = null;

	/**
	 * The unique number defining the slot no.
	 */
	private String acnfNumber = null;

	/**
	 * The unique number defining the stand no.
	 */
	private String acnfLocation = null;
	
	/**
	 * Regulatory Id or Stamp Id
	 */
	private String sealNumber = null;

	/**
	 * The unique number defining the site.
	 */
	private Integer siteId = null;
	
	/**
	 * The number defining the site.
	 */
	private String siteNo = null;	

	/**
	 * The unique number defining the GAME Combo ID used.
	 */
	private Long gameComboId = null;

	/**
	 * The unique number defining the slot denomination.
	 */
	private Long slotDenomination = null;

	/**
	 * The unique number defining the GMU denomination.
	 */
	private Long gmuDenomination = null;

	/**
	 * The user defined field that describes the slot machine
	 */
	private String slotDescription = null;

	/**
	 * The unique sequence number for this slip.
	 */
	private Long sequenceNumber = null;

	/**
	 * The unique message identifier for this slip.
	 */
	private Long messageId = null;

	/**
	 * Cashless Account Type
	 */
	private String accountType = null;
	
	/**
	 * Cardless Account Number
	 */
	private String accountNumber = null;
	
	/**
	 * The date and time in which this transaction occurred.
	 */
	private java.sql.Timestamp transactionDate = null;
		
	/**
	 * Field that holds the value 1 if the record is a change record else it would be null or 0
	 */
	private Short changeFlag = null;

	/**
	 * Date and time in which this record was created.
	 */
	private java.sql.Timestamp createdTs = null;

	/**
	 * The user responsible for creating this record. If null, this record was created by the system.
	 */
	private String createdUser = null;

	/**
	 * Date and time in which this record was last modified.
	 */
	private java.sql.Timestamp updatedTs = null;

	/**
	 * The user responsible for last modifying this record. If null, this record was modified by the system.
	 */
	private String updatedUser = null;

	/**
	 * Date and time in which this record was requested to be deleted.
	 */
	private java.sql.Timestamp deletedTs = null;

	/**
	 * The user responsible for requesting to delete this record. If null, this record was requested to be deleted by the system.
	 */
	private String deletedUser = null;

	/**
	 * The unique identifier defining audit code.
	 */
	private AuditCode auditCode = null;

	/**
	 * The unique identifier defining process flag.
	 */
	private ProcessFlag processFlag = null;

	/**
	 * The unique identifier defining slip type.
	 */
	private SlipType slipType = null;

	/**
	 * The unique identifier defining status flag.
	 */
	private StatusFlag statusFlag = null;
	
	/**
	 * Flag to check if jackpot is posted to accounting.
	 */
	private String postToAccounting = null;

	/**
	 * SLIP.SLIP_REFERENCE.JACKPOT
	 */
	private java.util.Set jackpot = null;

	/**
	 * SLIP.SLIP_REFERENCE.SLIP
	 */
	private java.util.Set slip = null;

	/**
	 * SLIP.SLIP_REFERENCE.SLIP_DATA
	 */
	private java.util.Set slipData = null;

	/**
	 * SLIP.SLIP_REFERENCE.TRANSACTION
	 */
	private java.util.Set transaction = null;

	/**
	 * The default constructor for POJO SlipReference.
	 */
	public SlipReference() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.SlipReference" );
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
	 * @return
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the slipTypeId.
	 */
	public Short getSlipTypeId() {
		return this.slipTypeId;
	}

	/**
	 * @param slipTypeId the slipTypeId to set
	 */
	public void setSlipTypeId(Short slipTypeId) {
		this.slipTypeId = slipTypeId;
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
	 * @return the processFlagId.
	 */
	public Short getProcessFlagId() {
		return this.processFlagId;
	}

	/**
	 * @param processFlagId the processFlagId to set
	 */
	public void setProcessFlagId(Short processFlagId) {
		this.processFlagId = processFlagId;
	}

	/**
	 * @return the auditCodeId.
	 */
	public Short getAuditCodeId() {
		return this.auditCodeId;
	}

	/**
	 * @param auditCodeId the auditCodeId to set
	 */
	public void setAuditCodeId(Short auditCodeId) {
		this.auditCodeId = auditCodeId;
	}

	/**
	 * @return the areaLongName.
	 */
	public String getAreaLongName() {
		return this.areaLongName;
	}

	/**
	 * @param areaLongName the areaLongName to set
	 */
	public void setAreaLongName(String areaLongName) {
		this.areaLongName = areaLongName;
	}

	/**
	 * @return the acnfNumber.
	 */
	public String getAcnfNumber() {
		return this.acnfNumber;
	}

	/**
	 * @param acnfNumber the acnfNumber to set
	 */
	public void setAcnfNumber(String acnfNumber) {
		this.acnfNumber = acnfNumber;
	}

	/**
	 * @return the acnfLocation.
	 */
	public String getAcnfLocation() {
		return this.acnfLocation;
	}

	/**
	 * @param acnfLocation the acnfLocation to set
	 */
	public void setAcnfLocation(String acnfLocation) {
		this.acnfLocation = acnfLocation;
	}

	/**
	 * @return the siteId.
	 */
	public Integer getSiteId() {
		return this.siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the gameComboId.
	 */
	public Long getGameComboId() {
		return this.gameComboId;
	}

	/**
	 * @param gameComboId the gameComboId to set
	 */
	public void setGameComboId(Long gameComboId) {
		this.gameComboId = gameComboId;
	}

	/**
	 * @return the slotDenomination.
	 */
	public Long getSlotDenomination() {
		return this.slotDenomination;
	}

	/**
	 * @param slotDenomination the slotDenomination to set
	 */
	public void setSlotDenomination(Long slotDenomination) {
		this.slotDenomination = slotDenomination;
	}

	/**
	 * @return the gmuDenomination.
	 */
	public Long getGmuDenomination() {
		return this.gmuDenomination;
	}

	/**
	 * @param gmuDenomination the gmuDenomination to set
	 */
	public void setGmuDenomination(Long gmuDenomination) {
		this.gmuDenomination = gmuDenomination;
	}

	/**
	 * @return the slotDescription.
	 */
	public String getSlotDescription() {
		return this.slotDescription;
	}

	/**
	 * @param slotDescription the slotDescription to set
	 */
	public void setSlotDescription(String slotDescription) {
		this.slotDescription = slotDescription;
	}

	/**
	 * @return the sequenceNumber.
	 */
	public Long getSequenceNumber() {
		return this.sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	 * @return the transactionDate.
	 */
	public java.sql.Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(java.sql.Timestamp transactionDate) {
		this.transactionDate = transactionDate;
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
	 * @return the updatedTs.
	 */
	public java.sql.Timestamp getUpdatedTs() {
		return this.updatedTs;
	}

	/**
	 * @param updatedTs the updatedTs to set
	 */
	public void setUpdatedTs(java.sql.Timestamp updatedTs) {
		this.updatedTs = updatedTs;
	}

	/**
	 * @return the updatedUser.
	 */
	public String getUpdatedUser() {
		return this.updatedUser;
	}

	/**
	 * @param updatedUser the updatedUser to set
	 */
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	/**
	 * @return the deletedTs.
	 */
	public java.sql.Timestamp getDeletedTs() {
		return this.deletedTs;
	}

	/**
	 * @param deletedTs the deletedTs to set
	 */
	public void setDeletedTs(java.sql.Timestamp deletedTs) {
		this.deletedTs = deletedTs;
	}

	/**
	 * @return the deletedUser.
	 */
	public String getDeletedUser() {
		return this.deletedUser;
	}

	/**
	 * @param deletedUser the deletedUser to set
	 */
	public void setDeletedUser(String deletedUser) {
		this.deletedUser = deletedUser;
	}

	/**
	 * @return the auditCode.
	 */
	public AuditCode getAuditCode() {
		return this.auditCode;
	}

	/**
	 * @param auditCode the auditCode to set
	 */
	public void setAuditCode(AuditCode auditCode) {
		this.auditCode = auditCode;
	}

	/**
	 * @return the processFlag.
	 */
	public ProcessFlag getProcessFlag() {
		return this.processFlag;
	}

	/**
	 * @param processFlag the processFlag to set
	 */
	public void setProcessFlag(ProcessFlag processFlag) {
		this.processFlag = processFlag;
	}

	/**
	 * @return the slipType.
	 */
	public SlipType getSlipType() {
		return this.slipType;
	}

	/**
	 * @param slipType the slipType to set
	 */
	public void setSlipType(SlipType slipType) {
		this.slipType = slipType;
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
	 * @return the jackpot.
	 */
	public java.util.Set getJackpot() {
		return this.jackpot;
	}

	/**
	 * @param jackpot the jackpot to set
	 */
	public void setJackpot(java.util.Set jackpot) {
		this.jackpot = jackpot;
	}

	/**
	 * @return the slip.
	 */
	public java.util.Set getSlip() {
		return this.slip;
	}

	/**
	 * @param slip the slip to set
	 */
	public void setSlip(java.util.Set slip) {
		this.slip = slip;
	}

	/**
	 * @return the slipData.
	 */
	public java.util.Set getSlipData() {
		return this.slipData;
	}

	/**
	 * @param slipData the slipData to set
	 */
	public void setSlipData(java.util.Set slipData) {
		this.slipData = slipData;
	}

	/**
	 * @return the transaction.
	 */
	public java.util.Set getTransaction() {
		return this.transaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(java.util.Set transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the areaShortName
	 */
	public String getAreaShortName() {
		return areaShortName;
	}

	/**
	 * @param areaShortName the areaShortName to set
	 */
	public void setAreaShortName(String areaShortName) {
		this.areaShortName = areaShortName;
	}

	/**
	 * @return the changeFlag
	 */
	public Short getChangeFlag() {
		return changeFlag;
	}

	/**
	 * @param changeFlag the changeFlag to set
	 */
	public void setChangeFlag(Short changeFlag) {
		this.changeFlag = changeFlag;
	}

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	/**
	 * Method to get if jackpot is posted to accounting
	 * @return
	 * @author vsubha
	 */
	public String getPostToAccounting() {
		return postToAccounting;
	}

	/**
	 * Method to set if jackpot is posted to accounting
	 * @param postToAccounting
	 * @author vsubha
	 */
	public void setPostToAccounting(String postToAccounting) {
		this.postToAccounting = postToAccounting;
	}

	/**
	 * Method to get the cashless account type
	 * @return
	 * @author vsubha
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * Method to set the cashless account type
	 * @param accountType
	 * @author vsubha
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * Method to get the cashless account number
	 * @return
	 * @author vsubha
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * Method to set the cashless account number
	 * @param accountNumber
	 * @author vsubha
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Method to get the asset's regulatory Id
	 * @return
	 * @author vsubha
	 */
	public String getSealNumber() {
		return sealNumber;
	}

	/**
	 * Method to set the asset's regulatory Id
	 * @param sealNumber
	 * @author vsubha
	 */
	public void setSealNumber(String sealNumber) {
		this.sealNumber = sealNumber;
	}
	
}