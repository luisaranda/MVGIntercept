/*****************************************************************************
 * $Id: Slip.java,v 1.1, 2010-10-13 13:27:11Z, Subha Viswanathan$
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
 * This is the POJO representation of SLIP.SLIP
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 2$
 */
public class Slip implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Slip.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 2$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 12L;

	/**
	 * The unique identifier defining this slip.
	 */
	private Long id = null;

	/**
	 * The unique identifier defining this slip reference.
	 */
	private Integer slipReferenceId = null;

	/**
	 * Slip amount.
	 */
	private Long amount = null;

	/**
	 * The number identifying the area fill for this slip.
	 */
	private Long areaPrintNumber = null;

	/**
	 * The fill denomination associated for this slip.
	 */
	private Long fillDenomination = null;

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
	 * The unique number defining the slip reference table.
	 */
	private SlipReference slipReference = null;

	/**
	 * The default constructor for POJO Slip.
	 */
	public Slip() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.Slip" );
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
	public Integer getSlipReferenceId() {
		return this.slipReferenceId;
	}

	/**
	 * @param slipReferenceId the slipReferenceId to set
	 */
	public void setSlipReferenceId(Integer slipReferenceId) {
		this.slipReferenceId = slipReferenceId;
	}

	/**
	 * @return the amount.
	 */
	public Long getAmount() {
		return this.amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

	/**
	 * @return the areaPrintNumber.
	 */
	public Long getAreaPrintNumber() {
		return this.areaPrintNumber;
	}

	/**
	 * @param areaPrintNumber the areaPrintNumber to set
	 */
	public void setAreaPrintNumber(Long areaPrintNumber) {
		this.areaPrintNumber = areaPrintNumber;
	}

	/**
	 * @return the fillDenomination.
	 */
	public Long getFillDenomination() {
		return this.fillDenomination;
	}

	/**
	 * @param fillDenomination the fillDenomination to set
	 */
	public void setFillDenomination(Long fillDenomination) {
		this.fillDenomination = fillDenomination;
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

}
