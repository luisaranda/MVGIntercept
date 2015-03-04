/*****************************************************************************
 * $Id: TopFooterComposite.java,v 1.2, 2010-02-16 08:13:08Z, Anbuselvi, Balasubramanian$
 * $Date: 2/16/2010 2:13:08 AM$
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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlButton;

/**
 * The Footer composite of Top Composite
 * 
 * @author dambereen
 * @version $Revision: 3$
 */
public class TopFooterComposite extends Composite {
	/**
	 * Logout Button Instance
	 */
	private CbctlButton btnExit = null;
	/**
	 * Exit Button Instance
	 */
	private CbctlButton btnLogout = null;
	/**
	 * Keyboard label Instance
	 */
	private CbctlLabel lblKeyboard = null;
	/**
	 * Manual label Instance
	 */
	private CbctlLabel lblExit = null;
	/**
	 * Keyboard Button Instance
	 */
	private CbctlButton btnKeyboard = null;
	/**
	 * Logout label Instance
	 */
	private CbctlLabel lblLogout = null;
	/**
	 * JackpotUIImage Label Instance
	 */
	private CbctlLabel jackpotUIImage = null;
	/**
	 * JackpotUIText Label Instance
	 */
	private CbctlLabel jackpotUIText = null;
	/**
	 * Printer Button Instance
	 */
	private CbctlButton btnPrinter = null;
	/**
	 * Printer Label Instance
	 */
	private CbctlLabel lblPrinter = null;
	/**
	 * BallyCopyright Label Instance
	 */
	private CbctlLabel lblBallyCopyright = null;
	
	private String imgPrinterBtn;
	
	private String imgKeyboardBtn;
	
	private String imgLogoutBtn;
	
	private String imgExitBtn;
	
	private String imgJackpotUITxt;
	
	private String imgJackpotUI;
	
	
	

	/**
	 * Class Constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public TopFooterComposite(Composite parent, int style) {
		super(parent, style);		
		if (Util.isSmallerResolution()) {
			imgPrinterBtn = ImageConstants.S_PRINTER_BUTTON_IMG;
			imgKeyboardBtn = ImageConstants.S_KEYBOARD_BUTTON_IMG;
			imgLogoutBtn = ImageConstants.S_LOGOUT_BUTTON_IMG;
			imgExitBtn = ImageConstants.S_EXIT_BUTTON_IMG;
			imgJackpotUITxt = ImageConstants.S_JACKPOT_UI_TEXT_IMAGE;
			imgJackpotUI = ImageConstants.S_JACKPOT_UI_TEXT;
		}
		else {
			imgPrinterBtn = ImageConstants.PRINTER_BUTTON_IMG;
			imgKeyboardBtn = ImageConstants.KEYBOARD_BUTTON_IMG;
			imgLogoutBtn = ImageConstants.LOGOUT_BUTTON_IMG;
			imgExitBtn = ImageConstants.EXIT_BUTTON_IMG;
			imgJackpotUITxt = ImageConstants.JACKPOT_UI_TEXT_IMAGE;
			imgJackpotUI = ImageConstants.JACKPOT_UI_TEXT;
		}
		initialize();
		setSize(new Point(800, 100));
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	/**
	 * Method to initialize the controls
	 */
	private void initialize() {
		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = GridData.BEGINNING;
		gridData14.horizontalSpan = 2;
		gridData14.verticalAlignment = GridData.END;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.CENTER;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = GridData.CENTER;
		GridData gridData6 = new GridData();
		gridData6.heightHint = 75;
		gridData6.horizontalAlignment = GridData.CENTER;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = false;
		gridData6.widthHint = 75;
	
		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = false;
		gridData21.horizontalAlignment = GridData.END;
		gridData21.verticalAlignment = GridData.BEGINNING;
		gridData21.grabExcessVerticalSpace = true;
		GridData gridData13 = new GridData();
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.horizontalAlignment = GridData.BEGINNING;
		gridData13.verticalAlignment = GridData.BEGINNING;
		gridData13.horizontalIndent = 0;
		gridData13.heightHint = -1;
		gridData13.grabExcessVerticalSpace = true;
		
		jackpotUIImage = new CbctlLabel(this, SWT.NONE);
		jackpotUIText = new CbctlLabel(this, SWT.NONE);
		btnPrinter = new CbctlButton(this, SWT.NONE,"","");
		btnPrinter.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgPrinterBtn)));
		btnPrinter.setLayoutData(gridData6);
		jackpotUIImage.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgJackpotUITxt)));
		jackpotUIImage.setLayoutData(gridData21);

		jackpotUIImage.setBackground(new Color(Display.getCurrent(), 171, 209,
				255));
		jackpotUIText.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgJackpotUI)));
		jackpotUIText.setLayoutData(gridData13);

		jackpotUIText.setBackground(new Color(Display.getCurrent(), 171, 209,
				255));
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.heightHint = 75;
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.widthHint = 75;

		GridData gridData32 = new GridData();
		gridData32.horizontalAlignment = GridData.CENTER;
		gridData32.grabExcessVerticalSpace = true;
		gridData32.horizontalIndent = 5;
		gridData32.grabExcessHorizontalSpace = true;
		gridData32.verticalAlignment = GridData.CENTER;

		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = GridData.CENTER;
		gridData31.grabExcessVerticalSpace = true;
		gridData31.horizontalIndent = 5;
		gridData31.grabExcessHorizontalSpace = true;
		gridData31.verticalAlignment = GridData.CENTER;

		GridData gridData12 = new GridData();
		gridData12.grabExcessVerticalSpace = false;
		gridData12.horizontalIndent = 0;
		gridData12.heightHint = 75;
		gridData12.widthHint = 75;
		gridData12.horizontalAlignment = GridData.CENTER;
		gridData12.verticalAlignment = GridData.CENTER;
		gridData12.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 5;
		gridLayout2.marginHeight = 20;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginWidth = 20;
		gridLayout2.makeColumnsEqualWidth = false;
		gridLayout2.numColumns = 6;
		GridData gridData11 = new GridData();
		gridData11.heightHint = 75;
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.verticalAlignment = GridData.CENTER;
		gridData11.grabExcessVerticalSpace = false;
		gridData11.grabExcessHorizontalSpace = false;
		gridData11.horizontalIndent = 0;
		gridData11.widthHint = 75;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = GridData.BEGINNING;
		// gridData.heightHint = 100;
		gridData.horizontalAlignment = GridData.FILL;

		btnKeyboard = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.KEYBOARD_BUTTON);
		btnKeyboard.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgKeyboardBtn)));
		btnKeyboard.setLayoutData(gridData1);

		btnLogout = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.LOG_OUT_BUTTON);
		btnLogout.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgLogoutBtn)));
		btnLogout.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnLogout.setLayoutData(gridData12);

		btnExit = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.EXIT_BUTTON); //
		btnExit.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgExitBtn)));
		btnExit
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnExit.setLayoutData(gridData11);

		lblBallyCopyright = new CbctlLabel(this, SWT.NONE);
		

		lblBallyCopyright.setText(LabelLoader.getLabelValue(LabelKeyConstants.BALLY_COPYRIGHT_LABEL));
		lblBallyCopyright
		.setBackground(new Color(Display.getCurrent(), 171, 209, 255));

		lblBallyCopyright.setLayoutData(gridData14);
		lblBallyCopyright.setFont(new Font(Display.getDefault(), "Arial", 8, SWT.NORMAL));
		lblPrinter = new CbctlLabel(this, SWT.NONE);
		lblPrinter.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.PRINTER_BUTTON));
		lblPrinter.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblPrinter.setLayoutData(gridData7);
		lblPrinter
		.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		
		lblKeyboard = new CbctlLabel(this, SWT.CENTER);
		lblKeyboard.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.KEYBOARD_BUTTON));
		lblKeyboard.setFont(new Font(Display.getDefault(), "Arial", 12,
				SWT.BOLD));
		lblKeyboard.setLayoutData(gridData31);
		lblKeyboard
				.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		
		lblLogout = new CbctlLabel(this, SWT.CENTER);
		lblLogout.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblLogout.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.LOG_OUT_BUTTON));

		lblLogout.setLayoutData(gridData2);
		lblLogout
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblExit = new CbctlLabel(this, SWT.CENTER);
		lblExit.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.CLOSE_BUTTON));
		lblExit.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblExit.setLayoutData(gridData32);

		lblExit
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		this.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		this.setLayout(gridLayout2);
		this.setLayoutData(gridData);
	}

	/**
	 * @return the btnExit
	 */
	public CbctlButton getBtnExit() {
		return btnExit;
	}

	/**
	 * @param btnExit the btnExit to set
	 */
	public void setBtnExit(CbctlButton btnExit) {
		this.btnExit = btnExit;
	}

	/**
	 * @return the btnKeyboard
	 */
	public CbctlButton getBtnKeyboard() {
		return btnKeyboard;
	}

	/**
	 * @param btnKeyboard the btnKeyboard to set
	 */
	public void setBtnKeyboard(CbctlButton btnKeyboard) {
		this.btnKeyboard = btnKeyboard;
	}

	/**
	 * @return the btnLogout
	 */
	public CbctlButton getBtnLogout() {
		return btnLogout;
	}

	/**
	 * @param btnLogout the btnLogout to set
	 */
	public void setBtnLogout(CbctlButton btnLogout) {
		this.btnLogout = btnLogout;
	}

	/**
	 * @return the btnPrinter
	 */
	public CbctlButton getBtnPrinter() {
		return btnPrinter;
	}

	/**
	 * @param btnPrinter the btnPrinter to set
	 */
	public void setBtnPrinter(CbctlButton btnPrinter) {
		this.btnPrinter = btnPrinter;
	}

	/**
	 * @return the jackpotUIImage
	 */
	public CbctlLabel getJackpotUIImage() {
		return jackpotUIImage;
	}

	/**
	 * @param jackpotUIImage the jackpotUIImage to set
	 */
	public void setJackpotUIImage(CbctlLabel jackpotUIImage) {
		this.jackpotUIImage = jackpotUIImage;
	}

	/**
	 * @return the jackpotUIText
	 */
	public CbctlLabel getJackpotUIText() {
		return jackpotUIText;
	}

	/**
	 * @param jackpotUIText the jackpotUIText to set
	 */
	public void setJackpotUIText(CbctlLabel jackpotUIText) {
		this.jackpotUIText = jackpotUIText;
	}

	/**
	 * @return the lblExit
	 */
	public CbctlLabel getLblExit() {
		return lblExit;
	}

	/**
	 * @param lblExit the lblExit to set
	 */
	public void setLblExit(CbctlLabel lblExit) {
		this.lblExit = lblExit;
	}

	/**
	 * @return the lblKeyboard
	 */
	public CbctlLabel getLblKeyboard() {
		return lblKeyboard;
	}

	/**
	 * @param lblKeyboard the lblKeyboard to set
	 */
	public void setLblKeyboard(CbctlLabel lblKeyboard) {
		this.lblKeyboard = lblKeyboard;
	}

	/**
	 * @return the lblLogout
	 */
	public CbctlLabel getLblLogout() {
		return lblLogout;
	}

	/**
	 * @param lblLogout the lblLogout to set
	 */
	public void setLblLogout(CbctlLabel lblLogout) {
		this.lblLogout = lblLogout;
	}

	/**
	 * @return the lblPrinter
	 */
	public CbctlLabel getLblPrinter() {
		return lblPrinter;
	}

	/**
	 * @param lblPrinter the lblPrinter to set
	 */
	public void setLblPrinter(CbctlLabel lblPrinter) {
		this.lblPrinter = lblPrinter;
	}


} // @jve:decl-index=0:visual-constraint="-110,51"
