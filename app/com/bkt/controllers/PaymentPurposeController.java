/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.BankAccount;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
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
public class PaymentPurposeController extends Controller {

	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		List<PaymentPurpose> all = PaymentPurpose.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentPurpose user : all) {
				userJson = Json.newObject();

				userJson.put("id", user.id);
				try {
					if(user.accountId.accountNumber!=null){
						userJson.put("accountId", user.accountId.id);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("accPriority", user.accPriority);
				userJson.put("description", user.description);
				userJson.put("purpose", user.purpose);

				array.add(userJson);
			}
			
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result listByInstitutionJson(Long instId) {
		ObjectNode httpStatus = Json.newObject();
		List<PaymentPurpose> all = PaymentPurpose.find.findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(userJson);
		} else {
			
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (PaymentPurpose user : all) {
				userJson = Json.newObject();

				userJson.put("id", user.id);
				try {
					if(user.accountId.accountNumber!=null){
						userJson.put("accountId", user.accountId.id);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userJson.put("accPriority", user.accPriority);
				userJson.put("description", user.description);
				userJson.put("purpose", user.purpose);

				array.add(userJson);
			}
			
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);
			
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		PaymentPurpose user = PaymentPurpose.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("id", user.id);
			try {
				if(user.accountId.accountNumber!=null){
					userJson.put("accountId", user.accountId.id);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userJson.put("accPriority", user.accPriority);
			userJson.put("description", user.description);
			userJson.put("purpose", user.purpose);

			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
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
	public static Result createPaymentPurposeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
				ObjectNode userJson = Json.newObject();
				PaymentPurpose paymentPurpose = new PaymentPurpose();
				if(asJson.has("accountId")){
					
					try {
						BankAccount myAcc = BankAccount.find.byId(asJson.findPath("accountId").longValue());
						if(myAcc.accountNumber!=null){
							paymentPurpose.accountId =myAcc;
							paymentPurpose.instId=myAcc.instId;
						}else{
							httpStatus.put("Code", "405");
							httpStatus.put("error", "accountId does not exist");
							return badRequest(httpStatus);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "405");
						httpStatus.put("error", "accountId does not exist");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "accountId does not exist");
					return badRequest(httpStatus);
				}
				
				paymentPurpose.accPriority = asJson.findPath("accPriority").textValue();
				paymentPurpose.description = asJson.findPath("description").textValue();
				if(asJson.has("purpose")){
					paymentPurpose.purpose = asJson.findPath("purpose").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "purpose is missing");
					return badRequest(httpStatus);
				}
				//check for duplicates at insert
				try {
					PaymentPurpose myPurpose = PaymentPurpose.find.where()
							.eq("purpose", asJson.findPath("purpose").textValue())
							.eq("bank_account_id", asJson.findPath("accountId").longValue())
							.findUnique();
					if(myPurpose.purpose!=null){
						httpStatus.put("Code", "401");
						httpStatus.put("error", "Payment purpose already exists");
						return badRequest(httpStatus);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}
				// save the user account
				try {
					paymentPurpose.save();

					userJson.put("status", "saved");
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
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
	public static Result updatePaymentPurposeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			
				ObjectNode userJson = Json.newObject();

				PaymentPurpose paymentPurpose = PaymentPurpose.find.byId(asJson.findPath("purposeId").longValue());

				if(asJson.has("accountId")){
					try {
						BankAccount myAcc = BankAccount.find.byId(asJson.findPath("accountId").longValue());
						if(myAcc.accountNumber!=null){
							
							paymentPurpose.accountId =myAcc;
							paymentPurpose.instId=myAcc.instId;
							
						}else{
							
							httpStatus.put("Code", "405");
							httpStatus.put("error", "accountId does not exist");
							return badRequest(httpStatus);
							
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						httpStatus.put("Code", "405");
						httpStatus.put("error", "accountId does not exist");
						return badRequest(httpStatus);
					}
					
				}else{
					httpStatus.put("Code", "405");
					httpStatus.put("error", "accountId does not exist");
					return badRequest(httpStatus);
				}
				
				paymentPurpose.accPriority = asJson.findPath("accPriority").textValue();
				paymentPurpose.description = asJson.findPath("description").textValue();
				if(asJson.has("purpose")){
					paymentPurpose.purpose = asJson.findPath("purpose").textValue();
						
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("error", "purpose is missing");
					return badRequest(httpStatus);
				}
				// save the user account
				try {
					paymentPurpose.update();

					userJson.put("status", "updated");
					httpStatus.put("Code", "200");
					httpStatus.put("status", "Success");
					httpStatus.put("response", userJson);
					
					return ok(httpStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					userJson.put("status", e.getMessage());

					httpStatus.put("Code", "401");
					httpStatus.put("status", "failed");
					httpStatus.put("error",e.getMessage());
					
					return badRequest(httpStatus);
				
				}
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deletePaymentPurposeJson() {
		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);
			
			return badRequest(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();
			List<PaymentLog> myPurpose=PaymentLog.find.where().eq("payment_purpose_id", asJson.findPath("purposeId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				
			PaymentPurpose paymentPurpose = PaymentPurpose.find.byId(asJson.findPath("purposeId").longValue());
			// save the user account
			try {
				paymentPurpose.delete();

				userJson.put("status", "updated");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				userJson.put("status", e.getMessage());
			}
			
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);
			
			return ok(httpStatus);
			}
		}

	}

}
