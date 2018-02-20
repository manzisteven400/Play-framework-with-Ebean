package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.DegreeStudyProgram;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.NoneDegreeStudyProgram;
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
public class TuitionFeesController extends Controller {
	
	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<TuitionFees> all = TuitionFees.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (TuitionFees user : all) {
				userJson = Json.newObject();
				
				userJson.put("amount",user.amount);
				userJson.put("status",user.status);
				try {
					userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
					//ObjectNode degree= Json.newObject();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("institutionId",user.institutionId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("instCalendar",user.instCalendar.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result listByDegreeProgramId(Long programId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<TuitionFees> all = TuitionFees.find.where().eq("degree_study_program_id", programId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (TuitionFees user : all) {
				userJson = Json.newObject();
				
				userJson.put("amount",user.amount);
				userJson.put("status",user.status);
				try {
					userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("institutionId",user.institutionId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("instCalendar",user.instCalendar.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByNoneDegreeProgramId(Long programId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<TuitionFees> all = TuitionFees.find.where().eq("none_degree_study_program_id", programId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (TuitionFees user : all) {
				userJson = Json.newObject();
				
				userJson.put("amount",user.amount);
				userJson.put("status",user.status);
				try {
					userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("institutionId",user.institutionId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("instCalendar",user.instCalendar.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByInstCalenderId(Long programId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<TuitionFees> all = TuitionFees.find.where().eq("institution_calender_id", programId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (TuitionFees user : all) {
				userJson = Json.newObject();
				
				userJson.put("amount",user.amount);
				userJson.put("status",user.status);
				try {
					userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("institutionId",user.institutionId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("instCalendar",user.instCalendar.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		Page<TuitionFees> pagedList = TuitionFees.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<TuitionFees> allAccounts = pagedList.getList();

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
			
			for (TuitionFees user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("amount",user.amount);
				userJson.put("status",user.status);
				try {
					userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("institutionId",user.institutionId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("instCalendar",user.instCalendar.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		TuitionFees user = TuitionFees.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("amount",user.amount);
			userJson.put("status",user.status);
			try {
				userJson.put("degreeStudyProgramId",user.degreeStudyProgramId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("noneDegreeStudyProgramId",user.noneDegreeStudyProgramId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("institutionId",user.institutionId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("facultyId",user.facultyId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("instCalendar",user.instCalendar.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

				ObjectNode userJson = Json.newObject();
				TuitionFees bank = new TuitionFees();
				
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
				if(asJson.has("amount")){
					if(asJson.findPath("amount").asDouble()>0){
						bank.amount =asJson.findPath("amount").asDouble();
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("status", "Zeor Amount can not be set as tuition fees");
						return badRequest(httpStatus);
					}
				}else{

					httpStatus.put("Code", "401");
					httpStatus.put("status", "Amount field is mandatory");
					return badRequest(httpStatus);
				
				}
				if (!(bank.amount > 0)) {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Amount field is mandatory");
					return badRequest(httpStatus);
				}
				if(asJson.has("degreeStudyProgramId")){
					try {
						DegreeStudyProgram degreeStudyProgram = DegreeStudyProgram.find.byId(asJson.findPath("degreeStudyProgramId").longValue());
						if(degreeStudyProgram.programStudyDuration!=null){
							bank.degreeStudyProgramId =degreeStudyProgram;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "degreeStudyProgramId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "degreeStudyProgramId not found");
						return badRequest(httpStatus);	
					}
					
				}
				
				if(asJson.has("noneDegreeStudyProgramId")){
					try {
						NoneDegreeStudyProgram noneDegreeStudyProgramId = NoneDegreeStudyProgram.find.byId(asJson.findPath("noneDegreeStudyProgramId").longValue());
						if(noneDegreeStudyProgramId.status!=null){
							bank.noneDegreeStudyProgramId=noneDegreeStudyProgramId;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "noneDegreeStudyProgramId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "noneDegreeStudyProgramId not found");
						return badRequest(httpStatus);	
					}
					
				}
				
				if(asJson.has("institutionId")){
					try {
						Institution institution = Institution.find.byId(asJson.findPath("institutionId").longValue());
						if(institution.name!=null){
							bank.institutionId =institution;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "institutionId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "institutionId not found");
						return badRequest(httpStatus);	
					}
					
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("status", "institutionId is not found");
					return badRequest(httpStatus);	
				}
				
			
				
				try {
					if (asJson.has("facultyId")) {
						
						Faculty faculty = Faculty.find.byId(asJson.findPath("facultyId").longValue());
						if(faculty.name!=null){
							bank.facultyId=faculty;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "facultyId not found");
							return badRequest(httpStatus);	
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "facultyId is not found");
					return badRequest(httpStatus);
				}
				try {
					if (asJson.has("instCalendar")) {
						 InstitutionCalender calenderid = InstitutionCalender.find.byId(asJson.findPath("instCalendar").longValue());
						if(calenderid.academicYear!=null){
							bank.instCalendar=calenderid;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "instCalendar not found");
							return badRequest(httpStatus);	
						}
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "instCalendar is not found");
						return badRequest(httpStatus);
					
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "instCalendar is not found");
					return badRequest(httpStatus);
				}
				
				//check for duplicates at insert
				try {
					if(asJson.has("degreeStudyProgramId")){
						List<TuitionFees> degreeProgram = TuitionFees.find.where()
								.eq("degree_study_program_id", asJson.findPath("degreeStudyProgramId").longValue())
								.eq("institution_calender_id", asJson.findPath("instCalendar").longValue())
								.eq("institution_id", asJson.findPath("institutionId").longValue())
								.eq("amount", asJson.findPath("amount").asDouble())
								.findList();
						if(degreeProgram.size()>0){
							httpStatus.put("Code", "401");
							httpStatus.put("error", "Same tuition Fees set up already exists");
							return badRequest(httpStatus);
						}	
					}else{
						List<TuitionFees> degreeProgram = TuitionFees.find.where()
								.eq("none_degree_study_program_id", asJson.findPath("noneDegreeStudyProgramId").longValue())
								.eq("institution_calender_id", asJson.findPath("instCalendar").longValue())
								.eq("institution_id", asJson.findPath("institutionId").longValue())
								.eq("amount", asJson.findPath("amount").asDouble())
								.findList();
						if(degreeProgram.size()>0){
							httpStatus.put("Code", "401");
							httpStatus.put("error", "Same tuition Fees set up already exists");
							return badRequest(httpStatus);
						}	
					
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
					httpStatus.put("status", "failed");
					httpStatus.put("error", e.getMessage());

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
			
				ObjectNode userJson = Json.newObject();

				TuitionFees bank = TuitionFees.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
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
				if(asJson.has("amount")){
					if(asJson.findPath("amount").asDouble()>0){
						bank.amount =asJson.findPath("amount").asDouble();
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("status", "Zeor Amount can not be set as tuition fees");
						return badRequest(httpStatus);
					}
				}else{

					httpStatus.put("Code", "401");
					httpStatus.put("status", "Amount field is mandatory");
					return badRequest(httpStatus);
				
				}
				if (!(bank.amount > 0)) {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Amount field is mandatory");
					return badRequest(httpStatus);
				}
				if(asJson.has("degreeStudyProgramId")){
					try {
						DegreeStudyProgram degreeStudyProgram = DegreeStudyProgram.find.byId(asJson.findPath("degreeStudyProgramId").longValue());
						if(degreeStudyProgram.programStudyDuration!=null){
							bank.degreeStudyProgramId =degreeStudyProgram;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "degreeStudyProgramId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "degreeStudyProgramId not found");
						return badRequest(httpStatus);	
					}
					
				}
				
				if(asJson.has("noneDegreeStudyProgramId")){
					try {
						NoneDegreeStudyProgram noneDegreeStudyProgramId = NoneDegreeStudyProgram.find.byId(asJson.findPath("noneDegreeStudyProgramId").longValue());
						if(noneDegreeStudyProgramId.status!=null){
							bank.noneDegreeStudyProgramId=noneDegreeStudyProgramId;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "noneDegreeStudyProgramId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "noneDegreeStudyProgramId not found");
						return badRequest(httpStatus);	
					}
					
				}
				
				if(asJson.has("institutionId")){
					try {
						Institution institution = Institution.find.byId(asJson.findPath("institutionId").longValue());
						if(institution.name!=null){
							bank.institutionId =institution;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "institutionId not found");
							return badRequest(httpStatus);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "401");
						httpStatus.put("error", "institutionId not found");
						return badRequest(httpStatus);	
					}
					
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("status", "institutionId is not found");
					return badRequest(httpStatus);	
				}
				
			
				
				try {
					if (asJson.has("facultyId")) {
						
						Faculty faculty = Faculty.find.byId(asJson.findPath("facultyId").longValue());
						if(faculty.name!=null){
							bank.facultyId=faculty;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "facultyId not found");
							return badRequest(httpStatus);	
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "facultyId is not found");
					return badRequest(httpStatus);
				}
				try {
					if (asJson.has("instCalendar")) {
						 InstitutionCalender calenderid = InstitutionCalender.find.byId(asJson.findPath("instCalendar").longValue());
						if(calenderid.academicYear!=null){
							bank.instCalendar=calenderid;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "instCalendar not found");
							return badRequest(httpStatus);	
						}
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "instCalendar is not found");
						return badRequest(httpStatus);
					
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "instCalendar is not found");
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

			TuitionFees bank = TuitionFees.find.byId(asJson.findPath("id").longValue());
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
