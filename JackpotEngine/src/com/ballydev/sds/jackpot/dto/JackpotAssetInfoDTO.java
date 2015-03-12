package com.ballydev.sds.jackpot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Jackpot Asset Info local DTO.
 * @author anantharajr
 * @version $Revision: 8$
 */
public class JackpotAssetInfoDTO extends JackpotBaseDTO{

	
	private static final long serialVersionUID = -4538603486087899536L;
	
	/**
	 * Asset Created Ts
	 */
	private long createdTs;
	
	/**
	 * Seal No / Asset Regulatory Id
	 */
	private String sealNumber;
	
	/**
	 * areaShortName
	 */
	private String areaShortName;
	
	/**
	 * Slot Type Description
	 */
	private String slotTypeDescription;
	
	/**
	 * Slot Denomination
	 */
	private String slotDenomination;
	
	/**
	 * Accounting Denom (GMU Denom)
	 */
	private String accountingDenom;
	
	/**
	 * areaLongName
	 */
	private String areaLongName;

	/**
	 * Stand no / Slot location no
	 */
	private String assetConfigLocation;

	/**
	 * Slot no
	 */
	private String assetConfigNumber;

	/**
	 * Slot status
	 */
	private String assetConfigStatus;

	/**
	 * Error code
	 */
	private int errorCode;

	/**
	 * Game type
	 */
	private String gameType;

	/**
	 * Hopper enabled field
	 */
	private String hopperEnabled;

	/**
	 * List of game details
	 */
	private List<JackpotGameDetailsDTO> listGameDetails;

	/**
	 * Multi denomination field
	 */
	private String multiDenom;

	/**
	 * Multi game field
	 */
	private String multiGame;

	/**
	 * Field to determine if the slot supports progressive
	 */
	private String progressive;

	/**
	 * Site id
	 */
	private Integer siteId;

	/**
	 * @return the areaLongName
	 */
	public String getAreaLongName() {
		return areaLongName;
	}

	/**
	 * @param areaLongName the areaLongName to set
	 */
	public void setAreaLongName(String areaLongName) {
		this.areaLongName = areaLongName;
	}

	/**
	 * @return the assetConfigLocation
	 */
	public String getAssetConfigLocation() {
		return assetConfigLocation;
	}

	/**
	 * @param assetConfigLocation the assetConfigLocation to set
	 */
	public void setAssetConfigLocation(String assetConfigLocation) {
		this.assetConfigLocation = assetConfigLocation;
	}

	/**
	 * @return the assetConfigNumber
	 */
	public String getAssetConfigNumber() {
		return assetConfigNumber;
	}

	/**
	 * @param assetConfigNumber the assetConfigNumber to set
	 */
	public void setAssetConfigNumber(String assetConfigNumber) {
		this.assetConfigNumber = assetConfigNumber;
	}

	/**
	 * @return the assetConfigStatus
	 */
	public String getAssetConfigStatus() {
		return assetConfigStatus;
	}

	/**
	 * @param assetConfigStatus the assetConfigStatus to set
	 */
	public void setAssetConfigStatus(String assetConfigStatus) {
		this.assetConfigStatus = assetConfigStatus;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the gameType
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 * @return the hopperEnabled
	 */
	public String getHopperEnabled() {
		return hopperEnabled;
	}

	/**
	 * @param hopperEnabled the hopperEnabled to set
	 */
	public void setHopperEnabled(String hopperEnabled) {
		this.hopperEnabled = hopperEnabled;
	}

	/**
	 * @return the multiDenom
	 */
	public String getMultiDenom() {
		return multiDenom;
	}

	/**
	 * @param multiDenom the multiDenom to set
	 */
	public void setMultiDenom(String multiDenom) {
		this.multiDenom = multiDenom;
	}

	/**
	 * @return the multiGame
	 */
	public String getMultiGame() {
		return multiGame;
	}

	/**
	 * @param multiGame the multiGame to set
	 */
	public void setMultiGame(String multiGame) {
		this.multiGame = multiGame;
	}

	/**
	 * @return the progressive
	 */
	public String getProgressive() {
		return progressive;
	}

	/**
	 * @param progressive the progressive to set
	 */
	public void setProgressive(String progressive) {
		this.progressive = progressive;
	}

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the listGameDetails
	 */
	public List<JackpotGameDetailsDTO> getListGameDetails() {
		return listGameDetails;
	}

	/**
	 * @param listGameDetails the listGameDetails to set
	 */
	public void setListGameDetails(List<JackpotGameDetailsDTO> listGameDetails) {
		this.listGameDetails = listGameDetails;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Object value = "";
		StringBuilder strBuilder = new StringBuilder();
		Method[] methods = this.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().startsWith("get")){
				try {
					value = methods[i].invoke(this, (Object[])null);
					if(value != null){
						strBuilder.append("Methods available: "+methods[i].getName()+" = "+value.toString());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}		
		return strBuilder.toString();
	}

	/**
	 * @return the accountingDenom
	 */
	public String getAccountingDenom() {
		return accountingDenom;
	}

	/**
	 * @param accountingDenom the accountingDenom to set
	 */
	public void setAccountingDenom(String accountingDenom) {
		this.accountingDenom = accountingDenom;
	}

	/**
	 * @return the slotDenomination
	 */
	public String getSlotDenomination() {
		return slotDenomination;
	}

	/**
	 * @param slotDenomination the slotDenomination to set
	 */
	public void setSlotDenomination(String slotDenomination) {
		this.slotDenomination = slotDenomination;
	}

	/**
	 * @return the slotTypeDescription
	 */
	public String getSlotTypeDescription() {
		return slotTypeDescription;
	}

	/**
	 * @param slotTypeDescription the slotTypeDescription to set
	 */
	public void setSlotTypeDescription(String slotTypeDescription) {
		this.slotTypeDescription = slotTypeDescription;
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
	 * @return the sealNumber
	 */
	public String getSealNumber() {
		return sealNumber;
	}

	/**
	 * @param sealNumber the sealNumber to set
	 */
	public void setSealNumber(String sealNumber) {
		this.sealNumber = sealNumber;
	}

	/**
	 * @return the createdTs
	 */
	public long getCreatedTs() {
		return createdTs;
	}

	/**
	 * @param createdTs the createdTs to set
	 */
	public void setCreatedTs(long createdTs) {
		this.createdTs = createdTs;
	}
	
}
