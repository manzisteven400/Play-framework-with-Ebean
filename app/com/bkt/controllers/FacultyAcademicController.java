package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.AcademicProgram;
import com.bkt.models.Faculty;
import com.bkt.models.FacultyAcademic;
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
public class FacultyAcademicController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<FacultyAcademic> all = FacultyAcademic.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (FacultyAcademic user : all) {
				userJson = Json.newObject();
				
				userJson.put("facultId",user.facultId.id);
				userJson.put("academicProgram",user.academicProgram.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result listByFacultyId(Long facultyId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<FacultyAcademic> all = FacultyAcademic.find.where().eq("faculty_id", facultyId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (FacultyAcademic user : all) {
				userJson = Json.newObject();
				
				userJson.put("facultId",user.facultId.id);
				userJson.put("academicProgram",user.academicProgram.id);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result listAcademicProgramId(Long academicProgramId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<FacultyAcademic> all = FacultyAcademic.find.where().eq("academic_program_id", academicProgramId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (FacultyAcademic user : all) {
				userJson = Json.newObject();
				
				userJson.put("facultId",user.facultId.id);
				userJson.put("academicProgram",user.academicProgram.id);
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
		Page<FacultyAcademic> pagedList = FacultyAcademic.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<FacultyAcademic> allAccounts = pagedList.getList();

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
			
			for (FacultyAcademic user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("facultId",user.facultId.id);
				userJson.put("academicProgram",user.academicProgram.id);
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

		FacultyAcademic user = FacultyAcademic.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("facultId",user.facultId.id);
			userJson.put("academicProgram",user.academicProgram.id);
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

			Form<FacultyAcademic> contactForm = Form.form(FacultyAcademic.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();
				FacultyAcademic bank = new FacultyAcademic();
				
				bank.facultId = Faculty.find.byId(asJson.findPath("facultId").longValue());
				bank.academicProgram = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());

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
			Form<FacultyAcademic> contactForm = Form.form(FacultyAcademic.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();

				FacultyAcademic bank = FacultyAcademic.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				
				bank.facultId = Faculty.find.byId(asJson.findPath("facultId").longValue());
				bank.academicProgram = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());

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

			FacultyAcademic bank = FacultyAcademic.find.byId(asJson.findPath("id").longValue());
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
