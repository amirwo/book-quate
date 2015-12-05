package com.gama.quatenation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

import com.google.gson.Gson;

import android.content.Context;

public class TransportHttp {

	private static final String TAG = "Transport";
	static private final int MAX_RETRY_COUNT = 3;

	public static <T> T transport(Context context, String fullUrl, String request, Class<T> responseType)
			throws Exception {
		StringBuilder response = new StringBuilder();
		TransportHttp.sendGet(context, fullUrl, request, response, MAX_RETRY_COUNT, 0);
		T object = new Gson().fromJson(response.toString(), responseType);
		return object;
	}

	private static void sendGet(Context context, String fullUrl, String request, StringBuilder sb, int maxRetryCount,
			long retryInterval) throws Exception {

		if (request != null) {
			fullUrl += request;
		}

		boolean success = false;
		int retryCount = 1;
		while (!success) {
			try {
				NetworkUtils.sendGet(context, fullUrl, sb);
				success = true;
			} catch (Exception e) {
				if (retryCount < maxRetryCount) {
					retryCount++;
					if (retryInterval > 0) {
						try {
							Thread.sleep(retryInterval);
						} catch (InterruptedException e1) {
						}
					}
				} else {
					throw e;
				}
			}
		}
	}

	public static String sendPost(Context context, String address, byte[] bytes)
			throws Exception {
		String responseString = null;
		HttpURLConnection connection = null;
		InputStream is = null;

		try {
			connection = NetworkUtils.createConnection(context, address, bytes);

			if (bytes != null) {

				OutputStream output = null;
				try {
					output = connection.getOutputStream();
					output.write(bytes);
				} finally {
					if (output != null) {
						try {
							output.flush();
							output.close();
						} catch (IOException logOrIgnore) {
						}
					}
				}
			}

			int statusCode = connection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				return null;
			}
			is = connection.getInputStream();
			if (is != null) {
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];

				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				responseString = writer.toString();
			}

			return responseString;
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Error execute Exception ").append(e.getMessage());
			throw new Exception(sb.toString());
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
