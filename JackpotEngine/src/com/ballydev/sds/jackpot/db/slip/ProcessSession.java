/*****************************************************************************
 * $Id: ProcessSession.java,v 1.8.3.0, 2011-09-12 18:36:53Z, Duenas, Samuel$
 * $Date: 9/12/2011 1:36:53 PM$
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
 * This is the POJO representation of SLIP.PROCESS_SESSION
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 10$
 */
public class ProcessSession implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ProcessSession.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 10$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 32L;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_ID
	 */
	private Long id = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_ACNF_NUMBER
	 */
	private String acnfNumber = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_ACNF_LOCATION
	 */
	private String acnfLocation = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_SITE_ID
	 */
	private Integer siteId = null;
	
	/**
	 * SLIP.PROCESS_SESSION.SLPR_ID
	 */
	private Long slipReferenceId = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_SESSION_STATUS
	 */
	private Integer sessionStatus = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_SEQUENCE_NUMBER
	 */
	private Long sequenceNumber = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_EMPLOYEE_CARD_NO
	 */
	private String employeeCardNo = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_EMPLOYEE_ID
	 */
	private Integer employeeId = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_EMPLOYEE_FIRST_NAME
	 */
	private String employeeFirstName = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_EMPLOYEE_LAST_NAME
	 */
	private String employeeLastName = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_REQUESTED_FLAG
	 */
	private String requestedFlag = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_PLAYER_CARD
	 */
	private String playerCard = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_JP_HIT_AMOUNT
	 */
	private Long jpHitAmount = null;

	private Long jpOriginalAmount = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_PAYLINE
	 */
	private String payline = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_WINNING_COMBINATION
	 */
	private String winningCombination = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_COINS_PLAYED
	 */
	private Integer coinsPlayed = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_SHIFT
	 */
	private Integer shift = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_POUCH_PAY
	 */
	private Integer pouchPay = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_AUTH_EMPLOYEE_CARD_NO
	 */
	private String authEmployeeCardNo = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_AUTH_EMPLOYEE_CARD_ID
	 */
	private Integer authEmployeeCardId = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_AUTH_EMPLOYEE_FIRST_NAME
	 */
	private String authEmployeeFirstName = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_AUTH_EMPLOYEE_LAST_NAME
	 */
	private String authEmployeeLastName = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_ERROR_MESSAGE
	 */
	private String errorMessage = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_PRINTER_SEL_NAME
	 */
	private String printerSelName = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_PRINTER_SEL
	 */
	private Integer printerSel = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_FINAL_MESSAGE
	 */
	private String finalMessage = null;
	
	/**
	 * SLIP.PROCESS_SESSION.PRSE_ATTEMPTS_NUMBER
	 */
	private Integer attemptsNumber = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_CREATED_TS
	 */
	private java.sql.Timestamp createdTs = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_CREATED_USER
	 */
	private String createdUser = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_UPDATED_TS
	 */
	private java.sql.Timestamp updatedTs = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_UPDATED_USER
	 */
	private String updatedUser = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_DELETED_TS
	 */
	private java.sql.Timestamp deletedTs = null;

	/**
	 * SLIP.PROCESS_SESSION.PRSE_DELETED_USER
	 */
	private String deletedUser = null;
	
	
	//Manual Fields added to the POJO.
	private String areaLongName=null;
	
	private int jackpotTypeId=0;
	
	private int jackpotProcessFlagId=0;
	
	//Commenting this since these are not needed since files are in server side itself.
	/*private String printerSchema=null;
	
	private String slipSchema=null;*/
	
	private String jackpotId=null;
	
	private String assetLineAddress=null;
	
	private String playerName=null;
	
	
	//Additional Fields Added to the POJO for accounting posting purpose.
		
	private String siteNumberUsed=null;
	
	private Short auditCodeId = null;
	
	private String auditCodeDesc = null;
	
//	private Integer slipReferenceId= null;
	
	private Long gmuDenomination = null;
	
	private Long transactionDate = null;
	
	private String jackpotTypeDesc = null;
	
	private String jackpotProcessDesc  = null;
	
	private Short slipTypeId = null;
	
	private String slipTypeDesc = null;
	
	private Short statusFlagId = null;
	
	private String statusFlagDesc = null;
	
	/**
	 * The Jackpot table primary key.
	 */
	private String slipIdUsed = null;
	
	private String languageSelectedHere  = null;
	
	private String sealNumberUsed = null;
	
	public String getSealNumberUsed() {
		return sealNumberUsed;
	}

	public void setSealNumberUsed(String sealNumberUsed) {
		this.sealNumberUsed = sealNumberUsed;
	}

	public String getLanguageSelectedHere() {
		return languageSelectedHere;
	}

	public void setLanguageSelectedHere(String languageSelectedHere) {
		this.languageSelectedHere = languageSelectedHere;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getAssetLineAddress() {
		return assetLineAddress;
	}

	public void setAssetLineAddress(String assetLineAddress) {
		this.assetLineAddress = assetLineAddress;
	}

	public String getJackpotId() {
		return jackpotId;
	}

	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}

	/*public String getPrinterSchema() {
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
	}*/

	public int getJackpotProcessFlagId() {
		return jackpotProcessFlagId;
	}

	public void setJackpotProcessFlagId(int jackpotProcessFlagId) {
		this.jackpotProcessFlagId = jackpotProcessFlagId;
	}

	public int getJackpotTypeId() {
		return jackpotTypeId;
	}

	public void setJackpotTypeId(int jackpotTypeId) {
		this.jackpotTypeId = jackpotTypeId;
	}

	/**
	 * The default constructor for POJO ProcessSession.
	 */
	public ProcessSession() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpot.db.slip.ProcessSession" );
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
	 * @return the sessionStatus.
	 */
	public Integer getSessionStatus() {
		return this.sessionStatus;
	}

	/**
	 * @param sessionStatus the sessionStatus to set
	 */
	public void setSessionStatus(Integer sessionStatus) {
		this.sessionStatus = sessionStatus;
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
	public Integer getEmployeeId() {
		return this.employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Integer employeeId) {
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
	 * @return the requestedFlag.
	 */
	public String getRequestedFlag() {
		return this.requestedFlag;
	}

	/**
	 * @param requestedFlag the requestedFlag to set
	 */
	public void setRequestedFlag(String requestedFlag) {
		this.requestedFlag = requestedFlag;
	}

	/**
	 * @return the playerCard.
	 */
	public String getPlayerCard() {
		return this.playerCard;
	}

	/**
	 * @param playerCard the playerCard to set
	 */
	public void setPlayerCard(String playerCard) {
		this.playerCard = playerCard;
	}

	/**
	 * @return the jpHitAmount.
	 */
	public Long getJpHitAmount() {
		return this.jpHitAmount;
	}

	/**
	 * @param jpHitAmount the jpHitAmount to set
	 */
	public void setJpHitAmount(Long jpHitAmount) {
		this.jpHitAmount = jpHitAmount;
	}

	/**
	 * @return the payline.
	 */
	public String getPayline() {
		return this.payline;
	}

	/**
	 * @param payline the payline to set
	 */
	public void setPayline(String payline) {
		this.payline = payline;
	}

	/**
	 * @return the winningCombination.
	 */
	public String getWinningCombination() {
		return this.winningCombination;
	}

	/**
	 * @param winningCombination the winningCombination to set
	 */
	public void setWinningCombination(String winningCombination) {
		this.winningCombination = winningCombination;
	}

	/**
	 * @return the coinsPlayed.
	 */
	public Integer getCoinsPlayed() {
		return this.coinsPlayed;
	}

	/**
	 * @param coinsPlayed the coinsPlayed to set
	 */
	public void setCoinsPlayed(Integer coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}

	/**
	 * @return the shift.
	 */
	public Integer getShift() {
		return this.shift;
	}

	/**
	 * @param shift the shift to set
	 */
	public void setShift(Integer shift) {
		this.shift = shift;
	}

	/**
	 * @return the pouchPay.
	 */
	public Integer getPouchPay() {
		return this.pouchPay;
	}

	/**
	 * @param pouchPay the pouchPay to set
	 */
	public void setPouchPay(Integer pouchPay) {
		this.pouchPay = pouchPay;
	}

	/**
	 * @return the authEmployeeCardNo.
	 */
	public String getAuthEmployeeCardNo() {
		return this.authEmployeeCardNo;
	}

	/**
	 * @param authEmployeeCardNo the authEmployeeCardNo to set
	 */
	public void setAuthEmployeeCardNo(String authEmployeeCardNo) {
		this.authEmployeeCardNo = authEmployeeCardNo;
	}

	/**
	 * @return the authEmployeeCardId.
	 */
	public Integer getAuthEmployeeCardId() {
		return this.authEmployeeCardId;
	}

	/**
	 * @param authEmployeeCardId the authEmployeeCardId to set
	 */
	public void setAuthEmployeeCardId(Integer authEmployeeCardId) {
		this.authEmployeeCardId = authEmployeeCardId;
	}

	/**
	 * @return the authEmployeeFirstName.
	 */
	public String getAuthEmployeeFirstName() {
		return this.authEmployeeFirstName;
	}

	/**
	 * @param authEmployeeFirstName the authEmployeeFirstName to set
	 */
	public void setAuthEmployeeFirstName(String authEmployeeFirstName) {
		this.authEmployeeFirstName = authEmployeeFirstName;
	}

	/**
	 * @return the authEmployeeLastName.
	 */
	public String getAuthEmployeeLastName() {
		return this.authEmployeeLastName;
	}

	/**
	 * @param authEmployeeLastName the authEmployeeLastName to set
	 */
	public void setAuthEmployeeLastName(String authEmployeeLastName) {
		this.authEmployeeLastName = authEmployeeLastName;
	}

	/**
	 * @return the errorMessage.
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the printerSelName.
	 */
	public String getPrinterSelName() {
		return this.printerSelName;
	}

	/**
	 * @param printerSelName the printerSelName to set
	 */
	public void setPrinterSelName(String printerSelName) {
		this.printerSelName = printerSelName;
	}

	/**
	 * @return the printerSel.
	 */
	public Integer getPrinterSel() {
		return this.printerSel;
	}

	/**
	 * @param printerSel the printerSel to set
	 */
	public void setPrinterSel(Integer printerSel) {
		this.printerSel = printerSel;
	}

	/**
	 * @return the finalMessage.
	 */
	public String getFinalMessage() {
		return this.finalMessage;
	}

	/**
	 * @param finalMessage the finalMessage to set
	 */
	public void setFinalMessage(String finalMessage) {
		this.finalMessage = finalMessage;
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

	public String getAreaLongName() {
		return areaLongName;
	}

	public void setAreaLongName(String areaLongName) {
		this.areaLongName = areaLongName;
	}

	public String getAuditCodeDesc() {
		return auditCodeDesc;
	}

	public void setAuditCodeDesc(String auditCodeDesc) {
		this.auditCodeDesc = auditCodeDesc;
	}

	public Short getAuditCodeId() {
		return auditCodeId;
	}

	public void setAuditCodeId(Short auditCodeId) {
		this.auditCodeId = auditCodeId;
	}

	public Long getGmuDenomination() {
		return gmuDenomination;
	}

	public void setGmuDenomination(Long gmuDenomination) {
		this.gmuDenomination = gmuDenomination;
	}

	public String getJackpotProcessDesc() {
		return jackpotProcessDesc;
	}

	public void setJackpotProcessDesc(String jackpotProcessDesc) {
		this.jackpotProcessDesc = jackpotProcessDesc;
	}

	public String getJackpotTypeDesc() {
		return jackpotTypeDesc;
	}

	public void setJackpotTypeDesc(String jackpotTypeDesc) {
		this.jackpotTypeDesc = jackpotTypeDesc;
	}

	public String getSiteNumberUsed() {
		return siteNumberUsed;
	}

	public void setSiteNumberUsed(String siteNumberUsed) {
		this.siteNumberUsed = siteNumberUsed;
	}

	public Long getSlipReferenceId() {
		return slipReferenceId;
	}

	public void setSlipReferenceId(Long slipReferenceId) {
		this.slipReferenceId = slipReferenceId;
	}

	public String getSlipTypeDesc() {
		return slipTypeDesc;
	}

	public void setSlipTypeDesc(String slipTypeDesc) {
		this.slipTypeDesc = slipTypeDesc;
	}

	public Short getSlipTypeId() {
		return slipTypeId;
	}

	public void setSlipTypeId(Short slipTypeId) {
		this.slipTypeId = slipTypeId;
	}

	public String getStatusFlagDesc() {
		return statusFlagDesc;
	}

	public void setStatusFlagDesc(String statusFlagDesc) {
		this.statusFlagDesc = statusFlagDesc;
	}

	public Short getStatusFlagId() {
		return statusFlagId;
	}

	public void setStatusFlagId(Short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}

	public Long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getSlipIdUsed() {
		return slipIdUsed;
	}

	public void setSlipIdUsed(String slipIdUsed) {
		this.slipIdUsed = slipIdUsed;
	}

	public Integer getAttemptsNumber() {
		return attemptsNumber;
	}

	public void setAttemptsNumber(Integer attemptsNumber) {
		this.attemptsNumber = attemptsNumber;
	}

	public Long getJpOriginalAmount() {
		return jpOriginalAmount;
	}

	public void setJpOriginalAmount(Long jpOriginalAmount) {
		this.jpOriginalAmount = jpOriginalAmount;
	}
	
}
