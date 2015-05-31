package Starter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

public class HttpRequester {
	
	private String response;
	private URLConnection conn;
	
	public String sendPost(String url, Properties props) throws IOException {
		URL _url = new URL(url);
		conn = _url.openConnection();
		
		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		
		
		writer.write(setPostData(props));
		writer.flush();
		writer.close();
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    while ((line = reader.readLine()) != null) {
	    	pw.println(line);
	    }
	    
	    reader.close();
	    pw.close();
		sw.close();
		
		response = sw.getBuffer().toString();
		
		return response;
	}
	
	private String setPostData(Properties props) throws UnsupportedEncodingException {
		StringBuffer data = new StringBuffer();
		data.append("?");
		for (Object key : props.keySet()) {
			data.append("&".concat(URLEncoder.encode(key.toString(), "UTF-8")).concat("=").concat(URLEncoder.encode(props.get(key).toString(),"UTF-8")));
		}
		return data.toString();
	}
	
}
