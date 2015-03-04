/*****************************************************************************
 * $Id: VoucherImageRegistry.java,v 1.0, 2010-07-06 06:37:52Z, Verma, Nitin Kumar$
 * $Date: 7/6/2010 1:37:52 AM$
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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.Activator;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IImageConstants;

/**
 * This class controls all aspects of the images of the entire voucher application's execution.
 * @author VNitinkumar
 */
public class VoucherImageRegistry  {
	
	Image redeemVActive 	= null;
	Image redeemVInactive 	= null;
	Image redeemVDisabled 	= null;
	Image overrideVActive 	= null;
	Image overrideVInactive = null;
	Image overrideVDisabled = null;
	
	private static boolean imagesLoaded = false;

	public void loadImages() {

		if( !imagesLoaded ) {
			if( Util.isSmallerResolution() ) {
				redeemVActive     = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG_SMALL));
				redeemVInactive   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG_SMALL));
				redeemVDisabled   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_DISABLED_BUTTON_IMG));
				overrideVActive   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG_SMALL));
				overrideVInactive = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL));
				overrideVDisabled = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG_SMALL));
				
			}
			else {
				redeemVActive     = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG));
				redeemVInactive   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG));
				redeemVDisabled   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_DISABLED_BUTTON_IMG));
				overrideVActive   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG));
				overrideVInactive = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG));
				overrideVDisabled = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
			}

			Activator.getPlugin().getImageRegistry().put(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG, redeemVActive);
			Activator.getPlugin().getImageRegistry().put(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG, redeemVInactive);
			Activator.getPlugin().getImageRegistry().put(IImageConstants.REDEEM_VOUCHER_DISABLED_BUTTON_IMG, redeemVDisabled);
			Activator.getPlugin().getImageRegistry().put(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG, overrideVActive);
			Activator.getPlugin().getImageRegistry().put(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG, overrideVInactive);
			Activator.getPlugin().getImageRegistry().put(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG, overrideVDisabled);
			imagesLoaded = true;
		}
	}
	
	public static Image getImage(String key ) {
		return Activator.getPlugin().getImageRegistry().get(key);
	}
}
