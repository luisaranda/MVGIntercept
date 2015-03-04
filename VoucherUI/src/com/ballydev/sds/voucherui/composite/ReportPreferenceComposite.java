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
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class created the reports preference composite
 * @author Nithya kalyani R
 * @version $Revision: 13$ 
 */
public class ReportPreferenceComposite extends Composite {

	/**
	 * Instance of the details composite
	 */
	private Composite detailsComposite;

	/**
	 * Instance of the report settings group 
	 */
	private Group grpReportSettings;

	/**
	 * Instance of the reconciliation grp 
	 */
	private Group grpReconciliationOption;

	/**
	 * Instance of the show label 
	 */
	private SDSTSLabel lblShow;

	/**
	 * Instance of the display all batches btn
	 */
	private SDSTSButton btnDisplayAllBatches;

	/**
	 * Instance of the redeemed label
	 */
	private SDSTSLabel lblRedeemed = null;

	/**
	 *Instance of the created label 
	 */
	private SDSTSLabel lblCreated = null;

	/**
	 *Instance of the voided label 
	 */
	private SDSTSLabel lblVoided = null;

	/**
	 * Instance of the redeemed btn
	 */
	private SDSTSCheckBox radioImgLblRedeemed = null;

	/**
	 *Instance of the voided btn 
	 */
	private SDSTSCheckBox radioImgLblCreated = null;

	/**
	 *Instance of the voided btn 
	 */
	private SDSTSCheckBox radioImgLblVoided = null;

	/**
	 * @param parent
	 * @param style
	 */
	public ReportPreferenceComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.setBounds(0,0, 800,700);
	}

	private void initialize() {
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 0;
		grlPage.marginHeight = 1;
		grlPage.marginWidth = 5;
		grlPage.horizontalSpacing = 5;

		GridData grdPage = new GridData();
		grdPage.horizontalAlignment = GridData.FILL;
		grdPage.grabExcessHorizontalSpace = true;
		grdPage.grabExcessVerticalSpace = true;
		grdPage.verticalAlignment = GridData.FILL;

		this.setLayout(grlPage);
		this.setLayoutData(grdPage);

		createDetailsComposite();
		setDefaultProperties();
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
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_REPORT_TITLE));
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		GridData grdDetailsComp = new GridData();
		grdDetailsComp.grabExcessHorizontalSpace = true;
		grdDetailsComp.verticalAlignment = GridData.CENTER;
		grdDetailsComp.grabExcessVerticalSpace = false;
		grdDetailsComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlDetailsComp = new GridLayout();
		grlDetailsComp.numColumns = 1;
		grlDetailsComp.verticalSpacing = 10;
		grlDetailsComp.horizontalSpacing = 10;

		detailsComposite = new Composite(container, SWT.NONE);
		detailsComposite.setLayoutData(grdDetailsComp);
		detailsComposite.setLayout(grlDetailsComp);
		detailsComposite.setBackground(SDSControlFactory.getTSBodyColor());
		createReportSettingGroup(detailsComposite);
		//createReconciliationOptionGroup(detailsComposite);
	}

	private void createReportSettingGroup(Composite detailsComposite){

		Composite container = new Composite(detailsComposite, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = false;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 769;
		}
		
		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 2;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);
		
		Composite header = new Composite(container, SWT.NONE);
		
		GridData headerData = new GridData();
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 865;
		} else 
			headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE | SWT.COLOR_WHITE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_CASHIER_REPORT_SETTINGS));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = false;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		if(Util.isSmallerResolution()) {
			lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		}else {
			lblMessage.setFont(SDSControlFactory.getDefaultBoldFont());
		}
		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		Composite readOnlyComp = new Composite(container, SWT.NONE);

		GridData gdReadOnly = new GridData();
		gdReadOnly.grabExcessHorizontalSpace = false;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 769;
		}
		gdReadOnly.horizontalAlignment = GridData.BEGINNING;
		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns 		  = 4;
		grlReadOnly.verticalSpacing   = 3;
		grlReadOnly.horizontalSpacing = 50;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.CENTER;
		gdRadio.horizontalIndent = 38;
		if( Util.isSmallerResolution() ) {
			gdRadio.horizontalIndent = 0;
		}
		gdRadio.grabExcessHorizontalSpace = true;


		lblShow = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_SHOW));
//		lblShow.setLayoutData(gridDataLBL);

		radioImgLblRedeemed = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "", LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_REDEEMED), false);
		radioImgLblRedeemed.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		radioImgLblRedeemed.setName(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_REDEEMED);
		radioImgLblRedeemed.setLayoutData(gdRadio);
//		radioButtonControl.add(radioImgLblRedeemed);

		radioImgLblCreated = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "", LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CREATED), false);
		radioImgLblCreated.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		radioImgLblCreated.setName(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_CREATED);
		radioImgLblCreated.setLayoutData(gdRadio);
//		radioButtonControl.add(radioImgLblCreated);
		
		radioImgLblVoided = new SDSTSCheckBox(readOnlyComp,SWT.NONE,"",LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_VOIDED), false);
		radioImgLblVoided.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		radioImgLblVoided.setName(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_VOIDED);
		radioImgLblVoided.setLayoutData(gdRadio);
//		radioButtonControl.add(radioImgLblVoided);
		
		lblRedeemed = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_REDEEMED));
		lblCreated  = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CREATED));
		lblVoided   = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_VOIDED));

	}

	@SuppressWarnings("unused")
	private void createReconciliationOptionGroup(Composite detailsCompoiste)
	{
		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.CENTER;
		gridDataBtn.heightHint = SDSControlFactory.getStandardButtonHeight();
		gridDataBtn.widthHint = SDSControlFactory.getStandardButtonWidth()+70;
		gridDataBtn.grabExcessHorizontalSpace = true;

		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessVerticalSpace = false;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.widthHint = 475;
		gridData1.heightHint = 100;

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 10;
		gridLayout1.horizontalSpacing = 10;

		grpReconciliationOption = new Group(detailsComposite, SWT.NONE);
		grpReconciliationOption.setLayoutData(gridData1);
		grpReconciliationOption.setLayout(gridLayout1);
		grpReconciliationOption.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_CASHIER_RECONCILIATION_SHOW_BATCH_DETAILS));
		grpReconciliationOption.setFont(SDSControlFactory.getDefaultNormalFont());

		btnDisplayAllBatches = new SDSTSButton(grpReconciliationOption,SWT.CHECK,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_DISPLAY_ALL_BATCHES),IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_DISPLAY_ALL_BATCHES, IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_DISPLAY_ALL_BATCHES,true);
		btnDisplayAllBatches.setLayoutData(gridDataBtn);
	}

	private void setDefaultProperties() {
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblCreated);
			SDSControlFactory.setTouchScreenLabelProperties(lblRedeemed);
			SDSControlFactory.setTouchScreenLabelProperties(lblShow);
			SDSControlFactory.setTouchScreenLabelProperties(lblVoided);
//			SDSControlFactory.setGroupHeadingProperties(grpReportSettings);

			GridData gdLblShow = (GridData)lblShow.getLayoutData();
			gdLblShow.verticalSpan=2;
			lblShow.setLayoutData(gdLblShow);

			GridData gdLblRedeemed = (GridData)lblRedeemed.getLayoutData();
			gdLblRedeemed.horizontalIndent = 42;
			if( Util.isSmallerResolution() ) {
				gdLblRedeemed.horizontalIndent = 30;
			}
			lblRedeemed.setLayoutData(gdLblRedeemed);

			GridData gdLblCreated = (GridData)lblCreated.getLayoutData();
			gdLblCreated.horizontalIndent = 38;
			if( Util.isSmallerResolution() ) {
				gdLblCreated.horizontalIndent = 25;
			}
			lblCreated.setLayoutData(gdLblCreated);

			GridData gdLblVoided = (GridData)lblVoided.getLayoutData();
			gdLblVoided.horizontalIndent = 40;
			if( Util.isSmallerResolution() ) {
				gdLblVoided.horizontalIndent = 22;
			}
			lblVoided.setLayoutData(gdLblVoided);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the detailsComposite
	 */
	public Composite getDetailsComposite() {
		return detailsComposite;
	}

	/**
	 * @param detailsComposite the detailsComposite to set
	 */
	public void setDetailsComposite(Composite detailsComposite) {
		this.detailsComposite = detailsComposite;
	}

	/**
	 * @return the grpReportSettings
	 */
	public Group getGrpReportSettings() {
		return grpReportSettings;
	}

	/**
	 * @param grpReportSettings the grpReportSettings to set
	 */
	public void setGrpReportSettings(Group grpReportSettings) {
		this.grpReportSettings = grpReportSettings;
	}

	/**
	 * @return the grpReconciliationOption
	 */
	public Group getGrpReconciliationOption() {
		return grpReconciliationOption;
	}

	/**
	 * @param grpReconciliationOption the grpReconciliationOption to set
	 */
	public void setGrpReconciliationOption(Group grpReconciliationOption) {
		this.grpReconciliationOption = grpReconciliationOption;
	}

	/**
	 * @return the btnDisplayAllBatches
	 */
	public SDSTSButton getBtnDisplayAllBatches() {
		return btnDisplayAllBatches;
	}

	/**
	 * @param btnDisplayAllBatches the btnDisplayAllBatches to set
	 */
	public void setBtnDisplayAllBatches(SDSTSButton btnDisplayAllBatches) {
		this.btnDisplayAllBatches = btnDisplayAllBatches;
	}

	/**
	 * @return the radioImgLblRedeemed
	 */
	public SDSTSCheckBox getRadioImgLblRedeemed() {
		return radioImgLblRedeemed;
	}

	/**
	 * @param radioImgLblRedeemed the radioImgLblRedeemed to set
	 */
	public void setRadioImgLblRedeemed(SDSTSCheckBox radioImgLblRedeemed) {
		this.radioImgLblRedeemed = radioImgLblRedeemed;
	}

	/**
	 * @return the radioImgLblCreated
	 */
	public SDSTSCheckBox getRadioImgLblCreated() {
		return radioImgLblCreated;
	}

	/**
	 * @param radioImgLblCreated the radioImgLblCreated to set
	 */
	public void setRadioImgLblCreated(SDSTSCheckBox radioImgLblCreated) {
		this.radioImgLblCreated = radioImgLblCreated;
	}

	/**
	 * @return the radioImgLblVoided
	 */
	public SDSTSCheckBox getRadioImgLblVoided() {
		return radioImgLblVoided;
	}

	/**
	 * @param radioImgLblVoided the radioImgLblVoided to set
	 */
	public void setRadioImgLblVoided(SDSTSCheckBox radioImgLblVoided) {
		this.radioImgLblVoided = radioImgLblVoided;
	}
}
