/*****************************************************************************
 * $Id: MessageBoxEnum.java,v 1.0, 2008-03-27 12:18:24Z, Nithyakalyani, Raman$
 * $Date: 3/27/2008 6:18:24 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.util;

public enum MessageBoxEnum {
	OK,
	OKCANCEL,
	YESNO,
	YESNOCANCEL;
	private String[] buttonLabels;

	public String[] getButtonLabels() {
		return buttonLabels;
	}

	public void setButtonLabels(String[] buttonLabels) {
		this.buttonLabels = buttonLabels;
	}
	
	
	private MessageBoxEnum(){
		if( this.name().equals("OK")){
			buttonLabels = new String[]{"Ok"};
		}else if( this.name().equals("OKCANCEL") ){
			buttonLabels = new String[]{"Ok","Cancel"};
		}else if( this.name().equals("YESNO") ){
			buttonLabels = new String[]{"Yes","No"};
		}else if( this.name().equals("YESNOCANCEL") ){
			buttonLabels = new String[]{"YES","NO","Cancel"};
		}
	}
	
	
	
}
