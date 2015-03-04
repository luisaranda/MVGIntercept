/**
 * 
 */
package com.ballydev.sds.slipsui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.composite.SlipPrinterPreferenceComposite;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.form.PrinterPreferenceForm;
import com.ballydev.sds.slipsui.print.PrinterUtil;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * @author gsrinivasulu
 *
 */
public class PrinterPreferenceController extends SDSBaseController {

	//private PrinterPreferenceComposite tableComposite;
	
	private SlipPrinterPreferenceComposite tableComposite;

	private PrinterPreferenceForm form;

	private int totalRecordCount = 0;

	private int numOfPages = 0;

	private int numOfRecords = 4;

	private int modValue = 0;

	private int initialIndex = 0;

	private int pageCount = 0;

	private boolean previousClicked = false;

	private List<String> pageList = new ArrayList<String>();

	private List<String> listOfAllAvailablePrinters = null;

	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
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
		String prefPrinter=SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
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
		PrinterPreferenceMouseListener printerPreferenceMouseListener = new PrinterPreferenceMouseListener();
		if(tableComposite!=null) {
			tableComposite.getBtnPrevious().addMouseListener(printerPreferenceMouseListener);
			tableComposite.getBtnPrevious().addTraverseListener(this);
			tableComposite.getBtnNext().addMouseListener(printerPreferenceMouseListener);
			tableComposite.getBtnNext().addTraverseListener(this);
		}
	}

	
	private class PrinterPreferenceMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {			
			
		}

		public void mouseDown(MouseEvent e) {
			if (!(Util.isEmpty(SDSApplication.getLoggedInUserID()))) {		
				return;
			}
			if (e.getSource() instanceof TSButtonLabel) {
				TSButtonLabel button = (TSButtonLabel) e.getSource();
				String selectedPrinter = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
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
