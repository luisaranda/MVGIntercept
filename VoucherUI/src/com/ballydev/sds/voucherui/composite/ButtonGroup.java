/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.voucherui.constants.IImageConstants;

/**
 * This class is to make the Push button behave like a radio button for a Touch Screen application.
 * @author HareshKumar
 * @version $Revision: 3$
 */
public class ButtonGroup {
	
	/**
	 * Group name instance
	 */
	private String groupName;
	
	/**
	 * Button list instance
	 */
	private List<TouchScreenRadioButton> buttonList = new ArrayList<TouchScreenRadioButton>();
	
	/**
	 * Selected button instance
	 */
	private TouchScreenRadioButton selectedButton;
	
	/**
	 * Class constructor
	 * @param groupName
	 */
	public ButtonGroup(String groupName){
		this.groupName = groupName;
	}

	/**
	 * Method to get the button list
	 * @return
	 */
	public List<TouchScreenRadioButton> getButtonList() {
		return buttonList;
	}

	/**
	 * Method to set the button list
	 * @param buttonList
	 */
	public void setButtonList(List<TouchScreenRadioButton> buttonList) {
		this.buttonList = buttonList;
	}

	/**
	 * Method to get the groupName
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Method to set the groupName
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Method to get the SelectedButton
	 * @return
	 */
	public TouchScreenRadioButton getSelectedButton() {
		return selectedButton;
	}
	
	/**
	 * Method to add a button to the button list
	 * @param button
	 */
	public void add(TouchScreenRadioButton button){
		buttonList.add(button);
	}

	/**
	 * Method to set the SelectedButton
	 * @param selectedButton
	 */
	public void setSelectedButton(TouchScreenRadioButton selectedButton) {
		this.selectedButton = selectedButton;
		selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE)));
		selectedButton.setSelected(true);
		setDeselectedImages();
	}
	
	/**
	 * Method to disable the Unselected button
	 */
	public void disableUnselected(){
		TouchScreenRadioButton button = null;
		for(int i=0; buttonList!= null && i<buttonList.size(); i++){
			button = buttonList.get(i);
			if(!button.getName().equalsIgnoreCase(selectedButton.getName())){
				button.setEnabled(false);
			}
		}
	}

	/**
	 * Method to set the group status
	 * @param status
	 */
	public void setGroupStatus(boolean status){
		TouchScreenRadioButton button = null;
		for(int i=0; buttonList!= null && i<buttonList.size(); i++){
			button = buttonList.get(i);
			button.setEnabled(status);
		}
	}
	
	/**
	 * Method to set the Deselected Images
	 */
	public void setDeselectedImages(){
		TouchScreenRadioButton button = null;
		for(int i=0; i < buttonList.size(); i++){
			button = buttonList.get(i);
			if(!button.getName().equalsIgnoreCase(selectedButton.getName())){
				button.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE)));
				button.setImage(null);
				button.setSelected(false);
			}
		}
	}
	
}
