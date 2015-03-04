/*****************************************************************************
 * $Id: AreaPreferenceComposite.java,v 1.3, 2010-02-02 10:24:12Z, Anbuselvi, Balasubramanian$
 * $Date: 2/2/2010 4:24:12 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Gaming Inc.  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui.composite;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSListViewer;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.controls.CbctlButton;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.slipsui.util.AreaListComparator;


/**
 * Slips Area Preference Composite
 * @author dambereen
 * @version $Revision: 4$
 */
public class AreaPreferenceComposite extends Composite implements LabelKeyConstants{

	/**
	 * Available Area SDSListViewer instance
	 */
	private SDSListViewer lstVwrSlipAreaAvailable=null;

	/**
	 * Selected Area SDSListViewer instance
	 */
	private SDSListViewer lstVwrSlipAreaSelected=null;

	/**
	 * Available Area List instance
	 */
	private List lstSlipAreaAvailable=null;

	/**
	 * Selected Area List instance
	 */
	private List lstSlipAreaSelected=null;

	/**
	 * PrefRight CbctlButton instance
	 */
	private CbctlButton btnPrefRight=null;

	/**
	 * PrefLeft CbctlButton instance
	 */
	private CbctlButton btnPrefLeft=null;

	/**
	 * Available Areas CbctlLabel instance
	 */
	private CbctlLabel lblAvailabelArea=null;

	/**
	 * SelectedAreas CbctlLabel instance
	 */
	private CbctlLabel lblSelectedArea=null;

	/**
	 * PreferenceLogOut Label instance
	 */
	private SDSTSLabel lblPrefLogOutMsg;
	
	private Composite grpComposite ;

	/**
	 * AreaPreferenceComposite constructor
	 * @param parent
	 * @param style
	 */
	public AreaPreferenceComposite(Composite parent, int style) {
		super(parent, style);
		initialize();

	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		createAreaGroup();
		GridData gridData12 = new GridData();
		gridData12.horizontalSpan=2;
		gridData12.verticalIndent=10;
		gridData12.horizontalAlignment = GridData.CENTER;    

		GridLayout gridLayout = new GridLayout();
		//gridLayout.numColumns = 3;
		
		lblPrefLogOutMsg = new SDSTSLabel(this, SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AREA_PERF_CAN_BE_SET_AFTER_LOGIN));
		lblPrefLogOutMsg.setGridData(gridData12);

		this.setLayout(gridLayout);
		this.setSize(new Point(300, 200));		
	}


	private void createAreaGroup() {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 25;
		
		
		grpComposite = new Composite(this,SWT.None);
		grpComposite.setLayout(gridLayout);
		
		GridData gridData = new GridData();
		gridData.horizontalSpan=2;               
		GridData gridData3 = new GridData(); 
		gridData3.horizontalSpan=2;  
		GridData gridData5 = new GridData();
		gridData5.horizontalSpan=2;

		GridData gridData6 = new GridData();
		gridData6.horizontalSpan=2;
		// gridData6.verticalIndent=10;
		gridData6.verticalAlignment = GridData.CENTER;

		GridData gridData7 = new GridData();
		gridData7.verticalAlignment =  GridData.CENTER;

		GridData gridData8 = new GridData();
		gridData8.verticalSpan=2;
		gridData8.horizontalAlignment = GridData.END;
		gridData8.widthHint=120;
		gridData8.heightHint=350; 

		GridData gdlstVwrSlipAreaAvailable = new GridData();
		gdlstVwrSlipAreaAvailable.verticalSpan = 2;
		gdlstVwrSlipAreaAvailable.widthHint= 120;
		gdlstVwrSlipAreaAvailable.heightHint = 350;
		gdlstVwrSlipAreaAvailable.horizontalAlignment = GridData.END;

		GridData gridData9 = new GridData(); 
		gridData9.horizontalAlignment = GridData.CENTER;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.verticalAlignment = GridData.END;
		gridData9.verticalIndent=65;
		gridData9.heightHint = 40;
		gridData9.widthHint = 80;

		GridData gridData10 = new GridData();
		gridData10.verticalSpan=2;
		gridData10.verticalAlignment = GridData.CENTER;
		gridData10.horizontalAlignment = GridData.CENTER;
		gridData10.widthHint=120;

		GridData gdlstVwrSlipAreaSelected = new GridData();
		gdlstVwrSlipAreaSelected.verticalSpan=2;
		gdlstVwrSlipAreaSelected.verticalAlignment = GridData.CENTER;
		gdlstVwrSlipAreaSelected.horizontalAlignment = GridData.CENTER;
		gdlstVwrSlipAreaSelected.widthHint=120;


		if(Util.isSmallerResolution()){
			gdlstVwrSlipAreaSelected.heightHint= 220;
			gdlstVwrSlipAreaSelected.verticalAlignment = GridData.BEGINNING;
			gdlstVwrSlipAreaSelected.horizontalAlignment = GridData.CENTER;
			gdlstVwrSlipAreaSelected.horizontalIndent = -13;

			gdlstVwrSlipAreaAvailable.heightHint = 220;
			gdlstVwrSlipAreaAvailable.verticalAlignment = GridData.BEGINNING;
			gdlstVwrSlipAreaAvailable.horizontalAlignment=  GridData.END;
			gdlstVwrSlipAreaAvailable.horizontalIndent = 60;

			gridData6.horizontalIndent= 80;
			gdlstVwrSlipAreaSelected.widthHint=150;
			gdlstVwrSlipAreaAvailable.widthHint = 120;

		}else{
			gdlstVwrSlipAreaSelected.heightHint= 350;
			gdlstVwrSlipAreaAvailable.heightHint = 350;
			gdlstVwrSlipAreaAvailable.verticalAlignment = GridData.CENTER;

		}
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.BEGINNING;
		gridData11.heightHint = 40;
		gridData11.widthHint = 80;


		lblAvailabelArea=new CbctlLabel(grpComposite,SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.SLIP_PREF_AVAILABLE_AREAS));
		lblAvailabelArea.setLayoutData(gridData6);

		lblSelectedArea=new CbctlLabel(grpComposite,SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.SLIP_PREF_SELECTED_AREAS));
		lblSelectedArea.setLayoutData(gridData7);

		lstVwrSlipAreaAvailable=new SDSListViewer(grpComposite, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.CENTER | SWT.H_SCROLL,"lstVwrAvailableAreasName","lstAllSlipAreaAvailable","lstSelSlipAreaAvailable");
		lstVwrSlipAreaAvailable.setContentProvider(new AreaListContentProvider());
		lstVwrSlipAreaAvailable.setLabelProvider(new AreaListLabelProvider());
		lstVwrSlipAreaAvailable.setComparator(new ViewerComparator(new AreaListComparator()));

		lstSlipAreaAvailable=lstVwrSlipAreaAvailable.getList();
		lstSlipAreaAvailable.setLayoutData(gdlstVwrSlipAreaAvailable);
		lstSlipAreaAvailable.setData("lstVwrAvailableAreasName");
		// lstSlipAreaAvailable.setFont(new Font(Display.getDefault(), "Arial", 14, SWT.NORMAL));

		lstSlipAreaAvailable.setFont(SDSControlFactory.getStandardTouchScreenFont());
		if(Util.isSmallerResolution()) {
			lstSlipAreaAvailable.setFont(SDSControlFactory.getSmallTouchScreenFont());
		}

		btnPrefRight=new CbctlButton(grpComposite,SWT.NONE,LabelLoader.getLabelValue(">"),"BTN_ALERTS_PREF_RIGHT");
		btnPrefRight.setLayoutData(gridData9);

		lstVwrSlipAreaSelected=new SDSListViewer(grpComposite,SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.CENTER | SWT.H_SCROLL,"lstVwrSelectedAreasName","lstAllSlipAreaSelected","lstSelSlipAreaSelected");
		lstVwrSlipAreaSelected.setContentProvider(new AreaListContentProvider());
		lstVwrSlipAreaSelected.setLabelProvider(new AreaListLabelProvider());
		lstVwrSlipAreaSelected.setComparator(new ViewerComparator(new AreaListComparator()));

		lstSlipAreaSelected=lstVwrSlipAreaSelected.getList();
		lstSlipAreaSelected.setLayoutData(gdlstVwrSlipAreaSelected);
		lstSlipAreaSelected.setData("lstVwrSelectedAreasName");
		// lstSlipAreaSelected.setFont(new Font(Display.getDefault(), "Arial", 14, SWT.NORMAL));

		lstSlipAreaSelected.setFont(SDSControlFactory.getStandardTouchScreenFont());
		if(Util.isSmallerResolution()) {
			lstSlipAreaSelected.setFont(SDSControlFactory.getSmallTouchScreenFont());
		}

		btnPrefLeft=new CbctlButton(grpComposite,SWT.NONE,LabelLoader.getLabelValue("<"),"BTN_ALERTS_PREF_LEFT");
		btnPrefLeft.setLayoutData(gridData11);



	}

	public class AreaListContentProvider implements IStructuredContentProvider{

		public Object[] getElements(Object inputElement) {
			return ((ArrayList)inputElement).toArray();
		}

		public void dispose() {


		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {


		}

	}

	public class AreaListLabelProvider implements ILabelProvider,IFontProvider{

		public Image getImage(Object element) {

			return null;
		}

		public String getText(Object element) {
			String tableText="";
			if(element!=null){
				String alertAreaObject=(String)element;
				if(alertAreaObject!=null && !((alertAreaObject.trim()+" ").equalsIgnoreCase(" ")) ){
					tableText=alertAreaObject;
				}
			}
			return tableText;
		}

		public void addListener(ILabelProviderListener listener) {


		}

		public void dispose() {


		}

		public boolean isLabelProperty(Object element, String property) {

			return false;
		}

		public void removeListener(ILabelProviderListener listener) {


		}

		public Font getFont(Object element) {
			Font font = SDSControlFactory.getStandardTouchScreenFont();
			if(Util.isSmallerResolution()) {
				font = SDSControlFactory.getSmallTouchScreenFont();
			}
			return font;
		}

	}



	/**
	 * @param pButton
	 */
	public void setArrowButtonProperties(SDSButton pButton){
		SDSButton sdsButton = pButton;
		sdsButton.setFont(SDSControlFactory.getButtonLabelFont());

		pButton.setForeground(SDSControlFactory.getButtonForegroundColor());	

		GridData gridData = (GridData)pButton.getLayoutData();
		if(gridData==null){
			gridData = new GridData();
		}
		gridData.widthHint = 65;
		gridData.heightHint = 32;
		pButton.setLayoutData(gridData);
	}

	/**
	 * @return
	 */
	public SDSButton getBtnPrefLeft() {
		return btnPrefLeft;
	}

	public SDSButton getBtnPrefRight() {
		return btnPrefRight;
	}

	public List getLstSlipAreaAvailable() {
		return lstSlipAreaAvailable;
	}

	public List getLstSlipAreaSelected() {
		return lstSlipAreaSelected;
	}


	/**
	 * @return the lblAvailabelArea
	 */
	public CbctlLabel getLblAvailabelArea() {
		return lblAvailabelArea;
	}


	/**
	 * @param lblAvailabelArea the lblAvailabelArea to set
	 */
	public void setLblAvailabelArea(CbctlLabel lblAvailabelArea) {
		this.lblAvailabelArea = lblAvailabelArea;
	}


	/**
	 * @return the lblSelectedArea
	 */
	public CbctlLabel getLblSelectedArea() {
		return lblSelectedArea;
	}


	/**
	 * @param lblSelectedArea the lblSelectedArea to set
	 */
	public void setLblSelectedArea(CbctlLabel lblSelectedArea) {
		this.lblSelectedArea = lblSelectedArea;
	}


	/**
	 * @return the lstVwrSlipAreaAvailable
	 */
	public SDSListViewer getLstVwrSlipAreaAvailable() {
		return lstVwrSlipAreaAvailable;
	}


	/**
	 * @param lstVwrSlipAreaAvailable the lstVwrSlipAreaAvailable to set
	 */
	public void setLstVwrSlipAreaAvailable(SDSListViewer lstVwrSlipAreaAvailable) {
		this.lstVwrSlipAreaAvailable = lstVwrSlipAreaAvailable;
	}


	/**
	 * @return the lstVwrSlipAreaSelected
	 */
	public SDSListViewer getLstVwrSlipAreaSelected() {
		return lstVwrSlipAreaSelected;
	}


	/**
	 * @param lstVwrSlipAreaSelected the lstVwrSlipAreaSelected to set
	 */
	public void setLstVwrSlipAreaSelected(SDSListViewer lstVwrSlipAreaSelected) {
		this.lstVwrSlipAreaSelected = lstVwrSlipAreaSelected;
	}


	/**
	 * @param btnPrefLeft the btnPrefLeft to set
	 */
	public void setBtnPrefLeft(CbctlButton btnPrefLeft) {
		this.btnPrefLeft = btnPrefLeft;
	}


	/**
	 * @param btnPrefRight the btnPrefRight to set
	 */
	public void setBtnPrefRight(CbctlButton btnPrefRight) {
		this.btnPrefRight = btnPrefRight;
	}


	/**
	 * @param lstSlipAreaAvailable the lstSlipAreaAvailable to set
	 */
	public void setLstSlipAreaAvailable(List lstSlipAreaAvailable) {
		this.lstSlipAreaAvailable = lstSlipAreaAvailable;
	}


	/**
	 * @param lstSlipAreaSelected the lstSlipAreaSelected to set
	 */
	public void setLstSlipAreaSelected(List lstSlipAreaSelected) {
		this.lstSlipAreaSelected = lstSlipAreaSelected;
	}


}  
