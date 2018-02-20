package com.bkt.controllers;

import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {

	public static Result index() {
		return ok(com.bkt.views.html.index.render("Your new application is ready."));
	}

	public static Result preflight(String all) {
		
		response().setHeader("Access-Control-Allow-Origin", "*");
		response().setHeader("Allow", "*");
		response().setHeader("Access-Control-Allow-Methods", "POST, HEAD, GET, PUT, DELETE, OPTIONS");
		response().setHeader("Access-Control-Allow-Headers","Access-Control-Allow-Headers, Origin,Accept,Authorization, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

		return ok();
		
	}

}
