package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
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


@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class InstitutionCalenderController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<InstitutionCalender> all = InstitutionCalender.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (InstitutionCalender user : all) {
				userJson = Json.newObject();
				
				userJson.put("status",user.status);
				userJson.put("academicYear",user.academicYear);
				userJson.put("batchCode",user.batchCode);
				userJson.put("endDate",user.endDate);
				userJson.put("startDate",user.startDate);
				try {
					userJson.put("institution",user.institution.id);
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

	public static Result listAllByInstId(Long instId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<InstitutionCalender> all = InstitutionCalender.find.where().eq("institution_id", instId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (InstitutionCalender user : all) {
				userJson = Json.newObject();
				
				userJson.put("status",user.status);
				userJson.put("academicYear",user.academicYear);
				userJson.put("batchCode",user.batchCode);
				userJson.put("endDate",user.endDate);
				userJson.put("startDate",user.startDate);
				try {
					userJson.put("institution",user.institution.id);
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
		Page<InstitutionCalender> pagedList = InstitutionCalender.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<InstitutionCalender> allAccounts = pagedList.getList();

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
			
			for (InstitutionCalender user : allAccounts) {
				userJson = Json.newObject();
				
				userJson.put("status",user.status);
				userJson.put("academicYear",user.academicYear);
				userJson.put("batchCode",user.batchCode);
				userJson.put("endDate",user.endDate);
				userJson.put("startDate",user.startDate);
				try {
					userJson.put("institution",user.institution.id);
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

		InstitutionCalender user = InstitutionCalender.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status",user.status);
			userJson.put("academicYear",user.academicYear);
			userJson.put("batchCode",user.batchCode);
			userJson.put("endDate",user.endDate);
			userJson.put("startDate",user.startDate);
			try {
				userJson.put("institution",user.institution.id);
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
				InstitutionCalender bank = new InstitutionCalender();
				
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
				if(asJson.has("academicYear")){
					if(asJson.findPath("academicYear").textValue().length()>0){
						bank.academicYear = asJson.findPath("academicYear").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "academicYear can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "academicYear field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("institution")){
					
					try {
						Institution instById = Institution.find.byId(asJson.findPath("institution").longValue());
						if(instById.name!=null){
							bank.institution = instById;
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
				
				if(asJson.has("batchCode")){
					if(asJson.findPath("batchCode").textValue().length()>0){
						bank.batchCode = asJson.findPath("batchCode").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "batchCode can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "batchCode field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("endDate")){
					if(asJson.findPath("endDate").textValue().length()>0){
						bank.endDate = asJson.findPath("endDate").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "endDate can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "endDate field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("startDate")){
					if(asJson.findPath("startDate").textValue().length()>0){
						bank.startDate = asJson.findPath("startDate").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "startDate can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "startDate field is missing");
					return badRequest(httpStatus);	
				}
				
				//avoid duplications
				try {
					List<InstitutionCalender> myfaculty=InstitutionCalender.find.where()
							.eq("academic_year", asJson.findPath("academicYear").textValue())
							.eq("institution_id", asJson.findPath("institution").longValue())
							.findList();
					if(myfaculty.size()>0){
						httpStatus.put("Code", "405");
						httpStatus.put("error", "academicYear exists already");
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

				InstitutionCalender bank = InstitutionCalender.find.byId(asJson.findPath("id").longValue());

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
				if(asJson.has("academicYear")){
					if(asJson.findPath("academicYear").textValue().length()>0){
						bank.academicYear = asJson.findPath("academicYear").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "academicYear can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "academicYear field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("institution")){
					
					try {
						Institution instById = Institution.find.byId(asJson.findPath("institution").longValue());
						if(instById.name!=null){
							bank.institution = instById;
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
					InstitutionCalender myfaculty=InstitutionCalender.find.where()
							.eq("academic_year", asJson.findPath("academicYear").textValue())
							.eq("institution_id", asJson.findPath("institution").longValue())
							.findUnique();
					if(myfaculty.id>0){
						httpStatus.put("Code", "405");
						httpStatus.put("error", "institution calender exists already");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(asJson.has("batchCode")){
					if(asJson.findPath("batchCode").textValue().length()>0){
						bank.batchCode = asJson.findPath("batchCode").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "batchCode can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "batchCode field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("endDate")){
					if(asJson.findPath("endDate").textValue().length()>0){
						bank.endDate = asJson.findPath("endDate").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "endDate can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "endDate field is missing");
					return badRequest(httpStatus);	
				}
				if(asJson.has("startDate")){
					if(asJson.findPath("startDate").textValue().length()>0){
						bank.startDate = asJson.findPath("startDate").textValue();
					}else{
						httpStatus.put("Code", "405");
						httpStatus.put("error", "startDate can not be empty");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "startDate field is missing");
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
			List<Student> myPurpose=Student.find.where().eq("institution_calender_id", asJson.findPath("id").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				InstitutionCalender bank = InstitutionCalender.find.byId(asJson.findPath("id").longValue());
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
