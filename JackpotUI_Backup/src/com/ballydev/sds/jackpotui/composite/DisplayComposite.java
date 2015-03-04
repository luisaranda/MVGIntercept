/*****************************************************************************
 * $Id: DisplayComposite.java,v 1.18.1.0, 2011-04-16 19:57:57Z, SDS12.3.2CheckinUser$
 * $Date: 4/16/2011 2:57:57 PM$
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
package com.ballydev.sds.jackpotui.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTableViewer;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.DataFieldComparator;
import com.ballydev.sds.jackpotui.util.DataFieldType;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;

/**
 * This composite is used to display all the pending jackpot slips
 * 
 * @author vijayrajm
 * @version $Revision : 55$
 */
public class DisplayComposite extends TouchScreenBaseComposite {

	/**
	 * Display Table Viewer instance
	 */
	private SDSTableViewer dispJackpotTableViewer = null; // @jve:decl-index=0:

	/**
	 * Display Table Composite instance
	 */
	private Composite dispTableComposite = null;

	/**
	 * Display Previous Button instance
	 */
	private TSButtonLabel butNext = null;

	/**
	 * Display Next Button instance
	 */
	private TSButtonLabel butPrevious = null;

	/**
	 * TotalPageCount Label instance
	 */
	private CbctlLabel totalPageCount = null;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME); // @jve:decl-index=0:

	/**
	 * ToFirstPage button instance
	 */
	private TSButtonLabel btnToFirstPage = null;

	/**
	 * ToLastPage button instance
	 */
	private TSButtonLabel btnToLastPage = null;
	
	private String imgDispPrevArrow;
	
	private String imgDispNextArrow;
	
	private String imgFirstPageBtn;
	
	private String imgLastPageBtn;
	
	private String imgDisableDispPrevArrow;
	
	private String imgDisableDispNextArrow;
	
	private String imgDisableFirstPageBtn;
	
	private String imgDisableLastPageBtn;
	
	private Image firstImage;
	
	private Image prevImage;
	
	private Image nextImage;
	
	private Image lastImage;
	
	private Image disableFirstImage;
	
	private Image disablePrevImage;
	
	private Image disableNextImage;
	
	private Image disableLastImage;

	/**
	 * DisplayComposite constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public DisplayComposite(Composite parent, int style) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.DISPLAY_JP_HEADING));
		if (Util.isSmallerResolution()) {
			imgDispPrevArrow = ImageConstants.S_DISPLAY_PREVIOUS_ARROW;
			imgDispNextArrow = ImageConstants.S_DISPLAY_NEXT_ARROW;
			imgFirstPageBtn = ImageConstants.S_TO_FIRST_PAGE_BUTTON_IMG;
			imgLastPageBtn = ImageConstants.S_TO_LAST_PAGE_BUTTON_IMG;
			imgDisableDispPrevArrow = ImageConstants.S_DISABLE_DISPLAY_PREVIOUS_ARROW;
			imgDisableDispNextArrow = ImageConstants.S_DISABLE_DISPLAY_NEXT_ARROW;
			imgDisableFirstPageBtn = ImageConstants.S_DISABLE_TO_FIRST_PAGE_BUTTON_IMG;
			imgDisableLastPageBtn = ImageConstants.S_DISABLE_TO_LAST_PAGE_BUTTON_IMG;
		}
		else {
			imgDispPrevArrow = ImageConstants.DISPLAY_PREVIOUS_ARROW;
			imgDispNextArrow = ImageConstants.DISPLAY_NEXT_ARROW;
			imgFirstPageBtn = ImageConstants.TO_FIRST_PAGE_BUTTON_IMG;
			imgLastPageBtn = ImageConstants.TO_LAST_PAGE_BUTTON_IMG;
			imgDisableDispPrevArrow = ImageConstants.DISABLE_DISPLAY_PREVIOUS_ARROW;
			imgDisableDispNextArrow = ImageConstants.DISABLE_DISPLAY_NEXT_ARROW;
			imgDisableFirstPageBtn = ImageConstants.DISABLE_TO_FIRST_PAGE_BUTTON_IMG;
			imgDisableLastPageBtn = ImageConstants.DISABLE_TO_LAST_PAGE_BUTTON_IMG;
		}
		
		firstImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgFirstPageBtn));
		prevImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDispPrevArrow));
		nextImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDispNextArrow));
		lastImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgLastPageBtn));
		disableFirstImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableFirstPageBtn));
		disableLastImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableLastPageBtn));
		disablePrevImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispPrevArrow));
		disableNextImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispNextArrow));
		initialize("C");
		layout();
		
		

	}

	
	public void drawMiddleComposite() {
		
		getMiddleComposite().setGdMiddleComp(getMiddleComposite().getGridDataForTableCmp());
		getMiddleComposite().setGlMiddleCom(getMiddleComposite().getLayoutForTableCmp());
		getMiddleComposite().setLayoutDataForMiddle();
		createTableComposite();
	}
	
	
	/**
	 * A method to initialize displayGroup
	 * 
	 */
	private void createTableComposite() {
		
		createDispTableComposite();
		
		GridData gdNavComp = new GridData();
		gdNavComp.grabExcessHorizontalSpace = true;
		gdNavComp.verticalAlignment = GridData.CENTER;
		gdNavComp.heightHint = firstImage.getBounds().height + 10;
		if(Util.isSmallerResolution()){
			gdNavComp.widthHint = 9 *  firstImage.getBounds().width;
		}else{
			gdNavComp.widthHint = 6 *  firstImage.getBounds().width + 17;
		}
		gdNavComp.grabExcessVerticalSpace = false;
		gdNavComp.horizontalAlignment = GridData.END;
		gdNavComp.horizontalSpan = 5;
		
		GridLayout glNavComp = new GridLayout();
		glNavComp.marginWidth = 0;
		glNavComp.marginHeight = 0;
		glNavComp.numColumns = 5;
		
		Composite navComposite = new Composite(getMiddleComposite(), SWT.NONE);
		navComposite.setLayout(glNavComp);
		navComposite.setLayoutData(gdNavComp);
		
		GridData gdFirstImage = new GridData();
		gdFirstImage.widthHint = firstImage.getBounds().width;
		gdFirstImage.heightHint = firstImage.getBounds().height;
		gdFirstImage.horizontalAlignment = GridData.END;
		
		GridData gdPrevImage = new GridData();
		gdPrevImage.widthHint = prevImage.getBounds().width;
		gdPrevImage.heightHint = prevImage.getBounds().height;
		gdPrevImage.horizontalAlignment = GridData.END;
		
		GridData gdPage = new GridData();
		gdPage.widthHint = 80;
		gdPage.horizontalAlignment = GridData.END;
		
		GridData gdNextImage = new GridData();
		gdNextImage.widthHint = nextImage.getBounds().width;
		gdNextImage.heightHint = nextImage.getBounds().height;
		gdNextImage.horizontalAlignment = GridData.END;
		
		GridData gdLastImage = new GridData();
		gdLastImage.widthHint = lastImage.getBounds().width;
		gdLastImage.heightHint = lastImage.getBounds().height;
		gdLastImage.horizontalAlignment = GridData.END;
		
		btnToFirstPage = new TSButtonLabel(navComposite, SWT.NONE, "toFirstPage");
		btnToFirstPage.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgFirstPageBtn)));
		btnToFirstPage.setLayoutData(gdFirstImage);
		btnToFirstPage.setBackground(SDSControlFactory.getTSBodyColor());
		
		butPrevious = new TSButtonLabel(navComposite, SWT.NONE, "previous");
		butPrevious.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDispPrevArrow)));
		butPrevious.setLayoutData(gdPrevImage);
		butPrevious.setBackground(SDSControlFactory.getTSBodyColor());
		
		totalPageCount = new CbctlLabel(navComposite, SWT.NONE);
		totalPageCount.setText(LabelLoader
				.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT));
		totalPageCount.setLayoutData(gdPage);

		butNext = new TSButtonLabel(navComposite, SWT.NONE, "next");
		butNext.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDispNextArrow)));
		butNext.setLayoutData(gdNextImage);
		butNext.setBackground(SDSControlFactory.getTSBodyColor());
		
		btnToLastPage = new TSButtonLabel(navComposite, SWT.NONE, "toLastPage");
		btnToLastPage.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgLastPageBtn)));
		btnToLastPage.setLayoutData(gdLastImage);
		btnToLastPage.setBackground(SDSControlFactory.getTSBodyColor());
	}

	/**
	 * This inner class is used to retrieve all the rows from the
	 * TableDescriptor
	 * 
	 */
	private class DispJackpotContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
		}

		public void dispose() {
			

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			

		}

	}

	/**
	 * This inner class is used to set the column values of the TableDescriptor
	 * 
	 */
	private class DispJackpotLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JackpotDTO jackpotDTO = (JackpotDTO) element;
			switch (columnIndex) {

			case 0: {
				//Timestamp localTimestamp=null;
				//localTimestamp = DateUtil.getLocalTimeFromUTC(jackpotDTO.getTransactionDate());
				return com.ballydev.sds.framework.util.DateUtil.getFormattedDate(jackpotDTO.getTransactionDate());
			}
			case 1:
				// String s= ((Long) jackpotDTO.getSequenceNumber()).toString();
				// return "J"+PadderUtil.leftPad(s, 5, '0');
				return ((Long) jackpotDTO.getSequenceNumber()).toString();
			case 2:
				return StringUtil.trimAcnfNo(jackpotDTO.getAssetConfigNumber());
			case 3:
				return jackpotDTO.getAssetConfigLocation().toString();
			case 4:

				long amount = 0;
				//short blindAttempts = 0;
				try {

					amount = jackpotDTO.getOriginalAmount();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
//				try {
//					if (jackpotDTO.getBlindAttempt() != null) {
//						blindAttempts = jackpotDTO.getBlindAttempt();
//					}
//				} catch (RuntimeException e) {
//					e.printStackTrace();
//				}
				
				long expressJPlimit=0;
				expressJPlimit=(long)Double.parseDouble(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
				expressJPlimit=ConversionUtil.dollarToCentsRtnLong(String.valueOf(expressJPlimit));
				
				
				if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED)
						.equalsIgnoreCase("yes")
						&& amount < expressJPlimit){
						//&& blindAttempts < 2) {//NJ REGULATORY ENHANCEMENT CHANGES
					
					return MainMenuController.jackpotForm.getSiteCurrencySymbol() 
							+ ConversionUtil.maskAmount();
				} else {
					return MainMenuController.jackpotForm.getSiteCurrencySymbol()
							+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(amount));
				}
			case 5: {
				if (jackpotDTO.getSlotAttendantFirstName() != null && jackpotDTO.getSlotAttendantLastName()!=null) {
					return jackpotDTO.getSlotAttendantFirstName()+ " " + jackpotDTO.getSlotAttendantLastName();
				} else if (jackpotDTO.getSlotAttendantFirstName() != null){
					return jackpotDTO.getSlotAttendantFirstName();
				}else if (jackpotDTO.getSlotAttendantLastName() != null){
					return jackpotDTO.getSlotAttendantLastName();
				}
				}
			default:
				return "";
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
		 *      java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			
		}

	}

	/**
	 * A method to initialize dispTableComposite and create SDSTableViewer
	 * 
	 */
	private void createDispTableComposite() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 0;
		fillLayout.type = org.eclipse.swt.SWT.HORIZONTAL;
		GridLayout layout = new GridLayout();		
		layout.verticalSpacing= GridData.FILL;
		layout.horizontalSpacing= GridData.FILL;
		
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 5;
		gridData1.widthHint = 600;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessVerticalSpace = false;
		if(Util.isSmallerResolution()){
			gridData1.verticalIndent = 30;
			gridData1.heightHint = 204;
		}else{
			gridData1.verticalIndent = 40;
			gridData1.heightHint = 200;
		}

		dispTableComposite = new Composite(getMiddleComposite(), SWT.BORDER);
		dispTableComposite.setLayout(fillLayout);
		//dispTableComposite.setLayout(layout);
		dispTableComposite.setLayoutData(gridData1);

		/* TableDescriptor for SDSTableViewer */
		TableDescriptor dispJackpotTblDescriptor = new TableDescriptor(
				dispTableComposite, SWT.FULL_SELECTION, "dispJackpotDTO",
				"dispJackpotDTO");
		dispTableComposite.setBackground(SDSControlFactory.getDefaultTouchScreenBackGround());
		dispJackpotTblDescriptor.setHeaderVisible(true);
		dispJackpotTblDescriptor.setLinesVisible(true);
		
		/*
		 * Rectangle screenBounds =
		 * ParentComposite.getCurrentComposite().getBounds(); // Rectangle
		 * tableCompositeBounds =
		 * dispJackpotTblDescriptor.getParent().getBounds(); log.debug("Table
		 * composite x ="+screenBounds.x+"y = " +screenBounds.y+"width
		 * ="+screenBounds.width+ "height ="+screenBounds.height); double width =
		 * (double)screenBounds.width; width = (width / 10000);
		 */

		ArrayList<TableColumnDescriptor> arrayListTblColDesc = new ArrayList<TableColumnDescriptor>();

		TableColumnDescriptor columnDescriptor = new TableColumnDescriptor(
				SWT.RIGHT, .14, LabelLoader
						.getLabelValue(LabelKeyConstants.DATE_OR_TIME));
		columnDescriptor.setDefaultSortColumn(true);
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.DATE));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .14,
				LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER));
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.SEQUENCE_NO));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .16,
				LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NO));
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.SLOT_NO));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .11, LabelLoader
				.getLabelValue(LabelKeyConstants.STAND_NUMBER));
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.STAND_NO));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(
				SWT.RIGHT,
				.12,
				LabelLoader
						.getLabelValue(LabelKeyConstants.EXPRESS_JP_AMOUNT_LABEL));
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.JACKPOT_AMOUNT));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .12, LabelLoader
				.getLabelValue(LabelKeyConstants.EMPLOYEE_NAME));
		columnDescriptor.setComparator(new DataFieldComparator(DataFieldType.EMPLOYEE_NAME));
		arrayListTblColDesc.add(columnDescriptor);

		dispJackpotTblDescriptor.setTableColsDescriptors(arrayListTblColDesc);
		dispJackpotTblDescriptor
				.setContentProvider(new DispJackpotContentProvider());
		dispJackpotTblDescriptor
				.setLabelProvider(new DispJackpotLabelProvider());

		/* Creating SDSTableViewer */
		dispJackpotTableViewer = Util.createTable(dispJackpotTblDescriptor);
		// Table dispJackpotTable = dispJackpotTblDescriptor.getTable().set
		dispJackpotTableViewer.getControl().addListener(SWT.MeasureItem,
				new Listener() {
					public void handleEvent(Event event) {
						int clientWidth = dispJackpotTableViewer.getTable()
								.getClientArea().width;
						event.height = event.gc.getFontMetrics().getHeight() * 2;
						event.width = clientWidth * 2;
					}

				});

		/* Setting the column width as not resizable */
		for (int i = 0; i < arrayListTblColDesc.size(); i++) {

			dispJackpotTblDescriptor.getTableColsDescriptors().get(i)
					.getTableColumn().setResizable(false);
		}

	}

	/**
	 * @return the dispJackpotTableViewer
	 */
	public SDSTableViewer getDispJackpotTableViewer() {
		return dispJackpotTableViewer;
	}

	/**
	 * @param dispJackpotTableViewer
	 *            the dispJackpotTableViewer to set
	 */
	public void setDispJackpotTableViewer(SDSTableViewer dispJackpotTableViewer) {
		this.dispJackpotTableViewer = dispJackpotTableViewer;
	}

	/**
	 * @return the butNext
	 */
	public TSButtonLabel getButNext() {
		return butNext;
	}

	/**
	 * @param butNext
	 *            the butNext to set
	 */
	public void setButNext(TSButtonLabel butNext) {
		this.butNext = butNext;
	}

	/**
	 * @return the butPrevious
	 */
	public TSButtonLabel getButPrevious() {
		return butPrevious;
	}

	/**
	 * @param butPrevious
	 *            the butPrevious to set
	 */
	public void setButPrevious(TSButtonLabel butPrevious) {
		this.butPrevious = butPrevious;
	}

	/**
	 * @return the dispTableComposite
	 */
	public Composite getDispTableComposite() {
		return dispTableComposite;
	}

	/**
	 * @param dispTableComposite
	 *            the dispTableComposite to set
	 */
	public void setDispTableComposite(Composite dispTableComposite) {
		this.dispTableComposite = dispTableComposite;
	}

	/**
	 * @return the totalPageCount
	 */
	public CbctlLabel getTotalPageCount() {
		return totalPageCount;
	}

	/**
	 * @param totalPageCount
	 *            the totalPageCount to set
	 */
	public void setTotalPageCount(CbctlLabel totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	/**
	 * @return the btnToFirstPage
	 */
	public TSButtonLabel getBtnToFirstPage() {
		return btnToFirstPage;
	}

	/**
	 * @param btnToFirstPage
	 *            the btnToFirstPage to set
	 */
	public void setBtnToFirstPage(TSButtonLabel btnToFirstPage) {
		this.btnToFirstPage = btnToFirstPage;
	}

	/**
	 * @return the btnToLastPage
	 */
	public TSButtonLabel getBtnToLastPage() {
		return btnToLastPage;
	}

	/**
	 * @param btnToLastPage
	 *            the btnToLastPage to set
	 */
	public void setBtnToLastPage(TSButtonLabel btnToLastPage) {
		this.btnToLastPage = btnToLastPage;
	}


	public Image getFirstImage() {
		return firstImage;
	}


	public void setFirstImage(Image firstImage) {
		this.firstImage = firstImage;
	}


	public Image getPrevImage() {
		return prevImage;
	}


	public void setPrevImage(Image prevImage) {
		this.prevImage = prevImage;
	}


	public Image getNextImage() {
		return nextImage;
	}


	public void setNextImage(Image nextImage) {
		this.nextImage = nextImage;
	}


	public Image getLastImage() {
		return lastImage;
	}


	public void setLastImage(Image lastImage) {
		this.lastImage = lastImage;
	}


	public Image getDisableFirstImage() {
		return disableFirstImage;
	}


	public void setDisableFirstImage(Image disableFirstImage) {
		this.disableFirstImage = disableFirstImage;
	}


	public Image getDisablePrevImage() {
		return disablePrevImage;
	}


	public void setDisablePrevImage(Image disablePrevImage) {
		this.disablePrevImage = disablePrevImage;
	}


	public Image getDisableNextImage() {
		return disableNextImage;
	}


	public void setDisableNextImage(Image disableNextImage) {
		this.disableNextImage = disableNextImage;
	}


	public Image getDisableLastImage() {
		return disableLastImage;
	}


	public void setDisableLastImage(Image disableLastImage) {
		this.disableLastImage = disableLastImage;
	}
} // @jve:decl-index=0:visual-constraint="106,34"
