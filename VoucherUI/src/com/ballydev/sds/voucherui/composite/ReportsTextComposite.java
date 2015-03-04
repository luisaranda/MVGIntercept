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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * @author Nithya kalyani R
 * @version $Revision: 15$ 
 */
public class ReportsTextComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants{

	/**
	 * Instance of the reports display text area
	 */
	private StyledText txtReportsDisplay = null;

	/**
	 * Instance of the Back button
	 */
	private SDSImageLabel btnCancel = null;

	/**
	 * Instance of the text area group
	 */
	private Group grpTextArea = null;

	/**
	 * Instance of the forward button
	 */
	private TSButtonLabel btnForward = null;

	/**
	 * Instance of the backward button
	 */
	private TSButtonLabel btnBackward = null;

	/**
	 * Instance of the forward button
	 */
	private TSButtonLabel btnUp = null;

	/**
	 * Instance of the backward button
	 */
	private TSButtonLabel btnDown = null;

	/**
	 * Instance of the backward button
	 */
	private SDSImageLabel btnPrint = null;

	/**
	 * Instance of the details composite
	 */
	private Composite detailsComposite = null;

	private Image buttonBG;

	/**
	 * Constructor of the class
	 */
	public ReportsTextComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		setSize(parent.getSize());
	}

	private void initialize(){
		createImages();
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 2;
		grlPage.marginHeight = 5;
		grlPage.marginWidth = 5;
		grlPage.horizontalSpacing = 5;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_REPORTS));
		createDetailsComposite();	

		setDefaultProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		layout();
	}

	private void createDetailsComposite(){

		GridLayout glMiddleComp = new GridLayout();
		glMiddleComp.marginWidth = 5;
		glMiddleComp.marginHeight = 0;
		glMiddleComp.numColumns = 4;
		glMiddleComp.verticalSpacing = 2;

		GridData gdMiddleComp = new GridData();
		gdMiddleComp.grabExcessHorizontalSpace = true;
		gdMiddleComp.verticalAlignment = GridData.BEGINNING;
		gdMiddleComp.widthHint  = 950;
		gdMiddleComp.heightHint = 500;
		if( Util.isSmallerResolution() ) {
			gdMiddleComp.widthHint  = 750;
			gdMiddleComp.heightHint = 400;
		}
		gdMiddleComp.grabExcessVerticalSpace = false;
		gdMiddleComp.horizontalAlignment = GridData.CENTER;
		
		Composite middleComp = new Composite(this, SWT.BORDER);
		middleComp.setLayoutData(gdMiddleComp);
		middleComp.setLayout(glMiddleComp);
//		middleComp.setBackground(new Color(Display.getCurrent(), 222,222,222) );
		
		GridData gdTextAreaComp = new GridData();
		gdTextAreaComp.grabExcessHorizontalSpace = true;
		gdTextAreaComp.grabExcessVerticalSpace = true;
		gdTextAreaComp.horizontalSpan = 3;
		gdTextAreaComp.verticalAlignment = GridData.CENTER;
		gdTextAreaComp.heightHint = 350;
		gdTextAreaComp.widthHint = 750;
		if( Util.isSmallerResolution() ) {
			gdTextAreaComp.verticalIndent = 10;
		} else {
			gdTextAreaComp.horizontalAlignment = GridData.FILL;
		}
		GridLayout glTextAreaComp = new GridLayout();
		glTextAreaComp.numColumns = 4;
		glTextAreaComp.verticalSpacing   = 2;
		glTextAreaComp.horizontalSpacing = 5;
		
		Composite reportsTextAreaComposite = new Composite(middleComp,SWT.NONE);
		reportsTextAreaComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		reportsTextAreaComposite.setLayout(glTextAreaComp);
		reportsTextAreaComposite.setLayoutData(gdTextAreaComp);
		
		GridData gdTxtArea = new GridData();
		gdTxtArea.widthHint  = 500;
		gdTxtArea.heightHint = 250;
		if( Util.isSmallerResolution() ) {
			gdTxtArea.widthHint  = 350;
		}
		gdTxtArea.horizontalAlignment = SWT.BEGINNING;
		gdTxtArea.horizontalSpan  = 3;
		gdTxtArea.verticalSpan	  = 2;
		gdTxtArea.horizontalIndent= 30;
		
		txtReportsDisplay = new StyledText(reportsTextAreaComposite,SWT.NONE);
		txtReportsDisplay.setEditable(false);
		txtReportsDisplay.setFont(new Font(Display.getCurrent(), "Courier New", 8, SWT.NORMAL));
		txtReportsDisplay.setLayoutData(gdTxtArea);
		
		GridData gdUpDownComp = new GridData();
		gdUpDownComp.grabExcessHorizontalSpace = false;
		gdUpDownComp.horizontalSpan 	= 1;
		gdUpDownComp.verticalAlignment  = SWT.CENTER;
		
		GridLayout grlUpDownComp = new GridLayout();
		grlUpDownComp.numColumns = 1;
		
		Composite reportsUpDownComposite = new Composite(middleComp, SWT.NONE);
		reportsUpDownComposite.setLayout(grlUpDownComp);
		reportsUpDownComposite.setLayoutData(gdUpDownComp);
//		reportsUpDownComposite.setBackground(new Color(Display.getCurrent(), 22,22,22) );
		
		GridData gdBtnUp = new GridData();
		gdBtnUp.grabExcessVerticalSpace = true;
		gdBtnUp.verticalAlignment = SWT.BEGINNING;
		btnUp = new TSButtonLabel(reportsUpDownComposite, SWT.None, "", "btnUp");
		btnUp.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.SCROLL_UP)));
		btnUp.setBounds(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.SCROLL_UP)).getBounds());
		btnUp.setLayoutData(gdBtnUp);
		
		GridData gdBtnDown = new GridData();
		gdBtnDown.grabExcessVerticalSpace = true;
		gdBtnUp.verticalAlignment = SWT.END;
		btnDown = new TSButtonLabel(reportsUpDownComposite,SWT.None,"","btnDown");
		btnDown.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.SCROLL_DOWN)));
		btnDown.setLayoutData(gdBtnDown);
		
		GridData gdBackForwComp = new GridData();
		gdBackForwComp.horizontalSpan = 3;
		gdBackForwComp.horizontalAlignment = SWT.BEGINNING;
		
		GridLayout grlBackForwComp = new GridLayout();
		grlBackForwComp.numColumns = 4;
		if( Util.isSmallerResolution() ) {
			grlBackForwComp.marginWidth = 230;
		} else {
			gdBackForwComp.horizontalAlignment = SWT.CENTER;
		}

		Composite reportsBackForwComposite = new Composite(middleComp,SWT.NONE);
		reportsBackForwComposite.setLayout(grlBackForwComp);
		reportsBackForwComposite.setLayoutData(gdBackForwComp);
		
		GridData gdBtnBack = new GridData();
		gdBtnBack.grabExcessHorizontalSpace = true;
		gdBtnBack.horizontalAlignment = SWT.CENTER;
		gdBtnBack.horizontalSpan = 3;
		
		btnBackward = new TSButtonLabel(reportsBackForwComposite,SWT.None,"","btnBackward");
		btnBackward.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.SCROLL_LEFT)));
		btnBackward.setLayoutData(gdBtnBack);
		
		GridData gdBtnForward = new GridData();
		gdBtnForward.horizontalAlignment = SWT.CENTER;
		gdBtnForward.grabExcessHorizontalSpace=true;
		
		btnForward = new TSButtonLabel(reportsBackForwComposite,SWT.None,"","btnForward");
		btnForward.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.SCROLL_RIGHT)));
		btnForward.setLayoutData(gdBtnForward);
		
		GridData gridData = new GridData();
		gridData.heightHint = buttonBG.getBounds().height;
		gridData.widthHint = buttonBG.getBounds().width;
		gridData.horizontalAlignment = GridData.END;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;

		GridData gdButtonComp = new GridData();
		gdButtonComp.grabExcessHorizontalSpace = true;
		gdButtonComp.horizontalSpan = 1;
		gdButtonComp.verticalAlignment = GridData.CENTER;
		if( Util.isSmallerResolution() ) {
			gdButtonComp.horizontalIndent = 190;
		} else {
			gdButtonComp.horizontalAlignment = GridData.CENTER;
		}			
		GridLayout grlButtonComp = new GridLayout();
		grlButtonComp.numColumns = 2;
		
		Composite reportsButtonComposite1 = new Composite(middleComp,SWT.NONE);
		
		Composite reportsButtonComposite = new Composite(middleComp,SWT.NONE);
		reportsButtonComposite.setLayout(grlButtonComp);
		reportsButtonComposite.setLayoutData(gdButtonComp);
		
		btnPrint  = new SDSImageLabel(reportsButtonComposite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_DETAILS),IVoucherConstants.REPORTS_CTRL_BTN_PRINT);
		btnPrint.setLayoutData(gdBtnBack);
		btnPrint.setBackgroundImage(buttonBG);
		
		btnCancel = new SDSImageLabel(reportsButtonComposite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.CANCEL_BUTTON),IVoucherConstants.REPORTS_CTRL_BTN_CANCEL);
		btnCancel.setLayoutData(gdBtnForward);
		btnCancel.setBackgroundImage(buttonBG);
		btnCancel.setBounds(buttonBG.getBounds());

		
	}

	/**
	 * This method sets the default properties for all the 
	 * controls
	 */
	public void setDefaultProperties(){

		try	{
//			SDSControlFactory.setTouchScreenTextProperties(txtReportsDisplay);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnCancel);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnPrint);
//			SDSControlFactory.setTouchScreenButtonProperties(btnForward);
//			SDSControlFactory.setTouchScreenButtonProperties(btnUp);
//			SDSControlFactory.setTouchScreenButtonProperties(btnDown);
//			SDSControlFactory.setTouchScreenButtonProperties(btnBackward);

			int width  			= 0;
			int height 			= 0;
			int width_Txt_Area  = 0;
			int height_Txt_Area = 0;
			if( Util.isSmallerResolution() ) {
				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 17;
				height_Txt_Area = 240;
				width_Txt_Area 	= 680;
			} else {
				width 			= ControlConstants.BUTTON_WIDTH;
				height 			= ControlConstants.BUTTON_HEIGHT;
				height_Txt_Area	= ControlConstants.TEXT_AREA_HEIGHT;
				width_Txt_Area 	= ControlConstants.TEXT_AREA_WIDTH;
			}
			
			GridData gdTxtArea = new GridData();
			gdTxtArea.heightHint = height_Txt_Area;
			gdTxtArea.widthHint  = width_Txt_Area;
			gdTxtArea.grabExcessHorizontalSpace = true;
			gdTxtArea.verticalAlignment = GridData.FILL;
			gdTxtArea.horizontalAlignment = SWT.CENTER;
			gdTxtArea.horizontalSpan=2;
			gdTxtArea.verticalSpan=2;
			txtReportsDisplay.setLayoutData(gdTxtArea);

			if( Util.isSmallerResolution() ) {
				SDSControlFactory.setTouchScreenImageLabelProperties(btnCancel);
				GridData gdBtnCancel = (GridData)btnCancel.getLayoutData();
				gdBtnCancel.heightHint = height;
				gdBtnCancel.widthHint  = width;
				btnCancel.setLayoutData(gdBtnCancel);
				
				SDSControlFactory.setTouchScreenImageLabelProperties(btnPrint);
				GridData gdBtnPrint = (GridData)btnPrint.getLayoutData();
				gdBtnPrint.heightHint = height;
				gdBtnPrint.widthHint  = width;
				btnPrint.setLayoutData(gdBtnPrint);

				GridData gdBtnBack = (GridData)btnBackward.getLayoutData();
				gdBtnBack.heightHint = ControlConstants.SCROLL_BUTTON_HEIGHT_1;
				gdBtnBack.widthHint = ControlConstants.SCROLL_BUTTON_WIDTH_1;		
				gdBtnBack.horizontalAlignment = SWT.CENTER;
				gdBtnBack.grabExcessVerticalSpace=true;
				gdBtnBack.verticalAlignment = SWT.BEGINNING;
				btnBackward.setLayoutData(gdBtnBack);
				
				GridData gdBtnUp = (GridData)btnUp.getLayoutData();
				gdBtnUp.heightHint = ControlConstants.SCROLL_BUTTON_HEIGHT;
				gdBtnUp.widthHint = ControlConstants.SCROLL_BUTTON_WIDTH;		
				gdBtnUp.horizontalAlignment = SWT.END;
				btnUp.setLayoutData(gdBtnUp);
				
				GridData gdBtnDown = (GridData)btnDown.getLayoutData();
				gdBtnDown.heightHint = ControlConstants.SCROLL_BUTTON_HEIGHT;
				gdBtnDown.widthHint = ControlConstants.SCROLL_BUTTON_WIDTH;		
				gdBtnDown.horizontalAlignment = SWT.BEGINNING;
				btnDown.setLayoutData(gdBtnDown);
				
				GridData gdBtnForward = (GridData)btnForward.getLayoutData();
				gdBtnForward.heightHint = ControlConstants.SCROLL_BUTTON_HEIGHT_1;
				gdBtnForward.widthHint = ControlConstants.SCROLL_BUTTON_WIDTH_1;		
				gdBtnForward.horizontalAlignment = SWT.CENTER;
				gdBtnForward.verticalAlignment = SWT.END;
				gdBtnForward.grabExcessVerticalSpace=true;
				btnForward.setLayoutData(gdBtnForward);
			}
			
		} 
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return the txtReportsDisplay
	 */
	public StyledText getTxtReportsDisplay() {
		return txtReportsDisplay;
	}

	/**
	 * @param txtReportsDisplay the txtReportsDisplay to set
	 */
	public void setTxtReportsDisplay(StyledText txtReportsDisplay) {
		this.txtReportsDisplay = txtReportsDisplay;
	}

	/**
	 * @return the btnCancel
	 */
	public SDSImageLabel getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @param btnCancel the btnCancel to set
	 */
	public void setBtnCancel(SDSImageLabel btnCancel) {
		this.btnCancel = btnCancel;
	}

	/**
	 * @return the btnForward
	 */
	public TSButtonLabel getBtnForward() {
		return btnForward;
	}

	/**
	 * @param btnForward the btnForward to set
	 */
	public void setBtnForward(TSButtonLabel btnForward) {
		this.btnForward = btnForward;
	}

	/**
	 * @return the btnBackward
	 */
	public TSButtonLabel getBtnBackward() {
		return btnBackward;
	}

	/**
	 * @param btnBackward the btnBackward to set
	 */
	public void setBtnBackward(TSButtonLabel btnBackward) {
		this.btnBackward = btnBackward;
	}

	/**
	 * @return the btnUp
	 */
	public TSButtonLabel getBtnUp() {
		return btnUp;
	}

	/**
	 * @param btnUp the btnUp to set
	 */
	public void setBtnUp(TSButtonLabel btnUp) {
		this.btnUp = btnUp;
	}

	/**
	 * @return the btnDown
	 */
	public TSButtonLabel getBtnDown() {
		return btnDown;
	}

	/**
	 * @param btnDown the btnDown to set
	 */
	public void setBtnDown(TSButtonLabel btnDown) {
		this.btnDown = btnDown;
	}

	/**
	 * @return the btnPrint
	 */
	public SDSImageLabel getBtnPrint() {
		return btnPrint;
	}

	/**
	 * @param btnPrint the btnPrint to set
	 */
	public void setBtnPrint(SDSImageLabel btnPrint) {
		this.btnPrint = btnPrint;
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
	 * @return the grpTextArea
	 */
	public Group getGrpTextArea() {
		return grpTextArea;
	}

	/**
	 * @param grpTextArea the grpTextArea to set
	 */
	public void setGrpTextArea(Group grpTextArea) {
		this.grpTextArea = grpTextArea;
	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
}

