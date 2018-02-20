package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.StudyProgramType;
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
public class StudyProgramTypeController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<StudyProgramType> all = StudyProgramType.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (StudyProgramType user : all) {
				userJson = Json.newObject();
				
				userJson.put("typeName",user.typeName);
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
		Page<StudyProgramType> pagedList = StudyProgramType.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<StudyProgramType> allAccounts = pagedList.getList();

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
			
			for (StudyProgramType user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("typeName",user.typeName);
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

		StudyProgramType user = StudyProgramType.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("typeName",user.typeName);
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

			Form<StudyProgramType> contactForm = Form.form(StudyProgramType.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();
				StudyProgramType bank = new StudyProgramType();
				
				bank.typeName =asJson.findPath("typeName").textValue();
				
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
			Form<StudyProgramType> contactForm = Form.form(StudyProgramType.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();

				StudyProgramType bank = StudyProgramType.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				bank.typeName =asJson.findPath("typeName").textValue();
				
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

			StudyProgramType bank = StudyProgramType.find.byId(asJson.findPath("id").longValue());
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
