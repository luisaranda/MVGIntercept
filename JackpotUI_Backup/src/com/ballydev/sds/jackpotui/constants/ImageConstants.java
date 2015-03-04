/*****************************************************************************
 * $Id: ImageConstants.java,v 1.13.1.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 4:01:12 AM$
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
package com.ballydev.sds.jackpotui.constants;

import com.ballydev.sds.framework.util.Util;

/**
 * Interface that contains the image paths for all the label and button images.
 * @author dambereen
 *@version $Revision: 15$
 */
//JP UI Redesign - Image Changes
public interface ImageConstants {

	//public   String  JACKPOT_UI_TOOLBAR_ICON="/images/Jackpot_Toolbar_Icon.gif";
	
	//public   String  MODULE_LOGO="/images/JackpotIcon.png";
	
	public   String  JACKPOT_UI_TOOLBAR_ICON="/images/JackpotUI.png";
	
	public   String  JACKPOT_UI_LABEL_NAME_IMG="/images/Tabs_Jackpot_Inactive.png";

	public   String  SLIPS_UI_BUTTON_IMG="/images/Slips_Active.png";

	public   String  JACKPOT_UI_BUTTON_IMG="/images/Jackpot_Active.gif";

	public   String  VOUCHER_UI_BUTTON_IMG="/images/Voucher_Active.png";

	public   String  PENDING_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Active.png";//"/images/J_Pending_Active.png"; //"/images/Pending_Active.png";

	public   String MANUAL_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Active.png";//"/images/J_Manual_Active.png"; //"/images/Manual_Active.png";

	public   String  VOID_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Active.png";///images/J_Void_Active.png";//"/images/Void_Active.png";

	public   String  REPRINT_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Active.png";//"/images/J_Reprint_Active.png";//"/images/Reprint_Active.png";

	public   String  DISPLAY_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Active.png";//"/images/J_Display_Active.png";///images/Display_Active.png";
	
	public   String  REPORT_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Active.png";//"/images/J_Report_Active.png";
	

	public   String  PENDING_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Disabled.png";///images/J_Pending_Disabled.png"; //"/images/Pending_Active.png";

	public   String MANUAL_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Disabled.png";///images/J_Manual_Disabled.png"; //"/images/Manual_Active.png";

	public   String  VOID_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Disabled.png";///images/J_Void_Disabled.png";//"/images/Void_Active.png";

	public   String  REPRINT_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Disabled.png";///images/J_Reprint_Disabled.png";//"/images/Reprint_Active.png";

	public   String  DISPLAY_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Disabled.png";///images/J_Display_Disabled.png";///images/Display_Active.png";

	public   String  REPORT_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Disabled.png";///images/J_Report_Disabled.png";///images/Display_Active.png";
	
	
	public   String  PRINTER_BUTTON_IMG="/images/PrinterIcon.png";

	public   String  KEYBOARD_BUTTON_IMG="/images/KeyboardIcon.png";

	public   String  LOGOUT_BUTTON_IMG="/images/LogoutIcon.png";

	public   String  EXIT_BUTTON_IMG="/images/Exit_Active.png";
	
	public   String  JACKPOT_UI_TEXT_IMAGE="/images/Jackpot_UI_Text_Image.png";
	
	public   String  JACKPOT_UI_TEXT="/images/Jackpot_UI_Text_New.png";
	
	public   String  TICK="/images/Tick.png";
	
	public   String  CROSS="/images/touchscreen/bluetheme/Radio_Unchecked.png";//"/images/Cross.png";
	
	
	public   String  DISPLAY_PREVIOUS_ARROW="/images/touchscreen/bluetheme/Prev.png";//"/images/Display_Previous_Page_Arrow.png";
	
	public   String  DISPLAY_NEXT_ARROW="/images/touchscreen/bluetheme/Next.png";//"/images/Display_Next_Page_Arrow.png";
	
	public   String  CANCEL_BUTTON="/images/Cancel.png";
	
	public   String  PENDING_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Inactive.png";//"/images/J_Pending_Inactive.png";//"/images/Pending_Inactive.png";

	public   String MANUAL_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Inactive.png";//"/images/J_Manual_Inactive.png";//"/images/Manual_Inactive.png";

	public   String  VOID_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Inactive.png";//"/images/J_Void_Inactive.png";//"/images/Void_Inactive.png";

	public   String  REPRINT_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Inactive.png";//"/images/J_Reprint_Inactive.png";//"/images/Reprint_Inactive.png";

	public   String  DISPLAY_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Inactive.png";//"/images/J_Display_Inactive.png";//"/images/Display_Inactive.png";	
	
	public   String  REPORT_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Inactive.png";//"/images/J_Report_Inactive.png";
	
	public   String  TO_FIRST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/First.png";
	
	public   String  TO_LAST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Last.png";//"/images/To_Last_Page_Arrow.png";
	
	public String FOOTER_BACKGROUND="/images/FooterBG.png";
	
	public String HOME_ICON="/images/HomeIcon.png";
	
	public String JACKPOT_MODULE="/images/JackpotModule_Top.png";
	
	public String BALLY_LOGO="/images/BallyLogo_32x32.png";

	public String RADIO_BUTTON_IMAGE="/images/touchscreen/bluetheme/Radio_Checked_Small.png";//"/images/RadioButton_Selected.png";
	
   public String SCROLL_UP="/images/scroll_up.png";
	
	public String SCROLL_DOWN="/images/scroll_down.png";
	
	// 800 Resolution	

	public   String  S_SLIPS_UI_BUTTON_IMG="/images/Slips_Active.png";

	public   String  S_JACKPOT_UI_BUTTON_IMG="/images/S_Jackpot_Active.gif";

	public   String  S_VOUCHER_UI_BUTTON_IMG="/images/S_Voucher_Active.png";

	public   String  S_PENDING_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Active_Small.png"; //"/images/Pending_Active.png";

	public   String S_MANUAL_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Active_Small.png"; //"/images/Manual_Active.png";

	public   String  S_VOID_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Active_Small.png";//"/images/Void_Active.png";

	public   String  S_REPRINT_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Active_Small.png";//"/images/Reprint_Active.png";

	public   String  S_DISPLAY_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Active_Small.png";///images/Display_Active.png";
	
	public   String  S_REPORT_ACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Active_Small.png";
	
	public   String  S_PENDING_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Disabled_Small.png";//"/images/S_J_Pending_Disabled.png"; //"/images/Pending_Active.png";

	public   String S_MANUAL_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Disabled_Small.png";//"/images/S_J_Manual_Disabled.png"; //"/images/Manual_Active.png";

	public   String  S_VOID_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Disabled_Small.png";//"/images/S_J_Void_Disabled.png";//"/images/Void_Active.png";

	public   String  S_REPRINT_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Disabled_Small.png";//"/images/S_J_Reprint_Disabled.png";//"/images/Reprint_Active.png";

	public   String  S_DISPLAY_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Disabled_Small.png";//"/images/S_J_Display_Disabled.png";///images/Display_Active.png";

	public   String  S_REPORT_DISABLE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Disabled_Small.png";//"/images/S_J_Report_Disabled.png";///images/Display_Active.png";
	
	
	public   String  S_PRINTER_BUTTON_IMG="/images/S_PrinterIcon.png";

	public   String  S_KEYBOARD_BUTTON_IMG="/images/S_KeyboardIcon.png";

	public   String  S_LOGOUT_BUTTON_IMG="/images/S_LogoutIcon.png";

	public   String  S_EXIT_BUTTON_IMG="/images/Exit_Active.png";
	
	public   String  S_JACKPOT_UI_TEXT_IMAGE="/images/Jackpot_UI_Text_Image.png";
	
	public   String  S_JACKPOT_UI_TEXT="/images/Jackpot_UI_Text_New.png";
	
	public   String  S_CROSS="/images/touchscreen/bluetheme/Radio_Unchecked_Small.png";//"/images/Cross.png";		
	
	public   String  S_PREVIOUS_PAGE_ARROW="/images/Previous_Page_Arrow.png";
	
	public   String  S_NEXT_PAGE_ARROW="/images/Next_Page_Arrow.png";
	
	public   String  S_DISPLAY_PREVIOUS_ARROW="/images/touchscreen/bluetheme/Prev_Small.png";//"/images/S_Previous.png";
	
	public   String  S_DISPLAY_NEXT_ARROW="/images/touchscreen/bluetheme/Next_Small.png";//"/images/S_Next.png";
	
	public   String  S_CANCEL_BUTTON="/images/S_Cancel.png";
	
	public   String  S_PENDING_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Pending_Inactive_Small.png";//"/images/S_J_Pending_Inactive.png";//"/images/Pending_Inactive.png";

	public   String S_MANUAL_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Manual_Inactive_Small.png";//"/images/S_J_Manual_Inactive.png";//"/images/Manual_Inactive.png";

	public   String  S_VOID_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Void_Inactive_Small.png";//"/images/S_J_Void_Inactive.png";//"/images/Void_Inactive.png";

	public   String  S_REPRINT_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Reprint_Inactive_Small.png";//"/images/S_J_Reprint_Inactive.png";//"/images/Reprint_Inactive.png";

	public   String  S_DISPLAY_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Display_Inactive_Small.png";//"/images/S_J_Display_Inactive.png";//"/images/Display_Inactive.png";	
	
	public   String  S_REPORT_INACTIVE_BUTTON_IMG="/images/touchscreen/bluetheme/Report_Inactive_Small.png";//"/images/S_J_Report_Inactive.png";
	
	public   String  S_TO_FIRST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/First_Small.png";//"/images/S_FirstPage.png";
	
	public   String  S_TO_LAST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Last_Small.png";//"/images/S_LastPage.png";
	
	public String S_FOOTER_BACKGROUND="/images/S_FooterBG.png";
	
	public String S_HOME_ICON="/images/S_HomeIcon.png";
	
	public String S_BALLY_LOGO="/images/BallyLogo_32x32.png";

	public String S_RADIO_BUTTON_IMAGE="/images/touchscreen/bluetheme/Radio_Checked.png";//images/S_RadioButton_Selected.png";
	
	public String S_SCROLL_UP="/images/S_scroll_up.png";
	
	public String S_SCROLL_DOWN="/images/S_scroll_down.png";
	
	public String IMAGE_RADIO_BUTTON_INACTIVE_BTHEME =Util.isSmallerResolution()
	? "/images/touchscreen/bluetheme/Radio_Unchecked_Small.png" : "/images/touchscreen/bluetheme/Radio_Unchecked.png";

	public String IMAGE_RADIO_BUTTON_ACTIVE_BTHEME = Util.isSmallerResolution()
	? "/images/touchscreen/bluetheme/Radio_Checked_Small.png" : "/images/touchscreen/bluetheme/Radio_Checked.png";
	
	public String IMAGE_TOUCH_SCREEN_BUTTON_BG = "/images/touchscreen/bluetheme/Button_Normal.png";
	
	public   String  S_DISABLE_TO_FIRST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Disabled_First_Small.png";
	
	public   String  DISABLE_TO_FIRST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Disabled_First.png";
	
	public   String  S_DISABLE_TO_LAST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Disabled_Last_Small.png";
	
	public   String  DISABLE_TO_LAST_PAGE_BUTTON_IMG="/images/touchscreen/bluetheme/Disabled_Last.png";
	
	public   String  S_DISABLE_DISPLAY_PREVIOUS_ARROW="/images/touchscreen/bluetheme/Disabled_Prev_Small.png";
	
	public   String  DISABLE_DISPLAY_PREVIOUS_ARROW="/images/touchscreen/bluetheme/Disabled_Prev.png";
	
	public   String  S_DISABLE_DISPLAY_NEXT_ARROW="/images/touchscreen/bluetheme/Disabled_Next_Small.png";
	
	public   String  DISABLE_DISPLAY_NEXT_ARROW="/images/touchscreen/bluetheme/Disabled_Next.png";
	
	public String S_UP_ARROW_BUTTON_IMAGE="/images/touchscreen/bluetheme/Up_Small.png";
	
	public String UP_ARROW_BUTTON_IMAGE="/images/touchscreen/bluetheme/Up.png";
	
	public String S_DOWN_ARROW_BUTTON_IMAGE="/images/touchscreen/bluetheme/Down_Small.png";
	
	public String DOWN_ARROW_BUTTON_IMAGE="/images/touchscreen/bluetheme/Down.png";
	
	public   String  S_DISABLE_DISPLAY_UP_ARROW = "/images/touchscreen/bluetheme/Disabled_Up_Small.png";
	
	public   String  DISABLE_DISPLAY_UP_ARROW = "/images/touchscreen/bluetheme/Disabled_Up.png";
	
	public   String  S_DISABLE_DISPLAY_DOWN_ARROW = "/images/touchscreen/bluetheme/Disabled_Down_Small.png";
	
	public   String  DISABLE_DISPLAY_DOWN_ARROW = "/images/touchscreen/bluetheme/Disabled_Down.png";
	
	public String IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED = Util.isSmallerResolution()
	? "/images/touchscreen/bluetheme/Checbox_Checked_Small.png" : "/images/touchscreen/bluetheme/Checbox_Checked.png";

public String IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED = Util.isSmallerResolution()
	? "/images/touchscreen/bluetheme/Checbox_Unchecked_Small.png" : "/images/touchscreen/bluetheme/Checbox_Unchecked.png";
	
}
