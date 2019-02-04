package com.bkt.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkt.models.InstitutionCalender;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Student;
import com.bkt.models.SubPaymentPurpose;
import com.bkt.models.USSDTempLog;
import com.bkt.utils.ProcessUssdResp;
import com.bkt.utils.SMSAPI;
import com.bkt.utils.SchoolFeesCron;
import com.bkt.utils.USSDHelperUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class USSDHandler extends Controller {
	private static final String OUTER_WELCOME = "Welcome to University portal for Urubuto:#1)Eng #2)Kiny";
	public static final String SCHOOL_SERVICES_KINY = "Hitamo Kaminuza: #1)Univeristy of Rwanda(UR)";
	//public static final String SCHOOL_SERVICES_KINY = "Hitamo Serivici: #1)Minerivari#2)Amanota#3)Amatangazo#4)Tanga ubutumwa";
	public static final String SCHOOL_SERVICES = "Select University: #1)University of Rwanda(UR)";
	//public static final String SCHOOL_SERVICES = "Select Service: #1)School fees#2)Marks Results#3)School Announcements#4)Leave Message";
	public static final String BACK_MENU_SCHOOL = "#00)Home#000)Exit";
	public static final String BACK_MENU_KINY_SCHOOL = "#00)Ahabanza#000)Gusohoka";
	public static final String EXIT_MESSAGE_SCHOOL = "Thank you for using URUBUTO Services. Powered by BK TecHouse.";
	public static final String EXIT_MESSAGE_KINY_SCHOOL = "Murakoze gukoresha Serivisi z'URUBUTO. Powered by BK TecHouse.";

	private static final Logger LOG = LoggerFactory.getLogger(USSDHandler.class);

	@SuppressWarnings("deprecation")
	@BodyParser.Of(BodyParser.Json.class)
	public static Result ussdRequests() {

		response().setContentType("text/xml");
		// String newRequest;
		String msisdn = null;
		String userMessage = null;
		String levelStr = null;
		try {
			JsonNode asJson = request().body().asJson();
			LOG.info("Request from USSD..."+asJson);
			// newRequest=asJson.findPath("newRequest").textValue();
			msisdn = asJson.findPath("msisdn").textValue();
			userMessage = asJson.findPath("userMessage").textValue();
			levelStr = asJson.findPath("levelStr").textValue();
		} catch (Exception e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		String responseText = "";
		String action = "FC";
		String myResponse = "input";

		LOG.info("Request coming....msidn:" + msisdn + "...input:" + userMessage + "....level:" + levelStr);
		try {

			USSDTempLog myLog = null;
			try {
				myLog = USSDTempLog.find.where().eq("msisdn", msisdn).findUnique();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (levelStr.equals("0") && userMessage.equalsIgnoreCase("5")) {
				//if (levelStr.equals("0") && userMessage.equalsIgnoreCase("34")) {
				try {
					if (myLog.id > 0) {
						for (USSDTempLog myTemp : USSDTempLog.find.where().eq("msisdn", msisdn).findList()) {
							myTemp.delete();
						}
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				int level = 1;
				myLog = new USSDTempLog();
				myLog.level = level;
				myLog.msisdn = msisdn;
				myLog.status = 0;
				myLog.textMessage = userMessage;

				try {
					myResponse = OUTER_WELCOME;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					myLog.register = 1;
					myResponse = OUTER_WELCOME;

				}
				myLog.save();
			} else {

				// now direct the user to the inner levels
				int level = myLog.level;

				if (level == 1) {
					try {

						String language = userMessage;

						if (language.equals("2")) {

							myResponse = SCHOOL_SERVICES_KINY;
							myLog.language = "KINY";
						} else {

							myLog.language = "ENG";
							myResponse = SCHOOL_SERVICES;
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						String language = userMessage;
						if (language.equals("2")) {

							myResponse = SCHOOL_SERVICES_KINY;
							myLog.language = "KINY";
						} else {

							myLog.language = "ENG";
							myResponse = SCHOOL_SERVICES;
						}
					}
					myLog.level = 2;
					myLog.update();

				} else if (level == 2) {

					int userChoice = Integer.parseInt(userMessage);
					String language = myLog.language;

					if (userChoice == 1) {

						if (language.equals("ENG")) {

							myResponse = "Select service:#1)Pay,#2)Check debt,#3)Previous payments.";

						} else {
							myResponse = "Hitamo serivisi:#1)Kwishyura,#2)Kureba ideni,#3)Uko wishyuye.";
						}

						myLog.menuFirst = 1;
						myLog.level = 3;

					} else if (userChoice == 2) {

						if (language.equals("ENG")) {

							myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

						} else {
							myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
						}

						action = "FB";

					} else if (userChoice == 3) {

						if (language.equals("ENG")) {

							myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

						} else {
							myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
						}

						action = "FB";

					} else {

						if (language.equals("ENG")) {

							myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

						} else {
							myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
						}

						action = "FB";

					}

					myLog.update();

				} else if (level == 3) {

					int userChoice = myLog.menuFirst;
					int secondChoice = Integer.parseInt(userMessage);
					String language = myLog.language;

					if (userChoice == 1) {
						if (secondChoice == 1) {

							if (language.equals("ENG")) {
								myResponse = "Choose registration status:#1)Have registration Number,#2)No registration number yet.";
							} else {
								myResponse = "Ufite Nomero ikurangwa kw'ishuli:#1)Yego,#2)Oya ntabwo ndiyandikisha.";
							}

							myLog.secondMenu = 1;
							myLog.level = 4;

						} else if (secondChoice == 2) {

							if (language.equals("ENG")) {

								myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

							} else {
								myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
							}

							action = "FB";

						} else if (secondChoice == 3) {

							if (language.equals("ENG")) {

								myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

							} else {
								myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
							}

							action = "FB";

						} else {
							
							if (language.equals("ENG")) {

								myResponse = "This service is still in implementation process." + EXIT_MESSAGE_SCHOOL;

							} else {
								
								myResponse = "Iyi serivisi iracyanozwa izabageraho vuba." + EXIT_MESSAGE_KINY_SCHOOL;
								
							}

							action = "FB";

						}
					}

					myLog.update();

				} else if (level == 4) {

					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = Integer.parseInt(userMessage);
					String language = myLog.language;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

								if (language.equals("ENG")) {
									myResponse = "Enter your Registration/Admission Number";
								} else {
									myResponse = "Andika nomero ikuranga kw'ishuli.";
								}

								myLog.thirdMenu = 1;
								myLog.level = 5;

							} else {/*

								String removeFirstBraces = USSDHelperUtils.getInstitutionsSorted().toString()
										.replace("{", "");
								String removeLastBraces = removeFirstBraces.replaceAll("}", "");
								String removeWhiteSpace = removeLastBraces.trim();

								String institutionList = removeWhiteSpace;
								if (myLog.language.equals("ENG")) {

									myResponse = "Select Institution:#"
											+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

								} else {

									myResponse = "Muhitemo Ikigo:#"
											+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

								}
								myLog.thirdMenu = 2;
								myLog.level = 5;
								myLog.institutionList = institutionList;

							*/
							
								if (myLog.language.equals("ENG")) {

									myResponse = "Service is not available for UR now. Check again later";

								} else {

									myResponse = "Iyi serivisi ntabwo ihari muri UR. Murakoze";

								}
								action="FB";
							}
						}

					}

					myLog.update();
				} else if (level == 5) {

					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

								String regNumber = userMessage;
								try {
									Student myStudent = Student.find.where().eq("reg_number", regNumber).findUnique();

									if (myStudent.id > 0) {

										String removeFirstBraces = USSDHelperUtils
												.getInstitutionsPaymentPurposesSorted(myStudent).toString()
												.replace("{", "");
										String removeLastBraces = removeFirstBraces.replaceAll("}", "");
										String removeWhiteSpace = removeLastBraces.trim();

										String institutionList = removeWhiteSpace;
										if (myLog.language.equals("ENG")) {

											myResponse = "Select payment purpose:#"
													+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

										} else {

											myResponse = "Hitamo icyo wishyura:#"
													+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

										}
										myLog.student = myStudent;
										myLog.level = 6;
										myLog.institution=myStudent.instId;
										myLog.purposeAccountList = institutionList;

									} else {

										if (myLog.language.equals("ENG")) {
											myResponse = "Registration number entered is not known or your institution hasn't registered to use this system."
													+ EXIT_MESSAGE_SCHOOL;
										} else {
											myResponse = "Nomero mwanditse ntabwo izwi muri iyi serivisi cg ikigo cyawe ntikiratangira gukoresha iyi serivisi.Mwongere mugerageze."
													+ EXIT_MESSAGE_KINY_SCHOOL;
										}
										action = "FB";

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equals("ENG")) {
										myResponse = "Registration number entered is not known or your institution hasn't registered to use this system."
												+ EXIT_MESSAGE_SCHOOL;
									} else {
										myResponse = "Nomero mwanditse ntabwo izwi muri iyi serivisi cg ikigo cyawe ntikiratangira gukoresha iyi serivisi.Mwongere mugerageze."
												+ EXIT_MESSAGE_KINY_SCHOOL;
									}
									action = "FB";

								}

							} else {/*

								Institution inst;
								int selectedInst = 0;
								try {
									selectedInst = Integer.parseInt(userMessage);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									inst = USSDHelperUtils.getInsitutionSelected(selectedInst);
									if (inst.id > 0) {
										String removeFirstBraces = USSDHelperUtils
												.getInstitutionsPaymentPurposesSorted(inst.id).toString()
												.replace("{", "");
										String removeLastBraces = removeFirstBraces.replaceAll("}", "");
										String removeWhiteSpace = removeLastBraces.trim();

										String institutionList = removeWhiteSpace;
										if (myLog.language.equals("ENG")) {

											myResponse = "Select college:#"
													+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

										} else {

											myResponse = "Hitamo ikigo:#"
													+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

										}
										myLog.institution = inst;
										myLog.level = 6;
										myLog.purposeAccountList = institutionList;

									} else {
										
										if (myLog.language.equals("ENG")) {
											myResponse = "You selected a wrong institution. Please try again."
													+ EXIT_MESSAGE_SCHOOL;
										} else {
											myResponse = "Muhisemo ikigo kitari cyo.Mwongere mugerageze."
													+ EXIT_MESSAGE_KINY_SCHOOL;
										}
										action = "FB";
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equals("ENG")) {
										myResponse = "You selected a wrong institution. Please try again."
												+ EXIT_MESSAGE_SCHOOL;
									} else {
										myResponse = "Muhisemo ikigo kitari cyo.Mwongere mugerageze."
												+ EXIT_MESSAGE_KINY_SCHOOL;
									}
									action = "FB";

								}

							*/}
						}

					}

					myLog.update();
				} else if (level == 6) {

					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

								if(myLog.fifthMenu==1){
									
									// get selected sub payment purpose

									int selectedId = 0;
									try {
										selectedId = Integer.parseInt(userMessage);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									SubPaymentPurpose paymentPurpose = USSDHelperUtils.getSubPaymentPurposeSelected(myLog.paymentPurpose, selectedId);
									
									if (paymentPurpose.id > 0) {

										if (myLog.language.equals("ENG")) {

											myResponse = "Enter amount to pay";

										} else {

											myResponse = "Andika Amafaraga ushaka kwishyura";

										}
										myLog.level = 7;
										myLog.subPaymentPurpose = paymentPurpose;
										
									} else {

										if (myLog.language.equals("ENG")) {
											myResponse = "You selected wrong purpose. Try again." + EXIT_MESSAGE_SCHOOL;
										} else {
											myResponse = "Muhisemo impanvu itanditse.Mwongere mugerageze."
													+ EXIT_MESSAGE_KINY_SCHOOL;
										}
										action = "FB";

									}

								
								}else{
									try {

										int selectedId = 0;
										try {
											selectedId = Integer.parseInt(userMessage);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										PaymentPurpose paymentPurpose = USSDHelperUtils.getPaymentPurposeSelected(myLog.student, selectedId);
										
										if (paymentPurpose.id > 0) {
											Integer hasDepents = 0;
											
											try {
												hasDepents = paymentPurpose.hasDependent;
												LOG.info("has dependents...."+hasDepents);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												//e.printStackTrace();
												LOG.info("has dependents...."+000000);
											}
											if(null !=hasDepents && hasDepents==1){

												String removeFirstBraces = USSDHelperUtils
														.getSubPaymentPurposesSorted(paymentPurpose).toString()
														.replace("{", "");
												String removeLastBraces = removeFirstBraces.replaceAll("}", "");
												String removeWhiteSpace = removeLastBraces.trim();

												if (myLog.language.equals("ENG")) {

													myResponse = "Select payment purpose:#"
															+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

												} else {

													myResponse = "Hitamo icyo wishyura:#"
															+ removeWhiteSpace.replaceAll(",", "#").replaceAll("=", ")");

												}
												
												myLog.level = 6;
												myLog.fifthMenu=1;
											
											}else{
												if (myLog.language.equals("ENG")) {

													myResponse = "Enter amount to pay";

												} else {

													myResponse = "Andika Amafaraga ushaka kwishyura";

												}
												myLog.level = 7;
											}
											myLog.paymentPurpose = paymentPurpose;
											

										} else {

											if (myLog.language.equals("ENG")) {
												myResponse = "You selected wrong purpose. Try again." + EXIT_MESSAGE_SCHOOL;
											} else {
												myResponse = "Muhisemo impanvu itanditse.Mwongere mugerageze."
														+ EXIT_MESSAGE_KINY_SCHOOL;
											}
											action = "FB";

										}

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();

										if (myLog.language.equals("ENG")) {
											myResponse = "You selected wrong purpose. Try again." + EXIT_MESSAGE_SCHOOL;
										} else {
											myResponse = "Muhisemo impanvu itanditse.Mwongere mugerageze."
													+ EXIT_MESSAGE_KINY_SCHOOL;
										}
										action = "FB";

									}
								}
								

							} else {

								try {

									int selectedId = 0;
									try {
										selectedId = Integer.parseInt(userMessage);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									PaymentPurpose paymentPurpose = USSDHelperUtils.getPaymentPurposeSelected(myLog.student, selectedId);
									
									if (paymentPurpose.id > 0) {


										if (myLog.language.equals("ENG")) {

											myResponse = "Enter amount to pay";

										} else {

											myResponse = "Andika Amafaraga ushaka kwishyura";

										}
										myLog.paymentPurpose = paymentPurpose;
										myLog.level = 7;

									} else {

										if (myLog.language.equals("ENG")) {
											myResponse = "You selected wrong purpose. Try again." + EXIT_MESSAGE_SCHOOL;
										} else {
											myResponse = "Muhisemo impanvu itanditse.Mwongere mugerageze."
													+ EXIT_MESSAGE_KINY_SCHOOL;
										}
										action = "FB";

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equals("ENG")) {
										myResponse = "You selected wrong purpose. Try again." + EXIT_MESSAGE_SCHOOL;
									} else {
										myResponse = "Muhisemo impanvu itanditse.Mwongere mugerageze."
												+ EXIT_MESSAGE_KINY_SCHOOL;
									}
									action = "FB";

								}

							}
						}

					}

					myLog.update();

				} else if (level == 7) {

					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

								try {

									int amount = Integer.parseInt(userMessage);

									if (amount >= 100) {

										if (myLog.language.equals("ENG")) {

											myResponse = myLog.student.firstName + " " + myLog.student.lastName
													+ " with RegNo:" + myLog.student.regNumber
													+ " are you sure you want to pay "
													+ myLog.student.instId.name.toUpperCase() + " amount:" + amount
													+ " for " + myLog.paymentPurpose.purpose + "?#1)Yes#2)No";

										} else {

											myResponse = myLog.student.firstName + " " + myLog.student.lastName
													+ " ufite RegNo:" + myLog.student.regNumber + " wiga "
													+ myLog.student.instId.name.toUpperCase()
													+ ", urashaka kwishyura amafaranga:" + amount + ", Impamvu:"
													+ myLog.paymentPurpose.purpose + "?#1)Yego#2)Oya";

										}
										myLog.amount = amount;
										myLog.level = 8;

									} else {

										if (myLog.language.equals("ENG")) {

											myResponse = "Re-Enter amount to pay(amount below 1000 is not allowed) ";

										} else {

											myResponse = "Andika Amafaraga ushaka kwishyura(Amafaranga hasi ya 1000 ntiyemewe)";

										}

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equals("ENG")) {

										myResponse = "Re-Enter amount to pay(amount below 1000 is not allowed) ";

									} else {

										myResponse = "Andika Amafaraga ushaka kwishyura(Amafaranga hasi ya 1000 ntiyemewe)";

									}

								}

							} else {

								try {

									int amount = Integer.parseInt(userMessage);

									if (amount >= 100) {

										if (myLog.language.equals("ENG")) {

											myResponse = "Enter Student Names";

										} else {
											
											myResponse = "Andika Amazina y'umunyeshuli";

										}
										myLog.amount = amount;
										myLog.level = 8;

									} else {

										if (myLog.language.equals("ENG")) {

											myResponse = "Re-Enter amount to pay(amount below 1000 is not allowed) ";

										} else {

											myResponse = "Andika Amafaraga ushaka kwishyura(Amafaranga hasi ya 1000 ntiyemewe)";

										}

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equals("ENG")) {

										myResponse = "Re-Enter amount to pay(amount below 1000 is not allowed) ";

									} else {

										myResponse = "Andika Amafaraga ushaka kwishyura(Amafaranga hasi ya 1000 ntiyemewe)";

									}

								}

							}
						}

					}

					myLog.update();
				} else if (level == 8) {

					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {
								try {

									int confirm = Integer.parseInt(userMessage);

									if (confirm == 1) {

										PaymentLog myFeesLog=new PaymentLog();
										try {
											//UNIV
											//UNIV
											String transactionId = "UNIV" + USSDHelperUtils.getDateNowString() + USSDHelperUtils.randomToken();
										
											String trxId = transactionId;
											
											String respFromMtekana="";
											try {
												respFromMtekana = USSDHelperUtils.coonectToMoMoUrubuto(msisdn,
														myLog.amount + "", trxId).get();
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											System.out.println("response from urubuto momo:::" + respFromMtekana);
											ProcessUssdResp ussdRespTemp = new ProcessUssdResp(respFromMtekana);

											String statCode = ussdRespTemp.getStatusCode();
											String statusDes = ussdRespTemp.getStatusDesc();
											//String ProcessingNumber = ussdRespTemp.getProcessingNumber();
											
											myFeesLog.bankAcc=myLog.paymentPurpose.accountId;
											myFeesLog.paymentChannel="MTN mobile money channel";
											myFeesLog.paymentDevice=msisdn;
											
											myFeesLog.amountPaid=myLog.amount;
											myFeesLog.payerName=msisdn;
											myFeesLog.msisdn=msisdn;
											myFeesLog.isRegistered="yes";
											myFeesLog.instId=myLog.student.instId;
											myFeesLog.studentId=myLog.student;
											//myFeesLog.studentRef = myLog.student.regNumber;
											myFeesLog.bank=myLog.paymentPurpose.accountId.bankId;
											if(myLog.fifthMenu==1 && myLog.subPaymentPurpose!=null ){
												myFeesLog.subPaymentPurpose=myLog.subPaymentPurpose;
											}
											myFeesLog.paymentPurpose=myLog.paymentPurpose;
											myFeesLog.processingNumber=trxId;
											myFeesLog.operator="MTN";
											myFeesLog.statusDesc=statusDes;
											myFeesLog.ussdStatus=statCode;
											myFeesLog.paymentDate=USSDHelperUtils.getDateFromStringPost();
											myFeesLog.postingDate=USSDHelperUtils.getDateFromStringPost();

											InstitutionCalender institutionCalender = null;
											try {
												institutionCalender = InstitutionCalender.find.where().eq("institution_id", myLog.student.instId.id).eq("status", "active").findUnique();
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											if(institutionCalender.academicYear!=null){
												long instCaleId = 0;
												try {
													instCaleId = institutionCalender.id;
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												if(instCaleId>0){
													myFeesLog.academicYear=institutionCalender;
												}
												if (statCode.equalsIgnoreCase("1000")
															&& statusDes.equalsIgnoreCase("Pending")) {

													//save the paymentlog
													
													if (myLog.language.equalsIgnoreCase("ENG")) {
														myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

														action = "FB";
													} else{
														myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

														action = "FB";
													}
													} else {
														//save the paymentlog
														
														
														if (myLog.language.equalsIgnoreCase("ENG")) {
															myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

															action = "FB";
														} else{
															myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

															action = "FB";
														}
													}

												
											}else{
												
												if (myLog.language.equalsIgnoreCase("ENG")) {
													myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

													action = "FB";
												} else{
													myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

													action = "FB";
												}
											}
											myFeesLog.save();
										} catch (Exception e) {
											// TODO
											// Auto-generated
											// catch block

											if (myLog.language.equalsIgnoreCase("ENG")) {
												responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

												action = "FB";
											} else{
												responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

												action = "FB";
											}

											e.printStackTrace();
										}
										

									} else {

										if (myLog.language.equalsIgnoreCase("ENG")) {
											responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

											action = "FB";
										} else{
											responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

											action = "FB";
										}
									
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equalsIgnoreCase("ENG")) {
										responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

										action = "FB";
									} else{
										responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

										action = "FB";
									}

								}

							} else {
								
								
								if (myLog.language.equals("ENG")) {

									myResponse = "Enter Student NID/Passport";

								} else {
									
									myResponse = "Andika No Indangamuntu/Passport y'umunyeshuli";

								}
								
								myLog.firstName=userMessage;
								myLog.level = 9;
								

							
							}
						}

					}

					myLog.update();

				}else if(level==9){


					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

							} else {

								
										if (myLog.language.equals("ENG")) {

											myResponse = "Are you sure you want to pay "
													+ myLog.institution.name.toUpperCase() + " amount:" + myLog.amount
													+ " for " + myLog.paymentPurpose.purpose + "?#1)Yes#2)No";

										} else {

											myResponse = "Urahamya ko ushaka kwishyura amafaranga:" + myLog.amount
													+ "Rwf ikigo: " + myLog.institution.name.toUpperCase()
													+ ", Impamvu:" + myLog.paymentPurpose.purpose + "?#1)Yego#2)Oya";

										}
										myLog.nid = userMessage;
										myLog.level = 11;
										
							}
						}

					}

					myLog.update();
				
				}else if(level==11){


					int userChoice = myLog.menuFirst;
					int secondChoice = myLog.secondMenu;
					int regStatus = myLog.thirdMenu;

					if (userChoice == 1) {
						if (secondChoice == 1) {
							if (regStatus == 1) {

							} else {
								
								try {

									int confirm = Integer.parseInt(userMessage);

									if (confirm == 1) {

										PaymentLog myFeesLog=new PaymentLog();
										try {
											//UNIV
											String transactionId = "UNIV" + USSDHelperUtils.getDateNowString() + USSDHelperUtils.randomToken();
										
											String trxId = transactionId;
											
											String respFromMtekana="";
											try {
												respFromMtekana = USSDHelperUtils.coonectToMoMoUrubuto(msisdn,
														myLog.amount + "", trxId).get();
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										
											System.out.println("response from urubuto momo:::" + respFromMtekana);
											ProcessUssdResp ussdRespTemp = new ProcessUssdResp(respFromMtekana);

											String statCode = ussdRespTemp.getStatusCode();
											String statusDes = ussdRespTemp.getStatusDesc();
											//String ProcessingNumber = ussdRespTemp.getProcessingNumber();
											
											myFeesLog.bankAcc=myLog.paymentPurpose.accountId;
											myFeesLog.paymentChannel="MTN mobile money channel";
											myFeesLog.paymentDevice=msisdn;
											myFeesLog.amountPaid=myLog.amount;
											myFeesLog.payerName=msisdn;
											myFeesLog.studentNames=myLog.firstName;
											myFeesLog.nida=myLog.nid;
											myFeesLog.isRegistered="No";
											myFeesLog.msisdn=msisdn;
											myFeesLog.instId=myLog.institution;
											//myFeesLog.studentId=myLog.student;
											myFeesLog.bank=myLog.paymentPurpose.accountId.bankId;
											myFeesLog.paymentPurpose=myLog.paymentPurpose;
											myFeesLog.processingNumber=trxId;
											myFeesLog.operator="MTN";
											myFeesLog.statusDesc=statusDes;
											myFeesLog.ussdStatus=statCode;
											myFeesLog.paymentDate=USSDHelperUtils.getDateFromStringPost();
											myFeesLog.postingDate=USSDHelperUtils.getDateFromStringPost();

											InstitutionCalender institutionCalender = null;
											try {
												institutionCalender = InstitutionCalender.find.where().eq("institution_id", myLog.institution.id).eq("status", "active").findUnique();
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											if(institutionCalender.academicYear!=null){
												long instCaleId = 0;
												try {
													instCaleId = institutionCalender.id;
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												if(instCaleId>0){
													myFeesLog.academicYear=institutionCalender;
												}
												if (statCode.equalsIgnoreCase("1000")
															&& statusDes.equalsIgnoreCase("Pending")) {

													//save the paymentlog
													
													if (myLog.language.equalsIgnoreCase("ENG")) {
														myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

														action = "FB";
													} else{
														myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

														action = "FB";
													}
													} else {
														
														if (myLog.language.equalsIgnoreCase("ENG")) {
															myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

															action = "FB";
														} else{
															myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

															action = "FB";
														}
													}

												
											}else{

												
												if (myLog.language.equalsIgnoreCase("ENG")) {
													myResponse = "Thank you,Dial *182# and go to pending approvals to finish payment";

													action = "FB";
												} else{
													myResponse = "Murakoze,Kanda *182# ujye mubyemezo bitaranozwa kugirango musoze kwishyurira.";

													action = "FB";
												}
											
											}

											myFeesLog.save();
										} catch (Exception e) {
											// TODO
											// Auto-generated
											// catch block

											if (myLog.language.equalsIgnoreCase("ENG")) {
												responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

												action = "FB";
											} else{
												responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

												action = "FB";
											}

											e.printStackTrace();
										}
										

									} else {

										if (myLog.language.equalsIgnoreCase("ENG")) {
											responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

											action = "FB";
										} else{
											responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

											action = "FB";
										}
									
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									if (myLog.language.equalsIgnoreCase("ENG")) {
										responseText = "School fees payment transaction has failed. try again later."+EXIT_MESSAGE_SCHOOL;

										action = "FB";
									} else{
										responseText = "Kwishyura minerivari ukoresheje telephone ntibyemeye. Mwongere mugerageze."+EXIT_MESSAGE_KINY_SCHOOL;

										action = "FB";
									}

								}

							
							}
						}

					}

					myLog.update();

				
				}
			}

			responseText = getUSSDResponse(action, 0, myResponse, msisdn);

		} catch (Exception e) {

			// TODO Auto-generated catch block
			int level = 0;

			myResponse = "There is a network issue.Hold on";
			action = "FB";
			e.printStackTrace();
			System.out.println("text to send to urubuto:::" + myResponse);
			responseText = getUSSDResponse(action, level, myResponse, msisdn);
		}
		return ok(responseText);
	}
	
	
	@SuppressWarnings("deprecation")
	@BodyParser.Of(BodyParser.Json.class)
	public static Result approvedPayment() {
		
		JsonNode asJson = request().body().asJson();
		String logBankAcc="";
		String stdentRef="";
		//String msisdn=asJson.findPath("msisdn").textValue();
		String momoRef=asJson.findPath("momoRef").textValue();
		String processNumber=asJson.findPath("processNumber").textValue();
		String momoCode=asJson.findPath("momoCode").textValue();
		String StatusDesc=asJson.findPath("StatusDesc").textValue();
		LOG.info("Request parsed..from MTN momo....momoref:"+momoRef+"...processingNumber:"+processNumber+"...statusDesc:"+StatusDesc+"..momoCode:"+momoCode);
		PaymentLog myFeesLog = null;
		try {
			myFeesLog = PaymentLog.find.where().eq("processing_number", processNumber).findUnique();
		//	myFeesLog = PaymentLog.find.where().eq("msisdn",msisdn).eq("processing_number", processNumber).findUnique();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//if (momoCode.equalsIgnoreCase("00000000")|| StatusDesc.contains("Successfully") || msisdn.contains("250788215324") || msisdn.contains("250788683008")) {//0783800812
		if (StatusDesc.equalsIgnoreCase("SUCCESSFUL")) {
		//if (StatusDesc.equalsIgnoreCase("SUCCESSFUL") || StatusDesc.equalsIgnoreCase("FAILED")) {
		//if (StatusDesc.length()>0) {
			
				try {
					if(myFeesLog.id>0){
						
						ObjectNode userJson = Json.newObject();
						logBankAcc=myFeesLog.bankAcc.accountNumber;
						stdentRef=myFeesLog.studentId.regNumber;
						
						if(myFeesLog.isRegistered.equalsIgnoreCase("no")){
							userJson.put("studentNid", myFeesLog.nida);
							userJson.put("studentnames", myFeesLog.studentNames);
							userJson.put("isRegistered", "no");
							userJson.put("schoolid", myFeesLog.instId.instCode);
							userJson.put("schoolname", myFeesLog.instId.name);
							userJson.put("pendingamount", 0);
							userJson.put("facultyName", "0");
							userJson.put("facultyId", "0");
							userJson.put("paymentPurpose", myFeesLog.paymentPurpose.description);	
							userJson.put("bankcode",myFeesLog.bank.bnrCode);	
							userJson.put("bankname", myFeesLog.bank.name);	
							userJson.put("bankaccount", myFeesLog.bankAcc.accountNumber);
						}else{

							userJson.put("isRegistered", "yes");
							userJson.put("studentid", myFeesLog.studentId.regNumber);
							userJson.put("studentnames", myFeesLog.studentId.firstName+" "+myFeesLog.studentId.lastName);
							userJson.put("schoolid", myFeesLog.instId.id);
							userJson.put("schoolname", myFeesLog.instId.name);
							userJson.put("facultyName", "0");
							userJson.put("facultyId", "0");
							userJson.put("bankcode",myFeesLog.bank.bnrCode);	
							userJson.put("studentclass", "1");			
							userJson.put("bankname", myFeesLog.bank.name);	
							userJson.put("paymentPurpose", myFeesLog.paymentPurpose.description);	
							userJson.put("pendingamount",0);
							userJson.put("bankaccount", myFeesLog.bankAcc.accountNumber);
						
						}
								
						myFeesLog.extTrxId=momoRef;
						myFeesLog.statusDesc=StatusDesc;
						
					
						userJson.put("code", "040");
						userJson.put("username", "BK");
						userJson.put("transactionid", myFeesLog.extTrxId);
						userJson.put("academicyear", "2018");
						userJson.put("semister", "1");		
						userJson.put("payername", myFeesLog.payerName);	
						userJson.put("amountpaid", myFeesLog.amountPaid);		
						userJson.put("datetime", USSDHelperUtils.getDateNow());
						userJson.put("operator", myFeesLog.operator);
						userJson.put("observation", "Momo payment via msisdn:"+myFeesLog.msisdn);
						
						LOG.info("::::::::::::::::::::::::resquest to BK Bank:::::"+userJson);
						
						JsonNode postBank = Json.newObject();
						try {
							ObjectMapper mapper = new ObjectMapper();
							String resu = "";
							try {
								resu = USSDHelperUtils.postPayment(userJson.toString());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							LOG.info("::::::::::::::::::::::::response from BK Bank:::::"+resu);
							
							try {
								if(resu.length()>0){
									postBank = mapper.readTree(resu);
								}
								
							}catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							String bankSlip = "";
							if(postBank.has("status")){
								if(postBank.findPath("status").textValue().equalsIgnoreCase("success")){
									
									if (postBank.findPath("bankSlip").textValue()!=null) {
										bankSlip = postBank.findPath("bankSlip").textValue();
										myFeesLog.bankSlip=bankSlip;
										myFeesLog.extTrxId=momoRef;
										myFeesLog.statusDesc="posted";
										myFeesLog.ussdStatus="success";
									
										String checkSum=USSDHelperUtils.getSequenceNo();
										if(checkSum.equals("none")){
											
											myFeesLog.batchCheckSum="none";
											myFeesLog.logged=5;
										}else{
											myFeesLog.batchCheckSum=checkSum;
											myFeesLog.logged=0;
										}
										myFeesLog.logBankAccount=logBankAcc;
										myFeesLog.studentRef=stdentRef;
										
										
										String amount="";
										//String instName="";
										//String bankName="";
										try {
											amount = myFeesLog.amountPaid+"";
											//instName = myFeesLog.instId.name;
											//bankName = myFeesLog.bank.name;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										String smsStr="School fees Amount payment:"+amount+"RWF is successfully deposited at institution's account with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										ObjectNode SMSJson = Json.newObject();
										
										SMSJson.put("clientId", "TMS");
										SMSJson.put("userName", "tmsuser");
										SMSJson.put("password", "tw3n3er");
										SMSJson.put("recipientNumber", myFeesLog.msisdn);
										SMSJson.put("textContent", smsStr);
										SMSJson.put("senderName", "URUBUTO");
										
										//String smsStr="School fees Amount payment:"+amount+"RWF is successfully deposited at "+instName+"'s account in:"+bankName+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										int SMSsent = SMSAPI.sendSMS(SMSJson).get();
											LOG.info("SMS is sent response code...."+SMSsent);
										
									}else {

										myFeesLog.bankSlip="";
										myFeesLog.extTrxId=momoRef;
										myFeesLog.ussdStatus="success";
										myFeesLog.statusDesc="Success at MTN and Pending at Bank";

									}
								}else{
									
									myFeesLog.bankSlip="";
									myFeesLog.extTrxId=momoRef;
									myFeesLog.ussdStatus="success";
									myFeesLog.statusDesc="Success at MTN and Pending at Bank";
								}	
							}else{
	
								myFeesLog.bankSlip="";
								myFeesLog.extTrxId=momoRef;
								myFeesLog.ussdStatus="success";
								myFeesLog.statusDesc="Success at MTN and Pending at Bank";
							
							}
							
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							myFeesLog.bankSlip="";
							myFeesLog.extTrxId=momoRef;
							myFeesLog.ussdStatus="success";
							myFeesLog.statusDesc="Success at MTN and Pending at Bank";
						}
						myFeesLog.update();
						
						
							LOG.info("Successfully updated>>>>>>>Rent M2U2B>>"
									+ myFeesLog.statusDesc
									+ ":::Institution name== "
									+ myFeesLog.instId.name
									+ ":::processingNumber=="
									+ myFeesLog.processingNumber
									+ ":::Amount=="
									+ myFeesLog.amountPaid
									+ ":::MSISDN==" + myFeesLog.msisdn);
						
						
					
					}else{//here is ok
						//LOG.info("SERIOUS ERROR:>>>>>>>READING APPROVED TRX FROM MTN>>"+ myFeesLog.msisdn+" with reference number"+processNumber);
						myFeesLog=new PaymentLog();
						myFeesLog.processingNumber=processNumber;
						myFeesLog.statusDesc="Success at MTN and Pending at Bank";
						myFeesLog.msisdn=myFeesLog.msisdn;
						myFeesLog.ussdStatus="success";
						myFeesLog.save();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//LOG.info("SERIOUS ERROR:>>>>>>>READING APPROVED TRX FROM MTN>>"+ myFeesLog.msisdn+" with reference number"+processNumber);
					e.printStackTrace();
				}
			
			
		}else if (momoCode.equalsIgnoreCase("1001")) {// rejected
			
			myFeesLog.statusDesc="Failed";
			myFeesLog.ussdStatus="Failed";//rejected
			myFeesLog.update();
			
			
		}else if (StatusDesc.equalsIgnoreCase("FAILED")) {// rejected
			
			myFeesLog.statusDesc="Failed";
			myFeesLog.ussdStatus="Failed";//rejected
			myFeesLog.update();
			
			
		} else {
			
		}
		return ok("Success");
	
	}public static Result sysCrosTrigger() {


		 LOG.debug("Univerisity...update transactions pending at mtn: " + USSDHelperUtils.getDateNow());
		 
		 try {
				USSDHelperUtils.manageTrxAndCheckSum();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
		 try {
				SchoolFeesCron.updatePurchaseTransaction();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 try {
			SchoolFeesCron.postSchoolFeesTransactions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 try {
				USSDHelperUtils.writeURXML();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		return ok();
	}

	public static String getUSSDResponse(String action, int level, String responseTxt, String msisdn) {
		StringBuffer responseBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		responseBuffer.append(
				"<appbody><action>" + action + "</action><msisdn>" + msisdn + "</msisdn><level>" + level + "</level>");
		responseBuffer.append("<response>" + responseTxt + "</response><application>100</application>");
		responseBuffer.append("<datetime>" + USSDHelperUtils.getDateNow() + "</datetime>");
		responseBuffer.append("</appbody>");

		return responseBuffer.toString();
	}
}
