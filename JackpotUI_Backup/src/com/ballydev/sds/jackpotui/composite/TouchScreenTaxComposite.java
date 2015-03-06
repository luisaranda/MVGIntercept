package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenMiddleComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.constants.FormConstants;



/**
 * 
 * TouchScreenShiftComposite - Draws the shift composite
 * Day, Swing, Graveyard	
 * 
 * @author ksuganthi
 * 
 */

public class TouchScreenTaxComposite extends Composite {

	
	private SDSTSCheckBox stateTaxCheckBox = null;
	
	private SDSTSCheckBox federalTaxCheckBox = null;
	
	private SDSTSCheckBox municipalTaxCheckBox = null;
		
	/**
	 * MVG Intercept Enhancement 
	 */
	private SDSTSCheckBox interceptCheckBox = null;
	
	private SDSTSLabel lblIntercept = null;
	
	private SDSTSText txtInterceptAmount = null;

	/**
	 * State Tax label instance
	 */
	private SDSTSLabel lblStateTax = null;
	
	/**
	 * Municipal Tax label instance
	 */
	private SDSTSLabel lblMunicipalTax = null;

	/**
	 * Federal Tax label instance
	 */
	private SDSTSLabel lblFederalTax = null;
	
	int SMALL_RESOLUTION_BTN_WIDTH = 42;
	
	private boolean enableStateTax = true;
	
	private boolean enableFederalTax = true;
	
	private boolean enableMunicipalTax = true;
	
	private TouchScreenMiddleComposite taxComposite;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;
	
	
	private Composite readOnlyComp ;
	
	public TouchScreenTaxComposite(TouchScreenMiddleComposite middleComposite, int style){
		super(middleComposite, style);
		this.taxComposite = middleComposite;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		GridData gdTaxComp = new GridData();
		gdTaxComp.grabExcessVerticalSpace = false;
		gdTaxComp.verticalAlignment = GridData.FILL;
		gdTaxComp.horizontalSpan = 2;
		gdTaxComp.widthHint = middleBodyWidth;
		gdTaxComp.heightHint = middleBodyHeight;
		gdTaxComp.horizontalAlignment = GridData.BEGINNING;
			
		GridLayout glTaxPComp = new GridLayout();
		glTaxPComp.numColumns = 2;
		glTaxPComp.verticalSpacing = 5;
		glTaxPComp.marginWidth = 5;
		glTaxPComp.marginHeight = 5;
		glTaxPComp.horizontalSpacing = 5;
		
		initialize();
		
		this.setBackground(new Color(Display.getCurrent(), 216, 234, 243));
		this.setLayout(glTaxPComp);
		this.setLayoutData(gdTaxComp);
	}

	private void initialize(){
		
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ILabelConstants.STATE_TAX_RATE_FOR_JACKPOT))==0)
		{
			enableStateTax = false;
		}
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ILabelConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT))==0)
		{
			enableFederalTax = false;
		}
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT))==0)
		{
			enableMunicipalTax = false;
		}
		
		
		GridData gdCheck = new GridData();
		gdCheck.verticalAlignment = GridData.BEGINNING;
		gdCheck.horizontalIndent = 10;
		
		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = false;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 400;
		}
		
		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 2;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);
		
		readOnlyComp = new Composite(container, SWT.NONE);

		GridData gdReadOnly = new GridData();
		gdReadOnly.grabExcessHorizontalSpace = false;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		gdReadOnly.horizontalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 400;
		}
		gdReadOnly.horizontalAlignment = GridData.BEGINNING;
		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns 		  = 3;
		grlReadOnly.verticalSpacing   = 7;
		grlReadOnly.horizontalSpacing = 15;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		GridData gdRadio1 = new GridData();
		gdRadio1.horizontalAlignment = GridData.CENTER;
		gdRadio1.horizontalIndent = 16;
		if( Util.isSmallerResolution() ) {
			gdRadio1.horizontalIndent = 0;
		}
		gdRadio1.grabExcessHorizontalSpace = false;
		gdRadio1.widthHint = 50 ;
		gdRadio1.heightHint = 50 ;
		
		
		GridData gdRadio2 = new GridData();
		gdRadio2.horizontalSpan = 2;
		gdRadio2.horizontalAlignment = GridData.VERTICAL_ALIGN_END;
		gdRadio2.horizontalIndent = 16;
		if( Util.isSmallerResolution() ) {
			gdRadio2.horizontalIndent = 0;
		}
		gdRadio2.grabExcessHorizontalSpace = false;
		gdRadio2.widthHint = 100 ;
		gdRadio2.heightHint = 25 ;
		
		
		if(enableStateTax){
			stateTaxCheckBox = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "" , "stateTax" , false);
			stateTaxCheckBox.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			stateTaxCheckBox.setLayoutData(gdRadio1);
			lblStateTax = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.STATE_TAX_LABEL));
			lblStateTax.setLayoutData(gdRadio2);
		
		}
		
		if(enableFederalTax){
			federalTaxCheckBox = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "" , "federalTax" , false);
			federalTaxCheckBox.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			federalTaxCheckBox.setLayoutData(gdRadio1);
			lblFederalTax  = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.FEDERAL_TAX_LABEL));
			lblFederalTax.setLayoutData(gdRadio2);
		
		}
		
		if(enableMunicipalTax){
			municipalTaxCheckBox = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "" , "municipalTax", false);
			municipalTaxCheckBox.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			municipalTaxCheckBox.setLayoutData(gdRadio1);
			lblMunicipalTax   = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.MUNICIPAL_TAX_LABEL));
			lblMunicipalTax.setLayoutData(gdRadio2);	
			
		}
		
		/*Custom Fields for MVG*/
		interceptCheckBox = new SDSTSCheckBox(readOnlyComp, SWT.NONE, "" , "intercept", false);
		interceptCheckBox.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		interceptCheckBox.setLayoutData(gdRadio1);
		
		GridData gdRadio3 = new GridData();
		gdRadio3.horizontalAlignment = GridData.VERTICAL_ALIGN_END;
		gdRadio3.horizontalIndent = 16;
		if( Util.isSmallerResolution() ) {
			gdRadio3.horizontalIndent = 0;
		}
		gdRadio3.grabExcessHorizontalSpace = false;
		gdRadio3.widthHint = 100 ;
		gdRadio3.heightHint = 25 ;
			
		lblIntercept  = new SDSTSLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue("Intercept"));
		lblIntercept.setLayoutData(gdRadio3);
		txtInterceptAmount = new SDSTSText(readOnlyComp, SWT.NONE, "", FormConstants.FORM_INTERCEPT_AMOUNT, true );
		txtInterceptAmount.setLayoutData(gdRadio3);
		txtInterceptAmount.setEditable(false);
		txtInterceptAmount.setEditable(false);
	}
	
	public SDSTSCheckBox getInterceptCheckBox(){
		return interceptCheckBox;
	}
	
	public void setInterceptCheckBox(SDSTSCheckBox interceptCheckBox) {
		this.interceptCheckBox = interceptCheckBox;
	}
	
	public SDSTSLabel getLblIntercept() {
		return lblIntercept;
	}
	
	public void setLblIntercept(SDSTSLabel lblIntercept) {
		this.lblIntercept = lblIntercept;
	}
	
	public SDSTSText getTxtInterceptAmount() {
		return txtInterceptAmount;
	}

	public void setTxtInterceptAmount(SDSTSText txtInterceptAmount) {
		this.txtInterceptAmount = txtInterceptAmount;
	}
	/*End Custom Fields*/
	
	public SDSTSCheckBox getStateTaxCheckBox() {
		return stateTaxCheckBox;
	}

	public void setStateTaxCheckBox(SDSTSCheckBox stateTaxCheckBox) {
		this.stateTaxCheckBox = stateTaxCheckBox;
	}

	public SDSTSCheckBox getFederalTaxCheckBox() {
		return federalTaxCheckBox;
	}

	public void setFederalTaxCheckBox(SDSTSCheckBox federalTaxCheckBox) {
		this.federalTaxCheckBox = federalTaxCheckBox;
	}

	public SDSTSCheckBox getMunicipalTaxCheckBox() {
		return municipalTaxCheckBox;
	}

	public void setMunicipalTaxCheckBox(SDSTSCheckBox municipalTaxCheckBox) {
		this.municipalTaxCheckBox = municipalTaxCheckBox;
	}

	public SDSTSLabel getLblStateTax() {
		return lblStateTax;
	}

	public void setLblStateTax(SDSTSLabel lblStateTax) {
		this.lblStateTax = lblStateTax;
	}

	public SDSTSLabel getLblMunicipalTax() {
		return lblMunicipalTax;
	}

	public void setLblMunicipalTax(SDSTSLabel lblMunicipalTax) {
		this.lblMunicipalTax = lblMunicipalTax;
	}

	public SDSTSLabel getLblFederalTax() {
		return lblFederalTax;
	}

	public void setLblFederalTax(SDSTSLabel lblFederalTax) {
		this.lblFederalTax = lblFederalTax;
	}

	public int getSMALL_RESOLUTION_BTN_WIDTH() {
		return SMALL_RESOLUTION_BTN_WIDTH;
	}

	public void setSMALL_RESOLUTION_BTN_WIDTH(int small_resolution_btn_width) {
		SMALL_RESOLUTION_BTN_WIDTH = small_resolution_btn_width;
	}

	public boolean isEnableStateTax() {
		return enableStateTax;
	}

	public void setEnableStateTax(boolean enableStateTax) {
		this.enableStateTax = enableStateTax;
	}

	public boolean isEnableFederalTax() {
		return enableFederalTax;
	}

	public void setEnableFederalTax(boolean enableFederalTax) {
		this.enableFederalTax = enableFederalTax;
	}

	public boolean isEnableMunicipalTax() {
		return enableMunicipalTax;
	}

	public void setEnableMunicipalTax(boolean enableMunicipalTax) {
		this.enableMunicipalTax = enableMunicipalTax;
	}

	public TouchScreenMiddleComposite getTaxComposite() {
		return taxComposite;
	}

	public void setTaxComposite(TouchScreenMiddleComposite taxComposite) {
		this.taxComposite = taxComposite;
	}

	public int getMiddleBodyWidth() {
		return middleBodyWidth;
	}

	public void setMiddleBodyWidth(int middleBodyWidth) {
		this.middleBodyWidth = middleBodyWidth;
	}

	public int getMiddleBodyHeight() {
		return middleBodyHeight;
	}

	public void setMiddleBodyHeight(int middleBodyHeight) {
		this.middleBodyHeight = middleBodyHeight;
	}

	public Composite getReadOnlyComp() {
		return readOnlyComp;
	}

	public void setReadOnlyComp(Composite readOnlyComp) {
		this.readOnlyComp = readOnlyComp;
	}
}


