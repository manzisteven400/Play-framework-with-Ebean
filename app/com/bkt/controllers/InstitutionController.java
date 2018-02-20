/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
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

/**
 * @author pc
 *
 */
@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class InstitutionController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		
		List<Institution> all = Institution.find.all();

		if (all.isEmpty()) {
			ObjectNode institutionJson = Json.newObject();

			institutionJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode institutionJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Institution user : all) {
				institutionJson = Json.newObject();

				institutionJson.put("accronym", user.accronym);
				institutionJson.put("address", user.address);
				institutionJson.put("id", user.id);
				institutionJson.put("email", user.email);
				institutionJson.put("instCode", user.instCode);
				institutionJson.put("instLogo", user.instLogo);
				institutionJson.put("name", user.name);
				institutionJson.put("phone", user.phone);
				institutionJson.put("thirdParty", user.thirdParty);
				institutionJson.put("tin", user.tin);
				institutionJson.put("studentPayTransactionFees", user.studentPayTransactionFees);

				array.add(institutionJson);
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
				Page<Institution> pagedList = Institution.find.findPagingList(pageMax.intValue())
						.setFetchAhead(false).getPage(pageNum.intValue());

				// fetch and return the list
				List<Institution> allInstitutions = pagedList.getList();

				// get the total row count (from the future)
				int totalRowCount = pagedList.getTotalRowCount();
				
		if (allInstitutions.size()<0) {
			ObjectNode institutionJson = Json.newObject();

			institutionJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode institutionJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Institution user : allInstitutions) {
				institutionJson = Json.newObject();

				institutionJson.put("accronym", user.accronym);
				institutionJson.put("address", user.address);
				institutionJson.put("email", user.email);
				institutionJson.put("instCode", user.instCode);
				institutionJson.put("instLogo", user.instLogo);
				institutionJson.put("name", user.name);
				institutionJson.put("phone", user.phone);
				institutionJson.put("thirdParty", user.thirdParty);
				institutionJson.put("tin", user.tin);
				institutionJson.put("id", user.id);
				institutionJson.put("studentPayTransactionFees", user.studentPayTransactionFees);

				array.add(institutionJson);
			}
			
			httpStatus.put("rowCount", allInstitutions.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		
		Institution user = Institution.find.byId(id);
		if (user.id > 0) {
			ObjectNode institutionJson = Json.newObject();

			institutionJson.put("accronym", user.accronym);
			institutionJson.put("address", user.address);
			institutionJson.put("email", user.email);
			institutionJson.put("instCode", user.instCode);
			institutionJson.put("instLogo", user.instLogo);
			institutionJson.put("name", user.name);
			institutionJson.put("phone", user.phone);
			institutionJson.put("id", user.id);
			institutionJson.put("thirdParty", user.thirdParty);
			institutionJson.put("tin", user.tin);
			institutionJson.put("studentPayTransactionFees", user.studentPayTransactionFees);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", institutionJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode institutionJson = Json.newObject();

			institutionJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createInstitutionJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode institutionJson = Json.newObject();
			institutionJson.put("status", "No data");
			

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		} else {
			Form<Institution> contactForm = Form.form(Institution.class).bind(asJson);
			if (contactForm.hasErrors()) {
				

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode institutionJson = Json.newObject();
				Institution institution = new Institution();

				institution.accronym = asJson.findPath("accronym").textValue();
				institution.address = asJson.findPath("address").textValue();
				institution.email = asJson.findPath("email").textValue();
				institution.instCode = asJson.findPath("instCode").textValue();
				institution.instLogo = asJson.findPath("instLogo").textValue();
				institution.name = asJson.findPath("name").textValue();
				institution.phone = asJson.findPath("phone").textValue();
				institution.thirdParty = asJson.findPath("thirdParty").textValue();
				institution.tin = asJson.findPath("tin").textValue();
				if(asJson.has("studentPayTransactionFees")){
					if(asJson.findPath("studentPayTransactionFees").textValue().length()>1){
						institution.studentPayTransactionFees = asJson.findPath("studentPayTransactionFees").textValue();
						
					}else{
						institution.studentPayTransactionFees = "studentPays";
	
					}
					
				}else{
					institution.studentPayTransactionFees = "studentPays";
				}
				
				// save the user account
				try {
					institution.save();
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", institutionJson);
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					institutionJson.put("status", e.getMessage());
					institutionJson.put("status", "failed");
					institutionJson.put("Code", "401");
					return badRequest(institutionJson);
				}
				
				
				
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateInstitutionJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode institutionJson = Json.newObject();
			institutionJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		} else {
			Form<Institution> contactForm = Form.form(Institution.class).bind(asJson);
			if (contactForm.hasErrors()) {

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode institutionJson = Json.newObject();

				Institution institution = Institution.find.byId(asJson.findPath("instId").longValue());

				institution.accronym = asJson.findPath("accronym").textValue();
				institution.address = asJson.findPath("address").textValue();
				institution.email = asJson.findPath("email").textValue();
				institution.instCode = asJson.findPath("instCode").textValue();
				institution.instLogo = asJson.findPath("instLogo").textValue();
				institution.name = asJson.findPath("name").textValue();
				institution.phone = asJson.findPath("phone").textValue();
				institution.thirdParty = asJson.findPath("thirdParty").textValue();
				institution.tin = asJson.findPath("tin").textValue();
				if(asJson.has("studentPayTransactionFees")){
					if(asJson.findPath("studentPayTransactionFees").textValue().length()>1){
						institution.studentPayTransactionFees = asJson.findPath("studentPayTransactionFees").textValue();
						
					}else{
						institution.studentPayTransactionFees = "studentPays";
	
					}
					
				}else{
					institution.studentPayTransactionFees = "studentPays";

				}
				// save the user account
				try {
					institution.update();
					
					httpStatus.put("status", "Success");
					httpStatus.put("Code", "200");
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					institutionJson.put("error", e.getMessage());
					institutionJson.put("status", "failed");
					institutionJson.put("Code", "401");
					return badRequest(institutionJson);
				}
				
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteInstitutionJson() {
		ObjectNode httpStatus = Json.newObject();
		
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode institutionJson = Json.newObject();
			institutionJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", institutionJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode institutionJson = Json.newObject();
			List<Student> myPurpose=Student.find.where().eq("institution_id", asJson.findPath("instId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				Institution institution = Institution.find.byId(asJson.findPath("instId").longValue());
				// save the user account
				try {
					institution.delete();

					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					institutionJson.put("error", e.getMessage());
					institutionJson.put("status", "failed");
					institutionJson.put("Code", "401");
					return badRequest(institutionJson);
				}
			}
			
			
		}

	}
}
