/*****************************************************************************
 * $Id: ManualJackpotProcess.java,v 1.52.1.0.3.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 4:01:12 AM$
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
package com.ballydev.sds.jackpotui.util;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.print.PrintWithGraphics;
import com.ballydev.sds.jackpotui.print.SlipImage;
import com.ballydev.sds.jackpotui.print.SlipPrinting;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.barcodelib.barcode.Barcode;

/**
 * Class to update the database and populate the printDTO to print
 * the slip once the manual jackpot process is done.
 * 
 * @author dambereen
 * @version $Revision: 55$
 */
public class ManualJackpotProcess {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Method to update the details to the DB for a manual jackpot
	 */
	public void processManual() {

		log.info("Inside processManual method");
		JackpotDTO jackpotDTO = new JackpotDTO();

		if (MainMenuController.jackpotForm.getJackpotID() != null) {
			jackpotDTO.setJackpotId(MainMenuController.jackpotForm.getJackpotID());
		} else {
			jackpotDTO.setJackpotId("FC");
		}
		if (MainMenuController.jackpotForm.getHandPaidAmount() != 0) {
			jackpotDTO.setOriginalAmount(0l);
			jackpotDTO.setHpjpAmount(MainMenuController.jackpotForm.getHandPaidAmount());
		}
		if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
			jackpotDTO.setHpjpAmountRounded(MainMenuController.jackpotForm.getRoundedHPJPAmount());
		}
		// SETTING CARDLESS ACCOUNT NUMBER
		if (MainMenuController.jackpotForm.getAccountNumber() != null) {
			jackpotDTO.setAccountNumber(MainMenuController.jackpotForm.getAccountNumber());
		}
		// SETTING CARDLESS ACCOUNT TYPE
		if (MainMenuController.jackpotForm.getAccountType() != null) {
			jackpotDTO.setCashlessAccountType(MainMenuController.jackpotForm.getAccountType());
		}
		if (MainMenuController.jackpotForm.getAssociatedPlayerCard() != null
				&& !MainMenuController.jackpotForm.getAssociatedPlayerCard().trim().isEmpty()) {
			jackpotDTO.setAssociatedPlayerCard(MainMenuController.jackpotForm.getAssociatedPlayerCard());
		} else {
			jackpotDTO.setAssociatedPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
		}
		if (MainMenuController.jackpotForm.getPlayerCard() != null
				&& !MainMenuController.jackpotForm.getPlayerCard().trim().isEmpty()) {
			jackpotDTO.setPlayerCard(MainMenuController.jackpotForm.getPlayerCard());
		} else {
			jackpotDTO.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
		}

		jackpotDTO.setAreaShortName(MainMenuController.jackpotForm.getArea());
		jackpotDTO.setSlipTypeId(ILookupTableConstants.JACKPOT_SLIP_TYPE_ID);
		jackpotDTO.setJackpotTypeId(MainMenuController.jackpotForm.getJackpotTypeId());//
		jackpotDTO.setProcessFlagId(ILookupTableConstants.MANUAL_PROCESS_FLAG);
		jackpotDTO.setSiteId(MainMenuController.jackpotForm.getSiteId());
		jackpotDTO.setSiteNo(MainMenuController.jackpotForm.getSiteNo());
		jackpotDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
		jackpotDTO.setActorLogin(MainMenuController.jackpotForm.getActorLogin());
		jackpotDTO.setAssetConfNumberUsed(MainMenuController.jackpotForm.getAssetConfigNumberUsed());
		jackpotDTO.setWindowNumber(MainMenuController.jackpotForm.getWindowNumber());
		jackpotDTO.setAuthEmployeeId1(MainMenuController.jackpotForm.getAuthEmployeeIdOne());
		jackpotDTO.setAuthEmployeeId2(MainMenuController.jackpotForm.getAuthEmployeeIdTwo());
		jackpotDTO.setTaxTypeId(MainMenuController.jackpotForm.getTaxID());
		jackpotDTO.setPlayerName(MainMenuController.jackpotForm.getPlayerName());
		jackpotDTO.setExpiryDate(MainMenuController.jackpotForm.getExpiryDate());
		jackpotDTO.setLstProgressiveLevel(MainMenuController.jackpotForm.getLstProgressiveLevel());
		jackpotDTO.setProgressiveLevel(MainMenuController.jackpotForm.getProgressiveLevel());
		jackpotDTO.setTaxRateAmount(MainMenuController.jackpotForm.getTaxRateAmount());

		String printerUsed = SDSPreferenceStore
				.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
		if (printerUsed != null) {
			jackpotDTO.setPrinterUsed(printerUsed);
			log.debug("------------- PRINTER USED: " + printerUsed);
		}

		if (MainMenuController.jackpotForm.getArea() != null) {
			jackpotDTO.setAreaLongName(MainMenuController.jackpotForm.getArea());
		}
		if (MainMenuController.jackpotForm.getWinningCombination() != null) {
			jackpotDTO.setWinningComb(MainMenuController.jackpotForm.getWinningCombination());
		}
		if (MainMenuController.jackpotForm.getPayline() != null) {
			jackpotDTO.setPayline(MainMenuController.jackpotForm.getPayline());
		}
		jackpotDTO.setCoinsPlayed(MainMenuController.jackpotForm.getCoinsPlayed());

		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
				IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
			if (MainMenuController.jackpotForm.getShift()
					.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)) {
				jackpotDTO.setShift("D");
			} else if (MainMenuController.jackpotForm.getShift().equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_SWING)) {
				jackpotDTO.setShift("S");
			} else if (MainMenuController.jackpotForm.getShift().equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
				jackpotDTO.setShift("G");
			}
		} else if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
				IAppConstants.SITE_VALUE_SHIFT_DAY)) {
			jackpotDTO.setShift("D");
		} else if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
				IAppConstants.SITE_VALUE_SHIFT_SWING)) {
			jackpotDTO.setShift("S");
		} else if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
				IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
			jackpotDTO.setShift("G");
		}

		if (MainMenuController.jackpotForm.getTaxDeductedAmount() != 0) {
			jackpotDTO.setTaxAmount(MainMenuController.jackpotForm.getTaxDeductedAmount());
			log.info("MainMenuController.jackpotForm.getTaxDeductedAmount()--: "
					+ MainMenuController.jackpotForm.getTaxDeductedAmount());
		}
		if (MainMenuController.jackpotForm.getJackpotNetAmount() != 0) {
			jackpotDTO.setJackpotNetAmount(MainMenuController.jackpotForm.getJackpotNetAmount());
			log.info("MainMenuController.jackpotForm.getJackpotNetAmount()--: "
					+ MainMenuController.jackpotForm.getJackpotNetAmount());
		}
		// jackpotDTO.setMachinePaidAmount(MainMenuController.jackpotForm.getMachinePaidAmount());
		
		jackpotDTO.setInterceptAmount(MainMenuController.jackpotForm.getInterceptAmount());

		JackpotFilter filter = new JackpotFilter();
		// JackpotAssetInfoDTO assetInfoDTO = null;
		if (new Long(
				MainMenuController.jackpotSiteConfigParams
						.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
			if (MainMenuController.jackpotForm.getSlotNo() != null) {
				jackpotDTO.setAssetConfigNumber(MainMenuController.jackpotForm.getSlotNo());
				if (MainMenuController.jackpotForm.getSlotLocationNo() != null)
					jackpotDTO.setAssetConfigLocation(MainMenuController.jackpotForm.getSlotLocationNo()
							.toUpperCase());
				filter.setFieldType("slot");
			}
		} else if (new Long(
				MainMenuController.jackpotSiteConfigParams
						.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
			if (MainMenuController.jackpotForm.getSlotLocationNo() != null) {
				jackpotDTO.setAssetConfigLocation(MainMenuController.jackpotForm.getSlotLocationNo()
						.toUpperCase());
				jackpotDTO.setAssetConfigNumber(MainMenuController.jackpotForm.getSlotNo());
				filter.setFieldType("stand");
			}
		}
		JackpotDTO processedDTO = null;
		try {

			processedDTO = JackpotServiceLocator.getService().processManualJackpot(jackpotDTO, filter);
			if (processedDTO.getSequenceNumber() != 0) {

				// MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_SUCCESS)+
				// " " +
				// LabelLoader.getLabelValue(MessageKeyConstants.WITH_SEQUENCE_NUMBER)+" "
				// + processedDTO.getSequenceNumber());
				log.info("Manual Jackpot process success");
				PrintDTO printDTO = new PrintDTO();

				java.sql.Timestamp timeStamp = DateUtil.getTime(MainMenuController.jackpotForm
						.getTransactionDate());
				String str[] = (timeStamp.toString().trim().replace(".", "_")).split("_");
				double handPaidAmount = ConversionUtil.centsToDollar(jackpotDTO.getHpjpAmount());
				/*
				 * double machinePaidAmount=ConversionUtil
				 * .centsToDollar(jackpotDTO.getMachinePaidAmount());
				 */

				/*
				 * 
				 * printDTO.setSlipSchema(processedDTO.getSlipSchema());
				 * printDTO.setPrinterSchema(processedDTO.getPrinterSchema());
				 * printDTO.setHpjpAmount(handPaidAmount);
				 * printDTO.setSequenceNumber
				 * (Long.toString(processedDTO.getSequenceNumber()));
				 * if(MainMenuController.jackpotForm.getShift()!=null){
				 * if(MainMenuController
				 * .jackpotForm.getShift().equalsIgnoreCase("day"))
				 * printDTO.setShift
				 * (LabelLoader.getLabelValue(LabelKeyConstants.
				 * DAY_SHIFT_LABEL)); else
				 * if(MainMenuController.jackpotForm.getShift
				 * ().equalsIgnoreCase("swing"))
				 * printDTO.setShift(LabelLoader.getLabelValue
				 * (LabelKeyConstants.SWING_SHIFT_LABEL)); else
				 * if(MainMenuController
				 * .jackpotForm.getShift().equalsIgnoreCase("graveyard"))
				 * printDTO
				 * .setShift(LabelLoader.getLabelValue(LabelKeyConstants.
				 * GRAVEYARD_SHIFT_LABEL)); }
				 * 
				 * printDTO.setWinningComb(jackpotDTO.getWinningComb());
				 * printDTO.setWindow(jackpotDTO.getWindowNumber());
				 * printDTO.setPayline(jackpotDTO.getPayline());
				 * printDTO.setCoinsPlayed(jackpotDTO.getCoinsPlayed());
				 * printDTO.setEmployeeName(jackpotDTO.getEmployeeName());
				 * printDTO.setPlayerCard(jackpotDTO.getPlayerCard());
				 * printDTO.setPlayerFirstName(jackpotDTO.getPlayerFirstName());
				 * printDTO
				 * .setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
				 * //printDTO.setMachinePaidAmount(machinePaidAmount);
				 * printDTO.setOriginalAmount
				 * (ConversionUtil.centsToDollar(jackpotDTO
				 * .getOriginalAmount()));
				 * printDTO.setAssetConfigLocation(jackpotDTO
				 * .getAssetConfigLocation());
				 * //printDTO.setTransactionDate(str[0]);
				 * 
				 * printDTO.setPlayerName(MainMenuController.jackpotForm
				 * .getPlayerName());
				 * printDTO.setPlayerCard(MainMenuController.jackpotForm
				 * .getPlayerCard());
				 * printDTO.setSlotAttendantId(MainMenuController.jackpotForm
				 * .getSlotAttentantId());
				 * 
				 * printDTO.setTransactionDate(com.ballydev.sds.framework.util.
				 * DateUtil.getDateString(DateUtil
				 * .getTime(MainMenuController.jackpotForm
				 * .getTransactionDate()), IAppConstants.SLIP_DATE_FORMAT));
				 * Date currentDate = new Date();
				 * 
				 * printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil
				 * .getDateString(currentDate, IAppConstants.SLIP_DATE_FORMAT));
				 */

				// printDTO = setPrintDTOValues(processedDTO) ;

				// SETTING THE FIELDS THAT ARE GENERATED AT THE ENGINE SIDE
				jackpotDTO.setSequenceNumber(processedDTO.getSequenceNumber());
				jackpotDTO.setSlipSchema(processedDTO.getSlipSchema());
				jackpotDTO.setPrinterSchema(processedDTO.getPrinterSchema());
				jackpotDTO.setTransactionDate(processedDTO.getTransactionDate());

				// MSG IS IS SET AND USED FOR SENDING THE JP ADJUSTMENT TO
				// MARKETING
				MainMenuController.jackpotForm.setMessageId(processedDTO.getMessageId());

				jackpotDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
				String slipPrintType = null;
				if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("No")
						|| SDSPreferenceStore
								.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY)
								.toUpperCase().contains(IAppConstants.EPSON_PRINTER_SUBSTRING)) {
					slipPrintType = IAppConstants.JACKPOT_PRINT_SLIP_TYPE;
					printDTO = SlipUtil.setPrintDTOValues(processedDTO);
				} else if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("Yes")) {
					slipPrintType = IAppConstants.CHECK_PRINT_SLIP_TYPE;
					printDTO = SlipUtil.setCheckDTOValues(processedDTO);
				}

				// printDTO = SlipUtil.setPrintDTOValues(jackpotDTO);

				SlipImage slipImage = new SlipImage();
				String image = null;
				try {
					// EPSON PRINTER
					if (SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY)
							.toUpperCase().contains(IAppConstants.EPSON_PRINTER_SUBSTRING)) {
						if (log.isInfoEnabled()) {
							log.info("Epson printer was selected, calling buildSlipImage method");
						}
						image = slipImage.buildSlipImage(printDTO, slipPrintType, SlipUtil.jackpotSlipMap,
								MainMenuController.jackpotForm.getHandPaidAmount(),
								MainMenuController.jackpotForm.getJackpotNetAmount());
						SlipPrinting slipPrinting = new SlipPrinting();
						slipPrinting.printSlip(image);
					} else if (SDSPreferenceStore.getStringStoreValue(
							IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).contains(
							IAppConstants.LASER_JET_PRINTER_SUBSTRING)) {
						if (log.isInfoEnabled()) {
							log.info("Laser Jet printer was selected, calling getLaserPrinterImage method");
						}
						if (slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
							image = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType,
									SlipUtil.checkSlipMap,
									MainMenuController.jackpotForm.getHandPaidAmount(),
									MainMenuController.jackpotForm.getJackpotNetAmount(),
									MainMenuController.jackpotForm.getExpiryDate());
						} else if (slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) {
							image = SlipImage.getLaserPrinterImage(printDTO, slipPrintType,
									SlipUtil.jackpotSlipMap,
									MainMenuController.jackpotForm.getHandPaidAmount(),
									MainMenuController.jackpotForm.getJackpotNetAmount());
						}
						PrintWithGraphics printWithGraphics = new PrintWithGraphics();
						Image barcodeImg = null;
						if (MainMenuController.jackpotForm.isPrintBarcode()) {
							Barcode barcode = JackpotServiceLocator.getService().createBarcode(
									MainMenuController.jackpotForm.getSiteId(),
									MainMenuController.jackpotForm.getJackpotNetAmount(),
									processedDTO.getSequenceNumber(), processedDTO.getBarEncodeFormat());
							String initialBarFile = processedDTO.getBarEncodeFormat() + ".jpg";
							barcode.createBarcodeImage(initialBarFile);

							File barcodeFile = new File(initialBarFile);
							barcodeImg = ImageIO.read(barcodeFile); 
						}
						printWithGraphics.printTextImage(image, barcodeImg, slipPrintType);
					} else {
						if (log.isInfoEnabled()) {
							log.info("Default was selected, calling getLaserPrinterImage method");
						}
						if (slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
							image = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType,
									SlipUtil.checkSlipMap,
									MainMenuController.jackpotForm.getHandPaidAmount(),
									MainMenuController.jackpotForm.getJackpotNetAmount(),
									MainMenuController.jackpotForm.getExpiryDate());
						} else if (slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) {
							image = SlipImage.getLaserPrinterImage(printDTO, slipPrintType,
									SlipUtil.jackpotSlipMap,
									MainMenuController.jackpotForm.getHandPaidAmount(),
									MainMenuController.jackpotForm.getJackpotNetAmount());
						}
						PrintWithGraphics printWithGraphics = new PrintWithGraphics();
						Image barcodeImg = null;
						if (MainMenuController.jackpotForm.isPrintBarcode()) {
							Barcode barcode = JackpotServiceLocator.getService().createBarcode(
									MainMenuController.jackpotForm.getSiteId(),
									MainMenuController.jackpotForm.getJackpotNetAmount(),
									processedDTO.getSequenceNumber(), processedDTO.getBarEncodeFormat());
							String initialBarFile = processedDTO.getBarEncodeFormat() + ".jpg";
							barcode.createBarcodeImage(initialBarFile);

							File barcodeFile = new File(initialBarFile);
							barcodeImg = ImageIO.read(barcodeFile); 
						}
						printWithGraphics.printTextImage(image, barcodeImg, slipPrintType);
					}
				} catch (Exception e) {
					log.error(e);
				}

				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
						.getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_SUCCESS)
						+ " "
						+ LabelLoader.getLabelValue(MessageKeyConstants.WITH_SEQUENCE_NUMBER)
						+ " "
						+ processedDTO.getSequenceNumber()
						+ " "
						+ LabelLoader.getLabelValue(MessageKeyConstants.ADDED_TO_TRANS));

				try {
					if (log.isInfoEnabled()) {
						log.info("BEFORE SENDING AUDIT TRAIL INFO");
					}
					if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
						Util.sendDataToAuditTrail(
								IAppConstants.MODULE_NAME,
								LabelLoader.getLabelValue(LabelKeyConstants.MANUAL_JACKPOT_SCREEN),
								LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_JP_FIELD),
								String.valueOf(0),
								String.valueOf(ConversionUtil
										.centsToDollar(MainMenuController.jackpotForm.getRoundedHPJPAmount())),
								LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID) + ": "
										+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip() + " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "
										+ MainMenuController.jackpotForm.getSlotNo() + " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO) + ": "
										+ processedDTO.getSequenceNumber(), EnumOperation.MANUAL_JACKPOT,
								MainMenuController.jackpotForm.getSlotNo());
					} else {
						Util.sendDataToAuditTrail(
								IAppConstants.MODULE_NAME,
								LabelLoader.getLabelValue(LabelKeyConstants.MANUAL_JACKPOT_SCREEN),
								LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_JP_FIELD),
								String.valueOf(0),
								String.valueOf(ConversionUtil
										.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())),
								LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID) + ": "
										+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip() + " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO) + ": "
										+ MainMenuController.jackpotForm.getSlotNo() + " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO) + ": "
										+ processedDTO.getSequenceNumber(), EnumOperation.MANUAL_JACKPOT,
								MainMenuController.jackpotForm.getSlotNo());
					}
					if (log.isInfoEnabled()) {
						log.info("AFTER SENDING AUDIT TRAIL INFO");
					}
				} catch (Exception e) {
					log.error("Exception when sending audit trail info", e);
				}

				// BY DEFAULT PT 10 SHOULD BE SENT FOR A MANUAL JP TRANSACTION
				// DO NOT SEND FOR CANCEL CREDIT JPS
				if (MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
					postManualJackpot(jackpotDTO.getAssetConfigNumber());
				} else if (log.isInfoEnabled()) {
					log.info("Adjustment is NOT SENT to Marketing AS CCJP");
				}

			} else {
				if (log.isInfoEnabled()) {
					log.info("Seq no returned by Jackpot Engine for Manual Process is 0");
				}
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
						.getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_FAILED));
				if (log.isInfoEnabled()) {
					log.info("Manual Jackpot process failed");
				}
			}
		} catch (JackpotEngineServiceException e1) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e1, e1.getMessage());
			log.error("Exception: " + e1);
			return;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			// Chk on the message passed
			handler.handleException(e2, "Exception in process manual Jackpot"); 
			log.error("Exception: " + e2);
		}
	}

	/**
	 * Method to post adjustnet to Marketing for a Manual Jackpot
	 */
	private void postManualJackpot(String slotNo) {
		if (log.isInfoEnabled()) {
			log.info("***Inside Post Manual JP****");
			log.info("PlayerCard" + MainMenuController.jackpotForm.getPlayerCard() + "\nHPJP Amount"
					+ MainMenuController.jackpotForm.getHandPaidAmount() + "Message Id"
					+ MainMenuController.jackpotForm.getMessageId());
		}
		try {
			if (log.isInfoEnabled()) {
				log.info("Before calling web method postManualJackpot");
				log.info("Site Id: " + MainMenuController.jackpotForm.getSiteId());

				log.info("Jp Net Amount: " + MainMenuController.jackpotForm.getJackpotNetAmount());
				log.info("Slot No: " + slotNo);
			}
			int marketingResp = 0;
			if (MainMenuController.jackpotForm.getPlayerCard() != null) {
				if (log.isInfoEnabled()) {
					log.info("Player Card: " + MainMenuController.jackpotForm.getPlayerCard());
				}
				marketingResp = JackpotServiceLocator.getService().postManualJackpot(
						MainMenuController.jackpotForm.getSiteId(),
						MainMenuController.jackpotForm.getPlayerCard(),
						MainMenuController.jackpotForm.getJackpotNetAmount(), slotNo,
						MainMenuController.jackpotForm.getMessageId(),
						MainMenuController.jackpotForm.getJackpotID());
			} else {
				if (log.isInfoEnabled()) {
					log.info("Player Card: " + IAppConstants.DEFAULT_PLAYER_CARD);
				}
				marketingResp = JackpotServiceLocator.getService().postManualJackpot(
						MainMenuController.jackpotForm.getSiteId(), IAppConstants.DEFAULT_PLAYER_CARD,
						MainMenuController.jackpotForm.getJackpotNetAmount(), slotNo,
						MainMenuController.jackpotForm.getMessageId(),
						MainMenuController.jackpotForm.getJackpotID());
			}

			if (log.isInfoEnabled()) {
				log.info("After calling web method postManualJackpot - Mkting Resp: " + marketingResp);
			}
			/*
			 * if(marketingResp > 0){
			 * MessageDialogUtil.displayTouchScreenInfoDialog
			 * (LabelLoader.getLabelValue(MessageKeyConstants.
			 * SUCCESS_POSTING_JACKPOT_ADJUSTMENT_PLAYER_DETAILS_TO_MARKETING));
			 * }
			 */
		} catch (JackpotEngineServiceException e) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception Calling wed method postManualJackpot");
			return;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN", e2);
			return;
		}
	}
}
