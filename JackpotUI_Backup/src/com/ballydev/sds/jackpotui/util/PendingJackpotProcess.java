/*****************************************************************************
 * $Id: PendingJackpotProcess.java,v 1.66.1.1.3.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
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
import java.sql.Timestamp;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
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
 * Class to update the database once the pending jackpot process is done.
 * 
 * @author dambereen
 * @version $Revision: 70$
 */
public class PendingJackpotProcess {	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Method to set the fields of the JackpotDTO
	 */
	String statusDesc = null;

	short statusFlagId = 0;

	/**
	 * method calls the process pending jackpot web method
	 */
	public void processPending() {

		try {
			if (log.isInfoEnabled()) {
				log.info("Calling web method getJackpotStatus");
				log.info("Seq no: " + MainMenuController.jackpotForm.getSequenceNumber());
				log.info("Site id: " + MainMenuController.jackpotForm.getSiteId());
			}
			statusFlagId = JackpotServiceLocator.getService().getJackpotStatus(
					MainMenuController.jackpotForm.getSequenceNumber(),
					MainMenuController.jackpotForm.getSiteId());

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

			case ILookupTableConstants.PENDING_STATUS_ID:
				statusDesc = ILookupTableConstants.PENDING_ST_DESC;
				break;
			}

			if (log.isInfoEnabled()) {
				log.info("After Calling web method getJackpotStatus");
			}
		} catch (JackpotEngineServiceException e1) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e1, e1.getMessage());
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN", e2);
			return;
		}
		if (statusDesc != null && statusDesc.equalsIgnoreCase(ILookupTableConstants.PENDING_ST_DESC)) {

			JackpotDTO jackpotDTO = new JackpotDTO();
			try {

				if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
						IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
					if (MainMenuController.jackpotForm.getShift().equalsIgnoreCase(
							IAppConstants.SITE_VALUE_SHIFT_DAY)) {
						jackpotDTO.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY_CODE);
					} else if (MainMenuController.jackpotForm.getShift().equalsIgnoreCase(
							IAppConstants.SITE_VALUE_SHIFT_SWING)) {
						jackpotDTO.setShift(IAppConstants.SITE_VALUE_SHIFT_SWING_CODE);
					} else if (MainMenuController.jackpotForm.getShift().equalsIgnoreCase(
							IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
						jackpotDTO.setShift(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD_CODE);
					}

				} else if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
						IAppConstants.SITE_VALUE_SHIFT_DAY)) {
					jackpotDTO.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY_CODE);
				} else if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
						IAppConstants.SITE_VALUE_SHIFT_SWING_CODE)) {
					jackpotDTO.setShift("S");
				} else if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
						IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
					jackpotDTO.setShift(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD_CODE);
				}
				//SET ACCOUNT NO(IF USER ENTERED)TO THE RTND JP DTO FOR A CONTROLLER GENERATED JP
				//AS CONTROLLER CREATED JP WILL NOT HAVE ACCOUNT DETAILS 
				if(MainMenuController.jackpotForm.isJpProgControllerGenerated()){
					// SETTING CARDLESS ACCOUNT NUMBER
					if (MainMenuController.jackpotForm.getAccountNumber() != null
							&& !MainMenuController.jackpotForm.getAccountNumber().trim().isEmpty()) {
						jackpotDTO.setAccountNumber(MainMenuController.jackpotForm.getAccountNumber());
					}
					// SETTING CARDLESS ACCOUNT TYPE
					if (MainMenuController.jackpotForm.getAccountType() != null
							&& !MainMenuController.jackpotForm.getAccountType().trim().isEmpty()) {
						jackpotDTO.setCashlessAccountType(MainMenuController.jackpotForm.getAccountType());
					}
				}
				
				jackpotDTO.setAreaShortName(MainMenuController.jackpotForm.getArea());
				jackpotDTO.setSealNumber(MainMenuController.jackpotForm.getSealNumber());
				jackpotDTO.setSiteId(MainMenuController.jackpotForm.getSiteId());
				jackpotDTO.setProcessFlagId(MainMenuController.jackpotForm.getProcessFlagId());
				jackpotDTO.setInsertPouchPayAttn(MainMenuController.jackpotForm.isInsertPouchPayAttn());
				if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("No")) {
					jackpotDTO.setCashDeskLocation(LabelLoader
							.getLabelValue(LabelKeyConstants.SLIP_PROCESS_BY_SDS));
				}
				jackpotDTO.setSequenceNumber(MainMenuController.jackpotForm.getSequenceNumber());
				jackpotDTO.setHpjpAmount(MainMenuController.jackpotForm.getHandPaidAmount());
				if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
					jackpotDTO.setHpjpAmountRounded(MainMenuController.jackpotForm.getRoundedHPJPAmount());
				}
				jackpotDTO.setExpiryDate(MainMenuController.jackpotForm.getExpiryDate());
				/*
				 * jackpotDTO.setMachinePaidAmount(MainMenuController.jackpotForm
				 * .getMachinePaidAmount());
				 */
				jackpotDTO.setWinningComb(MainMenuController.jackpotForm.getWinningCombination());
				if (MainMenuController.jackpotForm.getCoinsPlayed() != 0) {
					jackpotDTO.setCoinsPlayed(MainMenuController.jackpotForm.getCoinsPlayed());
				}
				jackpotDTO.setPayline(MainMenuController.jackpotForm.getPayline());
				jackpotDTO.setActorLogin(SDSApplication.getLoggedInUserID());
				jackpotDTO.setTaxTypeId(MainMenuController.jackpotForm.getTaxID());
				jackpotDTO.setWindowNumber(MainMenuController.jackpotForm.getWindowNumber());
				jackpotDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
				jackpotDTO.setAssetConfigNumber(MainMenuController.jackpotForm.getSlotNo());
				if (MainMenuController.jackpotForm.getSlotLocationNo() != null) {
					jackpotDTO.setAssetConfigLocation(MainMenuController.jackpotForm.getSlotLocationNo()
							.toUpperCase());
				}

				jackpotDTO.setAssetConfNumberUsed(MainMenuController.jackpotForm.getAssetConfigNumberUsed());
				jackpotDTO.setTaxAmount(MainMenuController.jackpotForm.getTaxDeductedAmount());
				jackpotDTO.setTaxRateAmount(MainMenuController.jackpotForm.getTaxRateAmount());

				if (MainMenuController.jackpotForm.getOriginalAmount() != 0) {
					jackpotDTO.setOriginalAmount(MainMenuController.jackpotForm.getOriginalAmount());
				}

				if (MainMenuController.jackpotForm.getJackpotNetAmount() != 0) {
					jackpotDTO.setJackpotNetAmount(MainMenuController.jackpotForm.getJackpotNetAmount());
				}
				
				String printerUsed = SDSPreferenceStore
						.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
				if (printerUsed != null)
					jackpotDTO.setPrinterUsed(printerUsed);

				jackpotDTO.setJackpotId(MainMenuController.jackpotForm.getJackpotID());
				jackpotDTO.setJackpotTypeId(MainMenuController.jackpotForm.getJackpotTypeId());
				jackpotDTO.setAuthEmployeeId1(MainMenuController.jackpotForm.getAuthEmployeeIdOne());
				jackpotDTO.setAuthEmployeeId2(MainMenuController.jackpotForm.getAuthEmployeeIdTwo());
				if (MainMenuController.jackpotForm.getAssociatedPlayerCard() != null
						&& !MainMenuController.jackpotForm.getAssociatedPlayerCard().trim().isEmpty()) {
					jackpotDTO.setAssociatedPlayerCard(MainMenuController.jackpotForm
							.getAssociatedPlayerCard());
				}
				jackpotDTO.setPlayerCard(MainMenuController.jackpotForm.getPlayerCard());
				jackpotDTO.setPlayerName(MainMenuController.jackpotForm.getPlayerName());
				jackpotDTO.setSiteNo(MainMenuController.jackpotForm.getSiteNo());

				if (MainMenuController.jackpotForm.getSlotAttentantId() != null
						&& MainMenuController.jackpotForm.getSlotAttentantId().length() > 0) {

					jackpotDTO.setSlotAttendantId(StringUtil.padLeftZeros(MainMenuController.jackpotForm
							.getSlotAttentantId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				// SETTING OVERIDE AUTHURIZATION EMPLOYEE ID
				if (MainMenuController.jackpotForm.getAmountAuthEmpId() != null
						&& MainMenuController.jackpotForm.getAmountAuthEmpId().length() > 0) {
					jackpotDTO.setAmountAuthEmpId(StringUtil.padLeftZeros(MainMenuController.jackpotForm
							.getAmountAuthEmpId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				}
				jackpotDTO.setExpiryDate(MainMenuController.jackpotForm.getExpiryDate());
				
				// Custom enhancement for MVG
				jackpotDTO.setInterceptAmount(MainMenuController.jackpotForm.getInterceptAmount());
				
				if (MainMenuController.jackpotForm.getSlotAttentantFirstName() != null) {
					jackpotDTO.setSlotAttendantFirstName(MainMenuController.jackpotForm
							.getSlotAttentantFirstName());
				}
				if (MainMenuController.jackpotForm.getSlotAttentantLastName() != null) {
					jackpotDTO.setSlotAttendantLastName(MainMenuController.jackpotForm
							.getSlotAttentantLastName());
				}
				JackpotDTO processedDTO = null;

				java.sql.Timestamp timeStamp = DateUtil.getTime(MainMenuController.jackpotForm
						.getTransactionDate());
				String str[] = (timeStamp.toString().trim().replace(".", "_")).split("_");

				processedDTO = JackpotServiceLocator.getService().processJackpot(jackpotDTO);
				PrintDTO printDTO = new PrintDTO();
				/*
				 * printDTO.setSlipSchema(processedDTO.getSlipSchema());
				 * printDTO.setPrinterSchema(processedDTO.getPrinterSchema());
				 * printDTO.setHpjpAmount(ConversionUtil.centsToDollar(jackpotDTO
				 * .getHpjpAmount()));
				 * printDTO.setSequenceNumber(Long.toString(jackpotDTO.getSequenceNumber()));
				 * if(MainMenuController.jackpotForm.getShift()!=null){
				 * if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("day"))
				 * printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
				 * else
				 * if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("swing"))
				 * printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
				 * else
				 * if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("graveyard"))
				 * printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL)); }
				 * 
				 * printDTO.setShift(MainMenuController.jackpotForm.getShift());
				 * printDTO.setWinningComb(jackpotDTO.getWinningComb());
				 * printDTO.setWindow(jackpotDTO.getWindowNumber());
				 * printDTO.setPayline(jackpotDTO.getPayline());
				 * printDTO.setCoinsPlayed(jackpotDTO.getCoinsPlayed());
				 * printDTO.setEmployeeName(jackpotDTO.getEmployeeName());
				 * printDTO.setAssetConfigNumber(jackpotDTO.getAssetConfigNumber());
				 * printDTO.setMachinePaidAmount(ConversionUtil
				 * .centsToDollar(jackpotDTO.getMachinePaidAmount()));
				 * printDTO.setOriginalAmount(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getOriginalAmount()));
				 * printDTO.setAssetConfigLocation(jackpotDTO
				 * .getAssetConfigLocation());
				 * //printDTO.setTransactionDate(str[0]);
				 * printDTO.setPlayerCard(MainMenuController.jackpotForm
				 * .getPlayerCard());
				 * printDTO.setPlayerName(MainMenuController.jackpotForm.getPlayerName());
				 * printDTO.setSlotAttendantId(MainMenuController.jackpotForm
				 * .getSlotAttentantId());
				 * printDTO.setTransactionDate(com.ballydev.sds.framework.util.DateUtil.getDateString(DateUtil
				 * .getTime(MainMenuController.jackpotForm
				 * .getTransactionDate()), IAppConstants.SLIP_DATE_FORMAT));
				 * Date currentDate = new Date();
				 * 
				 * printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getDateString(currentDate,
				 * IAppConstants.SLIP_DATE_FORMAT));
				 */

				if (processedDTO != null && processedDTO.isPostedSuccessfully()) {

					log.debug("Pending JP Response from de DB: " + processedDTO.isPostedSuccessfully());

					log.info("************ Slot ATt 1st name: " + processedDTO.getSlotAttendantFirstName());
					log.info("************ Slot ATt last name: " + processedDTO.getSlotAttendantLastName());

					processedDTO.setAssociatedPlayerCard(MainMenuController.jackpotForm
							.getAssociatedPlayerCard());

					/*
					 * MessageDialogUtil
					 * .displayTouchScreenInfoDialog(LabelLoader
					 * .getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_SUCCESS));
					 */
					jackpotDTO.setSlipSchema(processedDTO.getSlipSchema());
					jackpotDTO.setPrinterSchema(processedDTO.getPrinterSchema());
					// printDTO = SlipUtil.setPrintDTOValues(jackpotDTO); /**
					// this was used before */
					processedDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm
							.getEmployeeIdPrintedSlip());
					
					String slipPrintType = null;
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("No")
							|| SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(
									IAppConstants.EPSON_PRINTER_SUBSTRING)) {
						slipPrintType = IAppConstants.JACKPOT_PRINT_SLIP_TYPE;
						printDTO = SlipUtil.setPrintDTOValues(processedDTO);
					} else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("Yes")) {
						slipPrintType = IAppConstants.CHECK_PRINT_SLIP_TYPE;
						printDTO = SlipUtil.setCheckDTOValues(processedDTO);
					}
					SlipImage slipImage = new SlipImage();
					String image = null;

					try {
						if (SDSPreferenceStore.getStringStoreValue(
								IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(
								IAppConstants.EPSON_PRINTER_SUBSTRING)) {
							log.info("Epson printer was selected, calling buildSlipImage method");
							image = slipImage.buildSlipImage(printDTO, IAppConstants.JACKPOT_PRINT_SLIP_TYPE,
									SlipUtil.jackpotSlipMap, MainMenuController.jackpotForm
											.getHandPaidAmount(), MainMenuController.jackpotForm
											.getJackpotNetAmount());
							SlipPrinting slipPrinting = new SlipPrinting();
							slipPrinting.printSlip(image);
						} else if (SDSPreferenceStore.getStringStoreValue(
								IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).contains(
								IAppConstants.LASER_JET_PRINTER_SUBSTRING)) {
							log.info("Laser Jet printer was selected, calling getLaserPrinterImage method");
							
							if(slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
								image = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType, SlipUtil.checkSlipMap,
									MainMenuController.jackpotForm.getHandPaidAmount(),
									MainMenuController.jackpotForm.getJackpotNetAmount(), MainMenuController.jackpotForm.getExpiryDate());
							} else if(slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) {
								image = SlipImage.getLaserPrinterImage(printDTO, slipPrintType, SlipUtil.jackpotSlipMap, 
										MainMenuController.jackpotForm.getHandPaidAmount(), 
										MainMenuController.jackpotForm.getJackpotNetAmount());
							}
							PrintWithGraphics printWithGraphics = new PrintWithGraphics();
							Image barcodeImg = null;
							if (MainMenuController.jackpotForm.isPrintBarcode()) {
								Barcode barcode = JackpotServiceLocator.getService().createBarcode(MainMenuController.jackpotForm
										.getSiteId(), MainMenuController.jackpotForm.getJackpotNetAmount(),
										MainMenuController.jackpotForm.getSequenceNumber(), processedDTO
												.getBarEncodeFormat());
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
							
							if(slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
								image = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType, SlipUtil.checkSlipMap,
										MainMenuController.jackpotForm.getHandPaidAmount(),
										MainMenuController.jackpotForm.getJackpotNetAmount(), MainMenuController.jackpotForm.getExpiryDate());
							} else if(slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) { 
								image = SlipImage.getLaserPrinterImage(printDTO, slipPrintType, SlipUtil.jackpotSlipMap, 
										MainMenuController.jackpotForm.getHandPaidAmount(), 
										MainMenuController.jackpotForm.getJackpotNetAmount());
							}
							PrintWithGraphics printWithGraphics = new PrintWithGraphics();
							Image barcodeImg = null;
							if (MainMenuController.jackpotForm.isPrintBarcode()) {
								Barcode barcode = JackpotServiceLocator.getService().createBarcode(MainMenuController.jackpotForm
										.getSiteId(), MainMenuController.jackpotForm.getJackpotNetAmount(),
										MainMenuController.jackpotForm.getSequenceNumber(), processedDTO
												.getBarEncodeFormat());
								String initialBarFile = processedDTO.getBarEncodeFormat() + ".jpg";
								barcode.createBarcodeImage(initialBarFile);

								File barcodeFile = new File(initialBarFile);
								barcodeImg = ImageIO.read(barcodeFile); 
							}
							printWithGraphics.printTextImage(image, barcodeImg, slipPrintType);
						}
					} catch (Exception e) {
						JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
						handler.handleException(e, MessageKeyConstants.ERROR_PRINTING_SLIP_CHK_PRINTER_LOGS);
						log.error(e);
					}

					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_SUCCESS)
							+ " "
							+ LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SEQUENCE)
							+ " "
							+ MainMenuController.jackpotForm.getSequenceNumber() + ".");

					log.info("BEFORE SENDING AUDIT TRAIL INFO");
					try {

						String audOldValue = String.valueOf(ConversionUtil
								.centsToDollar(MainMenuController.jackpotForm.getOriginalAmount()));
						String audNewValue = String.valueOf(ConversionUtil
								.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()));
						String audFieldName = LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_JP_FIELD);

						if (MainMenuController.jackpotForm.getOriginalAmount() == MainMenuController.jackpotForm
								.getHandPaidAmount()) {
							audFieldName = null;
							audOldValue = null;
							audNewValue = null;
						} else if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
							audNewValue = String.valueOf(ConversionUtil
									.centsToDollar(MainMenuController.jackpotForm.getRoundedHPJPAmount()));
						}
						Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME, LabelLoader
								.getLabelValue(LabelKeyConstants.PENDING_JACKPOT_SCREEN), audFieldName,
								audOldValue, audNewValue, LabelLoader
										.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)
										+ ": "
										+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()
										+ " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO)
										+ ": "
										+ MainMenuController.jackpotForm.getSlotNo()
										+ " ,"
										+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO)
										+ ": "
										+ processedDTO.getSequenceNumber(), EnumOperation.PENDING_JACKPOT,
								MainMenuController.jackpotForm.getSlotNo());
					} catch (Exception e) {
						log.error("Exception when sending audit trail info", e);
					}
					if (log.isInfoEnabled()) {
						log.info("AFTER SENDING AUDIT TRAIL INFO");

						log.info("Org Amt: " + MainMenuController.jackpotForm.getOriginalAmount());
						log.info("New HPJP: " + MainMenuController.jackpotForm.getHandPaidAmount());
						log.info("Associated card: "
								+ MainMenuController.jackpotForm.getAssociatedPlayerCard());
						log.info("pl card: " + MainMenuController.jackpotForm.getPlayerCard());
					}

					// SEND PT 11 BASED ON BELOW CONDITIONS EXCEPT WHEN IT IS A
					// CANCEL CREDIT JP
					if (MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT) {
						// COND CHECKS TO SEND JACKPOT ADJUSTMENT TO MARKETING FOR
						// SENDING PT11
						if (MainMenuController.jackpotForm.getOriginalAmount() != MainMenuController.jackpotForm
								.getJackpotNetAmount()) {

							if (log.isInfoEnabled()) {
								log.info("Adjustment is SENT to Marketing");
							}
							processJackpotAdjustment(jackpotDTO.getAssetConfigNumber());

						} else if (MainMenuController.jackpotForm.getAssociatedPlayerCard() != null
								&& MainMenuController.jackpotForm.getAssociatedPlayerCard().matches("0+")
								&& MainMenuController.jackpotForm.getPlayerCard() != null
								&& !MainMenuController.jackpotForm.getPlayerCard().matches("0+")) {

							if (log.isInfoEnabled()) {
								log.info("Adjustment is SENT to Marketing");
							}
							processJackpotAdjustment(jackpotDTO.getAssetConfigNumber());
						} else if (MainMenuController.jackpotForm.getAssociatedPlayerCard() != null
								&& !MainMenuController.jackpotForm.getAssociatedPlayerCard().matches("0+")
								&& MainMenuController.jackpotForm.getPlayerCard() != null
								&& MainMenuController.jackpotForm.getPlayerCard().matches("0+")) {

							log.info("Adjustment is SENT to Marketing");
							processJackpotAdjustment(jackpotDTO.getAssetConfigNumber());
						} else if (MainMenuController.jackpotForm.getAssociatedPlayerCard() != null
								&& !MainMenuController.jackpotForm.getAssociatedPlayerCard().matches("0+")
								&& MainMenuController.jackpotForm.getPlayerCard() != null
								&& !MainMenuController.jackpotForm.getPlayerCard().matches("0+")) {

							if (log.isInfoEnabled()) {
								log.info("Adjustment is SENT to Marketing");
							}
							processJackpotAdjustment(jackpotDTO.getAssetConfigNumber());
						} else if (log.isInfoEnabled()) {
							log.info("Adjustment is NOT SENT to Marketing");
						}
					} else if (log.isInfoEnabled()) {
						log.info("Adjustment is NOT SENT to Marketing AS CCJP");
					}

					// POSTING JACKOT AMOUNT TO PROGRESSIVE ENGINE IF ITS A PROG
					// JACKOT WHICH WE PROCESSED
					/*postToProgressiveEngine(processedDTO);*/

				} else {
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.JACKPOT_PROCESS_FAILED));
				}
			} catch (JackpotEngineServiceException e1) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				log.error("JackpotEngineServiceException", e1);
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}

		} else if (statusDesc != null
				&& ((MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("No") && statusDesc
						.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC))
						|| (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("Yes") && statusDesc
								.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC)) // FOR CASHIER DESK FLOW
						|| statusDesc.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC) || statusDesc
						.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC))) {
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
					.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PRINTED));
		} else if (statusDesc != null && statusDesc.equalsIgnoreCase(ILookupTableConstants.VOID_ST_DESC)) {
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
					.getLabelValue(MessageKeyConstants.JP_SLIP_ALREADY_VOIDED));
		} else if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("No")
				&& statusDesc.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC)) {
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
					.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PROCESSED));
		}

	}

	/**
	 * Method to post to prog engine after a successful print.
	 * 
	 * @param processedDTO
	 */
	/*private void postToProgressiveEngine(JackpotDTO processedDTO) {
		try {
			if(log.isInfoEnabled()) {
				log.info("Inside postToProgressiveEngine method for checking to post slip amount to progressive engine.");
			}
			if (processedDTO != null) {
				if(log.isInfoEnabled()) {
					log.info("Slot Number Printing :" + processedDTO.getAssetConfigNumber());
					log.info("Messsage Id Printing :" + processedDTO.getMessageId());
					log.info("Jackpot Id Printing :" + processedDTO.getJackpotId());
					log.info("Slip Amount printed(in cents) Printing :" + processedDTO.getJackpotNetAmount());
				}
				if (processedDTO.getAssetConfigNumber() != null
						&& !((processedDTO.getAssetConfigNumber().trim() + " ").equalsIgnoreCase(" "))
						&& processedDTO.getJackpotId() != null
						&& !((processedDTO.getJackpotId().trim() + " ").equalsIgnoreCase(" "))
						&& Integer.parseInt(processedDTO.getJackpotId().trim(), 16) >= IAppConstants.JACKPOT_ID_PROG_START_112
						&& Integer.parseInt(processedDTO.getJackpotId().trim(), 16) <= IAppConstants.JACKPOT_ID_PROG_END_159) {
					// && processedDTO.getSdsCalculatedAmount()!=0 use this to
					// filter if u want to restrict to post prog jackpots that
					// are handled/posted by PMU
					if(log.isInfoEnabled()) {
						log.info("Calling postToProgressiveEngine method for progressive engine for posting the slip print amount....");
					}
					JackpotServiceLocator.getService().postToProgressiveEngine(
							processedDTO.getAssetConfigNumber(), processedDTO.getMessageId(),
							processedDTO.getJackpotId(), processedDTO.getJackpotNetAmount(),
							(new SessionUtility().getSiteDetails().getId()), true);
					if(log.isInfoEnabled()) {
						log.info("Successfully posted the slip print amount to prog engine.");
					}
				}
			}
			if(log.isInfoEnabled()) {
				log.info("Exiting postToProgressiveEngine method for posting slip amount to progressive engine.");
			}
		} catch (JackpotEngineServiceException e) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception calling web method postToProgressiveEngine in PendingJackpotProcess", e);
			return;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN", e2);
			return;
		}
	}*/

	/**
	 * Method to send the jackpot adjustment to Marketing
	 * 
	 * @param slotNo
	 */
	private void processJackpotAdjustment(String slotNo) {
		try {

			log.info("BEFORE PROCESS JP ADJUSTMENT IS CALLED");
			long orgAmt = MainMenuController.jackpotForm.getOriginalAmount();
			long newHPJPAmt = MainMenuController.jackpotForm.getJackpotNetAmount();

			log.info("Org Amt" + orgAmt);
			log.info("Net Jp Amt: " + newHPJPAmt);
			// COMMENTED BELOW LOGIC AS THE JP AMT IS NOT BEING DIVIDED WHEN THE
			// XC10 JP AMT IS RCVD
			/*
			 * if(MainMenuController.jackpotForm.getGmuDenom()!=0){
			 * log.info("Game Denom from JP Table for Pending record: "+
			 * MainMenuController.jackpotForm.getGmuDenom()); long gameDenom =
			 * MainMenuController.jackpotForm.getGmuDenom(); orgAmt = (orgAmt *
			 * gameDenom); newHPJPAmt = (newHPJPAmt * gameDenom); }
			 * log.info("Org Amt after * with Game Denom: "+ orgAmt);
			 * log.info("New HPJP Amt after * with Game Denom: "+ newHPJPAmt);
			 */
			log.info("Associated card: " + MainMenuController.jackpotForm.getAssociatedPlayerCard());
			log.info("pl card: " + MainMenuController.jackpotForm.getPlayerCard());
			log.info("Slot No: " + slotNo);
			log.info("Trans date: " + MainMenuController.jackpotForm.getTransactionDate());
			log.info("msg id: " + MainMenuController.jackpotForm.getMessageId());
			// Get the system current time
			Timestamp createDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
			log.info("trans date: " + DateUtil.getMilliSeconds(createDate));
			int mktingAdjustResp = 0;

			mktingAdjustResp = JackpotServiceLocator.getService().processJackpotAdjustment(
					MainMenuController.jackpotForm.getSiteId(),
					MainMenuController.jackpotForm.getAssociatedPlayerCard(),
					MainMenuController.jackpotForm.getPlayerCard(), orgAmt, newHPJPAmt, slotNo,
					MainMenuController.jackpotForm.getMessageId());
			log.info("Mkting response resp: " + mktingAdjustResp);

			log.info("AFTER PROCESS JP ADJUSTMENT IS CALLED");
			/*
			 * if(mktingAdjustResp>0){ MessageDialogUtil
			 * .displayTouchScreenInfoDialog(LabelLoader
			 * .getLabelValue(MessageKeyConstants.SUCCESS_POSTING_JACKPOT_ADJUSTMENT_PLAYER_DETAILS_TO_MARKETING)); }
			 */
		} catch (JackpotEngineServiceException e) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception calling web method processJackpotAdjustment", e);
			return;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN", e2);
			return;
		}
	}

}
