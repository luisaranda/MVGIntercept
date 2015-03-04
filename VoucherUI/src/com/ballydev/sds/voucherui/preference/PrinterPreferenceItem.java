/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.composite.SlipPrinterPreferenceComposite;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.controller.PrinterPreferenceController;
import com.ballydev.sds.voucherui.form.PrinterPreferenceForm;

/**
 * @author Nithya kalyani
 * @version $Revision: 11$
 *
 */
public class PrinterPreferenceItem extends TouchScreenPreferenceItem{

	/**
	 * PrinterPreferenceComposite instance
	 */
	private SlipPrinterPreferenceComposite composite = null;

	/**
	 * PrinterPreferenceController instance
	 */
	private PrinterPreferenceController controller = null;

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#checkState()
	 */
	@Override
	public void checkState() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			controller = new PrinterPreferenceController(parent,SWT.NONE,new PrinterPreferenceForm(),getSiteTableDescriptor());
			composite = (SlipPrinterPreferenceComposite) controller.getComposite();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return composite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#getName()
	 */
	@Override
	public String getName() {		
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_PRINTER_TITLE);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {	
		super.performDefaults();
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			if(controller.getForm().getLstPrinter().size()>0){
				SDSPreferenceStore.setStoreValue(IVoucherConstants.PRINTER_VOU_PREFERENCE,controller.getForm().getLstPrinter().get(0).toString());
				controller.getTableComposite().getTableViewer().getTable().select(0);
				controller.getForm().setSelectedPrinter(controller.getForm().getLstPrinter().get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			ProgressIndicatorUtil.closeInProgressWindow();
		}


	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performOk()
	 */
	@Override
	public boolean performOk() {
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			SDSPreferenceStore.setStoreValue(IVoucherConstants.PRINTER_VOU_PREFERENCE,controller.getForm().getSelectedPrinter());				
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			ProgressIndicatorUtil.closeInProgressWindow();
		}

		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performApply()
	 */
	@Override
	public void performApply() {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			if(composite.getTableViewer().getTable().getSelection() != null 
					&& composite.getTableViewer().getTable().getSelection().length > 0){
				SDSPreferenceStore.setStoreValue(IVoucherConstants.PRINTER_VOU_PREFERENCE, composite.getTableViewer().getTable().getSelection()[0].getText());
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
			}
			else
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_SELECT_PRINTER));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}

	/**
	 * Method to get the SiteTableDescriptor
	 * @return
	 */
	public TableDescriptor getSiteTableDescriptor(){
		TableDescriptor siteTableDescriptor = new TableDescriptor(null, 
				SWT.FULL_SELECTION | SWT.VIRTUAL| SWT.BORDER, "tableSite", "lstPrinter","selectedPrinter");
		siteTableDescriptor.setContentProvider(new SiteDetailsTableContentProvider());
		siteTableDescriptor.setLabelProvider(new SiteDetailsTableLabelProvider());

		TableColumnDescriptor siteNameColumn = new TableColumnDescriptor(SWT.NONE, .1, "Printer");
		List<TableColumnDescriptor> listOfColumns =  new ArrayList<TableColumnDescriptor>();
		listOfColumns.add(siteNameColumn);
		siteTableDescriptor.setTableColsDescriptors(listOfColumns);
		return siteTableDescriptor; 
	}

	/**
	 * SiteDetailsTableContentProvider class
	 * 
	 */
	class SiteDetailsTableContentProvider implements IStructuredContentProvider{

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof ArrayList){
				return ((ArrayList)inputElement).toArray();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}


	/**
	 *SiteDetailsTableLabelProvider class
	 *
	 */
	class SiteDetailsTableLabelProvider implements ITableLabelProvider, IFontProvider{

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String){
				return (String)element;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
		 */
		public Font getFont(Object element) {
			Font font = SDSControlFactory.getStandardTouchScreenFont();
			if(Util.isSmallerResolution()) {
				font = SDSControlFactory.getSmallTouchScreenFont();
			}
			return font;
		}

	}

	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}		
		return true;
	}

}
