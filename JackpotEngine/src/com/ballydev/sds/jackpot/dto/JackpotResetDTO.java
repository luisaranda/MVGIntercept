package com.ballydev.sds.jackpot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class JackpotResetDTO extends JackpotBaseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2267238880694975717L;

	/**
	 * jp slip sequence no 
	 */
	private long sequenceNumber;
	
	/**
	 * slot no 
	 */
	private String slotNo;
	
	/**
	 * Jackpot Original Amount
	 */
	private long jackpotAmount;
	
	/**
	 * Hex Value of the jackpot id
	 */
	private String jackpotId;
	
	/**
	 * Jackpot Transaction Date
	 */
	private Date transactionDate;
	
	/**
	 * Player Card no
	 */
	private String playerCard;
	
	/**
	 * Employee Card no
	 */
	private String employeeCardNo;

	/**
	 * @return the slotNo
	 */
	public String getSlotNo() {
		return slotNo;
	}

	/**
	 * @param slotNo the slotNo to set
	 */
	public void setSlotNo(String slotNo) {
		this.slotNo = slotNo;
	}

	/**
	 * @return the jackpotAmount
	 */
	public long getJackpotAmount() {
		return jackpotAmount;
	}

	/**
	 * @param jackpotAmount the jackpotAmount to set
	 */
	public void setJackpotAmount(long jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}

	/**
	 * @return the jackpotId
	 */
	public String getJackpotId() {
		return jackpotId;
	}

	/**
	 * @param jackpotId the jackpotId to set
	 */
	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the playerCard
	 */
	public String getPlayerCard() {
		return playerCard;
	}

	/**
	 * @param playerCard the playerCard to set
	 */
	public void setPlayerCard(String playerCard) {
		this.playerCard = playerCard;
	}
	
	
	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the employeeCardNo
	 */
	public String getEmployeeCardNo() {
		return employeeCardNo;
	}

	/**
	 * @param employeeCardNo the employeeCardNo to set
	 */
	public void setEmployeeCardNo(String employeeCardNo) {
		this.employeeCardNo = employeeCardNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Object value = "";
		StringBuilder strBuilder = new StringBuilder();
		Method[] methods = this.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().startsWith("get")){
				try {
					value = methods[i].invoke(this, (Object[])null);
					if(value != null) {
						strBuilder.append("Methods available: " + methods[i].getName() + " = " + value.toString());
					} else {
						strBuilder.append("Methods available: " + methods[i].getName() + " = " + value);
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
		return strBuilder.toString();
	}
	
}
