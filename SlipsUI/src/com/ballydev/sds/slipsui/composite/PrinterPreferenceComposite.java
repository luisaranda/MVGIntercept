/**
 * 
 */
package com.ballydev.sds.slipsui.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.constant.IImageConstants;
import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTableViewer;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.ImageConstants;

/**
 * @author gsrinivasulu
 *
 */
public class PrinterPreferenceComposite extends Composite {

	/**
	 * touchScreenTableGroup instance
	 */
	private Composite touchScreenTableGroup;

	/**
	 * tableViewer instance
	 */
	private SDSTableViewer tableViewer;

	/**
	 * btnPrevious instance
	 */
	private SDSButton btnPrevious;

	/**
	 * btnNext instance
	 */
	private SDSButton btnNext;

	/**
	 * tableDescriptor instance
	 */
	private TableDescriptor tableDescriptor;
	
	/**
	 * PreferenceLogOut Label instance
	 */
	private SDSTSLabel lblPrefLogOutMsg;	

	/**
	 * constructor
	 * @param parent
	 * @param style
	 * @param tableDescriptor
	 */
	public PrinterPreferenceComposite(Composite parent, int style, TableDescriptor tableDescriptor) {
		super(parent, style);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.tableDescriptor = tableDescriptor;
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * initialize method
	 */
	private void initialize() {

		createGroup();
		createContents();
		
			
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 5;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;
		gridLayout1.horizontalSpacing = 5;
		setLayout(gridLayout1);
		setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}

	/**
	 * creates main group
	 */
	private void createGroup() {
		touchScreenTableGroup = new Composite(this, SWT.NONE);
		touchScreenTableGroup.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		touchScreenTableGroup.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.CENTER;
		touchScreenTableGroup.setLayoutData(gridData);

	}


	/**
	 * creates contents
	 */
	private void createContents() {

		
		if (tableDescriptor != null){
			
			tableDescriptor.setParent(touchScreenTableGroup);
			tableDescriptor.setHeaderVisible(true);
			tableDescriptor.setLinesVisible(true);
			tableViewer = Util.createTable(tableDescriptor);
			tableViewer.getControl().addListener(SWT.MeasureItem,
					new Listener() {
				public void handleEvent(Event event) {
					int clientWidth = tableViewer.getTable().getClientArea().width;
					event.height = 44;
					event.width = clientWidth * 2;
				}

			});
			Table table = tableViewer.getTable();
			GridData gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.verticalAlignment = GridData.CENTER;
			gridData.heightHint = 180;
			gridData.widthHint = 300;
			gridData.horizontalSpan = 2;
			table.setLayoutData(gridData);
			table.getColumn(0).setWidth(315);
			
			if(Util.isSmallerResolution()) {
				gridData.heightHint = 200;
				gridData.widthHint = 300;
				table.getColumn(0).setWidth(226);
			}
			
			List<TableColumnDescriptor> listOfColumns = tableDescriptor.getTableColsDescriptors();
			for (int i=0;i < listOfColumns.size();i++) {
				listOfColumns.get(i).getTableColumn().setResizable(false);
			}
			
			
			btnPrevious = new SDSButton(touchScreenTableGroup, SWT.NONE,"","previous");
			
			String previousImage = ImageConstants.DISPLAY_PREVIOUS_ARROW;
			String nextImage = ImageConstants.DISPLAY_NEXT_ARROW;
			if(Util.isSmallerResolution()) {
				previousImage = ImageConstants.IMAGE_S_TOUCH_SCREEN_PREV_ARROW;
				nextImage = ImageConstants.IMAGE_S_TOUCH_SCREEN_NEXT_ARROW;
				
			}
			
			btnPrevious.setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(previousImage)));
			btnPrevious.setFont(SDSControlFactory.getStandardTouchScreenFont());
			GridData gridData2 = new GridData();
			gridData2.grabExcessVerticalSpace = true;
			gridData2.heightHint = 50;
			gridData2.widthHint = 55;
			gridData2.grabExcessHorizontalSpace = true;
			btnPrevious.setLayoutData(gridData2);
			
			btnNext = new SDSButton(touchScreenTableGroup, SWT.NONE,"","next");
			btnNext.setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(nextImage)));
			
			btnNext.setFont(SDSControlFactory.getStandardTouchScreenFont());
			GridData gridData11 = new GridData();
			gridData11.grabExcessVerticalSpace = true;
			gridData11.horizontalAlignment = GridData.END;
			gridData11.verticalAlignment = GridData.CENTER;
			gridData11.heightHint = 50;
			gridData11.widthHint = 55;
			gridData11.grabExcessHorizontalSpace = true;
			btnNext.setLayoutData(gridData11);
			
			if(Util.isSmallerResolution()) {
				gridData2.heightHint = 40;
				gridData2.widthHint = 45;
				gridData11.heightHint = 40;
				gridData11.widthHint = 45;
				
			}
			
			GridData gdPrefLogOut = new GridData();
			gdPrefLogOut.grabExcessHorizontalSpace = true;
			lblPrefLogOutMsg = new SDSTSLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SLIP_PRINTER_PERF_CANNOT_BE_SET_AFTER_LOGIN));
			lblPrefLogOutMsg.setLayoutData(gdPrefLogOut);
		}
	}
	
	
	/**
	 * @return
	 */
	public TableDescriptor getSiteTableDescriptor(){
		TableDescriptor siteTableDescriptor = new TableDescriptor(null, 
				SWT.FULL_SELECTION | SWT.VIRTUAL| SWT.BORDER, "tableSite", "lstPrinter", "selectedSiteName");
		siteTableDescriptor.setContentProvider(new SiteDetailsTableContentProvider());
		siteTableDescriptor.setLabelProvider(new SiteDetailsTableLabelProvider());

		TableColumnDescriptor siteNameColumn = new TableColumnDescriptor(SWT.NONE, .1, "Site");
		List<TableColumnDescriptor> listOfColumns =  new ArrayList<TableColumnDescriptor>();
		listOfColumns.add(siteNameColumn);
		siteTableDescriptor.setTableColsDescriptors(listOfColumns);
		return siteTableDescriptor; 
	}
	/**
	 * 
	 *SiteDetailsTableContentProvider
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
	 * SiteDetailsTableLabelProvider
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
			return SDSControlFactory.getStandardTouchScreenFont();
		}

	}
	
	/**
	 *  Method to disable the controls
	 */
	public void disableControls(){		
		btnNext.setEnabled(false);
		btnPrevious.setEnabled(false);
	}

	/**
	 * @return
	 */
	public TableDescriptor getTableDescriptor() {
		return tableDescriptor;
	}

	/**
	 * @param tableDescriptor
	 */
	public void setTableDescriptor(TableDescriptor tableDescriptor) {
		this.tableDescriptor = tableDescriptor;
	}

	/**
	 * @return
	 */
	public SDSTableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * @param tableViewer
	 */
	public void setTableViewer(SDSTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	/**
	 * @return the btnNext
	 */
	public SDSButton getBtnNext() {
		return btnNext;
	}

	/**
	 * @param btnNext the btnNext to set
	 */
	public void setBtnNext(SDSButton btnNext) {
		this.btnNext = btnNext;
	}

	
	/**
	 * @param btnPrevious the btnPrevious to set
	 */
	public void setBtnPrevious(SDSButton btnPrevious) {
		this.btnPrevious = btnPrevious;
	}

	/**
	 * @return the btnPrevious
	 */
	public SDSButton getBtnPrevious() {
		return btnPrevious;
	}

	/**
	 * @return the touchScreenTableGroup
	 */
	public Composite getTouchScreenTableGroup() {
		return touchScreenTableGroup;
	}

	/**
	 * @param touchScreenTableGroup the touchScreenTableGroup to set
	 */
	public void setTouchScreenTableGroup(Composite touchScreenTableGroup) {
		this.touchScreenTableGroup = touchScreenTableGroup;
	}

} 