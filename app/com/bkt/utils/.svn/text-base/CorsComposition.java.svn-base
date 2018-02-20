package com.bkt.utils;


import java.lang.annotation.*;
import play.libs.F.Promise;
import play.mvc.*;
import play.mvc.Http.Context;
import play.mvc.Http.*;


public class CorsComposition {

private static final String RES_HEADER_CORS_ALLOW_ORIGIN_KEY = "Access-Control-Allow-Origin";


private static final String RES_HEADER_CORS_ALLOW_ORIGIN_VALUE = "*";


private static final String PF_RES_HEADER_CORS_ALLOW_METHODS_KEY = "Access-Control-Allow-Methods";


private static final String PF_RES_HEADER_CORS_ALLOW_METHODS_VALUE = "POST, HEAD,GET, PUT, DELETE, OPTIONS";


private static final String PF_RES_HEADER_CORS_ALLOW_HEADERS_KEY = "Access-Control-Allow-Headers";


private static final String PF_RES_HEADER_CORS_ALLOW_HEADERS_VALUE = "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers";


private static final String RES_HEADER_CORS_EXPOSE_HEADERS_KEY = "Access-Control-Expose-Headers";


private static final String RES_HEADER_CORS_EXPOSE_HEADERS_VALUE = "Content-Length";


private static final String RES_HEADER_CONTENT_TYPE_VALUE = "application/json,text/plain";


	private static final String PRE_FLIGHT_METHOD = "OPTIONS";

 


@With(CorsAction.class)


@Target({ ElementType.TYPE, ElementType.METHOD })


@Retention(RetentionPolicy.RUNTIME)


	public @interface Cors {

    String value() default RES_HEADER_CORS_ALLOW_ORIGIN_VALUE;


}




public static class CorsAction extends Action<Cors> {



    @Override
    public Promise<SimpleResult> call(Context context) throws Throwable{



        Response response = context.response();


        response.setHeader(RES_HEADER_CORS_ALLOW_ORIGIN_KEY, configuration.value());


        if (context.request().method().equals(PRE_FLIGHT_METHOD)) {


            response.setHeader(PF_RES_HEADER_CORS_ALLOW_METHODS_KEY, PF_RES_HEADER_CORS_ALLOW_METHODS_VALUE);



            response.setHeader(PF_RES_HEADER_CORS_ALLOW_HEADERS_KEY, PF_RES_HEADER_CORS_ALLOW_HEADERS_VALUE);



            return delegate.call(context);

        }


        response.setHeader(RES_HEADER_CORS_EXPOSE_HEADERS_KEY, RES_HEADER_CORS_EXPOSE_HEADERS_VALUE);



        response.setContentType(RES_HEADER_CONTENT_TYPE_VALUE);


        return delegate.call(context);



  }
}


}
