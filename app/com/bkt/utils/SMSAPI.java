package com.bkt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;

public class SMSAPI {
	private static final Logger LOG = LoggerFactory.getLogger(SMSAPI.class);

	public static Promise<Integer> sendSMS(ObjectNode userRequest) {

		String feedUrl = "http://10.10.76.10:8084/nkunganirecore/thirdParty/sendSMS";

		Promise<Integer> resultPromise = null;
		try {
			resultPromise = WS.url(feedUrl).post(userRequest).map(new Function<WS.Response, Integer>() {
				public Integer apply(WS.Response response) {
					LOG.info("UNIVERSITY SMS SENDING url.."+feedUrl+"...body.."+userRequest+"..response:"+response.getBody());
					if (response.getStatus() == 200 || response.getStatus() == 201) {
						return 200;
					} else {

						return 400;
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

		}
		return resultPromise;
	}
	public static Promise<String> sendSMS(String smsText,String recepients,String senderName) {

    	Promise<String> resultPromise = null;
		try {
			String feedUrl="http://sms.fdibiz.com:8866/cgi-bin/sendsms";
			LOG.info("SMS to sent...."+feedUrl);
			resultPromise = WS.url(feedUrl)
					.setQueryParameter("username", "bkrw")
					.setQueryParameter("password", "Bk4wx1")
					.setQueryParameter("from", senderName)
					.setQueryParameter("to", recepients)
					.setQueryParameter("text",smsText)
					.get().map(
			new Function<WS.Response, String>() {
			    public String apply(WS.Response response) {
			    	String s = new String(response.asByteArray());
			        return s;
			    }
			}
           );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return resultPromise;

    }

}
