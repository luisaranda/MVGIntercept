/*****************************************************************************
 * $Id: JackpotSlot.java,v 1.0, 2010-03-03 13:14:37Z, Ambereen Drewitt$
 * $Date: 3/3/2010 7:14:37 AM$
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

import com.ballydev.sds.db.util.StringHelper;

/**
 * This is the POJO representation of SLIP.JACKPOT_SLOT
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 1$
 */
public class JackpotSlot implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( JackpotSlot.class );

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
	 * The unique identifier defining slot number.
	 */
	private String acnfNumber = null;

	/**
	 * Date and time in which this record was created.
	 */
	private java.sql.Timestamp createdTs = null;

	/**
	 * The user responsible for creating this record. If null, this record was created by the system.
	 */
	private String createdUser = null;


	/**
	 * The default constructor for POJO AuditCode.
	 */
	public JackpotSlot() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpotengine.db.slip.JackpotSlot" );
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return StringHelper.beanToString( this );
	}

	/**
	 * @return the acnfNumber
	 */
	public String getAcnfNumber() {
		return acnfNumber;
	}

	/**
	 * @param acnfNumber the acnfNumber to set
	 */
	public void setAcnfNumber(String acnfNumber) {
		this.acnfNumber = acnfNumber;
	}

	/**
	 * @return the createdTs
	 */
	public java.sql.Timestamp getCreatedTs() {
		return createdTs;
	}

	/**
	 * @param createdTs the createdTs to set
	 */
	public void setCreatedTs(java.sql.Timestamp createdTs) {
		this.createdTs = createdTs;
	}

	/**
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

}
