/*****************************************************************************
 * $Id: PendingJackpotDisplayForm.java,v 1.0, 2008-04-03 15:51:48Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:51:48 AM$
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

import java.util.ArrayList;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpot.dto.JackpotDTO;

/**
 * This Display Form contains the properties of Display Composite 
 * @author vijayrajm
 * @version $Revision: 1$
 */
public class PendingJackpotDisplayForm extends SDSForm {

	/**
	 * ArrayList of JackpotDTO instance
	 */
	private ArrayList<JackpotDTO> dispJackpotDTO;

	
	/**
	 * @return the dispJackpotSlipDTO
	 */
	public ArrayList<JackpotDTO> getDispJackpotDTO() {
		return dispJackpotDTO;
	}

	/**
	 * @param dispJackpotSlipDTO the dispJackpotSlipDTO to set
	 */
	public void setDispJackpotDTO(ArrayList<JackpotDTO> dispJackpotSlipDTO) {
		ArrayList<JackpotDTO> oldArrayList=this.dispJackpotDTO;
		ArrayList<JackpotDTO> newArrayList=dispJackpotSlipDTO;
		this.dispJackpotDTO = dispJackpotSlipDTO;
		firePropertyChange("dispJackpotDTO", oldArrayList, newArrayList);
		
	}
	
	
}
