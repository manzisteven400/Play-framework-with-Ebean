package com.bkt.controllers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkt.utils.SchoolFeesCron;
import com.bkt.utils.USSDHelperUtils;

import play.GlobalSettings;
import play.libs.Akka;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
//import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class Global extends GlobalSettings {
	private static final Logger LOG = LoggerFactory.getLogger(Global.class);

	public void onStart(Application app) {
		LOG.debug("BK Techouse - University fees payment - Application has started");

		/*
		 * Cron running every 5 sec Update bkcai and bkcaib
		 */
		FiniteDuration delay = FiniteDuration.create(2, TimeUnit.MINUTES);
		FiniteDuration frequency = FiniteDuration.create(2, TimeUnit.MINUTES);

		Runnable updateBkCaisse = new Runnable() {
			@Override
			public void run() {
				 LOG.debug("Univerisity...AKKA schedule statrts...Time is now: " + new Date());
				 
				try {
					USSDHelperUtils.manageTrxAndCheckSum();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				 try {
						SchoolFeesCron.updatePurchaseTransaction();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
				 try {
					SchoolFeesCron.postSchoolFeesTransactions();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 
				 try {
						USSDHelperUtils.writeURXML();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 LOG.debug("Univerisity...Updating transaction status is started...........");
				 
				}
		};
		Akka.system().scheduler().schedule(delay, frequency, updateBkCaisse, Akka.system().dispatcher());
	}

	public void onStop(Application app) {
		LOG.debug("BK Techouse - University fees payment - Application stoping....");
	}

	private class ActionWrapper extends Action.Simple {
		public ActionWrapper(Action<?> action) {
			this.delegate = action;
		}

		@Override
		public Promise<SimpleResult> call(Http.Context ctx) throws java.lang.Throwable {
			Promise<SimpleResult> result = this.delegate.call(ctx);
			Http.Response response = ctx.response();
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Allow", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, HEAD, GET, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers",
					"Access-Control-Allow-Headers, Origin,Accept,Authorization, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

			return result;
		}
	}

	@Override
	public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
		return new ActionWrapper(super.onRequest(request, actionMethod));
	}

	/*
	 * private class ActionWrapper extends Action.Simple {
	 * 
	 * public ActionWrapper(Action<?> action) { this.delegate = action; }
	 * 
	 * @Override public Promise<SimpleResult> call(Http.Context ctx) throws
	 * java.lang.Throwable { Promise<SimpleResult> result =
	 * this.delegate.call(ctx); Http.Response response = ctx.response();
	 * 
	 * response.setHeader("Access-Control-Allow-Origin", "*");
	 * response.setHeader("Access-Control-Allow-Credentials", "true");
	 * response.setHeader("Allow", "*");
	 * response.setHeader("Access-Control-Allow-Methods",
	 * "POST, HEAD, GET, PUT, DELETE, OPTIONS");
	 * response.setHeader("Access-Control-Allow-Headers",
	 * "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
	 * );
	 * 
	 * return result; }
	 * 
	 * }
	 * 
	 * @Override public Action<?> onRequest(Http.Request request,
	 * java.lang.reflect.Method actionMethod) { return new
	 * ActionWrapper(super.onRequest(request, actionMethod)); }
	 * 
	 * public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
	 * 
	 * ObjectNode result = Json.newObject(); result.put("error",
	 * "Resource not found"); result.put("code", "401");
	 * 
	 * return Promise.<SimpleResult>pure(Results.notFound(result)); }
	 * 
	 * public Promise<SimpleResult> onBadRequest(RequestHeader request, String
	 * error) {
	 * 
	 * ObjectNode result = Json.newObject(); result.put("error", "Bad request."
	 * + error); result.put("code", "404");
	 * 
	 * return Promise.<SimpleResult>pure(Results.badRequest(result));
	 * 
	 * }
	 * 
	 * public Promise<SimpleResult> onError(RequestHeader request, Throwable t)
	 * {
	 * 
	 * if (play.Play.isDev()) { return super.onError(request, t); }
	 * 
	 * ObjectNode result = Json.newObject(); result.put("error",
	 * "Internal server error."); result.put("code", "500");
	 * 
	 * return Promise.<SimpleResult>pure(Results.internalServerError(result));
	 * 
	 * }
	 */

}
