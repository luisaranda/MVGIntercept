/*****************************************************************************
 * $Id: VoucherHeaderController.java,v 1.37.3.0, 2013-10-25 17:50:06Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 12:50:06 PM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.controller;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.VoucherCenterComposite;
import com.ballydev.sds.voucherui.composite.VoucherHeaderComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.form.BarcodeEnquiryForm;
import com.ballydev.sds.voucherui.form.BatchReconciliationForm;
import com.ballydev.sds.voucherui.form.OverrideForm;
import com.ballydev.sds.voucherui.form.PrintVoucherForm;
import com.ballydev.sds.voucherui.form.RedeemVoucherForm;
import com.ballydev.sds.voucherui.form.ReportsForm;
import com.ballydev.sds.voucherui.form.ReprintVoucherForm;
import com.ballydev.sds.voucherui.form.VerifyOTForm;
import com.ballydev.sds.voucherui.util.DefaultScreen;
import com.ballydev.sds.voucherui.util.VoucherImageRegistry;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class VoucherHeaderController extends VoucherBaseController implements IVoucherConstants {

	/**
	 *Instance of the voucher header composite 
	 */
	private VoucherHeaderComposite composite = null;

	/**
	 * Instance of the voucher home controller
	 */
	private VoucherHomeController voucherHomeController;

	/**
	 * Instance of the voucher center composite
	 */
	private VoucherCenterComposite centerComposite = null;

	/**
	 * Instance of the redeem voucher controller
	 */
	private RedeemVoucherController redeemVoucherController = null;

	private boolean disposed = false;
	
	private boolean overrideAccess = false;
	
	/**
	 * VoucherHomeController constructor
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public VoucherHeaderController(VoucherHomeController caller, Composite parent,int style) throws Exception {
		super(new SDSForm(), null);	
		this.voucherHomeController = caller;
		createVoucherHeaderComposite(parent,style);
		parent.layout();
		super.registerEvents(composite);
		registerCustomizedListeners(composite);	
		authorizeUserAccess();	
	}

	/**
	 * This method creates the print screen at the load of
	 * the voucher plugin
	 * @param parent
	 * @param style
	 */
	public void intialize(Composite parent,int style) {
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REDEEM) ) {
			try	{
				/*new PrintVoucherController(parent,SWT.NONE,new PrintVoucherForm(), new SDSValidator((getClass()),true));
				AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_PRINT_SCREEN);
				composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_ACTIVE_BUTTON_IMG)));*/
				
				redeemVoucherController = new RedeemVoucherController(voucherHomeController.getMiddleComposite(), SWT.NONE, new RedeemVoucherForm(), new SDSValidator((getClass()),true),false);
				AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_REDEEM_SCREEN);
				if( Util.isSmallerResolution() )
					composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));
				else 
					composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG)));
			} 
			catch(Exception e1) {
				e1.printStackTrace();
			}
		} else {
			centerComposite = new VoucherCenterComposite(parent,style,LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_USER_NOACCESS));
			VoucherMiddleComposite.setCurrentComposite(centerComposite);
			AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_DUMMY_SCREEN);
		}
	}

	@Override
	public void mouseDown(MouseEvent e){
		Control control = (Control) e.getSource();
		if( !((Control)e.getSource() instanceof SDSTSLabel))	{
			return;
		}	
		
		if(redeemVoucherController.multiRedeemCount !=0)
		{
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_REDEEM_TICKETS_INPROCESS));
			return;
		}
		if( redeemVoucherController != null ) {
			redeemVoucherController.navCheck(true,false);
		}
		if( redeemVoucherController != null && !redeemVoucherController.isScreenReadyToOpen() ) {			
			return;
		}
		if( ((Control) e.getSource() instanceof SDSTSLabel) ) {
			if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER) ) {
				try	{
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					redeemVoucherController = new RedeemVoucherController(voucherHomeController.getMiddleComposite(), SWT.NONE, new RedeemVoucherForm(), new SDSValidator((getClass()),true),false);
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_REDEEM_SCREEN);
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VOIDVOUCHER) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed())){
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new RedeemVoucherController(voucherHomeController.getMiddleComposite(), SWT.NONE, new RedeemVoucherForm(), new SDSValidator((getClass()),true),true);
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_VOID_SCREEN);
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
				
			} else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_PRINTVOUCHER) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new PrintVoucherController(voucherHomeController.getMiddleComposite(), SWT.NONE,new PrintVoucherForm(), new SDSValidator((getClass()),true));
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_PRINT_SCREEN);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPRINT) ) {	
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}		
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new ReprintVoucherController(voucherHomeController.getMiddleComposite(), SWT.NONE,new ReprintVoucherForm(), new SDSValidator((getClass()),true));
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_REPRINT_SCREEN);
				} 
				catch (Exception e1){
					e1.printStackTrace();
				}
				
			} else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPORTS) ) {
				try {	
					if( !getControlValueForTKtStatus() ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.SELECT_ONE_OPT_REP));
						return;
					}
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new ReportsController(voucherHomeController.getMiddleComposite(), SWT.NONE,new ReportsForm(), new SDSValidator((getClass()),true));
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_REPORTS_SCREEN);
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
			} else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_ENQUIREVOUCHER) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}	
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new BarcodeEnquiryController(voucherHomeController.getMiddleComposite(), SWT.NONE,new BarcodeEnquiryForm(), new SDSValidator((getClass()),true));
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_ENQUIRY_SCREEN);
					ProgressIndicatorUtil.closeInProgressWindow();
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
				
			}
			else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_RECONCILIATION) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					ProgressIndicatorUtil.openInProgressWindow();
					overrideAccess = true;
					new ReconciliationController(voucherHomeController.getMiddleComposite(), SWT.NONE, new BatchReconciliationForm(), new SDSValidator((getClass()),true));
					ProgressIndicatorUtil.closeInProgressWindow();
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_RECONCILE_SCREEN);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VERIFY_OT) ) {
				try {
					if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
						VoucherMiddleComposite.getCurrentComposite().dispose();
						disposed = true;
					}
					DefaultScreen.setOpen(false);
					overrideAccess = true;
					new VerifyOTController(voucherHomeController.getMiddleComposite(),SWT.NONE, new VerifyOTForm(), new SDSValidator((getClass()),true));
					AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_TKT_VERIFY_SCREEN);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else if( ((SDSTSLabel) control).getName().equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_OVERRIDEVOUCHER) ) {
//			DialogShellForOverride dialogShellForOverride = new DialogShellForOverride(composite.getShell(),voucherHomeController);
				if( isOverrideVoucherFuncEnabled() && VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE_FUNC)
						&& !((SDSTSLabel) control).getImage().equals(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG)) ) {
					overrideAccess = MessageDialogUtil.displayTouchScreenQuestionDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ACCESS_OVERRIDE_SCREEN_MSG) );
					if( overrideAccess ) {
						try {
							if( VoucherMiddleComposite.getCurrentComposite() != null && !(VoucherMiddleComposite.getCurrentComposite().isDisposed()) ) {
								VoucherMiddleComposite.getCurrentComposite().dispose();
								disposed = true;
							}
							ProgressIndicatorUtil.openInProgressWindow();
							new OverrideController(voucherHomeController.getMiddleComposite(), SWT.NONE, new OverrideForm(), new SDSValidator((getClass()), true), composite);
							ProgressIndicatorUtil.closeInProgressWindow();
							AppContextValues.getInstance().setCurrentScreen(IVoucherConstants.VOU_OVERRIDE_SCREEN);
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
//					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_NOT_ACCESS_OVERRIDE_SITECONFIG));
					if(!composite.getLblOverrideVoucherIcon().getImage().equals(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG)) ) {
						if( !isOverrideVoucherFuncEnabled() ) {
							DefaultScreen.openDefaultScreen(voucherHomeController.getMiddleComposite(), composite);
							composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
						}
					}
					return;
				}
			}
		}
		if( disposed ) {
			displayWelcomeMessage();
		}
		try {
			handelImagesActivation(((SDSTSLabel) control).getName());
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public boolean getControlValueForTKtStatus() {
		boolean retValue = true;
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		if( (!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED))) &&
				(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED))) && 
				(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED))) ) {
			if( (!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED)) &&
					(!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED))&&
					(!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED))) {
				retValue = false;
			}
		}		
		return retValue;
	}

	/**
	 * Method to create the MainMenuComposite
	 * @param parent
	 * @param style
	 */
	private void createVoucherHeaderComposite(Composite parent, int style) {	
		composite = new VoucherHeaderComposite(parent,style);	
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {

		composite.getLblPrintVoucherIcon().addMouseListener(this);
		composite.getLblReconciliationIcon().addMouseListener(this);
		composite.getLblRedeemVoucherIcon().addMouseListener(this);
		composite.getLblVoidVoucherIcon().addMouseListener(this);
//		composite.getLblVerifyOTIcon().addMouseListener(this);
//		composite.getLblReprintIcon().addMouseListener(this);
		composite.getLblReportsIcon().addMouseListener(this);
		composite.getLblEnquireIcon().addMouseListener(this);
		composite.getLblOverrideVoucherIcon().addMouseListener(this);
	}

	/**
	 * This method determines whether the user has the
	 * access to the particular screen and removes the listener to that
	 * screen if not
	 */
	public void authorizeUserAccess(){
		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_PRINT) ) {
			composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_DISABLED_BUTTON_IMG)));
			composite.getLblPrintVoucherIcon().removeMouseListener(this);
			/*composite.getLblReprintIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPRINT_VOUCHER_DISABLED_BUTTON_IMG)));
			composite.getLblReprintIcon().removeMouseListener(this);*/
		}

		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REDEEM) ) {
			composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_DISABLED_BUTTON_IMG)));
			composite.getLblRedeemVoucherIcon().removeMouseListener(this);
		}

		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_RECONCILATION) ) {
			composite.getLblReconciliationIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER__DISABLED_BUTTON_IMG)));
			composite.getLblReconciliationIcon().removeMouseListener(this);
		}

		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_VOID) ) {
			composite.getLblVoidVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_DISABLED_BUTTON_IMG)));
			composite.getLblVoidVoucherIcon().removeMouseListener(this);
		}

		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_VERIFY_OT) ) {
			/*composite.getLblVerifyOTIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VERIFY_OT_DISABLED_BUTTON_IMG)));
			composite.getLblVerifyOTIcon().removeMouseListener(this);*/
		}

		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REPORT) ) {
			composite.getLblReportsIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_DISABLED_BUTTON_IMG)));
			composite.getLblReportsIcon().removeMouseListener(this);
		}
		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_ENQUIRE) ) {
			composite.getLblEnquireIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_DISABLED_BUTTON_IMG)));
			composite.getLblEnquireIcon().removeMouseListener(this);
		}
		
		if( !VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE_FUNC) ) {
			composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
		}
		if( !isOverrideVoucherFuncEnabled() ) {
			composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
		}
	}

	/**
	 * This method handles the images that needs to be set for
	 * all the header icons
	 * @param btnName
	 */
	public void handelImagesActivation(String btnName){
		if( Util.isSmallerResolution() && overrideAccess ) {
			setInactivateSmallImages();
			if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_PRINTVOUCHER) ) {			
				composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));
				composite.getLblPrintVoucherIcon().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER)) {
				composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));

			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VOIDVOUCHER)) {
				composite.getLblVoidVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_RECONCILIATION) ) {
				composite.getLblReconciliationIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPORTS) ) {
				composite.getLblReportsIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_ACTIVE_BUTTON_IMG_SMALL)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VERIFY_OT) ) {
				//composite.getLblVerifyOTIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VERIFY_OT_ACTIVE_BUTTON_IMG)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPRINT) ) {
				//composite.getLblReprintIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPRINT_VOUCHER_ACTIVE_BUTTON_IMG)));
			
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_ENQUIREVOUCHER) ) {
				composite.getLblEnquireIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_ACTIVE_BUTTON_IMG_SMALL)));
			
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_OVERRIDEVOUCHER) ) {
				composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG_SMALL));

			} if( overrideAccess && !isOverrideVoucherFuncEnabled() ) {
				composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
			}
		} else if( overrideAccess ) {
			setInactivateImages();
			if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_PRINTVOUCHER) ) {
				composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_ACTIVE_BUTTON_IMG)));
				composite.getLblPrintVoucherIcon().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER)) {
				composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_ACTIVE_BUTTON_IMG)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VOIDVOUCHER)) {
				composite.getLblVoidVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_ACTIVE_BUTTON_IMG)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_RECONCILIATION) ) {
				composite.getLblReconciliationIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_ACTIVE_BUTTON_IMG)));
				
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPORTS) ) {
				composite.getLblReportsIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_ACTIVE_BUTTON_IMG)));
			
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_VERIFY_OT) ) {
				//composite.getLblVerifyOTIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VERIFY_OT_ACTIVE_BUTTON_IMG)));

			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_REPRINT) ) {
				//composite.getLblReprintIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPRINT_VOUCHER_ACTIVE_BUTTON_IMG)));
			
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_ENQUIREVOUCHER) ) {
				composite.getLblEnquireIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_ACTIVE_BUTTON_IMG)));
		
			} else if( btnName.equalsIgnoreCase(VOUCHERHOME_CTRL_BTN_OVERRIDEVOUCHER) && overrideAccess && isOverrideVoucherFuncEnabled() ) {
				composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_ACTIVE_BUTTON_IMG));
//				composite.getLblOverrideVoucherIcon().addMouseListener(this);
				
			} if( overrideAccess && !isOverrideVoucherFuncEnabled() ) {
				composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_DISABLED_BUTTON_IMG));
//				composite.getLblOverrideVoucherIcon().removeMouseListener(this);
			}
		}
	}

	/**
	 * This method sets the inactive images to all
	 * the header icons
	 */
	public void setInactivateImages(){
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_PRINT)) {
			composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_INACTIVE_BUTTON_IMG)));
			//composite.getLblReprintIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPRINT_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REDEEM)) {
			composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if((VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_VOID))) {
			composite.getLblVoidVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_RECONCILATION) ) {
			composite.getLblReconciliationIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_VERIFY_OT) )	{
			//composite.getLblVerifyOTIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VERIFY_OT_INACTIVE_BUTTON_IMG)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REPORT) ) {
			composite.getLblReportsIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_INACTIVE_BUTTON_IMG)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_ENQUIRE)) {
			composite.getLblEnquireIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE_FUNC) && isOverrideVoucherFuncEnabled() ) {
			composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG));
		}
	}
	
	/**
	 * This method sets the inactive images to all
	 * the header icons for smaller resolution
	 */
	public void setInactivateSmallImages(){
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_PRINT)) {
			composite.getLblPrintVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
			//composite.getLblReprintIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPRINT_VOUCHER_INACTIVE_BUTTON_IMG)));
		}
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REDEEM)) {
			composite.getLblRedeemVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		}
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_VOID) ) {
			composite.getLblVoidVoucherIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		}
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_RECONCILATION) ) {
			composite.getLblReconciliationIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_VERIFY_OT) )	{
			//composite.getLblVerifyOTIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VERIFY_OT_INACTIVE_BUTTON_IMG)));
		}
		if(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_REPORT) ) {
			composite.getLblReportsIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_INACTIVE_BUTTON_IMG_SMALL)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_ENQUIRE) ) {
			composite.getLblEnquireIcon().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		}
		if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_OVERRIDE_FUNC) ) {
			composite.getLblOverrideVoucherIcon().setImage(VoucherImageRegistry.getImage(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL));
		}
	}

	public VoucherHeaderComposite getVoucherHeaderComposite(){
		return composite;
	}

	/**
	 * @return the redeemVoucherController
	 */
	public RedeemVoucherController getRedeemVoucherController() {
		return redeemVoucherController;
	}

	/**
	 * @param redeemVoucherController the redeemVoucherController to set
	 */
	public void setRedeemVoucherController(
			RedeemVoucherController redeemVoucherController) {
		this.redeemVoucherController = redeemVoucherController;
	}
	
	/**
	 * Method to display the welcome message in the customer display. Used for erasing any previous message
	 */
	public void displayWelcomeMessage() {		
		com.ballydev.sds.voucherui.displays.Display display = null;
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			boolean isCustomerDisplay = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY);
			if( isCustomerDisplay ) {				
				String dispModel = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL);
				String dispManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER);
				String dispPort = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT);
				String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
				String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
				String displayMessage = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE);
				if(Util.isEmpty(dispModel)|| Util.isEmpty(dispManufacturer)||Util.isEmpty(dispPort)){
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_PREF_VALUE_TO_SET));
					return;
				}
				Class.forName(dispDriver);
				display = com.ballydev.sds.voucherui.displays.Display.getDisplay(dispType);
				if( display != null ) {
					display.close();
				}
				display.open(dispPort);
				display.blankDisplay();
				display.dispMsg(displayMessage);
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();	
		}catch (DisplayNoSuchDriverException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This API checks if Cashier has permission "Allow Ticket not found"
	 * based on the siteconfig parameter.
	 * @return
	 */
	public static boolean isOverrideVoucherFuncEnabled() {
		boolean isEnabled = false;
		SessionUtility sessionUtility = new SessionUtility();
		String tktNotFoundPermission = sessionUtility.getSiteConfigurationValue(IVoucherConstants.OVERRIDEVOUCHER_ALLOW_TICKET_NOT_FOUND);
		if( tktNotFoundPermission != null && tktNotFoundPermission.equalsIgnoreCase("YES") ) {
			isEnabled = true;
		}		
		return isEnabled;
	}

}
