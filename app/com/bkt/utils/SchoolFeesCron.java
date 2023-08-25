package com.bkt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkt.models.PaymentLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class SchoolFeesCron {

	//send posted transactions to BK
	private static final Logger LOG = LoggerFactory.getLogger(SchoolFeesCron.class);

	public static void updatePurchaseTransaction(){
		//get all transactions pending
		LOG.info("Univerisity...Cron checking .............................momo ....updatePurchaseTransaction");
		
		for(PaymentLog myFeesLog:PaymentLog.find.where().eq("ussd_status", "1000").eq("status_desc", "PENDING").findList()){
			try {
					if(myFeesLog.id>0){
						
						if (myFeesLog.processingNumber.length() > 0) {
							JsonNode trxStatusJson = handleMomoTransactionStatus(myFeesLog.processingNumber);
							LOG.info("JSON response for checking trx status..." + trxStatusJson);
							if (trxStatusJson.findPath("httpStatus").intValue() == 200) {
								if (trxStatusJson.findPath("status").textValue().equalsIgnoreCase("SUCCESSFUL")) {
									myFeesLog.extTrxId = trxStatusJson.findPath("MOMTransactionID").textValue();
									myFeesLog.ussdStatus="success";
									myFeesLog.statusDesc="Success at MTN and Pending at Bank";
								} else if (trxStatusJson.findPath("status").textValue().equalsIgnoreCase("FAILED")) {
									myFeesLog.extTrxId = trxStatusJson.findPath("MOMTransactionID").textValue();
									myFeesLog.ussdStatus="FAILED";
									myFeesLog.statusDesc="FAILED AT MOMO";
									/*myFeesLog.ussdStatus="success";
									myFeesLog.statusDesc="Success at MTN and Pending at Bank";*/
								}
							} else {

								myFeesLog.extTrxId = trxStatusJson.findPath("MOMTransactionID").textValue();
								myFeesLog.ussdStatus="FAILED";
								myFeesLog.statusDesc="DECLINED AT MOMO";

							}
							
						}
						myFeesLog.update();
						
						
							LOG.info("Successfully updated>>>>>>>Rent M2U2B>>"
									+ myFeesLog.statusDesc
									+ ":::Institution name== "
									+ myFeesLog.instId.name
									+ ":::processingNumber=="
									+ myFeesLog.processingNumber
									+ ":::Amount=="
									+ myFeesLog.amountPaid
									+ ":::MSISDN==" + myFeesLog.msisdn);
						
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//LOG.info("SERIOUS ERROR:>>>>>>>READING APPROVED TRX FROM MTN>>"+ myFeesLog.msisdn+" with reference number"+processNumber);
					e.printStackTrace();
				}
			
			
		
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static void postSchoolFeesTransactions(){
		//get all transactions pending
		LOG.info("Univerisity...Cron checking .............................bank ....postSchoolFeesTransactions");
		
		for(PaymentLog myFeesLog:PaymentLog.find.where().eq("ussd_status", "success").eq("status_desc", "Success at MTN and Pending at Bank").findList()){
			String logBankAcc="";
			String stdentRef="";
				try {
					if(myFeesLog.id>0){
						logBankAcc=myFeesLog.bankAcc.accountNumber;
						stdentRef=myFeesLog.studentId.regNumber;
						ObjectNode userJson = Json.newObject();
						if(myFeesLog.isRegistered.equalsIgnoreCase("no")){
							userJson.put("studentNid", myFeesLog.nida);
							userJson.put("studentnames", myFeesLog.studentNames);
							userJson.put("isRegistered", "no");
							userJson.put("schoolid", myFeesLog.instId.instCode);
							userJson.put("schoolname", myFeesLog.instId.name);
							userJson.put("pendingamount", 0);
							userJson.put("facultyName", "0");
							userJson.put("facultyId", "0");
							userJson.put("paymentPurpose", myFeesLog.paymentPurpose.description);	
							userJson.put("bankcode",myFeesLog.bank.bnrCode);	
							userJson.put("bankname", myFeesLog.bank.name);	
							userJson.put("bankaccount", myFeesLog.bankAcc.accountNumber);
						}else{

							userJson.put("isRegistered", "yes");
							userJson.put("studentid", myFeesLog.studentId.regNumber);
							userJson.put("studentnames", myFeesLog.studentId.firstName+" "+myFeesLog.studentId.lastName);
							userJson.put("schoolid", myFeesLog.instId.id);
							userJson.put("schoolname", myFeesLog.instId.name);
							userJson.put("facultyName", "0");
							userJson.put("facultyId", "0");
							userJson.put("bankcode",myFeesLog.bank.bnrCode);	
							userJson.put("studentclass", "1");			
							userJson.put("bankname", myFeesLog.bank.name);	
							userJson.put("paymentPurpose", myFeesLog.paymentPurpose.description);	
							userJson.put("pendingamount",0);
							userJson.put("bankaccount", myFeesLog.bankAcc.accountNumber);
						
						}
						
						userJson.put("code", "040");
						userJson.put("username", "BK");
						userJson.put("transactionid", myFeesLog.extTrxId);
						userJson.put("academicyear", "2018");
						userJson.put("semister", "1");	
						userJson.put("payername", myFeesLog.payerName);	
						
						userJson.put("amountpaid", myFeesLog.amountPaid);		
						userJson.put("datetime", USSDHelperUtils.getDateNow());
						userJson.put("operator", myFeesLog.operator);
						userJson.put("observation", "Momo payment via msisdn:"+myFeesLog.msisdn);
						
						LOG.info("::::::::::::::::::::::::resquest to BK Bank:::::"+userJson);
						
						JsonNode postBank = Json.newObject();
						try {
							ObjectMapper mapper = new ObjectMapper();
							String resu = "";
							try {
								resu = USSDHelperUtils.postPayment(userJson.toString());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							LOG.info("::::::::::::::::::::::::response from BK Bank:::::"+resu);
							
							try {
								if(resu.length()>0){
									postBank = mapper.readTree(resu);
								}
								
							}catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							String bankSlip = "";
							if(postBank.has("status")){
								if(postBank.findPath("status").textValue().equalsIgnoreCase("success")){
									if (postBank.findPath("bankSlip").textValue()!=null) {
										bankSlip = postBank.findPath("bankSlip").textValue();
										
										myFeesLog.bankSlip=bankSlip;
										myFeesLog.statusDesc="posted";
										myFeesLog.ussdStatus="success";
										
										String checkSum=USSDHelperUtils.getSequenceNo();
										if(checkSum.equals("none")){
											
											myFeesLog.batchCheckSum="none";
											myFeesLog.logged=5;
											 
										}else{
											myFeesLog.batchCheckSum=checkSum;
											myFeesLog.logged=0;
										}
										
										myFeesLog.logBankAccount=logBankAcc;
										myFeesLog.studentRef=stdentRef;
										
										String amount = null;
										String names=myFeesLog.studentId.firstName+" "+myFeesLog.studentId.lastName;
										try {
											amount = Double.toString(myFeesLog.amountPaid);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									
										String smsStr="School fees Amount payment:"+amount+"RWF for student:"+names+" with ID:"+myFeesLog.studentId.regNumber+" is successfully deposited at "+myFeesLog.instId.name+"'s account in:"+myFeesLog.bank.name+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										
										ObjectNode SMSJson = Json.newObject();
										
										SMSJson.put("clientId", "TMS");
										SMSJson.put("userName", "tmsuser");
										SMSJson.put("password", "tw3n3er");
										SMSJson.put("recipientNumber", myFeesLog.msisdn);
										SMSJson.put("textContent", smsStr);
										SMSJson.put("senderName", "URUBUTO");
										
										//String smsStr="School fees Amount payment:"+amount+"RWF is successfully deposited at "+instName+"'s account in:"+bankName+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										int SMSsent = SMSAPI.sendSMS(SMSJson).get();
											LOG.info("SMS is sent response code...."+SMSsent);
										//LOG.info("SMS is sent...."+SMSAPI.sendSMS(myFeesLog.msisdn, "URUBUTO", smsStr));
										//LOG.info("SMS SENT...TO..."+myFeesLog.msisdn+"...Text..."+smsStr);
										
									}/*else if(postBank.findPath("status").textValue().equalsIgnoreCase("failed")){
										myFeesLog.bankSlip=myFeesLog.extTrxId;
										myFeesLog.statusDesc="failed at Bank";
										myFeesLog.ussdStatus="failed";
										
										//String smsStr="School fees Amount payment:"+myFeesLog.amountPaid+"RWF is successfully deposited at "+myFeesLog.instId.name+"'s account in:"+myFeesLog.bank.name+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										String smsStr="School fees Amount payment:"+myFeesLog.amountPaid+"RWF is successfully deposited at institution's account with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										ObjectNode SMSJson = Json.newObject();
										
										SMSJson.put("clientId", "TMS");
										SMSJson.put("userName", "tmsuser");
										SMSJson.put("password", "tw3n3er");
										SMSJson.put("recipientNumber", myFeesLog.msisdn);
										SMSJson.put("textContent", smsStr);
										SMSJson.put("senderName", "URUBUTO");
										
										//String smsStr="School fees Amount payment:"+amount+"RWF is successfully deposited at "+instName+"'s account in:"+bankName+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
										// int SMSsent = SMSAPI.sendSMS(SMSJson).get();
										//	LOG.info("SMS is sent response code...."+SMSsent);
										//LOG.info("SMS is sent...."+SMSAPI.sendSMS(myFeesLog.msisdn, "URUBUTO", smsStr));
										//LOG.info("SMS SENT...TO..."+myFeesLog.msisdn+"...Text..."+smsStr);
									}*/
								}/*else{
									myFeesLog.bankSlip=myFeesLog.extTrxId;
									myFeesLog.statusDesc="failed at Bank";
									myFeesLog.ussdStatus="failed";
									
									//String smsStr="School fees Amount payment:"+myFeesLog.amountPaid+"RWF is successfully deposited at "+myFeesLog.instId.name+"'s account in:"+myFeesLog.bank.name+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
									String smsStr="School fees Amount payment:"+myFeesLog.amountPaid+"RWF is successfully deposited at institution's account with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
									ObjectNode SMSJson = Json.newObject();
									
									SMSJson.put("clientId", "TMS");
									SMSJson.put("userName", "tmsuser");
									SMSJson.put("password", "tw3n3er");
									SMSJson.put("recipientNumber", myFeesLog.msisdn);
									SMSJson.put("textContent", smsStr);
									SMSJson.put("senderName", "URUBUTO");
									
									//String smsStr="School fees Amount payment:"+amount+"RWF is successfully deposited at "+instName+"'s account in:"+bankName+" with bankslip:"+myFeesLog.bankSlip+". RWF.Powered by BK TecHouse.";
									//int SMSsent = SMSAPI.sendSMS(SMSJson).get();
									//	LOG.info("SMS is sent response code...."+SMSsent);
									//LOG.info("SMS is sent...."+SMSAPI.sendSMS(myFeesLog.msisdn, "URUBUTO", smsStr));
									//LOG.info("SMS SENT...TO..."+myFeesLog.msisdn+"...Text..."+smsStr);
									
								}*/	
							}
							
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						myFeesLog.update();
						
						
							LOG.info("Successfully updated>>>>>>>Rent M2U2B>>"
									+ myFeesLog.statusDesc
									+ ":::Institution name== "
									+ myFeesLog.instId.name
									+ ":::processingNumber=="
									+ myFeesLog.processingNumber
									+ ":::Amount=="
									+ myFeesLog.amountPaid
									+ ":::MSISDN==" + myFeesLog.msisdn);
						
						
					
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//LOG.info("SERIOUS ERROR:>>>>>>>READING APPROVED TRX FROM MTN>>"+ myFeesLog.msisdn+" with reference number"+processNumber);
					e.printStackTrace();
				}
			
			
		
		}
		
	}
	
	
	public static JsonNode handleMomoTransactionStatus(String momoTrx) {

        ObjectNode memberDetails = Json.newObject();
        
		String myresponse="";
		try {
			myresponse = getMomoTransactionStatus(momoTrx);
			System.out.println("Response from momo agrigator>>>>>>>>>>>>" + myresponse);
			String MOMTransactionID = momoTrx;
			String status = "";
			
			if (myresponse.contains("<transactionid>")) {
				MOMTransactionID = myresponse.split("<transactionid>")[1].split("</transactionid>")[0];
			}
			if (myresponse.contains("<status>")) {
				status = myresponse.split("<status>")[1].split("</status>")[0];
			}

			if (MOMTransactionID.length()>1 && status.length()>0) {

				memberDetails.put("httpStatus", 200);
				memberDetails.put("status", status);
				memberDetails.put("MOMTransactionID", MOMTransactionID);
				
			} else {

				LOG.info("Contribution is not saved");
				memberDetails.put("httpStatus", 400);
				memberDetails.put("status", "failed");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch
			// block
			e.printStackTrace();
			memberDetails.put("httpStatus", 400);
			memberDetails.put("status", "failed");
		}
	
		return memberDetails;
	}public static String getMomoTransactionStatus(String trxId) throws IOException {
		
		String beginPoint = "http://localhost:8084/urubuto/momo.status?trxId=" + trxId;
		URL url = new URL(beginPoint);
		// String credentials = "imescrow:Abc12345";

		String response = "";

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setUseCaches(false);
		conn.setDoInput(true);

		String line = "";
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		InputStream is;
		if (httpConn.getResponseCode() >= 400) {
			is = httpConn.getErrorStream();
		} else {
			is = httpConn.getInputStream();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while ((line = reader.readLine()) != null) {
			response += line;
		}
		reader.close();

		return response;
	}
	

}
