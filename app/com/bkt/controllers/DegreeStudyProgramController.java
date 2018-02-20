package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.DegreeProgram;
import com.bkt.models.DegreeStudyProgram;
import com.bkt.models.StudyProgramType;
import com.bkt.models.TuitionFees;
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
public class DegreeStudyProgramController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<DegreeStudyProgram> all = DegreeStudyProgram.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeStudyProgram user : all) {
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

		List<DegreeStudyProgram> all = DegreeStudyProgram.find.where().eq("degree_program_id", degreeProgramId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeStudyProgram user : all) {
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

		List<DegreeStudyProgram> all = DegreeStudyProgram.find.where().eq("study_program_type_id", studyTypeId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeStudyProgram user : all) {
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
		Page<DegreeStudyProgram> pagedList = DegreeStudyProgram.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<DegreeStudyProgram> allAccounts = pagedList.getList();

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
			
			for (DegreeStudyProgram user : allAccounts) {
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

		DegreeStudyProgram user = DegreeStudyProgram.find.byId(id);
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
				DegreeStudyProgram bank = new DegreeStudyProgram();
				
				
				if(asJson.has("degreeProgram")){
					try {
						DegreeProgram degreeProgram = DegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
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
					DegreeStudyProgram degreeProgram = DegreeStudyProgram.find.where()
							.eq("program_study_duration", asJson.findPath("programStudyDuration").textValue())
							.eq("status", asJson.findPath("status").textValue())
							.eq("degree_program_id", asJson.findPath("degreeProgram").longValue())
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
					httpStatus.put("status", "Failed");

					httpStatus.put("error", e.getMessage());

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

				DegreeStudyProgram bank = DegreeStudyProgram.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				
				if(asJson.has("degreeProgram")){
					try {
						DegreeProgram degreeProgram = DegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
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
					httpStatus.put("status", "Failed");

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
			List<TuitionFees> myPurpose=TuitionFees.find.where().eq("degree_study_program_id", asJson.findPath("id").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				DegreeStudyProgram bank = DegreeStudyProgram.find.byId(asJson.findPath("id").longValue());
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
