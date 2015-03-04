/*****************************************************************************
 * $Id: DisplayController.java,v 1.30, 2010-10-22 11:04:25Z, Subha Viswanathan$
 * $Date: 10/22/2010 6:04:25 AM$
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
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpotui.composite.DisplayComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.DisplayForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;

/**
 * Class to control the events of the DisplayComposite
 * 
 * @version $Revision: 31$
 */
public class DisplayController extends SDSBaseController {
	/**
	 * DisplayForm instance
	 */
	private static DisplayForm form;

	/**
	 * This field is used in selecting the next page for jackpot records. this
	 * variable holds the index of the first record in each page
	 */
	private int initialIndex = 0;

	/**
	 * This field returns ths total number of jackpot slips in the database.
	 */
	private int totalRecordCount = 0;

	/**
	 * the mod of totalRecordCount by numOfRecords
	 */
	private int modValue = 0;

	/**
	 * The maximum number of records allowed to display in each page as there is
	 * no scrollbar in a Touch Screen UI.
	 */
	private int numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

	/**
	 * Boolean variable to checked if the previous button on the display screen
	 * is clicked
	 */
	private boolean previousClicked = false;

	/**
	 * Count of the no of pages
	 */
	private int numOfPages = 0;

	/**
	 * Page Count
	 */
	private int pageCount = 0;

	/**
	 * Object to hold the list of jackpot records for a particular page in UI
	 */
	ArrayList<JackpotDTO> jackpotDTOList = new ArrayList<JackpotDTO>();

	/**
	 * Object to holds all the jackpot records in the database
	 */
	List<JackpotDTO> jackpotList;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * DisplayComposite instance
	 */
	private DisplayComposite composite;

	/**
	 * dummy variable to store the page
	 */
	private String dummy;

	/**
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public DisplayController(Composite parent, int style, DisplayForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		JackpotFilter filter = new JackpotFilter();

		filter.setStatusFlagId((short) 4001); // For a Pending Jackpot

		int siteId = MainMenuController.jackpotForm.getSiteId();
		if (!MainMenuController.jackpotForm.getSequenceNoOrAll().equalsIgnoreCase("")) {
			if (log.isInfoEnabled()) {
				log.info("************Sequence no: " + MainMenuController.jackpotForm.getSequenceNoOrAll());
			}
			if (MainMenuController.jackpotForm.getSequenceNoOrAll().equalsIgnoreCase("a")) {
				if (log.isInfoEnabled()) {
					log.info("All is the input for de sequence number");
				}
				try {
					if (log.isDebugEnabled()) {
						log.debug("Calling the web method getAllPendingJackpotSlipDetails");
						log.debug("*******************************************");
						log.debug("Site id: " + siteId);
						log.debug("*******************************************");
					}
					jackpotList = JackpotServiceLocator.getService().getAllPendingJackpotSlipDetails(siteId);
					if (log.isDebugEnabled()) {
						log.debug("Values returned after calling the getAllPendingJackpotSlipDetails web method");
						log.debug("*******************************************");
					}
					if (jackpotList != null && jackpotList.size() > 0) {
						if (log.isDebugEnabled()) {
							log.debug("*******************************************");
						}
						if (log.isInfoEnabled()) {
							log.info("----- list size: " + jackpotList.size());
						}
					}					
				} catch (JackpotEngineServiceException e1) {
					JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
					handler.handleException(e1, e1.getMessage());
					log.error("Exception occured while calling the getAllPendingJackpotSlipDetails", e1);
					return;
				} catch (Exception e2) {
					JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
					handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
					log.error("SERVICE_DOWN", e2);
					return;
				}
			} else {
				String statusDesc = null;
				short statusFlagId = 0;
				try {
					if (log.isInfoEnabled()) {
						log.info("Calling web method to get the slip status before gettin the pending records");
					}
					if (log.isDebugEnabled()) {
						log.debug("Calling the web method getJackpotStatus");
						log.debug("*******************************************");
						log.debug("Sequence no Or All: " + MainMenuController.jackpotForm.getSiteId());
						log.debug("Site id: " + MainMenuController.jackpotForm.getSequenceNoOrAll());
						log.debug("*******************************************");
					}
					statusFlagId = JackpotServiceLocator.getService().getJackpotStatus(
							new Long(MainMenuController.jackpotForm.getSequenceNoOrAll()),
							MainMenuController.jackpotForm.getSiteId());
					if (log.isInfoEnabled()) {
						log.info("Status Id: " + statusFlagId);
					}
					switch (statusFlagId) {
					case ILookupTableConstants.PRINTED_STATUS_ID:
						statusDesc = ILookupTableConstants.PRINTED_ST_DESC; 
						break;
					case ILookupTableConstants.PROCESSED_STATUS_ID:
						statusDesc = ILookupTableConstants.PROCESSED_ST_DESC;
						break;

					case ILookupTableConstants.CHANGE_STATUS_ID:
						statusDesc = ILookupTableConstants.CHANGE_ST_DESC;
						break;

					case ILookupTableConstants.REPRINT_STATUS_ID:
						statusDesc = ILookupTableConstants.REPRINT_ST_DESC;
						break;

					case ILookupTableConstants.VOID_STATUS_ID:
						statusDesc = ILookupTableConstants.VOID_ST_DESC;
						break;

					case ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID:
						statusDesc = ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC;
						break;

					case ILookupTableConstants.PENDING_STATUS_ID:
						statusDesc = ILookupTableConstants.PENDING_ST_DESC;
						break;

					case ILookupTableConstants.MECHANICS_DELTA_STATUS_ID:
						statusDesc = ILookupTableConstants.MECHANICS_DELTA_ST_DESC;
						break;

					case ILookupTableConstants.INVALID_STATUS_ID:
						statusDesc = ILookupTableConstants.INVALID_ST_DESC;
						break;
					}
					if (log.isDebugEnabled()) {
						log.debug("Values returned after calling the getJackpotStatus web method");
						log.debug("*******************************************");
						log.debug("Status Description: " + statusDesc);
						log.debug("*******************************************");
					}
				} catch (JackpotEngineServiceException e1) {
					JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
					handler.handleException(e1, e1.getMessage());
					log.error("Exception while calling the getJackpotStatus web method", e1);
					return;
				} catch (Exception e2) {
					JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
					handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
					log.error("SERVICE_DOWN", e2);
					return;
				}

				if (statusDesc != null) {
					if ((MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("No") && statusDesc
							.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC))
							|| (MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("Yes") && statusDesc
									.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC)) 
							|| statusDesc.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC)
							|| statusDesc.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC)) {
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PRINTED));
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
						return;
					} else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("Yes")
							&& statusDesc.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC)) {
						// CASHIER DESK FLOW AND STATUS PROCESSED
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PROCESSED)); 
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
						return;
					} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.VOID_ST_DESC)) {
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.JP_SLIP_ALREADY_VOIDED));
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
						return;
					} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC)) {
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.SLIP_CREDIT_KEYED_OFF));
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
						return;
					} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.MECHANICS_DELTA_ST_DESC)) {
						if (log.isInfoEnabled()) {
							log.info("SliP has Mechanics Delta Status");
						}
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.JP_SLIP_MECH_DELTA));
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
					} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.INVALID_ST_DESC)) {
						if (log.isInfoEnabled()) {
							log.info("SliP is Invalid");
						}
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.JP_SLIP_INVALID));
						CallInitialScreenUtil call = new CallInitialScreenUtil();
						call.callDisplayJPFirstScreen();
					} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.PENDING_ST_DESC)) { 
						// called if the slip status is pending
						filter.setSiteId(siteId);
						filter.setSequenceNumber(new Long(MainMenuController.jackpotForm.getSequenceNoOrAll()));
						filter.setType("getJackpotDetailsForSequenceNumber");
						try {
							if (log.isInfoEnabled()) {
								log.info("Calling the web method getJackpotDetails for a sequence");
							}
							if (log.isDebugEnabled()) {
								log.debug("*******************************************");
								log.debug("Sequence no Or All: " + MainMenuController.jackpotForm.getSiteId());
								log.debug("Filter values: " + filter.toString());
								log.debug("*******************************************");
							}
							jackpotList = JackpotServiceLocator.getService().getJackpotDetails(filter);
							if (log.isDebugEnabled()) {
								log.debug("Values returned after calling the getJackpotDetails web method");
								log.debug("*******************************************");
								log.debug("jackpotList size: " + jackpotList.size());
								log.debug("*******************************************");
							}

							if (jackpotList != null && jackpotList.size() != 0) {

								/** MULTI AREA SUPPORT CHECK */
								if (MultiAreaSupportUtil.isMultiAreaSupportEnabled()
										&& jackpotList.get(0).getAreaShortName() != null) {
									if (!MultiAreaSupportUtil.validateSlotForSelectedArea(jackpotList.get(0)
											.getAreaShortName())) {
										if (log.isInfoEnabled()) {
											log.info("Slot does not belong to this area for the sequence no entered");
										}
										MessageDialogUtil
												.displayTouchScreenErrorMsgDialog(LabelLoader
														.getLabelValue(MessageKeyConstants.SEQ_SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
										CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
										initialScr.callDisplayJPFirstScreen();
										return;
									}
								}
							}
						} catch (JackpotEngineServiceException e1) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							log.error("Exception occured while calling the getJackpotDetails", e1);
							return;
						} catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN", e2);
							return;
						}
					}
				} else {
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
					CallInitialScreenUtil call = new CallInitialScreenUtil();
					call.callDisplayJPFirstScreen();
					return;
				}
			}
		} else {
			filter.setSiteId(siteId);
			if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
				if (log.isInfoEnabled()) {
					log.info("************Slot no");
				}
				filter.setSlotNumber(MainMenuController.jackpotForm.getSlotNo());
				filter.setType("getJackpotDetailsForSlotNumber");
			} else if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
				if (log.isInfoEnabled()) {
					log.info("************Stand no");
				}
				filter.setLstSlotNo(MainMenuController.jackpotForm.getLstSlotNo());
				filter.setStandNumber(MainMenuController.jackpotForm.getSlotLocationNo());
				filter.setType("getJackpotDetailsForSlotLocation");
			}
			try {
				if (log.isInfoEnabled()) {
					log.info("Calling web method for " + filter.getType());
					log.info("Calling the web method getJackpotDetails");
				}
				if (log.isDebugEnabled()) {
					log.debug("*******************************************");
					log.debug("Sequence no Or All: " + MainMenuController.jackpotForm.getSiteId());
					log.debug("Filter values: " + filter.toString());
					log.debug("*******************************************");
				}
				jackpotList = JackpotServiceLocator.getService().getJackpotDetails(filter);
				if (log.isDebugEnabled()) {
					log.debug("Values returned after calling the getJackpotDetails web method");
					log.debug("*******************************************");
					log.debug("jackpotList size: " + jackpotList.size());
					log.debug("*******************************************");
				}
			} catch (JackpotEngineServiceException e1) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				log.error("Exception occured while calling the getJackpotDetails", e1);
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		}

		if (jackpotList != null && !(jackpotList.size() > 0)) {
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
					.getLabelValue(MessageKeyConstants.NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO));
			CallInitialScreenUtil call = new CallInitialScreenUtil();
			call.callDisplayJPFirstScreen();
			return;
		}
		// form.setDispJackpotDTO(jackpotDTOList);
		TopMiddleController.getCurrentComposite().dispose();
		CbctlUtil.setMandatoryLabelOff();
		createDisplayComposite(parent, style);
		/** Method to adjust the table size based on the screen resolution */
		adjustTableSizeBasedOnResolution();
		composite.setVisible(true);
		if (jackpotList != null && jackpotList.size() > 0) {
			totalRecordCount = jackpotList.size();
		}
		numOfPages = totalRecordCount / numOfRecords;
		modValue = totalRecordCount % numOfRecords;
		if (modValue > 0) {
			numOfPages += 1;
		}
		if (log.isDebugEnabled()) {
			log.debug("The total record count is " + totalRecordCount);
			log.debug("The remainder is " + modValue);
			log.debug("num of records" + numOfRecords);
		}

		if (totalRecordCount < numOfRecords || numOfRecords == totalRecordCount) {
			numOfRecords = totalRecordCount;
			composite.getButPrevious().setImage(composite.getDisablePrevImage());
			composite.getBtnToFirstPage().setImage(composite.getDisableFirstImage());
			composite.getButNext().setImage(composite.getDisableNextImage());
			composite.getBtnToLastPage().setImage(composite.getDisableLastImage());

		}
		for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
			composite.getButPrevious().setImage(composite.getDisablePrevImage());
			composite.getBtnToFirstPage().setImage(composite.getDisableFirstImage());
			JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
			jackpotDTOList.add(jackpotDTO);
		}
		form.setDispJackpotDTO(jackpotDTOList);
		pageCount++;
		dummy = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
		composite.getTotalPageCount().setText(dummy + " " + pageCount + " / " + numOfPages);
		composite.layout();
		initialIndex += numOfRecords;
		if (log.isDebugEnabled()) {
			log.debug("Initial index initially" + initialIndex);
		}

		/* This block code is to disable the scroll bar */
		try {
			if (composite.getDispJackpotTableViewer().getTable().getHorizontalBar().isVisible()) {
				ScrollBar horizondalScroll = composite.getDispJackpotTableViewer().getTable()
						.getHorizontalBar();
				horizondalScroll.setThumb(1);
			}
			if (composite.getDispJackpotTableViewer().getTable().getHorizontalBar().isVisible()) {
				ScrollBar verticalScroll = composite.getDispJackpotTableViewer().getTable()
						.getHorizontalBar();
				verticalScroll.setMaximum(1);
			}
		} catch (SWTException e) {
			e.printStackTrace();
		}
		TopMiddleController.setCurrentComposite(composite);

		// parent.layout();
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);

		populateScreen(composite);
		registerCustomizedListeners(composite);
		if (log.isDebugEnabled()) {
			log.debug("*************" + TopMiddleController.getCurrentComposite());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected
	 * (org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#
	 * registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		composite.getDispJackpotTableViewer().getTable().addFocusListener(this);
		DisplayMouseListener listener = new DisplayMouseListener();
		composite.getBtnToFirstPage().addMouseListener(listener);
		composite.getBtnToFirstPage().addTraverseListener(this);
		composite.getBtnToLastPage().addMouseListener(listener);
		composite.getBtnToLastPage().addTraverseListener(this);
		composite.getButNext().addMouseListener(listener);
		composite.getButNext().addTraverseListener(this);
		composite.getButPrevious().addMouseListener(listener);
		composite.getButPrevious().addTraverseListener(this);
		composite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		composite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
	}

	/**
	 * This method is to create DisplayComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createDisplayComposite(Composite p, int s) {
		composite = new DisplayComposite(p, s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return composite;
	}

	/**
	 * Method to adjust the table size based on the screen resolution
	 */
	public void adjustTableSizeBasedOnResolution() {
		// Point resolutionInBounds = TopController.topComposite.getSize();
		/*
		 * int screenHeight = resolutionInBounds.x; int screenWidth =
		 * resolutionInBounds.y;
		 */
		// Monitor monitor = Display.getDefault().getPrimaryMonitor();
		int screenHeight = Display.getCurrent().getActiveShell().getBounds().width;
		int screenWidth = Display.getCurrent().getActiveShell().getBounds().height;
		if (log.isInfoEnabled()) {
			log.info("The screen height is " + Display.getCurrent().getActiveShell().getBounds().width);
			log.info("The screen width is " + Display.getCurrent().getActiveShell().getBounds().height);
		}
		if ((screenHeight == 800 || screenHeight == 802) && (screenWidth == 600 || screenWidth == 602)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 800 x 600 ");
			}
			// numOfRecords = 1;
			/*
			 * for (int i = 0; i < 6; i++) {
			 * composite.getDispJackpotTableViewer().getTable().getColumn(i)
			 * .setWidth(60); }
			 */
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;
			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(112);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(126);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(65);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(87);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(130);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(90);

		} else if ((screenHeight == 1024 || screenHeight == 1026)
				&& (screenWidth == 768 || screenWidth == 770)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 1024 x 768 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(135);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(155);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(80);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(100);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(160);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(168);
		} else if ((screenHeight == 1152 || screenHeight == 1154)
				&& (screenWidth == 864 || screenWidth == 866)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 1152 x 864 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;// 11

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(145);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(145);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(110);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(110);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(170);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(182);
		} else if ((screenHeight == 1280 || screenHeight == 1282)
				&& (screenWidth == 768 || screenWidth == 770)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 1280 x 768 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(180);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(184);
		} else if ((screenHeight == 1280 || screenHeight == 1282)
				&& (screenWidth == 800 || screenWidth == 802)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 1280 x 800 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(180);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(184);
		} else if ((screenHeight == 1280 || screenHeight == 1282)
				&& (screenWidth == 1024 || screenWidth == 1026)) {
			if (log.isDebugEnabled()) {
				log.debug("Screen Resolution is 1280 x 1024 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(158);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(123);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(180);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(185);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Def Screen Resolution is 1024 x 768 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;
			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(135);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(155);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(80);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(100);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(160);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(168);
		}
	}

	private class DisplayMouseListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseDown(MouseEvent e) {
			try {
				populateScreen(composite);

				if (log.isDebugEnabled()) {
					log.debug("inside mousedown.....");
				}
				Control control = (Control) e.getSource();
				if (!(control instanceof TSButtonLabel) && !(control instanceof SDSImageLabel)) {
					return;
				}
				if (control instanceof TSButtonLabel) {
					if (((TSButtonLabel) control).getName().equalsIgnoreCase("next")) {
						if (pageCount != numOfPages) {
							pageCount++;
							if (pageCount >= numOfPages) {
								composite.getButNext().setImage(composite.getDisableNextImage());
								composite.getBtnToLastPage().setImage(composite.getDisableLastImage());
							}
							if (pageCount > 1) {
								composite.getButPrevious().setImage(composite.getPrevImage());
								composite.getBtnToFirstPage().setImage(composite.getFirstImage());
							}
							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							composite.layout();
							composite.getDispJackpotTableViewer().getTable().setSelection(-1);

							if (initialIndex < totalRecordCount - modValue) {
								if (log.isInfoEnabled()) {
									log.info("the initial index inside next arrow" + initialIndex);
								}
								jackpotDTOList.clear();
								if (previousClicked) {
									previousClicked = false;
									initialIndex += numOfRecords;
								}
								for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i i is " + i);
									}
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex += numOfRecords;
							} else if (initialIndex < totalRecordCount) {
								if (log.isInfoEnabled()) {
									log.info("Initial index next first else condition " + initialIndex);
								}
								jackpotDTOList.clear();
								if (previousClicked) {
									previousClicked = false;
								}

								for (int i = initialIndex; i < initialIndex + modValue; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i i is " + i);
									}
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex += modValue;
							} else {
								return;
							}
							form.setDispJackpotDTO(jackpotDTOList);
							populateScreen(composite);
						}
					}

					if (((TSButtonLabel) control).getName().equalsIgnoreCase("toLastPage")) {
						if (modValue > 0) {
							pageCount = numOfPages;
							dummy = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							initialIndex = totalRecordCount - modValue;
							jackpotDTOList.clear();
							// jackpotDTOList.trimToSize();
							if (log.isInfoEnabled()) {
								log.info("The initial index inside last Page is " + initialIndex + "  /"
										+ totalRecordCount);
							}
							for (int i = initialIndex; i < totalRecordCount; i++) {
								JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
								jackpotDTOList.add(jackpotDTO);
							}
							initialIndex += modValue;

							composite.getButNext().setImage(composite.getDisableNextImage());
							composite.getBtnToLastPage().setImage(composite.getDisableLastImage());
							composite.getButPrevious().setImage(composite.getPrevImage());
							composite.getBtnToFirstPage().setImage(composite.getFirstImage());
							if (previousClicked) {
								previousClicked = false;
							}

						} else {
							pageCount = numOfPages;
							dummy = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							initialIndex = totalRecordCount - numOfRecords;
							jackpotDTOList.clear();
							// jackpotDTOList.trimToSize();
							for (int i = initialIndex; i < totalRecordCount; i++) {
								JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
								jackpotDTOList.add(jackpotDTO);
							}
							initialIndex += numOfRecords;

							composite.getButNext().setImage(composite.getDisableNextImage());
							composite.getBtnToLastPage().setImage(composite.getDisableLastImage());
							composite.getButPrevious().setImage(composite.getPrevImage());
							composite.getBtnToFirstPage().setImage(composite.getFirstImage());
							if (previousClicked) {
								previousClicked = false;
							}

						}
						form.setDispJackpotDTO(jackpotDTOList);
						populateScreen(composite);
					}

					if (((TSButtonLabel) control).getName().equalsIgnoreCase("toFirstPage")) {

						if (totalRecordCount >= numOfRecords) {
							pageCount = 1;
							dummy = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							initialIndex = 0;
							jackpotDTOList.clear();
							if (log.isInfoEnabled()) {
								log.info("The initial index inside last Page is " + initialIndex + "  /"
										+ totalRecordCount);
							}
							for (int i = initialIndex; i < numOfRecords; i++) {
								JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
								jackpotDTOList.add(jackpotDTO);
							}

							composite.getButNext().setImage(composite.getNextImage());
							composite.getBtnToLastPage().setImage(composite.getLastImage());
							composite.getButPrevious().setImage(composite.getDisablePrevImage());
							composite.getBtnToFirstPage().setImage(composite.getDisableFirstImage());
							if (!previousClicked) {
								previousClicked = true;
							}
						} else {
							pageCount = 1;
							dummy = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							initialIndex = 0;
							jackpotDTOList.clear();
							if (log.isInfoEnabled()) {
								log.info("The initial index inside last Page is " + initialIndex + "  /"
										+ totalRecordCount);
							}
							for (int i = initialIndex; i < modValue; i++) {
								JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
								jackpotDTOList.add(jackpotDTO);
							}
							composite.getButNext().setImage(composite.getNextImage());
							composite.getBtnToLastPage().setImage(composite.getLastImage());
							composite.getButPrevious().setImage(composite.getDisablePrevImage());
							composite.getBtnToFirstPage().setImage(composite.getDisableFirstImage());
							if (!previousClicked) {
								previousClicked = true;
							}
						}
						form.setDispJackpotDTO(jackpotDTOList);
						populateScreen(composite);
					}

					if (((TSButtonLabel) control).getName().equalsIgnoreCase("previous")) {
						if (pageCount != 1) {
							pageCount--;
							composite.getBtnToLastPage().setImage(composite.getLastImage());

							if (log.isInfoEnabled()) {
								log.info("The initial index inside previous arrow is " + initialIndex);
							}
							if (pageCount < numOfPages) {
								composite.getButNext().setImage(composite.getNextImage());
								composite.getBtnToLastPage().setImage(composite.getLastImage());
							}
							if (pageCount <= 1) {
								composite.getButPrevious().setImage(composite.getDisablePrevImage());
								composite.getBtnToFirstPage().setImage(composite.getDisableFirstImage());
							}

							composite.getTotalPageCount().setText(
									dummy + " " + pageCount + " / " + numOfPages);
							composite.layout();
							composite.getDispJackpotTableViewer().getTable().setSelection(-1);

							if (initialIndex > totalRecordCount - modValue) {
								if (log.isInfoEnabled()) {
									log.info("the initial index inside previous firts if" + initialIndex);
								}

								if (!previousClicked) {
									previousClicked = true;
									// initialIndex-=modValue;

								}
								jackpotDTOList.clear();
								int count = initialIndex - numOfRecords - modValue;
								for (int i = count; i < count + numOfRecords; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i i is " + i);
									}
									// composite.getBtnPreviousRecords().setEnabled(true);
									// composite.getBtnNextRecords().setEnabled(true);
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}

								initialIndex -= modValue;
								// form.setDispJackpotDTO(jackpotDTOList);

							} else if (initialIndex > modValue) {
								if (log.isInfoEnabled()) {
									log.info("Initial index previous first else condition " + initialIndex);
								}
								if (!previousClicked || (initialIndex == totalRecordCount - modValue)) {
									previousClicked = true;
									initialIndex -= numOfRecords;

								}
								jackpotDTOList.clear();
								int count = initialIndex - numOfRecords;
								for (int i = count; i < count + numOfRecords; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i i is " + i);
									}
									// composite.getBtnPreviousRecords().setEnabled(true);
									// composite.getBtnNextRecords().setEnabled(true);
									if (i < 10) {
										// composite.getBtnPreviousRecords().setEnabled(false);
										// composite.getBtnNextRecords().setEnabled(true);
									}
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex -= numOfRecords;
								// form.setDispJackpotDTO(jackpotDTOList);
							} else if (initialIndex > 0) {
								if (log.isInfoEnabled()) {
									log.info("Initial index previous second else condition " + initialIndex);
								}
								if (!previousClicked) {
									previousClicked = true;
									initialIndex -= modValue;
								}
								jackpotDTOList.clear();
								for (int i = 0; i < numOfRecords; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i i is " + i);
									}
									// composite.getBtnPreviousRecords().setEnabled(false);
									// composite.getBtnNextRecords().setEnabled(true);
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex -= modValue;
								// form.setDispJackpotDTO(jackpotDTOList);
							} else {
								return;
							}
							form.setDispJackpotDTO(jackpotDTOList);
							populateScreen(composite);
						}
					}
				} else if (((SDSImageLabel) e.getSource()).getName().equalsIgnoreCase("cancel")) {
					if (log.isDebugEnabled()) {
						log.debug("inside button cancel");
					}
					CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
					callInitialScreenUtil.callDisplayJPFirstScreen();
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
}