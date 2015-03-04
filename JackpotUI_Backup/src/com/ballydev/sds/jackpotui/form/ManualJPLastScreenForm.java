/*****************************************************************************
 * $Id: ManualJPLastScreenForm.java,v 1.2, 2009-08-07 07:12:06Z, Devi Balusamy$
 * $Date: 8/7/2009 2:12:06 AM$
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
package com.ballydev.sds.jackpotui.form;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * ManualJPLastScreenForm
 * @author anantharajr
 * @version $Revision: 3$
 */
public class ManualJPLastScreenForm extends SDSForm {

	/**
	 * winningCombination
	 */
	private String winningCombination;
	
	/**
	 * payline 
	 */
	private String payline;
	
	/**
	 * coins played
	 */
	private String coinsPlayed;
	
	/**
	 * window number
	 */
	private String windowNo;
	
	/**
	 * player card
	 */
	private String playerCard;
	
	/**
	 * player name
	 */
	private String playerName;

	/**
	 * @return the coinsPlayed
	 */
	public String getCoinsPlayed() {
		return coinsPlayed;
	}

	/**
	 * @param coinsPlayed the coinsPlayed to set
	 */
	public void setCoinsPlayed(String coinsPlayed) {
		String oldValue = this.coinsPlayed;
		String newValue = coinsPlayed;
		this.coinsPlayed = coinsPlayed;
		firePropertyChange("coinsPlayed", oldValue, newValue);
	}

	/**
	 * @return the payline
	 */
	public String getPayline() {
		return payline;
	}

	/**
	 * @param payline the payline to set
	 */
	public void setPayline(String payline) {
		String oldValue = this.payline;
		String newValue = payline;
		this.payline = payline;
		firePropertyChange("payline", oldValue, newValue);	
	}

	/**
	 * @return the playerCard
	 */
	public String getPlayerCard() {
		if(playerCard!=null && playerCard.trim().length()>0) {
			playerCard = playerCard.replaceAll("\\s", "");
		}
		return playerCard;
	}

	/**
	 * @param playerCard the playerCard to set
	 */
	public void setPlayerCard(String playerCard) {
		String oldValue = this.playerCard;
		String newValue = playerCard;
		this.playerCard = playerCard;
		firePropertyChange("playerCard", oldValue, newValue);
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		String oldValue = this.playerName;
		String newValue = playerName;
		this.playerName = playerName;
		firePropertyChange("playerName", oldValue, newValue);
	}

	/**
	 * @return the windowNo
	 */
	public String getWindowNo() {
		return windowNo;
	}

	/**
	 * @param windowNo the windowNo to set
	 */
	public void setWindowNo(String windowNo) {
		String oldValue = this.windowNo;
		String newValue = windowNo;
		this.windowNo = windowNo;
		firePropertyChange("windowNo", oldValue, newValue);	
	}

	/**
	 * @return the winningCombination
	 */
	public String getWinningCombination() {
		return winningCombination;
	}

	/**
	 * @param winningCombination the winningCombination to set
	 */
	public void setWinningCombination(String winningCombination) {
		String oldValue = this.winningCombination;
		String newValue = winningCombination;
		this.winningCombination = winningCombination;
		firePropertyChange("winningCombination", oldValue, newValue);
	}
	
}
