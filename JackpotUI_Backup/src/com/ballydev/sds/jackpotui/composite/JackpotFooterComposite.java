package com.ballydev.sds.jackpotui.composite;

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
import com.ballydev.sds.framework.constant.IImageConstants;
import com.ballydev.sds.framework.constant.IMessageConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;

/**
 * This class gives the footer look for Jackpot UI.
 * @author anantharajr
 * @version $Revision: 3$
 */
public class JackpotFooterComposite extends Composite {

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
	private TSButtonLabel lblKeyBoardIcon=null;
	/**
	 * home icon label
	 */
	private TSButtonLabel lblHomeIcon=null;
	/**
	 * logout icon label
	 */
	private TSButtonLabel lblLogoutIcon=null;
	/**
	 * copyright info icon label
	 */
	private SDSLabel lblCopyRightInfo = null;
	/**
	 * lblKeyboard label
	 */
	private TSButtonLabel lblKeyboard = null;
	/**
	 * lblHomePage label
	 */
	private TSButtonLabel lblHomePage = null;
	/**
	 * lblLogout label
	 */
	private TSButtonLabel lblLogout = null;
	/**
	 * lblBallyImage label
	 */
	private TSButtonLabel lblBallyImage = null;
	/**
	 * lblPreferenceIcon label
	 */
	private TSButtonLabel lblPreferenceIcon = null;
	/**
	 * lblPreference label
	 */
	private TSButtonLabel lblPreference = null;
	/**
	 * filler label
	 */
	private Label label = null;
	/**
	 *  filler label
	 */
	private Label label1 = null;
	
	private String imgKeyboardBtn;
	
	private String imgLogoutBtn;
	
	private String imgFooterBackGrd;
	
	private String imgHomeIcon;
	
	private String imgBallyLogo;

	/**
	 * @param parent
	 * @param style
	 */
	public JackpotFooterComposite(Composite parent, int style) {
		super(parent, style);
		if (Util.isSmallerResolution()) {
			imgKeyboardBtn = ImageConstants.S_KEYBOARD_BUTTON_IMG;
			imgLogoutBtn = ImageConstants.S_LOGOUT_BUTTON_IMG;
			imgFooterBackGrd = ImageConstants.S_FOOTER_BACKGROUND;
			imgHomeIcon = ImageConstants.S_HOME_ICON;
			imgBallyLogo = ImageConstants.S_BALLY_LOGO;
		}
		else {
			imgKeyboardBtn = ImageConstants.KEYBOARD_BUTTON_IMG;
			imgLogoutBtn = ImageConstants.LOGOUT_BUTTON_IMG;
			imgFooterBackGrd = ImageConstants.FOOTER_BACKGROUND;
			imgHomeIcon = ImageConstants.HOME_ICON;
			imgBallyLogo = ImageConstants.BALLY_LOGO;
		}
		initialize();
		
	}

	/**
	 * intialize method
	 */
	private void initialize() {
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
	private void createFooterLeftTopComposite() {
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
	private void createFooterLeftBottomComposite() {
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
		lblBallyImage = new TSButtonLabel(getFooterLeftBottomComposite(), SWT.NONE);
		lblBallyImage.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgHomeIcon)));
		lblBallyImage.setLayoutData(gridData6);
		lblCopyRightInfo = new SDSLabel(getFooterLeftBottomComposite(), SWT.NONE);
		//lblCopyRightInfo.setBackground(new Color(Display.getCurrent(), 255, 204, 0));
		lblCopyRightInfo.setText(LabelLoader.getLabelValue(IMessageConstants.FRAMEWORK_COPYRIGHT_TEXT));
		//lblCopyRightInfo.setFont(new Font(Display.getDefault(), "Arial", 8, SWT.NORMAL));
		lblCopyRightInfo.setLayoutData(gridData1);
		
	}

	/**
	 * This method initializes footerRightComposite	
	 *
	 */
	private void createFooterRightComposite() {
		

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
		gridLayout2.numColumns = 5;
		gridLayout2.makeColumnsEqualWidth = true;
		gridLayout2.verticalSpacing = 0;
		footerRightComposite = new Composite(this, SWT.NONE);
		footerRightComposite.setLayout(gridLayout2);
		footerRightComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		footerRightComposite.setBackgroundImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgFooterBackGrd)));
		footerRightComposite.setLayoutData(gridData3);
		label = new Label(getFooterRightComposite(), SWT.NONE);
		label.setText("");
		label.setLayoutData(gridData9);
		lblPreferenceIcon = new TSButtonLabel(footerRightComposite, SWT.NONE,"preference");
		lblPreferenceIcon.setImage(Activator.getPlugin().getImageRegistry().get(IImageConstants.IMAGE_TOUCH_SCREEN_PREFERENCE));
		lblPreferenceIcon.setLayoutData(gridData7);
		lblKeyBoardIcon = new TSButtonLabel(footerRightComposite, SWT.NONE,"keyboard");
		lblKeyBoardIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgKeyboardBtn)));
		lblKeyBoardIcon.setLayoutData(gridData25);
		lblHomeIcon = new TSButtonLabel(footerRightComposite, SWT.NONE,"home");
		lblHomeIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgHomeIcon)));
		lblHomeIcon.setLayoutData(gridData12);
		lblLogoutIcon = new TSButtonLabel(footerRightComposite, SWT.NONE,"logout");
		lblLogoutIcon.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgLogoutBtn)));
		lblLogoutIcon.setLayoutData(gridData13);
		label1 = new Label(getFooterRightComposite(), SWT.NONE);
		label1.setLayoutData(gridData14);
		lblPreference = new TSButtonLabel(getFooterRightComposite(), SWT.NONE);
		lblPreference.setText(LabelLoader.getLabelValue(LabelKeyConstants.PREFERENCE));
		lblPreference.setForeground(SDSControlFactory.getDefaultForegroundColor());
		lblPreference.setLayoutData(gridData8);
		lblKeyboard = new TSButtonLabel(getFooterRightComposite(), SWT.NONE);
		lblKeyboard.setText(LabelLoader.getLabelValue(LabelKeyConstants.KEYBOARD_BUTTON));
		lblKeyboard.setForeground(SDSControlFactory.getDefaultForegroundColor());
		lblKeyboard.setLayoutData(gridData2);
		lblHomePage = new TSButtonLabel(getFooterRightComposite(), SWT.NONE);
		lblHomePage.setText(LabelLoader.getLabelValue(LabelKeyConstants.HOME));
		lblHomePage.setForeground(SDSControlFactory.getDefaultForegroundColor());
		lblHomePage.setLayoutData(gridData4);
		lblLogout = new TSButtonLabel(getFooterRightComposite(), SWT.NONE);
		lblLogout.setText(LabelLoader.getLabelValue(LabelKeyConstants.LOG_OUT_BUTTON));
		lblLogout.setForeground(SDSControlFactory.getDefaultForegroundColor());
		lblLogout.setLayoutData(gridData5);
		}

	/**
	 * @return the footerLeftBottomComposite
	 */
	public Composite getFooterLeftBottomComposite() {
		return footerLeftBottomComposite;
	}

	/**
	 * @param footerLeftBottomComposite the footerLeftBottomComposite to set
	 */
	public void setFooterLeftBottomComposite(Composite footerLeftBottomComposite) {
		this.footerLeftBottomComposite = footerLeftBottomComposite;
	}

	/**
	 * @return the footerLeftTopComposite
	 */
	public Composite getFooterLeftTopComposite() {
		return footerLeftTopComposite;
	}

	/**
	 * @param footerLeftTopComposite the footerLeftTopComposite to set
	 */
	public void setFooterLeftTopComposite(Composite footerLeftTopComposite) {
		this.footerLeftTopComposite = footerLeftTopComposite;
	}

	/**
	 * @return the footerRightComposite
	 */
	public Composite getFooterRightComposite() {
		return footerRightComposite;
	}

	/**
	 * @param footerRightComposite the footerRightComposite to set
	 */
	public void setFooterRightComposite(Composite footerRightComposite) {
		this.footerRightComposite = footerRightComposite;
	}

	/**
	 * @return the lblHomeIcon
	 */
	public TSButtonLabel getLblHomeIcon() {
		return lblHomeIcon;
	}

	/**
	 * @param lblHomeIcon the lblHomeIcon to set
	 */
	public void setLblHomeIcon(TSButtonLabel lblHomeIcon) {
		this.lblHomeIcon = lblHomeIcon;
	}

	/**
	 * @return the lblLogoutIcon
	 */
	public TSButtonLabel getLblLogoutIcon() {
		return lblLogoutIcon;
	}

	/**
	 * @param lblLogoutIcon the lblLogoutIcon to set
	 */
	public void setLblLogoutIcon(TSButtonLabel lblLogoutIcon) {
		this.lblLogoutIcon = lblLogoutIcon;
	}

	/**
	 * @return the lblKeyBoardIcon
	 */
	public TSButtonLabel getLblKeyBoardIcon() {
		return lblKeyBoardIcon;
	}

	/**
	 * @param lblKeyBoardIcon the lblKeyBoardIcon to set
	 */
	public void setLblKeyBoardIcon(TSButtonLabel lblKeyBoardIcon) {
		this.lblKeyBoardIcon = lblKeyBoardIcon;
	}

	/**
	 * @return the lblPreferenceIcon
	 */
	public TSButtonLabel getLblPreferenceIcon() {
		return lblPreferenceIcon;
	}

	/**
	 * @param lblPreferenceIcon the lblPreferenceIcon to set
	 */
	public void setLblPreferenceIcon(TSButtonLabel lblPreferenceIcon) {
		this.lblPreferenceIcon = lblPreferenceIcon;
	}

	
}  //  @jve:decl-index=0:visual-constraint="29,12"
