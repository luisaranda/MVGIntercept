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
package com.ballydev.sds.voucherui.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.ReportsTextComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.form.ReportsForm;
import com.ballydev.sds.voucherui.form.ReportsTextForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.VoucherPrintReports;

/**
 * This acts as a controller class for the 
 * reports composite
 * @author Nithya kalyani R
 * @version $Revision: 8$ 
 */
public class ReportsTextController  extends SDSBaseController{

	/**
	 * Instance of ReprintVoucherComposite
	 */
	private ReportsTextComposite reportsTextComposite;

	/**
	 * Instance of ReprintForm
	 */
	private ReportsTextForm reportsTextForm;

	/**
	 * Instance of the parent composite
	 */
	private Composite parent;
	/**
	 * Instance of Logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Constructor of the class
	 */
	public ReportsTextController(Composite parent,int style,ReportsTextForm reportsForm, SDSValidator pValidator)throws Exception {
		super(reportsForm, pValidator);
		reportsTextComposite = new ReportsTextComposite(parent,style);
		this.parent = parent;
		this.reportsTextForm = reportsForm;
		VoucherMiddleComposite.setCurrentComposite(reportsTextComposite);	
		parent.layout();
		super.registerEvents(reportsTextComposite);
		this.registerCustomizedListeners(reportsTextComposite);
		reportsTextForm.setTxtReportsDisplay(reportsForm.getTxtReportsDisplay());
		reportsTextComposite.getTxtReportsDisplay().setText(reportsForm.getTxtReportsDisplay());
		//reportsTextForm.setTxtReportsDisplay(getTextForTest().toString());
	}

	public StringBuilder getTextForTest(){
		StringBuilder textDisplay = new StringBuilder();
		textDisplay.append("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest"+"\r\n\r\n");
		textDisplay.append("Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1Test1"+"\r\n\r\n");
		textDisplay.append("Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2Test2"+"\r\n\r\n");
		textDisplay.append("Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3Test3"+"\r\n\r\n");
		textDisplay.append("Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4Test4"+"\r\n\r\n");
		textDisplay.append("Test5"+"\r\n\r\n");
		textDisplay.append("Test6"+"\r\n\r\n");
		textDisplay.append("Test7"+"\r\n\r\n");
		textDisplay.append("Test8"+"\r\n\r\n");
		textDisplay.append("Test9"+"\r\n\r\n");
		textDisplay.append("Test10"+"\r\n\r\n");
		textDisplay.append("Test11"+"\r\n\r\n");
		textDisplay.append("Test12"+"\r\n\r\n");
		textDisplay.append("Test11"+"\r\n\r\n");
		textDisplay.append("Test111"+"\r\n\r\n");
		textDisplay.append("Test211"+"\r\n\r\n");
		textDisplay.append("Test311"+"\r\n\r\n");
		textDisplay.append("Test411"+"\r\n\r\n");
		textDisplay.append("Test511"+"\r\n\r\n");
		textDisplay.append("Test611"+"\r\n\r\n");
		textDisplay.append("Test711"+"\r\n\r\n");
		textDisplay.append("Test811"+"\r\n\r\n");
		textDisplay.append("Test911"+"\r\n\r\n");
		textDisplay.append("Test1011"+"\r\n\r\n");
		textDisplay.append("Test111111"+"\r\n\r\n");
		textDisplay.append("Test1211"+"\r\n\r\n");

		return textDisplay;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {		
		return reportsTextComposite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ReportsTextComposite vReportTextComposite = ((ReportsTextComposite)argComposite);
		vReportTextComposite.getBtnPrint().addMouseListener(this);
		vReportTextComposite.getBtnPrint().getTextLabel().addTraverseListener(this);
		vReportTextComposite.getBtnCancel().addMouseListener(this);
		vReportTextComposite.getBtnCancel().getTextLabel().addTraverseListener(this);
		vReportTextComposite.getBtnForward().addMouseListener(this);
		vReportTextComposite.getBtnForward().addTraverseListener(this);
		vReportTextComposite.getBtnBackward().addMouseListener(this);
		vReportTextComposite.getBtnBackward().addTraverseListener(this);
		vReportTextComposite.getBtnUp().addMouseListener(this);
		vReportTextComposite.getBtnUp().addTraverseListener(this);
		vReportTextComposite.getBtnDown().addMouseListener(this);
		vReportTextComposite.getBtnDown().addTraverseListener(this);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		try {
			populateForm(reportsTextComposite);
			Control control = (Control) e.getSource();
			if( control instanceof SDSImageLabel ) {
				
				if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_CANCEL)) {
					reportsTextComposite.dispose();
					new ReportsController(parent,SWT.NONE,new ReportsForm(), new SDSValidator((getClass()),true));

				} else if( ((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_PRINT)) {
					try {
						ProgressIndicatorUtil.openInProgressWindow();
						VoucherPrintReports printingService = new VoucherPrintReports();
						printingService.printText(reportsTextForm.getTxtReportsDisplay());
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_SUCCESS));
					} catch (Exception e1) {
						log.error("Exception occured while printing the details",e1);
						if( e1.getMessage()!=null && e1.getMessage().trim().length() > 0 ) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(e1.getMessage());
						} else {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_DETAILS_ERROR));
						}
					} finally {
						ProgressIndicatorUtil.closeInProgressWindow();
					}
				} 
			} else if( control instanceof TSButtonLabel ) {
				if( ((TSButtonLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_UP)) {
					if(reportsTextComposite.getTxtReportsDisplay()!=null){
						StyledText text = reportsTextComposite.getTxtReportsDisplay();
						text.setTopIndex(text.getTopIndex() - 2);
					}
				} else if( ((TSButtonLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_DOWN)) {
					if( reportsTextComposite.getTxtReportsDisplay() != null ) {
						StyledText text = reportsTextComposite.getTxtReportsDisplay();
						text.setTopIndex(text.getTopIndex() + 2);
					}
					
				} else if( ((TSButtonLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_BACKWARD)) {
					if( reportsTextComposite.getTxtReportsDisplay() != null ) {
						StyledText text = reportsTextComposite.getTxtReportsDisplay();
						text.setHorizontalIndex(text.getHorizontalIndex() - 2);
					}
				} else if( ((TSButtonLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_FORWARD)) {
					if( reportsTextComposite.getTxtReportsDisplay() != null ) {
						StyledText text = reportsTextComposite.getTxtReportsDisplay();
						text.setHorizontalIndex(text.getHorizontalIndex() + 2);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
