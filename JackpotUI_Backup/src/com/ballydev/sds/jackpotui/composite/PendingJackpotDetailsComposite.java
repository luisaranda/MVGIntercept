/*****************************************************************************
 * $Id: PendingJackpotDetailsComposite.java,v 1.20, 2011-02-14 15:00:51Z, Subha Viswanathan$
 * $Date: 2/14/2011 9:00:51 AM$
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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.composite.TouchScreenMiddleHeaderComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;


/**
 * Class contains the user entered data for winning combination, coins
 * played , payline,window no, player name and player card for a Manual Jackpot
 * 
 * @author dambereen
 * @version $Revision: 21$
 */
public class PendingJackpotDetailsComposite extends TouchScreenBaseComposite {
	
	/**
	 * WinningCombination Label
	 */
	private CbctlLabel lblWinningCombination = null;

	/**
	 * WinningCombination Label
	 */
	private CbctlMandatoryLabel lblManWinningCombination = null;
	
	/**
	 * WinningCombination Text
	 */
	private SDSTSText txtWinningCombination = null;

	/**
	 * Payline Label
	 */
	private CbctlLabel lblPayline = null;
	
	/**
	 * Payline Label
	 */
	private CbctlMandatoryLabel lblManPayline = null;

	/**
	 * Payline text
	 */
	private SDSTSText txtPayline = null;

	/**
	 * CoinsPlayed label
	 */
	private CbctlLabel lblCoinsPlayed = null;
	
	/**
	 * CoinsPlayed label
	 */
	private CbctlMandatoryLabel lblManCoinsPlayed = null;

	/**
	 * CoinsPlayed Button
	 */
	private SDSTSText txtCoinsPlayed = null;

	/**
	 * WindowNo Label
	 */
	private CbctlLabel lblWindowNo = null;
	
	/**
	 * WindowNo Label
	 */
	private CbctlMandatoryLabel lblManWindowNo = null;

	/**
	 * WindowNo Button
	 */
	private SDSTSText txtWindowNo = null;
	
	/**
	 * SlipStaticInfoComposite instance
	 */
	private SlipStaticInfoComposite slipStaticInfoComposite = null;  //  @jve:decl-index=0:visual-constraint="126,593"

	/**
	 * String array having the site configuration values for the parameters
	 * given below ALLOW_WINNING_COMB_ON_JP_SLIPS ALLOW_PAYLINE_ON_JP_SLIPS
	 * ALLOW_COINS_PLAYED_ON_JP_SLIPS ALLOW_WINDOW_NO_ON_JP_SLIPS
	 */
	private String[] siteConfigParamFlag;

	/**
	 * Player card label instance
	 */
	private CbctlLabel lblPlayerCard = null;

	/**
	 * Player card label instance
	 */
	private CbctlMandatoryLabel lblManPlayerCard = null;
	
	/**
	 * Player card text instance
	 */
	private SDSTSText txtPlayerCard = null;

	/**
	 * Player name label instance
	 */
	private CbctlLabel lblPlayerName = null;
	
	/**
	 * Player name label instance
	 */
	private CbctlMandatoryLabel lblManPlayerName = null;
	
	/**
	 * Player card text instance
	 */
	private SDSTSText txtPlayerName = null;
	
	private Composite winningGroupComposite = null;

	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;
	

	/**
	 * Class constructor 
	 * @param parent
	 * @param style
	 * @param siteParam
	 */
	public PendingJackpotDetailsComposite(Composite parent, int style,
			String[] siteParam) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));
		this.siteConfigParamFlag = siteParam;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		initialize("P,N,C");
		layout();
	}

	public void drawMiddleComposite() {
		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_ENTER_DETAILS));
		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_DETAILS));
		
		createWinningCombinationComposite(getMiddleComposite());

		slipStaticInfoComposite = new SlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);
		
		
		
	}

	/**
	 * This method initializes WinningCombinationComposite mainGroup
	 * 
	 */
	private void createWinningCombinationComposite(Composite middleComposite) {
		
		
		GridData gdWinComp = new GridData();
		gdWinComp.grabExcessVerticalSpace = false;
		gdWinComp.verticalAlignment = GridData.FILL;
		gdWinComp.horizontalSpan = 2;
						
		gdWinComp.widthHint = middleBodyWidth;
		gdWinComp.heightHint = middleBodyHeight;
		gdWinComp.horizontalAlignment = GridData.BEGINNING;
		
		GridLayout glWinComp = new GridLayout();
		glWinComp.numColumns = 2;
		glWinComp.verticalSpacing = 10;
		glWinComp.marginWidth = 5;
		glWinComp.marginHeight = 5;
		glWinComp.horizontalSpacing = 5;
		
		winningGroupComposite = new Composite(middleComposite, SWT.NONE);
		winningGroupComposite.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		winningGroupComposite.setLayoutData(gdWinComp);
		winningGroupComposite.setLayout(glWinComp);
		
		
		
		if (siteConfigParamFlag[0].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINNING_COMB_MANDATORY).equalsIgnoreCase("yes")) {
			
			lblManWinningCombination = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.WINNING_COMBINATION));
			lblManWinningCombination.setLayoutData(getMiddleComposite().getGDForExtraLargeBodyLabel());

			txtWinningCombination = new SDSTSText(
					winningGroupComposite,
					SWT.BORDER,
					LabelLoader
							.getLabelValue(LabelKeyConstants.WINNING_COMBINATION),
					FormConstants.FORM_WINNING_COMB);
			txtWinningCombination.setTextLimit(IFieldTextLimits.WIN_COMB_TEXT_LIMIT);
			txtWinningCombination.setLayoutData(getMiddleComposite().getGDForBodyText());			
			
		}else if (siteConfigParamFlag[0].equalsIgnoreCase("yes")) {
			lblWinningCombination = new CbctlLabel(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.WINNING_COMBINATION));
			lblWinningCombination.setLayoutData(getMiddleComposite().getGDForExtraLargeBodyLabel());

			txtWinningCombination = new SDSTSText(
					winningGroupComposite,
					SWT.BORDER,
					LabelLoader
							.getLabelValue(LabelKeyConstants.WINNING_COMBINATION),
					FormConstants.FORM_WINNING_COMB);
			txtWinningCombination.setTextLimit(IFieldTextLimits.WIN_COMB_TEXT_LIMIT);
			txtWinningCombination.setLayoutData(getMiddleComposite().getGDForBodyText());

		}
		
		
		if (siteConfigParamFlag[1].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PAYLINE_MANDATORY).equalsIgnoreCase("yes")) {
			lblManPayline = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.PAY_LINE));
			lblManPayline.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtPayline = new SDSTSText(winningGroupComposite, SWT.BORDER, LabelLoader
					.getLabelValue(LabelKeyConstants.PAY_LINE), FormConstants.FORM_PAYLINE);
			txtPayline.setTextLimit(IFieldTextLimits.PAYLINE_TEXT_LIMIT);
			txtPayline.setLayoutData(getMiddleComposite().getGDForBodyText());

		}else if (siteConfigParamFlag[1].equalsIgnoreCase("yes")) {
			lblPayline = new CbctlLabel(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.PAY_LINE));
			lblPayline.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtPayline = new SDSTSText(winningGroupComposite, SWT.BORDER, LabelLoader
					.getLabelValue(LabelKeyConstants.PAY_LINE), FormConstants.FORM_PAYLINE);
			txtPayline.setTextLimit(IFieldTextLimits.PAYLINE_TEXT_LIMIT);
			txtPayline.setLayoutData(getMiddleComposite().getGDForBodyText());

		}
		
		if (siteConfigParamFlag[2].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_COINS_PLAYED_MANDATORY).equalsIgnoreCase("yes")) {
			lblManCoinsPlayed = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.COINS_PLAYED));
			lblManCoinsPlayed.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtCoinsPlayed = new SDSTSText(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.COINS_PLAYED),
					FormConstants.FORM_COINS_PLAYED);
			txtCoinsPlayed.setTextLimit(IFieldTextLimits.COINS_PLAYED_TEXT_LIMIT);
			txtCoinsPlayed.setLayoutData(getMiddleComposite().getGDForBodyText());

		}else if (siteConfigParamFlag[2].equalsIgnoreCase("yes")) {
			lblCoinsPlayed = new CbctlLabel(winningGroupComposite, SWT.NONE,LabelLoader
					.getLabelValue(LabelKeyConstants.COINS_PLAYED));
			lblCoinsPlayed.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtCoinsPlayed = new SDSTSText(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.COINS_PLAYED),
					FormConstants.FORM_COINS_PLAYED);
			txtCoinsPlayed.setTextLimit(IFieldTextLimits.COINS_PLAYED_TEXT_LIMIT);
			txtCoinsPlayed.setLayoutData(getMiddleComposite().getGDForBodyText());

		}
		
		if (siteConfigParamFlag[3].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINDOW_NO_MANDATORY).equalsIgnoreCase("yes")) {
			lblManWindowNo = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.WINDOW_NUMBER));
			lblManWindowNo.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtWindowNo = new SDSTSText(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.WINDOW_NUMBER), FormConstants.FORM_WINDOW_NO);
			txtWindowNo.setTextLimit(IFieldTextLimits.WINDOW_TEXT_LIMIT);
			txtWindowNo.setLayoutData(getMiddleComposite().getGDForBodyText());			
		}
		else if (siteConfigParamFlag[3].equalsIgnoreCase("yes")) {
			lblWindowNo = new CbctlLabel(winningGroupComposite, SWT.NONE,LabelLoader
					.getLabelValue(LabelKeyConstants.WINDOW_NUMBER));
			lblWindowNo.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());

			txtWindowNo = new SDSTSText(winningGroupComposite, SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.WINDOW_NUMBER), FormConstants.FORM_WINDOW_NO);
			txtWindowNo.setTextLimit(IFieldTextLimits.WINDOW_TEXT_LIMIT);
			txtWindowNo.setLayoutData(getMiddleComposite().getGDForBodyText());			
		}
		
		if (siteConfigParamFlag[4].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.IS_PLAYER_CARD_MANDATORY).equalsIgnoreCase("yes")) {
			lblManPlayerCard = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_CARD));
			lblManPlayerCard.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());
			txtPlayerCard = new SDSTSText(winningGroupComposite, SWT.NONE, "", FormConstants.FORM_PLAYER_CARD);
			txtPlayerCard.setTextLimit(IFieldTextLimits.PLAYER_CARD_LIMIT);
			txtPlayerCard.setLayoutData(getMiddleComposite().getGDForBodyText());
		} else if (siteConfigParamFlag[4].equalsIgnoreCase("yes")) {
			lblPlayerCard = new CbctlLabel(winningGroupComposite, SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_CARD));
			lblPlayerCard.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());
			txtPlayerCard = new SDSTSText(winningGroupComposite, SWT.NONE, "", FormConstants.FORM_PLAYER_CARD);
			txtPlayerCard.setTextLimit(IFieldTextLimits.PLAYER_CARD_LIMIT);
			txtPlayerCard.setLayoutData(getMiddleComposite().getGDForBodyText());
		}
		
		if (siteConfigParamFlag[5].equalsIgnoreCase("yes")
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.IS_PLAYER_NAME_MANDATORY).equalsIgnoreCase("yes")) {
			lblManPlayerName = new CbctlMandatoryLabel(winningGroupComposite, SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_NAME));
			lblManPlayerName.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());
			txtPlayerName = new SDSTSText(winningGroupComposite, SWT.NONE, "", FormConstants.FORM_PLAYER_NAME);
			txtPlayerName.setLayoutData(getMiddleComposite().getGDForBodyText());
			txtPlayerName.setTextLimit(IFieldTextLimits.PLAYER_NAME_LIMIT);
		} else if (siteConfigParamFlag[5].equalsIgnoreCase("yes")) {
			lblPlayerName = new CbctlLabel(winningGroupComposite, SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_NAME));
			lblPlayerName.setLayoutData(getMiddleComposite().getGDExtraForBodyLabel());
			txtPlayerName = new SDSTSText(winningGroupComposite, SWT.NONE, "", FormConstants.FORM_PLAYER_NAME);
			txtPlayerName.setLayoutData(getMiddleComposite().getGDForBodyText());
			txtPlayerName.setTextLimit(IFieldTextLimits.PLAYER_NAME_LIMIT);
		}
	}	
	/**
	 * @return the lblCoinsPlayed
	 */
	public CbctlLabel getLblCoinsPlayed() {
		return lblCoinsPlayed;
	}

	/**
	 * @param lblCoinsPlayed the lblCoinsPlayed to set
	 */
	public void setLblCoinsPlayed(CbctlLabel lblCoinsPlayed) {
		this.lblCoinsPlayed = lblCoinsPlayed;
	}

	/**
	 * @return the lblPayline
	 */
	public CbctlLabel getLblPayline() {
		return lblPayline;
	}

	/**
	 * @param lblPayline the lblPayline to set
	 */
	public void setLblPayline(CbctlLabel lblPayline) {
		this.lblPayline = lblPayline;
	}

	/**
	 * @return the lblPlayerCard
	 */
	public CbctlLabel getLblPlayerCard() {
		return lblPlayerCard;
	}

	/**
	 * @param lblPlayerCard the lblPlayerCard to set
	 */
	public void setLblPlayerCard(CbctlLabel lblPlayerCard) {
		this.lblPlayerCard = lblPlayerCard;
	}

	/**
	 * @return the lblPlayerName
	 */
	public CbctlLabel getLblPlayerName() {
		return lblPlayerName;
	}

	/**
	 * @param lblPlayerName the lblPlayerName to set
	 */
	public void setLblPlayerName(CbctlLabel lblPlayerName) {
		this.lblPlayerName = lblPlayerName;
	}

	/**
	 * @return the lblWindowNo
	 */
	public CbctlLabel getLblWindowNo() {
		return lblWindowNo;
	}

	/**
	 * @param lblWindowNo the lblWindowNo to set
	 */
	public void setLblWindowNo(CbctlLabel lblWindowNo) {
		this.lblWindowNo = lblWindowNo;
	}

	/**
	 * @return the lblWinningCombination
	 */
	public CbctlLabel getLblWinningCombination() {
		return lblWinningCombination;
	}

	/**
	 * @param lblWinningCombination the lblWinningCombination to set
	 */
	public void setLblWinningCombination(CbctlLabel lblWinningCombination) {
		this.lblWinningCombination = lblWinningCombination;
	}

	/**
	 * @return the siteConfigParamFlag
	 */
	public String[] getSiteConfigParamFlag() {
		return siteConfigParamFlag;
	}

	/**
	 * @param siteConfigParamFlag the siteConfigParamFlag to set
	 */
	public void setSiteConfigParamFlag(String[] siteConfigParamFlag) {
		this.siteConfigParamFlag = siteConfigParamFlag;
	}

	

	/**
	 * @return the txtCoinsPlayed
	 */
	public SDSTSText getTxtCoinsPlayed() {
		return txtCoinsPlayed;
	}

	/**
	 * @param txtCoinsPlayed the txtCoinsPlayed to set
	 */
	public void setTxtCoinsPlayed(SDSTSText txtCoinsPlayed) {
		this.txtCoinsPlayed = txtCoinsPlayed;
	}

	/**
	 * @return the txtPayline
	 */
	public SDSTSText getTxtPayline() {
		return txtPayline;
	}

	/**
	 * @param txtPayline the txtPayline to set
	 */
	public void setTxtPayline(SDSTSText txtPayline) {
		this.txtPayline = txtPayline;
	}

	/**
	 * @return the txtPlayerCard
	 */
	public SDSTSText getTxtPlayerCard() {
		return txtPlayerCard;
	}

	/**
	 * @param txtPlayerCard the txtPlayerCard to set
	 */
	public void setTxtPlayerCard(SDSTSText txtPlayerCard) {
		this.txtPlayerCard = txtPlayerCard;
	}

	/**
	 * @return the txtPlayerName
	 */
	public SDSTSText getTxtPlayerName() {
		return txtPlayerName;
	}

	/**
	 * @param txtPlayerName the txtPlayerName to set
	 */
	public void setTxtPlayerName(SDSTSText txtPlayerName) {
		this.txtPlayerName = txtPlayerName;
	}

	/**
	 * @return the txtWindowNo
	 */
	public SDSTSText getTxtWindowNo() {
		return txtWindowNo;
	}

	/**
	 * @param txtWindowNo the txtWindowNo to set
	 */
	public void setTxtWindowNo(SDSTSText txtWindowNo) {
		this.txtWindowNo = txtWindowNo;
	}

	/**
	 * @return the txtWinningCombination
	 */
	public SDSTSText getTxtWinningCombination() {
		return txtWinningCombination;
	}

	/**
	 * @param txtWinningCombination the txtWinningCombination to set
	 */
	public void setTxtWinningCombination(SDSTSText txtWinningCombination) {
		this.txtWinningCombination = txtWinningCombination;
	}

	/**
	 * @return the slipStaticInfoComposite
	 */
	public SlipStaticInfoComposite getSlipStaticInfoComposite() {
		return slipStaticInfoComposite;
	}

	/**
	 * @param slipStaticInfoComposite the slipStaticInfoComposite to set
	 */
	public void setSlipStaticInfoComposite(
			SlipStaticInfoComposite slipStaticInfoComposite) {
		this.slipStaticInfoComposite = slipStaticInfoComposite;
	}

	public Composite getWinningGroupComposite() {
		return winningGroupComposite;
	}

	public void setWinningGroupComposite(Composite winningGroupComposite) {
		this.winningGroupComposite = winningGroupComposite;
	}



}  //  @jve:decl-index=0:visual-constraint="4,-7"
