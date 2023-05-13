package wcs.ccp.api.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class CCPToken {

	public String getToken() throws Exception {

		// HTTP/HTTPS Proxy
		System.setProperty("http.proxyHost", "proxy.cmltd.net.au");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("https.proxyHost", "proxy.cmltd.net.au");
		System.setProperty("https.proxyPort", "8080");

		String tenant_id = "82551a12-bbc8-4fed-8b7f-2b758284b5ea";
		String client_id = "90e33e9a-ec0c-41ac-a5c6-baed9c8fb72f";
		String client_secret = "9-3wqydFAiBVdp9z-6.-69buS-xzIcvQfw";
		String resource = "782623be-8783-483e-bced-e1273661b69e";
		HttpURLConnection conn = null;
		InputStream input = null;
		String access_token = "";

		try {
			// Java standard HTTP request processing
			URL url = new URL("https://login.microsoftonline.com/" + tenant_id + "/oauth2/token");
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);// Time to connect millisecond
			conn.setReadTimeout(5000);// Time to read data milliseconds
			conn.setRequestMethod("POST");// HTTP method
			conn.setUseCaches(false);// Use cache
			conn.setDoOutput(true);// Allow sending of request body(False for GET,Set to true for POST)
			conn.setDoInput(true);// Allow receiving body of response
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			String data = // Data to post
					"client_id=" + client_id + "&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default" // java.net.May be escaped with URLEncoder
							+ "&client_secret=" + client_secret + "&grant_type=client_credentials" + "&resource=" + resource;
			conn.getOutputStream().write(data.getBytes("utf-8"));
			conn.getOutputStream().close(); // send

			// Receive the result and convert it to a Json object
			// If you don't use the JSON library, just parse the returned text on your own
			// I'm getting something similar to the string I was getting with Curl
			int code = conn.getResponseCode();
			input = (code == 200 ? conn.getInputStream() : conn.getErrorStream());
			JsonReader jsonReader = Json.createReader(new BufferedReader(new InputStreamReader(input, "utf-8")));
			JsonObject json = jsonReader.readObject();
			jsonReader.close();
			conn.disconnect();

			//access token!
			access_token = json.getString("access_token");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return access_token;

	}

}
