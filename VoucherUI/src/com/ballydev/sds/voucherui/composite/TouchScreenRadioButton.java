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

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.util.StringUtil;

/**
 *  Custom radio button for touch screen
 * @author Hareshkumar
 * @version $Revision: 4$
 */
public class TouchScreenRadioButton extends SDSButton {

	private boolean isSelected;

	/**
	 * @param parent
	 * @param style
	 * @param text
	 * @param name
	 * @param formPropery
	 * @param checkValue
	 */
	public TouchScreenRadioButton(Composite parent, int style, String text,
			String name, String formProperty, boolean isSelected) {
		super(parent, style, text, name, formProperty, isSelected);
		this.isSelected = isSelected;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.control.SDSButton#refreshControl(java.lang.Object)
	 */
	@Override
	public void refreshControl(Object form) {
		if (getFormProperty() != null && !(getFormProperty().equals(""))) {
			String strMethodName = "get"
				+ (String) (StringUtil.initCap(getFormProperty()));
			try {
				Boolean checkValue = null;
				Method method = form.getClass().getMethod(strMethodName,
						(Class[]) null);
				checkValue = (Boolean) method.invoke(form, (Object[]) null);
				if (checkValue != null) {
					this.setSelected(checkValue.booleanValue());					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.control.SDSButton#refreshForm(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void refreshForm(Object form) {
		if (getFormProperty() != null && !getFormProperty().equals("")) {
			String strMethodName = "set"
				+ (String) (StringUtil.initCap(getFormProperty()));
			try {
				Object chkValue = (Object) ((Boolean) this.isSelected());				
				if (chkValue == null) {
					return;
				}
				Class[] cls;
				Object[] obj;
				cls = new Class[1];
				cls[0] = chkValue.getClass();
				obj = new Object[1];
				obj[0] = chkValue;
				Method method = form.getClass().getMethod(strMethodName, cls);
				method.invoke(form, obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
	}
}
