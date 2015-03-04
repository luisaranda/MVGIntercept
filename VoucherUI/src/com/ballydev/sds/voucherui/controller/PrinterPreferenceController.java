/*****************************************************************************
 *  Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.composite.SlipPrinterPreferenceComposite;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.form.PrinterPreferenceForm;
import com.ballydev.sds.voucherui.print.PrinterUtil;

/**
 * Controller class for printerPreferenceComposite
 * @author Nithya kalyani
 * @version $Revision: 11$
 */

public class PrinterPreferenceController extends SDSBaseController {

	/**
	 * Instance of PrinterPreferenceComposite
	 */
//	private PrinterPreferenceComposite tableComposite;

	/**
	 * PrinterPreferenceComposite tableComposite
	 */
	private SlipPrinterPreferenceComposite tableComposite;

	/**
	 * Instance of PrinterPreferenceForm
	 */
	private PrinterPreferenceForm form;

	/**
	 * Total record count
	 */
	private int totalRecordCount = 0;

	/**
	 * Number of pages
	 */
	private int numOfPages = 0;

	/**
	 * Total no of records
	 */
	private int numOfRecords = 4;

	/**
	 * Variable to hold the mod value
	 */
	private int modValue = 0;

	/**
	 * Variable to hold the intial index value 
	 */
	private int initialIndex = 0;

	/**
	 *Page count 
	 */
	private int pageCount = 0;

	/**
	 * valriable to track the last clicked button
	 */
	private boolean previousClicked = false;

	/**
	 * List if items available
	 */
	private List<String> pageList = new ArrayList<String>();

	/**
	 * List of printers available
	 */
	private List<String> listOfAllAvailablePrinters = null;

	/**
	 * LoginForm instance - the associate form of TouchScreenLoginController.
	 * This is needed to set the site text box in login screen when a site is
	 * chosen from the site table.
	 */

	public PrinterPreferenceController(Composite parent, int style,PrinterPreferenceForm form,
			TableDescriptor tableDescriptor) throws Exception {
		super(form,null);
		this.form = form;

		tableComposite = new SlipPrinterPreferenceComposite(parent, style,tableDescriptor);
		tableComposite.getTableViewer().getTable().getColumns()[0].setWidth(315);
//		tableComposite.getLblPrefLogOutMsg().setText(LabelLoader.getLabelValue(I));
		registerEvents(tableComposite);
		this.registerCustomizedListeners(tableComposite);
		if( Util.isSmallerResolution() ) {
			numOfRecords = 3;	
		}
		listOfAllAvailablePrinters =new ArrayList<String>();
		int count=0;
		PrinterUtil printerUtil = new PrinterUtil();
		count = printerUtil.getPrinterCount();
		int selectionIndex = 0;
		String prefPrinter = SDSPreferenceStore.getStringStoreValue(IVoucherConstants.PRINTER_VOU_PREFERENCE);
		for( int i=0; i < count; i++ ) {
			String printerName=printerUtil.getPrinterName(i);
			if( printerName.contains(prefPrinter) ) {
				selectionIndex=i;
			}
			listOfAllAvailablePrinters.add(printerUtil.getPrinterName(i));
		}
		totalRecordCount = listOfAllAvailablePrinters.size();
		numOfPages = totalRecordCount / numOfRecords;
		modValue = totalRecordCount % numOfRecords;
		if( modValue > 0 ) {
			numOfPages += 1;
		}

		if( listOfAllAvailablePrinters.size() <= numOfRecords ) {
			numOfRecords = totalRecordCount;
			tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());
			tableComposite.getBtnNext().setImage(tableComposite.getDisableNextImage());

		}
		for( int i = initialIndex; i < initialIndex + numOfRecords; i++ ) {
			tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());
			String page = (String) listOfAllAvailablePrinters.get(i);
			pageList.add(page);
		}
		pageCount++;
		tableComposite.layout();
		initialIndex += numOfRecords;

		form.setLstPrinter(pageList);
		refreshControl(tableComposite.getTableViewer());
		if( selectionIndex <= 4 ) {
			tableComposite.getTableViewer().getTable().setSelection(selectionIndex);
		}
		
		
		populateScreen(tableComposite);
		
		if( !Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
			tableComposite.getLblPrefLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINTER_PERF_CANNOT_BE_SET_AFTER_LOGIN));
			tableComposite.disableControls();
			tableComposite.getTableDescriptor().getTable().setEnabled(false);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return tableComposite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		SlipPrinterPreferenceComposite slipPrinterPreferenceComposite = (SlipPrinterPreferenceComposite)argComposite;
		slipPrinterPreferenceComposite.getBtnNext().addMouseListener(this);
		slipPrinterPreferenceComposite.getBtnPrevious().addMouseListener(this);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		if( e.getSource() instanceof TSButtonLabel ) {
			TSButtonLabel tsBtnLabel = (TSButtonLabel) e.getSource();
			if ( "previous".equalsIgnoreCase(tsBtnLabel.getName()) ) {
				getPreviousData();
				setDefaultSelection();
			} else if ("next".equalsIgnoreCase(tsBtnLabel.getName())) {
				getNextData();
				setDefaultSelection();
			}
		} else if( e.getSource()  instanceof Table ){
			try {
				populateForm(tableComposite);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void setDefaultSelection() {
		try {
			String printerFromStore = PlatformUI.getPreferenceStore().getString(IVoucherConstants.PRINTER_VOU_PREFERENCE);
			if(printerFromStore == null) {
				return;
			}
			for(int i = 0; i < pageList.size(); ++i) {
				String printerName = (String) pageList.get(i);
				if(printerName.equalsIgnoreCase(printerFromStore)) {
					form.setSelectedPrinter((String)pageList.get(i));
					refreshControl(tableComposite.getTableViewer());
					tableComposite.getTableViewer().getTable().setSelection(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * 
	 */
	private void getPreviousData() {
		if( pageCount != 1 ) {
			pageCount--;
	
			if (pageCount < numOfPages) {
				tableComposite.getBtnNext().setImage(tableComposite.getNextImage());
	
			}
			if (pageCount <= 1) {
				tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());
	
			}
			tableComposite.layout();
			tableComposite.getTableViewer().getTable().setSelection(-1);
			if (initialIndex > totalRecordCount - modValue) {
				if (!previousClicked) {
					previousClicked = true;
	
				}
				pageList.clear();
				int count = initialIndex - numOfRecords - modValue;
				for (int i = count; i < count + numOfRecords; i++) {
					String site = (String) listOfAllAvailablePrinters.get(i);
					pageList.add(site);
	
					try {
						populateScreen(tableComposite);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
	
				initialIndex -= modValue;
	
			} else if (initialIndex > modValue) {
				if (!previousClicked
						|| (initialIndex == totalRecordCount - modValue)) {
					previousClicked = true;
					initialIndex -= numOfRecords;
	
				}
				pageList.clear();
				int count = initialIndex - numOfRecords;
				for (int i = count; i < count + numOfRecords; i++) {
					String site = (String) listOfAllAvailablePrinters.get(i);
					pageList.add(site);
					try {
						populateScreen(tableComposite);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				initialIndex -= numOfRecords;
	
			} else if (initialIndex > 0) {
				if (!previousClicked) {
					previousClicked = true;
					initialIndex -= modValue;
				}
				pageList.clear();
				for (int i = 0; i < numOfRecords; i++) {
					String site = (String) listOfAllAvailablePrinters.get(i);
					pageList.add(site);
					try {
						populateScreen(tableComposite);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				initialIndex -= modValue;
			} else {
				return;
			}
		}
	}

	/**
	 * 
	 */
	private void getNextData() {
		if(pageCount != numOfPages){
			pageCount++;
			if (pageCount >= numOfPages) {
				tableComposite.getBtnNext().setImage(tableComposite.getDisableNextImage());
			}
			if (pageCount > 1) {
				tableComposite.getBtnPrevious().setImage(tableComposite.getPrevImage());
	
			}
			tableComposite.layout();
			tableComposite.getTableViewer().getTable().setSelection(-1);
	
			if (initialIndex < totalRecordCount - modValue) {
	
				pageList.clear();
				if (previousClicked) {
					previousClicked = false;
					initialIndex += numOfRecords;
				}
				for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
					String site = (String) listOfAllAvailablePrinters.get(i);
					pageList.add(site);
					try {
						populateScreen(tableComposite);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
	
				}
				initialIndex += numOfRecords;
	
			} else if (initialIndex < totalRecordCount) {
				pageList.clear();
				if (previousClicked) {
					previousClicked = false;
	
				}
				for (int i = initialIndex; i < initialIndex + modValue; i++) {
					String site = (String) listOfAllAvailablePrinters.get(i);
					pageList.add(site);
					try {
						populateScreen(tableComposite);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				initialIndex += modValue;
			} else {
				return;
			}
		}
	}

	/**
	 * @return the tableComposite
	 */
	public SlipPrinterPreferenceComposite getTableComposite() {
		return tableComposite;
	}

	/**
	 * @param tableComposite the tableComposite to set
	 */
	public void setTableComposite(SlipPrinterPreferenceComposite tableComposite) {
		this.tableComposite = tableComposite;
	}

	/**
	 * @return the form
	 */
	public PrinterPreferenceForm getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(PrinterPreferenceForm form) {
		this.form = form;
	}

}
