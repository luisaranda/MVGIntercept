/*****************************************************************************
 * $Id: JackpotGameDetailsDTO.java,v 1.5, 2010-10-27 11:55:31Z, Subha Viswanathan$
 * $Date: 10/27/2010 6:55:31 AM$
 * $Log$
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
package com.ballydev.sds.jackpot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JackpotGameDetailsDTO class
 * @author anantharajr
 * @version $Revision: 6$
 */
public class JackpotGameDetailsDTO extends JackpotBaseDTO{

	
	private static final long serialVersionUID = -5098106191985438197L;

	/**
	 * Denomination amt
	 */
	private Double denomAmount;

	/**
	 * Denomination id
	 */
	private Long denomId;

	/**
	 * Game id
	 */
	private Long gameId;

	/**
	 * Paytable hold percent
	 */
	private Double paytabeHoldPercent;

	/**
	 * Paytable  id
	 */
	private Integer paytableId;

	/**
	 * Theme id
	 */
	private Long themeId;

	/**
	 * Theme name
	 */
	private String themeName;

	/**
	 * Wager id
	 */
	private Long wagerId;

	/**
	 * Wager Max Credits Bet
	 */
	private Integer wagerMaxCreditsBet;

	/**
	 * @return the denomAmount
	 */
	public Double getDenomAmount() {
		return denomAmount;
	}

	/**
	 * @param denomAmount the denomAmount to set
	 */
	public void setDenomAmount(Double denomAmount) {
		this.denomAmount = denomAmount;
	}

	/**
	 * @return the denomId
	 */
	public Long getDenomId() {
		return denomId;
	}

	/**
	 * @param denomId the denomId to set
	 */
	public void setDenomId(Long denomId) {
		this.denomId = denomId;
	}

	/**
	 * @return the gameId
	 */
	public Long getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the paytabeHoldPercent
	 */
	public Double getPaytabeHoldPercent() {
		return paytabeHoldPercent;
	}

	/**
	 * @param paytabeHoldPercent the paytabeHoldPercent to set
	 */
	public void setPaytabeHoldPercent(Double paytabeHoldPercent) {
		this.paytabeHoldPercent = paytabeHoldPercent;
	}

	/**
	 * @return the paytableId
	 */
	public Integer getPaytableId() {
		return paytableId;
	}

	/**
	 * @param paytableId the paytableId to set
	 */
	public void setPaytableId(Integer paytableId) {
		this.paytableId = paytableId;
	}

	/**
	 * @return the themeId
	 */
	public Long getThemeId() {
		return themeId;
	}

	/**
	 * @param themeId the themeId to set
	 */
	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}

	/**
	 * @return the themeName
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @param themeName the themeName to set
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return the wagerId
	 */
	public Long getWagerId() {
		return wagerId;
	}

	/**
	 * @param wagerId the wagerId to set
	 */
	public void setWagerId(Long wagerId) {
		this.wagerId = wagerId;
	}

	/**
	 * @return the wagerMaxCreditsBet
	 */
	public Integer getWagerMaxCreditsBet() {
		return wagerMaxCreditsBet;
	}

	/**
	 * @param wagerMaxCreditsBet the wagerMaxCreditsBet to set
	 */
	public void setWagerMaxCreditsBet(Integer wagerMaxCreditsBet) {
		this.wagerMaxCreditsBet = wagerMaxCreditsBet;
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
	
}
