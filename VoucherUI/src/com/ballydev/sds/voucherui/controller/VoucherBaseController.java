/*****************************************************************************
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

import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.validator.SDSValidator;

/**
 * This class acts as a base controller for all the
 * controllers
 * @author Nithya kalyani R
 * @version $Revision: 1$ 
 */
public class VoucherBaseController extends SDSBaseController{
	/**
	 * Instance of the parent composite
	 */
	private Composite parentComposite = null;
	
	/**
	 * This variable will be set to true if teh next screen
	 * can be opened
	 */
	private boolean isScreenReady = true;


	/**
	 * Constructor of the class
	 */
	public VoucherBaseController(Composite parent, int style,SDSForm sdsForm, SDSValidator validator)throws Exception {
		super(sdsForm, validator);		
		this.parentComposite = parent;		
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor of the class
	 */
	public VoucherBaseController(SDSForm sdsForm, SDSValidator validator)throws Exception {
		super(sdsForm, validator);	
		// TODO Auto-generated constructor stub		
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return parentComposite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method returns true if the next screen is 
	 * ready to open
	 * @return
	 */
	public boolean isScreenReadyToOpen(){	
		return isScreenReady;
	}


	/**
	 * @return the parentComposite
	 */
	public Composite getParentComposite() {
		return parentComposite;
	}


	/**
	 * @param parentComposite the parentComposite to set
	 */
	public void setParentComposite(Composite parentComposite) {
		this.parentComposite = parentComposite;
	}

	/**
	 * @return the isScreenReady
	 */
	public boolean isScreenReady() {
		return isScreenReady;
	}

	/**
	 * @param isScreenReady the isScreenReady to set
	 */
	public void setScreenReady(boolean isScreenReady) {		
		this.isScreenReady = isScreenReady;
	}

}
