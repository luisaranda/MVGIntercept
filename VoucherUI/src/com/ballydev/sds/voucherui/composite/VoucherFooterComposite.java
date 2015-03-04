package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.ballydev.sds.framework.Activator;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.IMessageConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;

/**
 * This class gives the footer look for Voucher UI.
 * @author 
 * @version $Revision: 2$
 */
public class VoucherFooterComposite extends Composite 
{

	/**
	 * footerLeftTopComposite instance
	 */
	private Composite footerLeftTopComposite = null;
	/**
	 * footerLeftBottomComposite instance
	 */
	private Composite footerLeftBottomComposite = null;
	/**
	 * footerRightComposite instance
	 */
	private Composite footerRightComposite = null;
	/**
	 * keyboard icon label
	 */
	private SDSTSLabel lblKeyBoardIcon=null;
	/**
	 * home icon label
	 */
	private SDSTSLabel lblHomeIcon=null;
	/**
	 * logout icon label
	 */
	private SDSTSLabel lblLogoutIcon=null;
	/**
	 * copyright info icon label
	 */
	private SDSLabel lblCopyRightInfo = null;
	/**
	 * lblKeyboard label
	 */
	private SDSTSLabel lblKeyboard = null;
	/**
	 * lblHomePage label
	 */
	private SDSTSLabel lblHomePage = null;
	/**
	 * lblLogout label
	 */
	private SDSTSLabel lblLogout = null;
	/**
	 * lblBallyImage label
	 */
	private SDSTSLabel lblBallyImage = null;
	/**
	 * lblPreferenceIcon label
	 */
	private SDSTSLabel lblPreferenceIcon = null;
	/**
	 * lblPreference label
	 */
	private SDSTSLabel lblPreference = null;
	/**
	 * filler label
	 */
	private Label label = null;
	/**
	 *  filler label
	 */
	private Label label1 = null;

	/**
	 * Label exit icon
	 */
	private SDSLabel lblExitIcon = null;

	/**
	 * lbl exit
	 */
	private SDSTSLabel lblExit = null;


	/**
	 * @param parent
	 * @param style
	 */
	public VoucherFooterComposite(Composite parent, int style) 
	{
		super(parent, style);
		initialize();
	}

	/**
	 * intialize method
	 */
	private void initialize() 
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 110;
		createFooterLeftTopComposite();
		this.setLayout(gridLayout);
		this.setLayoutData(gridData);
		setSize(new Point(913, 110));
		createFooterRightComposite();
		createFooterLeftBottomComposite();
	}

	/**
	 * This method initializes footerLeftTopComposite	
	 *
	 */
	private void createFooterLeftTopComposite() 
	{
		GridLayout gridLayout11 = new GridLayout();
		gridLayout11.horizontalSpacing = 0;
		gridLayout11.marginHeight = 0;
		gridLayout11.verticalSpacing = 0;
		GridData gridData10 = new GridData();
		gridData10.grabExcessVerticalSpace = true;
		gridData10.horizontalAlignment = GridData.FILL;
		gridData10.verticalAlignment = GridData.FILL;
		gridData10.heightHint = 43;
		gridData10.grabExcessHorizontalSpace = true;

		footerLeftTopComposite = new Composite(this, SWT.NONE);
		footerLeftTopComposite.setBackground(SDSControlFactory.getTouchScreenHomeBackgroundColor());
		footerLeftTopComposite.setLayoutData(gridData10);
		footerLeftTopComposite.setLayout(gridLayout11);
	}

	/**
	 * This method initializes footerLeftBottomComposite	
	 *
	 */
	private void createFooterLeftBottomComposite() 
	{
		try 
		{
			GridData gridData6 = new GridData();
			gridData6.grabExcessVerticalSpace = true;
			gridData6.horizontalAlignment = GridData.END;
			gridData6.verticalAlignment = GridData.CENTER;
			gridData6.grabExcessHorizontalSpace = false;
			GridData gridData1 = new GridData();
			gridData1.horizontalAlignment = GridData.BEGINNING;
			gridData1.grabExcessVerticalSpace = true;
			gridData1.grabExcessHorizontalSpace = true;
			gridData1.horizontalIndent = 0;
			gridData1.verticalAlignment = GridData.CENTER;
			GridLayout gridLayout12 = new GridLayout();
			gridLayout12.horizontalSpacing = 0;
			gridLayout12.marginWidth = 0;
			gridLayout12.marginHeight = 0;
			gridLayout12.numColumns = 2;
			gridLayout12.verticalSpacing = 0;
			GridData gridData11 = new GridData();
			gridData11.grabExcessVerticalSpace = true;
			gridData11.horizontalAlignment = GridData.FILL;
			gridData11.verticalAlignment = GridData.FILL;
			gridData11.heightHint = 43;
			gridData11.grabExcessHorizontalSpace = true;
			footerLeftBottomComposite = new Composite(this, SWT.NONE);
			footerLeftBottomComposite.setBackground(SDSControlFactory.getTouchScreenFooterBackgroundColor());
			footerLeftBottomComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
			footerLeftBottomComposite.setLayoutData(gridData11);
			footerLeftBottomComposite.setLayout(gridLayout12);

			lblBallyImage = new SDSTSLabel(getFooterLeftBottomComposite(), SWT.NONE);
			lblBallyImage.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.BALLY_LOGO)));
			lblBallyImage.setLayoutData(gridData6);

			lblCopyRightInfo = new SDSTSLabel(getFooterLeftBottomComposite(), SWT.NONE);
			lblCopyRightInfo.setText(LabelLoader.getLabelValue(IMessageConstants.FRAMEWORK_COPYRIGHT_TEXT));
			SDSControlFactory.setDefaultLabelFontProperties(lblCopyRightInfo);
			lblCopyRightInfo.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblCopyRightInfo.setLayoutData(gridData1);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	/**
	 * This method initializes footerRightComposite	
	 *
	 */
	private void createFooterRightComposite() 
	{
		try 
		{
			GridData gridData14 = new GridData();
			gridData14.grabExcessVerticalSpace = false;
			gridData14.grabExcessHorizontalSpace = true;
			GridData gridData9 = new GridData();
			gridData9.grabExcessVerticalSpace = true;
			gridData9.grabExcessHorizontalSpace = true;
			GridData gridData8 = new GridData();
			gridData8.grabExcessHorizontalSpace = false;
			gridData8.verticalAlignment = GridData.CENTER;
			gridData8.horizontalAlignment = GridData.CENTER;
			GridData gridData7 = new GridData();
			gridData7.grabExcessHorizontalSpace = false;
			gridData7.verticalAlignment = GridData.CENTER;
			gridData7.horizontalAlignment = GridData.CENTER;
			GridData gridData5 = new GridData();
			gridData5.widthHint = 50;
			gridData5.horizontalAlignment = GridData.CENTER;
			gridData5.horizontalIndent = 0;
			gridData5.verticalAlignment = GridData.CENTER;
			GridData gridData4 = new GridData();
			gridData4.horizontalAlignment = GridData.CENTER;
			gridData4.horizontalIndent = 0;
			gridData4.verticalAlignment = GridData.CENTER;
			GridData gridData2 = new GridData();
			gridData2.horizontalAlignment = GridData.CENTER;
			gridData2.grabExcessVerticalSpace = false;
			gridData2.horizontalIndent = 0;
			gridData2.verticalAlignment = GridData.CENTER;
			GridData gridData25 = new GridData();
			gridData25.grabExcessVerticalSpace = true;
			gridData25.horizontalAlignment = GridData.CENTER;
			gridData25.verticalAlignment = GridData.CENTER;
			gridData25.grabExcessHorizontalSpace = false;
			GridData gridData13 = new GridData();
			gridData13.grabExcessVerticalSpace = true;
			gridData13.horizontalAlignment = GridData.CENTER;
			gridData13.verticalAlignment = GridData.CENTER;
			gridData13.grabExcessHorizontalSpace = true;
			GridData gridData12 = new GridData();
			gridData12.grabExcessVerticalSpace = true;
			gridData12.horizontalAlignment = GridData.CENTER;
			gridData12.verticalAlignment = GridData.CENTER;
			gridData12.grabExcessHorizontalSpace = true;
			GridData gridData3 = new GridData();
			gridData3.grabExcessVerticalSpace = true;
			gridData3.verticalAlignment = GridData.FILL;
			gridData3.widthHint = 500;
			gridData3.grabExcessHorizontalSpace = false;
			gridData3.verticalSpan = 2;
			gridData3.horizontalAlignment = GridData.CENTER;
			GridLayout gridLayout2 = new GridLayout();
			gridLayout2.horizontalSpacing = 0;
			gridLayout2.marginWidth = 5;
			gridLayout2.marginHeight = 17;
			gridLayout2.numColumns = 6;
			gridLayout2.makeColumnsEqualWidth = true;
			gridLayout2.verticalSpacing = 0;

			footerRightComposite = new Composite(this, SWT.NONE);
			footerRightComposite.setLayout(gridLayout2);
			footerRightComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
			footerRightComposite.setBackgroundImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.FOOTER_BACKGROUND)));
			footerRightComposite.setLayoutData(gridData3);

			label = new Label(getFooterRightComposite(), SWT.NONE);
			label.setText("");
			label.setLayoutData(gridData9);

			lblPreferenceIcon = new SDSTSLabel(footerRightComposite, SWT.NONE,"preference");
			lblPreferenceIcon.setName("preference");
			lblPreferenceIcon.setImage(Activator.getPlugin().getImageRegistry().get(IImageConstants.IMAGE_TOUCH_SCREEN_PREFERENCE));
			lblPreferenceIcon.setLayoutData(gridData7);

			lblKeyBoardIcon = new SDSTSLabel(footerRightComposite, SWT.NONE,"keyboard");
			lblKeyBoardIcon.setName("keyboard");
			lblKeyBoardIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.KEYBOARD_BUTTON_IMG)));
			lblKeyBoardIcon.setLayoutData(gridData25);

			lblHomeIcon = new SDSTSLabel(footerRightComposite, SWT.NONE,"home");
			lblHomeIcon.setName("home");
			lblHomeIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.HOME_ICON)));
			lblHomeIcon.setLayoutData(gridData12);

			lblLogoutIcon = new SDSTSLabel(footerRightComposite, SWT.NONE,"logout");
			lblLogoutIcon.setName("logout");
			lblLogoutIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.LOGOUT_BUTTON_IMG)));
			lblLogoutIcon.setLayoutData(gridData13);

			lblExitIcon = new SDSTSLabel(footerRightComposite, SWT.NONE,"lblExit");
			lblExitIcon.setName("lblExit");
			lblExitIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.EXIT_BUTTON_IMG)));
			lblExitIcon.setLayoutData(gridData13);

			label1 = new Label(getFooterRightComposite(), SWT.NONE);
			label1.setLayoutData(gridData14);

			lblPreference = new SDSTSLabel(getFooterRightComposite(), SWT.NONE);
			lblPreference.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.PREFERENCE));
			SDSControlFactory.setDefaultLabelFontProperties(lblPreference);
			lblPreference.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblPreference.setLayoutData(gridData8);

			lblKeyboard = new SDSTSLabel(getFooterRightComposite(), SWT.NONE);
			lblKeyboard.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.KEYBOARD_BUTTON));
			SDSControlFactory.setDefaultLabelFontProperties(lblKeyboard);
			lblKeyboard.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblKeyboard.setLayoutData(gridData2);

			lblHomePage = new SDSTSLabel(getFooterRightComposite(), SWT.NONE);
			lblHomePage.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.HOME));
			SDSControlFactory.setDefaultLabelFontProperties(lblHomePage);
			lblHomePage.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblHomePage.setLayoutData(gridData4);

			lblLogout = new SDSTSLabel(getFooterRightComposite(), SWT.NONE);
			lblLogout.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_LOG_OUT));
			SDSControlFactory.setDefaultLabelFontProperties(lblLogout);
			lblLogout.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblLogout.setLayoutData(gridData5);

			lblExit = new SDSTSLabel(getFooterRightComposite(), SWT.NONE);
			lblExit.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXIT));
			SDSControlFactory.setDefaultLabelFontProperties(lblExit);
			lblExit.setForeground(SDSControlFactory.getTouchScreenFooterLabelColor());
			lblExit.setLayoutData(gridData5);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the footerLeftBottomComposite
	 */
	public Composite getFooterLeftBottomComposite() 
	{
		return footerLeftBottomComposite;
	}

	/**
	 * @param footerLeftBottomComposite the footerLeftBottomComposite to set
	 */
	public void setFooterLeftBottomComposite(Composite footerLeftBottomComposite) 
	{
		this.footerLeftBottomComposite = footerLeftBottomComposite;
	}

	/**
	 * @return the footerLeftTopComposite
	 */
	public Composite getFooterLeftTopComposite() 
	{
		return footerLeftTopComposite;
	}

	/**
	 * @param footerLeftTopComposite the footerLeftTopComposite to set
	 */
	public void setFooterLeftTopComposite(Composite footerLeftTopComposite) 
	{
		this.footerLeftTopComposite = footerLeftTopComposite;
	}

	/**
	 * @return the footerRightComposite
	 */
	public Composite getFooterRightComposite() 
	{
		return footerRightComposite;
	}

	/**
	 * @param footerRightComposite the footerRightComposite to set
	 */
	public void setFooterRightComposite(Composite footerRightComposite) 
	{
		this.footerRightComposite = footerRightComposite;
	}

	/**
	 * @return the lblHomeIcon
	 */
	public SDSTSLabel getLblHomeIcon() 
	{
		return lblHomeIcon;
	}

	/**
	 * @param lblHomeIcon the lblHomeIcon to set
	 */
	public void setLblHomeIcon(SDSTSLabel lblHomeIcon) 
	{
		this.lblHomeIcon = lblHomeIcon;
	}

	/**
	 * @return the lblLogoutIcon
	 */
	public SDSTSLabel getLblLogoutIcon() 
	{
		return lblLogoutIcon;
	}

	/**
	 * @param lblLogoutIcon the lblLogoutIcon to set
	 */
	public void setLblLogoutIcon(SDSTSLabel lblLogoutIcon) 
	{
		this.lblLogoutIcon = lblLogoutIcon;
	}

	/**
	 * @return the lblKeyBoardIcon
	 */
	public SDSTSLabel getLblKeyBoardIcon() 
	{
		return lblKeyBoardIcon;
	}

	/**
	 * @param lblKeyBoardIcon the lblKeyBoardIcon to set
	 */
	public void setLblKeyBoardIcon(SDSTSLabel lblKeyBoardIcon) 
	{
		this.lblKeyBoardIcon = lblKeyBoardIcon;
	}

	/**
	 * @return the lblPreferenceIcon
	 */
	public SDSTSLabel getLblPreferenceIcon() 
	{
		return lblPreferenceIcon;
	}

	/**
	 * @param lblPreferenceIcon the lblPreferenceIcon to set
	 */
	public void setLblPreferenceIcon(SDSTSLabel lblPreferenceIcon) 
	{
		this.lblPreferenceIcon = lblPreferenceIcon;
	}

	public SDSTSLabel getLblExit() {
		return lblExit;
	}

	public void setLblExit(SDSTSLabel lblExit) {
		this.lblExit = lblExit;
	}

	public SDSLabel getLblExitIcon() {
		return lblExitIcon;
	}

	public void setLblExitIcon(SDSLabel lblExitIcon) {
		this.lblExitIcon = lblExitIcon;
	}


}  //  @jve:decl-index=0:visual-constraint="29,12"
