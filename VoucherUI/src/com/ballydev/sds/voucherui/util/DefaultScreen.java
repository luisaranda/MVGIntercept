/*****************************************************************************
 * $Id: DefaultScreen.java,v 1.0, 2010-07-06 06:37:52Z, Verma, Nitin Kumar$
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.VoucherCenterComposite;
import com.ballydev.sds.voucherui.composite.VoucherHeaderComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.controller.RedeemVoucherController;
import com.ballydev.sds.voucherui.form.RedeemVoucherForm;

/**
 * This class is used to open the default screen(i.e. Voucher Redeem screen)
 * @author VNitinkumar
 */
public class DefaultScreen {
	
	/**
	 * Instance of the voucher center composite
	 */
	private static VoucherCenterComposite centerComposite = null;
	
	private static boolean isOpen  = false;

	/** When the redeem process will complete, it will open default RedeemVoucher screen. */
	public static void openDefaultScreen(Composite comp, VoucherHeaderComposite vHeaderComp) {
		try	{
			
			if( !isOpen ) {
				if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REDEEM) ) {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
					}
	
					if(Util.isSmallerResolution()) {
						vHeaderComp.getLblRedeemVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG_SMALL));
						vHeaderComp.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL));
					} else {
						vHeaderComp.getLblRedeemVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG));
						vHeaderComp.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG));
					}
	
					ProgressIndicatorUtil.openInProgressWindow();
					new RedeemVoucherController(comp, SWT.NONE, new RedeemVoucherForm(), null, false);
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_REDEEM_SCREEN);
				} else {
					VoucherMiddleComposite.getCurrentComposite().dispose();
					centerComposite = new VoucherCenterComposite(comp, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_USER_NOACCESS));
					VoucherMiddleComposite.setCurrentComposite(centerComposite);
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_DUMMY_SCREEN);
				}
				isOpen = true;
			}
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param isOpen the isOpen to set
	 */
	public static void setOpen(boolean isOpen) {
		DefaultScreen.isOpen = isOpen;
	}
}
