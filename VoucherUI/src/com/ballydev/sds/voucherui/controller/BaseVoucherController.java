/*****************************************************************************
 * $Id: BaseVoucherController.java,v 1.2, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:43:47 AM$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSKeyBoardDialog;
import com.ballydev.sds.framework.control.SDSTSKeyPadButton;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public abstract class BaseVoucherController extends SDSBaseController
{
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	protected Composite composite ;

	@Override
	public void widgetSelected(SelectionEvent e) {
		baseEventHandler(e);
	}

	public BaseVoucherController(Composite parent,
			int style,SDSForm sdsForm,SDSValidator pValidator) throws Exception {
		super(sdsForm, pValidator);
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		// TODO Auto-generated method stub
	}

	private void baseEventHandler(TypedEvent typedEvent)
	{
		try
		{
			if( typedEvent.getSource() instanceof SDSTSKeyPadButton )
			{
				SDSKeyBoardDialog sDSKeyBoardDialog = 
					new SDSKeyBoardDialog(getComposite().getShell(),
							((SDSTSKeyPadButton)typedEvent.getSource()).getSdsTsText());
				sDSKeyBoardDialog.open(SWT.NONE,false);
			}
		}
		catch(Exception e)
		{
			log.error("Error occured in baseEventHandler", e);
		}
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public void showMessageBox(String message){
		MessageDialogUtil.displayTouchScreenInfoDialog(message);
	}

	public long getSiteId() {
		UserDTO userDTO = SDSApplication.getUserDetails();		
		return userDTO.getSiteId();
	}
}


