package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenMenuHeaderComposite;
import com.ballydev.sds.framework.constant.ISDSThemeConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSThemeControlFactory;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.ImageConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.controls.TSButtonLabel;

/**
 * This class gives the Header look for Slips Plugin
 * @author anantharajr
 *
 */
public class SlipsHeaderComposite extends TouchScreenMenuHeaderComposite {

	/**
	 * headerRightComposite instance
	 */
	private Composite headerRightComposite = null;

	private TSButtonLabel lblBeefImage = null;
	/**
	 * void icon label
	 */
	private TSButtonLabel lblVoidImage = null;
	/**
	 * reprint icon label
	 */
	private TSButtonLabel lblReprintImage = null;
	
	private TSButtonLabel lblReportImage = null;
	
	private SDSTSLabel lblBeef = null;
	/**
	 * void label
	 */
	private SDSTSLabel lblVoid = null;
	/**
	 * reprint label
	 */
	private SDSTSLabel lblReprint = null;
	
	private SDSTSLabel lblReport = null;
			
	
	private String imgBeefInactiveBtn;
		
	private String imgVoidInactiveBtn;
	
	private String imgReprintInactiveBtn;
		
	private String imgRptInactiveBtn;
	
	private int compHeight = 90;
	
	private int vertSpace = 5;
	
	/**
	 * contructor
	 * @param parent
	 * @param style
	 */
	public SlipsHeaderComposite(Composite parent, int style) {
		super(parent, style);						
		if(Util.isSmallerResolution()) {
			imgBeefInactiveBtn = ImageConstants.SMALL_RESOLUTION_BEEF_INACTIVE;
			imgVoidInactiveBtn = ImageConstants.SMALL_RESOLUTION_VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.SMALL_RESOLUTION_REPRINT_INACTIVE_BUTTON_IMG;
			imgRptInactiveBtn = ImageConstants.SMALL_RESOLUTION_REPORT_INACTIVE;
			compHeight = 80;
			vertSpace = 0;
		}else {
			imgBeefInactiveBtn = ImageConstants.BEEF_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.REPRINT_INACTIVE_BUTTON_IMG;
			imgRptInactiveBtn = ImageConstants.REPORT_INACTIVE_BUTTON_IMG;	
		}		
		initialize();
	}


	public void createHeaderRightComposite() {
		
		GridLayout grlRightComposite = new GridLayout();
		grlRightComposite.horizontalSpacing = 5;
		grlRightComposite.verticalSpacing = vertSpace;
		grlRightComposite.marginHeight = 0;
		grlRightComposite.numColumns = 4;
		grlRightComposite.marginWidth = 5;
		

		GridData gdRightComposite = new GridData();
		gdRightComposite.grabExcessHorizontalSpace = false;
		gdRightComposite.verticalAlignment = GridData.BEGINNING;
		gdRightComposite.heightHint = compHeight;
		gdRightComposite.grabExcessVerticalSpace = true;
		gdRightComposite.horizontalAlignment = GridData.END;
		

		headerRightComposite = new Composite(this, SWT.NONE);
		headerRightComposite.setLayout(grlRightComposite);
		headerRightComposite.setLayoutData(gdRightComposite);
		
		GridData gdLblImage = new GridData();
		gdLblImage.grabExcessVerticalSpace = true;
		gdLblImage.grabExcessHorizontalSpace = true;
		gdLblImage.horizontalAlignment = GridData.CENTER;
		gdLblImage.verticalAlignment = GridData.CENTER;
		
		lblBeefImage = new TSButtonLabel(headerRightComposite, SWT.NONE,"beef");		
		lblBeefImage.setImage(new Image(Display.getCurrent(),this.getClass().getClassLoader().getResourceAsStream(imgBeefInactiveBtn)));		
		lblBeefImage.setLayoutData(gdLblImage);
		lblBeefImage.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblBeefImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
	
		lblVoidImage = new TSButtonLabel(headerRightComposite, SWT.NONE,"void");
		lblVoidImage.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgVoidInactiveBtn)));
		lblVoidImage.setLayoutData(gdLblImage);
		lblVoidImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
				
		lblReprintImage = new TSButtonLabel(headerRightComposite, SWT.NONE,"reprint");
		lblReprintImage.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgReprintInactiveBtn)));
		lblReprintImage.setLayoutData(gdLblImage);
		lblReprintImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
		
		lblReportImage = new TSButtonLabel(headerRightComposite, SWT.NONE,"report");
		lblReportImage.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(imgRptInactiveBtn)));
		lblReportImage.setLayoutData(gdLblImage);
		lblReportImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
				
		GridData gdLabel = new GridData();        
		gdLabel.heightHint = 25;
		gdLabel.horizontalAlignment = GridData.CENTER;
		gdLabel.verticalAlignment = GridData.BEGINNING;
		gdLabel.widthHint = 75;
		//gdLabel.horizontalIndent = 10;
		if(Util.isSmallerResolution())
			gdLabel.verticalIndent = 5;
				
		lblBeef = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.BEEF_BUTTON));
		lblBeef.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		//lblBeef.setFont(lblFont);
		lblBeef.setLayoutData(gdLabel);
		if(Util.isSmallerResolution())
			lblBeef.setFont(SDSControlFactory.getSmallDefaultBoldFont());
				
		lblVoid = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.VOID_BUTTON));
		//lblVoid.setText();
		lblVoid.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		//lblVoid.setFont(lblFont);
		lblVoid.setLayoutData(gdLabel);
		if(Util.isSmallerResolution())
			lblVoid.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblReprint = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_BUTTON));
		//lblReprint.setText();
		lblReprint.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		//lblReprint.setFont(lblFont);
		lblReprint.setLayoutData(gdLabel);
		if(Util.isSmallerResolution())
			lblReprint.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblReport = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.REPORT_BUTTON));
		//lblReport.setText();
		lblReport.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		//lblReport.setFont(lblFont);
		lblReport.setLayoutData(gdLabel);
		if(Util.isSmallerResolution())
			lblReport.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
	}

	/**
	 * @return the headerRightComposite
	 */
	public Composite getHeaderRightComposite() {
		return headerRightComposite;
	}

	/**
	 * @param headerRightComposite the headerRightComposite to set
	 */
	public void setHeaderRightComposite(Composite headerRightComposite) {
		this.headerRightComposite = headerRightComposite;
	}

	/**
	 * @return the lblBeefImage
	 */
	public TSButtonLabel getLblBeefImage() {
		return lblBeefImage;
	}

	/**
	 * @param lblBeefImage the lblBeefImage to set
	 */
	public void setLblBeefImage(TSButtonLabel lblBeefImage) {
		this.lblBeefImage = lblBeefImage;
	}

	/**
	 * @return the lblVoidImage
	 */
	public TSButtonLabel getLblVoidImage() {
		return lblVoidImage;
	}

	/**
	 * @param lblVoidImage the lblVoidImage to set
	 */
	public void setLblVoidImage(TSButtonLabel lblVoidImage) {
		this.lblVoidImage = lblVoidImage;
	}

	/**
	 * @return the lblReprintImage
	 */
	public TSButtonLabel getLblReprintImage() {
		return lblReprintImage;
	}

	/**
	 * @param lblReprintImage the lblReprintImage to set
	 */
	public void setLblReprintImage(TSButtonLabel lblReprintImage) {
		this.lblReprintImage = lblReprintImage;
	}

	/**
	 * @return the lblReportImage
	 */
	public TSButtonLabel getLblReportImage() {
		return lblReportImage;
	}

	/**
	 * @param lblReportImage the lblReportImage to set
	 */
	public void setLblReportImage(TSButtonLabel lblReportImage) {
		this.lblReportImage = lblReportImage;
	}

	/**
	 * @return the lblBeef
	 */
	public SDSTSLabel getLblBeef() {
		return lblBeef;
	}

	/**
	 * @param lblBeef the lblBeef to set
	 */
	public void setLblBeef(SDSTSLabel lblBeef) {
		this.lblBeef = lblBeef;
	}

	/**
	 * @return the lblVoid
	 */
	public SDSTSLabel getLblVoid() {
		return lblVoid;
	}

	/**
	 * @param lblVoid the lblVoid to set
	 */
	public void setLblVoid(SDSTSLabel lblVoid) {
		this.lblVoid = lblVoid;
	}

	/**
	 * @return the lblReprint
	 */
	public SDSTSLabel getLblReprint() {
		return lblReprint;
	}

	/**
	 * @param lblReprint the lblReprint to set
	 */
	public void setLblReprint(SDSTSLabel lblReprint) {
		this.lblReprint = lblReprint;
	}

	/**
	 * @return the lblReport
	 */
	public SDSTSLabel getLblReport() {
		return lblReport;
	}

	/**
	 * @param lblReport the lblReport to set
	 */
	public void setLblReport(SDSTSLabel lblReport) {
		this.lblReport = lblReport;
	}
} 
