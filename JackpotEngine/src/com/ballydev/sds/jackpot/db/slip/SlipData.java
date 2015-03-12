/*****************************************************************************
 * $Id: SlipData.java,v 1.3, 2011-02-22 14:52:13Z, Subha Viswanathan$
 * $Date: 2/22/2011 8:52:13 AM$
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
 * This is the POJO representation of SLIP.SLIP_DATA
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 4$
 */
public class SlipData implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( SlipData.class );

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
	public static final long serialVersionUID = 16L;

	/**
	 * The unique identifer defining the data associated with this slip printing.
	 */
	private Long id = null;

	/**
	 * The unique identifier defining this slip reference.
	 */
	private Long slipReferenceId = null;

	/**
	 * The employee logged into the system for this slip printing.
	 */
	private String actrLogin = null;

	/**
	 * The asset (kiosk or touch screen terminal) used to manipulate this slip.
	 */
	private String kioskProcessed = null;

	/**
	 * Employee identifier number for first authorizing signature for this slip printing.
	 */
	private String authEmployeeId1 = null;

	/**
	 * Employee identifier number for second authorizing signature for this slip printing.
	 */
	private String authEmployeeId2 = null;
	
	/**
	 * Employee identifier number for authorizing when the jackpot amount exceeds the amount
	 * specified in the site configuration
	 */
	private String amountAuthEmpId = null;

	/**
	 * The gaming shift on which this transaction was reported.
	 */
	private String shift = null;

	/**
	 * The physical window number where this jackpot amount was paid.
	 */
	private String windowNumber = null;

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
	 * The default constructor for POJO SlipData.
	 */
	public SlipData() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.SlipData" );
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
	 * @return the actrLogin.
	 */
	public String getActrLogin() {
		return this.actrLogin;
	}

	/**
	 * @param actrLogin the actrLogin to set
	 */
	public void setActrLogin(String actrLogin) {
		this.actrLogin = actrLogin;
	}

	/**
	 * @return the kioskProcessed.
	 */
	public String getKioskProcessed() {
		return this.kioskProcessed;
	}

	/**
	 * @param kioskProcessed the kioskProcessed to set
	 */
	public void setKioskProcessed(String kioskProcessed) {
		this.kioskProcessed = kioskProcessed;
	}

	/**
	 * @return the authEmployeeId1.
	 */
	public String getAuthEmployeeId1() {
		return this.authEmployeeId1;
	}

	/**
	 * @param authEmployeeId1 the authEmployeeId1 to set
	 */
	public void setAuthEmployeeId1(String authEmployeeId1) {
		this.authEmployeeId1 = authEmployeeId1;
	}

	/**
	 * @return the authEmployeeId2.
	 */
	public String getAuthEmployeeId2() {
		return this.authEmployeeId2;
	}

	/**
	 * @param authEmployeeId2 the authEmployeeId2 to set
	 */
	public void setAuthEmployeeId2(String authEmployeeId2) {
		this.authEmployeeId2 = authEmployeeId2;
	}

	/**
	 * @return the shift.
	 */
	public String getShift() {
		return this.shift;
	}

	/**
	 * @param shift the shift to set
	 */
	public void setShift(String shift) {
		this.shift = shift;
	}

	/**
	 * @return the windowNumber.
	 */
	public String getWindowNumber() {
		return this.windowNumber;
	}

	/**
	 * @param windowNumber the windowNumber to set
	 */
	public void setWindowNumber(String windowNumber) {
		this.windowNumber = windowNumber;
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

	/**
	 * @return
	 */
	public String getAmountAuthEmpId() {
		return amountAuthEmpId;
	}

	/**
	 * @param amountAuthEmpId
	 */
	public void setAmountAuthEmpId(String amountAuthEmpId) {
		this.amountAuthEmpId = amountAuthEmpId;
	}
}
