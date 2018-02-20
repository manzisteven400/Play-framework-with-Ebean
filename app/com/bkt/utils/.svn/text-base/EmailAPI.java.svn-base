/**
 * 
 */
package com.bkt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pc
 *
 */
public class EmailAPI {/*

	private static final Logger LOG = LoggerFactory.getLogger(EmailAPI.class);
	public static final String SENDER_EMAIL = "bktnoreply@bk.rw";
	
		
	*//**
	 * sendEmail Method: used to send email to a user
	 *  
	 * @param receiver
	 * @param subject
	 * @param msgBody
	 *//*
	
	public static void sendEmail(String receiver,String subject, String msgBody) {
		try {
			LOG.debug("Start sending Email ...");
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
			mail.setCharset("UTF-8");
			mail.setRecipient(receiver);
			mail.setFrom("BK TECHOUSE - Tenant MIS <"+SENDER_EMAIL+">");
			mail.setSubject(subject);
			mail.sendHtml(msgBody);
			
			LOG.debug("Email sent to "+receiver);
		} catch (Exception e) {
			LOG.debug(e.toString());
		}
	}
	
	
	*//**
	 * sendEmail Method: used to send email to a user and CC to others
	 * @param receiver
	 * @param subject
	 * @param msgBody
	 *//*
	public static void sendEmail(String receiver, String ccReceiver, String subject, String msgBody) {
		try {
			LOG.debug("Start sending Email ...");
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
			mail.setCharset("UTF-8");
			mail.setRecipient(receiver, ccReceiver);
			mail.setFrom("BK TECHOUSE - Tenant MIS <"+SENDER_EMAIL+">");
			mail.setSubject(subject);
			mail.sendHtml(msgBody);
			
			LOG.debug("Email sent to "+receiver);
			LOG.debug("Email cc to "+ccReceiver);
		} catch (Exception e) {
			LOG.debug(e.toString());
		}
	}



*/}
