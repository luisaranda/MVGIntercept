/*****************************************************************************
 * $Id: StatusFlag.java,v 1.0, 2008-04-02 13:29:57Z, Ambereen Drewitt$
 * $Date: 4/2/2008 7:29:57 AM$
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
 * This is the POJO representation of SLIP.STATUS_FLAG
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 1$
 */
public class StatusFlag implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( StatusFlag.class );

	/**
	 * The version of this class.
	 */
	public static final String VERSION = "$Revision: 1$"; //$NON-NLS-1$

	/**
	 * The serialization runtime associates with each serializable class a
	 * version number, called a serialVersionUID, which is used during 
	 * deserialization to verify that the sender and receiver of a serialized
	 * object have loaded classes for that object that are compatible with 
	 * respect to serialization.
	 */
	public static final long serialVersionUID = 10L;

	/**
	 * The unique identifier defining status flag.
	 */
	private Short id = null;

	/**
	 * Description of the status flag.
	 */
	private String flagDescription = null;

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
	 * SLIP.STATUS_FLAG.SLIP_REFERENCE
	 */
	private java.util.Set slipReference = null;

	/**
	 * SLIP.STATUS_FLAG.TRANSACTION
	 */
	private java.util.Set transaction = null;

	/**
	 * The default constructor for POJO StatusFlag.
	 */
	public StatusFlag() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.StatusFlag" );
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
	public Short getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return the flagDescription.
	 */
	public String getFlagDescription() {
		return this.flagDescription;
	}

	/**
	 * @param flagDescription the flagDescription to set
	 */
	public void setFlagDescription(String flagDescription) {
		this.flagDescription = flagDescription;
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
	public java.util.Set getSlipReference() {
		return this.slipReference;
	}

	/**
	 * @param slipReference the slipReference to set
	 */
	public void setSlipReference(java.util.Set slipReference) {
		this.slipReference = slipReference;
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

}
