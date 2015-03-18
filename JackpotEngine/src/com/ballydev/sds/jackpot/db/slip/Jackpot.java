/*****************************************************************************
 * $Id: Jackpot.java,v 1.8.1.0.1.0, 2013-10-12 08:39:47Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 3:39:47 AM$
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
 * This is the POJO representation of SLIP.JACKPOT
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 11$
 */
public class Jackpot implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Jackpot.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 11$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 27L;

	/**
	 * The unique identifier defining this jackpot slip.
	 */
	private Long id = null;

	/**
	 * The unique identifier defining this slip reference.
	 */
	private Long slipReferenceId = null;

	/**
	 * SLIP.JACKPOT.JPTY_ID
	 */
	private Short jackpotTypeId = null;

	/**
	 * The unique identifier defining the tax calculated for this slip.
	 */
	private Short taxTypeId = null;

	/**
	 * The jackpot identifier defining jackpot type.
	 */
	private String jackpotId = null;

	/**
	 * The original player card associated with this jackpot.
	 */
	private String associatedPlayerCard = null;

	/**
	 * The player card associated with the slip in case original player card IS incorrect or player card was not present during play.
	 */
	private String playerCard = null;

	/**
	 * The player name that was entered while processin the slip.
	 */
	private String playerName = null;

	/**
	 * The original value of the jackpot.
	 */
	private Long originalAmount = null;

	/**
	 * Machine paid jackpot amount.
	 */
	private Long machinePaidAmount = null;

	/**
	 * The hand paid jackpot amount.
	 */
	private Long hpjpAmount = null;

	/**
	 * The adjusted hand paid amount.
	 */
	private Long hpjpAmountRounded = null;

	/**
	 * The net jackpot amount, after tax deductions.
	 */
	private Long netAmount = null;

	/**
	 * The tax amount calculated for this slip.
	 */
	private Long taxAmount = null;

	/**
	 * The payline associated with the jackpot.
	 */
	private String payline = null;

	/**
	 * The physical or equivalent coins bet for this jackpot.
	 */
	private Integer coinsPlayed = null;

	/**
	 * The winning combinathing of this jackpot reported.
	 */
	private String winningCombination = null;

	/**
	 * The count on express jackpot blind attempts.
	 */
	private Short blindAttempt = null;
	
	/**
	 * The amount which is calculated for progressive jackpot.
	 */
	private Long calculatedSDSAmount = null;
	
	/**
	 * The amount which was prompted in the PMU Controller.
	 */
	private Long promptedPMUAmount = null;

	/**
	 * Flag mentioning whether PMU amount is used.
	 */
	private Integer pmuAmountUsed = null;
	
	/**
	 * Jackpot Check Expiry Date
	 */
	private java.sql.Timestamp expiryDate = null;
	
	/**
	 * Stores if the slot is online or not when the jackpot is hit.
	 * 0 - Offline Slot Message
	 * 1 - Online Slot Message
	 * @author vsubha
	 */
	private Short isSlotOnline = null;
	/**
	 * Progressive Level for Manual Progressive Jackpot
	 * @author vsubha
	 */
	private String progressiveLevel = null;
	
	/**
	 * Field that determines how the jackpot was generated 
	 * whether from Slot/System/Controller etc. - Refer Enum JpGeneratedBy
	 * @author dambereen
	 */
	private String generatedBy = null;
	
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
	 * The unique identifier defining jackpot type.
	 */
	private JackpotType jackpotType = null;

	/**
	 * The unique number defining the slip reference table.
	 */
	private SlipReference slipReference = null;

	/**
	 * The unique identifier defining tax type.
	 */
	private TaxType taxType = null;
	
	/**
	 * String which holds comma separated values of all tax rates & tax amounts.
	 */
	private String taxRateAmount = null;
	
	/**
	 * MVG Custom enhancement
	 */
	private Long interceptAmount = null;
	
	
	public long getInterceptAmount() {
		return this.interceptAmount;
	}
		
	public void setInterceptAmount(Long interceptAmount) {
		this.interceptAmount = interceptAmount;
	}	
	
	/**
	 * The default constructor for POJO Jackpot.
	 */
	public Jackpot() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.Jackpot" );
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
	 * @return the jackpotTypeId.
	 */
	public Short getJackpotTypeId() {
		return this.jackpotTypeId;
	}

	/**
	 * @param jackpotTypeId the jackpotTypeId to set
	 */
	public void setJackpotTypeId(Short jackpotTypeId) {
		this.jackpotTypeId = jackpotTypeId;
	}

	/**
	 * @return the taxTypeId.
	 */
	public Short getTaxTypeId() {
		return this.taxTypeId;
	}

	/**
	 * @param taxTypeId the taxTypeId to set
	 */
	public void setTaxTypeId(Short taxTypeId) {
		this.taxTypeId = taxTypeId;
	}

	/**
	 * @return the jackpotId.
	 */
	public String getJackpotId() {
		return this.jackpotId;
	}

	/**
	 * @param jackpotId the jackpotId to set
	 */
	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}

	/**
	 * @return the associatedPlayerCard.
	 */
	public String getAssociatedPlayerCard() {
		return this.associatedPlayerCard;
	}

	/**
	 * @param associatedPlayerCard the associatedPlayerCard to set
	 */
	public void setAssociatedPlayerCard(String associatedPlayerCard) {
		this.associatedPlayerCard = associatedPlayerCard;
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
	 * @return the playerName.
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the originalAmount.
	 */
	public Long getOriginalAmount() {
		return this.originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(Long originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the machinePaidAmount.
	 */
	public Long getMachinePaidAmount() {
		return this.machinePaidAmount;
	}

	/**
	 * @param machinePaidAmount the machinePaidAmount to set
	 */
	public void setMachinePaidAmount(Long machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}

	/**
	 * @return the hpjpAmount.
	 */
	public Long getHpjpAmount() {
		return this.hpjpAmount;
	}

	/**
	 * @param hpjpAmount the hpjpAmount to set
	 */
	public void setHpjpAmount(Long hpjpAmount) {
		this.hpjpAmount = hpjpAmount;
	}

	/**
	 * @return the hpjpAmountRounded.
	 */
	public Long getHpjpAmountRounded() {
		return this.hpjpAmountRounded;
	}

	/**
	 * @param hpjpAmountRounded the hpjpAmountRounded to set
	 */
	public void setHpjpAmountRounded(Long hpjpAmountRounded) {
		this.hpjpAmountRounded = hpjpAmountRounded;
	}

	/**
	 * @return the netAmount.
	 */
	public Long getNetAmount() {
		return this.netAmount;
	}

	/**
	 * @param netAmount the netAmount to set
	 */
	public void setNetAmount(Long netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the taxAmount.
	 */
	public Long getTaxAmount() {
		return this.taxAmount;
	}

	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(Long taxAmount) {
		this.taxAmount = taxAmount;
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
	 * @return the blindAttempt.
	 */
	public Short getBlindAttempt() {
		return this.blindAttempt;
	}

	/**
	 * @param blindAttempt the blindAttempt to set
	 */
	public void setBlindAttempt(Short blindAttempt) {
		this.blindAttempt = blindAttempt;
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
	 * @return the jackpotType.
	 */
	public JackpotType getJackpotType() {
		return this.jackpotType;
	}

	/**
	 * @param jackpotType the jackpotType to set
	 */
	public void setJackpotType(JackpotType jackpotType) {
		this.jackpotType = jackpotType;
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
	 * @return the taxType.
	 */
	public TaxType getTaxType() {
		return this.taxType;
	}

	/**
	 * @param taxType the taxType to set
	 */
	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public Long getCalculatedSDSAmount() {
		return calculatedSDSAmount;
	}

	public void setCalculatedSDSAmount(Long calculatedSDSAmount) {
		this.calculatedSDSAmount = calculatedSDSAmount;
	}

	public Integer getPmuAmountUsed() {
		return pmuAmountUsed;
	}

	public void setPmuAmountUsed(Integer pmuAmountUsed) {
		this.pmuAmountUsed = pmuAmountUsed;
	}

	public Long getPromptedPMUAmount() {
		return promptedPMUAmount;
	}

	public void setPromptedPMUAmount(Long promptedPMUAmount) {
		this.promptedPMUAmount = promptedPMUAmount;
	}

	/**
	 * Gets the jackpot check expiry date
	 * @return
	 * @author vsubha
	 */
	public java.sql.Timestamp getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the jackpot check expiry date
	 * @param expiryDate
	 * @author vsubha
	 */
	public void setExpiryDate(java.sql.Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * Method to get if slot is online when JP is hit
	 * 1 - Slot is online when JP is hit
	 * 0 - Slot is offline when JP is hit
	 * @return
	 * @author vsubha
	 */
	public Short getIsSlotOnline() {
		return isSlotOnline;
	}

	/**
	 * Method to set if slot is online when JP is hit
	 * 1 - Slot is online when JP is hit
	 * 0 - Slot is offline when JP is hit
	 * @param isSlotOnline
	 * @author vsubha
	 */
	public void setIsSlotOnline(Short isSlotOnline) {
		this.isSlotOnline = isSlotOnline;
	}

	/**
	 * Returns the progressive Level values for the Manual Progressive Jackpot
	 * @return
	 * @author vsubha
	 */
	public String getProgressiveLevel() {
		return progressiveLevel;
	}

	/**
	 * Sets the progressive Level values for the Manual Progressive Jackpot
	 * @param progressiveLevel
	 * @author vsubha
	 */
	public void setProgressiveLevel(String progressiveLevel) {
		this.progressiveLevel = progressiveLevel;
	}

	/**
	 * @return the generatedBy
	 */
	public String getGeneratedBy() {
		return generatedBy;
	}

	/**
	 * @param generatedBy the generatedBy to set
	 */
	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	/**
	 * @return the taxRateAmount
	 */
	public String getTaxRateAmount() {
		return taxRateAmount;
	}

	/**
	 * @param taxRateAmount the taxRateAmount to set
	 */
	public void setTaxRateAmount(String taxRateAmount) {
		this.taxRateAmount = taxRateAmount;
	}
	
}