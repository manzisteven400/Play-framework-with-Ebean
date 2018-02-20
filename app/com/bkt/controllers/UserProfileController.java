/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.bkt.controllers.auth.AgendaAuthenticator;
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
public class UserProfileController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		List<UserProfile> all = UserProfile.find.all();

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
			for (UserProfile user : all) {
				userProfileJson = Json.newObject();

				userProfileJson.put("profileId", user.id);
				userProfileJson.put("name", user.name);
				userProfileJson.put("description", user.description);

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
		UserProfile user = UserProfile.find.byId(id);
		if (user.id > 0) {
			ObjectNode userProfileJson = Json.newObject();

			userProfileJson.put("profileId", user.id);
			userProfileJson.put("name", user.name);
			userProfileJson.put("description", user.description);

			
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
	public static Result createProfileJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userProfileJson = Json.newObject();
			userProfileJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response",userProfileJson);
			
			return badRequest(httpStatus);
		} else {
			Form<UserProfile> contactForm = Form.form(UserProfile.class).bind(asJson);
			if (contactForm.hasErrors()) {

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode userProfileJson = Json.newObject();
				UserProfile userProfile = new UserProfile();

				userProfile.name = asJson.findPath("name").textValue();
				userProfile.description = asJson.findPath("description").textValue();

				// save the user account
				try {
					userProfile.save();

					userProfileJson.put("status", "saved");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					userProfileJson.put("status", e.getMessage());
				}
				
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", userProfileJson);
				
				return ok(httpStatus);
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateProfileJson() {
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
			Form<UserProfile> contactForm = Form.form(UserProfile.class).bind(asJson);
			if (contactForm.hasErrors()) {

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				ObjectNode userProfileJson = Json.newObject();

				UserProfile userProfile = UserProfile.find.byId(asJson.findPath("profileId").longValue());

				userProfile.name = asJson.findPath("name").textValue();
				userProfile.description = asJson.findPath("description").textValue();

				// save the user account
				try {
					userProfile.update();

					userProfileJson.put("status", "updated");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					userProfileJson.put("status", e.getMessage());
				}
				
				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", userProfileJson);
				
				return ok(httpStatus);
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteProfileJson() {
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

			UserProfile userProfile = UserProfile.find.byId(asJson.findPath("profileId").longValue());
			// delete the user account
			try {
				userProfile.delete();

				userProfileJson.put("status", "deleted");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				userProfileJson.put("status", e.getMessage());
			}
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userProfileJson);
			
			return ok(httpStatus);
		}

	}
}
