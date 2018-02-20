package com.bkt.controllers.auth;

import com.bkt.controllers.auth.BasicAuthHelper.BasicAuthUser;

import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;

public class AgendaAuthenticator extends Authenticator {
    
    @Override
    public String getUsername(Context context) {
        BasicAuthUser basicAuthUser = BasicAuthHelper.auth(context);
        if (basicAuthUser == null) {
            return null;
        }
       // String userName=basicAuthUser.name;
        //String password=basicAuthUser.password;
        //UserAccount myUser;
		try {
			//myUser = UserAccount.find.where().eq("email", userName).eq("password", password).findUnique();
			//if (myUser.userId>0) {
			if (basicAuthUser.name.equals("super@test.com") && basicAuthUser.password.equals("123")) {
	              return basicAuthUser.name;
	        } else {
	            return null;
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        //if (basicAuthUser.name.equals("mgonto") && basicAuthUser.password.equals("mgonto")) {
        
    }

}
