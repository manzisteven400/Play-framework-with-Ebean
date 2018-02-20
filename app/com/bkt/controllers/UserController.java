/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import org.slf4j.MDC;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.UserAccount;
import com.bkt.models.UserAccountArchive;
import com.bkt.models.UserProfile;
import com.bkt.utils.HelperManager;
import com.bkt.utils.LogRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
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
public class UserController extends Controller {
	 private static final Logger.ALogger logger = Logger.of(UserController.class);

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
	
		MDC.put("method", "all");
		List<UserAccount> all = UserAccount.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson ;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (UserAccount user : all) {
				userJson = Json.newObject();
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("lastName", user.lastName);
				userJson.put("phoneNumber", user.phoneNumber);
				userJson.put("userId", user.id);
				if(user.userProfile.id>1){
					userJson.put("instId", user.instId.id);
				}
				userJson.put("status", user.status);
				userJson.put("userProfile", user.userProfile.id);
				userJson.put("regDate", user.regDate.toString());
				userJson.put("password", user.password);

				array.add(userJson);
			}
			logger.trace("Responded json: {}", array.toString());
			
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(array);
		}

	}public static Result allByInstIdPaging(Long instId,Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		// Paging starts
		Page<UserAccount> pagedList = UserAccount.find.where().eq("institution_id", instId).findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<UserAccount> allusers = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allusers.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode userJson ;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (UserAccount user : allusers) {
				userJson = Json.newObject();
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("lastName", user.lastName);
				userJson.put("phoneNumber", user.phoneNumber);
				userJson.put("userId", user.id);
				if(user.userProfile.id>1){
					userJson.put("instId", user.instId.id);
				}
				userJson.put("status", user.status);
				userJson.put("userProfile", user.userProfile.id);
				userJson.put("regDate", user.regDate.toString());
				userJson.put("password", user.password);

				array.add(userJson);
			}
			logger.trace("Responded json: {}", array.toString());
			
			httpStatus.put("rowCount", allusers.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}public static Result allByGroupIdPaging(Long groupId,Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		// Paging starts
		Page<UserAccount> pagedList = UserAccount.find.where().eq("user-profile", groupId).findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<UserAccount> allusers = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allusers.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode userJson ;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (UserAccount user : allusers) {
				userJson = Json.newObject();
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("lastName", user.lastName);
				userJson.put("phoneNumber", user.phoneNumber);
				userJson.put("userId", user.id);
				if(user.userProfile.id>1){
					userJson.put("instId", user.instId.id);
				}
				userJson.put("status", user.status);
				userJson.put("userProfile", user.userProfile.id);
				userJson.put("regDate", user.regDate.toString());
				userJson.put("password", user.password);

				array.add(userJson);
			}
			logger.trace("Responded json: {}", array.toString());
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}public static Result allByPaging(Long pageNum, Long pageMax){
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		// Paging starts
		Page<UserAccount> pagedList = UserAccount.find.findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<UserAccount> allusers = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();
		
		
		if (allusers.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode respJson = Json.newObject();

			respJson.put("rowCount", totalRowCount);
			
			ObjectNode userJson ;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (UserAccount user : allusers) {
				userJson = Json.newObject();
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("lastName", user.lastName);
				userJson.put("phoneNumber", user.phoneNumber);
				userJson.put("userId", user.id);
				if(user.userProfile.id>1){
					userJson.put("instId", user.instId.id);
				}
				userJson.put("status", user.status);
				userJson.put("userProfile", user.userProfile.id);
				userJson.put("regDate", user.regDate.toString());
				userJson.put("password", user.password);

				array.add(userJson);
			}
			logger.trace("Responded json: {}", array.toString());
			
			respJson.put("recordFound", array);
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		logger.trace("received userId:", 1);
		
		UserAccount user = UserAccount.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();
			userJson.put("email", user.email);
			userJson.put("firstName", user.firstName);
			userJson.put("lastName", user.lastName);
			userJson.put("phoneNumber", user.phoneNumber);
			userJson.put("userId", user.id);
			if(user.userProfile.id>1){
				userJson.put("instId", user.instId.id);
			}
			userJson.put("status", user.status);
			userJson.put("userProfile", user.userProfile.id);
			userJson.put("regDate", user.regDate.toString());
			userJson.put("password", user.password);
			
			logger.trace("Responded json: {}", userJson.toString());

			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			
			logger.trace("Responded json: {}", userJson.toString());


			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result userCredentials() {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "Empty logins not allowed");
			
			logger.trace("Responded json: {}", userJson.toString());

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			
			String email = asJson.findPath("email").textValue();
			String password= asJson.findPath("password").textValue();
			UserAccount user;
			try {
				user = UserAccount.find.where().eq("email", email).eq("password", password).findUnique();
				if (user.id > 0) {
					ObjectNode userJson = Json.newObject();
					userJson.put("email", user.email);
					userJson.put("firstName", user.firstName);
					userJson.put("lastName", user.lastName);
					userJson.put("phoneNumber", user.phoneNumber);
					userJson.put("userId", user.id);
					userJson.put("status", user.status);
					userJson.put("userProfile", user.userProfile.id);
					userJson.put("regDate", user.regDate.toString());
					userJson.put("password", user.password);
					
					//logger.info("I am here in.........................."+user.instId);
					
					//institution json
					
					if(user.instId.id>0){
						
						ObjectNode institutionJson = Json.newObject();
					
						institutionJson.put("accronym", user.instId.accronym);
						institutionJson.put("address", user.instId.address);
						institutionJson.put("email", user.instId.email);
						institutionJson.put("instCode", user.instId.instCode);
						institutionJson.put("instLogo", user.instId.instLogo);
						institutionJson.put("name", user.instId.name);
						institutionJson.put("phone", user.instId.phone);
						institutionJson.put("thirdParty", user.instId.thirdParty);
						institutionJson.put("tin", user.instId.tin);
						
						// loops and get institution faculties
						List<Faculty> faculties= Faculty.find.where().eq("institution_id", user.instId.id).findList();
						if(faculties.size()>0){

							ObjectMapper mapper = new ObjectMapper();
							ArrayNode array = mapper.createArrayNode();
							
							for (Faculty instFaculty : faculties) {
								ObjectNode facultyJson = Json.newObject();

								facultyJson.put("accronym", instFaculty.accronym);
								facultyJson.put("code", instFaculty.code);
								facultyJson.put("facultyId", instFaculty.id);
								facultyJson.put("instId", user.instId.id);
								facultyJson.put("name", instFaculty.name);

								array.add(facultyJson);
							}
							institutionJson.put("faculties", array);
							
							userJson.put("institution", institutionJson);
							
						}else{

							userJson.put("institution", institutionJson);
							logger.debug("Empty results - No faculties found - User credentials....");
						}
						
					}else{

						logger.debug("Empty results - No University found - User credentials....");
					
					}
				
					logger.trace("Responded json: {}", userJson.toString());

					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", userJson);
					
					return ok(httpStatus);
				} else {
					ObjectNode userJson = Json.newObject();

					userJson.put("status", "Invalid credentialss");
					
					logger.trace("Responded json: {}", userJson.toString());


					httpStatus.put("Code", "401");
					httpStatus.put("status", "Bad request");
					httpStatus.put("response", userJson);
					
					return badRequest(userJson);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ObjectNode userJson = Json.newObject();

				userJson.put("status", "Invalid credentialss");
				
				logger.error("Responded json: {}", userJson.toString());


				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", userJson);
				
				return badRequest(httpStatus);
			}
			
			
		}
		

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createUserJson() {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			logger.trace("Responded json: {}", asJson.toString());

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();
			
			Form<UserAccount> contactForm = Form.form(UserAccount.class).bind(asJson);
			
			/*if(contactForm.hasErrors()){

				logger.trace("Received json: {}", contactForm.errorsAsJson().toString());

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			}else{*/
				
				UserAccount userAccount = new UserAccount();
				userAccount.email = asJson.findPath("email").textValue();
				userAccount.firstName = asJson.findPath("firstName").textValue();
				userAccount.lastName = asJson.findPath("lastName").textValue();
				userAccount.password = asJson.findPath("password").textValue();
				userAccount.userProfile = UserProfile.find.byId(asJson.findPath("userProfile").longValue());
				userAccount.status = asJson.findPath("status").textValue();
				userAccount.phoneNumber = asJson.findPath("phoneNumber").textValue();
				userAccount.instId= Institution.find.byId(asJson.findPath("instId").longValue());
				userAccount.createdById = asJson.findPath("createdById").longValue();
				userAccount.regDate = HelperManager.getDateToday();

				// save the user account
				try {
					userAccount.save();
					userJson.put("status", "saved");
					logger.trace("Responded json: {}", asJson.toString());
					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", userJson);
					
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					userJson.put("status", e.getMessage());
					logger.trace("Received json: {}", contactForm.errorsAsJson().toString());

					httpStatus.put("Code", "401");
					httpStatus.put("status", "Bad request");
					httpStatus.put("response", contactForm.errorsAsJson());
					
					return badRequest(httpStatus);
				}

				
				
			

		}

	}
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateUserJson() {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("Received json: {}", asJson.toString());
		
		/*if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {*/
			ObjectNode userJson = Json.newObject();

			Form<UserAccount> contactForm = Form.form(UserAccount.class).bind(asJson);
			if(contactForm.hasErrors()){

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			}else{
			UserAccount userAccount = UserAccount.find.byId(asJson.findPath("userId").longValue());

			userAccount.email = asJson.findPath("email").textValue();
			userAccount.firstName = asJson.findPath("firstName").textValue();
			userAccount.lastName = asJson.findPath("lastName").textValue();
			userAccount.password = asJson.findPath("password").textValue();
			userAccount.userProfile = UserProfile.find.byId(asJson.findPath("userProfile").longValue());
			userAccount.status = asJson.findPath("status").textValue();
			userAccount.phoneNumber = asJson.findPath("phoneNumber").textValue();
			userAccount.instId= Institution.find.byId(asJson.findPath("instId").longValue());
			userAccount.modById = asJson.findPath("modById").longValue();

			// save the user account
			try {
				userAccount.update();
				userJson.put("status", "updated");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				userJson.put("status", e.getMessage());
			}

			
			logger.trace("Responded json: {}", userJson.toString());
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);
			
			return ok(httpStatus);
			
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteUserJson() {
		ObjectNode httpStatus = Json.newObject();
		MDC.put("method", "all");
		JsonNode asJson = request().body().asJson();
		logger.trace("received json: {}", asJson.toString());
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			logger.trace("Responded json: {}", userJson.toString());

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			UserAccount userAccount = UserAccount.find.byId(asJson.findPath("userId").longValue());
			// save the user account
			UserAccountArchive archiveUser = new UserAccountArchive();

			// save the archive
			archiveUser.userEmail = userAccount.email;
			archiveUser.userId = userAccount.id;
			archiveUser.userFirstName = userAccount.firstName;
			archiveUser.userLastName = userAccount.lastName;
			archiveUser.userPass = userAccount.password;
			archiveUser.userPrivilege = userAccount.userProfile + "";
			archiveUser.accountStatus = userAccount.status;
			archiveUser.phoneNumber = userAccount.phoneNumber;
			archiveUser.instId = userAccount.instId.id;
			archiveUser.modById = userAccount.modById;
			archiveUser.modDate = userAccount.modDate;
			archiveUser.createdById = userAccount.createdById;
			archiveUser.regDate = userAccount.regDate;

			// save user in archive;
			archiveUser.save();

			// delete the user
			try {
				userAccount.delete();
				userJson.put("status", "deleted");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				userJson.put("status", e.getMessage());
			}

			logger.trace("Responded json: {}", userJson.toString());
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);
			
			return ok(httpStatus);
		}

	}

}
