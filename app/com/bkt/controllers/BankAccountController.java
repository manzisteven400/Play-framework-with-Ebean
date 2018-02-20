/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.Bank;
import com.bkt.models.BankAccount;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
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
public class BankAccountController extends Controller {
	
	public static Result listJson() {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<BankAccount> all = BankAccount.find.all();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectNode accPurpose;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (BankAccount user : all) {
				
				userJson = Json.newObject();

				ArrayNode accPurposeArray = mapper.createArrayNode();
				userJson.put("accountId", user.id);
				userJson.put("accountNumber", user.accountNumber);
				userJson.put("accountStatus", user.accountStatus);
				for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", user.id).findList()){
					accPurpose = Json.newObject();
					accPurpose.put("purposeId", myPurpose.id);
					accPurpose.put("purpose", myPurpose.purpose);
					accPurposeArray.add(accPurpose);
					}
				userJson.put("bankId", user.bankId.name);
				userJson.put("instId", user.instId.name);
				userJson.put("accPurpose", accPurposeArray);
				

				array.add(userJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}

	public static Result listByInstIdJson(Long instId) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		List<BankAccount> all = BankAccount.find.where().eq("institution_id", instId).findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectNode accPurpose;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (BankAccount user : all) {
				userJson = Json.newObject();

				ArrayNode accPurposeArray = mapper.createArrayNode();
				userJson.put("accountId", user.id);
				userJson.put("accountNumber", user.accountNumber);
				userJson.put("accountStatus", user.accountStatus);
				for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", user.id).findList()){
					accPurpose = Json.newObject();
					accPurpose.put("purposeId", myPurpose.id);
					accPurpose.put("purpose", myPurpose.purpose);
					accPurposeArray.add(accPurpose);
					}
				userJson.put("bankId", user.bankId.name);
				userJson.put("instId", user.instId.name);
				userJson.put("accPurpose", accPurposeArray);
				

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
		Page<BankAccount> pagedList = BankAccount.find.findPagingList(pageMax.intValue()).setFetchAhead(false)
				.getPage(pageNum.intValue());

		// fetch and return the list
		List<BankAccount> allAccounts = pagedList.getList();

		// get the total row count (from the future)
		//int totalRowCount = pagedList.getTotalRowCount();

		if (allAccounts.size() < 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");
			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectNode accPurpose;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (BankAccount user : allAccounts) {
				userJson = Json.newObject();

				ArrayNode accPurposeArray = mapper.createArrayNode();
				userJson.put("accountId", user.id);
				userJson.put("accountNumber", user.accountNumber);
				userJson.put("accountStatus", user.accountStatus);
				for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", user.id).findList()){
					accPurpose = Json.newObject();
					accPurpose.put("purposeId", myPurpose.id);
					accPurpose.put("purpose", myPurpose.purpose);
					accPurposeArray.add(accPurpose);
					}
				userJson.put("bankId", user.bankId.name);
				userJson.put("instId", user.instId.name);
				userJson.put("accPurpose", accPurposeArray);
				
				array.add(userJson);
			}
			httpStatus.put("rowCount", allAccounts.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}

	public static Result allByInstIdPaging(Long instId, Long pageNum, Long pageMax) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		// Paging starts
		Page<BankAccount> pagedList = BankAccount.find.where().eq("institution_id", instId).findPagingList(pageMax.intValue())
				.setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<BankAccount> allAccounts = pagedList.getList();

		// get the total row count (from the future)
		//int totalRowCount = pagedList.getTotalRowCount();

		if (allAccounts.size() < 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("response", userJson);
			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectNode accPurpose;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (BankAccount user : allAccounts) {
				userJson = Json.newObject();

				ArrayNode accPurposeArray = mapper.createArrayNode();
				userJson.put("accountId", user.id);
				userJson.put("accountNumber", user.accountNumber);
				userJson.put("accountStatus", user.accountStatus);
				for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", user.id).findList()){
					accPurpose = Json.newObject();
					accPurpose.put("purposeId", myPurpose.id);
					accPurpose.put("purpose", myPurpose.purpose);
					accPurposeArray.add(accPurpose);
					}
				userJson.put("bankId", user.bankId.name);
				userJson.put("instId", user.instId.name);
				userJson.put("accPurpose", accPurposeArray);
				

				array.add(userJson);
			}
			httpStatus.put("rowCount", allAccounts.size());
			httpStatus.put("response", array);
			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		ObjectNode httpStatus = Json.newObject();
		httpStatus.put("status", "success");
		httpStatus.put("Code", "200");

		BankAccount user = BankAccount.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("accountId", user.id);
			userJson.put("accountNumber", user.accountNumber);
			userJson.put("accountStatus", user.accountStatus);
			ObjectNode accPurpose;
			
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode accPurposeArray = mapper.createArrayNode();
			for(PaymentPurpose myPurpose:PaymentPurpose.find.where().eq("bank_account_id", user.id).findList()){
				accPurpose = Json.newObject();
				accPurpose.put("purposeId", myPurpose.id);
				accPurpose.put("purpose", myPurpose.purpose);
				accPurposeArray.add(accPurpose);
				
			}
			userJson.put("bankId", user.bankId.name);
			userJson.put("instId", user.instId.id);
			userJson.put("accPurpose", accPurposeArray);

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
	public static Result createBankAccountJson() {
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
				BankAccount bank = new BankAccount();

				bank.accountNumber = asJson.findPath("accountNumber").textValue();
				bank.accountStatus = asJson.findPath("accountStatus").textValue();
				try {
					if(asJson.has("bankId")){
						Bank bankbyId = Bank.find.byId(asJson.findPath("bankId").longValue());
						if(bankbyId.id>0){
							bank.bankId = bankbyId;
								
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "bank is not found");
							return badRequest(httpStatus);
						}
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "bank is not found");
						return badRequest(httpStatus);
					
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "bank is not found");
					
					return badRequest(httpStatus);
				}
				try {
					
					if (asJson.has("instId")) {
						Institution instById = Institution.find.byId(asJson.findPath("instId").longValue());
						if(instById.id>0){
							bank.instId = instById;
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
						Faculty facultybyId = Faculty.find.byId(asJson.findPath("facultyId").longValue());
						if(facultybyId.id>0 && facultybyId.instId.id==bank.instId.id){
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
	public static Result updateBankAccountJson() {
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

				BankAccount bank = BankAccount.find.byId(asJson.findPath("accountId").longValue());

				bank.id = asJson.findPath("accountId").longValue();
				bank.accountNumber = asJson.findPath("accountNumber").textValue();
				bank.accountStatus = asJson.findPath("accountStatus").textValue();
				try {
					if(asJson.has("bankId")){
						Bank bankbyId = Bank.find.byId(asJson.findPath("bankId").longValue());
						if(bankbyId.id>0){
							bank.bankId = bankbyId;
								
						}else{
							httpStatus.put("Code", "401");
							httpStatus.put("status", "bank is not found");
							return badRequest(httpStatus);
						}
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "bank is not found");
						return badRequest(httpStatus);
					
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "bank is not found");
					
					return badRequest(httpStatus);
				}
				try {
					bank.instId = Institution.find.byId(asJson.findPath("instId").longValue());
					if (asJson.has("instId")) {
						bank.instId = Institution.find.byId(asJson.findPath("instId").longValue());
						
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
						bank.facultyId = Faculty.find.byId(asJson.findPath("facultyId").longValue());
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "facultyId is not found");
					return badRequest(httpStatus);
				}
				// save the user account
				try {
					bank.update();

					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
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
	public static Result deleteBankAccountJson() {
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

			List<PaymentPurpose> myPurpose=PaymentPurpose.find.where().eq("bank_account_id", asJson.findPath("accountId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
				BankAccount bank = BankAccount.find.byId(asJson.findPath("accountId").longValue());
				// save the user account
				try {
					bank.delete();

					userJson.put("status", "updated");

					httpStatus.put("Code", "200");
					httpStatus.put("status", "success");
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

	}

}
