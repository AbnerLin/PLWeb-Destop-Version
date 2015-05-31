package Starter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONParserClass {

	private JSONParser parser;
	
	public JSONParserClass() {
		parser = new JSONParser();
	}
	
	public JSONObject parseObj(String str) throws ParseException {
		return (JSONObject) parser.parse(str);
	}
	
	public JSONArray parseArr(String str) throws ParseException {
		return (JSONArray) parser.parse(str);
	}
	
	public Object getDataFromArr(String str, int index) throws ParseException {
		JSONArray _return = (JSONArray) parser.parse(str);
		return _return.get(index);
	}
	
	public Object getDataFromObj(String str, String key) throws ParseException {
		JSONObject _return = (JSONObject) parser.parse(str);
		return _return.get(key);
	}
	
	public int getArrlengh(JSONArray arr) {
		return arr.size();
	}
	
}
