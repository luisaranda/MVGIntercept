/**
 * 
 */
package com.ballydev.sds.slipsui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.controller.PrinterPreferenceController;
import com.ballydev.sds.slipsui.form.PrinterPreferenceForm;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * @author gsrinivasulu
 * @version $Revision :1$
 */
public class SlipPrinterPreferenceItem extends TouchScreenPreferenceItem {

	/**
	 * SlipPrinterPreferenceComposite instance
	 */
	private SlipPrinterPreferenceComposite composite = null;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#checkState()
	 */
	@Override
	public void checkState() {
		
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			PrinterPreferenceController controller = new PrinterPreferenceController(parent,SWT.NONE,new PrinterPreferenceForm(),getSiteTableDescriptor());
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
		
		return LabelLoader.getLabelValue(LabelKeyConstants.PRINTER_PREF);
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performApply()
	 */
	@Override
	public void performApply() {
		ProgressIndicatorUtil.openInProgressWindow();
			try {
				
				if(!Util.isEmpty(SDSApplication.getLoggedInUserID()) && !(composite.isDisposed())){					
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SLIP_PRINTER_PERF_CANNOT_BE_SET_AFTER_LOGIN));
					return;					
				}else if(Util.isEmpty(SDSApplication.getLoggedInUserID()))  {				
					SDSPreferenceStore.setStoreValue(PrinterConstants.PREFERENCE_PRINTER, composite.getTableViewer().getTable().getSelection()[0].getText());
				}
				
			} catch (Exception e) {				
				log.error("Exception in performApply() of PrinterPreferenceItem",e);
			}
			finally
			{
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

		TableColumnDescriptor siteNameColumn = new TableColumnDescriptor(SWT.NONE, .1, LabelLoader.getLabelValue(LabelKeyConstants.PRINTER_PREF));
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

	
	/** 
	 * Method to enable / disable the controls after logging in
	 */
	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}
		return true;
	}
	
}
