/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.Student;
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

/**
 * @author pc
 *
 */
@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class FacultyController extends Controller {

	public static Result listJson() {
		List<Faculty> all = Faculty.find.all();
		ObjectNode httpStatus = Json.newObject();
		
		if (all.isEmpty()) {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode facultyJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Faculty user : all) {
				facultyJson = Json.newObject();

				facultyJson.put("accronym", user.accronym);
				facultyJson.put("code", user.code);
				try {
					facultyJson.put("facultyId", user.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					facultyJson.put("instId", user.instId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				facultyJson.put("name", user.name);

				array.add(facultyJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}public static Result listAllByInstid(Long instId) {
		List<Faculty> all = Faculty.find.where().eq("institution_id", instId).order("id desc").findList();
		ObjectNode httpStatus = Json.newObject();
		
		if (all.isEmpty()) {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode facultyJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Faculty user : all) {
				facultyJson = Json.newObject();

				facultyJson.put("accronym", user.accronym);
				facultyJson.put("code", user.code);
				try {
					facultyJson.put("facultyId", user.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					facultyJson.put("instId", user.instId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				facultyJson.put("name", user.name);

				array.add(facultyJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}public static Result allByPaging(Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		// Paging starts
				Page<Faculty> pagedList = Faculty.find.findPagingList(pageMax.intValue())
						.setFetchAhead(false).getPage(pageNum.intValue());

				// fetch and return the list
				List<Faculty> allFaculties = pagedList.getList();

				// get the total row count (from the future)
				int totalRowCount = pagedList.getTotalRowCount();
				
		

		if (allFaculties.isEmpty()) {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "bad request");
			httpStatus.put("response", httpStatus);
			
			return badRequest(httpStatus);
		} else {
			
			ObjectNode facultyJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Faculty user : allFaculties) {
				facultyJson = Json.newObject();

				facultyJson.put("accronym", user.accronym);
				facultyJson.put("code", user.code);
				try {
					facultyJson.put("facultyId", user.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					facultyJson.put("instId", user.instId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				facultyJson.put("name", user.name);

				array.add(facultyJson);
			}
			
			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}public static Result allByInstIdPaging(Long instId,Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		
		// Paging starts
				Page<Faculty> pagedList = Faculty.find.where().eq("institution_id", instId).order("id desc").findPagingList(pageMax.intValue())
						.setFetchAhead(false).getPage(pageNum.intValue());

				// fetch and return the list
				List<Faculty> allFaculties = pagedList.getList();

				// get the total row count (from the future)
				int totalRowCount = pagedList.getTotalRowCount();
				
		

		if (allFaculties.isEmpty()) {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		} else {
			
			
			ObjectNode facultyJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Faculty user : allFaculties) {
				facultyJson = Json.newObject();

				facultyJson.put("accronym", user.accronym);
				facultyJson.put("code", user.code);
				try {
					facultyJson.put("facultyId", user.id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					facultyJson.put("instId", user.instId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				facultyJson.put("name", user.name);

				array.add(facultyJson);
			}
			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		
		Faculty user = Faculty.find.byId(id);
		if (user.id > 0) {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("accronym", user.accronym);
			facultyJson.put("code", user.code);
			try {
				facultyJson.put("facultyId", user.id);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				facultyJson.put("instId", user.instId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			facultyJson.put("name", user.name);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", facultyJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode facultyJson = Json.newObject();

			facultyJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createFacultyJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode facultyJson = Json.newObject();
			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		} else {

			
				ObjectNode facultyJson = Json.newObject();
				Faculty faculty = new Faculty();

				if(asJson.has("accronym")){
					if(asJson.findPath("accronym").textValue().length()>0){
						faculty.accronym = asJson.findPath("accronym").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "accronym can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "accronym field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("code")){
					if(asJson.findPath("code").textValue().length()>0){
						faculty.code = asJson.findPath("code").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "code can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "code field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("instId")){
					
					try {
						Institution instById = Institution.find.byId(asJson.findPath("instId").longValue());
						if(instById.name!=null){
							faculty.instId = instById;
						}else{
							httpStatus.put("Code", "405");
							httpStatus.put("error", "institution does not exist");
							return badRequest(httpStatus);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "405");
						httpStatus.put("error", "institution does not exist");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "institution does not exist");
					return badRequest(httpStatus);
				}
				try {
					Faculty myfaculty=Faculty.find.where().eq("name", asJson.findPath("name").textValue()).eq("institution_id", asJson.findPath("instId").longValue()).findUnique();
					if(myfaculty.id>0){
						httpStatus.put("Code", "405");
						httpStatus.put("error", "faculty name exists already");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(asJson.has("name")){
					if(asJson.findPath("name").textValue().length()>0){
						faculty.name = asJson.findPath("name").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "Name can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "Name field is missing");
					return badRequest(httpStatus);	
				}
				// save the user account
				try {
					faculty.save();
					facultyJson.put("status", "saved");
					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", facultyJson);
					
					return ok(httpStatus);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("error", e.getMessage());
					httpStatus.put("Code", "401");
					httpStatus.put("status", "failed");
					
					return badRequest(httpStatus);
				
					
				}
				
				
			
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateFacultyJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

				ObjectNode facultyJson = Json.newObject();

				Faculty faculty = Faculty.find.byId(asJson.findPath("facultyId").longValue());

				if(asJson.has("accronym")){
					if(asJson.findPath("accronym").textValue().length()>0){
						faculty.accronym = asJson.findPath("accronym").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "accronym can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "accronym field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("code")){
					if(asJson.findPath("code").textValue().length()>0){
						faculty.code = asJson.findPath("code").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "code can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "code field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("instId")){
					
					try {
						Institution instById = Institution.find.byId(asJson.findPath("instId").longValue());
						if(instById.name!=null){
							faculty.instId = instById;
						}else{
							httpStatus.put("Code", "405");
							httpStatus.put("error", "institution does not exist");
							return badRequest(httpStatus);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "405");
						httpStatus.put("error", "institution does not exist");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "institution does not exist");
					return badRequest(httpStatus);
				}
				try {
					Faculty myfaculty=Faculty.find.where().eq("name", asJson.findPath("name").textValue()).eq("institution_id", asJson.findPath("instId").longValue()).findUnique();
					if(myfaculty.id>0){
						httpStatus.put("Code", "405");
						httpStatus.put("error", "faculty name exists already");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(asJson.has("name")){
					if(asJson.findPath("name").textValue().length()>0){
						faculty.name = asJson.findPath("name").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "Name can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "Name field is missing");
					return badRequest(httpStatus);	
				}
				// save the user account
				try {
					faculty.update();

					facultyJson.put("status", "updated");
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", facultyJson);
					
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("error", e.getMessage());
					
					httpStatus.put("Code", "401");
					httpStatus.put("status", "failed");
					
					return badRequest(httpStatus);
				
				}
		

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteFacultyJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode facultyJson = Json.newObject();
			facultyJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", facultyJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode facultyJson = Json.newObject();
			List<Student> myPurpose=Student.find.where().eq("faculty_id", asJson.findPath("facultyId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				Faculty faculty = Faculty.find.byId(asJson.findPath("facultyId").longValue());
				// save the user account
				try {
					faculty.delete();

					facultyJson.put("status", "updated");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					facultyJson.put("status", e.getMessage());
				}
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", facultyJson);
				
				return ok(httpStatus);
			}
			
		}

	}

}
