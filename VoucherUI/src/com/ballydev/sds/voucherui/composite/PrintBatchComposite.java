/*****************************************************************************
 *	Copyright (c) 2006 Bally Technology  1977 - 2008
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;

/**
 * @author Ganesh Amirtharaj
 */
public class PrintBatchComposite extends Composite {

	private SDSTSLabel lblHeading 				= null;
	private SDSTSLabel lblPrintSelected 		= null;
	private SDSTSLabel lblPrintEntered 			= null;
	private SDSTSLabel lblPrintAll 				= null;
	private SDSTSLabel lblBatchNumber 			= null;
	private SDSTSText txtBatchNumber 			= null;
	private Composite composite 				= null;
	private TSButtonLabel rBtnSelected	 		= null;
	private TSButtonLabel rBtnPrintEnteredBatch = null;
	private TSButtonLabel rBtnPrintAll 			= null;
	private SDSImageLabel iLblPrint 			= null;
	private SDSImageLabel iLblCancel 			= null;
	private RadioButtonControl radioButtonControl;
	private Image buttonBG;

	public static final String BTN_PRINT_SELECTED="BTN_PRINT_SELECTED";
	public static final String BTN_PRINT_ENTERED="BTN_PRINT_ENTERED";
	public static final String BTN_PRINT_ALL="BTN_PRINT_ALL";
	public static final String BTN_PRINT="BTN_PRINT";
	public static final String BTN_CANCEL="BTN_CANCEL";
	public static final String TXT_BATCH_NUMBER="TXT_BATCH_NUMBER";
	
	public PrintBatchComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		createImages();
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 3;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = false;
		gridData6.horizontalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.widthHint = 160;
		GridData gridData4 = new GridData();
		gridData4.widthHint = 160;
		GridData gridData3 = new GridData();
		gridData3.widthHint = 160;
		GridData gridData21 = new GridData();
		gridData21.widthHint = 160;
		GridData gridData11 = new GridData();
		gridData11.heightHint = 25;
		gridData11.widthHint = 150;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		GridData gridData2 = new GridData();
		gridData2.heightHint = 50;
		gridData2.widthHint = 50;
		GridData gridData1 = new GridData();
		gridData1.heightHint = 50;
		gridData1.widthHint = 50;
		GridData gridData = new GridData();
		gridData.heightHint = 50;
		gridData.widthHint = 50;
		
		lblHeading = new SDSTSLabel(this, SWT.NONE);
		lblHeading.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_HEADING));
		lblHeading.setLayoutData(gridData6);
		lblHeading.setBackground(SDSControlFactory.getTSBodyColor());
		
		radioButtonControl = new RadioButtonControl("Search by any one of the following");
		
		rBtnSelected = new TSButtonLabel(this, SWT.NONE, "", BTN_PRINT_SELECTED );
		rBtnSelected.setLayoutData(gridData);
		rBtnSelected.setBackground(SDSControlFactory.getTSBodyColor());
		radioButtonControl.add(rBtnSelected);
		
		lblPrintSelected = new SDSTSLabel(this, SWT.NONE);
		lblPrintSelected.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_SELECTED));
		lblPrintSelected.setBackground(SDSControlFactory.getTSBodyColor());
		lblPrintSelected.setLayoutData(gridData21);
		
		lblBatchNumber = new SDSTSLabel(this, SWT.NONE);
		lblBatchNumber.setText("");
		lblBatchNumber.setBackground(SDSControlFactory.getTSBodyColor());
		lblBatchNumber.setLayoutData(gridData5);
		
		rBtnPrintEnteredBatch = new TSButtonLabel(this, SWT.NONE, "", BTN_PRINT_ENTERED);
		rBtnPrintEnteredBatch.setLayoutData(gridData1);
		rBtnPrintEnteredBatch.setBackground(SDSControlFactory.getTSBodyColor());
		radioButtonControl.add(rBtnPrintEnteredBatch);
		
		lblPrintEntered = new SDSTSLabel(this, SWT.NONE);
		lblPrintEntered.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_ENTERED));
		lblPrintEntered.setBackground(SDSControlFactory.getTSBodyColor());
		lblPrintEntered.setLayoutData(gridData3);
		txtBatchNumber = new SDSTSText(this, SWT.BORDER, TXT_BATCH_NUMBER, "batchNumber", true);
		txtBatchNumber.setLayoutData(gridData11);
		
		rBtnPrintAll = new TSButtonLabel(this, SWT.NONE, "", BTN_PRINT_ALL);
		rBtnPrintAll.setLayoutData(gridData2);
		rBtnPrintAll.setBackground(SDSControlFactory.getTSBodyColor());
		radioButtonControl.add(rBtnPrintAll);
		
		lblPrintAll = new SDSTSLabel(this, SWT.NONE);
		lblPrintAll.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_ALL));
		lblPrintAll.setBackground(SDSControlFactory.getTSBodyColor());
		lblPrintAll.setLayoutData(gridData4);
		
		new Label(this, SWT.NONE);
		this.setLayout(gridLayout);
		createComposite();
		setControlProperties();
		this.setBackground(SDSControlFactory.getTSBodyColor());
		if( Util.isSmallerResolution() ) {
			this.setSize(new Point(393, 250));
		} else {
			this.setSize(new Point(393, 260));
		}
	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
	
	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		
		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 10;
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.verticalSpacing = 5;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 0;
		
		GridData gridData8 = new GridData();
		gridData8.horizontalSpan = 3;
		gridData8.widthHint = 340;
		gridData8.horizontalAlignment = GridData.BEGINNING;
		gridData8.verticalAlignment = GridData.CENTER;
		gridData8.horizontalIndent = 1;
		gridData8.grabExcessHorizontalSpace = false;
		gridData8.verticalSpan = 2;
		gridData8.heightHint = 100;
		
		composite = new Composite(this, SWT.NONE);
		composite.setBackground(SDSControlFactory.getTSBodyColor());
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData8);
		
		iLblPrint = new SDSImageLabel(composite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_DETAILS), BTN_PRINT);
		iLblPrint.setLayoutData(gdRadio);
		
		iLblCancel = new SDSImageLabel(composite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.CANCEL_BUTTON), BTN_CANCEL);
		iLblCancel.setLayoutData(gdRadio);
	}

	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblCancel);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblPrint);
			
			SDSControlFactory.setTouchScreenLabelProperties(lblHeading);
			SDSControlFactory.setTouchScreenLabelProperties(lblBatchNumber);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrintSelected);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrintEntered);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrintAll);
			
			GridData gdBtnCancel = (GridData) iLblCancel.getLayoutData();			
			GridData gdBtnPrint = (GridData) iLblPrint.getLayoutData();			

			if( Util.isSmallerResolution() ) {
				int width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 8;
				int height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 13;
				gdBtnCancel.widthHint  = width;
				gdBtnCancel.heightHint 	= height;
				gdBtnPrint.widthHint = width;
				gdBtnPrint.heightHint = height;
			}
			iLblCancel.setLayoutData(gdBtnCancel);
			iLblPrint.setLayoutData(gdBtnPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the iLblPrint
	 */
	public SDSImageLabel getILblPrint() {
		return iLblPrint;
	}

	/**
	 * @return the iLblCancel
	 */
	public SDSImageLabel getILblCancel() {
		return iLblCancel;
	}

	public TSButtonLabel getRBtnPrintAll() {
		return rBtnPrintAll;
	}

	public TSButtonLabel getRBtnPrintEnteredBatch() {
		return rBtnPrintEnteredBatch;
	}

	public TSButtonLabel getRBtnPrintSelected() {
		return rBtnSelected;
	}

	/**
	 * @return the radioButtonControl
	 */
	public RadioButtonControl getRadioButtonControl() {
		return radioButtonControl;
	}

	public SDSTSText getTxtBatchNumber() {
		return txtBatchNumber;
	}

	public SDSTSLabel getLblBatchNumber() {
		return lblBatchNumber;
	}

} 
