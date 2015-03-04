package com.ballydev.sds.voucherui.exception;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

public class VoucherUIExceptionHandler {

	/**
	 * This method displays the message dialog when the 
	 * exception is thrown in the specified composite
	 * @param composite
	 * @param e
	 */
	public static void handleException(Exception e){
		if(e.getMessage()!=null){
			if(e.getMessage().contains(IVoucherConstants.SERVER_ERROR)){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}else if(e.getMessage().contains(IVoucherConstants.JDBC_ROLLBACK)){		
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DB_ERROR));
			}		
		}
	}

	/**
	 * This method displays the message dialog when the 
	 * exception is thrown in the specified composite
	 * @param composite
	 * @param e
	 * @param user message
	 */
	public static void handleClientException( String userMessage){		
		if(userMessage.contains(IVoucherConstants.JDBC_ROLLBACK)){		
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DB_ERROR));
		}else{
			if(userMessage!=null && userMessage.trim().length()!=0){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(userMessage);
			}
		}			
	}


}
