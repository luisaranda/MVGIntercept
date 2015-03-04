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

package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This method creates the location preference composite
 * 
 * @author Nithya kalyani R
 * @version $Revision: 25$
 */
public class LocationPreferenceComposite extends Composite {

	/**
	 * Instance of the location id label
	 */
	private SDSTSLabel lblLocationId;

	/**
	 * Instance of the location id text
	 */
	private SDSTSText txtLocationId;

	/**
	 * Instance of the details composite
	 */
	private Composite detailsComposite;

	/**
	 * Instance of the button group
	 */
	private Group grpBtnGroup;

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;

	/**
	 * Instance of the cashier touch screen button
	 */
	private TSButtonLabel radioImgLblCashier;

	/**
	 * Instance of the touch screen auditor button
	 */
	private TSButtonLabel radioImgLblAuditor;

	/**
	 * Instance of the toggle button composite
	 */
	private Composite toggleBtnComposite = null;

	/**
	 * Instance of the cashier label
	 */
	private SDSTSLabel lblCashier = null;

	/**
	 * Instance of the auditor label
	 */
	private SDSTSLabel lblAuditor = null;

	/**
	 * Instance to check whether the cashier button is selected or not
	 */
	private boolean isCashier = true;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;
	
	private Image noteImage;

	Composite noteComposite = null; 
	
	/**
	 * Constructor of the class
	 */
	public LocationPreferenceComposite(Composite parent, int style) {
		super(parent, style);
		noteImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
		initialize();
	}

	/**
	 * This method initializes the page grid properties
	 */
	private void initialize() {
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 4;
		grlPage.marginHeight = 1;
		grlPage.marginWidth = 5;
		grlPage.horizontalSpacing = 5;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.verticalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;

		createDetailsComposite();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		layout();
	}

	private void createDetailsComposite() {

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData containerData = new GridData();
		containerData.widthHint = 655;
		
		if(Util.isSmallerResolution())
			containerData.heightHint = 530;
		else 
			containerData.heightHint = 498;
		
		containerData.horizontalIndent = 0;
		containerData.verticalIndent = 0;
		container.setLayoutData(containerData);
		
		GridLayout containerLayout = new GridLayout(2, true);
		containerLayout.marginTop = 0;
		containerLayout.marginLeft = 0;
		containerLayout.marginRight = 0;
		containerLayout.marginBottom = 0;
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth = 0;
		container.setLayout(containerLayout);
		
		Composite header = new Composite(container, SWT.NONE);
		GridData headerData = new GridData();
		headerData.widthHint = 653;
		if(Util.isSmallerResolution())
			headerData.heightHint = 30;
		else 
			headerData.heightHint = 30;
		headerData.horizontalIndent = 1;
		headerData.horizontalSpan = 2;
		headerData.verticalIndent = 1;
		headerData.horizontalAlignment = GridData.BEGINNING;
		headerData.verticalAlignment = GridData.BEGINNING;
		headerData.grabExcessHorizontalSpace = false;
		headerData.grabExcessVerticalSpace = false;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getButtonForegroundColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_LOCATION_TITLE));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessVerticalSpace = false;
		gridData1.horizontalAlignment = GridData.CENTER;
		if (Util.isSmallerResolution()) {
			gridData1.widthHint = 250;
		}

		GridLayout grlDetailsComposite = new GridLayout();
		grlDetailsComposite.numColumns = 3;
		grlDetailsComposite.verticalSpacing = 10;
		grlDetailsComposite.horizontalSpacing = 10;

		detailsComposite = new Composite(container, SWT.NONE);
		detailsComposite.setBackground(SDSControlFactory.getTSBodyColor());
		detailsComposite.setLayoutData(gridData1);
		detailsComposite.setLayout(grlDetailsComposite);

		createBtnGroup(detailsComposite);

		lblLocationId = new SDSTSLabel(detailsComposite, SWT.NONE, 
				LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_LOCATION_ID) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtLocationId = new SDSTSText(detailsComposite, SWT.BORDER,
				IVoucherConstants.PREFERENCE_LOCATION_CTRL_TXT_LOCATION_ID,
				IVoucherConstants.PREFERENCE_LOCATION_FRM_TXT_LOCATION_ID);
		txtLocationId.setTextLimit(10);
		
		noteComposite = new Composite(container, SWT.NONE);
		noteComposite.setBackground(SDSControlFactory.getTSBodyColor());
		GridData gdData = new GridData();
		gdData.horizontalAlignment = GridData.BEGINNING;
		gdData.horizontalSpan = 3;
		gdData.horizontalIndent = 30;
		
		GridData lblGridData = new GridData();
		lblGridData.horizontalAlignment = GridData.CENTER;
		lblGridData.verticalAlignment = GridData.CENTER;
		
		if(Util.isSmallerResolution()){
			gdData.widthHint = 588;
			gdData.heightHint = 37;
			lblGridData.verticalIndent = 2;
		}else{
			gdData.widthHint = noteImage.getBounds().width;
			gdData.heightHint = noteImage.getBounds().height;
			lblGridData.verticalIndent = 5;
		}
		
		GridLayout glLayout = new GridLayout();
		glLayout.marginWidth = 5;
		
		noteComposite.setLayoutData(gdData);
		noteComposite.setLayout(glLayout);
		
		lblGridData.widthHint = noteImage.getBounds().width; 
		
		lblLogOutMsg = new SDSTSLabel(noteComposite, SWT.CENTER, "");
		lblLogOutMsg.setLayoutData(lblGridData);
		
//		SDSTSLabel blLogOutMsg = new SDSTSLabel(this, SWT.NONE, "");
	}

	private void createBtnGroup(Composite detailsComposite) {
		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.CENTER;
		gridDataBtn.heightHint = SDSControlFactory.getStandardButtonHeight();
		gridDataBtn.widthHint = SDSControlFactory.getStandardButtonWidth();
		gridDataBtn.grabExcessHorizontalSpace = true;

		GridData gdBtnGroup = new GridData();
		gdBtnGroup.grabExcessHorizontalSpace = true;
		gdBtnGroup.verticalAlignment = GridData.CENTER;
		gdBtnGroup.grabExcessVerticalSpace = false;
		gdBtnGroup.heightHint = 100;
		if( Util.isSmallerResolution() )
			gdBtnGroup.widthHint = 180;
		else
			gdBtnGroup.horizontalAlignment = GridData.FILL;
		gdBtnGroup.horizontalSpan = 3;

		grpBtnGroup = new Group(detailsComposite, SWT.NONE);
		grpBtnGroup.setLayoutData(gdBtnGroup);
		grpBtnGroup.setLayout(new GridLayout(2, false));
		grpBtnGroup.setBackground(SDSControlFactory.getTSBodyColor());

		toggleBtnComposite = new Composite(grpBtnGroup, SWT.NONE);
		toggleBtnComposite.setBackground(SDSControlFactory.getTSBodyColor());
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gridLayout3.verticalSpacing = 1;
		gridLayout3.horizontalSpacing = 10;
		gridLayout3.marginHeight = 5;
		gridLayout3.marginWidth = 5;
		gridLayout3.marginLeft = 5;
		toggleBtnComposite.setLayout(gridLayout3);

		GridData toggleCompositeGridData = new GridData();
		toggleCompositeGridData.horizontalAlignment = SWT.CENTER;
		toggleCompositeGridData.grabExcessHorizontalSpace = true;

		toggleBtnComposite.setLayoutData(toggleCompositeGridData);

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.heightHint = 50;
		gdTouchScreenButton.widthHint = 50;
		if (Util.isSmallerResolution()) {
			gdTouchScreenButton.heightHint = 43;
			gdTouchScreenButton.widthHint = 43;
		}
		gdTouchScreenButton.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton.verticalAlignment = GridData.END;
		gdTouchScreenButton.grabExcessHorizontalSpace = true;
		gdTouchScreenButton.grabExcessVerticalSpace = true;

		GridData gdTouchScreenButton1 = new GridData();
		gdTouchScreenButton1.heightHint = 50;
		gdTouchScreenButton1.widthHint = 50;
		if (Util.isSmallerResolution()) {
			gdTouchScreenButton1.heightHint = 43;
			gdTouchScreenButton1.widthHint = 43;
		}
		gdTouchScreenButton1.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton1.verticalAlignment = GridData.END;
		gdTouchScreenButton1.grabExcessHorizontalSpace = true;
		gdTouchScreenButton1.grabExcessVerticalSpace = true;
		gdTouchScreenButton1.horizontalIndent = 40;

		GridData gdTSButtonLabel = new GridData();
		gdTSButtonLabel.grabExcessHorizontalSpace = true;
		gdTSButtonLabel.horizontalAlignment = GridData.BEGINNING;
		
		GridData gdTSButtonLabel1 = new GridData();
		gdTSButtonLabel1.grabExcessHorizontalSpace = true;
		gdTSButtonLabel1.horizontalAlignment = GridData.BEGINNING;
		gdTSButtonLabel1.horizontalIndent = 40;
		
		radioButtonControl = new RadioButtonControl("Search by any one of the following");

		radioImgLblCashier = new TSButtonLabel(toggleBtnComposite, SWT.NONE,IVoucherConstants.PREFERENCE_LOCATION_FRM_BTN_CASHIER);
		radioImgLblCashier.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		radioImgLblCashier.setLayoutData(gdTouchScreenButton);
		radioButtonControl.add(radioImgLblCashier);

		radioImgLblAuditor = new TSButtonLabel(toggleBtnComposite, SWT.NONE,IVoucherConstants.PREFERENCE_LOCATION_FRM_BTN_AUDITOR);
		radioImgLblAuditor.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		radioImgLblAuditor.setLayoutData(gdTouchScreenButton1);
		radioButtonControl.add(radioImgLblAuditor);

		lblCashier = new SDSTSLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CASHIER));
		lblCashier.setLayoutData(gdTSButtonLabel);
		lblAuditor = new SDSTSLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_AUDITOR));
		lblAuditor.setLayoutData(gdTSButtonLabel1);

	}

	/**
	 * This method sets the default grid properties of the control
	 */
	private void setControlProperties() {
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblAuditor);
			SDSControlFactory.setTouchScreenLabelProperties(lblCashier);
			SDSControlFactory.setTouchScreenLabelProperties(lblLocationId);
			SDSControlFactory.setTouchScreenTextProperties(txtLocationId);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLblLogOut = (GridData) lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan = 2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableControls() {
//		lblLocationId.setEnabled(false);
		txtLocationId.setEnabled(false);
		radioImgLblCashier.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
		radioImgLblAuditor.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
//		lblCashier.setEnabled(false);
//		lblAuditor.setEnabled(false);
		noteComposite.setBackgroundImage(noteImage);
	}

	public void setCashier(boolean isCashier) {
		this.isCashier = isCashier;
		if (isCashier) {
			lblCashier.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CASHIER));
		} else {
			lblAuditor.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_AUDITOR));
		}
	}

	public void setAuditorToggleStatus(boolean isCashier) {
		this.isCashier = isCashier;
		// if( isCashier ){
		// radioImgLblCashier.setSelection(true);
		// radioImgLblAuditor.setSelection(false);
		// }else{
		// radioImgLblAuditor.setSelection(true);
		// radioImgLblCashier.setSelection(false);
		// }
	}

	/**
	 * @return the lblLocationId
	 */
	public SDSTSLabel getLblLocationId() {
		return lblLocationId;
	}

	/**
	 * @param lblLocationId
	 *            the lblLocationId to set
	 */
	public void setLblLocationId(SDSTSLabel lblLocationId) {
		this.lblLocationId = lblLocationId;
	}

	/**
	 * @return the txtLocationId
	 */
	public SDSTSText getTxtLocationId() {
		return txtLocationId;
	}

	/**
	 * @param txtLocationId
	 *            the txtLocationId to set
	 */
	public void setTxtLocationId(SDSTSText txtLocationId) {
		this.txtLocationId = txtLocationId;
	}

	/**
	 * @return the detailsComposite
	 */
	public Composite getDetailsComposite() {
		return detailsComposite;
	}

	/**
	 * @param detailsComposite
	 *            the detailsComposite to set
	 */
	public void setDetailsComposite(Composite detailsComposite) {
		this.detailsComposite = detailsComposite;
	}

	/**
	 * @return the grpBtnGroup
	 */
	public Group getGrpBtnGroup() {
		return grpBtnGroup;
	}

	/**
	 * @param grpBtnGroup
	 *            the grpBtnGroup to set
	 */
	public void setGrpBtnGroup(Group grpBtnGroup) {
		this.grpBtnGroup = grpBtnGroup;
	}

	/**
	 * @return the radioImgLblCashier
	 */
	public TSButtonLabel getRadioImgLblCashier() {
		return radioImgLblCashier;
	}

	/**
	 * @return the radioButtonControl
	 */
	public RadioButtonControl getRadioButtonControl() {
		return radioButtonControl;
	}

	/**
	 * @param radioButtonControl the radioButtonControl to set
	 */
	public void setRadioButtonControl(RadioButtonControl radioButtonControl) {
		this.radioButtonControl = radioButtonControl;
	}

	/**
	 * @param radioImgLblCashier
	 *            the radioImgLblCashier to set
	 */
	public void setRadioImgLblCashier(TSButtonLabel radioImgLblCashier) {
		this.radioImgLblCashier = radioImgLblCashier;
	}

	/**
	 * @return the radioImgLblAuditor
	 */
	public TSButtonLabel getRadioImgLblAuditor() {
		return radioImgLblAuditor;
	}

	/**
	 * @param radioImgLblAuditor
	 *            the radioImgLblAuditor to set
	 */
	public void setRadioImgLblAuditor(TSButtonLabel radioImgLblAuditor) {
		this.radioImgLblAuditor = radioImgLblAuditor;
	}

	/**
	 * @return the toggleBtnComposite
	 */
	public Composite getToggleBtnComposite() {
		return toggleBtnComposite;
	}

	/**
	 * @param toggleBtnComposite
	 *            the toggleBtnComposite to set
	 */
	public void setToggleBtnComposite(Composite toggleBtnComposite) {
		this.toggleBtnComposite = toggleBtnComposite;
	}

	/**
	 * @return the lblCashier
	 */
	public SDSTSLabel getLblCashier() {
		return lblCashier;
	}

	/**
	 * @param lblCashier
	 *            the lblCashier to set
	 */
	public void setLblCashier(SDSTSLabel lblCashier) {
		this.lblCashier = lblCashier;
	}

	/**
	 * @return the lblAuditor
	 */
	public SDSTSLabel getLblAuditor() {
		return lblAuditor;
	}

	/**
	 * @param lblAuditor
	 *            the lblAuditor to set
	 */
	public void setLblAuditor(SDSTSLabel lblAuditor) {
		this.lblAuditor = lblAuditor;
	}

	/**
	 * @return the isCashier
	 */
	public boolean isCashier() {
		return isCashier;
	}

	/**
	 * @return the lblLogOutMsg
	 */
	public SDSTSLabel getLblLogOutMsg() {
		return lblLogOutMsg;
	}

	/**
	 * @param lblLogOutMsg
	 *            the lblLogOutMsg to set
	 */
	public void setLblLogOutMsg(SDSTSLabel lblLogOutMsg) {
		this.lblLogOutMsg = lblLogOutMsg;
	}

}
