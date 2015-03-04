/*****************************************************************************
 * $Id: IImageConstants.java,v 1.32, 2010-07-06 06:37:52Z, Verma, Nitin Kumar$
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

package com.ballydev.sds.voucherui.constants;

import com.ballydev.sds.framework.util.Util;

/**
 * Constants interface that contains the image paths for all the label and button images.
 */
public interface IImageConstants
{
	public   String  VOUCHER_UI_BUTTON_IMG="/images/VoucherIcon.png";

	public String LOGOUT_BUTTON_IMG= "/images/LogoutIcon.png";

	public String EXIT_BUTTON_IMG= "/images/ExitIcon.png";

	public String PRINT_VOUCHER_ACTIVE_BUTTON_IMG= "/images/bluetheme/Print_Active.png";
	
	public String PRINT_VOUCHER_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Print_Active_Small.png";

	public String PRINT_VOUCHER_INACTIVE_BUTTON_IMG= "/images/bluetheme/Print_Inactive.png";
	
	public String PRINT_VOUCHER_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Print_Inactive_Small.png";

	public String IMAGE_TOUCH_SCREEN_KEYBOARD = "/images/KeyboardIcon.png";

	public String PRINT_VOUCHER_DISABLED_BUTTON_IMG= "/images/V_PrintVoucher_Disabled.png";

	public String RECONCILIATION_VOUCHER_ACTIVE_BUTTON_IMG= "/images/bluetheme/Reconciliation_Active.png";

	public String RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG= "/images/bluetheme/Reconciliation_Inactive.PNG";

	public String RECONCILIATION_VOUCHER_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Reconciliation_Active_Small.png";

	public String RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Reconciliation_Inactive_Small.png";
	
	public String RECONCILIATION_VOUCHER__DISABLED_BUTTON_IMG= "/images/V_Reconciliation_Disabled.png";

	public String REDEEM_VOUCHER_ACTIVE_BUTTON_IMG= "/images/bluetheme/Redeem_Active.png";//"/images/V_RedeemVoucher_Active.png";

	public String REDEEM_VOUCHER_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Redeem_Inactive_Small.png";//"/images/V_RedeemVoucher_Inactive.png";

	public String REDEEM_VOUCHER_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Redeem_Active_Small.png";//"/images/V_RedeemVoucher_Active.png";

	public String REDEEM_VOUCHER_INACTIVE_BUTTON_IMG= "/images/bluetheme/Redeem_Inactive.png";//"/images/V_RedeemVoucher_Inactive.png";

	
	public String REDEEM_VOUCHER_DISABLED_BUTTON_IMG ="/images/V_RedeemVoucher_Disabled.png";



	public String REPRINT_VOUCHER_ACTIVE_BUTTON_IMG= "/images/V_Reprint_Active.png";

	public String REPRINT_VOUCHER_INACTIVE_BUTTON_IMG= "/images/V_Reprint_Inactive.png";

	public String REPRINT_VOUCHER_DISABLED_BUTTON_IMG ="/images/V_Reprint_Disabled.png";

	public String REPORT_ACTIVE_BUTTON_IMG= "/images/bluetheme/Report_Active.png";

	public String REPORT_INACTIVE_BUTTON_IMG= "/images/bluetheme/Report_Inactive.png";
	
	public String REPORT_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Report_Active_Small.png";

	public String REPORT_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Report_Inactive_Small.png";
	
	public String REPORT_DISABLED_BUTTON_IMG= "/images/V_Report_Disabled.png";

	public String VOID_VOUCHER_ACTIVE_BUTTON_IMG= "/images/bluetheme/Void_Active.png";

	public String VOID_VOUCHER_INACTIVE_BUTTON_IMG= "/images/bluetheme/Void_Inactive.png";
	
	public String VOID_VOUCHER_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Void_Active_Small.png";

	public String VOID_VOUCHER_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Void_Inactive_Small.png";

	public String VOID_VOUCHER_DISABLED_BUTTON_IMG= "/images/V_VoidVoucher_Disabled.png";

	public String VERIFY_OT_ACTIVE_BUTTON_IMG= "/images/V_VerifyTicket_Active.png";

	public String VERIFY_OT_INACTIVE_BUTTON_IMG= "/images/V_VerifyTicket_Inactive.png";

	public String VERIFY_OT_DISABLED_BUTTON_IMG ="/images/V_VerifyTicket_Disabled.png";

	public String ENQUIRE_VOUCHER_ACTIVE_BUTTON_IMG= "/images/bluetheme/Barcode_Active.png";

	public String ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG= "/images/bluetheme/Barcode_Inactive.png";
	
	public String ENQUIRE_VOUCHER_ACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Barcode_Active_Small.png";

	public String ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL= "/images/bluetheme/Barcode_Inactive_Small.png";

	public String ENQUIRE_VOUCHER_DISABLED_BUTTON_IMG= "/images/V_Enquiry_Disabled.png";


	public String DISPLAY_PREVIOUS_ARROW = Util.isSmallerResolution() ? 
			"/images/bluetheme/Prev_Small.png" : "/images/bluetheme/Prev.png";

	public String DISPLAY_NEXT_ARROW = Util.isSmallerResolution() ? 
			"/images/bluetheme/Next_Small.png" : "/images/bluetheme/Next.png";
	
	public String DISPLAY_NEXT_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Next_Small.png" : "/images/bluetheme/Next.png";

	public String DISPLAY_PREVIOUS_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Prev_Small.png" : "/images/bluetheme/Prev.png";
	
	public String DISPLAY_FIRST_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/First_Small.png" : "/images/bluetheme/First.png";
	
	public String DISPLAY_LAST_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Last_Small.png" :"/images/bluetheme/Last.png";

	public String DISABLED_DISPLAY_NEXT_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Disabled_Next_Small.png" : "/images/bluetheme/Disabled_Next.png";

	public String DISABLED_DISPLAY_PREVIOUS_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Disabled_Prev_Small.png" : "/images/bluetheme/Disabled_Prev.png";
	
	public String DISABLED_DISPLAY_FIRST_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Disabled_First_Small.png" : "/images/bluetheme/Disabled_First.png";
	
	public String DISABLED_DISPLAY_LAST_PAGE = Util.isSmallerResolution() ? 
			"/images/bluetheme/Disabled_Last_Small.png" :"/images/bluetheme/Disabled_Last.png";

	public String BALLY_LOGO="/images/CopyrightLogo.png";

	public String FOOTER_BACKGROUND="/images/FooterBG.png";

	public String HOME_ICON="/images/HomeIcon.png";

	public String KEYBOARD_BUTTON_IMG="/images/KeyboardIcon.png";

	public String IMAGE_TOUCH_SCREEN_PREFERENCE = "/images/touchscreen/PreferenceIcon.png";

	public String  TICK="/images/Tick.png";

	public String  CROSS="/images/Cross.png";

	public String SCROLL_RIGHT = Util.isSmallerResolution() ? 
			"/images/bluetheme/Next_Small.png" : "/images/bluetheme/Next.png";

	public String SCROLL_LEFT = Util.isSmallerResolution() ? 
			"/images/bluetheme/Prev_Small.png" : "/images/bluetheme/Prev.png";

	public String SCROLL_UP = Util.isSmallerResolution() ? 
			"/images/bluetheme/Up_Small.png" : "/images/bluetheme/Up.png";

	public String SCROLL_DOWN = Util.isSmallerResolution() ? 
			"/images/bluetheme/Down_Small.png" : "/images/bluetheme/Down.png";
	
	public String IMAGE_RADIO_BUTTON_INACTIVE =Util.isSmallerResolution()
			? "/images/S_RadioButton_Unselected.png" : "/images/RadioButton_Unselected.png";

	public String IMAGE_RADIO_BUTTON_ACTIVE = Util.isSmallerResolution()
			? "/images/Radio_Unchecked_Small.png" : "/images/RadioButton_Selected.png";

	public String IMAGE_RADIO_BUTTON_INACTIVE_BTHEME =Util.isSmallerResolution()
			? "/images/bluetheme/Radio_Unchecked_Small.png" : "/images/bluetheme/Radio_Unchecked.png";

	public String IMAGE_RADIO_BUTTON_ACTIVE_BTHEME = Util.isSmallerResolution()
			? "/images/bluetheme/Radio_Checked_Small.png" : "/images/bluetheme/Radio_Checked.png";

	public String IMAGE_RADIO_BUTTON_DISABLED_BTHEME = Util.isSmallerResolution()
	? "/images/bluetheme/Radio_Disabled_Small.png" : "/images/bluetheme/Radio_Disabled.png";

	public String IMAGE_TOUCH_SCREEN_BUTTON_IMAGE = Util.isSmallerResolution()
			? "/images/bluetheme/Button_Normal_Small.png" : "/images/bluetheme/Button_Normal.png";

	public String IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED = Util.isSmallerResolution()
	? "/images/bluetheme/Button_Disabled_Small.png" : "/images/bluetheme/Button_Disabled.png";
	
//	public String IMAGE_TOUCH_SCREEN_BUTTON_BG = "/images/bluetheme/ButtonBg.png";
	
	public String IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED = Util.isSmallerResolution()
			? "/images/bluetheme/Checbox_Checked_Small.png" : "/images/bluetheme/Checbox_Checked.png";

	public String IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED = Util.isSmallerResolution()
			? "/images/bluetheme/Checbox_Unchecked_Small.png" : "/images/bluetheme/Checbox_Unchecked.png";

	public String IMAGE_TOUCH_SCREEN_HOME_PAGE = "/images/bluetheme/homepagebg.png";

	public String NOTE_IMAGE = Util.isSmallerResolution()
			? "/images/bluetheme/Notebg_Small.png" : "/images/bluetheme/Notebg.png";
	
	public String OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG			= "/images/bluetheme/Override_Active.png";

	public String OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG_SMALL		= "/images/bluetheme/Override_Active_Small.png";
	
	public String OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG			= "/images/bluetheme/Override_Inactive.png";

	public String OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL	= "/images/bluetheme/Override_Inactive_Small.png";
	
	public String OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG			= Util.isSmallerResolution() ? "/images/bluetheme/Override_Disabled_Small.png" : "/images/bluetheme/Override_Disabled.png";

	public String OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG_SMALL	= Util.isSmallerResolution() ? "/images/bluetheme/Override_Disabled_Small.png" : "/images/bluetheme/Override_Disabled.png";

}
