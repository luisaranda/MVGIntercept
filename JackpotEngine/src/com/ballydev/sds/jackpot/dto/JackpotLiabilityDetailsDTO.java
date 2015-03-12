package com.ballydev.sds.jackpot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author vsubha
 *
 */
public class JackpotLiabilityDetailsDTO extends JackpotBaseDTO {

	private static final long serialVersionUID = -6786649947997566879L;
	
	private long slipsAdded;
	
	private long slipsRedeemed;
	
	private long slipsVoided;
	
	private long slipsExpired;
	
	private long slipsAddedAmt;
	
	private long slipsReddemedAmt;
	
	private long slipsVoidedAmt;
	
	private long slipsExpiredAmt;

	/**
	 * @return the slipsAdded
	 */
	public long getSlipsAdded() {
		return slipsAdded;
	}

	/**
	 * @param slipsAdded the slipsAdded to set
	 */
	public void setSlipsAdded(long slipsAdded) {
		this.slipsAdded = slipsAdded;
	}

	/**
	 * @return the slipsRedeemed
	 */
	public long getSlipsRedeemed() {
		return slipsRedeemed;
	}

	/**
	 * @param slipsRedeemed the slipsRedeemed to set
	 */
	public void setSlipsRedeemed(long slipsRedeemed) {
		this.slipsRedeemed = slipsRedeemed;
	}

	/**
	 * @return the slipsVoided
	 */
	public long getSlipsVoided() {
		return slipsVoided;
	}

	/**
	 * @param slipsVoided the slipsVoided to set
	 */
	public void setSlipsVoided(long slipsVoided) {
		this.slipsVoided = slipsVoided;
	}

	/**
	 * @return the slipsExpired
	 */
	public long getSlipsExpired() {
		return slipsExpired;
	}

	/**
	 * @param slipsExpired the slipsExpired to set
	 */
	public void setSlipsExpired(long slipsExpired) {
		this.slipsExpired = slipsExpired;
	}

	/**
	 * @return the slipsAddedTotAmt
	 */
	public long getSlipsAddedAmt() {
		return slipsAddedAmt;
	}

	/**
	 * @param slipsAddedTotAmt the slipsAddedTotAmt to set
	 */
	public void setSlipsAddedAmt(long slipsAddedAmt) {
		this.slipsAddedAmt = slipsAddedAmt;
	}

	/**
	 * @return the slipsReddemedAmt
	 */
	public long getSlipsReddemedAmt() {
		return slipsReddemedAmt;
	}

	/**
	 * @param slipsReddemedAmt the slipsReddemedAmt to set
	 */
	public void setSlipsReddemedAmt(long slipsReddemedAmt) {
		this.slipsReddemedAmt = slipsReddemedAmt;
	}

	/**
	 * @return the slipsVoidedAmt
	 */
	public long getSlipsVoidedAmt() {
		return slipsVoidedAmt;
	}

	/**
	 * @param slipsVoidedAmt the slipsVoidedAmt to set
	 */
	public void setSlipsVoidedAmt(long slipsVoidedAmt) {
		this.slipsVoidedAmt = slipsVoidedAmt;
	}

	/**
	 * @return the slipsExpiredAmt
	 */
	public long getSlipsExpiredAmt() {
		return slipsExpiredAmt;
	}

	/**
	 * @param slipsExpiredAmt the slipsExpiredAmt to set
	 */
	public void setSlipsExpiredAmt(long slipsExpiredAmt) {
		this.slipsExpiredAmt = slipsExpiredAmt;
	}
	
	public String toString() {
		Object value = "";
		StringBuilder toStrBuilder = new StringBuilder();
		toStrBuilder.append("JackpotLiabilityDetailsDTO============>");
		Method[] methods = this.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")) {
				try {
					value = methods[i].invoke(this, (Object[]) null);
					if (value != null) {
						toStrBuilder.append("Methods available: " + methods[i].getName() + " = "
								+ value.toString());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return super.toString();
	}
}
