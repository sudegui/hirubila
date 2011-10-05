package com.m4f.utils;


public class StackTraceUtil {
	

	public static String getStackTrace(Throwable aThrowable) {
		//add the class name and any message passed to constructor
	    final StringBuilder result = new StringBuilder( "BOO-BOO: " );
	    result.append(aThrowable.toString());
	    final String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }

}