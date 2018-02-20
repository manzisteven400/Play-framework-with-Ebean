package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.Student;
import com.bkt.models.StudentPayLog;
import com.bkt.models.TuitionFees;
import com.bkt.utils.LogRequest;
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


@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class StudentPayLogController extends Controller {
	
	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<StudentPayLog> all = StudentPayLog.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (StudentPayLog user : all) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result listByInstId(Long instId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<StudentPayLog> all = StudentPayLog.find.where().eq("institution_id", instId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (StudentPayLog user : all) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByStudentId(Long stdId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<StudentPayLog> all = StudentPayLog.find.where().eq("student_id", stdId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (StudentPayLog user : all) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}

	public static Result allByPaging(Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		// Paging starts
		Page<StudentPayLog> pagedList = StudentPayLog.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<StudentPayLog> allAccounts = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allAccounts.size() < 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {

			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			
			for (StudentPayLog user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result allByInstIdPaging(Long instId,Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		// Paging starts
		Page<StudentPayLog> pagedList = StudentPayLog.find.where().eq("institution_id", instId).findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<StudentPayLog> allAccounts = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allAccounts.size() < 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {

			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			
			for (StudentPayLog user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result allByStudentIdPaging(Long stdId,Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		// Paging starts
		Page<StudentPayLog> pagedList = StudentPayLog.find.where().eq("student_id", stdId).findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<StudentPayLog> allAccounts = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allAccounts.size() < 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {

			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			
			for (StudentPayLog user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("amountExpected",user.amountExpected);
				userJson.put("amountPaid",user.amountPaid);
				userJson.put("amountPrev",user.amountPrev);
				userJson.put("statusDesc",user.statusDesc);
				userJson.put("facultId",user.facultId.id);
				userJson.put("instId",user.instId.id);
				userJson.put("studentId",user.studentId.id);
				userJson.put("tuitionFee",user.tuitionFee.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		StudentPayLog user = StudentPayLog.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("amountExpected",user.amountExpected);
			userJson.put("amountPaid",user.amountPaid);
			userJson.put("amountPrev",user.amountPrev);
			userJson.put("statusDesc",user.statusDesc);
			userJson.put("facultId",user.facultId.id);
			userJson.put("instId",user.instId.id);
			userJson.put("studentId",user.studentId.id);
			userJson.put("tuitionFee",user.tuitionFee.id);
			userJson.put("id",user.id);
			
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		}

	}public static Result showByStudentAndTuition(Long studentId,Long tuitionFeeId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		StudentPayLog user = StudentPayLog.find.where().eq("student_id", studentId).eq("tuition_fees_id", tuitionFeeId).findUnique();
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("amountExpected",user.amountExpected);
			userJson.put("amountPaid",user.amountPaid);
			userJson.put("amountPrev",user.amountPrev);
			userJson.put("statusDesc",user.statusDesc);
			userJson.put("facultId",user.facultId.id);
			userJson.put("instId",user.instId.id);
			userJson.put("studentId",user.studentId.id);
			userJson.put("tuitionFee",user.tuitionFee.id);
			userJson.put("id",user.id);
			
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createRecord() {
		ObjectNode httpStatus = Json.newObject();

		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			httpStatus.put("Code", "200");
			httpStatus.put("status", "success");

			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {

			Form<StudentPayLog> contactForm = Form.form(StudentPayLog.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();
				StudentPayLog bank = new StudentPayLog();
				
				bank.amountExpected =asJson.findPath("amount").doubleValue();
				bank.amountPaid =asJson.findPath("amount").doubleValue();
				bank.amountPrev =asJson.findPath("amount").doubleValue();
				bank.statusDesc =asJson.findPath("statusDesc").textValue();
				bank.tuitionFee =TuitionFees.find.byId(asJson.findPath("tuitionFee").longValue());
				bank.studentId =Student.find.byId(asJson.findPath("studentId").longValue());
				bank.instId =Institution.find.byId(asJson.findPath("instId").longValue());
				bank.facultId =Faculty.find.byId(asJson.findPath("facultId").longValue());
				
				// save the user account
				try {
					bank.save();

					userJson.put("status", "saved");
					httpStatus.put("status", "success");
					httpStatus.put("Code", "200");

					httpStatus.put("response", userJson);
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					httpStatus.put("Code", "401");
					httpStatus.put("status", "bad request");

					httpStatus.put("response", contactForm.errorsAsJson());

					return badRequest(httpStatus);

				
				}
				
			
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateRecord() {
		ObjectNode httpStatus = Json.newObject();

		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			httpStatus.put("Code", "200");

			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			Form<StudentPayLog> contactForm = Form.form(StudentPayLog.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();

				StudentPayLog bank = StudentPayLog.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();

				bank.amountExpected =asJson.findPath("amount").doubleValue();
				bank.amountPaid =asJson.findPath("amount").doubleValue();
				bank.amountPrev =asJson.findPath("amount").doubleValue();
				bank.statusDesc =asJson.findPath("statusDesc").textValue();
				bank.tuitionFee =TuitionFees.find.byId(asJson.findPath("tuitionFee").longValue());
				bank.studentId =Student.find.byId(asJson.findPath("studentId").longValue());
				bank.instId =Institution.find.byId(asJson.findPath("instId").longValue());
				bank.facultId =Faculty.find.byId(asJson.findPath("facultId").longValue());
				// save the user account
				try {
					bank.update();

					userJson.put("status", "updated");
					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
					httpStatus.put("response", userJson);
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					httpStatus.put("Code", "401");
					httpStatus.put("status", "Bad request");

					httpStatus.put("response", contactForm.errorsAsJson());

					return badRequest(httpStatus);
				}
				
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteRecord() {
		JsonNode asJson = request().body().asJson();
		ObjectNode httpStatus = Json.newObject();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Not deleted");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "success");
			httpStatus.put("response", userJson);

			return ok(httpStatus);

		} else {
			ObjectNode userJson = Json.newObject();

			StudentPayLog bank = StudentPayLog.find.byId(asJson.findPath("id").longValue());
			// save the user account
			try {
				bank.delete();

				userJson.put("status", "updated");

				httpStatus.put("Code", "200");
				httpStatus.put("status", "success");
				httpStatus.put("response", userJson);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				userJson.put("status", e.getMessage());

				httpStatus.put("Code", "500");
				httpStatus.put("status", "Internal Error");
				httpStatus.put("response", userJson);
			}
			return ok(httpStatus);
		}

	}
}
