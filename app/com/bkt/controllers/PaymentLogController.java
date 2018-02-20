/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.avaje.ebean.Page;
import com.avaje.ebean.annotation.Transactional;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.BankAccount;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Student;
import com.bkt.utils.HelperManager;
import com.bkt.utils.LogRequest;
import com.bkt.utils.PaymentLogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

/**
 * @author pc
 *
 */
@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class PaymentLogController extends Controller {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentLogController.class);
	
	//-------------------------------------------payments from registered students----------------------------------
	
	/*
	 * List all paymentLogs from registered students only
	 * */
	@Transactional
	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		
		List<PaymentLog> all;
		try {
			all = PaymentLog.find.where()
					.eq("is_registered", "yes")
					.eq("status_desc","posted")
					.eq("ussd_status","success")
					.findList();
			if (all.isEmpty()) {
				ObjectNode pymtLongJson = Json.newObject();

				pymtLongJson.put("status", "No data");
				
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request....");
				httpStatus.put("response", pymtLongJson);
				
				return badRequest(httpStatus);
			} else {
				ObjectNode pymtLongJson;
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode array = mapper.createArrayNode();
				
				for (PaymentLog user : all) {
					pymtLongJson = Json.newObject();

					pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
					pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
					pymtLongJson.put("amountPaid", user.amountPaid);
					pymtLongJson.put("extTrxId", user.extTrxId);
					pymtLongJson.put("instId", user.instId.name);
					pymtLongJson.put("bankSlip", user.bankSlip);
					pymtLongJson.put("paymentChannel", user.paymentChannel);
					pymtLongJson.put("paymentDevice", user.paymentDevice);
					pymtLongJson.put("processingNumber", user.processingNumber);
					try {
						if(user.academicYear.id>0){
							pymtLongJson.put("academicYear", user.academicYear.academicYear);
						}else{
							pymtLongJson.put("academicYear", 0);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
					pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
					
					pymtLongJson.put("paymentId", user.id);
					//get student by student id

					ObjectNode studentJson = Json.newObject();
					Student myStudent=Student.find.byId(user.studentId.id);
					Faculty myFaculty;
					try {
						myFaculty = Faculty.find.byId(myStudent.facultyId.id);

						studentJson.put("faculty", myFaculty.name);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					studentJson.put("studentId", myStudent.id);
					studentJson.put("firstName", myStudent.firstName);
					studentJson.put("lastName", myStudent.lastName);
					studentJson.put("regNumber", myStudent.regNumber);
					
					
					try {
						if(myStudent.degreeProgram.id>0){
							studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
						}else{
							studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					pymtLongJson.put("student", studentJson);

					array.add(pymtLongJson);
				}

				httpStatus.put("rowCount", all.size());
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", array);
				
				return ok(httpStatus);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", e.toString());
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		}

	}
	/*
	 * List all paymentLogs from registered students only
	 * */
	@Transactional
	public static Result listAllByInstIdJson(Long instId) {
		ObjectNode httpStatus = Json.newObject();
		
		List<PaymentLog> all;
		try {
			all = PaymentLog.find.where()
					.eq("is_registered", "yes")
					.eq("institution_id", instId)
					.eq("status_desc","posted")
					.eq("ussd_status","success")
					.findList();
			if (all.isEmpty()) {
				ObjectNode pymtLongJson = Json.newObject();

				pymtLongJson.put("status", "No data");
				
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request....");
				httpStatus.put("response", pymtLongJson);
				
				return badRequest(httpStatus);
			} else {
				ObjectNode pymtLongJson;
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode array = mapper.createArrayNode();
				
				for (PaymentLog user : all) {
					pymtLongJson = Json.newObject();

					pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
					pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
					pymtLongJson.put("amountPaid", user.amountPaid);
					pymtLongJson.put("extTrxId", user.extTrxId);
					pymtLongJson.put("instId", user.instId.name);
					pymtLongJson.put("bankSlip", user.bankSlip);
					pymtLongJson.put("paymentChannel", user.paymentChannel);
					pymtLongJson.put("paymentDevice", user.paymentDevice);
					pymtLongJson.put("processingNumber", user.processingNumber);
					try {
						if(user.academicYear.id>0){
							pymtLongJson.put("academicYear", user.academicYear.academicYear);
						}else{
							pymtLongJson.put("academicYear", 0);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
					pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
					
					pymtLongJson.put("paymentId", user.id);
					
					//get student by student id
					ObjectNode studentJson = Json.newObject();
					Student myStudent=Student.find.byId(user.studentId.id);
					Faculty myFaculty;
					try {
						myFaculty = Faculty.find.byId(myStudent.facultyId.id);
						studentJson.put("faculty", myFaculty.name);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					studentJson.put("studentId", myStudent.id);
					studentJson.put("firstName", myStudent.firstName);
					studentJson.put("lastName", myStudent.lastName);
					studentJson.put("regNumber", myStudent.regNumber);
					try {
						if(myStudent.degreeProgram.id>0){
							studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
						}else{
							studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pymtLongJson.put("student", studentJson);

					array.add(pymtLongJson);
				}

				httpStatus.put("rowCount", all.size());
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", array);
				
				return ok(httpStatus);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", e.toString());
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		}

	}
	/*
	 * Search in all paymentLogs from registered students only
	 * */
	public static Result searchAll(String name) {
		MDC.put("method", "searchAll");

		ObjectNode httpStatus = Json.newObject();

		Long bankAccId = Long.parseLong("0");
		try {
			bankAccId = BankAccount.find.where().like("account_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long purposeId = Long.parseLong("0");
		try {
			purposeId = PaymentPurpose.find.where().like("purpose", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		Long instId = Long.parseLong("0");
		try {
			instId = Institution.find.where().like("name", "%" + name + "%").like("accronym", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long stdId = Long.parseLong("0");
		try {
			stdId = Student.find.where()
					.like("first_name", "%" + name + "%")
					.like("last_name", "%" + name + "%")
					.like("reg_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes").disjunction()
				.like("bank_account_id","%"+bankAccId+"%")
				.like("institution_id", "%"+instId+"%")
				.like("payment_purpose_id","%"+purposeId+"%")
				.like("amount_paid", "%" + name + "%")
				.like("ext_trx_id", "%" + name + "%")
				.like("bank_slip", "%" + name + "%")
				.like("payment_channel", "%" + name + "%")
				.like("processing_number", "%" + name + "%")
				.like("student_id", "%" + stdId + "%")
				.order("id desc").findList();
	

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);
			LOG.info("List is returned for all PaymentLog with count:" + 0);

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}

	/*
	 * Search in all paymentLogs from registered students only by institution id
	 * */
	public static Result searchAllByInsist(Long insitId, String name) {
		MDC.put("method", "searchAllByInsist");

		ObjectNode httpStatus = Json.newObject();
		
		/*Long bankAccId = Long.parseLong("0");
		try {
			bankAccId = BankAccount.find.where().like("account_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long purposeId = Long.parseLong("0");
		try {
			purposeId = PaymentPurpose.find.where().like("purpose", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		Long stdId = Long.parseLong("0");
		try {
			stdId = Student.find.where()
					.like("first_name", "%" + name + "%")
					.like("last_name", "%" + name + "%")
					.like("reg_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}*/
		//searchPaymentByInstitutionAndKeyword
		List<PaymentLog> all =PaymentLogUtils.searchPaymentByInstitutionAndKeyword(insitId, name);
		
		/*List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes").eq("institution_id", insitId).disjunction()
				.like("bank_account_id","%"+bankAccId+"%")
				.like("payment_purpose_id","%"+purposeId+"%")
				.like("amount_paid", "%" + name + "%")
				.like("ext_trx_id", "%" + name + "%")
				.like("bank_slip", "%" + name + "%")
				.like("payment_channel", "%" + name + "%")
				.like("processing_number", "%" + name + "%")
				.like("student_id", "%" + stdId + "%")
				.order("id desc").findList();*/
		

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("isRegistered", user.isRegistered);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}
	/*
	 * Search in all paymentLogs from a registered student by institution id and regNumber
	 * */
	public static Result searchAllByInstIdYearAndRegNumber(Long insitId, String name,String year) {
		MDC.put("method", "searchAllByInstIdYearAndRegNumber");

		ObjectNode httpStatus = Json.newObject();
		
		Long stdId = Long.parseLong("0");
		try {
			stdId = Student.find.where()
					.like("first_name", "%" + name + "%")
					.like("last_name", "%" + name + "%")
					.like("reg_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long academicYear = Long.parseLong("0");
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year).eq("institution_id", insitId).findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes")
				.eq("institution_id", insitId)
				.eq("student_id", stdId)
				.eq("institution_calender_id", academicYear)
				.order("id desc").findList();
		
		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				pymtLongJson.put("paymentYear", user.paymentYear);
				
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}
	/*
	 * Display all paymentLogs from registered 
	 * students only by institution id
	 *
	 * */
	public static Result allByPaging(Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		
		// Paging starts

		Page<PaymentLog> pagedList = PaymentLog
				.find.where()
				.eq("is_registered","yes")
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber",user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				pymtLongJson.put("paymentId", user.id);

				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("Code", "200");
			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	/*
	 * Retrieve total sum and count of all payments done
	 * per channel,instId and payment year for either registered or none registered students
	 *
	 * */
	public static Result paymentStatByYear(Long instId,String year) {
		ObjectNode httpStatus = Json.newObject();
		
		long academicYear = 0;
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year.trim()).eq("institution_id", instId).findUnique().id;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("Adacemic year Id..."+academicYear);
		long totalMomoForRegistered = PaymentLogUtils.sumOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "MTN mobile money channel", "yes");
		long totalMomoForUnRegistered = PaymentLogUtils.sumOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "MTN mobile money channel", "no");
		
		long totalBankForRegistered = PaymentLogUtils.sumOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "O2C", "yes");
		long totalbankForUnRegistered = PaymentLogUtils.sumOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "O2C", "no");
		
		if ((totalMomoForRegistered+totalMomoForUnRegistered)>0) {
			long totMomoCountRegistered = PaymentLogUtils.totalCountOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "MTN mobile money channel", "yes");
			long totMomoCountNotRegistered = PaymentLogUtils.totalCountOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "MTN mobile money channel", "no");
			
			httpStatus.put("momoTotal", totalMomoForRegistered+totalMomoForUnRegistered);
			httpStatus.put("momoCount", totMomoCountRegistered+totMomoCountNotRegistered);
		} else {
			httpStatus.put("momoTotal", 0);
			httpStatus.put("momoCount", 0);
		}
		if ((totalBankForRegistered+totalbankForUnRegistered)>0) {
			long totBankCount = PaymentLogUtils.totalCountOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "O2C", "yes");
			long totBankCountUnre = PaymentLogUtils.totalCountOfPaymentByChannelPerInstIdAndYear(instId, academicYear, "O2C", "no");
			
			httpStatus.put("bankTotal", totalBankForRegistered+totalbankForUnRegistered);
			httpStatus.put("bankCount", totBankCount+totBankCountUnre);
		} else {
			httpStatus.put("bankTotal", 0);
			httpStatus.put("bankCount", 0);
		}

		httpStatus.put("Code", "200");
		return ok(httpStatus);
	}public static Result paymentStatGroupByStudentPerYear(Long instId,String year) {
		long academicYear = 0;
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year.trim()).eq("institution_id", instId).findUnique().id;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("Adacemic year Id..."+academicYear);
		JsonNode totStdPayment = PaymentLogUtils.sumOfPaymentByStudentPerInstIdAndYear(instId, academicYear, "yes");
		

		return ok(totStdPayment);
	}
	public static Result paymentStatGroupByPurposePerYear(Long instId,String year) {
		long academicYear = 0;
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year.trim()).eq("institution_id", instId).findUnique().id;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("Adacemic year Id..."+academicYear);
		JsonNode totStdPayment = PaymentLogUtils.sumOfPaymentByPurposePerInstIdAndYear(instId, academicYear);
		

		return ok(totStdPayment);
	}
	
	/*
	 * Display all paymentLogs from registered 
	 * students only by institution id
	 *
	 * */
	public static Result allByInstIdPaging(Long instId, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes")
				.eq("institution_id", instId).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	/*
	 * Display all paymentLogs from registered 
	 * students only by institution id and payment year with paging
	 *
	 * */
	public static Result allByInstIdAndYearPaging(Long instId,String year, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		Long academicYear = Long.parseLong("0");
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year).eq("institution_id", instId).findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes")
				.eq("institution_calender_id", academicYear)
				.eq("institution_id", instId).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	
	/*
	 * Display all paymentLogs from registered 
	 * students only by institution id and payment year and faculty id with paging
	 *
	 * */
	public static Result allByInstIdAndYearAndFacultyIdPaging(Long instId,String year,Long facultyId, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		Long academicYear = Long.parseLong("0");
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year).eq("institution_id", instId).findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes")
				.eq("institution_calender_id", academicYear)
				.eq("faculty_id", facultyId).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	/*
	 * Display all paymentLogs from registered 
	 * students only by institution id and payment year and registration number with paging
	 *
	 * */
	public static Result allByInstIdAndYearAndRegNumberPaging(Long instId,String year,String regNumber, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		Long academicYear = Long.parseLong("0");
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year).eq("institution_id", instId).findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes")
				.eq("institution_calender_id", academicYear)
				.eq("student_id", Student.find.where().eq("reg_number", regNumber).findUnique().id).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				Student myStudent=Student.find.byId(user.studentId.id);
				Faculty myFaculty;
				try {
					myFaculty = Faculty.find.byId(myStudent.facultyId.id);
					studentJson.put("faculty", myFaculty.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				studentJson.put("firstName", myStudent.firstName);
				studentJson.put("lastName", myStudent.lastName);
				studentJson.put("regNumber", myStudent.regNumber);
				studentJson.put("studentId", myStudent.id);
				try {
					if(myStudent.degreeProgram.id>0){
						studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
					}else{
						studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	/*
	 * PaymentLogs from registered 
	 * students only by payment id
	 *
	 * */
	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		
		PaymentLog user = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "yes").eq("id", id).findUnique();
		if (user.id > 0) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
			pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
			pymtLongJson.put("amountPaid", user.amountPaid);
			pymtLongJson.put("extTrxId", user.extTrxId);
			pymtLongJson.put("instId", user.instId.id);
			pymtLongJson.put("bankSlip", user.bankSlip);
			pymtLongJson.put("paymentChannel", user.paymentChannel);
			pymtLongJson.put("paymentDevice", user.paymentDevice);
			pymtLongJson.put("processingNumber", user.processingNumber);
			try {
				if(user.academicYear.id>0){
					pymtLongJson.put("academicYear", user.academicYear.academicYear);
				}else{
					pymtLongJson.put("academicYear", 0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
			pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
			
			pymtLongJson.put("paymentId", user.id);
			
			//get student by student id
			ObjectNode studentJson = Json.newObject();
			Student myStudent=Student.find.byId(user.studentId.id);
			Faculty myFaculty;
			try {
				myFaculty = Faculty.find.byId(myStudent.facultyId.id);
				studentJson.put("faculty", myFaculty.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			studentJson.put("firstName", myStudent.firstName);
			studentJson.put("lastName", myStudent.lastName);
			studentJson.put("regNumber", myStudent.regNumber);
			studentJson.put("studentId", myStudent.id);
			try {
				if(myStudent.degreeProgram.id>0){
					studentJson.put("degreeProgram", myStudent.degreeProgram.degreeName);
				}else{
					studentJson.put("noneDegreeProgram", myStudent.noneDegreeProgram.degreeName);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pymtLongJson.put("student", studentJson);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", pymtLongJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createPaymentRegisteredStudentJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode pymtLongJson = Json.newObject();
			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			//Form<PaymentLog> contactForm = Form.form(PaymentLog.class).bind(asJson);
				
				ObjectNode pymtLongJson = Json.newObject();
				PaymentLog pymtLong = new PaymentLog();
				
				String isRegistered = "no";
				try {
					 isRegistered = asJson.findPath("isRegistered").textValue();
					 if(isRegistered.equalsIgnoreCase("yes")){
							
							Student studentById = Student.find.byId(asJson.findPath("studentId").longValue());
							Institution instId = Institution.find.byId(asJson.findPath("instId").longValue());
							if(studentById.instId.id==instId.id){
								BankAccount bankAcc = null;
								try {
									bankAcc = BankAccount.find.where().eq("account_number", asJson.findPath("accountNumber").textValue()).findUnique();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									//institution bank account does not exist
									httpStatus.put("Code", "405");
									httpStatus.put("error", "institution bank account does not exist");
									return badRequest(httpStatus);
								
								}
								PaymentPurpose purpose = null;
								try {
									purpose = PaymentPurpose.find.where().eq("purpose", asJson.findPath("purpose").textValue()).eq("institution_id", instId.id).eq("bank_account_id", bankAcc.id).findUnique();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									//institution bank account does not exist
									httpStatus.put("Code", "405");
									httpStatus.put("error", "institution payment purpose does not exist");
									return badRequest(httpStatus);
								
								}
								
								if(bankAcc.instId.id==instId.id){
									pymtLong.bankAcc=bankAcc;
									pymtLong.instId = instId;
									pymtLong.paymentPurpose = purpose;
									pymtLong.bank = bankAcc.bankId;
									
									//student details

									pymtLong.studentId=studentById;
									pymtLong.isRegistered = asJson.findPath("isRegistered").textValue();
									
									pymtLong.amountPaid = asJson.findPath("amountPaid").doubleValue();
									
									pymtLong.operator = asJson.findPath("operator").textValue();
									pymtLong.statusDesc = "posted";
									pymtLong.ussdStatus = "success";
									pymtLong.payerName = asJson.findPath("payerName").textValue();
									pymtLong.extTrxId = asJson.findPath("extTrxId").textValue();
									pymtLong.bankSlip = asJson.findPath("bankSlip").textValue();
									pymtLong.paymentChannel = asJson.findPath("paymentChannel").textValue();
									pymtLong.paymentDevice = asJson.findPath("paymentDevice").textValue();
									pymtLong.processingNumber = asJson.findPath("processingNumber").textValue();
									pymtLong.academicYear=studentById.academicYear;
									pymtLong.paymentDate = HelperManager.getDateToday();
									pymtLong.postingDate = HelperManager.getDateToday();
									pymtLong.paymentYear=HelperManager.getYearToday();
									
									pymtLong.save();

									pymtLongJson.put("status", "saved");
									httpStatus.put("Code", "200");
									httpStatus.put("status", "Success");
									httpStatus.put("response", pymtLongJson);
									
									return ok(httpStatus);
								}else{
									//institution bank account does not exist
									httpStatus.put("Code", "405");
									httpStatus.put("error", "institution bank account does not exist");
									return badRequest(httpStatus);
								}
								
								
							}else{
								//student institution does not match with institution entered
								httpStatus.put("Code", "405");
								httpStatus.put("error", "student institution does not match with institution entered");
								return badRequest(httpStatus);
							}
						
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("error", "student institution does not match with institution entered");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					// TODO Auto-generated catch block
					
					pymtLongJson.put("status", e1.getMessage());
					httpStatus.put("Code", "401");
					httpStatus.put("error", "Bad request");
					
					return badRequest(httpStatus);
				
				}
				
		}

	}
	

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePaymentLongJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode pymtLongJson = Json.newObject();
			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			Form<PaymentLog> contactForm = Form.form(PaymentLog.class).bind(asJson);
			
				ObjectNode pymtLongJson = Json.newObject();

				PaymentLog pymtLong = PaymentLog.find.byId(asJson.findPath("paymentId").longValue());

				pymtLong.bankAcc.accountNumber = asJson.findPath("accountNumber").textValue();
				pymtLong.amountPaid = asJson.findPath("amountPaid").doubleValue();
				pymtLong.extTrxId = asJson.findPath("extTrxId").textValue();
				pymtLong.instId = Institution.find.byId(asJson.findPath("instId").longValue());
				pymtLong.studentId = Student.find.byId(asJson.findPath("studentId").longValue());
				pymtLong.paymentChannel = asJson.findPath("paymentChannel").textValue();
				pymtLong.paymentDevice = asJson.findPath("paymentDevice").textValue();
				pymtLong.processingNumber = asJson.findPath("processingNumber").textValue();
				pymtLong.postingDate = HelperManager.getDateToday();
				pymtLong.paymentYear=HelperManager.getYearToday();
				pymtLong.statusDesc = "posted";
				pymtLong.ussdStatus = "success";
				// save the user account
				try {
					pymtLong.update();

					pymtLongJson.put("status", "updated");
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", pymtLongJson);
					
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					pymtLongJson.put("status", e.getMessage());
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Bad request");
					httpStatus.put("response", contactForm.errorsAsJson());
					
					return badRequest(httpStatus);
				}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deletePaymentLongJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode pymtLongJson = Json.newObject();
			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode pymtLongJson = Json.newObject();

			PaymentLog pymtLong = PaymentLog.find.byId(asJson.findPath("paymentId").longValue());
			// save the user account
			try {
				pymtLong.delete();

				pymtLongJson.put("status", "updated");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				pymtLongJson.put("status", e.getMessage());
			}
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", pymtLongJson);
			
			return ok(httpStatus);
		}

	}
//-------------------------------------------------------------un registered student payment APIs-------------------------------
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result createPaymentUnRegisteredStudentJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode pymtLongJson = Json.newObject();
			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			//Form<PaymentLog> contactForm = Form.form(PaymentLog.class).bind(asJson);
				
				ObjectNode pymtLongJson = Json.newObject();
				PaymentLog pymtLong = new PaymentLog();

				if(asJson.findPath("isRegistered").textValue().equalsIgnoreCase("no")){
					
						BankAccount bankAcc = BankAccount.find.where().eq("account_number", asJson.findPath("accountNumber").textValue()).findUnique();
						Institution instId = Institution.find.byId(asJson.findPath("instId").longValue());
						
						PaymentPurpose purpose=PaymentPurpose.find.where().eq("purpose", asJson.findPath("purpose").textValue()).eq("institution_id", instId.id).eq("bank_account_id", bankAcc.id).findUnique();
						
						pymtLong.bankAcc=bankAcc;
						pymtLong.instId = instId;
						pymtLong.paymentPurpose = purpose;
						pymtLong.bank = bankAcc.bankId;
						
						//student details

						if(asJson.has("studentNames")){
							pymtLong.studentNames = asJson.findPath("studentNames").textValue();
								
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "Student names is missing");
							return badRequest(httpStatus);
						}
						if(asJson.has("nida")){
							pymtLong.nida = asJson.findPath("nida").textValue();
								
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "Student NIDA/passport is missing");
							return badRequest(httpStatus);
						}
						if(asJson.has("msisdn")){
							pymtLong.msisdn = asJson.findPath("msisdn").textValue();
							
								
						}else{

							httpStatus.put("Code", "401");
							httpStatus.put("error", "Student phone number is missing");
							return badRequest(httpStatus);
						
						}
						
						
						pymtLong.isRegistered = asJson.findPath("isRegistered").textValue();
						
						pymtLong.amountPaid = asJson.findPath("amountPaid").doubleValue();
						
						pymtLong.operator = asJson.findPath("operator").textValue();
						pymtLong.statusDesc = "posted";
						pymtLong.ussdStatus = "success";
						
						pymtLong.payerName = asJson.findPath("payerName").textValue();
						pymtLong.extTrxId = asJson.findPath("extTrxId").textValue();
						pymtLong.bankSlip = asJson.findPath("bankSlip").textValue();
						pymtLong.paymentChannel = asJson.findPath("paymentChannel").textValue();
						pymtLong.paymentDevice = asJson.findPath("paymentDevice").textValue();
						pymtLong.processingNumber = asJson.findPath("processingNumber").textValue();
						pymtLong.paymentDate = HelperManager.getDateToday();
						pymtLong.postingDate = HelperManager.getDateToday();
						pymtLong.paymentYear=HelperManager.getYearToday();
						
							InstitutionCalender institutionCalender = InstitutionCalender.find.where().eq("institution_id", instId.id).eq("status", "active").findUnique();
							pymtLong.academicYear=institutionCalender;
						
						pymtLong.save();

						pymtLongJson.put("status", "saved");
						httpStatus.put("Code", "200");
						httpStatus.put("status", "Success");
						httpStatus.put("response", pymtLongJson);
						
						return ok(httpStatus);
					
					
					
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("status", "selected wrong student status");
					
					return badRequest(httpStatus);
				}
				
		}

	}
	
	/*
	 * Display all paymentLogs from none registered 
	 * students only by institution id and payment year with paging
	 *
	 * */
	public static Result allNoneRegisteredByInstIdAndYearPaging(Long instId,String year, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		Long academicYear = Long.parseLong("0");
		try {
			academicYear = InstitutionCalender.find.where().eq("academic_year", year).eq("institution_id", instId).findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "no").eq("institution_calender_id", academicYear).eq("institution_id", instId).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	
	public static Result allUnRegisteredByInstIdPaging(Long instId, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "no").eq("institution_id", instId).order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}
	public static Result searchAllUnRegisteredByInsist(Long insitId, String name) {
		MDC.put("method", "searchAllByInsist");

		ObjectNode httpStatus = Json.newObject();
		Long bankAccId = Long.parseLong("0");
		try {
			bankAccId = BankAccount.find.where().like("account_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long purposeId = Long.parseLong("0");
		try {
			purposeId = PaymentPurpose.find.where().like("purpose", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "no").eq("institution_id", insitId).disjunction()
				.like("bank_account_id","%"+bankAccId+"%")
				.like("payment_purpose_id","%"+purposeId+"%")
				.like("amount_paid", "%" + name + "%")
				.like("ext_trx_id", "%" + name + "%")
				.like("bank_slip", "%" + name + "%")
				.like("payment_channel", "%" + name + "%")
				.like("processing_number", "%" + name + "%")
				.like("student_names", "%" + name + "%")
				.like("nida", "%" + name + "%")
				.like("msisdn", "%" + name + "%").order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}
	public static Result searchAllUnRegisteredByInsistId(Long insitId) {
		MDC.put("method", "searchAllByInsist");

		ObjectNode httpStatus = Json.newObject();
		
		List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "no")
				.eq("institution_id", insitId)
				.order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}
	/*
	 * List all paymentLogs from unregistered students only
	 * */
	public static Result listAllFromUnregisteredStudents() {
		ObjectNode httpStatus = Json.newObject();
		
		List<PaymentLog> all;
		try {
			all = PaymentLog.find.where()
					.eq("status_desc","posted")
					.eq("ussd_status","success")
					.eq("is_registered", "no").findList();
			if (all.isEmpty()) {
				ObjectNode pymtLongJson = Json.newObject();

				pymtLongJson.put("status", "No data");
				
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request....");
				httpStatus.put("response", pymtLongJson);
				
				return badRequest(httpStatus);
			} else {
				ObjectNode pymtLongJson;
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode array = mapper.createArrayNode();
				
				for (PaymentLog user : all) {
					pymtLongJson = Json.newObject();

					pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
					pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
					pymtLongJson.put("amountPaid", user.amountPaid);
					pymtLongJson.put("extTrxId", user.extTrxId);
					pymtLongJson.put("instId", user.instId.name);
					pymtLongJson.put("bankSlip", user.bankSlip);
					pymtLongJson.put("paymentChannel", user.paymentChannel);
					pymtLongJson.put("paymentDevice", user.paymentDevice);
					pymtLongJson.put("processingNumber", user.processingNumber);
					pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
					pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

					try {
						if(user.academicYear.id>0){
							pymtLongJson.put("academicYear", user.academicYear.academicYear);
						}else{
							pymtLongJson.put("academicYear", 0);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pymtLongJson.put("paymentId", user.id);
					
					//get student by student id
					ObjectNode studentJson = Json.newObject();
					studentJson.put("studentNames", user.studentNames);
					studentJson.put("nida", user.nida);
					studentJson.put("msisdn", user.msisdn);
					studentJson.put("isRegistered", user.isRegistered);
					pymtLongJson.put("student", studentJson);

					array.add(pymtLongJson);
				}

				httpStatus.put("rowCount", all.size());
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", array);
				
				return ok(httpStatus);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", e.toString());
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		}

	}/*
	 * Search in all paymentLogs from un registered students only
	 * */
	public static Result searchAllFromUnRegistered(String name) {
		MDC.put("method", "searchAll");

		ObjectNode httpStatus = Json.newObject();

		Long bankAccId = Long.parseLong("0");
		try {
			bankAccId = BankAccount.find.where().like("account_number", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Long purposeId = Long.parseLong("0");
		try {
			purposeId = PaymentPurpose.find.where().like("purpose", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		Long instId = Long.parseLong("0");
		try {
			instId = Institution.find.where().like("name", "%" + name + "%").like("accronym", "%" + name + "%").findUnique().id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		List<PaymentLog> all = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered", "no").disjunction()
				.like("bank_account_id","%"+bankAccId+"%")
				.like("institution_id", "%"+instId+"%")
				.like("payment_purpose_id","%"+purposeId+"%")
				.like("amount_paid", "%" + name + "%")
				.like("ext_trx_id", "%" + name + "%")
				.like("bank_slip", "%" + name + "%")
				.like("payment_channel", "%" + name + "%")
				.like("processing_number", "%" + name + "%")
				.like("student_names", "%" + name + "%")
				.like("nida", "%" + name + "%")
				.like("msisdn", "%" + name + "%")
				.order("id desc").findList();
	

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);
			LOG.info("List is returned for all PaymentLog with count:" + 0);

			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : all) {
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentId", user.id);
				
				//get student by student id
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);

				array.add(pymtLongJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all PaymentLog with count:" + all.size());

			return ok(httpStatus);
		}

	}
	/*
	 * Display all paymentLogs from unregistered 
	 * students only by institution id
	 *
	 * */
	public static Result allFromUnRegisteredByPaging(Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		
		// Paging starts
		Page<PaymentLog> pagedList = PaymentLog.find.where()
				.eq("status_desc","posted")
				.eq("ussd_status","success")
				.eq("is_registered","no").order("id desc").findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<PaymentLog> allPayents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allPayents.isEmpty()) {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("rowCount", 0);
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode pymtLongJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentLog user : allPayents) {
				
				pymtLongJson = Json.newObject();

				pymtLongJson.put("accountNumber",user.bankAcc.accountNumber);
				pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
				pymtLongJson.put("amountPaid", user.amountPaid);
				pymtLongJson.put("extTrxId", user.extTrxId);
				pymtLongJson.put("instId", user.instId.id);
				pymtLongJson.put("bankSlip", user.bankSlip);
				pymtLongJson.put("paymentChannel", user.paymentChannel);
				pymtLongJson.put("paymentDevice", user.paymentDevice);
				pymtLongJson.put("processingNumber", user.processingNumber);

				try {
					if(user.academicYear.id>0){
						pymtLongJson.put("academicYear", user.academicYear.academicYear);
					}else{
						pymtLongJson.put("academicYear", 0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
				pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
				pymtLongJson.put("paymentId", user.id);

				//get student by student id
				ObjectNode studentJson = Json.newObject();
				studentJson.put("studentNames", user.studentNames);
				studentJson.put("nida", user.nida);
				studentJson.put("msisdn", user.msisdn);
				studentJson.put("isRegistered", user.isRegistered);
				pymtLongJson.put("student", studentJson);
				
				array.add(pymtLongJson);
			}

			httpStatus.put("Code", "200");
			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}/*
	 * PaymentLogs from unregistered 
	 * students only by payment id
	 *
	 * */
	public static Result paymentFromUnregisteredById(Long id) {
		ObjectNode httpStatus = Json.newObject();
		
		PaymentLog user = null;
		try {
			user = PaymentLog.find.where()
					.eq("status_desc","posted")
					.eq("ussd_status","success")
					.eq("is_registered", "no").eq("id", id).findUnique();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (user!=null) {
			
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("accountNumber", user.bankAcc.accountNumber);
			pymtLongJson.put("paymentPurpose", user.paymentPurpose.purpose);
			pymtLongJson.put("amountPaid", user.amountPaid);
			pymtLongJson.put("extTrxId", user.extTrxId);
			pymtLongJson.put("instId", user.instId.id);
			pymtLongJson.put("bankSlip", user.bankSlip);
			pymtLongJson.put("paymentChannel", user.paymentChannel);
			pymtLongJson.put("paymentDevice", user.paymentDevice);
			pymtLongJson.put("processingNumber", user.processingNumber);
			pymtLongJson.put("paymentDate", HelperManager.converDateFormat(user.paymentDate));
			pymtLongJson.put("postingDate", HelperManager.converDateFormat(user.postingDate));
			try {
				if(user.academicYear.id>0){
					pymtLongJson.put("academicYear", user.academicYear.academicYear);
				}else{
					pymtLongJson.put("academicYear", 0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pymtLongJson.put("paymentId", user.id);
			
			//get student by student id
			ObjectNode studentJson = Json.newObject();
			studentJson.put("studentNames", user.studentNames);
			studentJson.put("nida", user.nida);
			studentJson.put("msisdn", user.msisdn);
			studentJson.put("isRegistered", user.isRegistered);
			pymtLongJson.put("student", studentJson);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", pymtLongJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode pymtLongJson = Json.newObject();

			pymtLongJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", pymtLongJson);
			
			return badRequest(httpStatus);
		}

	}
}
