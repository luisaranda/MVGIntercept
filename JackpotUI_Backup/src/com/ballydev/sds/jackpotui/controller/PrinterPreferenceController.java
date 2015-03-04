/*****************************************************************************
 * $Id: PrinterPreferenceController.java,v 1.9, 2010-02-22 09:34:37Z, Immaculate Annamal Irudayaraj$
 * $Date: 2/22/2010 3:34:37 AM$
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
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.composite.SlipPrinterPreferenceComposite;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.form.PrinterPreferenceForm;
import com.ballydev.sds.jackpotui.print.PrinterUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Controller class for printerPreferenceComposite
 * @author anantharajr
 * @version $Revision: 10$
 */

public class PrinterPreferenceController extends SDSBaseController {

	/**
	 * PrinterPreferenceComposite tableComposite
	 */
	private SlipPrinterPreferenceComposite tableComposite;

	/**
	 * PrinterPreferenceForm instance
	 */
	private PrinterPreferenceForm form;

	/**
	 * Total Record Count
	 */
	private int totalRecordCount = 0;

	/**
	 * No of pages
	 */
	private int numOfPages = 0;

	/**
	 * Num of records
	 */
	private int numOfRecords = 4;

	/**
	 * modValue
	 */
	private int modValue = 0;

	/**
	 * initialIndex
	 */
	private int initialIndex = 0;

	/**
	 * pageCount
	 */
	private int pageCount = 0;

	/**
	 * Flag to check if the previous button was clicked
	 */
	private boolean previousClicked = false;

	/**
	 * List of the pages
	 */
	private List<String> pageList = new ArrayList<String>();

	/**
	 * List of the available printers
	 */
	private List<String> listOfAllAvailablePrinters = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	
	
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
		tableComposite.getTableViewer().getTable().getColumns()[0]
				.setWidth(315);
		super.registerEvents(tableComposite);
		listOfAllAvailablePrinters =new ArrayList<String>();
		int count=0;
		PrinterUtil printerUtil = new PrinterUtil();
		count = printerUtil.getPrinterCount();
		int selectionIndex=0;
		String prefPrinter=SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
		for(int i=0;i<count;i++)
		{
			String printerName=printerUtil.getPrinterName(i);
			if(printerName.contains(prefPrinter)) {
				selectionIndex=i;
			}
			listOfAllAvailablePrinters.add(printerUtil.getPrinterName(i));
		}
		totalRecordCount = listOfAllAvailablePrinters.size();
		numOfPages = totalRecordCount / numOfRecords;
		modValue = totalRecordCount % numOfRecords;
		if (modValue > 0) {
			numOfPages += 1;
		}

		if (listOfAllAvailablePrinters.size() <= numOfRecords) {
			numOfRecords = totalRecordCount;
			tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());
			tableComposite.getBtnNext().setImage(tableComposite.getDisableNextImage());

		}
		for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
			tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());
			String page = (String) listOfAllAvailablePrinters.get(i);
			pageList.add(page);
		}
		pageCount++;
		tableComposite.layout();
		initialIndex += numOfRecords;

		form.setLstPrinter(pageList);
		refreshControl(tableComposite.getTableViewer());
		if(selectionIndex <= 4) {
			tableComposite.getTableViewer().getTable().setSelection(selectionIndex);
		}
		registerCustomizedListeners(tableComposite);
	
	}

	@Override
	public Composite getComposite() {
		return tableComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		PrinterPreferenceMouseListener listener = new PrinterPreferenceMouseListener();
		tableComposite.getBtnNext().addMouseListener(listener);
		tableComposite.getBtnNext().addTraverseListener(this);
		tableComposite.getBtnPrevious().addMouseListener(listener);
		tableComposite.getBtnPrevious().addTraverseListener(this);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {}
	
	private class PrinterPreferenceMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {
			if (!(Util.isEmpty(SDSApplication.getLoggedInUserID()))) {		
				return;
			}
			if (e.getSource() instanceof TSButtonLabel) {
				TSButtonLabel button = (TSButtonLabel) e.getSource();
				String selectedPrinter = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
					if ("previous".equalsIgnoreCase(button.getName())) {
					if(pageCount != 1) {
							
						
					pageCount--;

					if (pageCount < numOfPages) {
						tableComposite.getBtnNext().setImage(tableComposite.getNextImage());

					}
					if (pageCount <= 1) {
						tableComposite.getBtnPrevious().setImage(tableComposite.getDisablePrevImage());

					}
					tableComposite.layout();
					//tableComposite.getTableViewer().getTable().setSelection(-1);
					tableComposite.getTableViewer().getTable().deselectAll();	
					
					if (initialIndex > totalRecordCount - modValue) {
						if (!previousClicked) {
							previousClicked = true;

						}
						pageList.clear();
						int selectedIndex = -1;
						int count = initialIndex - numOfRecords - modValue;
						for (int i = count; i < count + numOfRecords; i++) {
							String site = (String) listOfAllAvailablePrinters.get(i);
							pageList.add(site);
							if(site.equalsIgnoreCase(selectedPrinter)) {
								selectedIndex = i-count;
							}
							
							try {
								populateScreen(tableComposite);
							} catch (Exception e1) {
								log.error(e1);
							}
						}
						initialIndex -= modValue;
						tableComposite.getTableViewer().getTable().setSelection(selectedIndex);

					} else if (initialIndex > modValue) {
						if (!previousClicked
								|| (initialIndex == totalRecordCount - modValue)) {
							previousClicked = true;
							initialIndex -= numOfRecords;

						}
						pageList.clear();
						int count = initialIndex - numOfRecords;
						int selectedIndex = -1;
						for (int i = count; i < count + numOfRecords; i++) {
							String site = (String) listOfAllAvailablePrinters.get(i);
							
							pageList.add(site);
							if(site.equalsIgnoreCase(selectedPrinter)) {
								selectedIndex = i - count;
							}
							try {
								populateScreen(tableComposite);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						initialIndex -= numOfRecords;
						tableComposite.getTableViewer().getTable().setSelection(selectedIndex);

					} else if (initialIndex > 0) {
						if (!previousClicked) {
							previousClicked = true;
							initialIndex -= modValue;
						}
						pageList.clear();
						int selectedIndex = -1;
						for (int i = 0; i < numOfRecords; i++) {
							String site = (String) listOfAllAvailablePrinters.get(i);
							
							pageList.add(site);
							if(site.equalsIgnoreCase(selectedPrinter)) {
								selectedIndex = i - numOfRecords;
							}
							try {
								populateScreen(tableComposite);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						initialIndex -= modValue;
						tableComposite.getTableViewer().getTable().setSelection(selectedIndex);
				
					} else {
					
						return;
					}
				}
			
				} else if ("next".equalsIgnoreCase(button.getName())) {
					
					if(pageCount != numOfPages){
						
					

					pageCount++;
					if (pageCount >= numOfPages) {
						tableComposite.getBtnNext().setImage(tableComposite.getDisableNextImage());
					}
					if (pageCount > 1) {
						tableComposite.getBtnPrevious().setImage(tableComposite.getPrevImage());

					}
					tableComposite.layout();
					tableComposite.getTableViewer().getTable().deselectAll();	

					if (initialIndex < totalRecordCount - modValue) {

						pageList.clear();
						if (previousClicked) {
							previousClicked = false;
							initialIndex += numOfRecords;
						}
						int selectedIndex = -1;
						for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
							String site = (String) listOfAllAvailablePrinters.get(i);
							pageList.add(site);
							if(site.equalsIgnoreCase(selectedPrinter)) {
								selectedIndex = i - initialIndex;
							}
							try {
								populateScreen(tableComposite);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}
						initialIndex += numOfRecords;
						tableComposite.getTableViewer().getTable().setSelection(selectedIndex);

					} else if (initialIndex < totalRecordCount) {
						pageList.clear();
						if (previousClicked) {
							previousClicked = false;

						}
						int selectedIndex = -1;
						for (int i = initialIndex; i < initialIndex + modValue; i++) {
							String site = (String) listOfAllAvailablePrinters.get(i);
							pageList.add(site);
							if(site.equalsIgnoreCase(selectedPrinter)) {
								selectedIndex = i - initialIndex;
							}

							try {
								populateScreen(tableComposite);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						initialIndex += modValue;
						tableComposite.getTableViewer().getTable().setSelection(selectedIndex);
				
					} else {
					
						return;
					}
					
					}
				}
				
			}
		
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
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

}
