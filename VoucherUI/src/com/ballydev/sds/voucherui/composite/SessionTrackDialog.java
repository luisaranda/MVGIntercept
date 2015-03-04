/*****************************************************************************
 * $Id: SessionTrackDialog.java,v 1.15, 2010-10-27 13:56:01Z, Bhandari, Vineet$
 * $Date: 10/27/2010 8:56:01 AM$
 *****************************************************************************
           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.composite;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.dto.CashierSessionReportDTO;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.SessionController;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.VoucherUtil;


/**
 * This class creates the session dialog shell when ever any log out or 
 * exit action of the voucher takes place
 * @author Devi B
 * @version $Revision: 16$ 
 */
public class SessionTrackDialog extends Dialog implements TraverseListener {

	private String response;

	private SDSImageLabel btnSubmit;

	private Integer x;

	private Integer y;

	private Shell mainShell;

	private SDSTSLabel label;

	private boolean altErrorText;	

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;
	
	/**
	 * Instance of the currSessDet touch screen radio image
	 */	
	private TSButtonLabel currSessDet = null;
	
	/**
	 * Instance of the currSessSumm touch screen radio image
	 */	
	private TSButtonLabel currSessSumm = null;

	/**
	 * Instance of the prevSessDet touch screen radio image
	 */	
	private TSButtonLabel prevSessDet = null;
	
	/**
	 * Instance of the prevSessSumm touch screen radio image
	 */	
	private TSButtonLabel prevSessSumm = null;
	/**
	 * Instance of the exitSessWithoutClose touch screen radio image
	 */	
	private TSButtonLabel exitSessWithoutClose = null;
	
	/**
	 * Instance of the exitSessAfterClose touch screen radio image
	 */	
	private TSButtonLabel exitSessAfterClose = null;

	/**
	 * Instance of the cancel touch screen radio image
	 */	
	private TSButtonLabel cancel = null;

	private SDSTSLabel lblCurrSessDet = null;

	private SDSTSLabel lblCurrSessSumm = null;

	private SDSTSLabel lblPrevSessDet = null;

	private SDSTSLabel lblPrevSessSumm = null;

	private SDSTSLabel lblExitWithoutSave = null;

	private SDSTSLabel lblExitAfterSave = null;

	private SDSTSLabel lblCancel = null;

	private Shell sessionShell = null;

	private Composite composite = null;

	private Image buttonBG;

	private SDSTSLabel lblSessMgmt = null;

	private Logger logger = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME); 

	/**
	 * InputDialog constructor
	 * 
	 * @param parent
	 *            the parent
	 */
	public SessionTrackDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	/**
	 * InputDialog constructor
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public SessionTrackDialog(Shell parent, int style) {		
		super(parent, style);
		createImages();
		sessionShell = parent;
	}

	/**
	 * Gets the response
	 * 
	 * @return String
	 */
	public String getInput() {
		return response;
	}

	/**
	 * Sets the response
	 * 
	 * @param response
	 *            the new response
	 */
	public void setInput(String input) {
		this.response = input;
	}

	/**
	 * Opens the dialog and returns the response
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		mainShell = new Shell(sessionShell, SWT.APPLICATION_MODAL);
		mainShell.setText(getText());
		mainShell.setLocation(350,150);
		if( x != null && y != null ) {
			mainShell.setLocation(x.intValue(),y.intValue());			
		}

		createContents(0,true);
		mainShell.setBackground(SDSControlFactory.getDefaultBackGround());
		mainShell.pack();
		mainShell.open();
		Display display = sessionShell.getDisplay();
		while( !mainShell.isDisposed() ) {
			if( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		return response;
	}

	/**
	 * Opens the dialog and returns the response
	 * 
	 * @return String
	 */
	public String open(int textLimit,boolean grid) {
		// Create the dialog window
		mainShell = new Shell(sessionShell, SWT.APPLICATION_MODAL);
		lblSessMgmt = new SDSTSLabel(mainShell, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_SESSION_TRACKING));
		lblSessMgmt.setBackground(SDSControlFactory.getTSBodyColor());
		mainShell.setText(getText());
		mainShell.setLocation(350,150 );
		if( x != null && y != null ) {
			mainShell.setLocation(x.intValue(),y.intValue());
		}

		createContents(textLimit,grid);
		mainShell.setBackground(SDSControlFactory.getTSBodyColor());
		mainShell.pack();
		Util.initializeShellBounds(mainShell);
		mainShell.open();
		Display display = sessionShell.getDisplay();
		while( !mainShell.isDisposed() ) {
			if( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		return response;
	}

	/**
	 * Creates the dialog's contents
	 * @param mainShell the dialog window
	 */

	private void createContents(int textLimit,boolean grid) {
		if( grid ){
			mainShell.setLayout(new GridLayout(2, false));
		} else {
			mainShell.setLayout(new GridLayout(1, false));
		}
		GridData dialogBoxGridData  = new GridData();
		dialogBoxGridData.heightHint= 600;
		dialogBoxGridData.widthHint = 800;
		mainShell.setLayoutData(dialogBoxGridData);
		mainShell.setBackground(SDSControlFactory.getLabelBackGroundColor());
		label = new SDSTSLabel(mainShell,SWT.NONE|SWT.WRAP|SWT.CENTER,getText());
		label.setBackground(SDSControlFactory.getLabelBackGroundColor());
		try {
			Color aColor = SDSControlFactory.getLabelBackGroundColor();
			SDSControlFactory.setTouchScreenLabelProperties(label);
			mainShell.setBackground(aColor);			
		} catch (Exception exc) {
			logger.error(exc.getMessage());
		}
		createButtons();
		mainShell.addDisposeListener(getDisposeLitener());
	}

	/**
	 * Creates the required Buttons
	 * @param mainShell
	 */
	private void createButtons() {
		composite = new Composite(mainShell,SWT.NONE);
		composite.setLayout(new GridLayout(2,false));
		GridData cGridData = new GridData();
		cGridData.horizontalAlignment = GridData.CENTER;
		cGridData.horizontalSpan=2;
		composite.setLayoutData(cGridData);
		composite.setBackground(SDSControlFactory.getTSBodyColor());
//		composite.setBackground(new Color(Display.getCurrent(), 236, 233, 216));

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.grabExcessVerticalSpace = true;
		gdTouchScreenButton.heightHint = 40;
		gdTouchScreenButton.widthHint  = 40;
		if( Util.isSmallerResolution() ) {
			gdTouchScreenButton.heightHint = 43;
			gdTouchScreenButton.widthHint = 43;
		}
		gdTouchScreenButton.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton.verticalAlignment = GridData.END;
		gdTouchScreenButton.grabExcessHorizontalSpace = true;
		
		String print = LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT)+" ";

		radioButtonControl = new RadioButtonControl("Search by any one of the following");
		
		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 10;
		
		// radio button 1
		currSessDet = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_CURR_SESS_DET);
		currSessDet.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		response = currSessDet.getName().toString();
		currSessDet.setBackground(SDSControlFactory.getTSBodyColor());
		currSessDet.setLayoutData(gdRadio);
		radioButtonControl.add(currSessDet);
	
		// label 1
		lblCurrSessDet = new SDSTSLabel(composite, SWT.NONE, print + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_DET));
		lblCurrSessDet.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 2
		currSessSumm = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_CURNT_SESS_SUMM);
		currSessSumm.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		currSessSumm.setBackground(SDSControlFactory.getTSBodyColor());
		currSessSumm.setLayoutData(gdRadio);
		radioButtonControl.add(currSessSumm);
	
		// label 2		
		lblCurrSessSumm = new SDSTSLabel(composite, SWT.NONE, print + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_SUMM));
		lblCurrSessSumm.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 3
		prevSessDet = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_PREV_SESS_DET);
		prevSessDet.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		prevSessDet.setBackground(SDSControlFactory.getTSBodyColor());
		prevSessDet.setLayoutData(gdRadio);
		radioButtonControl.add(prevSessDet);
	
		// label 3
		lblPrevSessDet = new SDSTSLabel(composite, SWT.NONE, print + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PREV_SESS_DET));
		lblPrevSessDet.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 4
		prevSessSumm = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_PREV_SESS_SUMM);
		prevSessSumm.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		prevSessSumm.setBackground(SDSControlFactory.getTSBodyColor());
		prevSessSumm.setLayoutData(gdRadio);
		radioButtonControl.add(prevSessSumm);
	
		// 	label 4		
		lblPrevSessSumm = new SDSTSLabel(composite, SWT.NONE, print + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PREV_SESS_SUMM));
		lblPrevSessSumm.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 5
		exitSessWithoutClose = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_EXIT_WITHOUT_CLOSING);
		exitSessWithoutClose.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		exitSessWithoutClose.setBackground(SDSControlFactory.getTSBodyColor());
		exitSessWithoutClose.setLayoutData(gdRadio);
		radioButtonControl.add(exitSessWithoutClose);
	
		// label 5		
		lblExitWithoutSave = new SDSTSLabel(composite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXIT_WITHOUT_CLOSING));
		lblExitWithoutSave.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 6
		exitSessAfterClose = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_EXIT_AFTER_CLOSING);
		exitSessAfterClose.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		exitSessAfterClose.setBackground(SDSControlFactory.getTSBodyColor());
		exitSessAfterClose.setLayoutData(gdRadio);
		radioButtonControl.add(exitSessAfterClose);
	
		// label 6		
		lblExitAfterSave = new SDSTSLabel(composite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EXIT_AFTER_CLOSING));
		lblExitAfterSave.setBackground(SDSControlFactory.getTSBodyColor());
		
		// radio button 6
		cancel = new TSButtonLabel(composite, SWT.NONE, IVoucherConstants.VOU_SESS_CANCEL);
		cancel.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		cancel.setBackground(SDSControlFactory.getTSBodyColor());
		cancel.setLayoutData(gdRadio);
		radioButtonControl.add(cancel);
	
		// label 6		
		lblCancel = new SDSTSLabel(composite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL));
		lblCancel.setBackground(SDSControlFactory.getTSBodyColor());
		
		// Submit 
		GridData gdBtnSubmit = new GridData();
		gdBtnSubmit.heightHint = buttonBG.getBounds().height;
		gdBtnSubmit.widthHint = buttonBG.getBounds().width;
		gdBtnSubmit.horizontalAlignment = GridData.CENTER;
		gdBtnSubmit.grabExcessHorizontalSpace = true;
		gdBtnSubmit.horizontalIndent = 20;
		gdBtnSubmit.horizontalSpan=2;

		btnSubmit = new SDSImageLabel(composite, SWT.PUSH, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_SUBMIT_BARCODE), IVoucherConstants.VOU_SESS_SUBMIT);
		btnSubmit.getTextLabel().setData("btnSubmit");
		btnSubmit.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		btnSubmit.setLayoutData(gdBtnSubmit);
		btnSubmit.setFont(SDSControlFactory.getDefaultBoldFont());
		btnSubmit.setBackgroundImage(buttonBG);
		btnSubmit.setBounds(buttonBG.getBounds());

		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblCurrSessDet);
			SDSControlFactory.setTouchScreenLabelProperties(lblCurrSessSumm);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrevSessDet);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrevSessSumm);
			SDSControlFactory.setTouchScreenLabelProperties(lblExitWithoutSave);
			SDSControlFactory.setTouchScreenLabelProperties(lblExitAfterSave);
			SDSControlFactory.setTouchScreenLabelProperties(lblCancel);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		registerCustomizedListeners(composite);
	}



	public void closeShell() {
		if( composite != null ) {
			composite.dispose();
		}
		if( mainShell != null ) {
			mainShell.dispose();
		}
	}
	
	public void registerCustomizedListeners(Composite argComposite) {
		SessionMouseListener listener = new SessionMouseListener();
		currSessDet.addMouseListener(listener);
		currSessSumm.addMouseListener(listener);
		prevSessDet.addMouseListener(listener);
		prevSessSumm.addMouseListener(listener);
		exitSessWithoutClose.addMouseListener(listener);
		exitSessAfterClose.addMouseListener(listener);
		cancel.addMouseListener(listener);
		btnSubmit.addMouseListener(listener);
		btnSubmit.getTextLabel().addTraverseListener(this);

	}

	@SuppressWarnings("unused")
	private void doSelect(Control[] ctrl) {

		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof TSButtonLabel ) {
				TSButtonLabel btn = (TSButtonLabel) ctrl[i];
				if( btn.isEnabled() ) {
					response = btn.getName().toString();
					break;
				}
			}
		}
	}

	private void doCheck(Control[] ctrl, MouseEvent e) {
		TSButtonLabel selectedRadioBtn = (TSButtonLabel) ((Control)e.getSource());
		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof TSButtonLabel ) {
				TSButtonLabel btn = (TSButtonLabel) ctrl[i];
				if( selectedRadioBtn.getName().equalsIgnoreCase(btn.getName().toString()) ) {
					btn.setEnabled(true);
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
					response = btn.getName().toString();
				}
				else {
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
					btn.setEnabled(true);
				}
			}
		}
	}

	/**
	 * Creates DisposeLitener with Parent Window focused
	 * @return
	 */
	private DisposeListener getDisposeLitener() {
		return new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				sessionShell.setFocus();
			}
		};
	}
	
	public void setLocation(final int x, final int y) {
		this.x = new Integer(x);
		this.y = new Integer(y);
	}

	/**
	 * @return the altErrorText
	 */
	public boolean isAltErrorText() {
		return altErrorText;
	}

	/**
	 * @param altErrorText the altErrorText to set
	 */
	public void setAltErrorText(boolean altErrorText) {
		this.altErrorText = altErrorText;
	}

	public TSButtonLabel getCurrSessDet() {
		return currSessDet;
	}

	public void setCurrSessDet(TSButtonLabel currSessDet) {
		this.currSessDet = currSessDet;
	}

	public TSButtonLabel getCurrSessSumm() {
		return currSessSumm;
	}

	public void setCurrSessSumm(TSButtonLabel currSessSumm) {
		this.currSessSumm = currSessSumm;
	}

	public TSButtonLabel getPrevSessDet() {
		return prevSessDet;
	}

	public void setPrevSessDet(TSButtonLabel prevSessDet) {
		this.prevSessDet = prevSessDet;
	}

	public TSButtonLabel getPrevSessSumm() {
		return prevSessSumm;
	}

	public void setPrevSessSumm(TSButtonLabel prevSessSumm) {
		this.prevSessSumm = prevSessSumm;
	}

	public TSButtonLabel getexitSessWithoutClose() {
		return exitSessWithoutClose;
	}

	public void setexitSessWithoutClose(
			TSButtonLabel exitSessWithoutClose) {
		this.exitSessWithoutClose = exitSessWithoutClose;
	}

	public TSButtonLabel getexitSessAfterClose() {
		return exitSessAfterClose;
	}

	public void setexitSessAfterClose(TSButtonLabel exitSessAfterClose) {
		this.exitSessAfterClose = exitSessAfterClose;
	}

	public TSButtonLabel getCancel() {
		return cancel;
	}

	public void setCancel(TSButtonLabel cancel) {
		this.cancel = cancel;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	private void createImages() {
		buttonBG  = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
	
	private class SessionMouseListener implements MouseListener{

		public void mouseDown(MouseEvent e) {		
			Control control = (Control) e.getSource();
			Control [] ctrl = null;
			if(composite != null && !composite.isDisposed())
				ctrl = composite.getChildren();

			if( !((Control)e.getSource() instanceof TSButtonLabel || (Control)e.getSource() instanceof SDSImageLabel) ) {
				return;
			}
			
			boolean isCurrentSession = false;
			SessionController sessionController = new SessionController();
			String cashierID = SDSApplication.getLoggedInUserID();
			int siteID = SDSApplication.getSiteDetails().getId();

			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
			CashierSessionReportDTO cashierSessRptDTO = null;

			com.ballydev.sds.voucherui.displays.Display display = null;
			String displayMessage = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE);
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			String dispPort = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT);

			if( control instanceof SDSImageLabel ) {
				if( ((SDSImageLabel) control).getName().toString().equalsIgnoreCase(IVoucherConstants.VOU_SESS_SUBMIT) ) {
//					if(ctrl != null)
//						doSelect(ctrl);
					if( response != null && ((response.equalsIgnoreCase(IVoucherConstants.VOU_EXIT_WITHOUT_CLOSING)) ||
							(response.equalsIgnoreCase(IVoucherConstants.VOU_EXIT_AFTER_CLOSING)) ||
							(response.equalsIgnoreCase(IVoucherConstants.VOU_SESS_CANCEL))) ) {
						if(composite != null && !composite.isDisposed())
							composite.dispose();
						if(mainShell != null && !mainShell.isDisposed())
							mainShell.dispose();
						getParent().setFocus();
						try {
							Class.forName(dispDriver);
							display = com.ballydev.sds.voucherui.displays.Display.getDisplay(dispType);
							if( display != null ) {				
								display.close();
							}
							display.open(dispPort);
							display.dispMsg(displayMessage);	
						} catch (ClassNotFoundException e1) {
							logger.error(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_CLASS_DEF));
						} catch (DisplayNoSuchDriverException e1) {
							logger.error(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN_NO_DRIVER) + e1.getMessage());
						} catch (IOException e2) {
							logger.error(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_EXPTN));
						}
					} else if( response!= null && (response.equalsIgnoreCase(IVoucherConstants.VOU_CURR_SESS_DET) || response.equalsIgnoreCase(IVoucherConstants.VOU_PREV_SESS_DET))) {
						if( response.equalsIgnoreCase(IVoucherConstants.VOU_CURR_SESS_DET) ) {
							isCurrentSession = true;
						}					
						try {
							cashierSessRptDTO = ServiceCall.getInstance().fetchDetailReport(siteID, cashierID, locationValueFromPreference, isCurrentSession);
							
						} catch(VoucherEngineServiceException ex)	{							
							logger.error("VoucherEngineServiceException while fetching the report", ex);				
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
						} catch (Exception ex){								
							logger.error("Exception while while fetching the report ",ex);
							VoucherUIExceptionHandler.handleException(ex);			
						}
						if( cashierSessRptDTO.getSessionId() == null ) {
							MessageDialogUtil.displayTouchScreenInfoDialog("There is no data.");
						} else {
							sessionController.setCashierSessRptDTO(cashierSessRptDTO);
							sessionController.printSessionDetails(response);					
						}
					} else if( response != null &&( response.equalsIgnoreCase(IVoucherConstants.VOU_CURNT_SESS_SUMM) || response.equalsIgnoreCase(IVoucherConstants.VOU_PREV_SESS_SUMM))) {
						if(response.equalsIgnoreCase(IVoucherConstants.VOU_CURNT_SESS_SUMM)) {
							isCurrentSession = true;
						}
						try {
							cashierSessRptDTO = ServiceCall.getInstance().fetchSummaryReport(siteID, cashierID, locationValueFromPreference, isCurrentSession);

						} catch(VoucherEngineServiceException ex)	{							
							logger.error("VoucherEngineServiceException while fetching the report", ex);				
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
						} catch (Exception ex){								
							logger.error("Exception while while fetching the report ",ex);
							VoucherUIExceptionHandler.handleException(ex);			
						}
						if( cashierSessRptDTO.getSessionId() == null ) {
							MessageDialogUtil.displayTouchScreenInfoDialog("There is no data.");
						} else {
							sessionController.setCashierSessRptDTO(cashierSessRptDTO);
							sessionController.printSessionSummary(response);					
						}
					}
				} else {
					response = null;
				}
			}
			else if( control instanceof TSButtonLabel ) {
				doCheck(ctrl, e);
			}
		}

		public void mouseDoubleClick(MouseEvent e) {
			
		}

		public void mouseUp(MouseEvent e) {

		}
	}

	public void keyTraversed(TraverseEvent e) {
		
	}	
}
