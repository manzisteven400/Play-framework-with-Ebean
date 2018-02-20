package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.AcademicProgram;
import com.bkt.models.DegreeProgram;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
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
public class DegreeProgramController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<DegreeProgram> all = DegreeProgram.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeProgram user : all) {
				userJson = Json.newObject();
				
				try {
					userJson.put("academicProgram",user.academicProgram.id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					userJson.put("institution",user.institution.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("degreeAccronym",user.degreeAccronym);
				userJson.put("degreeName",user.degreeName);
				userJson.put("status",user.status);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByFacultyId(Long facultId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<DegreeProgram> all = DegreeProgram.find.where().eq("faculty_id", facultId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeProgram user : all) {
				userJson = Json.newObject();
				
				try {
					userJson.put("academicProgram",user.academicProgram.id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					userJson.put("institution",user.institution.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("degreeAccronym",user.degreeAccronym);
				userJson.put("degreeName",user.degreeName);
				userJson.put("status",user.status);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}public static Result listByInstId(Long facultId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<DegreeProgram> all = DegreeProgram.find.where().eq("institution_id", facultId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeProgram user : all) {
				userJson = Json.newObject();
				
				try {
					userJson.put("academicProgram",user.academicProgram.id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					userJson.put("institution",user.institution.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("degreeAccronym",user.degreeAccronym);
				userJson.put("degreeName",user.degreeName);
				userJson.put("status",user.status);
				userJson.put("id",user.id);
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}
	public static Result listByInstIdAndFacultyId(Long instId,Long facultId ) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<DegreeProgram> all = DegreeProgram.find.where()
				.eq("institution_id",instId)
				.eq("faculty_id",facultId)
				.findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (DegreeProgram user : all) {
				userJson = Json.newObject();
				
				try {
					userJson.put("academicProgram",user.academicProgram.id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					userJson.put("institution",user.institution.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("degreeAccronym",user.degreeAccronym);
				userJson.put("degreeName",user.degreeName);
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
		Page<DegreeProgram> pagedList = DegreeProgram.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<DegreeProgram> allAccounts = pagedList.getList();

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
			
			for (DegreeProgram user : allAccounts) {
				userJson = Json.newObject();
				
				try {
					userJson.put("academicProgram",user.academicProgram.id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					userJson.put("facultyId",user.facultyId.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					userJson.put("institution",user.institution.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("degreeAccronym",user.degreeAccronym);
				userJson.put("degreeName",user.degreeName);
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

		DegreeProgram user = DegreeProgram.find.byId(id);
		ObjectNode userJson = Json.newObject();

		if (user.id > 0) {
			
			
			try {
				if(user.academicProgram.accronym !=null){
					userJson.put("academicProgram",user.academicProgram.id);
				}
				
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				if(user.facultyId.name !=null){
					userJson.put("facultyId",user.facultyId.id);
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(user.institution.name !=null){
				userJson.put("institution",user.institution.id);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			userJson.put("degreeAccronym",user.degreeAccronym);
			userJson.put("degreeName",user.degreeName);
			userJson.put("status",user.status);
			userJson.put("id",user.id);

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
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

			//Form<DegreeProgram> contactForm = Form.form(DegreeProgram.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();
				DegreeProgram bank = new DegreeProgram();
				
				try {
					if(asJson.has("academicProgram")){
						AcademicProgram acdebyId = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());
						if(acdebyId.name !=null){
							bank.academicProgram = acdebyId;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "academicProgram is not found");
							return badRequest(httpStatus);
						}
							
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("error", "academicProgram is missing");
						return badRequest(httpStatus);
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("error", "academicProgram is found");
					return badRequest(httpStatus);
				}
				try {
					
					if (asJson.has("institution")) {
						Institution instById = Institution.find.byId(asJson.findPath("institution").longValue());
						if(instById.name!=null){
							bank.institution = instById;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "institutionId is not found");
							return badRequest(httpStatus);
						}
						
						
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "institutionId is not found");
						return badRequest(httpStatus);
					
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "institutionId is not found");
					return badRequest(httpStatus);
				}
				try {
					if (asJson.has("facultyId")) {
						Faculty facultybyId = Faculty.find.where()
								.eq("id",asJson.findPath("facultyId").longValue())
								.eq("institution_id", asJson.findPath("institution").longValue())
								.findUnique();
						if(facultybyId!=null){
							bank.facultyId = facultybyId ;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "FacultyId is not found or does not mapp with institution");
							return badRequest(httpStatus);
						}
						
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "facultyId is not found");
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
				if(asJson.has("degreeName")){
					bank.degreeName = asJson.findPath("degreeName").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "degreeName is missing");
					return badRequest(httpStatus);
				}
				if(asJson.has("degreeAccronym")){
					bank.degreeAccronym = asJson.findPath("degreeAccronym").textValue();
						
				}
				try {
					DegreeProgram degreeProgram = DegreeProgram.find.where()
							.eq("degree_accronym", asJson.findPath("degreeAccronym").textValue())
							.eq("degree_name", asJson.findPath("degreeName").textValue())
							.eq("institution_id", asJson.findPath("institution").longValue())
							.eq("faculty_id",asJson.findPath("facultyId").longValue())
							.eq("academic_program_id", asJson.findPath("academicProgram").longValue())
							.findUnique();
					if(degreeProgram.degreeName!=null){
						httpStatus.put("Code", "401");
						httpStatus.put("error", "Degree program already exists");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}
				if(asJson.has("degreeName")){
					bank.degreeName = asJson.findPath("degreeName").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "degreeName is missing");
					return badRequest(httpStatus);
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
			Form<DegreeProgram> contactForm = Form.form(DegreeProgram.class).bind(asJson);
			
				ObjectNode userJson = Json.newObject();

				DegreeProgram bank = DegreeProgram.find.byId(asJson.findPath("id").longValue());

				bank.id = asJson.findPath("id").longValue();
				
				try {
					if(asJson.has("academicProgram")){
						AcademicProgram acdebyId = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());
						if(acdebyId.name !=null){
							bank.academicProgram = acdebyId;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("error", "academicProgram is not found");
							return badRequest(httpStatus);
						}
							
					}else{
						httpStatus.put("Code", "401");
						httpStatus.put("error", "academicProgram is missing");
						return badRequest(httpStatus);
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("error", "academicProgram is found");
					return badRequest(httpStatus);
				}
				try {
					
					if (asJson.has("institution")) {
						Institution instById = Institution.find.byId(asJson.findPath("institution").longValue());
						if(instById.name!=null){
							bank.institution = instById;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "institutionId is not found");
							return badRequest(httpStatus);
						}
						
						
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "institutionId is not found");
						return badRequest(httpStatus);
					
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "institutionId is not found");
					return badRequest(httpStatus);
				}
				try {
					if (asJson.has("facultyId")) {
						Faculty facultybyId = Faculty.find.where()
								.eq("id",asJson.findPath("facultyId").longValue())
								.eq("institution_id", asJson.findPath("institution").longValue())
								.findUnique();
						if(facultybyId!=null){
							bank.facultyId = facultybyId ;
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "FacultyId is not found or does not mapp with institution");
							return badRequest(httpStatus);
						}
						
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "facultyId is not found");
					return badRequest(httpStatus);
				}
				
				bank.status =asJson.findPath("status").textValue();
				bank.degreeName = asJson.findPath("degreeName").textValue();
				if(asJson.has("degreeAccronym")){
					bank.degreeAccronym = asJson.findPath("degreeAccronym").textValue();
						
				}
				if(asJson.has("degreeName")){
					bank.degreeName = asJson.findPath("degreeName").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "degreeName is missing");
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
			List<Student> myPurpose=Student.find.where().eq("degree_program_id", asJson.findPath("id").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				DegreeProgram bank = DegreeProgram.find.byId(asJson.findPath("id").longValue());
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
