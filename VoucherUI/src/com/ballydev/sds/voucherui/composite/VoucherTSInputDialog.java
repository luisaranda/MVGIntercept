/*****************************************************************************
 * $Id: VoucherTSInputDialog.java,v 1.13, 2010-04-13 09:27:35Z, Lokesh, Krishna Sinha$
 * $Date: 4/13/2010 4:27:35 AM$
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
package com.ballydev.sds.voucherui.composite;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.SDSText;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;

/**
 * Provides an Input Dialog that has a Keypad Image Labels.
 * @author Abbas
 */

public class VoucherTSInputDialog extends Dialog implements MouseListener {

	private String response;

	private SDSImageLabel iLblOk;

	private SDSImageLabel iLblCancel;

	private SDSTSText labelTextKeyPad;

	private Integer x;

	private Integer y;

	private Shell mainShell;

	private SDSTSMandatoryLabel label;

	private boolean altErrorText;

	private Image buttonBG;

	/**
	 * InputDialog constructor
	 * 
	 * @param parent the parent
	 */
	public VoucherTSInputDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		createImages();
	}

	/**
	 * InputDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public VoucherTSInputDialog(Shell parent, int style) {
		super(parent, style);
		createImages();
	}

	/**
	 * Opens the dialog and returns the response
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		mainShell = new Shell(getParent(), SWT.APPLICATION_MODAL);
		mainShell.setText(getText());
		mainShell.setLocation(10,20 );
		
		if( x != null && y != null ) {
			mainShell.setLocation(x.intValue(),y.intValue());			
		}

		createContents(0,true);
		mainShell.setBackground(SDSControlFactory.getTSBodyColor());
//		mainShell.setBackground(SDSControlFactory.getDefaultBackGround());
		mainShell.pack();
		mainShell.open();
		Display display = getParent().getDisplay();
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
		mainShell = new Shell(getParent(), SWT.APPLICATION_MODAL);
		mainShell.setText(getText());
		mainShell.setLocation(10,20 );

		if( x != null && y != null ) {
			mainShell.setLocation(x.intValue(),y.intValue());
		}

		createContents(textLimit,grid);
		mainShell.setBackground(SDSControlFactory.getTSBodyColor());
//		mainShell.setBackground(SDSControlFactory.getDefaultBackGround());
		mainShell.pack();
		mainShell.open();
		Display display = getParent().getDisplay();
		while( !mainShell.isDisposed() ) {
			if( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		return response;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param mainShell the dialog window
	 */
	private void createContents(int textLimit,boolean grid) {
		GridLayout layout =  null;
		if( grid ) {
			layout = new GridLayout(3, false);
		} else {
			layout = new GridLayout(1, false);
		}
		layout.marginHeight = 20;
		layout.marginLeft = 20;
		mainShell.setLayout(layout);
		
		GridData dialogBoxGridData = new GridData();
		mainShell.setLayoutData(dialogBoxGridData);
		mainShell.setBackground(SDSControlFactory.getTSBodyColor());
//		mainShell.setBackground(SDSControlFactory.getLabelBackGroundColor());
		label = new SDSTSMandatoryLabel(mainShell, SWT.NONE | SWT.WRAP | SWT.CENTER, getText());
		label.setBackground(SDSControlFactory.getTSBodyColor());
		label.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
//		label.setBackground(SDSControlFactory.getLabelBackGroundColor());

		// Display the response box
		labelTextKeyPad = new SDSTSText(mainShell,"", "");
		if( textLimit != 0 ) {
			labelTextKeyPad.setTextLimit(textLimit);
			labelTextKeyPad.setFocus();
			labelTextKeyPad.setTextLimitChkEnabled(true);
		}

//		SDSTSLabel lblKeyBoard = new SDSTSLabel(mainShell, SWT.PUSH, "");
//		lblKeyBoard.setName("keyboard");
//		lblKeyBoard.addMouseListener(this);
//		lblKeyBoard.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_KEYBOARD)));
//		lblKeyBoard.setBackground(SDSControlFactory.getTSBodyColor());
//		lblKeyBoard.setBackground(SDSControlFactory.getDefaultBackGround());

		try {
			SDSControlFactory.setTouchScreenTextProperties((SDSText)labelTextKeyPad);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( !grid ) {
			try {
				GridData grdLabel  = new GridData();
				grdLabel.widthHint = 340;
				label.getLabel().setLayoutData(grdLabel);
				GridData grTxtbox = labelTextKeyPad.getGridData();
				if( grTxtbox == null ) {
					grTxtbox = new GridData();
				}
				grTxtbox.horizontalAlignment 	= SWT.CENTER;
				grTxtbox.horizontalIndent	 	= 20;
				grTxtbox.widthHint			 	= 240;
				labelTextKeyPad.setLayoutData(grTxtbox);
				GridData grKeypadLabel = new GridData();
				grKeypadLabel.horizontalAlignment = SWT.CENTER;
				grKeypadLabel.widthHint		 	= 150;
				grKeypadLabel.horizontalIndent	= 50;
//				lblKeyBoard.setLayoutData(grKeypadLabel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		createButtons();

		mainShell.addDisposeListener(getDisposeLitener());
	}

	/**
	 * Creates the required Buttons
	 * @param mainShell
	 */
	private void createButtons() {
		Composite composite = new Composite(mainShell,SWT.NONE);
		composite.setLayout(new GridLayout(2,false));
		GridData cGridData = new GridData();
		cGridData.horizontalAlignment = GridData.CENTER;
		cGridData.horizontalSpan = 25;
		composite.setLayoutData(cGridData);
		composite.setBackground(SDSControlFactory.getTSBodyColor());
//		composite.setBackground(SDSControlFactory.getDefaultBackGround());
		GridData data;
		
		iLblOk = new SDSImageLabel(composite, SWT.PUSH, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_SUBMIT), "");
		data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 1;

		data.grabExcessHorizontalSpace = true;
		iLblOk.setLayoutData(data);
		iLblOk.addMouseListener(getMouseListenerForOk());
//		iLblOk.getTextLabel().addMouseListener(getMouseListenerForOk());

		iLblCancel = new SDSImageLabel(composite, SWT.PUSH, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL), "");
		data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		iLblCancel.setLayoutData(data);
		data.horizontalSpan = 1;
		data.grabExcessHorizontalSpace = true;
		iLblCancel.addMouseListener(getMouseListenerForCancel());
	}
	
	/**
	 * If the submit Image Label selected, text value will be returned
	 * @param mainShell
	 * @return
	 */
	private MouseListener getMouseListenerForOk() {
		return new MouseListener() {
			public void mouseDown(MouseEvent e) {
				if( (Control) e.widget instanceof SDSImageLabel || (Control) e.widget instanceof SDSLabel ) {
					System.out.println("e.widget : " + e.widget);
					if( Util.isEmpty(labelTextKeyPad.getText()) ) {
						
						if( !isAltErrorText() ) {
							
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(MessageFormat.format(LabelLoader.getLabelValue("FRAMEWORK.ERRORS.REQUIRED"),getText()));
							labelTextKeyPad.setFocus();
							return;
						}
					}
				}

				response = labelTextKeyPad.getText();
				mainShell.close();
				getParent().setFocus();
			}

			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
			}
		};
	}

	/**
	 * If the cancel Image Label selected, the response will be null 
	 * @param mainShell
	 * @return
	 */
	private MouseListener getMouseListenerForCancel() {
		return new MouseListener() {
			
			public void mouseDown(MouseEvent e) {
				response = null;
				mainShell.close();
			}

			public void mouseDoubleClick(MouseEvent e) {
			}
			
			public void mouseUp(MouseEvent e) {
			}
		};
	}
	
	/**
	 * Creates DisposeLitener with Parent Window focused
	 * @return
	 */
	private DisposeListener getDisposeLitener() {
		return new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				getParent().setFocus();
			}
		};
	}
	
	public void setLocation(final int x, final int y) {
		this.x = new Integer(x);
		this.y = new Integer(y);
	}

	private void baseFocusEventHandler(TypedEvent typedEvent){
		if( typedEvent.getSource() instanceof SDSTSLabel ) {
			try {
				Shell keyBoardShell;
				Rectangle screenBounds;
				Rectangle keyboardBounds;
				keyBoardShell = KeyBoardUtil.createKeyBoard();
				KeyBoardUtil.setTxtInFocus(labelTextKeyPad);
				screenBounds = mainShell.getBounds();
				keyboardBounds = keyBoardShell.getBounds();
				keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height - keyboardBounds.height + 400);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void mouseDown(MouseEvent e) {
		baseFocusEventHandler(e);
	}

	public void mouseDoubleClick(MouseEvent e) {

	}

	public void mouseUp(MouseEvent e) {

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
	 * @param response the new response
	 */
	public void setInput(String input) {
		this.response = input;
	}

	/**
	 * Gets the labelTextKeyPad
	 * 
	 * @return SDSTSText
	 */
	public SDSTSText getLabelTextKeyPad() {
		return labelTextKeyPad;
	}

	/**
	 * Sets the labelTextKeyPad
	 * 
	 * @param labelTextKeyPad the new labelTextKeyPad
	 */
	public void setLabelTextKeyPad(SDSTSText labelTextKeyPad) {
		this.labelTextKeyPad = labelTextKeyPad;
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
	
	private void createImages() {
		buttonBG = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
}
