/*****************************************************************************
 * $Id: IVoucherConstants.java,v 1.68.3.0.1.0, 2013-10-25 16:52:01Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 11:52:01 AM$
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




public interface IVoucherConstants 
{
	public final static String MODULE_NAME = "Voucher";

	String NO_PLAYER_REQD 		= "0";
	String SPECIFIC_PLAYER_REQD = "1";
	String ANY_PLAYER_REQD 		= "2";


	public static final String USER_FUNCTION_ADMINISTRATOR 				= "Voucher - Administration";
	public static final String USER_FUNCTION_TKT_PRINT 					= "Voucher - Print";
	public static final String USER_FUNCTION_TKT_REDEEM 				= "Voucher - Redeem";
	public static final String USER_FUNCTION_TKT_OVERRIDE 				= "Voucher - Override";
	public static final String USER_FUNCTION_TKT_RECONCILATION 			= "Voucher - Reconciliation";
	public static final String USER_FUNCTION_TKT_REPORT 				= "Voucher - Reports";
	public static final String USER_FUNCTION_TKT_ENQUIRE 				= "Voucher - Enquire";
	public static final String USER_FUNCTION_TKT_VOID 					= "Voucher - Void";
	public static final String USER_FUNCTION_VERIFY_OT 					= "Voucher - Verify Offline Ticket";
	public static final String USER_FUNCTION_TKT_VOID_CASHIER_GEN_TKTS 	= "Voucher - Void Cashier Generated Tickets";
	public static final String USER_FUNCTION_TKT_VOID_SLOT_GEN_TKTS		= "Voucher - Void Slot Generated Tickets";
	public static final String USER_FUNCTION_TKT_OVERRIDE_FUNC 			= "Voucher - Allow Ticket Not Found";
	public static final String USER_FUNCTION_CREATE_SIGN_ON_SIGN_OFF 	= "VOUCHER.CREATE.AND.SIGN.ON.AND.SIGN.OFF";
	public static final String USER_FUNCTION_SIGN_ON_AND_SIGN_OFF 		= "VOUCHER.SIGN.ON.AND.SIGN.OFF" ;

	//////////////////////////VOUCHERHOME///////////////////////////
	//PROPERTY NAMES

	//CONTROL NAMES
	String VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER		= "redeemVoucher";
	String VOUCHERHOME_CTRL_BTN_PRINTVOUCHER		= "printVoucher";
	String VOUCHERHOME_CTRL_BTN_VOIDVOUCHER			= "voidVoucher";
	String VOUCHERHOME_CTRL_BTN_REPORTS				= "reports";
	String VOUCHERHOME_CTRL_BTN_ENQUIREVOUCHER		= "enquireVoucher";
	String VOUCHERHOME_CTRL_BTN_RECONCILIATION		= "reconciliation";
	String VOUCHERHOME_CTRL_BTN_OVERRIDEVOUCHER		= "overrideVoucher";
	String VOUCHERHOME_CTRL_BTN_ADMINISTRATION		= "administration";
	String VOUCHERHOME_CTRL_BTN_LOGOUT				= "logOut";
	String VOUCHERHOME_CTRL_BTN_VERIFY_OT			= "verifyot";
	String VOUCHERHOME_CTRL_BTN_REPRINT				= "reprint";
	////////////////////////////////////////////////////////////////

	//////////////////////////REDEEMVOUCHER/////////////////////////
	//PROPERTY NAMES
	String REDEEMVOUCHER_FRM_TXT_BARCODE= "barCode";
	String REDEEMVOUCHER_FRM_TXT_AMOUNT= "amount";
	String ENQUIREVOUCHER_FRM_TXT_AMOUNT= "eamount";
	String REDEEMVOUCHER_FRM_TXT_STATE= "state";
	String REDEEMVOUCHER_FRM_TXT_EFFECTIVEDATE= "effectiveDate";
	String REDEEMVOUCHER_FRM_TXT_EXPIREDATE= "expireDate";
	String REDEEMVOUCHER_FRM_TXT_LOCATION= "location";
	String REDEEMVOUCHER_FRM_TXT_OFFLINE= "txtOffline";
	String REDEEMVOUCHER_FRM_TXT_TKT_TYPE= "txtTicketType";
	String REDEEMVOUCHER_FRM_TXT_PLAYERID= "playerId";

	//CONTROL NAMES
	String REDEEMVOUCHER_CTRL_TXT_BARCODE= "barCode";
	String REDEEMVOUCHER_CTRL_TXT_AMOUNT= "amount";
	String ENQURIYVOUCHER_CTRL_TXT_AMOUNT= "eamount";
	String REDEEMVOUCHER_CTRL_TXT_STATE= "state";
	String REDEEMVOUCHER_CTRL_TXT_EFFECTIVEDATE= "effectiveDate";
	String REDEEMVOUCHER_CTRL_TXT_EXPIREDATE= "expireDate";
	String REDEEMVOUCHER_CTRL_TXT_LOCATION= "location";
	String REDEEMVOUCHER_CTRL_TXT_OFFLINE= "offline";
	String REDEEMVOUCHER_CTRL_TXT_PLAYERID= "playerId";
	String REDEEMVOUCHER_CTRL_TXT_TKT_TYPE= "ticketType";
	String REDEEMVOUCHER_CTRL_BTN_CANCEL= "btnCancel";
	String REDEEMVOUCHER_CTRL_BTN_REDEEM= "btnRedeem";
	String REDEEMVOUCHER_CTRL_BTN_OVERRIDE = "btnOverride";
	String REDEEMVOUCHER_CTRL_BTN_SUBMIT = "btnSubmit";
	String REDEEMVOUCHER_CTRL_BTN_VOID = "btnVoid";
	String REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE = "btnSubmitBarcode";
	String VOIDVOUCHER_CTRL_BTN_CANCEL = "btnVoidCancel";
	String REDEEMVOUCHER_CTRL_BTN_CANCELPROCESS= "btnCancelProcess";

	String REDEEMVOUCHER_CTRL_LBL_CANCEL		= "lblCancel";
	String REDEEMVOUCHER_CTRL_LBL_REDEEM 		= "lblRedeem";
	String REDEEMVOUCHER_CTRL_LBL_MULTIREDEEM 	= "multiRedeem";
	String REDEEMVOUCHER_CTRL_LBL_OVERRIDE 		= "lblOverride";
	String REDEEMVOUCHER_CTRL_LBL_SUBMIT 		= "lblSubmit";
	String REDEEMVOUCHER_CTRL_LBL_VOID 			= "lblVoid";
	String REDEEMVOUCHER_CTRL_LBL_SUBMIT_BARCODE= "lblSubmitBarcode";
	String REDEEMVOUCHER_CTRL_LBL_CANCELPROCESS	= "lblCancelProcess";
	String VOIDVOUCHER_CTRL_LBL_CANCEL 			= "lblVoidCancel";
	////////////////////////////////////////////////////////////////

	//////////////////////////BUTTON////////////////////////////////
	//PROPERTY NAMES

	//CONTROL NAMES
	String BUTTON_CTRL_BTN_EXIT = "exit";
	////////////////////////////////////////////////////////////////
	String REDEEMVOUCHER_CTRL_BTN_MULTIREDEEM = "multiRedeem";

	//////////////////////////MULTIREDEEM///////////////////////////
	//PROPERTY NAMES
	String MULTIREDEEM_FRM_TXT_VOUCHERCOUPON= "voucherCoupon";
	String MULTIREDEEM_FRM_TXT_AMOUNTTOTAL= "amountTotal";

	//CONTROL NAMES
	String MULTIREDEEM_CTRL_TXT_VOUCHERCOUPON= "txtVoucherCoupon";
	String MULTIREDEEM_CTRL_BTN_RESETTOTAL= "ResetTotal";
	String MULTIREDEEM_CTRL_BTN_SHOWTOTAL= "ShowTotal";
	String MULTIREDEEM_CTRL_TXT_AMOUNTTOTAL= "txtAmountTotal";
	String MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL = "btnCancel";


//////////////////////////PRINT VOUCHER///////////////////////////
//	PROPERTY NAMES
	String PRINTVOUCHER_FRM_TXT_TICKETAMOUNT= "ticketAmount";
	String PRINTVOUCHER_FRM_TXT_COUNT= "count";
	String PRINTVOUCHER_FRM_TXT_TOTAL_AMOUNT= "totalAmount";
	String PRINTVOUCHER_FRM_TXT_TOTAL_COUNT= "totalCount";
	String PRINTVOUCHER_FRM_TXT_NO_TKTS= "totalTktsToPrint";

//	CONTROL NAMES
	String PRINTVOUCHER_CTRL_TXT_TICKETAMOUNT= "txtTicketAmount";
	String PRINTVOUCHER_CTRL_TXT_COUNT= "count";
	String PRINTVOUCHER_CTRL_TXT_TOTAL_AMOUNT= "totalAmount";
	String PRINTVOUCHER_CTRL_TXT_TOTAL_COUNT= "totalCount";
	String PRINT_VOUCHER_CTRL_BTN_CANCEL="btnCancel";
	String PRINT_VOUCHER_CTRL_BTN_PRINT = "btnSubmit";
	String PRINTVOUCHER_CTRL_TXT_NO_TKTS= "totalTktsToPrint";

//	REPORTS CONTROL NAMES
	String REPORTS_CTRL_BTN_CANCEL="btnCancel";
	String REPORTS_CTRL_BTN_PRINT="btnPrint";
	String REPORTS_CTRL_BTN_SUBMIT = "btnSubmit";
	String REPORTS_FRM_CMB_SELECTED_REPORT = "selectedReport";
	String REPORTS_FRM_CMB_REPORT_LIST = "reportsList";
	String REPORTS_CTRL_TXT_AREA= "reportsDisplayArea";
	String REPORTS_FRM_TXT_AREA= "txtReportsDisplay";
	String REPORTS_CTRL_TXT_PROPERTY_ID = "propertyId";
	String REPORTS_FRM_TXT_PROPERTY_ID = "txtPropertyId";
	String REPORTS_CTRL_LBL_EMPLOYE_ID = "employeeId";
	String REPORTS_FRM_LBL_EMPLOYE_ID = "txtEmployeeId";
	String REPORTS_CTRL_TGL_BTN_EMPLOYEE_ASSET = "employeeAllToggleBtn";
	String REPORTS_CTRL_BTN_FORWARD = "btnForward";
	String REPORTS_CTRL_BTN_BACKWARD = "btnBackward";
	String REPORTS_CTRL_BTN_UP = "btnUp";
	String REPORTS_CTRL_BTN_DOWN = "btnDown";

//	REPRINT CONTROL NAMES

	String PRINTVOUCHER_CTRL_TXT_BARCODE= "barCode";
	int BARCODE_LENGTH = 18;

	String REP_CTRL_TGL_BTN_BAND = "detailBandToggleBtn";
	String REP_CTRL_TGL_BTN_SUMM = "summBandToggleBtn";

//	RECONCILIATION PAGE CONTROL NAMES
	String RECON_CTRL_LBL_START_TIME = "startTime";
	String RECON_CTRL_LBL_END_TIME = "endTime";
	String RECON_CTRL_LBL_EMPLOYE_ID = "employeeAssetId";
	String RECON_CTRL_TGL_BTN_EMPLOYEE_ASSET = "employeeAssetToggleBtn";
	String RECON_CTRL_BTN_CANCEL = "recnBtnCancel";
	String RECON_CTRL_BTN_SUBMIT = "btnSubmit";//clearBtn;
	String RECON_CTRL_TXT_BARCODE= "barcode";
	String RECON_CTRL_LBLTXT_ACCEPTED="accepted";
	String RECON_CTRL_LBLTXT_NO_READ="notRead";
	String RECON_CTRL_LBLTXT_UNCOMMITED="uncommitted";
	String RECON_CTRL_LBLTXT_TOTAL="total";	
	
	String UPLOAD_FRM_TXT_UPLOAD_XML_FILE = "txtUploadXmlFile";
	String RECON_CTRL_BTN_BROWSEXML = "btnBrowseXML";//clearBtn;
	String RECON_CTRL_BTN_SUMBITXML = "btnSubmitXML";//clearBtn;

	

//	RECONCILIATION BATCH DETAIL COMPOSITE CONTROLS
	String RECON_CTRL_BTN_RELOAD_BATCH =  "reloadBatchDtls";
	String RECON_CTRL_BTN_QUESTIONABLE_VOUCHER = "questionableVouchers";
	String RECON_CTRL_BTN_PRINT_BATCH_DTL = "printBatchDtls";
	String RECON_CTRL_BTN_DELETE_CURRENT_BATCH = "deleteCurrBatch";
	String RECON_CTRL_BTN_CHANGE_LOCATION = "changeEmployee";
	String RECON_CTRL_BTN_DELETE_TKT_BATCH = "deleteTktInABatch";
	String RECON_CTRL_BTN_ADD_TKT_BATCH = "addTktInABatch";
	String RECON_CTRL_BTN_MODIY_TKT = "modifyBarCode";
	String RECON_CTRL_BTN_PREVIOUS_ARROW = "previousArrow";
	String RECON_CTRL_BTN_NEXTARROW_ARROW ="nextArrow";
	String RECON_CTRL_BTN_FIRST_ARROW = "firstArrow";
	String RECON_CTRL_BTN_LAST_ARROW ="lastArrow";
	String RECON_CTRL_TABLE_BATCH_DETAIL = "dispBatchDetailsDTO";
	String RECON_CTRL_TABLE_BATCH_DETAIL_SEL_ITEM = "dispBatchDetailsDTOSelectedValue";


	String ENQURIY_CTRL_TABLE_BATCH_SUMMARY = "transactionListDTOList";
	String ENQURIY_CTRL_TABLE_BATCH_DETAIL = "transactionListDTO";
	String ENQURIY_CTRL_TABLE_BATCH_DETAIL_SEL_ITEM = "transactionListDTOSelectedValue";

//	RECONCILIATION - BATCH SUMMARY COMPOSITE CONTROLS

	String RECON_CTRL_TABLE_BATCH_SUMMARY = "batchSummaries";
	String RECON_BATCH_SUMMARY_DETAIL = "batchSummaryDtl" ;
	String RECON_LOAD_SELECTED_BATCH = "loadSelectedBatch";
	String RECON_DELETE_SELECTED_BATCH = "deleteSelectedBatch";
	String RECON_CHANGE_EMPLOYEE = "changeEmployee";
	String RECON_CHANGE_ASSET = "changeAsset";
	String RECON_PRINT_BATCH_SUMMARY = "prntBatchSummary";
	String RECON_CANCEL = "cancel";
	String CASHIER = "Cashier";
	String KIOSK = "Kiosk";

	String RECON_BATCH_SUMM_CTRL_TXT_BATCH_NBR="txtBatchNbr";
	String RECON_BATCH_SUMM_CTRL_TXT_VOU_CNT="txtVoucherCount";
	String RECON_BATCH_SUMM_CTRL_TXT_VOU_AMT="txtVoucherAmt";
	String RECON_BATCH_SUMM_CTRL_TXT_EMP="txtEmployee";
	String RECON_BATCH_SUMM_CTRL_TXT_COU_CNT="txtCouponCount";
	String RECON_BATCH_SUMM_CTRL_TXT_COU_AMT="txtCouponAmt";
	String RECON_CTRL_TABLE_BATCH_SUMM_SEL_ITEM = "batchSummaryDTO";


	String RECON_BATCH_SUMM_FRM_TXT_BATCH_NBR="batchNbrValue";
	String RECON_BATCH_SUMM_FRM_TXT_VOU_CNT="batchVoucherCountValue";
	String RECON_BATCH_SUMM_FRM_TXT_VOU_AMT="batchVoucherAmountValue";
	String RECON_BATCH_SUMM_FRM_TXT_EMP="batchEmployeeValue";
	String RECON_BATCH_SUMM_FRM_TXT_COU_CNT="batchCouponCountValue";
	String RECON_BATCH_SUMM_FRM_TXT_COU_AMT="batchCouponAmountValue";


//	RECONCILIATION - QUESTIONABLE COMPOSITE CONTROLS

	String RECON_CTRL_BTN_GETQUESTIONABLE = "getQuestBatchBtn";
	String RECON_CTRL_BTN_GET_CURRENT_BATCH = "getCurBatchBtn";
	String RECON_CTRL_BTN_GET_BATCH_SUMM = "getBatchSummaryBtn";
	String RECON_CTRL_BTN_PRINT_DETAILS = "printDetails"; 



	//////////////////////////OVERRIDEAUTH///////////////////////////
	//PROPERTY NAMES
	String OVERRIDEAUTH_FRM_CMB_VIWER_REASONCOMBO	= "reasonCombo";
	String OVERRIDEAUTH_FRM_CMB_VIWER_LIST_REASON	= "reasonList";
	String OVERRIDEAUTH_FRM_TXT_USERID				= "userId";
	String OVERRIDEAUTH_FRM_TXT_PASSWORD			= "password";
	String OVERRIDEAUTH_FRM_TXT_REASON_FOR_OVERRIDE	= "reasonForOverride";
	String OVERRIDEAUTH_FRM_CMB_VIWER_SEL_ITEM_SELECTEDREASONVALUE	= "selectedReasonValue";

	//CONTROL NAMES
	String OVERRIDEAUTH_CTRL_TXT_USERID= "userId";
	String OVERRIDEAUTH_CTRL_TXT_PASSWORD= "password";
	String OVERRIDEAUTH_CTRL_TXT_REASON_FOR_OVERRIDE= "reasonForOverride";
	String OVERRIDEAUTH_CTRL_BTN_SUBMIT= "Submit";
	String OVERRIDEAUTH_CTRL_BTN_CANCEL= "Cancel";
	String OVERRIDEAUTH_CTRL_BTN_KEYBOARD= "keyboard";
	////////////////////////////////////////////////////////////////

////////////////////////VERIFY OT///////////////////////////
//	PROPERTY NAMES
	String VERIFYOT_FRM_TXT_TICKETBRCD="ticketBarcodeValue";
	String VERIFYOT_FRM_TXT_TICKETAMOUNT="ticketAmountValue";
//	CONTROL NAMES
	String VERIFYOT_CTRL_BTN_VERIFY= "btnVerify";
	String VERIFYOT_CTRL_BTN_REDEEM= "btnRedeem";
	String VERIFYOT_CTRL_BTN_CANCEL= "btnCancel";
	String VERIFYOT_CTRL_BTN_SUBMIT= "btnSubmit";
	String VERIFYOT_CTRL_BTN_KEYBOARD= "btnKeyBoard";




////////Preference Pages Controls
//	General Preferences
	String PREFERENCE_GENERAL_CTRL_TXT_SERVER_NAME = "serverName";
	String PREFERENCE_GENERAL_CTRL_TXT_SERVER_PORT = "serverPort";
	String PREFERENCE_GENERAL_CTRL_TXT_CASINO_NAME = "casinoName";
	String PREFERENCE_GENERAL_CTRL_TXT_CASINO_ID = "casinoId";
	String PREFERENCE_GENERAL_CTRL_TXT_CLIENT_TIME_OUT = "clientTimeOut";
	String PREFERENCE_GENERAL_CTRL_BTN_1024_768 = "btn_1024_768";
	String PREFERENCE_GENERAL_CTRL_BTN_800_600 = "btn_800_600";

	String PREFERENCE_GENERAL_FRM_TXT_SERVER_NAME = "serverName";
	String PREFERENCE_GENERAL_FRM_TXT_SERVER_PORT = "serverPort";
	String PREFERENCE_GENERAL_FRM_TXT_CASINO_NAME = "casinoName";
	String PREFERENCE_GENERAL_FRM_TXT_CASINO_ID = "casinoId";
	String PREFERENCE_GENERAL_FRM_TXT_CLIENT_TIME_OUT = "clientTimeOut";
	String PREFERENCE_GENERAL_FRM_BTN_1024_768 = "_1024_768";
	String PREFERENCE_GENERAL_FRM_BTN_800_600 = "_800_600";

//	Location Preferences
	String PREFERENCE_LOCATION_CTRL_TXT_LOCATION_ID = "locationId";
	String PREFERENCE_LOCATION_CTRL_TXT_LOCATION_TIME_OUT = "locationTimeOut";

	String PREFERENCE_LOCATION_FRM_TXT_LOCATION_ID = "locationId";
	String PREFERENCE_LOCATION_FRM_TXT_LOCATION_TIME_OUT = "locationTimeOut";
	String PREFERENCE_LOCATION_FRM_BTN_CASHIER = "isCashier";
	String PREFERENCE_LOCATION_FRM_BTN_AUDITOR = "isAuditor";
	String PREFERENCE_LOCATION_FRM_BTN_ADMIN = "isAdmin";
	String PREF_LOCATION_TITLE="Location";


	String REPORT_FRM_BTN_TICKET = "isVoucher";
	String REPORT_FRM_BTN_CSH = "isCashable";
	String REPORT_FRM_BTN_NCSH = "isNonCashable";

//	REPORT PREFERENCES
	String PREFERENCE_REPORT_FRM_BTN_REDEEMED = "isRedeemed";
	String PREFERENCE_REPORT_FRM_BTN_CREATED = "isCreated";
	String PREFERENCE_REPORT_FRM_BTN_VOIDED = "isVoided";
	String PREFERENCE_REPORT_FRM_BTN_DISPLAY_ALL_BATCHES = "isDisplayAllBatches";

//	BarCode Scanner
	String PREFERENCE_BARCODE_SCANNER_CTRL_BTN_BARCODE_SCANNER = "btnBarcodeScanner";
	String PREFERENCE_BARCODE_SCANNER_CTRL_BTN_TICKET_SCANNER_TEST = "btnTicketScannerTest";
	String PREFERENCE_BARCODE_SCANNER_CTRL_TXT_TEST_STATUS = "testStatus";
	String PREFERENCE_BARCODE_SCANNER_CTRL_CMB_SCANNER_PORT = "scannerPort";
	String PREFERENCE_BARCODE_SCANNER_CTRL_CMB_SCANNER_TYPE = "scannerType";
	String PREFERENCE_BARCODE_SCANNER_CTRL_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_BARCODE_SCANNER_CTRL_TXT_MODEL = "model";
	String PREFERENCE_BARCODE_SCANNER_CTRL_TXT_DRIVER = "driver";

	String PREFERENCE_BARCODE_SCANNER_FRM_BTN_BARCODE_SCANNER = "isBarcodeScanner";
	String PREFERENCE_BARCODE_SCANNER_FRM_TXT_TEST_STATUS = "testStatus";
	String PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_PORT = "scannerPortList";
	String PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_PORT = "selectedScannerPort";
	String PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_TYPE = "scannerTypeList";
	String PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_TYPE = "selectedScannerType";
	String PREFERENCE_BARCODE_SCANNER_FRM_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_BARCODE_SCANNER_FRM_TXT_MODEL = "model";
	String PREFERENCE_BARCODE_SCANNER_FRM_TXT_DRIVER = "driver";
	String PREF_BARCODE_SCANNER_TITLE="Barcode Scanner";	

//	TICKET PRINTER
	String PREFERENCE_TICKET_PRINTER_CTRL_BTN_TICKET_PRINTING = "btnTicketPrinting";
	String PREFERENCE_TICKET_PRINTER_CTRL_BTN_MULTIPLE_TICKETS = "btnMultipleTickets";
	String PREFERENCE_TICKET_PRINTER_CTRL_BTN_SECURE_PRINT = "btnSecurePrint";
	String PREFERENCE_TICKET_PRINTER_CTRL_BTN_PRINT_VOID_TICKET = "btnPrintVoidTicket";
	String PREFERENCE_TICKET_PRINTER_CTRL_TXT_MAX_LIMIT = "maxLimit";
	String PREFERENCE_TICKET_PRINTER_CTRL_CMB_PRINTER_PORT = "printerPort";
	String PREFERENCE_TICKET_PRINTER_CTRL_CMB_PRINTER_TYPE = "printerType";
	String PREFERENCE_TICKET_PRINTER_CTRL_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_TICKET_PRINTER_CTRL_TXT_MODEL = "model";
	String PREFERENCE_TICKET_PRINTER_CTRL_TXT_DRIVER = "driver";
	String PREFERENCE_TICKET_PRINTER_CTRL_TXT_TST_STATUS = "testStatus";

	String PREFERENCE_TICKET_PRINTER_FRM_BTN_TICKET_PRINTING = "isTicketPrinting";
	String PREFERENCE_TICKET_PRINTER_FRM_BTN_MULTIPLE_TICKETS = "isMultipleTickets";
	String PREFERENCE_TICKET_PRINTER_FRM_BTN_SECURE_PRINT = "isSecurePrint";
	String PREFERENCE_TICKET_PRINTER_FRM_TXT_MAX_LIMIT = "maxLimit";
	String PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_PORT = "printerPortList";
	String PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT = "selectedPrinterPort";
	String PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_TYPE = "printerTypeList";
	String PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE = "selectedPrinterType";
	String PREFERENCE_TICKET_PRINTER_FRM_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_TICKET_PRINTER_FRM_TXT_MODEL = "model";
	String PREFERENCE_TICKET_PRINTER_FRM_TXT_DRIVER = "driver";
	String PREFERENCE_TICKET_PRINTER_FRM_TST_STUS = "testStatus";	
	String PREFERENCE_TICKET_PRINTER_FRM_SMALL_SIZE  = "isSmallSize";
	String PREFERENCE_TICKET_PRINTER_FRM_NORMAL_SIZE = "isNormalSize";	
	String PREF_TKT_PRINTER_TITLE="Ticket Printer";	


//	RECEIPT PRINTER
	String PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING = "btnReceiptPrinting";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING_TEST = "btnReceiptPrintintTest";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_CMB_PRINTER_PORT = "printerPort";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_CMB_PRINTER_TYPE = "printerType";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MODEL = "model";
	String PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_DRIVER = "driver";

	String PREFERENCE_RECEIPT_PRINTER_FRM_BTN_RECEIPT_PRINTING = "isReceiptPrinting";
	String PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_PORT = "printerPortList";
	String PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT = "selectedPrinterPort";
	String PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_TYPE = "printerTypeList";
	String PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE = "selectedPrinterType";
	String PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MODEL = "model";
	String PREFERENCE_RECEIPT_PRINTER_FRM_TXT_DRIVER = "driver";
	String PREF_RECEIPT_PRINTER_TITLE="Receipt Printer";

//	CUSTOMER DISPLAY
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY = "btnCustomerDisplay";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY_TEST = "btnCustomerDisplayTest";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_CMB_PORT = "port";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_CMB_DISPLAY_TYPE = "displayType";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_TEST_STATUS = "testStatus";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_WELCOME_MESSAGE = "welcomeMessage";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_MODEL = "model";
	String PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_DRIVER = "driver";

	String PREFERENCE_LOCATION_FRM_BTN_CENTER_DISP = "isCenterDisplay";
	String PREFERENCE_LOCATION_FRM_BTN_SCROLL_DISP = "isScrollDisplay";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_BTN_CUSTOMER_DISPLAY = "isCustomerDisplay";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_PORT = "portList";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_PORT = "selectedPort";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_DISPLAY_TYPE = "displayTypeList";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_DISPLAY_TYPE = "selectedDisplayType";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_TEST_STATUS = "testStatus";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_WELCOME_MESSAGE = "welcomeMessage";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_MANUFACTURER = "manufacturer";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_MODEL = "model";
	String PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_DRIVER = "driver";
	String PREF_CUST_DISP_TITLE="Customer Display";	

//	MULTIAREA
	String PREFERENCE_MULTIAREA_CTRL_BTN_MULTIAREA = "btnMultiarea";
	String PREFERENCE_MULTIAREA_CTRL_CMB_MULTIAREA_ID = "multiareaId";
	String PREFERENCE_MULTIAREA_CTRL_CMB_SITENAME = "siteName";
	String PREFERENCE_MULTIAREA_CTRL_TXT_PROPERTY_NAME = "propertyName";
	String PREFERENCE_MULTIAREA_CTRL_TXT_PROPERTY_ID = "propertyId";

	String PREFERENCE_MULTIAREA_FRM_BTN_MULTIAREA = "isMultiarea";
	String PREFERENCE_MULTIAREA_FRM_CMB_MULTIAREA_ID = "multiareaIdList";
	String PREFERENCE_MULTIAREA_FRM_CMB_SELECTED_MULTIAREA_ID = "selectedMultiareaId";
	String PREFERENCE_MULTIAREA_FRM_TXT_PROPERTY_NAME = "propertyName";
	String PREFERENCE_MULTIAREA_FRM_TXT_PROPERTY_ID= "propertyId";
	String PREF_MULTI_AREA_TITLE="Multiarea";

//	CHANGE STC PWD
	String PREFERENCE_CHANGE_STC_PWD_CTRL_TXT_ENTER_PWD = "enterPwd";
	String PREFERENCE_CHANGE_STC_PWD_CTRL_TXT_ENTER_NEW_PWD = "enterNewPwd";
	String PREFERENCE_CHANGE_STC_PWD_CTRL_TXT_CONFIRM_PWD = "confirmPwd";

	String PREFERENCE_CHANGE_STC_PWD_FRM_TXT_ENTER_PWD = "enterPwd";
	String PREFERENCE_CHANGE_STC_PWD_FRM_TXT_ENTER_NEW_PWD = "enterNewPwd";
	String PREFERENCE_CHANGE_STC_PWD_FRM_TXT_CONFIRM_PWD = "confirmPwd";

	String BUTTON_GRP_CASHIER = "cashierSelection";
	String BUTTON_GRP_BAND_SELECT = "bandSelection";
	String BUTTON_GRP_USER_PREF = "userSelection";
	String BUTTON_DISP_USER_PREF = "displaySelection";
	String BUTTON_SIZE_USER_PREF = "sizeSelection";

	String SLOT_TEXT="SLOT.LABEL.TEXT";
	String TICKET_TEXT="TICKET.LABEL.TEXT";
	String DEFAULT_SLOT_TEXT ="Slot";
	String DEFAULT_TICKET_TEXT ="Voucher";
	
	String ALLOW_VOIDED_TKTS="ALLOW.VOIDED.TICKETS.IN.STC";
	String ALLOW_CASHIER_TO_REDEEM_THEIR_OWN_TKTS="VCH.ALLOW.CSHR.REDEM.OWN.TKTS";
	String ALLOW_EXPIRED_TKTS="ALLOW.EXPIRED.TICKETS.IN.STC";
	String ALLOW_PENDING_TKTS="ALLOW.PENDING.TICKETS.IN.STC";
	String ALLOW_TKTS_NOT_YET_EFF="ALLOW.TICKETS.NOT.EFFFECTIVE";
	String ALLOW_VOIDED_CP_TKTS="ALLOW.VOIDABLE.PROMO.TICKETS";
	String ALLOW_EXPIRED_CP_TKTS="ALLOW.EXPIRED.CASHABLE.PROMO";
	String ALLOW_PENDING_CP_TKTS="ALLOW.PENDING.CASHABLE.PROMO";
	String ALLOW_CP_TKTS_NOT_YET_EFF="ALLOW.CP.TKS.NOT.EFFECTIVE";
	String NUMBER_DAYS_TO_EXPIRE = "NUMBER.DAYS.TO.EXPIRE";
	String MAX_REDEMPTION_AMT = "MAX.TICKET.REDEMPTION.AMOUNT";
	String ALLOW_TICKET_PRINT = "VCH.TICKET.PRINT.ENABLED";
	String ALLOW_TICKET_VOID = "VCH.TKT.VOID.ENABLE.PRINT";
	String RESTRICTED_CASHERING_STC = "VCH.RESTRICT.CASHRNG.STC";
	String NO_OF_TKTS_TO_PRINT="NUMBR.TKTS.TO.PRINTD";
	String MIN_LEN_BARCODE_ENQUIRY="BARCODE.LENGTH.TO.VALIDATE";
	String MULTI_AREA_ENABLED="MULTIAREA.FOR.TICKETS";
	String VOU_LIC_ENABLED="MODULE.15.LIC.SITE.KEY";
	String ALLOW_PRINT_TKT_OVERRIDE="ALLOW.PRINT.TKT.OVERRIDE";
	String ALLOW_CASHIER_LOC_ON_TICKET="ALLOW.CASHIER.LOC.ON.TICKET";
	
	String VOUCHER_PRINTER_PREFERENCE_KEY = "Preference.voucherPrinterName";

	String REDEMPTION_NOT_ALLOWED = "Do Not Allow Redemption";
	String REDEMPTION_OVERRIRDE="Require Override";
	String NORMAL_REDEMPTION ="Normal Redemption";

	String PRINTER_CLASS_TO_USE = "com.ballydev.sds.voucherui.print.Ithaca";

//	SCREEN NAMES

	String VOU_RECONCILE_SCREEN = "Reconcillation screen";
	String VOU_TKT_VERIFY_SCREEN = "Verify offline screen";
	String VOU_PRINT_SCREEN = "Print screen";
	String VOU_REPRINT_SCREEN = "Reprint screen";
	String VOU_REDEEM_SCREEN = "Redeem screen";
	String VOU_VOID_SCREEN = "Void screen";
	String VOU_DUMMY_SCREEN = "Dummy screen";
	String VOU_START_SCREEN= "Startup Screen";
	String VOU_REPORTS_SCREEN= "Reports Screen";
	String VOU_ENQUIRY_SCREEN= "Enquiry Screen";
	String VOU_OVERRIDE_SCREEN = "Override screen";
	
	String TKT_TYPE_NON_SLOT ="Printed at a Non-slot Location";
	String TKT_TYPE_SLOT ="Printed By a Slot Machine";

	String SERVER_ERROR="Connection refused: connect";
	String WIDGET_DISPOSED="Widget is disposed";
	String EXP_INIT="Unknown Fault Type";
	String JDBC_ROLLBACK="RollbackException";


	/**FORM NAMES**/
	String BARCODE_SCANNER_PREFERENCE_FORM = "BarcodeScannerPreferenceForm";
	String BASE_RECONCILIATION_FORM = "BaseReconciliationForm";
	String BATCH_DETAILS_FORM = "BatchDetailsForm";
	String BATCH_RECONCILIATION_FORM = "BatchReconciliationForm";
	String BATCH_SUMMARY_FORM = "BatchSummaryForm";
	String CUSTOMER_DISPLAY_PREFERENCE_FORM = "CustomerDisplayPreferenceForm";
	String LOCATION_PREFERENCE_FORM = "LocationPreferenceForm";
	String MULTIAREA_PREFERENCE_FORM = "MultiareaPreferenceForm";
	String MULTI_REDEEM_FORM = "MultiRedeemForm";
	String OTKEY_VERIFICATION_FORM = "OTKeyVerificationForm";
	String OVERRIDE_AUTH_FORM = "OverrideAuthForm";
	String OVERRIDE_PRINT_FORM = "OverridePrintForm";
	String PRINTER_PREFERENCE_FORM = "PrinterPreferenceForm";
	String PRINT_VOUCHER_FORM = "PrintVoucherForm";
	String RECEIPT_PRINTER_PREFERENCE_FORM = "ReceiptPrinterPreferenceForm";
	String REDEEM_VOUCHER_FORM = "RedeemVoucherForm";
	String REPORT_PREFERENCE_FORM = "ReportPreferenceForm";
	String ENQUIRE_VOUCHER_FORM = "BarcodeEnquiryForm";
	String REPRINT_VOUCHER_FORM = "ReprintVoucherForm";
	String REPORTS_VOUCHER_FORM = "ReportsVoucherForm";
	String TICKET_PRINTER_PREFERENCE_FORM = "TicketPrinterPreferenceForm";
	String UI_BASE_FORM = "UIBaseForm";
	String VERIFY_OT_FORM = "VerifyOTForm";
	String OVERRIDE_VOUCHER_FORM = "OverrideVoucherForm";


	String VOU_PRINT_TIMEOUT ="Printer TimeOut";
	String VOU_PRINT_NOT_COMPLETED ="Barcode not printed";

	String VOU_PREF_TKT_PRINT_COM1 = "COM1";
	String VOU_PREF_TKT_PRINT_COM2 = "COM2";
	String VOU_PREF_TKT_PRINT_COM3 = "COM3";
	String VOU_PREF_TKT_PRINT_COM4 = "COM4";
	String VOU_PREF_TKT_PRINT_LPT1 = "LPT1";


	String VOU_PREF_TKT_PRINT_TYPE1 = "TYPE1";
	String VOU_PREF_TKT_PRINT_TYPE2 = "TYPE2";
	String VOU_PREF_TKT_PRINT_TYPE3 = "TYPE3";
	String VOU_PREF_TKT_PRINT_TYPE4 = "TYPE4";
	String VOU_PREF_TKT_PRINT_TYPE5 = "TYPE5";
	String VOU_PREF_TKT_PRINT_TYPE6 = "TYPE6";

	String VOU_PREF_DIS_TYPE1 = "DIS_TYPE1";
	String VOU_PREF_DIS_TYPE2 = "DIS_TYPE2";	


	//TKT SCANNER PREF VALUES

	String TKTSCANNER_ITHACA_MANFT		= "Ithaca";
	String TKTSCANNER_EPSON_MANFT		= "Epson";
	String TKTSCANNER_ITHACA_750_MANFT	= "Ithaca";
	String TKTSCANNER_ITHACA_950_MANFT	= "Ithaca";
	String TKTSCANNER_NANOPTIX_MANFT	= "Nanoptix";

	String TKTSCANNER_ITHACA_MODEL		= "850";
	String TKTSCANNER_EPSON_MODEL		= "TM-U200";
	String TKTSCANNER_ITHACA_750_MODEL	= "750";
	String TKTSCANNER_ITHACA_950_MODEL	= "950";
	String TKTSCANNER_NANOPTIX_MODEL	= "PAYCHECK HSC";

	String TKTSCANNER_ITHACA_DRIVER		= "com.ballydev.sds.voucherui.print.Ithaca";

	String TKTSCANNER_EPSON_DRIVER		= "com.ballydev.sds.voucherui.print.EpsonWithLPTPort";

	//DIS PREFERENCE SCANNER PREF VALUES
	String CUST_DISP_PIONEER_MANFT  = "Pioneer";
	String CUST_DISP_PIONEER_MODEL  = "STLH-RORJ";
	String CUST_DISP_PIONEER_DRIVER = "com.ballydev.sds.voucherui.displays.AEDEX";

	//VALIDATOR CONSTANTS
	String DEPENDS_FOR_EMP ="required,numeric,MaximumLength";
	String DEPENDS_FOR_ASSET ="required,AlphaNumericWithSpaces";
	String[] ARG_FOR_EMP = {"COMMON.LABEL.EMPLOYEE.ID","5"};
	String[] ARG_FOR_ASSET = {"VOUCHER.RECONCILIATION.KIOSK"};

	String DEPENDS_FOR_BARCODE ="required,MinimumLength,validateNumericWithSplChar";
	
 
	String MISSING_FROM_BATCH="VOU.LKP.AUDIT.MISG.BATCH";
	String PRINTER_VOU_PREFERENCE="Preference.voucherPrinterName";
	String VOUCHER_PENDIG_AT_DIFF_LOCATION="VOUCHER.PENDIG.AT.DIFF.LOCATION";
	String VOUCHER_ASSET_NOT_VALID="VOUCHER.SLOT.NOT.VALID";
	String VOUCHER_ALREADY_REDEEEMED= "VOUCHER.ALREADY.REDEEEMED";

	String CASHIER_AND_KIOSK_SESS_REP = "Cashier and Kiosk Session Report";
	String CASHIER_SESS_REP = "Cashier Session Report";
	String KIOSK_SESS_REP = "Kiosk Session Report";


	String TKTSCANNER_DUPLO_MANFT="Duplo";
	String TKTSCANNER_CUMMINS_MANFT="Cummins";
	String TKTSCANNER_MULTISCAN_MANFT="MultiScan";
	String TKTSCANNER_METROLOGIC_MANFT = "Metrologic";

	String TKTSCANNER_DUPLO_MODEL="NB-510";
	String TKTSCANNER_CUMMINS_MODEL="JetScan-4091";
	String TKTSCANNER_MULTISCAN_MODEL="MT31";
	String TKTSCANNER_METROLOGIC_MODEL="MS3780";

	String TKTSCANNER_DUPLO_DRIVER="com.ballydev.sds.framework.readers.Duplo";
	String TKTSCANNER_CUMMINS_DRIVER="com.ballydev.sds.framework.readers.MultiScan";
	String TKTSCANNER_MULTISCAN_DRIVER="com.ballydev.sds.framework.readers.MultiScan";
	String TKTSCANNER_METROLOGIC_DRIVER="com.ballydev.sds.framework.readers.MetroLogicScan";

	String LINE_SEPERATOR= "\r\n";

	//USER PARAMETER CONSTANTS
	String STC_RECONCILIATION="STC RECONCILIATION";

	//REPORT CONSTANTS	
	String ALL_OPERATOR="ALL";
	String ALL_CONTROL_VALUE="--ALL--";
	String EQUAL_OPERATOR="=";
	String DETAIL_VALUE ="Detail";
	String SUMM_VALUE ="Summary";
	String CSH_FORM_VALUE="VOU.LKP.AMT.CASHABLE";
	String NCSH_PROMO_FORM_VALUE="VOU.LKP.AMT.NON.CASH.PROMO";
	String CSH_PROMO_FORM_VALUE="VOU.LKP.AMT.CASHABLE.PROMO";


	int START_TIME_PARAM_ID=106;
	int END_TIME_PARAM_ID=107;
	int EMP_ID_PARAM_ID=213;
	int EMP_ID_PARAM_ID1=50126;
	int PROPERTY_ID_PARAM_ID=214;
	int SITE_ID_PARAM_ID=7781;
	int TKT_TYPE_PARAM_ID=7848;
	int DETAILS_SUMM_PARAM_ID=7844;
	int COU_TYPE_PARAM_ID=82;
	int DATA_SRC_PARAM_ID=-100;

	//REPORT ID
	int CASHIER_AND_KIOSK_SESS_REP_ID=219;
	int CASHIER_SESS_REP_ID=220;
	int KIOSK_SESS_REP_ID=221;
	String REPORTS_MODULE_NAME="Reports";
	String PLAYER_CARD_ZERO="0000000000";


	String SERVICE_UNAVAILABLE_MSG1 ="Unreachable?: Service unavailable";
	String SERVICE_UNAVAILABLE_MSG2 ="cluster invocation failed";
	String SERVICE_UNAVAILABLE_MSG3 ="Connection refused";
	
	String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	String INVALID_USER="VOU.INVALID.USER";
	

	String VOU_CURR_SESS_DET = "currSessDetToggleBtn";
	String VOU_CURNT_SESS_SUMM = "currSessSummToggleBtn";
	String VOU_PREV_SESS_DET = "prevSessDetToggleBtn";
	String VOU_PREV_SESS_SUMM = "prevSessSummToggleBtn";
	String VOU_EXIT_WITHOUT_CLOSING = "exitWithoutCloseToggleBtn";
	String VOU_EXIT_AFTER_CLOSING = "exitAfterCloseToggleBtn";
	String VOU_SESS_CANCEL = "sessionCancel";
	String VOU_SESS_SUBMIT = "sessSubmit";
	int MAX_LENGTH_PRINT = 40;
	String NEWLINE = "\r\n";
	
	//The same set of values for the below two is mainted in the
	//voucherconstants of server side code. If this is modifed, please modified that too
	//1- to indicate the log out is done but session is not exited
	//0- session is normally exited
	int SESSION_NOT_PAUSED	=	0;	
	int SESSION_PAUSED		=	1;

//////////////////////////OVERRIDE VOUCHER///////////////////////////
	
	String DEFAULT_PLAYER_ID 							= "0000000000";

//	PROPERTY NAMES
	String OVERRIDEVOUCHER_FRM_TXT_TICKETBARCODE		= "barCode";
	String OVERRIDEVOUCHER_FRM_TXT_TICKET_AMOUNT		= "amount";
	String OVERRIDEVOUCHER_FRM_TXT_TICKET_CREATED_TIME	= "cretaedDateTime";
	String OVERRIDEVOUCHER_FRM_TXT_TICKET_CREATED_ASSET = "assetNumber";
	String OVERRIDEVOUCHER_FRM_TXT_TICKET_PLAYER_ID 	= "playerId";

//	CONTROL NAMES
	String OVERRIDEVOUCHER_CTRL_TXT_TICKETBARCODE 		= "txtTicketBarcode";
	String OVERRIDEVOUCHER_CTRL_TXT_TICKET_AMOUNT		= "ticketAmount";
	String OVERRIDEVOUCHER_CTRL_TXT_TICKET_CREATED_TIME	= "ticketCreatedTime";
	String OVERRIDEVOUCHER_CTRL_TXT_TICKET_CREATED_ASSET= "ticketCreatedAsset";
	String OVERRIDEVOUCHER_CTRL_TXT_TICKET_PLAYER_ID	= "ticketPlayrId";
	
	String OVERRIDEVOUCHER_CTRL_BTN_SUBMIT 				= "btnSubmitBarcode";
	String OVERRIDEVOUCHER_CTRL_BTN_DELETE 				= "btnDelete";
	String OVERRIDEVOUCHER_CTRL_BTN_AUTHORISE 			= "btnAuthorise";
	String OVERRIDEVOUCHER_CTRL_BTN_PRINT_TICKET_LOGS	= "btnPrintTktLogs";

//	COMPOSITE CONTROLS
	String OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SUMMARY 	= "lstTktInfoDTO";
	String OVERRIDEVOUCHER_CTRL_TABLE_TICKET_DETAIL 	= "tktInfoDTO";
	String OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SEL_ITEM 	= "ticketListDTOSelectedValue";
	
//	SITECONFIG PARAMETER	
	String OVERRIDEVOUCHER_ALLOW_TICKET_NOT_FOUND		= "ALLOW.TICKET.NOT.FOUND";
	

	//STANDARD AND ENHANCED VALDATION PARAMETER
	String STANDARD_ENHANCED_VALIDATION_BARCODE_INDICATOR = "00";

	String STANDARD_VALIDATION_FOR_TICKET 	= "VCH.STANDARD.VALIDATION.ENABLED";
    String VCH_BARCODE_SYSTEM_VALIDATION 	= "VCH.BARCODE.SYSTEM.VALIDATION";
	String VCH_BARCODE_STANDARD_VALIDATION 	= "VCH.BARCODE.STANDARD.VALIDATION";
	String VCH_BARCODE_ENHANCED_VALIDATION 	= "VCH.BARCODE.ENHANCED.VALIDATION";
	
	int VOCH_BARCODE_STANDARD_VALIDATION 	= 1;
	int VOCH_BARCODE_ENHANCED_VALIDATION 	= 2;
	
	//PROPERTY FILE NAME
	public static final String VOUCHER_PROPERTIES = "settings/voucher.properties";
	public static final String VCH_CURRENCY_SYMBOL_ASCII = "CURRENCY.SYMBOL.VALUE";
	public static final String VCH_CONVERT_TO_ASCII = "CONVERT.TO.ASCII";
}
