package com.gama.quatenation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;

public class NetworkUtils {

	
	static HttpURLConnection createConnection(Context context,
			String address, byte[] bytes)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.addRequestProperty("Cache-Control", "no-cache");

		if (bytes != null) {
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setFixedLengthStreamingMode(bytes.length);
			connection.setRequestProperty("Content-Type", "application/json");
		} else {
			connection.setRequestMethod("GET");
		}

		connection.setRequestProperty("Accept",
				"application/json;text/html;text/plain");

		// addHeaders(connection, requestHeaders);

		connection.setReadTimeout(Constants.SOCKET_TIMEOUT);
		connection.setConnectTimeout(Constants.SOCKET_TIMEOUT);
		
		return connection;
	}
	
	
	public static boolean sendGet(Context context, String fullURL, StringBuilder response)
			throws Exception {
		HttpURLConnection connection = null;
		InputStream is = null;

		try {
			connection = createConnection(context, fullURL, null);
			
			int statusCode = connection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				sb.append("Error sendGetWithResponse code = [")
						.append(statusCode).append(']');

				is = connection.getErrorStream();
				if (is != null) {
					Writer writer = new StringWriter();
					char[] buffer = new char[1024];

					Reader reader = new BufferedReader(new InputStreamReader(
							is, "UTF-8"));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
					sb.append(writer.toString());
				}

				boolean retry = true; // Should try again on custom exception
				throw new Exception(sb.toString());
			}
			// Connection 200 ok
			is = connection.getInputStream();

			if (response != null) {
				if (is != null) {
					Writer writer = new StringWriter();
					char[] buffer = new char[1024];

					Reader reader = new BufferedReader(new InputStreamReader(
							is, "UTF-8"));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
					response.setLength(0);
					response.append(writer.toString());
				}
			}

			return true;
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
