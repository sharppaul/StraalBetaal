package nl.hr.project3_4.straalbetaal.language;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Language {
	private JSONObject langJson;
	public static final String EN = "http://student.hr.nl/0907412/languages/EN.json";
	
	public static final String GER = "http://student.hr.nl/0907412/languages/GER.json";
	
	public static final String NL = "http://student.hr.nl/0907412/languages/NL.json";
	

	public static void main(String[] args) {
		System.out.println(new Language(Language.EN).getString("language"));
	}

	public Language(String file) {
		try {
			langJson = new JSONObject(getFile(file));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public String getString(String string) {
		try {
			if (langJson != null) {
				return langJson.getString(string);
			} else {
				return "ERROR_LANGUAGE_NULL";
			}
		} catch (JSONException e) {
			System.err.println("String does not exist in json file...");
			return "ERROR_FETCHING_STRING";
		}
	}

	private String getFile(String fileName) throws IOException, NullPointerException {
		URL url = new URL(fileName);
		Scanner s = new Scanner(url.openStream());
		String str = "";
		while (s.hasNext()) {
			str += s.nextLine() + "\n";
		}
		s.close();
		return str;
	}

	public JSONObject getJson() {
		return this.langJson;
	}
}
