/*****************************************************************************
 * $Id: TopHeaderComposite.java,v 1.2, 2010-02-16 08:13:08Z, Anbuselvi, Balasubramanian$
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
 * The header composite of Top Composite
 * 
 * @author dambereen
 * @version $Revision: 3$
 */
public class TopHeaderComposite extends Composite {
	/**
	 * Pending Button Instance
	 */
	public CbctlButton btnPending = null;

	/**
	 * Manual Button Instance
	 */
	public CbctlButton btnManual = null;

	/**
	 * Void Button Instance
	 */
	public CbctlButton btnVoid = null;

	/**
	 * Reprint Button Instance
	 */
	public CbctlButton btnReprint = null;

	/**
	 * Display Button Instance
	 */
	public CbctlButton btnDisplay = null;

	/**
	 * Pending Label Instance
	 */
	public CbctlLabel lblPending = null;

	/**
	 * Manual Label Instance
	 */
	public CbctlLabel lblManual = null;

	/**
	 * Void Label Instance
	 */
	public CbctlLabel lblVoid = null;

	/**
	 * Reprint Label Instance
	 */
	public CbctlLabel lblReprint = null;

	/**
	 * Display Label Instance
	 */
	public CbctlLabel lblDisplay = null;
	
	private String imgPendingInactBtn;
	
	private String imgManualInactiveBtn;
	
	private String imgVoidInactiveBtn;
	
	private String imgReprintInactiveBtn;
	
	private String imgDisplayInactiveBtn;

	/**
	 * Class Constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public TopHeaderComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		if (Util.isSmallerResolution()) {
			imgPendingInactBtn = ImageConstants.S_PENDING_INACTIVE_BUTTON_IMG;
			imgManualInactiveBtn = ImageConstants.S_MANUAL_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.S_VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.S_REPRINT_INACTIVE_BUTTON_IMG;
			imgDisplayInactiveBtn = ImageConstants.S_DISPLAY_INACTIVE_BUTTON_IMG;
		} else {
			imgPendingInactBtn = ImageConstants.PENDING_INACTIVE_BUTTON_IMG;
			imgManualInactiveBtn = ImageConstants.MANUAL_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.REPRINT_INACTIVE_BUTTON_IMG;
			imgDisplayInactiveBtn = ImageConstants.DISPLAY_INACTIVE_BUTTON_IMG;
		}
		setSize(new Point(800, 100));
		layout();
	}

	/**
	 * initialize method
	 */
	private void initialize() {

		GridData gridData35 = new GridData();
		gridData35.horizontalAlignment = GridData.CENTER;
		gridData35.grabExcessVerticalSpace = true;
		gridData35.verticalAlignment = GridData.BEGINNING;

		GridData gridData34 = new GridData();
		gridData34.horizontalAlignment = GridData.CENTER;
		gridData34.grabExcessVerticalSpace = true;
		gridData34.verticalAlignment = GridData.BEGINNING;

		GridData gridData33 = new GridData();
		gridData33.horizontalAlignment = GridData.CENTER;
		gridData33.grabExcessVerticalSpace = true;
		gridData33.verticalAlignment = GridData.BEGINNING;

		GridData gridData32 = new GridData();
		gridData32.horizontalAlignment = GridData.CENTER;
		gridData32.grabExcessVerticalSpace = true;
		gridData32.verticalAlignment = GridData.BEGINNING;

		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = GridData.CENTER;
		gridData31.grabExcessVerticalSpace = true;
		gridData31.verticalAlignment = GridData.BEGINNING;

		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = false;
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.verticalAlignment = GridData.BEGINNING;
		// gridData21.verticalIndent=100;

		GridData gridData5 = new GridData();
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalAlignment = GridData.CENTER;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.heightHint = 75;
		gridData5.widthHint = 75;
		gridData5.grabExcessHorizontalSpace = true;
		GridData gridData4 = new GridData();
		gridData4.grabExcessVerticalSpace = true;
		gridData4.horizontalAlignment = GridData.CENTER;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.heightHint = 75;
		gridData4.widthHint = 75;
		gridData4.grabExcessHorizontalSpace = true;
		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.verticalAlignment = GridData.CENTER;
		gridData3.heightHint = 75;
		gridData3.widthHint = 75;
		gridData3.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.heightHint = 75;
		gridData2.widthHint = 75;
		gridData2.grabExcessHorizontalSpace = true;
		GridData gridData1 = new GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.heightHint = 75;
		gridData1.widthHint = 75;
		gridData1.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 50;
		gridLayout.marginWidth = 20;
		gridLayout.marginHeight = 20;
		gridLayout.numColumns = 5;
		gridLayout.verticalSpacing = 0;

		btnPending = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.PENDING_JACKPOT_BUTTON);
		btnPending
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnPending.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgPendingInactBtn)));
		btnPending.setLayoutData(gridData1);

		btnManual = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.MANUAL_JACKPOT_BUTTON);
		btnManual
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnManual.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgManualInactiveBtn)));
		btnManual.setLayoutData(gridData2);

		btnVoid = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.VOID_BUTTON);
		btnVoid.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnVoid.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgVoidInactiveBtn)));
		btnVoid.setLayoutData(gridData3);

		btnReprint = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.REPRINT_BUTTON);
		btnReprint
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnReprint.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgReprintInactiveBtn)));
		btnReprint.setLayoutData(gridData4);

		btnDisplay = new CbctlButton(this, SWT.NONE, "",
				LabelKeyConstants.DISPLAY_BUTTON);
		btnDisplay
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnDisplay.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisplayInactiveBtn)));
		btnDisplay.setLayoutData(gridData5);

		lblPending = new CbctlLabel(this, SWT.NONE);
		lblPending.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.PENDING_JACKPOT_BUTTON));
		lblPending
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblPending
				.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblPending.setLayoutData(gridData31);

		lblManual = new CbctlLabel(this, SWT.NONE);
		lblManual.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.MANUAL_JACKPOT_BUTTON));
		lblManual
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblManual.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblManual.setLayoutData(gridData32);

		lblVoid = new CbctlLabel(this, SWT.NONE);
		lblVoid.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.VOID_BUTTON));
		lblVoid.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblVoid.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblVoid.setLayoutData(gridData33);

		lblReprint = new CbctlLabel(this, SWT.NONE);
		lblReprint.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.REPRINT_BUTTON));
		lblReprint
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblReprint
				.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblReprint.setLayoutData(gridData34);

		lblDisplay = new CbctlLabel(this, SWT.NONE);
		lblDisplay.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.DISPLAY_BUTTON));
		lblDisplay
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblDisplay
				.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblDisplay.setLayoutData(gridData35);

		this.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		this.setLayoutData(gridData21);
		this.setLayout(gridLayout);
		this.layout();
	}

	/**
	 * @return the btnDisplay
	 */
	public CbctlButton getBtnDisplay() {
		return btnDisplay;
	}

	/**
	 * @param btnDisplay
	 *            the btnDisplay to set
	 */
	public void setBtnDisplay(CbctlButton btnDisplay) {
		this.btnDisplay = btnDisplay;
	}

	/**
	 * @return the btnManual
	 */
	public CbctlButton getBtnManual() {
		return btnManual;
	}

	/**
	 * @param btnManual
	 *            the btnManual to set
	 */
	public void setBtnManual(CbctlButton btnManual) {
		this.btnManual = btnManual;
	}

	/**
	 * @return the btnPending
	 */
	public CbctlButton getBtnPending() {
		return btnPending;
	}

	/**
	 * @param btnPending
	 *            the btnPending to set
	 */
	public void setBtnPending(CbctlButton btnPending) {
		this.btnPending = btnPending;
	}

	/**
	 * @return the btnReprint
	 */
	public CbctlButton getBtnReprint() {
		return btnReprint;
	}

	/**
	 * @param btnReprint
	 *            the btnReprint to set
	 */
	public void setBtnReprint(CbctlButton btnReprint) {
		this.btnReprint = btnReprint;
	}

	/**
	 * @return the btnVoid
	 */
	public CbctlButton getBtnVoid() {
		return btnVoid;
	}

	/**
	 * @param btnVoid
	 *            the btnVoid to set
	 */
	public void setBtnVoid(CbctlButton btnVoid) {
		this.btnVoid = btnVoid;
	}

	/**
	 * @return the lblDisplay
	 */
	public CbctlLabel getLblDisplay() {
		return lblDisplay;
	}

	/**
	 * @param lblDisplay
	 *            the lblDisplay to set
	 */
	public void setLblDisplay(CbctlLabel lblDisplay) {
		this.lblDisplay = lblDisplay;
	}

	/**
	 * @return the lblManual
	 */
	public CbctlLabel getLblManual() {
		return lblManual;
	}

	/**
	 * @param lblManual
	 *            the lblManual to set
	 */
	public void setLblManual(CbctlLabel lblManual) {
		this.lblManual = lblManual;
	}

	/**
	 * @return the lblPending
	 */
	public CbctlLabel getLblPending() {
		return lblPending;
	}

	/**
	 * @param lblPending
	 *            the lblPending to set
	 */
	public void setLblPending(CbctlLabel lblPending) {
		this.lblPending = lblPending;
	}

	/**
	 * @return the lblReprint
	 */
	public CbctlLabel getLblReprint() {
		return lblReprint;
	}

	/**
	 * @param lblReprint
	 *            the lblReprint to set
	 */
	public void setLblReprint(CbctlLabel lblReprint) {
		this.lblReprint = lblReprint;
	}

	/**
	 * @return the lblVoid
	 */
	public CbctlLabel getLblVoid() {
		return lblVoid;
	}

	/**
	 * @param lblVoid
	 *            the lblVoid to set
	 */
	public void setLblVoid(CbctlLabel lblVoid) {
		this.lblVoid = lblVoid;
	}
} // @jve:decl-index=0:visual-constraint="4,5"
