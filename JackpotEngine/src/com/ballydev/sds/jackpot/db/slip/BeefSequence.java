/*****************************************************************************
 * $Id: BeefSequence.java,v 1.0, 2008-06-06 06:30:22Z, Ambereen Drewitt$
 * $Date: 6/6/2008 1:30:22 AM$
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
 * This is the POJO representation of SLIP.BEEF_SEQUENCE
 * <p>
 * !WARNING! - Do not manually modify this file.
 *
 * @author automatically created with HibernateTask
 * @version $Revision: 1$
 */
public class BeefSequence implements java.io.Serializable {

	/**
	 * The logging mechanism for this class.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BeefSequence.class );

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
	 * SLIP.BEEF_SEQUENCE.BESE_ID
	 */
	private Long beseId = null;


	/**
	 * The default constructor for POJO SlipTransfer.
	 */
	public BeefSequence() {
		super();
		logger.debug( "Constructing POJO com.ballydev.sds.jackpot.db.slip.BeefSequence" );
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
	 * @return the beseId
	 */
	public Long getBeseId() {
		return beseId;
	}

	/**
	 * @param beseId the beseId to set
	 */
	public void setBeseId(Long beseId) {
		this.beseId = beseId;
	}

}
