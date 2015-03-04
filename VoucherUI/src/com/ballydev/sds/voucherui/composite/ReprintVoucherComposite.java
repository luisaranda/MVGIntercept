/*****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class creates the voucher reprint composite
 * @author Nithya kalyani
 * @version $Revision: 3$ 
 */
public class ReprintVoucherComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {
	/**
	 * Instance of the barcode label
	 */
	private SDSTSMandatoryLabel lblBarcode = null;

	/**
	 * Instance of the barcode text
	 */
	private SDSTSText txtBarcode= null;

	/**
	 * Instance of the cancel button 
	 */
	private SDSTSButton btnCancel = null;

	/**
	 * Instance of the print button
	 */
	private SDSTSButton btnPrint = null;

	/**
	 * Instance of the main composite
	 */
	private Composite mainComposite = null;

	/**
	 *Instance of the button composite 
	 */
	private Composite btnComposite = null;

	/**
	 * Constructor of the class
	 */
	public ReprintVoucherComposite(Composite parent, int style)
	{
		super(parent, style);
		initialize();
		this.setBounds(0,0, 800,600);
	}

	/**
	 * This method sets the layoutdata for the
	 * composite
	 */
	private void initialize() {
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 5;
		grlPage.marginHeight = 5;
		grlPage.marginWidth = 5;
		grlPage.horizontalSpacing = 5;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_REPRINT_VOUCHER_HEADER)+ AppContextValues.getInstance().getTicketText());
		createMainComposite();	

		setDefaultProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		layout();
	}

	/**
	 * This method creates the main composite
	 */
	public void createMainComposite()
	{
		GridData gdMainComp = new GridData();
		gdMainComp.grabExcessHorizontalSpace = true;
		gdMainComp.verticalAlignment = GridData.CENTER;
		gdMainComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlMainComp = new GridLayout();
		grlMainComp.numColumns = 2;
		grlMainComp.verticalSpacing = 15;
		grlMainComp.horizontalSpacing = 20;

		mainComposite = new Composite(this,SWT.NONE);
		mainComposite.setLayout(grlMainComp);
		mainComposite.setLayoutData(gdMainComp);			


		lblBarcode = new SDSTSMandatoryLabel(mainComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_ENTER_BARCODE));
		txtBarcode = new SDSTSText(mainComposite,SWT.NONE ,PRINTVOUCHER_CTRL_TXT_BARCODE,PRINTVOUCHER_CTRL_TXT_BARCODE,true);
		txtBarcode.setTextLimit(18);		
		createBtnComposite(mainComposite);			


	}

	/**
	 * This method initializes btnComposite	Prata
	 *
	 */
	private void createBtnComposite(Composite mainComposite) 
	{
		GridData gdBtnComp = new GridData();
		gdBtnComp.grabExcessHorizontalSpace = true;
		gdBtnComp.verticalAlignment = GridData.CENTER;
		gdBtnComp.horizontalSpan = 2;	
		gdBtnComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlBtnComp = new GridLayout();
		grlBtnComp.numColumns = 2;
		grlBtnComp.marginWidth = 4;
		grlBtnComp.marginHeight = 15;
		grlBtnComp.verticalSpacing = 5;
		grlBtnComp.horizontalSpacing = 25;

		btnComposite = new Composite(mainComposite, SWT.NONE);
		btnComposite.setLayout(grlBtnComp);
		btnComposite.setLayoutData(gdBtnComp);

		btnPrint = new SDSTSButton(btnComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_SUBMIT),PRINT_VOUCHER_CTRL_BTN_PRINT);	
		btnCancel = new SDSTSButton(btnComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_CANCEL),PRINT_VOUCHER_CTRL_BTN_CANCEL);	
	}

	/**
	 * This method sets the default properties for all the 
	 * controls
	 */
	public void setDefaultProperties(){
		try	{			
			SDSControlFactory.setTouchScreenLabelProperties(lblBarcode.getLabel());			
			SDSControlFactory.setTouchScreenTextProperties(txtBarcode);	
			SDSControlFactory.setTouchScreenButtonProperties(btnCancel);
			SDSControlFactory.setTouchScreenButtonProperties(btnPrint);

			GridData gdBtnCancel = (GridData)btnCancel.getLayoutData();
			gdBtnCancel.heightHint = ControlConstants.BUTTON_HEIGHT;
			gdBtnCancel.widthHint = ControlConstants.BUTTON_WIDTH;
			btnCancel.setLayoutData(gdBtnCancel);

			GridData gdBtnPrint = (GridData)btnPrint.getLayoutData();
			gdBtnPrint.heightHint = ControlConstants.BUTTON_HEIGHT;
			gdBtnPrint.widthHint = ControlConstants.BUTTON_WIDTH;
			btnPrint.setLayoutData(gdBtnPrint);
		} 
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return the btnCancel
	 */
	public SDSTSButton getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @param btnCancel the btnCancel to set
	 */
	public void setBtnCancel(SDSTSButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	/**
	 * @return the btnPrint
	 */
	public SDSTSButton getBtnPrint() {
		return btnPrint;
	}

	/**
	 * @param btnPrint the btnPrint to set
	 */
	public void setBtnPrint(SDSTSButton btnPrint) {
		this.btnPrint = btnPrint;
	}

	/**
	 * @return the txtBarcode
	 */
	public SDSTSText getTxtBarcode() {
		return txtBarcode;
	}

	/**
	 * @param txtBarcode the txtBarcode to set
	 */
	public void setTxtBarcode(SDSTSText txtBarcode) {
		this.txtBarcode = txtBarcode;
	}



}

