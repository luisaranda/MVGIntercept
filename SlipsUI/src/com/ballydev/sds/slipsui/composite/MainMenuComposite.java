/*****************************************************************************
 * $Id: MainMenuComposite.java,v 1.1, 2009-07-22 14:49:10Z, Radhika, Kameswaran$
 * $Date: 7/22/2009 9:49:10 AM$
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
package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


/**
 * This is the composite having the three touch screen application buttons
 * 
 * @author dambereen
 * 
 */
public class MainMenuComposite extends Composite {
	

	
	/**
	 * ParentComposite Instance
	 */
	public static MainMenuComposite parentComposite;
	
	
	/**
	 * CurrentComposite Instance
	 */
	public static Composite currentComposite;
	/**
	 * ParentComposite constructor
	 * @param parent
	 * @param style
	 */
	public MainMenuComposite(Composite parent, int style) {
		super(parent, style);
		initialize();		
		parentComposite=this;
	}

	/**
	 * initialize method of composite
	 */
	private void initialize() {
		
		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.verticalAlignment = GridData.FILL;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		
		
		this.setLayout(gridLayout);
		this.setLayoutData(gridData21);
	}
	
	/**
	 * method to get the instance of the BaseComposite
	 * @param parent
	 * @param style
	 * @return
	 */
	public static MainMenuComposite getInstance(Composite parent, int style)
	{
		if(parentComposite==null || parentComposite.isDisposed())
		{
			parentComposite=new MainMenuComposite(parent,style);
		}
		return parentComposite;
	}
	/**
	 * @return the currentComposite
	 */
	public static Composite getCurrentComposite() {
		return currentComposite;
	}

	/**
	 * @param currentComposite the currentComposite to set
	 */
	public static void setCurrentComposite(Composite currentComposite) {
		MainMenuComposite.currentComposite = currentComposite;
	}

	/*

	
	public static MainMenuComposite parentComposite;
	
	*//**
	 * mainMenuHeaderComposite Instance
	 *//*
	private Composite mainMenuHeaderComposite = null;

	*//**
	 * mainMenuMiddleComposite Instance
	 *//*
	private Composite mainMenuMiddleComposite = null;

	*//**
	 * mainMenuFooterComposite Instance
	 *//*
	private Composite mainMenuFooterComposite = null;

	*//**
	 * SlipsUI Button Instance
	 *//*
	private CbctlButton btnSlipUI = null;

	*//**
	 * JackpotUI Button Instance
	 *//*
	private CbctlButton btnJackpotUI = null;

	*//**
	 * VoucherUI Button Instance
	 *//*
	private CbctlButton btnVoucherUI = null;

	*//**
	 * Title1 Label instance.
	 *//*
	private CbctlLabel lblTitle1 = null;

	*//**
	 * Title2 Label instance
	 *//*
	private CbctlLabel lblTitle2 = null;

	*//**
	 * JackpotUI Label instance
	 *//*
	private CbctlLabel lblJackpotUI = null;

	*//**
	 * SlipUI Label instance.
	 *//*
	private CbctlLabel lblSlipUI = null;

	*//**
	 * VoucherUI label instance.
	 *//*
	private CbctlLabel lblVoucherUI = null;

	*//**
	 * Exit Button instance.
	 *//*
	private CbctlButton btnExit = null;

	private CbctlLabel lblBallyCopyright = null;
	
	
	*//**
	 * CurrentComposite Instance
	 *//*
	public static Composite currentComposite;

	*//**
	 * MainMenuComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 *//*
	public MainMenuComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		setSize(new Point(800, 600));
		// this.setLayout(parent.getLayout());
		// parent.layout();

	}

	*//**
	 * initialize method
	 *//*
	private void initialize() {

		GridData gridData4 = new GridData();
		gridData4.grabExcessVerticalSpace = true;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.FILL;

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 0;

		createMainMenuHeaderComposite();
		createMainMenuMiddleComposite();
		createMainMenuFooterComposite();

		this.setLayout(gridLayout1);
		// this.setBackground(new Color(Display.getCurrent(),171, 209, 255));
		layout();
	}

	*//**
	 * This method initializes MainMenuHeaderComposite
	 * 
	 *//*
	private void createMainMenuHeaderComposite() {

		GridData gridData8 = new GridData();
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.verticalAlignment = GridData.BEGINNING;
		gridData8.grabExcessVerticalSpace = true;
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.END;
		gridData7.grabExcessVerticalSpace = true;
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = false;
		gridData21.heightHint = 150;
		gridData21.verticalAlignment = GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 50;
		gridLayout.marginWidth = 20;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 35;
		mainMenuHeaderComposite = new Composite(this, SWT.NONE);
		mainMenuHeaderComposite.setBackground(new Color(Display.getCurrent(),
				255, 255, 255));
		mainMenuHeaderComposite.setLayoutData(gridData21);
		mainMenuHeaderComposite.setLayout(gridLayout);

		lblTitle1 = new CbctlLabel(mainMenuHeaderComposite, SWT.CENTER);
		lblTitle1
				.setText(LabelLoader
						.getLabelValue(LabelKeyConstants.WELCOME_NOTE_ON_TOUCH_SCREEN_HOMEPAGE));
		lblTitle1
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblTitle1.setLayoutData(gridData7);

		lblTitle2 = new CbctlLabel(mainMenuHeaderComposite, SWT.CENTER);
		lblTitle2
				.setText(LabelLoader
						.getLabelValue(LabelKeyConstants.SELECT_NOTE_ON_TOUCH_SCREEN_HOMEPAGE));
		lblTitle2
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblTitle2.setLayoutData(gridData8);
	}

	*//**
	 * This method initializes MainMenuMiddleComposite
	 * 
	 *//*
	private void createMainMenuMiddleComposite() {

		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.grabExcessVerticalSpace = true;
		gridData11.verticalAlignment = GridData.BEGINNING;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.CENTER;
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.grabExcessVerticalSpace = true;
		gridData10.verticalAlignment = GridData.BEGINNING;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.CENTER;
		gridData9.verticalAlignment = GridData.BEGINNING;
		GridData gridData6 = new GridData();
		gridData6.heightHint = 100;
		// gridData6.heightHint = 70;
		gridData6.widthHint = 100;
		// gridData6.widthHint = 90;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.horizontalAlignment = GridData.CENTER;
		gridData6.verticalAlignment = GridData.END;

		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 20;
		gridLayout3.numColumns = 3;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.heightHint = 100;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalAlignment = GridData.FILL;
		mainMenuMiddleComposite = new Composite(this, SWT.NONE);
		mainMenuMiddleComposite.setBackground(new Color(Display.getCurrent(),
				171, 209, 255));
		mainMenuMiddleComposite.setLayout(gridLayout3);
		mainMenuMiddleComposite.setLayoutData(gridData5);

		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.verticalAlignment = GridData.END;
		gridData3.heightHint = 100;
		gridData3.widthHint = 100;
		gridData3.grabExcessHorizontalSpace = true;

		GridData gridData1 = new GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.END;
		gridData1.heightHint = 100;
		gridData1.widthHint = 100;
		gridData1.grabExcessHorizontalSpace = true;

		btnSlipUI = new CbctlButton(mainMenuMiddleComposite, SWT.FLAT, "",
				LabelKeyConstants.SLIPS_UI_BUTTON);
		btnSlipUI
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		btnSlipUI.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(ImageConstants.SLIPS_UI_BUTTON_IMG)));
		btnSlipUI.setLayoutData(gridData1);

		btnJackpotUI = new CbctlButton(mainMenuMiddleComposite, SWT.NONE, "",
				LabelKeyConstants.JACKPOT_UI_BUTTON);
		btnJackpotUI.setFont(new Font(Display.getDefault(), "Arial", 12,
				SWT.BOLD));
		btnJackpotUI.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(ImageConstants.JACKPOT_UI_BUTTON_IMG)));
		btnJackpotUI.setLayoutData(gridData3);

		btnVoucherUI = new CbctlButton(mainMenuMiddleComposite, SWT.NONE, "",
				LabelLoader.getLabelValue(LabelKeyConstants.VOUCHER_UI_BUTTON));
		btnVoucherUI.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(ImageConstants.VOUCHER_UI_BUTTON_IMG)));
		btnVoucherUI.setLayoutData(gridData6);

		lblSlipUI = new CbctlLabel(mainMenuMiddleComposite, SWT.NONE);
		lblSlipUI.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.SLIPS_UI_BUTTON));
		lblSlipUI.setBackground(new Color(Display.getCurrent(), 171, 209, 255));
		lblSlipUI
				.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.BOLD));
		lblSlipUI.setLayoutData(gridData9);

		lblJackpotUI = new CbctlLabel(mainMenuMiddleComposite, SWT.NONE);
		lblJackpotUI.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.JACKPOT_UI_BUTTON));
		lblJackpotUI.setBackground(new Color(Display.getCurrent(), 171, 209,
				255));
		lblJackpotUI.setFont(new Font(Display.getDefault(), "Arial", 12,
				SWT.BOLD));
		lblJackpotUI.setLayoutData(gridData10);

		lblVoucherUI = new CbctlLabel(mainMenuMiddleComposite, SWT.NONE);
		lblVoucherUI.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.VOUCHER_UI_BUTTON));
		lblVoucherUI.setBackground(new Color(Display.getCurrent(), 171, 209,
				255));
		lblVoucherUI.setFont(new Font(Display.getDefault(), "Arial", 12,
				SWT.BOLD));
		lblVoucherUI.setLayoutData(gridData11);

	}

	*//**
	 * This method initializes MainMenuFooterComposite
	 * 
	 *//*
	private void createMainMenuFooterComposite() {

		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = GridData.BEGINNING;
		gridData22.verticalAlignment = GridData.END;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.END;
		gridData2.heightHint = 75;
		gridData2.widthHint = 75;
		gridData2.horizontalAlignment = GridData.END;

		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 30;
		gridLayout2.marginHeight = 20;
		gridLayout2.numColumns = 2;
		gridLayout2.marginWidth = 20;

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.END;
		gridData.heightHint = 150;
		gridData.horizontalAlignment = GridData.FILL;
		mainMenuFooterComposite = new Composite(this, SWT.NONE);
		mainMenuFooterComposite.setBackground(new Color(Display.getCurrent(),
				255, 255, 255));
		mainMenuFooterComposite.setLayout(gridLayout2);
		mainMenuFooterComposite.setLayoutData(gridData);

		lblBallyCopyright = new CbctlLabel(mainMenuFooterComposite, SWT.NONE);
		lblBallyCopyright.setText(LabelLoader.getLabelValue(LabelKeyConstants.BALLY_COPYRIGHT_LABEL));
		lblBallyCopyright.setLayoutData(gridData22);
		btnExit = new CbctlButton(mainMenuFooterComposite, SWT.NONE, "",
				LabelKeyConstants.EXIT_BUTTON);
		btnExit.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(ImageConstants.EXIT_BUTTON_IMG)));
		btnExit.setLayoutData(gridData2);
	}

	*//**
	 * @param currentComposite the currentComposite to set
	 *//*
	public static void setCurrentComposite(Composite currentComposite) {
		MainMenuComposite.currentComposite = currentComposite;
	}

	*//**
	 * @param parentComposite the parentComposite to set
	 *//*
	public static void setParentComposite(MainMenuComposite parentComposite) {
		MainMenuComposite.parentComposite = parentComposite;
	}

	*//**
	 * @return the currentComposite
	 *//*
	public static Composite getCurrentComposite() {
		return currentComposite;
	}

	*//**
	 * @return the parentComposite
	 *//*
	public static MainMenuComposite getParentComposite() {
		return parentComposite;
	}
*/}
