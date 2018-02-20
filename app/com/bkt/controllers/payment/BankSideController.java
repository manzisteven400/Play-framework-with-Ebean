package com.bkt.controllers.payment;

import java.util.List;

import org.slf4j.MDC;

import com.bkt.models.BankAccount;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Student;
import com.bkt.utils.HelperManager;
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
	  * The function to call when third party applications 
	  * are checking the student details using student id
	  * */
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
		}else{
			//String code = asJson.findPath("code").textValue();
			//String service= asJson.findPath("service").textValue();
			String username= asJson.findPath("username").textValue();
			String password= asJson.findPath("password").textValue();
			//String operator= asJson.findPath("operator").textValue();
			String studentid= asJson.findPath("studentid").textValue();
			
			// do authentication
			if(username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("bk123!")){
				Student user = Student.find.where().eq("reg_number",studentid).findUnique();
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
					userJson.put("studentnames", user.firstName+" "+user.lastName);
					userJson.put("schoolid", user.instId.id);
					userJson.put("schoolname", user.instId.name);
					userJson.put("accronym", user.instId.accronym);
					userJson.put("instCode", user.instId.instCode);
					userJson.put("schoolPayCharges", user.instId.studentPayTransactionFees);
					userJson.put("facultyName", user.facultyId.name);
					userJson.put("facultyId", user.facultyId.id);
					userJson.put("academicyear", user.academicYear.academicYear);
					userJson.put("stdClass", user.stdClass);
					userJson.put("semister", "");

					//set bank accounts
					ObjectNode accSectionJson = Json.newObject();
					ObjectNode bankAccJson;
					ArrayNode arrayAccs = mapper.createArrayNode();
					int count=0;
					//fetch all accounts for above institution
					try {
						
						for(BankAccount bankAcc:BankAccount.find.where().eq("institution_id", user.instId.id).findList()){
							
							if(bankAcc.id>0){
								
								int myPurpsoe=1;
								for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()){
									count++;
									bankAccJson = Json.newObject();
									bankAccJson.put("accountPurpose", myPurpose.purpose);
									bankAccJson.put("bankCode", bankAcc.bankId.bnrCode);
									bankAccJson.put("bankName", bankAcc.bankId.name);
									bankAccJson.put("countNo", myPurpsoe);
									bankAccJson.put("accountNumber", bankAcc.accountNumber);
									bankAccJson.put("bankAbrrev", bankAcc.bankId.accronym);
									arrayAccs.add(bankAccJson);
									myPurpsoe++;
									}
								
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ObjectNode errorJson = Json.newObject();
						
						errorJson.put("status", "Institution has no active bank account");
						errorJson.put("errorCode", "100");
						return badRequest(errorJson);
					}
					accSectionJson.put("totalBankAccounts", count);
					accSectionJson.put("accounts",arrayAccs);
					
					userJson.put("bankaccount",accSectionJson);

					return ok(userJson);
				} else {
					ObjectNode errorJson = Json.newObject();
					
					errorJson.put("status", "Student record is not found");
					errorJson.put("errorCode", "100");
					return badRequest(errorJson);
				}
			}else{

				ObjectNode errorJson = Json.newObject();
				errorJson.put("status", "No not authorized");
				errorJson.put("errorCode", "100");
				return forbidden(errorJson);
			
			}
		} 
		

	}
	 
	 //check school id for student who is not registered but wants to pay
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
		}else{
			//String code = asJson.findPath("code").textValue();
			//String service= asJson.findPath("service").textValue();
			String username= asJson.findPath("username").textValue();
			String password= asJson.findPath("password").textValue();
			//String operator= asJson.findPath("operator").textValue();
			String schoolid= asJson.findPath("schoolid").textValue();
			
			// do authentication
			if(username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("bk123!")){
				Institution user = Institution.find.where().eq("inst_code",schoolid).findUnique();
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
					userJson.put("schoolPayCharges", user.studentPayTransactionFees);
					InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", user.id).eq("status", "active").findUnique();
					
					try {
						userJson.put("academicyear",institutionCalender.academicYear);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ObjectNode errorJson = Json.newObject();
						
						errorJson.put("status", "Institution has no active academic year found");
						errorJson.put("errorCode", "100");
						return badRequest(errorJson);
					}
					userJson.put("semister", "");

					//set bank accounts
					ObjectNode accSectionJson = Json.newObject();
					ObjectNode bankAccJson;
					ArrayNode arrayAccs = mapper.createArrayNode();
					int count=0;
					//fetch all accounts for above institution
					try {
						
						for(BankAccount bankAcc:BankAccount.find.where().eq("institution_id", user.id).findList()){
							
							if(bankAcc.id>0){
								int myPurpsoe=1;
								for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()){
									count++;
									bankAccJson = Json.newObject();
									bankAccJson.put("accountPurpose", myPurpose.purpose);
									bankAccJson.put("bankCode", bankAcc.bankId.bnrCode);
									bankAccJson.put("bankName", bankAcc.bankId.name);
									bankAccJson.put("countNo", myPurpsoe);
									bankAccJson.put("accountNumber", bankAcc.accountNumber);
									bankAccJson.put("bankAbrrev", bankAcc.bankId.accronym);
									arrayAccs.add(bankAccJson);

									myPurpsoe++;
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
					accSectionJson.put("accounts",arrayAccs);
					
					userJson.put("bankaccount",accSectionJson);


					return ok(userJson);
				} else {
					ObjectNode errorJson = Json.newObject();
					errorJson.put("status", "Institution not found");
					errorJson.put("errorCode", "100");
					return badRequest(errorJson);
				}
			}else{

				ObjectNode userJson = Json.newObject();
				userJson.put("status", "No not authorized");
				return forbidden(userJson);
			
			}
		}
	
		 }
	 
	//Receive payment from the bank by student with id
		 @BodyParser.Of(BodyParser.Json.class)
		 public static Result postSchoolFeesPaidByRegisteredStudent() {
			
			MDC.put("method", "all");
			JsonNode asJson = request().body().asJson();
			logger.trace("Received json: {}", asJson.toString());
			
			if (asJson.isNull()) {
				ObjectNode userJson = Json.newObject();
				userJson.put("status", "Empty logins not allowed");
				
				logger.trace("Responded json: {}", userJson.toString());
				
				return badRequest(userJson);
			}else{
				
				String code = asJson.findPath("code").textValue();
				String service= asJson.findPath("service").textValue();
				String username= asJson.findPath("username").textValue();
				String password= asJson.findPath("password").textValue();
				String operator= asJson.findPath("operator").textValue();
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
				String transactionid= asJson.findPath("transactionid").textValue();
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
				//String academicyear= asJson.findPath("academicyear").textValue();
				//String semister= asJson.findPath("semister").textValue();
				String datetime= asJson.findPath("datetime").textValue();
				//String bankname= asJson.findPath("bankname").textValue();
				String bankaccount= asJson.findPath("bankaccount").textValue();
				String paymentPurpose= asJson.findPath("paymentPurpose").textValue();
				
				//String observation= asJson.findPath("observation").textValue();
				String payername= asJson.findPath("payername").textValue();
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
				if(username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("bk123!")){
					Student myStudent = Student.find.where().eq("reg_number",studentid).findUnique();
					long stdId = 0;
					try {
						stdId = myStudent.id;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (stdId> 0) {
						// check institution 
						Institution myInst=Institution.find.byId(schoolid);
						long instId = 0;
						try {
							 instId=myInst.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(myStudent.instId.id==instId){
							//check bank accounts
							BankAccount myBankAcc=BankAccount.find.where().eq("account_number", bankaccount).findUnique();
							long bankId = 0;
							try {
								bankId = myBankAcc.id;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(bankId>0){
								//check payment purpose
								PaymentPurpose myPurpose=PaymentPurpose.find.where().eq("purpose", paymentPurpose).eq("bank_account_id", myBankAcc.id).findUnique();
								long purposeId = 0;
								try {
									purposeId = myPurpose.id;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(purposeId>0){
									//now save the payment
									PaymentLog myPayment=new PaymentLog();
									int amount;
									try {
										amount = asJson.findPath("amount").intValue();
										if(amount>0){
											myPayment.amountPaid=amount;
										}else{
											ObjectNode userJson = Json.newObject();
											userJson.put("status", "Amount cannot be zero");
											userJson.put("errorCode", "100");
											return badRequest(userJson);
										}
									} catch (Exception e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
										ObjectNode userJson = Json.newObject();
										userJson.put("status", "Amount field is missing");
										userJson.put("errorCode", "100");
										return badRequest(userJson);
									}
									
									myPayment.bank=myBankAcc.bankId;
									myPayment.bankAcc=myBankAcc;
									myPayment.paymentPurpose=myPurpose;
									myPayment.extTrxId=USSDHelperUtils.getDateNowString();
									myPayment.instId=myInst;
									myPayment.bankSlip=slipnumber;
									myPayment.msisdn=payername;
									myPayment.paymentChannel="O2C";
									myPayment.operator=operator;
									myPayment.payerName=payername;
									myPayment.paymentDate=USSDHelperUtils.getDateFromString();
									myPayment.paymentDevice="O2C";
									myPayment.postingDate=USSDHelperUtils.getDateFromStringPost();
									myPayment.processingNumber=transactionid;
									myPayment.statusDesc="posted";
									myPayment.studentId=myStudent;
									myPayment.ussdStatus="success";
									myPayment.isRegistered="yes";
									
								
										InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", myInst.id).eq("status", "active").findUnique();
										long instCaleId = 0;
										try {
											instCaleId = institutionCalender.id;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if(instCaleId>0){
											myPayment.academicYear=institutionCalender;
										}else{
											ObjectNode userJson = Json.newObject();
											userJson.put("status", "Academic year not found");
											return badRequest(userJson);
										}
									
									myPayment.save();
								
									//insert payment log
									ObjectNode userJson = Json.newObject();
									userJson.put("transactionid", myPayment.extTrxId);
									userJson.put("status", "success");
									userJson.put("code", code);
									userJson.put("service", service);
									userJson.put("username", username);
									userJson.put("datetime", USSDHelperUtils.getDateNow());

									//update the tuition fees payment log
									/*TuitionFees tuitFees=TuitionFees.find.where()
											.eq("institution_calender_id", myPayment.studentId.academicYear.id)
											.eq("degree_program_id", myPayment.studentId.degreeProgram.id)
											.eq("status", "active")
											.findUnique();
									long tuitionSetId = 0;
									try {
										tuitionSetId = tuitFees.id;
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									if(tuitionSetId>0){
										StudentPayLog myTutLog=StudentPayLog.find.where()
												.eq("student_id", myPayment.studentId.id)
												.eq("tuition_fees_id", tuitFees.id)
												.findUnique();
										long stdPayLogId = 0;
										try {
											stdPayLogId = myTutLog.id;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if(stdPayLogId>0){
											double allPaid=myPayment.amountPaid+myTutLog.amountPaid;
											myTutLog.amountPaid=allPaid;
											myTutLog.facultId=myPayment.studentId.facultyId;
											myTutLog.instId=myPayment.instId;
											myTutLog.statusDesc="Active";
											myTutLog.tuitionFee=tuitFees;
											myTutLog.update();
											}else{
												myTutLog.amountExpected=tuitFees.amount;
												myTutLog.amountPaid=myPayment.amountPaid;
												myTutLog.amountPrev=0;
												myTutLog.facultId=myPayment.studentId.facultyId;
												myTutLog.instId=myPayment.instId;
												myTutLog.statusDesc="Active";
												myTutLog.tuitionFee=tuitFees;
												myTutLog.save();
											}
										
									}*/
									return ok(userJson);
									
								}else{

									ObjectNode userJson = Json.newObject();
									userJson.put("status", "Payment purpose not found");
									return badRequest(userJson);
								
								}
							}else{
								ObjectNode userJson = Json.newObject();
								userJson.put("status", "Account found");
								return badRequest(userJson);
							}
							
						}else{
							//selected a wrong university

							ObjectNode userJson = Json.newObject();
							userJson.put("status", "Selected wrong university");
							return badRequest(userJson);
						
						}
						
					} else {
						ObjectNode userJson = Json.newObject();
						userJson.put("status", "Student is not found");
						return badRequest(userJson);
					}
				}else{

					ObjectNode userJson = Json.newObject();
					userJson.put("status", "No not authorized");
					return forbidden(userJson);
				
				}
			}
			 }//Receive payment from the bank by un registered student
		 @BodyParser.Of(BodyParser.Json.class)
		 public static Result postSchoolFeesPaidByUnRegisteredStudent() {
			
			MDC.put("method", "all");
			JsonNode asJson = request().body().asJson();
			logger.trace("Received json:", asJson.toString());
			
			if (asJson.isNull()) {
				ObjectNode userJson = Json.newObject();
				userJson.put("status", "Empty logins not allowed");
				
				logger.trace("Responded json: {}", userJson.toString());
				
				return badRequest(userJson);
			}else{
				
				String code = asJson.findPath("code").textValue();
				String service= asJson.findPath("service").textValue();
				String username= asJson.findPath("username").textValue();
				String password= asJson.findPath("password").textValue();
				String operator= asJson.findPath("operator").textValue();
				long schoolid= asJson.findPath("schoolid").longValue();
				String transactionid= asJson.findPath("transactionid").textValue();
				String datetime= asJson.findPath("datetime").textValue();
				String bankaccount= asJson.findPath("bankaccount").textValue();
				String paymentPurpose= asJson.findPath("paymentPurpose").textValue();
				//int amount= asJson.findPath("amount").intValue();

				// do authentication
				if(username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("bk123!")){
					//Student myStudent = new Student();
					

						// check institution 
						Institution myInst=Institution.find.byId(schoolid);
						
						if(myInst.id>0){
							
							//check bank accounts
							BankAccount myBankAcc=BankAccount.find.where().eq("account_number", bankaccount).findUnique();
							long bankAccId = 0;
							try {
								bankAccId = myBankAcc.id;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(bankAccId>0){
								//check payment purpose
								PaymentPurpose myPurpose=PaymentPurpose.find.where().eq("purpose", paymentPurpose).eq("bank_account_id", myBankAcc.id).findUnique();
								long purposeId = 0;
								try {
									purposeId = myPurpose.id;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(purposeId>0){
									
									PaymentLog pymtLong=new PaymentLog();
									
									pymtLong.bankAcc=myBankAcc;
									pymtLong.instId = myInst;
									pymtLong.paymentPurpose = myPurpose;
									pymtLong.bank = myBankAcc.bankId;

									
									//student details

									if(asJson.has("studentNames")) {
										pymtLong.studentNames = asJson.findPath("studentNames").textValue();
										
									}else{
										ObjectNode httpStatus = Json.newObject();
										
										httpStatus.put("Code", "100");
										httpStatus.put("error", "Student names are missing");
										return badRequest(httpStatus);
									}
									if(asJson.has("nationalId")){
										pymtLong.nida = asJson.findPath("nationalId").textValue();
											
									}else{
										ObjectNode httpStatus = Json.newObject();
										httpStatus.put("Code", "101");
										httpStatus.put("error", "Student NIDA/passport is missing");
										return badRequest(httpStatus);
									}
									if(asJson.has("payername")){
										pymtLong.payerName = asJson.findPath("payername").textValue();
										
											
									}else{
										ObjectNode httpStatus = Json.newObject();
										httpStatus.put("Code", "103");
										httpStatus.put("error", "Payer name is missing");
										return badRequest(httpStatus);
									
									}
									
									
									if(asJson.has("studentPhone")){
										if(USSDHelperUtils.isPhoneValid(asJson.findPath("studentPhone").textValue())){
											pymtLong.msisdn = asJson.findPath("studentPhone").textValue();
												
										}else{

											ObjectNode httpStatus = Json.newObject();
											httpStatus.put("Code", "104");
											httpStatus.put("error", "Student phone should be in format 2507xxxxxxxx i.e 12 digits");
											return badRequest(httpStatus);
										
										
										}
											
									}else{
										ObjectNode httpStatus = Json.newObject();
										httpStatus.put("Code", "105");
										httpStatus.put("error", "Student phone is missing");
										return badRequest(httpStatus);
									
									}
									
									pymtLong.isRegistered = "no";
									
									int amount;
									try {
										amount = asJson.findPath("amount").intValue();
										if(amount>0){
											pymtLong.amountPaid=amount;
										}else{
											ObjectNode userJson = Json.newObject();
											userJson.put("status", "Amount cannot be zero");
											userJson.put("errorCode", "102");
											return badRequest(userJson);
										}
									} catch (Exception e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
										ObjectNode userJson = Json.newObject();
										userJson.put("status", "Amount field is missing");
										userJson.put("errorCode", "106");
										return badRequest(userJson);
									}
									pymtLong.operator = operator;
									pymtLong.statusDesc = "posted";
									pymtLong.ussdStatus = "success";
									
									
									pymtLong.extTrxId = USSDHelperUtils.getDateNowString();
									pymtLong.bankSlip = asJson.findPath("bankSlip").textValue();
									pymtLong.paymentChannel = "O2C";
									pymtLong.paymentDevice = "O2C";
									pymtLong.processingNumber = transactionid;
									pymtLong.paymentDate = USSDHelperUtils.getDateFromString();
									pymtLong.postingDate = USSDHelperUtils.getDateFromStringPost();
									pymtLong.paymentYear=HelperManager.getYearToday();
									//try {
										InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", myInst.id).eq("status", "active").findUnique();
										
										long instCaleId = 0;
										try {
											instCaleId = institutionCalender.id;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if(instCaleId>0){
											pymtLong.academicYear=institutionCalender;
										}else{
											ObjectNode userJson = Json.newObject();
											userJson.put("errorCode", "106");
											userJson.put("status", "Academic year not found");
											return badRequest(userJson);
										}
									
									pymtLong.save();
				
									ObjectNode userJson = Json.newObject();
									userJson.put("transactionid", transactionid);
									userJson.put("status", "success");
									userJson.put("code", code);
									userJson.put("service", service);
									userJson.put("username", username);
									userJson.put("datetime", USSDHelperUtils.getDateNow());

									return ok(userJson);
									
								}else{

									ObjectNode userJson = Json.newObject();
									userJson.put("status", "Payment purpose not saved");
									return badRequest(userJson);
								
								}
							}else{
								ObjectNode userJson = Json.newObject();
								userJson.put("status", "Account found");
								return badRequest(userJson);
							}
							
						}else{
							//selected a wrong university

							ObjectNode userJson = Json.newObject();
							userJson.put("status", "Selected wrong university");
							return badRequest(userJson);
						
						}
						
					
				}else{

					ObjectNode userJson = Json.newObject();
					userJson.put("status", "No not authorized");
					return forbidden(userJson);
				
				}
			} 
			
			 }

		 //check all schools available in the system
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
			}else{
				String username= asJson.findPath("username").textValue();
				String password= asJson.findPath("password").textValue();
				
				// do authentication
				if(username.equalsIgnoreCase("bk") && password.equalsIgnoreCase("bk123!")){
				
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
							institutionJson.put("schoolPayCharges", user.studentPayTransactionFees);
							
							//set bank accounts
							ObjectNode accSectionJson = Json.newObject();
							ObjectNode bankAccJson;
							ArrayNode arrayAccs = mapper.createArrayNode();
							int count=0;
							//fetch all accounts for above institution
							try {
								for(BankAccount bankAcc:BankAccount.find.where().eq("institution_id", user.id).findList()){
									
									if(bankAcc.id>0){
										int myPurpsoe=1;
										for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", bankAcc.id).findList()){
											bankAccJson = Json.newObject();
											count++;
											bankAccJson.put("accountPurpose", myPurpose.purpose);
											bankAccJson.put("bankCode", bankAcc.bankId.bnrCode);
											bankAccJson.put("bankName", bankAcc.bankId.name);
											bankAccJson.put("countNo", myPurpsoe);
											bankAccJson.put("accountNumber", bankAcc.accountNumber);
											bankAccJson.put("bankAbrrev", bankAcc.bankId.accronym);
											arrayAccs.add(bankAccJson);
											myPurpsoe++;
										}
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							accSectionJson.put("totalBankAccounts", count);
							accSectionJson.put("accounts",arrayAccs);
							
							institutionJson.put("bankaccount",accSectionJson);
							array.add(institutionJson);
						}

						httpStatus.put("rowCount", all.size());
						httpStatus.put("Code", "200");
						httpStatus.put("status", "Success");
						httpStatus.put("response", array);
						
						return ok(httpStatus);
					}
				}else{
				
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
