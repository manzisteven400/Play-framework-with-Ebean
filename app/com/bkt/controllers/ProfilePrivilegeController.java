/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.ProfilePrivilege;
import com.bkt.models.UserProfile;
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
public class ProfilePrivilegeController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		List<ProfilePrivilege> all = ProfilePrivilege.find.all();

		if (all.isEmpty()) {
			ObjectNode userProfileJson = Json.newObject();

			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			
			return badRequest(userProfileJson);
		} else {
			ObjectNode userProfileJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (ProfilePrivilege user : all) {
				userProfileJson = Json.newObject();

				userProfileJson.put("id", user.id);
				userProfileJson.put("privilegeId", user.privilegeId);
				userProfileJson.put("profileId", user.profileId);
				userProfileJson.put("status", user.status);

				array.add(userProfileJson);
			}
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result listUserPrivileges(Long profileId) {
		ObjectNode httpStatus = Json.newObject();
	
		List<ProfilePrivilege> all = ProfilePrivilege.find.where().eq("profileId", profileId).findList();

		if (all.isEmpty()) {
			ObjectNode userProfileJson = Json.newObject();

			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userProfileJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (ProfilePrivilege user : all) {
				userProfileJson = Json.newObject();

				userProfileJson.put("id", user.id);
				userProfileJson.put("privilegeId", user.privilegeId);
				userProfileJson.put("profileId", user.profileId);
				userProfileJson.put("status", user.status);

				array.add(userProfileJson);
			}
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		ProfilePrivilege user = ProfilePrivilege.find.byId(id);
		if (user.privilegeId > 0) {
			ObjectNode userProfileJson = Json.newObject();

			userProfileJson.put("id", user.id);
			userProfileJson.put("privilegeId", user.privilegeId);
			userProfileJson.put("profileId", user.profileId);
			userProfileJson.put("status", user.status);

			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userProfileJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode userProfileJson = Json.newObject();

			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			

			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result assignPrivilegeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userProfileJson = Json.newObject();
			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			
			return badRequest(userProfileJson);
		} else {
			Form<ProfilePrivilege> contactForm = Form.form(ProfilePrivilege.class).bind(asJson);
			if (contactForm.hasErrors()) {

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode userProfileJson = Json.newObject();
				ProfilePrivilege userProfile = new ProfilePrivilege();

				userProfileJson.put("privilegeId", userProfile.privilegeId);
				userProfileJson.put("profileId", userProfile.profileId);
				userProfileJson.put("status", userProfile.status);

				// save the user account
				userProfile.save();

				userProfileJson.put("status", "saved");
				
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", userProfileJson);
				
				return ok(httpStatus);
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateAssignedPrivilegeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userProfileJson = Json.newObject();
			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			
			return badRequest(httpStatus);
		} else {
			Form<ProfilePrivilege> contactForm = Form.form(ProfilePrivilege.class).bind(asJson);
			if (contactForm.hasErrors()) {

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode userProfileJson = Json.newObject();

				ProfilePrivilege userProfile = ProfilePrivilege.find.byId(asJson.findPath("id").longValue());

				userProfileJson.put("privilegeId", userProfile.privilegeId);
				userProfileJson.put("profileId", userProfile.profileId);
				userProfileJson.put("status", userProfile.status);

				// save the user account
				userProfile.update();

				userProfileJson.put("status", "updated");
				
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", userProfileJson);
				
				return ok(httpStatus);
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteAssignPrivilegeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userProfileJson = Json.newObject();
			userProfileJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userProfileJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userProfileJson = Json.newObject();

			UserProfile userProfile = UserProfile.find.byId(asJson.findPath("id").longValue());
			// delete the user account
			userProfile.delete();

			userProfileJson.put("status", "deleted");
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userProfileJson);
			
			return ok(httpStatus);
		}

	}

}
