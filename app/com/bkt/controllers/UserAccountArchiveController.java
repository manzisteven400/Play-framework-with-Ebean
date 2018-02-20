/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.UserAccountArchive;
import com.bkt.utils.HelperManager;
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
public class UserAccountArchiveController extends Controller {


	public static Result listJson() {
		List<UserAccountArchive> all = UserAccountArchive.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			return badRequest(userJson);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (UserAccountArchive user : all) {
				userJson = Json.newObject();
				userJson.put("email", user.userEmail);
				userJson.put("firstName", user.userFirstName);
				userJson.put("lastName", user.userLastName);
				userJson.put("phoneNumber", user.phoneNumber);
				userJson.put("userId", user.userId);
				userJson.put("instId", user.instId);
				userJson.put("accountStatus", user.accountStatus);
				userJson.put("userPrivilege", user.userPrivilege);
				userJson.put("userStatus", user.userStatus);
				userJson.put("regDate", user.regDate.toString());
				array.add(userJson);
			}
			return ok(array);
		}

	}

	public static Result showJson(Long id) {
		UserAccountArchive user = UserAccountArchive.find.byId(id);
		if (user.Id > 0) {
			ObjectNode userJson = Json.newObject();
			userJson.put("email", user.userEmail);
			userJson.put("firstName", user.userFirstName);
			userJson.put("lastName", user.userLastName);
			userJson.put("phoneNumber", user.phoneNumber);
			userJson.put("userId", user.userId);
			userJson.put("instId", user.instId);
			userJson.put("accountStatus", user.accountStatus);
			userJson.put("userPrivilege", user.userPrivilege);
			userJson.put("userStatus", user.userStatus);
			userJson.put("regDate", user.regDate.toString());

			return ok(userJson);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			return badRequest(userJson);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createUserJson() {
		JsonNode asJson = request().body().asJson();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			return badRequest(userJson);
		} else {
			ObjectNode userJson = Json.newObject();
			UserAccountArchive userAccount=new UserAccountArchive();
			userAccount.userEmail = asJson.findPath("userEmail").textValue();
			userAccount.userFirstName = asJson.findPath("userFirstName").textValue();
			userAccount.userLastName = asJson.findPath("userLastName").textValue();
			userAccount.userPass = asJson.findPath("userPass").textValue();
			userAccount.userPrivilege = asJson.findPath("userPrivilege").textValue();
			userAccount.accountStatus = asJson.findPath("accountStatus").textValue();
			userAccount.phoneNumber = asJson.findPath("phoneNumber").textValue();
			userAccount.instId = asJson.findPath("instId").longValue();
			userAccount.createdById = asJson.findPath("createdById").longValue();
			userAccount.userId = asJson.findPath("userId").longValue();
			userAccount.regDate = HelperManager.getDateToday();
			
			//save the user account
			userAccount.save();
			
			userJson.put("status", "saved");
			return ok(userJson);
		}

	}
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateUserJson() {
		JsonNode asJson = request().body().asJson();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			return badRequest(userJson);
		} else {
			ObjectNode userJson = Json.newObject();
			
			UserAccountArchive userAccount = UserAccountArchive.find.byId(asJson.findPath("userId").longValue());
			
			userAccount.userEmail = asJson.findPath("userEmail").textValue();
			userAccount.userFirstName = asJson.findPath("userFirstName").textValue();
			userAccount.userLastName = asJson.findPath("userLastName").textValue();
			userAccount.userPass = asJson.findPath("userPass").textValue();
			userAccount.userPrivilege = asJson.findPath("userPrivilege").textValue();
			userAccount.accountStatus = asJson.findPath("accountStatus").textValue();
			userAccount.phoneNumber = asJson.findPath("phoneNumber").textValue();
			userAccount.instId = asJson.findPath("instId").longValue();
			userAccount.userId = asJson.findPath("userId").longValue();
			userAccount.modById = asJson.findPath("modById").longValue();
			userAccount.modDate = HelperManager.getDateToday();
			
			//save the user account
			userAccount.update();
			
			userJson.put("status", "updated");
			return ok(userJson);
		}

	}
	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteUserJson() {
		JsonNode asJson = request().body().asJson();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			return badRequest(userJson);
		} else {
			ObjectNode userJson = Json.newObject();
			
			UserAccountArchive userAccount = UserAccountArchive.find.byId(asJson.findPath("id").longValue());
			//save the user account
			userAccount.delete();
			
			userJson.put("status", "updated");
			return ok(userJson);
		}

	}



}
