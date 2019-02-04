package com.bkt.controllers.payment;

import java.util.List;

import org.slf4j.MDC;

import com.bkt.models.BankAccount;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Student;
import com.bkt.models.SubPaymentPurpose;
import com.bkt.utils.USSDHelperUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class BankSideController extends Controller {
	private static final Logger.ALogger logger = Logger.of(BankSideController.class);

	/*
	 * The function to call when third party applications are checking the
	 * student details using student id
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getStudentDetails() {

		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");

			logger.trace("Responded json: {}", userJson.toString());

			return badRequest(userJson);
		} else {
			// String code = asJson.findPath("code").textValue();
			// String service= asJson.findPath("service").textValue();
			String username = asJson.findPath("username").textValue();
			String password = asJson.findPath("password").textValue();
			// String operator= asJson.findPath("operator").textValue();
			String studentid = asJson.findPath("studentid").textValue();

			// do authentication
			if (username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("BKbauk123!")) {
				// if(username.equalsIgnoreCase("bk") &&
				// password.equalsIgnoreCase("bk123!")){
				Student user = Student.find.where().eq("reg_number", studentid).findUnique();
				long stdId = 0;
				try {
					stdId = user.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (stdId > 0) {
					ObjectNode userJson = Json.newObject();

					ObjectMapper mapper = new ObjectMapper();

					userJson.put("studentid", user.regNumber);
					userJson.put("studentnames", user.firstName.trim() + " " + user.lastName.trim());
					userJson.put("schoolid", user.instId.id);
					userJson.put("schoolname", user.instId.name);
					userJson.put("accronym", user.instId.accronym);
					userJson.put("instCode", user.instId.instCode);
					userJson.put("studentPayTransactionFees", user.instId.studentPayTransactionFees);
					userJson.put("facultyName", user.facultyId.name);
					userJson.put("facultyId", user.facultyId.id);
					userJson.put("academicyear", user.academicYear.academicYear);
					userJson.put("stdClass", user.stdClass);
					userJson.put("semister", "");

					// set bank accounts
					ObjectNode accSectionJson = Json.newObject();
					ObjectNode paymentPurposeJson;
					ArrayNode arrayAccs = mapper.createArrayNode();
					int count = 0;
					// fetch all accounts for above institution
					try {
						// int myPurpsoe=1;
						// check if student has college then call bank accounts
						// by faculty id
						Long facultId = null;
						try {
							facultId = user.facultyId.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (null != facultId && !(facultId==29) && !(facultId==0)) {

							for (BankAccount bankAcc : BankAccount.find.where().eq("faculty_id", facultId).findList()) {

								if (bankAcc.id > 0) {

									for (PaymentPurpose myPurpose : PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).eq("faculty_id", facultId).findList()) {
										// count++;
										
										if (myPurpose.hasDependent == 1) {

											// int subCount=0;
											 //ObjectNode subpaymentJson;
											// ArrayNode arraySubAccs =
											// mapper.createArrayNode();
											for (SubPaymentPurpose suPurpose : SubPaymentPurpose.find.where().eq("payment_purpose", myPurpose.id).findList()) {
												// subCount++;
												count++;
												 //subpaymentJson =Json.newObject();
												paymentPurposeJson = Json.newObject();
												paymentPurposeJson.put("accountPurpose", myPurpose.purpose);
												paymentPurposeJson.put("accountPurposeId", myPurpose.id);
												paymentPurposeJson.put("subPurposeId", suPurpose.id);
												paymentPurposeJson.put("subPurpose", suPurpose.purpose);
												paymentPurposeJson.put("bankCode", suPurpose.accountId.bankId.bnrCode);
												paymentPurposeJson.put("bankName", suPurpose.accountId.bankId.name);
												paymentPurposeJson.put("countNo", count);
												paymentPurposeJson.put("accountNumber", suPurpose.accountId.accountNumber);
												paymentPurposeJson.put("bankAbrrev", suPurpose.accountId.bankId.accronym);

												arrayAccs.add(paymentPurposeJson);
												// arraySubAccs.add(subpaymentJson);
												// logger.info("Tracing..."+arraySubAccs);
											}
											
											// add sub purposes to the json body
											/*
											 * if(!arraySubAccs.isNull()){
											 * bankAccJson.put(
											 * "subPaymentPurposes",
											 * arraySubAccs); }
											 */

										} else {
											count++;
											paymentPurposeJson = Json.newObject();
											paymentPurposeJson.put("accountPurpose", myPurpose.purpose);
											paymentPurposeJson.put("accountPurposeId", myPurpose.id);

											paymentPurposeJson.put("bankCode", bankAcc.bankId.bnrCode);
											paymentPurposeJson.put("bankName", bankAcc.bankId.name);
											paymentPurposeJson.put("countNo", count);
											paymentPurposeJson.put("accountNumber", bankAcc.accountNumber);
											paymentPurposeJson.put("bankAbrrev", bankAcc.bankId.accronym);

											arrayAccs.add(paymentPurposeJson);
										}
										
										// myPurpsoe++;
									}

								}
							}
						} else {
							for (BankAccount bankAcc : BankAccount.find.where().eq("institution_id", user.instId.id).findList()) {

								if (bankAcc.id > 0) {

									for (PaymentPurpose myPurpose : PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()) {
										// count++;

										if (myPurpose.hasDependent == 1) {
											// int subCount=0;
											// ObjectNode subpaymentJson;
											// ArrayNode arraySubAccs =
											// mapper.createArrayNode();
											for (SubPaymentPurpose suPurpose : SubPaymentPurpose.find.where().eq("payment_purpose", myPurpose.id).findList()) {
												count++;
												// subpaymentJson =
												// Json.newObject();
												paymentPurposeJson = Json.newObject();
												paymentPurposeJson.put("accountPurposeId", myPurpose.id);
												paymentPurposeJson.put("accountPurpose", myPurpose.purpose);

												paymentPurposeJson.put("subPurposeId", suPurpose.id);
												paymentPurposeJson.put("subPurpose", suPurpose.purpose);
												paymentPurposeJson.put("bankCode", suPurpose.accountId.bankId.bnrCode);
												paymentPurposeJson.put("bankName", suPurpose.accountId.bankId.name);
												paymentPurposeJson.put("countNo", count);
												paymentPurposeJson.put("accountNumber", suPurpose.accountId.accountNumber);
												paymentPurposeJson.put("bankAbrrev", suPurpose.accountId.bankId.accronym);
												
												arrayAccs.add(paymentPurposeJson);
												
												// arraySubAccs.add(subpaymentJson);
											}
											// add sub purposes to the json body
											/*
											 * if(!arraySubAccs.isNull()){
											 * bankAccJson.put(
											 * "subPaymentPurposes",
											 * arraySubAccs); }
											 */
										} else {
											count++;
											paymentPurposeJson = Json.newObject();
											paymentPurposeJson.put("accountPurposeId", myPurpose.id);
											paymentPurposeJson.put("accountPurpose", myPurpose.purpose);

											paymentPurposeJson.put("bankCode", bankAcc.bankId.bnrCode);
											paymentPurposeJson.put("bankName", bankAcc.bankId.name);
											paymentPurposeJson.put("countNo", count);
											paymentPurposeJson.put("accountNumber", bankAcc.accountNumber);
											paymentPurposeJson.put("bankAbrrev", bankAcc.bankId.accronym);
											arrayAccs.add(paymentPurposeJson);
											
										}
										// myPurpsoe++;
									}

								}
							}
						}

					} catch (Exception e) {

						// TODO Auto-generated catch block
						e.printStackTrace();
						ObjectNode errorJson = Json.newObject();

						errorJson.put("status", "Institution has no active bank account");
						errorJson.put("errorCode", "100");

						logger.trace("Responded json: {}", errorJson.toString());

						return badRequest(errorJson);
					}
					accSectionJson.put("totalBankAccounts", count);
					accSectionJson.put("accounts", arrayAccs);

					userJson.put("bankaccount", accSectionJson);

					logger.trace("Responded json: {}", userJson.toString());

					return ok(userJson);
				} else {
					ObjectNode errorJson = Json.newObject();

					errorJson.put("status", "Student record is not found");
					errorJson.put("errorCode", "100");
					return badRequest(errorJson);
				}
			} else {

				ObjectNode errorJson = Json.newObject();
				errorJson.put("status", "No not authorized");
				errorJson.put("errorCode", "100");
				return forbidden(errorJson);

			}
		}

	}

	// check school id for student who is not registered but wants to pay
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getSchoolDetails() {

		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");

			logger.trace("Responded json: {}", userJson.toString());

			return badRequest(userJson);
		} else {
			// String code = asJson.findPath("code").textValue();
			// String service= asJson.findPath("service").textValue();
			String username = asJson.findPath("username").textValue();
			String password = asJson.findPath("password").textValue();
			// String operator= asJson.findPath("operator").textValue();
			String schoolid = asJson.findPath("schoolid").textValue();

			// do authentication
			if (username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("BKbauk123!")) {
				Institution user = Institution.find.where().eq("inst_code", schoolid).findUnique();
				long userId = 0;
				try {
					userId = user.id;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (userId > 0) {
					ObjectNode userJson = Json.newObject();

					ObjectMapper mapper = new ObjectMapper();

					userJson.put("schoolid", user.id);
					userJson.put("schoolname", user.name);
					userJson.put("studentPayTransactionFees", user.studentPayTransactionFees);
					InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", user.id).eq("status", "active").findUnique();

					try {
						userJson.put("academicyear", institutionCalender.academicYear);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ObjectNode errorJson = Json.newObject();

						errorJson.put("status", "Institution has no active academic year found");
						errorJson.put("errorCode", "100");
						return badRequest(errorJson);
					}
					userJson.put("semister", "");

					// set bank accounts
					ObjectNode accSectionJson = Json.newObject();
					ObjectNode bankAccJson;
					ArrayNode arrayAccs = mapper.createArrayNode();
					int count = 0;
					// fetch all accounts for above institution
					try {

						for (BankAccount bankAcc : BankAccount.find.where().eq("institution_id", user.id).findList()) {

							if (bankAcc.id > 0) {

								// int myPurpsoe=1;
								for (PaymentPurpose myPurpose : PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()) {
									// count++;

									if (myPurpose.hasDependent == 1) {
										// int subCount=0;
										// ObjectNode subpaymentJson;
										// ArrayNode arraySubAccs =
										// mapper.createArrayNode();
										for (SubPaymentPurpose suPurpose : SubPaymentPurpose.find.where().eq("payment_purpose", myPurpose.id).findList()) {
											count++;
											// subpaymentJson =
											// Json.newObject();
											bankAccJson = Json.newObject();
											bankAccJson.put("accountPurpose", myPurpose.purpose);
											bankAccJson.put("accountPurposeId", myPurpose.id);

											bankAccJson.put("subPurposeId", suPurpose.id);
											bankAccJson.put("subPurpose", suPurpose.purpose);
											bankAccJson.put("bankCode", suPurpose.accountId.bankId.bnrCode);
											bankAccJson.put("bankName", suPurpose.accountId.bankId.name);
											bankAccJson.put("countNo", count);
											bankAccJson.put("accountNumber", suPurpose.accountId.accountNumber);
											bankAccJson.put("bankAbrrev", suPurpose.accountId.bankId.accronym);

											arrayAccs.add(bankAccJson);

											// arraySubAccs.add(subpaymentJson);
										}
										// add sub purposes to the json body
										/*
										 * if(!arraySubAccs.isNull()){
										 * bankAccJson.put("subPaymentPurposes",
										 * arraySubAccs); }
										 */
									} else {
										count++;
										bankAccJson = Json.newObject();
										bankAccJson.put("accountPurpose", myPurpose.purpose);
										bankAccJson.put("accountPurposeId", myPurpose.id);

										bankAccJson.put("bankCode", bankAcc.bankId.bnrCode);
										bankAccJson.put("bankName", bankAcc.bankId.name);
										bankAccJson.put("countNo", count);
										bankAccJson.put("accountNumber", bankAcc.accountNumber);
										bankAccJson.put("bankAbrrev", bankAcc.bankId.accronym);
										arrayAccs.add(bankAccJson);

									}

									// myPurpsoe++;
								}

							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ObjectNode errorJson = Json.newObject();
						errorJson.put("status", "Institution has no active bank accounts found");
						errorJson.put("errorCode", "100");
						return badRequest(errorJson);
					}
					accSectionJson.put("totalBankAccounts", count);
					accSectionJson.put("accounts", arrayAccs);

					userJson.put("bankaccount", accSectionJson);

					return ok(userJson);
				} else {
					ObjectNode errorJson = Json.newObject();
					errorJson.put("status", "Institution not found");
					errorJson.put("errorCode", "100");
					return badRequest(errorJson);
				}
			} else {

				ObjectNode userJson = Json.newObject();
				userJson.put("status", "No not authorized");
				return forbidden(userJson);

			}
		}

	}

	// Receive payment from the bank by student with id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result postSchoolFeesPaidByRegisteredStudent() {

		String transactionId = USSDHelperUtils.getDateNowString();
		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.info("Received json: {}", request().body().toString());

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");

			logger.info("Responded json: {}", userJson.toString());

			return badRequest(userJson);
		} else {

			String code = asJson.findPath("code").textValue();
			String service = asJson.findPath("service").textValue();
			String username = asJson.findPath("username").textValue();
			String password = asJson.findPath("password").textValue();
			String operator = asJson.findPath("operator").textValue();
			long schoolid;
			try {
				schoolid = asJson.findPath("schoolid").longValue();
			} catch (Exception e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
				ObjectNode userJson = Json.newObject();
				userJson.put("status", "schoolid field is missing");
				userJson.put("errorCode", "100");
				return badRequest(userJson);
			}
			String transactionid = asJson.findPath("transactionid").textValue();
			String studentid;
			try {
				studentid = asJson.findPath("studentid").textValue();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				ObjectNode userJson = Json.newObject();
				userJson.put("status", "studentid field is missing");
				userJson.put("errorCode", "100");
				return badRequest(userJson);
			}
			// String academicyear= asJson.findPath("academicyear").textValue();
			// String semister= asJson.findPath("semister").textValue();
			// String datetime= asJson.findPath("datetime").textValue();
			// String bankname= asJson.findPath("bankname").textValue();
			String bankaccount = asJson.findPath("bankaccount").textValue();
			Long paymentPurposeId = asJson.findPath("paymentPurposeId").longValue();
			// String paymentPurpose=
			// asJson.findPath("paymentPurpose").textValue();

			// String observation= asJson.findPath("observation").textValue();
			String payername = asJson.findPath("payername").textValue();
			String slipnumber = null;
			try {
				slipnumber = asJson.findPath("slipnumber").textValue();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ObjectNode userJson = Json.newObject();
				userJson.put("status", "slipnumber field is missing");
				userJson.put("errorCode", "100");
				return badRequest(userJson);
			}

			// do authentication
			if (username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("BKbauk123!")) {
				Student myStudent = Student.find.where().eq("reg_number", studentid).findUnique();
				long stdId = 0;
				try {
					stdId = myStudent.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (stdId > 0) {
					// check institution
					Institution myInst = Institution.find.byId(schoolid);
					long instId = 0;
					try {
						instId = myInst.id;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (myStudent.instId.id == instId) {
						
						long facultId=0;
						if(myStudent.facultyId !=null){
							facultId = myStudent.facultyId.id;
						}
						
						// check bank accounts
						BankAccount myBankAcc = BankAccount.find.where().eq("account_number", bankaccount).findUnique();
						long bankId = 0;
						// boolean canceld=false;
						try {
							bankId = myBankAcc.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (bankId > 0) {
							int amount = asJson.findPath("amount").intValue();
							if (amount > 0) {
								PaymentLog myPayment = new PaymentLog();

								myPayment.amountPaid = amount;
								
								if(facultId>0){
									long myFacultyId=0;
									
									Faculty myFaculty = Faculty.find.byId(facultId);
									try {
										myFacultyId = myFaculty.id;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (myFacultyId > 0) {
										myPayment.facultId = myFaculty;
									}
								}
								
								myPayment.bank = myBankAcc.bankId;
								myPayment.bankAcc = myBankAcc;

								myPayment.extTrxId = transactionId;
								myPayment.instId = myInst;
								myPayment.bankSlip = slipnumber;
								myPayment.msisdn = payername;
								myPayment.paymentChannel = "O2C";
								myPayment.operator = operator;
								myPayment.payerName = payername;
								myPayment.paymentDate = USSDHelperUtils.getDateFromString();
								myPayment.paymentDevice = "O2C";
								myPayment.postingDate = USSDHelperUtils.getDateFromStringPost();
								myPayment.processingNumber = transactionid;
								myPayment.statusDesc = "posted";
								myPayment.studentId = myStudent;
								myPayment.ussdStatus = "success";
								myPayment.isRegistered = "yes";

								if (asJson.has("subPaymentPurposeId")) {

									SubPaymentPurpose subPurpose = SubPaymentPurpose.find.where().eq("id", asJson.get("subPaymentPurposeId").longValue()).eq("bank_account_id", myBankAcc.id).findUnique();
									long subPurposeId = 0;
									try {
										subPurposeId = subPurpose.id;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (subPurposeId > 0) {
										myPayment.subPaymentPurpose = subPurpose;
										myPayment.paymentPurpose = subPurpose.paymentPurpose;
									} else {
										// reject payment

										ObjectNode userJson = Json.newObject();
										userJson.put("status", "Sub Payment purpose not found");
										return badRequest(userJson);

									}
								} else {
									// check payment purpose
									PaymentPurpose myPurpose = PaymentPurpose.find.where().eq("id", paymentPurposeId).eq("bank_account_id", myBankAcc.id).findUnique();
									long purposeId = 0;
									try {
										purposeId = myPurpose.id;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (purposeId > 0) {
										myPayment.paymentPurpose = myPurpose;
									} else {
										// reject payment

										ObjectNode userJson = Json.newObject();
										userJson.put("status", "Payment purpose not found");
										return badRequest(userJson);

									}
								}
								InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", myInst.id).eq("status", "active").findUnique();
								long instCaleId = 0;
								try {
									instCaleId = institutionCalender.id;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (instCaleId > 0) {
									myPayment.academicYear = institutionCalender;
								} else {
									ObjectNode userJson = Json.newObject();
									userJson.put("status", "Academic year not found");
									return badRequest(userJson);
								}
								String checkSum = USSDHelperUtils.getSequenceNo();
								if (checkSum.equals("none")) {

									/* do not save trx without checksum */
									ObjectNode userJson = Json.newObject();
									userJson.put("status", "Checksum error");
									userJson.put("errorCode", "401");
									return badRequest();
								} else {
									myPayment.batchCheckSum = checkSum;

									myPayment.logBankAccount = myBankAcc.accountNumber;
									myPayment.studentRef = myStudent.regNumber;
									myPayment.logged = 0;

									myPayment.save();

									// insert payment log
									ObjectNode userJson = Json.newObject();
									userJson.put("transactionid", transactionId);
									userJson.put("status", "success");
									userJson.put("code", code);
									userJson.put("service", service);
									userJson.put("username", username);
									userJson.put("datetime", USSDHelperUtils.getDateNow());

									return ok(userJson);
								}
							} else if (amount < 0) {// cancelled trx

								// this is a cancelled transaction
								int amountCancld = Math.abs(amount);
								PaymentLog canceledPymt = PaymentLog.find.where().eq("amount_paid", amountCancld).eq("bank_slip", asJson.findPath("slipnumber").textValue()).findUnique();
								long idcanced = 0;
								try {
									idcanced = canceledPymt.id;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (idcanced > 0) {
									// date the transaction as cancelled
									canceledPymt.statusDesc = "CANCELLED";
									canceledPymt.ussdStatus = "CANCELLED";
									canceledPymt.amountPaid = amount;
									canceledPymt.update();
									// canceld=true;

									ObjectNode userJson = Json.newObject();
									userJson.put("transactionid", transactionId);
									userJson.put("status", "success");
									userJson.put("code", code);
									userJson.put("service", service);
									userJson.put("username", username);
									userJson.put("datetime", USSDHelperUtils.getDateNow());

									return ok(userJson);

								} else {
									PaymentLog myPayment = new PaymentLog();

									myPayment.amountPaid = amount;
									myPayment.bank = myBankAcc.bankId;
									myPayment.bankAcc = myBankAcc;

									if (asJson.has("subPaymentPurposeId")) {
										SubPaymentPurpose subPurpose = SubPaymentPurpose.find.where().eq("id", asJson.get("subPaymentPurposeId").longValue()).eq("bank_account_id", myBankAcc.id).findUnique();
										long subPurposeId = 0;
										try {
											subPurposeId = subPurpose.id;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (subPurposeId > 0) {
											myPayment.subPaymentPurpose = subPurpose;
											myPayment.paymentPurpose = subPurpose.paymentPurpose;
										} else {
											// reject payment

											ObjectNode userJson = Json.newObject();
											userJson.put("status", "Sub Payment purpose not found");
											return badRequest(userJson);

										}
									} else {
										// check payment purpose
										PaymentPurpose myPurpose = PaymentPurpose.find.where().eq("id", paymentPurposeId).eq("bank_account_id", myBankAcc.id).findUnique();
										long purposeId = 0;
										try {
											purposeId = myPurpose.id;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (purposeId > 0) {
											myPayment.paymentPurpose = myPurpose;
										} else {
											// reject payment

											ObjectNode userJson = Json.newObject();
											userJson.put("status", "Payment purpose not found");
											return badRequest(userJson);

										}
									}
									myPayment.extTrxId = transactionId;
									myPayment.instId = myInst;
									myPayment.bankSlip = slipnumber;
									myPayment.msisdn = payername;
									myPayment.paymentChannel = "O2C";
									myPayment.operator = operator;
									myPayment.payerName = payername;
									myPayment.paymentDate = USSDHelperUtils.getDateFromString();
									myPayment.paymentDevice = "O2C";
									myPayment.postingDate = USSDHelperUtils.getDateFromStringPost();
									myPayment.processingNumber = transactionid;
									myPayment.statusDesc = "CANCELLED";
									myPayment.studentId = myStudent;
									myPayment.ussdStatus = "CANCELLED";
									myPayment.isRegistered = "yes";

									InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", myInst.id).eq("status", "active").findUnique();
									long instCaleId = 0;
									try {
										instCaleId = institutionCalender.id;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (instCaleId > 0) {
										myPayment.academicYear = institutionCalender;
									} else {
										ObjectNode userJson = Json.newObject();
										userJson.put("status", "Academic year not found");
										return badRequest(userJson);
									}

									myPayment.batchCheckSum = USSDHelperUtils.getSequenceNo();
									myPayment.logBankAccount = myBankAcc.accountNumber;
									myPayment.studentRef = myStudent.regNumber;
									myPayment.logged = 0;

									myPayment.save();

									// insert payment log
									ObjectNode userJson = Json.newObject();
									userJson.put("transactionid", transactionId);
									userJson.put("status", "success");
									userJson.put("code", code);
									userJson.put("service", service);
									userJson.put("username", username);
									userJson.put("datetime", USSDHelperUtils.getDateNow());

									return ok(userJson);

								}

							} else {
								// reject payment

								ObjectNode userJson = Json.newObject();
								userJson.put("status", "Amount cannot be zero");
								userJson.put("errorCode", "100");
								return badRequest(userJson);

							}
						} else {
							ObjectNode userJson = Json.newObject();
							userJson.put("status", "Account found");
							return badRequest(userJson);
						}

					} else {
						// selected a wrong university

						ObjectNode userJson = Json.newObject();
						userJson.put("status", "Selected wrong university");
						return badRequest(userJson);

					}

				} else {
					ObjectNode userJson = Json.newObject();
					userJson.put("status", "Student is not found");
					return badRequest(userJson);
				}
			} else {

				ObjectNode userJson = Json.newObject();
				userJson.put("status", "Not authorized");
				return forbidden(userJson);

			}
		}
	}// Receive payment from the bank by un registered student

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postSchoolFeesPaidByUnRegisteredStudent() {
		// String transactionId="S"+USSDHelperUtils.getDateNowString();

		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.info("Received json:", asJson.toString());

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");

			logger.trace("Responded json: {}", userJson.toString());

			return badRequest(userJson);
		} else {/*
				 * 
				 * logger.info("Request from Bank----university: {}", asJson);
				 * 
				 * String code = asJson.findPath("code").textValue(); String
				 * service= asJson.findPath("service").textValue(); String
				 * username= asJson.findPath("username").textValue(); String
				 * password= asJson.findPath("password").textValue(); String
				 * operator= asJson.findPath("operator").textValue(); long
				 * schoolid= asJson.findPath("schoolid").longValue(); String
				 * transactionid= asJson.findPath("transactionid").textValue();
				 * //String datetime= asJson.findPath("datetime").textValue();
				 * String bankaccount=
				 * asJson.findPath("bankaccount").textValue(); String
				 * paymentPurpose=
				 * asJson.findPath("paymentPurpose").textValue(); //int amount=
				 * asJson.findPath("amount").intValue();
				 * 
				 * // do authentication if(username.equalsIgnoreCase("bk") &&
				 * password.equalsIgnoreCase("BKbauk123!")){ //Student myStudent
				 * = new Student(); // check institution
				 * 
				 * Institution myInst=Institution.find.byId(schoolid);
				 * 
				 * if(myInst.id>0){
				 * 
				 * //check bank accounts BankAccount
				 * myBankAcc=BankAccount.find.where().eq("account_number",
				 * bankaccount).findUnique(); long bankAccId = 0; try {
				 * bankAccId = myBankAcc.id; } catch (Exception e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * if(bankAccId>0){ //check payment purpose PaymentPurpose
				 * myPurpose=PaymentPurpose.find.where().eq("purpose",
				 * paymentPurpose).eq("bank_account_id",
				 * myBankAcc.id).findUnique(); long purposeId = 0; try {
				 * purposeId = myPurpose.id; } catch (Exception e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * if(purposeId>0){
				 * 
				 * PaymentLog pymtLong=new PaymentLog();
				 * 
				 * pymtLong.bankAcc=myBankAcc; pymtLong.instId = myInst;
				 * pymtLong.paymentPurpose = myPurpose; pymtLong.bank =
				 * myBankAcc.bankId;
				 * 
				 * 
				 * //student details
				 * 
				 * if(asJson.has("studentNames")) { pymtLong.studentNames =
				 * asJson.findPath("studentNames").textValue();
				 * 
				 * }else{ ObjectNode httpStatus = Json.newObject();
				 * 
				 * httpStatus.put("Code", "100"); httpStatus.put("error",
				 * "Student names are missing"); return badRequest(httpStatus);
				 * } if(asJson.has("nationalId")){ pymtLong.nida =
				 * asJson.findPath("nationalId").textValue();
				 * 
				 * }else{ ObjectNode httpStatus = Json.newObject();
				 * httpStatus.put("Code", "101"); httpStatus.put("error",
				 * "Student NIDA/passport is missing"); return
				 * badRequest(httpStatus); } if(asJson.has("payername")){
				 * pymtLong.payerName =
				 * asJson.findPath("payername").textValue(); }else{ ObjectNode
				 * httpStatus = Json.newObject(); httpStatus.put("Code", "103");
				 * httpStatus.put("error", "Payer name is missing"); return
				 * badRequest(httpStatus);
				 * 
				 * }
				 * 
				 * if(asJson.has("studentPhone")){
				 * if(USSDHelperUtils.isPhoneValid(asJson.findPath(
				 * "studentPhone").textValue())){ pymtLong.msisdn =
				 * asJson.findPath("studentPhone").textValue();
				 * 
				 * }else{
				 * 
				 * ObjectNode httpStatus = Json.newObject();
				 * httpStatus.put("Code", "104"); httpStatus.put("error",
				 * "Student phone should be in format 2507xxxxxxxx i.e 12 digits"
				 * ); return badRequest(httpStatus); }
				 * 
				 * }else{ ObjectNode httpStatus = Json.newObject();
				 * httpStatus.put("Code", "105"); httpStatus.put("error",
				 * "Student phone is missing"); return badRequest(httpStatus); }
				 * 
				 * pymtLong.isRegistered = "no"; boolean canceld=false; int
				 * amount; try { amount = asJson.findPath("amount").intValue();
				 * if(amount>0){ pymtLong.amountPaid=amount; }else if(amount<0){
				 * // this is a cancelled transaction int
				 * amountCancld=Math.abs(amount); PaymentLog canceledPymt =
				 * PaymentLog.find.where() .eq("amount_paid", amountCancld)
				 * .eq("bank_slip", asJson.findPath("slipnumber").textValue())
				 * .findUnique();
				 * 
				 * if(canceledPymt.id>0){ //date the transaction as cancelled
				 * canceledPymt.statusDesc="CANCELLED";
				 * canceledPymt.ussdStatus="CANCELLED";
				 * canceledPymt.amountPaid=amount; canceledPymt.update();
				 * canceld=true; } }else{
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("status", "Amount cannot be zero");
				 * userJson.put("errorCode", "102"); return
				 * badRequest(userJson); } } catch (Exception e2) { // TODO
				 * Auto-generated catch block e2.printStackTrace(); ObjectNode
				 * userJson = Json.newObject(); userJson.put("status",
				 * "Amount field is missing"); userJson.put("errorCode", "106");
				 * return badRequest(userJson); } if(canceld){
				 * 
				 * pymtLong.operator = operator; pymtLong.statusDesc = "posted";
				 * pymtLong.ussdStatus = "success";
				 * 
				 * pymtLong.extTrxId = transactionId; pymtLong.bankSlip =
				 * asJson.findPath("slipnumber").textValue();
				 * pymtLong.paymentChannel = "O2C"; pymtLong.paymentDevice =
				 * "O2C"; pymtLong.processingNumber = transactionid;
				 * pymtLong.paymentDate = USSDHelperUtils.getDateFromString();
				 * pymtLong.postingDate =
				 * USSDHelperUtils.getDateFromStringPost();
				 * pymtLong.paymentYear=HelperManager.getYearToday(); //try {
				 * InstitutionCalender institutionCalender =
				 * InstitutionCalender.find.where().eq("institution_id",
				 * myInst.id).eq("status", "active").findUnique();
				 * 
				 * long instCaleId = 0; try { instCaleId =
				 * institutionCalender.id; } catch (Exception e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * if(instCaleId>0){ pymtLong.academicYear=institutionCalender;
				 * }else{ ObjectNode userJson = Json.newObject();
				 * userJson.put("errorCode", "106"); userJson.put("status",
				 * "Academic year not found"); return badRequest(userJson); }
				 * 
				 * pymtLong.save();
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("transactionid", transactionId);
				 * userJson.put("status", "success"); userJson.put("code",
				 * code); userJson.put("service", service);
				 * userJson.put("username", username); userJson.put("datetime",
				 * USSDHelperUtils.getDateNow());
				 * 
				 * return ok(userJson); }else{
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("transactionid", transactionId);
				 * userJson.put("status", "success"); userJson.put("code",
				 * code); userJson.put("service", service);
				 * userJson.put("username", username); userJson.put("datetime",
				 * USSDHelperUtils.getDateNow());
				 * 
				 * return ok(userJson); }
				 * 
				 * 
				 * }else{
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("status", "Payment purpose not saved"); return
				 * badRequest(userJson);
				 * 
				 * } }else{ ObjectNode userJson = Json.newObject();
				 * userJson.put("status", "Account found"); return
				 * badRequest(userJson); }
				 * 
				 * }else{ //selected a wrong university
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("status", "Selected wrong university"); return
				 * badRequest(userJson);
				 * 
				 * }
				 * 
				 * }else{
				 * 
				 * ObjectNode userJson = Json.newObject();
				 * userJson.put("status", "No not authorized"); return
				 * forbidden(userJson);
				 * 
				 * }
				 */

			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No not authorized, the service is not available!");
			return forbidden(userJson);

		}

	}

	// check all schools available in the system
	@BodyParser.Of(BodyParser.Json.class)
	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();

		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");

			logger.trace("Responded json: {}", userJson.toString());

			return badRequest(userJson);
		} else {
			String username = asJson.findPath("username").textValue();
			String password = asJson.findPath("password").textValue();

			// do authentication
			if (username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("BKbauk123!")) {

				List<Institution> all = Institution.find.all();

				if (all.isEmpty()) {
					ObjectNode institutionJson = Json.newObject();

					institutionJson.put("status", "No data");

					httpStatus.put("Code", "401");
					httpStatus.put("status", "Bad request");
					httpStatus.put("response", institutionJson);

					return badRequest(httpStatus);
				} else {
					ObjectNode institutionJson;
					ObjectMapper mapper = new ObjectMapper();
					ArrayNode array = mapper.createArrayNode();
					for (Institution user : all) {
						institutionJson = Json.newObject();

						institutionJson.put("accronym", user.accronym);
						institutionJson.put("instCode", user.instCode);
						institutionJson.put("schoolid", user.id);
						institutionJson.put("schoolname", user.name);
						institutionJson.put("studentPayTransactionFees", user.studentPayTransactionFees);

						// set bank accounts
						ObjectNode accSectionJson = Json.newObject();
						ObjectNode bankAccJson;
						ArrayNode arrayAccs = mapper.createArrayNode();
						int count = 0;
						// fetch all accounts for above institution
						try {
							for (BankAccount bankAcc : BankAccount.find.where().eq("institution_id", user.id).findList()) {

								if (bankAcc.id > 0) {
									// int myPurpsoe=1;
									for (PaymentPurpose myPurpose : PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()) {

										if (myPurpose.hasDependent == 1) {
											// int subCount=0;
											// ObjectNode subpaymentJson;
											// ArrayNode arraySubAccs =
											// mapper.createArrayNode();
											for (SubPaymentPurpose suPurpose : SubPaymentPurpose.find.where().eq("payment_purpose", myPurpose.id).findList()) {
												count++;
												// subpaymentJson =
												// Json.newObject();
												bankAccJson = Json.newObject();
												bankAccJson.put("accountPurpose", myPurpose.purpose);
												bankAccJson.put("accountPurposeId", myPurpose.id);

												bankAccJson.put("subPurposeId", suPurpose.id);
												bankAccJson.put("subPurpose", suPurpose.purpose);
												bankAccJson.put("bankCode", suPurpose.accountId.bankId.bnrCode);
												bankAccJson.put("bankName", suPurpose.accountId.bankId.name);
												bankAccJson.put("countNo", count);
												bankAccJson.put("accountNumber", suPurpose.accountId.accountNumber);
												bankAccJson.put("bankAbrrev", suPurpose.accountId.bankId.accronym);

												arrayAccs.add(bankAccJson);
												
												// arraySubAccs.add(bankAccJson);
											}
											// add sub purposes to the json body
											// bankAccJson.put("subPaymentPurposes",
											// arraySubAccs);
										} else {

											count++;
											bankAccJson = Json.newObject();
											bankAccJson.put("accountPurpose", myPurpose.purpose);
											bankAccJson.put("accountPurposeId", myPurpose.id);

											bankAccJson.put("bankCode", bankAcc.bankId.bnrCode);
											bankAccJson.put("bankName", bankAcc.bankId.name);
											bankAccJson.put("countNo", count);
											bankAccJson.put("accountNumber", bankAcc.accountNumber);
											bankAccJson.put("bankAbrrev", bankAcc.bankId.accronym);
											arrayAccs.add(bankAccJson);
											
										}
										// myPurpsoe++;
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						accSectionJson.put("totalBankAccounts", count);
						accSectionJson.put("accounts", arrayAccs);

						institutionJson.put("bankaccount", accSectionJson);
						array.add(institutionJson);
					}

					httpStatus.put("rowCount", all.size());
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", array);

					return ok(httpStatus);
				}
			} else {

				ObjectNode institutionJson = Json.newObject();

				institutionJson.put("status", "No data");

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Un autharized");
				httpStatus.put("response", institutionJson);

				return badRequest(httpStatus);

			}
		}
	}

}
