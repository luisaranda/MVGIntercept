/*****************************************************************************
 * $Id: JackpotSequence.java,v 1.0, 2008-06-06 06:30:31Z, Ambereen Drewitt$
 * $Date: 6/6/2008 1:30:31 AM$
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
 * This is the POJO representation of SLIP.JACKPOT_SEQUENCE
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 1$
 */
public class JackpotSequence implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( JackpotSequence.class );

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
	public static final long serialVersionUID = 9L;

	/**
	 * SLIP.JACKPOT_SEQUENCE.JPSE_ID
	 */
	private Long jpseId = null;


	/**
	 * The default constructor for POJO SlipTransfer.
	 */
	public JackpotSequence() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpot.db.slip.JackpotSequence" );
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
	 * @return the jpseId
	 */
	public Long getJpseId() {
		return jpseId;
	}

	/**
	 * @param jpseId the jpseId to set
	 */
	public void setJpseId(Long jpseId) {
		this.jpseId = jpseId;
	}

}
