/*****************************************************************************
 * $Id: JackpotFilter.java,v 1.2, 2008-09-22 04:17:56Z, Ambereen Drewitt$
 * $Date: 9/21/2008 11:17:56 PM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.filter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;

/**
 * A class to filter the methods based on slot number, stand number and sequence number
 * @author vijayrajm
 * @version $Revision: 3$
 */
public class JackpotFilter implements Serializable{
	
	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * List of slot nos
	 */
	private List<String> lstSlotNo;
	
	/**
	 * Field Type instance (Can be either Slot/Stand)
	 */
	private String fieldType ;
	/**
	 * Method Type instance
	 */
	private String type ;
	/**
	 * Slot Number instance
	 */
	private String slotNumber;
	/**
	 * Stand Number instance
	 */
	private String standNumber;
	/**
	 * Sequence Number instance
	 */
	private long sequenceNumber;
	/**
	 * Status Flag Id instance
	 */
	private short statusFlagId;	
	/**
	 * Site Id instance
	 */
	private int siteId;	
	
	/**
	 * numOfMinutes instance
	 */
	private int numOfMinutes;
	
	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	/**
	 * @return the slotNumber
	 */
	public String getSlotNumber() {
		return slotNumber;
	}
	/**
	 * @param slotNumber the slotNumber to set
	 */
	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}
	/**
	 * @return the standNumber
	 */
	public String getStandNumber() {
		return standNumber;
	}
	/**
	 * @param standNumber the standNumber to set
	 */
	public void setStandNumber(String standNumber) {
		this.standNumber = standNumber;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the statusFlagId
	 */
	public short getStatusFlagId() {
		return statusFlagId;
	}
	/**
	 * @param statusFlagId the statusFlagId to set
	 */
	public void setStatusFlagId(short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return the siteId
	 */
	public int getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the numOfMinutes
	 */
	public int getNumOfMinutes() {
		return numOfMinutes;
	}
	/**
	 * @param numOfMinutes the numOfMinutes to set
	 */
	public void setNumOfMinutes(int numOfMinutes) {
		this.numOfMinutes = numOfMinutes;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Object value = "";
		Method[] methods = this.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().startsWith("get")){
				try {
					value = methods[i].invoke(this, (Object[])null);
					if(value != null){
						log.info("Methods available: "+methods[i].getName()+" = "+value.toString());
					}
				} catch (IllegalArgumentException e) {
					log.error("IllegalArgument Exception occured while converting object to string");
				} catch (IllegalAccessException e) {
					log.error("IllegalAccess Exception occured while converting object to string");
				} catch (InvocationTargetException e) {
					log.error("InvocationTarget Exception occured while converting object to string");
				}
			}
		}		
		return super.toString();
	}
	/**
	 * @return the lstSlotNo
	 */
	public List<String> getLstSlotNo() {
		return lstSlotNo;
	}
	/**
	 * @param lstSlotNo the lstSlotNo to set
	 */
	public void setLstSlotNo(List<String> lstSlotNo) {
		this.lstSlotNo = lstSlotNo;
	}
	
	
}
