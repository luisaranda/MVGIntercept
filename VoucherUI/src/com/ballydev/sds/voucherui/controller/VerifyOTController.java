/*****************************************************************************
 * $Id: VerifyOTController.java,v 1.5, 2010-12-07 10:00:46Z, Verma, Nitin Kumar$
 * $Date: 12/7/2010 4:00:46 AM$
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
package com.ballydev.sds.voucherui.controller;

import java.io.File;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSLabelText;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucher.dto.OfflineTicketInfoDTO;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.VerifyOTComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.VerifyOTForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.PreferencesUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.TicketStorage;
import com.ballydev.sds.voucherui.util.VoucherUtil;


public class VerifyOTController extends SDSBaseController {

	private VerifyOTComposite composite;
	private VerifyOTForm form;
	private boolean keyVerified = false;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public VerifyOTController(Composite parent, int style,
			VerifyOTForm form, SDSValidator validator) throws Exception  {

		super(form,validator);
		this.form = form;
		createVerifyOTComposite(parent, style);
		VoucherMiddleComposite.setCurrentComposite(composite);
		parent.layout();
		form.addPropertyChangeListener(this);
		super.registerEvents(composite);
		composite.setEnabled(true);
		this.composite.getTxtTicketBarcode().setFocus();		
	}

	private void createVerifyOTComposite(Composite parent,int style) {
		composite = new VerifyOTComposite(parent,style);
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {

	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
		super.widgetSelected(e);
		Control control = (Control) e.getSource();

		if(!(control instanceof SDSTSButton)){
			return;
		}

		SDSTSButton eventSrc = (SDSTSButton) control;

		if (eventSrc.getName().equalsIgnoreCase(IVoucherConstants.VERIFYOT_CTRL_BTN_VERIFY)){
			try
			{
				populateForm(composite);
				boolean isValidate = validate(IVoucherConstants.VERIFY_OT_FORM,form,composite);
				if(!isValidate)
					return;

				TicketInfoDTO tktInfoDTO = new TicketInfoDTO();
				tktInfoDTO.setBarcode(form.getTicketBarcodeValue());
				tktInfoDTO.setAmount(ConversionUtil.dollarToCents(form.getTicketAmountValue()));							

				OfflineTicketInfoDTO otForm =  ServiceCall.getInstance().validateOfflineTicket(tktInfoDTO);
				if(otForm!=null){
					if(otForm.isErrorPresent())		{
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getI18nMessageForDisplay(otForm));
					}
					else	{
						if(otForm.isOfflineTktValid())	{
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_OFFLINE_TICKET_VERIFY_SUCCESS));
							composite.getBtnVerify().setEnabled(false);
							composite.getBtnRedeem().setEnabled(true);
							composite.getTxtTicketBarcode().setEnabled(false);
							composite.getTxtTicketAmount().setEnabled(false);
						}
						else {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_OFFLINE_TICKET_VERIFY_FAILURE));
						}

					}
				}
			}catch(VoucherEngineServiceException ex)	{
				log.error("Exception while validating the offline ticket", ex);						
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
			}
			catch (Exception ex){
				log.error(ex);
			}
		}

		if (eventSrc.getName().equalsIgnoreCase(IVoucherConstants.VERIFYOT_CTRL_BTN_REDEEM)){
			try	{
				//System.out.println("Clicked Redeem");
				String dirName="/sds/ot";//Should be configurable?
				String fileName = "otstore";

				File dir = new File(dirName);

				if(!dir.exists())
				{
					dir.mkdirs();
				}	

				TicketStorage ts = new TicketStorage(dirName+"/"+fileName);
				ts.setOTC(true);
				String barcode = form.getTicketBarcodeValue();
				String amount = form.getTicketAmountValue();
				GregorianCalendar currTime = new GregorianCalendar();
				//TODO: check what info needs to be saved in files. Currently using the default info that is saved by existing versions of SDS.
				TicketDetails td = new TicketDetails("CreateTicket", 
						"Success", barcode, PreferencesUtil.getClientAssetNumber(), Double.parseDouble(amount), 
						"0", "Success", "00000000", currTime.getTimeInMillis(),
						currTime.getTimeInMillis(), SDSApplication.getUserDetails().getUserName(), 
						currTime.getTime().toString(), "false");

				String amountValue = composite.getTxtTicketAmount().getText();
				td.setAmount(Double.parseDouble(amountValue));
				ts.add(td);
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.MSG_OFFLINE_TICKET_REDEEM_SUCCESS));
				resetScreen();
				composite.getTxtTicketBarcode().setFocus();
			}
			catch (Exception ex)
			{
				log.error(ex);
				VoucherUIExceptionHandler.handleException(ex); 	
			}
		}

		if (eventSrc.getName().equalsIgnoreCase(IVoucherConstants.VERIFYOT_CTRL_BTN_CANCEL)){			
			resetScreen();
			composite.getTxtTicketBarcode().setFocus();
		}
	}

	private void resetScreen()
	{
		//Clear controls and prepare screen to verify a ticket.
		composite.getTxtTicketBarcode().setEnabled(true);
		composite.getTxtTicketBarcode().setText("");

		composite.getTxtTicketAmount().setEnabled(true);
		composite.getTxtTicketAmount().setText("");

		composite.getBtnVerify().setEnabled(true);
		composite.getBtnRedeem().setEnabled(false);
	}

	public void allowOTVerification() {
		composite.setEnabled(true);
	}

	public boolean isKeyVerified() {
		return keyVerified;
	}

	public void setKeyVerified(boolean keyVerified) {
		this.keyVerified = keyVerified;
	}

	@Override
	public void focusGained(FocusEvent e) 
	{
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
		if (e.getSource() instanceof SDSTSLabelText)
			KeyBoardUtil.setCurrentTextInFocus(((SDSTSLabelText) e.getSource()).getSdsText());
	}
}
