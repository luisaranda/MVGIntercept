package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;



public class JackpotReportTextComposite extends TouchScreenBaseComposite {

	/**
	 * Report Text Area Box
	 */
	private StyledText txtReportsDisplay = null;
	
	/**
	 * Instance of the forward button
	 */
	private TSButtonLabel btnForward = null;

	/**
	 * Instance of the backward button
	 */
	private TSButtonLabel btnBackward = null;

	/**
	 * Instance of the forward button
	 */
	private TSButtonLabel btnUp = null;
	
	private Composite reportsTextAreaComposite;

	/**
	 * Instance of the backward button
	 */
	private TSButtonLabel btnDown = null;
	
	private String imgUp = ImageConstants.UP_ARROW_BUTTON_IMAGE;
	
	private String imgDown = ImageConstants.DOWN_ARROW_BUTTON_IMAGE;
	
	private String imgNext = ImageConstants.DISPLAY_NEXT_ARROW;
	
	private String imgPrev = ImageConstants.DISPLAY_PREVIOUS_ARROW;
	
	private String imgDisableDispPrevArrow = ImageConstants.DISABLE_DISPLAY_PREVIOUS_ARROW;
	
	private String imgDisableDispNextArrow = ImageConstants.DISABLE_DISPLAY_NEXT_ARROW;
	
	private String imgDisableDispUpArrow = ImageConstants.DISABLE_DISPLAY_UP_ARROW;

	private String imgDisableDispDownArrow = ImageConstants.DISABLE_DISPLAY_DOWN_ARROW;
	
	private int reportWidth = ITSControlSizeConstants.REPORT_WIDTH;
	
	private int reportHeight = ITSControlSizeConstants.REPORT_HEIGHT;
	
	private Image btnUpImage;
	
	private Image btnDownImage;
	
	private Image btnPrevImage;
	
	private Image btnNextImage;
	
	private Image disablePrevImage;
	
	private Image disableNextImage;
	
	private Image disableUpImage;
	
	private Image disableDownImage;
	
	
	int iconMarginWidth = ITSControlSizeConstants.ICON_MARGIN_WIDTH;
	
	
	
	/**
	 * VoidComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public JackpotReportTextComposite(Composite parent, int style) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.REPORT_JP_HEADING));
		if(Util.isSmallerResolution()){
			imgUp = ImageConstants.S_UP_ARROW_BUTTON_IMAGE;
			
			imgDown = ImageConstants.S_DOWN_ARROW_BUTTON_IMAGE;
			
			imgNext = ImageConstants.S_DISPLAY_NEXT_ARROW;
			
			imgPrev = ImageConstants.S_DISPLAY_PREVIOUS_ARROW;
			
			imgDisableDispPrevArrow = ImageConstants.S_DISABLE_DISPLAY_PREVIOUS_ARROW;
			
			imgDisableDispNextArrow = ImageConstants.S_DISABLE_DISPLAY_NEXT_ARROW;
			
			reportWidth = ITSControlSizeConstants.S_REPORT_WIDTH;
			
			reportHeight = ITSControlSizeConstants.S_REPORT_HEIGHT;
			
			iconMarginWidth = ITSControlSizeConstants.S_ICON_MARGIN_WIDTH;
			
			imgDisableDispUpArrow = ImageConstants.S_DISABLE_DISPLAY_UP_ARROW;
			
			imgDisableDispDownArrow = ImageConstants.S_DISABLE_DISPLAY_DOWN_ARROW;
			
		}
		
		btnUpImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(imgUp));
		
		btnDownImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(imgDown));
		
		btnPrevImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(imgPrev));
		
		btnNextImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(imgNext));
		
		disablePrevImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispPrevArrow));
		
		disableNextImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispNextArrow));
		
		disableUpImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispUpArrow));
		
		disableDownImage = new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgDisableDispDownArrow));
		initialize("PR,C");
		layout();
	}
	
	public void drawMiddleComposite() {
		
		getMiddleComposite().setGdMiddleComp(getMiddleComposite().getGridDataForReportCmp());
		getMiddleComposite().setGlMiddleCom(getMiddleComposite().getGlMiddleCom());
		getMiddleComposite().setLayoutDataForMiddle();
		
		GridData gdTextAreaComp = new GridData();
		gdTextAreaComp.grabExcessHorizontalSpace = true;
		gdTextAreaComp.grabExcessVerticalSpace = true;
		gdTextAreaComp.horizontalAlignment = GridData.FILL;
		gdTextAreaComp.horizontalSpan=3;
		gdTextAreaComp.verticalAlignment = GridData.CENTER;
		gdTextAreaComp.widthHint = reportWidth;
		gdTextAreaComp.heightHint = reportHeight;
		
		GridLayout glTextAreaComp = new GridLayout();
		glTextAreaComp.numColumns = 4;
		glTextAreaComp.verticalSpacing = 2;
		glTextAreaComp.horizontalSpacing = 5;

		
		reportsTextAreaComposite = new Composite(getMiddleComposite(),SWT.NONE);
		reportsTextAreaComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		reportsTextAreaComposite.setLayout(glTextAreaComp);
		reportsTextAreaComposite.setLayoutData(gdTextAreaComp);
		
		GridData gdTxtArea = new GridData();
		gdTxtArea.heightHint = reportHeight;
		gdTxtArea.widthHint = reportWidth;
		gdTxtArea.horizontalAlignment = SWT.BEGINNING;
		gdTxtArea.horizontalSpan=3;
		gdTxtArea.verticalSpan=2;
		gdTxtArea.horizontalIndent=30;
		
		
		txtReportsDisplay = new StyledText(reportsTextAreaComposite,SWT.NONE);
		txtReportsDisplay.setEditable(false);
		txtReportsDisplay.setFont(new Font(Display.getCurrent(),"Arial",8,SWT.NORMAL));
		txtReportsDisplay.setLayoutData(gdTxtArea);
		
		GridData gdUpDownComp = new GridData();
		gdUpDownComp.grabExcessHorizontalSpace = true;
		gdUpDownComp.horizontalSpan=1;
		gdUpDownComp.verticalAlignment = SWT.CENTER;
		
		GridLayout grlUpDownComp = new GridLayout();
		grlUpDownComp.numColumns = 1;

		
		Composite reportsUpDownComposite = new Composite(getMiddleComposite(),SWT.NONE);
		reportsUpDownComposite.setLayout(grlUpDownComp);
		reportsUpDownComposite.setLayoutData(gdUpDownComp);
		
		GridData gdBtnUp = new GridData();
		gdBtnUp.grabExcessVerticalSpace=true;
		gdBtnUp.verticalAlignment = SWT.BEGINNING;
		btnUp = new TSButtonLabel(reportsUpDownComposite,SWT.None,"","btnUp");
		btnUp.setImage(disableUpImage);
		btnUp.setBounds(btnUpImage.getBounds());
		btnUp.setLayoutData(gdBtnUp);
		
		GridData gdBtnDown = new GridData();
		gdBtnDown.grabExcessVerticalSpace = true;
		gdBtnUp.verticalAlignment = SWT.END;
		btnDown = new TSButtonLabel(reportsUpDownComposite,SWT.None,"","btnDown");
		btnDown.setImage(btnDownImage);
		btnDown.setLayoutData(gdBtnDown);
		
		GridData gdBackForwComp = new GridData();
		gdBackForwComp.horizontalSpan=3;
		gdBackForwComp.horizontalAlignment = SWT.BEGINNING;
		
		GridLayout grlBackForwComp = new GridLayout();
		grlBackForwComp.numColumns = 4;
		grlBackForwComp.marginWidth=iconMarginWidth;
		
		Composite reportsBackForwComposite = new Composite(getMiddleComposite(),SWT.NONE);
		reportsBackForwComposite.setLayout(grlBackForwComp);
		reportsBackForwComposite.setLayoutData(gdBackForwComp);
		
		GridData gdBtnBack = new GridData();
		gdBtnBack.grabExcessHorizontalSpace = true;
		gdBtnBack.horizontalAlignment = SWT.CENTER;
		gdBtnBack.horizontalSpan = 3;
		
		btnBackward = new TSButtonLabel(reportsBackForwComposite,SWT.None,"","btnBackward");
		btnBackward.setImage(disablePrevImage);
		btnBackward.setLayoutData(gdBtnBack);
		
		GridData gdBtnForward = new GridData();
		gdBtnForward.horizontalAlignment = SWT.CENTER;
		gdBtnForward.grabExcessHorizontalSpace=true;
		
		btnForward = new TSButtonLabel(reportsBackForwComposite,SWT.None,"","btnForward");
		btnForward.setImage(btnNextImage);
		btnForward.setLayoutData(gdBtnForward);

	}
	
	/**
	 * @return the txtReportsDisplay
	 */
	public StyledText getTxtReportsDisplay() {
		return txtReportsDisplay;
	}

	/**
	 * @param txtReportsDisplay the txtReportsDisplay to set
	 */
	public void setTxtReportsDisplay(StyledText txtReportsDisplay) {
		this.txtReportsDisplay = txtReportsDisplay;
	}

	public TSButtonLabel getBtnForward() {
		return btnForward;
	}

	public void setBtnForward(TSButtonLabel btnForward) {
		this.btnForward = btnForward;
	}

	public TSButtonLabel getBtnBackward() {
		return btnBackward;
	}

	public void setBtnBackward(TSButtonLabel btnBackward) {
		this.btnBackward = btnBackward;
	}

	public TSButtonLabel getBtnUp() {
		return btnUp;
	}

	public void setBtnUp(TSButtonLabel btnUp) {
		this.btnUp = btnUp;
	}

	public TSButtonLabel getBtnDown() {
		return btnDown;
	}

	public void setBtnDown(TSButtonLabel btnDown) {
		this.btnDown = btnDown;
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

	public Image getDisableUpImage() {
		return disableUpImage;
	}

	public void setDisableUpImage(Image disableUpImage) {
		this.disableUpImage = disableUpImage;
	}

	public Image getDisableDownImage() {
		return disableDownImage;
	}

	public void setDisableDownImage(Image disableDownImage) {
		this.disableDownImage = disableDownImage;
	}

	public Image getBtnUpImage() {
		return btnUpImage;
	}

	public void setBtnUpImage(Image btnUpImage) {
		this.btnUpImage = btnUpImage;
	}

	public Image getBtnDownImage() {
		return btnDownImage;
	}

	public void setBtnDownImage(Image btnDownImage) {
		this.btnDownImage = btnDownImage;
	}

	public Image getBtnPrevImage() {
		return btnPrevImage;
	}

	public void setBtnPrevImage(Image btnPrevImage) {
		this.btnPrevImage = btnPrevImage;
	}

	public Image getBtnNextImage() {
		return btnNextImage;
	}

	public void setBtnNextImage(Image btnNextImage) {
		this.btnNextImage = btnNextImage;
	}

	public Composite getReportsTextAreaComposite() {
		return reportsTextAreaComposite;
	}

	public void setReportsTextAreaComposite(Composite reportsTextAreaComposite) {
		this.reportsTextAreaComposite = reportsTextAreaComposite;
	}

} // @jve:decl-index=0:visual-constraint="215,84"
