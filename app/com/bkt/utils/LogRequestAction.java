/**
 * 
 */
package com.bkt.utils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

/**
 * @author pc
 *
 */
public class LogRequestAction extends Action<LogRequest> {

	@Override
	public Promise<SimpleResult> call(Context ctx) throws Throwable {
		Logger.info("The request was called:"+ctx);
		return delegate.call(ctx);
	}

}
