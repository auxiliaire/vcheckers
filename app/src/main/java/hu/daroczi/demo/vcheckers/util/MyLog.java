package hu.daroczi.demo.vcheckers.util;

import android.util.Log;

public class MyLog {
	private final static String LOG_TAG = "VCHECKER";

	private final static boolean LOG_ENABLED = true;

	public static void logMessage(String msg) {
		if (LOG_ENABLED) {
			Log.d(LOG_TAG, msg);
		}
	}
}
