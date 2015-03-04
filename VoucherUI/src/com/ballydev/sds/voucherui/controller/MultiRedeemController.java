/*****************************************************************************
 * $Id: MultiRedeemController.java,v 1.8.1.1.1.0, 2013-10-25 17:50:06Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 12:50:06 PM$
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

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.MultiRedeemComposite;
import com.ballydev.sds.voucherui.composite.RedeemVoucherComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.form.MultiRedeemForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public class MultiRedeemController extends SDSBaseController {
	public MultiRedeemComposite composite;

	public MultiRedeemForm form;

	RedeemVoucherController redeemVoucherController;
	
	private Image enableImage;
	private Image disableImage;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public MultiRedeemController(Composite parent, int style, MultiRedeemForm form,SDSValidator validator,RedeemVoucherController redeemVoucherController) throws Exception
	{
		super(form, validator);
		this.composite = new MultiRedeemComposite(parent, style);
		this.form = form;
		this.redeemVoucherController = redeemVoucherController;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);
		refreshControl(composite.getTxtAmountTotal());
		refreshControl(composite.getTxtVoucherCoupon());
		registerCustomizedListeners(composite);
		if( Util.isSmallerResolution() ) {
			parent.layout();
		}
		enableImage = ((RedeemVoucherComposite)redeemVoucherController.getComposite()).getButtonImage();
		disableImage = ((RedeemVoucherComposite)redeemVoucherController.getComposite()).getButtonDisableImage();
		composite.getBtnResetTotal().setEnabled(false);
		composite.getBtnResetTotal().setImage(disableImage);
		composite.getBtnResetTotal().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		
	}

	@Override
	public MultiRedeemComposite getComposite()
	{
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite)
	{
		if(argComposite instanceof MultiRedeemComposite) {
			MultiRedeemComposite mrComp = (MultiRedeemComposite) argComposite;
			mrComp.getBtnCancel().addMouseListener(this);
			mrComp.getBtnResetTotal().addMouseListener(this);
		}
	}
	
	
	@Override
	public void mouseDown(MouseEvent e) {
		try
		{
			Control control = (Control)e.getSource();
			if(control instanceof SDSImageLabel 
					&& ((SDSImageLabel)control).getName().equals(IVoucherConstants.MULTIREDEEM_CTRL_BTN_RESETTOTAL))
			{
				NumberFormat formatter = new DecimalFormat("000000");
		        formatter = new DecimalFormat("0.00");
		        String FormattedNumber_Count = formatter.format(redeemVoucherController.getMultiRedeemCount());
		        String FormattedNumber_Amount = formatter.format(redeemVoucherController.getMultiRedeemAmount());
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_REDEEM_TICKETS_COUNTS) + String.valueOf(FormattedNumber_Count) +
                        "\r\n" +LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_REDEEM_TICKETS_TOTALAMOUNT) + String.valueOf(FormattedNumber_Amount));

				redeemVoucherController.setMultiRedeemAmount(0);
				redeemVoucherController.setMultiRedeemCount(0);
				form.setAmountTotal("0.00");
				form.setVoucherCoupon("0");
				composite.getBtnResetTotal().setEnabled(false);
				composite.getBtnResetTotal().setImage(disableImage);
				composite.getBtnResetTotal().getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
				
			}
			if(control instanceof SDSImageLabel
					&& ((SDSImageLabel)control).getName().equals(IVoucherConstants.MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL))
			{
				if (redeemVoucherController.getMultiRedeemCount()!=0)
				{
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_REDEEM_TICKETS_INPROCESS));
					return;
				}
				if(redeemVoucherController != null 
						&& ((RedeemVoucherComposite)redeemVoucherController.getComposite()) != null 
						&& ((RedeemVoucherComposite)redeemVoucherController.getComposite()).getMultiRedeemComposite() != null 
						&& !((RedeemVoucherComposite)redeemVoucherController.getComposite()).getMultiRedeemComposite().isDisposed())
				{
					composite.dispose();
					((RedeemVoucherComposite)redeemVoucherController.getComposite()).getLblMultiRedeem().setImage(enableImage);
					((RedeemVoucherComposite)redeemVoucherController.getComposite()).getLblMultiRedeem().setEnabled(true);
					((RedeemVoucherComposite)redeemVoucherController.getComposite()).getLblMultiRedeem().getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
				}
			}
		}
		catch(Exception ex)
		{
			log.error("Error in MultiRedeemController.widgetSelected",ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
	}

	

	public MultiRedeemForm getForm()
	{
		return form;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		super.focusGained(e);
		composite.redraw();
	}*/
}
