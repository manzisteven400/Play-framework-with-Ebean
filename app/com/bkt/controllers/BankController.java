/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Bank;
import com.bkt.models.BankAccount;
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
public class BankController extends Controller {

	public static Result listJson() {
		List<Bank> all = Bank.find.all();
		ObjectNode httpStatus = Json.newObject();
		
		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "bad request");
			httpStatus.put("response", userJson);
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Bank user : all) {
				userJson = Json.newObject();
				userJson.put("accronym", user.accronym);
				userJson.put("bankId", user.id);
				userJson.put("bankStatus", user.bankStatus);
				userJson.put("bnrCode", user.bnrCode);
				userJson.put("name", user.name);

				array.add(userJson);
			}
			httpStatus.put("Code", "200");
			httpStatus.put("status", "success");
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		Bank user = Bank.find.byId(id);
		ObjectNode httpStatus = Json.newObject();
		
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();
			userJson.put("accronym", user.accronym);
			userJson.put("bankId", user.id);
			userJson.put("bankStatus", user.bankStatus);
			userJson.put("bnrCode", user.bnrCode);
			userJson.put("name", user.name);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "success");
			httpStatus.put("response", userJson);
			
			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			return badRequest(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createBankJson() {
		JsonNode asJson = request().body().asJson();
		ObjectNode httpStatus = Json.newObject();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad Request");
			httpStatus.put("response", userJson);
			return badRequest(httpStatus);
		} else {
			Form<Bank> contactForm = Form.form(Bank.class).bind(asJson);
			if (contactForm.hasErrors()) {
				
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Failed");
				httpStatus.put("response", contactForm.errorsAsJson());
				return badRequest(httpStatus);
			} else {
				Bank bank = new Bank();
				bank.accronym = asJson.findPath("accronym").textValue();
				bank.bankStatus = asJson.findPath("bankStatus").textValue();
				bank.bnrCode = asJson.findPath("bnrCode").textValue();
				bank.name = asJson.findPath("name").textValue();

				// save the user account
				try {
					bank.save();
					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					httpStatus.put("status", "failed");
					httpStatus.put("Code", "401");
					httpStatus.put("error", e.getMessage());
					
					return badRequest(httpStatus);
				}

				
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateBankJson() {
		JsonNode asJson = request().body().asJson();
		ObjectNode httpStatus = Json.newObject();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "bad request");
			httpStatus.put("response", userJson);
			return badRequest(httpStatus);
		} else {
			Form<Bank> contactForm = Form.form(Bank.class).bind(asJson);
			if (contactForm.hasErrors()) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "bad request");
				httpStatus.put("response", contactForm.errorsAsJson());
				
				return badRequest(httpStatus);
			} else {
				
				Bank bank = Bank.find.byId(asJson.findPath("bankId").longValue());

				bank.accronym = asJson.findPath("accronym").textValue();
				bank.bankStatus = asJson.findPath("bankStatus").textValue();
				bank.bnrCode = asJson.findPath("bnrCode").textValue();
				bank.name = asJson.findPath("name").textValue();

				// save the user account
				try {
					bank.update();
					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					httpStatus.put("status", "failed");
					httpStatus.put("Code", "401");
					httpStatus.put("error", e.getMessage());
					
					return badRequest(httpStatus);
				}

				
			}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteBankJson() {
		JsonNode asJson = request().body().asJson();
		ObjectNode httpStatus = Json.newObject();
		
		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");
			
			httpStatus.put("Code", "401");
			httpStatus.put("status", "bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			List<BankAccount> myPurpose=BankAccount.find.where().eq("bank_id", asJson.findPath("bankId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				Bank bank = Bank.find.byId(asJson.findPath("bankId").longValue());
				// save the user account
				try {
					bank.delete();
					
					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					httpStatus.put("status", "failed");
					httpStatus.put("error", e.getMessage());
					return badRequest(httpStatus);
				}
			}
			
			
			
			
		}

	}

}
