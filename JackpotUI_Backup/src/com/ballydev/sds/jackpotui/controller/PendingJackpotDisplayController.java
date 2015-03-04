/*****************************************************************************
 * $Id: PendingJackpotDisplayController.java,v 1.61.1.5, 2011-05-17 20:01:53Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 3:01:53 PM$
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableItem;

import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.ecash.enumconstants.AccountTransTypeEnum;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpotui.composite.PendingJackpotDisplayComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.AmountSlotAttendantIdForm;
import com.ballydev.sds.jackpotui.form.EmployeeShiftSlotDetailsForm;
import com.ballydev.sds.jackpotui.form.PendingJackpotDisplayForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;

/**
 * Class to handle all the events of the PendingJackpotDisplayComposite
 * 
 * @author anantharajr
 * @version $Revision: 68$
 */
public class PendingJackpotDisplayController extends SDSBaseController {

	/**
	 * PendingJackpotDisplayForm instance
	 */
	public static PendingJackpotDisplayForm form;

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
	 * previousClicked button bollean flag
	 */
	private boolean previousClicked = false;

	/**
	 * No of pages
	 */
	private int numOfPages = 0;

	/**
	 * Page count field
	 */
	private int pageCount = 0;

	/**
	 * dummy field
	 */
	private String dummyPageLabel;

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
	 * PendingJackpotDisplayComposite instance
	 */
	private PendingJackpotDisplayComposite composite;

	/**
	 * JackpotFilter instance
	 */
	private JackpotFilter filter = new JackpotFilter();

	/**
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public PendingJackpotDisplayController(Composite parent, int style, PendingJackpotDisplayForm form,
			SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		/** This is set for de time being */
		// For a Pending Jackpot
		filter.setStatusFlagId(ILookupTableConstants.PENDING_STATUS_ID);
		int siteId = MainMenuController.jackpotForm.getSiteId();
		filter.setSiteId(siteId);
		if (MainMenuController.jackpotForm.getNumOfMinutes() != 0) {
			try {
				filter.setNumOfMinutes(MainMenuController.jackpotForm.getNumOfMinutes());
				filter.setType("getJackpotSlipsforLastXXMinutes");
				if (log.isInfoEnabled()) {
					log.info("************************************************************");
					log.info("Calling web method : getJackpotSlipsforLastXXMinutes ");
					log.info("Filter Site Id: " + filter.getSiteId());
					log.info("Filter Minutes: " + filter.getNumOfMinutes());
					log.info("Filter Status flag id: " + filter.getStatusFlagId());
				}
				jackpotList = JackpotServiceLocator.getService().getJackpotDetails(filter);
				log.info("After Calling web method : getJackpotSlipsforLastXXMinutes " + jackpotList);
			} catch (JackpotEngineServiceException e1) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		} else if (MainMenuController.jackpotForm.getSequenceNumber() != 0) {
			/** Comment to be removed later */
			filter.setSequenceNumber(MainMenuController.jackpotForm.getSequenceNumber());
			filter.setType("getJackpotDetailsForSequenceNumber");
			try {
				if (log.isInfoEnabled()) {
					log.info("Calling web method getJackpotDetails : ");
					log.info("Site id: " + filter.getSiteId());
					log.info("Type: " + filter.getType());
					log.info("Seq no: " + filter.getSequenceNumber());
				}
				jackpotList = JackpotServiceLocator.getService().getJackpotDetails(filter);
				if (log.isInfoEnabled()) {
					log.info("After calling the getJackpotDetails method: ");
				}

				if (jackpotList != null && jackpotList.size() != 0) {

					/** MULTI AREA SUPPORT CHECK */
					if (MultiAreaSupportUtil.isMultiAreaSupportEnabled()
							&& jackpotList.get(0).getAreaShortName() != null) {
						if (!MultiAreaSupportUtil.validateSlotForSelectedArea(jackpotList.get(0)
								.getAreaShortName())) {
							log.info("Slot does not belong to this area for the sequence no entered");
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.SEQ_SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
							CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
							initialScr.callPendingJPFirstScreen();
							return;
						}
					}
				}

			} catch (JackpotEngineServiceException e1) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		} else {
			if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
				/** Comment to be removed later */
				filter.setSlotNumber(MainMenuController.jackpotForm.getSlotNo());
				filter.setType("getJackpotDetailsForSlotNumber");
			} else if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
				/** Comment to be removed later */
				filter.setStandNumber(MainMenuController.jackpotForm.getSlotLocationNo());
				filter.setLstSlotNo(MainMenuController.jackpotForm.getLstSlotNo());
				filter.setType("getJackpotDetailsForSlotLocation");
			}
			try {
				if (log.isInfoEnabled()) {
					log.info("calling the web method getJackpotDetails : " + filter.toString());
					log.info("filter " + filter.getType());
				}
				jackpotList = JackpotServiceLocator.getService().getJackpotDetails(filter);
				if (log.isInfoEnabled()) {
					log.info("After the getJackpotDetails method is called");
				}
			} catch (JackpotEngineServiceException e1) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		}

		// USED FOR THE PREVIOUS SCREEN ACTION TO DISPLAY THE PENDING JP RECS
		// ACCORDINGLY
		MainMenuController.jackpotForm.setPendingDisplayFilter(filter.getType());

		if (jackpotList != null && !(jackpotList.size() > 0)) {

			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
					.getLabelValue(MessageKeyConstants.NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO));
			CallInitialScreenUtil call = new CallInitialScreenUtil();
			call.callPendingJPFirstScreen();
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("The size of jackpotList is " + jackpotList.size());
		}
		TopMiddleController.getCurrentComposite().dispose();
		CbctlUtil.setMandatoryLabelOff();
		createPendingJackpotDisplayComposite(parent, style);

		/** Method to adjust the table size based on the screen resolution */
		adjustTableSizeBasedOnResolution();
		composite.setVisible(true);

		totalRecordCount = jackpotList.size();
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
			composite.getBtnPreviousRecords().setImage(composite.getDisablePrevImage());
			composite.getBtnNextRecords().setImage(composite.getDisableNextImage());
			composite.getBtnFirstPage().setImage(composite.getDisableFirstImage());
			composite.getBtnLastPage().setImage(composite.getDisableLastImage());
		}
		for (int i = initialIndex; i < initialIndex + numOfRecords; i++) {
			composite.getBtnPreviousRecords().setImage(composite.getDisablePrevImage());
			composite.getBtnFirstPage().setImage(composite.getDisableFirstImage());
			JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
			jackpotDTOList.add(jackpotDTO);

		}
		form.setDispJackpotDTO(jackpotDTOList);
		pageCount++;

		dummyPageLabel = LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
		composite.getTotalPageCount().setText(dummyPageLabel + pageCount + " / " + numOfPages);
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
		PendingJackpotMouseListener listener = new PendingJackpotMouseListener();

		composite.getBtnFirstPage().addMouseListener(listener);
		composite.getBtnFirstPage().addTraverseListener(this);
		composite.getBtnLastPage().addMouseListener(listener);
		composite.getBtnLastPage().addTraverseListener(this);
		composite.getButNext().addMouseListener(listener);
		composite.getButNext().addTraverseListener(this);
		composite.getButPrevious().addMouseListener(listener);
		composite.getButPrevious().addTraverseListener(this);

		composite.getButtonComposite().getBtnNext().addMouseListener(listener);
		composite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		composite.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		composite.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		composite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		composite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org
	 * .eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);

	}

	/**
	 * This method is to create PendingJackpotDisplayComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createPendingJackpotDisplayComposite(Composite p, int s) {
		composite = new PendingJackpotDisplayComposite(p, s);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(106);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(185);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(200);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(200);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(200);
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
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(200);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Def Screen Resolution is 1024 x 768 ");
			}
			numOfRecords = IAppConstants.NO_OF_RECORDS_TO_DISPLAY_IN_TABLE;

			composite.getDispJackpotTableViewer().getTable().getColumn(0).setWidth(135);
			composite.getDispJackpotTableViewer().getTable().getColumn(1).setWidth(135);
			composite.getDispJackpotTableViewer().getTable().getColumn(2).setWidth(100);
			composite.getDispJackpotTableViewer().getTable().getColumn(3).setWidth(100);
			composite.getDispJackpotTableViewer().getTable().getColumn(4).setWidth(160);
			composite.getDispJackpotTableViewer().getTable().getColumn(5).setWidth(160);
		}
	}
	
	public void setPlayerNameForAccountNumber() {
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")
				&& MainMenuController.jackpotForm.getAccountNumber() != null
				&& !MainMenuController.jackpotForm.getAccountNumber().trim().isEmpty()
				&& Long.parseLong(MainMenuController.jackpotForm.getAccountNumber()) > 0) {
			// SETTING PLAYER NAME FOR THE CASHLESS ACCOUNT NUMBER
			try {
				CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator.getService()
						.validateCashlessAccountNo(MainMenuController.jackpotForm.getSiteId(),
								MainMenuController.jackpotForm.getAccountNumber());
				if(cashlessAccountDTO.isSuccess()) {
					String playerCardNumber = cashlessAccountDTO.getPlayerCardNumber();
					if(playerCardNumber != null && !playerCardNumber.trim().isEmpty()) {
						MainMenuController.jackpotForm.setAssociatedPlayerCard(playerCardNumber);
						String playerName = JackpotServiceLocator.getService().getPlayerCardName(
								playerCardNumber, MainMenuController.jackpotForm.getSiteId());
						if(playerName != null && !playerName.trim().isEmpty()) {
							MainMenuController.jackpotForm.setPlayerName(playerName.trim());
						}
					} else if(log.isInfoEnabled()){
						log.info("Cashless account's player number not available. Hence Player Name is not retrieved.");
					}
				}
			} catch (JackpotEngineServiceException e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, "Exception in setPlayerNameForAccountNumber");
				log.error("Exception in setPlayerNameForAccountNumber", e2);
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		}
	}

	private class PendingJackpotMouseListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			
		}

		public void mouseDown(MouseEvent e) {
			try {
				if (log.isInfoEnabled()) {
					log.info("inside mousedown.....");
				}
				Control control = (Control) e.getSource();
				populateScreen(composite);

				if (!(control instanceof SDSImageLabel) && !(control instanceof TSButtonLabel)) {

					return;
				}
				if (control instanceof SDSImageLabel) {
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {

						form.setDispJackpotDTO(jackpotDTOList);

						if (log.isInfoEnabled()) {
							log.info("inside next button");
						}

						int index = composite.getDispJackpotTableViewer().getTable().getSelectionIndex();

						if (index < 0) {
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.PLZ_SELECT_A_SEQ_NO));
							return;
						}
						if (log.isInfoEnabled()) {
							log.info("After credit meter handpay");
						}

						JackpotDTO jpDTO = null;
						try {
							jpDTO = form.getDispJackpotDTO().get(index);
						} catch (RuntimeException e1) {
							e1.printStackTrace();
						}

						if (log.isDebugEnabled()) {
							log.debug("The amount is " + jpDTO.getOriginalAmount());
						}

						if (jpDTO.getGmuDenom() != 0) {
							MainMenuController.jackpotForm.setGmuDenom(jpDTO.getGmuDenom());
							if (log.isDebugEnabled()) {
								log.debug("Set the Game Denom into jackpot form as : "
										+ MainMenuController.jackpotForm.getGmuDenom());
							}
						}
						MainMenuController.jackpotForm.setAssociatedPlayerCard(jpDTO
								.getAssociatedPlayerCard());
						if (jpDTO.getAccountNumber() != null
								&& !jpDTO.getAccountNumber().trim().isEmpty()) {
							MainMenuController.jackpotForm.setAccountNumber(jpDTO.getAccountNumber());
						} else {
							MainMenuController.jackpotForm.setAccountNumber(IAppConstants.EMPTY_STRING);
						}
						setPlayerNameForAccountNumber();
						
						MainMenuController.jackpotForm.setAccountType(jpDTO.getCashlessAccountType());

						MainMenuController.jackpotForm.setSequenceNumber(jpDTO.getSequenceNumber());
						MainMenuController.jackpotForm.setHandPaidAmount(jpDTO.getOriginalAmount());
						MainMenuController.jackpotForm.setOriginalAmount(jpDTO.getOriginalAmount());
						MainMenuController.jackpotForm.setSdsCalculatedAmount(new Long(jpDTO
								.getSdsCalculatedAmount()));
						MainMenuController.jackpotForm.setTransactionDate(jpDTO.getTransactionDate()
								.getTime());
						MainMenuController.jackpotForm.setSlotNo(jpDTO.getAssetConfigNumber());
						MainMenuController.jackpotForm.setJackpotID(jpDTO.getJackpotId());
						MainMenuController.jackpotForm.setJackpotTypeId(jpDTO.getJackpotTypeId());
						MainMenuController.jackpotForm.setJpProgControllerGenerated(jpDTO.isJpProgControllerGenerated());
												
						TableItem tableItem = composite.getDispJackpotTableViewer().getTable().getItem(index);
						MainMenuController.jackpotForm.setSlotLocationNo(tableItem.getText(3).toUpperCase());
						MainMenuController.jackpotForm.setSlotAttentantName(tableItem.getText(5));
						MainMenuController.jackpotForm.setSlotAttentantId(StringUtil.trimAcnfNo(jpDTO
								.getSlotAttendantId()));

						//USED FOR POUCH PAY JACKPOTS, WHEN IT IS A NON CARDED PP JP, SHOW THE SLOT ATTNDT FIELD
						if (jpDTO.getSlotAttendantId() != null) {
							MainMenuController.jackpotForm.setShowSlotAttnId(false);
						} else {
							MainMenuController.jackpotForm.setShowSlotAttnId(true);
						}
						
						//ADDED FIX FOR ENHANCEMENT CR 99606 - 
						//FOR EXP CARDED JPS THE JP INITIATOR IS NOT ALLOWED TO PROCESS HIS OWN JP (IF PARAMETER IS YES)												
						String printEmpId 	= MainMenuController.jackpotForm.getEmployeeIdPrintedSlip();
						String slotAttndId 	= MainMenuController.jackpotForm.getSlotAttentantId();
											
						long expressJPlimit = 0;
						expressJPlimit = (long) Double.parseDouble(MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
						expressJPlimit = ConversionUtil.dollarToCentsRtnLong(String.valueOf(expressJPlimit));

						long pouchPayJPLimit = Long.parseLong(MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.POUCH_PAY_JACKPOT_LIMIT));
						pouchPayJPLimit = ConversionUtil
								.dollarToCentsRtnLong(String.valueOf(pouchPayJPLimit));
						
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("Yes")
							&&  MainMenuController.jackpotForm.getOriginalAmount() < expressJPlimit
							&&	MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_JACKPOT_INITIATOR_TO_PROCESS_EXP_JP).equalsIgnoreCase("No")
							&& (printEmpId!=null && slotAttndId!=null
									&& printEmpId.equalsIgnoreCase(slotAttndId))){
								
							log.info("JP_INITIATOR_NOT_ALLOWED_TO_PROCESS_EXP_JP");
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JP_INITIATOR_NOT_ALLOWED_TO_PROCESS_EXP_JP));
							exitCurrentPendTableScreenNRtnToPrevScreen();							
							return;							
						}
						
						if (log.isInfoEnabled()) {
							log.info("Calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							log.info("Slot no: " + MainMenuController.jackpotForm.getSlotNo());
							log.info("JackpotAssetParamType.ASSET_CONFIG_NUMBER: "
									+ JackpotAssetParamType.ASSET_CONFIG_NUMBER);
							log.info("*******************************************");
						}
						JackpotAssetInfoDTO jackpotAssetInfoDTO = JackpotServiceLocator.getService()
								.getJackpotAssetInfo(
										StringUtil.padAcnfNo(MainMenuController.jackpotForm.getSlotNo()),
										JackpotAssetParamType.ASSET_CONFIG_NUMBER,
										MainMenuController.jackpotForm.getSiteId());

						if (log.isInfoEnabled()) {
							log.info("Values returned after calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							log.info("JackpotAssetInfoDTO values returned: " + jackpotAssetInfoDTO.toString());
							log.info("*******************************************");
						}
						if (jackpotAssetInfoDTO != null) {
							if (jackpotAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
								CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
								initialScr.callPendingJPFirstScreen();
								return;
							}
							if (!jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(
									IAppConstants.ASSET_ONLINE_STATUS)
									&& !jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(
											IAppConstants.ASSET_CET_STATUS)) {

								if (log.isInfoEnabled()) {
									log.info("Pending display jackpot process is not allowed for offline slots");
								}
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
								CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
								initialScr.callPendingJPFirstScreen();
								return;
							}
							if (!jackpotAssetInfoDTO.getSiteId().equals(
									MainMenuController.jackpotForm.getSiteId())) {
								if (log.isInfoEnabled()) {
									log.info("Slot does not belong to this site");
								}
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
								CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
								initialScr.callPendingJPFirstScreen();
								return;
							} else {
								/**
								 * MULTI AREA SUPPORT CHECK IF THE JACKPOT
								 * FILTER IS FOR THE LAST NO OF MINUTES
								 */
								if (filter != null
										&& filter.getType().equalsIgnoreCase(
												"getJackpotSlipsforLastXXMinutes")
										&& MultiAreaSupportUtil.isMultiAreaSupportEnabled()
										&& jackpotAssetInfoDTO.getAreaShortName() != null) {

									if (!MultiAreaSupportUtil.validateSlotForSelectedArea(jackpotAssetInfoDTO
											.getAreaShortName())) {
										if (log.isInfoEnabled()) {
											log.info("Slot does not belong to this area");
										}
										MessageDialogUtil
												.displayTouchScreenErrorMsgDialog(LabelLoader
														.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
										CallInitialScreenUtil initialScr = new CallInitialScreenUtil();
										initialScr.callPendingJPFirstScreen();
										return;
									}
								}
								// THIS FIELD IS MAINLY SET IF THE USER HAS
								// ENTERED A SLOT LOC NO AS THE ENTRY
								MainMenuController.jackpotForm.setSlotNo(jackpotAssetInfoDTO
										.getAssetConfigNumber());
								MainMenuController.jackpotForm.setSealNumber(jackpotAssetInfoDTO
										.getSealNumber());
							}

						}
						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase(
								"Yes")
								&& jpDTO.getAccountNumber() != null
								&& !jpDTO.getAccountNumber().trim().isEmpty()
								&& Long.parseLong(jpDTO.getAccountNumber()) > 0) {
							// VALIDATE CAHSLESS ACCOUNT NUMBER
							CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator.getService()
									.isJackpotOperationAllowed(jpDTO.getAccountNumber(),
											MainMenuController.jackpotForm.getSiteId(),
											AccountTransTypeEnum.JACKPOT.getTransId());
							if (!cashlessAccountDTO.isSuccess()) {
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.JKPT_ACCOUNT_NUMBER)
												+ jpDTO.getAccountNumber()
												+ LabelLoader
														.getLabelValue(MessageKeyConstants.JKPT_PENDING_INVALID_ACCOUNT_NUMBER));
								return;
							}
							// Account number is VALID and hence set to JACKPOT
							// FORM
							MainMenuController.jackpotForm.setAccountNumber(cashlessAccountDTO.getAcntNo());
							MainMenuController.jackpotForm.setAccountType(cashlessAccountDTO
									.getAcntAccessType());

							// SHOWING WARNING MESSAGE IF PLAYER IS NOT ACTIVE
							// IN THE SLOT
							if (MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.PROMPT_FOR_ACTIVE_PLAYER_SESSION_ON_JP)
									.equalsIgnoreCase("Yes")) {
								PlayerSessionDTO playerSessionDTO = JackpotServiceLocator.getService()
										.getActivePlayerSession(jpDTO.getAccountNumber(),
												MainMenuController.jackpotForm.getSlotNo(),
												MainMenuController.jackpotForm.getSiteId());
								if (!playerSessionDTO.isSuccess()) {
									// PLAYER NOT ACTIVE IN THE ASSET
									if (log.isInfoEnabled()) {
										log.info("Player is not active in the slot "
												+ MainMenuController.jackpotForm.getSlotNo());
									}
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.PLAYER_SESSION_NOT_ACTIVE)
											+ MainMenuController.jackpotForm.getSlotNo());
								} else if (log.isDebugEnabled()) {
									// PLAYER ACTIVE IN THE ASSET
									log.debug("Player is active in the slot "
											+ MainMenuController.jackpotForm.getSlotNo());
								}
							}
						} else if (log.isInfoEnabled()) {
							log.info("ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT site param value : "
									+ MainMenuController.jackpotSiteConfigParams
											.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT));
							log.info("Cashless Account Number : " + jpDTO.getAccountNumber());
							log.info("Either Post to eCash param is set to NO or cashless account number is not available");
						}

						if (log.isDebugEnabled()) {
							log.debug("Jackpot Id : "
									+ Integer.parseInt(MainMenuController.jackpotForm.getJackpotID(),
											IAppConstants.HEXA_DECIMAL_RADIX));
						}
						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.PROMPT_FOR_CREDIT_METER_HANDPAY_ON_JP).equalsIgnoreCase("yes")
								&& (MainMenuController.jackpotForm.getJackpotID() != null 
								&& !(Integer.parseInt(MainMenuController.jackpotForm.getJackpotID(), IAppConstants.HEXA_DECIMAL_RADIX) >= IAppConstants.JACKPOT_ID_PROG_START_112 
								&& Integer.parseInt(MainMenuController.jackpotForm.getJackpotID(), IAppConstants.HEXA_DECIMAL_RADIX) <= IAppConstants.JACKPOT_ID_PROG_END_159)
								&& Integer.parseInt(MainMenuController.jackpotForm.getJackpotID(), IAppConstants.HEXA_DECIMAL_RADIX) != IAppConstants.JACKPOT_ID_254_FE)) {
							boolean cMHP = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.IS_THIS_A_CREDIT_METER_HANDPAY),
									composite);

							if (cMHP) {
								if (log.isInfoEnabled()) {
									log.info("-************* IS_THIS_A_CREDIT_METER_HANDPAY is YES");
								}
								MainMenuController.jackpotForm.setJackpotID("FE");
								MainMenuController.jackpotForm
										.setJackpotTypeId(ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT);
							} /*
							 * else {
							 * MainMenuController.jackpotForm.setJackpotID
							 * ("FC");
							 * MainMenuController.jackpotForm.setJackpotTypeId
							 * ((short)7001); }
							 */
						}

						if (jpDTO.getSlotDenomination() != 0) {
							MainMenuController.jackpotForm.setDenomination(ConversionUtil
									.twoPlaceDecimalOf(ConversionUtil.centsToDollar(jpDTO
											.getSlotDenomination())));
						}
						// MainMenuController.jackpotForm.setPlayerCard(jpDTO.getAssociatedPlayerCard());

						MainMenuController.jackpotForm.setMessageId(jpDTO.getMessageId());
						if (log.isDebugEnabled()) {
							log.debug("-------MSG ID: " + jpDTO.getMessageId());
						}

						/*TableItem tableItem = composite.getDispJackpotTableViewer().getTable().getItem(index);
						MainMenuController.jackpotForm.setSlotLocationNo(tableItem.getText(3).toUpperCase());
						MainMenuController.jackpotForm.setSlotAttentantName(tableItem.getText(5));
						MainMenuController.jackpotForm.setSlotAttentantId(StringUtil.trimAcnfNo(jpDTO
								.getSlotAttendantId()));*/

						if (jpDTO.getSlotAttendantId() != null) {
							MainMenuController.jackpotForm.setShowSlotAttnId(false);
						} else {
							MainMenuController.jackpotForm.setShowSlotAttnId(true);
						}

						/*
						 * if(jpDTO.getJackpotTypeId()!=0)
						 * MainMenuController.jackpotForm
						 * .setJackpotTypeId(jpDTO.getJackpotTypeId());
						 */

						if (jpDTO.getBlindAttempt() != null){
							//SETTING THE BLIND ATTEMPTS TO -1 SINCE THE EXPRESS JP PROCESS OF BLIND ATTEMPTS 2 
							//HAS BEEN ABORTED WITHOUT DOING A CANCEL
							if(jpDTO.getBlindAttempt()==IAppConstants.MAX_BLIND_ATTEMPTS) {
								MainMenuController.jackpotForm.setBlindAttempts(IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT);
							}else{
								MainMenuController.jackpotForm.setBlindAttempts(jpDTO.getBlindAttempt());
							}							
						}				

						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("yes")
								&& jackpotDTOList.get(index).getOriginalAmount() < expressJPlimit){
								//&& jackpotDTOList.get(index).getBlindAttempt() != null
								//&& jackpotDTOList.get(index).getBlindAttempt() != 2
								//&& jackpotDTOList.get(index).getBlindAttempt() != IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT) {
							if (log.isInfoEnabled()) {
								log.info("inside express jackpot if condition");
							}

							MainMenuController.jackpotForm.setHandPaidAmount(jackpotDTOList.get(index)
									.getOriginalAmount());
							MainMenuController.jackpotForm.setBlindAttempts(jackpotDTOList.get(index)
									.getBlindAttempt());

							int empNameLen = 0;
							try {
								empNameLen = MainMenuController.jackpotForm.getSlotAttentantName().length();
							} catch (RuntimeException e1) {
							}
							
							//FOR EXP CARDED/NON-CARDED JP MASK THE HPJP AMT FIELD
							MainMenuController.jackpotForm.setDisplayMaskAmt(true);
							
							if (empNameLen == 0) {
								
								//EXP NON CARDED JP FLOW								
								boolean success = MessageDialogUtil
										.displayTouchScreenQuestionDialog(
												LabelLoader
														.getLabelValue(MessageKeyConstants.EXPRESS_JP_GAME_NOT_CARDED),
												composite);
								if (success) {
									
									//COMMENTED FOR CR 99322- For NON CARDED WITHIN EXP LIMIT JPS SHOULD STILL BE MASKED
									/*try {
										JackpotServiceLocator.getService()
												.postNonCardedExpressJackpotBlindAttempts(
														MainMenuController.jackpotForm.getSequenceNumber());
									} catch (JackpotEngineServiceException e1) {
										JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
										handler.handleException(e1, e1.getMessage());
										return;
									} catch (Exception e2) {
										JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
										handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
										log.error("SERVICE_DOWN", e2);
										return;
									}*/
									
									//FOR EXP NON CARDED JP MASK THE HPJP AMT FIELD
									MainMenuController.jackpotForm.setDisplayMaskAmt(true);
									
									//FIX FOR CR 99497 - AUTHORIZER SHOULD ASKED FOR NON CARDED EXPRESS JP
									MainMenuController.jackpotForm.setAuthorizationRequired(true);
									MainMenuController.jackpotForm.setExpressSuccess(false);
									MainMenuController.jackpotForm.setProcessAsExpress(false);
									
									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
										TopMiddleController.getCurrentComposite().dispose();
									}
									new AmountSlotAttendantIdController(
											TopMiddleController.jackpotMiddleComposite, SWT.NONE,
											new AmountSlotAttendantIdForm(), new SDSValidator(getClass(),
													true), false);
								} else {
									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
										TopMiddleController.getCurrentComposite().dispose();
										CallInitialScreenUtil initialScreenUtil = new CallInitialScreenUtil();
										initialScreenUtil.callPendingJPFirstScreen();
									}
								}
							} else {
								//EXP CARDED JP FLOW
								if(MainMenuController.jackpotForm.getBlindAttempts()!=IAppConstants.MAX_BLIND_ATTEMPTS
										&& MainMenuController.jackpotForm.getBlindAttempts()!=IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT){
									
									MainMenuController.jackpotForm.setProcessAsExpress(true);
								}else{
									MainMenuController.jackpotForm.setProcessAsExpress(false);
								}
								MainMenuController.jackpotForm.setAuthorizationRequired(false);
								
								if (TopMiddleController.getCurrentComposite() != null
										&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
									TopMiddleController.getCurrentComposite().dispose();
								}
								new AmountSlotAttendantIdController(
										TopMiddleController.jackpotMiddleComposite, SWT.NONE,
										new AmountSlotAttendantIdForm(), new SDSValidator(getClass(), true),
										false);
							}
						} else if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.POUCH_PAY_JP_ENABLED).equalsIgnoreCase("yes")
								&& jackpotDTOList.get(index).getOriginalAmount() < pouchPayJPLimit) {
							// &&
							// MainMenuController.jackpotForm.getSlotAttentantId()==null)
							// {
							if (log.isInfoEnabled()) {
								log.info("inside pouch pay");
							}							
							MainMenuController.jackpotForm.setSlotAttentantName(jackpotDTOList.get(index)
									.getEmployeeName());

							boolean pouchPay = MessageDialogUtil.displayTouchScreenQuestionDialog(
									LabelLoader.getLabelValue(MessageKeyConstants.WAS_THIS_A_POUCH_PAY),
									composite);
							if (pouchPay) {
								MainMenuController.jackpotForm.setPouchPay(true);
								MainMenuController.jackpotForm
										.setProcessFlagId(ILookupTableConstants.POUCH_PAY_PROCESS_FLAG);
							} else {
								MainMenuController.jackpotForm.setPouchPay(false);
							}
							/*
							 * if(MainMenuController.jackpotForm.getSlotAttentantId
							 * ()!=null &&
							 * MainMenuController.jackpotForm.getSlotAttentantId
							 * ().length()>0) {
							 * MainMenuController.jackpotForm.setPouchPay
							 * (false); }
							 */
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
							}
							new AmountSlotAttendantIdController(TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new AmountSlotAttendantIdForm(), new SDSValidator(getClass(),
											true), false);
						} else {							
							if (log.isInfoEnabled()) {
								log.info("inside normal jackpot processing");
							}							
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
							}
							new AmountSlotAttendantIdController(TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new AmountSlotAttendantIdForm(), new SDSValidator(getClass(),
											true), false);
						}
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {

						if (log.isDebugEnabled()) {
							log.debug("Previous button action clause");
						}
						if (MessageDialogUtil.displayTouchScreenQuestionDialog(
								LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG),
								composite)) {
							exitCurrentPendTableScreenNRtnToPrevScreen();
							return;
						}
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
						boolean response = false;
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(
								LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS),
								composite);
						if (response) {
							if (log.isDebugEnabled()) {
								log.debug("Pending Jackpot process aborted");
							}
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
								new EmployeeShiftSlotDetailsController(
										TopMiddleController.jackpotMiddleComposite, SWT.NONE,
										new EmployeeShiftSlotDetailsForm(),
										new SDSValidator(getClass(), true));
							}
						}
					}
				} else if (control instanceof TSButtonLabel) {
					if (((TSButtonLabel) control).getName().equalsIgnoreCase("nextArrow")) {
						if (pageCount != numOfPages) {
							pageCount++;
							if (pageCount >= numOfPages) {
								composite.getBtnNextRecords().setImage(composite.getDisableNextImage());
								composite.getBtnLastPage().setImage(composite.getDisableLastImage());
							}
							if (pageCount > 1) {
								composite.getBtnPreviousRecords().setImage(composite.getPrevImage());
								composite.getBtnFirstPage().setImage(composite.getFirstImage());
							}
							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
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
					if (((TSButtonLabel) control).getName().equalsIgnoreCase("lastPage")) {
						if (modValue > 0) {
							pageCount = numOfPages;
							dummyPageLabel = LabelLoader
									.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
							initialIndex = totalRecordCount - modValue;
							jackpotDTOList.clear();
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
							composite.getBtnLastPage().setImage(composite.getDisableLastImage());
							composite.getButPrevious().setImage(composite.getPrevImage());
							composite.getBtnFirstPage().setImage(composite.getFirstImage());
							if (previousClicked) {
								previousClicked = false;
							}
						} else {
							pageCount = numOfPages;
							dummyPageLabel = LabelLoader
									.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
							initialIndex = totalRecordCount - numOfRecords;
							jackpotDTOList.clear();
							for (int i = initialIndex; i < totalRecordCount; i++) {
								JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
								jackpotDTOList.add(jackpotDTO);
							}
							initialIndex += numOfRecords;
							composite.getButNext().setImage(composite.getDisableNextImage());
							composite.getBtnLastPage().setImage(composite.getDisableLastImage());
							composite.getButPrevious().setImage(composite.getPrevImage());
							composite.getBtnFirstPage().setImage(composite.getFirstImage());
							if (previousClicked) {
								previousClicked = false;
							}
						}
						form.setDispJackpotDTO(jackpotDTOList);
						populateScreen(composite);
					}

					if (((TSButtonLabel) control).getName().equalsIgnoreCase("firstPage")) {

						if (totalRecordCount >= numOfRecords) {
							pageCount = 1;
							dummyPageLabel = LabelLoader
									.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
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
							composite.getBtnLastPage().setImage(composite.getLastImage());
							composite.getButPrevious().setImage(composite.getDisablePrevImage());
							composite.getBtnFirstPage().setImage(composite.getDisableFirstImage());
							if (!previousClicked) {
								previousClicked = true;
							}
						} else {
							pageCount = 1;
							dummyPageLabel = LabelLoader
									.getLabelValue(LabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT);
							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
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
							composite.getBtnLastPage().setImage(composite.getLastImage());
							composite.getButPrevious().setImage(composite.getDisablePrevImage());
							composite.getBtnFirstPage().setImage(composite.getDisableFirstImage());
							if (!previousClicked) {
								previousClicked = true;
							}
						}
						form.setDispJackpotDTO(jackpotDTOList);
						populateScreen(composite);
					}

					if (((TSButtonLabel) control).getName().equalsIgnoreCase("previousArrow")) {
						if (pageCount != 1) {
							pageCount--;
							composite.getBtnLastPage().setImage(composite.getLastImage());

							if (log.isInfoEnabled()) {
								log.info("The initial index inside previous arrow is " + initialIndex);
							}

							if (pageCount < numOfPages) {
								composite.getBtnNextRecords().setImage(composite.getNextImage());
								composite.getBtnLastPage().setImage(composite.getLastImage());
							}
							if (pageCount <= 1) {
								composite.getBtnPreviousRecords().setImage(composite.getDisablePrevImage());
								composite.getBtnFirstPage().setImage(composite.getDisableFirstImage());
							}

							composite.getTotalPageCount().setText(
									dummyPageLabel + pageCount + " / " + numOfPages);
							composite.layout();
							composite.getDispJackpotTableViewer().getTable().setSelection(-1);

							if (initialIndex > totalRecordCount - modValue) {
								if (log.isInfoEnabled()) {
									log.info("the initial index inside previous firts if" + initialIndex);
								}

								if (!previousClicked) {
									previousClicked = true;

								}
								jackpotDTOList.clear();
								int count = initialIndex - numOfRecords - modValue;
								for (int i = count; i < count + numOfRecords; i++) {
									if (log.isDebugEnabled()) {
										log.debug("The value of i is " + i);
									}
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex -= modValue;

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
									if (i < 10) {
									}
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex -= numOfRecords;
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
									JackpotDTO jackpotDTO = (JackpotDTO) jackpotList.get(i);
									jackpotDTOList.add(jackpotDTO);
								}
								initialIndex -= modValue;
							} else {
								return;
							}
							form.setDispJackpotDTO(jackpotDTOList);
							populateScreen(composite);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Exception: " + ex);
			}
		}

		public void mouseUp(MouseEvent e) {
			
		}
	}
	
	/**
	 * Method to exit the current page and return to the previous screen
	 * @throws Exception
	 */
	private void exitCurrentPendTableScreenNRtnToPrevScreen() throws Exception{
		JackpotUIUtil.disposeCurrentMiddleComposite();
		EmployeeShiftSlotDetailsForm prevScrForm = new EmployeeShiftSlotDetailsForm();
		ObjectMapping objMapping = new ObjectMapping();
		objMapping.copyAlikeFields(EmployeeShiftSlotDetailsController.form, prevScrForm);

		// RESET THE FOLLOWING FIELDS AS THE USER HAS TO
		// RE-ENTER THE FOLLOWING FIELDS AFTER A PREVIOUS
		// ACTION
		MainMenuController.jackpotForm.setSequenceNumber(0);
		MainMenuController.jackpotForm.setLstSlotNo(null);
		MainMenuController.jackpotForm.setSlotNo("");
		MainMenuController.jackpotForm.setSlotLocationNo("");
		MainMenuController.jackpotForm.setNumOfMinutes(0);

		try {
			new EmployeeShiftSlotDetailsController(
					TopMiddleController.jackpotMiddleComposite, SWT.NONE, prevScrForm,
					new SDSValidator(getClass(), true));
			
		} catch (Exception e) {
			log.error("Exception in toExitCurrentrScreenNReturnToPreviousScreen(): ",e);
			throw e;
		}
	}
	
}
