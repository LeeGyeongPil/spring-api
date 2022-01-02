package com.errormeta.idus.middleware;

import java.util.Map;

public class ApiMiddleware {
	public ApiMiddleware()
	{
	}

	public static boolean apiTokenValidation(Map<String, String> headers)
	{
		if (headers.getOrDefault("authorization", "").equals("Bearer YmFja3BhY2tlcjo6aWR1cw==")) {
			return false;
		} else {
			return true;
		}
	}
}
