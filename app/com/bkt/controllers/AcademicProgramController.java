package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.AcademicProgram;
import com.bkt.models.Student;
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
public class AcademicProgramController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<AcademicProgram> all = AcademicProgram.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (AcademicProgram user : all) {
				userJson = Json.newObject();
				
				userJson.put("id", user.id);
				userJson.put("accronym", user.accronym);
				userJson.put("code", user.code);
				userJson.put("status", user.status);
				userJson.put("name", user.name);
				
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
		Page<AcademicProgram> pagedList = AcademicProgram.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<AcademicProgram> allAccounts = pagedList.getList();

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
			
			for (AcademicProgram user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("id", user.id);
				userJson.put("accronym", user.accronym);
				userJson.put("code", user.code);
				userJson.put("status", user.status);
				userJson.put("name", user.name);
				
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

		AcademicProgram user = AcademicProgram.find.byId(id);
		
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("id", user.id);
			userJson.put("accronym", user.accronym);
			userJson.put("code", user.code);
			userJson.put("status", user.status);
			userJson.put("name", user.name);

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
	public static Result createAcademicProgramJson() {
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

			//Form<AcademicProgram> contactForm = Form.form(AcademicProgram.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();
				AcademicProgram bank = new AcademicProgram();
				
				bank.accronym = asJson.findPath("accronym").textValue();
				bank.name = asJson.findPath("name").textValue();
				try {
					if(asJson.has("status")){
						if(asJson.findPath("status").textValue().equalsIgnoreCase("Active") || asJson.findPath("status").textValue().equalsIgnoreCase("Pending")){
							bank.status =asJson.findPath("status").textValue();
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "Failed");

							httpStatus.put("error", "status value must be ACTIVE or DISABLED");

							return badRequest(httpStatus);
						}
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("status", "Failed");

						httpStatus.put("error", "status value must be ACTIVE or DISABLED");

						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Failed");

					httpStatus.put("error", "status value must be ACTIVE or DISABLED");

					return badRequest(httpStatus);
				}
				bank.code = asJson.findPath("code").textValue();

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
					httpStatus.put("status", "failed");
					httpStatus.put("error", e.getLocalizedMessage());
					return badRequest(httpStatus);

				
				}
				
			
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateAcademicProgramJson() {
		ObjectNode httpStatus = Json.newObject();

		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			httpStatus.put("Code", "200");

			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			Form<AcademicProgram> contactForm = Form.form(AcademicProgram.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();

				AcademicProgram bank = AcademicProgram.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				bank.accronym = asJson.findPath("accronym").textValue();
				bank.name = asJson.findPath("name").textValue();
				
				if(asJson.has("status")){
					if(asJson.findPath("status").textValue().equalsIgnoreCase("Active") || asJson.findPath("status").textValue().equalsIgnoreCase("Pending")){
						bank.status =asJson.findPath("status").textValue();
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("status", "Failed");

						httpStatus.put("error", "status value must be ACTIVE or DISABLED");

						return badRequest(httpStatus);
					}
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Failed");

					httpStatus.put("error", "status value must be ACTIVE or DISABLED");

					return badRequest(httpStatus);
				}
				bank.code = asJson.findPath("code").textValue();

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
	public static Result deleteAcademicProgramJson() {
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
			List<Student> myPurpose=Student.find.where().eq("academic_program_id", asJson.findPath("id").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				AcademicProgram bank = AcademicProgram.find.byId(asJson.findPath("id").longValue());
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


}
