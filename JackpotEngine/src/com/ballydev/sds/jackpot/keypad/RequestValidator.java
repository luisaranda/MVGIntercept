package com.ballydev.sds.jackpot.keypad;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_EMPLOYEE_BO;

import org.apache.log4j.Logger;

import com.ballydev.sds.ana.bo.IAnABO;
import com.ballydev.sds.employee.web.form.AnAUserForm;
import com.ballydev.sds.employee.web.form.FunctionForm;
import com.ballydev.sds.employee.web.form.ParameterForm;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.lookup.ServiceLocator;

/**
 * 
 * This Class is used as an generic validation utility for all request objects.
 * 
 * @author ranjithkumarm
 *
 */
public class RequestValidator implements IKeypadProcessConstants, IAppConstants{
	
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	public ValidationResponseDTO validateRequest(ICustomRequest customRequest, Integer siteNumber,Long jpOriginalAmount){
		log.info("***Inside validateRequest method***");
		ValidationResponseDTO validationResponseDTO=new ValidationResponseDTO();
		try{
			if(customRequest!=null && siteNumber!=null && siteNumber.intValue()!=0){
				if(customRequest instanceof InitiationRequestObject){
					log.info("***Inside validateRequest method - InitiationRequestObject with siteNumber "+siteNumber+" ***");

					InitiationRequestObject initiationRequestObject=(InitiationRequestObject)customRequest;
					String employeeCardNumber=initiationRequestObject.getEmployeeIdCardInserted();
					if(employeeCardNumber!=null && !((employeeCardNumber.trim()+" ").equalsIgnoreCase(" ")) ){
						IAnABO AnABO=(IAnABO)ServiceLocator.fetchService(LOOKUP_SDS_EMPLOYEE_BO, IS_LOCAL_LOOKUP);
						if(AnABO!=null){
							AnAUserForm anAUserFormGot=AnABO.getEmployeeNameAndFunctionsAdvanced(employeeCardNumber.trim(), siteNumber);
							if(anAUserFormGot!=null && !(anAUserFormGot.isErrorPresent())){								
								FunctionForm[] functionFormsGot=anAUserFormGot.getFunctionForms();
								if(functionFormsGot!=null && functionFormsGot.length!=0){
									boolean processFunctionPresent=false;
									for(int i=0;i<functionFormsGot.length;i++){
										FunctionForm functionFormGot=functionFormsGot[i];
										if(functionFormGot.getFunctionName().equalsIgnoreCase(EMP_FUNCTION_PROCESS_PENDING_JP)){
											processFunctionPresent=true;
											break;
										}										
									}									
									if(processFunctionPresent){
										String empFirstName=anAUserFormGot.getFirstName();
										String empLastName=anAUserFormGot.getLastName();
										String empLogin=anAUserFormGot.getUserName();
										validationResponseDTO.setValidationSuccess(true);
										validationResponseDTO.setEmployeeCardNo(employeeCardNumber.trim());
										try{
											validationResponseDTO.setEmployeeId(Integer.parseInt(empLogin.trim()));
										}catch (Exception e) {

										}
										validationResponseDTO.setEmployeeFirstName(empFirstName);
										validationResponseDTO.setEmployeeLastName(empLastName);	
										//getting the auth level for the employee.
										AnAUserForm anAUserFormForParams=AnABO.getAnARoleDetailsAdvancedWithAllParams(employeeCardNumber.trim(), siteNumber);
										if(anAUserFormForParams!=null && anAUserFormForParams.getParameterForms()!=null &&
												anAUserFormForParams.getParameterForms().length!=0){
											ParameterForm[] parameterForms=anAUserFormForParams.getParameterForms();
											for(int l=0;l<parameterForms.length;l++){
												ParameterForm parameterFormGotHere=parameterForms[l];
												if(parameterFormGotHere!=null && 
														parameterFormGotHere.getParameterName()!=null && parameterFormGotHere.getParameterName().equalsIgnoreCase(IKeypadProcessConstants.ROLE_PARAM_AUTH_LVL_KEY)){
													validationResponseDTO.setEmployeeAuthorizationLevel(parameterFormGotHere.getParameterValue());
												}
												
												if(parameterFormGotHere!=null && 
														parameterFormGotHere.getParameterName()!=null && parameterFormGotHere.getParameterName().equalsIgnoreCase(IKeypadProcessConstants.ROLE_PARAM_AUTH_AMOUNT)){
													validationResponseDTO.setEmployeeAuthAmount(parameterFormGotHere.getParameterValue());
												}
											}
										}
										
										
									}else{
										//log error as emp doesnot have the function to authorize pending jackpot.
										validationResponseDTO.setValidationSuccess(false);
										validationResponseDTO.setErrorMessage("Employee does not have function to auth.");
										log.info("Employee does not have function to auth.");
										return validationResponseDTO;
									}

								}else{
									//log error as emp doesnot have the function to authorize pending jackpot.
									validationResponseDTO.setValidationSuccess(false);
									validationResponseDTO.setErrorMessage("Employee does not have function.");
									log.info("Employee does not have function.");
									return validationResponseDTO;
								}

							}else{
								//log error as emp card invalid.
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Employee Card invalid.");
								log.info("Employee Card invalid.");
								return validationResponseDTO;
							}
						}
					}else{
						//log error as emp card number null.
						validationResponseDTO.setValidationSuccess(false);
						validationResponseDTO.setErrorMessage("Employee card not present.");
						log.info("Employee card not present.");
						return validationResponseDTO;
					}


				}else if(customRequest instanceof PromptDetailsRequestObject){
					log.info("***Inside validateRequest method - PromptDetailsRequestObject***");
					
					validationResponseDTO.setValidationSuccess(true);//default made true;

					PromptDetailsRequestObject promptDetailsRequestObject=(PromptDetailsRequestObject)customRequest;
					
					String requestedPromptFlag=promptDetailsRequestObject.getRequestFlagFromSession();
					InitiationResponseObject flagsRequested=KeypadUtil.getRequestedFlagStatus(requestedPromptFlag);
					
					if(flagsRequested!=null){
						
						if(flagsRequested.isPromptJackpotAmount()){
							
							double siteJPDollarVarValue = new Double(JackpotUtil.getSiteParamValue(JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS,siteNumber));
							long siteJPPercentVarValue = new Long(JackpotUtil.getSiteParamValue(JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS,siteNumber));
							int numOfAuthSignatures = new Integer(JackpotUtil.getSiteParamValue(NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES,siteNumber));
							
							long jackpotAmount=promptDetailsRequestObject.getJackpotAmountOccurred();
							long dolVarCal = jackpotAmount - jpOriginalAmount;
							double difference = dolVarCal;
							double orgAmount = jpOriginalAmount;
							double orgPerVarCal = ((Math.abs(difference))*100)/orgAmount;
							double perVarCal = Math.ceil(((Math.abs(difference))*100)/orgAmount);
							
							if(log.isDebugEnabled())
							{
								log.debug("Original JP Amount(cents) "+jpOriginalAmount+" JP Amount entered(cents) "+jackpotAmount);
								log.debug("Employee Authorized amount(cents) "+flagsRequested.getAuthAmount());
								log.debug("siteJPPercentageVariance "+siteJPPercentVarValue+" orgPerVarCal "+orgPerVarCal+" Ceiled Percentage value "+perVarCal);								
								log.debug("siteJPDollarVarValue "+siteJPDollarVarValue+" Dollar Variance Calculated(cents) "+dolVarCal);
								log.debug("numOfAuthSignatures "+numOfAuthSignatures );
							}
							
							if(jackpotAmount<=0){
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Jackpot Amount not valid");
								if(log.isInfoEnabled())
									log.info("Jackpot Amount not valid");
								return validationResponseDTO;
							}
							/**
							 * If the employee entered amount exceeds the authorized amount, we send an error message to the GMU,
							 * at that point transaction is canceled.
							 */
							else if (jackpotAmount > Long.parseLong(flagsRequested.getAuthAmount()) && !flagsRequested.isPromptEmployeeAuth()){
								
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Amount Exceeds Authorization Limit");
								if(log.isInfoEnabled())
									log.info("JP Amount Entered "+jackpotAmount+" Authorized Amount "+flagsRequested.getAuthAmount());
								return validationResponseDTO;
							}
							/**
							 * If the dollar variance amount exceeds the value specfied by the siteconfig,send an error message to the GMU.
							 * multiplying siteJPDollarVarValue with 100 as it is in dollar format,converting it to cents.
							 */
							else if(numOfAuthSignatures > 0 && siteJPDollarVarValue > 0 &&
									Math.abs((dolVarCal)) > (siteJPDollarVarValue*100)){
								
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Exceeds Dollar Variance Limit");
								if(log.isInfoEnabled())
									log.info("siteJPDollarVarValue "+siteJPDollarVarValue+" variance amount cal "+Math.abs(dolVarCal));
								return validationResponseDTO;
								
							}
							/**
							 * If the percentage variance amount exceeds the value specified by the siteconfig,send an error message to the GMU.
							 */
							else if(numOfAuthSignatures > 0 && siteJPPercentVarValue > 0 && (Math.abs(perVarCal) > siteJPPercentVarValue)){
								
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Exceeds Percentage Variance Limit");
								if(log.isInfoEnabled())
									log.info("SiteJPPercentageValue "+(siteJPPercentVarValue)+" variancePerCal  "+Math.abs(perVarCal));
								return validationResponseDTO;
							}
								
							else{
								validationResponseDTO.setValidationSuccess(true);
							}
						}
						
						
						if(flagsRequested.isPromptPayLine()){
							int payLineEntered=promptDetailsRequestObject.getPayLine();
							if(payLineEntered<=0){
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("PayLine not valid");
								log.info("PayLine not valid");
								return validationResponseDTO;
							}else{
								validationResponseDTO.setValidationSuccess(true);
							}
						}
						
						if(flagsRequested.isPromptWinningCombination()){
							int winningCombi=promptDetailsRequestObject.getWinningCombination();
							if(winningCombi<=0){
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Winning Combination not valid");
								log.info("Winning Combination not valid");
								return validationResponseDTO;
							}else{
								validationResponseDTO.setValidationSuccess(true);
							}
						}
						
						if(flagsRequested.isPromptForCoinsPlayed()){
							int coinsPlayed=promptDetailsRequestObject.getCoinsPlayed();
							if(coinsPlayed<=0){
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Coins Played not valid");
								log.info("Coins Played not valid");
								return validationResponseDTO;
							}else{
								validationResponseDTO.setValidationSuccess(true);
							}
						}
						
						if(flagsRequested.isPromptShift()){
							int shiftSelected=promptDetailsRequestObject.getShiftSelected();
							if( (shiftSelected == JACKPOT_SHIFT_DAY || 
									shiftSelected == JACKPOT_SHIFT_SWING ||
									shiftSelected == JACKPOT_SHIFT_GRAVEYARD) ){
								validationResponseDTO.setValidationSuccess(true);
							}else{
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Shift Entered not valid");
								log.info("Shift Entered not valid");
								return validationResponseDTO;
							}
						}
						
						
						
						//checking for Auth Emp.
						if(flagsRequested.isPromptEmployeeAuth()){
							//do emp auth validation
							String employeeCardNumber=promptDetailsRequestObject.getEmployeeCrdNumberAuthorizing();
							if(employeeCardNumber!=null && !((employeeCardNumber.trim()+" ").equalsIgnoreCase(" ")) ){
								
								
								//checking for same employee card number
								String employeeCardNoProcessing=promptDetailsRequestObject.getEmployeeCrdNumberProcessing();
								if(employeeCardNoProcessing!=null && !((employeeCardNoProcessing.trim()+" ").equalsIgnoreCase(" ")) ){
									if(employeeCardNoProcessing.equalsIgnoreCase(employeeCardNumber)){
										validationResponseDTO.setValidationSuccess(false);
										validationResponseDTO.setErrorMessage("Employee Card should not be the same.");
										log.info("Employee Card should not be the same.");
										return validationResponseDTO;
									}
								}
								
								IAnABO AnABO=(IAnABO)ServiceLocator.fetchService(LOOKUP_SDS_EMPLOYEE_BO, IS_LOCAL_LOOKUP);
								if(AnABO!=null){
									AnAUserForm anAUserFormGot=AnABO.getEmployeeNameAndFunctionsAdvanced(employeeCardNumber.trim(), siteNumber);
									if(anAUserFormGot!=null && !(anAUserFormGot.isErrorPresent())){								
										FunctionForm[] functionFormsGot=anAUserFormGot.getFunctionForms();
										if(functionFormsGot!=null && functionFormsGot.length!=0){
											boolean processFunctionPresent=false;
											for(int i=0;i<functionFormsGot.length;i++){
												FunctionForm functionFormGot=functionFormsGot[i];
												if(functionFormGot.getFunctionName().equalsIgnoreCase(EMP_FUNCTION_AUTH_ONE_PERSON)){
													processFunctionPresent=true;
													break;
												}										
											}									
											if(processFunctionPresent){
												String empFirstName=anAUserFormGot.getFirstName();
												String empLastName=anAUserFormGot.getLastName();
												String empLogin=anAUserFormGot.getUserName();
												validationResponseDTO.setValidationSuccess(true);
												validationResponseDTO.setEmployeeCardNoAuth(employeeCardNumber.trim());
												try{
													validationResponseDTO.setEmployeeIdAuth(Integer.parseInt(empLogin.trim()));
												}catch (Exception e) {

												}
												validationResponseDTO.setEmployeeFirstNameAuth(empFirstName);
												validationResponseDTO.setEmployeeLastNameAuth(empLastName);	
												
												boolean authLevelValidationSuccess=false;
												//checking for higher auth level of the auth employee .
												AnAUserForm anaUserFormForAuth=AnABO.getAnARoleDetailsAdvancedWithAllParams(employeeCardNumber.trim(), siteNumber);
												if(anaUserFormForAuth!=null && anaUserFormForAuth.getParameterForms()!=null &&
														anaUserFormForAuth.getParameterForms().length!=0){
													ParameterForm[] parameterForms=anaUserFormForAuth.getParameterForms();
													for(int l=0;l<parameterForms.length;l++){
														ParameterForm parameterFormGotHere=parameterForms[l];
														if(parameterFormGotHere!=null && 
																parameterFormGotHere.getParameterName()!=null && parameterFormGotHere.getParameterName().equalsIgnoreCase(IKeypadProcessConstants.ROLE_PARAM_AUTH_LVL_KEY)){
															String authLevelOfAuthorizer=parameterFormGotHere.getParameterValue();
															if(authLevelOfAuthorizer!=null && flagsRequested.getAuthLevel()!=null){																
																try{
																	int authOfAuthorizer=Integer.parseInt(authLevelOfAuthorizer.trim());
																	int authOfEmployee=Integer.parseInt(flagsRequested.getAuthLevel().trim());
																	log.info("Authorization Level of the Authorizer :"+authOfAuthorizer);
																	log.info("Authorization Level of the Processor :"+authOfEmployee);
																	if((authOfAuthorizer > authOfEmployee) && !(authOfAuthorizer<=0) ){
																		authLevelValidationSuccess=true;
																		break;
																	}
																}catch (Exception e) {
																	
																}
															}
														}													
														
													}													
												}
												
												if(!authLevelValidationSuccess){
//													log error as emp authorization not enough.
													validationResponseDTO.setValidationSuccess(false);
													validationResponseDTO.setErrorMessage("Authorization Level is not Sufficient.");
													log.info("Authorization Level is not Sufficient.");
													return validationResponseDTO;
												}
												
												
											}else{
												//log error as emp doesnot have the function to authorize pending jackpot.
												validationResponseDTO.setValidationSuccess(false);
												validationResponseDTO.setErrorMessage("Employee Does not have function.");
												log.info("Employee Doesnt have function");
												return validationResponseDTO;
											}

										}else{
											//log error as emp doesnot have the function to authorize pending jackpot.
											validationResponseDTO.setValidationSuccess(false);
											validationResponseDTO.setErrorMessage("Employee Does not have function.");
											log.info("Employee Doesnt have function");
											return validationResponseDTO;
										}

									}else{
										//log error as emp card invalid.
										validationResponseDTO.setValidationSuccess(false);
										validationResponseDTO.setErrorMessage("Card Invalid.");
										log.info("Card Invalid");
										return validationResponseDTO;
									}
								}
							}else{
								//log error as emp card number null.
								validationResponseDTO.setValidationSuccess(false);
								validationResponseDTO.setErrorMessage("Employee Card Id not present.");
								log.info("Employee Card Id not present.");
								return validationResponseDTO;
							}
						}				
						
					}					

				}else if(customRequest instanceof PrinterSelectionRequestObject){

				}
			}else{
				log.info("Required details are not there in validateRequest method.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return validationResponseDTO;
	}


}
