/*****************************************************************************
 * $Id: ReprintJackpotProcess.java,v 1.32, 2011-02-04 13:46:35Z, Anbuselvi, Balasubramanian$
 * $Date: 2/4/2011 7:46:35 AM$
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
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.controller.ReprintController;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.print.PrintWithGraphics;
import com.ballydev.sds.jackpotui.print.SlipImage;
import com.ballydev.sds.jackpotui.print.SlipPrinting;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.barcodelib.barcode.Barcode;

/**
 * Class to update the database and populate the printDTO to print
 * the slip once the  jackpot reprint process is done.
 * 
 * @author dambereen
 * @version $Revision: 33$
 */
public class ReprintJackpotProcess {
		
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);
		
	/**
	 * Method to update the details to the DB for a jackpot reprint
	 */
	public void reprintJackpot() {
		
		JackpotDTO jackpotDTO = new JackpotDTO();
		jackpotDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
		jackpotDTO.setSequenceNumber(MainMenuController.jackpotForm.getSequenceNumber());
		jackpotDTO.setSiteId(MainMenuController.jackpotForm.getSiteId());
		String printerUsed = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY);
		if (printerUsed != null)
			jackpotDTO.setPrinterUsed(printerUsed);
		JackpotDTO postReprintDTO = null;
		try {
			if(log.isInfoEnabled()) {
				log.info("Calling the postJackpotReprint web method");
				log.info("*******************************************");
				log.info("JackpotDTO values: "+jackpotDTO.toString());						
				log.info("*******************************************");
			}
			postReprintDTO = JackpotServiceLocator.getService().postJackpotReprint(jackpotDTO);
					
			//FOR PEERMONT REQ IF AUTO VOID REPRINT IS YES, THEN THE postJackpotReprint RETURNS
			//THE NEW SLIP DETAILS AND SO NEED TO RESET THE ReprintJackpotSlipDTO FORM
			//IF PARAM IS NO, THEN NO NEED TO RESET THE ReprintJackpotSlipDTO FORM AS IT IS ALREADY SET IN THE REPRINT CONTROLLER
			if(MainMenuController.jackpotSiteConfigParams
					.get(ISiteConfigConstants.ALLOW_AUTO_VOID_ON_REPRINTED_JP).equalsIgnoreCase("Yes")) {
				ReprintController.getForm().setReprintJackpotSlipDTO(postReprintDTO);
			}			
			if(log.isInfoEnabled()) {
				log.info("Values returned after calling the postJackpotReprint web method");
				log.info("*******************************************");
				log.info("Jackpot DTO values returned: " + postReprintDTO.toString());					
				log.info("*******************************************");
			}
		} catch (JackpotEngineServiceException e1) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e1, e1.getMessage());
			log.error("Exception while checking the postJackpotReprint web method", e1);
			return;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN", e2);
			return;
		}				
		
		if (postReprintDTO.isPostedSuccessfully()) {
			jackpotDTO = ReprintController.getForm().getReprintJackpotSlipDTO();
			jackpotDTO.setSequenceNumber(postReprintDTO.getSequenceNumber());
			jackpotDTO.setAccountNumber(postReprintDTO.getAccountNumber());
			jackpotDTO.setCashlessAccountType(postReprintDTO.getCashlessAccountType());
			MainMenuController.jackpotForm.setSequenceNumber(postReprintDTO.getSequenceNumber());
			PrintDTO printDTO = new PrintDTO();
			//printDTO = setPrintDTOValues(jackpotDTO) ;
			//jackpotDTO.setPrintEmployeeLogin(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
			jackpotDTO.setPrintEmployeeLogin(jackpotDTO.getPrintEmployeeLogin());
			
			String slipPrintType = null;
			if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("No")
					|| SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(
							IAppConstants.EPSON_PRINTER_SUBSTRING)) {
				slipPrintType = IAppConstants.JACKPOT_PRINT_SLIP_TYPE;
				printDTO = SlipUtil.setPrintDTOValues(jackpotDTO);
			} else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP).equalsIgnoreCase("Yes")) {
				slipPrintType = IAppConstants.CHECK_PRINT_SLIP_TYPE;
				printDTO = SlipUtil.setCheckDTOValues(jackpotDTO);
			}
			
			SlipImage slipImage = new SlipImage();
			String slipTextImage = null;
			try {				
				if (SDSPreferenceStore.getStringStoreValue(
						IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).toUpperCase().contains(
								IAppConstants.EPSON_PRINTER_SUBSTRING)) {
					if(log.isInfoEnabled()) {
						log.info("Epson printer was selected, calling buildSlipImage method");					
					}
					slipTextImage = slipImage.buildSlipImage(printDTO, IAppConstants.JACKPOT_PRINT_SLIP_TYPE,
							SlipUtil.jackpotSlipMap, jackpotDTO.getHpjpAmount(), jackpotDTO.getJackpotNetAmount());
					SlipPrinting slipPrinting = new SlipPrinting();
					slipPrinting.printSlip(slipTextImage);
				} else if (SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).contains(
						IAppConstants.LASER_JET_PRINTER_SUBSTRING)) {
					if(log.isInfoEnabled()) {
						log.info("Laser Jet printer was selected, calling getLaserPrinterImage method");
					}
					if(slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
						slipTextImage = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType, SlipUtil.checkSlipMap,
								jackpotDTO.getHpjpAmount(), jackpotDTO.getJackpotNetAmount(), MainMenuController.jackpotForm.getExpiryDate());
					} else if(slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) {
						slipTextImage = SlipImage.getLaserPrinterImage(printDTO, slipPrintType, SlipUtil.jackpotSlipMap, 
								jackpotDTO.getHpjpAmount(), jackpotDTO.getJackpotNetAmount());
					}
					PrintWithGraphics printWithGraphics = new PrintWithGraphics();
					Image barcodeImg = null;
					if (MainMenuController.jackpotForm.isPrintBarcode()) {
						Barcode barcode = JackpotServiceLocator.getService().createBarcode(jackpotDTO.getSiteId(), jackpotDTO
								.getJackpotNetAmount(), MainMenuController.jackpotForm.getSequenceNumber(),
								postReprintDTO.getBarEncodeFormat());
						String initialBarFile = postReprintDTO.getBarEncodeFormat() + ".jpg";
						barcode.createBarcodeImage(initialBarFile);

						File barcodeFile = new File(initialBarFile);
						barcodeImg = ImageIO.read(barcodeFile); 
					}
					printWithGraphics.printTextImage(slipTextImage, barcodeImg, slipPrintType);
				} else {
					if (log.isInfoEnabled()) {
						log.info("Default was selected, calling getLaserPrinterImage method");
					}
					if(slipPrintType.equalsIgnoreCase(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
						slipTextImage = SlipImage.getCheckLaserPrinterImage(printDTO, slipPrintType, SlipUtil.checkSlipMap,
								jackpotDTO.getHpjpAmount(), jackpotDTO.getJackpotNetAmount(), MainMenuController.jackpotForm.getExpiryDate());
					} else if(slipPrintType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)) { 
						slipTextImage = SlipImage.getLaserPrinterImage(printDTO, slipPrintType, SlipUtil.jackpotSlipMap, 
								jackpotDTO.getHpjpAmount(), jackpotDTO.getJackpotNetAmount());
					}
					PrintWithGraphics printWithGraphics = new PrintWithGraphics();
					Image barcodeImg = null;
					if(MainMenuController.jackpotForm.isPrintBarcode()){
						Barcode barcode = JackpotServiceLocator.getService().createBarcode(jackpotDTO.getSiteId(), jackpotDTO.getJackpotNetAmount(),
								MainMenuController.jackpotForm.getSequenceNumber(), postReprintDTO.getBarEncodeFormat());
						String initialBarFile = postReprintDTO.getBarEncodeFormat() + ".jpg";
						barcode.createBarcodeImage(initialBarFile);
	
						File barcodeFile = new File(initialBarFile);
						barcodeImg = ImageIO.read(barcodeFile); 
					}
					printWithGraphics.printTextImage(slipTextImage, barcodeImg, slipPrintType);
				}
			} catch (Exception e) {
				log.error(e);
			}
						
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPRINT_SUCCESS)
							+ " "
							+ LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SEQUENCE)
							+ " " + jackpotDTO.getSequenceNumber() + ".");
			
			if(log.isInfoEnabled()) {
				log.info("BEFORE SENDING AUDIT TRAIL INFO FOR REPRINT");
			}
			try { 
				Util.sendDataToAuditTrail(IAppConstants.MODULE_NAME,
						LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_JACKPOT_SCREEN),
						null,
						null,
						null,
						LabelLoader.getLabelValue(LabelKeyConstants.PRINT_PROCESS_EMP_ID)
							+ ": "
							+ MainMenuController.jackpotForm.getEmployeeIdPrintedSlip()
							+ " ,"
							+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SLOT_NO)
							+ ": "
							+ jackpotDTO.getAssetConfigNumber()
							+ " ,"
							+ LabelLoader.getLabelValue(LabelKeyConstants.PRINT_SEQ_NO)
							+ ": " + jackpotDTO.getSequenceNumber(),
					EnumOperation.REPRINT_JACKPOT, jackpotDTO.getAssetConfigNumber());
			} catch (Exception e) {
				log.error("Exception when sending audit trail info", e);
			}
			if(log.isInfoEnabled()) {
				log.info("AFTER SENDING AUDIT TRAIL INFO");
			}
		}
	}	
}
