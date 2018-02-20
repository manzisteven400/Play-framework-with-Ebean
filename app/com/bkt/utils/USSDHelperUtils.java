package com.bkt.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkt.models.Institution;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Topic;
import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;

public class USSDHelperUtils {
	private static final Logger LOG = LoggerFactory.getLogger(USSDHelperUtils.class);

	public static String getUSSDResponse(String action, int level, String responseTxt, String msisdn) {
		StringBuffer responseBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		responseBuffer.append("<appbody><action>" + action + "</action><msisdn>" + msisdn + "</msisdn><level>" + level + "</level>");
		responseBuffer.append("<response>" + responseTxt + "</response><application>100</application>");
		responseBuffer.append("<datetime>" + getDateNow() + "</datetime>");
		responseBuffer.append("</appbody>");

		return responseBuffer.toString();
	}

	public static boolean isPhoneValid(String pin) {

		boolean validPIN = false;

		System.out.println("Phone................................" + pin);
		if (pin.matches("[0-9]{12}")) {
			validPIN = true;
		}

		return validPIN;
	}

	public static String getDateNow() {
		String today = null;
		Date d = new Date();

		SimpleDateFormat dataBaseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		today = dataBaseTime.format(d);
		System.out.println("date today is::" + today);
		return today;
	}

	public static Date getDateFromString() {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date today = format.parse(getDateNow());
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Date today = new Date();

			return today;
		}

	}

	public static Date getDateFromStringPost() {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date today = format.parse(getDateNow());
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Date today = new Date();

			return today;
		}

	}

	public static String getDateNowString() {
		String today = null;
		Date d = new Date();

		SimpleDateFormat dataBaseTime = new SimpleDateFormat("yyyyMMddHHmmss");

		today = dataBaseTime.format(d);
		System.out.println("date today is::" + today);
		return today;
	}

	public static Map<Integer, String> getInstitutionsSorted() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		int count = 0;
		for (Institution inst : Institution.find.all()) {
			count++;
			map.put(count, inst.name);
		}
		return map;
	}

	public static Institution getInsitutionSelected(int selectedId) {

		Institution institution = new Institution();
		int count = 0;
		for (Institution inst : Institution.find.all()) {

			count++;
			if (selectedId == count) {
				institution = inst;
			}

		}
		return institution;
	}

	public static Map<Integer, String> getInstitutionsPaymentPurposesSorted(Long instId) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		int count = 0;
		for (PaymentPurpose inst : PaymentPurpose.find.where().eq("institution_id", instId).findList()) {
			count++;
			map.put(count, inst.purpose);
		}
		return map;
	}

	public static PaymentPurpose getPaymentPurposeSelected(Long instId, int selectedId) {

		PaymentPurpose institution = new PaymentPurpose();
		int count = 0;
		for (PaymentPurpose inst : PaymentPurpose.find.where().eq("institution_id", instId).findList()) {

			count++;
			if (selectedId == count) {
				institution = inst;
			}

		}
		return institution;
	}

	public static String postPayment(String params) {

		String line = "";

		String beginPoint = "https://10.102.148.201:8443/schoolfees/bk/techouse/rest/payment/university/from/momo-rwanda";

		try {
			// URL url = new URL("https://hostname/index.html");
			URL url = new URL(beginPoint);

			String urlParameters = params;
			MyTrustManager.disableSSL();
			// HttpURLConnection connection =
			// (HttpURLConnection)url.openConnection();
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");

			connection.setDoOutput(true);

			connection.connect();

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(urlParameters);
			writer.flush();
			int statusCode = connection.getResponseCode();
			String decodedString;
			if (statusCode == 200) {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((decodedString = in.readLine()) != null) {
					line=decodedString;
					LOG.info("Success Response from BK:"+decodedString);
				}
				
			} else {
				/* error from server */
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((decodedString = in.readLine()) != null) {
					LOG.info("Error Response from BK:"+decodedString);
					line=decodedString;
				}
			}

		} catch (Exception e) {
			//

			e.printStackTrace();
			
		}
		return line;

	}

	public static class MyTrustManager implements X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
			// TODO Auto-generated method stub

		}

		public static void disableSSL() {
			try {
				TrustManager[] trustAllCerts = new TrustManager[] { new MyTrustManager() };

				// Install the all-trusting trust manager
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static Promise<String> coonectToMoMoUrubuto(String msisdn, String amount, String trxId) {

		String feedUrl = "http://localhost:8084/urubuto/ussd";
		Promise<String> resultPromise = null;
		try {
			resultPromise = WS.url(feedUrl).setQueryParameter("initiateMoMo", "next").setQueryParameter("msisdn", msisdn).setQueryParameter("trxId", trxId).setQueryParameter("amount", amount).get().map(new Function<WS.Response, String>() {
				public String apply(WS.Response response) {
					return response.getBody();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			LOG.info("Error occured at registration:" + e.getMessage());
		}
		return resultPromise;

	}

	public static Promise<JsonNode> postPaymentToBK(JsonNode docId) {

		String feedUrl = "https://172.16.20.45:8443/schoolfees/bk/techouse/rest/payment/schoolfees/from/momo-rwanda";
		Promise<JsonNode> resultPromise = null;
		try {
			resultPromise = WS.url(feedUrl).post(docId).map(new Function<WS.Response, JsonNode>() {
				public JsonNode apply(WS.Response response) {
					return response.asJson();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			LOG.info("Error occured at registration:" + e.getMessage());
		}
		return resultPromise;

	}

	public static Map<Long, String> getInstitutionsPaymentPurposes(Long instId) {
		Map<Long, String> map = new HashMap<Long, String>();
		for (PaymentPurpose inst : PaymentPurpose.find.where().eq("institution_id", instId).findList()) {

			map.put(inst.id, inst.purpose);
		}
		return map;
	}

	public static Map<Long, String> getEnglishTopicEng() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (Topic acronym : Topic.find.all()) {
			map.put(Long.valueOf(acronym.id), acronym.headingEng);
		}
		return map;
	}

	public static Map<Long, String> getEnglishTopicKiny() {
		Map<Long, String> map = new HashMap<Long, String>();
		for (Topic acronym : Topic.find.all()) {
			map.put(Long.valueOf(acronym.id), acronym.headingKiny);
		}

		return map;
	}

	public static int randomToken() {
		int number = (int) (Math.random() * 10000000) + 1;
		return number;
	}

	public static Date getDateToday() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		return date;

	}

}
