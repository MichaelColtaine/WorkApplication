package auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class Authentication {

	private static Authentication INSTANCE;

	public static final String IS_BLOCKED = "isblocked";
	public static final String VERSION_NUMBER = "versionnumber";
	public static final String SOURCE_URL = "http://skladovypomocnik.cz/authinfo.json";

	private Preferences prefs;

	public Authentication() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}

	public static Authentication getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Authentication();
		}
		return INSTANCE;
	}

	public int getLatestVersion() {
		return prefs.getInt(VERSION_NUMBER, 0);
	}

	public boolean isBlocked() {
		return prefs.getBoolean(IS_BLOCKED, false);
	}

	public void check() {

		try {
			getInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getInfo() throws IOException, JSONException {
		URL url = new URL(SOURCE_URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setUseCaches(false);
		JSONObject responseJson = createJsonObject(readResponse(con));
		con.disconnect();
		prefs.putInt(VERSION_NUMBER, responseJson.getInt("spVersion"));
		prefs.putBoolean(IS_BLOCKED, responseJson.getBoolean("spBlocked"));
	}

	private String readResponse(HttpURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	private JSONObject createJsonObject(String content) throws JSONException {
		return new JSONObject(content);
	}

}
