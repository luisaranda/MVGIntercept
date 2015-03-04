package com.ballydev.sds.jackpotui.composite;

import org.apache.log4j.Logger;
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
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * This class gives the Header look for Jackpot Plugin
 * @author anantharajr
 * @version $Revision: 11$
 */
public class JackpotHeaderComposite extends TouchScreenMenuHeaderComposite {

	/**
	 * headerRightComposite instance
	 */
	private Composite headerRightComposite = null;
	/**
	 * headerLeftComposite instance
	 */
	private Composite headerLeftComposite = null;
	/**
	 * pending jp icon label
	 */
	private TSButtonLabel lblPendingImage = null;
	/**
	 * manual jp icon label
	 */
	private TSButtonLabel lblManualImage = null;
	/**
	 * void jp icon label
	 */
	private TSButtonLabel lblVoidImage = null;
	/**
	 * reprint jp icon label
	 */
	private TSButtonLabel lblReprintImage = null;
	/**
	 * display jp icon label
	 */
	private TSButtonLabel lblDisplayImage = null;
	/**
	 * Report jp icon label
	 */
	private TSButtonLabel lblReportImage = null;
	/**
	 * pending jp label
	 */
	private SDSTSLabel lblPending = null;
	/**
	 * manual jp label
	 */
	private SDSTSLabel lblManual = null;
	/**
	 * void jp label
	 */
	private SDSTSLabel lblVoid = null;
	/**
	 * reprint jp label
	 */
	private SDSTSLabel lblReprint = null;
	/**
	 * display jp label
	 */
	private SDSTSLabel lblDisplay = null;
	/**
	 * Report jp label
	 */
	private SDSTSLabel lblReport = null;
	/**
	 * module name label
	 */
	private TSButtonLabel lblModule = null;
	
	private String imgPendingInactiveBtn;
	
	private String imgManualInactiveBtn;
	
	private String imgVoidInactiveBtn;
	
	private String imgReprintInactiveBtn;
	
	private String imgDisplayInactiveBtn;
	
	private String imgRptInactiveBtn;
	
	private int compHeight = 90;
	
	private int vertSpace = 5;
	
	private int vertIndent = 0;
	
	private int horizontalSpace = 10;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	

	/**
	 * contructor
	 * @param parent
	 * @param style
	 */
	public JackpotHeaderComposite(Composite parent, int style) {
		super(parent, style);
		log.info("Inside JackpotHeaderComposite");
		if (Util.isSmallerResolution()) {
			imgPendingInactiveBtn = ImageConstants.S_PENDING_INACTIVE_BUTTON_IMG;
			imgManualInactiveBtn = ImageConstants.S_MANUAL_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.S_VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.S_REPRINT_INACTIVE_BUTTON_IMG;
			imgDisplayInactiveBtn = ImageConstants.S_DISPLAY_INACTIVE_BUTTON_IMG;
			imgRptInactiveBtn = ImageConstants.S_REPORT_INACTIVE_BUTTON_IMG;
			compHeight = 80;
			vertSpace = 0;
			vertIndent = 5;
			horizontalSpace = 5;
		}
		else {
			imgPendingInactiveBtn = ImageConstants.PENDING_INACTIVE_BUTTON_IMG;
			imgManualInactiveBtn = ImageConstants.MANUAL_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.REPRINT_INACTIVE_BUTTON_IMG;
			imgDisplayInactiveBtn = ImageConstants.DISPLAY_INACTIVE_BUTTON_IMG;
			imgRptInactiveBtn = ImageConstants.REPORT_INACTIVE_BUTTON_IMG;
		}
		initialize();
	}

	/**
	 * This method initializes headerRightComposite	
	 *
	 */
	public void createHeaderRightComposite() {
		try{
		
			GridLayout grlRightComposite = new GridLayout();
			grlRightComposite.horizontalSpacing = horizontalSpace;
			grlRightComposite.verticalSpacing = vertSpace;
			grlRightComposite.marginHeight = 0;
			grlRightComposite.numColumns = 6;
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
	
			
			
			log.info("Images:"+imgPendingInactiveBtn+imgManualInactiveBtn+imgVoidInactiveBtn+imgReprintInactiveBtn+imgDisplayInactiveBtn+imgRptInactiveBtn);
			lblPendingImage = new TSButtonLabel(headerRightComposite, SWT.CENTER,"pending");
			lblPendingImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgPendingInactiveBtn)));
			lblPendingImage.setLayoutData(getGDForImage());
			lblPendingImage.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			
	
			lblManualImage = new TSButtonLabel(headerRightComposite, SWT.CENTER, "manual");
			lblManualImage.setLayoutData(getGDForImage());
			lblManualImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgManualInactiveBtn)));
			lblManualImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
	
			
			lblVoidImage = new TSButtonLabel(headerRightComposite, SWT.CENTER,"void");
			lblVoidImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgVoidInactiveBtn)));
			lblVoidImage.setLayoutData(getGDForImage());
			lblVoidImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
			
			
			lblReprintImage = new TSButtonLabel(headerRightComposite, SWT.CENTER, "reprint");
			lblReprintImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgReprintInactiveBtn)));
			lblReprintImage.setLayoutData(getGDForImage());
			lblReprintImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
			
			lblDisplayImage = new TSButtonLabel(headerRightComposite, SWT.CENTER,"display");
			lblDisplayImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgDisplayInactiveBtn)));
			lblDisplayImage.setLayoutData(getGDForImage());
			lblDisplayImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
			
			lblReportImage = new TSButtonLabel(headerRightComposite, SWT.CENTER,"report");
			lblReportImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgRptInactiveBtn)));
			lblReportImage.setLayoutData(getGDForImage());
			lblReportImage.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
			
			lblPending = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.PENDING_JACKPOT_BUTTON));
			lblPending.setLayoutData(getGDForLabel());
			lblPending.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblPending.setFont(SDSControlFactory.getSmallDefaultBoldFont());
			
			lblManual = new SDSTSLabel(headerRightComposite, SWT.CENTER, LabelLoader.getLabelValue(LabelKeyConstants.MANUAL_JACKPOT_BUTTON));
			lblManual.setLayoutData(getGDForLabel());
			lblManual.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblManual.setFont(SDSControlFactory.getSmallDefaultBoldFont());
			GridData gdLabelVoid = getGDForLabel();        
			//gdLabelVoid.horizontalIndent = 25;
			//gdLabelVoid.widthHint=35;
						
			lblVoid = new SDSTSLabel(headerRightComposite, SWT.CENTER,LabelLoader.getLabelValue(LabelKeyConstants.VOID_BUTTON));
			lblVoid.setLayoutData(gdLabelVoid);
			lblVoid.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblVoid.setFont(SDSControlFactory.getSmallDefaultBoldFont());
			
			lblReprint = new SDSTSLabel(headerRightComposite, SWT.CENTER, LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_BUTTON));
			lblReprint.setLayoutData(getGDForLabel());
			lblReprint.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblReprint.setFont(SDSControlFactory.getSmallDefaultBoldFont());
			
			lblDisplay = new SDSTSLabel(headerRightComposite, SWT.CENTER, LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_BUTTON));
			lblDisplay.setLayoutData(getGDForLabel());
			lblDisplay.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblDisplay.setFont(SDSControlFactory.getSmallDefaultBoldFont());
			
			lblReport = new SDSTSLabel(headerRightComposite, SWT.CENTER, LabelLoader.getLabelValue(LabelKeyConstants.REPORT_BUTTON));
			lblReport.setLayoutData(getGDForLabel());
			lblReport.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			if(Util.isSmallerResolution())
				lblReport.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		}catch(Exception ex){
			log.info("Error inside initialise");
			log.info("StackTrace:"+ex.getMessage());
		}
		
	}
	
	private GridData getGDForLabel(){
		GridData gdLabel = new GridData();        
		gdLabel.heightHint = 25;
		gdLabel.horizontalAlignment = GridData.CENTER;
		gdLabel.verticalAlignment = GridData.BEGINNING;
		gdLabel.widthHint = 75;
		//gdLabel.horizontalIndent = 10;
		gdLabel.verticalIndent = vertIndent;
		return gdLabel;
	}
	
	/**
	 * @return the headerLeftComposite
	 */
	public Composite getHeaderLeftComposite() {
		return headerLeftComposite;
	}

	/**
	 * @param headerLeftComposite the headerLeftComposite to set
	 */
	public void setHeaderLeftComposite(Composite headerLeftComposite) {
		this.headerLeftComposite = headerLeftComposite;
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
	 * @return the lblDisplay
	 */
	public SDSTSLabel getLblDisplay() {
		return lblDisplay;
	}

	/**
	 * @param lblDisplay the lblDisplay to set
	 */
	public void setLblDisplay(SDSTSLabel lblDisplay) {
		this.lblDisplay = lblDisplay;
	}

	/**
	 * @return the lblDisplayImage
	 */
	public TSButtonLabel getLblDisplayImage() {
		return lblDisplayImage;
	}

	/**
	 * @param lblDisplayImage the lblDisplayImage to set
	 */
	public void setLblDisplayImage(TSButtonLabel lblDisplayImage) {
		this.lblDisplayImage = lblDisplayImage;
	}

	/**
	 * @return the lblManual
	 */
	public SDSTSLabel getLblManual() {
		return lblManual;
	}

	/**
	 * @param lblManual the lblManual to set
	 */
	public void setLblManual(SDSTSLabel lblManual) {
		this.lblManual = lblManual;
	}

	/**
	 * @return the lblManualImage
	 */
	public TSButtonLabel getLblManualImage() {
		return lblManualImage;
	}

	/**
	 * @param lblManualImage the lblManualImage to set
	 */
	public void setLblManualImage(TSButtonLabel lblManualImage) {
		this.lblManualImage = lblManualImage;
	}

	/**
	 * @return the lblModule
	 */
	public TSButtonLabel getLblModule() {
		return lblModule;
	}

	/**
	 * @param lblModule the lblModule to set
	 */
	public void setLblModule(TSButtonLabel lblModule) {
		this.lblModule = lblModule;
	}

	/**
	 * @return the lblPending
	 */
	public SDSTSLabel getLblPending() {
		return lblPending;
	}

	/**
	 * @param lblPending the lblPending to set
	 */
	public void setLblPending(SDSTSLabel lblPending) {
		this.lblPending = lblPending;
	}

	/**
	 * @return the lblPendingImage
	 */
	public TSButtonLabel getLblPendingImage() {
		return lblPendingImage;
	}

	/**
	 * @param lblPendingImage the lblPendingImage to set
	 */
	public void setLblPendingImage(TSButtonLabel lblPendingImage) {
		this.lblPendingImage = lblPendingImage;
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

} 
