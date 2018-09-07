package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.NoneDegreeProgram;
import com.bkt.models.NoneDegreeStudyProgram;
import com.bkt.models.StudyProgramType;
import com.bkt.utils.LogRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class NoneDegreeStudyProgramController extends Controller{

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<NoneDegreeStudyProgram> all = NoneDegreeStudyProgram.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (NoneDegreeStudyProgram user : all) {
				userJson = Json.newObject();
				
				userJson.put("programStudyDuration",user.programStudyDuration);
				try {
					userJson.put("degreeProgram",user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgramId",user.studyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("status",user.status);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByDegreeId(Long degreeProgramId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<NoneDegreeStudyProgram> all = NoneDegreeStudyProgram.find.where().eq("none_degree_program_id", degreeProgramId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (NoneDegreeStudyProgram user : all) {
				userJson = Json.newObject();
				
				userJson.put("programStudyDuration",user.programStudyDuration);
				try {
					userJson.put("degreeProgram",user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgramId",user.studyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("status",user.status);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByStudyType(Long studyTypeId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<NoneDegreeStudyProgram> all = NoneDegreeStudyProgram.find.where().eq("study_program_type_id", studyTypeId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (NoneDegreeStudyProgram user : all) {
				userJson = Json.newObject();
				
				userJson.put("programStudyDuration",user.programStudyDuration);
				try {
					userJson.put("degreeProgram",user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgramId",user.studyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("status",user.status);
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
		Page<NoneDegreeStudyProgram> pagedList = NoneDegreeStudyProgram.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<NoneDegreeStudyProgram> allAccounts = pagedList.getList();

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
			
			for (NoneDegreeStudyProgram user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("programStudyDuration",user.programStudyDuration);
				try {
					userJson.put("degreeProgram",user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgramId",user.studyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("status",user.status);
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

		NoneDegreeStudyProgram user = NoneDegreeStudyProgram.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("programStudyDuration",user.programStudyDuration);
			try {
				userJson.put("degreeProgram",user.degreeProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("studyProgramId",user.studyProgramId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userJson.put("status",user.status);
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

				ObjectNode userJson = Json.newObject();
				NoneDegreeStudyProgram bank = new NoneDegreeStudyProgram();
				
				
				if(asJson.has("degreeProgram")){
					try {
						NoneDegreeProgram degreeProgram = NoneDegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
						if(degreeProgram.degreeName!=null){
							bank.degreeProgram =degreeProgram;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "degreeProgram not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "degreeProgram not found");
						return badRequest(httpStatus);	
					}
					
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "degreeProgram is missing");
					return badRequest(httpStatus);
				}
				if(asJson.has("studyProgramId")){
					try {
						StudyProgramType studyType = StudyProgramType.find.byId(asJson.findPath("studyProgramId").longValue());
						if(studyType.typeName!=null){

							bank.studyProgramId = studyType;	
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "studyProgramId not found");
							return badRequest(httpStatus);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "studyProgramId not found");
						return badRequest(httpStatus);
					}

						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "studyProgramId not found");
					return badRequest(httpStatus);
				}
				if(asJson.has("programStudyDuration")){
					bank.programStudyDuration = asJson.findPath("programStudyDuration").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "programStudyDuration is missing");
					return badRequest(httpStatus);
				}
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
				
				//check for duplicates at insert
				try {
					NoneDegreeStudyProgram degreeProgram = NoneDegreeStudyProgram.find.where()
							.eq("program_study_duration", asJson.findPath("programStudyDuration").textValue())
							.eq("status", asJson.findPath("status").textValue())
							.eq("none_none_degree_program_id", asJson.findPath("degreeProgram").longValue())
							.eq("study_program_type_id",asJson.findPath("studyProgramId").longValue())
							.findUnique();
					if(degreeProgram.status!=null){
						httpStatus.put("Code", "401");
						httpStatus.put("error", "Degree study program already exists");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}
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

					httpStatus.put("response", e.getMessage());

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
			
				ObjectNode userJson = Json.newObject();

				NoneDegreeStudyProgram bank = NoneDegreeStudyProgram.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				
				if(asJson.has("degreeProgram")){
					try {
						NoneDegreeProgram degreeProgram = NoneDegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
						if(degreeProgram.degreeName!=null){
							bank.degreeProgram =degreeProgram;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "degreeProgram not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "degreeProgram not found");
						return badRequest(httpStatus);	
					}
					
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "degreeProgram is missing");
					return badRequest(httpStatus);
				}
				if(asJson.has("studyProgramId")){
					try {
						StudyProgramType studyType = StudyProgramType.find.byId(asJson.findPath("studyProgramId").longValue());
						if(studyType.typeName!=null){

							bank.studyProgramId = studyType;	
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "studyProgramId not found");
							return badRequest(httpStatus);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "studyProgramId not found");
						return badRequest(httpStatus);
					}

						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "studyProgramId not found");
					return badRequest(httpStatus);
				}
				if(asJson.has("programStudyDuration")){
					bank.programStudyDuration = asJson.findPath("programStudyDuration").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "programStudyDuration is missing");
					return badRequest(httpStatus);
				}
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

					httpStatus.put("error", e.getMessage());

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

			NoneDegreeStudyProgram bank = NoneDegreeStudyProgram.find.byId(asJson.findPath("id").longValue());
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
