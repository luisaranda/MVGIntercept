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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSCombo;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * This class creates the multi area preference composite
 * @author Nithya kalyani R
 * @version $Revision: 7$ 
 */
public class MultiareaPreferenceComposite extends Composite{

	/**
	 * Instance of the multi area id label
	 */
	private SDSTSLabel lblMultiareaId;

	/**
	 *  Instance of the property name label
	 */
	private SDSTSLabel lblPropertyName;

	/**
	 *  Instance of the property id label
	 */
	private SDSTSLabel lblPropertyId;

	/**
	 * Instance of the site name label
	 */
	private SDSTSLabel lblSiteName;

	/**
	 * Instance of the multi area combo
	 */
	private SDSTSCombo<ComboLabelValuePair> comboMultiareaId ;


	/**
	 * Instance of the site name combo
	 */
	private SDSTSCombo<ComboLabelValuePair> comboSiteName ;

	/**
	 * Instance of the property name text
	 */
	private SDSTSText txtPropertyName;

	/**
	 * Instance of the property id text
	 */
	private SDSTSText txtPropertyId;


	/**
	 * Instance of the details composite
	 */
	private Composite detailsComposite;

	/**
	 * Instance of the multi area button
	 */
	//private SDSTSButton btnMultiarea;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;

	/**
	 * Constructor of the class
	 */
	public MultiareaPreferenceComposite(Composite parent, int style){
		super(parent, style);
		initialize();
		this.setBounds(0,0, 800,700);
	}

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

		createDetailsComposite();

		lblLogOutMsg = new SDSTSLabel(this, SWT.NONE,"");				
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);		
		layout();
	}

	private void createDetailsComposite(){		

		GridData gdDetailsComposite = new GridData();
		gdDetailsComposite.grabExcessHorizontalSpace = true;
		gdDetailsComposite.verticalAlignment = GridData.CENTER;
		gdDetailsComposite.grabExcessVerticalSpace = false;
		gdDetailsComposite.horizontalAlignment = GridData.CENTER;
		gdDetailsComposite.widthHint = 475;

		GridLayout grlDetailsComposite = new GridLayout();
		grlDetailsComposite.numColumns = 2;
		grlDetailsComposite.verticalSpacing = 10;
		grlDetailsComposite.horizontalSpacing = 20;

		detailsComposite = new Composite(this, SWT.NONE);
		detailsComposite.setLayoutData(gdDetailsComposite);
		detailsComposite.setLayout(grlDetailsComposite);

		//	btnMultiarea = new SDSTSButton(detailsComposite,SWT.CHECK,IDBPreferenceLabelConstants.LBL_BTN_MULTIAREA,IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_BTN_MULTIAREA, IVoucherConstants.PREFERENCE_MULTIAREA_FRM_BTN_MULTIAREA,true);

		lblSiteName = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_SITENAME_ID));		
		comboSiteName = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_SITENAME, "label", "value", "siteNameList");
		comboSiteName.getRight().setName(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_SITENAME);
		comboSiteName.getLeft().setName(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_SITENAME);

		lblMultiareaId = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MULTIAREA_ID));		
		comboMultiareaId = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_MULTIAREA_ID, "label", "value", "multiareaIdList");
		comboMultiareaId.getRight().setName(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_MULTIAREA_ID);
		comboMultiareaId.getLeft().setName(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_MULTIAREA_ID);

		lblPropertyName = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PROPERTY_NAME));
		txtPropertyName = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_TXT_PROPERTY_NAME,IVoucherConstants.PREFERENCE_MULTIAREA_FRM_TXT_PROPERTY_NAME);
		txtPropertyName.setEditable(false);

		lblPropertyId = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PROPERTY_ID));
		txtPropertyId = new SDSTSText(detailsComposite,SWT.BORDER ,IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_TXT_PROPERTY_ID,IVoucherConstants.PREFERENCE_MULTIAREA_FRM_TXT_PROPERTY_ID,true);
		txtPropertyId.setTextLimit(3);

	}

	/**
	 * This method sets the default grid properties for the controls
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblMultiareaId);
			SDSControlFactory.setTouchScreenLabelProperties(lblSiteName);
			SDSControlFactory.setTouchScreenLabelProperties(lblPropertyName);
			SDSControlFactory.setTouchScreenLabelProperties(lblPropertyId);
			SDSControlFactory.setTouchScreenTextProperties(txtPropertyId);
			SDSControlFactory.setTouchScreenTextProperties(txtPropertyName);
			//SDSControlFactory.setTouchScreenButtonProperties(btnMultiarea);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLblLogOut = (GridData)lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan=2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);

			GridData gdTxtPropertyId = (GridData)txtPropertyId.getLayoutData();
			gdTxtPropertyId.widthHint= ControlConstants.COMBO_TEXT_WIDTH;
			gdTxtPropertyId.horizontalIndent = 5;
			txtPropertyId.setLayoutData(gdTxtPropertyId);

			GridData gdTxtPropertyName = (GridData)txtPropertyName.getLayoutData();
			gdTxtPropertyName.widthHint= ControlConstants.COMBO_TEXT_WIDTH;
			gdTxtPropertyName.horizontalIndent = 5;
			txtPropertyName.setLayoutData(gdTxtPropertyName);

			/*GridData gridDataBtn = (GridData)btnMultiarea.getLayoutData();			
			gridDataBtn.horizontalSpan = 2;
			gridDataBtn.widthHint = ControlConstants.BUTTON_WIDTH;
			btnMultiarea.setLayoutData(gridDataBtn);*/
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void disableControls(){
		lblMultiareaId.setEnabled(false);		
		comboMultiareaId.getText().setEnabled(false);
		comboMultiareaId.getRight().setEnabled(false);
		comboMultiareaId.getLeft().setEnabled(false);
		comboSiteName.getText().setEnabled(false);
		comboSiteName.getRight().setEnabled(false);
		comboSiteName.getLeft().setEnabled(false);
		txtPropertyName.setEnabled(false);		
		lblPropertyId.setEnabled(false);
		lblPropertyName.setEnabled(false);
		txtPropertyId.setEnabled(false);
		lblSiteName.setEnabled(false);
		//btnMultiarea.setEnabled(false);
	}

	/**
	 * @return the comboMultiareaId
	 */
	public SDSTSCombo<ComboLabelValuePair> getComboMultiareaId() {
		return comboMultiareaId;
	}

	/**
	 * @param comboMultiareaId the comboMultiareaId to set
	 */
	public void setComboMultiareaId(SDSTSCombo<ComboLabelValuePair> comboMultiareaId) {
		this.comboMultiareaId = comboMultiareaId;
	}


	/**
	 * @return the txtPropertyId
	 */
	public SDSTSText getTxtPropertyId() {
		return txtPropertyId;
	}

	/**
	 * @return the txtPropertyName
	 */
	public SDSTSText getTxtPropertyName() {
		return txtPropertyName;
	}

	/**
	 * @param txtPropertyName the txtPropertyName to set
	 */
	public void setTxtPropertyName(SDSTSText txtPropertyName) {
		this.txtPropertyName = txtPropertyName;
	}

	/**
	 * @param txtPropertyId the txtPropertyId to set
	 */
	public void setTxtPropertyId(SDSTSText txtPropertyId) {
		this.txtPropertyId = txtPropertyId;
	}

	/**
	 * @return the btnMultiarea
	 *//*
	public SDSTSButton getBtnMultiarea() {
		return btnMultiarea;
	}

	  *//**
	  * @param btnMultiarea the btnMultiarea to set
	  *//*
	public void setBtnMultiarea(SDSTSButton btnMultiarea) {
		this.btnMultiarea = btnMultiarea;
	}*/

	/**
	 * @return the lblLogOutMsg
	 */
	public SDSTSLabel getLblLogOutMsg() {
		return lblLogOutMsg;
	}

	/**
	 * @param lblLogOutMsg the lblLogOutMsg to set
	 */
	public void setLblLogOutMsg(SDSTSLabel lblLogOutMsg) {
		this.lblLogOutMsg = lblLogOutMsg;
	}

	/**
	 * @return the comboSiteName
	 */
	public SDSTSCombo<ComboLabelValuePair> getComboSiteName() {
		return comboSiteName;
	}

	/**
	 * @param comboSiteName the comboSiteName to set
	 */
	public void setComboSiteName(SDSTSCombo<ComboLabelValuePair> comboSiteName) {
		this.comboSiteName = comboSiteName;
	}

}

