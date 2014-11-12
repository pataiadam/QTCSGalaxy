package control.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;

public class RestAPI  {

	private String urls;
	private String user;
	private String password;
	private String authStringEnc;


	public RestAPI(String urls, String users, String passw) {
		this.urls = urls;
		this.user = users;
		this.password = passw;
		String authString = user + ":" + password;
		byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
		this.authStringEnc = new String(authEncBytes);
	}

	public String ping() {
		String response = getURLString("/JavaChallenge1/rest/ping");
		System.out.println("PING: "+response);
		return response;
	}

	public String getGalaxy() {
		String response = getURLString("/JavaChallenge1/rest/getGalaxy");
		return response;
	}

	public String go(String planet) {
		String response = getURLString("/JavaChallenge1/rest/go?planetName="+planet);
		return response;
	}

	public String wherels() {
		String response = getURLString("/JavaChallenge1/rest/whereIs");
		return response;
	}

	public String pickPackage(int packageId) {
		String response = getURLString("/JavaChallenge1/rest/pickPackage?packageId="+packageId);
		return response;
	}

	public String dropPackage(int packageId) {
		String response = getURLString("/JavaChallenge1/rest/dropPackage?packageId="+packageId);
		return response;
	}

	private String getURLString(String path) {
		URL url;
		String result="";
		try {
			url = new URL(urls + path);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
